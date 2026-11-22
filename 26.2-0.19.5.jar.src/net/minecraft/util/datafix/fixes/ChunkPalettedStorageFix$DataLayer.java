/*     */ package net.minecraft.util.datafix.fixes;
/*     */ 
/*     */ class DataLayer {
/*     */   private static final int SIZE = 2048;
/*     */   
/*     */   private static final int NIBBLE_SIZE = 4;
/*     */   
/*     */   private final byte[] data;
/*     */   
/*     */   public DataLayer() {
/* 790 */     this.data = new byte[2048];
/*     */   }
/*     */   
/*     */   public DataLayer(byte[] data) {
/* 794 */     this.data = data;
/* 796 */     if (data.length != 2048)
/* 797 */       throw new IllegalArgumentException("ChunkNibbleArrays should be 2048 bytes not: " + data.length); 
/*     */   }
/*     */   
/*     */   public int get(int x, int y, int z) {
/* 802 */     int position = getPosition(y << 8 | z << 4 | x);
/* 804 */     if (isFirst(y << 8 | z << 4 | x))
/* 805 */       return this.data[position] & 0xF; 
/* 807 */     return this.data[position] >> 4 & 0xF;
/*     */   }
/*     */   
/*     */   private boolean isFirst(int position) {
/* 812 */     return ((position & 0x1) == 0);
/*     */   }
/*     */   
/*     */   private int getPosition(int position) {
/* 816 */     return position >> 1;
/*     */   }
/*     */ }


/* Location:              C:\Users\karol\AppData\Roaming\ModrinthApp\meta\versions\26.2-0.19.5\26.2-0.19.5.jar!\net\minecraf\\util\datafix\fixes\ChunkPalettedStorageFix$DataLayer.class
 * Java compiler version: 25 (69.0)
 * JD-Core Version:       1.1.3
 */