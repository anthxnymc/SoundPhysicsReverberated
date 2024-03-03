package toni.sound_physics.mixin;

import toni.sound_physics.SectionCache;
import net.minecraft.client.multiplayer.ClientChunkCache;
import net.minecraft.core.SectionPos;
import net.minecraft.world.level.chunk.LevelChunk;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import org.spongepowered.asm.mixin.injection.callback.LocalCapture;

@Mixin(ClientChunkCache.class)
public class ClientChunkCacheMixin
{
    @Inject(at = @At(value = "INVOKE", target = "Lnet/minecraft/client/multiplayer/ClientChunkCache$Storage;replace(ILnet/minecraft/world/level/chunk/LevelChunk;Lnet/minecraft/world/level/chunk/LevelChunk;)Lnet/minecraft/world/level/chunk/LevelChunk;"), method = "drop", locals = LocalCapture.CAPTURE_FAILHARD)
    public void render(int x, int z, CallbackInfo ci, int i, LevelChunk levelChunk)
    {
        var min = levelChunk.getMinSection();
        var max = levelChunk.getMaxSection();

        for (int idx = min; idx <= max; idx++)
        {
            var section = SectionPos.of(levelChunk.getPos(), idx);
            SectionCache.cache.remove(section.asLong());
        }
    }
}