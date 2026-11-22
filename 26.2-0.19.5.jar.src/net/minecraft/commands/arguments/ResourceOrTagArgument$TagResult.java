/*    */ package net.minecraft.commands.arguments;
/*    */ 
/*    */ import com.mojang.datafixers.util.Either;
/*    */ import java.util.Optional;
/*    */ import net.minecraft.core.Holder;
/*    */ import net.minecraft.core.HolderSet;
/*    */ import net.minecraft.core.Registry;
/*    */ import net.minecraft.resources.ResourceKey;
/*    */ 
/*    */ final class TagResult<T> extends Record implements ResourceOrTagArgument.Result<T> {
/*    */   private final HolderSet.Named<T> tag;
/*    */   
/*    */   private TagResult(HolderSet.Named<T> tag) {
/* 70 */     this.tag = tag;
/*    */   }
/*    */   
/*    */   public final String toString() {
/*    */     // Byte code:
/*    */     //   0: aload_0
/*    */     //   1: <illegal opcode> toString : (Lnet/minecraft/commands/arguments/ResourceOrTagArgument$TagResult;)Ljava/lang/String;
/*    */     //   6: areturn
/*    */     // Line number table:
/*    */     //   Java source line number -> byte code offset
/*    */     //   #70	-> 0
/*    */     // Local variable table:
/*    */     //   start	length	slot	name	descriptor
/*    */     //   0	7	0	this	Lnet/minecraft/commands/arguments/ResourceOrTagArgument$TagResult;
/*    */     // Local variable type table:
/*    */     //   start	length	slot	name	signature
/*    */     //   0	7	0	this	Lnet/minecraft/commands/arguments/ResourceOrTagArgument$TagResult<TT;>;
/*    */   }
/*    */   
/*    */   public final int hashCode() {
/*    */     // Byte code:
/*    */     //   0: aload_0
/*    */     //   1: <illegal opcode> hashCode : (Lnet/minecraft/commands/arguments/ResourceOrTagArgument$TagResult;)I
/*    */     //   6: ireturn
/*    */     // Line number table:
/*    */     //   Java source line number -> byte code offset
/*    */     //   #70	-> 0
/*    */     // Local variable table:
/*    */     //   start	length	slot	name	descriptor
/*    */     //   0	7	0	this	Lnet/minecraft/commands/arguments/ResourceOrTagArgument$TagResult;
/*    */     // Local variable type table:
/*    */     //   start	length	slot	name	signature
/*    */     //   0	7	0	this	Lnet/minecraft/commands/arguments/ResourceOrTagArgument$TagResult<TT;>;
/*    */   }
/*    */   
/*    */   public final boolean equals(Object o) {
/*    */     // Byte code:
/*    */     //   0: aload_0
/*    */     //   1: aload_1
/*    */     //   2: <illegal opcode> equals : (Lnet/minecraft/commands/arguments/ResourceOrTagArgument$TagResult;Ljava/lang/Object;)Z
/*    */     //   7: ireturn
/*    */     // Line number table:
/*    */     //   Java source line number -> byte code offset
/*    */     //   #70	-> 0
/*    */     // Local variable table:
/*    */     //   start	length	slot	name	descriptor
/*    */     //   0	8	0	this	Lnet/minecraft/commands/arguments/ResourceOrTagArgument$TagResult;
/*    */     //   0	8	1	o	Ljava/lang/Object;
/*    */     // Local variable type table:
/*    */     //   start	length	slot	name	signature
/*    */     //   0	8	0	this	Lnet/minecraft/commands/arguments/ResourceOrTagArgument$TagResult<TT;>;
/*    */   }
/*    */   
/*    */   public HolderSet.Named<T> tag() {
/* 70 */     return this.tag;
/*    */   }
/*    */   
/*    */   public Either<Holder.Reference<T>, HolderSet.Named<T>> unwrap() {
/* 73 */     return Either.right(this.tag);
/*    */   }
/*    */   
/*    */   public <E> Optional<ResourceOrTagArgument.Result<E>> cast(ResourceKey<? extends Registry<E>> registryKey) {
/* 79 */     return this.tag.key().isFor(registryKey) ? Optional.<ResourceOrTagArgument.Result<E>>of(this) : Optional.<ResourceOrTagArgument.Result<E>>empty();
/*    */   }
/*    */   
/*    */   public boolean test(Holder<T> holder) {
/* 84 */     return this.tag.contains(holder);
/*    */   }
/*    */   
/*    */   public String asPrintable() {
/* 89 */     return "#" + String.valueOf(this.tag.key().location());
/*    */   }
/*    */ }


/* Location:              C:\Users\karol\AppData\Roaming\ModrinthApp\meta\versions\26.2-0.19.5\26.2-0.19.5.jar!\net\minecraft\commands\arguments\ResourceOrTagArgument$TagResult.class
 * Java compiler version: 25 (69.0)
 * JD-Core Version:       1.1.3
 */