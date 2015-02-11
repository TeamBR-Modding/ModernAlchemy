package com.dyonovan.itemreplication.util;

import com.dyonovan.itemreplication.effects.LightningBolt;
import com.dyonovan.itemreplication.handlers.PacketHandler;
import com.dyonovan.itemreplication.network.RenderLightningBoltPacket;
import com.dyonovan.itemreplication.tileentity.TileTeslaCoil;
import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;
import net.minecraft.client.Minecraft;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.world.World;

import java.awt.*;

public class RenderUtils {

    @SideOnly(Side.CLIENT)
    public static void renderLightningBolt(World worldObj, double xCoord, double yCoord, double zCoord, TileEntity coil, int age) {
        renderLightningBolt(worldObj, xCoord + 0.5, yCoord + 0.5, zCoord + 0.5, coil.xCoord + 0.5, coil.yCoord + 0.9, coil.zCoord + 0.5, age > 4 ? age : 4);
    }

    @SideOnly(Side.CLIENT)
    public static void renderLightningBolt(World worldObj, double xCoord, double yCoord, double zCoord, double tx, double ty, double tz, int age) {
        Minecraft.getMinecraft().effectRenderer.addEffect(new LightningBolt(worldObj, xCoord + 0.5, yCoord + 0.5, zCoord + 0.5, tx, ty, tz, age > 4 ? age : 4, new Color(255, 255, 255, 255)));
    }

    public static void sendBoltToClient(int dimID, double x, double y, double z, double x1, double y1, double z1, int age) {
        PacketHandler.net.sendToDimension(new RenderLightningBoltPacket.BoltMessage(x, y, z, x1, y1, z1, age > 4 ? age : 4), dimID);
    }

    public static void sendBoltToClient(double x, double y, double z, TileTeslaCoil coil, int age) {
        sendBoltToClient(coil.getWorldObj().provider.dimensionId, x, y, z, coil.xCoord + 0.5, coil.yCoord + 0.9, coil.zCoord + 0.5, age > 4 ? age : 4);
    }
}
