/*    */ package net.minecraft.commands.arguments.item;
/*    */ 
/*    */ import com.mojang.brigadier.ImmutableStringReader;
/*    */ import com.mojang.brigadier.exceptions.CommandSyntaxException;
/*    */ import com.mojang.serialization.DataResult;
/*    */ import com.mojang.serialization.Decoder;
/*    */ import com.mojang.serialization.Dynamic;
/*    */ import java.util.Objects;
/*    */ import java.util.function.Predicate;
/*    */ import net.minecraft.core.Holder;
/*    */ import net.minecraft.core.component.predicates.DataComponentPredicate;
/*    */ import net.minecraft.resources.Identifier;
/*    */ import net.minecraft.world.item.ItemStack;
/*    */ 
/*    */ final class PredicateWrapper extends Record {
/*    */   private final Identifier id;
/*    */   
/*    */   private final Decoder<? extends Predicate<ItemStack>> type;
/*    */   
/*    */   private PredicateWrapper(Identifier id, Decoder<? extends Predicate<ItemStack>> type) {
/* 74 */     this.id = id;
/* 74 */     this.type = type;
/*    */   }
/*    */   
/*    */   public final String toString() {
/*    */     // Byte code:
/*    */     //   0: aload_0
/*    */     //   1: <illegal opcode> toString : (Lnet/minecraft/commands/arguments/item/ItemPredicateArgument$PredicateWrapper;)Ljava/lang/String;
/*    */     //   6: areturn
/*    */     // Line number table:
/*    */     //   Java source line number -> byte code offset
/*    */     //   #74	-> 0
/*    */     // Local variable table:
/*    */     //   start	length	slot	name	descriptor
/*    */     //   0	7	0	this	Lnet/minecraft/commands/arguments/item/ItemPredicateArgument$PredicateWrapper;
/*    */   }
/*    */   
/*    */   public final int hashCode() {
/*    */     // Byte code:
/*    */     //   0: aload_0
/*    */     //   1: <illegal opcode> hashCode : (Lnet/minecraft/commands/arguments/item/ItemPredicateArgument$PredicateWrapper;)I
/*    */     //   6: ireturn
/*    */     // Line number table:
/*    */     //   Java source line number -> byte code offset
/*    */     //   #74	-> 0
/*    */     // Local variable table:
/*    */     //   start	length	slot	name	descriptor
/*    */     //   0	7	0	this	Lnet/minecraft/commands/arguments/item/ItemPredicateArgument$PredicateWrapper;
/*    */   }
/*    */   
/*    */   public final boolean equals(Object o) {
/*    */     // Byte code:
/*    */     //   0: aload_0
/*    */     //   1: aload_1
/*    */     //   2: <illegal opcode> equals : (Lnet/minecraft/commands/arguments/item/ItemPredicateArgument$PredicateWrapper;Ljava/lang/Object;)Z
/*    */     //   7: ireturn
/*    */     // Line number table:
/*    */     //   Java source line number -> byte code offset
/*    */     //   #74	-> 0
/*    */     // Local variable table:
/*    */     //   start	length	slot	name	descriptor
/*    */     //   0	8	0	this	Lnet/minecraft/commands/arguments/item/ItemPredicateArgument$PredicateWrapper;
/*    */     //   0	8	1	o	Ljava/lang/Object;
/*    */   }
/*    */   
/*    */   public Identifier id() {
/* 74 */     return this.id;
/*    */   }
/*    */   
/*    */   public Decoder<? extends Predicate<ItemStack>> type() {
/* 74 */     return this.type;
/*    */   }
/*    */   
/*    */   public PredicateWrapper(Holder.Reference<DataComponentPredicate.Type<?>> holder) {
/* 76 */     this(holder
/* 77 */         .key().identifier(), ((DataComponentPredicate.Type)holder
/* 78 */         .value()).codec().map(v -> {
/*    */             Objects.requireNonNull(v);
/*    */             return v::matches;
/*    */           }));
/*    */   }
/*    */   
/*    */   public Predicate<ItemStack> decode(ImmutableStringReader reader, Dynamic<?> value) throws CommandSyntaxException {
/* 83 */     DataResult<? extends Predicate<ItemStack>> result = this.type.parse(value);
/* 84 */     return (Predicate<ItemStack>)result.getOrThrow(message -> ItemPredicateArgument.ERROR_MALFORMED_PREDICATE.createWithContext(reader, this.id.toString(), message));
/*    */   }
/*    */ }


/* Location:              C:\Users\karol\AppData\Roaming\ModrinthApp\meta\versions\26.2-0.19.5\26.2-0.19.5.jar!\net\minecraft\commands\arguments\item\ItemPredicateArgument$PredicateWrapper.class
 * Java compiler version: 25 (69.0)
 * JD-Core Version:       1.1.3
 */