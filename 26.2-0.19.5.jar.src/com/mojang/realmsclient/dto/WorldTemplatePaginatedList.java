/*    */ package com.mojang.realmsclient.dto;
/*    */ 
/*    */ import com.google.gson.JsonElement;
/*    */ import com.google.gson.JsonObject;
/*    */ import com.mojang.logging.LogUtils;
/*    */ import com.mojang.realmsclient.util.JsonUtils;
/*    */ import java.util.ArrayList;
/*    */ import java.util.List;
/*    */ import net.minecraft.util.LenientJsonParser;
/*    */ import org.slf4j.Logger;
/*    */ 
/*    */ public final class WorldTemplatePaginatedList extends Record {
/*    */   private final List<WorldTemplate> templates;
/*    */   
/*    */   private final int page;
/*    */   
/*    */   private final int size;
/*    */   
/*    */   private final int total;
/*    */   
/*    */   public WorldTemplatePaginatedList(List<WorldTemplate> templates, int page, int size, int total) {
/* 15 */     this.templates = templates;
/* 15 */     this.page = page;
/* 15 */     this.size = size;
/* 15 */     this.total = total;
/*    */   }
/*    */   
/*    */   public final String toString() {
/*    */     // Byte code:
/*    */     //   0: aload_0
/*    */     //   1: <illegal opcode> toString : (Lcom/mojang/realmsclient/dto/WorldTemplatePaginatedList;)Ljava/lang/String;
/*    */     //   6: areturn
/*    */     // Line number table:
/*    */     //   Java source line number -> byte code offset
/*    */     //   #15	-> 0
/*    */     // Local variable table:
/*    */     //   start	length	slot	name	descriptor
/*    */     //   0	7	0	this	Lcom/mojang/realmsclient/dto/WorldTemplatePaginatedList;
/*    */   }
/*    */   
/*    */   public final int hashCode() {
/*    */     // Byte code:
/*    */     //   0: aload_0
/*    */     //   1: <illegal opcode> hashCode : (Lcom/mojang/realmsclient/dto/WorldTemplatePaginatedList;)I
/*    */     //   6: ireturn
/*    */     // Line number table:
/*    */     //   Java source line number -> byte code offset
/*    */     //   #15	-> 0
/*    */     // Local variable table:
/*    */     //   start	length	slot	name	descriptor
/*    */     //   0	7	0	this	Lcom/mojang/realmsclient/dto/WorldTemplatePaginatedList;
/*    */   }
/*    */   
/*    */   public final boolean equals(Object o) {
/*    */     // Byte code:
/*    */     //   0: aload_0
/*    */     //   1: aload_1
/*    */     //   2: <illegal opcode> equals : (Lcom/mojang/realmsclient/dto/WorldTemplatePaginatedList;Ljava/lang/Object;)Z
/*    */     //   7: ireturn
/*    */     // Line number table:
/*    */     //   Java source line number -> byte code offset
/*    */     //   #15	-> 0
/*    */     // Local variable table:
/*    */     //   start	length	slot	name	descriptor
/*    */     //   0	8	0	this	Lcom/mojang/realmsclient/dto/WorldTemplatePaginatedList;
/*    */     //   0	8	1	o	Ljava/lang/Object;
/*    */   }
/*    */   
/*    */   public List<WorldTemplate> templates() {
/* 15 */     return this.templates;
/*    */   }
/*    */   
/*    */   public int page() {
/* 15 */     return this.page;
/*    */   }
/*    */   
/*    */   public int size() {
/* 15 */     return this.size;
/*    */   }
/*    */   
/*    */   public int total() {
/* 15 */     return this.total;
/*    */   }
/*    */   
/* 21 */   private static final Logger LOGGER = LogUtils.getLogger();
/*    */   
/*    */   public WorldTemplatePaginatedList(int size) {
/* 24 */     this(
/* 25 */         List.of(), 0, size, -1);
/*    */   }
/*    */   
/*    */   public boolean isLastPage() {
/* 35 */     return (this.page * this.size >= this.total && this.page > 0 && this.total > 0 && this.size > 0);
/*    */   }
/*    */   
/*    */   public static WorldTemplatePaginatedList parse(String json) {
/* 39 */     List<WorldTemplate> templates = new ArrayList<>();
/* 40 */     int page = 0;
/* 41 */     int size = 0;
/* 42 */     int total = 0;
/*    */     try {
/* 44 */       JsonObject object = LenientJsonParser.parse(json).getAsJsonObject();
/* 45 */       if (object.get("templates").isJsonArray())
/* 46 */         for (JsonElement element : object.get("templates").getAsJsonArray()) {
/* 47 */           WorldTemplate template = WorldTemplate.parse(element.getAsJsonObject());
/* 48 */           if (template != null)
/* 49 */             templates.add(template); 
/*    */         }  
/* 54 */       page = JsonUtils.getIntOr("page", object, 0);
/* 55 */       size = JsonUtils.getIntOr("size", object, 0);
/* 56 */       total = JsonUtils.getIntOr("total", object, 0);
/* 57 */     } catch (Exception e) {
/* 58 */       LOGGER.error("Could not parse WorldTemplatePaginatedList", e);
/*    */     } 
/* 60 */     return new WorldTemplatePaginatedList(templates, page, size, total);
/*    */   }
/*    */ }


/* Location:              C:\Users\karol\AppData\Roaming\ModrinthApp\meta\versions\26.2-0.19.5\26.2-0.19.5.jar!\com\mojang\realmsclient\dto\WorldTemplatePaginatedList.class
 * Java compiler version: 25 (69.0)
 * JD-Core Version:       1.1.3
 */