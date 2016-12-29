package hellfirepvp.growableeverything.client;

import hellfirepvp.growableeverything.CommonProxy;
import hellfirepvp.growableeverything.block.tile.TileScientificCrop;
import hellfirepvp.growableeverything.lib.Items;
import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.ItemModelMesher;
import net.minecraft.client.renderer.block.model.ModelResourceLocation;
import net.minecraftforge.fml.client.registry.ClientRegistry;

/**
 * This class is part of the GrowableEverything Mod
 * GrowableEverything is Open Source and distributed under the
 * GNU LESSER GENERAL PUBLIC LICENSE
 * for more read the LICENSE file
 * <p>
 * Created by HellFirePvP @ 28.12.2016 22:08
 */
public class ClientProxy extends CommonProxy {

    @Override
    public void initRendering() {
        ItemModelMesher imm = Minecraft.getMinecraft().getRenderItem().getItemModelMesher();
        imm.register(Items.itemSeed, 0, new ModelResourceLocation(Items.itemSeed.getRegistryName(), "inventory"));

        ClientRegistry.bindTileEntitySpecialRenderer(TileScientificCrop.class, new TESRCrop());
    }

}
