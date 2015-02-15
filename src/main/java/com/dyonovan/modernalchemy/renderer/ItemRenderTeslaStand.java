package com.dyonovan.modernalchemy.renderer;

import com.dyonovan.modernalchemy.lib.Constants;
import com.dyonovan.modernalchemy.model.ModelTeslaStand;
import cpw.mods.fml.client.FMLClientHandler;
import net.minecraft.item.ItemStack;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.client.IItemRenderer;
import org.lwjgl.opengl.GL11;

public class ItemRenderTeslaStand implements IItemRenderer {

    private ModelTeslaStand model;

    public ItemRenderTeslaStand() {
        this.model = new ModelTeslaStand();
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
            renderCoil(0.0F, 0.5F, 0.0F, item.getItemDamage());
            break;
        }
        case EQUIPPED: {
            GL11.glScalef(2.0F, 2.0F, 2.0F);
            GL11.glRotatef(-45, 1.0F, 0.0F, 0.0F);
            GL11.glRotatef(45, 0.0F, 0.0F, 1.0F);
            renderCoil(0.2F, 0.2F, 0.5F, item.getItemDamage());
            break;
        }
        case EQUIPPED_FIRST_PERSON: {
            GL11.glScalef(1.5F, 1.5F, 1.5F);
            renderCoil(1.0F, 0.6F, 0.6F, item.getItemDamage());
            break;
        }
        case INVENTORY: {
            GL11.glScalef(1.35F, 1.35F, 1.35F);
            renderCoil(0.0F, -0.0F, 0.0F, item.getItemDamage());
            break;
        }
        default:
            break;
        }
    }

    public void renderCoil(float x, float y, float z, int metaData)
    {
        FMLClientHandler.instance().getClient().renderEngine.bindTexture(new ResourceLocation(Constants.MODID + ":textures/models/teslaStand.png"));

        GL11.glPushMatrix(); //start
        GL11.glTranslatef(x, y, z); //size
        model.render();
        GL11.glPopMatrix(); //end
    }
}

