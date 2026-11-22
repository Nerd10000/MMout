/*    */ package net.minecraft.commands.arguments.item;
/*    */ 
/*    */ import com.mojang.brigadier.ImmutableStringReader;
/*    */ import com.mojang.brigadier.exceptions.CommandSyntaxException;
/*    */ import com.mojang.serialization.Codec;
/*    */ import com.mojang.serialization.DataResult;
/*    */ import com.mojang.serialization.Decoder;
/*    */ import com.mojang.serialization.Dynamic;
/*    */ import java.util.Objects;
/*    */ import java.util.function.Predicate;
/*    */ import net.minecraft.core.component.DataComponentType;
/*    */ import net.minecraft.resources.Identifier;
/*    */ import net.minecraft.world.item.ItemStack;
/*    */ 
/*    */ final class ComponentWrapper extends Record {
/*    */   private final Identifier id;
/*    */   
/*    */   private final Predicate<ItemStack> presenceChecker;
/*    */   
/*    */   private final Decoder<? extends Predicate<ItemStack>> valueChecker;
/*    */   
/*    */   private ComponentWrapper(Identifier id, Predicate<ItemStack> presenceChecker, Decoder<? extends Predicate<ItemStack>> valueChecker) {
/* 51 */     this.id = id;
/* 51 */     this.presenceChecker = presenceChecker;
/* 51 */     this.valueChecker = valueChecker;
/*    */   }
/*    */   
/*    */   public final String toString() {
/*    */     // Byte code:
/*    */     //   0: aload_0
/*    */     //   1: <illegal opcode> toString : (Lnet/minecraft/commands/arguments/item/ItemPredicateArgument$ComponentWrapper;)Ljava/lang/String;
/*    */     //   6: areturn
/*    */     // Line number table:
/*    */     //   Java source line number -> byte code offset
/*    */     //   #51	-> 0
/*    */     // Local variable table:
/*    */     //   start	length	slot	name	descriptor
/*    */     //   0	7	0	this	Lnet/minecraft/commands/arguments/item/ItemPredicateArgument$ComponentWrapper;
/*    */   }
/*    */   
/*    */   public final int hashCode() {
/*    */     // Byte code:
/*    */     //   0: aload_0
/*    */     //   1: <illegal opcode> hashCode : (Lnet/minecraft/commands/arguments/item/ItemPredicateArgument$ComponentWrapper;)I
/*    */     //   6: ireturn
/*    */     // Line number table:
/*    */     //   Java source line number -> byte code offset
/*    */     //   #51	-> 0
/*    */     // Local variable table:
/*    */     //   start	length	slot	name	descriptor
/*    */     //   0	7	0	this	Lnet/minecraft/commands/arguments/item/ItemPredicateArgument$ComponentWrapper;
/*    */   }
/*    */   
/*    */   public final boolean equals(Object o) {
/*    */     // Byte code:
/*    */     //   0: aload_0
/*    */     //   1: aload_1
/*    */     //   2: <illegal opcode> equals : (Lnet/minecraft/commands/arguments/item/ItemPredicateArgument$ComponentWrapper;Ljava/lang/Object;)Z
/*    */     //   7: ireturn
/*    */     // Line number table:
/*    */     //   Java source line number -> byte code offset
/*    */     //   #51	-> 0
/*    */     // Local variable table:
/*    */     //   start	length	slot	name	descriptor
/*    */     //   0	8	0	this	Lnet/minecraft/commands/arguments/item/ItemPredicateArgument$ComponentWrapper;
/*    */     //   0	8	1	o	Ljava/lang/Object;
/*    */   }
/*    */   
/*    */   public Identifier id() {
/* 51 */     return this.id;
/*    */   }
/*    */   
/*    */   public Predicate<ItemStack> presenceChecker() {
/* 51 */     return this.presenceChecker;
/*    */   }
/*    */   
/*    */   public Decoder<? extends Predicate<ItemStack>> valueChecker() {
/* 51 */     return this.valueChecker;
/*    */   }
/*    */   
/*    */   public static <T> ComponentWrapper create(ImmutableStringReader reader, Identifier id, DataComponentType<T> type) throws CommandSyntaxException {
/* 53 */     Codec<T> codec = type.codec();
/* 54 */     if (codec == null)
/* 55 */       throw ItemPredicateArgument.ERROR_UNKNOWN_COMPONENT.createWithContext(reader, id); 
/* 58 */     return new ComponentWrapper(id, itemStack -> itemStack.has(type), codec
/*    */         
/* 61 */         .map(expected -> ()));
/*    */   }
/*    */   
/*    */   public Predicate<ItemStack> decode(ImmutableStringReader reader, Dynamic<?> value) throws CommandSyntaxException {
/* 69 */     DataResult<? extends Predicate<ItemStack>> result = this.valueChecker.parse(value);
/* 70 */     return (Predicate<ItemStack>)result.getOrThrow(message -> ItemPredicateArgument.ERROR_MALFORMED_COMPONENT.createWithContext(reader, this.id.toString(), message));
/*    */   }
/*    */ }


/* Location:              C:\Users\karol\AppData\Roaming\ModrinthApp\meta\versions\26.2-0.19.5\26.2-0.19.5.jar!\net\minecraft\commands\arguments\item\ItemPredicateArgument$ComponentWrapper.class
 * Java compiler version: 25 (69.0)
 * JD-Core Version:       1.1.3
 */