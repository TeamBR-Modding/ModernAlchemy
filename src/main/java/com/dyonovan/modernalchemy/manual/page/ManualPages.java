package com.dyonovan.modernalchemy.manual.page;

import com.dyonovan.modernalchemy.blocks.BlockBase;
import com.dyonovan.modernalchemy.handlers.BlockHandler;
import com.dyonovan.modernalchemy.handlers.PacketHandler;
import com.dyonovan.modernalchemy.manual.ItemManual;
import com.dyonovan.modernalchemy.network.UpdateManualPacket;
import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;
import io.netty.buffer.ByteBuf;
import io.netty.buffer.Unpooled;
import net.minecraft.block.Block;
import net.minecraft.client.Minecraft;
import net.minecraft.network.PacketBuffer;
import net.minecraft.network.play.client.C17PacketCustomPayload;

import java.util.HashMap;

public class ManualPages {
    public static ManualPages instance = new ManualPages();

    public static HashMap<String, BasePage> pages;

    public ManualPages() {
        pages = new HashMap<String, BasePage>();
    }

    public void init() {
        pages.put("Main Page", new MainPage());
        for(Block block : BlockHandler.blockRegistry) {
            if(block instanceof BlockBase) {
                if(((BlockBase) block).getManualComponents() != null)
                    pages.put(block.getUnlocalizedName(), new BlockPage(block.getUnlocalizedName(), (BlockBase) block));
            }
        }
    }

    public void addPage(BasePage page) {
        pages.put(page.getID(), page);
    }

    public BasePage getPage(String id) {
        if(pages.get(id) != null)
            return pages.get(id);
        else
            return new MainPage();
    }

    @SideOnly(Side.CLIENT)
    public void openPage(BasePage page) {
        Minecraft.getMinecraft().displayGuiScreen(page);
        ItemManual.addPageToVisitedPages(Minecraft.getMinecraft().thePlayer.getCurrentEquippedItem(), page.getID());
        PacketHandler.net.sendToServer(new UpdateManualPacket.UpdateManualMessage(Minecraft.getMinecraft().thePlayer.getCurrentEquippedItem().getTagCompound()));
    }
}
