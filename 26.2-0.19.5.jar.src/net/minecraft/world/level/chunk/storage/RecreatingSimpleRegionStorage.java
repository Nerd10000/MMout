/*    */ package net.minecraft.world.level.chunk.storage;
/*    */ 
/*    */ import com.mojang.datafixers.DataFixer;
/*    */ import java.io.IOException;
/*    */ import java.nio.file.Path;
/*    */ import java.util.concurrent.CompletableFuture;
/*    */ import java.util.function.Supplier;
/*    */ import net.minecraft.nbt.CompoundTag;
/*    */ import net.minecraft.util.datafix.DataFixTypes;
/*    */ import net.minecraft.world.level.ChunkPos;
/*    */ import org.apache.commons.io.FileUtils;
/*    */ 
/*    */ public class RecreatingSimpleRegionStorage extends SimpleRegionStorage {
/*    */   private final IOWorker writeWorker;
/*    */   
/*    */   private final Path writeFolder;
/*    */   
/*    */   public RecreatingSimpleRegionStorage(RegionStorageInfo readInfo, Path readFolder, RegionStorageInfo writeInfo, Path writeFolder, DataFixer fixerUpper, boolean syncWrites, DataFixTypes dataFixType) {
/* 19 */     super(readInfo, readFolder, fixerUpper, syncWrites, dataFixType);
/* 20 */     this.writeFolder = writeFolder;
/* 21 */     this.writeWorker = new IOWorker(writeInfo, writeFolder, syncWrites);
/*    */   }
/*    */   
/*    */   public CompletableFuture<Void> write(ChunkPos pos, Supplier<CompoundTag> supplier) {
/* 26 */     return this.writeWorker.store(pos, supplier);
/*    */   }
/*    */   
/*    */   public void close() throws IOException {
/* 31 */     super.close();
/* 32 */     this.writeWorker.close();
/* 33 */     if (this.writeFolder.toFile().exists())
/* 34 */       FileUtils.deleteDirectory(this.writeFolder.toFile()); 
/*    */   }
/*    */ }


/* Location:              C:\Users\karol\AppData\Roaming\ModrinthApp\meta\versions\26.2-0.19.5\26.2-0.19.5.jar!\net\minecraft\world\level\chunk\storage\RecreatingSimpleRegionStorage.class
 * Java compiler version: 25 (69.0)
 * JD-Core Version:       1.1.3
 */