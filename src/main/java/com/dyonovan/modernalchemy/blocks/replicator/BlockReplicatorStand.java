package com.dyonovan.modernalchemy.blocks.replicator;

import com.dyonovan.modernalchemy.blocks.BlockBase;
import com.dyonovan.modernalchemy.lib.Constants;
import com.dyonovan.modernalchemy.manual.component.ComponentBase;
import com.dyonovan.modernalchemy.manual.component.ComponentItemRender;
import com.dyonovan.modernalchemy.tileentity.replicator.TileReplicatorStand;
import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;
import net.minecraft.block.material.Material;
import net.minecraft.client.renderer.texture.IIconRegister;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemStack;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.world.World;

import java.util.ArrayList;
import java.util.List;

public class BlockReplicatorStand extends BlockBase {

    public BlockReplicatorStand() {
        super(Material.iron);

        this.setBlockName(Constants.MODID + ":blockReplicatorStand");
    }

    @SideOnly(Side.CLIENT)
    public void registerBlockIcons(IIconRegister iconregister) {
        this.blockIcon = iconregister.registerIcon("minecraft:iron_block");
    }

    @Override
    public boolean hasTileEntity(int metadata) {
        return true;
    }

    @Override
    public TileEntity createNewTileEntity(World world, int i) {
        return new TileReplicatorStand();
    }

    @Override
    public List<ComponentBase> getManualComponents() {
        List<ComponentBase> parts = new ArrayList<ComponentBase>();
        parts.add(new ComponentItemRender(30, new ItemStack(this)));
        return parts;
    }

    @Override
    public boolean onBlockActivated(World world, int x, int y, int z, EntityPlayer player, int par6, float par7, float par8, float par9) {
        super.onBlockActivated(world, x, y, z, player, par6, par7, par8, par9);

        return true;
    }

    @Override
    public int getRenderType() {
        return -1;
    }

    @Override
    public boolean isOpaqueCube() {
        return false;
    }

    @Override
    public boolean renderAsNormalBlock() {
        return false;
    }
}
