/*    */ package net.minecraft.world.level.chunk;
/*    */ 
/*    */ import java.util.BitSet;
/*    */ import java.util.stream.Stream;
/*    */ import net.minecraft.core.BlockPos;
/*    */ import net.minecraft.world.level.ChunkPos;
/*    */ 
/*    */ public class CarvingMask {
/*    */   private final int minY;
/*    */   
/*    */   private final BitSet mask;
/*    */   
/*    */   private Mask additionalMask = (x, y, z) -> false;
/*    */   
/*    */   public CarvingMask(int height, int minY) {
/* 20 */     this.minY = minY;
/* 21 */     this.mask = new BitSet(256 * height);
/*    */   }
/*    */   
/*    */   public void setAdditionalMask(Mask additionalMask) {
/* 25 */     this.additionalMask = additionalMask;
/*    */   }
/*    */   
/*    */   public CarvingMask(long[] array, int minY) {
/* 29 */     this.minY = minY;
/* 30 */     this.mask = BitSet.valueOf(array);
/*    */   }
/*    */   
/*    */   private int getIndex(int x, int y, int z) {
/* 34 */     return x & 0xF | (z & 0xF) << 4 | y - this.minY << 8;
/*    */   }
/*    */   
/*    */   public void set(int x, int y, int z) {
/* 38 */     this.mask.set(getIndex(x, y, z));
/*    */   }
/*    */   
/*    */   public boolean get(int x, int y, int z) {
/* 42 */     return (this.additionalMask.test(x, y, z) || this.mask.get(getIndex(x, y, z)));
/*    */   }
/*    */   
/*    */   public Stream<BlockPos> stream(ChunkPos pos) {
/* 46 */     return this.mask.stream().mapToObj(i -> {
/*    */           int x = i & 0xF;
/*    */           int z = i >> 4 & 0xF;
/*    */           int y = i >> 8;
/*    */           return pos.getBlockAt(x, y + this.minY, z);
/*    */         });
/*    */   }
/*    */   
/*    */   public long[] toArray() {
/* 55 */     return this.mask.toLongArray();
/*    */   }
/*    */   
/*    */   public static interface Mask {
/*    */     boolean test(int param1Int1, int param1Int2, int param1Int3);
/*    */   }
/*    */ }


/* Location:              C:\Users\karol\AppData\Roaming\ModrinthApp\meta\versions\26.2-0.19.5\26.2-0.19.5.jar!\net\minecraft\world\level\chunk\CarvingMask.class
 * Java compiler version: 25 (69.0)
 * JD-Core Version:       1.1.3
 */