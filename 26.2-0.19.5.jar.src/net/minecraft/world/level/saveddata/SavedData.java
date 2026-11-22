/*    */ package net.minecraft.world.level.saveddata;
/*    */ 
/*    */ public abstract class SavedData {
/*    */   private boolean dirty;
/*    */   
/*    */   public void setDirty() {
/*  7 */     setDirty(true);
/*    */   }
/*    */   
/*    */   public void setDirty(boolean dirty) {
/* 11 */     this.dirty = dirty;
/*    */   }
/*    */   
/*    */   public boolean isDirty() {
/* 15 */     return this.dirty;
/*    */   }
/*    */ }


/* Location:              C:\Users\karol\AppData\Roaming\ModrinthApp\meta\versions\26.2-0.19.5\26.2-0.19.5.jar!\net\minecraft\world\level\saveddata\SavedData.class
 * Java compiler version: 25 (69.0)
 * JD-Core Version:       1.1.3
 */