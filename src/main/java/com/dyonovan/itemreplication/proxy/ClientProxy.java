package com.dyonovan.itemreplication.proxy;

import com.dyonovan.itemreplication.entities.EntityLaserNode;
import com.dyonovan.itemreplication.handlers.BlockHandler;
import com.dyonovan.itemreplication.renderer.*;
import com.dyonovan.itemreplication.tileentity.*;
import cpw.mods.fml.client.registry.ClientRegistry;
import cpw.mods.fml.client.registry.RenderingRegistry;
import net.minecraft.client.renderer.tileentity.TileEntitySpecialRenderer;
import net.minecraft.item.Item;
import net.minecraftforge.client.MinecraftForgeClient;

public class ClientProxy extends  CommonProxy{

    public void registerRenderer() {

        //TeslaStand
        TileEntitySpecialRenderer renderStand = new RenderTeslaStand();
        ClientRegistry.bindTileEntitySpecialRenderer(TileTeslaStand.class, renderStand);
        MinecraftForgeClient.registerItemRenderer(Item.getItemFromBlock(BlockHandler.blockTeslaStand),
                new ItemRenderTeslaStand());

        //TeslaCoil
        TileEntitySpecialRenderer renderCoil = new RenderTeslaCoil();
        ClientRegistry.bindTileEntitySpecialRenderer(TileTeslaCoil.class, renderCoil);
        MinecraftForgeClient.registerItemRenderer(Item.getItemFromBlock(BlockHandler.blockCoil),
                new ItemRenderTeslaCoil());

        //TeslaBase
        TileEntitySpecialRenderer renderBase = new RenderTeslaBase();
        ClientRegistry.bindTileEntitySpecialRenderer(TileTeslaBase.class, renderBase);
        MinecraftForgeClient.registerItemRenderer(Item.getItemFromBlock(BlockHandler.blockTeslaBase),
                new ItemRenderTeslaBase(renderBase, new TileTeslaBase()));

        //Frame
        TileEntitySpecialRenderer renderFrame = new RenderFrame();
        ClientRegistry.bindTileEntitySpecialRenderer(TileFrame.class, renderFrame);
        MinecraftForgeClient.registerItemRenderer(Item.getItemFromBlock(BlockHandler.blockFrame),
                new ItemRenderFrame(renderFrame, new TileFrame()));

        //Laser Node
        RenderingRegistry.registerEntityRenderingHandler(EntityLaserNode.class, new RenderLaserNode());

        //Stand
        TileEntitySpecialRenderer renderReplicatorStand = new RenderReplicatorStand();
        ClientRegistry.bindTileEntitySpecialRenderer(TileReplicatorStand.class, renderReplicatorStand);
        MinecraftForgeClient.registerItemRenderer(Item.getItemFromBlock(BlockHandler.blockReplicatorStand),
                new ItemRenderFrame(renderReplicatorStand, new TileReplicatorStand()));
    }
}
