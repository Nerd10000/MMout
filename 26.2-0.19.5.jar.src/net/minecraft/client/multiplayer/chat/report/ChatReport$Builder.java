/*     */ package net.minecraft.client.multiplayer.chat.report;
/*     */ 
/*     */ import com.google.common.collect.Lists;
/*     */ import com.mojang.authlib.minecraft.report.AbuseReport;
/*     */ import com.mojang.authlib.minecraft.report.AbuseReportLimits;
/*     */ import com.mojang.authlib.minecraft.report.ReportChatMessage;
/*     */ import com.mojang.authlib.minecraft.report.ReportEvidence;
/*     */ import com.mojang.authlib.minecraft.report.ReportedEntity;
/*     */ import com.mojang.datafixers.util.Either;
/*     */ import it.unimi.dsi.fastutil.ints.IntCollection;
/*     */ import it.unimi.dsi.fastutil.ints.IntSet;
/*     */ import java.nio.ByteBuffer;
/*     */ import java.time.Instant;
/*     */ import java.util.ArrayList;
/*     */ import java.util.List;
/*     */ import java.util.Objects;
/*     */ import java.util.UUID;
/*     */ import net.minecraft.Optionull;
/*     */ import net.minecraft.client.multiplayer.chat.LoggedChatMessage;
/*     */ import net.minecraft.network.chat.MessageSignature;
/*     */ import net.minecraft.network.chat.SignedMessageBody;
/*     */ import net.minecraft.network.chat.SignedMessageLink;
/*     */ import org.apache.commons.lang3.StringUtils;
/*     */ 
/*     */ public class Builder extends Report.Builder<ChatReport> {
/*     */   public Builder(ChatReport report, AbuseReportLimits limits) {
/*  61 */     super(report, limits);
/*     */   }
/*     */   
/*     */   public Builder(UUID reportedProfileId, AbuseReportLimits limits) {
/*  65 */     super(new ChatReport(UUID.randomUUID(), Instant.now(), reportedProfileId), limits);
/*     */   }
/*     */   
/*     */   public IntSet reportedMessages() {
/*  69 */     return this.report.reportedMessages;
/*     */   }
/*     */   
/*     */   public void toggleReported(int id) {
/*  73 */     this.report.toggleReported(id, this.limits);
/*     */   }
/*     */   
/*     */   public boolean isReported(int id) {
/*  77 */     return this.report.reportedMessages.contains(id);
/*     */   }
/*     */   
/*     */   public boolean hasContent() {
/*  82 */     return (StringUtils.isNotEmpty(comments()) || !reportedMessages().isEmpty() || reason() != null);
/*     */   }
/*     */   
/*     */   public Report.CannotBuildReason checkBuildable() {
/*  87 */     if (this.report.reportedMessages.isEmpty())
/*  88 */       return Report.CannotBuildReason.NO_REPORTED_MESSAGES; 
/*  90 */     if (this.report.reportedMessages.size() > this.limits.maxReportedMessageCount())
/*  91 */       return Report.CannotBuildReason.TOO_MANY_MESSAGES; 
/*  93 */     if (this.report.reason == null)
/*  94 */       return Report.CannotBuildReason.NO_REASON; 
/*  96 */     if (this.report.comments.length() > this.limits.maxOpinionCommentsLength())
/*  97 */       return Report.CannotBuildReason.COMMENT_TOO_LONG; 
/*  99 */     return super.checkBuildable();
/*     */   }
/*     */   
/*     */   public Either<Report.Result, Report.CannotBuildReason> build(ReportingContext reportingContext) {
/* 104 */     Report.CannotBuildReason error = checkBuildable();
/* 105 */     if (error != null)
/* 106 */       return Either.right(error); 
/* 109 */     String reason = ((ReportReason)Objects.<ReportReason>requireNonNull(this.report.reason)).backendName();
/* 111 */     ReportEvidence evidence = buildEvidence(reportingContext);
/* 113 */     ReportedEntity reportedEntity = new ReportedEntity(this.report.reportedProfileId);
/* 115 */     AbuseReport abuseReport = AbuseReport.chat(this.report.comments, reason, evidence, reportedEntity, this.report.createdAt);
/* 116 */     return Either.left(new Report.Result(this.report.reportId, ReportType.CHAT, abuseReport));
/*     */   }
/*     */   
/*     */   private ReportEvidence buildEvidence(ReportingContext reportingContext) {
/* 120 */     List<ReportChatMessage> allReportMessages = new ArrayList<>();
/* 122 */     ChatReportContextBuilder contextBuilder = new ChatReportContextBuilder(this.limits.leadingContextMessageCount());
/* 123 */     contextBuilder.collectAllContext(reportingContext.chatLog(), (IntCollection)this.report.reportedMessages, (id, event) -> allReportMessages.add(buildReportedChatMessage(event, isReported(id))));
/* 127 */     return new ReportEvidence(Lists.reverse(allReportMessages));
/*     */   }
/*     */   
/*     */   private ReportChatMessage buildReportedChatMessage(LoggedChatMessage.Player chat, boolean reported) {
/* 131 */     SignedMessageLink link = chat.message().link();
/* 132 */     SignedMessageBody body = chat.message().signedBody();
/* 136 */     List<ByteBuffer> lastSeen = body.lastSeen().entries().stream().map(MessageSignature::asByteBuffer).toList();
/* 137 */     ByteBuffer signature = (ByteBuffer)Optionull.map(chat.message().signature(), MessageSignature::asByteBuffer);
/* 139 */     return new ReportChatMessage(link.index(), link.sender(), link.sessionId(), body.timeStamp(), body.salt(), lastSeen, body.content(), signature, reported);
/*     */   }
/*     */   
/*     */   public Builder copy() {
/* 143 */     return new Builder(this.report.copy(), this.limits);
/*     */   }
/*     */ }


/* Location:              C:\Users\karol\AppData\Roaming\ModrinthApp\meta\versions\26.2-0.19.5\26.2-0.19.5.jar!\net\minecraft\client\multiplayer\chat\report\ChatReport$Builder.class
 * Java compiler version: 25 (69.0)
 * JD-Core Version:       1.1.3
 */