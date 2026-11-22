/*    */ package net.minecraft.server.jsonrpc.methods;
/*    */ 
/*    */ import com.mojang.datafixers.kinds.App;
/*    */ import com.mojang.datafixers.kinds.Applicative;
/*    */ import com.mojang.datafixers.util.Function4;
/*    */ import com.mojang.serialization.Codec;
/*    */ import com.mojang.serialization.MapCodec;
/*    */ import com.mojang.serialization.codecs.RecordCodecBuilder;
/*    */ import java.util.List;
/*    */ import net.minecraft.server.jsonrpc.api.MethodInfo;
/*    */ 
/*    */ public final class DiscoverResponse extends Record {
/*    */   private final String jsonRpcProtocolVersion;
/*    */   
/*    */   private final DiscoveryService.DiscoverInfo discoverInfo;
/*    */   
/*    */   private final List<MethodInfo.Named<?, ?>> methods;
/*    */   
/*    */   private final DiscoveryService.DiscoverComponents components;
/*    */   
/*    */   public static final MapCodec<DiscoverResponse> CODEC;
/*    */   
/*    */   public DiscoverResponse(String jsonRpcProtocolVersion, DiscoveryService.DiscoverInfo discoverInfo, List<MethodInfo.Named<?, ?>> methods, DiscoveryService.DiscoverComponents components) {
/* 42 */     this.jsonRpcProtocolVersion = jsonRpcProtocolVersion;
/* 42 */     this.discoverInfo = discoverInfo;
/* 42 */     this.methods = methods;
/* 42 */     this.components = components;
/*    */   }
/*    */   
/*    */   public final String toString() {
/*    */     // Byte code:
/*    */     //   0: aload_0
/*    */     //   1: <illegal opcode> toString : (Lnet/minecraft/server/jsonrpc/methods/DiscoveryService$DiscoverResponse;)Ljava/lang/String;
/*    */     //   6: areturn
/*    */     // Line number table:
/*    */     //   Java source line number -> byte code offset
/*    */     //   #42	-> 0
/*    */     // Local variable table:
/*    */     //   start	length	slot	name	descriptor
/*    */     //   0	7	0	this	Lnet/minecraft/server/jsonrpc/methods/DiscoveryService$DiscoverResponse;
/*    */   }
/*    */   
/*    */   public final int hashCode() {
/*    */     // Byte code:
/*    */     //   0: aload_0
/*    */     //   1: <illegal opcode> hashCode : (Lnet/minecraft/server/jsonrpc/methods/DiscoveryService$DiscoverResponse;)I
/*    */     //   6: ireturn
/*    */     // Line number table:
/*    */     //   Java source line number -> byte code offset
/*    */     //   #42	-> 0
/*    */     // Local variable table:
/*    */     //   start	length	slot	name	descriptor
/*    */     //   0	7	0	this	Lnet/minecraft/server/jsonrpc/methods/DiscoveryService$DiscoverResponse;
/*    */   }
/*    */   
/*    */   public final boolean equals(Object o) {
/*    */     // Byte code:
/*    */     //   0: aload_0
/*    */     //   1: aload_1
/*    */     //   2: <illegal opcode> equals : (Lnet/minecraft/server/jsonrpc/methods/DiscoveryService$DiscoverResponse;Ljava/lang/Object;)Z
/*    */     //   7: ireturn
/*    */     // Line number table:
/*    */     //   Java source line number -> byte code offset
/*    */     //   #42	-> 0
/*    */     // Local variable table:
/*    */     //   start	length	slot	name	descriptor
/*    */     //   0	8	0	this	Lnet/minecraft/server/jsonrpc/methods/DiscoveryService$DiscoverResponse;
/*    */     //   0	8	1	o	Ljava/lang/Object;
/*    */   }
/*    */   
/*    */   public String jsonRpcProtocolVersion() {
/* 42 */     return this.jsonRpcProtocolVersion;
/*    */   }
/*    */   
/*    */   public DiscoveryService.DiscoverInfo discoverInfo() {
/* 42 */     return this.discoverInfo;
/*    */   }
/*    */   
/*    */   public List<MethodInfo.Named<?, ?>> methods() {
/* 42 */     return this.methods;
/*    */   }
/*    */   
/*    */   public DiscoveryService.DiscoverComponents components() {
/* 42 */     return this.components;
/*    */   }
/*    */   
/*    */   static {
/* 43 */     CODEC = RecordCodecBuilder.mapCodec(i -> i.group((App)Codec.STRING.fieldOf("openrpc").forGetter(DiscoverResponse::jsonRpcProtocolVersion), (App)DiscoveryService.DiscoverInfo.CODEC.codec().fieldOf("info").forGetter(DiscoverResponse::discoverInfo), (App)Codec.list(MethodInfo.Named.CODEC).fieldOf("methods").forGetter(DiscoverResponse::methods), (App)DiscoveryService.DiscoverComponents.CODEC.codec().fieldOf("components").forGetter(DiscoverResponse::components)).apply((Applicative)i, DiscoverResponse::new));
/*    */   }
/*    */ }


/* Location:              C:\Users\karol\AppData\Roaming\ModrinthApp\meta\versions\26.2-0.19.5\26.2-0.19.5.jar!\net\minecraft\server\jsonrpc\methods\DiscoveryService$DiscoverResponse.class
 * Java compiler version: 25 (69.0)
 * JD-Core Version:       1.1.3
 */