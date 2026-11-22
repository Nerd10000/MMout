/*    */ package net.minecraft.client.multiplayer.chat.report;
/*    */ 
/*    */ import com.mojang.authlib.minecraft.report.AbuseReportLimits;
/*    */ import com.mojang.datafixers.util.Either;
/*    */ import java.util.UUID;
/*    */ 
/*    */ public abstract class Builder<R extends Report> {
/*    */   protected final R report;
/*    */   
/*    */   protected final AbuseReportLimits limits;
/*    */   
/*    */   protected Builder(R report, AbuseReportLimits limits) {
/* 42 */     this.report = report;
/* 43 */     this.limits = limits;
/*    */   }
/*    */   
/*    */   public R report() {
/* 47 */     return this.report;
/*    */   }
/*    */   
/*    */   public UUID reportedProfileId() {
/* 51 */     return ((Report)this.report).reportedProfileId;
/*    */   }
/*    */   
/*    */   public String comments() {
/* 55 */     return ((Report)this.report).comments;
/*    */   }
/*    */   
/*    */   public boolean attested() {
/* 59 */     return ((Report)report()).attested;
/*    */   }
/*    */   
/*    */   public void setComments(String comments) {
/* 63 */     ((Report)this.report).comments = comments;
/*    */   }
/*    */   
/*    */   public ReportReason reason() {
/* 67 */     return ((Report)this.report).reason;
/*    */   }
/*    */   
/*    */   public void setReason(ReportReason reason) {
/* 71 */     ((Report)this.report).reason = reason;
/*    */   }
/*    */   
/*    */   public void setAttested(boolean attested) {
/* 75 */     ((Report)this.report).attested = attested;
/*    */   }
/*    */   
/*    */   public abstract boolean hasContent();
/*    */   
/*    */   public Report.CannotBuildReason checkBuildable() {
/* 81 */     if (!((Report)report()).attested)
/* 82 */       return Report.CannotBuildReason.NOT_ATTESTED; 
/* 84 */     return null;
/*    */   }
/*    */   
/*    */   public abstract Either<Report.Result, Report.CannotBuildReason> build(ReportingContext paramReportingContext);
/*    */ }


/* Location:              C:\Users\karol\AppData\Roaming\ModrinthApp\meta\versions\26.2-0.19.5\26.2-0.19.5.jar!\net\minecraft\client\multiplayer\chat\report\Report$Builder.class
 * Java compiler version: 25 (69.0)
 * JD-Core Version:       1.1.3
 */