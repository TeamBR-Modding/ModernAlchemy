package com.dyonovan.modernalchemy.client.nei;

import codechicken.nei.api.API;
import codechicken.nei.api.IConfigureNEI;
import codechicken.nei.recipe.TemplateRecipeHandler;
import com.dyonovan.modernalchemy.ModernAlchemy;
import com.dyonovan.modernalchemy.client.nei.machines.RecipeHandlerAdvancedCrafter;
import com.dyonovan.modernalchemy.client.nei.machines.RecipeHandlerArcFurnace;
import com.dyonovan.modernalchemy.client.nei.machines.RecipeHandlerSolidifier;
import com.dyonovan.modernalchemy.lib.Constants;
import net.minecraft.util.StatCollector;

public class NEIAddonConfig implements IConfigureNEI {

    @Override
    public void loadConfig() {
        registerHandler(new RecipeHandlerArcFurnace());
        registerHandler(new RecipeHandlerAdvancedCrafter());
        registerHandler(new RecipeHandlerSolidifier());

        ModernAlchemy.nei = new NEICallback();
    }

    @Override
    public String getName() {
        return StatCollector.translateToLocal("modernalchemy.nei.name");
    }

    @Override
    public String getVersion() {
        return Constants.VERSION;
    }

    private static void registerHandler (TemplateRecipeHandler handler)
    {
        API.registerRecipeHandler(handler);
        API.registerUsageHandler(handler);
    }
}
