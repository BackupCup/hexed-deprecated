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

        var damageReduction: Float = 0.125f
    }

    @Comment(
        "Averting Hex settings" + skip +
        "shouldRegister - whether the hex should be obtainable\n" +
        "damageReduction - how much each armor piece with Averting reduces damage taken (Percentage value, 0.125 = 12.5%)"
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

        var minimumDivergence: Double = -5.0
        var maximumDivergence: Double = 5.0
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
    }

    @Comment(
        "Ephemeral Hex settings" + skip +
		"shouldRegister - whether the hex should be obtainable"
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
    }

    @Comment(
        "Flaring Hex settings" + skip +
		"shouldRegister - whether the hex should be obtainable"
    ) var flaringHex: FlaringHexParams = FlaringHexParams()

    //FRANTIC
    class FranticHexParams {
        var shouldRegister: Boolean = true
    }

    @Comment(
        "Frantic Hex settings" + skip +
		"shouldRegister - whether the hex should be obtainable"
    ) var franticHex: FranticHexParams = FranticHexParams()

    //IRONCLAD
    class IroncladHexParams {
        var shouldRegister: Boolean = true
    }

    @Comment(
        "Ironclad Hex settings" + skip +
		"shouldRegister - whether the hex should be obtainable"
    ) var ironcladHex: IroncladHexParams = IroncladHexParams()

    //LINGER
    class LingerHexParams {
        var shouldRegister: Boolean = true
    }

    @Comment(
        "Linger Hex settings" + skip +
		"shouldRegister - whether the hex should be obtainable"
    ) var lingerHex: LingerHexParams = LingerHexParams()

    //METAMORPHOSIS
    class MetamorphosisHexParams {
        var shouldRegister: Boolean = true
    }

    @Comment(
        "Metamorphosis Hex settings" + skip +
		"shouldRegister - whether the hex should be obtainable"
    ) var metamorphosisHex: MetamorphosisHexParams = MetamorphosisHexParams()

    //OVERBURDEN
    class OverburdenHexParams {
        var shouldRegister: Boolean = true
    }

    @Comment(
        "Overburden Hex settings" + skip +
		"shouldRegister - whether the hex should be obtainable"
    ) var overburdenHex: OverburdenHexParams = OverburdenHexParams()

    //PERSECUTED
    class PersecutedHexParams {
        var shouldRegister: Boolean = true
    }

    @Comment(
        "Persecuted Hex settings" + skip +
		"shouldRegister - whether the hex should be obtainable"
    ) var persecutedHex: PersecutedHexParams = PersecutedHexParams()

    //RUINOUS
    class RuinousHexParams {
        var shouldRegister: Boolean = true
    }

    @Comment(
        "Ruinous Hex settings" + skip +
		"shouldRegister - whether the hex should be obtainable"
    ) var ruinousHex: RuinousHexParams = RuinousHexParams()

    //SEIZE
    class SeizeHexParams {
        var shouldRegister: Boolean = true
    }

    @Comment(
        "Seize Hex settings" + skip +
		"shouldRegister - whether the hex should be obtainable"
    ) var seizeHex: SeizeHexParams = SeizeHexParams()

    //SEPULTURE
    class SepultureHexParams {
        var shouldRegister: Boolean = true
    }

    @Comment(
        "Sepulture Hex settings" + skip +
		"shouldRegister - whether the hex should be obtainable"
    ) var sepultureHex: SepultureHexParams = SepultureHexParams()

    //TRAITOROUS
    class TraitorousHexParams {
        var shouldRegister: Boolean = true
    }

    @Comment(
        "Traitorous Hex settings" + skip +
		"shouldRegister - whether the hex should be obtainable"
    ) var traitorousHex: TraitorousHexParams = TraitorousHexParams()

    //VINDICTIVE
    class VindictiveHexParams {
        var shouldRegister: Boolean = true
    }

    @Comment(
        "Vindictive Hex settings" + skip +
	    "shouldRegister - whether the hex should be obtainable"
    ) var vindictiveHex: VindictiveHexParams = VindictiveHexParams()
}