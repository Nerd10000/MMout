/*    */ package net.minecraft.world.attribute.modifier;
/*    */ 
/*    */ import com.mojang.datafixers.kinds.App;
/*    */ import com.mojang.datafixers.kinds.Applicative;
/*    */ import com.mojang.serialization.Codec;
/*    */ import com.mojang.serialization.codecs.RecordCodecBuilder;
/*    */ import java.util.function.BiFunction;
/*    */ 
/*    */ public final class BlendToGray extends Record {
/*    */   private final float brightness;
/*    */   
/*    */   private final float factor;
/*    */   
/*    */   public static final Codec<BlendToGray> CODEC;
/*    */   
/*    */   public BlendToGray(float brightness, float factor) {
/* 85 */     this.brightness = brightness;
/* 85 */     this.factor = factor;
/*    */   }
/*    */   
/*    */   public final String toString() {
/*    */     // Byte code:
/*    */     //   0: aload_0
/*    */     //   1: <illegal opcode> toString : (Lnet/minecraft/world/attribute/modifier/ColorModifier$BlendToGray;)Ljava/lang/String;
/*    */     //   6: areturn
/*    */     // Line number table:
/*    */     //   Java source line number -> byte code offset
/*    */     //   #85	-> 0
/*    */     // Local variable table:
/*    */     //   start	length	slot	name	descriptor
/*    */     //   0	7	0	this	Lnet/minecraft/world/attribute/modifier/ColorModifier$BlendToGray;
/*    */   }
/*    */   
/*    */   public final int hashCode() {
/*    */     // Byte code:
/*    */     //   0: aload_0
/*    */     //   1: <illegal opcode> hashCode : (Lnet/minecraft/world/attribute/modifier/ColorModifier$BlendToGray;)I
/*    */     //   6: ireturn
/*    */     // Line number table:
/*    */     //   Java source line number -> byte code offset
/*    */     //   #85	-> 0
/*    */     // Local variable table:
/*    */     //   start	length	slot	name	descriptor
/*    */     //   0	7	0	this	Lnet/minecraft/world/attribute/modifier/ColorModifier$BlendToGray;
/*    */   }
/*    */   
/*    */   public final boolean equals(Object o) {
/*    */     // Byte code:
/*    */     //   0: aload_0
/*    */     //   1: aload_1
/*    */     //   2: <illegal opcode> equals : (Lnet/minecraft/world/attribute/modifier/ColorModifier$BlendToGray;Ljava/lang/Object;)Z
/*    */     //   7: ireturn
/*    */     // Line number table:
/*    */     //   Java source line number -> byte code offset
/*    */     //   #85	-> 0
/*    */     // Local variable table:
/*    */     //   start	length	slot	name	descriptor
/*    */     //   0	8	0	this	Lnet/minecraft/world/attribute/modifier/ColorModifier$BlendToGray;
/*    */     //   0	8	1	o	Ljava/lang/Object;
/*    */   }
/*    */   
/*    */   public float brightness() {
/* 85 */     return this.brightness;
/*    */   }
/*    */   
/*    */   public float factor() {
/* 85 */     return this.factor;
/*    */   }
/*    */   
/*    */   static {
/* 89 */     CODEC = RecordCodecBuilder.create(i -> i.group((App)Codec.floatRange(0.0F, 1.0F).fieldOf("brightness").forGetter(BlendToGray::brightness), (App)Codec.floatRange(0.0F, 1.0F).fieldOf("factor").forGetter(BlendToGray::factor)).apply((Applicative)i, BlendToGray::new));
/*    */   }
/*    */ }


/* Location:              C:\Users\karol\AppData\Roaming\ModrinthApp\meta\versions\26.2-0.19.5\26.2-0.19.5.jar!\net\minecraft\world\attribute\modifier\ColorModifier$BlendToGray.class
 * Java compiler version: 25 (69.0)
 * JD-Core Version:       1.1.3
 */