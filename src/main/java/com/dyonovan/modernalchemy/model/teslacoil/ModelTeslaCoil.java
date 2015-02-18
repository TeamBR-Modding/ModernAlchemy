package com.dyonovan.modernalchemy.model.teslacoil;

import com.dyonovan.modernalchemy.lib.Constants;
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

