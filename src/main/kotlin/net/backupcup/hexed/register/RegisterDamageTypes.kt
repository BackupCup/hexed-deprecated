package net.backupcup.hexed.register

import net.backupcup.hexed.Hexed
import net.minecraft.entity.damage.DamageSource
import net.minecraft.entity.damage.DamageType
import net.minecraft.registry.RegistryKey
import net.minecraft.registry.RegistryKeys
import net.minecraft.util.Identifier
import net.minecraft.world.World


object RegisterDamageTypes {
    val ABLAZE_DAMAGE = RegistryKey.of(RegistryKeys.DAMAGE_TYPE, Identifier(Hexed.MOD_ID, "ablazed"))
    val VINDICTIVE_DAMAGE = RegistryKey.of(RegistryKeys.DAMAGE_TYPE, Identifier(Hexed.MOD_ID, "vindictive"))
    val FRANTIC_DAMAGE = RegistryKey.of(RegistryKeys.DAMAGE_TYPE, Identifier(Hexed.MOD_ID, "frantic"))

    fun of(world: World, key: RegistryKey<DamageType>?): DamageSource {
        return DamageSource(world.registryManager.get(RegistryKeys.DAMAGE_TYPE).entryOf(key))
    }
}