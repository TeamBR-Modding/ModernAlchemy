package com.dyonovan.modernalchemy.client.nei;

import codechicken.lib.gui.GuiDraw;
import codechicken.nei.guihook.GuiContainerManager;
import codechicken.nei.recipe.GuiRecipe;
import com.dyonovan.modernalchemy.client.nei.elements.TeslaBankElement;

import java.awt.*;
import java.util.List;

public class RecipeHandlerBaseTesla extends RecipeHandlerBase {

    public abstract class CachedBaseRecipeTesla extends RecipeHandlerBase.CachedBaseRecipe {
        public List<TeslaBankElement> getEnergyBanks ()
        {
            return null;
        }
    }

    @Override
    public String getRecipeID() {
        return null;
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
    public void drawForeground (int recipe) {
        super.drawForeground(recipe);
        this.drawEnergyBanks(recipe);
    }


    @Override
    public List<String> handleTooltip (GuiRecipe guiRecipe, List<String> currenttip, int recipe) {
        super.handleTooltip(guiRecipe, currenttip, recipe);
        CachedBaseRecipeTesla crecipe = (CachedBaseRecipeTesla) this.arecipes.get(recipe);
        if (GuiContainerManager.shouldShowTooltip(guiRecipe)) {
            Point mouse = GuiDraw.getMousePosition();
            Point offset = guiRecipe.getRecipePosition(recipe);
            Point relMouse = new Point(mouse.x - ((guiRecipe.width - 176) / 2) - offset.x, mouse.y - ((guiRecipe.height - 166) / 2) - offset.y);

            if (crecipe.getEnergyBanks() != null) {
                for (TeslaBankElement bank : crecipe.getEnergyBanks()) {
                    if (bank.position.contains(relMouse)) {
                        bank.handleTooltip(currenttip);
                    }
                }
            }
        }
        return currenttip;
    }

    public void drawEnergyBanks (int recipe)
    {
        CachedBaseRecipeTesla crecipe = (CachedBaseRecipeTesla) this.arecipes.get(recipe);
        if (crecipe.getEnergyBanks() != null)
        {
            for (TeslaBankElement teslaBankElement : crecipe.getEnergyBanks())
            {
                teslaBankElement.draw();
            }
        }
    }
}
