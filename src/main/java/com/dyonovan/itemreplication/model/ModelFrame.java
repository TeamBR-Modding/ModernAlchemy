package com.dyonovan.itemreplication.model;

import com.dyonovan.itemreplication.blocks.replicator.BlockReplicatorFrame;
import com.dyonovan.itemreplication.blocks.replicator.BlockReplicatorCPU;
import net.minecraft.client.Minecraft;
import net.minecraft.client.model.ModelBase;
import net.minecraft.client.model.ModelRenderer;
import net.minecraft.entity.Entity;

public class ModelFrame extends ModelBase
{
    //fields
    ModelRenderer midFrame1;
    ModelRenderer midFrame2;
    ModelRenderer midFrame3;
    ModelRenderer midFrame4;
    ModelRenderer midFrame5;
    ModelRenderer midFrame6;
    ModelRenderer midFrame7;
    ModelRenderer midFrame8;
    ModelRenderer midFrame9;
    ModelRenderer midFrame10;
    ModelRenderer midFrame11;
    ModelRenderer midFrame12;
    ModelRenderer topFrame1;
    ModelRenderer topFrame2;
    ModelRenderer topFrame3;
    ModelRenderer topFrame4;
    ModelRenderer bottomFrame1;
    ModelRenderer bottomFrame2;
    ModelRenderer bottomFrame4;
    ModelRenderer bottomFrame3;
    ModelRenderer frontFrame1;
    ModelRenderer frontFrame2;
    ModelRenderer frontFrame3;
    ModelRenderer frontFrame4;
    ModelRenderer backFrame1;
    ModelRenderer backFrame2;
    ModelRenderer backFrame3;
    ModelRenderer backFrame4;
    ModelRenderer leftFrame1;
    ModelRenderer leftFrame2;
    ModelRenderer leftFrame3;
    ModelRenderer leftFrame4;
    ModelRenderer rightFrame2;
    ModelRenderer rightFrame3;
    ModelRenderer rightFrame4;
    ModelRenderer rightFrame1;

    public ModelFrame()
    {
        textureWidth = 64;
        textureHeight = 64;

        midFrame1 = new ModelRenderer(this, 0, 9);
        midFrame1.addBox(0F, 0F, 0F, 1, 1, 8);
        midFrame1.setRotationPoint(-4F, 12F, -4F);
        midFrame1.setTextureSize(64, 64);
        midFrame1.mirror = true;
        setRotation(midFrame1, 0F, 0F, 0F);
        midFrame2 = new ModelRenderer(this, 0, 18);
        midFrame2.addBox(0F, 0F, 0F, 1, 1, 8);
        midFrame2.setRotationPoint(3F, 12F, -4F);
        midFrame2.setTextureSize(64, 64);
        midFrame2.mirror = true;
        setRotation(midFrame2, 0F, 0F, 0F);
        midFrame3 = new ModelRenderer(this, 20, 0);
        midFrame3.addBox(0F, 0F, 0F, 6, 1, 1);
        midFrame3.setRotationPoint(-3F, 12F, -4F);
        midFrame3.setTextureSize(64, 64);
        midFrame3.mirror = true;
        setRotation(midFrame3, 0F, 0F, 0F);
        midFrame4 = new ModelRenderer(this, 20, 2);
        midFrame4.addBox(0F, 0F, 0F, 6, 1, 1);
        midFrame4.setRotationPoint(-3F, 12F, 3F);
        midFrame4.setTextureSize(64, 64);
        midFrame4.mirror = true;
        setRotation(midFrame4, 0F, 0F, 0F);
        midFrame5 = new ModelRenderer(this, 18, 9);
        midFrame5.addBox(0F, 0F, 0F, 1, 6, 1);
        midFrame5.setRotationPoint(3F, 13F, 3F);
        midFrame5.setTextureSize(64, 64);
        midFrame5.mirror = true;
        setRotation(midFrame5, 0F, 0F, 0F);
        midFrame6 = new ModelRenderer(this, 22, 9);
        midFrame6.addBox(0F, 0F, 0F, 1, 6, 1);
        midFrame6.setRotationPoint(-4F, 13F, 3F);
        midFrame6.setTextureSize(64, 64);
        midFrame6.mirror = true;
        setRotation(midFrame6, 0F, 0F, 0F);
        midFrame7 = new ModelRenderer(this, 26, 9);
        midFrame7.addBox(0F, 0F, 0F, 1, 6, 1);
        midFrame7.setRotationPoint(-4F, 13F, -4F);
        midFrame7.setTextureSize(64, 64);
        midFrame7.mirror = true;
        setRotation(midFrame7, 0F, 0F, 0F);
        midFrame8 = new ModelRenderer(this, 30, 9);
        midFrame8.addBox(0F, 0F, 0F, 1, 6, 1);
        midFrame8.setRotationPoint(3F, 13F, -4F);
        midFrame8.setTextureSize(64, 64);
        midFrame8.mirror = true;
        setRotation(midFrame8, 0F, 0F, 0F);
        midFrame9 = new ModelRenderer(this, 20, 4);
        midFrame9.addBox(0F, 0F, 0F, 6, 1, 1);
        midFrame9.setRotationPoint(-3F, 19F, 3F);
        midFrame9.setTextureSize(64, 64);
        midFrame9.mirror = true;
        setRotation(midFrame9, 0F, 0F, 0F);
        midFrame10 = new ModelRenderer(this, 0, 27);
        midFrame10.addBox(0F, 0F, 0F, 1, 1, 8);
        midFrame10.setRotationPoint(-4F, 19F, -4F);
        midFrame10.setTextureSize(64, 64);
        midFrame10.mirror = true;
        setRotation(midFrame10, 0F, 0F, 0F);
        midFrame11 = new ModelRenderer(this, 20, 6);
        midFrame11.addBox(0F, 0F, 0F, 6, 1, 1);
        midFrame11.setRotationPoint(-3F, 19F, -4F);
        midFrame11.setTextureSize(64, 64);
        midFrame11.mirror = true;
        setRotation(midFrame11, 0F, 0F, 0F);
        midFrame12 = new ModelRenderer(this, 0, 36);
        midFrame12.addBox(0F, 0F, 0F, 1, 1, 8);
        midFrame12.setRotationPoint(3F, 19F, -4F);
        midFrame12.setTextureSize(64, 64);
        midFrame12.mirror = true;
        setRotation(midFrame12, 0F, 0F, 0F);
        topFrame1 = new ModelRenderer(this, 18, 16);
        topFrame1.addBox(0F, 0F, 0F, 1, 4, 1);
        topFrame1.setRotationPoint(-4F, 8F, -4F);
        topFrame1.setTextureSize(64, 64);
        topFrame1.mirror = true;
        setRotation(topFrame1, 0F, 0F, 0F);
        topFrame2 = new ModelRenderer(this, 22, 16);
        topFrame2.addBox(0F, 0F, 0F, 1, 4, 1);
        topFrame2.setRotationPoint(3F, 8F, -4F);
        topFrame2.setTextureSize(64, 64);
        topFrame2.mirror = true;
        setRotation(topFrame2, 0F, 0F, 0F);
        topFrame3 = new ModelRenderer(this, 26, 16);
        topFrame3.addBox(0F, 0F, 0F, 1, 4, 1);
        topFrame3.setRotationPoint(-4F, 8F, 3F);
        topFrame3.setTextureSize(64, 64);
        topFrame3.mirror = true;
        setRotation(topFrame3, 0F, 0F, 0F);
        topFrame4 = new ModelRenderer(this, 30, 16);
        topFrame4.addBox(0F, 0F, 0F, 1, 4, 1);
        topFrame4.setRotationPoint(3F, 8F, 3F);
        topFrame4.setTextureSize(64, 64);
        topFrame4.mirror = true;
        setRotation(topFrame4, 0F, 0F, 0F);
        bottomFrame1 = new ModelRenderer(this, 18, 21);
        bottomFrame1.addBox(0F, 0F, 0F, 1, 4, 1);
        bottomFrame1.setRotationPoint(-4F, 20F, -4F);
        bottomFrame1.setTextureSize(64, 64);
        bottomFrame1.mirror = true;
        setRotation(bottomFrame1, 0F, 0F, 0F);
        bottomFrame2 = new ModelRenderer(this, 22, 21);
        bottomFrame2.addBox(0F, 0F, 0F, 1, 4, 1);
        bottomFrame2.setRotationPoint(3F, 20F, -4F);
        bottomFrame2.setTextureSize(64, 64);
        bottomFrame2.mirror = true;
        setRotation(bottomFrame2, 0F, 0F, 0F);
        bottomFrame4 = new ModelRenderer(this, 30, 21);
        bottomFrame4.addBox(0F, 0F, 0F, 1, 4, 1);
        bottomFrame4.setRotationPoint(3F, 20F, 3F);
        bottomFrame4.setTextureSize(64, 64);
        bottomFrame4.mirror = true;
        setRotation(bottomFrame4, 0F, 0F, 0F);
        bottomFrame3 = new ModelRenderer(this, 26, 21);
        bottomFrame3.addBox(0F, 0F, 0F, 1, 4, 1);
        bottomFrame3.setRotationPoint(-4F, 20F, 3F);
        bottomFrame3.setTextureSize(64, 64);
        bottomFrame3.mirror = true;
        setRotation(bottomFrame3, 0F, 0F, 0F);
        frontFrame1 = new ModelRenderer(this, 18, 26);
        frontFrame1.addBox(0F, 0F, 0F, 1, 1, 4);
        frontFrame1.setRotationPoint(3F, 12F, -8F);
        frontFrame1.setTextureSize(64, 64);
        frontFrame1.mirror = true;
        setRotation(frontFrame1, 0F, 0F, 0F);
        frontFrame2 = new ModelRenderer(this, 18, 31);
        frontFrame2.addBox(0F, 0F, 0F, 1, 1, 4);
        frontFrame2.setRotationPoint(3F, 19F, -8F);
        frontFrame2.setTextureSize(64, 64);
        frontFrame2.mirror = true;
        setRotation(frontFrame2, 0F, 0F, 0F);
        frontFrame3 = new ModelRenderer(this, 18, 36);
        frontFrame3.addBox(0F, 0F, 0F, 1, 1, 4);
        frontFrame3.setRotationPoint(-4F, 12F, -8F);
        frontFrame3.setTextureSize(64, 64);
        frontFrame3.mirror = true;
        setRotation(frontFrame3, 0F, 0F, 0F);
        frontFrame4 = new ModelRenderer(this, 18, 41);
        frontFrame4.addBox(0F, 0F, 0F, 1, 1, 4);
        frontFrame4.setRotationPoint(-4F, 19F, -8F);
        frontFrame4.setTextureSize(64, 64);
        frontFrame4.mirror = true;
        setRotation(frontFrame4, 0F, 0F, 0F);
        backFrame1 = new ModelRenderer(this, 28, 26);
        backFrame1.addBox(0F, 0F, 0F, 1, 1, 4);
        backFrame1.setRotationPoint(3F, 12F, 4F);
        backFrame1.setTextureSize(64, 64);
        backFrame1.mirror = true;
        setRotation(backFrame1, 0F, 0F, 0F);
        backFrame2 = new ModelRenderer(this, 28, 31);
        backFrame2.addBox(0F, 0F, 0F, 1, 1, 4);
        backFrame2.setRotationPoint(3F, 19F, 4F);
        backFrame2.setTextureSize(64, 64);
        backFrame2.mirror = true;
        setRotation(backFrame2, 0F, 0F, 0F);
        backFrame3 = new ModelRenderer(this, 28, 36);
        backFrame3.addBox(0F, 0F, 0F, 1, 1, 4);
        backFrame3.setRotationPoint(-4F, 12F, 4F);
        backFrame3.setTextureSize(64, 64);
        backFrame3.mirror = true;
        setRotation(backFrame3, 0F, 0F, 0F);
        backFrame4 = new ModelRenderer(this, 28, 41);
        backFrame4.addBox(0F, 0F, 0F, 1, 1, 4);
        backFrame4.setRotationPoint(-4F, 19F, 4F);
        backFrame4.setTextureSize(64, 64);
        backFrame4.mirror = true;
        setRotation(backFrame4, 0F, 0F, 0F);
        leftFrame1 = new ModelRenderer(this, 0, 0);
        leftFrame1.addBox(0F, 0F, 0F, 4, 1, 1);
        leftFrame1.setRotationPoint(4F, 12F, 3F);
        leftFrame1.setTextureSize(64, 64);
        leftFrame1.mirror = true;
        setRotation(leftFrame1, 0F, 0F, 0F);
        leftFrame2 = new ModelRenderer(this, 0, 2);
        leftFrame2.addBox(0F, 0F, 0F, 4, 1, 1);
        leftFrame2.setRotationPoint(4F, 19F, 3F);
        leftFrame2.setTextureSize(64, 64);
        leftFrame2.mirror = true;
        setRotation(leftFrame2, 0F, 0F, 0F);
        leftFrame3 = new ModelRenderer(this, 0, 4);
        leftFrame3.addBox(0F, 0F, 0F, 4, 1, 1);
        leftFrame3.setRotationPoint(4F, 12F, -4F);
        leftFrame3.setTextureSize(64, 64);
        leftFrame3.mirror = true;
        setRotation(leftFrame3, 0F, 0F, 0F);
        leftFrame4 = new ModelRenderer(this, 0, 6);
        leftFrame4.addBox(0F, 0F, 0F, 4, 1, 1);
        leftFrame4.setRotationPoint(4F, 19F, -4F);
        leftFrame4.setTextureSize(64, 64);
        leftFrame4.mirror = true;
        setRotation(leftFrame4, 0F, 0F, 0F);
        rightFrame2 = new ModelRenderer(this, 10, 2);
        rightFrame2.addBox(0F, 0F, 0F, 4, 1, 1);
        rightFrame2.setRotationPoint(-8F, 19F, 3F);
        rightFrame2.setTextureSize(64, 64);
        rightFrame2.mirror = true;
        setRotation(rightFrame2, 0F, 0F, 0F);
        rightFrame3 = new ModelRenderer(this, 10, 4);
        rightFrame3.addBox(0F, 0F, 0F, 4, 1, 1);
        rightFrame3.setRotationPoint(-8F, 12F, -4F);
        rightFrame3.setTextureSize(64, 64);
        rightFrame3.mirror = true;
        setRotation(rightFrame3, 0F, 0F, 0F);
        rightFrame4 = new ModelRenderer(this, 10, 6);
        rightFrame4.addBox(0F, 0F, 0F, 4, 1, 1);
        rightFrame4.setRotationPoint(-8F, 19F, -4F);
        rightFrame4.setTextureSize(64, 64);
        rightFrame4.mirror = true;
        setRotation(rightFrame4, 0F, 0F, 0F);
        rightFrame1 = new ModelRenderer(this, 10, 0);
        rightFrame1.addBox(0F, 0F, 0F, 4, 1, 1);
        rightFrame1.setRotationPoint(-8F, 12F, 3F);
        rightFrame1.setTextureSize(64, 64);
        rightFrame1.mirror = true;
        setRotation(rightFrame1, 0F, 0F, 0F);
    }

    public void renderModel(float f, int x, int y, int z, boolean inHand) {
        midFrame1.render(f);
        midFrame2.render(f);
        midFrame3.render(f);
        midFrame4.render(f);
        midFrame5.render(f);
        midFrame6.render(f);
        midFrame7.render(f);
        midFrame8.render(f);
        midFrame9.render(f);
        midFrame10.render(f);
        midFrame11.render(f);
        midFrame12.render(f);
        if (canConnect(x, y, z, "top") || inHand) {
            topFrame1.render(f);
            topFrame2.render(f);
            topFrame3.render(f);
            topFrame4.render(f);
        }
        if (canConnect(x, y, z, "bottom") || inHand) {
            bottomFrame1.render(f);
            bottomFrame2.render(f);
            bottomFrame4.render(f);
            bottomFrame3.render(f);
        }
        if (canConnect(x, y, z, "front") && !inHand) {
            frontFrame1.render(f);
            frontFrame2.render(f);
            frontFrame3.render(f);
            frontFrame4.render(f);
        }
        if (canConnect(x, y, z, "back") && !inHand) {
            backFrame1.render(f);
            backFrame2.render(f);
            backFrame3.render(f);
            backFrame4.render(f);
        }
        if (canConnect(x, y, z, "left") && !inHand) {
            leftFrame1.render(f);
            leftFrame2.render(f);
            leftFrame3.render(f);
            leftFrame4.render(f);
        }
        if (canConnect(x, y, z, "right") && !inHand) {
        rightFrame2.render(f);
        rightFrame3.render(f);
        rightFrame4.render(f);
        rightFrame1.render(f);
        }
    }

    public void render(Entity entity, float f, float f1, float f2, float f3, float f4, float f5)
    {
        super.render(entity, f, f1, f2, f3, f4, f5);
        setRotationAngles(f, f1, f2, f3, f4, f5, entity);
        midFrame1.render(f5);
        midFrame2.render(f5);
        midFrame3.render(f5);
        midFrame4.render(f5);
        midFrame5.render(f5);
        midFrame6.render(f5);
        midFrame7.render(f5);
        midFrame8.render(f5);
        midFrame9.render(f5);
        midFrame10.render(f5);
        midFrame11.render(f5);
        midFrame12.render(f5);
        topFrame1.render(f5);
        topFrame2.render(f5);
        topFrame3.render(f5);
        topFrame4.render(f5);
        bottomFrame1.render(f5);
        bottomFrame2.render(f5);
        bottomFrame4.render(f5);
        bottomFrame3.render(f5);
        frontFrame1.render(f5);
        frontFrame2.render(f5);
        frontFrame3.render(f5);
        frontFrame4.render(f5);
        backFrame1.render(f5);
        backFrame2.render(f5);
        backFrame3.render(f5);
        backFrame4.render(f5);
        leftFrame1.render(f5);
        leftFrame2.render(f5);
        leftFrame3.render(f5);
        leftFrame4.render(f5);
        rightFrame2.render(f5);
        rightFrame3.render(f5);
        rightFrame4.render(f5);
        rightFrame1.render(f5);
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

    private boolean canConnect(int x, int y, int z, String side) {

        if (side.equals("top")) {
            return Minecraft.getMinecraft().theWorld.getBlock(x, y + 1, z) instanceof BlockReplicatorFrame ||
                    Minecraft.getMinecraft().theWorld.getBlock(x, y + 1, z) instanceof BlockReplicatorCPU;
        } else if (side.equals("bottom")) {
            return Minecraft.getMinecraft().theWorld.getBlock(x, y - 1, z) instanceof BlockReplicatorFrame ||
                    Minecraft.getMinecraft().theWorld.getBlock(x, y - 1, z) instanceof BlockReplicatorCPU ||
                    !Minecraft.getMinecraft().theWorld.isAirBlock(x, y - 1, z);
        } else if (side.equals("front")) {
            return Minecraft.getMinecraft().theWorld.getBlock(x, y, z - 1) instanceof BlockReplicatorFrame ||
                    Minecraft.getMinecraft().theWorld.getBlock(x, y, z - 1) instanceof BlockReplicatorCPU;
        } else if (side.equals("back")) {
            return Minecraft.getMinecraft().theWorld.getBlock(x, y, z + 1) instanceof BlockReplicatorFrame ||
                    Minecraft.getMinecraft().theWorld.getBlock(x, y, z + 1) instanceof BlockReplicatorCPU;
        } else if (side.equals("left")) {
            return Minecraft.getMinecraft().theWorld.getBlock(x - 1, y, z) instanceof BlockReplicatorFrame ||
                    Minecraft.getMinecraft().theWorld.getBlock(x - 1, y, z) instanceof BlockReplicatorCPU;
        } else if (side.equals("right")) {
            return Minecraft.getMinecraft().theWorld.getBlock(x + 1, y, z) instanceof BlockReplicatorFrame ||
                    Minecraft.getMinecraft().theWorld.getBlock(x + 1, y, z) instanceof BlockReplicatorCPU;
        }
        return false;
    }
}