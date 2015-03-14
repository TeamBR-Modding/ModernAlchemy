package com.dyonovan.modernalchemy.client.audio;

import com.dyonovan.modernalchemy.lib.Constants;
import com.dyonovan.modernalchemy.common.tileentity.BaseMachine;
import cpw.mods.fml.client.FMLClientHandler;
import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;
import net.minecraft.client.Minecraft;
import net.minecraft.client.audio.ISound;
import net.minecraft.client.audio.PositionedSoundRecord;
import net.minecraft.util.ResourceLocation;

public class SoundHelper {

    @SideOnly(Side.CLIENT)
    public static void playSound(String soundName, double xCoord, double yCoord, double zCoord, float volume, float pitch)
    {
        FMLClientHandler.instance().getClient().getSoundHandler().playSound(new PositionedSoundRecord(new ResourceLocation(Constants.MODID, soundName), volume, pitch, (float) xCoord, (float) yCoord, (float) zCoord));
    }

    @SideOnly(Side.CLIENT)
    public static void playMachineSound(String soundName, BaseMachine machine, float volume, float pitch) {
        ISound eventHorizonSound = new MachineSound(soundName, machine, volume, pitch);
        Minecraft.getMinecraft().getSoundHandler().playSound(eventHorizonSound);
    }
}
