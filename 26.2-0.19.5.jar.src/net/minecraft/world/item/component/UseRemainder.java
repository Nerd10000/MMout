/*    */ package net.minecraft.world.item.component;
/*    */ 
/*    */ import com.mojang.serialization.Codec;
/*    */ import java.util.function.Function;
/*    */ import net.minecraft.network.RegistryFriendlyByteBuf;
/*    */ import net.minecraft.network.codec.StreamCodec;
/*    */ import net.minecraft.world.item.ItemStack;
/*    */ import net.minecraft.world.item.ItemStackTemplate;
/*    */ 
/*    */ public final class UseRemainder extends Record {
/*    */   private final ItemStackTemplate convertInto;
/*    */   
/*    */   public UseRemainder(ItemStackTemplate convertInto) {
/*  9 */     this.convertInto = convertInto;
/*    */   }
/*    */   
/*    */   public final String toString() {
/*    */     // Byte code:
/*    */     //   0: aload_0
/*    */     //   1: <illegal opcode> toString : (Lnet/minecraft/world/item/component/UseRemainder;)Ljava/lang/String;
/*    */     //   6: areturn
/*    */     // Line number table:
/*    */     //   Java source line number -> byte code offset
/*    */     //   #9	-> 0
/*    */     // Local variable table:
/*    */     //   start	length	slot	name	descriptor
/*    */     //   0	7	0	this	Lnet/minecraft/world/item/component/UseRemainder;
/*    */   }
/*    */   
/*    */   public final int hashCode() {
/*    */     // Byte code:
/*    */     //   0: aload_0
/*    */     //   1: <illegal opcode> hashCode : (Lnet/minecraft/world/item/component/UseRemainder;)I
/*    */     //   6: ireturn
/*    */     // Line number table:
/*    */     //   Java source line number -> byte code offset
/*    */     //   #9	-> 0
/*    */     // Local variable table:
/*    */     //   start	length	slot	name	descriptor
/*    */     //   0	7	0	this	Lnet/minecraft/world/item/component/UseRemainder;
/*    */   }
/*    */   
/*    */   public final boolean equals(Object o) {
/*    */     // Byte code:
/*    */     //   0: aload_0
/*    */     //   1: aload_1
/*    */     //   2: <illegal opcode> equals : (Lnet/minecraft/world/item/component/UseRemainder;Ljava/lang/Object;)Z
/*    */     //   7: ireturn
/*    */     // Line number table:
/*    */     //   Java source line number -> byte code offset
/*    */     //   #9	-> 0
/*    */     // Local variable table:
/*    */     //   start	length	slot	name	descriptor
/*    */     //   0	8	0	this	Lnet/minecraft/world/item/component/UseRemainder;
/*    */     //   0	8	1	o	Ljava/lang/Object;
/*    */   }
/*    */   
/*    */   public ItemStackTemplate convertInto() {
/*  9 */     return this.convertInto;
/*    */   }
/*    */   
/* 15 */   public static final Codec<UseRemainder> CODEC = ItemStackTemplate.CODEC.xmap(UseRemainder::new, UseRemainder::convertInto);
/*    */   
/* 17 */   public static final StreamCodec<RegistryFriendlyByteBuf, UseRemainder> STREAM_CODEC = StreamCodec.composite(ItemStackTemplate.STREAM_CODEC, UseRemainder::convertInto, UseRemainder::new);
/*    */   
/*    */   public ItemStack convertIntoRemainder(ItemStack usedStack, int stackCountBeforeUsing, boolean hasInfiniteMaterials, OnExtraCreatedRemainder onExtraCreatedRemainder) {
/* 24 */     if (hasInfiniteMaterials)
/* 25 */       return usedStack; 
/* 28 */     if (usedStack.getCount() >= stackCountBeforeUsing)
/* 29 */       return usedStack; 
/* 32 */     ItemStack remainderStack = this.convertInto.create();
/* 34 */     if (usedStack.isEmpty())
/* 35 */       return remainderStack; 
/* 38 */     onExtraCreatedRemainder.apply(remainderStack);
/* 40 */     return usedStack;
/*    */   }
/*    */   
/*    */   @FunctionalInterface
/*    */   public static interface OnExtraCreatedRemainder {
/*    */     void apply(ItemStack param1ItemStack);
/*    */   }
/*    */ }


/* Location:              C:\Users\karol\AppData\Roaming\ModrinthApp\meta\versions\26.2-0.19.5\26.2-0.19.5.jar!\net\minecraft\world\item\component\UseRemainder.class
 * Java compiler version: 25 (69.0)
 * JD-Core Version:       1.1.3
 */