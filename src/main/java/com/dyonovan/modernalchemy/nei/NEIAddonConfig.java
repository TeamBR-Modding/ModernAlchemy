package com.dyonovan.modernalchemy.nei;

import codechicken.nei.api.API;
import codechicken.nei.api.IConfigureNEI;
import codechicken.nei.recipe.TemplateRecipeHandler;
import com.dyonovan.modernalchemy.lib.Constants;
import net.minecraft.util.StatCollector;

public class NEIAddonConfig implements IConfigureNEI {

    @Override
    public void loadConfig() {
        registerHandler(new RecipeHandlerArcFurnace());
        registerHandler(new RecipeHandlerAdvancedCrafter());
        registerHandler(new RecipeHandlerSolidifier());
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
