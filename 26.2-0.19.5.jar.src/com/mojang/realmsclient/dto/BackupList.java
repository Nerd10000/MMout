/*    */ package com.mojang.realmsclient.dto;
/*    */ 
/*    */ import com.google.gson.JsonElement;
/*    */ import com.mojang.logging.LogUtils;
/*    */ import java.util.ArrayList;
/*    */ import java.util.List;
/*    */ import net.minecraft.util.LenientJsonParser;
/*    */ import org.slf4j.Logger;
/*    */ 
/*    */ public final class BackupList extends Record {
/*    */   private final List<Backup> backups;
/*    */   
/*    */   public BackupList(List<Backup> backups) {
/* 11 */     this.backups = backups;
/*    */   }
/*    */   
/*    */   public final String toString() {
/*    */     // Byte code:
/*    */     //   0: aload_0
/*    */     //   1: <illegal opcode> toString : (Lcom/mojang/realmsclient/dto/BackupList;)Ljava/lang/String;
/*    */     //   6: areturn
/*    */     // Line number table:
/*    */     //   Java source line number -> byte code offset
/*    */     //   #11	-> 0
/*    */     // Local variable table:
/*    */     //   start	length	slot	name	descriptor
/*    */     //   0	7	0	this	Lcom/mojang/realmsclient/dto/BackupList;
/*    */   }
/*    */   
/*    */   public final int hashCode() {
/*    */     // Byte code:
/*    */     //   0: aload_0
/*    */     //   1: <illegal opcode> hashCode : (Lcom/mojang/realmsclient/dto/BackupList;)I
/*    */     //   6: ireturn
/*    */     // Line number table:
/*    */     //   Java source line number -> byte code offset
/*    */     //   #11	-> 0
/*    */     // Local variable table:
/*    */     //   start	length	slot	name	descriptor
/*    */     //   0	7	0	this	Lcom/mojang/realmsclient/dto/BackupList;
/*    */   }
/*    */   
/*    */   public final boolean equals(Object o) {
/*    */     // Byte code:
/*    */     //   0: aload_0
/*    */     //   1: aload_1
/*    */     //   2: <illegal opcode> equals : (Lcom/mojang/realmsclient/dto/BackupList;Ljava/lang/Object;)Z
/*    */     //   7: ireturn
/*    */     // Line number table:
/*    */     //   Java source line number -> byte code offset
/*    */     //   #11	-> 0
/*    */     // Local variable table:
/*    */     //   start	length	slot	name	descriptor
/*    */     //   0	8	0	this	Lcom/mojang/realmsclient/dto/BackupList;
/*    */     //   0	8	1	o	Ljava/lang/Object;
/*    */   }
/*    */   
/*    */   public List<Backup> backups() {
/* 11 */     return this.backups;
/*    */   }
/*    */   
/* 14 */   private static final Logger LOGGER = LogUtils.getLogger();
/*    */   
/*    */   public static BackupList parse(String json) {
/* 17 */     List<Backup> backups = new ArrayList<>();
/*    */     try {
/* 19 */       JsonElement node = LenientJsonParser.parse(json).getAsJsonObject().get("backups");
/* 20 */       if (node.isJsonArray())
/* 21 */         for (JsonElement element : node.getAsJsonArray()) {
/* 22 */           Backup entry = Backup.parse(element);
/* 23 */           if (entry != null)
/* 24 */             backups.add(entry); 
/*    */         }  
/* 28 */     } catch (Exception e) {
/* 29 */       LOGGER.error("Could not parse BackupList", e);
/*    */     } 
/* 31 */     return new BackupList(backups);
/*    */   }
/*    */ }


/* Location:              C:\Users\karol\AppData\Roaming\ModrinthApp\meta\versions\26.2-0.19.5\26.2-0.19.5.jar!\com\mojang\realmsclient\dto\BackupList.class
 * Java compiler version: 25 (69.0)
 * JD-Core Version:       1.1.3
 */