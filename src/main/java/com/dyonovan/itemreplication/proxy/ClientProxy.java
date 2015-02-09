package com.dyonovan.itemreplication.proxy;

import com.dyonovan.itemreplication.handlers.BlockHandler;
import com.dyonovan.itemreplication.renderer.ItemRenderTeslaStand;
import com.dyonovan.itemreplication.renderer.RenderTeslaCoil;
import com.dyonovan.itemreplication.renderer.RenderTeslaStand;
import com.dyonovan.itemreplication.tileentity.TileTeslaCoil;
import com.dyonovan.itemreplication.tileentity.TileTeslaStand;
import cpw.mods.fml.client.registry.ClientRegistry;
import net.minecraft.client.renderer.tileentity.TileEntitySpecialRenderer;
import net.minecraft.item.Item;
import net.minecraftforge.client.MinecraftForgeClient;

public class ClientProxy extends  CommonProxy{

    public void registerRenderer() {

        //TeslaStand
        TileEntitySpecialRenderer renderStand = new RenderTeslaStand();
        ClientRegistry.bindTileEntitySpecialRenderer(TileTeslaStand.class, renderStand);
        MinecraftForgeClient.registerItemRenderer(Item.getItemFromBlock(BlockHandler.blockTeslaStand),
                new ItemRenderTeslaStand(renderStand, new TileTeslaStand()));

        //TeslaCoil
        TileEntitySpecialRenderer renderCoil = new RenderTeslaCoil();
        ClientRegistry.bindTileEntitySpecialRenderer(TileTeslaCoil.class, renderCoil);
        //MinecraftForgeClient.registerItemRenderer(Item.getItemFromBlock(BlockHandler.blockCoil),
                //new ItemRenderTeslaCoil(renderCoil, new TileTeslaCoil()));
    }
}
