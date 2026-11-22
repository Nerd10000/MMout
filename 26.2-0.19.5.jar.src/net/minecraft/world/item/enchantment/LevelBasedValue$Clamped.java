/*     */ package net.minecraft.world.item.enchantment;
/*     */ 
/*     */ import com.mojang.datafixers.kinds.App;
/*     */ import com.mojang.datafixers.kinds.Applicative;
/*     */ import com.mojang.datafixers.util.Function3;
/*     */ import com.mojang.serialization.Codec;
/*     */ import com.mojang.serialization.DataResult;
/*     */ import com.mojang.serialization.MapCodec;
/*     */ import com.mojang.serialization.codecs.RecordCodecBuilder;
/*     */ import net.minecraft.util.Mth;
/*     */ 
/*     */ public final class Clamped extends Record implements LevelBasedValue {
/*     */   private final LevelBasedValue value;
/*     */   
/*     */   private final float min;
/*     */   
/*     */   private final float max;
/*     */   
/*     */   public static final MapCodec<Clamped> CODEC;
/*     */   
/*     */   public Clamped(LevelBasedValue value, float min, float max) {
/* 104 */     this.value = value;
/* 104 */     this.min = min;
/* 104 */     this.max = max;
/*     */   }
/*     */   
/*     */   public final String toString() {
/*     */     // Byte code:
/*     */     //   0: aload_0
/*     */     //   1: <illegal opcode> toString : (Lnet/minecraft/world/item/enchantment/LevelBasedValue$Clamped;)Ljava/lang/String;
/*     */     //   6: areturn
/*     */     // Line number table:
/*     */     //   Java source line number -> byte code offset
/*     */     //   #104	-> 0
/*     */     // Local variable table:
/*     */     //   start	length	slot	name	descriptor
/*     */     //   0	7	0	this	Lnet/minecraft/world/item/enchantment/LevelBasedValue$Clamped;
/*     */   }
/*     */   
/*     */   public final int hashCode() {
/*     */     // Byte code:
/*     */     //   0: aload_0
/*     */     //   1: <illegal opcode> hashCode : (Lnet/minecraft/world/item/enchantment/LevelBasedValue$Clamped;)I
/*     */     //   6: ireturn
/*     */     // Line number table:
/*     */     //   Java source line number -> byte code offset
/*     */     //   #104	-> 0
/*     */     // Local variable table:
/*     */     //   start	length	slot	name	descriptor
/*     */     //   0	7	0	this	Lnet/minecraft/world/item/enchantment/LevelBasedValue$Clamped;
/*     */   }
/*     */   
/*     */   public final boolean equals(Object o) {
/*     */     // Byte code:
/*     */     //   0: aload_0
/*     */     //   1: aload_1
/*     */     //   2: <illegal opcode> equals : (Lnet/minecraft/world/item/enchantment/LevelBasedValue$Clamped;Ljava/lang/Object;)Z
/*     */     //   7: ireturn
/*     */     // Line number table:
/*     */     //   Java source line number -> byte code offset
/*     */     //   #104	-> 0
/*     */     // Local variable table:
/*     */     //   start	length	slot	name	descriptor
/*     */     //   0	8	0	this	Lnet/minecraft/world/item/enchantment/LevelBasedValue$Clamped;
/*     */     //   0	8	1	o	Ljava/lang/Object;
/*     */   }
/*     */   
/*     */   public LevelBasedValue value() {
/* 104 */     return this.value;
/*     */   }
/*     */   
/*     */   public float min() {
/* 104 */     return this.min;
/*     */   }
/*     */   
/*     */   public float max() {
/* 104 */     return this.max;
/*     */   }
/*     */   
/*     */   static {
/* 109 */     CODEC = RecordCodecBuilder.mapCodec(i -> i.group((App)LevelBasedValue.CODEC.fieldOf("value").forGetter(Clamped::value), (App)Codec.FLOAT.fieldOf("min").forGetter(Clamped::min), (App)Codec.FLOAT.fieldOf("max").forGetter(Clamped::max)).apply((Applicative)i, Clamped::new)).validate(u -> (u.max <= u.min) ? DataResult.error(()) : DataResult.success(u));
/*     */   }
/*     */   
/*     */   public float calculate(int level) {
/* 118 */     return Mth.clamp(this.value.calculate(level), this.min, this.max);
/*     */   }
/*     */   
/*     */   public MapCodec<Clamped> codec() {
/* 123 */     return CODEC;
/*     */   }
/*     */ }


/* Location:              C:\Users\karol\AppData\Roaming\ModrinthApp\meta\versions\26.2-0.19.5\26.2-0.19.5.jar!\net\minecraft\world\item\enchantment\LevelBasedValue$Clamped.class
 * Java compiler version: 25 (69.0)
 * JD-Core Version:       1.1.3
 */