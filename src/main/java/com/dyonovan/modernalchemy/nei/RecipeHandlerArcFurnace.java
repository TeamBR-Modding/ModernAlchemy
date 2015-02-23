package com.dyonovan.modernalchemy.nei;

import codechicken.nei.PositionedStack;
import codechicken.nei.recipe.TemplateRecipeHandler;
import com.dyonovan.modernalchemy.crafting.RecipeArcFurnace;
import com.dyonovan.modernalchemy.gui.buttons.ItemStackButton;
import com.dyonovan.modernalchemy.handlers.BlockHandler;
import net.minecraft.init.Items;
import net.minecraft.item.ItemStack;
import net.minecraftforge.fluids.FluidStack;

import java.awt.*;

public class RecipeHandlerArcFurnace extends RecipeHandlerBase {

    public static final Rectangle TANK = new Rectangle(147, 77, 18, 52);

    public class CachedArcFurnaceRecipe extends CachedBaseRecipe {
        private PositionedStack input;
        private PositionedStack fuel;
        private FluidTankElement output;

        public CachedArcFurnaceRecipe(ItemStack item) {
            this.input = new PositionedStack(input, 71, 9);
            this.fuel = new PositionedStack(new ItemStack(Items.coal), 71, 59);
            this.output = new FluidTankElement(TANK, 1, new FluidStack(BlockHandler.fluidActinium, RecipeArcFurnace.instance.getRecipeOutput(item.getItem())));
            this.output.capacity = this.output.fluid != null ? this.output.fluid.amount : 1000;
        }

        @Override
        public PositionedStack getIngredient () {
            return this.input;
        }


        @Override
        public PositionedStack getResult() {
            return null;
        }
    }
    @Override
    public String getGuiTexture() {
        return null;
    }

    @Override
    public String getRecipeName() {
        return null;
    }

    @Override
    public String getRecipeID() {
        return null;
    }
}
