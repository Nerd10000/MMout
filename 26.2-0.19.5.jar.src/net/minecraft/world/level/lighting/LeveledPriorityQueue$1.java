/*    */ package net.minecraft.world.level.lighting;
/*    */ 
/*    */ import it.unimi.dsi.fastutil.longs.LongLinkedOpenHashSet;
/*    */ 
/*    */ class null extends LongLinkedOpenHashSet {
/*    */   null(int expected, float f) {
/* 15 */     super(expected, f);
/*    */   }
/*    */   
/*    */   protected void rehash(int newN) {
/* 18 */     if (newN > minSize)
/* 19 */       super.rehash(newN); 
/*    */   }
/*    */ }


/* Location:              C:\Users\karol\AppData\Roaming\ModrinthApp\meta\versions\26.2-0.19.5\26.2-0.19.5.jar!\net\minecraft\world\level\lighting\LeveledPriorityQueue$1.class
 * Java compiler version: 25 (69.0)
 * JD-Core Version:       1.1.3
 */