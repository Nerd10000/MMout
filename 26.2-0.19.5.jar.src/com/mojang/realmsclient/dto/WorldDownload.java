/*    */ package com.mojang.realmsclient.dto;
/*    */ 
/*    */ import com.google.gson.JsonObject;
/*    */ import com.mojang.logging.LogUtils;
/*    */ import com.mojang.realmsclient.util.JsonUtils;
/*    */ import net.minecraft.util.LenientJsonParser;
/*    */ import org.slf4j.Logger;
/*    */ 
/*    */ public final class WorldDownload extends Record {
/*    */   private final String downloadLink;
/*    */   
/*    */   private final String resourcePackUrl;
/*    */   
/*    */   private final String resourcePackHash;
/*    */   
/*    */   public WorldDownload(String downloadLink, String resourcePackUrl, String resourcePackHash) {
/* 10 */     this.downloadLink = downloadLink;
/* 10 */     this.resourcePackUrl = resourcePackUrl;
/* 10 */     this.resourcePackHash = resourcePackHash;
/*    */   }
/*    */   
/*    */   public final String toString() {
/*    */     // Byte code:
/*    */     //   0: aload_0
/*    */     //   1: <illegal opcode> toString : (Lcom/mojang/realmsclient/dto/WorldDownload;)Ljava/lang/String;
/*    */     //   6: areturn
/*    */     // Line number table:
/*    */     //   Java source line number -> byte code offset
/*    */     //   #10	-> 0
/*    */     // Local variable table:
/*    */     //   start	length	slot	name	descriptor
/*    */     //   0	7	0	this	Lcom/mojang/realmsclient/dto/WorldDownload;
/*    */   }
/*    */   
/*    */   public final int hashCode() {
/*    */     // Byte code:
/*    */     //   0: aload_0
/*    */     //   1: <illegal opcode> hashCode : (Lcom/mojang/realmsclient/dto/WorldDownload;)I
/*    */     //   6: ireturn
/*    */     // Line number table:
/*    */     //   Java source line number -> byte code offset
/*    */     //   #10	-> 0
/*    */     // Local variable table:
/*    */     //   start	length	slot	name	descriptor
/*    */     //   0	7	0	this	Lcom/mojang/realmsclient/dto/WorldDownload;
/*    */   }
/*    */   
/*    */   public final boolean equals(Object o) {
/*    */     // Byte code:
/*    */     //   0: aload_0
/*    */     //   1: aload_1
/*    */     //   2: <illegal opcode> equals : (Lcom/mojang/realmsclient/dto/WorldDownload;Ljava/lang/Object;)Z
/*    */     //   7: ireturn
/*    */     // Line number table:
/*    */     //   Java source line number -> byte code offset
/*    */     //   #10	-> 0
/*    */     // Local variable table:
/*    */     //   start	length	slot	name	descriptor
/*    */     //   0	8	0	this	Lcom/mojang/realmsclient/dto/WorldDownload;
/*    */     //   0	8	1	o	Ljava/lang/Object;
/*    */   }
/*    */   
/*    */   public String downloadLink() {
/* 10 */     return this.downloadLink;
/*    */   }
/*    */   
/*    */   public String resourcePackUrl() {
/* 10 */     return this.resourcePackUrl;
/*    */   }
/*    */   
/*    */   public String resourcePackHash() {
/* 10 */     return this.resourcePackHash;
/*    */   }
/*    */   
/* 15 */   private static final Logger LOGGER = LogUtils.getLogger();
/*    */   
/*    */   public static WorldDownload parse(String json) {
/* 18 */     JsonObject jsonObject = LenientJsonParser.parse(json).getAsJsonObject();
/*    */     try {
/* 20 */       return new WorldDownload(
/* 21 */           JsonUtils.getStringOr("downloadLink", jsonObject, ""), 
/* 22 */           JsonUtils.getStringOr("resourcePackUrl", jsonObject, ""), 
/* 23 */           JsonUtils.getStringOr("resourcePackHash", jsonObject, ""));
/* 25 */     } catch (Exception e) {
/* 26 */       LOGGER.error("Could not parse WorldDownload", e);
/* 29 */       return new WorldDownload("", "", "");
/*    */     } 
/*    */   }
/*    */ }


/* Location:              C:\Users\karol\AppData\Roaming\ModrinthApp\meta\versions\26.2-0.19.5\26.2-0.19.5.jar!\com\mojang\realmsclient\dto\WorldDownload.class
 * Java compiler version: 25 (69.0)
 * JD-Core Version:       1.1.3
 */