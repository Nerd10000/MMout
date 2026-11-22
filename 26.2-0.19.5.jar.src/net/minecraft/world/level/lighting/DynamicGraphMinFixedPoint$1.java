/*    */ package net.minecraft.world.level.lighting;
/*    */ 
/*    */ import it.unimi.dsi.fastutil.longs.Long2ByteOpenHashMap;
/*    */ 
/*    */ class null extends Long2ByteOpenHashMap {
/*    */   null(int expected, float f) {
/* 43 */     super(expected, f);
/*    */   }
/*    */   
/*    */   protected void rehash(int newN) {
/* 46 */     if (newN > minMapSize)
/* 47 */       super.rehash(newN); 
/*    */   }
/*    */ }


/* Location:              C:\Users\karol\AppData\Roaming\ModrinthApp\meta\versions\26.2-0.19.5\26.2-0.19.5.jar!\net\minecraft\world\level\lighting\DynamicGraphMinFixedPoint$1.class
 * Java compiler version: 25 (69.0)
 * JD-Core Version:       1.1.3
 */