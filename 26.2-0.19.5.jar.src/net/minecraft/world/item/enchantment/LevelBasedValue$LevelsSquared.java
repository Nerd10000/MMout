/*     */ package net.minecraft.world.item.enchantment;
/*     */ 
/*     */ import com.mojang.datafixers.kinds.App;
/*     */ import com.mojang.datafixers.kinds.Applicative;
/*     */ import com.mojang.serialization.Codec;
/*     */ import com.mojang.serialization.MapCodec;
/*     */ import com.mojang.serialization.codecs.RecordCodecBuilder;
/*     */ import java.util.function.Function;
/*     */ import net.minecraft.util.Mth;
/*     */ 
/*     */ public final class LevelsSquared extends Record implements LevelBasedValue {
/*     */   private final float added;
/*     */   
/*     */   public static final MapCodec<LevelsSquared> CODEC;
/*     */   
/*     */   public LevelsSquared(float added) {
/* 166 */     this.added = added;
/*     */   }
/*     */   
/*     */   public final String toString() {
/*     */     // Byte code:
/*     */     //   0: aload_0
/*     */     //   1: <illegal opcode> toString : (Lnet/minecraft/world/item/enchantment/LevelBasedValue$LevelsSquared;)Ljava/lang/String;
/*     */     //   6: areturn
/*     */     // Line number table:
/*     */     //   Java source line number -> byte code offset
/*     */     //   #166	-> 0
/*     */     // Local variable table:
/*     */     //   start	length	slot	name	descriptor
/*     */     //   0	7	0	this	Lnet/minecraft/world/item/enchantment/LevelBasedValue$LevelsSquared;
/*     */   }
/*     */   
/*     */   public final int hashCode() {
/*     */     // Byte code:
/*     */     //   0: aload_0
/*     */     //   1: <illegal opcode> hashCode : (Lnet/minecraft/world/item/enchantment/LevelBasedValue$LevelsSquared;)I
/*     */     //   6: ireturn
/*     */     // Line number table:
/*     */     //   Java source line number -> byte code offset
/*     */     //   #166	-> 0
/*     */     // Local variable table:
/*     */     //   start	length	slot	name	descriptor
/*     */     //   0	7	0	this	Lnet/minecraft/world/item/enchantment/LevelBasedValue$LevelsSquared;
/*     */   }
/*     */   
/*     */   public final boolean equals(Object o) {
/*     */     // Byte code:
/*     */     //   0: aload_0
/*     */     //   1: aload_1
/*     */     //   2: <illegal opcode> equals : (Lnet/minecraft/world/item/enchantment/LevelBasedValue$LevelsSquared;Ljava/lang/Object;)Z
/*     */     //   7: ireturn
/*     */     // Line number table:
/*     */     //   Java source line number -> byte code offset
/*     */     //   #166	-> 0
/*     */     // Local variable table:
/*     */     //   start	length	slot	name	descriptor
/*     */     //   0	8	0	this	Lnet/minecraft/world/item/enchantment/LevelBasedValue$LevelsSquared;
/*     */     //   0	8	1	o	Ljava/lang/Object;
/*     */   }
/*     */   
/*     */   public float added() {
/* 166 */     return this.added;
/*     */   }
/*     */   
/*     */   static {
/* 167 */     CODEC = RecordCodecBuilder.mapCodec(i -> i.group((App)Codec.FLOAT.fieldOf("added").forGetter(LevelsSquared::added)).apply((Applicative)i, LevelsSquared::new));
/*     */   }
/*     */   
/*     */   public float calculate(int level) {
/* 173 */     return Mth.square(level) + this.added;
/*     */   }
/*     */   
/*     */   public MapCodec<LevelsSquared> codec() {
/* 178 */     return CODEC;
/*     */   }
/*     */ }


/* Location:              C:\Users\karol\AppData\Roaming\ModrinthApp\meta\versions\26.2-0.19.5\26.2-0.19.5.jar!\net\minecraft\world\item\enchantment\LevelBasedValue$LevelsSquared.class
 * Java compiler version: 25 (69.0)
 * JD-Core Version:       1.1.3
 */