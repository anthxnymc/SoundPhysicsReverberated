package toni.sound_physics;

import toni.sound_physics.utils.SoundPhysicsClonedChunkSection;
import net.minecraft.core.BlockPos;
import net.minecraft.core.SectionPos;
import net.minecraft.world.level.BlockGetter;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.material.FluidState;
import net.minecraft.world.level.material.Fluids;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.HashMap;
import java.util.Map;

public class SectionCache
{
    public static final Map<Long, SoundPhysicsClonedChunkSection> cache = new HashMap<>();

    public static final BlockGetter cachedWorldBlockGetter = new BlockGetter()
    {
        @Override @NotNull public BlockState getBlockState(BlockPos blockPos)  {
            try
            {
                long sectionPosAsLong = SectionPos.of(blockPos).asLong();
                if (cache.containsKey(sectionPosAsLong))
                {
                    SoundPhysicsClonedChunkSection section = cache.get(sectionPosAsLong);

                    var blockstate = section.getBlockState(blockPos.subtract(section.pos.origin()));
                    assert blockstate != null;
                    return blockstate;
                }
            }
            catch (Exception e)
            {
                System.out.println(e);
                return Blocks.AIR.defaultBlockState();
            }

            return Blocks.AIR.defaultBlockState();
        }


        @Nullable
        @Override public BlockEntity getBlockEntity(BlockPos blockPos) { return null; }
        @Override  public @NotNull FluidState getFluidState(BlockPos blockPos)  { return Fluids.EMPTY.defaultFluidState(); }

        @Override public int getHeight() { return 0; }
        @Override public int getMinBuildHeight()  { return 0; }
    };
}