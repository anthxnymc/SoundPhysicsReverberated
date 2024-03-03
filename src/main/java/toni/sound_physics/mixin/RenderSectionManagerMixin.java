package toni.sound_physics.mixin;

import me.jellysquid.mods.sodium.client.render.chunk.compile.tasks.ChunkBuilderMeshingTask;

import toni.sound_physics.SectionCache;
import toni.sound_physics.SoundPhysicsMod;
import toni.sound_physics.utils.SectionCacheAccess;
import toni.sound_physics.utils.SoundPhysicsClonedChunkSection;
import it.unimi.dsi.fastutil.longs.Long2ReferenceMap;
import me.jellysquid.mods.sodium.client.render.chunk.RenderSection;
import me.jellysquid.mods.sodium.client.render.chunk.RenderSectionManager;
import me.jellysquid.mods.sodium.client.world.cloned.ChunkRenderContext;
import me.jellysquid.mods.sodium.client.world.cloned.ClonedChunkSection;
import me.jellysquid.mods.sodium.client.world.cloned.ClonedChunkSectionCache;
import net.minecraft.client.Minecraft;
import net.minecraft.client.multiplayer.ClientLevel;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.chunk.PalettedContainer;
import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;
import org.spongepowered.asm.mixin.injection.callback.LocalCapture;

@Mixin(value = RenderSectionManager.class, remap = false)
public class RenderSectionManagerMixin implements SectionCacheAccess
{
    @Shadow
    @Final
    private ClonedChunkSectionCache sectionCache;

    @Shadow @Final private Long2ReferenceMap<RenderSection> sectionByPosition;

    @Shadow @Final private ClientLevel world;



    @Override
    public ClonedChunkSectionCache sound_physics$getSectionCache()
    {
        return sectionCache;
    }

    @Inject(locals = LocalCapture.CAPTURE_FAILHARD, method = "createRebuildTask", at = @At(value = "RETURN"))
    private void processChunkBuildResults(RenderSection render, int frame, CallbackInfoReturnable<ChunkBuilderMeshingTask> cir, ChunkRenderContext context) {

        var level = Minecraft.getInstance().level;
        if (context == null || level == null)
            return;

        if (!SoundPhysicsMod.CONFIG.enabled.get())
            return;

        // Unpack data for each section
        for (ClonedChunkSection section : context.getSections()) {
            var sectionPos = section.getPosition();

            var blockData = (PalettedContainer<BlockState>) section.getBlockData();

            if (blockData == null)
                continue;

            var newSectionData = blockData.copy();
            SoundPhysicsClonedChunkSection newSection = new SoundPhysicsClonedChunkSection(newSectionData, sectionPos);

            // Updating renderContextCache with sectionPos and data
            SectionCache.cache.put(sectionPos.asLong(), newSection);
        }
    }
}