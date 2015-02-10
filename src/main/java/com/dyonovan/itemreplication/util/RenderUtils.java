package com.dyonovan.itemreplication.util;

import com.dyonovan.itemreplication.effects.LightningBolt;
import com.dyonovan.itemreplication.tileentity.TileTeslaCoil;
import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;
import net.minecraft.client.Minecraft;
import net.minecraft.world.World;

import java.awt.*;

public class RenderUtils {

    @SideOnly(Side.CLIENT)
    public static void renderLightningBolt(World worldObj, int xCoord, int yCoord, int zCoord, TileTeslaCoil coil, int fill) {
        Minecraft.getMinecraft().effectRenderer.addEffect(new LightningBolt(worldObj, xCoord + 0.5, yCoord + 0.5, zCoord + 0.5, coil.xCoord + 0.5, coil.yCoord + 1.5, coil.zCoord + 0.5, fill > 4 ? fill : 4, new Color(255, 255, 255, 255)));

    }
}
