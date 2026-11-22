/*    */ package net.minecraft.server.jsonrpc.dataprovider;
/*    */ 
/*    */ import com.google.gson.JsonElement;
/*    */ import com.mojang.serialization.DynamicOps;
/*    */ import com.mojang.serialization.JsonOps;
/*    */ import java.nio.file.Path;
/*    */ import java.util.concurrent.CompletableFuture;
/*    */ import net.minecraft.data.CachedOutput;
/*    */ import net.minecraft.data.DataProvider;
/*    */ import net.minecraft.data.PackOutput;
/*    */ import net.minecraft.server.jsonrpc.api.Schema;
/*    */ import net.minecraft.server.jsonrpc.methods.DiscoveryService;
/*    */ 
/*    */ public class JsonRpcApiSchema implements DataProvider {
/*    */   private final Path path;
/*    */   
/*    */   public JsonRpcApiSchema(PackOutput packOutput) {
/* 18 */     this.path = packOutput.getOutputFolder(PackOutput.Target.REPORTS).resolve("json-rpc-api-schema.json");
/*    */   }
/*    */   
/*    */   public CompletableFuture<?> run(CachedOutput cache) {
/* 23 */     DiscoveryService.DiscoverResponse discover = DiscoveryService.discover(Schema.getSchemaRegistry());
/* 24 */     return DataProvider.saveStable(cache, (JsonElement)DiscoveryService.DiscoverResponse.CODEC.codec().encodeStart((DynamicOps)JsonOps.INSTANCE, discover).getOrThrow(), this.path);
/*    */   }
/*    */   
/*    */   public String getName() {
/* 29 */     return "Json RPC API schema";
/*    */   }
/*    */ }


/* Location:              C:\Users\karol\AppData\Roaming\ModrinthApp\meta\versions\26.2-0.19.5\26.2-0.19.5.jar!\net\minecraft\server\jsonrpc\dataprovider\JsonRpcApiSchema.class
 * Java compiler version: 25 (69.0)
 * JD-Core Version:       1.1.3
 */