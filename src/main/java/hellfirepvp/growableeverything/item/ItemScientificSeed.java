package hellfirepvp.growableeverything.item;

import com.mojang.realmsclient.gui.ChatFormatting;
import hellfirepvp.growableeverything.block.BlockScientificCrop;
import hellfirepvp.growableeverything.block.tile.TileScientificCrop;
import hellfirepvp.growableeverything.lib.Blocks;
import hellfirepvp.growableeverything.util.NBTHelper;
import net.minecraft.block.state.IBlockState;
import net.minecraft.client.resources.I18n;
import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.init.SoundEvents;
import net.minecraft.item.Item;
import net.minecraft.item.ItemSeeds;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.util.*;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.IBlockAccess;
import net.minecraft.world.World;
import net.minecraftforge.common.EnumPlantType;
import net.minecraftforge.common.IPlantable;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

import javax.annotation.Nullable;
import java.util.List;

/**
 * This class is part of the GrowableEverything Mod
 * GrowableEverything is Open Source and distributed under the
 * GNU LESSER GENERAL PUBLIC LICENSE
 * for more read the LICENSE file
 * <p>
 * Created by HellFirePvP @ 28.12.2016 19:09
 */
public class ItemScientificSeed extends Item implements IPlantable {

    public ItemScientificSeed() {
        this.setMaxStackSize(64);
        this.setCreativeTab(CreativeTabs.MATERIALS);
    }

    @Override
    @SideOnly(Side.CLIENT)
    public void addInformation(ItemStack stack, EntityPlayer playerIn, List<String> tooltip, boolean advanced) {
        if(stack.isEmpty()) return;
        ItemStack container = getCropItem(stack);
        if (!container.isEmpty()) {
            String tr = I18n.format(container.getUnlocalizedName() + ".name");
            tooltip.add(ChatFormatting.BLUE + I18n.format("misc.grow.info", tr));
        }
    }

    @Override
    public EnumActionResult onItemUse(EntityPlayer player, World worldIn, BlockPos pos, EnumHand hand, EnumFacing facing, float hitX, float hitY, float hitZ) {
        if(worldIn.isRemote) {
            return EnumActionResult.PASS;
        } else {
            ItemStack itemstack = player.getHeldItem(hand);
            IBlockState state = worldIn.getBlockState(pos);
            if (facing == EnumFacing.UP &&
                    player.canPlayerEdit(pos.offset(facing), facing, itemstack) &&
                    state.getBlock().canSustainPlant(state, worldIn, pos, EnumFacing.UP, this) &&
                    worldIn.isAirBlock(pos.up())) {

                BlockPos at = pos.up();
                worldIn.setBlockState(at, Blocks.blockCrop.getDefaultState(), 3);
                TileScientificCrop crop = BlockScientificCrop.getCropTile(worldIn, at);
                if(crop != null) {
                    crop.setSeedItemStack(getCropItem(itemstack));
                }
                worldIn.playSound(null, pos, SoundEvents.BLOCK_GRASS_BREAK, SoundCategory.BLOCKS, 1F, 1F);
                itemstack.shrink(1);
                return EnumActionResult.SUCCESS;
            } else {
                return EnumActionResult.FAIL;
            }
        }
    }

    public static void setCropItem(ItemStack scientificSeed, ItemStack otherItem) {
        if(scientificSeed.isEmpty() || !(scientificSeed.getItem() instanceof ItemScientificSeed)) return;

        NBTTagCompound cmp = NBTHelper.getPersistentData(scientificSeed);
        NBTHelper.setStack(cmp, "CropStack", otherItem);
    }

    public static ItemStack getCropItem(ItemStack scientificSeed) {
        if(scientificSeed.isEmpty() || !(scientificSeed.getItem() instanceof ItemScientificSeed)) return ItemStack.EMPTY;

        NBTTagCompound cmp = NBTHelper.getPersistentData(scientificSeed);
        return NBTHelper.getStack(cmp, "CropStack", ItemStack.EMPTY);
    }

    @Override
    public EnumPlantType getPlantType(IBlockAccess world, BlockPos pos) {
        return EnumPlantType.Crop;
    }

    @Override
    public IBlockState getPlant(IBlockAccess world, BlockPos pos) {
        return Blocks.blockCrop.getDefaultState();
    }
}
