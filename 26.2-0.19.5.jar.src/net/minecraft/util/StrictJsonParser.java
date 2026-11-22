/*    */ package net.minecraft.util;
/*    */ 
/*    */ import com.google.gson.JsonElement;
/*    */ import com.google.gson.JsonIOException;
/*    */ import com.google.gson.JsonParser;
/*    */ import com.google.gson.JsonSyntaxException;
/*    */ import com.google.gson.Strictness;
/*    */ import com.google.gson.stream.JsonReader;
/*    */ import com.google.gson.stream.JsonToken;
/*    */ import com.google.gson.stream.MalformedJsonException;
/*    */ import java.io.IOException;
/*    */ import java.io.Reader;
/*    */ import java.io.StringReader;
/*    */ 
/*    */ public class StrictJsonParser {
/*    */   public static JsonElement parse(Reader reader) throws JsonIOException, JsonSyntaxException {
/*    */     try {
/* 25 */       JsonReader jsonReader = new JsonReader(reader);
/* 26 */       jsonReader.setStrictness(Strictness.STRICT);
/* 27 */       JsonElement element = JsonParser.parseReader(jsonReader);
/* 28 */       if (!element.isJsonNull() && jsonReader.peek() != JsonToken.END_DOCUMENT)
/* 29 */         throw new JsonSyntaxException("Did not consume the entire document."); 
/* 31 */       return element;
/* 32 */     } catch (MalformedJsonException|NumberFormatException e) {
/* 33 */       throw new JsonSyntaxException(e);
/* 34 */     } catch (IOException e) {
/* 35 */       throw new JsonIOException(e);
/*    */     } 
/*    */   }
/*    */   
/*    */   public static JsonElement parse(String json) throws JsonSyntaxException {
/* 41 */     return parse(new StringReader(json));
/*    */   }
/*    */ }


/* Location:              C:\Users\karol\AppData\Roaming\ModrinthApp\meta\versions\26.2-0.19.5\26.2-0.19.5.jar!\net\minecraf\\util\StrictJsonParser.class
 * Java compiler version: 25 (69.0)
 * JD-Core Version:       1.1.3
 */