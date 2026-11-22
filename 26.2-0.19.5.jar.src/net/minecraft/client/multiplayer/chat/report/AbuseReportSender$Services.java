/*    */ package net.minecraft.client.multiplayer.chat.report;
/*    */ 
/*    */ import com.mojang.authlib.exceptions.MinecraftClientException;
/*    */ import com.mojang.authlib.exceptions.MinecraftClientHttpException;
/*    */ import com.mojang.authlib.minecraft.UserApiService;
/*    */ import com.mojang.authlib.minecraft.report.AbuseReport;
/*    */ import com.mojang.authlib.minecraft.report.AbuseReportLimits;
/*    */ import com.mojang.authlib.yggdrasil.request.AbuseReportRequest;
/*    */ import com.mojang.datafixers.util.Unit;
/*    */ import java.util.UUID;
/*    */ import java.util.concurrent.CompletableFuture;
/*    */ import java.util.concurrent.CompletionException;
/*    */ import java.util.concurrent.Executor;
/*    */ import net.minecraft.network.chat.Component;
/*    */ import net.minecraft.util.Util;
/*    */ 
/*    */ public final class Services extends Record implements AbuseReportSender {
/*    */   private final ReportEnvironment environment;
/*    */   
/*    */   private final UserApiService userApiService;
/*    */   
/*    */   public Services(ReportEnvironment environment, UserApiService userApiService) {
/* 32 */     this.environment = environment;
/* 32 */     this.userApiService = userApiService;
/*    */   }
/*    */   
/*    */   public final String toString() {
/*    */     // Byte code:
/*    */     //   0: aload_0
/*    */     //   1: <illegal opcode> toString : (Lnet/minecraft/client/multiplayer/chat/report/AbuseReportSender$Services;)Ljava/lang/String;
/*    */     //   6: areturn
/*    */     // Line number table:
/*    */     //   Java source line number -> byte code offset
/*    */     //   #32	-> 0
/*    */     // Local variable table:
/*    */     //   start	length	slot	name	descriptor
/*    */     //   0	7	0	this	Lnet/minecraft/client/multiplayer/chat/report/AbuseReportSender$Services;
/*    */   }
/*    */   
/*    */   public final int hashCode() {
/*    */     // Byte code:
/*    */     //   0: aload_0
/*    */     //   1: <illegal opcode> hashCode : (Lnet/minecraft/client/multiplayer/chat/report/AbuseReportSender$Services;)I
/*    */     //   6: ireturn
/*    */     // Line number table:
/*    */     //   Java source line number -> byte code offset
/*    */     //   #32	-> 0
/*    */     // Local variable table:
/*    */     //   start	length	slot	name	descriptor
/*    */     //   0	7	0	this	Lnet/minecraft/client/multiplayer/chat/report/AbuseReportSender$Services;
/*    */   }
/*    */   
/*    */   public final boolean equals(Object o) {
/*    */     // Byte code:
/*    */     //   0: aload_0
/*    */     //   1: aload_1
/*    */     //   2: <illegal opcode> equals : (Lnet/minecraft/client/multiplayer/chat/report/AbuseReportSender$Services;Ljava/lang/Object;)Z
/*    */     //   7: ireturn
/*    */     // Line number table:
/*    */     //   Java source line number -> byte code offset
/*    */     //   #32	-> 0
/*    */     // Local variable table:
/*    */     //   start	length	slot	name	descriptor
/*    */     //   0	8	0	this	Lnet/minecraft/client/multiplayer/chat/report/AbuseReportSender$Services;
/*    */     //   0	8	1	o	Ljava/lang/Object;
/*    */   }
/*    */   
/*    */   public ReportEnvironment environment() {
/* 32 */     return this.environment;
/*    */   }
/*    */   
/*    */   public UserApiService userApiService() {
/* 32 */     return this.userApiService;
/*    */   }
/*    */   
/* 33 */   private static final Component SERVICE_UNAVAILABLE_TEXT = (Component)Component.translatable("gui.abuseReport.send.service_unavailable");
/*    */   
/* 34 */   private static final Component HTTP_ERROR_TEXT = (Component)Component.translatable("gui.abuseReport.send.http_error");
/*    */   
/* 35 */   private static final Component JSON_ERROR_TEXT = (Component)Component.translatable("gui.abuseReport.send.json_error");
/*    */   
/*    */   public CompletableFuture<Unit> send(UUID id, ReportType reportType, AbuseReport report) {
/* 39 */     return CompletableFuture.supplyAsync(() -> {
/*    */           AbuseReportRequest request = new AbuseReportRequest(1, id, report, this.environment.clientInfo(), this.environment.thirdPartyServerInfo(), this.environment.realmInfo(), reportType.backendName());
/*    */           try {
/*    */             this.userApiService.reportAbuse(request);
/*    */             return Unit.INSTANCE;
/* 52 */           } catch (MinecraftClientHttpException e) {
/*    */             Component description = getHttpErrorDescription(e);
/*    */             throw new CompletionException(new AbuseReportSender.SendException(description, e));
/* 55 */           } catch (MinecraftClientException e) {
/*    */             Component description = getErrorDescription(e);
/*    */             throw new CompletionException(new AbuseReportSender.SendException(description, e));
/*    */           } 
/* 59 */         }(Executor)Util.ioPool());
/*    */   }
/*    */   
/*    */   public boolean isEnabled() {
/* 64 */     return this.userApiService.canSendReports();
/*    */   }
/*    */   
/*    */   private Component getHttpErrorDescription(MinecraftClientHttpException e) {
/* 68 */     return (Component)Component.translatable("gui.abuseReport.send.error_message", new Object[] { e.getMessage() });
/*    */   }
/*    */   
/*    */   private Component getErrorDescription(MinecraftClientException e) {
/* 72 */     switch (AbuseReportSender.null.$SwitchMap$com$mojang$authlib$exceptions$MinecraftClientException$ErrorType[e.getType().ordinal()]) {
/*    */       default:
/* 72 */         throw new MatchException(null, null);
/*    */       case 1:
/*    */       
/*    */       case 2:
/*    */       
/*    */       case 3:
/*    */         break;
/*    */     } 
/* 72 */     return 
/*    */       
/* 75 */       JSON_ERROR_TEXT;
/*    */   }
/*    */   
/*    */   public AbuseReportLimits reportLimits() {
/* 81 */     return this.userApiService.getAbuseReportLimits();
/*    */   }
/*    */ }


/* Location:              C:\Users\karol\AppData\Roaming\ModrinthApp\meta\versions\26.2-0.19.5\26.2-0.19.5.jar!\net\minecraft\client\multiplayer\chat\report\AbuseReportSender$Services.class
 * Java compiler version: 25 (69.0)
 * JD-Core Version:       1.1.3
 */