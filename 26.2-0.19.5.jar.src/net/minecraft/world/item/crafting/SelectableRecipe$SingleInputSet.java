/*    */ package net.minecraft.world.item.crafting;
/*    */ 
/*    */ import java.util.List;
/*    */ import java.util.function.Function;
/*    */ import net.minecraft.network.RegistryFriendlyByteBuf;
/*    */ import net.minecraft.network.codec.ByteBufCodecs;
/*    */ import net.minecraft.network.codec.StreamCodec;
/*    */ import net.minecraft.world.item.ItemStack;
/*    */ 
/*    */ public final class SingleInputSet<T extends Recipe<?>> extends Record {
/*    */   private final List<SelectableRecipe.SingleInputEntry<T>> entries;
/*    */   
/*    */   public SingleInputSet(List<SelectableRecipe.SingleInputEntry<T>> entries) {
/* 31 */     this.entries = entries;
/*    */   }
/*    */   
/*    */   public final String toString() {
/*    */     // Byte code:
/*    */     //   0: aload_0
/*    */     //   1: <illegal opcode> toString : (Lnet/minecraft/world/item/crafting/SelectableRecipe$SingleInputSet;)Ljava/lang/String;
/*    */     //   6: areturn
/*    */     // Line number table:
/*    */     //   Java source line number -> byte code offset
/*    */     //   #31	-> 0
/*    */     // Local variable table:
/*    */     //   start	length	slot	name	descriptor
/*    */     //   0	7	0	this	Lnet/minecraft/world/item/crafting/SelectableRecipe$SingleInputSet;
/*    */     // Local variable type table:
/*    */     //   start	length	slot	name	signature
/*    */     //   0	7	0	this	Lnet/minecraft/world/item/crafting/SelectableRecipe$SingleInputSet<TT;>;
/*    */   }
/*    */   
/*    */   public final int hashCode() {
/*    */     // Byte code:
/*    */     //   0: aload_0
/*    */     //   1: <illegal opcode> hashCode : (Lnet/minecraft/world/item/crafting/SelectableRecipe$SingleInputSet;)I
/*    */     //   6: ireturn
/*    */     // Line number table:
/*    */     //   Java source line number -> byte code offset
/*    */     //   #31	-> 0
/*    */     // Local variable table:
/*    */     //   start	length	slot	name	descriptor
/*    */     //   0	7	0	this	Lnet/minecraft/world/item/crafting/SelectableRecipe$SingleInputSet;
/*    */     // Local variable type table:
/*    */     //   start	length	slot	name	signature
/*    */     //   0	7	0	this	Lnet/minecraft/world/item/crafting/SelectableRecipe$SingleInputSet<TT;>;
/*    */   }
/*    */   
/*    */   public final boolean equals(Object o) {
/*    */     // Byte code:
/*    */     //   0: aload_0
/*    */     //   1: aload_1
/*    */     //   2: <illegal opcode> equals : (Lnet/minecraft/world/item/crafting/SelectableRecipe$SingleInputSet;Ljava/lang/Object;)Z
/*    */     //   7: ireturn
/*    */     // Line number table:
/*    */     //   Java source line number -> byte code offset
/*    */     //   #31	-> 0
/*    */     // Local variable table:
/*    */     //   start	length	slot	name	descriptor
/*    */     //   0	8	0	this	Lnet/minecraft/world/item/crafting/SelectableRecipe$SingleInputSet;
/*    */     //   0	8	1	o	Ljava/lang/Object;
/*    */     // Local variable type table:
/*    */     //   start	length	slot	name	signature
/*    */     //   0	8	0	this	Lnet/minecraft/world/item/crafting/SelectableRecipe$SingleInputSet<TT;>;
/*    */   }
/*    */   
/*    */   public List<SelectableRecipe.SingleInputEntry<T>> entries() {
/* 31 */     return this.entries;
/*    */   }
/*    */   
/*    */   public static <T extends Recipe<?>> SingleInputSet<T> empty() {
/* 33 */     return new SingleInputSet<>(List.of());
/*    */   }
/*    */   
/*    */   public static <T extends Recipe<?>> StreamCodec<RegistryFriendlyByteBuf, SingleInputSet<T>> noRecipeCodec() {
/* 37 */     return StreamCodec.composite(
/* 38 */         SelectableRecipe.SingleInputEntry.<T>noRecipeCodec().apply(ByteBufCodecs.list()), SingleInputSet::entries, SingleInputSet::new);
/*    */   }
/*    */   
/*    */   public boolean acceptsInput(ItemStack input) {
/* 44 */     return this.entries.stream().anyMatch(e -> e.input.test(input));
/*    */   }
/*    */   
/*    */   public SingleInputSet<T> selectByInput(ItemStack input) {
/* 48 */     return new SingleInputSet(this.entries.stream().filter(e -> e.input.test(input)).toList());
/*    */   }
/*    */   
/*    */   public boolean isEmpty() {
/* 52 */     return this.entries.isEmpty();
/*    */   }
/*    */   
/*    */   public int size() {
/* 56 */     return this.entries.size();
/*    */   }
/*    */ }


/* Location:              C:\Users\karol\AppData\Roaming\ModrinthApp\meta\versions\26.2-0.19.5\26.2-0.19.5.jar!\net\minecraft\world\item\crafting\SelectableRecipe$SingleInputSet.class
 * Java compiler version: 25 (69.0)
 * JD-Core Version:       1.1.3
 */