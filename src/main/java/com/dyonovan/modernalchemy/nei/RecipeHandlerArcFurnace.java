package com.dyonovan.modernalchemy.nei;

import codechicken.lib.gui.GuiDraw;
import codechicken.nei.NEIServerUtils;
import codechicken.nei.PositionedStack;
import com.dyonovan.modernalchemy.crafting.ArcFurnaceRecipeRegistry;
import com.dyonovan.modernalchemy.crafting.RecipeArcFurnace;
import com.dyonovan.modernalchemy.energy.TeslaBank;
import com.dyonovan.modernalchemy.handlers.BlockHandler;
import com.dyonovan.modernalchemy.lib.Constants;
import com.dyonovan.teambrcore.helpers.GuiHelper;
import net.minecraft.init.Items;
import net.minecraft.item.ItemStack;
import net.minecraft.util.StatCollector;
import net.minecraftforge.fluids.FluidStack;
import org.lwjgl.opengl.GL11;

import java.awt.*;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class RecipeHandlerArcFurnace extends RecipeHandlerBaseTesla {

    public static final Rectangle TANK = new Rectangle(142, 26, 16, 52);
    public static final Rectangle AIR_TANK = new Rectangle(32, 26, 16, 52);
    public static final Rectangle BANK = new Rectangle(3, 26, 16, 52);
    private static final List<String> nothing = new ArrayList<String>();

    public class CachedArcFurnaceRecipe extends CachedBaseRecipeTesla {
        private PositionedStack input;
        private FluidTankElement output;
        private FluidTankElement airTank;
        private TeslaBankElement bank;

        public CachedArcFurnaceRecipe(RecipeArcFurnace recipe) {
            this.input = new PositionedStack(new ItemStack(recipe.getInput()), 67, 10);
            this.output = new FluidTankElement(TANK, 1, new FluidStack(BlockHandler.fluidActinium, recipe.getOutputValue()), nothing);
            this.output.capacity = this.output.fluid != null ? this.output.fluid.amount : 1000;
            List<String> airTankTip = new ArrayList<String>();
            airTankTip.add(GuiHelper.GuiColor.YELLOW + "Requirement");
            this.airTank = new FluidTankElement(AIR_TANK, 1, new FluidStack(BlockHandler.fluidCompressedAir, 1), airTankTip);
            this.bank = new TeslaBankElement(BANK, new TeslaBank(1000, 1000), airTankTip);
        }

        @Override
        public java.util.List<PositionedStack> getIngredients() {
            return getCycledIngredients(cycleticks / 48, Arrays.asList(input));
        }

        @Override
        public PositionedStack getIngredient () {
            return this.input;
        }


        @Override
        public PositionedStack getResult() {
            return null;
        }

        @Override
        public List<FluidTankElement> getFluidTanks()
        {
            List<FluidTankElement> tanks = new ArrayList<FluidTankElement>();
            tanks.add(this.output);
            tanks.add(this.airTank);
            return tanks;
        }

        @Override
        public List<TeslaBankElement> getEnergyBanks() {
            List<TeslaBankElement> banks = new ArrayList<TeslaBankElement>();
            banks.add(this.bank);
            return banks;
        }

        @Override
        public PositionedStack getOtherStack() {
            return new PositionedStack(fuels.get((cycleticks / 48) % fuels.size()), 67, 60);
        }
    }

    private List<ItemStack> fuels = new ArrayList<ItemStack>();

    @Override
    public int recipiesPerPage() { return 1; }

    @Override
    public String getGuiTexture() {
        return Constants.MODID + ":textures/gui/nei/arcFurnace.png";
    }

    @Override
    public String getRecipeName() {
        return  StatCollector.translateToLocal("modernalchemy.nei.arcfurnace");
    }

    @Override
    public String getRecipeID() {
        return "modernalchemy.arcfurnace.recipes";
    }

    @Override
    public void loadTransferRects ()
    {
        transferRects.add(new RecipeTransferRect(new Rectangle(103, 36, 24, 18), getRecipeID()));
    }

    @Override
    public void drawExtras (int recipe)
    {
        drawProgressBar(103, 35, 176, 0, 24, 16, 48, 0);
    }

    @Override
    public void drawBackground (int recipe)
    {
        GL11.glColor4f(1.0F, 1.0F, 1.0F, 1.0F);
        GuiDraw.changeTexture(this.getGuiTexture());
        GuiDraw.drawTexturedModalRect(-5, 0, 0, 0, 176, 85);
    }

    @Override
    public void loadCraftingRecipes (String outputId, Object... results)
    {
        if (outputId.equals(getRecipeID()))
        {
            for (RecipeArcFurnace recipe : ArcFurnaceRecipeRegistry.instance.recipes)
            {
                this.arecipes.add(new CachedArcFurnaceRecipe(recipe));
            }
            fuels.add(new ItemStack(Items.coal, 1)); //Coal
            fuels.add(new ItemStack(Items.coal, 1, 1)); //Charcoal
        }
        else
        {
            super.loadCraftingRecipes(outputId, results);
        }
    }

    @Override
    public void loadCraftingRecipes (FluidStack result)
    {
        for (RecipeArcFurnace recipe : ArcFurnaceRecipeRegistry.instance.recipes)
        {
            if (areFluidsEqual(new FluidStack(BlockHandler.fluidActinium, recipe.getOutputValue()), result)) {
                this.arecipes.add(new CachedArcFurnaceRecipe(recipe));
            }
        }
        fuels.add(new ItemStack(Items.coal, 1)); //Coal
        fuels.add(new ItemStack(Items.coal, 1, 1)); //Charcoal
    }

    @Override
    public void loadUsageRecipes (ItemStack ingred)
    {
        for (RecipeArcFurnace recipe : ArcFurnaceRecipeRegistry.instance.recipes)
        {
            if (NEIServerUtils.areStacksSameTypeCrafting(new ItemStack(recipe.getInput(), 1), ingred))
            {
                this.arecipes.add(new CachedArcFurnaceRecipe(recipe));
            }
        }
        fuels.add(new ItemStack(Items.coal, 1)); //Coal
        fuels.add(new ItemStack(Items.coal, 1, 1)); //Charcoal
    }
}
