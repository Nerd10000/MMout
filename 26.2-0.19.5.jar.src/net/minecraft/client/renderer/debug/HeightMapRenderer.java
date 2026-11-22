/*    */ package net.minecraft.client.renderer.debug;
/*    */ 
/*    */ import java.util.Map;
/*    */ import net.minecraft.client.Minecraft;
/*    */ import net.minecraft.client.multiplayer.ClientLevel;
/*    */ import net.minecraft.client.renderer.culling.Frustum;
/*    */ import net.minecraft.core.BlockPos;
/*    */ import net.minecraft.core.SectionPos;
/*    */ import net.minecraft.gizmos.GizmoStyle;
/*    */ import net.minecraft.gizmos.Gizmos;
/*    */ import net.minecraft.util.ARGB;
/*    */ import net.minecraft.util.debug.DebugValueAccess;
/*    */ import net.minecraft.world.level.ChunkPos;
/*    */ import net.minecraft.world.level.chunk.ChunkAccess;
/*    */ import net.minecraft.world.level.levelgen.Heightmap;
/*    */ import net.minecraft.world.phys.AABB;
/*    */ import org.joml.Vector3f;
/*    */ 
/*    */ public class HeightMapRenderer implements DebugRenderer.SimpleDebugRenderer {
/*    */   private final Minecraft minecraft;
/*    */   
/*    */   private static final int CHUNK_DIST = 2;
/*    */   
/*    */   private static final float BOX_HEIGHT = 0.09375F;
/*    */   
/*    */   public HeightMapRenderer(Minecraft minecraft) {
/* 26 */     this.minecraft = minecraft;
/*    */   }
/*    */   
/*    */   public void emitGizmos(double camX, double camY, double camZ, DebugValueAccess debugValues, Frustum frustum, float partialTicks) {
/* 31 */     ClientLevel clientLevel = this.minecraft.level;
/* 33 */     BlockPos playerPos = BlockPos.containing(camX, 0.0D, camZ);
/* 35 */     for (int chunkX = -2; chunkX <= 2; chunkX++) {
/* 36 */       for (int chunkZ = -2; chunkZ <= 2; chunkZ++) {
/* 37 */         ChunkAccess chunk = clientLevel.getChunk(playerPos.offset(chunkX * 16, 0, chunkZ * 16));
/* 38 */         for (Map.Entry<Heightmap.Types, Heightmap> heightmapEntry : (Iterable<Map.Entry<Heightmap.Types, Heightmap>>)chunk.getHeightmaps()) {
/* 39 */           Heightmap.Types type = heightmapEntry.getKey();
/* 40 */           ChunkPos chunkPos = chunk.getPos();
/* 41 */           Vector3f color = getColor(type);
/* 42 */           for (int relativeX = 0; relativeX < 16; relativeX++) {
/* 43 */             for (int relativeZ = 0; relativeZ < 16; relativeZ++) {
/* 44 */               int xx = SectionPos.sectionToBlockCoord(chunkPos.x(), relativeX);
/* 45 */               int zz = SectionPos.sectionToBlockCoord(chunkPos.z(), relativeZ);
/* 46 */               float height = clientLevel.getHeight(type, xx, zz) + type.ordinal() * 0.09375F;
/* 47 */               Gizmos.cuboid(new AABB((xx + 0.25F), height, (zz + 0.25F), (xx + 0.75F), (height + 0.09375F), (zz + 0.75F)), GizmoStyle.fill(ARGB.colorFromFloat(1.0F, color.x(), color.y(), color.z())));
/*    */             } 
/*    */           } 
/*    */         } 
/*    */       } 
/*    */     } 
/*    */   }
/*    */   
/*    */   private Vector3f getColor(Heightmap.Types type) {
/* 56 */     switch (type) {
/*    */       default:
/* 56 */         throw new MatchException(null, null);
/*    */       case WORLD_SURFACE_WG:
/*    */       
/*    */       case OCEAN_FLOOR_WG:
/*    */       
/*    */       case WORLD_SURFACE:
/*    */       
/*    */       case OCEAN_FLOOR:
/*    */       
/*    */       case MOTION_BLOCKING:
/*    */       
/*    */       case MOTION_BLOCKING_NO_LEAVES:
/*    */         break;
/*    */     } 
/* 56 */     return 
/*    */       
/* 62 */       new Vector3f(0.0F, 0.5F, 0.5F);
/*    */   }
/*    */ }


/* Location:              C:\Users\karol\AppData\Roaming\ModrinthApp\meta\versions\26.2-0.19.5\26.2-0.19.5.jar!\net\minecraft\client\renderer\debug\HeightMapRenderer.class
 * Java compiler version: 25 (69.0)
 * JD-Core Version:       1.1.3
 */