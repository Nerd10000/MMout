/*    */ package net.minecraft.commands.arguments.item;
/*    */ 
/*    */ import com.mojang.brigadier.Message;
/*    */ import com.mojang.brigadier.exceptions.CommandSyntaxException;
/*    */ import com.mojang.brigadier.exceptions.Dynamic2CommandExceptionType;
/*    */ import com.mojang.brigadier.exceptions.DynamicCommandExceptionType;
/*    */ import com.mojang.serialization.DataResult;
/*    */ import java.util.Objects;
/*    */ import net.minecraft.core.Holder;
/*    */ import net.minecraft.core.component.DataComponentPatch;
/*    */ import net.minecraft.network.chat.Component;
/*    */ import net.minecraft.world.item.Item;
/*    */ import net.minecraft.world.item.ItemStack;
/*    */ 
/*    */ public final class ItemInput extends Record {
/*    */   private final Holder<Item> item;
/*    */   
/*    */   private final DataComponentPatch components;
/*    */   
/*    */   private static final Dynamic2CommandExceptionType ERROR_STACK_TOO_BIG;
/*    */   
/*    */   private static final DynamicCommandExceptionType ERROR_MALFORMED_ITEM;
/*    */   
/*    */   public ItemInput(Holder<Item> item, DataComponentPatch components) {
/* 13 */     this.item = item;
/* 13 */     this.components = components;
/*    */   }
/*    */   
/*    */   public final String toString() {
/*    */     // Byte code:
/*    */     //   0: aload_0
/*    */     //   1: <illegal opcode> toString : (Lnet/minecraft/commands/arguments/item/ItemInput;)Ljava/lang/String;
/*    */     //   6: areturn
/*    */     // Line number table:
/*    */     //   Java source line number -> byte code offset
/*    */     //   #13	-> 0
/*    */     // Local variable table:
/*    */     //   start	length	slot	name	descriptor
/*    */     //   0	7	0	this	Lnet/minecraft/commands/arguments/item/ItemInput;
/*    */   }
/*    */   
/*    */   public final int hashCode() {
/*    */     // Byte code:
/*    */     //   0: aload_0
/*    */     //   1: <illegal opcode> hashCode : (Lnet/minecraft/commands/arguments/item/ItemInput;)I
/*    */     //   6: ireturn
/*    */     // Line number table:
/*    */     //   Java source line number -> byte code offset
/*    */     //   #13	-> 0
/*    */     // Local variable table:
/*    */     //   start	length	slot	name	descriptor
/*    */     //   0	7	0	this	Lnet/minecraft/commands/arguments/item/ItemInput;
/*    */   }
/*    */   
/*    */   public final boolean equals(Object o) {
/*    */     // Byte code:
/*    */     //   0: aload_0
/*    */     //   1: aload_1
/*    */     //   2: <illegal opcode> equals : (Lnet/minecraft/commands/arguments/item/ItemInput;Ljava/lang/Object;)Z
/*    */     //   7: ireturn
/*    */     // Line number table:
/*    */     //   Java source line number -> byte code offset
/*    */     //   #13	-> 0
/*    */     // Local variable table:
/*    */     //   start	length	slot	name	descriptor
/*    */     //   0	8	0	this	Lnet/minecraft/commands/arguments/item/ItemInput;
/*    */     //   0	8	1	o	Ljava/lang/Object;
/*    */   }
/*    */   
/*    */   public Holder<Item> item() {
/* 13 */     return this.item;
/*    */   }
/*    */   
/*    */   public DataComponentPatch components() {
/* 13 */     return this.components;
/*    */   }
/*    */   
/*    */   static {
/* 14 */     ERROR_STACK_TOO_BIG = new Dynamic2CommandExceptionType((item, count) -> Component.translatableEscape("arguments.item.overstacked", new Object[] { item, count }));
/* 15 */     ERROR_MALFORMED_ITEM = new DynamicCommandExceptionType(error -> Component.translatableEscape("arguments.item.malformed", new Object[] { error }));
/*    */   }
/*    */   
/*    */   public ItemStack createItemStack(int count) throws CommandSyntaxException {
/* 18 */     ItemStack result = new ItemStack(this.item, count, this.components);
/* 19 */     if (count > result.getMaxStackSize())
/* 20 */       throw ERROR_STACK_TOO_BIG.create(this.item.getRegisteredName(), Integer.valueOf(result.getMaxStackSize())); 
/* 22 */     DataResult<ItemStack> validationResult = ItemStack.validateStrict(result);
/* 24 */     Objects.requireNonNull(ERROR_MALFORMED_ITEM);
/* 24 */     return (ItemStack)validationResult.getOrThrow(ERROR_MALFORMED_ITEM::create);
/*    */   }
/*    */ }


/* Location:              C:\Users\karol\AppData\Roaming\ModrinthApp\meta\versions\26.2-0.19.5\26.2-0.19.5.jar!\net\minecraft\commands\arguments\item\ItemInput.class
 * Java compiler version: 25 (69.0)
 * JD-Core Version:       1.1.3
 */