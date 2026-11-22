/*     */ package net.minecraft.world.item.enchantment;
/*     */ 
/*     */ import com.mojang.datafixers.kinds.App;
/*     */ import com.mojang.datafixers.kinds.Applicative;
/*     */ import com.mojang.serialization.MapCodec;
/*     */ import com.mojang.serialization.codecs.RecordCodecBuilder;
/*     */ import java.util.function.BiFunction;
/*     */ 
/*     */ public final class Exponent extends Record implements LevelBasedValue {
/*     */   private final LevelBasedValue base;
/*     */   
/*     */   private final LevelBasedValue power;
/*     */   
/*     */   public static final MapCodec<Exponent> CODEC;
/*     */   
/*     */   public Exponent(LevelBasedValue base, LevelBasedValue power) {
/* 148 */     this.base = base;
/* 148 */     this.power = power;
/*     */   }
/*     */   
/*     */   public final String toString() {
/*     */     // Byte code:
/*     */     //   0: aload_0
/*     */     //   1: <illegal opcode> toString : (Lnet/minecraft/world/item/enchantment/LevelBasedValue$Exponent;)Ljava/lang/String;
/*     */     //   6: areturn
/*     */     // Line number table:
/*     */     //   Java source line number -> byte code offset
/*     */     //   #148	-> 0
/*     */     // Local variable table:
/*     */     //   start	length	slot	name	descriptor
/*     */     //   0	7	0	this	Lnet/minecraft/world/item/enchantment/LevelBasedValue$Exponent;
/*     */   }
/*     */   
/*     */   public final int hashCode() {
/*     */     // Byte code:
/*     */     //   0: aload_0
/*     */     //   1: <illegal opcode> hashCode : (Lnet/minecraft/world/item/enchantment/LevelBasedValue$Exponent;)I
/*     */     //   6: ireturn
/*     */     // Line number table:
/*     */     //   Java source line number -> byte code offset
/*     */     //   #148	-> 0
/*     */     // Local variable table:
/*     */     //   start	length	slot	name	descriptor
/*     */     //   0	7	0	this	Lnet/minecraft/world/item/enchantment/LevelBasedValue$Exponent;
/*     */   }
/*     */   
/*     */   public final boolean equals(Object o) {
/*     */     // Byte code:
/*     */     //   0: aload_0
/*     */     //   1: aload_1
/*     */     //   2: <illegal opcode> equals : (Lnet/minecraft/world/item/enchantment/LevelBasedValue$Exponent;Ljava/lang/Object;)Z
/*     */     //   7: ireturn
/*     */     // Line number table:
/*     */     //   Java source line number -> byte code offset
/*     */     //   #148	-> 0
/*     */     // Local variable table:
/*     */     //   start	length	slot	name	descriptor
/*     */     //   0	8	0	this	Lnet/minecraft/world/item/enchantment/LevelBasedValue$Exponent;
/*     */     //   0	8	1	o	Ljava/lang/Object;
/*     */   }
/*     */   
/*     */   public LevelBasedValue base() {
/* 148 */     return this.base;
/*     */   }
/*     */   
/*     */   public LevelBasedValue power() {
/* 148 */     return this.power;
/*     */   }
/*     */   
/*     */   static {
/* 149 */     CODEC = RecordCodecBuilder.mapCodec(i -> i.group((App)LevelBasedValue.CODEC.fieldOf("base").forGetter(Exponent::base), (App)LevelBasedValue.CODEC.fieldOf("power").forGetter(Exponent::power)).apply((Applicative)i, Exponent::new));
/*     */   }
/*     */   
/*     */   public float calculate(int level) {
/* 156 */     return (float)Math.pow(this.base.calculate(level), this.power.calculate(level));
/*     */   }
/*     */   
/*     */   public MapCodec<Exponent> codec() {
/* 161 */     return CODEC;
/*     */   }
/*     */ }


/* Location:              C:\Users\karol\AppData\Roaming\ModrinthApp\meta\versions\26.2-0.19.5\26.2-0.19.5.jar!\net\minecraft\world\item\enchantment\LevelBasedValue$Exponent.class
 * Java compiler version: 25 (69.0)
 * JD-Core Version:       1.1.3
 */