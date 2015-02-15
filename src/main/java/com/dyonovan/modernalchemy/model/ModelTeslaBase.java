package com.dyonovan.modernalchemy.model;

import net.minecraft.client.model.ModelBase;
import net.minecraft.client.model.ModelRenderer;
import net.minecraft.entity.Entity;

public class ModelTeslaBase extends ModelBase {

    ModelRenderer mainStand;

    public ModelTeslaBase() {
        textureWidth = 32;
        textureHeight = 32;

        mainStand = new ModelRenderer(this, 0, 0);
        mainStand.addBox(0F, 0F, 0F, 4, 16, 4);
        mainStand.setRotationPoint(-2F, 8F, -2F);
        mainStand.setTextureSize(32, 32);
        mainStand.mirror = true;
        setRotation(mainStand, 0F, 0F, 0F);
    }

    public void render(Entity entity, float f, float f1, float f2, float f3, float f4, float f5)
    {
        super.render(entity, f, f1, f2, f3, f4, f5);
        setRotationAngles(f, f1, f2, f3, f4, f5, entity);
        mainStand.render(f5);
    }

    public void renderModel(float f, int x, int y, int z, boolean inHand) {
        mainStand.render(f);
    }

    private void setRotation(ModelRenderer model, float x, float y, float z)
    {
        model.rotateAngleX = x;
        model.rotateAngleY = y;
        model.rotateAngleZ = z;
    }

    public void setRotationAngles(float f, float f1, float f2, float f3, float f4, float f5, Entity entity)
    {
        super.setRotationAngles(f, f1, f2, f3, f4, f5, entity);
    }
}
