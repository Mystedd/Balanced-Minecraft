package com.balancedmc.loot_tables;

import com.balancedmc.items.ModItems;
import net.fabricmc.fabric.api.loot.v2.LootTableEvents;
import net.minecraft.enchantment.Enchantments;
import net.minecraft.item.Items;
import net.minecraft.loot.LootPool;
import net.minecraft.loot.LootTables;
import net.minecraft.loot.condition.RandomChanceLootCondition;
import net.minecraft.loot.entry.ItemEntry;
import net.minecraft.loot.function.SetEnchantmentsLootFunction;
import net.minecraft.loot.provider.number.ConstantLootNumberProvider;

public class ModLootTables {

    public static void registerLootTables() {
        LootTableEvents.MODIFY.register((resourceManager, lootManager, id, tableBuilder, source) -> {
            if (source.isBuiltin()) {
                LootPool.Builder builder = null;
                // bastion treasure
                if (id.equals(LootTables.BASTION_TREASURE_CHEST)) {
                    builder = LootPool.builder()
                            // Fire protection V book
                            .with(ItemEntry.builder(Items.BOOK)
                                    .apply(new SetEnchantmentsLootFunction.Builder()
                                            .enchantment(Enchantments.FIRE_PROTECTION, ConstantLootNumberProvider.create(5))
                                    ).conditionally(
                                            RandomChanceLootCondition.builder(0.5F)
                                    )
                            );
                }
                // woodland mansion
                else if (id.equals(LootTables.WOODLAND_MANSION_CHEST)) {
                    builder = LootPool.builder()
                            // Blast protection V book
                            .with(ItemEntry.builder(Items.BOOK)
                                    .apply(new SetEnchantmentsLootFunction.Builder()
                                            .enchantment(Enchantments.PROJECTILE_PROTECTION, ConstantLootNumberProvider.create(5))
                                    ).conditionally(
                                            RandomChanceLootCondition.builder(0.3F)
                                    )
                            );
                }
                // ancient city
                else if (id.equals(LootTables.ANCIENT_CITY_CHEST)) {
                    builder = LootPool.builder()
                            // Projectile protection V book
                            .with(ItemEntry.builder(Items.BOOK)
                                    .apply(new SetEnchantmentsLootFunction.Builder()
                                            .enchantment(Enchantments.BLAST_PROTECTION, ConstantLootNumberProvider.create(5))
                                    ).conditionally(
                                            RandomChanceLootCondition.builder(0.5F)
                                    )
                            );
                }
                // toolsmith hero of the village
                else if (id.equals(LootTables.HERO_OF_THE_VILLAGE_TOOLSMITH_GIFT_GAMEPLAY)) {
                    builder = LootPool.builder()
                            // Tool trim smithing template
                            .with(ItemEntry.builder(ModItems.TOOL_TRIM_SMITHING_TEMPLATE));
                }
                if (builder != null) {
                    tableBuilder.pool(builder);
                }
            }
        });
    }
}
