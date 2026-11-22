/*    */ package net.minecraft.server.level;
/*    */ 
/*    */ public enum FullChunkStatus {
/*  4 */   INACCESSIBLE, FULL, BLOCK_TICKING, ENTITY_TICKING;
/*    */   
/*    */   public boolean isOrAfter(FullChunkStatus step) {
/* 11 */     return (ordinal() >= step.ordinal());
/*    */   }
/*    */ }


/* Location:              C:\Users\karol\AppData\Roaming\ModrinthApp\meta\versions\26.2-0.19.5\26.2-0.19.5.jar!\net\minecraft\server\level\FullChunkStatus.class
 * Java compiler version: 25 (69.0)
 * JD-Core Version:       1.1.3
 */