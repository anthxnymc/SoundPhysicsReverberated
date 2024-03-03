package toni.sound_physics.debug;

import com.mojang.blaze3d.systems.RenderSystem;
import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.blaze3d.vertex.VertexConsumer;
import toni.sound_physics.SoundPhysicsMod;
import net.minecraft.Util;
import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.client.renderer.RenderType;
import net.minecraft.world.phys.Vec3;
import org.joml.Matrix4f;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class RaycastRenderer {

    private static final List<Ray> rays = Collections.synchronizedList(new ArrayList<>());
    private static final Minecraft mc = Minecraft.getInstance();

    public static void renderRays(PoseStack poseStack, MultiBufferSource.BufferSource bufferSource, double x, double y, double z) {
        if (mc.level == null) {
            return;
        }
//
//        poseStack.pushPose();
//        RenderSystem.disableDepthTest();
//        var player = mc.player.blockPosition();
//
//        long sectionPosAsLong = SectionPos.of(player).asLong();
//        if (SectionCache.cache.containsKey(sectionPosAsLong))
//        {
//            SoundPhysicsClonedChunkSection section = SectionCache.cache.get(sectionPosAsLong);
//
//            for (int xd = 0; xd < 15; xd++)
//            {
//                for (int yd= 0; yd < 15; yd++)
//                {
//                    for (int zd = 0; zd < 15; zd++)
//                    {
////                        var block = section.getBlockState(new BlockPos(xd, yd, zd));
////                        if (block == Blocks.AIR.defaultBlockState())
////                            continue;
////
////                        var pos = new BlockPos(section.pos.minBlockX() + xd, section.pos.minBlockY()  + yd, section.pos.minBlockZ()  + zd);
////
////                        var start = pos.getCenter().add(0.51d, 0.51d, 0.51d);
////                        var end = start.add(-1.01f, 0f, -1.01f);
////
////                        VertexConsumer consumer = bufferSource.getBuffer(RenderType.debugLineStrip(1D));
////                        Matrix4f matrix4f = poseStack.last().pose();
////
////                        consumer.vertex(matrix4f, (float) (start.x - x), (float) (start.y - y), (float) (start.z - z)).color(255,255,255,255).endVertex();
////                        consumer.vertex(matrix4f, (float) (end.x - x), (float) (end.y - y), (float) (end.z - z)).color(255,255,255,255).endVertex();
////
////                        start = pos.getCenter().add(-0.51d, 0.51d, 0.51d);
////                        end = start.add(1.01f, 0f, -1.01f);
////
////                        consumer = bufferSource.getBuffer(RenderType.debugLineStrip(1D));
////                        matrix4f = poseStack.last().pose();
////
////                        consumer.vertex(matrix4f, (float) (start.x - x), (float) (start.y - y), (float) (start.z - z)).color(255,255,255,255).endVertex();
////                        consumer.vertex(matrix4f, (float) (end.x - x), (float) (end.y - y), (float) (end.z - z)).color(255,255,255,255).endVertex();
//                    }
//                }
//            }
//        }
//
////
////        for (int xd = -8; xd < 8; xd++)
////        {
////            for (int yd= -8; yd < 8; yd++)
////            {
////                for (int zd = -8; zd < 8; zd++)
////                {
////                    var pos = new BlockPos(player.getX() + xd, player.getY() + yd, player.getZ() + zd);
////                    var block = SectionCache.cachedWorldBlockGetter.getBlockState(pos);
////
////                    long sectionPosAsLong = SectionPos.of(pos).asLong();
////                    if (SectionCache.cache.containsKey(sectionPosAsLong))
////                    {
////                        SoundPhysicsClonedChunkSection section = SectionCache.cache.get(sectionPosAsLong);
////
////                        var relative = pos.subtract(section.pos.origin());
////                        block = section.getBlockState(relative);
////                    }
////
////                    if (block == Blocks.AIR.defaultBlockState())
////                        continue;
////
////                    double d = 1.3;
////                    double e = 0.2;
////                    double f = (double)pos.getX() + 0.5;
////                    double g = (double)pos.getY() + 1.3 + (double) 0 * 0.2;
////                    double h = (double)pos.getZ() + 0.5;
////                    DebugRenderer.renderFloatingText(poseStack, bufferSource, block.toString(), f, g, h, -256, 0.02F, true, 0.0F, true);
//////
//////                    var light = LightTexture.pack(15, 15);
//////
//////                    poseStack.translate(pos.getX() - xd, pos.getY() - yd, pos.getZ() - zd);
//////
//////                    mc.getBlockRenderer().renderSingleBlock(Blocks.STONE.defaultBlockState(), poseStack, bufferSource, light, OverlayTexture.NO_OVERLAY);
////////
//////
//////                    var start = pos.getCenter().add(0.51d, 0.51d, 0.51d);
//////                    var end = start.add(-1.01f, 0f, -1.01f);
//////
//////                    VertexConsumer consumer = bufferSource.getBuffer(RenderType.debugLineStrip(1D));
////                      Matrix4f matrix4f = poseStack.last().pose();
//////
//////                    consumer.vertex(matrix4f, (float) (start.x - x), (float) (start.y - y), (float) (start.z - z)).color(255,255,255,255).endVertex();
//////                    consumer.vertex(matrix4f, (float) (end.x - x), (float) (end.y - y), (float) (end.z - z)).color(255,255,255,255).endVertex();
//////
//////                    start = pos.getCenter().add(-0.51d, 0.51d, 0.51d);
//////                    end = start.add(1.01f, 0f, -1.01f);
//////
//////                    consumer = bufferSource.getBuffer(RenderType.debugLineStrip(1D));
//////                    matrix4f = poseStack.last().pose();
//////
//////                    consumer.vertex(matrix4f, (float) (start.x - x), (float) (start.y - y), (float) (start.z - z)).color(255,255,255,255).endVertex();
//////                    consumer.vertex(matrix4f, (float) (end.x - x), (float) (end.y - y), (float) (end.z - z)).color(255,255,255,255).endVertex();
////
////                }
////            }
////        }
//
//        poseStack.popPose();


        if (!(SoundPhysicsMod.CONFIG.renderSoundBounces.get() || SoundPhysicsMod.CONFIG.renderOcclusion.get())) {
            synchronized (rays) {
                rays.clear();
            }
            return;
        }
        long gameTime = Util.getMillis();
        synchronized (rays) {
            rays.removeIf(ray -> (gameTime - ray.tickCreated) > ray.lifespan || (gameTime - ray.tickCreated) < 0L);
            for (Ray ray : rays) {
                renderRay(ray, poseStack, bufferSource, x, y, z);
            }
        }
    }

    public static void addSoundBounceRay(Vec3 start, Vec3 end, int color) {
        if (!SoundPhysicsMod.CONFIG.renderSoundBounces.get()) {
            return;
        }
        addRay(start, end, color, false);
    }

    public static void addOcclusionRay(Vec3 start, Vec3 end, int color) {
        if (!SoundPhysicsMod.CONFIG.renderOcclusion.get()) {
            return;
        }
        addRay(start, end, color, true);
    }

    public static void addRay(Vec3 start, Vec3 end, int color, boolean throughWalls) {
        if (mc.player.position().distanceTo(start) > 32D && mc.player.position().distanceTo(end) > 32D) {
            return;
        }
        synchronized (rays) {
            rays.add(new Ray(start, end, color, throughWalls));
        }
    }

    public static void renderRay(Ray ray, PoseStack poseStack, MultiBufferSource.BufferSource bufferSource, double x, double y, double z) {
        int red = getRed(ray.color);
        int green = getGreen(ray.color);
        int blue = getBlue(ray.color);

        if (!ray.start.closerThan(mc.player.position(), 32f))
            return;

        if (red == 255 && blue == 255)
            return;

        poseStack.pushPose();


        if (ray.throughWalls) {
            //TODO Fix rays through walls not rendering properly
            RenderSystem.disableDepthTest();
        }

        VertexConsumer consumer = bufferSource.getBuffer(RenderType.debugLineStrip(1D));
        Matrix4f matrix4f = poseStack.last().pose();

        consumer.vertex(matrix4f, (float) (ray.start.x - x), (float) (ray.start.y - y), (float) (ray.start.z - z)).color(red, green, blue, 255).endVertex();
        consumer.vertex(matrix4f, (float) (ray.end.x - x), (float) (ray.end.y - y), (float) (ray.end.z - z)).color(red, green, blue, 255).endVertex();

        poseStack.popPose();
    }

    private static int getRed(int argb) {
        return (argb >> 16) & 0xFF;
    }

    private static int getGreen(int argb) {
        return (argb >> 8) & 0xFF;
    }

    private static int getBlue(int argb) {
        return argb & 0xFF;
    }

    private static class Ray {
        private final Vec3 start;
        private final Vec3 end;
        private final int color;
        private final long tickCreated;
        private final long lifespan;
        private final boolean throughWalls;

        public Ray(Vec3 start, Vec3 end, int color, boolean throughWalls) {
            this.start = start;
            this.end = end;
            this.color = color;
            this.throughWalls = throughWalls;
            this.tickCreated = Util.getMillis();
            this.lifespan = (1000 / 20) * 50;
        }
    }

}
