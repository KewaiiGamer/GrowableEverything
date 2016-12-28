package hellfirepvp.growableeverything.registry;

import hellfirepvp.growableeverything.item.ItemScientificSeed;
import net.minecraft.item.Item;
import net.minecraftforge.fml.common.registry.GameRegistry;

import static hellfirepvp.growableeverything.lib.Items.*;

/**
 * This class is part of the GrowableEverything Mod
 * GrowableEverything is Open Source and distributed under the
 * GNU LESSER GENERAL PUBLIC LICENSE
 * for more read the LICENSE file
 * <p>
 * Created by HellFirePvP @ 28.12.2016 19:18
 */
public class RegistryItems {

    public static void init() {
        itemSeed = registerItem(new ItemScientificSeed());
    }

    private static <T extends Item> T registerItem(T i) {
        i.setRegistryName(i.getClass().getSimpleName()).setUnlocalizedName(i.getClass().getSimpleName());
        GameRegistry.register(i);
        return i;
    }

}
