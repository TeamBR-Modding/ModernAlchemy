package com.dyonovan.itemreplication.model;

import com.dyonovan.itemreplication.lib.Constants;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.client.model.AdvancedModelLoader;
import net.minecraftforge.client.model.IModelCustom;

public class ModelTeslaCoil {
    private IModelCustom modelCoil;

    public ModelTeslaCoil() {
        modelCoil = AdvancedModelLoader.loadModel(new ResourceLocation(Constants.MODID + ":models/teslaCoil.obj"));
    }

    public void render() {
        modelCoil.renderAll();
    }
}

