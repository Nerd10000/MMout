/*    */ package net.minecraft.client.color.item;
/*    */ 
/*    */ import com.mojang.datafixers.kinds.App;
/*    */ import com.mojang.datafixers.kinds.Applicative;
/*    */ import com.mojang.serialization.MapCodec;
/*    */ import com.mojang.serialization.codecs.RecordCodecBuilder;
/*    */ import java.util.function.Function;
/*    */ import net.minecraft.client.multiplayer.ClientLevel;
/*    */ import net.minecraft.core.component.DataComponents;
/*    */ import net.minecraft.util.ARGB;
/*    */ import net.minecraft.util.ExtraCodecs;
/*    */ import net.minecraft.world.entity.LivingEntity;
/*    */ import net.minecraft.world.item.ItemStack;
/*    */ import net.minecraft.world.item.alchemy.PotionContents;
/*    */ 
/*    */ public final class Potion extends Record implements ItemTintSource {
/*    */   private final int defaultColor;
/*    */   
/*    */   public static final MapCodec<Potion> MAP_CODEC;
/*    */   
/*    */   public Potion(int defaultColor) {
/* 14 */     this.defaultColor = defaultColor;
/*    */   }
/*    */   
/*    */   public final String toString() {
/*    */     // Byte code:
/*    */     //   0: aload_0
/*    */     //   1: <illegal opcode> toString : (Lnet/minecraft/client/color/item/Potion;)Ljava/lang/String;
/*    */     //   6: areturn
/*    */     // Line number table:
/*    */     //   Java source line number -> byte code offset
/*    */     //   #14	-> 0
/*    */     // Local variable table:
/*    */     //   start	length	slot	name	descriptor
/*    */     //   0	7	0	this	Lnet/minecraft/client/color/item/Potion;
/*    */   }
/*    */   
/*    */   public final int hashCode() {
/*    */     // Byte code:
/*    */     //   0: aload_0
/*    */     //   1: <illegal opcode> hashCode : (Lnet/minecraft/client/color/item/Potion;)I
/*    */     //   6: ireturn
/*    */     // Line number table:
/*    */     //   Java source line number -> byte code offset
/*    */     //   #14	-> 0
/*    */     // Local variable table:
/*    */     //   start	length	slot	name	descriptor
/*    */     //   0	7	0	this	Lnet/minecraft/client/color/item/Potion;
/*    */   }
/*    */   
/*    */   public final boolean equals(Object o) {
/*    */     // Byte code:
/*    */     //   0: aload_0
/*    */     //   1: aload_1
/*    */     //   2: <illegal opcode> equals : (Lnet/minecraft/client/color/item/Potion;Ljava/lang/Object;)Z
/*    */     //   7: ireturn
/*    */     // Line number table:
/*    */     //   Java source line number -> byte code offset
/*    */     //   #14	-> 0
/*    */     // Local variable table:
/*    */     //   start	length	slot	name	descriptor
/*    */     //   0	8	0	this	Lnet/minecraft/client/color/item/Potion;
/*    */     //   0	8	1	o	Ljava/lang/Object;
/*    */   }
/*    */   
/*    */   public int defaultColor() {
/* 14 */     return this.defaultColor;
/*    */   }
/*    */   
/*    */   static {
/* 17 */     MAP_CODEC = RecordCodecBuilder.mapCodec(i -> i.group((App)ExtraCodecs.RGB_COLOR_CODEC.fieldOf("default").forGetter(Potion::defaultColor)).apply((Applicative)i, Potion::new));
/*    */   }
/*    */   
/*    */   public Potion() {
/* 22 */     this(-13083194);
/*    */   }
/*    */   
/*    */   public int calculate(ItemStack itemStack, ClientLevel level, LivingEntity owner) {
/* 27 */     PotionContents contents = (PotionContents)itemStack.get(DataComponents.POTION_CONTENTS);
/* 28 */     if (contents != null)
/* 29 */       return ARGB.opaque(contents.getColorOr(this.defaultColor)); 
/* 32 */     return ARGB.opaque(this.defaultColor);
/*    */   }
/*    */   
/*    */   public MapCodec<Potion> type() {
/* 37 */     return MAP_CODEC;
/*    */   }
/*    */ }


/* Location:              C:\Users\karol\AppData\Roaming\ModrinthApp\meta\versions\26.2-0.19.5\26.2-0.19.5.jar!\net\minecraft\client\color\item\Potion.class
 * Java compiler version: 25 (69.0)
 * JD-Core Version:       1.1.3
 */