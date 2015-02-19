package com.dyonovan.modernalchemy.network;

import com.dyonovan.modernalchemy.tileentity.teslacoil.TileTeslaCoil;
import com.dyonovan.modernalchemy.util.Location;
import cpw.mods.fml.common.network.ByteBufUtils;
import cpw.mods.fml.common.network.simpleimpl.IMessage;
import cpw.mods.fml.common.network.simpleimpl.IMessageHandler;
import cpw.mods.fml.common.network.simpleimpl.MessageContext;
import io.netty.buffer.ByteBuf;

import java.util.LinkedList;

public class UpdateServerCoilLists implements IMessageHandler<UpdateServerCoilLists.UpdateMessage, IMessage> {

    @Override
    public IMessage onMessage(UpdateServerCoilLists.UpdateMessage message, MessageContext ctx) {
        if (ctx.side.isServer()) {
            TileTeslaCoil tile = (TileTeslaCoil) ctx.getServerHandler().playerEntity.worldObj.getTileEntity(message.x, message.y, message.z);
            if (message.listName.equals("linkedMachines")) {
                tile.linkedMachines.clear();
                tile.linkedMachines.addAll(message.list);
            } else if (message.listName.equals("rangeMachines")) {
                tile.rangeMachines.clear();
                tile.rangeMachines.addAll(message.list);
            }
            ctx.getServerHandler().playerEntity.worldObj.markBlockForUpdate(message.x, message.y, message.z);
        }
        return null;
    }

    public static class UpdateMessage implements IMessage {

        private String listName;
        private LinkedList<Location> list;
        private int size, x, y, z;

        @SuppressWarnings("unused")
        public UpdateMessage() {}

        public UpdateMessage(int x, int y, int z, String listName, LinkedList<Location> list) {
            this.x = x;
            this.y = y;
            this.z = z;
            this.listName = listName;
            this.list = list;
            this.size = list.size();
        }

        @Override
        public void fromBytes(ByteBuf buf) {
            list = new LinkedList<Location>();
            x = buf.readInt();
            y = buf.readInt();
            z = buf.readInt();
            size = buf.readInt();
            for (int i = 0; i < size; i++) {
                int tempX = buf.readInt();
                int tempY = buf.readInt();
                int tempZ = buf.readInt();
                list.add(new Location(tempX, tempY, tempZ));
            }
            listName = ByteBufUtils.readUTF8String(buf);


        }

        @Override
        public void toBytes(ByteBuf buf) {
            buf.writeInt(x);
            buf.writeInt(y);
            buf.writeInt(z);
            buf.writeInt(size);
            for (Location loc : list) {
                buf.writeInt(loc.x);
                buf.writeInt(loc.y);
                buf.writeInt(loc.z);
            }
            ByteBufUtils.writeUTF8String(buf, listName);
        }
    }
}

