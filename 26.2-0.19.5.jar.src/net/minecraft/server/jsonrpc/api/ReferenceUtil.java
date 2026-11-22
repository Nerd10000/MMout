/*    */ package net.minecraft.server.jsonrpc.api;
/*    */ 
/*    */ import com.mojang.serialization.Codec;
/*    */ import com.mojang.serialization.DataResult;
/*    */ import java.net.URI;
/*    */ import java.net.URISyntaxException;
/*    */ import java.util.Objects;
/*    */ 
/*    */ public class ReferenceUtil {
/*    */   public static final Codec<URI> REFERENCE_CODEC;
/*    */   
/*    */   static {
/* 10 */     REFERENCE_CODEC = Codec.STRING.comapFlatMap(string -> {
/*    */           try {
/*    */             return DataResult.success(new URI(string));
/* 14 */           } catch (URISyntaxException e) {
/*    */             Objects.requireNonNull(e);
/*    */             return DataResult.error(e::getMessage);
/*    */           } 
/*    */         }URI::toString);
/*    */   }
/*    */   
/*    */   public static URI createLocalReference(String typeId) {
/* 25 */     return URI.create("#/components/schemas/" + typeId);
/*    */   }
/*    */ }


/* Location:              C:\Users\karol\AppData\Roaming\ModrinthApp\meta\versions\26.2-0.19.5\26.2-0.19.5.jar!\net\minecraft\server\jsonrpc\api\ReferenceUtil.class
 * Java compiler version: 25 (69.0)
 * JD-Core Version:       1.1.3
 */