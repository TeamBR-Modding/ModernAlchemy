package com.dyonovan.modernalchemy;

import com.dyonovan.modernalchemy.collections.AutoInit;
import com.dyonovan.modernalchemy.handlers.*;
import com.dyonovan.modernalchemy.helpers.ClassHelper;
import com.dyonovan.modernalchemy.lib.Constants;
import com.dyonovan.modernalchemy.proxy.CommonProxy;
import com.dyonovan.modernalchemy.util.JsonUtils;
import com.dyonovan.modernalchemy.util.ReplicatorUtils;
import cpw.mods.fml.common.Mod;
import cpw.mods.fml.common.Mod.EventHandler;
import cpw.mods.fml.common.Mod.Instance;
import cpw.mods.fml.common.SidedProxy;
import cpw.mods.fml.common.event.FMLInitializationEvent;
import cpw.mods.fml.common.event.FMLInterModComms;
import cpw.mods.fml.common.event.FMLPostInitializationEvent;
import cpw.mods.fml.common.event.FMLPreInitializationEvent;
import cpw.mods.fml.common.network.NetworkRegistry;
import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;
import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.item.Item;
import net.minecraft.util.DamageSource;
import net.minecraftforge.common.config.Configuration;
import net.minecraftforge.oredict.OreDictionary;

import java.io.File;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.Collection;

@Mod(name = Constants.MODNAME, modid = Constants.MODID, version = Constants.VERSION, dependencies = Constants.DEPENDENCIES)

public class ModernAlchemy {

    @Instance(Constants.MODID)
    public static ModernAlchemy instance;

    public static final DamageSource shock = new DamageSource("shock").setFireDamage().setDamageBypassesArmor();

    @SidedProxy(clientSide = "com.dyonovan." + Constants.MODID + ".proxy.ClientProxy",
            serverSide = "com.dyonovan." + Constants.MODID + ".proxy.CommonProxy")
    public static CommonProxy proxy;

    public static CreativeTabs tabModernAlchemy = new CreativeTabs("tabModernAlchemy") {
        @Override
        @SideOnly(Side.CLIENT)
        public Item getTabIconItem() {
            return Item.getItemFromBlock(BlockHandler.blockCoil);
        }
    };

    @EventHandler
    public void preInit(FMLPreInitializationEvent event){

        ConfigHandler.init(new Configuration(new File(event.getModConfigurationDirectory().getAbsolutePath() + File.separator + Constants.MODID.toLowerCase() + File.separator + "general.properties")));
        BlockHandler.preInit();
        ItemHandler.preInit();
        CraftingHandler.preInit();
        EntityHandler.init();
        EventManager.init();
        proxy.init();

        BucketHandler.INSTANCE.buckets.put(BlockHandler.blockFluidActinium, ItemHandler.itemBucketActinium);

        ReplicatorUtils.buildDirectory(event.getModConfigurationDirectory().getAbsolutePath() + File.separator + Constants.MODID.toLowerCase() + File.separator + "replicatorValues");

        Collection<Class<?>> ourClasses = ClassHelper.getClassesInJar(ModernAlchemy.class.getResource(""));
        for(Class cl : ourClasses) {
            for(Method method : cl.getMethods()) {
                if(method.isAnnotationPresent(AutoInit.class)) {
                    try {
                        method.invoke(cl, null);
                    } catch (IllegalAccessException e) {
                        e.printStackTrace();
                    } catch (InvocationTargetException e) {
                        e.printStackTrace();
                    }
                }
            }
        }
    }

    @SuppressWarnings("unused")
    @EventHandler
    public void init(FMLInitializationEvent event) {

        if (OreDictionary.getOres("oreCopper").isEmpty()) {
            BlockHandler.initCopper();
            ItemHandler.initCopper();
        }
        if (OreDictionary.getOres("ingotSteel").isEmpty()) {
            ItemHandler.initSteel();
        }

        WorldGeneratorHandler.init();
        CraftingHandler.init();
        NetworkRegistry.INSTANCE.registerGuiHandler(this, new GuiHandler());
        PacketHandler.initPackets();
        proxy.init();
        FMLInterModComms.sendMessage("Waila", "register", "com.dyonovan.modernalchemy.waila.WailaDataProvider.callbackRegister");
    }

    @SuppressWarnings("unused")
    @EventHandler
    public void postInit(FMLPostInitializationEvent event) {}
}
