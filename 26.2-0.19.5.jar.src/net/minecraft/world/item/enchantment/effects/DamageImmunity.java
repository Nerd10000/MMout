/*   */ package net.minecraft.world.item.enchantment.effects;
/*   */ 
/*   */ import com.mojang.serialization.Codec;
/*   */ import com.mojang.serialization.MapCodec;
/*   */ 
/*   */ public final class DamageImmunity extends Record {
/*   */   public final String toString() {
/*   */     // Byte code:
/*   */     //   0: aload_0
/*   */     //   1: <illegal opcode> toString : (Lnet/minecraft/world/item/enchantment/effects/DamageImmunity;)Ljava/lang/String;
/*   */     //   6: areturn
/*   */     // Line number table:
/*   */     //   Java source line number -> byte code offset
/*   */     //   #6	-> 0
/*   */     // Local variable table:
/*   */     //   start	length	slot	name	descriptor
/*   */     //   0	7	0	this	Lnet/minecraft/world/item/enchantment/effects/DamageImmunity;
/*   */   }
/*   */   
/*   */   public final int hashCode() {
/*   */     // Byte code:
/*   */     //   0: aload_0
/*   */     //   1: <illegal opcode> hashCode : (Lnet/minecraft/world/item/enchantment/effects/DamageImmunity;)I
/*   */     //   6: ireturn
/*   */     // Line number table:
/*   */     //   Java source line number -> byte code offset
/*   */     //   #6	-> 0
/*   */     // Local variable table:
/*   */     //   start	length	slot	name	descriptor
/*   */     //   0	7	0	this	Lnet/minecraft/world/item/enchantment/effects/DamageImmunity;
/*   */   }
/*   */   
/*   */   public final boolean equals(Object o) {
/*   */     // Byte code:
/*   */     //   0: aload_0
/*   */     //   1: aload_1
/*   */     //   2: <illegal opcode> equals : (Lnet/minecraft/world/item/enchantment/effects/DamageImmunity;Ljava/lang/Object;)Z
/*   */     //   7: ireturn
/*   */     // Line number table:
/*   */     //   Java source line number -> byte code offset
/*   */     //   #6	-> 0
/*   */     // Local variable table:
/*   */     //   start	length	slot	name	descriptor
/*   */     //   0	8	0	this	Lnet/minecraft/world/item/enchantment/effects/DamageImmunity;
/*   */     //   0	8	1	o	Ljava/lang/Object;
/*   */   }
/*   */   
/* 7 */   public static final DamageImmunity INSTANCE = new DamageImmunity();
/*   */   
/* 8 */   public static final Codec<DamageImmunity> CODEC = MapCodec.unitCodec(INSTANCE);
/*   */ }


/* Location:              C:\Users\karol\AppData\Roaming\ModrinthApp\meta\versions\26.2-0.19.5\26.2-0.19.5.jar!\net\minecraft\world\item\enchantment\effects\DamageImmunity.class
 * Java compiler version: 25 (69.0)
 * JD-Core Version:       1.1.3
 */