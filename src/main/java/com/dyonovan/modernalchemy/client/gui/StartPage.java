package com.dyonovan.modernalchemy.client.gui;

import com.dyonovan.modernalchemy.lib.Constants;
import net.minecraft.util.IIcon;
import net.minecraft.util.ResourceLocation;
import openmods.gui.component.GuiComponentSprite;
import openmods.gui.component.page.PageBase;
import openmods.utils.render.FakeIcon;

public class StartPage extends PageBase {
    private GuiComponentSprite image;

    private static final ResourceLocation texture = new ResourceLocation(Constants.MODID, "textures/gui/bookimage.png");
    public static IIcon iconImage = new FakeIcon(0, 0.7421875f, 0, 0.546875f, 95, 70);

    public StartPage() {
        image = new GuiComponentSprite((getWidth() - iconImage.getIconWidth()) / 2, (getHeight() - iconImage.getIconHeight()) / 2, iconImage, texture);
        addComponent(image);
    }
}
