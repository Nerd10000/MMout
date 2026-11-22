/*    */ package net.minecraft.world.level.levelgen;
/*    */ 
/*    */ import net.minecraft.world.level.LevelHeightAccessor;
/*    */ import net.minecraft.world.level.chunk.ChunkGenerator;
/*    */ 
/*    */ public class WorldGenerationContext {
/*    */   private final int minY;
/*    */   
/*    */   private final int height;
/*    */   
/*    */   public WorldGenerationContext(ChunkGenerator generator, LevelHeightAccessor heightAccessor) {
/* 11 */     this.minY = Math.max(heightAccessor.getMinY(), generator.getMinY());
/* 12 */     this.height = Math.min(heightAccessor.getHeight(), generator.getGenDepth());
/*    */   }
/*    */   
/*    */   public int getMinGenY() {
/* 16 */     return this.minY;
/*    */   }
/*    */   
/*    */   public int getGenDepth() {
/* 20 */     return this.height;
/*    */   }
/*    */ }


/* Location:              C:\Users\karol\AppData\Roaming\ModrinthApp\meta\versions\26.2-0.19.5\26.2-0.19.5.jar!\net\minecraft\world\level\levelgen\WorldGenerationContext.class
 * Java compiler version: 25 (69.0)
 * JD-Core Version:       1.1.3
 */