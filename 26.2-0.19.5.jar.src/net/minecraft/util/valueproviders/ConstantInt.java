/*    */ package net.minecraft.util.valueproviders;
/*    */ 
/*    */ import com.mojang.datafixers.kinds.App;
/*    */ import com.mojang.datafixers.kinds.Applicative;
/*    */ import com.mojang.serialization.Codec;
/*    */ import com.mojang.serialization.MapCodec;
/*    */ import com.mojang.serialization.codecs.RecordCodecBuilder;
/*    */ import net.minecraft.util.RandomSource;
/*    */ 
/*    */ public final class ConstantInt extends Record implements IntProvider {
/*    */   private final int value;
/*    */   
/*    */   public ConstantInt(int value) {
/*  8 */     this.value = value;
/*    */   }
/*    */   
/*    */   public final int hashCode() {
/*    */     // Byte code:
/*    */     //   0: aload_0
/*    */     //   1: <illegal opcode> hashCode : (Lnet/minecraft/util/valueproviders/ConstantInt;)I
/*    */     //   6: ireturn
/*    */     // Line number table:
/*    */     //   Java source line number -> byte code offset
/*    */     //   #8	-> 0
/*    */     // Local variable table:
/*    */     //   start	length	slot	name	descriptor
/*    */     //   0	7	0	this	Lnet/minecraft/util/valueproviders/ConstantInt;
/*    */   }
/*    */   
/*    */   public final boolean equals(Object o) {
/*    */     // Byte code:
/*    */     //   0: aload_0
/*    */     //   1: aload_1
/*    */     //   2: <illegal opcode> equals : (Lnet/minecraft/util/valueproviders/ConstantInt;Ljava/lang/Object;)Z
/*    */     //   7: ireturn
/*    */     // Line number table:
/*    */     //   Java source line number -> byte code offset
/*    */     //   #8	-> 0
/*    */     // Local variable table:
/*    */     //   start	length	slot	name	descriptor
/*    */     //   0	8	0	this	Lnet/minecraft/util/valueproviders/ConstantInt;
/*    */     //   0	8	1	o	Ljava/lang/Object;
/*    */   }
/*    */   
/*    */   public int value() {
/*  8 */     return this.value;
/*    */   }
/*    */   
/*  9 */   public static final ConstantInt ZERO = new ConstantInt(0);
/*    */   
/*    */   public static final MapCodec<ConstantInt> MAP_CODEC;
/*    */   
/*    */   static {
/* 11 */     MAP_CODEC = RecordCodecBuilder.mapCodec(i -> i.group((App)Codec.INT.fieldOf("value").forGetter(ConstantInt::value)).apply((Applicative)i, ConstantInt::of));
/*    */   }
/*    */   
/*    */   public static ConstantInt of(int value) {
/* 16 */     if (value == 0)
/* 17 */       return ZERO; 
/* 19 */     return new ConstantInt(value);
/*    */   }
/*    */   
/*    */   public int sample(RandomSource random) {
/* 24 */     return this.value;
/*    */   }
/*    */   
/*    */   public int minInclusive() {
/* 29 */     return this.value;
/*    */   }
/*    */   
/*    */   public int maxInclusive() {
/* 34 */     return this.value;
/*    */   }
/*    */   
/*    */   public MapCodec<ConstantInt> codec() {
/* 39 */     return MAP_CODEC;
/*    */   }
/*    */   
/*    */   public String toString() {
/* 44 */     return Integer.toString(this.value);
/*    */   }
/*    */ }


/* Location:              C:\Users\karol\AppData\Roaming\ModrinthApp\meta\versions\26.2-0.19.5\26.2-0.19.5.jar!\net\minecraf\\util\valueproviders\ConstantInt.class
 * Java compiler version: 25 (69.0)
 * JD-Core Version:       1.1.3
 */