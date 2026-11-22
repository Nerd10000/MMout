/*      */ package net.minecraft.world.entity;
/*      */ 
/*      */ public enum RemovalReason {
/* 4222 */   KILLED(true, false),
/* 4224 */   DISCARDED(true, false),
/* 4226 */   UNLOADED_TO_CHUNK(false, true),
/* 4228 */   UNLOADED_WITH_PLAYER(false, false),
/* 4230 */   CHANGED_DIMENSION(false, false);
/*      */   
/*      */   private final boolean destroy;
/*      */   
/*      */   private final boolean save;
/*      */   
/*      */   RemovalReason(boolean destroy, boolean save) {
/* 4238 */     this.destroy = destroy;
/* 4239 */     this.save = save;
/*      */   }
/*      */   
/*      */   public boolean shouldDestroy() {
/* 4244 */     return this.destroy;
/*      */   }
/*      */   
/*      */   public boolean shouldSave() {
/* 4249 */     return this.save;
/*      */   }
/*      */ }


/* Location:              C:\Users\karol\AppData\Roaming\ModrinthApp\meta\versions\26.2-0.19.5\26.2-0.19.5.jar!\net\minecraft\world\entity\Entity$RemovalReason.class
 * Java compiler version: 25 (69.0)
 * JD-Core Version:       1.1.3
 */