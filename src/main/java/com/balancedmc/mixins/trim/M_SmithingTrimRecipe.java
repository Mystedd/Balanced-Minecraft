package com.balancedmc.mixins.trim;

import com.balancedmc.items.ModItems;
import net.minecraft.item.ItemStack;
import net.minecraft.item.Items;
import net.minecraft.item.trim.ArmorTrim;
import net.minecraft.item.trim.ArmorTrimPattern;
import net.minecraft.item.trim.ArmorTrimPatterns;
import net.minecraft.recipe.SmithingTrimRecipe;
import net.minecraft.registry.DynamicRegistryManager;
import net.minecraft.registry.entry.RegistryEntry;
import net.minecraft.registry.tag.ItemTags;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Redirect;

import java.util.Optional;

@Mixin(SmithingTrimRecipe.class)
public abstract class M_SmithingTrimRecipe {

    @Redirect(
            method = "craft(Lnet/minecraft/inventory/Inventory;Lnet/minecraft/registry/DynamicRegistryManager;)Lnet/minecraft/item/ItemStack;",
            at = @At(
                    value = "INVOKE",
                    target = "Lnet/minecraft/item/trim/ArmorTrimPatterns;get(Lnet/minecraft/registry/DynamicRegistryManager;Lnet/minecraft/item/ItemStack;)Ljava/util/Optional;"
            )
    )
    private Optional<RegistryEntry.Reference<ArmorTrimPattern>> redirect(DynamicRegistryManager registryManager, ItemStack stack) {
        if (stack.isOf(ModItems.TOOL_TRIM_SMITHING_TEMPLATE)) {
            stack = new ItemStack(Items.COAST_ARMOR_TRIM_SMITHING_TEMPLATE);
        }
        return ArmorTrimPatterns.get(registryManager, stack);
    }

    @Redirect(
            method = "craft(Lnet/minecraft/inventory/Inventory;Lnet/minecraft/registry/DynamicRegistryManager;)Lnet/minecraft/item/ItemStack;",
            at = @At(
                    value = "INVOKE",
                    target = "Lnet/minecraft/item/trim/ArmorTrim;apply(Lnet/minecraft/registry/DynamicRegistryManager;Lnet/minecraft/item/ItemStack;Lnet/minecraft/item/trim/ArmorTrim;)Z"
            )
    )
    private boolean injected(DynamicRegistryManager registryManager, ItemStack stack, ArmorTrim trim) {
        boolean result = ArmorTrim.apply(registryManager, stack, trim);
        if (result && stack.isIn(ItemTags.TOOLS)) {
            if (stack.getNbt() != null) {
                stack.getNbt().putInt("CustomModelData", (int) (trim.getMaterial().value().itemModelIndex() * 10));
            }
        }
        return result;
    }
}
