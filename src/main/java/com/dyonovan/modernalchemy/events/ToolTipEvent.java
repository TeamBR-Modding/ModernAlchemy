package com.dyonovan.modernalchemy.events;

import com.dyonovan.modernalchemy.handlers.BlockHandler;
import com.dyonovan.modernalchemy.manual.pages.GuiManual;
import com.dyonovan.modernalchemy.util.ReplicatorUtils;
import com.dyonovan.teambrcore.gui.BaseGui;
import com.dyonovan.teambrcore.helpers.GuiHelper;
import com.dyonovan.teambrcore.notification.GuiColor;
import cpw.mods.fml.common.eventhandler.SubscribeEvent;
import net.minecraft.client.Minecraft;
import net.minecraft.item.Item;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraftforge.event.entity.player.ItemTooltipEvent;
import net.minecraftforge.fluids.FluidContainerRegistry;
import net.minecraftforge.fluids.FluidStack;
import net.minecraftforge.fluids.FluidTank;

public class ToolTipEvent {
    @SubscribeEvent
    public void onToolTip(ItemTooltipEvent event) {
        if(Minecraft.getMinecraft().currentScreen instanceof BaseGui && !(Minecraft.getMinecraft().currentScreen instanceof GuiManual)) {
            if (ReplicatorUtils.getValueForItem(event.itemStack) > 0) {
                event.toolTip.add(GuiHelper.GuiColor.YELLOW + "Replicator Value: " + ReplicatorUtils.getValueForItem(event.itemStack));
            }
        }
        else if(event.itemStack.getItem() == Item.getItemFromBlock(BlockHandler.blockTank)) {
            NBTTagCompound tag = event.itemStack.getTagCompound();
            if (tag != null && tag.hasKey("Fluid")) {
                FluidTank tank = new FluidTank(FluidContainerRegistry.BUCKET_VOLUME * 16);
                readFluid(tag, tank);
                event.toolTip.add(GuiColor.TURQUISE + "Fluid: " + tank.getFluid().getFluid().getLocalizedName(tank.getFluid()));
                event.toolTip.add(GuiColor.ORANGE + "" + tank.getFluidAmount() + " / " + tank.getCapacity() + "mb");

            }
            else
                event.toolTip.add("Empty");
        }
    }

    private FluidStack readFluid(NBTTagCompound tag, FluidTank tank) {
        synchronized (tank) {
            tank.setFluid(null);
            tank.readFromNBT(tag.getCompoundTag("Fluid"));
            return tank.getFluid();
        }
    }
}
