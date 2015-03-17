package com.dyonovan.modernalchemy.common.blocks.replicator;

import com.dyonovan.modernalchemy.ModernAlchemy;
import com.dyonovan.modernalchemy.client.ClientProxy;
import com.dyonovan.modernalchemy.common.blocks.BlockModernAlchemy;
import com.dyonovan.modernalchemy.util.Location;
import com.dyonovan.modernalchemy.util.WorldUtils;
import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;
import net.minecraft.block.Block;
import net.minecraft.block.material.Material;
import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.texture.IIconRegister;
import net.minecraft.entity.Entity;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.entity.player.EntityPlayerMP;
import net.minecraft.util.AxisAlignedBB;
import net.minecraft.util.MovingObjectPosition;
import net.minecraft.util.Vec3;
import net.minecraft.world.World;
import net.minecraftforge.common.util.ForgeDirection;
import openmods.block.OpenBlock;
import openmods.infobook.BookDocumentation;

import java.util.Arrays;
import java.util.List;
@BookDocumentation
public class BlockReplicatorFrame extends BlockModernAlchemy {

    private static final float PIPE_MIN_POS = 0.2F;
    private static final float PIPE_MAX_POS = 0.8F;

    public BlockReplicatorFrame() {
        super(Material.iron);
        setRenderMode(RenderMode.TESR_ONLY);
    }

    @Override
    public boolean renderAsNormalBlock() {
        return false;
    }

    @Override
    public int getRenderType() {
        return -1;
    }

    @Override
    public void addCollisionBoxesToList(World world, int x, int y, int z, AxisAlignedBB axisalignedbb, List arraylist, Entity par7Entity) {
        setBlockBounds(PIPE_MIN_POS, PIPE_MIN_POS, PIPE_MIN_POS, PIPE_MAX_POS, PIPE_MAX_POS, PIPE_MAX_POS);
        super.addCollisionBoxesToList(world, x, y, z, axisalignedbb, arraylist, par7Entity);

        Block block = world.getBlock(x, y, z);
        if (block instanceof BlockReplicatorFrame) {

            if (WorldUtils.getBlockInDirection(world, new Location(x, y, z), ForgeDirection.WEST) instanceof BlockReplicatorFrame) {
                setBlockBounds(0.0F, PIPE_MIN_POS, PIPE_MIN_POS, PIPE_MAX_POS, PIPE_MAX_POS, PIPE_MAX_POS);
                super.addCollisionBoxesToList(world, x, y, z, axisalignedbb, arraylist, par7Entity);
            }

            if (WorldUtils.getBlockInDirection(world, new Location(x, y, z), ForgeDirection.EAST) instanceof BlockReplicatorFrame) {
                setBlockBounds(PIPE_MIN_POS, PIPE_MIN_POS, PIPE_MIN_POS, 1.0F, PIPE_MAX_POS, PIPE_MAX_POS);
                super.addCollisionBoxesToList(world, x, y, z, axisalignedbb, arraylist, par7Entity);
            }

            if (WorldUtils.getBlockInDirection(world, new Location(x, y, z), ForgeDirection.DOWN) instanceof BlockReplicatorFrame) {
                setBlockBounds(PIPE_MIN_POS, 0.0F, PIPE_MIN_POS, PIPE_MAX_POS, PIPE_MAX_POS, PIPE_MAX_POS);
                super.addCollisionBoxesToList(world, x, y, z, axisalignedbb, arraylist, par7Entity);
            }

            if (WorldUtils.getBlockInDirection(world, new Location(x, y, z), ForgeDirection.UP) instanceof BlockReplicatorFrame) {
                setBlockBounds(PIPE_MIN_POS, PIPE_MIN_POS, PIPE_MIN_POS, PIPE_MAX_POS, 1.0F, PIPE_MAX_POS);
                super.addCollisionBoxesToList(world, x, y, z, axisalignedbb, arraylist, par7Entity);
            }

            if (WorldUtils.getBlockInDirection(world, new Location(x, y, z), ForgeDirection.NORTH) instanceof BlockReplicatorFrame) {
                setBlockBounds(PIPE_MIN_POS, PIPE_MIN_POS, 0.0F, PIPE_MAX_POS, PIPE_MAX_POS, PIPE_MAX_POS);
                super.addCollisionBoxesToList(world, x, y, z, axisalignedbb, arraylist, par7Entity);
            }

            if (WorldUtils.getBlockInDirection(world, new Location(x, y, z), ForgeDirection.SOUTH) instanceof BlockReplicatorFrame) {
                setBlockBounds(PIPE_MIN_POS, PIPE_MIN_POS, PIPE_MIN_POS, PIPE_MAX_POS, PIPE_MAX_POS, 1.0F);
                super.addCollisionBoxesToList(world, x, y, z, axisalignedbb, arraylist, par7Entity);
            }
        }
        setBlockBounds(0.0F, 0.0F, 0.0F, 1.0F, 1.0F, 1.0F);
    }

    @SideOnly(Side.CLIENT)
    @Override
    public AxisAlignedBB getSelectedBoundingBoxFromPool(World world, int x, int y, int z) {
        RaytraceResult rayTraceResult = doRayTrace(world, x, y, z, Minecraft.getMinecraft().thePlayer);

        if (rayTraceResult != null && rayTraceResult.boundingBox != null) {
            AxisAlignedBB box = rayTraceResult.boundingBox;

            float scale = 0.08F;
            box = box.expand(scale, scale, scale);

            return box.getOffsetBoundingBox(x, y, z);
        }
        return super.getSelectedBoundingBoxFromPool(world, x, y, z).expand(-0.85F, -0.85F, -0.85F);
    }

    @Override
    protected Object getModInstance() {
        return ModernAlchemy.instance;
    }


    @SideOnly(Side.CLIENT)
    public void registerBlockIcons(IIconRegister iconregister) {
        this.blockIcon = iconregister.registerIcon("minecraft:iron_block");
    }

    @Override
    public boolean isOpaqueCube() {
        return false;
    }

    public RaytraceResult doRayTrace(World world, int x, int y, int z, EntityPlayer player) {
        double reachDistance = 5;

        if (player instanceof EntityPlayerMP) {
            reachDistance = ((EntityPlayerMP) player).theItemInWorldManager.getBlockReachDistance();
        }

        double eyeHeight = world.isRemote ? player.getEyeHeight() - player.getDefaultEyeHeight() : player.getEyeHeight();
        Vec3 lookVec = player.getLookVec();
        Vec3 origin = Vec3.createVectorHelper(player.posX, player.posY + eyeHeight, player.posZ);
        Vec3 direction = origin.addVector(lookVec.xCoord * reachDistance, lookVec.yCoord * reachDistance, lookVec.zCoord * reachDistance);

        return doRayTrace(world, x, y, z, origin, direction);
    }

    private RaytraceResult doRayTrace(World world, int x, int y, int z, Vec3 origin, Vec3 direction) {

        MovingObjectPosition[] hits = new MovingObjectPosition[31];
        AxisAlignedBB[] boxes = new AxisAlignedBB[31];
        ForgeDirection[] sideHit = new ForgeDirection[31];
        Arrays.fill(sideHit, ForgeDirection.UNKNOWN);

        for (ForgeDirection side : ForgeDirection.VALID_DIRECTIONS) {
            if (side == ForgeDirection.UNKNOWN || (WorldUtils.getBlockInDirection(world, new Location(x, y, z), side) instanceof BlockReplicatorFrame)) {
                AxisAlignedBB bb = getPipeBoundingBox(side);
                setBlockBounds(bb);
                boxes[side.ordinal()] = bb;
                hits[side.ordinal()] = super.collisionRayTrace(world, x, y, z, origin, direction);
                sideHit[side.ordinal()] = side;
            }
        }

        double minLengthSquared = Double.POSITIVE_INFINITY;
        int minIndex = -1;

        for (int i = 0; i < hits.length; i++) {
            MovingObjectPosition hit = hits[i];
            if (hit == null) {
                continue;
            }

            double lengthSquared = hit.hitVec.squareDistanceTo(origin);

            if (lengthSquared < minLengthSquared) {
                minLengthSquared = lengthSquared;
                minIndex = i;
            }
        }

        setBlockBounds(0, 0, 0, 1, 1, 1);
        if (minIndex == -1) {
            return null;
        }
        return new RaytraceResult(hits[minIndex], boxes[minIndex], sideHit[minIndex]);
    }

    private void setBlockBounds(AxisAlignedBB bb) {
        setBlockBounds((float) bb.minX, (float) bb.minY, (float) bb.minZ, (float) bb.maxX, (float) bb.maxY, (float) bb.maxZ);
    }

    private AxisAlignedBB getPipeBoundingBox(ForgeDirection side) {
        float min = PIPE_MIN_POS;
        float max = PIPE_MAX_POS;

        if (side == ForgeDirection.UNKNOWN) {
            return AxisAlignedBB.getBoundingBox(min, min, min, max, max, max);
        }

        float[][] bounds = new float[3][2];
        // X START - END
        bounds[0][0] = min;
        bounds[0][1] = max;
        // Y START - END
        bounds[1][0] = 0;
        bounds[1][1] = min;
        // Z START - END
        bounds[2][0] = min;
        bounds[2][1] = max;

        transform(bounds, side);
        return AxisAlignedBB.getBoundingBox(bounds[0][0], bounds[1][0], bounds[2][0], bounds[0][1], bounds[1][1], bounds[2][1]);
    }

    public static void transform(float[][] targetArray, ForgeDirection direction) {
        if ((direction.ordinal() & 0x1) == 1) {
            mirrorY(targetArray);
        }

        for (int i = 0; i < (direction.ordinal() >> 1); i++) {
            rotate(targetArray);
        }
    }

    public static void mirrorY(float[][] targetArray) {
        float temp = targetArray[1][0];
        targetArray[1][0] = (targetArray[1][1] - 0.5F) * -1F + 0.5F; // 1 -> 0.5F -> -0.5F -> 0F
        targetArray[1][1] = (temp - 0.5F) * -1F + 0.5F; // 0 -> -0.5F -> 0.5F -> 1F
    }

    public static void rotate(float[][] targetArray) {
        for (int i = 0; i < 2; i++) {
            float temp = targetArray[2][i];
            targetArray[2][i] = targetArray[1][i];
            targetArray[1][i] = targetArray[0][i];
            targetArray[0][i] = temp;
        }
    }

    static class RaytraceResult {
        public final MovingObjectPosition movingObjectPosition;
        public final AxisAlignedBB boundingBox;
        public final ForgeDirection sideHit;

        RaytraceResult(MovingObjectPosition movingObjectPosition, AxisAlignedBB boundingBox, ForgeDirection side) {
            this.movingObjectPosition = movingObjectPosition;
            this.boundingBox = boundingBox;
            this.sideHit = side;
        }
    }
}
