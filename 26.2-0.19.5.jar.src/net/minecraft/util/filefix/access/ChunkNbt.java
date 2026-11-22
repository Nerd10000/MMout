/*    */ package net.minecraft.util.filefix.access;
/*    */ 
/*    */ import java.io.IOException;
/*    */ import java.nio.file.Path;
/*    */ import java.util.Optional;
/*    */ import java.util.concurrent.CompletableFuture;
/*    */ import java.util.concurrent.CompletionStage;
/*    */ import java.util.concurrent.Executor;
/*    */ import java.util.function.UnaryOperator;
/*    */ import net.minecraft.nbt.CompoundTag;
/*    */ import net.minecraft.util.Util;
/*    */ import net.minecraft.util.datafix.DataFixTypes;
/*    */ import net.minecraft.util.datafix.DataFixers;
/*    */ import net.minecraft.world.level.ChunkPos;
/*    */ import net.minecraft.world.level.chunk.storage.RegionStorageInfo;
/*    */ import net.minecraft.world.level.chunk.storage.SimpleRegionStorage;
/*    */ 
/*    */ public class ChunkNbt implements AutoCloseable {
/*    */   private final SimpleRegionStorage storage;
/*    */   
/*    */   private final int targetVersion;
/*    */   
/*    */   public ChunkNbt(RegionStorageInfo info, Path path, DataFixTypes type, int targetVersion) {
/* 23 */     this.targetVersion = targetVersion;
/* 24 */     this.storage = new SimpleRegionStorage(info, path, DataFixers.getDataFixer(), false, type);
/*    */   }
/*    */   
/*    */   public CompletableFuture<?> updateChunk(ChunkPos pos, CompoundTag dataFixContext, UnaryOperator<CompoundTag> fixer) {
/* 30 */     return this.storage.read(pos).thenComposeAsync(maybeTag -> {
/*    */           if (maybeTag.isEmpty())
/*    */             return CompletableFuture.completedFuture(null); 
/*    */           CompoundTag tag = maybeTag.get();
/*    */           tag = this.storage.upgradeChunkTag(tag, -1, dataFixContext, this.targetVersion);
/*    */           tag = fixer.apply(tag);
/*    */           return this.storage.write(pos, tag);
/* 40 */         }(Executor)Util.backgroundExecutor());
/*    */   }
/*    */   
/*    */   public void close() throws IOException {
/* 46 */     this.storage.close();
/*    */   }
/*    */ }


/* Location:              C:\Users\karol\AppData\Roaming\ModrinthApp\meta\versions\26.2-0.19.5\26.2-0.19.5.jar!\net\minecraf\\util\filefix\access\ChunkNbt.class
 * Java compiler version: 25 (69.0)
 * JD-Core Version:       1.1.3
 */