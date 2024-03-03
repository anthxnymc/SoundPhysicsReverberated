package toni.sound_physics.utils;

import net.minecraft.core.BlockPos;
import net.minecraft.core.SectionPos;
import net.minecraft.util.BitStorage;
import net.minecraft.world.level.BlockGetter;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.chunk.PalettedContainerRO;
import net.minecraft.world.level.material.FluidState;
import org.jetbrains.annotations.Nullable;

public class SoundPhysicsClonedChunkSection implements BlockGetter
{
    public final SectionPos pos;
    public final @Nullable PalettedContainerRO<BlockState> blockData;

    public SoundPhysicsClonedChunkSection(@Nullable PalettedContainerRO<BlockState> blockData, SectionPos pos)
    {
        this.blockData = blockData;
        this.pos = pos;
    }


    @Nullable
    @Override
    public BlockEntity getBlockEntity(BlockPos blockPos)
    {
        return null;
    }

    @Nullable
    @Override
    public  BlockState getBlockState(BlockPos blockPos)
    {
        if (blockData == null)
            return null;

        return blockData.get(blockPos.getX(), blockPos.getY(), blockPos.getZ());
    }

    @Override
    public FluidState getFluidState(BlockPos blockPos)
    {
        return null;
    }

    @Override
    public int getHeight()
    {
        return 0;
    }

    @Override
    public int getMinBuildHeight()
    {
        return 0;
    }
}
