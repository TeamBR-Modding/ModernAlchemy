package com.dyonovan.modernalchemy.network;

import cpw.mods.fml.common.network.ByteBufUtils;
import cpw.mods.fml.common.network.simpleimpl.IMessage;
import cpw.mods.fml.common.network.simpleimpl.IMessageHandler;
import cpw.mods.fml.common.network.simpleimpl.MessageContext;
import io.netty.buffer.ByteBuf;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;

public class UpdateManualPacket implements IMessageHandler<UpdateManualPacket.UpdateManualMessage, IMessage> {

    @Override
    public IMessage onMessage(UpdateManualMessage message, MessageContext ctx) {
        if(ctx.side.isServer()) {
            ItemStack stack = ctx.getServerHandler().playerEntity.getCurrentEquippedItem();
            stack.setTagCompound(message.tag);
        }
        return null;
    }

    public static class UpdateManualMessage implements IMessage {
        protected NBTTagCompound tag;

        public UpdateManualMessage() {}

        public UpdateManualMessage(NBTTagCompound tagCompound) {
            this.tag = tagCompound;
        }

        @Override
        public void fromBytes(ByteBuf buf) {
            tag = ByteBufUtils.readTag(buf);
        }

        @Override
        public void toBytes(ByteBuf buf) {
            ByteBufUtils.writeTag(buf, tag);
        }
    }
}
