package hellfirepvp.growableeverything.block;

import com.google.common.collect.Lists;
import net.minecraft.block.Block;
import net.minecraft.block.BlockCrops;
import net.minecraft.block.IGrowable;
import net.minecraft.block.SoundType;
import net.minecraft.block.material.Material;
import net.minecraft.block.properties.PropertyInteger;
import net.minecraft.block.state.BlockStateContainer;
import net.minecraft.block.state.IBlockState;
import net.minecraft.init.Blocks;
import net.minecraft.item.ItemStack;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.MathHelper;
import net.minecraft.world.IBlockAccess;
import net.minecraft.world.World;
import net.minecraftforge.common.ForgeHooks;

import java.util.List;
import java.util.Random;

/**
 * This class is part of the GrowableEverything Mod
 * GrowableEverything is Open Source and distributed under the
 * GNU LESSER GENERAL PUBLIC LICENSE
 * for more read the LICENSE file
 * <p>
 * Created by HellFirePvP @ 28.12.2016 19:15
 */
public class BlockScientificCrop extends Block implements IGrowable {

    public static final PropertyInteger AGE = PropertyInteger.create("age", 0, 7);

    public BlockScientificCrop() {
        super(Material.PLANTS);
        this.setTickRandomly(true);
        this.setHardness(0.0F);
        this.setSoundType(SoundType.PLANT);
        this.disableStats();
    }

    @Override
    public List<ItemStack> getDrops(IBlockAccess world, BlockPos pos, IBlockState state, int fortune) {
        return Lists.newArrayList();
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
        } else {
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
    }

    private static float getGrowthChance(Block blockIn, World worldIn, BlockPos pos) {
        float chance = 1.0F;
        BlockPos blockpos = pos.down();

        for (int x = -1; x <= 1; ++x) {
            for (int z = -1; z <= 1; ++z) {
                float f1 = 0.0F;
                IBlockState iblockstate = worldIn.getBlockState(blockpos.add(x, 0, z));

                if (iblockstate.getBlock().canSustainPlant(iblockstate, worldIn, blockpos.add(x, 0, z), net.minecraft.util.EnumFacing.UP, (net.minecraftforge.common.IPlantable)blockIn)) {
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

}
