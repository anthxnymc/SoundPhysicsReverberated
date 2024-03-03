package toni.sound_physics.config.blocksound;

//#if MC <= 11902
//$$ import net.minecraft.core.Registry;
//#else
import net.minecraft.core.registries.BuiltInRegistries;
//#endif

import net.minecraft.network.chat.Component;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.level.block.Block;
import org.jetbrains.annotations.Nullable;

import java.util.Objects;

public class BlockIdDefinition extends BlockDefinition {

    private final Block block;

    public BlockIdDefinition(Block block) {
        this.block = block;
    }

    @Override
    public String getConfigString() {
        //#if MC <= 11902
        //$$ return Registry.BLOCK.getKey(block).toString();
        //#else
        return BuiltInRegistries.BLOCK.getKey(block).toString();
        //#endif
    }

    @Override
    @Nullable
    public String getConfigComment() {
        return getName().getString();
    }

    @Override
    public Component getName() {
        return block.getName().append(Component.literal(" (Block)"));
    }

    public Block getBlock() {
        return block;
    }

    @Nullable
    public static BlockIdDefinition fromConfigString(String configString) {
        if (!configString.contains(":")) {
            return null;
        }
        if (!ResourceLocation.isValidResourceLocation(configString)) {
            return null;
        }

        #if BEFORE_20_1
        return new BlockIdDefinition(Registry.BLOCK.get(new ResourceLocation(configString)));
        #else
        return new BlockIdDefinition(BuiltInRegistries.BLOCK.get(new ResourceLocation(configString)));
        #endif
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }
        BlockIdDefinition that = (BlockIdDefinition) o;
        return Objects.equals(block, that.block);
    }

    @Override
    public int hashCode() {
        return block != null ? block.hashCode() : 0;
    }
}
