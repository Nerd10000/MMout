/*    */ package net.minecraft.core;
/*    */ 
/*    */ public final class QuartPos {
/*    */   public static final int BITS = 2;
/*    */   
/*    */   public static final int SIZE = 4;
/*    */   
/*    */   public static final int MASK = 3;
/*    */   
/*    */   private static final int SECTION_TO_QUARTS_BITS = 2;
/*    */   
/*    */   public static int fromBlock(int blockCoord) {
/* 14 */     return blockCoord >> 2;
/*    */   }
/*    */   
/*    */   public static int quartLocal(int blockCoord) {
/* 18 */     return blockCoord & 0x3;
/*    */   }
/*    */   
/*    */   public static int toBlock(int quart) {
/* 22 */     return quart << 2;
/*    */   }
/*    */   
/*    */   public static int fromSection(int section) {
/* 26 */     return section << 2;
/*    */   }
/*    */   
/*    */   public static int toSection(int quart) {
/* 30 */     return quart >> 2;
/*    */   }
/*    */ }


/* Location:              C:\Users\karol\AppData\Roaming\ModrinthApp\meta\versions\26.2-0.19.5\26.2-0.19.5.jar!\net\minecraft\core\QuartPos.class
 * Java compiler version: 25 (69.0)
 * JD-Core Version:       1.1.3
 */