package com.dyonovan.modernalchemy.nei;

import codechicken.lib.gui.GuiDraw;
import codechicken.nei.NEIServerUtils;
import codechicken.nei.PositionedStack;
import com.dyonovan.modernalchemy.crafting.AdvancedCrafterRecipeRegistry;
import com.dyonovan.modernalchemy.crafting.RecipeAdvancedCrafter;
import com.dyonovan.modernalchemy.lib.Constants;
import com.dyonovan.modernalchemy.tileentity.machines.TileAdvancedCrafter;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.FontRenderer;
import net.minecraft.item.ItemStack;
import net.minecraft.util.StatCollector;
import org.lwjgl.opengl.GL11;

import java.awt.*;
import java.util.ArrayList;
import java.util.List;

public class RecipeHandlerAdvancedCrafter extends RecipeHandlerBase {

    public class CachedAdvancedCraftingRecipe extends CachedBaseRecipe {
        private List<PositionedStack> inputArray;
        private PositionedStack output;
        public int mode;
        public int tickTime;

        public CachedAdvancedCraftingRecipe(RecipeAdvancedCrafter recipe) {
            inputArray = new ArrayList<PositionedStack>();
            for(int i = 0; i < recipe.getInput().size(); i++) {
                if(recipe.getInput().get(i) != null)
                    inputArray.add(new PositionedStack(recipe.getInput().get(i), 53 + (18 * i), (i < 2 ? 11 : 29)));
            }
            output = new PositionedStack(recipe.getOutputItem(), 129, 19);
            mode = recipe.getRequiredMode();
            tickTime = recipe.getProcessTime();
        }

        @Override
        public PositionedStack getResult() {
            return output;
        }

        @Override
        public java.util.List<PositionedStack> getIngredients() {
            return inputArray;
        }
    }

    @Override
    public int recipiesPerPage() { return 2; }

    @Override
    public String getGuiTexture() {
        return Constants.MODID + ":textures/gui/nei/advanceCrafter.png";
    }

    @Override
    public String getRecipeID() {
        return "modernalchemy.advancedCrafter.recipes";
    }

    @Override
    public String getRecipeName() {
        return StatCollector.translateToLocal("modernalchemy.nei.advancedCrafter");
    }

    @Override
    public void loadTransferRects ()
    {
        transferRects.add(new RecipeTransferRect(new Rectangle(94, 20, 24, 18), getRecipeID()));
    }

    @Override
    public void drawExtras (int recipe)
    {
        drawProgressBar(94, 20, 176, 0, 24, 16, 48, 0);
        int mode = ((CachedAdvancedCraftingRecipe)this.arecipes.get(recipe)).mode;
        FontRenderer fontRenderer = Minecraft.getMinecraft().fontRenderer;
        switch (mode) {
            case TileAdvancedCrafter.BEND :
                GuiDraw.drawTexturedModalRect(17, 20, 208, 17, 16, 16);
                GuiDraw.drawString(StatCollector.translateToLocal("modernalchemy.advancedCrafter.bending"), 25 - (fontRenderer.getStringWidth(StatCollector.translateToLocal("modernalchemy.advancedCrafter.bending")) / 2), 10, 0, false);
                break;
            case TileAdvancedCrafter.EXTRUDE :
                GuiDraw.drawTexturedModalRect(17, 20, 192, 17, 16, 16);
                GuiDraw.drawString(StatCollector.translateToLocal("modernalchemy.advancedCrafter.extruding"), 25 - (fontRenderer.getStringWidth(StatCollector.translateToLocal("modernalchemy.advancedCrafter.extruding")) / 2), 10, 0, false);
                break;
            case TileAdvancedCrafter.COOK :
                GuiDraw.drawTexturedModalRect(17, 20, 176, 17, 16, 16);
                GuiDraw.drawString(StatCollector.translateToLocal("modernalchemy.advancedCrafter.enriching"), 25 - (fontRenderer.getStringWidth(StatCollector.translateToLocal("modernalchemy.advancedCrafter.enriching")) / 2), 10, 0, false);
                break;
            case TileAdvancedCrafter.FURNACE :
                GuiDraw.drawTexturedModalRect(17, 20, 224, 17, 16, 16);
                GuiDraw.drawString(StatCollector.translateToLocal("modernalchemy.advancedCrafter.furnace"), 25 - (fontRenderer.getStringWidth(StatCollector.translateToLocal("modernalchemy.advancedCrafter.enriching")) / 2), 10, 0, false);
                break;

        }
        int time = ((CachedAdvancedCraftingRecipe)this.arecipes.get(recipe)).tickTime;
        GuiDraw.drawString("" + time + "t", 94, 12, 0, false);
    }

    @Override
    public void drawBackground (int recipe)
    {
        GL11.glColor4f(1.0F, 1.0F, 1.0F, 1.0F);
        GuiDraw.changeTexture(this.getGuiTexture());
        GuiDraw.drawTexturedModalRect(0, 0, 0, 0, 176, 70);
    }

    @Override
    public void loadCraftingRecipes (String outputId, Object... results)
    {
        if (outputId.equals(getRecipeID()))
        {
            for (RecipeAdvancedCrafter recipe : AdvancedCrafterRecipeRegistry.instance.recipes)
            {
                this.arecipes.add(new CachedAdvancedCraftingRecipe(recipe));
            }
        }
        else
        {
            super.loadCraftingRecipes(outputId, results);
        }
    }

    @Override
    public void loadUsageRecipes (ItemStack ingred)
    {
        for (RecipeAdvancedCrafter recipe : AdvancedCrafterRecipeRegistry.instance.recipes) {
            for (int i = 0; i < recipe.getInput().size(); i++) {
                if (NEIServerUtils.areStacksSameTypeCrafting(recipe.getInput().get(i), ingred)) {
                    this.arecipes.add(new CachedAdvancedCraftingRecipe(recipe));
                }
            }
        }
    }

    @Override
    public void loadCraftingRecipes (ItemStack result)
    {
        for (RecipeAdvancedCrafter recipe : AdvancedCrafterRecipeRegistry.instance.recipes) {
            if(NEIServerUtils.areStacksSameType(recipe.getOutputItem(), result)) {
                this.arecipes.add(new CachedAdvancedCraftingRecipe(recipe));
            }
        }
    }
}
