/*     */ package com.mojang.realmsclient.util;
/*     */ 
/*     */ import com.mojang.realmsclient.client.RealmsClient;
/*     */ import com.mojang.realmsclient.exception.RealmsServiceException;
/*     */ 
/*     */ @FunctionalInterface
/*     */ public interface RealmsIoConsumer extends RealmsUtil.RealmsIoFunction<Void> {
/*     */   default Void apply(RealmsClient client) throws RealmsServiceException {
/* 106 */     accept(client);
/* 107 */     return null;
/*     */   }
/*     */   
/*     */   void accept(RealmsClient paramRealmsClient) throws RealmsServiceException;
/*     */ }


/* Location:              C:\Users\karol\AppData\Roaming\ModrinthApp\meta\versions\26.2-0.19.5\26.2-0.19.5.jar!\com\mojang\realmsclien\\util\RealmsUtil$RealmsIoConsumer.class
 * Java compiler version: 25 (69.0)
 * JD-Core Version:       1.1.3
 */