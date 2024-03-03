package toni.sound_physics.utils;

import net.minecraft.client.multiplayer.ClientLevel;
import net.minecraft.world.level.block.state.BlockState;

public interface WorldSliceAccess
{
    ClientLevel sound_physics$getWorld();

    BlockState[][] sound_physics$getBlockArrays();

    int sound_physics$originX();

    int sound_physics$originY();

    int sound_physics$originZ();
}
