package com.dyonovan.modernalchemy.client;

import com.dyonovan.modernalchemy.client.renderer.arcfurnace.ArcFurnaceDummyRenderer;
import com.dyonovan.modernalchemy.client.renderer.teslacoil.*;
import com.dyonovan.modernalchemy.common.tileentity.teslacoil.TileSuperTeslaCoil;
import com.dyonovan.modernalchemy.common.tileentity.teslacoil.TileTeslaBase;
import com.dyonovan.modernalchemy.common.tileentity.teslacoil.TileTeslaCoil;
import com.dyonovan.modernalchemy.common.tileentity.teslacoil.TileTeslaStand;
import com.dyonovan.modernalchemy.handlers.BlockHandler;
import com.dyonovan.modernalchemy.lib.Constants;
import cpw.mods.fml.client.registry.ClientRegistry;
import cpw.mods.fml.client.registry.RenderingRegistry;
import net.minecraft.client.renderer.tileentity.TileEntitySpecialRenderer;
import net.minecraft.item.Item;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.client.MinecraftForgeClient;
import openmods.api.IProxy;
import openmods.renderer.BlockRenderingHandler;

public class ClientProxy implements IProxy {

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
        MinecraftForgeClient.registerItemRenderer(Item.getItemFromBlock(BlockHandler.blockTeslaCoil),
                new ItemRenderTeslaCoil(new ResourceLocation(Constants.MODID + ":textures/models/teslaCoil.png")));

        //SuperTeslaCoil
        TileEntitySpecialRenderer renderSuperCoil = new RenderTeslaCoil(new ResourceLocation(Constants.MODID + ":textures/models/superTeslaCoil.png"));
        ClientRegistry.bindTileEntitySpecialRenderer(TileSuperTeslaCoil.class, renderSuperCoil);
        MinecraftForgeClient.registerItemRenderer(Item.getItemFromBlock(BlockHandler.blockSuperTeslaCoil),
                new ItemRenderTeslaCoil(new ResourceLocation(Constants.MODID + ":textures/models/superTeslaCoil.png")));

        //TeslaBase
        TileEntitySpecialRenderer renderBase = new RenderTeslaBase();
        ClientRegistry.bindTileEntitySpecialRenderer(TileTeslaBase.class, renderBase);
        MinecraftForgeClient.registerItemRenderer(Item.getItemFromBlock(BlockHandler.blockTeslaBase),
                new ItemRenderTeslaBase());

        //Arc Furnace Dummies
        RenderingRegistry.registerBlockHandler(new ArcFurnaceDummyRenderer());
    }

    @Override
    public void preInit() {
    }

    @Override
    public void init() {
        //ManualRegistry.instance.init();
        //TODO: Register manual
    }

    @Override
    public void postInit() {

    }
}
