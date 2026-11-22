/*     */ package net.minecraft.world.item.enchantment;
/*     */ 
/*     */ import com.mojang.datafixers.kinds.App;
/*     */ import com.mojang.datafixers.kinds.Applicative;
/*     */ import com.mojang.serialization.MapCodec;
/*     */ import com.mojang.serialization.codecs.RecordCodecBuilder;
/*     */ import java.util.function.BiFunction;
/*     */ 
/*     */ public final class Fraction extends Record implements LevelBasedValue {
/*     */   private final LevelBasedValue numerator;
/*     */   
/*     */   private final LevelBasedValue denominator;
/*     */   
/*     */   public static final MapCodec<Fraction> CODEC;
/*     */   
/*     */   public Fraction(LevelBasedValue numerator, LevelBasedValue denominator) {
/* 127 */     this.numerator = numerator;
/* 127 */     this.denominator = denominator;
/*     */   }
/*     */   
/*     */   public final String toString() {
/*     */     // Byte code:
/*     */     //   0: aload_0
/*     */     //   1: <illegal opcode> toString : (Lnet/minecraft/world/item/enchantment/LevelBasedValue$Fraction;)Ljava/lang/String;
/*     */     //   6: areturn
/*     */     // Line number table:
/*     */     //   Java source line number -> byte code offset
/*     */     //   #127	-> 0
/*     */     // Local variable table:
/*     */     //   start	length	slot	name	descriptor
/*     */     //   0	7	0	this	Lnet/minecraft/world/item/enchantment/LevelBasedValue$Fraction;
/*     */   }
/*     */   
/*     */   public final int hashCode() {
/*     */     // Byte code:
/*     */     //   0: aload_0
/*     */     //   1: <illegal opcode> hashCode : (Lnet/minecraft/world/item/enchantment/LevelBasedValue$Fraction;)I
/*     */     //   6: ireturn
/*     */     // Line number table:
/*     */     //   Java source line number -> byte code offset
/*     */     //   #127	-> 0
/*     */     // Local variable table:
/*     */     //   start	length	slot	name	descriptor
/*     */     //   0	7	0	this	Lnet/minecraft/world/item/enchantment/LevelBasedValue$Fraction;
/*     */   }
/*     */   
/*     */   public final boolean equals(Object o) {
/*     */     // Byte code:
/*     */     //   0: aload_0
/*     */     //   1: aload_1
/*     */     //   2: <illegal opcode> equals : (Lnet/minecraft/world/item/enchantment/LevelBasedValue$Fraction;Ljava/lang/Object;)Z
/*     */     //   7: ireturn
/*     */     // Line number table:
/*     */     //   Java source line number -> byte code offset
/*     */     //   #127	-> 0
/*     */     // Local variable table:
/*     */     //   start	length	slot	name	descriptor
/*     */     //   0	8	0	this	Lnet/minecraft/world/item/enchantment/LevelBasedValue$Fraction;
/*     */     //   0	8	1	o	Ljava/lang/Object;
/*     */   }
/*     */   
/*     */   public LevelBasedValue numerator() {
/* 127 */     return this.numerator;
/*     */   }
/*     */   
/*     */   public LevelBasedValue denominator() {
/* 127 */     return this.denominator;
/*     */   }
/*     */   
/*     */   static {
/* 128 */     CODEC = RecordCodecBuilder.mapCodec(i -> i.group((App)LevelBasedValue.CODEC.fieldOf("numerator").forGetter(Fraction::numerator), (App)LevelBasedValue.CODEC.fieldOf("denominator").forGetter(Fraction::denominator)).apply((Applicative)i, Fraction::new));
/*     */   }
/*     */   
/*     */   public float calculate(int level) {
/* 135 */     float denominator = this.denominator.calculate(level);
/* 136 */     if (denominator == 0.0F)
/* 137 */       return 0.0F; 
/* 139 */     return this.numerator.calculate(level) / denominator;
/*     */   }
/*     */   
/*     */   public MapCodec<Fraction> codec() {
/* 144 */     return CODEC;
/*     */   }
/*     */ }


/* Location:              C:\Users\karol\AppData\Roaming\ModrinthApp\meta\versions\26.2-0.19.5\26.2-0.19.5.jar!\net\minecraft\world\item\enchantment\LevelBasedValue$Fraction.class
 * Java compiler version: 25 (69.0)
 * JD-Core Version:       1.1.3
 */