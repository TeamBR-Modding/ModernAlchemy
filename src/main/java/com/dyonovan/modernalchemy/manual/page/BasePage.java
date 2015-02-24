package com.dyonovan.modernalchemy.manual.page;

import com.dyonovan.modernalchemy.gui.BaseGui;
import com.dyonovan.modernalchemy.lib.Constants;
import com.dyonovan.modernalchemy.manual.component.ComponentTitle;
import com.dyonovan.modernalchemy.manual.component.IComponent;
import net.minecraft.util.ResourceLocation;
import org.lwjgl.opengl.GL11;

import java.util.ArrayList;
import java.util.List;

public class BasePage extends BaseGui {

    /**
     * The list of components on the page
     */
    public List<IComponent> components;

    /**
     * The Title of the page
     */
    public ComponentTitle title = null;

    public BasePage() {
        super(new ContainerPage());
        title = new ComponentTitle("");
        components = new ArrayList<IComponent>();
        this.xSize = 256;
        this.ySize = 170;
    }

    /**
     * Add a new section to the page
     * @param component The component to add
     */
    public void addComponent(IComponent component) {
        components.add(component);
    }

    /**
     * Sets the title for this page
     * @param string Title string
     */
    public void setTitle(String string) {
        title.setTitle(string);
    }

    /**
     * Retrieve the title for the page
     * @return Page Title
     */
    public String getTitle() {
        return title.getTitle();
    }

    @Override
    public void drawScreen(int mouseX, int mouseY, float par3) {
        super.drawScreen(mouseX, mouseY, par3);
        for(IComponent component : components) {
            component.drawComponent(guiLeft, guiTop, mouseX, mouseY);
        }
        title.drawComponent(guiLeft, guiTop, mouseX, mouseY);
    }

    private ResourceLocation background = new ResourceLocation(Constants.MODID + ":textures/gui/manual/manual.png");

    @Override
    protected void drawGuiContainerBackgroundLayer(float f, int i, int j) {
        GL11.glColor4f(1.0F, 1.0F, 1.0F, 1.0F); //Could do some fun colors and transparency here
        this.mc.renderEngine.bindTexture(background);
        int x = (width - xSize) / 2;
        int y = (height - ySize) / 2;
        drawTexturedModalRect(x, y, 0, 0, xSize, ySize);
    }
}
