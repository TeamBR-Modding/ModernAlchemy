package com.dyonovan.modernalchemy.handlers;

import com.dyonovan.modernalchemy.lib.Constants;
import com.dyonovan.modernalchemy.network.MachineSoundPacket;
import com.dyonovan.modernalchemy.network.ModeSwitchPacket;
import com.dyonovan.modernalchemy.network.RenderLightningBoltPacket;
import com.dyonovan.modernalchemy.network.UpdateServerCoilLists;
import cpw.mods.fml.common.network.NetworkRegistry;
import cpw.mods.fml.common.network.simpleimpl.SimpleNetworkWrapper;
import cpw.mods.fml.relauncher.Side;

public class PacketHandler {

    public static SimpleNetworkWrapper net;

    public static void initPackets() {
        net = NetworkRegistry.INSTANCE.newSimpleChannel(Constants.MODID.toUpperCase());
        registerMessage(RenderLightningBoltPacket.class, RenderLightningBoltPacket.BoltMessage.class);
        registerMessage(UpdateServerCoilLists.class, UpdateServerCoilLists.UpdateMessage.class);
        registerMessage(ModeSwitchPacket.class, ModeSwitchPacket.UpdateMessage.class);
        registerMessage(MachineSoundPacket.class, MachineSoundPacket.MachineSoundMessage.class);
    }

    private static int nextPacketId = 0;

    @SuppressWarnings("unchecked")
    private static void registerMessage(Class packet, Class message)
    {
        net.registerMessage(packet, message, nextPacketId, Side.CLIENT);
        net.registerMessage(packet, message, nextPacketId, Side.SERVER);
        nextPacketId++;
    }
}
