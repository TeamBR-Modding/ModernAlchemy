package com.dyonovan.modernalchemy.renderer.machines;

import com.dyonovan.modernalchemy.blocks.misc.BlockTank;
import com.dyonovan.modernalchemy.handlers.BlockHandler;
import com.dyonovan.modernalchemy.helpers.RenderHelper;
import com.dyonovan.modernalchemy.renderer.DisplayListWrapper;
import com.dyonovan.modernalchemy.tileentity.misc.TileTank;
import com.dyonovan.modernalchemy.util.RenderUtils;
import cpw.mods.fml.client.registry.ISimpleBlockRenderingHandler;
import cpw.mods.fml.client.registry.RenderingRegistry;
import net.minecraft.block.Block;
import net.minecraft.client.renderer.RenderBlocks;
import net.minecraft.client.renderer.Tessellator;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.IIcon;
import net.minecraft.world.IBlockAccess;
import net.minecraftforge.common.util.ForgeDirection;
import net.minecraftforge.fluids.Fluid;
import net.minecraftforge.fluids.FluidStack;
import org.lwjgl.opengl.GL11;

public class BlockTankRenderer implements ISimpleBlockRenderingHandler
{
    public static int tankModelID = RenderingRegistry.getNextAvailableRenderId();
    public static int renderPass = 0;
    public static final DisplayListWrapper EMPTY_FRAME = new DisplayListWrapper() {

        @Override
        public void compile() {
            GL11.glColor3f(1, 1, 1);
            GL11.glDisable(GL11.GL_TEXTURE_2D);

            Tessellator tes = new Tessellator();
            tes.startDrawingQuads();
            tes.setColorOpaque(0, 0, 0);
            tes.setTextureUV(0, 0);
            BlockTankRenderer.render(tes, TileTank.NO_NEIGHBOURS, 0, 0, 0);
            tes.draw();

            GL11.glEnable(GL11.GL_TEXTURE_2D);
        }
    };

    @Override
    public void renderInventoryBlock (Block block, int metadata, int modelID, RenderBlocks renderer) {
        EMPTY_FRAME.compile();
    }

    @Override
    public boolean renderWorldBlock (IBlockAccess world, int x, int y, int z, Block block, int modelID, RenderBlocks renderer)
    {
        if (modelID == tankModelID)
        {
            TileTank logic = (TileTank) world.getTileEntity(x, y, z);
            //Liquid
            if (renderPass == 0)
            {
                if (logic.containsFluid())
                {
                    FluidStack liquid = logic.tank.getFluid();
                    renderer.setRenderBounds(0.001, 0.001, 0.001, 0.999, Math.max(0.01, logic.getFluidAmountScaled()), 0.999);
                    Fluid fluid = liquid.getFluid();
                    RenderHelper.renderLiquidBlock(fluid.getStillIcon(), fluid.getStillIcon(), x, y, z, renderer, world, true);
                    renderer.setRenderBounds(0, 0.001, 0.001, 0.999, logic.getFluidAmountScaled(), 0.999);
                }

                if(logic.isFilling()) {
                    if (logic.getDirectionFillingFrom() == ForgeDirection.UP) {
                        Fluid fluid = logic.getFillingLiquid();
                        renderer.setRenderBounds(0.5 - logic.getTransferAmountScaled(), 0.001, 0.5 - logic.getTransferAmountScaled(), 0.5 + logic.getTransferAmountScaled(), 0.999, 0.5 + logic.getTransferAmountScaled());
                        RenderHelper.renderLiquidBlock(fluid.getStillIcon(), fluid.getStillIcon(), x, y, z, renderer, world, true);
                    }
                    else if(logic.renderOffset > 0 && (logic.getDirectionFillingFrom() == ForgeDirection.DOWN || logic.getDirectionFillingFrom() == ForgeDirection.UNKNOWN)){
                        Fluid fluid = logic.getFillingLiquid();
                        double renderMax = 0;
                        if(logic.containsFluid())
                            renderMax= logic.getFluidAmountScaled() + 0.1;
                        if(renderMax > 0.999)
                            renderMax = 0.999;
                        renderer.setRenderBounds(0.5 - logic.getTransferAmountScaled(), 0.001, 0.5 - logic.getTransferAmountScaled(), 0.5 + logic.getTransferAmountScaled(), renderMax, 0.5 + logic.getTransferAmountScaled());
                        RenderHelper.renderLiquidBlock(fluid.getStillIcon(), fluid.getStillIcon(), x, y, z, renderer, world, true);
                    }
                }
            }

            //Block
            else
            {
                if(logic.isLocked()) {
                    if(!(logic.getTileInDirection(ForgeDirection.UP) instanceof TileTank))
                        renderer.renderFaceYPos(BlockHandler.blockTank, x, y, z, BlockTank.locked);
                    if(!(logic.getTileInDirection(ForgeDirection.DOWN) instanceof TileTank))
                    renderer.renderFaceYNeg(BlockHandler.blockTank, x, y, z, BlockTank.locked);
                    if(!(logic.getTileInDirection(ForgeDirection.NORTH) instanceof TileTank))
                        renderer.renderFaceZNeg(BlockHandler.blockTank, x, y, z, BlockTank.locked);
                    if(!(logic.getTileInDirection(ForgeDirection.SOUTH) instanceof TileTank))
                        renderer.renderFaceZPos(BlockHandler.blockTank, x, y, z, BlockTank.locked);
                    if(!(logic.getTileInDirection(ForgeDirection.EAST) instanceof TileTank))
                        renderer.renderFaceXPos(BlockHandler.blockTank, x, y, z, BlockTank.locked);
                    if(!(logic.getTileInDirection(ForgeDirection.WEST) instanceof TileTank))
                        renderer.renderFaceXNeg(BlockHandler.blockTank, x, y, z, BlockTank.locked);
                }

                TileEntity te = world.getTileEntity(x, y, z);
                TileTank.IRenderNeighbours connections = (te instanceof TileTank) ? ((TileTank)te).getRenderConnections() : TileTank.NO_NEIGHBOURS;
                final IIcon icon = ((BlockTank)block).getIcon();
                Tessellator.instance.setTextureUV(icon.getMinU(), icon.getMinV());
                Tessellator.instance.setColorOpaque(255, 255, 255);
                render(Tessellator.instance, connections, x, y, z);
            }
        }
        return true;
    }

    private static final double H = 0.015;

    private static boolean shouldRenderEdgeT(TileTank.IRenderNeighbours connections, int d1, int d2) {
        final boolean n1 = connections.hasDirectNeighbour(d1);
        final boolean n2 = connections.hasDirectNeighbour(d2);
        final boolean n12 = connections.hasDiagonalNeighbour(d1, d2);
        return (n1 == n2) & !n12;
    }

    private static boolean shouldRenderEdgeB(TileTank.IRenderNeighbours connections, int d1, int d2) {
        final boolean n1 = connections.hasDirectNeighbour(d1);
        final boolean n2 = connections.hasDirectNeighbour(d2);
        final boolean n12 = connections.hasDiagonalNeighbour(d1, d2);
        return (!n2 & !n1) | (n2 & !n12 & n1);
    }

    private static void render(Tessellator tes, TileTank.IRenderNeighbours connections, double x, double y, double z) {
        if (shouldRenderEdgeT(connections, TileTank.DIR_SOUTH, TileTank.DIR_DOWN)) RenderUtils.renderCube(tes, x - H + 0, y - H + 0, z - H + 1, x + H + 1, y + H + 0, z + H + 1);
        if (shouldRenderEdgeT(connections, TileTank.DIR_NORTH, TileTank.DIR_DOWN)) RenderUtils.renderCube(tes, x - H + 0, y - H + 0, z - H + 0, x + H + 1, y + H + 0, z + H + 0);
        if (shouldRenderEdgeB(connections, TileTank.DIR_SOUTH, TileTank.DIR_UP)) RenderUtils.renderCube(tes, x - H + 0, y - H + 1, z - H + 1, x + H + 1, y + H + 1, z + H + 1);
        if (shouldRenderEdgeB(connections, TileTank.DIR_NORTH, TileTank.DIR_UP)) RenderUtils.renderCube(tes, x - H + 0, y - H + 1, z - H + 0, x + H + 1, y + H + 1, z + H + 0);

        if (shouldRenderEdgeT(connections, TileTank.DIR_EAST, TileTank.DIR_NORTH)) RenderUtils.renderCube(tes, x - H + 1, y - H + 0, z - H + 0, x + H + 1, y + H + 1, z + H + 0);
        if (shouldRenderEdgeT(connections, TileTank.DIR_WEST, TileTank.DIR_NORTH)) RenderUtils.renderCube(tes, x - H + 0, y - H + 0, z - H + 0, x + H + 0, y + H + 1, z + H + 0);
        if (shouldRenderEdgeB(connections, TileTank.DIR_EAST, TileTank.DIR_SOUTH)) RenderUtils.renderCube(tes, x - H + 1, y - H + 0, z - H + 1, x + H + 1, y + H + 1, z + H + 1);
        if (shouldRenderEdgeB(connections, TileTank.DIR_WEST, TileTank.DIR_SOUTH)) RenderUtils.renderCube(tes, x - H + 0, y - H + 0, z - H + 1, x + H + 0, y + H + 1, z + H + 1);

        if (shouldRenderEdgeT(connections, TileTank.DIR_EAST, TileTank.DIR_DOWN)) RenderUtils.renderCube(tes, x - H + 1, y - H + 0, z - H + 0, x + H + 1, y + H + 0, z + H + 1);
        if (shouldRenderEdgeT(connections, TileTank.DIR_WEST, TileTank.DIR_DOWN)) RenderUtils.renderCube(tes, x - H + 0, y - H + 0, z - H + 0, x + H + 0, y + H + 0, z + H + 1);
        if (shouldRenderEdgeB(connections, TileTank.DIR_EAST, TileTank.DIR_UP)) RenderUtils.renderCube(tes, x - H + 1, y - H + 1, z - H + 0, x + H + 1, y + H + 1, z + H + 1);
        if (shouldRenderEdgeB(connections, TileTank.DIR_WEST, TileTank.DIR_UP)) RenderUtils.renderCube(tes, x - H + 0, y - H + 1, z - H + 0, x + H + 0, y + H + 1, z + H + 1);
    }

    @Override
    public boolean shouldRender3DInInventory (int modelID)
    {
        return true;
    }

    @Override
    public int getRenderId ()
    {
        return tankModelID;
    }
}
