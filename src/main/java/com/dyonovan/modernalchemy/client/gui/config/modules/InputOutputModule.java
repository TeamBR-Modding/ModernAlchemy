package com.dyonovan.modernalchemy.client.gui.config.modules;

import com.dyonovan.modernalchemy.handlers.BlockHandler;
import com.dyonovan.modernalchemy.lib.Constants;
import com.dyonovan.modernalchemy.common.tileentity.IInputOutput;
import com.dyonovan.modernalchemy.helpers.GuiHelper;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.FontRenderer;
import net.minecraft.client.gui.GuiScreen;
import net.minecraft.client.renderer.ItemRenderer;
import net.minecraft.client.renderer.RenderHelper;
import net.minecraft.item.ItemStack;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.client.IItemRenderer;
import org.lwjgl.opengl.GL11;

public class InputOutputModule extends GuiScreen implements IModule {
    protected static ItemRenderer itemRender = new ItemRenderer(Minecraft.getMinecraft());

    private IInputOutput tile;
    int xSize;
    int ySize;
    int xPos;
    int yPos;
    public static ResourceLocation background = new ResourceLocation(Constants.MODID + ":textures/gui/ioConfigure.png");

    public InputOutputModule(IInputOutput controller, int x, int y) {
        this.tile = controller;
        xSize = 109;
        ySize = 116;
        xPos = x;
        yPos = y;
    }

    @Override
    public void drawGuiContainerForegroundLayer(FontRenderer fontRenderer, int par1, int par2) {
        GL11.glPushMatrix();
        GL11.glScaled(0.8, 0.85, 0.8);
        fontRenderer.drawString(GuiHelper.GuiColor.BLACK + "I/O Control", 85 - (fontRenderer.getStringWidth("I/O Control") / 2), yPos + 14, 4210752);
        GL11.glPopMatrix();
    }

    @Override
    public void drawGuiContainerBackgroundLayer(int x, int y, float f, int i, int j) {
        GL11.glColor4f(1.0F, 1.0F, 1.0F, 1.0F);
        Minecraft.getMinecraft().renderEngine.bindTexture(background);
        drawTexturedModalRect(x + xPos, y + yPos, 0, 0, xSize, ySize);

        GL11.glPushMatrix();
        RenderHelper.enableGUIStandardItemLighting();
        GL11.glColor4f(1.0F, 1.0F, 1.0F, 1.0F);
        GL11.glTranslated(x + xPos, y + yPos, 100);
        GL11.glDisable(GL11.GL_CULL_FACE);

        GL11.glRotated(150, 1.0, 0.0, 0.0);
        GL11.glRotated(-135, 0.0, 1.0, 0.0);
        GL11.glScaled(50, 50, 50);

        ItemStack stack = new ItemStack(BlockHandler.blockAdvancedCrafter);
        itemRender.renderItem(Minecraft.getMinecraft().thePlayer, stack, stack.getItemDamage(), IItemRenderer.ItemRenderType.INVENTORY);

        GL11.glPopMatrix();
    }
}
