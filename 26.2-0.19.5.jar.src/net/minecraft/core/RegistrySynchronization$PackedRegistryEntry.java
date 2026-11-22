/*    */ package net.minecraft.core;
/*    */ 
/*    */ import io.netty.buffer.ByteBuf;
/*    */ import java.util.Optional;
/*    */ import java.util.function.BiFunction;
/*    */ import net.minecraft.nbt.Tag;
/*    */ import net.minecraft.network.codec.ByteBufCodecs;
/*    */ import net.minecraft.network.codec.StreamCodec;
/*    */ import net.minecraft.resources.Identifier;
/*    */ 
/*    */ public final class PackedRegistryEntry extends Record {
/*    */   private final Identifier id;
/*    */   
/*    */   private final Optional<Tag> data;
/*    */   
/*    */   public PackedRegistryEntry(Identifier id, Optional<Tag> data) {
/* 64 */     this.id = id;
/* 64 */     this.data = data;
/*    */   }
/*    */   
/*    */   public final String toString() {
/*    */     // Byte code:
/*    */     //   0: aload_0
/*    */     //   1: <illegal opcode> toString : (Lnet/minecraft/core/RegistrySynchronization$PackedRegistryEntry;)Ljava/lang/String;
/*    */     //   6: areturn
/*    */     // Line number table:
/*    */     //   Java source line number -> byte code offset
/*    */     //   #64	-> 0
/*    */     // Local variable table:
/*    */     //   start	length	slot	name	descriptor
/*    */     //   0	7	0	this	Lnet/minecraft/core/RegistrySynchronization$PackedRegistryEntry;
/*    */   }
/*    */   
/*    */   public final int hashCode() {
/*    */     // Byte code:
/*    */     //   0: aload_0
/*    */     //   1: <illegal opcode> hashCode : (Lnet/minecraft/core/RegistrySynchronization$PackedRegistryEntry;)I
/*    */     //   6: ireturn
/*    */     // Line number table:
/*    */     //   Java source line number -> byte code offset
/*    */     //   #64	-> 0
/*    */     // Local variable table:
/*    */     //   start	length	slot	name	descriptor
/*    */     //   0	7	0	this	Lnet/minecraft/core/RegistrySynchronization$PackedRegistryEntry;
/*    */   }
/*    */   
/*    */   public final boolean equals(Object o) {
/*    */     // Byte code:
/*    */     //   0: aload_0
/*    */     //   1: aload_1
/*    */     //   2: <illegal opcode> equals : (Lnet/minecraft/core/RegistrySynchronization$PackedRegistryEntry;Ljava/lang/Object;)Z
/*    */     //   7: ireturn
/*    */     // Line number table:
/*    */     //   Java source line number -> byte code offset
/*    */     //   #64	-> 0
/*    */     // Local variable table:
/*    */     //   start	length	slot	name	descriptor
/*    */     //   0	8	0	this	Lnet/minecraft/core/RegistrySynchronization$PackedRegistryEntry;
/*    */     //   0	8	1	o	Ljava/lang/Object;
/*    */   }
/*    */   
/*    */   public Identifier id() {
/* 64 */     return this.id;
/*    */   }
/*    */   
/*    */   public Optional<Tag> data() {
/* 64 */     return this.data;
/*    */   }
/*    */   
/* 65 */   public static final StreamCodec<ByteBuf, PackedRegistryEntry> STREAM_CODEC = StreamCodec.composite(Identifier.STREAM_CODEC, PackedRegistryEntry::id, ByteBufCodecs.TAG
/*    */       
/* 67 */       .apply(ByteBufCodecs::optional), PackedRegistryEntry::data, PackedRegistryEntry::new);
/*    */ }


/* Location:              C:\Users\karol\AppData\Roaming\ModrinthApp\meta\versions\26.2-0.19.5\26.2-0.19.5.jar!\net\minecraft\core\RegistrySynchronization$PackedRegistryEntry.class
 * Java compiler version: 25 (69.0)
 * JD-Core Version:       1.1.3
 */