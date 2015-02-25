package com.dyonovan.modernalchemy.manual.page;

import com.dyonovan.modernalchemy.lib.Constants;
import com.dyonovan.modernalchemy.manual.component.*;
import net.minecraft.util.ResourceLocation;

public class MainPage extends BasePage {

    public MainPage() {
        super("Main Page");
        setTitle("Modern Alchemy");
        title.setAlignment(IComponent.ALIGNMENT.CENTER);
        addComponent(new ComponentTextBox("Welcome to Modern Alchemy!"));
        addComponent(new ComponentSpace(15));
        addComponent(new ComponentImage(108, 70, new ResourceLocation(Constants.MODID + ":textures/gui/manual/mainImage.png")));
        addComponent(new ComponentSpace(15));
        addComponent(new ComponentHeader("Mod Info"));
        addComponent(new ComponentTextBox("Create items with lighting and radioactive material. A mid to late game mod focusing on replicating items in a fun, yet challenging way. With empasis on realism this mod will make you think not just craft."));
    }
}
