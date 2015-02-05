package com.dyonovan.itemreplication;

import com.dyonovan.itemreplication.handlers.BlockHandler;
import com.dyonovan.itemreplication.handlers.ConfigHandler;
import com.dyonovan.itemreplication.handlers.WorldGeneratorHandler;
import com.dyonovan.itemreplication.lib.Constants;
import com.dyonovan.itemreplication.proxy.CommonProxy;
import cpw.mods.fml.common.Mod;
import cpw.mods.fml.common.Mod.EventHandler;
import cpw.mods.fml.common.Mod.Instance;
import cpw.mods.fml.common.SidedProxy;
import cpw.mods.fml.common.event.FMLPostInitializationEvent;
import cpw.mods.fml.common.event.FMLPreInitializationEvent;
import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;
import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.init.Items;
import net.minecraft.item.Item;
import net.minecraftforge.common.config.Configuration;

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
            return Items.cauldron;
        }
    };

    @EventHandler
    public void preInit(FMLPreInitializationEvent event){

        ConfigHandler.init(new Configuration(event.getSuggestedConfigurationFile()));
        BlockHandler.init();
        WorldGeneratorHandler.init();
    }

    @EventHandler
    public void postInit(FMLPostInitializationEvent event) {}
}
