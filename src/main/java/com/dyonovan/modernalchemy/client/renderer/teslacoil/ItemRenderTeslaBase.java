package com.dyonovan.modernalchemy.client.renderer.teslacoil;

import com.dyonovan.modernalchemy.client.gui.GuiManual;
import com.dyonovan.modernalchemy.lib.Constants;
import com.dyonovan.modernalchemy.client.model.teslacoil.ModelTeslaBase;
import cpw.mods.fml.client.FMLClientHandler;
import net.minecraft.client.Minecraft;
import net.minecraft.item.ItemStack;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.client.IItemRenderer;
import org.lwjgl.opengl.GL11;

public class ItemRenderTeslaBase implements IItemRenderer {

    private ModelTeslaBase base;

    public ItemRenderTeslaBase() {
        base = new ModelTeslaBase();
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
                renderBase(0.0F, 0.5F, 0.0F, item.getItemDamage());
                break;
            }
            case EQUIPPED: {
                if(Minecraft.getMinecraft().currentScreen == null || !(Minecraft.getMinecraft().currentScreen instanceof GuiManual)) {
                    GL11.glScalef(2.0F, 2.0F, 2.0F);
                    renderBase(0.2F, 0.4F, 0.0F, item.getItemDamage());
                } else
                    renderBase(0.0F, 0.5F, 0.5F, item.getItemDamage());
                break;
            }
            case EQUIPPED_FIRST_PERSON: {
                GL11.glScalef(1.5F, 1.5F, 1.5F);
                renderBase(1.0F, 0.6F, 0.6F, item.getItemDamage());
                break;
            }
            case INVENTORY: {
                renderBase(0.0F, 0.03F, 0.0F, item.getItemDamage());
                break;
            }
            default:
                break;
        }
    }

    public void renderBase(float x, float y, float z, int metaData)
    {
        FMLClientHandler.instance().getClient().renderEngine.bindTexture(new ResourceLocation(Constants.MODID + ":textures/models/teslaBase.png"));

        GL11.glPushMatrix(); //start
        GL11.glTranslatef(x, y, z); //size
        GL11.glDisable(GL11.GL_CULL_FACE);

        base.renderMain();

        GL11.glEnable(GL11.GL_CULL_FACE);

        GL11.glPopMatrix(); //end
    }
}
