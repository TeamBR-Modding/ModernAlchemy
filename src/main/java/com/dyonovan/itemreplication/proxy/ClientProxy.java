package com.dyonovan.itemreplication.proxy;

import com.dyonovan.itemreplication.handlers.BlockHandler;
import com.dyonovan.itemreplication.renderer.ItemRenderTeslaBase;
import com.dyonovan.itemreplication.renderer.RenderTeslaBase;
import com.dyonovan.itemreplication.tileentity.TETeslaBase;
import cpw.mods.fml.client.registry.ClientRegistry;
import net.minecraft.client.renderer.tileentity.TileEntitySpecialRenderer;
import net.minecraft.item.Item;
import net.minecraftforge.client.MinecraftForgeClient;

public class ClientProxy extends  CommonProxy{

    public void registerRenderer() {
        //TeslaBase
        TileEntitySpecialRenderer render = new RenderTeslaBase();
        ClientRegistry.bindTileEntitySpecialRenderer(TETeslaBase.class, render);
        MinecraftForgeClient.registerItemRenderer(Item.getItemFromBlock(BlockHandler.blockTeslaBase),
                new ItemRenderTeslaBase(render, new TETeslaBase()));
    }
}
