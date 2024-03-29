package toni.sound_physics.integration;

import toni.sound_physics.Loggers;
import toni.sound_physics.SoundPhysicsMod;
import toni.sound_physics.config.blocksound.BlockDefinition;
import de.maxhenkel.configbuilder.entry.*;
import me.shedaniel.clothconfig2.api.AbstractConfigListEntry;
import me.shedaniel.clothconfig2.api.ConfigBuilder;
import me.shedaniel.clothconfig2.api.ConfigCategory;
import me.shedaniel.clothconfig2.api.ConfigEntryBuilder;
import me.shedaniel.clothconfig2.gui.entries.FloatListEntry;
import net.minecraft.client.gui.screens.Screen;
import net.minecraft.network.chat.Component;

import java.util.LinkedHashMap;
import java.util.Map;

public class ClothConfigIntegration {

    public static Screen createConfigScreen(Screen parent) {
        ConfigBuilder builder = ConfigBuilder
                .create()
                .setParentScreen(parent)
                .setTitle(Component.translatable("cloth_config.sound_physics.settings"));

        ConfigEntryBuilder entryBuilder = builder.entryBuilder();

        ConfigCategory general = builder.getOrCreateCategory(Component.translatable("cloth_config.sound_physics.category.general"));

        general.addEntry(fromConfigEntry(entryBuilder,
                Component.translatable("cloth_config.sound_physics.enabled"),
                Component.translatable("cloth_config.sound_physics.enabled.description"),
                SoundPhysicsMod.CONFIG.enabled
        ));
        general.addEntry(fromConfigEntry(entryBuilder,
                Component.translatable("cloth_config.sound_physics.simple_voice_chat_integration"),
                Component.translatable("cloth_config.sound_physics.simple_voice_chat_integration.description"),
                SoundPhysicsMod.CONFIG.simpleVoiceChatIntegration
        ));
        general.addEntry(fromConfigEntry(entryBuilder,
                Component.translatable("cloth_config.sound_physics.simple_voice_chat_hear_self"),
                Component.translatable("cloth_config.sound_physics.simple_voice_chat_hear_self.description"),
                SoundPhysicsMod.CONFIG.hearSelf
        ));
        general.addEntry(fromConfigEntry(entryBuilder,
                Component.translatable("cloth_config.sound_physics.attenuation_factor"),
                Component.translatable("cloth_config.sound_physics.attenuation_factor.description"),
                SoundPhysicsMod.CONFIG.attenuationFactor
        ));
        general.addEntry(fromConfigEntry(entryBuilder,
                Component.translatable("cloth_config.sound_physics.reverb_gain"),
                Component.translatable("cloth_config.sound_physics.reverb_gain.description"),
                SoundPhysicsMod.CONFIG.reverbGain
        ));
        general.addEntry(fromConfigEntry(entryBuilder,
                Component.translatable("cloth_config.sound_physics.reverb_brightness"),
                Component.translatable("cloth_config.sound_physics.reverb_brightness.description"),
                SoundPhysicsMod.CONFIG.reverbBrightness
        ));
        general.addEntry(fromConfigEntry(entryBuilder,
                Component.translatable("cloth_config.sound_physics.block_absorption"),
                Component.translatable("cloth_config.sound_physics.block_absorption.description"),
                SoundPhysicsMod.CONFIG.blockAbsorption
        ));
        general.addEntry(fromConfigEntry(entryBuilder,
                Component.translatable("cloth_config.sound_physics.occlusion_variation"),
                Component.translatable("cloth_config.sound_physics.occlusion_variation.description"),
                SoundPhysicsMod.CONFIG.occlusionVariation
        ));
        general.addEntry(fromConfigEntry(entryBuilder,
                Component.translatable("cloth_config.sound_physics.default_block_reflectivity"),
                Component.translatable("cloth_config.sound_physics.default_block_reflectivity.description"),
                SoundPhysicsMod.CONFIG.defaultBlockReflectivity
        ));
        general.addEntry(fromConfigEntry(entryBuilder,
                Component.translatable("cloth_config.sound_physics.default_block_occlusion_factor"),
                Component.translatable("cloth_config.sound_physics.default_block_occlusion_factor.description"),
                SoundPhysicsMod.CONFIG.defaultBlockOcclusionFactor
        ));
        general.addEntry(fromConfigEntry(entryBuilder,
                Component.translatable("cloth_config.sound_physics.sound_distance_allowance"),
                Component.translatable("cloth_config.sound_physics.sound_distance_allowance.description"),
                SoundPhysicsMod.CONFIG.soundDistanceAllowance
        ));
        general.addEntry(fromConfigEntry(entryBuilder,
                Component.translatable("cloth_config.sound_physics.air_absorption"),
                Component.translatable("cloth_config.sound_physics.air_absorption.description"),
                SoundPhysicsMod.CONFIG.airAbsorption
        ));
        general.addEntry(fromConfigEntry(entryBuilder,
                Component.translatable("cloth_config.sound_physics.underwater_filter"),
                Component.translatable("cloth_config.sound_physics.underwater_filter.description"),
                SoundPhysicsMod.CONFIG.underwaterFilter
        ));
        general.addEntry(fromConfigEntry(entryBuilder,
                Component.translatable("cloth_config.sound_physics.evaluate_ambient_sounds"),
                Component.translatable("cloth_config.sound_physics.evaluate_ambient_sounds.description"),
                SoundPhysicsMod.CONFIG.evaluateAmbientSounds
        ));

        ConfigCategory performance = builder.getOrCreateCategory(Component.translatable("cloth_config.sound_physics.category.performance"));

        performance.addEntry(fromConfigEntry(entryBuilder,
                Component.translatable("cloth_config.sound_physics.environment_evaluation_ray_count"),
                Component.translatable("cloth_config.sound_physics.environment_evaluation_ray_count.description"),
                SoundPhysicsMod.CONFIG.environmentEvaluationRayCount
        ));
        performance.addEntry(fromConfigEntry(entryBuilder,
                Component.translatable("cloth_config.sound_physics.environment_evaluation_ray_bounces"),
                Component.translatable("cloth_config.sound_physics.environment_evaluation_ray_bounces.description"),
                SoundPhysicsMod.CONFIG.environmentEvaluationRayBounces
        ));
        performance.addEntry(fromConfigEntry(entryBuilder,
                Component.translatable("cloth_config.sound_physics.non_full_block_occlusion_factor"),
                Component.translatable("cloth_config.sound_physics.non_full_block_occlusion_factor.description"),
                SoundPhysicsMod.CONFIG.nonFullBlockOcclusionFactor
        ));
        performance.addEntry(fromConfigEntry(entryBuilder,
                Component.translatable("cloth_config.sound_physics.max_occlusion_rays"),
                Component.translatable("cloth_config.sound_physics.max_occlusion_rays.description"),
                SoundPhysicsMod.CONFIG.maxOcclusionRays
        ));
        performance.addEntry(fromConfigEntry(entryBuilder,
                Component.translatable("cloth_config.sound_physics.max_occlusion"),
                Component.translatable("cloth_config.sound_physics.max_occlusion.description"),
                SoundPhysicsMod.CONFIG.maxOcclusion
        ));
        performance.addEntry(fromConfigEntry(entryBuilder,
                Component.translatable("cloth_config.sound_physics.strict_occlusion"),
                Component.translatable("cloth_config.sound_physics.strict_occlusion.description"),
                SoundPhysicsMod.CONFIG.strictOcclusion
        ));
        performance.addEntry(fromConfigEntry(entryBuilder,
                Component.translatable("cloth_config.sound_physics.sound_direction_evaluation"),
                Component.translatable("cloth_config.sound_physics.sound_direction_evaluation.description"),
                SoundPhysicsMod.CONFIG.soundDirectionEvaluation
        ));
        performance.addEntry(fromConfigEntry(entryBuilder,
                Component.translatable("cloth_config.sound_physics.redirect_non_occluded_sounds"),
                Component.translatable("cloth_config.sound_physics.redirect_non_occluded_sounds.description"),
                SoundPhysicsMod.CONFIG.redirectNonOccludedSounds
        ));
        performance.addEntry(fromConfigEntry(entryBuilder,
                Component.translatable("cloth_config.sound_physics.update_moving_sounds"),
                Component.translatable("cloth_config.sound_physics.update_moving_sounds.description"),
                SoundPhysicsMod.CONFIG.updateMovingSounds
        ));
        performance.addEntry(fromConfigEntry(entryBuilder,
                Component.translatable("cloth_config.sound_physics.sound_update_interval"),
                Component.translatable("cloth_config.sound_physics.sound_update_interval.description"),
                SoundPhysicsMod.CONFIG.soundUpdateInterval
        ));

        ConfigCategory reflectivity = builder.getOrCreateCategory(Component.translatable("cloth_config.sound_physics.category.reflectivity"));

        Map<BlockDefinition, Float> defaultReflectivityMap = new LinkedHashMap<>();
        SoundPhysicsMod.REFLECTIVITY_CONFIG.addDefaults(defaultReflectivityMap);

        for (Map.Entry<BlockDefinition, Float> entry : SoundPhysicsMod.REFLECTIVITY_CONFIG.getBlockDefinitions().entrySet()) {
            FloatListEntry e = entryBuilder
                    .startFloatField(entry.getKey().getName(), entry.getValue())
                    .setMin(0.01F)
                    .setMax(10F)
                    .setDefaultValue(defaultReflectivityMap.getOrDefault(entry.getKey(), SoundPhysicsMod.CONFIG.defaultBlockReflectivity.get()))
                    .setSaveConsumer(value -> SoundPhysicsMod.REFLECTIVITY_CONFIG.setBlockDefinitionValue(entry.getKey(), value)).build();
            reflectivity.addEntry(e);
        }

        ConfigCategory occlusion = builder.getOrCreateCategory(Component.translatable("cloth_config.sound_physics.category.occlusion"));

        Map<BlockDefinition, Float> defaultOcclusionMap = new LinkedHashMap<>();
        SoundPhysicsMod.OCCLUSION_CONFIG.addDefaults(defaultOcclusionMap);

        for (Map.Entry<BlockDefinition, Float> entry : SoundPhysicsMod.OCCLUSION_CONFIG.getBlockDefinitions().entrySet()) {
            FloatListEntry e = entryBuilder
                    .startFloatField(entry.getKey().getName(), entry.getValue())
                    .setMin(0F)
                    .setMax(10F)
                    .setDefaultValue(defaultOcclusionMap.getOrDefault(entry.getKey(), SoundPhysicsMod.CONFIG.defaultBlockOcclusionFactor.get()))
                    .setSaveConsumer(value -> SoundPhysicsMod.OCCLUSION_CONFIG.setBlockDefinitionValue(entry.getKey(), value)).build();
            occlusion.addEntry(e);
        }

        ConfigCategory logging = builder.getOrCreateCategory(Component.translatable("cloth_config.sound_physics.category.debug"));

        logging.addEntry(fromConfigEntry(entryBuilder,
                Component.translatable("cloth_config.sound_physics.debug_logging"),
                Component.translatable("cloth_config.sound_physics.debug_logging.description"),
                SoundPhysicsMod.CONFIG.debugLogging
        ));
        logging.addEntry(fromConfigEntry(entryBuilder,
                Component.translatable("cloth_config.sound_physics.occlusion_logging"),
                Component.translatable("cloth_config.sound_physics.occlusion_logging.description"),
                SoundPhysicsMod.CONFIG.occlusionLogging
        ));
        logging.addEntry(fromConfigEntry(entryBuilder,
                Component.translatable("cloth_config.sound_physics.environment_logging"),
                Component.translatable("cloth_config.sound_physics.environment_logging.description"),
                SoundPhysicsMod.CONFIG.environmentLogging
        ));
        logging.addEntry(fromConfigEntry(entryBuilder,
                Component.translatable("cloth_config.sound_physics.performance_logging"),
                Component.translatable("cloth_config.sound_physics.performance_logging.description"),
                SoundPhysicsMod.CONFIG.performanceLogging
        ));
        logging.addEntry(fromConfigEntry(entryBuilder,
                Component.translatable("cloth_config.sound_physics.render_sound_bounces"),
                Component.translatable("cloth_config.sound_physics.render_sound_bounces.description"),
                SoundPhysicsMod.CONFIG.renderSoundBounces
        ));
        logging.addEntry(fromConfigEntry(entryBuilder,
                Component.translatable("cloth_config.sound_physics.render_occlusion"),
                Component.translatable("cloth_config.sound_physics.render_occlusion.description"),
                SoundPhysicsMod.CONFIG.renderOcclusion
        ));

        builder.setSavingRunnable(() -> {
            Loggers.LOGGER.info("Saving configs");
            SoundPhysicsMod.CONFIG.enabled.save();
            SoundPhysicsMod.REFLECTIVITY_CONFIG.save();
            SoundPhysicsMod.OCCLUSION_CONFIG.save();
            SoundPhysicsMod.ALLOWED_SOUND_CONFIG.save();
            SoundPhysicsMod.CONFIG.reloadClient();
        });

        return builder.build();
    }

    private static <T> AbstractConfigListEntry<T> fromConfigEntry(ConfigEntryBuilder entryBuilder, Component name, Component description, ConfigEntry<T> entry) {
        if (entry instanceof DoubleConfigEntry e) {
            return (AbstractConfigListEntry<T>) entryBuilder
                    .startDoubleField(name, e.get())
                    .setTooltip(description)
                    .setMin(e.getMin())
                    .setMax(e.getMax())
                    .setDefaultValue(e::getDefault)
                    .setSaveConsumer(d -> e.set(d))
                    .build();
        } else if (entry instanceof FloatConfigEntry e) {
            return (AbstractConfigListEntry<T>) entryBuilder
                    .startFloatField(name, e.get())
                    .setTooltip(description)
                    .setMin(e.getMin())
                    .setMax(e.getMax())
                    .setDefaultValue(e::getDefault)
                    .setSaveConsumer(d -> e.set(d))
                    .build();
        } else if (entry instanceof IntegerConfigEntry e) {
            return (AbstractConfigListEntry<T>) entryBuilder
                    .startIntField(name, e.get())
                    .setTooltip(description)
                    .setMin(e.getMin())
                    .setMax(e.getMax())
                    .setDefaultValue(e::getDefault)
                    .setSaveConsumer(i -> e.set(i))
                    .build();
        } else if (entry instanceof BooleanConfigEntry e) {
            return (AbstractConfigListEntry<T>) entryBuilder
                    .startBooleanToggle(name, e.get())
                    .setTooltip(description)
                    .setDefaultValue(e::getDefault)
                    .setSaveConsumer(b -> e.set(b))
                    .build();
        } else if (entry instanceof StringConfigEntry e) {
            return (AbstractConfigListEntry<T>) entryBuilder
                    .startStrField(name, e.get())
                    .setTooltip(description)
                    .setDefaultValue(e::getDefault)
                    .setSaveConsumer(s -> e.set(s))
                    .build();
        }

        return null;
    }

}
