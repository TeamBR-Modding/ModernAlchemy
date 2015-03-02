package com.dyonovan.modernalchemy.manual.pages;

import com.dyonovan.modernalchemy.gui.BaseGui;
import com.dyonovan.modernalchemy.lib.Constants;
import com.dyonovan.modernalchemy.manual.ManualRegistry;
import com.dyonovan.modernalchemy.manual.component.ComponentHeader;
import com.dyonovan.modernalchemy.manual.component.ComponentSet;
import com.dyonovan.modernalchemy.manual.component.IComponent;
import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;
import net.minecraft.client.Minecraft;
import net.minecraft.client.audio.PositionedSoundRecord;
import net.minecraft.client.audio.SoundHandler;
import net.minecraft.client.gui.GuiButton;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.client.event.GuiScreenEvent;
import net.minecraftforge.common.MinecraftForge;
import org.lwjgl.opengl.GL11;

import java.util.ArrayList;
import java.util.List;

@SideOnly(Side.CLIENT)
public class GuiManual extends BaseGui implements Comparable<GuiManual> {

    public static int inputDelay = 0;
    /**
     * The list of components on the pages
     */
    public List<ComponentSet> pages;

    /**
     * The Title of the pages
     */
    public ComponentHeader title = null;
    protected String id;

    public int currentIndex = 0;

    public GuiManual(String pageId) {
        super(new ContainerPage());
        title = new ComponentHeader("");
        title.setPositionAndSize(15, 15, 100, 15);
        title.setAlignment(IComponent.ALIGNMENT.CENTER);
        pages = new ArrayList<ComponentSet>();
        pages.add(new ComponentSet());
        pages.get(0).add(title);
        this.xSize = 256;
        this.ySize = 170;
        id = pageId;
    }

    public String getID() {
        return id;
    }

    @Override
    public void initGui() {
        updateScale();
        this.buttonList.add(new GuiButtonManual(0, -1000, -1000, 0));
        this.buttonList.add(new GuiButtonManual(1, -1000, -1000, 1));
        this.buttonList.add(new GuiButtonManual(2, -1000, -1000, 2));
        if(!ManualRegistry.instance.isAtRoot())
            this.buttonList.set(2, new GuiButtonManual(2, guiLeft + 6, guiTop + 5, 2));

        if(currentIndex == 0 && pages.size() > 1)
            this.buttonList.set(0, new GuiButtonManual(0, guiLeft + 232, guiTop + 150, 0));
        else if(currentIndex > 0 && pages.size() > currentIndex + 1) {
            this.buttonList.set(0, new GuiButtonManual(0, guiLeft + 232, guiTop + 150, 0));
            this.buttonList.set(1, new GuiButtonManual(1, guiLeft + 6, guiTop + 150, 1));
        }
        else if(currentIndex > 0)
            this.buttonList.set(1, new GuiButtonManual(1, guiLeft + 6, guiTop + 150, 1));
    }

    /**
     * Add a new section to the pages
     * @param component The component to add
     */
    public void addComponent(IComponent component, int page) {
        pages.get(page).add(component);
    }

    /**
     * Sets the title for this pages
     * @param string Title string
     */
    public void setTitle(String string) {
        title.setTitle(string);
    }

    /**
     * Retrieve the title for the pages
     * @return Page Title
     */
    public String getTitle() {
        return title.getTitle();
    }

    @Override
    public void drawScreen(int mouseX, int mouseY, float par3) {
        super.drawScreen(mouseX, mouseY, par3);
        updateScale();
        for(int i = 0; i < pages.get(currentIndex).size(); i++) {
            pages.get(currentIndex).get(i).drawComponent(guiLeft, guiTop, mouseX, mouseY);
        }
        inputDelay--;
    }

    @Override
    protected void drawGuiContainerForegroundLayer(int mouseX, int mouseY)
    {
        super.drawGuiContainerForegroundLayer(mouseX, mouseY);
    }

    @Override
    public void mouseClicked(int x, int y, int button) {
        for(Object button1 : buttonList) {
            GuiButton guiButton = (GuiButton)button1;
            if(x >= guiButton.xPosition && x <= guiButton.xPosition + guiButton.width && y >= guiButton.yPosition && y <= guiButton.yPosition + guiButton.height) {
                GuiScreenEvent.ActionPerformedEvent.Pre event = new GuiScreenEvent.ActionPerformedEvent.Pre(this, guiButton, this.buttonList);
                if (MinecraftForge.EVENT_BUS.post(event))
                    break;
                event.button.func_146113_a(this.mc.getSoundHandler());
                this.actionPerformed(event.button);
                if (this.equals(this.mc.currentScreen))
                    MinecraftForge.EVENT_BUS.post(new GuiScreenEvent.ActionPerformedEvent.Post(this, event.button, this.buttonList));

                return;
            }
        }
    }

    @Override
    protected void actionPerformed(GuiButton button) {
        if(button.id == 0) {
            currentIndex++;
            this.buttonList.set(1, new GuiButtonManual(1, guiLeft + 6, guiTop + 150, 1));
            if(pages.size() > currentIndex + 1)
                this.buttonList.set(0, new GuiButtonManual(0, guiLeft + 232, guiTop + 150, 0));
            else
                this.buttonList.set(0, new GuiButtonManual(1, -1000, -1000, 1));
        }
        else if(button.id == 1) {
            currentIndex--;
            if(currentIndex == 0)
                this.buttonList.set(1, new GuiButtonManual(1, -1000, -1000, 1));
            if(pages.get(currentIndex + 1) != null)
                this.buttonList.set(0, new GuiButtonManual(0, guiLeft + 232, guiTop + 150, 0));
        }
        else if(button.id == 2) {
            ManualRegistry.instance.deleteLastPage();
            ManualRegistry.instance.openManual();
            if(ManualRegistry.instance.isAtRoot())
                this.buttonList.set(2, new GuiButtonManual(2, -1000, -1000, 2));
        }
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

    public static void playClickSound()
    {
        Minecraft.getMinecraft().getSoundHandler().playSound(PositionedSoundRecord.func_147674_a(new ResourceLocation("gui.button.press"), 1.0F));
    }

    @Override
    public int compareTo(GuiManual o) {
        return this.id.equalsIgnoreCase(o.id) ? 0 : 1;
    }
}
