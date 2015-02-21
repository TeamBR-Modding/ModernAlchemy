package com.dyonovan.modernalchemy.model.teslacoil;

import com.dyonovan.modernalchemy.lib.Constants;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.client.model.AdvancedModelLoader;
import net.minecraftforge.client.model.IModelCustom;

public class ModelTeslaBase {
    private IModelCustom modelBase;

    public ModelTeslaBase() {
        modelBase = AdvancedModelLoader.loadModel(new ResourceLocation(Constants.MODID + ":models/teslaBase.obj"));
    }

    //Model Names:
    //Main   : NurbsPath.001_Mesh
    //Switch : Cube.007_Cube.010
    //Rotor  : Cylinder.014_Cylinder.018

    public void renderMain() {
        modelBase.renderPart("NurbsPath.001_Mesh");
    }

    public void renderSwitch() {
        modelBase.renderPart("Cube.007_Cube.010");
    }

    public void renderRotor() {
        modelBase.renderPart("Cylinder.014_Cylinder.018");
    }
}
