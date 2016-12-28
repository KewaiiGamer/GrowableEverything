package hellfirepvp.growableeverything.registry;

import hellfirepvp.growableeverything.GrowableEverything;
import hellfirepvp.growableeverything.block.BlockScientificCrop;
import hellfirepvp.growableeverything.block.tile.TileScientificCrop;
import net.minecraft.block.Block;
import net.minecraft.tileentity.TileEntity;
import net.minecraftforge.fml.common.registry.GameRegistry;

import static hellfirepvp.growableeverything.lib.Blocks.*;

/**
 * This class is part of the GrowableEverything Mod
 * GrowableEverything is Open Source and distributed under the
 * GNU LESSER GENERAL PUBLIC LICENSE
 * for more read the LICENSE file
 * <p>
 * Created by HellFirePvP @ 28.12.2016 19:24
 */
public class RegistryBlocks {

    public static void init() {
        blockCrop = registerBlock(new BlockScientificCrop());
    }

    public static void initTiles() {
        registerTile(TileScientificCrop.class);
    }

    private static void registerTile(Class<? extends TileEntity> teClass) {
        GameRegistry.registerTileEntity(teClass, GrowableEverything.MODID + ":" + teClass.getSimpleName().toLowerCase());
    }

    private static <T extends Block> T registerBlock(T i) {
        i.setRegistryName(i.getClass().getSimpleName()).setUnlocalizedName(i.getClass().getSimpleName());
        GameRegistry.register(i);
        return i;
    }

}
