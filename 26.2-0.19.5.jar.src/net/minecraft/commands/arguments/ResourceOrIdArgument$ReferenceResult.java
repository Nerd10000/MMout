/*     */ package net.minecraft.commands.arguments;
/*     */ 
/*     */ import com.mojang.brigadier.ImmutableStringReader;
/*     */ import com.mojang.brigadier.exceptions.CommandSyntaxException;
/*     */ import com.mojang.serialization.Codec;
/*     */ import com.mojang.serialization.DynamicOps;
/*     */ import net.minecraft.core.Holder;
/*     */ import net.minecraft.core.HolderLookup;
/*     */ import net.minecraft.resources.ResourceKey;
/*     */ 
/*     */ public final class ReferenceResult<T, O> extends Record implements ResourceOrIdArgument.Result<T, O> {
/*     */   private final ResourceKey<T> key;
/*     */   
/*     */   public ReferenceResult(ResourceKey<T> key) {
/* 189 */     this.key = key;
/*     */   }
/*     */   
/*     */   public final String toString() {
/*     */     // Byte code:
/*     */     //   0: aload_0
/*     */     //   1: <illegal opcode> toString : (Lnet/minecraft/commands/arguments/ResourceOrIdArgument$ReferenceResult;)Ljava/lang/String;
/*     */     //   6: areturn
/*     */     // Line number table:
/*     */     //   Java source line number -> byte code offset
/*     */     //   #189	-> 0
/*     */     // Local variable table:
/*     */     //   start	length	slot	name	descriptor
/*     */     //   0	7	0	this	Lnet/minecraft/commands/arguments/ResourceOrIdArgument$ReferenceResult;
/*     */     // Local variable type table:
/*     */     //   start	length	slot	name	signature
/*     */     //   0	7	0	this	Lnet/minecraft/commands/arguments/ResourceOrIdArgument$ReferenceResult<TT;TO;>;
/*     */   }
/*     */   
/*     */   public final int hashCode() {
/*     */     // Byte code:
/*     */     //   0: aload_0
/*     */     //   1: <illegal opcode> hashCode : (Lnet/minecraft/commands/arguments/ResourceOrIdArgument$ReferenceResult;)I
/*     */     //   6: ireturn
/*     */     // Line number table:
/*     */     //   Java source line number -> byte code offset
/*     */     //   #189	-> 0
/*     */     // Local variable table:
/*     */     //   start	length	slot	name	descriptor
/*     */     //   0	7	0	this	Lnet/minecraft/commands/arguments/ResourceOrIdArgument$ReferenceResult;
/*     */     // Local variable type table:
/*     */     //   start	length	slot	name	signature
/*     */     //   0	7	0	this	Lnet/minecraft/commands/arguments/ResourceOrIdArgument$ReferenceResult<TT;TO;>;
/*     */   }
/*     */   
/*     */   public final boolean equals(Object o) {
/*     */     // Byte code:
/*     */     //   0: aload_0
/*     */     //   1: aload_1
/*     */     //   2: <illegal opcode> equals : (Lnet/minecraft/commands/arguments/ResourceOrIdArgument$ReferenceResult;Ljava/lang/Object;)Z
/*     */     //   7: ireturn
/*     */     // Line number table:
/*     */     //   Java source line number -> byte code offset
/*     */     //   #189	-> 0
/*     */     // Local variable table:
/*     */     //   start	length	slot	name	descriptor
/*     */     //   0	8	0	this	Lnet/minecraft/commands/arguments/ResourceOrIdArgument$ReferenceResult;
/*     */     //   0	8	1	o	Ljava/lang/Object;
/*     */     // Local variable type table:
/*     */     //   start	length	slot	name	signature
/*     */     //   0	8	0	this	Lnet/minecraft/commands/arguments/ResourceOrIdArgument$ReferenceResult<TT;TO;>;
/*     */   }
/*     */   
/*     */   public ResourceKey<T> key() {
/* 189 */     return this.key;
/*     */   }
/*     */   
/*     */   public Holder<T> parse(ImmutableStringReader reader, HolderLookup.Provider lookup, DynamicOps<O> ops, Codec<T> codec, HolderLookup.RegistryLookup<T> elementLookup) throws CommandSyntaxException {
/* 192 */     return (Holder<T>)elementLookup.get(this.key).orElseThrow(() -> ResourceOrIdArgument.ERROR_NO_SUCH_ELEMENT.createWithContext(reader, this.key.identifier(), this.key.registry()));
/*     */   }
/*     */ }


/* Location:              C:\Users\karol\AppData\Roaming\ModrinthApp\meta\versions\26.2-0.19.5\26.2-0.19.5.jar!\net\minecraft\commands\arguments\ResourceOrIdArgument$ReferenceResult.class
 * Java compiler version: 25 (69.0)
 * JD-Core Version:       1.1.3
 */