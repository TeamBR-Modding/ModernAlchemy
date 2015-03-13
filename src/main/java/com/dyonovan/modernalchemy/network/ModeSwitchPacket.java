package com.dyonovan.modernalchemy.network;

import com.dyonovan.modernalchemy.tileentity.machines.TileAdvancedCrafter;
import com.dyonovan.modernalchemy.tileentity.teslacoil.TileTeslaCoil;
import com.dyonovan.modernalchemy.util.Location;
import cpw.mods.fml.common.network.ByteBufUtils;
import cpw.mods.fml.common.network.simpleimpl.IMessage;
import cpw.mods.fml.common.network.simpleimpl.IMessageHandler;
import cpw.mods.fml.common.network.simpleimpl.MessageContext;
import io.netty.buffer.ByteBuf;
import net.minecraft.client.Minecraft;

import java.util.LinkedList;

public class ModeSwitchPacket implements IMessageHandler<ModeSwitchPacket.UpdateMessage, IMessage> {

    @Override
    public IMessage onMessage(ModeSwitchPacket.UpdateMessage message, MessageContext ctx) {
        if (ctx.side.isServer()) {
            TileAdvancedCrafter tile = (TileAdvancedCrafter) ctx.getServerHandler().playerEntity.worldObj.getTileEntity(message.x, message.y, message.z);
            //tile.currentMode = message.mode;
            ctx.getServerHandler().playerEntity.worldObj.markBlockForUpdate(message.x, message.y, message.z);
        }
        return null;
    }

    public static class UpdateMessage implements IMessage {

        private int mode, x, y, z;

        @SuppressWarnings("unused")
        public UpdateMessage() {}

        public UpdateMessage(int mode, int x, int y, int z) {
            this.mode = mode;
            this.x = x;
            this.y = y;
            this.z = z;
        }

        @Override
        public void fromBytes(ByteBuf buf) {
            mode = buf.readInt();
            x = buf.readInt();
            y = buf.readInt();
            z = buf.readInt();
        }

        @Override
        public void toBytes(ByteBuf buf) {
            buf.writeInt(mode);
            buf.writeInt(x);
            buf.writeInt(y);
            buf.writeInt(z);
        }
    }
}

