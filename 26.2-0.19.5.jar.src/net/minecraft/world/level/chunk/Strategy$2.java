/*    */ package net.minecraft.world.level.chunk;
/*    */ 
/*    */ import net.minecraft.core.IdMap;
/*    */ 
/*    */ class null extends Strategy<T> {
/*    */   null(IdMap<T> globalMap, int bitsPerAxis) {
/* 57 */     super(globalMap, bitsPerAxis);
/*    */   }
/*    */   
/*    */   public Configuration getConfigurationForBitCount(int entryBits) {
/* 60 */     switch (entryBits) {
/*    */       case 0:
/*    */       
/*    */       case 1:
/*    */       
/*    */       case 2:
/*    */       
/*    */       case 3:
/*    */       
/*    */     } 
/* 60 */     return 
/*    */       
/* 66 */       new Configuration.Global(this.globalPaletteBitsInMemory, entryBits);
/*    */   }
/*    */ }


/* Location:              C:\Users\karol\AppData\Roaming\ModrinthApp\meta\versions\26.2-0.19.5\26.2-0.19.5.jar!\net\minecraft\world\level\chunk\Strategy$2.class
 * Java compiler version: 25 (69.0)
 * JD-Core Version:       1.1.3
 */