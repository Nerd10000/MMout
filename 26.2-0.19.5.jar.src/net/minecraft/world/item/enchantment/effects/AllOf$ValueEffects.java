/*    */ package net.minecraft.world.item.enchantment.effects;
/*    */ 
/*    */ import com.mojang.serialization.MapCodec;
/*    */ import java.util.List;
/*    */ import java.util.function.Function;
/*    */ import net.minecraft.util.RandomSource;
/*    */ 
/*    */ public final class ValueEffects extends Record implements EnchantmentValueEffect {
/*    */   private final List<EnchantmentValueEffect> effects;
/*    */   
/*    */   public ValueEffects(List<EnchantmentValueEffect> effects) {
/* 75 */     this.effects = effects;
/*    */   }
/*    */   
/*    */   public final String toString() {
/*    */     // Byte code:
/*    */     //   0: aload_0
/*    */     //   1: <illegal opcode> toString : (Lnet/minecraft/world/item/enchantment/effects/AllOf$ValueEffects;)Ljava/lang/String;
/*    */     //   6: areturn
/*    */     // Line number table:
/*    */     //   Java source line number -> byte code offset
/*    */     //   #75	-> 0
/*    */     // Local variable table:
/*    */     //   start	length	slot	name	descriptor
/*    */     //   0	7	0	this	Lnet/minecraft/world/item/enchantment/effects/AllOf$ValueEffects;
/*    */   }
/*    */   
/*    */   public final int hashCode() {
/*    */     // Byte code:
/*    */     //   0: aload_0
/*    */     //   1: <illegal opcode> hashCode : (Lnet/minecraft/world/item/enchantment/effects/AllOf$ValueEffects;)I
/*    */     //   6: ireturn
/*    */     // Line number table:
/*    */     //   Java source line number -> byte code offset
/*    */     //   #75	-> 0
/*    */     // Local variable table:
/*    */     //   start	length	slot	name	descriptor
/*    */     //   0	7	0	this	Lnet/minecraft/world/item/enchantment/effects/AllOf$ValueEffects;
/*    */   }
/*    */   
/*    */   public final boolean equals(Object o) {
/*    */     // Byte code:
/*    */     //   0: aload_0
/*    */     //   1: aload_1
/*    */     //   2: <illegal opcode> equals : (Lnet/minecraft/world/item/enchantment/effects/AllOf$ValueEffects;Ljava/lang/Object;)Z
/*    */     //   7: ireturn
/*    */     // Line number table:
/*    */     //   Java source line number -> byte code offset
/*    */     //   #75	-> 0
/*    */     // Local variable table:
/*    */     //   start	length	slot	name	descriptor
/*    */     //   0	8	0	this	Lnet/minecraft/world/item/enchantment/effects/AllOf$ValueEffects;
/*    */     //   0	8	1	o	Ljava/lang/Object;
/*    */   }
/*    */   
/*    */   public List<EnchantmentValueEffect> effects() {
/* 75 */     return this.effects;
/*    */   }
/*    */   
/* 76 */   public static final MapCodec<ValueEffects> CODEC = AllOf.codec(EnchantmentValueEffect.CODEC, ValueEffects::new, ValueEffects::effects);
/*    */   
/*    */   public float process(int enchantmentLevel, RandomSource random, float value) {
/* 80 */     for (EnchantmentValueEffect effect : this.effects)
/* 81 */       value = effect.process(enchantmentLevel, random, value); 
/* 83 */     return value;
/*    */   }
/*    */   
/*    */   public MapCodec<ValueEffects> codec() {
/* 88 */     return CODEC;
/*    */   }
/*    */ }


/* Location:              C:\Users\karol\AppData\Roaming\ModrinthApp\meta\versions\26.2-0.19.5\26.2-0.19.5.jar!\net\minecraft\world\item\enchantment\effects\AllOf$ValueEffects.class
 * Java compiler version: 25 (69.0)
 * JD-Core Version:       1.1.3
 */