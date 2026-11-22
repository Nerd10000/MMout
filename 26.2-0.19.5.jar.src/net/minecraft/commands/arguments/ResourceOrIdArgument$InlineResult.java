/*     */ package net.minecraft.commands.arguments;
/*     */ 
/*     */ import com.mojang.brigadier.ImmutableStringReader;
/*     */ import com.mojang.brigadier.exceptions.CommandSyntaxException;
/*     */ import com.mojang.serialization.Codec;
/*     */ import com.mojang.serialization.DynamicOps;
/*     */ import net.minecraft.core.Holder;
/*     */ import net.minecraft.core.HolderLookup;
/*     */ 
/*     */ public final class InlineResult<T, O> extends Record implements ResourceOrIdArgument.Result<T, O> {
/*     */   private final O value;
/*     */   
/*     */   public InlineResult(O value) {
/* 181 */     this.value = value;
/*     */   }
/*     */   
/*     */   public final String toString() {
/*     */     // Byte code:
/*     */     //   0: aload_0
/*     */     //   1: <illegal opcode> toString : (Lnet/minecraft/commands/arguments/ResourceOrIdArgument$InlineResult;)Ljava/lang/String;
/*     */     //   6: areturn
/*     */     // Line number table:
/*     */     //   Java source line number -> byte code offset
/*     */     //   #181	-> 0
/*     */     // Local variable table:
/*     */     //   start	length	slot	name	descriptor
/*     */     //   0	7	0	this	Lnet/minecraft/commands/arguments/ResourceOrIdArgument$InlineResult;
/*     */     // Local variable type table:
/*     */     //   start	length	slot	name	signature
/*     */     //   0	7	0	this	Lnet/minecraft/commands/arguments/ResourceOrIdArgument$InlineResult<TT;TO;>;
/*     */   }
/*     */   
/*     */   public final int hashCode() {
/*     */     // Byte code:
/*     */     //   0: aload_0
/*     */     //   1: <illegal opcode> hashCode : (Lnet/minecraft/commands/arguments/ResourceOrIdArgument$InlineResult;)I
/*     */     //   6: ireturn
/*     */     // Line number table:
/*     */     //   Java source line number -> byte code offset
/*     */     //   #181	-> 0
/*     */     // Local variable table:
/*     */     //   start	length	slot	name	descriptor
/*     */     //   0	7	0	this	Lnet/minecraft/commands/arguments/ResourceOrIdArgument$InlineResult;
/*     */     // Local variable type table:
/*     */     //   start	length	slot	name	signature
/*     */     //   0	7	0	this	Lnet/minecraft/commands/arguments/ResourceOrIdArgument$InlineResult<TT;TO;>;
/*     */   }
/*     */   
/*     */   public final boolean equals(Object o) {
/*     */     // Byte code:
/*     */     //   0: aload_0
/*     */     //   1: aload_1
/*     */     //   2: <illegal opcode> equals : (Lnet/minecraft/commands/arguments/ResourceOrIdArgument$InlineResult;Ljava/lang/Object;)Z
/*     */     //   7: ireturn
/*     */     // Line number table:
/*     */     //   Java source line number -> byte code offset
/*     */     //   #181	-> 0
/*     */     // Local variable table:
/*     */     //   start	length	slot	name	descriptor
/*     */     //   0	8	0	this	Lnet/minecraft/commands/arguments/ResourceOrIdArgument$InlineResult;
/*     */     //   0	8	1	o	Ljava/lang/Object;
/*     */     // Local variable type table:
/*     */     //   start	length	slot	name	signature
/*     */     //   0	8	0	this	Lnet/minecraft/commands/arguments/ResourceOrIdArgument$InlineResult<TT;TO;>;
/*     */   }
/*     */   
/*     */   public O value() {
/* 181 */     return this.value;
/*     */   }
/*     */   
/*     */   public Holder<T> parse(ImmutableStringReader reader, HolderLookup.Provider lookup, DynamicOps<O> ops, Codec<T> codec, HolderLookup.RegistryLookup<T> elementLookup) throws CommandSyntaxException {
/* 184 */     return Holder.direct(codec.parse((DynamicOps)lookup.createSerializationContext(ops), this.value)
/* 185 */         .getOrThrow(msg -> ResourceOrIdArgument.ERROR_FAILED_TO_PARSE.createWithContext(reader, msg)));
/*     */   }
/*     */ }


/* Location:              C:\Users\karol\AppData\Roaming\ModrinthApp\meta\versions\26.2-0.19.5\26.2-0.19.5.jar!\net\minecraft\commands\arguments\ResourceOrIdArgument$InlineResult.class
 * Java compiler version: 25 (69.0)
 * JD-Core Version:       1.1.3
 */