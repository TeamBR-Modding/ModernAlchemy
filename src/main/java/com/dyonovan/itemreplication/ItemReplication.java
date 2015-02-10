package com.dyonovan.itemreplication;

import com.dyonovan.itemreplication.handlers.*;
import com.dyonovan.itemreplication.lib.Constants;
import com.dyonovan.itemreplication.proxy.CommonProxy;
import com.dyonovan.itemreplication.util.ReplicatorUtils;
import cpw.mods.fml.common.Mod;
import cpw.mods.fml.common.Mod.EventHandler;
import cpw.mods.fml.common.Mod.Instance;
import cpw.mods.fml.common.SidedProxy;
import cpw.mods.fml.common.event.FMLInitializationEvent;
import cpw.mods.fml.common.event.FMLPostInitializationEvent;
import cpw.mods.fml.common.event.FMLPreInitializationEvent;
import cpw.mods.fml.common.network.NetworkRegistry;
import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;
import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.item.Item;
import net.minecraftforge.common.config.Configuration;

import java.io.File;

@Mod(name = Constants.MODNAME, modid = Constants.MODID, version = Constants.VERSION, dependencies = Constants.DEPENDENCIES)

public class ItemReplication {

    @Instance(Constants.MODID)
    public static ItemReplication instance;

    @SidedProxy(clientSide = "com.dyonovan." + Constants.MODID + ".proxy.ClientProxy",
            serverSide = "com.dyonovan." + Constants.MODID + ".proxy.CommonProxy")
    public static CommonProxy proxy;

    public static CreativeTabs tabItemReplication = new CreativeTabs("tabItemReplication") {
        @Override
        @SideOnly(Side.CLIENT)
        public Item getTabIconItem() {
            return Item.getItemFromBlock(BlockHandler.blockCoil);
        }
    };

    @EventHandler
    public void preInit(FMLPreInitializationEvent event){
        ConfigHandler.init(new Configuration(new File(event.getModConfigurationDirectory().getAbsolutePath() + File.separator + Constants.MODID.toLowerCase() + File.separator + "general.properties")));
        BlockHandler.init();
        ItemHandler.init();
        WorldGeneratorHandler.init();
        EventManager.init();
        proxy.registerRenderer();

        BucketHandler.INSTANCE.buckets.put(BlockHandler.blockFluidActinium, ItemHandler.itemBucketActinium);

        ReplicatorUtils.buildDirectory(event.getModConfigurationDirectory().getAbsolutePath() + File.separator + Constants.MODID.toLowerCase() + File.separator + "replicatorValues" + File.separator);
    }

    @EventHandler
    public void init(FMLInitializationEvent event) {
        NetworkRegistry.INSTANCE.registerGuiHandler(this, new GuiHandler());
        PacketHandler.initPackets();
    }

    @EventHandler
    public void postInit(FMLPostInitializationEvent event) {}
}
