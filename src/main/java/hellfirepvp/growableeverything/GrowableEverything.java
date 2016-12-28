package hellfirepvp.growableeverything;

import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.common.event.FMLInitializationEvent;
import net.minecraftforge.fml.common.event.FMLPostInitializationEvent;
import net.minecraftforge.fml.common.event.FMLPreInitializationEvent;

/**
 * This class is part of the GrowableEverything Mod
 * GrowableEverything is Open Source and distributed under the
 * GNU LESSER GENERAL PUBLIC LICENSE
 * for more read the LICENSE file
 * <p>
 * Created by HellFirePvP @ 28.12.2016 18:28
 */
@Mod(modid = GrowableEverything.MODID, name = GrowableEverything.NAME, version = GrowableEverything.VERSION)
public class GrowableEverything {

    public static final String MODID = "growableeverything";
    public static final String NAME = "GrowableEverything";
    public static final String VERSION = "0.1-indev";
    public static final String CLIENT_PROXY = "hellfirepvp.astralsorcery.client.ClientProxy";
    public static final String COMMON_PROXY = "hellfirepvp.astralsorcery.common.CommonProxy";

    @Mod.Instance(MODID)
    public static GrowableEverything instance;

    @Mod.EventHandler
    public void preInit(FMLPreInitializationEvent event) {

    }

    @Mod.EventHandler
    public void init(FMLInitializationEvent event) {

    }

    @Mod.EventHandler
    public void postInit(FMLPostInitializationEvent event) {
        
    }

}
