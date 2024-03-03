package toni.sound_physics.utils;


import it.unimi.dsi.fastutil.ints.Int2ReferenceMap;
import me.jellysquid.mods.sodium.client.world.ReadableContainerExtended;
import me.jellysquid.mods.sodium.client.world.biome.BiomeColorCache;
import me.jellysquid.mods.sodium.client.world.biome.BiomeColorSource;
import me.jellysquid.mods.sodium.client.world.biome.BiomeColorView;
import me.jellysquid.mods.sodium.client.world.biome.BiomeSlice;
import me.jellysquid.mods.sodium.client.world.cloned.ChunkRenderContext;
import me.jellysquid.mods.sodium.client.world.cloned.ClonedChunkSection;
import me.jellysquid.mods.sodium.client.world.cloned.ClonedChunkSectionCache;
import net.minecraft.client.Minecraft;
import net.minecraft.client.multiplayer.ClientLevel;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.core.SectionPos;
import net.minecraft.util.Mth;
import net.minecraft.world.level.BlockAndTintGetter;
import net.minecraft.world.level.ColorResolver;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.LightLayer;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.chunk.DataLayer;
import net.minecraft.world.level.chunk.LevelChunk;
import net.minecraft.world.level.chunk.LevelChunkSection;
import net.minecraft.world.level.levelgen.structure.BoundingBox;
import net.minecraft.world.level.lighting.LevelLightEngine;
import net.minecraft.world.level.material.FluidState;
import org.jetbrains.annotations.Nullable;

import java.util.Arrays;
import java.util.Objects;

public final class SoundPhysicsWorldSlice implements BlockAndTintGetter, BiomeColorView
{
    private static final LightLayer[] LIGHT_TYPES = LightLayer.values();
    private static final int SECTION_BLOCK_COUNT = 4096;
    private static final int NEIGHBOR_BLOCK_RADIUS = 2;
    private static final int NEIGHBOR_CHUNK_RADIUS = Mth.roundToward(2, 16) >> 4;
    private static final int SECTION_ARRAY_LENGTH;
    private static final int SECTION_ARRAY_SIZE;
    private static final int LOCAL_XYZ_BITS = 4;
    private final ClientLevel world;
    private final BiomeSlice biomeSlice;
    private final BiomeColorCache biomeColors;
    private final BlockState[][] blockArrays;
    private final @Nullable DataLayer[][] lightArrays;
    private final @Nullable Int2ReferenceMap<BlockEntity>[] blockEntityArrays;
    private final @Nullable Int2ReferenceMap<Object>[] blockEntityRenderDataArrays;
    private int originX;
    private int originY;
    private int originZ;

    public static ChunkRenderContext prepare(Level world, SectionPos origin, ClonedChunkSectionCache sectionCache) {
        LevelChunk chunk = world.getChunk(origin.getX(), origin.getZ());
        LevelChunkSection section = chunk.getSections()[world.getSectionIndexFromSectionY(origin.getY())];
        if (section != null && !section.hasOnlyAir()) {
            BoundingBox volume = new BoundingBox(origin.minBlockX() - 2, origin.minBlockY() - 2, origin.minBlockZ() - 2, origin.maxBlockX() + 2, origin.maxBlockY() + 2, origin.maxBlockZ() + 2);
            int minChunkX = origin.getX() - NEIGHBOR_CHUNK_RADIUS;
            int minChunkY = origin.getY() - NEIGHBOR_CHUNK_RADIUS;
            int minChunkZ = origin.getZ() - NEIGHBOR_CHUNK_RADIUS;
            int maxChunkX = origin.getX() + NEIGHBOR_CHUNK_RADIUS;
            int maxChunkY = origin.getY() + NEIGHBOR_CHUNK_RADIUS;
            int maxChunkZ = origin.getZ() + NEIGHBOR_CHUNK_RADIUS;
            ClonedChunkSection[] sections = new ClonedChunkSection[SECTION_ARRAY_SIZE];

            for(int chunkX = minChunkX; chunkX <= maxChunkX; ++chunkX) {
                for(int chunkZ = minChunkZ; chunkZ <= maxChunkZ; ++chunkZ) {
                    for(int chunkY = minChunkY; chunkY <= maxChunkY; ++chunkY) {
                        sections[getLocalSectionIndex(chunkX - minChunkX, chunkY - minChunkY, chunkZ - minChunkZ)] = sectionCache.acquire(chunkX, chunkY, chunkZ);
                    }
                }
            }

            return new ChunkRenderContext(origin, sections, volume);
        } else {
            return null;
        }
    }

    public SoundPhysicsWorldSlice(ClientLevel world) {
        this.world = world;
        this.blockArrays = new BlockState[SECTION_ARRAY_SIZE][4096];
        this.lightArrays = new DataLayer[SECTION_ARRAY_SIZE][LIGHT_TYPES.length];
        this.blockEntityArrays = new Int2ReferenceMap[SECTION_ARRAY_SIZE];
        this.blockEntityRenderDataArrays = new Int2ReferenceMap[SECTION_ARRAY_SIZE];
        this.biomeSlice = new BiomeSlice();
        this.biomeColors = new BiomeColorCache(this.biomeSlice, (Integer) Minecraft.getInstance().options.biomeBlendRadius().get());
    }

    public void copyData(ChunkRenderContext context) {
        this.originX = context.getOrigin().getX() - NEIGHBOR_CHUNK_RADIUS << 4;
        this.originY = context.getOrigin().getY() - NEIGHBOR_CHUNK_RADIUS << 4;
        this.originZ = context.getOrigin().getZ() - NEIGHBOR_CHUNK_RADIUS << 4;

        for(int x = 0; x < SECTION_ARRAY_LENGTH; ++x) {
            for(int y = 0; y < SECTION_ARRAY_LENGTH; ++y) {
                for(int z = 0; z < SECTION_ARRAY_LENGTH; ++z) {
                    this.copySectionData(context, getLocalSectionIndex(x, y, z));
                }
            }
        }

        this.biomeSlice.update(this.world, context);
        this.biomeColors.update(context);
    }

    private void copySectionData(ChunkRenderContext context, int sectionIndex) {
        ClonedChunkSection section = context.getSections()[sectionIndex];
        Objects.requireNonNull(section, "Chunk section must be non-null");
        this.unpackBlockData(this.blockArrays[sectionIndex], context, section);
        this.lightArrays[sectionIndex][LightLayer.BLOCK.ordinal()] = section.getLightArray(LightLayer.BLOCK);
        this.lightArrays[sectionIndex][LightLayer.SKY.ordinal()] = section.getLightArray(LightLayer.SKY);
        this.blockEntityArrays[sectionIndex] = section.getBlockEntityMap();
        //this.blockEntityRenderDataArrays[sectionIndex] = section.getBlockEntityRenderDataMap();
    }

    private void unpackBlockData(BlockState[] blockArray, ChunkRenderContext context, ClonedChunkSection section) {
        if (section.getBlockData() == null) {
            Arrays.fill(blockArray, Blocks.AIR.defaultBlockState());
        } else {
            ReadableContainerExtended<BlockState> container = ReadableContainerExtended.of(section.getBlockData());
            SectionPos origin = context.getOrigin();
            SectionPos pos = section.getPosition();
            if (origin.equals(pos)) {
                container.sodium$unpack(blockArray);
            } else {
                BoundingBox bounds = context.getVolume();
                int minBlockX = Math.max(bounds.minX(), pos.minBlockX());
                int maxBlockX = Math.min(bounds.maxX(), pos.maxBlockX());
                int minBlockY = Math.max(bounds.minY(), pos.minBlockY());
                int maxBlockY = Math.min(bounds.maxY(), pos.maxBlockY());
                int minBlockZ = Math.max(bounds.minZ(), pos.minBlockZ());
                int maxBlockZ = Math.min(bounds.maxZ(), pos.maxBlockZ());
                container.sodium$unpack(blockArray, minBlockX & 15, minBlockY & 15, minBlockZ & 15, maxBlockX & 15, maxBlockY & 15, maxBlockZ & 15);
            }

        }
    }

    public void reset() {
        for(int sectionIndex = 0; sectionIndex < SECTION_ARRAY_LENGTH; ++sectionIndex) {
            Arrays.fill(this.lightArrays[sectionIndex], (Object)null);
            this.blockEntityArrays[sectionIndex] = null;
            this.blockEntityRenderDataArrays[sectionIndex] = null;
        }

    }

    public BlockState getBlockState(BlockPos pos) {
        return this.getBlockState(pos.getX(), pos.getY(), pos.getZ());
    }

    public BlockState getBlockState(int x, int y, int z) {
        int relX = x - this.originX;
        int relY = y - this.originY;
        int relZ = z - this.originZ;
        return this.blockArrays[getLocalSectionIndex(relX >> 4, relY >> 4, relZ >> 4)][getLocalBlockIndex(relX & 15, relY & 15, relZ & 15)];
    }

    public FluidState getFluidState(BlockPos pos) {
        return this.getBlockState(pos).getFluidState();
    }

    public float getShade(Direction direction, boolean shaded) {
        return this.world.getShade(direction, shaded);
    }

    public LevelLightEngine getLightEngine() {
        throw new UnsupportedOperationException();
    }

    public int getBrightness(LightLayer type, BlockPos pos) {
        int relX = pos.getX() - this.originX;
        int relY = pos.getY() - this.originY;
        int relZ = pos.getZ() - this.originZ;
        DataLayer lightArray = this.lightArrays[getLocalSectionIndex(relX >> 4, relY >> 4, relZ >> 4)][type.ordinal()];
        return lightArray == null ? 0 : lightArray.get(relX & 15, relY & 15, relZ & 15);
    }

    public int getRawBrightness(BlockPos pos, int ambientDarkness) {
        int relX = pos.getX() - this.originX;
        int relY = pos.getY() - this.originY;
        int relZ = pos.getZ() - this.originZ;
        DataLayer[] lightArrays = this.lightArrays[getLocalSectionIndex(relX >> 4, relY >> 4, relZ >> 4)];
        DataLayer skyLightArray = lightArrays[LightLayer.SKY.ordinal()];
        DataLayer blockLightArray = lightArrays[LightLayer.BLOCK.ordinal()];
        int localX = relX & 15;
        int localY = relY & 15;
        int localZ = relZ & 15;
        int skyLight = skyLightArray == null ? 0 : skyLightArray.get(localX, localY, localZ) - ambientDarkness;
        int blockLight = blockLightArray == null ? 0 : blockLightArray.get(localX, localY, localZ);
        return Math.max(blockLight, skyLight);
    }

    public BlockEntity getBlockEntity(BlockPos pos) {
        return this.getBlockEntity(pos.getX(), pos.getY(), pos.getZ());
    }

    public BlockEntity getBlockEntity(int x, int y, int z) {
        int relX = x - this.originX;
        int relY = y - this.originY;
        int relZ = z - this.originZ;
        Int2ReferenceMap<BlockEntity> blockEntities = this.blockEntityArrays[getLocalSectionIndex(relX >> 4, relY >> 4, relZ >> 4)];
        return blockEntities == null ? null : (BlockEntity)blockEntities.get(getLocalBlockIndex(relX & 15, relY & 15, relZ & 15));
    }

    public int getBlockTint(BlockPos pos, ColorResolver resolver) {
        return this.biomeColors.getColor(BiomeColorSource.from(resolver), pos.getX(), pos.getY(), pos.getZ());
    }

    public int getHeight() {
        return this.world.getHeight();
    }

    public int getMinBuildHeight() {
        return this.world.getMinBuildHeight();
    }

    public int getColor(BiomeColorSource source, int x, int y, int z) {
        return this.biomeColors.getColor(source, x, y, z);
    }

    public @Nullable Object getBlockEntityRenderData(BlockPos pos) {
        int relX = pos.getX() - this.originX;
        int relY = pos.getY() - this.originY;
        int relZ = pos.getZ() - this.originZ;
        Int2ReferenceMap<Object> blockEntityRenderDataMap = this.blockEntityRenderDataArrays[getLocalSectionIndex(relX >> 4, relY >> 4, relZ >> 4)];
        return blockEntityRenderDataMap == null ? null : blockEntityRenderDataMap.get(getLocalBlockIndex(relX & 15, relY & 15, relZ & 15));
    }

    public boolean hasBiomes() {
        return true;
    }


    public static int getLocalBlockIndex(int x, int y, int z) {
        return y << 4 << 4 | z << 4 | x;
    }

    public static int getLocalSectionIndex(int x, int y, int z) {
        return y * SECTION_ARRAY_LENGTH * SECTION_ARRAY_LENGTH + z * SECTION_ARRAY_LENGTH + x;
    }

    static {
        SECTION_ARRAY_LENGTH = 1 + NEIGHBOR_CHUNK_RADIUS * 2;
        SECTION_ARRAY_SIZE = SECTION_ARRAY_LENGTH * SECTION_ARRAY_LENGTH * SECTION_ARRAY_LENGTH;
    }
}