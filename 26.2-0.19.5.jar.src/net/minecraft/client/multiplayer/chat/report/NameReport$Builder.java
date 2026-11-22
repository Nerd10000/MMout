/*    */ package net.minecraft.client.multiplayer.chat.report;
/*    */ 
/*    */ import com.mojang.authlib.minecraft.report.AbuseReport;
/*    */ import com.mojang.authlib.minecraft.report.AbuseReportLimits;
/*    */ import com.mojang.authlib.minecraft.report.ReportedEntity;
/*    */ import com.mojang.datafixers.util.Either;
/*    */ import java.time.Instant;
/*    */ import java.util.UUID;
/*    */ import org.apache.commons.lang3.StringUtils;
/*    */ 
/*    */ public class Builder extends Report.Builder<NameReport> {
/*    */   public Builder(NameReport report, AbuseReportLimits limits) {
/* 42 */     super(report, limits);
/*    */   }
/*    */   
/*    */   public Builder(UUID reportedProfileId, String reportedName, AbuseReportLimits limits) {
/* 46 */     super(new NameReport(UUID.randomUUID(), Instant.now(), reportedProfileId, reportedName), limits);
/*    */   }
/*    */   
/*    */   public boolean hasContent() {
/* 51 */     return StringUtils.isNotEmpty(comments());
/*    */   }
/*    */   
/*    */   public Report.CannotBuildReason checkBuildable() {
/* 56 */     if (this.report.comments.length() > this.limits.maxOpinionCommentsLength())
/* 57 */       return Report.CannotBuildReason.COMMENT_TOO_LONG; 
/* 59 */     return super.checkBuildable();
/*    */   }
/*    */   
/*    */   public Either<Report.Result, Report.CannotBuildReason> build(ReportingContext reportingContext) {
/* 64 */     Report.CannotBuildReason error = checkBuildable();
/* 65 */     if (error != null)
/* 66 */       return Either.right(error); 
/* 69 */     ReportedEntity reportedEntity = new ReportedEntity(this.report.reportedProfileId);
/* 71 */     AbuseReport abuseReport = AbuseReport.name(this.report.comments, reportedEntity, this.report.createdAt);
/* 72 */     return Either.left(new Report.Result(this.report.reportId, ReportType.USERNAME, abuseReport));
/*    */   }
/*    */ }


/* Location:              C:\Users\karol\AppData\Roaming\ModrinthApp\meta\versions\26.2-0.19.5\26.2-0.19.5.jar!\net\minecraft\client\multiplayer\chat\report\NameReport$Builder.class
 * Java compiler version: 25 (69.0)
 * JD-Core Version:       1.1.3
 */