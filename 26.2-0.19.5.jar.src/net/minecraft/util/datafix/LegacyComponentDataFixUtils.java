/*    */ package net.minecraft.util.datafix;
/*    */ 
/*    */ import com.google.gson.JsonElement;
/*    */ import com.google.gson.JsonObject;
/*    */ import com.google.gson.JsonParseException;
/*    */ import com.mojang.serialization.Dynamic;
/*    */ import com.mojang.serialization.DynamicOps;
/*    */ import java.util.Optional;
/*    */ import net.minecraft.util.GsonHelper;
/*    */ import net.minecraft.util.LenientJsonParser;
/*    */ import net.minecraft.util.StrictJsonParser;
/*    */ 
/*    */ public class LegacyComponentDataFixUtils {
/* 21 */   private static final String EMPTY_CONTENTS = createTextComponentJson("");
/*    */   
/*    */   public static <T> Dynamic<T> createPlainTextComponent(DynamicOps<T> ops, String text) {
/* 24 */     String stableString = createTextComponentJson(text);
/* 25 */     return new Dynamic(ops, ops.createString(stableString));
/*    */   }
/*    */   
/*    */   public static <T> Dynamic<T> createEmptyComponent(DynamicOps<T> ops) {
/* 29 */     return new Dynamic(ops, ops.createString(EMPTY_CONTENTS));
/*    */   }
/*    */   
/*    */   public static String createTextComponentJson(String text) {
/* 33 */     JsonObject result = new JsonObject();
/* 34 */     result.addProperty("text", text);
/* 35 */     return GsonHelper.toStableString((JsonElement)result);
/*    */   }
/*    */   
/*    */   public static String createTranslatableComponentJson(String key) {
/* 39 */     JsonObject result = new JsonObject();
/* 40 */     result.addProperty("translate", key);
/* 41 */     return GsonHelper.toStableString((JsonElement)result);
/*    */   }
/*    */   
/*    */   public static <T> Dynamic<T> createTranslatableComponent(DynamicOps<T> ops, String key) {
/* 45 */     String stableString = createTranslatableComponentJson(key);
/* 46 */     return new Dynamic(ops, ops.createString(stableString));
/*    */   }
/*    */   
/*    */   public static String rewriteFromLenient(String string) {
/* 50 */     if (string.isEmpty() || string.equals("null"))
/* 51 */       return EMPTY_CONTENTS; 
/* 54 */     char firstChar = string.charAt(0);
/* 55 */     char lastChar = string.charAt(string.length() - 1);
/* 56 */     if ((firstChar == '"' && lastChar == '"') || (firstChar == '{' && lastChar == '}') || (firstChar == '[' && lastChar == ']'))
/*    */       try {
/* 58 */         JsonElement json = LenientJsonParser.parse(string);
/* 59 */         if (json.isJsonPrimitive())
/* 60 */           return createTextComponentJson(json.getAsString()); 
/* 62 */         return GsonHelper.toStableString(json);
/* 63 */       } catch (JsonParseException jsonParseException) {} 
/* 67 */     return createTextComponentJson(string);
/*    */   }
/*    */   
/*    */   public static boolean isStrictlyValidJson(Dynamic<?> component) {
/* 71 */     return component.asString().result()
/* 72 */       .filter(string -> {
/*    */           try {
/*    */             StrictJsonParser.parse(string);
/*    */             return true;
/* 76 */           } catch (JsonParseException ignored) {
/*    */             return false;
/*    */           } 
/* 80 */         }).isPresent();
/*    */   }
/*    */   
/*    */   public static Optional<String> extractTranslationString(String component) {
/*    */     try {
/* 85 */       JsonElement parsed = LenientJsonParser.parse(component);
/* 86 */       if (parsed.isJsonObject()) {
/* 87 */         JsonObject parsedObject = parsed.getAsJsonObject();
/* 88 */         JsonElement key = parsedObject.get("translate");
/* 89 */         if (key != null && key.isJsonPrimitive())
/* 90 */           return Optional.of(key.getAsString()); 
/*    */       } 
/* 93 */     } catch (JsonParseException jsonParseException) {}
/* 96 */     return Optional.empty();
/*    */   }
/*    */ }


/* Location:              C:\Users\karol\AppData\Roaming\ModrinthApp\meta\versions\26.2-0.19.5\26.2-0.19.5.jar!\net\minecraf\\util\datafix\LegacyComponentDataFixUtils.class
 * Java compiler version: 25 (69.0)
 * JD-Core Version:       1.1.3
 */