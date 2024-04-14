package net.backupcup.hexed.register

import net.backupcup.hexed.Hexed
import net.fabricmc.fabric.api.item.v1.FabricItemSettings
import net.minecraft.item.Item
import net.minecraft.registry.Registries
import net.minecraft.registry.Registry
import net.minecraft.util.Identifier

object RegisterRunes {
    val AFLAME: Item = Item(FabricItemSettings())
    val AQUATIQUE: Item = Item(FabricItemSettings())
    val AVERTING: Item = Item(FabricItemSettings())
    val BLOODTHIRSTY: Item = Item(FabricItemSettings())
    val DISFIGUREMENT: Item = Item(FabricItemSettings())
    val DISPLACED: Item = Item(FabricItemSettings())
    val DIVINE: Item = Item(FabricItemSettings())
    val DYNAMIQUE: Item = Item(FabricItemSettings())
    val EPHEMERAL: Item = Item(FabricItemSettings())
    val EXUBERANCE: Item = Item(FabricItemSettings())
    val FRANTIC: Item = Item(FabricItemSettings())
    val IRONCLAD: Item = Item(FabricItemSettings())
    val METAMORPHOSIS: Item = Item(FabricItemSettings())
    val PERSECUTED: Item = Item(FabricItemSettings())
    val TRAITOROUS: Item = Item(FabricItemSettings())
    val VINDICTIVE: Item = Item(FabricItemSettings())

    val RUNE_LIST: List<Pair<Item, String>> = listOf(
        AFLAME          to           "aflame",
        AQUATIQUE       to        "aquatique",
        AVERTING        to         "averting",
        BLOODTHIRSTY    to     "bloodthirsty",
        DISFIGUREMENT   to    "disfigurement",
        DISPLACED       to        "displaced",
        DIVINE          to           "divine",
        DYNAMIQUE       to        "dynamique",
        EPHEMERAL       to        "ephemeral",
        EXUBERANCE      to       "exuberance",
        FRANTIC         to          "frantic",
        IRONCLAD        to         "ironclad",
        METAMORPHOSIS   to    "metamorphosis",
        PERSECUTED      to       "persecuted",
        TRAITOROUS      to       "traitorous",
        VINDICTIVE      to       "vindictive"
    )

    fun registerRunes() {
        RUNE_LIST.forEach { (item, id) ->
            Registry.register(Registries.ITEM, Identifier(Hexed.MOD_ID, id), item)
        }
    }
}