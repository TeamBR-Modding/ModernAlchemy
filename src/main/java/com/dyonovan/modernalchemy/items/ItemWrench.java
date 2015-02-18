package com.dyonovan.modernalchemy.items;


import buildcraft.api.tools.IToolWrench;
import cofh.api.block.IDismantleable;
import cofh.api.item.IToolHammer;
import cofh.asm.relauncher.Strippable;
import com.dyonovan.modernalchemy.ModernAlchemy;
import com.dyonovan.modernalchemy.lib.Constants;
import com.dyonovan.modernalchemy.tileentity.BaseTile;
import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;
import net.minecraft.block.Block;
import net.minecraft.client.renderer.texture.IIconRegister;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.world.World;
import net.minecraftforge.common.util.ForgeDirection;

@Strippable({"buildcraft.api.tools.IToolWrench"})
public class ItemWrench extends Item implements IToolWrench, IToolHammer {

    public ItemWrench() {
        this.setUnlocalizedName(Constants.MODID + ":wrench");
        this.setCreativeTab(ModernAlchemy.tabModernAlchemy);
        this.setMaxStackSize(1);
    }

    @Override
    @SideOnly(Side.CLIENT)
    public void registerIcons(IIconRegister IIconRegister) {
        itemIcon = IIconRegister.registerIcon(Constants.MODID + ":wrench");
    }

    @Override
    public boolean onItemUseFirst(ItemStack stack, EntityPlayer player, World world, int x, int y, int z, int side, float hitX, float hitY, float hitZ) {
        Block block = world.getBlock(x, y, z);
        if(!player.isSneaking() && world.getTileEntity(x, y, z) != null && world.getTileEntity(x, y, z) instanceof BaseTile) {
            BaseTile tile = (BaseTile)world.getTileEntity(x, y, z);
            tile.onWrench(player);
            player.swingItem();
            return true;
        }

        if(block != null && !player.isSneaking() && block.rotateBlock(world, x, y, z, ForgeDirection.getOrientation(side))) {
            player.swingItem();
            return true;
        }
        return false;
    }

    @Override
    public ItemStack onItemRightClick(ItemStack equipped, World world, EntityPlayer player) {
        if(!player.isSneaking()) {
            return equipped;
        }
        return equipped;
    }

    @Override
    public boolean isFull3D() {
        return true;
    }

    @Override
    public boolean isUsable(ItemStack itemStack, EntityLivingBase entityLivingBase, int i, int i1, int i2) {
        return true;
    }

    @Override
    public void toolUsed(ItemStack itemStack, EntityLivingBase entityLivingBase, int i, int i1, int i2) {

    }

    @Override
    public boolean canWrench(EntityPlayer player, int x, int y, int z) {
        return true;
    }

    @Override
    public void wrenchUsed(EntityPlayer player, int x, int y, int z) {
        player.swingItem();
    }

    @Override
    public boolean doesSneakBypassUse(World world, int x, int y, int z, EntityPlayer player) {
        return true;
    }
}
