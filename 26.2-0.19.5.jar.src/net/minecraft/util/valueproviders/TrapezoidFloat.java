/*    */ package net.minecraft.util.valueproviders;
/*    */ 
/*    */ import com.mojang.datafixers.kinds.App;
/*    */ import com.mojang.datafixers.kinds.Applicative;
/*    */ import com.mojang.datafixers.util.Function3;
/*    */ import com.mojang.serialization.Codec;
/*    */ import com.mojang.serialization.DataResult;
/*    */ import com.mojang.serialization.MapCodec;
/*    */ import com.mojang.serialization.codecs.RecordCodecBuilder;
/*    */ import net.minecraft.util.RandomSource;
/*    */ 
/*    */ public final class TrapezoidFloat extends Record implements FloatProvider {
/*    */   private final float min;
/*    */   
/*    */   private final float max;
/*    */   
/*    */   private final float plateau;
/*    */   
/*    */   public static final MapCodec<TrapezoidFloat> MAP_CODEC;
/*    */   
/*    */   public TrapezoidFloat(float min, float max, float plateau) {
/*  9 */     this.min = min;
/*  9 */     this.max = max;
/*  9 */     this.plateau = plateau;
/*    */   }
/*    */   
/*    */   public final int hashCode() {
/*    */     // Byte code:
/*    */     //   0: aload_0
/*    */     //   1: <illegal opcode> hashCode : (Lnet/minecraft/util/valueproviders/TrapezoidFloat;)I
/*    */     //   6: ireturn
/*    */     // Line number table:
/*    */     //   Java source line number -> byte code offset
/*    */     //   #9	-> 0
/*    */     // Local variable table:
/*    */     //   start	length	slot	name	descriptor
/*    */     //   0	7	0	this	Lnet/minecraft/util/valueproviders/TrapezoidFloat;
/*    */   }
/*    */   
/*    */   public final boolean equals(Object o) {
/*    */     // Byte code:
/*    */     //   0: aload_0
/*    */     //   1: aload_1
/*    */     //   2: <illegal opcode> equals : (Lnet/minecraft/util/valueproviders/TrapezoidFloat;Ljava/lang/Object;)Z
/*    */     //   7: ireturn
/*    */     // Line number table:
/*    */     //   Java source line number -> byte code offset
/*    */     //   #9	-> 0
/*    */     // Local variable table:
/*    */     //   start	length	slot	name	descriptor
/*    */     //   0	8	0	this	Lnet/minecraft/util/valueproviders/TrapezoidFloat;
/*    */     //   0	8	1	o	Ljava/lang/Object;
/*    */   }
/*    */   
/*    */   public float min() {
/*  9 */     return this.min;
/*    */   }
/*    */   
/*    */   public float max() {
/*  9 */     return this.max;
/*    */   }
/*    */   
/*    */   public float plateau() {
/*  9 */     return this.plateau;
/*    */   }
/*    */   
/*    */   static {
/* 14 */     MAP_CODEC = RecordCodecBuilder.mapCodec(i -> i.group((App)Codec.FLOAT.fieldOf("min").forGetter(TrapezoidFloat::min), (App)Codec.FLOAT.fieldOf("max").forGetter(TrapezoidFloat::max), (App)Codec.FLOAT.fieldOf("plateau").forGetter(TrapezoidFloat::plateau)).apply((Applicative)i, TrapezoidFloat::new)).validate(c -> (c.max < c.min) ? DataResult.error(()) : ((c.plateau > c.max - c.min) ? DataResult.error(()) : DataResult.success(c)));
/*    */   }
/*    */   
/*    */   public static TrapezoidFloat of(float min, float max, float plateau) {
/* 25 */     return new TrapezoidFloat(min, max, plateau);
/*    */   }
/*    */   
/*    */   public float sample(RandomSource random) {
/* 30 */     float range = this.max - this.min;
/* 31 */     float plateauStart = (range - this.plateau) / 2.0F;
/* 32 */     float plateauEnd = range - plateauStart;
/* 34 */     return this.min + random.nextFloat() * plateauEnd + random.nextFloat() * plateauStart;
/*    */   }
/*    */   
/*    */   public MapCodec<TrapezoidFloat> codec() {
/* 39 */     return MAP_CODEC;
/*    */   }
/*    */   
/*    */   public String toString() {
/* 44 */     return "trapezoid(" + this.plateau + ") in [" + this.min + "-" + this.max + "]";
/*    */   }
/*    */ }


/* Location:              C:\Users\karol\AppData\Roaming\ModrinthApp\meta\versions\26.2-0.19.5\26.2-0.19.5.jar!\net\minecraf\\util\valueproviders\TrapezoidFloat.class
 * Java compiler version: 25 (69.0)
 * JD-Core Version:       1.1.3
 */