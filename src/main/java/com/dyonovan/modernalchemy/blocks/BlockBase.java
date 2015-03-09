package com.dyonovan.modernalchemy.blocks;

import com.dyonovan.modernalchemy.ModernAlchemy;
import com.dyonovan.modernalchemy.blocks.misc.BlockTank;
import com.dyonovan.modernalchemy.blocks.teslacoil.BlockTeslaBase;
import com.dyonovan.modernalchemy.blocks.teslacoil.BlockTeslaCoil;
import com.dyonovan.modernalchemy.blocks.teslacoil.BlockTeslaStand;
import com.dyonovan.modernalchemy.helpers.WrenchHelper;
import com.dyonovan.modernalchemy.tileentity.misc.TileTank;
import com.dyonovan.modernalchemy.util.Location;
import com.dyonovan.modernalchemy.util.WorldUtils;
import net.minecraft.block.Block;
import net.minecraft.block.BlockContainer;
import net.minecraft.block.material.Material;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.world.World;
import net.minecraftforge.fluids.FluidStack;

import java.util.Random;

public abstract class BlockBase extends BlockContainer {

    public BlockBase(Material mat) {
        super(mat);
        this.setHardness(3.0F);
        this.setCreativeTab(ModernAlchemy.tabModernAlchemy);
    }

    public boolean useWrench(World world, int x, int y, int z) {
        if (!world.isRemote) {
            if(this instanceof BlockTank) {
                int meta = world.getBlockMetadata(x, y, z);
                ItemStack stack = new ItemStack(this, 1, meta);
                TileTank logic = (TileTank) world.getTileEntity(x, y, z);
                FluidStack liquid = logic.tank.getFluid();
                if (liquid != null)
                {
                    NBTTagCompound tag = new NBTTagCompound();
                    NBTTagCompound liquidTag = new NBTTagCompound();
                    liquid.writeToNBT(liquidTag);
                    tag.setTag("Fluid", liquidTag);
                    stack.setTagCompound(tag);
                }

                world.setBlockToAir(x, y, z);
                world.markBlockForUpdate(x, y, z);
                return true;
            }
            if (this instanceof BlockTeslaBase || this instanceof BlockTeslaStand) {
                Location loc = new Location(x, y + 1, z);
                int count = 0;
                while (true) {
                    if (world.getBlock(loc.x, loc.y, loc.z) instanceof BlockTeslaStand || world.getBlock(loc.x, loc.y, loc.z) instanceof BlockTeslaCoil) {
                        loc.y += 1;
                        count += 1;
                    } else {
                        doBreak(world, loc.x, loc.y - 1, loc.z, count);
                        break;
                    }
                }
            }
            WorldUtils.expelItem(world, x, y, z, new ItemStack(Item.getItemFromBlock(this)));
            world.setBlockToAir(x, y, z);
        }
        return true;
    }

    @Override
    public boolean onBlockActivated(World world, int x, int y, int z, EntityPlayer player, int par6, float par7, float par8, float par9) {
        if (player.isSneaking() && player.getCurrentEquippedItem() != null && WrenchHelper.isWrench(player.getCurrentEquippedItem().getItem())) {
            useWrench(world, x, y, z);
            return true;
        }
        return false;
    }

    @Override
    public int quantityDropped(Random random)
    {
        return 0;
    }

    private void doBreak(World world, int x, int y, int z, int count) {
        if (count > 0) {
            for (int i = 0; i < count; i++) {
                WorldUtils.expelItem(world, x, y - i, z, new ItemStack(Item.getItemFromBlock(world.getBlock(x, y - i, z))));
                world.setBlockToAir(x, y - i, z);
            }
        }
    }

    @Override
    public void breakBlock(World world, int x, int y, int z, Block par5, int par6)
    {
        super.breakBlock(world, x, y, z, par5, par6);
    }
}
