/*    */ package com.mojang.realmsclient.dto;
/*    */ 
/*    */ import com.google.gson.JsonObject;
/*    */ import com.mojang.logging.LogUtils;
/*    */ import com.mojang.realmsclient.util.JsonUtils;
/*    */ import org.slf4j.Logger;
/*    */ 
/*    */ public final class WorldTemplate extends Record {
/*    */   private final String id;
/*    */   
/*    */   private final String name;
/*    */   
/*    */   private final String version;
/*    */   
/*    */   private final String author;
/*    */   
/*    */   private final String link;
/*    */   
/*    */   private final String image;
/*    */   
/*    */   private final String trailer;
/*    */   
/*    */   private final String recommendedPlayers;
/*    */   
/*    */   private final WorldTemplateType type;
/*    */   
/*    */   public WorldTemplate(String id, String name, String version, String author, String link, String image, String trailer, String recommendedPlayers, WorldTemplateType type) {
/* 10 */     this.id = id;
/* 10 */     this.name = name;
/* 10 */     this.version = version;
/* 10 */     this.author = author;
/* 10 */     this.link = link;
/* 10 */     this.image = image;
/* 10 */     this.trailer = trailer;
/* 10 */     this.recommendedPlayers = recommendedPlayers;
/* 10 */     this.type = type;
/*    */   }
/*    */   
/*    */   public final String toString() {
/*    */     // Byte code:
/*    */     //   0: aload_0
/*    */     //   1: <illegal opcode> toString : (Lcom/mojang/realmsclient/dto/WorldTemplate;)Ljava/lang/String;
/*    */     //   6: areturn
/*    */     // Line number table:
/*    */     //   Java source line number -> byte code offset
/*    */     //   #10	-> 0
/*    */     // Local variable table:
/*    */     //   start	length	slot	name	descriptor
/*    */     //   0	7	0	this	Lcom/mojang/realmsclient/dto/WorldTemplate;
/*    */   }
/*    */   
/*    */   public final int hashCode() {
/*    */     // Byte code:
/*    */     //   0: aload_0
/*    */     //   1: <illegal opcode> hashCode : (Lcom/mojang/realmsclient/dto/WorldTemplate;)I
/*    */     //   6: ireturn
/*    */     // Line number table:
/*    */     //   Java source line number -> byte code offset
/*    */     //   #10	-> 0
/*    */     // Local variable table:
/*    */     //   start	length	slot	name	descriptor
/*    */     //   0	7	0	this	Lcom/mojang/realmsclient/dto/WorldTemplate;
/*    */   }
/*    */   
/*    */   public final boolean equals(Object o) {
/*    */     // Byte code:
/*    */     //   0: aload_0
/*    */     //   1: aload_1
/*    */     //   2: <illegal opcode> equals : (Lcom/mojang/realmsclient/dto/WorldTemplate;Ljava/lang/Object;)Z
/*    */     //   7: ireturn
/*    */     // Line number table:
/*    */     //   Java source line number -> byte code offset
/*    */     //   #10	-> 0
/*    */     // Local variable table:
/*    */     //   start	length	slot	name	descriptor
/*    */     //   0	8	0	this	Lcom/mojang/realmsclient/dto/WorldTemplate;
/*    */     //   0	8	1	o	Ljava/lang/Object;
/*    */   }
/*    */   
/*    */   public String id() {
/* 10 */     return this.id;
/*    */   }
/*    */   
/*    */   public String name() {
/* 10 */     return this.name;
/*    */   }
/*    */   
/*    */   public String version() {
/* 10 */     return this.version;
/*    */   }
/*    */   
/*    */   public String author() {
/* 10 */     return this.author;
/*    */   }
/*    */   
/*    */   public String link() {
/* 10 */     return this.link;
/*    */   }
/*    */   
/*    */   public String image() {
/* 10 */     return this.image;
/*    */   }
/*    */   
/*    */   public String trailer() {
/* 10 */     return this.trailer;
/*    */   }
/*    */   
/*    */   public String recommendedPlayers() {
/* 10 */     return this.recommendedPlayers;
/*    */   }
/*    */   
/*    */   public WorldTemplateType type() {
/* 10 */     return this.type;
/*    */   }
/*    */   
/* 21 */   private static final Logger LOGGER = LogUtils.getLogger();
/*    */   
/*    */   public static WorldTemplate parse(JsonObject node) {
/*    */     try {
/* 25 */       String templateTypeName = JsonUtils.getStringOr("type", node, null);
/* 26 */       return new WorldTemplate(
/* 27 */           JsonUtils.getStringOr("id", node, ""), 
/* 28 */           JsonUtils.getStringOr("name", node, ""), 
/* 29 */           JsonUtils.getStringOr("version", node, ""), 
/* 30 */           JsonUtils.getStringOr("author", node, ""), 
/* 31 */           JsonUtils.getStringOr("link", node, ""), 
/* 32 */           JsonUtils.getStringOr("image", node, null), 
/* 33 */           JsonUtils.getStringOr("trailer", node, ""), 
/* 34 */           JsonUtils.getStringOr("recommendedPlayers", node, ""), 
/* 35 */           (templateTypeName == null) ? WorldTemplateType.WORLD_TEMPLATE : WorldTemplateType.valueOf(templateTypeName));
/* 37 */     } catch (Exception e) {
/* 38 */       LOGGER.error("Could not parse WorldTemplate", e);
/* 40 */       return null;
/*    */     } 
/*    */   }
/*    */   
/*    */   public enum WorldTemplateType {
/* 44 */     WORLD_TEMPLATE, MINIGAME, ADVENTUREMAP, EXPERIENCE, INSPIRATION;
/*    */   }
/*    */ }


/* Location:              C:\Users\karol\AppData\Roaming\ModrinthApp\meta\versions\26.2-0.19.5\26.2-0.19.5.jar!\com\mojang\realmsclient\dto\WorldTemplate.class
 * Java compiler version: 25 (69.0)
 * JD-Core Version:       1.1.3
 */