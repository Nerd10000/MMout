/*   */ package net.minecraft.world.item.crafting;
/*   */ 
/*   */ import com.mojang.serialization.MapCodec;
/*   */ import net.minecraft.network.RegistryFriendlyByteBuf;
/*   */ import net.minecraft.network.codec.StreamCodec;
/*   */ 
/*   */ public final class RecipeSerializer<T extends Recipe<?>> extends Record {
/*   */   private final MapCodec<T> codec;
/*   */   
/*   */   @Deprecated
/*   */   private final StreamCodec<RegistryFriendlyByteBuf, T> streamCodec;
/*   */   
/*   */   public RecipeSerializer(MapCodec<T> codec, @Deprecated StreamCodec<RegistryFriendlyByteBuf, T> streamCodec) {
/* 7 */     this.codec = codec;
/* 7 */     this.streamCodec = streamCodec;
/*   */   }
/*   */   
/*   */   public final String toString() {
/*   */     // Byte code:
/*   */     //   0: aload_0
/*   */     //   1: <illegal opcode> toString : (Lnet/minecraft/world/item/crafting/RecipeSerializer;)Ljava/lang/String;
/*   */     //   6: areturn
/*   */     // Line number table:
/*   */     //   Java source line number -> byte code offset
/*   */     //   #7	-> 0
/*   */     // Local variable table:
/*   */     //   start	length	slot	name	descriptor
/*   */     //   0	7	0	this	Lnet/minecraft/world/item/crafting/RecipeSerializer;
/*   */     // Local variable type table:
/*   */     //   start	length	slot	name	signature
/*   */     //   0	7	0	this	Lnet/minecraft/world/item/crafting/RecipeSerializer<TT;>;
/*   */   }
/*   */   
/*   */   public final int hashCode() {
/*   */     // Byte code:
/*   */     //   0: aload_0
/*   */     //   1: <illegal opcode> hashCode : (Lnet/minecraft/world/item/crafting/RecipeSerializer;)I
/*   */     //   6: ireturn
/*   */     // Line number table:
/*   */     //   Java source line number -> byte code offset
/*   */     //   #7	-> 0
/*   */     // Local variable table:
/*   */     //   start	length	slot	name	descriptor
/*   */     //   0	7	0	this	Lnet/minecraft/world/item/crafting/RecipeSerializer;
/*   */     // Local variable type table:
/*   */     //   start	length	slot	name	signature
/*   */     //   0	7	0	this	Lnet/minecraft/world/item/crafting/RecipeSerializer<TT;>;
/*   */   }
/*   */   
/*   */   public final boolean equals(Object o) {
/*   */     // Byte code:
/*   */     //   0: aload_0
/*   */     //   1: aload_1
/*   */     //   2: <illegal opcode> equals : (Lnet/minecraft/world/item/crafting/RecipeSerializer;Ljava/lang/Object;)Z
/*   */     //   7: ireturn
/*   */     // Line number table:
/*   */     //   Java source line number -> byte code offset
/*   */     //   #7	-> 0
/*   */     // Local variable table:
/*   */     //   start	length	slot	name	descriptor
/*   */     //   0	8	0	this	Lnet/minecraft/world/item/crafting/RecipeSerializer;
/*   */     //   0	8	1	o	Ljava/lang/Object;
/*   */     // Local variable type table:
/*   */     //   start	length	slot	name	signature
/*   */     //   0	8	0	this	Lnet/minecraft/world/item/crafting/RecipeSerializer<TT;>;
/*   */   }
/*   */   
/*   */   public MapCodec<T> codec() {
/* 7 */     return this.codec;
/*   */   }
/*   */   
/*   */   @Deprecated
/*   */   public StreamCodec<RegistryFriendlyByteBuf, T> streamCodec() {
/* 7 */     return this.streamCodec;
/*   */   }
/*   */ }


/* Location:              C:\Users\karol\AppData\Roaming\ModrinthApp\meta\versions\26.2-0.19.5\26.2-0.19.5.jar!\net\minecraft\world\item\crafting\RecipeSerializer.class
 * Java compiler version: 25 (69.0)
 * JD-Core Version:       1.1.3
 */