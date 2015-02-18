package com.dyonovan.modernalchemy.proxy;

import com.dyonovan.modernalchemy.entities.EntityLaserNode;
import com.dyonovan.modernalchemy.handlers.BlockHandler;
import com.dyonovan.modernalchemy.renderer.replicator.ItemRenderFrame;
import com.dyonovan.modernalchemy.renderer.replicator.RenderFrame;
import com.dyonovan.modernalchemy.renderer.replicator.RenderLaserNode;
import com.dyonovan.modernalchemy.renderer.replicator.RenderReplicatorStand;
import com.dyonovan.modernalchemy.renderer.teslacoil.*;
import com.dyonovan.modernalchemy.tileentity.replicator.TileReplicatorFrame;
import com.dyonovan.modernalchemy.tileentity.replicator.TileReplicatorStand;
import com.dyonovan.modernalchemy.tileentity.teslacoil.TileTeslaBase;
import com.dyonovan.modernalchemy.tileentity.teslacoil.TileTeslaCoil;
import com.dyonovan.modernalchemy.tileentity.teslacoil.TileTeslaStand;
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
                new ItemRenderTeslaBase());

        //Frame
        TileEntitySpecialRenderer renderFrame = new RenderFrame();
        ClientRegistry.bindTileEntitySpecialRenderer(TileReplicatorFrame.class, renderFrame);
        MinecraftForgeClient.registerItemRenderer(Item.getItemFromBlock(BlockHandler.blockReplicatorFrame),
                new ItemRenderFrame(renderFrame, new TileReplicatorFrame()));

        //Laser Node
        RenderingRegistry.registerEntityRenderingHandler(EntityLaserNode.class, new RenderLaserNode());

        //Stand
        TileEntitySpecialRenderer renderReplicatorStand = new RenderReplicatorStand();
        ClientRegistry.bindTileEntitySpecialRenderer(TileReplicatorStand.class, renderReplicatorStand);
        MinecraftForgeClient.registerItemRenderer(Item.getItemFromBlock(BlockHandler.blockReplicatorStand),
                new ItemRenderFrame(renderReplicatorStand, new TileReplicatorStand()));
    }
}
