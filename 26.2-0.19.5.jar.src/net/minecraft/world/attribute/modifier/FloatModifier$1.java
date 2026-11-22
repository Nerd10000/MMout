/*    */ package net.minecraft.world.attribute.modifier;
/*    */ 
/*    */ import com.mojang.serialization.Codec;
/*    */ import net.minecraft.util.Mth;
/*    */ import net.minecraft.world.attribute.EnvironmentAttribute;
/*    */ import net.minecraft.world.attribute.LerpFunction;
/*    */ 
/*    */ class null implements FloatModifier<FloatWithAlpha> {
/*    */   public Float apply(Float subject, FloatWithAlpha argument) {
/* 12 */     return Float.valueOf(Mth.lerp(argument.alpha(), subject.floatValue(), argument.value()));
/*    */   }
/*    */   
/*    */   public Codec<FloatWithAlpha> argumentCodec(EnvironmentAttribute<Float> type) {
/* 17 */     return FloatWithAlpha.CODEC;
/*    */   }
/*    */   
/*    */   public LerpFunction<FloatWithAlpha> argumentKeyframeLerp(EnvironmentAttribute<Float> type) {
/* 22 */     return (alpha, from, to) -> new FloatWithAlpha(Mth.lerp(alpha, from.value(), to.value()), Mth.lerp(alpha, from.alpha(), to.alpha()));
/*    */   }
/*    */ }


/* Location:              C:\Users\karol\AppData\Roaming\ModrinthApp\meta\versions\26.2-0.19.5\26.2-0.19.5.jar!\net\minecraft\world\attribute\modifier\FloatModifier$1.class
 * Java compiler version: 25 (69.0)
 * JD-Core Version:       1.1.3
 */