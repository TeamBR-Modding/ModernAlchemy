package com.dyonovan.modernalchemy.client.audio;

import com.dyonovan.modernalchemy.common.tileentity.TileModernAlchemy;
import com.dyonovan.modernalchemy.handlers.ConfigHandler;
import com.dyonovan.modernalchemy.common.tileentity.BaseMachine;
import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;
import net.minecraft.client.audio.ITickableSound;
import net.minecraft.client.audio.PositionedSound;
import net.minecraft.util.ResourceLocation;

@SideOnly(Side.CLIENT)
public class MachineSound extends PositionedSound implements ITickableSound {
    protected boolean donePlaying = false;
    private TileModernAlchemy tileEntity;

    public MachineSound(ResourceLocation path, TileModernAlchemy tileEntity, float volume, float pitch) {
        super(path);
        this.repeat = true;
        this.tileEntity = tileEntity;
        this.volume = volume;
        this.field_147663_c = pitch;
        this.xPosF = tileEntity.xCoord;
        this.yPosF = tileEntity.yCoord;
        this.zPosF = tileEntity.zCoord;
        this.field_147665_h = 0;
    }

    public MachineSound(String path, TileModernAlchemy tileEntity, float volume, float pitch) {
        this(new ResourceLocation(path), tileEntity, volume, pitch);
    }

    @Override
    public void update() {
        if (!this.tileEntity.isActive() || this.tileEntity.isInvalid() || !ConfigHandler.machineSounds) {
            this.donePlaying = true;
        }
    }

    @Override
    public boolean isDonePlaying() {
        return this.donePlaying;
    }
}
