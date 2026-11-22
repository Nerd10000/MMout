/*    */ package com.mojang.realmsclient.dto;
/*    */ 
/*    */ import com.google.gson.JsonElement;
/*    */ import com.google.gson.JsonObject;
/*    */ import com.mojang.logging.LogUtils;
/*    */ import java.util.ArrayList;
/*    */ import java.util.List;
/*    */ import net.minecraft.util.LenientJsonParser;
/*    */ import org.slf4j.Logger;
/*    */ 
/*    */ public final class PendingInvitesList extends Record {
/*    */   private final List<PendingInvite> pendingInvites;
/*    */   
/*    */   public PendingInvitesList(List<PendingInvite> pendingInvites) {
/* 12 */     this.pendingInvites = pendingInvites;
/*    */   }
/*    */   
/*    */   public final String toString() {
/*    */     // Byte code:
/*    */     //   0: aload_0
/*    */     //   1: <illegal opcode> toString : (Lcom/mojang/realmsclient/dto/PendingInvitesList;)Ljava/lang/String;
/*    */     //   6: areturn
/*    */     // Line number table:
/*    */     //   Java source line number -> byte code offset
/*    */     //   #12	-> 0
/*    */     // Local variable table:
/*    */     //   start	length	slot	name	descriptor
/*    */     //   0	7	0	this	Lcom/mojang/realmsclient/dto/PendingInvitesList;
/*    */   }
/*    */   
/*    */   public final int hashCode() {
/*    */     // Byte code:
/*    */     //   0: aload_0
/*    */     //   1: <illegal opcode> hashCode : (Lcom/mojang/realmsclient/dto/PendingInvitesList;)I
/*    */     //   6: ireturn
/*    */     // Line number table:
/*    */     //   Java source line number -> byte code offset
/*    */     //   #12	-> 0
/*    */     // Local variable table:
/*    */     //   start	length	slot	name	descriptor
/*    */     //   0	7	0	this	Lcom/mojang/realmsclient/dto/PendingInvitesList;
/*    */   }
/*    */   
/*    */   public final boolean equals(Object o) {
/*    */     // Byte code:
/*    */     //   0: aload_0
/*    */     //   1: aload_1
/*    */     //   2: <illegal opcode> equals : (Lcom/mojang/realmsclient/dto/PendingInvitesList;Ljava/lang/Object;)Z
/*    */     //   7: ireturn
/*    */     // Line number table:
/*    */     //   Java source line number -> byte code offset
/*    */     //   #12	-> 0
/*    */     // Local variable table:
/*    */     //   start	length	slot	name	descriptor
/*    */     //   0	8	0	this	Lcom/mojang/realmsclient/dto/PendingInvitesList;
/*    */     //   0	8	1	o	Ljava/lang/Object;
/*    */   }
/*    */   
/*    */   public List<PendingInvite> pendingInvites() {
/* 12 */     return this.pendingInvites;
/*    */   }
/*    */   
/* 15 */   private static final Logger LOGGER = LogUtils.getLogger();
/*    */   
/*    */   public static PendingInvitesList parse(String json) {
/* 18 */     List<PendingInvite> pendingInvites = new ArrayList<>();
/*    */     try {
/* 20 */       JsonObject jsonObject = LenientJsonParser.parse(json).getAsJsonObject();
/* 21 */       if (jsonObject.get("invites").isJsonArray())
/* 22 */         for (JsonElement element : jsonObject.get("invites").getAsJsonArray()) {
/* 23 */           PendingInvite entry = PendingInvite.parse(element.getAsJsonObject());
/* 24 */           if (entry != null)
/* 25 */             pendingInvites.add(entry); 
/*    */         }  
/* 29 */     } catch (Exception e) {
/* 30 */       LOGGER.error("Could not parse PendingInvitesList", e);
/*    */     } 
/* 32 */     return new PendingInvitesList(pendingInvites);
/*    */   }
/*    */ }


/* Location:              C:\Users\karol\AppData\Roaming\ModrinthApp\meta\versions\26.2-0.19.5\26.2-0.19.5.jar!\com\mojang\realmsclient\dto\PendingInvitesList.class
 * Java compiler version: 25 (69.0)
 * JD-Core Version:       1.1.3
 */