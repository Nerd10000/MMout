/*    */ package net.minecraft.world.level.entity;
/*    */ 
/*    */ import net.minecraft.server.level.FullChunkStatus;
/*    */ 
/*    */ public enum Visibility {
/*  6 */   HIDDEN(false, false),
/*  7 */   TRACKED(true, false),
/*  8 */   TICKING(true, true);
/*    */   
/*    */   private final boolean accessible;
/*    */   
/*    */   private final boolean ticking;
/*    */   
/*    */   Visibility(boolean accessible, boolean ticking) {
/* 14 */     this.accessible = accessible;
/* 15 */     this.ticking = ticking;
/*    */   }
/*    */   
/*    */   public boolean isTicking() {
/* 19 */     return this.ticking;
/*    */   }
/*    */   
/*    */   public boolean isAccessible() {
/* 23 */     return this.accessible;
/*    */   }
/*    */   
/*    */   public static Visibility fromFullChunkStatus(FullChunkStatus status) {
/* 27 */     if (status.isOrAfter(FullChunkStatus.ENTITY_TICKING))
/* 28 */       return TICKING; 
/* 30 */     if (status.isOrAfter(FullChunkStatus.FULL))
/* 31 */       return TRACKED; 
/* 33 */     return HIDDEN;
/*    */   }
/*    */ }


/* Location:              C:\Users\karol\AppData\Roaming\ModrinthApp\meta\versions\26.2-0.19.5\26.2-0.19.5.jar!\net\minecraft\world\level\entity\Visibility.class
 * Java compiler version: 25 (69.0)
 * JD-Core Version:       1.1.3
 */