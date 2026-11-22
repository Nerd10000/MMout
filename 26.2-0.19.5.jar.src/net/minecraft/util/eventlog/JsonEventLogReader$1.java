/*    */ package net.minecraft.util.eventlog;
/*    */ 
/*    */ import com.google.gson.JsonElement;
/*    */ import com.google.gson.JsonParseException;
/*    */ import com.google.gson.JsonParser;
/*    */ import com.google.gson.stream.JsonReader;
/*    */ import com.mojang.serialization.Codec;
/*    */ import com.mojang.serialization.DynamicOps;
/*    */ import com.mojang.serialization.JsonOps;
/*    */ import java.io.EOFException;
/*    */ import java.io.IOException;
/*    */ import java.util.function.Function;
/*    */ 
/*    */ class null implements JsonEventLogReader<T> {
/*    */   public T next() throws IOException {
/*    */     try {
/* 25 */       if (!jsonReader.hasNext())
/* 26 */         return null; 
/* 28 */       JsonElement json = JsonParser.parseReader(jsonReader);
/* 29 */       return (T)codec.parse((DynamicOps)JsonOps.INSTANCE, json).getOrThrow(IOException::new);
/* 30 */     } catch (JsonParseException e) {
/* 31 */       throw new IOException(e);
/* 32 */     } catch (EOFException e) {
/* 34 */       return null;
/*    */     } 
/*    */   }
/*    */   
/*    */   public void close() throws IOException {
/* 40 */     jsonReader.close();
/*    */   }
/*    */ }


/* Location:              C:\Users\karol\AppData\Roaming\ModrinthApp\meta\versions\26.2-0.19.5\26.2-0.19.5.jar!\net\minecraf\\util\eventlog\JsonEventLogReader$1.class
 * Java compiler version: 25 (69.0)
 * JD-Core Version:       1.1.3
 */