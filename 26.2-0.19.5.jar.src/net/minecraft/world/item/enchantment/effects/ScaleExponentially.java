/*    */ package net.minecraft.world.item.enchantment.effects;
/*    */ 
/*    */ import com.mojang.datafixers.kinds.App;
/*    */ import com.mojang.datafixers.kinds.Applicative;
/*    */ import com.mojang.serialization.MapCodec;
/*    */ import com.mojang.serialization.codecs.RecordCodecBuilder;
/*    */ import java.util.function.BiFunction;
/*    */ import net.minecraft.util.RandomSource;
/*    */ import net.minecraft.world.item.enchantment.LevelBasedValue;
/*    */ 
/*    */ public final class ScaleExponentially extends Record implements EnchantmentValueEffect {
/*    */   private final LevelBasedValue base;
/*    */   
/*    */   private final LevelBasedValue exponent;
/*    */   
/*    */   public static final MapCodec<ScaleExponentially> CODEC;
/*    */   
/*    */   public ScaleExponentially(LevelBasedValue base, LevelBasedValue exponent) {
/*  8 */     this.base = base;
/*  8 */     this.exponent = exponent;
/*    */   }
/*    */   
/*    */   public final String toString() {
/*    */     // Byte code:
/*    */     //   0: aload_0
/*    */     //   1: <illegal opcode> toString : (Lnet/minecraft/world/item/enchantment/effects/ScaleExponentially;)Ljava/lang/String;
/*    */     //   6: areturn
/*    */     // Line number table:
/*    */     //   Java source line number -> byte code offset
/*    */     //   #8	-> 0
/*    */     // Local variable table:
/*    */     //   start	length	slot	name	descriptor
/*    */     //   0	7	0	this	Lnet/minecraft/world/item/enchantment/effects/ScaleExponentially;
/*    */   }
/*    */   
/*    */   public final int hashCode() {
/*    */     // Byte code:
/*    */     //   0: aload_0
/*    */     //   1: <illegal opcode> hashCode : (Lnet/minecraft/world/item/enchantment/effects/ScaleExponentially;)I
/*    */     //   6: ireturn
/*    */     // Line number table:
/*    */     //   Java source line number -> byte code offset
/*    */     //   #8	-> 0
/*    */     // Local variable table:
/*    */     //   start	length	slot	name	descriptor
/*    */     //   0	7	0	this	Lnet/minecraft/world/item/enchantment/effects/ScaleExponentially;
/*    */   }
/*    */   
/*    */   public final boolean equals(Object o) {
/*    */     // Byte code:
/*    */     //   0: aload_0
/*    */     //   1: aload_1
/*    */     //   2: <illegal opcode> equals : (Lnet/minecraft/world/item/enchantment/effects/ScaleExponentially;Ljava/lang/Object;)Z
/*    */     //   7: ireturn
/*    */     // Line number table:
/*    */     //   Java source line number -> byte code offset
/*    */     //   #8	-> 0
/*    */     // Local variable table:
/*    */     //   start	length	slot	name	descriptor
/*    */     //   0	8	0	this	Lnet/minecraft/world/item/enchantment/effects/ScaleExponentially;
/*    */     //   0	8	1	o	Ljava/lang/Object;
/*    */   }
/*    */   
/*    */   public LevelBasedValue base() {
/*  8 */     return this.base;
/*    */   }
/*    */   
/*    */   public LevelBasedValue exponent() {
/*  8 */     return this.exponent;
/*    */   }
/*    */   
/*    */   static {
/*  9 */     CODEC = RecordCodecBuilder.mapCodec(i -> i.group((App)LevelBasedValue.CODEC.fieldOf("base").forGetter(ScaleExponentially::base), (App)LevelBasedValue.CODEC.fieldOf("exponent").forGetter(ScaleExponentially::exponent)).apply((Applicative)i, ScaleExponentially::new));
/*    */   }
/*    */   
/*    */   public float process(int level, RandomSource random, float inputValue) {
/* 16 */     return (float)(inputValue * Math.pow(this.base.calculate(level), this.exponent.calculate(level)));
/*    */   }
/*    */   
/*    */   public MapCodec<ScaleExponentially> codec() {
/* 21 */     return CODEC;
/*    */   }
/*    */ }


/* Location:              C:\Users\karol\AppData\Roaming\ModrinthApp\meta\versions\26.2-0.19.5\26.2-0.19.5.jar!\net\minecraft\world\item\enchantment\effects\ScaleExponentially.class
 * Java compiler version: 25 (69.0)
 * JD-Core Version:       1.1.3
 */