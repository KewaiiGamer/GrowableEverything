package hellfirepvp.growableeverything.registry;

import hellfirepvp.growableeverything.item.ItemScientificSeed;
import hellfirepvp.growableeverything.lib.Items;
import net.minecraft.inventory.InventoryCrafting;
import net.minecraft.item.ItemSeeds;
import net.minecraft.item.ItemStack;
import net.minecraft.item.crafting.CraftingManager;
import net.minecraft.item.crafting.IRecipe;
import net.minecraft.util.NonNullList;
import net.minecraft.world.World;
import net.minecraftforge.common.ForgeHooks;
import net.minecraftforge.oredict.RecipeSorter;

/**
 * This class is part of the GrowableEverything Mod
 * GrowableEverything is Open Source and distributed under the
 * GNU LESSER GENERAL PUBLIC LICENSE
 * for more read the LICENSE file
 * <p>
 * Created by HellFirePvP @ 28.12.2016 20:10
 */
public class RegistryRecipes {

    public static void init() {
        RecipeSorter.register("ScientificSeedCrafting", RecipeSeeds.class, RecipeSorter.Category.SHAPED, "after:minecraft:shaped");

        CraftingManager.getInstance().addRecipe(new RecipeSeeds());
    }

    public static class RecipeSeeds implements IRecipe {

        @Override
        public boolean matches(InventoryCrafting inv, World worldIn) {
            return !getSeedType(inv).isEmpty();
        }

        @Override
        public ItemStack getCraftingResult(InventoryCrafting inv) {
            ItemStack type = getSeedType(inv);
            if(type.isEmpty()) return ItemStack.EMPTY;
            ItemStack seed = new ItemStack(Items.itemSeed);
            ItemScientificSeed.setCropItem(seed, type);
            return seed;
        }

        private ItemStack getSeedType(InventoryCrafting inv) {
            if(inv.getWidth() != 3 || inv.getHeight() != 3) return ItemStack.EMPTY;
            ItemStack stack = inv.getStackInRowAndColumn(1, 1);
            if(stack.isEmpty() || !(stack.getItem() instanceof ItemSeeds)) return ItemStack.EMPTY;
            ItemStack toComp = inv.getStackInRowAndColumn(0, 0);
            if(toComp.isEmpty()) return ItemStack.EMPTY;
            toComp.copy().setCount(1);
            for (int x = 0; x < 3; x++) {
                for (int z = 0; z < 3; z++) {
                    if(x == 1 && z == 1) continue;
                    ItemStack at = inv.getStackInRowAndColumn(x, z).copy();
                    at.setCount(1);
                    if(!ItemStack.areItemStacksEqual(toComp, at)) {
                        return ItemStack.EMPTY;
                    }
                }
            }
            return toComp;
        }

        @Override
        public int getRecipeSize() {
            return 9;
        }

        @Override
        public ItemStack getRecipeOutput() {
            return new ItemStack(Items.itemSeed);
        }

        @Override
        public NonNullList<ItemStack> getRemainingItems(InventoryCrafting inv) {
            return ForgeHooks.defaultRecipeGetRemainingItems(inv);
        }
    }

}
