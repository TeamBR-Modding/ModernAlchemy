package com.dyonovan.modernalchemy.client.renderer.arcfurnace;

import com.dyonovan.modernalchemy.common.tileentity.arcfurnace.dummies.TileDummy;
import cpw.mods.fml.client.registry.ISimpleBlockRenderingHandler;
import cpw.mods.fml.client.registry.RenderingRegistry;
import net.minecraft.block.Block;
import net.minecraft.client.renderer.RenderBlocks;
import net.minecraft.client.renderer.Tessellator;
import net.minecraft.world.IBlockAccess;
import org.lwjgl.opengl.GL11;

public class ArcFurnaceDummyRenderer implements ISimpleBlockRenderingHandler {
    public static int dummyRenderID = RenderingRegistry.getNextAvailableRenderId();

    @Override
    public void renderInventoryBlock(Block block, int metadata, int modelId, RenderBlocks renderer) {
        Tessellator tessellator = Tessellator.instance;

        block.setBlockBoundsForItemRender();
        GL11.glTranslatef(-0.5F, -0.5F, -0.5F);
        tessellator.startDrawingQuads();
        tessellator.setNormal(0.0F, -1F, 0.0F);
        renderer.renderFaceYNeg(block, 0.0D, 0.0D, 0.0D, renderer.getBlockIconFromSideAndMetadata(block, 0, metadata));
        tessellator.draw();

        tessellator.startDrawingQuads();
        tessellator.setNormal(0.0F, 1.0F, 0.0F);
        renderer.renderFaceYPos(block, 0.0D, 0.0D, 0.0D, renderer.getBlockIconFromSideAndMetadata(block, 1, metadata));

        tessellator.draw();

        tessellator.startDrawingQuads();
        tessellator.setNormal(0.0F, 0.0F, -1F);
        renderer.renderFaceZNeg(block, 0.0D, 0.0D, 0.0D, renderer.getBlockIconFromSideAndMetadata(block, 2, metadata));

        tessellator.draw();

        tessellator.startDrawingQuads();
        tessellator.setNormal(0.0F, 0.0F, 1.0F);
        renderer.renderFaceZPos(block, 0.0D, 0.0D, 0.0D, renderer.getBlockIconFromSideAndMetadata(block, 3, metadata));

        tessellator.draw();

        tessellator.startDrawingQuads();
        tessellator.setNormal(-1F, 0.0F, 0.0F);
        renderer.renderFaceXNeg(block, 0.0D, 0.0D, 0.0D, renderer.getBlockIconFromSideAndMetadata(block, 4, metadata));

        tessellator.draw();

        tessellator.startDrawingQuads();
        tessellator.setNormal(1.0F, 0.0F, 0.0F);
        renderer.renderFaceXPos(block, 0.0D, 0.0D, 0.0D, renderer.getBlockIconFromSideAndMetadata(block, 5, 5));

        tessellator.draw();

        GL11.glTranslatef(0.5F, 0.5F, 0.5F);
    }

    @Override
    public boolean renderWorldBlock(IBlockAccess world, int x, int y, int z, Block block, int modelId, RenderBlocks renderer) {
        TileDummy tile = (TileDummy)world.getTileEntity(x, y, z);
        if(tile.getCore() != null) {
            float minU = block.getIcon(0, 0).getMinU();
            float minV = block.getIcon(0, 0).getMinV();

            float maxU = block.getIcon(0, 0).getMaxU();
            float maxV = block.getIcon(0, 0).getMaxV();
            if(tile.yCoord < tile.getCore().yCoord) { //Bottom level
                renderer.setRenderBounds(0, 0, 0, 1, 1, 1);
                renderer.renderStandardBlock(block, x, y, z);
            } else if(tile.yCoord == tile.getCore().yCoord) { //Middle Level
                if(tile.xCoord == tile.getCore().xCoord || tile.zCoord == tile.getCore().zCoord) { //Center faces
                    renderer.setRenderBounds(0, 0, 0, 1, 1, 1);
                    renderer.renderStandardBlock(block, x, y, z);
                }
                GL11.glDisable(GL11.GL_CULL_FACE);

                Tessellator tess = Tessellator.instance;
                tess.addTranslation(x, y, z);

                //Lower right, top left
                if((tile.xCoord > tile.getCore().xCoord && tile.zCoord < tile.getCore().zCoord) || (tile.xCoord < tile.getCore().xCoord && tile.zCoord > tile.getCore().zCoord)) {
                    tess.addVertexWithUV(0, 0, 0, minU, minV);
                    tess.addVertexWithUV(1, 0, 1, maxU, minV);
                    tess.addVertexWithUV(1, 1, 1, maxU, maxV);
                    tess.addVertexWithUV(0, 1, 0, minU, maxV);
                    tess.draw();

                    tess.startDrawing(GL11.GL_TRIANGLES);

                    if(tile.xCoord < tile.getCore().xCoord) {
                        tess.addVertexWithUV(1, 1, 0, minU, minV);
                        tess.addVertexWithUV(0, 1, 0, minU, maxV);
                        tess.addVertexWithUV(1, 1, 1, maxU, minV);
                    } else {
                        tess.addVertexWithUV(0, 1, 0, maxU, maxV);
                        tess.addVertexWithUV(0, 1, 1, minU, maxV);
                        tess.addVertexWithUV(1, 1, 1, maxU, minV);
                    }

                    tess.draw();
                    tess.startDrawingQuads();
                }
                //Lower left, top right
                else if((tile.xCoord < tile.getCore().xCoord && tile.zCoord < tile.getCore().zCoord) || (tile.xCoord > tile.getCore().xCoord && tile.zCoord > tile.getCore().zCoord)) {
                    tess.addVertexWithUV(1, 0, 0, minU, minV);
                    tess.addVertexWithUV(0, 0, 1, maxU, minV);
                    tess.addVertexWithUV(0, 1, 1, maxU, maxV);
                    tess.addVertexWithUV(1, 1, 0, minU, maxV);
                    tess.draw();

                    tess.startDrawing(GL11.GL_TRIANGLES);

                    if(tile.xCoord < tile.getCore().xCoord) {
                        tess.addVertexWithUV(1, 1, 0, minU, minV);
                        tess.addVertexWithUV(1, 1, 1, minU, maxV);
                        tess.addVertexWithUV(0, 1, 1, maxU, minV);
                    } else {
                        tess.addVertexWithUV(0, 1, 0, maxU, maxV);
                        tess.addVertexWithUV(1, 1, 0, minU, maxV);
                        tess.addVertexWithUV(0, 1, 1, maxU, minV);
                    }

                    tess.draw();
                    tess.startDrawingQuads();
                }
                tess.addTranslation(-x, -y, -z);

            } else if(tile.yCoord > tile.getCore().yCoord) { //Top
                if(tile.zCoord == tile.getCore().zCoord && tile.xCoord == tile.getCore().xCoord) {
                    renderer.setRenderBounds(0, 0, 0, 1, 1, 1);
                    renderer.renderStandardBlock(block, x, y, z);
                    return true;
                }
                Tessellator tess = Tessellator.instance;
                tess.addTranslation(x, y, z);
                if(tile.xCoord == tile.getCore().xCoord) {
                    if(tile.zCoord > tile.getCore().zCoord) {
                        tess.addVertexWithUV(0, 0, 1, maxU, maxV);
                        tess.addVertexWithUV(0, 1, 0, minU, maxV);
                        tess.addVertexWithUV(1, 1, 0, minU, minV);
                        tess.addVertexWithUV(1, 0, 1, maxU, minV);
                    } else if(tile.zCoord < tile.getCore().zCoord) {
                        tess.addVertexWithUV(0, 0, 0, maxU, maxV);
                        tess.addVertexWithUV(0, 1, 1, minU, maxV);
                        tess.addVertexWithUV(1, 1, 1, minU, minV);
                        tess.addVertexWithUV(1, 0, 0, maxU, minV);
                    }
                } else if(tile.xCoord > tile.getCore().xCoord && tile.zCoord == tile.getCore().zCoord) {
                    tess.addVertexWithUV(0, 1, 1, maxU, maxV);
                    tess.addVertexWithUV(0, 1, 0, minU, maxV);
                    tess.addVertexWithUV(1, 0, 0, minU, minV);
                    tess.addVertexWithUV(1, 0, 1, maxU, minV);
                } else if(tile.xCoord < tile.getCore().xCoord && tile.zCoord == tile.getCore().zCoord) {
                    tess.addVertexWithUV(0, 0, 1, minU, maxV);
                    tess.addVertexWithUV(1, 1, 1, minU, minV);
                    tess.addVertexWithUV(1, 1, 0, maxU, minV);
                    tess.addVertexWithUV(0, 0, 0, maxU, maxV);
                }
                else {
                    tess.draw();
                    tess.startDrawing(GL11.GL_TRIANGLES);
                    if(tile.xCoord < tile.getCore().xCoord && tile.zCoord < tile.getCore().zCoord) {
                        tess.addVertexWithUV(1, 0, 0, minU, minV);
                        tess.addVertexWithUV(1, 1, 1, maxU, minV);
                        tess.addVertexWithUV(0, 0, 1, maxU, maxV);
                    }
                    else if(tile.xCoord < tile.getCore().xCoord && tile.zCoord > tile.getCore().zCoord) {
                        tess.addVertexWithUV(0, 0, 0, minU, maxV);
                        tess.addVertexWithUV(1, 1, 0, maxU, maxV);
                        tess.addVertexWithUV(1, 0, 1, maxU, minV);
                    }
                    else if(tile.xCoord > tile.getCore().xCoord && tile.zCoord < tile.getCore().zCoord) {
                        tess.addVertexWithUV(0, 0, 0, minU, maxV);
                        tess.addVertexWithUV(0, 1, 1, minU, minV);
                        tess.addVertexWithUV(1, 0, 1, maxU, minV);
                    }
                    else if(tile.xCoord > tile.getCore().xCoord && tile.zCoord > tile.getCore().zCoord) {
                        tess.addVertexWithUV(0, 0, 1, minU, minV);
                        tess.addVertexWithUV(0, 1, 0, minU, maxV);
                        tess.addVertexWithUV(1, 0, 0, maxU, maxV);
                    }
                    tess.draw();
                    tess.startDrawingQuads();
                }
                tess.addTranslation(-x, -y, -z);
            } else if(tile.yCoord == tile.getCore().yCoord + 2) {
                renderer.setRenderBounds(0.3, 0, 0.3, 0.7, 1, 0.7);
                renderer.renderStandardBlock(block, x, y, z);
            }
        }
        else {
            renderer.setRenderBounds(0, 0, 0, 1, 1, 1);
            renderer.renderStandardBlock(block, x, y, z);
        }
        return true;
    }

    @Override
    public boolean shouldRender3DInInventory(int modelId) {
        return true;
    }

    @Override
    public int getRenderId() {
        return dummyRenderID;
    }
}
