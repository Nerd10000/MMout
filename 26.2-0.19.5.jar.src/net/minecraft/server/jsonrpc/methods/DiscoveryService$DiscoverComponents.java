/*    */ package net.minecraft.server.jsonrpc.methods;
/*    */ 
/*    */ import com.mojang.datafixers.kinds.App;
/*    */ import com.mojang.datafixers.kinds.Applicative;
/*    */ import com.mojang.serialization.Codec;
/*    */ import com.mojang.serialization.MapCodec;
/*    */ import com.mojang.serialization.codecs.RecordCodecBuilder;
/*    */ import java.util.Map;
/*    */ import java.util.function.Function;
/*    */ import net.minecraft.server.jsonrpc.api.Schema;
/*    */ 
/*    */ public final class DiscoverComponents extends Record {
/*    */   private final Map<String, Schema<?>> schemas;
/*    */   
/*    */   public DiscoverComponents(Map<String, Schema<?>> schemas) {
/* 51 */     this.schemas = schemas;
/*    */   }
/*    */   
/*    */   public final String toString() {
/*    */     // Byte code:
/*    */     //   0: aload_0
/*    */     //   1: <illegal opcode> toString : (Lnet/minecraft/server/jsonrpc/methods/DiscoveryService$DiscoverComponents;)Ljava/lang/String;
/*    */     //   6: areturn
/*    */     // Line number table:
/*    */     //   Java source line number -> byte code offset
/*    */     //   #51	-> 0
/*    */     // Local variable table:
/*    */     //   start	length	slot	name	descriptor
/*    */     //   0	7	0	this	Lnet/minecraft/server/jsonrpc/methods/DiscoveryService$DiscoverComponents;
/*    */   }
/*    */   
/*    */   public final int hashCode() {
/*    */     // Byte code:
/*    */     //   0: aload_0
/*    */     //   1: <illegal opcode> hashCode : (Lnet/minecraft/server/jsonrpc/methods/DiscoveryService$DiscoverComponents;)I
/*    */     //   6: ireturn
/*    */     // Line number table:
/*    */     //   Java source line number -> byte code offset
/*    */     //   #51	-> 0
/*    */     // Local variable table:
/*    */     //   start	length	slot	name	descriptor
/*    */     //   0	7	0	this	Lnet/minecraft/server/jsonrpc/methods/DiscoveryService$DiscoverComponents;
/*    */   }
/*    */   
/*    */   public final boolean equals(Object o) {
/*    */     // Byte code:
/*    */     //   0: aload_0
/*    */     //   1: aload_1
/*    */     //   2: <illegal opcode> equals : (Lnet/minecraft/server/jsonrpc/methods/DiscoveryService$DiscoverComponents;Ljava/lang/Object;)Z
/*    */     //   7: ireturn
/*    */     // Line number table:
/*    */     //   Java source line number -> byte code offset
/*    */     //   #51	-> 0
/*    */     // Local variable table:
/*    */     //   start	length	slot	name	descriptor
/*    */     //   0	8	0	this	Lnet/minecraft/server/jsonrpc/methods/DiscoveryService$DiscoverComponents;
/*    */     //   0	8	1	o	Ljava/lang/Object;
/*    */   }
/*    */   
/*    */   public Map<String, Schema<?>> schemas() {
/* 51 */     return this.schemas;
/*    */   }
/*    */   
/* 52 */   public static final MapCodec<DiscoverComponents> CODEC = typedSchema();
/*    */   
/*    */   private static MapCodec<DiscoverComponents> typedSchema() {
/* 56 */     return RecordCodecBuilder.mapCodec(i -> i.group((App)Codec.unboundedMap((Codec)Codec.STRING, Schema.CODEC).fieldOf("schemas").forGetter(DiscoverComponents::schemas)).apply((Applicative)i, DiscoverComponents::new));
/*    */   }
/*    */ }


/* Location:              C:\Users\karol\AppData\Roaming\ModrinthApp\meta\versions\26.2-0.19.5\26.2-0.19.5.jar!\net\minecraft\server\jsonrpc\methods\DiscoveryService$DiscoverComponents.class
 * Java compiler version: 25 (69.0)
 * JD-Core Version:       1.1.3
 */