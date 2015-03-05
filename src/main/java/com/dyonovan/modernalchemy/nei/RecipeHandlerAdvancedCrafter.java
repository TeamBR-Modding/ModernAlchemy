package com.dyonovan.modernalchemy.nei;

import codechicken.lib.gui.GuiDraw;
import codechicken.nei.NEIServerUtils;
import codechicken.nei.PositionedStack;
import com.dyonovan.modernalchemy.crafting.AdvancedCrafterRecipeRegistry;
import com.dyonovan.modernalchemy.crafting.OreDictStack;
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
import java.util.Random;

public class RecipeHandlerAdvancedCrafter extends RecipeHandlerBase {
    public static Random rand = new Random();
    public class CachedAdvancedCraftingRecipe extends CachedBaseRecipe {
        private List<PositionedStack> inputArray;
        private List<PositionedStack> output;
        public int mode;
        public int tickTime;
        List<Integer> oreSpots = new ArrayList<Integer>();

        public CachedAdvancedCraftingRecipe(RecipeAdvancedCrafter recipe) {
            inputArray = new ArrayList<PositionedStack>();
            for(int i = 0; i < recipe.getInput().size(); i++) {
                if(recipe.getInput().get(i) != null)
                    if(recipe.getInput().get(i) instanceof ItemStack)
                        inputArray.add(new PositionedStack(recipe.getConvertedStack(i), 53 + (i < 2 ? (18 * i) : (18 * (i - 2))), (i < 2 ? 11 : 29)));
                    else if(recipe.getInput().get(i) instanceof OreDictStack) {
                        oreSpots.add(i);
                        for(ItemStack stack : ((OreDictStack) recipe.getInput().get(i)).getItemList())
                            inputArray.add(new PositionedStack(stack, 53 + (i < 2 ? (18 * i) : (18 * (i - 2))), (i < 2 ? 11 : 29)));
                    }
            }
            output.add(new PositionedStack(recipe.getOutputItem(), 129, 19));
            mode = recipe.getRequiredMode();
            tickTime = recipe.getProcessTime();
        }

        @Override
        public PositionedStack getResult() {
            return output.get(0);
        }

        @Override
        public java.util.List<PositionedStack> getIngredients() {
            List<PositionedStack> out = new ArrayList<PositionedStack>();
            List<PositionedStack> slot1 = new ArrayList<PositionedStack>();
            List<PositionedStack> slot2 = new ArrayList<PositionedStack>();
            List<PositionedStack> slot3 = new ArrayList<PositionedStack>();
            List<PositionedStack> slot4 = new ArrayList<PositionedStack>();

            for(PositionedStack stack : inputArray) {
                switch(getPositionFromPoint(stack.relx, stack.rely)) {
                    case 1 :
                        slot1.add(stack);
                        break;
                    case 2 :
                        slot2.add(stack);
                        break;
                    case 3 :
                        slot3.add(stack);
                        break;
                    case 4 :
                        slot4.add(stack);
                }
            }

            if(!slot1.isEmpty())
                out.add(slot1.get((cycleticks / 48) % slot1.size()));
            if(!slot2.isEmpty())
                out.add(slot2.get((cycleticks / 48) % slot2.size()));
            if(!slot3.isEmpty())
                out.add(slot3.get((cycleticks / 48) % slot3.size()));
            if(!slot4.isEmpty())
                out.add(slot4.get((cycleticks / 48) % slot4.size()));

            return out;
        }

        public int getPositionFromPoint(int x, int y) {
            if(x == 53 && y == 11)
                return 1;
            else if(x == (53 + 18) && y == 11)
                return 2;
            else if(x == 53 && y == 29)
                return 3;
            else
                return 4;
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
                if(recipe.getInput().get(i) instanceof ItemStack) {
                    if (NEIServerUtils.areStacksSameTypeCrafting((ItemStack) recipe.getInput().get(i), ingred)) {
                        this.arecipes.add(new CachedAdvancedCraftingRecipe(recipe));
                    }
                } else if(recipe.getInput().get(i) instanceof OreDictStack) {
                    if(((OreDictStack) recipe.getInput().get(i)).isEqual(ingred))
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
