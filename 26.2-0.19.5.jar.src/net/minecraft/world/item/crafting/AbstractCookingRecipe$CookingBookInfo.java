/*     */ package net.minecraft.world.item.crafting;
/*     */ 
/*     */ import com.mojang.serialization.MapCodec;
/*     */ import net.minecraft.network.RegistryFriendlyByteBuf;
/*     */ import net.minecraft.network.codec.StreamCodec;
/*     */ 
/*     */ public final class CookingBookInfo extends Record implements Recipe.BookInfo<CookingBookCategory> {
/*     */   private final CookingBookCategory category;
/*     */   
/*     */   private final String group;
/*     */   
/*     */   public CookingBookInfo(CookingBookCategory category, String group) {
/*  94 */     this.category = category;
/*  94 */     this.group = group;
/*     */   }
/*     */   
/*     */   public final String toString() {
/*     */     // Byte code:
/*     */     //   0: aload_0
/*     */     //   1: <illegal opcode> toString : (Lnet/minecraft/world/item/crafting/AbstractCookingRecipe$CookingBookInfo;)Ljava/lang/String;
/*     */     //   6: areturn
/*     */     // Line number table:
/*     */     //   Java source line number -> byte code offset
/*     */     //   #94	-> 0
/*     */     // Local variable table:
/*     */     //   start	length	slot	name	descriptor
/*     */     //   0	7	0	this	Lnet/minecraft/world/item/crafting/AbstractCookingRecipe$CookingBookInfo;
/*     */   }
/*     */   
/*     */   public final int hashCode() {
/*     */     // Byte code:
/*     */     //   0: aload_0
/*     */     //   1: <illegal opcode> hashCode : (Lnet/minecraft/world/item/crafting/AbstractCookingRecipe$CookingBookInfo;)I
/*     */     //   6: ireturn
/*     */     // Line number table:
/*     */     //   Java source line number -> byte code offset
/*     */     //   #94	-> 0
/*     */     // Local variable table:
/*     */     //   start	length	slot	name	descriptor
/*     */     //   0	7	0	this	Lnet/minecraft/world/item/crafting/AbstractCookingRecipe$CookingBookInfo;
/*     */   }
/*     */   
/*     */   public final boolean equals(Object o) {
/*     */     // Byte code:
/*     */     //   0: aload_0
/*     */     //   1: aload_1
/*     */     //   2: <illegal opcode> equals : (Lnet/minecraft/world/item/crafting/AbstractCookingRecipe$CookingBookInfo;Ljava/lang/Object;)Z
/*     */     //   7: ireturn
/*     */     // Line number table:
/*     */     //   Java source line number -> byte code offset
/*     */     //   #94	-> 0
/*     */     // Local variable table:
/*     */     //   start	length	slot	name	descriptor
/*     */     //   0	8	0	this	Lnet/minecraft/world/item/crafting/AbstractCookingRecipe$CookingBookInfo;
/*     */     //   0	8	1	o	Ljava/lang/Object;
/*     */   }
/*     */   
/*     */   public CookingBookCategory category() {
/*  94 */     return this.category;
/*     */   }
/*     */   
/*     */   public String group() {
/*  94 */     return this.group;
/*     */   }
/*     */   
/*  98 */   public static final MapCodec<CookingBookInfo> MAP_CODEC = Recipe.BookInfo.mapCodec(CookingBookCategory.CODEC, CookingBookCategory.MISC, CookingBookInfo::new);
/*     */   
/* 100 */   public static final StreamCodec<RegistryFriendlyByteBuf, CookingBookInfo> STREAM_CODEC = Recipe.BookInfo.streamCodec((StreamCodec)CookingBookCategory.STREAM_CODEC, CookingBookInfo::new);
/*     */ }


/* Location:              C:\Users\karol\AppData\Roaming\ModrinthApp\meta\versions\26.2-0.19.5\26.2-0.19.5.jar!\net\minecraft\world\item\crafting\AbstractCookingRecipe$CookingBookInfo.class
 * Java compiler version: 25 (69.0)
 * JD-Core Version:       1.1.3
 */