/*    */ package net.minecraft.client.color.item;
/*    */ 
/*    */ import com.mojang.datafixers.kinds.App;
/*    */ import com.mojang.datafixers.kinds.Applicative;
/*    */ import com.mojang.serialization.MapCodec;
/*    */ import com.mojang.serialization.codecs.RecordCodecBuilder;
/*    */ import java.util.function.Function;
/*    */ import net.minecraft.client.multiplayer.ClientLevel;
/*    */ import net.minecraft.util.ExtraCodecs;
/*    */ import net.minecraft.world.entity.LivingEntity;
/*    */ import net.minecraft.world.item.ItemStack;
/*    */ import net.minecraft.world.item.component.DyedItemColor;
/*    */ 
/*    */ public final class Dye extends Record implements ItemTintSource {
/*    */   private final int defaultColor;
/*    */   
/*    */   public static final MapCodec<Dye> MAP_CODEC;
/*    */   
/*    */   public Dye(int defaultColor) {
/* 12 */     this.defaultColor = defaultColor;
/*    */   }
/*    */   
/*    */   public final String toString() {
/*    */     // Byte code:
/*    */     //   0: aload_0
/*    */     //   1: <illegal opcode> toString : (Lnet/minecraft/client/color/item/Dye;)Ljava/lang/String;
/*    */     //   6: areturn
/*    */     // Line number table:
/*    */     //   Java source line number -> byte code offset
/*    */     //   #12	-> 0
/*    */     // Local variable table:
/*    */     //   start	length	slot	name	descriptor
/*    */     //   0	7	0	this	Lnet/minecraft/client/color/item/Dye;
/*    */   }
/*    */   
/*    */   public final int hashCode() {
/*    */     // Byte code:
/*    */     //   0: aload_0
/*    */     //   1: <illegal opcode> hashCode : (Lnet/minecraft/client/color/item/Dye;)I
/*    */     //   6: ireturn
/*    */     // Line number table:
/*    */     //   Java source line number -> byte code offset
/*    */     //   #12	-> 0
/*    */     // Local variable table:
/*    */     //   start	length	slot	name	descriptor
/*    */     //   0	7	0	this	Lnet/minecraft/client/color/item/Dye;
/*    */   }
/*    */   
/*    */   public final boolean equals(Object o) {
/*    */     // Byte code:
/*    */     //   0: aload_0
/*    */     //   1: aload_1
/*    */     //   2: <illegal opcode> equals : (Lnet/minecraft/client/color/item/Dye;Ljava/lang/Object;)Z
/*    */     //   7: ireturn
/*    */     // Line number table:
/*    */     //   Java source line number -> byte code offset
/*    */     //   #12	-> 0
/*    */     // Local variable table:
/*    */     //   start	length	slot	name	descriptor
/*    */     //   0	8	0	this	Lnet/minecraft/client/color/item/Dye;
/*    */     //   0	8	1	o	Ljava/lang/Object;
/*    */   }
/*    */   
/*    */   public int defaultColor() {
/* 12 */     return this.defaultColor;
/*    */   }
/*    */   
/*    */   static {
/* 13 */     MAP_CODEC = RecordCodecBuilder.mapCodec(i -> i.group((App)ExtraCodecs.RGB_COLOR_CODEC.fieldOf("default").forGetter(Dye::defaultColor)).apply((Applicative)i, Dye::new));
/*    */   }
/*    */   
/*    */   public int calculate(ItemStack itemStack, ClientLevel level, LivingEntity owner) {
/* 19 */     return DyedItemColor.getOrDefault(itemStack, this.defaultColor);
/*    */   }
/*    */   
/*    */   public MapCodec<Dye> type() {
/* 24 */     return MAP_CODEC;
/*    */   }
/*    */ }


/* Location:              C:\Users\karol\AppData\Roaming\ModrinthApp\meta\versions\26.2-0.19.5\26.2-0.19.5.jar!\net\minecraft\client\color\item\Dye.class
 * Java compiler version: 25 (69.0)
 * JD-Core Version:       1.1.3
 */