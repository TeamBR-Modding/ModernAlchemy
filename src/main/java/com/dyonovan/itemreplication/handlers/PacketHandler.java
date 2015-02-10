package com.dyonovan.itemreplication.handlers;

import com.dyonovan.itemreplication.lib.Constants;
import cpw.mods.fml.common.network.NetworkRegistry;
import cpw.mods.fml.common.network.simpleimpl.SimpleNetworkWrapper;

public class PacketHandler {

    public static SimpleNetworkWrapper net;

    public static void initPackets() {
        net = NetworkRegistry.INSTANCE.newSimpleChannel(Constants.MODID.toUpperCase());

    }
}
