package com.dyonovan.modernalchemy.client.gui.buttons;

import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.FontRenderer;
import net.minecraft.client.gui.GuiButton;
import net.minecraft.client.renderer.OpenGlHelper;
import net.minecraft.client.renderer.RenderHelper;
import net.minecraft.client.renderer.entity.RenderItem;
import net.minecraft.item.ItemStack;
import org.lwjgl.opengl.GL11;
import org.lwjgl.opengl.GL12;

import java.util.ArrayList;
import java.util.List;

public class ItemStackButton extends GuiButton {
    private ItemStack stack;
    protected static RenderItem itemRender = new RenderItem();
    public ItemStackButton(int index, int x, int y, ItemStack stack) {
        super(index, x, y, 20, 20, "");
        this.stack = stack;
    }

    @Override
    public void drawButton(Minecraft mc, int x, int y)
    {
        super.drawButton(mc, x, y);
        GL11.glPushMatrix();
        RenderHelper.enableGUIStandardItemLighting();
        GL11.glDisable(GL12.GL_RESCALE_NORMAL);
        GL11.glDisable(GL11.GL_DEPTH_TEST);
        short short1 = 240;
        short short2 = 240;
        OpenGlHelper.setLightmapTextureCoords(OpenGlHelper.lightmapTexUnit, (float) short1 / 1.0F, (float) short2 / 1.0F);
        GL11.glColor4f(1.0F, 1.0F, 1.0F, 1.0F);

        this.zLevel = 200.0F;
        itemRender.zLevel = 200.0F;
        FontRenderer font = Minecraft.getMinecraft().fontRenderer;
        itemRender.renderItemIntoGUI(font, Minecraft.getMinecraft().getTextureManager(), stack, xPosition + 2, yPosition + 2);
        this.zLevel = 0.0F;
        itemRender.zLevel = 0.0F;

        GL11.glPopMatrix();

        List<String> strings = new ArrayList<String>();
        strings.add("test");
        this.field_146123_n = x >= this.xPosition && y >= this.yPosition && x < this.xPosition + this.width && y < this.yPosition + this.height;
        //TODO: Tooltip
    }

}
