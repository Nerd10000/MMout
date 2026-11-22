/*    */ package com.mojang.realmsclient.dto;
/*    */ 
/*    */ import com.google.gson.JsonArray;
/*    */ import com.google.gson.JsonElement;
/*    */ import com.google.gson.JsonObject;
/*    */ import com.mojang.realmsclient.util.JsonUtils;
/*    */ import java.util.Objects;
/*    */ import net.minecraft.locale.Language;
/*    */ import net.minecraft.network.chat.Component;
/*    */ 
/*    */ public class RealmsText {
/*    */   private static final String TRANSLATION_KEY = "translationKey";
/*    */   
/*    */   private static final String ARGS = "args";
/*    */   
/*    */   private final String translationKey;
/*    */   
/*    */   private final String[] args;
/*    */   
/*    */   private RealmsText(String translationKey, String[] args) {
/* 21 */     this.translationKey = translationKey;
/* 22 */     this.args = args;
/*    */   }
/*    */   
/*    */   public Component createComponent(Component fallback) {
/* 26 */     return Objects.<Component>requireNonNullElse(createComponent(), fallback);
/*    */   }
/*    */   
/*    */   public Component createComponent() {
/* 31 */     if (!Language.getInstance().has(this.translationKey))
/* 32 */       return null; 
/* 34 */     if (this.args == null)
/* 35 */       return (Component)Component.translatable(this.translationKey); 
/* 38 */     return (Component)Component.translatable(this.translationKey, (Object[])this.args);
/*    */   }
/*    */   
/*    */   public static RealmsText parse(JsonObject jsonObject) {
/* 43 */     String args[], translationKey = JsonUtils.getRequiredString("translationKey", jsonObject);
/* 44 */     JsonElement argsJsonElement = jsonObject.get("args");
/* 46 */     if (argsJsonElement == null || argsJsonElement.isJsonNull()) {
/* 47 */       args = null;
/*    */     } else {
/* 49 */       JsonArray argsJsonArray = argsJsonElement.getAsJsonArray();
/* 50 */       args = new String[argsJsonArray.size()];
/* 51 */       for (int i = 0; i < argsJsonArray.size(); i++)
/* 52 */         args[i] = argsJsonArray.get(i).getAsString(); 
/*    */     } 
/* 55 */     return new RealmsText(translationKey, args);
/*    */   }
/*    */   
/*    */   public String toString() {
/* 60 */     return this.translationKey;
/*    */   }
/*    */ }


/* Location:              C:\Users\karol\AppData\Roaming\ModrinthApp\meta\versions\26.2-0.19.5\26.2-0.19.5.jar!\com\mojang\realmsclient\dto\RealmsText.class
 * Java compiler version: 25 (69.0)
 * JD-Core Version:       1.1.3
 */