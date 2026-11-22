/*    */ package net.minecraft.util;
/*    */ 
/*    */ import com.google.gson.JsonElement;
/*    */ import com.google.gson.JsonIOException;
/*    */ import com.google.gson.JsonParser;
/*    */ import com.google.gson.JsonSyntaxException;
/*    */ import java.io.Reader;
/*    */ 
/*    */ public class LenientJsonParser {
/*    */   public static JsonElement parse(Reader reader) throws JsonIOException, JsonSyntaxException {
/* 16 */     return JsonParser.parseReader(reader);
/*    */   }
/*    */   
/*    */   public static JsonElement parse(String json) throws JsonSyntaxException {
/* 20 */     return JsonParser.parseString(json);
/*    */   }
/*    */ }


/* Location:              C:\Users\karol\AppData\Roaming\ModrinthApp\meta\versions\26.2-0.19.5\26.2-0.19.5.jar!\net\minecraf\\util\LenientJsonParser.class
 * Java compiler version: 25 (69.0)
 * JD-Core Version:       1.1.3
 */