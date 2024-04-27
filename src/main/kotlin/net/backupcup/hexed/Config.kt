package net.backupcup.hexed

import blue.endless.jankson.Comment
import blue.endless.jankson.Jankson
import blue.endless.jankson.api.DeserializationException
import blue.endless.jankson.api.SyntaxError
import net.backupcup.hexed.Hexed.MOD_ID
import net.backupcup.hexed.Hexed.LOGGER
import net.fabricmc.loader.api.FabricLoader
import net.minecraft.item.Item
import net.minecraft.network.PacketByteBuf
import net.minecraft.registry.Registries
import net.minecraft.text.Text
import net.minecraft.util.Identifier
import java.io.File
import java.io.FileNotFoundException
import java.io.FileOutputStream
import java.io.IOException
import java.nio.file.Path


class Config {
    companion object {
        var lastError: Text? = null

        private fun getConfigFile(): File? {
            return Path.of(
                FabricLoader.getInstance().configDir.toString(),
                MOD_ID,
                "config.json5"
            ).toFile()
        }

        private val JANKSON: Jankson = Jankson.builder()
            .registerSerializer(Identifier::class.java) { id, marshaller -> marshaller.serialize(id.toString()) }
            .registerDeserializer(String::class.java, Identifier::class.java) { str, _ ->
                Identifier.tryParse(
                    str
                )
            }
            .build()

        fun load(): Config? {
            val defaults = Config()
            try {
                if (getConfigFile()!!.exists()) {
                    lastError = null
                    val json = JANKSON.load(getConfigFile())
                    return JANKSON.fromJsonCarefully(json, Config::class.java)
                }
                defaults.save()
                lastError = null
                return defaults
            } catch (e: SyntaxError) {
                LOGGER?.error("Config syntax error. {}.", e.lineMessage)
                LOGGER?.error(e.message)
                LOGGER?.warn("Using default configuration.")
                lastError = Text.translatable("message.hexed.error.config.general")
            } catch (e: DeserializationException) {
                LOGGER?.error("Hexed config deserialization error.")
                LOGGER?.error("{}", e.message)
                if (e.cause != null) {
                    LOGGER?.error("Cause: {}", e.cause!!.message)
                }
                LOGGER?.warn("Using default configuration.")
                lastError = Text.translatable("message.hexed.error.config.general")
            } catch (e: IOException) {
                LOGGER?.error("IO exception occurred while reading config. Using defaults.")
                LOGGER?.error(e.message)
                LOGGER?.warn("Using default configuration.")
                lastError = Text.translatable("message.hexed.error.config.general")
            }
            return defaults
        }

        fun readFromServer(buf: PacketByteBuf): Config? {
            try {
                return JANKSON.fromJsonCarefully(buf.readString(), Config::class.java)
            } catch (e: SyntaxError) {
                LOGGER?.error("Error while retrieving config from server: {}", e)
            }
            return null
        }

        const val skip: String = "\n\n"
    }

    @Throws(FileNotFoundException::class)
    fun save() {
        getConfigFile()!!.parentFile.mkdirs()
        try {
            FileOutputStream(getConfigFile()).use { outStream ->
                outStream.write(
                    JANKSON.toJson(this).toJson(true, true).toByteArray()
                )
            }
        } catch (e: IOException) {
            LOGGER?.error("IO exception while saving config: {}", e.message)
        }
    }

    fun writeToClient(buf: PacketByteBuf) {
        buf.writeString(JANKSON.toJson(this).toJson())
    }



    enum class ListType {
        ALLOW,
        DENY
    }

    @Comment(
        "List type\n" +
        "ALLOW - whitelist, only the items in this list WILL have the ability to be hexed\n" +
        "DENY - blacklist, all the items in this list WONT have the ability to be hexed"
    )
    private var listType: ListType = ListType.DENY

    @Comment(
        "An item list. What the list does depends on the \"listType\""
    ) private var itemList: Array<String> = arrayOf()
    fun isListed(item: Item): Boolean {
        return if (listType == ListType.DENY)
              itemList.contains(Registries.ITEM.getId(item).toString())
        else !itemList.contains(Registries.ITEM.getId(item).toString())
    }

    @Comment(
        "Whether the \"Calamitous\" Full armor effect should apply."
    ) private var fullArmorEffectApply: Boolean = true
    fun shouldArmorApply(): Boolean { return fullArmorEffectApply }



    //AFLAME
    class AflameHexParams {
        var shouldRegister: Boolean = true

        var AblazeDuration: Int = 80
        var AblazeAmplifier: Int = 1

        var AflameDuration: Int = 40
        var AflameAmplifier: Int = 0
    }
    @Comment(
        "\"Aflame\" Hex settings" + skip +
        "shouldRegister - whether the hex should be obtainable\n" +
        "AblazeDuration/Amplifier - The duration (in ticks) & the amplifier of the Ablaze debuff\n" +
        "AflameDuration/Amplifier - The duration (in ticks) & the amplifier of the Aflame debuff\n"
    ) var aflameHex: AflameHexParams = AflameHexParams()

    //AMPLIFY
    class AmplifyHexParams {
        var shouldRegister: Boolean = true

        var dropChance: Double = 0.5
        var exhaustionAmount: Double = 0.01
        var toolDamage: Int = 1
    }

    @Comment(
        "Amplify Hex settings" + skip +
        "shouldRegister - whether the hex should be obtainable\n" +
        "dropChance - chance for the additional mined blocks to drop\n" +
        "exhaustionAmount - how much exhaustion is applied for each additional mined block\n" +
        "toolDamage - how much durability is lost per additional mined block"
    ) var amplifyHex: AmplifyHexParams = AmplifyHexParams()

    //AQUATIQUE
    class AquatiqueHexParams {
        var shouldRegister: Boolean = true

        var speedMultiplier: Float = 2.0f
    }

    @Comment(
        "Aquatique Hex settings" + skip +
        "shouldRegister - whether the hex should be obtainable\n" +
        "speedMultiplier - by how much the movement speed in water is multiplied (Anything below 1 will actually make the wearer slower)"
    ) var aquatiqueHex: AquatiqueHexParams = AquatiqueHexParams()

    //AVERTING
    class AvertingHexParams {
        var shouldRegister: Boolean = true

        var damageReduction: Double = 0.125
        var avertingCooldown: Int = 50
    }

    @Comment(
        "Averting Hex settings" + skip +
        "shouldRegister - whether the hex should be obtainable\n" +
        "damageReduction - how much each armor piece with Averting adds to the chance of negating damage (Percentage value, 0.125 = 12.5%)\n" +
        "avertingCooldown - for how much time (in ticks) the invinsibility frames will be taken away after dodging damage"
    ) var avertingHex: AvertingHexParams = AvertingHexParams()

    //BLOODTHIRSTY
    class BloodthirstyHexParams {
        var shouldRegister: Boolean = true

        var minimumHealAmount: Float = 3f
        var maximumHealAmount: Float = 10f
        var healModifier: Float = 0.125f
    }

    @Comment(
        "Bloodthirsty Hex settings" + skip +
	    "shouldRegister - whether the hex should be obtainable\n" +
        "minimumHealAmount - minimum amount of Health that will be regenerated\n" +
        "maximumHealAmount - maximum amount of Health that will be regenerated\n" +
        "healModifier - heal scaling amount (Percentage value, 0.125 = 12.5%)"
    ) var bloodthirstyHex: BloodthirstyHexParams = BloodthirstyHexParams()

    //CELEBRATION
    class CelebrationHexParams {
        var shouldRegister: Boolean = true

        var minimumDivergence: Float = -5.0f
        var maximumDivergence: Float = 5.0f
        var faulyChance: Double = 0.333
        var faultySpeed: Float = 0.0625f
    }

    @Comment(
        "Celebration Hex settings" + skip +
		"shouldRegister - whether the hex should be obtainable\n" +
        "minimumDivergence - minimum variation of the firework's direction\n" +
        "maximumDivergence - maximum variation of the firework's direction\n" +
        "faulyChance - the chance that the launched firework will be faulty (Percentage value, 0.333 = 33.3%)\n" +
        "faultySpeed - speed of the faulty firework"
    ) var celebrationHex: CelebrationHexParams = CelebrationHexParams()

    //DISFIGUREMENT
    class DisfigurementHexParams {
        var shouldRegister: Boolean = true

        var boxRadius: Int = 3
        var hungerDuration: Int = 100
        var hungerAmplifier: Int = 0
        var weaknessDuration: Int = 100
        var weaknessAmplifier: Int = 0
    }

    @Comment(
        "Disfigurement Hex settings" + skip +
		"shouldRegister - whether the hex should be obtainable\n" +
        "boxRadius - radius of the effect\n" +
        "hungerDuration/Amplifier - The duration (in ticks) & the amplifier of the Hunger debuff\n" +
        "weaknessDuration/Amplifier - The duration (in ticks) & the amplifier of the Weakness debuff\n"
    ) var disfigurementHex: DisfigurementHexParams = DisfigurementHexParams()

    //DISPLACED
    class DisplacedHexParams {
        var shouldRegister: Boolean = true
    }

    @Comment(
        "Displaced Hex settings" + skip +
		"shouldRegister - whether the hex should be obtainable"
    ) var displacedHex: DisplacedHexParams = DisplacedHexParams()

    //DIVINE
    class DivineHexParams {
        var shouldRegister: Boolean = true

        var defaultDuration: Int = 20
        var defaultAmplifier: Int = 0

        var ironcladDuration: Int = 40
        var ironcladAmplifier: Int = 3

        var exhaustionDuration: Int = 60
        var exhaustionAmplifier: Int = 9

        var etherealDuration: Int = 80
        var etherealAmplifier: Int = 5
    }

    @Comment(
        "Divine Hex settings" + skip +
		"shouldRegister - whether the hex should be obtainable\n" +
        "defaultDuration/Amplifier - The duration (in seconds) & the amplifier of the Ablaze & Traitorous debuffs\n" +
        "ironcladDuration/Amplifier - The duration (in seconds) & the amplifier of the Ironclad debuffs\n" +
        "exhaustionDuration/Amplifier - The duration (in seconds) & the amplifier of the Exhaustion debuffs\n" +
        "etherealDuration/Amplifier - The duration (in seconds) & the amplifier of the Ethereal debuffs\n"
    ) var divineHex: DivineHexParams = DivineHexParams()

    //DYNAMIQUE
    class DynamiqueHexParams {
        var shouldRegister: Boolean = true

        var jumpModifier: Float = 0.15625f
    }

    @Comment(
        "Dynamique Hex settings" + skip +
		"shouldRegister - whether the hex should be obtainable\n" +
        "jumpModifier - the additional amount of velocity the user will receive (0.15625 = 1 block)"
    ) var dynamiqueHex: DynamiqueHexParams = DynamiqueHexParams()

    //EPHEMERAL
    class EphemeralHexParams {
        var shouldRegister: Boolean = true

        var damageAddition: Float = 4f
        var debuffDuration: Int = 150
        var debuffDecayLength: Int = 50
    }

    @Comment(
        "Ephemeral Hex settings" + skip +
		"shouldRegister - whether the hex should be obtainable\n" +
        "damageAddition - how much more damage the weapon does at first\n" +
        "debuffDuration - the initial duration of the debuff (in ticks)\n" +
        "debuffDecayLength - how much each additional level of the debuff lasts\n"
    ) var ephemeralHex: EphemeralHexParams = EphemeralHexParams()

    //FAMISHMENT
    class FamishmentHexParams {
        var shouldRegister: Boolean = true
    }

    @Comment(
        "Famishment Hex settings" + skip +
		"shouldRegister - whether the hex should be obtainable"
    ) var famishmentHex: FamishmentHexParams = FamishmentHexParams()

    //FLARING
    class FlaringHexParams {
        var shouldRegister: Boolean = true

        var isSoulFire: Boolean = false
    }

    @Comment(
        "Flaring Hex settings" + skip +
		"shouldRegister - whether the hex should be obtainable\n" +
        "isSoulFire - whether the fire spawned should be Soul Fire"
    ) var flaringHex: FlaringHexParams = FlaringHexParams()

    //FRANTIC
    class FranticHexParams {
        var shouldRegister: Boolean = true

        var franticDuration: Int = 100
        var franticDecayLength: Int = 50

        var damageModifier: Float = 2f
    }

    @Comment(
        "Frantic Hex settings" + skip +
		"shouldRegister - whether the hex should be obtainable\n" +
        "franticDuration - the initial duration of the debuff (in ticks)\n" +
        "franticDecayLength - how much each additional level of the debuff lasts\n" +
        "damageModifier - the amplifier of the debuff is divided by this to get the actual damage modifier\n" +
        "Formula: damage = damage * (1 + (FranticDebuffLevel / damageModifier))"
    ) var franticHex: FranticHexParams = FranticHexParams()

    //IRONCLAD
    class IroncladHexParams {
        var shouldRegister: Boolean = true

        var debuffDuration: Int = 200
        var debuffDecayLength: Int = 10
        var robesDebuffModifier: Int = 2
        var damageReductionAmount: Float = 0.33f

    }

    @Comment(
        "Ironclad Hex settings" + skip +
		"shouldRegister - whether the hex should be obtainable\n" +
        "debuffDuration - the initial duration of the debuff (in ticks)\n" +
        "debuffDecayLength - how much each additional level of the debuff lasts\n" +
        "robesDebuffModifier - by how much the initial duration of the debuff is divided\n" +
        "damageReductionAmount - the amount of damage reduction this hex provides\n" +
        "Formula: damage = damage * (1 - damageReductionAmount)"
    ) var ironcladHex: IroncladHexParams = IroncladHexParams()

    //LINGER
    class LingerHexParams {
        var shouldRegister: Boolean = true

        var lingerRadius: Int = 3
        var debuffDuration: Int = 25
        var debuffAmplifier: Int = 0
        var tridentDamage: Int = 1
    }

    @Comment(
        "Linger Hex settings" + skip +
		"shouldRegister - whether the hex should be obtainable\n" +
        "lingerRadius - the radius from the trident in which the debuff is applied\n" +
        "debuffDuration/Amplifier - The duration (in seconds) & the amplifier of the Ablaze debuff\n" +
        "tridentDamage - the amount of durability damage the trident receives overtime (This is affected by the amount of creatures in the radius)"
    ) var lingerHex: LingerHexParams = LingerHexParams()

    //METAMORPHOSIS
    class MetamorphosisHexParams {
        var shouldRegister: Boolean = true

        var foodModifier: Float = 1f
        var saturationAmount: Float = 0f
    }

    @Comment(
        "Metamorphosis Hex settings" + skip +
		"shouldRegister - whether the hex should be obtainable\n" +
        "foodModifier - modifies the amount of food recieved per half heart (anything below 1 will nullify the effect of the hex)\n" +
        "saturationAmount - how much saturation is applied"
    ) var metamorphosisHex: MetamorphosisHexParams = MetamorphosisHexParams()

    //OVERBURDEN
    class OverburdenHexParams {
        var shouldRegister: Boolean = true

        var buffDuration: Int = 200
        var movementSpeedModifier: Float = 0.025f
    }

    @Comment(
        "Overburden Hex settings" + skip +
		"shouldRegister - whether the hex should be obtainable\n" +
        "buffDuration - the initial duration of the buff (in ticks)\n" +
        "movementSpeedModifier - how much speed is removed for each level of the buff"
    ) var overburdenHex: OverburdenHexParams = OverburdenHexParams()

    //PERSECUTED
    class PersecutedHexParams {
        var shouldRegister: Boolean = true

        var healthCap: Float = 25f
        var debuffDurationModifier: Int = 10
    }

    @Comment(
        "Persecuted Hex settings" + skip +
		"shouldRegister - whether the hex should be obtainable\n" +
        "healthCap - the maximum amount of damage a weapon with this hex can deal\n" +
        "debuffDurationModifier - a modifier determining how much the debuff lasts for"
    ) var persecutedHex: PersecutedHexParams = PersecutedHexParams()

    //RUINOUS
    class RuinousHexParams {
        var shouldRegister: Boolean = true

        var explosionPower: Float = 1.25f
    }

    @Comment(
        "Ruinous Hex settings" + skip +
		"shouldRegister - whether the hex should be obtainable\n" +
        "explosionPower - the power of the explosion"
    ) var ruinousHex: RuinousHexParams = RuinousHexParams()

    //SEIZE
    class SeizeHexParams {
        var shouldRegister: Boolean = true

        var maxPullableHP: Int = 50
        var minimumPullStrength: Double = 1.0
        var maximumPullStrength: Double = 4.0
    }

    @Comment(
        "Seize Hex settings" + skip +
		"shouldRegister - whether the hex should be obtainable\n" +
        "maxPullableHP - determines whether the creature can be pulled based on it's max Health\n" +
        "minimumPullStrength - the minimum strength of the pull if the trident doesn't have Loyalty\n" +
        "maximumPullStrength - the maximum strength of the pull if the trident doesn't have Loyalty\n"
    ) var seizeHex: SeizeHexParams = SeizeHexParams()

    //SEPULTURE
    class SepultureHexParams {
        var shouldRegister: Boolean = true

        var angerChance: Double = 0.333
        var explosionPower: Float = 1.5f
    }

    @Comment(
        "Sepulture Hex settings" + skip +
		"shouldRegister - whether the hex should be obtainable\n" +
        "angerChance - chance that a skull will target the user instead of the nearest entity\n" +
        "explosionPower - the power of the produced explosion\n"
    ) var sepultureHex: SepultureHexParams = SepultureHexParams()

    //TRAITOROUS
    class TraitorousHexParams {
        var shouldRegister: Boolean = true

        var debuffDuration: Int = 80
        var cooldownDuration: Int = 40
    }

    @Comment(
        "Traitorous Hex settings" + skip +
		"shouldRegister - whether the hex should be obtainable\n" +
        "debuffDuration - The duration (in ticks) of the Traitorous debuff\n" +
        "cooldownDuration - The duration (in ticks) of the weapon cooldown\n"
    ) var traitorousHex: TraitorousHexParams = TraitorousHexParams()

    //VINDICTIVE
    class VindictiveHexParams {
        var shouldRegister: Boolean = true

        var maxAmplifier: Int = 10

        var vindictiveDuration: Int = 60
        var vindictiveDecayLength: Int = 20

        var smoulderingDuration: Int = 60
        var smoulderingDecayLength: Int = 20
    }

    @Comment(
        "Vindictive Hex settings" + skip +
	    "shouldRegister - whether the hex should be obtainable\n" +
        "maxAmplifier - max amount of damage this hex can do when the debuff runs out\n" +
        "vindictiveDuration - the initial duration of the Vindictive debuff (in ticks)\n" +
        "vindictiveDecayLength - how much each additional level of the Vindictive debuff lasts\n" +
        "smoulderingDuration - the initial duration of the Smouldering debuff (in ticks)\n" +
        "smoulderingDecayLength - how much each additional level of the Smouldering debuff lasts"
    ) var vindictiveHex: VindictiveHexParams = VindictiveHexParams()

    //AGGRAVATE
    class AggravateHexParams {
        var shouldRegister: Boolean = true

        var chargeTicks: Int = 10
        var arrowsPerCharge: Int = 1
        var explosionPower: Float = 2f
    }

    @Comment(
        "Aggravate Hex settings" + skip +
                "shouldRegister - whether the hex should be obtainable\n" +
                "chargeTicks - How much ticks does it take to add a single charge\n" +
                "arrowsPerCharge - How much arrows is a single charge equal to\n" +
                "explosionPower - The power of the explosion on an overcharge"
    ) var aggravateHex: AggravateHexParams = AggravateHexParams()

    //VOLATILITY
    class VolatilityHexParams {
        var shouldRegister: Boolean = true

        var explosionPower: Float = 1f
    }

    @Comment(
        "Volatility Hex settings" + skip +
                "shouldRegister - whether the hex should be obtainable\n" +
                "explosionPower - the power of the explosion"
    ) var volatilityHex: VolatilityHexParams = VolatilityHexParams()

    //PHASED
    class PhasedHexParams {
        var shouldRegister: Boolean = true

        var hitscanDistance: Int = 64
        var initialDamage: Float = 5f
        var damageMultiplier: Float = 0.4f
            var powerModifier: Float = 1f
            var flameSeconds: Int = 5
    }

    @Comment(
        "Phased Hex settings" + skip +
                "shouldRegister - whether the hex should be obtainable\n" +
                "initialDamage - the initial damage the hitscan does\n" +
                "damageMultiplier - by how much the initial damage is multiplied for each pierced enemy\n" +
                "powerModifier - how much damage does a level of Power adds to the initial damage (Setting to 0 will make hitscan ignore Power)\n" +
                "flameSeconds - for how much seconds the enemy will be set on fire (Settings to 0 will make hitscan ignore Flame)"
    ) var phasedHex: PhasedHexParams = PhasedHexParams()

    //PROVISION
    class ProvisionHexParams {
        var shouldRegister: Boolean = true

        var maxReloadSpeed: Int = 10
        var itemCooldown: Int = 80
    }

    @Comment(
        "Provision Hex settings" + skip +
                "shouldRegister - whether the hex should be obtainable\n" +
                "maxReloadSpeed - the max speed the reload indicator can have\n" +
                "itemCooldown - the cooldown penalty in ticks\n"
    ) var provisionHex: ProvisionHexParams = ProvisionHexParams()

    //OVERCLOCK
    class OverclockHexParams {
        var shouldRegister: Boolean = true

        var overheatLasting: Int = 200
    }

    @Comment(
        "Overclock Hex settings" + skip +
                "shouldRegister - whether the hex should be obtainable\n" +
                "overheatLasting - how much ticks will the game wait before removing overheat stacks\n"
    ) var overclockHex: OverclockHexParams = OverclockHexParams()

    //RESENTFUL
    class ResentfulHexParams {
        var shouldRegister: Boolean = true

        var increasePoint: Double = 16.0
        var maxIncreaseRange: Double = 80.0
    }

    @Comment(
        "Resentful Hex settings" + skip +
                "shouldRegister - whether the hex should be obtainable\n" +
                "increasePoint - At which distance the damage starts to increase\n" +
                "maxIncreaseRange - At which distance the damage stops increasing\n"
    ) var resentfulHex: ResentfulHexParams = ResentfulHexParams()
}