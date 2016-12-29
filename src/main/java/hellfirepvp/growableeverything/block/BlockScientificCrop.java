package hellfirepvp.growableeverything.block;

import com.google.common.collect.Lists;
import hellfirepvp.growableeverything.GrowableEverything;
import hellfirepvp.growableeverything.block.tile.TileScientificCrop;
import hellfirepvp.growableeverything.item.ItemScientificSeed;
import hellfirepvp.growableeverything.lib.Items;
import net.minecraft.block.Block;
import net.minecraft.block.BlockCrops;
import net.minecraft.block.IGrowable;
import net.minecraft.block.SoundType;
import net.minecraft.block.material.Material;
import net.minecraft.block.properties.PropertyInteger;
import net.minecraft.block.state.BlockStateContainer;
import net.minecraft.block.state.IBlockState;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.init.Blocks;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.BlockRenderLayer;
import net.minecraft.util.math.AxisAlignedBB;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.MathHelper;
import net.minecraft.util.math.RayTraceResult;
import net.minecraft.world.IBlockAccess;
import net.minecraft.world.World;
import net.minecraftforge.common.EnumPlantType;
import net.minecraftforge.common.ForgeHooks;
import net.minecraftforge.common.IPlantable;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

import javax.annotation.Nullable;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Random;

/**
 * This class is part of the GrowableEverything Mod
 * GrowableEverything is Open Source and distributed under the
 * GNU LESSER GENERAL PUBLIC LICENSE
 * for more read the LICENSE file
 * <p>
 * Created by HellFirePvP @ 28.12.2016 19:15
 */
public class BlockScientificCrop extends Block implements IGrowable, IPlantable {

    private static Map<BlockPos, ItemStack> specificDropMapping = new HashMap<BlockPos, ItemStack>();

    private static final AxisAlignedBB bb0 =  new AxisAlignedBB(4D / 16D, 0D, 4D / 16D, 12D / 16D,  4D / 16D, 12D / 16D);
    private static final AxisAlignedBB bb1 =  new AxisAlignedBB(3D / 16D, 0D, 3D / 16D, 13D / 16D,  6D / 16D, 13D / 16D);
    private static final AxisAlignedBB bb2 =  new AxisAlignedBB(2D / 16D, 0D, 2D / 16D, 14D / 16D,  7D / 16D, 14D / 16D);
    private static final AxisAlignedBB bb3 =  new AxisAlignedBB(2D / 16D, 0D, 2D / 16D, 14D / 16D, 12D / 16D, 14D / 16D);
    private static final AxisAlignedBB bb4 =  new AxisAlignedBB(2D / 16D, 0D, 2D / 16D, 14D / 16D, 14D / 16D, 14D / 16D);
    private static final AxisAlignedBB bb5 =  new AxisAlignedBB(1D / 16D, 0D, 1D / 16D, 15D / 16D, 15D / 16D, 15D / 16D);
    private static final AxisAlignedBB bb67 = new AxisAlignedBB(1D / 16D, 0D, 1D / 16D, 15D / 16D, 16D / 16D, 15D / 16D);

    public static final PropertyInteger AGE = PropertyInteger.create("age", 0, 7);
    private static final Random rand = new Random();

    public BlockScientificCrop() {
        super(Material.PLANTS);
        this.setTickRandomly(true);
        this.setHardness(0.0F);
        this.setSoundType(SoundType.PLANT);
        this.disableStats();
        this.setDefaultState(this.blockState.getBaseState().withProperty(AGE, 0));
    }

    @Override
    public boolean hasTileEntity(IBlockState state) {
        return true;
    }

    @Override
    public boolean hasTileEntity() {
        return true;
    }

    @Nullable
    @Override
    public TileEntity createTileEntity(World world, IBlockState state) {
        return new TileScientificCrop();
    }

    @Override
    public void onBlockHarvested(World worldIn, BlockPos pos, IBlockState state, EntityPlayer player) {
        TileScientificCrop crop = getCropTile(worldIn, pos);
        if(crop != null) {
            ItemStack container = crop.getSeedItemStack();
            if(!container.isEmpty()) {
                specificDropMapping.put(pos, container);
            }
        }
    }

    @Override
    public ItemStack getPickBlock(IBlockState state, RayTraceResult target, World world, BlockPos pos, EntityPlayer player) {
        TileScientificCrop crop = getCropTile(world, pos);
        if(crop != null) {
            ItemStack container = crop.getSeedItemStack();
            if(!container.isEmpty()) {
                ItemStack pick = new ItemStack(Items.itemSeed);
                ItemScientificSeed.setCropItem(pick, container);
                return pick;
            }
        }
        return ItemStack.EMPTY;
    }

    @Override
    public List<ItemStack> getDrops(IBlockAccess world, BlockPos pos, IBlockState state, int fortune) {
        ItemStack preSpecified = specificDropMapping.get(pos);
        specificDropMapping.remove(pos);
        if(preSpecified == null) {
            TileScientificCrop crop = getCropTile(world, pos);
            if(crop == null || crop.getSeedItemStack().isEmpty()) {
                return Lists.newArrayList(new ItemStack(net.minecraft.init.Items.WHEAT_SEEDS));
            } else {
                preSpecified = crop.getSeedItemStack();
            }
        }
        if(preSpecified == null) {
            return Lists.newArrayList(new ItemStack(net.minecraft.init.Items.WHEAT_SEEDS));
        }

        List<ItemStack> out = Lists.newArrayList();
        ItemStack seed = new ItemStack(Items.itemSeed);
        ItemScientificSeed.setCropItem(seed, preSpecified);
        out.add(seed);
        if(getAge(state) >= getMaxAge()) {
            out.add(preSpecified);
            if(GrowableEverything.dropAdditionalSeeds && rand.nextInt(GrowableEverything.chanceForSeed) == 0) {
                out.add(seed.copy());
            }
        }
        return out;
    }

    @Override
    public void neighborChanged(IBlockState state, World worldIn, BlockPos pos, Block blockIn, BlockPos fromPos) {
        super.neighborChanged(state, worldIn, pos, blockIn, fromPos);
        if(!isValidAtPosition(worldIn, pos)) {
            this.dropBlockAsItem(worldIn, pos, state, 0);
            worldIn.setBlockState(pos, Blocks.AIR.getDefaultState(), 3);
            return;
        }
        TileScientificCrop crop = getCropTile(worldIn, pos);
        if(crop == null || crop.getSeedItemStack().isEmpty()) {
            this.dropBlockAsItem(worldIn, pos, state, 0);
            worldIn.setBlockState(pos, Blocks.AIR.getDefaultState(), 3);
        }
    }

    @Override
    public AxisAlignedBB getBoundingBox(IBlockState state, IBlockAccess source, BlockPos pos) {
        int age = state.getValue(AGE);
        switch (age) {
            case 0:
                return bb0;
            case 1:
                return bb1;
            case 2:
                return bb2;
            case 3:
                return bb3;
            case 4:
                return bb4;
            case 5:
                return bb5;
            case 6:
            case 7:
                return bb67;
        }
        return FULL_BLOCK_AABB;
    }

    @Nullable
    @Override
    public AxisAlignedBB getCollisionBoundingBox(IBlockState blockState, IBlockAccess worldIn, BlockPos pos) {
        return NULL_AABB;
    }

    @Override
    public boolean isOpaqueCube(IBlockState state) {
        return false;
    }

    @Override
    public boolean isFullCube(IBlockState state) {
        return false;
    }

    @Override
    @SideOnly(Side.CLIENT)
    public BlockRenderLayer getBlockLayer() {
        return BlockRenderLayer.CUTOUT;
    }

    @Override
    public int quantityDropped(Random random) {
        return 0;
    }

    @Override
    public Item getItemDropped(IBlockState state, Random rand, int fortune) {
        return net.minecraft.init.Items.AIR;
    }

    @Override
    public IBlockState getStateFromMeta(int meta) {
        return getDefaultState().withProperty(AGE, meta);
    }

    @Override
    public int getMetaFromState(IBlockState state) {
        return state.getValue(AGE);
    }

    @Override
    protected BlockStateContainer createBlockState() {
        return new BlockStateContainer(this, AGE);
    }

    @Override
    public void updateTick(World worldIn, BlockPos pos, IBlockState state, Random rand) {
        if(!isValidAtPosition(worldIn, pos)) {
            this.dropBlockAsItem(worldIn, pos, state, 0);
            worldIn.setBlockState(pos, Blocks.AIR.getDefaultState(), 3);
            return;
        }
        TileScientificCrop crop = getCropTile(worldIn, pos);
        if(crop == null || crop.getSeedItemStack().isEmpty()) {
            this.dropBlockAsItem(worldIn, pos, state, 0);
            worldIn.setBlockState(pos, Blocks.AIR.getDefaultState(), 3);
            return;
        }
        if (worldIn.getLightFromNeighbors(pos.up()) >= 9) {
            int i = this.getAge(state);

            if (i < this.getMaxAge()) {

                float change = getGrowthChance(this, worldIn, pos);
                if(ForgeHooks.onCropsGrowPre(worldIn, pos, state, rand.nextInt((int)(25.0F / change) + 1) == 0)) {
                    worldIn.setBlockState(pos, getDefaultState().withProperty(AGE, i + 1), 3);
                    ForgeHooks.onCropsGrowPost(worldIn, pos, state, worldIn.getBlockState(pos));
                }
            }
        }
    }

    @Nullable
    public static TileScientificCrop getCropTile(IBlockAccess world, BlockPos pos) {
        TileEntity crop = world.getTileEntity(pos);
        if(crop == null || !(crop instanceof TileScientificCrop)) return null;
        return (TileScientificCrop) crop;
    }

    private static float getGrowthChance(Block blockIn, World worldIn, BlockPos pos) {
        float chance = 1.0F;
        BlockPos blockpos = pos.down();

        for (int x = -1; x <= 1; ++x) {
            for (int z = -1; z <= 1; ++z) {
                float f1 = 0.0F;
                IBlockState iblockstate = worldIn.getBlockState(blockpos.add(x, 0, z));

                if (iblockstate.getBlock().canSustainPlant(iblockstate, worldIn, blockpos.add(x, 0, z), net.minecraft.util.EnumFacing.UP, (net.minecraftforge.common.IPlantable) blockIn)) {
                    f1 = 1.0F;

                    if (iblockstate.getBlock().isFertile(worldIn, blockpos.add(x, 0, z))) {
                        f1 = 3.0F;
                    }
                }

                if (x != 0 || z != 0) {
                    f1 /= 4.0F;
                }

                chance += f1;
            }
        }

        BlockPos north = pos.north();
        BlockPos south = pos.south();
        BlockPos west = pos.west();
        BlockPos east = pos.east();
        boolean hasPlantHorizontal = blockIn == worldIn.getBlockState(west).getBlock() || blockIn == worldIn.getBlockState(east).getBlock();
        boolean hasPlantVertical = blockIn == worldIn.getBlockState(north).getBlock() || blockIn == worldIn.getBlockState(south).getBlock();

        if (hasPlantHorizontal && hasPlantVertical) {
            chance /= 2.0F;
        } else {
            boolean hasPlantNextToCorners = blockIn == worldIn.getBlockState(west.north()).getBlock() || blockIn == worldIn.getBlockState(east.north()).getBlock() || blockIn == worldIn.getBlockState(east.south()).getBlock() || blockIn == worldIn.getBlockState(west.south()).getBlock();
            if (hasPlantNextToCorners) {
                chance /= 2.0F;
            }
        }
        return chance;
    }

    public static boolean isValidAtPosition(World world, BlockPos pos) {
        IBlockState down = world.getBlockState(pos.down());
        return down.getBlock() == Blocks.FARMLAND;
    }

    public int getMaxAge() {
        return 7;
    }

    public int getAge(IBlockState state) {
        return state.getValue(AGE);
    }

    public boolean isMaxAge(IBlockState state) {
        return state.getValue(AGE) >= this.getMaxAge();
    }

    @Override
    public boolean canGrow(World worldIn, BlockPos pos, IBlockState state, boolean isClient) {
        return !isMaxAge(state);
    }

    @Override
    public boolean canUseBonemeal(World worldIn, Random rand, BlockPos pos, IBlockState state) {
        return true;
    }

    @Override
    public void grow(World worldIn, Random rand, BlockPos pos, IBlockState state) {
        int i = this.getAge(state) + MathHelper.getInt(worldIn.rand, 2, 5);
        int j = this.getMaxAge();

        if (i > j) {
            i = j;
        }

        worldIn.setBlockState(pos, getDefaultState().withProperty(AGE, i), 3);
    }

    @Override
    public EnumPlantType getPlantType(IBlockAccess world, BlockPos pos) {
        return EnumPlantType.Crop;
    }

    @Override
    public IBlockState getPlant(IBlockAccess world, BlockPos pos) {
        return getDefaultState();
    }

}
