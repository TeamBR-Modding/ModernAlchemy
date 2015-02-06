package com.dyonovan.itemreplication.model;

import net.minecraft.client.model.ModelBase;
import net.minecraft.client.model.ModelRenderer;
import net.minecraft.entity.Entity;

public class ModelTeslaBase extends ModelBase
{
    //fields
    ModelRenderer baseShaft;
    ModelRenderer baseBase;

    public ModelTeslaBase()
    {
        textureWidth = 128;
        textureHeight = 64;

        baseShaft = new ModelRenderer(this, 0, 0);
        baseShaft.addBox(0F, 0F, 0F, 6, 47, 6);
        baseShaft.setRotationPoint(-3F, -24F, -3F);
        baseShaft.setTextureSize(128, 64);
        baseShaft.mirror = true;
        setRotation(baseShaft, 0F, 0F, 0F);
        baseBase = new ModelRenderer(this, 24, 0);
        baseBase.addBox(-8F, 0F, -8F, 16, 1, 16);
        baseBase.setRotationPoint(0F, 23F, 0F);
        baseBase.setTextureSize(128, 64);
        baseBase.mirror = true;
        setRotation(baseBase, 0F, 0F, 0F);
    }

    public void render(Entity entity, float f, float f1, float f2, float f3, float f4, float f5)
    {
        super.render(entity, f, f1, f2, f3, f4, f5);
        setRotationAngles(f, f1, f2, f3, f4, f5, entity);
        baseShaft.render(f5);
        baseBase.render(f5);
    }

    public void renderModel(float f, int x, int y, int z, boolean inHand) {
        baseShaft.render(f);
        baseBase.render(f);
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
