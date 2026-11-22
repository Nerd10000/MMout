/*    */ package net.minecraft.client.renderer.debug;
/*    */ 
/*    */ import com.google.common.collect.ImmutableMap;
/*    */ import java.util.Map;
/*    */ import java.util.concurrent.CompletableFuture;
/*    */ import net.minecraft.client.multiplayer.ClientChunkCache;
/*    */ import net.minecraft.client.multiplayer.ClientLevel;
/*    */ import net.minecraft.client.server.IntegratedServer;
/*    */ import net.minecraft.core.SectionPos;
/*    */ import net.minecraft.resources.ResourceKey;
/*    */ import net.minecraft.server.level.ServerChunkCache;
/*    */ import net.minecraft.server.level.ServerLevel;
/*    */ import net.minecraft.world.level.ChunkPos;
/*    */ import net.minecraft.world.level.Level;
/*    */ import net.minecraft.world.level.chunk.LevelChunk;
/*    */ 
/*    */ final class ChunkData {
/*    */   private final Map<ChunkPos, String> clientData;
/*    */   
/*    */   private final CompletableFuture<Map<ChunkPos, String>> serverData;
/*    */   
/*    */   private ChunkData(ChunkDebugRenderer paramChunkDebugRenderer, IntegratedServer server, double camX, double camZ) {
/* 75 */     ClientLevel clientLevel = paramChunkDebugRenderer.minecraft.level;
/* 76 */     ResourceKey<Level> dimension = clientLevel.dimension();
/* 77 */     int cx = SectionPos.posToSectionCoord(camX);
/* 78 */     int cz = SectionPos.posToSectionCoord(camZ);
/* 80 */     ImmutableMap.Builder<ChunkPos, String> builder = ImmutableMap.builder();
/* 81 */     ClientChunkCache clientChunkSource = clientLevel.getChunkSource();
/* 82 */     for (int x = cx - 12; x <= cx + 12; x++) {
/* 83 */       for (int z = cz - 12; z <= cz + 12; z++) {
/* 84 */         ChunkPos pos = new ChunkPos(x, z);
/* 85 */         String result = "";
/* 86 */         LevelChunk clientChunk = clientChunkSource.getChunk(x, z, false);
/* 87 */         result = result + "Client: ";
/* 88 */         if (clientChunk == null) {
/* 89 */           result = result + "0n/a\n";
/*    */         } else {
/* 91 */           result = result + result;
/* 92 */           result = result + "\n";
/*    */         } 
/* 94 */         builder.put(pos, result);
/*    */       } 
/*    */     } 
/* 97 */     this.clientData = (Map<ChunkPos, String>)builder.build();
/* 98 */     this.serverData = server.submit(() -> {
/*    */           ServerLevel serverLevel = server.getLevel(dimension);
/*    */           if (serverLevel == null)
/*    */             return (Map)ImmutableMap.of(); 
/*    */           ImmutableMap.Builder<ChunkPos, String> serverBuilder = ImmutableMap.builder();
/*    */           ServerChunkCache serverChunkSource = serverLevel.getChunkSource();
/*    */           for (int x = cx - 12; x <= cx + 12; x++) {
/*    */             for (int z = cz - 12; z <= cz + 12; z++) {
/*    */               ChunkPos pos = new ChunkPos(x, z);
/*    */               serverBuilder.put(pos, "Server: " + serverChunkSource.getChunkDebugData(pos));
/*    */             } 
/*    */           } 
/*    */           return (Map)serverBuilder.build();
/*    */         });
/*    */   }
/*    */ }


/* Location:              C:\Users\karol\AppData\Roaming\ModrinthApp\meta\versions\26.2-0.19.5\26.2-0.19.5.jar!\net\minecraft\client\renderer\debug\ChunkDebugRenderer$ChunkData.class
 * Java compiler version: 25 (69.0)
 * JD-Core Version:       1.1.3
 */