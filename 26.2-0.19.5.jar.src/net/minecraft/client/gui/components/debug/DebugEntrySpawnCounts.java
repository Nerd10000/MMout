/*    */ package net.minecraft.client.gui.components.debug;
/*    */ 
/*    */ import it.unimi.dsi.fastutil.objects.Object2IntMap;
/*    */ import java.util.stream.Collectors;
/*    */ import java.util.stream.Stream;
/*    */ import net.minecraft.client.Minecraft;
/*    */ import net.minecraft.server.level.ServerChunkCache;
/*    */ import net.minecraft.server.level.ServerLevel;
/*    */ import net.minecraft.world.entity.Entity;
/*    */ import net.minecraft.world.entity.MobCategory;
/*    */ import net.minecraft.world.level.Level;
/*    */ import net.minecraft.world.level.NaturalSpawner;
/*    */ import net.minecraft.world.level.chunk.LevelChunk;
/*    */ 
/*    */ public class DebugEntrySpawnCounts implements DebugScreenEntry {
/*    */   public void display(DebugScreenDisplayer displayer, Level serverOrClientLevel, LevelChunk clientChunk, LevelChunk serverChunk) {
/* 20 */     Minecraft minecraft = Minecraft.getInstance();
/* 21 */     Entity entity = minecraft.getCameraEntity();
/* 22 */     ServerLevel level = (ServerLevel)serverOrClientLevel, serverLevel = (serverOrClientLevel instanceof ServerLevel) ? level : null;
/* 24 */     if (entity == null || serverLevel == null)
/*    */       return; 
/* 28 */     ServerChunkCache chunkSource = serverLevel.getChunkSource();
/* 29 */     NaturalSpawner.SpawnState lastSpawnState = chunkSource.getLastSpawnState();
/* 31 */     if (lastSpawnState != null) {
/* 32 */       Object2IntMap<MobCategory> mobCategoryCounts = lastSpawnState.getMobCategoryCounts();
/* 33 */       int chunkCount = lastSpawnState.getSpawnableChunkCount();
/* 34 */       displayer.addLine("SC: " + chunkCount + ", " + (String)Stream.<MobCategory>of(MobCategory.values()).map(c -> c.getDebugAbbreviation() + ": " + c.getDebugAbbreviation()).collect(Collectors.joining(", ")));
/*    */     } 
/*    */   }
/*    */ }


/* Location:              C:\Users\karol\AppData\Roaming\ModrinthApp\meta\versions\26.2-0.19.5\26.2-0.19.5.jar!\net\minecraft\client\gui\components\debug\DebugEntrySpawnCounts.class
 * Java compiler version: 25 (69.0)
 * JD-Core Version:       1.1.3
 */