/*    */ package net.minecraft.client.gui.components.debug;
/*    */ 
/*    */ import it.unimi.dsi.fastutil.longs.LongSet;
/*    */ import it.unimi.dsi.fastutil.longs.LongSets;
/*    */ import java.util.List;
/*    */ import java.util.Locale;
/*    */ import net.minecraft.client.Minecraft;
/*    */ import net.minecraft.core.BlockPos;
/*    */ import net.minecraft.core.Direction;
/*    */ import net.minecraft.core.SectionPos;
/*    */ import net.minecraft.resources.Identifier;
/*    */ import net.minecraft.server.level.ServerLevel;
/*    */ import net.minecraft.util.Mth;
/*    */ import net.minecraft.world.entity.Entity;
/*    */ import net.minecraft.world.level.ChunkPos;
/*    */ import net.minecraft.world.level.Level;
/*    */ import net.minecraft.world.level.chunk.LevelChunk;
/*    */ 
/*    */ public class DebugEntryPosition implements DebugScreenEntry {
/* 22 */   public static final Identifier GROUP = Identifier.withDefaultNamespace("position");
/*    */   
/*    */   public void display(DebugScreenDisplayer displayer, Level serverOrClientLevel, LevelChunk clientChunk, LevelChunk serverChunk) {
/* 26 */     Minecraft minecraft = Minecraft.getInstance();
/* 27 */     Entity entity = minecraft.getCameraEntity();
/* 29 */     if (entity == null)
/*    */       return; 
/* 33 */     BlockPos feetPos = minecraft.getCameraEntity().blockPosition();
/* 34 */     ChunkPos chunkPos = ChunkPos.containing(feetPos);
/* 35 */     Direction direction = entity.getDirection();
/* 36 */     switch (direction) {
/*    */       case NORTH:
/*    */       
/*    */       case SOUTH:
/*    */       
/*    */       case WEST:
/*    */       
/*    */       case EAST:
/*    */       
/*    */       default:
/*    */         break;
/*    */     } 
/* 36 */     String faceString = 
/*    */       
/* 41 */       "Invalid";
/* 43 */     ServerLevel serverLevel = (ServerLevel)serverOrClientLevel;
/* 43 */     LongSet chunks = (serverOrClientLevel instanceof ServerLevel) ? serverLevel.getForceLoadedChunks() : (LongSet)LongSets.EMPTY_SET;
/* 45 */     displayer.addToGroup(GROUP, List.of(
/* 46 */           String.format(Locale.ROOT, "XYZ: %.3f / %.5f / %.3f", new Object[] { Double.valueOf(minecraft.getCameraEntity().getX()), Double.valueOf(minecraft.getCameraEntity().getY()), Double.valueOf(minecraft.getCameraEntity().getZ()) }), String.format(Locale.ROOT, "Block: %d %d %d", new Object[] { Integer.valueOf(feetPos.getX()), Integer.valueOf(feetPos.getY()), Integer.valueOf(feetPos.getZ()) }), String.format(Locale.ROOT, "Chunk: %d %d %d [%d %d in r.%d.%d.mca]", new Object[] { Integer.valueOf(chunkPos.x()), Integer.valueOf(SectionPos.blockToSectionCoord(feetPos.getY())), Integer.valueOf(chunkPos.z()), Integer.valueOf(chunkPos.getRegionLocalX()), Integer.valueOf(chunkPos.getRegionLocalZ()), Integer.valueOf(chunkPos.getRegionX()), Integer.valueOf(chunkPos.getRegionZ()) }), String.format(Locale.ROOT, "Facing: %s (%s) (%.1f / %.1f)", new Object[] { direction, faceString, Float.valueOf(Mth.wrapDegrees(entity.getYRot())), Float.valueOf(Mth.wrapDegrees(entity.getXRot())) }), String.valueOf(minecraft.level.dimension().identifier()) + " FC: " + String.valueOf(minecraft.level.dimension().identifier())));
/*    */   }
/*    */ }


/* Location:              C:\Users\karol\AppData\Roaming\ModrinthApp\meta\versions\26.2-0.19.5\26.2-0.19.5.jar!\net\minecraft\client\gui\components\debug\DebugEntryPosition.class
 * Java compiler version: 25 (69.0)
 * JD-Core Version:       1.1.3
 */