package com.dyonovan.itemreplication.renderer;

import com.dyonovan.itemreplication.lib.Constants;
import com.dyonovan.itemreplication.model.ModelTeslaCoil;
import cpw.mods.fml.client.FMLClientHandler;
import net.minecraft.item.ItemStack;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.client.IItemRenderer;
import org.lwjgl.opengl.GL11;

public class ItemRenderTeslaCoil implements IItemRenderer {

    private ModelTeslaCoil coil;

    public ItemRenderTeslaCoil() {
        coil = new ModelTeslaCoil();
    }

    @Override
    public boolean handleRenderType(ItemStack item, ItemRenderType type) {
        return true;
    }

    @Override
    public boolean shouldUseRenderHelper(ItemRenderType type, ItemStack item, ItemRendererHelper helper) {
        return true;
    }

    @Override
    public void renderItem(ItemRenderType type, ItemStack item, Object... data) {
        switch (type) {
        case ENTITY: {
            GL11.glScalef(2.0F, 2.0F, 2.0F);
            renderCoil(0.5F, 0.5F, 0.5F, item.getItemDamage());
            break;
        }
        case EQUIPPED: {
            GL11.glRotatef(-45, 1.0F, 0.0F, 0.0F);
            GL11.glRotatef(45, 0.0F, 0.0F, 1.0F);
            renderCoil(0.5F, -0.25F, 1.0F, item.getItemDamage());
            break;
        }
        case EQUIPPED_FIRST_PERSON: {
            renderCoil(1.0F, 1.0F, 1.0F, item.getItemDamage());
            break;
        }
        case INVENTORY: {
            GL11.glScalef(0.75F, 0.75F, 0.75F);
            renderCoil(0.0F, -0.45F, 0.0F, item.getItemDamage());
            break;
        }
        default:
            break;
        }
    }

    public void renderCoil(float x, float y, float z, int metaData)
    {
        FMLClientHandler.instance().getClient().renderEngine.bindTexture(new ResourceLocation(Constants.MODID + ":textures/models/tesla_coil.png"));

        GL11.glPushMatrix(); //start
        GL11.glTranslatef(x, y, z); //size
        GL11.glRotatef(180, 1, 0, 0);
        GL11.glRotatef(-90, 0, 1, 0);
        coil.renderAll();
        GL11.glPopMatrix(); //end
    }
}
