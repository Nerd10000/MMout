/*    */ package net.minecraft.client.renderer.chunk;
/*    */ 
/*    */ import com.google.common.collect.ImmutableMap;
/*    */ import java.util.Map;
/*    */ import net.minecraft.CrashReport;
/*    */ import net.minecraft.CrashReportCategory;
/*    */ import net.minecraft.ReportedException;
/*    */ import net.minecraft.core.BlockPos;
/*    */ import net.minecraft.world.level.LevelHeightAccessor;
/*    */ import net.minecraft.world.level.block.Blocks;
/*    */ import net.minecraft.world.level.block.entity.BlockEntity;
/*    */ import net.minecraft.world.level.block.state.BlockState;
/*    */ import net.minecraft.world.level.chunk.LevelChunk;
/*    */ import net.minecraft.world.level.chunk.LevelChunkSection;
/*    */ import net.minecraft.world.level.chunk.PalettedContainer;
/*    */ import net.minecraft.world.level.levelgen.DebugLevelSource;
/*    */ 
/*    */ public class SectionCopy {
/*    */   private final Map<BlockPos, BlockEntity> blockEntities;
/*    */   
/*    */   private final PalettedContainer<BlockState> section;
/*    */   
/*    */   private final boolean debug;
/*    */   
/*    */   private final LevelHeightAccessor levelHeightAccessor;
/*    */   
/*    */   public SectionCopy(LevelChunk levelChunk, int sectionIndex) {
/* 29 */     this.levelHeightAccessor = (LevelHeightAccessor)levelChunk;
/* 30 */     this.debug = levelChunk.getLevel().isDebug();
/* 31 */     this.blockEntities = (Map<BlockPos, BlockEntity>)ImmutableMap.copyOf(levelChunk.getBlockEntities());
/* 32 */     if (levelChunk instanceof net.minecraft.world.level.chunk.EmptyLevelChunk) {
/* 33 */       this.section = null;
/*    */     } else {
/* 35 */       LevelChunkSection[] sections = levelChunk.getSections();
/* 36 */       if (sectionIndex < 0 || sectionIndex >= sections.length) {
/* 37 */         this.section = null;
/*    */       } else {
/* 39 */         LevelChunkSection levelChunkSection = sections[sectionIndex];
/* 40 */         this.section = levelChunkSection.hasOnlyAir() ? null : levelChunkSection.getStates().copy();
/*    */       } 
/*    */     } 
/*    */   }
/*    */   
/*    */   public BlockEntity getBlockEntity(BlockPos pos) {
/* 46 */     return this.blockEntities.get(pos);
/*    */   }
/*    */   
/*    */   public BlockState getBlockState(BlockPos pos) {
/* 51 */     int x = pos.getX();
/* 52 */     int y = pos.getY();
/* 53 */     int z = pos.getZ();
/* 54 */     if (this.debug) {
/* 55 */       BlockState blockState = null;
/* 56 */       if (y == 60)
/* 57 */         blockState = Blocks.BARRIER.defaultBlockState(); 
/* 59 */       if (y == 70)
/* 60 */         blockState = DebugLevelSource.getBlockStateFor(x, z); 
/* 62 */       return (blockState == null) ? Blocks.AIR.defaultBlockState() : blockState;
/*    */     } 
/* 65 */     if (this.section == null)
/* 66 */       return Blocks.AIR.defaultBlockState(); 
/*    */     try {
/* 70 */       return (BlockState)this.section.get(x & 0xF, y & 0xF, z & 0xF);
/* 71 */     } catch (Throwable t) {
/* 72 */       CrashReport report = CrashReport.forThrowable(t, "Getting block state");
/* 73 */       CrashReportCategory category = report.addCategory("Block being got");
/* 74 */       category.setDetail("Location", () -> CrashReportCategory.formatLocation(this.levelHeightAccessor, x, y, z));
/* 75 */       throw new ReportedException(report);
/*    */     } 
/*    */   }
/*    */ }


/* Location:              C:\Users\karol\AppData\Roaming\ModrinthApp\meta\versions\26.2-0.19.5\26.2-0.19.5.jar!\net\minecraft\client\renderer\chunk\SectionCopy.class
 * Java compiler version: 25 (69.0)
 * JD-Core Version:       1.1.3
 */