/*    */ package net.minecraft.world.attribute.modifier;
/*    */ 
/*    */ import com.mojang.serialization.Codec;
/*    */ import net.minecraft.util.ARGB;
/*    */ import net.minecraft.util.ExtraCodecs;
/*    */ import net.minecraft.world.attribute.EnvironmentAttribute;
/*    */ import net.minecraft.world.attribute.LerpFunction;
/*    */ 
/*    */ class null implements ColorModifier<Integer> {
/*    */   public Integer apply(Integer subject, Integer argument) {
/* 16 */     return Integer.valueOf(ARGB.alphaBlend(subject.intValue(), argument.intValue()));
/*    */   }
/*    */   
/*    */   public Codec<Integer> argumentCodec(EnvironmentAttribute<Integer> type) {
/* 21 */     return ExtraCodecs.STRING_ARGB_COLOR;
/*    */   }
/*    */   
/*    */   public LerpFunction<Integer> argumentKeyframeLerp(EnvironmentAttribute<Integer> type) {
/* 26 */     return LerpFunction.ofColor();
/*    */   }
/*    */ }


/* Location:              C:\Users\karol\AppData\Roaming\ModrinthApp\meta\versions\26.2-0.19.5\26.2-0.19.5.jar!\net\minecraft\world\attribute\modifier\ColorModifier$1.class
 * Java compiler version: 25 (69.0)
 * JD-Core Version:       1.1.3
 */