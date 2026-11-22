/*     */ package net.minecraft.network.codec;
/*     */ 
/*     */ import com.google.gson.Gson;
/*     */ import com.google.gson.GsonBuilder;
/*     */ import com.google.gson.JsonElement;
/*     */ import com.google.gson.JsonSyntaxException;
/*     */ import io.netty.buffer.ByteBuf;
/*     */ import io.netty.handler.codec.DecoderException;
/*     */ import net.minecraft.network.Utf8String;
/*     */ import net.minecraft.util.LenientJsonParser;
/*     */ 
/*     */ class null implements StreamCodec<ByteBuf, JsonElement> {
/* 751 */   private static final Gson GSON = (new GsonBuilder()).disableHtmlEscaping().create();
/*     */   
/*     */   public JsonElement decode(ByteBuf input) {
/* 755 */     String payload = Utf8String.read(input, maxStringLength);
/*     */     try {
/* 758 */       return LenientJsonParser.parse(payload);
/* 759 */     } catch (JsonSyntaxException e) {
/* 760 */       throw new DecoderException("Failed to parse JSON", e);
/*     */     } 
/*     */   }
/*     */   
/*     */   public void encode(ByteBuf output, JsonElement value) {
/* 766 */     String payload = GSON.toJson(value);
/* 767 */     Utf8String.write(output, payload, maxStringLength);
/*     */   }
/*     */ }


/* Location:              C:\Users\karol\AppData\Roaming\ModrinthApp\meta\versions\26.2-0.19.5\26.2-0.19.5.jar!\net\minecraft\network\codec\ByteBufCodecs$34.class
 * Java compiler version: 25 (69.0)
 * JD-Core Version:       1.1.3
 */