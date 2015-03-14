package com.dyonovan.modernalchemy.client.nei.machines;

import codechicken.lib.gui.GuiDraw;
import codechicken.nei.NEIServerUtils;
import codechicken.nei.PositionedStack;
import com.dyonovan.modernalchemy.client.nei.RecipeHandlerBase;
import com.dyonovan.modernalchemy.handlers.BlockHandler;
import com.dyonovan.modernalchemy.handlers.ItemHandler;
import com.dyonovan.modernalchemy.lib.Constants;
import net.minecraft.item.ItemStack;
import net.minecraft.util.StatCollector;
import net.minecraftforge.fluids.FluidStack;
import org.lwjgl.opengl.GL11;

import java.awt.*;
import java.util.ArrayList;
import java.util.List;

public class RecipeHandlerSolidifier extends RecipeHandlerBase {
    public static final Rectangle TANK = new Rectangle(50, 23, 16, 52);
    private static final java.util.List<String> nothing = new ArrayList<String>();

    public class CachedSolidifierRecipe extends RecipeHandlerBase.CachedBaseRecipe {
        private FluidTankElement input;
        private PositionedStack output;

        public CachedSolidifierRecipe() {
            this.input = new FluidTankElement(TANK, 1, new FluidStack(BlockHandler.fluidActinium, 1000), nothing);
            output = new PositionedStack(new ItemStack(ItemHandler.itemReplicationMedium, 1), 123, 40);
        }

        @Override
        public List<FluidTankElement> getFluidTanks() {
            List<FluidTankElement> tanks = new ArrayList<FluidTankElement>();
            tanks.add(input);
            return tanks;
        }

        @Override
        public PositionedStack getResult() {
            return output;
        }
    }

    @Override
    public int recipiesPerPage() { return 1; }

    @Override
    public String getRecipeID() {
        return "modernalchemy.solidifier.recipes";
    }

    @Override
    public String getGuiTexture() {
        return Constants.MODID + ":textures/gui/nei/solidifier.png";
    }

    @Override
    public String getRecipeName() {
        return StatCollector.translateToLocal("modernalchemy.nei.solidifier");
    }

    @Override
    public void loadTransferRects ()
    {
        transferRects.add(new RecipeTransferRect(new Rectangle(81, 39, 24, 18), getRecipeID()));
    }

    @Override
    public void drawExtras (int recipe) {
        drawProgressBar(81, 39, 176, 0, 24, 16, 48, 0);
    }

    @Override
    public void drawBackground (int recipe)
    {
        GL11.glColor4f(1.0F, 1.0F, 1.0F, 1.0F);
        GuiDraw.changeTexture(this.getGuiTexture());
        GuiDraw.drawTexturedModalRect(0, 0, 0, 0, 176, 76);
    }

    @Override
    public void loadCraftingRecipes (String outputId, Object... results) {
        if (outputId.equals(getRecipeID())) {
            this.arecipes.add(new CachedSolidifierRecipe());
        }else
        {
            super.loadCraftingRecipes(outputId, results);
        }
    }

    @Override
    public void loadUsageRecipes (FluidStack ingredient)
    {
        if (areFluidsEqual(new FluidStack(BlockHandler.fluidActinium, 1000), ingredient)) {
            this.arecipes.add(new CachedSolidifierRecipe());
        }
    }

    @Override
    public void loadCraftingRecipes (ItemStack result)
    {
        if (NEIServerUtils.areStacksSameTypeCrafting(new ItemStack(ItemHandler.itemReplicationMedium, 1), result)) {
            this.arecipes.add(new CachedSolidifierRecipe());
        }
    }
}
