/*    */ package net.minecraft.client;
/*    */ 
/*    */ public class SectionDirtyState implements RotatingSectionStorage.Value {
/*    */   private boolean isDirty;
/*    */   
/*    */   private boolean isDirtyFromPlayer;
/*    */   
/*    */   private long sectionNode;
/*    */   
/*    */   private SectionDirtyState(boolean isDirty, boolean isDirtyFromPlayer, long sectionNode) {
/* 60 */     this.isDirty = isDirty;
/* 61 */     this.isDirtyFromPlayer = isDirtyFromPlayer;
/*    */   }
/*    */   
/*    */   public void setDirty(boolean fromPlayer) {
/* 65 */     boolean wasDirty = this.isDirty;
/* 66 */     this.isDirty = true;
/* 67 */     this.isDirtyFromPlayer = fromPlayer | ((wasDirty && this.isDirtyFromPlayer));
/*    */   }
/*    */   
/*    */   public void setNotDirty() {
/* 71 */     this.isDirty = false;
/* 72 */     this.isDirtyFromPlayer = false;
/*    */   }
/*    */   
/*    */   public void setSectionNode(long sectionNode) {
/* 77 */     if (this.sectionNode != sectionNode) {
/* 78 */       this.sectionNode = sectionNode;
/* 79 */       this.isDirty = true;
/* 80 */       this.isDirtyFromPlayer = false;
/*    */     } 
/*    */   }
/*    */   
/*    */   public long getSectionNode() {
/* 86 */     return this.sectionNode;
/*    */   }
/*    */   
/*    */   public boolean isDirty() {
/* 90 */     return this.isDirty;
/*    */   }
/*    */   
/*    */   public boolean isDirtyFromPlayer() {
/* 94 */     return this.isDirtyFromPlayer;
/*    */   }
/*    */ }


/* Location:              C:\Users\karol\AppData\Roaming\ModrinthApp\meta\versions\26.2-0.19.5\26.2-0.19.5.jar!\net\minecraft\client\SectionUpdateTracker$SectionDirtyState.class
 * Java compiler version: 25 (69.0)
 * JD-Core Version:       1.1.3
 */