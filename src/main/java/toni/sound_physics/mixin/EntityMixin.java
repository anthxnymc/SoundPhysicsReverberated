package toni.sound_physics.mixin;

import toni.sound_physics.SoundPhysicsMod;
import toni.sound_physics.utils.SoundUtils;
import net.minecraft.sounds.SoundEvent;
import net.minecraft.sounds.SoundSource;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.player.Player;
import org.jetbrains.annotations.Nullable;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.ModifyArg;

@Mixin(Entity.class)
public abstract class EntityMixin {

    @Shadow
    public abstract float getEyeHeight();

    @ModifyArg(method = "playSound(Lnet/minecraft/sounds/SoundEvent;FF)V", at = @At(value = "INVOKE", target = "Lnet/minecraft/world/level/Level;playSound(Lnet/minecraft/world/entity/player/Player;DDDLnet/minecraft/sounds/SoundEvent;Lnet/minecraft/sounds/SoundSource;FF)V"), index = 2)
    private double playSound(@Nullable Player player, double x, double y, double z, SoundEvent sound, SoundSource category, float volume, float pitch) {
        if (sound == null) {
            return y;
        }
        if (!SoundPhysicsMod.CONFIG.enabled.get()) {
            return y;
        }
        return y + SoundUtils.calculateEntitySoundYOffset(getEyeHeight(), sound);
    }

}
