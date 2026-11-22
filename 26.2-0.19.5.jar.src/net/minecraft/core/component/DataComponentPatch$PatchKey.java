/*     */ package net.minecraft.core.component;
/*     */ 
/*     */ import com.mojang.serialization.Codec;
/*     */ import com.mojang.serialization.DataResult;
/*     */ import net.minecraft.core.registries.BuiltInRegistries;
/*     */ import net.minecraft.resources.Identifier;
/*     */ 
/*     */ final class PatchKey extends Record {
/*     */   private final DataComponentType<?> type;
/*     */   
/*     */   private final boolean removed;
/*     */   
/*     */   public static final Codec<PatchKey> CODEC;
/*     */   
/*     */   private PatchKey(DataComponentType<?> type, boolean removed) {
/* 272 */     this.type = type;
/* 272 */     this.removed = removed;
/*     */   }
/*     */   
/*     */   public final String toString() {
/*     */     // Byte code:
/*     */     //   0: aload_0
/*     */     //   1: <illegal opcode> toString : (Lnet/minecraft/core/component/DataComponentPatch$PatchKey;)Ljava/lang/String;
/*     */     //   6: areturn
/*     */     // Line number table:
/*     */     //   Java source line number -> byte code offset
/*     */     //   #272	-> 0
/*     */     // Local variable table:
/*     */     //   start	length	slot	name	descriptor
/*     */     //   0	7	0	this	Lnet/minecraft/core/component/DataComponentPatch$PatchKey;
/*     */   }
/*     */   
/*     */   public final int hashCode() {
/*     */     // Byte code:
/*     */     //   0: aload_0
/*     */     //   1: <illegal opcode> hashCode : (Lnet/minecraft/core/component/DataComponentPatch$PatchKey;)I
/*     */     //   6: ireturn
/*     */     // Line number table:
/*     */     //   Java source line number -> byte code offset
/*     */     //   #272	-> 0
/*     */     // Local variable table:
/*     */     //   start	length	slot	name	descriptor
/*     */     //   0	7	0	this	Lnet/minecraft/core/component/DataComponentPatch$PatchKey;
/*     */   }
/*     */   
/*     */   public final boolean equals(Object o) {
/*     */     // Byte code:
/*     */     //   0: aload_0
/*     */     //   1: aload_1
/*     */     //   2: <illegal opcode> equals : (Lnet/minecraft/core/component/DataComponentPatch$PatchKey;Ljava/lang/Object;)Z
/*     */     //   7: ireturn
/*     */     // Line number table:
/*     */     //   Java source line number -> byte code offset
/*     */     //   #272	-> 0
/*     */     // Local variable table:
/*     */     //   start	length	slot	name	descriptor
/*     */     //   0	8	0	this	Lnet/minecraft/core/component/DataComponentPatch$PatchKey;
/*     */     //   0	8	1	o	Ljava/lang/Object;
/*     */   }
/*     */   
/*     */   public DataComponentType<?> type() {
/* 272 */     return this.type;
/*     */   }
/*     */   
/*     */   public boolean removed() {
/* 272 */     return this.removed;
/*     */   }
/*     */   
/*     */   static {
/* 273 */     CODEC = Codec.STRING.flatXmap(string -> {
/*     */           boolean removed = string.startsWith("!");
/*     */           if (removed)
/*     */             string = string.substring("!".length()); 
/*     */           Identifier id = Identifier.tryParse(string);
/*     */           DataComponentType<?> type = (DataComponentType)BuiltInRegistries.DATA_COMPONENT_TYPE.getValue(id);
/*     */           return (type == null) ? DataResult.error(()) : (type.isTransient() ? DataResult.error(()) : DataResult.success(new PatchKey(type, removed)));
/*     */         }key -> {
/*     */           DataComponentType<?> type = key.type();
/*     */           Identifier id = BuiltInRegistries.DATA_COMPONENT_TYPE.getKey(type);
/*     */           return (id == null) ? DataResult.error(()) : DataResult.success(key.removed() ? ("!" + String.valueOf(id)) : id.toString());
/*     */         });
/*     */   }
/*     */   
/*     */   public Codec<?> valueCodec() {
/* 300 */     return this.removed ? Codec.EMPTY.codec() : this.type.codecOrThrow();
/*     */   }
/*     */ }


/* Location:              C:\Users\karol\AppData\Roaming\ModrinthApp\meta\versions\26.2-0.19.5\26.2-0.19.5.jar!\net\minecraft\core\component\DataComponentPatch$PatchKey.class
 * Java compiler version: 25 (69.0)
 * JD-Core Version:       1.1.3
 */