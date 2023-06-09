package com.balancedmc.mixins.potion;

import net.minecraft.item.*;
import net.minecraft.potion.Potion;
import net.minecraft.potion.Potions;
import net.minecraft.recipe.BrewingRecipeRegistry;
import net.minecraft.recipe.Ingredient;
import net.minecraft.registry.Registries;
import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

import java.util.List;

@Mixin(BrewingRecipeRegistry.class)
public abstract class M_BrewingRecipeRegistry {

    @Shadow public static void registerPotionRecipe(Potion input, Item item, Potion output) {}
    @Shadow public static void registerItemRecipe(Item input, Item ingredient, Item output) {}

    @Shadow @Final private static List<BrewingRecipeRegistry.Recipe<Item>> ITEM_RECIPES;

    @Shadow @Final private static List<Ingredient> POTION_TYPES;

    /**
     * Leaping is now brewed with a slimeball
     * Slow falling is now brewed with phantom membrane
     */
    @Inject(
            method = "registerPotionRecipe(Lnet/minecraft/potion/Potion;Lnet/minecraft/item/Item;Lnet/minecraft/potion/Potion;)V",
            at = @At("HEAD"),
            cancellable = true
    )
    private static void injected(Potion input, Item item, Potion output, CallbackInfo ci) {
        if (input == Potions.AWKWARD && item == Items.RABBIT_FOOT && output == Potions.LEAPING) {
            registerPotionRecipe(input, Items.SLIME_BALL, output);
            ci.cancel();
        } else if (input == Potions.AWKWARD && item == Items.PHANTOM_MEMBRANE && output == Potions.SLOW_FALLING) {
            registerPotionRecipe(input, Items.DRAGON_BREATH, output);
            ci.cancel();
        }
    }

    @Inject(
            method = "registerItemRecipe",
            at = @At("HEAD"),
            cancellable = true
    )
    private static void mRegisterItemRecipe(Item input, Item ingredient, Item output, CallbackInfo ci) {
        if (input == Items.SPLASH_POTION && ingredient == Items.DRAGON_BREATH && output == Items.LINGERING_POTION) {
            registerItemRecipe(input, Items.PHANTOM_MEMBRANE, output);
            ci.cancel();
        }

        if (!(input instanceof PotionItem || input == Items.DRAGON_BREATH)) {
            throw new IllegalArgumentException("Expected a potion, got: " + Registries.ITEM.getId(input));
        } else if (!(output instanceof PotionItem || output == Items.DRAGON_BREATH)) {
            throw new IllegalArgumentException("Expected a potion, got: " + Registries.ITEM.getId(output));
        } else {
            ITEM_RECIPES.add(new BrewingRecipeRegistry.Recipe(input, Ingredient.ofItems(new ItemConvertible[]{ingredient}), output));
            ci.cancel();
        }
    }

    @Inject(
            method = "registerPotionType",
            at = @At("HEAD"),
            cancellable = true
    )
    private static void mRegisterPotionType(Item item, CallbackInfo ci) {
        if (!(item instanceof PotionItem || item == Items.DRAGON_BREATH)) {
            throw new IllegalArgumentException("Expected a potion, got: " + Registries.ITEM.getId(item));
        } else {
            POTION_TYPES.add(Ingredient.ofItems(new ItemConvertible[]{item}));
            ci.cancel();
        }
    }

}
