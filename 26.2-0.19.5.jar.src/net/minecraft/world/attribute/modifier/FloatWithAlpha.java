/*    */ package net.minecraft.world.attribute.modifier;
/*    */ 
/*    */ import com.mojang.datafixers.kinds.App;
/*    */ import com.mojang.datafixers.kinds.Applicative;
/*    */ import com.mojang.datafixers.util.Either;
/*    */ import com.mojang.serialization.Codec;
/*    */ import com.mojang.serialization.codecs.RecordCodecBuilder;
/*    */ import java.util.function.BiFunction;
/*    */ import java.util.function.Function;
/*    */ 
/*    */ public final class FloatWithAlpha extends Record {
/*    */   private final float value;
/*    */   
/*    */   private final float alpha;
/*    */   
/*    */   private static final Codec<FloatWithAlpha> FULL_CODEC;
/*    */   
/*    */   public static final Codec<FloatWithAlpha> CODEC;
/*    */   
/*    */   public FloatWithAlpha(float value, float alpha) {
/*  7 */     this.value = value;
/*  7 */     this.alpha = alpha;
/*    */   }
/*    */   
/*    */   public final String toString() {
/*    */     // Byte code:
/*    */     //   0: aload_0
/*    */     //   1: <illegal opcode> toString : (Lnet/minecraft/world/attribute/modifier/FloatWithAlpha;)Ljava/lang/String;
/*    */     //   6: areturn
/*    */     // Line number table:
/*    */     //   Java source line number -> byte code offset
/*    */     //   #7	-> 0
/*    */     // Local variable table:
/*    */     //   start	length	slot	name	descriptor
/*    */     //   0	7	0	this	Lnet/minecraft/world/attribute/modifier/FloatWithAlpha;
/*    */   }
/*    */   
/*    */   public final int hashCode() {
/*    */     // Byte code:
/*    */     //   0: aload_0
/*    */     //   1: <illegal opcode> hashCode : (Lnet/minecraft/world/attribute/modifier/FloatWithAlpha;)I
/*    */     //   6: ireturn
/*    */     // Line number table:
/*    */     //   Java source line number -> byte code offset
/*    */     //   #7	-> 0
/*    */     // Local variable table:
/*    */     //   start	length	slot	name	descriptor
/*    */     //   0	7	0	this	Lnet/minecraft/world/attribute/modifier/FloatWithAlpha;
/*    */   }
/*    */   
/*    */   public final boolean equals(Object o) {
/*    */     // Byte code:
/*    */     //   0: aload_0
/*    */     //   1: aload_1
/*    */     //   2: <illegal opcode> equals : (Lnet/minecraft/world/attribute/modifier/FloatWithAlpha;Ljava/lang/Object;)Z
/*    */     //   7: ireturn
/*    */     // Line number table:
/*    */     //   Java source line number -> byte code offset
/*    */     //   #7	-> 0
/*    */     // Local variable table:
/*    */     //   start	length	slot	name	descriptor
/*    */     //   0	8	0	this	Lnet/minecraft/world/attribute/modifier/FloatWithAlpha;
/*    */     //   0	8	1	o	Ljava/lang/Object;
/*    */   }
/*    */   
/*    */   public float value() {
/*  7 */     return this.value;
/*    */   }
/*    */   
/*    */   public float alpha() {
/*  7 */     return this.alpha;
/*    */   }
/*    */   
/*    */   static {
/*  8 */     FULL_CODEC = RecordCodecBuilder.create(i -> i.group((App)Codec.FLOAT.fieldOf("value").forGetter(FloatWithAlpha::value), (App)Codec.floatRange(0.0F, 1.0F).optionalFieldOf("alpha", Float.valueOf(1.0F)).forGetter(FloatWithAlpha::alpha)).apply((Applicative)i, FloatWithAlpha::new));
/* 13 */     CODEC = Codec.either((Codec)Codec.FLOAT, FULL_CODEC).xmap(either -> (FloatWithAlpha)either.map(FloatWithAlpha::new, ()), parameter -> (parameter.alpha() == 1.0F) ? Either.left(Float.valueOf(parameter.value())) : Either.right(parameter));
/*    */   }
/*    */   
/*    */   public FloatWithAlpha(float value) {
/* 19 */     this(value, 1.0F);
/*    */   }
/*    */ }


/* Location:              C:\Users\karol\AppData\Roaming\ModrinthApp\meta\versions\26.2-0.19.5\26.2-0.19.5.jar!\net\minecraft\world\attribute\modifier\FloatWithAlpha.class
 * Java compiler version: 25 (69.0)
 * JD-Core Version:       1.1.3
 */