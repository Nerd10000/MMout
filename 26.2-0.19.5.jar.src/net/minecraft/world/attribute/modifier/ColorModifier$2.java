/*    */ package net.minecraft.world.attribute.modifier;
/*    */ 
/*    */ import com.mojang.serialization.Codec;
/*    */ import net.minecraft.util.ARGB;
/*    */ import net.minecraft.util.Mth;
/*    */ import net.minecraft.world.attribute.EnvironmentAttribute;
/*    */ import net.minecraft.world.attribute.LerpFunction;
/*    */ 
/*    */ class null implements ColorModifier<ColorModifier.BlendToGray> {
/*    */   public Integer apply(Integer subject, ColorModifier.BlendToGray argument) {
/* 67 */     int multipliedGreyscale = ARGB.scaleRGB(ARGB.greyscale(subject.intValue()), argument.brightness);
/* 68 */     return Integer.valueOf(ARGB.srgbLerp(argument.factor, subject.intValue(), multipliedGreyscale));
/*    */   }
/*    */   
/*    */   public Codec<ColorModifier.BlendToGray> argumentCodec(EnvironmentAttribute<Integer> type) {
/* 73 */     return ColorModifier.BlendToGray.CODEC;
/*    */   }
/*    */   
/*    */   public LerpFunction<ColorModifier.BlendToGray> argumentKeyframeLerp(EnvironmentAttribute<Integer> type) {
/* 78 */     return (alpha, from, to) -> new ColorModifier.BlendToGray(Mth.lerp(alpha, from.brightness, to.brightness), Mth.lerp(alpha, from.factor, to.factor));
/*    */   }
/*    */ }


/* Location:              C:\Users\karol\AppData\Roaming\ModrinthApp\meta\versions\26.2-0.19.5\26.2-0.19.5.jar!\net\minecraft\world\attribute\modifier\ColorModifier$2.class
 * Java compiler version: 25 (69.0)
 * JD-Core Version:       1.1.3
 */