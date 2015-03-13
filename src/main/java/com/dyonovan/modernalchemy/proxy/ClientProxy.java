package com.dyonovan.modernalchemy.proxy;

import com.dyonovan.modernalchemy.entities.EntityLaserNode;
import com.dyonovan.modernalchemy.handlers.BlockHandler;
import com.dyonovan.modernalchemy.lib.Constants;
import com.dyonovan.modernalchemy.manual.ManualRegistry;
import com.dyonovan.modernalchemy.renderer.replicator.ItemRenderFrame;
import com.dyonovan.modernalchemy.renderer.replicator.RenderFrame;
import com.dyonovan.modernalchemy.renderer.replicator.RenderLaserNode;
import com.dyonovan.modernalchemy.renderer.replicator.RenderReplicatorStand;
import com.dyonovan.modernalchemy.renderer.teslacoil.*;
import com.dyonovan.modernalchemy.tileentity.replicator.TileReplicatorFrame;
import com.dyonovan.modernalchemy.tileentity.replicator.TileReplicatorStand;
import com.dyonovan.modernalchemy.tileentity.teslacoil.TileSuperTeslaCoil;
import com.dyonovan.modernalchemy.tileentity.teslacoil.TileTeslaBase;
import com.dyonovan.modernalchemy.tileentity.teslacoil.TileTeslaCoil;
import com.dyonovan.modernalchemy.tileentity.teslacoil.TileTeslaStand;
import cpw.mods.fml.client.registry.ClientRegistry;
import cpw.mods.fml.client.registry.RenderingRegistry;
import net.minecraft.client.renderer.tileentity.TileEntitySpecialRenderer;
import net.minecraft.item.Item;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.client.MinecraftForgeClient;
import openmods.api.IProxy;
import openmods.proxy.IOpenModsProxy;
import openmods.proxy.OpenClientProxy;
import openmods.renderer.BlockRenderingHandler;

public class ClientProxy extends OpenClientProxy implements IProxy {

    public static int renderId;

    @Override
    public void registerRenderInformation() {

        renderId = RenderingRegistry.getNextAvailableRenderId();
        final BlockRenderingHandler blockRenderingHandler = new BlockRenderingHandler(renderId, true);
        RenderingRegistry.registerBlockHandler(blockRenderingHandler);

        //TeslaStand
        TileEntitySpecialRenderer renderStand = new RenderTeslaStand();
        ClientRegistry.bindTileEntitySpecialRenderer(TileTeslaStand.class, renderStand);
        MinecraftForgeClient.registerItemRenderer(Item.getItemFromBlock(BlockHandler.blockTeslaStand),
                new ItemRenderTeslaStand());

        //TeslaCoil
        TileEntitySpecialRenderer renderCoil = new RenderTeslaCoil(new ResourceLocation(Constants.MODID + ":textures/models/teslaCoil.png"));
        ClientRegistry.bindTileEntitySpecialRenderer(TileTeslaCoil.class, renderCoil);
        MinecraftForgeClient.registerItemRenderer(Item.getItemFromBlock(BlockHandler.blockCoil),
                new ItemRenderTeslaCoil(new ResourceLocation(Constants.MODID + ":textures/models/teslaCoil.png")));

        //SuperTeslaCoil
        TileEntitySpecialRenderer renderSuperCoil = new RenderTeslaCoil(new ResourceLocation(Constants.MODID + ":textures/models/superTeslaCoil.png"));
        ClientRegistry.bindTileEntitySpecialRenderer(TileSuperTeslaCoil.class, renderSuperCoil);
        MinecraftForgeClient.registerItemRenderer(Item.getItemFromBlock(BlockHandler.blockSuperCoil),
                new ItemRenderTeslaCoil(new ResourceLocation(Constants.MODID + ":textures/models/superTeslaCoil.png")));

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

    @Override
    public void preInit() {
    }

    @Override
    public void init() {
        super.init();
        //ManualRegistry.instance.init();
        //TODO: Register manual
    }

    @Override
    public void postInit() {

    }
}
