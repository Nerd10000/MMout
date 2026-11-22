/*    */ package net.minecraft.world;
/*    */ 
/*    */ import com.mojang.datafixers.kinds.App;
/*    */ import com.mojang.datafixers.kinds.Applicative;
/*    */ import com.mojang.serialization.Codec;
/*    */ import com.mojang.serialization.codecs.RecordCodecBuilder;
/*    */ import java.util.function.BiFunction;
/*    */ import net.minecraft.util.ExtraCodecs;
/*    */ import net.minecraft.world.item.ItemStack;
/*    */ 
/*    */ public final class ItemStackWithSlot extends Record {
/*    */   private final int slot;
/*    */   
/*    */   private final ItemStack stack;
/*    */   
/*    */   public static final Codec<ItemStackWithSlot> CODEC;
/*    */   
/*    */   public ItemStackWithSlot(int slot, ItemStack stack) {
/*  8 */     this.slot = slot;
/*  8 */     this.stack = stack;
/*    */   }
/*    */   
/*    */   public final String toString() {
/*    */     // Byte code:
/*    */     //   0: aload_0
/*    */     //   1: <illegal opcode> toString : (Lnet/minecraft/world/ItemStackWithSlot;)Ljava/lang/String;
/*    */     //   6: areturn
/*    */     // Line number table:
/*    */     //   Java source line number -> byte code offset
/*    */     //   #8	-> 0
/*    */     // Local variable table:
/*    */     //   start	length	slot	name	descriptor
/*    */     //   0	7	0	this	Lnet/minecraft/world/ItemStackWithSlot;
/*    */   }
/*    */   
/*    */   public final int hashCode() {
/*    */     // Byte code:
/*    */     //   0: aload_0
/*    */     //   1: <illegal opcode> hashCode : (Lnet/minecraft/world/ItemStackWithSlot;)I
/*    */     //   6: ireturn
/*    */     // Line number table:
/*    */     //   Java source line number -> byte code offset
/*    */     //   #8	-> 0
/*    */     // Local variable table:
/*    */     //   start	length	slot	name	descriptor
/*    */     //   0	7	0	this	Lnet/minecraft/world/ItemStackWithSlot;
/*    */   }
/*    */   
/*    */   public final boolean equals(Object o) {
/*    */     // Byte code:
/*    */     //   0: aload_0
/*    */     //   1: aload_1
/*    */     //   2: <illegal opcode> equals : (Lnet/minecraft/world/ItemStackWithSlot;Ljava/lang/Object;)Z
/*    */     //   7: ireturn
/*    */     // Line number table:
/*    */     //   Java source line number -> byte code offset
/*    */     //   #8	-> 0
/*    */     // Local variable table:
/*    */     //   start	length	slot	name	descriptor
/*    */     //   0	8	0	this	Lnet/minecraft/world/ItemStackWithSlot;
/*    */     //   0	8	1	o	Ljava/lang/Object;
/*    */   }
/*    */   
/*    */   public int slot() {
/*  8 */     return this.slot;
/*    */   }
/*    */   
/*    */   public ItemStack stack() {
/*  8 */     return this.stack;
/*    */   }
/*    */   
/*    */   static {
/* 12 */     CODEC = RecordCodecBuilder.create(i -> i.group((App)ExtraCodecs.optionalAlwaysPresentFieldOf(ExtraCodecs.UNSIGNED_BYTE, "Slot", Integer.valueOf(0)).forGetter(ItemStackWithSlot::slot), (App)ItemStack.MAP_CODEC.forGetter(ItemStackWithSlot::stack)).apply((Applicative)i, ItemStackWithSlot::new));
/*    */   }
/*    */   
/*    */   public boolean isValidInContainer(int containerSize) {
/* 19 */     return (this.slot >= 0 && this.slot < containerSize);
/*    */   }
/*    */ }


/* Location:              C:\Users\karol\AppData\Roaming\ModrinthApp\meta\versions\26.2-0.19.5\26.2-0.19.5.jar!\net\minecraft\world\ItemStackWithSlot.class
 * Java compiler version: 25 (69.0)
 * JD-Core Version:       1.1.3
 */