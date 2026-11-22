/*    */ package net.minecraft.world.level.chunk;
/*    */ 
/*    */ import net.minecraft.core.IdMap;
/*    */ 
/*    */ class null extends Strategy<T> {
/*    */   null(IdMap<T> globalMap, int bitsPerAxis) {
/* 39 */     super(globalMap, bitsPerAxis);
/*    */   }
/*    */   
/*    */   public Configuration getConfigurationForBitCount(int entryBits) {
/* 42 */     switch (entryBits) {
/*    */       case 0:
/*    */       
/*    */       case 1:
/*    */       case 2:
/*    */       case 3:
/*    */       case 4:
/*    */       
/*    */       case 5:
/*    */       
/*    */       case 6:
/*    */       
/*    */       case 7:
/*    */       
/*    */       case 8:
/*    */       
/*    */     } 
/* 42 */     return 
/*    */       
/* 50 */       new Configuration.Global(this.globalPaletteBitsInMemory, entryBits);
/*    */   }
/*    */ }


/* Location:              C:\Users\karol\AppData\Roaming\ModrinthApp\meta\versions\26.2-0.19.5\26.2-0.19.5.jar!\net\minecraft\world\level\chunk\Strategy$1.class
 * Java compiler version: 25 (69.0)
 * JD-Core Version:       1.1.3
 */