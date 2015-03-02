package com.dyonovan.modernalchemy.gui.buttons;

import com.dyonovan.modernalchemy.lib.Constants;
import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.GuiButton;
import net.minecraft.util.ResourceLocation;
import org.lwjgl.opengl.GL11;

@SideOnly(Side.CLIENT)
public class MultiStateButton extends GuiButton {

    int states;
    int currentState = 0;
    String resource;

    /**
     * Makes a button with provided states
     * @param index The button index
     * @param x xPos
     * @param y yPos
     * @param width button width
     * @param height button height
     * @param statesNum how many states
     * @param string Resource location (don't worry about mod id, just "textures/gui/...")
     */
    public MultiStateButton(int index, int x, int y, int width, int height, int statesNum, String string) {
        super(index, x, y, "");
        this.width = width;
        this.height = height;
        states = statesNum;
        resource = string;
    }

    /**
     * Increments the icon 1 state
     */
    public void cycleIcon() {
        currentState++;
        if(currentState >= states)
            currentState = 0;
    }

    /**
     * Set the state of the button
     * @param i What state (should probably be less than the max)
     */
    public void setCurrentState(int i) {
        currentState = i;
    }

    /**
     * Get what state the button is in
     * @return the int representation of the current state
     */
    public int getState() {
        return currentState;
    }

    public void drawButton(Minecraft par1Minecraft, int par2, int par3)
    {
        super.drawButton(par1Minecraft, par2, par3);
        if (this.visible)
        {
            par1Minecraft.getTextureManager().bindTexture(new ResourceLocation(Constants.MODID, resource));
            GL11.glColor4f(1.0F, 1.0F, 1.0F, 1.0F);
            boolean flag = par2 >= this.xPosition && par3 >= this.yPosition && par2 < this.xPosition + this.width && par3 < this.yPosition + this.height;

            int u = currentState * width;
            int v = 0;
            if (flag)
            {
                v += this.height;

            }
            this.drawTexturedModalRect(this.xPosition, this.yPosition, u, v, this.width, this.height);
        }
    }
}
