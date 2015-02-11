package com.dyonovan.itemreplication.network;

import com.dyonovan.itemreplication.util.RenderUtils;
import cpw.mods.fml.common.network.simpleimpl.IMessage;
import cpw.mods.fml.common.network.simpleimpl.IMessageHandler;
import cpw.mods.fml.common.network.simpleimpl.MessageContext;
import io.netty.buffer.ByteBuf;
import net.minecraft.client.Minecraft;

public class RenderLightningBoltPacket implements IMessageHandler<RenderLightningBoltPacket.BoltMessage, IMessage>{

    @Override
    public IMessage onMessage(RenderLightningBoltPacket.BoltMessage message, MessageContext ctx) {
        if (ctx.side.isClient()) {
            RenderUtils.renderLightningBolt(Minecraft.getMinecraft().theWorld, message.x, message.y, message.z, message.x1, message.y1, message.z1, message.age);
        }
        return null;
    }

    public static class BoltMessage implements IMessage {

        private double x;
        private double y;
        private double z;
        private double x1;
        private double y1;
        private double z1;
        private int age;

        public BoltMessage() {}

        public BoltMessage(double xC, double yC, double zC, double x1C, double y2C, double z3C, int ageC) {
            x = xC;
            y = yC;
            z = zC;
            x1 = x1C;
            y1 = y2C;
            z1 = z3C;
            age = ageC;
        }

        @Override
        public void fromBytes(ByteBuf buf) {
            x = buf.readDouble();
            y = buf.readDouble();
            z = buf.readDouble();
            x1 = buf.readDouble();
            y1 = buf.readDouble();
            z1 = buf.readDouble();
            age = buf.readInt();
        }

        @Override
        public void toBytes(ByteBuf buf) {
            buf.writeDouble(x);
            buf.writeDouble(y);
            buf.writeDouble(z);
            buf.writeDouble(x1);
            buf.writeDouble(y1);
            buf.writeDouble(z1);
            buf.writeInt(age);
        }
    }
}
