package hellfirepvp.growableeverything.block.tile;

import hellfirepvp.growableeverything.util.NBTHelper;
import net.minecraft.block.state.IBlockState;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;

/**
 * This class is part of the GrowableEverything Mod
 * GrowableEverything is Open Source and distributed under the
 * GNU LESSER GENERAL PUBLIC LICENSE
 * for more read the LICENSE file
 * <p>
 * Created by HellFirePvP @ 28.12.2016 19:24
 */
public class TileScientificCrop extends TileEntitySynchronized {

    private ItemStack seedItemStack = ItemStack.EMPTY;

    public void setSeedItemStack(ItemStack seedItemStack) {
        this.seedItemStack = seedItemStack;
        markForUpdate();
    }

    public ItemStack getSeedItemStack() {
        return seedItemStack;
    }

    @Override
    public boolean shouldRefresh(World world, BlockPos pos, IBlockState oldState, IBlockState newSate) {
        return oldState.getBlock() != newSate.getBlock();
    }

    @Override
    public void readCustomNBT(NBTTagCompound compound) {
        super.readCustomNBT(compound);

        this.seedItemStack = NBTHelper.getStack(compound, "CropStack", ItemStack.EMPTY);
    }

    @Override
    public void writeCustomNBT(NBTTagCompound compound) {
        super.writeCustomNBT(compound);

        NBTHelper.setStack(compound, "CropStack", seedItemStack);
    }
}
