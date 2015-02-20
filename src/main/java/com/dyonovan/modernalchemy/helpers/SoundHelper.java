package com.dyonovan.modernalchemy.helpers;

import com.dyonovan.modernalchemy.lib.Constants;
import cpw.mods.fml.client.FMLClientHandler;
import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;
import net.minecraft.client.audio.PositionedSoundRecord;
import net.minecraft.util.ResourceLocation;

public class SoundHelper {

    @SideOnly(Side.CLIENT)
    public static void playSound(String soundName, float xCoord, float yCoord, float zCoord, float volume, float pitch)
    {
        FMLClientHandler.instance().getClient().getSoundHandler().playSound(new PositionedSoundRecord(new ResourceLocation(Constants.MODID, soundName), volume, pitch, xCoord, yCoord, zCoord));
    }
}
