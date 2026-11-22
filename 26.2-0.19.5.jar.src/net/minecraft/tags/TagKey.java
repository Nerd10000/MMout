/*    */ package net.minecraft.tags;
/*    */ 
/*    */ import com.google.common.collect.Interner;
/*    */ import com.google.common.collect.Interners;
/*    */ import com.mojang.serialization.Codec;
/*    */ import com.mojang.serialization.DataResult;
/*    */ import io.netty.buffer.ByteBuf;
/*    */ import java.util.Optional;
/*    */ import net.minecraft.core.Registry;
/*    */ import net.minecraft.network.codec.StreamCodec;
/*    */ import net.minecraft.resources.Identifier;
/*    */ import net.minecraft.resources.ResourceKey;
/*    */ 
/*    */ public final class TagKey<T> extends Record {
/*    */   private final ResourceKey<? extends Registry<T>> registry;
/*    */   
/*    */   private final Identifier location;
/*    */   
/*    */   public final int hashCode() {
/*    */     // Byte code:
/*    */     //   0: aload_0
/*    */     //   1: <illegal opcode> hashCode : (Lnet/minecraft/tags/TagKey;)I
/*    */     //   6: ireturn
/*    */     // Line number table:
/*    */     //   Java source line number -> byte code offset
/*    */     //   #15	-> 0
/*    */     // Local variable table:
/*    */     //   start	length	slot	name	descriptor
/*    */     //   0	7	0	this	Lnet/minecraft/tags/TagKey;
/*    */     // Local variable type table:
/*    */     //   start	length	slot	name	signature
/*    */     //   0	7	0	this	Lnet/minecraft/tags/TagKey<TT;>;
/*    */   }
/*    */   
/*    */   public final boolean equals(Object o) {
/*    */     // Byte code:
/*    */     //   0: aload_0
/*    */     //   1: aload_1
/*    */     //   2: <illegal opcode> equals : (Lnet/minecraft/tags/TagKey;Ljava/lang/Object;)Z
/*    */     //   7: ireturn
/*    */     // Line number table:
/*    */     //   Java source line number -> byte code offset
/*    */     //   #15	-> 0
/*    */     // Local variable table:
/*    */     //   start	length	slot	name	descriptor
/*    */     //   0	8	0	this	Lnet/minecraft/tags/TagKey;
/*    */     //   0	8	1	o	Ljava/lang/Object;
/*    */     // Local variable type table:
/*    */     //   start	length	slot	name	signature
/*    */     //   0	8	0	this	Lnet/minecraft/tags/TagKey<TT;>;
/*    */   }
/*    */   
/*    */   public ResourceKey<? extends Registry<T>> registry() {
/* 15 */     return this.registry;
/*    */   }
/*    */   
/*    */   public Identifier location() {
/* 15 */     return this.location;
/*    */   }
/*    */   
/* 16 */   private static final Interner<TagKey<?>> VALUES = Interners.newWeakInterner();
/*    */   
/*    */   @Deprecated
/*    */   public TagKey(ResourceKey<? extends Registry<T>> registry, Identifier location) {
/* 21 */     this.registry = registry;
/* 21 */     this.location = location;
/*    */   }
/*    */   
/*    */   public static <T> Codec<TagKey<T>> codec(ResourceKey<? extends Registry<T>> registryName) {
/* 25 */     return Identifier.CODEC.xmap(name -> create(registryName, name), TagKey::location);
/*    */   }
/*    */   
/*    */   public static <T> Codec<TagKey<T>> hashedCodec(ResourceKey<? extends Registry<T>> registryName) {
/* 29 */     return Codec.STRING.comapFlatMap(name -> name.startsWith("#") ? Identifier.read(name.substring(1)).map(()) : DataResult.error(()), e -> "#" + String.valueOf(e.location));
/*    */   }
/*    */   
/*    */   public static <T> StreamCodec<ByteBuf, TagKey<T>> streamCodec(ResourceKey<? extends Registry<T>> registryName) {
/* 35 */     return Identifier.STREAM_CODEC.map(location -> create(registryName, location), TagKey::location);
/*    */   }
/*    */   
/*    */   public static <T> TagKey<T> create(ResourceKey<? extends Registry<T>> registry, Identifier location) {
/* 40 */     return (TagKey<T>)VALUES.intern(new TagKey<>(registry, location));
/*    */   }
/*    */   
/*    */   public boolean isFor(ResourceKey<? extends Registry<?>> registry) {
/* 44 */     return (this.registry == registry);
/*    */   }
/*    */   
/*    */   public <E> Optional<TagKey<E>> cast(ResourceKey<? extends Registry<E>> registry) {
/* 49 */     return isFor(registry) ? Optional.<TagKey<E>>of(this) : Optional.<TagKey<E>>empty();
/*    */   }
/*    */   
/*    */   public String toString() {
/* 54 */     return "TagKey[" + String.valueOf(this.registry.identifier()) + " / " + String.valueOf(this.location) + "]";
/*    */   }
/*    */ }


/* Location:              C:\Users\karol\AppData\Roaming\ModrinthApp\meta\versions\26.2-0.19.5\26.2-0.19.5.jar!\net\minecraft\tags\TagKey.class
 * Java compiler version: 25 (69.0)
 * JD-Core Version:       1.1.3
 */