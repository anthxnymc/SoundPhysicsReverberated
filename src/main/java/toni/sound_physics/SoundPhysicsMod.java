package toni.sound_physics;

import toni.sound_physics.config.AllowedSoundConfig;
import toni.sound_physics.config.OcclusionConfig;
import toni.sound_physics.config.ReflectivityConfig;
import toni.sound_physics.config.SoundPhysicsConfig;
import toni.sound_physics.integration.ClothConfigIntegration;
import de.maxhenkel.configbuilder.ConfigBuilder;

import java.nio.file.Path;

#if FABRIC
import net.fabricmc.api.ClientModInitializer;
import net.fabricmc.api.ModInitializer;
import net.fabricmc.loader.api.FabricLoader;

#else
import net.minecraftforge.fml.ModList;
import net.minecraftforge.client.ConfigScreenHandler;
import net.minecraftforge.fml.ModLoadingContext;
import net.minecraftforge.fml.event.lifecycle.FMLClientSetupEvent;
import net.minecraftforge.fml.event.lifecycle.FMLCommonSetupEvent;
import net.minecraftforge.fml.javafmlmod.FMLJavaModLoadingContext;
import net.minecraftforge.fml.loading.FMLLoader;
#endif

public class SoundPhysicsMod #if FABRIC implements ModInitializer, ClientModInitializer #endif {

    public static final String MODID = "sound_physics";

    public static SoundPhysicsConfig CONFIG;
    public static ReflectivityConfig REFLECTIVITY_CONFIG;
    public static OcclusionConfig OCCLUSION_CONFIG;
    public static AllowedSoundConfig ALLOWED_SOUND_CONFIG;

    SoundPhysicsMod() {
        #if FORGE
        FMLJavaModLoadingContext.get().getModEventBus().addListener(this::commonSetup);
        FMLJavaModLoadingContext.get().getModEventBus().addListener(this::clientSetup);
        #endif
    }


    #if FABRIC @Override #endif
    public void onInitialize() {
        initConfig();
    }

    #if FABRIC @Override #endif
    public void onInitializeClient() {
        initConfig();
        CONFIG.reloadClient();

        REFLECTIVITY_CONFIG = new ReflectivityConfig(getConfigFolder().resolve(MODID).resolve("reflectivity.properties"));
        OCCLUSION_CONFIG = new OcclusionConfig(getConfigFolder().resolve(MODID).resolve("occlusion.properties"));
        ALLOWED_SOUND_CONFIG = new AllowedSoundConfig(getConfigFolder().resolve(MODID).resolve("allowed_sounds.properties"));
    }

    private void initConfig() {
        if (CONFIG == null) {
            CONFIG = ConfigBuilder.builder(SoundPhysicsConfig::new).path(getConfigFolder().resolve(MODID).resolve("soundphysics.properties")).build();
        }
    }

    public Path getConfigFolder() {
        #if FORGE
        return FMLLoader.getGamePath().resolve("config");
        #else
        return FabricLoader.getInstance().getConfigDir();
        #endif
    }


    // Forg event stubs to call the Fabric initialize methods, and set up cloth config screen
    #if FORGE
    public void commonSetup(FMLCommonSetupEvent event) {
        onInitialize();
    }

    public void clientSetup(FMLClientSetupEvent event) {
        onInitializeClient();
        if (!isClothConfigLoaded())
            return;

        ModLoadingContext.get().registerExtensionPoint(ConfigScreenHandler.ConfigScreenFactory.class, () -> new ConfigScreenHandler.ConfigScreenFactory((client, parent) -> ClothConfigIntegration.createConfigScreen(parent)));
    }

    private static boolean isClothConfigLoaded() {
        if (ModList.get().isLoaded("cloth_config")) {
            try {
                Class.forName("me.shedaniel.clothconfig2.api.ConfigBuilder");
                Loggers.LOGGER.info("Using Cloth Config GUI");
                return true;
            } catch (Exception e) {
                Loggers.LOGGER.warn("Failed to load Cloth Config: {}", e.getMessage());
                e.printStackTrace();
            }
        }
        return false;
    }
    #endif

}
