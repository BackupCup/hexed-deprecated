package net.backupcup.hexed.enchantments.armor

import com.google.common.collect.Multimap
import net.backupcup.hexed.Hexed
import net.backupcup.hexed.enchantments.AbstractHex
import net.backupcup.hexed.packets.HexNetworkingConstants
import net.backupcup.hexed.register.RegisterEnchantments
import net.backupcup.hexed.util.AttributeProviding
import net.backupcup.hexed.util.HexHelper
import net.fabricmc.fabric.api.networking.v1.PacketByteBufs
import net.fabricmc.fabric.api.networking.v1.PlayerLookup
import net.fabricmc.fabric.api.networking.v1.ServerPlayNetworking
import net.minecraft.enchantment.EnchantmentTarget
import net.minecraft.entity.Entity
import net.minecraft.entity.EquipmentSlot
import net.minecraft.entity.LivingEntity
import net.minecraft.entity.attribute.EntityAttribute
import net.minecraft.entity.attribute.EntityAttributeModifier
import net.minecraft.entity.attribute.EntityAttributes
import net.minecraft.entity.player.PlayerEntity
import net.minecraft.network.PacketByteBuf
import net.minecraft.util.Identifier
import java.util.*
import kotlin.math.min

class BloodthirstyHex(
    weight: Rarity?,
    target: EnchantmentTarget?,
    slotTypes: Array<out EquipmentSlot>?,
    texturepath: Identifier
) : AbstractHex(
    weight,
    target,
    slotTypes,
    texturepath
), AttributeProviding {
    override fun modifyAttributeMap(
        map: Multimap<EntityAttribute, EntityAttributeModifier>,
        slot: EquipmentSlot,
        level: Int
    ) {
        map.put(
            EntityAttributes.GENERIC_MAX_HEALTH,
            EntityAttributeModifier(
                UUID.fromString("71d8172a-509f-47f5-89b8-d1541c3c48c2"),
                "Bloodthirsty max health", 8.0, EntityAttributeModifier.Operation.ADDITION))
    }

    override fun onTargetDamaged(user: LivingEntity, target: Entity?, level: Int) {
        super.onTargetDamaged(user, target, level)
        if (target is LivingEntity) {
            if (target.isDead) {

                var additionalHealth = if (target.maxHealth * (Hexed.getConfig()?.bloodthirstyHex?.healModifier ?: 0.125f) >= (Hexed.getConfig()?.bloodthirstyHex?.minimumHealAmount ?: 3f))
                    (Hexed.getConfig()?.bloodthirstyHex?.maximumHealAmount ?: 10f).coerceAtMost(target.maxHealth * (Hexed.getConfig()?.bloodthirstyHex?.healModifier ?: 0.125f))
                    else Hexed.getConfig()?.bloodthirstyHex?.minimumHealAmount ?: 3f

                for (player in PlayerLookup.tracking(target)) {
                    ServerPlayNetworking.send(player, HexNetworkingConstants.BLOODTHIRSTY_PARTICLE_PACKET, sendParticlePacket(target.x, target.y, target.z,
                            (10..if (target.maxHealth > 20) (min(target.maxHealth, 40f)).toInt() else 20).random()))
                }

                if (user.maxHealth < user.health + additionalHealth
                    && HexHelper.stackHasEnchantment(user.getEquippedStack(EquipmentSlot.HEAD), RegisterEnchantments.METAMORPHOSIS_HEX)
                    && user is PlayerEntity) {
                    additionalHealth -= (user.maxHealth - user.health)
                    user.hungerManager.add(
                        (additionalHealth * (Hexed.getConfig()?.metamorphosisHex?.foodModifier ?: 1f)).toInt(),
                        Hexed.getConfig()?.metamorphosisHex?.saturationAmount ?: 0f)
                }

                user.health += additionalHealth
            }
        }
    }

    fun sendParticlePacket(x: Double, y: Double, z: Double, random: Int): PacketByteBuf {
        val buf = PacketByteBufs.create()
        buf.writeInt(random)
        buf.writeDouble(x)
        buf.writeDouble(y)
        buf.writeDouble(z)

        return buf
    }
}