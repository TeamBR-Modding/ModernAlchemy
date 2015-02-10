package com.dyonovan.itemreplication.network;

import com.dyonovan.itemreplication.util.RenderUtils;
import cpw.mods.fml.common.network.simpleimpl.IMessage;
import cpw.mods.fml.common.network.simpleimpl.IMessageHandler;
import cpw.mods.fml.common.network.simpleimpl.MessageContext;
import io.netty.buffer.ByteBuf;

public class RenderLightningBoltPacket implements IMessageHandler<RenderLightningBoltPacket.BoltMessage, IMessage>{

    @Override
    public IMessage onMessage(RenderLightningBoltPacket.BoltMessage message, MessageContext ctx) {
        //if (ctx.side.isClient())

        return null;
    }

    public static class BoltMessage implements IMessage {

        public BoltMessage() {}


        @Override
        public void fromBytes(ByteBuf buf) {

        }

        @Override
        public void toBytes(ByteBuf buf) {

        }
    }
}
