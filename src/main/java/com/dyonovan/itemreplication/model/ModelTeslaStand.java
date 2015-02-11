package com.dyonovan.itemreplication.model;

import com.dyonovan.itemreplication.lib.Constants;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.client.model.AdvancedModelLoader;
import net.minecraftforge.client.model.IModelCustom;

public class ModelTeslaStand {
    private IModelCustom modelStand;

    public ModelTeslaStand() {
        modelStand = AdvancedModelLoader.loadModel(new ResourceLocation(Constants.MODID + ":models/teslaStand.obj"));
    }

    public void render() {
        modelStand.renderAll();
    }
}
