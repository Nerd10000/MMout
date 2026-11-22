/*    */ package net.minecraft.world.level.chunk.storage;
/*    */ 
/*    */ import java.util.Objects;
/*    */ import net.minecraft.CrashReport;
/*    */ import net.minecraft.CrashReportCategory;
/*    */ import net.minecraft.ReportedException;
/*    */ import net.minecraft.world.level.ChunkPos;
/*    */ 
/*    */ public interface ChunkIOErrorReporter {
/*    */   void reportChunkLoadFailure(Throwable paramThrowable, RegionStorageInfo paramRegionStorageInfo, ChunkPos paramChunkPos);
/*    */   
/*    */   void reportChunkSaveFailure(Throwable paramThrowable, RegionStorageInfo paramRegionStorageInfo, ChunkPos paramChunkPos);
/*    */   
/*    */   static ReportedException createMisplacedChunkReport(ChunkPos storedPos, ChunkPos requestedPos) {
/* 16 */     CrashReport report = CrashReport.forThrowable(new IllegalStateException("Retrieved chunk position " + String.valueOf(storedPos) + " does not match requested " + String.valueOf(requestedPos)), "Chunk found in invalid location");
/* 17 */     CrashReportCategory category = report.addCategory("Misplaced Chunk");
/* 18 */     Objects.requireNonNull(storedPos);
/* 18 */     category.setDetail("Stored Position", storedPos::toString);
/* 19 */     return new ReportedException(report);
/*    */   }
/*    */   
/*    */   default void reportMisplacedChunk(ChunkPos storedPos, ChunkPos requestedPos, RegionStorageInfo storageInfo) {
/* 24 */     reportChunkLoadFailure((Throwable)createMisplacedChunkReport(storedPos, requestedPos), storageInfo, requestedPos);
/*    */   }
/*    */ }


/* Location:              C:\Users\karol\AppData\Roaming\ModrinthApp\meta\versions\26.2-0.19.5\26.2-0.19.5.jar!\net\minecraft\world\level\chunk\storage\ChunkIOErrorReporter.class
 * Java compiler version: 25 (69.0)
 * JD-Core Version:       1.1.3
 */