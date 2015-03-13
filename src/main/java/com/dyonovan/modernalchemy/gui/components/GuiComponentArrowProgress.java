package com.dyonovan.modernalchemy.gui.components;

import com.dyonovan.modernalchemy.util.RenderUtils;
import net.minecraft.client.Minecraft;
import openmods.api.IValueReceiver;
import openmods.gui.component.BaseComponent;
import org.lwjgl.opengl.GL11;

public class GuiComponentArrowProgress extends BaseComponent {

    private int progress;
    private float scale;

    public GuiComponentArrowProgress(int x, int y, int maxProgress) {
        super(x, y);
        setMaxProgress(maxProgress);
    }

    @Override
    public void render(Minecraft minecraft, int offsetX, int offsetY, int mouseX, int mouseY) {
        RenderUtils.bindTextureSheet();
        GL11.glColor3f(1, 1, 1);
        drawTexturedModalRect(offsetX + x, offsetY + y, 0, 0, getWidth(), getHeight());
        int pxProgress = Math.round(progress * scale);
        drawTexturedModalRect(offsetX + x, offsetY + y, 0, 17, pxProgress, getHeight());
    }

    @Override
    public void renderOverlay(Minecraft minecraft, int offsetX, int offsetY, int mouseX, int mouseY) {}

    @Override
    public int getWidth() {
        return 23;
    }

    @Override
    public int getHeight() {
        return 16;
    }

    public void setProgress(int progress) {
        this.progress = progress;
    }

    public void setMaxProgress(int maxProgress) {
        this.scale = (float)getWidth() / maxProgress;
    }

    public IValueReceiver<Integer> progressReceiver() {
        return new IValueReceiver<Integer>() {
            @Override
            public void setValue(Integer value) {
                progress = value;
            }
        };
    }

    public IValueReceiver<Integer> maxProgressReceiver() {
        return new IValueReceiver<Integer>() {
            @Override
            public void setValue(Integer value) {
                setMaxProgress(value);
            }
        };
    }
}
