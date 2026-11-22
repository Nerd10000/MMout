/*    */ package net.minecraft.client.gui.components.debug;
/*    */ 
/*    */ import java.util.ArrayList;
/*    */ import java.util.List;
/*    */ import net.minecraft.client.Minecraft;
/*    */ import net.minecraft.core.BlockPos;
/*    */ import net.minecraft.resources.Identifier;
/*    */ import net.minecraft.server.level.ServerChunkCache;
/*    */ import net.minecraft.server.level.ServerLevel;
/*    */ import net.minecraft.world.entity.Entity;
/*    */ import net.minecraft.world.level.Level;
/*    */ import net.minecraft.world.level.biome.BiomeSource;
/*    */ import net.minecraft.world.level.biome.Climate;
/*    */ import net.minecraft.world.level.chunk.ChunkGenerator;
/*    */ import net.minecraft.world.level.chunk.LevelChunk;
/*    */ import net.minecraft.world.level.levelgen.RandomState;
/*    */ 
/*    */ public class DebugEntryChunkGeneration implements DebugScreenEntry {
/* 21 */   private static final Identifier GROUP = Identifier.withDefaultNamespace("chunk_generation");
/*    */   
/* 23 */   private final List<String> result = new ArrayList<>();
/*    */   
/* 24 */   private BlockPos lastPos = null;
/*    */   
/*    */   public void display(DebugScreenDisplayer displayer, Level serverOrClientLevel, LevelChunk clientChunk, LevelChunk serverChunk) {
/* 28 */     Minecraft minecraft = Minecraft.getInstance();
/* 29 */     Entity entity = minecraft.getCameraEntity();
/* 30 */     ServerLevel level = (ServerLevel)serverOrClientLevel, serverLevel = (serverOrClientLevel instanceof ServerLevel) ? level : null;
/* 32 */     if (entity == null || serverLevel == null)
/*    */       return; 
/* 36 */     BlockPos feetPos = entity.blockPosition();
/* 38 */     if (!feetPos.equals(this.lastPos))
/* 39 */       update(serverChunk, feetPos, serverLevel); 
/* 42 */     displayer.addToGroup(GROUP, this.result);
/*    */   }
/*    */   
/*    */   private void update(LevelChunk serverChunk, BlockPos feetPos, ServerLevel serverLevel) {
/* 46 */     this.result.clear();
/* 47 */     this.lastPos = feetPos;
/* 49 */     ServerChunkCache chunkSource = serverLevel.getChunkSource();
/* 51 */     ChunkGenerator generator = chunkSource.getGenerator();
/* 52 */     RandomState randomState = chunkSource.randomState();
/* 53 */     generator.addDebugScreenInfo(this.result, randomState, feetPos);
/* 55 */     Climate.Sampler sampler = randomState.sampler();
/* 56 */     BiomeSource biomeSource = generator.getBiomeSource();
/* 57 */     biomeSource.addDebugInfo(this.result, feetPos, sampler);
/* 59 */     if (serverChunk != null && serverChunk.isOldNoiseGeneration())
/* 60 */       this.result.add("Blending: Old"); 
/*    */   }
/*    */ }


/* Location:              C:\Users\karol\AppData\Roaming\ModrinthApp\meta\versions\26.2-0.19.5\26.2-0.19.5.jar!\net\minecraft\client\gui\components\debug\DebugEntryChunkGeneration.class
 * Java compiler version: 25 (69.0)
 * JD-Core Version:       1.1.3
 */