/*    */ package net.minecraft.world.attribute.modifier;
/*    */ 
/*    */ import com.mojang.serialization.Codec;
/*    */ import net.minecraft.util.ExtraCodecs;
/*    */ import net.minecraft.world.attribute.EnvironmentAttribute;
/*    */ import net.minecraft.world.attribute.LerpFunction;
/*    */ 
/*    */ @FunctionalInterface
/*    */ public interface RgbModifier extends ColorModifier<Integer> {
/*    */   default Codec<Integer> argumentCodec(EnvironmentAttribute<Integer> type) {
/* 39 */     return ExtraCodecs.STRING_RGB_COLOR;
/*    */   }
/*    */   
/*    */   default LerpFunction<Integer> argumentKeyframeLerp(EnvironmentAttribute<Integer> type) {
/* 44 */     return LerpFunction.ofColor();
/*    */   }
/*    */ }


/* Location:              C:\Users\karol\AppData\Roaming\ModrinthApp\meta\versions\26.2-0.19.5\26.2-0.19.5.jar!\net\minecraft\world\attribute\modifier\ColorModifier$RgbModifier.class
 * Java compiler version: 25 (69.0)
 * JD-Core Version:       1.1.3
 */