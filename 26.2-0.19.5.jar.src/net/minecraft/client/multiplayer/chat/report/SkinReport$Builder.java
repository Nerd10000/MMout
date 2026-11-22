/*    */ package net.minecraft.client.multiplayer.chat.report;
/*    */ 
/*    */ import com.mojang.authlib.minecraft.report.AbuseReport;
/*    */ import com.mojang.authlib.minecraft.report.AbuseReportLimits;
/*    */ import com.mojang.authlib.minecraft.report.ReportedEntity;
/*    */ import com.mojang.datafixers.util.Either;
/*    */ import java.time.Instant;
/*    */ import java.util.Objects;
/*    */ import java.util.UUID;
/*    */ import java.util.function.Supplier;
/*    */ import net.minecraft.core.ClientAsset;
/*    */ import net.minecraft.world.entity.player.PlayerSkin;
/*    */ import org.apache.commons.lang3.StringUtils;
/*    */ 
/*    */ public class Builder extends Report.Builder<SkinReport> {
/*    */   public Builder(SkinReport report, AbuseReportLimits limits) {
/* 47 */     super(report, limits);
/*    */   }
/*    */   
/*    */   public Builder(UUID reportedProfileId, Supplier<PlayerSkin> skin, AbuseReportLimits limits) {
/* 51 */     super(new SkinReport(UUID.randomUUID(), Instant.now(), reportedProfileId, skin), limits);
/*    */   }
/*    */   
/*    */   public boolean hasContent() {
/* 56 */     return (StringUtils.isNotEmpty(comments()) || reason() != null);
/*    */   }
/*    */   
/*    */   public Report.CannotBuildReason checkBuildable() {
/* 61 */     if (this.report.reason == null)
/* 62 */       return Report.CannotBuildReason.NO_REASON; 
/* 64 */     if (this.report.comments.length() > this.limits.maxOpinionCommentsLength())
/* 65 */       return Report.CannotBuildReason.COMMENT_TOO_LONG; 
/* 67 */     return super.checkBuildable();
/*    */   }
/*    */   
/*    */   public Either<Report.Result, Report.CannotBuildReason> build(ReportingContext reportingContext) {
/* 72 */     Report.CannotBuildReason error = checkBuildable();
/* 73 */     if (error != null)
/* 74 */       return Either.right(error); 
/* 77 */     String reason = ((ReportReason)Objects.<ReportReason>requireNonNull(this.report.reason)).backendName();
/* 79 */     ReportedEntity reportedEntity = new ReportedEntity(this.report.reportedProfileId);
/* 82 */     PlayerSkin skin = this.report.skinGetter.get();
/* 83 */     ClientAsset.Texture texture = skin.body();
/* 83 */     ClientAsset.DownloadedTexture downloadedTexture = (ClientAsset.DownloadedTexture)texture;
/* 83 */     String skinUrl = (texture instanceof ClientAsset.DownloadedTexture) ? downloadedTexture.url() : null;
/* 85 */     AbuseReport abuseReport = AbuseReport.skin(this.report.comments, reason, skinUrl, reportedEntity, this.report.createdAt);
/* 86 */     return Either.left(new Report.Result(this.report.reportId, ReportType.SKIN, abuseReport));
/*    */   }
/*    */ }


/* Location:              C:\Users\karol\AppData\Roaming\ModrinthApp\meta\versions\26.2-0.19.5\26.2-0.19.5.jar!\net\minecraft\client\multiplayer\chat\report\SkinReport$Builder.class
 * Java compiler version: 25 (69.0)
 * JD-Core Version:       1.1.3
 */