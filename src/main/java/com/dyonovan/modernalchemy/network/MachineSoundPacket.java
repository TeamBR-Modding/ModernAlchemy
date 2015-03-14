package com.dyonovan.modernalchemy.network;

import com.dyonovan.modernalchemy.client.audio.SoundHelper;
import com.dyonovan.modernalchemy.common.tileentity.BaseMachine;
import cpw.mods.fml.common.network.ByteBufUtils;
import cpw.mods.fml.common.network.simpleimpl.IMessage;
import cpw.mods.fml.common.network.simpleimpl.IMessageHandler;
import cpw.mods.fml.common.network.simpleimpl.MessageContext;
import io.netty.buffer.ByteBuf;
import net.minecraft.client.Minecraft;

public class MachineSoundPacket implements IMessageHandler<MachineSoundPacket.MachineSoundMessage, IMessage> {


    @Override
    public IMessage onMessage(MachineSoundMessage message, MessageContext ctx) {
        if(ctx.side.isClient()) {
            SoundHelper.playMachineSound(message.name, (BaseMachine) Minecraft.getMinecraft().theWorld.getTileEntity(message.x, message.y, message.z), message.volume, message.pitch);
        }
        return null;
    }

    public static class MachineSoundMessage implements IMessage {
        private String name;
        private int x;
        private int y;
        private int z;
        private float volume;
        private float pitch;

        public MachineSoundMessage() {}

        public MachineSoundMessage(String machineSound, int xCoord, int yCoord, int zCoord, float soundVolume, float soundPitch) {
            name = machineSound;
            x = xCoord;
            y = yCoord;
            z = zCoord;
            volume = soundVolume;
            pitch = soundPitch;
        }

        @Override
        public void fromBytes(ByteBuf buf) {
            name = ByteBufUtils.readUTF8String(buf);
            x = buf.readInt();
            y = buf.readInt();
            z = buf.readInt();
            volume = buf.readFloat();
            pitch = buf.readFloat();
        }

        @Override
        public void toBytes(ByteBuf buf) {
            ByteBufUtils.writeUTF8String(buf, name);
            buf.writeInt(x);
            buf.writeInt(y);
            buf.writeInt(z);
            buf.writeFloat(volume);
            buf.writeFloat(pitch);
        }
    }
}
