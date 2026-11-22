/*     */ package net.minecraft.world.item.enchantment;
/*     */ 
/*     */ import com.mojang.datafixers.kinds.App;
/*     */ import com.mojang.datafixers.kinds.Applicative;
/*     */ import com.mojang.serialization.Codec;
/*     */ import com.mojang.serialization.MapCodec;
/*     */ import com.mojang.serialization.codecs.RecordCodecBuilder;
/*     */ import java.util.function.BiFunction;
/*     */ 
/*     */ public final class Linear extends Record implements LevelBasedValue {
/*     */   private final float base;
/*     */   
/*     */   private final float perLevelAboveFirst;
/*     */   
/*     */   public static final MapCodec<Linear> CODEC;
/*     */   
/*     */   public Linear(float base, float perLevelAboveFirst) {
/*  87 */     this.base = base;
/*  87 */     this.perLevelAboveFirst = perLevelAboveFirst;
/*     */   }
/*     */   
/*     */   public final String toString() {
/*     */     // Byte code:
/*     */     //   0: aload_0
/*     */     //   1: <illegal opcode> toString : (Lnet/minecraft/world/item/enchantment/LevelBasedValue$Linear;)Ljava/lang/String;
/*     */     //   6: areturn
/*     */     // Line number table:
/*     */     //   Java source line number -> byte code offset
/*     */     //   #87	-> 0
/*     */     // Local variable table:
/*     */     //   start	length	slot	name	descriptor
/*     */     //   0	7	0	this	Lnet/minecraft/world/item/enchantment/LevelBasedValue$Linear;
/*     */   }
/*     */   
/*     */   public final int hashCode() {
/*     */     // Byte code:
/*     */     //   0: aload_0
/*     */     //   1: <illegal opcode> hashCode : (Lnet/minecraft/world/item/enchantment/LevelBasedValue$Linear;)I
/*     */     //   6: ireturn
/*     */     // Line number table:
/*     */     //   Java source line number -> byte code offset
/*     */     //   #87	-> 0
/*     */     // Local variable table:
/*     */     //   start	length	slot	name	descriptor
/*     */     //   0	7	0	this	Lnet/minecraft/world/item/enchantment/LevelBasedValue$Linear;
/*     */   }
/*     */   
/*     */   public final boolean equals(Object o) {
/*     */     // Byte code:
/*     */     //   0: aload_0
/*     */     //   1: aload_1
/*     */     //   2: <illegal opcode> equals : (Lnet/minecraft/world/item/enchantment/LevelBasedValue$Linear;Ljava/lang/Object;)Z
/*     */     //   7: ireturn
/*     */     // Line number table:
/*     */     //   Java source line number -> byte code offset
/*     */     //   #87	-> 0
/*     */     // Local variable table:
/*     */     //   start	length	slot	name	descriptor
/*     */     //   0	8	0	this	Lnet/minecraft/world/item/enchantment/LevelBasedValue$Linear;
/*     */     //   0	8	1	o	Ljava/lang/Object;
/*     */   }
/*     */   
/*     */   public float base() {
/*  87 */     return this.base;
/*     */   }
/*     */   
/*     */   public float perLevelAboveFirst() {
/*  87 */     return this.perLevelAboveFirst;
/*     */   }
/*     */   
/*     */   static {
/*  88 */     CODEC = RecordCodecBuilder.mapCodec(i -> i.group((App)Codec.FLOAT.fieldOf("base").forGetter(Linear::base), (App)Codec.FLOAT.fieldOf("per_level_above_first").forGetter(Linear::perLevelAboveFirst)).apply((Applicative)i, Linear::new));
/*     */   }
/*     */   
/*     */   public float calculate(int level) {
/*  95 */     return this.base + this.perLevelAboveFirst * (level - 1);
/*     */   }
/*     */   
/*     */   public MapCodec<Linear> codec() {
/* 100 */     return CODEC;
/*     */   }
/*     */ }


/* Location:              C:\Users\karol\AppData\Roaming\ModrinthApp\meta\versions\26.2-0.19.5\26.2-0.19.5.jar!\net\minecraft\world\item\enchantment\LevelBasedValue$Linear.class
 * Java compiler version: 25 (69.0)
 * JD-Core Version:       1.1.3
 */