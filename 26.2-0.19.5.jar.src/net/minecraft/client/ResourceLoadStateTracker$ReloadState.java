/*    */ package net.minecraft.client;
/*    */ 
/*    */ import java.util.List;
/*    */ import net.minecraft.CrashReportCategory;
/*    */ 
/*    */ class ReloadState {
/*    */   private final ResourceLoadStateTracker.ReloadReason reloadReason;
/*    */   
/*    */   private final List<String> packs;
/*    */   
/*    */   private ResourceLoadStateTracker.RecoveryInfo recoveryReloadInfo;
/*    */   
/*    */   private boolean finished;
/*    */   
/*    */   private ReloadState(ResourceLoadStateTracker.ReloadReason reloadReason, List<String> packs) {
/* 82 */     this.reloadReason = reloadReason;
/* 83 */     this.packs = packs;
/*    */   }
/*    */   
/*    */   public void fillCrashInfo(CrashReportCategory category) {
/* 87 */     category.setDetail("Reload reason", this.reloadReason.name);
/* 88 */     category.setDetail("Finished", this.finished ? "Yes" : "No");
/* 89 */     category.setDetail("Packs", () -> String.join(", ", (Iterable)this.packs));
/* 91 */     if (this.recoveryReloadInfo != null)
/* 92 */       this.recoveryReloadInfo.fillCrashInfo(category); 
/*    */   }
/*    */ }


/* Location:              C:\Users\karol\AppData\Roaming\ModrinthApp\meta\versions\26.2-0.19.5\26.2-0.19.5.jar!\net\minecraft\client\ResourceLoadStateTracker$ReloadState.class
 * Java compiler version: 25 (69.0)
 * JD-Core Version:       1.1.3
 */