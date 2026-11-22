/*    */ package net.minecraft.resources;
/*    */ 
/*    */ import com.google.common.collect.MapMaker;
/*    */ import com.mojang.serialization.Codec;
/*    */ import io.netty.buffer.ByteBuf;
/*    */ import java.util.Optional;
/*    */ import java.util.concurrent.ConcurrentMap;
/*    */ import java.util.function.UnaryOperator;
/*    */ import net.minecraft.core.Registry;
/*    */ import net.minecraft.core.registries.Registries;
/*    */ import net.minecraft.network.codec.StreamCodec;
/*    */ 
/*    */ public class ResourceKey<T> {
/*    */   private static final class InternKey extends Record {
/*    */     private final Identifier registry;
/*    */     
/*    */     private final Identifier identifier;
/*    */     
/*    */     private InternKey(Identifier registry, Identifier identifier) {
/* 17 */       this.registry = registry;
/* 17 */       this.identifier = identifier;
/*    */     }
/*    */     
/*    */     public final String toString() {
/*    */       // Byte code:
/*    */       //   0: aload_0
/*    */       //   1: <illegal opcode> toString : (Lnet/minecraft/resources/ResourceKey$InternKey;)Ljava/lang/String;
/*    */       //   6: areturn
/*    */       // Line number table:
/*    */       //   Java source line number -> byte code offset
/*    */       //   #17	-> 0
/*    */       // Local variable table:
/*    */       //   start	length	slot	name	descriptor
/*    */       //   0	7	0	this	Lnet/minecraft/resources/ResourceKey$InternKey;
/*    */     }
/*    */     
/*    */     public final int hashCode() {
/*    */       // Byte code:
/*    */       //   0: aload_0
/*    */       //   1: <illegal opcode> hashCode : (Lnet/minecraft/resources/ResourceKey$InternKey;)I
/*    */       //   6: ireturn
/*    */       // Line number table:
/*    */       //   Java source line number -> byte code offset
/*    */       //   #17	-> 0
/*    */       // Local variable table:
/*    */       //   start	length	slot	name	descriptor
/*    */       //   0	7	0	this	Lnet/minecraft/resources/ResourceKey$InternKey;
/*    */     }
/*    */     
/*    */     public final boolean equals(Object o) {
/*    */       // Byte code:
/*    */       //   0: aload_0
/*    */       //   1: aload_1
/*    */       //   2: <illegal opcode> equals : (Lnet/minecraft/resources/ResourceKey$InternKey;Ljava/lang/Object;)Z
/*    */       //   7: ireturn
/*    */       // Line number table:
/*    */       //   Java source line number -> byte code offset
/*    */       //   #17	-> 0
/*    */       // Local variable table:
/*    */       //   start	length	slot	name	descriptor
/*    */       //   0	8	0	this	Lnet/minecraft/resources/ResourceKey$InternKey;
/*    */       //   0	8	1	o	Ljava/lang/Object;
/*    */     }
/*    */     
/*    */     public Identifier registry() {
/* 17 */       return this.registry;
/*    */     }
/*    */     
/*    */     public Identifier identifier() {
/* 17 */       return this.identifier;
/*    */     }
/*    */   }
/*    */   
/* 19 */   private static final ConcurrentMap<InternKey, ResourceKey<?>> VALUES = (new MapMaker()).weakValues().makeMap();
/*    */   
/*    */   private final Identifier registryName;
/*    */   
/*    */   private final Identifier identifier;
/*    */   
/*    */   public static <T> Codec<ResourceKey<T>> codec(ResourceKey<? extends Registry<T>> registryName) {
/* 25 */     return Identifier.CODEC.xmap(name -> create(registryName, name), ResourceKey::identifier);
/*    */   }
/*    */   
/*    */   public static <T> StreamCodec<ByteBuf, ResourceKey<T>> streamCodec(ResourceKey<? extends Registry<T>> registryName) {
/* 29 */     return Identifier.STREAM_CODEC.map(name -> create(registryName, name), ResourceKey::identifier);
/*    */   }
/*    */   
/*    */   public static <T> ResourceKey<T> create(ResourceKey<? extends Registry<T>> registryName, Identifier location) {
/* 33 */     return create(registryName.identifier, location);
/*    */   }
/*    */   
/*    */   public static <T> ResourceKey<Registry<T>> createRegistryKey(Identifier identifier) {
/* 38 */     return create(Registries.ROOT_REGISTRY_NAME, identifier);
/*    */   }
/*    */   
/*    */   private static <T> ResourceKey<T> create(Identifier registryName, Identifier identifier) {
/* 43 */     return (ResourceKey<T>)VALUES.computeIfAbsent(new InternKey(registryName, identifier), k -> new ResourceKey(k.registry, k.identifier));
/*    */   }
/*    */   
/*    */   private ResourceKey(Identifier registryName, Identifier identifier) {
/* 47 */     this.registryName = registryName;
/* 48 */     this.identifier = identifier;
/*    */   }
/*    */   
/*    */   public String toString() {
/* 53 */     return "ResourceKey[" + String.valueOf(this.registryName) + " / " + String.valueOf(this.identifier) + "]";
/*    */   }
/*    */   
/*    */   public boolean isFor(ResourceKey<? extends Registry<?>> registry) {
/* 57 */     return this.registryName.equals(registry.identifier());
/*    */   }
/*    */   
/*    */   public <E> Optional<ResourceKey<E>> cast(ResourceKey<? extends Registry<E>> registry) {
/* 62 */     return isFor(registry) ? Optional.<ResourceKey<E>>of(this) : Optional.<ResourceKey<E>>empty();
/*    */   }
/*    */   
/*    */   public <E> ResourceKey<E> dependent(ResourceKey<? extends Registry<E>> registryKey, String suffix) {
/* 66 */     return create(registryKey, this.identifier.withSuffix(suffix));
/*    */   }
/*    */   
/*    */   public <E> ResourceKey<E> dependent(ResourceKey<? extends Registry<E>> registryKey, UnaryOperator<String> decoration) {
/* 70 */     return create(registryKey, this.identifier.withPath(decoration));
/*    */   }
/*    */   
/*    */   public Identifier identifier() {
/* 74 */     return this.identifier;
/*    */   }
/*    */   
/*    */   public Identifier registry() {
/* 78 */     return this.registryName;
/*    */   }
/*    */   
/*    */   public ResourceKey<Registry<T>> registryKey() {
/* 82 */     return createRegistryKey(this.registryName);
/*    */   }
/*    */ }


/* Location:              C:\Users\karol\AppData\Roaming\ModrinthApp\meta\versions\26.2-0.19.5\26.2-0.19.5.jar!\net\minecraft\resources\ResourceKey.class
 * Java compiler version: 25 (69.0)
 * JD-Core Version:       1.1.3
 */