/*    */ package com.mojang.realmsclient.dto;
/*    */ 
/*    */ import com.google.gson.JsonObject;
/*    */ import com.mojang.logging.LogUtils;
/*    */ import com.mojang.realmsclient.util.JsonUtils;
/*    */ import java.time.Instant;
/*    */ import java.util.UUID;
/*    */ import net.minecraft.util.Util;
/*    */ import org.slf4j.Logger;
/*    */ 
/*    */ public final class PendingInvite extends Record {
/*    */   private final String invitationId;
/*    */   
/*    */   private final String realmName;
/*    */   
/*    */   private final String realmOwnerName;
/*    */   
/*    */   private final UUID realmOwnerUuid;
/*    */   
/*    */   private final Instant date;
/*    */   
/*    */   public PendingInvite(String invitationId, String realmName, String realmOwnerName, UUID realmOwnerUuid, Instant date) {
/* 16 */     this.invitationId = invitationId;
/* 16 */     this.realmName = realmName;
/* 16 */     this.realmOwnerName = realmOwnerName;
/* 16 */     this.realmOwnerUuid = realmOwnerUuid;
/* 16 */     this.date = date;
/*    */   }
/*    */   
/*    */   public final String toString() {
/*    */     // Byte code:
/*    */     //   0: aload_0
/*    */     //   1: <illegal opcode> toString : (Lcom/mojang/realmsclient/dto/PendingInvite;)Ljava/lang/String;
/*    */     //   6: areturn
/*    */     // Line number table:
/*    */     //   Java source line number -> byte code offset
/*    */     //   #16	-> 0
/*    */     // Local variable table:
/*    */     //   start	length	slot	name	descriptor
/*    */     //   0	7	0	this	Lcom/mojang/realmsclient/dto/PendingInvite;
/*    */   }
/*    */   
/*    */   public final int hashCode() {
/*    */     // Byte code:
/*    */     //   0: aload_0
/*    */     //   1: <illegal opcode> hashCode : (Lcom/mojang/realmsclient/dto/PendingInvite;)I
/*    */     //   6: ireturn
/*    */     // Line number table:
/*    */     //   Java source line number -> byte code offset
/*    */     //   #16	-> 0
/*    */     // Local variable table:
/*    */     //   start	length	slot	name	descriptor
/*    */     //   0	7	0	this	Lcom/mojang/realmsclient/dto/PendingInvite;
/*    */   }
/*    */   
/*    */   public final boolean equals(Object o) {
/*    */     // Byte code:
/*    */     //   0: aload_0
/*    */     //   1: aload_1
/*    */     //   2: <illegal opcode> equals : (Lcom/mojang/realmsclient/dto/PendingInvite;Ljava/lang/Object;)Z
/*    */     //   7: ireturn
/*    */     // Line number table:
/*    */     //   Java source line number -> byte code offset
/*    */     //   #16	-> 0
/*    */     // Local variable table:
/*    */     //   start	length	slot	name	descriptor
/*    */     //   0	8	0	this	Lcom/mojang/realmsclient/dto/PendingInvite;
/*    */     //   0	8	1	o	Ljava/lang/Object;
/*    */   }
/*    */   
/*    */   public String invitationId() {
/* 16 */     return this.invitationId;
/*    */   }
/*    */   
/*    */   public String realmName() {
/* 16 */     return this.realmName;
/*    */   }
/*    */   
/*    */   public String realmOwnerName() {
/* 16 */     return this.realmOwnerName;
/*    */   }
/*    */   
/*    */   public UUID realmOwnerUuid() {
/* 16 */     return this.realmOwnerUuid;
/*    */   }
/*    */   
/*    */   public Instant date() {
/* 16 */     return this.date;
/*    */   }
/*    */   
/* 23 */   private static final Logger LOGGER = LogUtils.getLogger();
/*    */   
/*    */   public static PendingInvite parse(JsonObject json) {
/*    */     try {
/* 27 */       return new PendingInvite(
/* 28 */           JsonUtils.getStringOr("invitationId", json, ""), 
/* 29 */           JsonUtils.getStringOr("worldName", json, ""), 
/* 30 */           JsonUtils.getStringOr("worldOwnerName", json, ""), 
/* 31 */           JsonUtils.getUuidOr("worldOwnerUuid", json, Util.NIL_UUID), 
/* 32 */           JsonUtils.getDateOr("date", json));
/* 34 */     } catch (Exception e) {
/* 35 */       LOGGER.error("Could not parse PendingInvite", e);
/* 37 */       return null;
/*    */     } 
/*    */   }
/*    */ }


/* Location:              C:\Users\karol\AppData\Roaming\ModrinthApp\meta\versions\26.2-0.19.5\26.2-0.19.5.jar!\com\mojang\realmsclient\dto\PendingInvite.class
 * Java compiler version: 25 (69.0)
 * JD-Core Version:       1.1.3
 */