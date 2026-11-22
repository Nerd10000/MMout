/*    */ package net.minecraft.world.attribute.modifier;
/*    */ 
/*    */ import com.mojang.serialization.Codec;
/*    */ import net.minecraft.world.attribute.EnvironmentAttribute;
/*    */ import net.minecraft.world.attribute.LerpFunction;
/*    */ 
/*    */ @FunctionalInterface
/*    */ public interface Simple extends FloatModifier<Float> {
/*    */   default Codec<Float> argumentCodec(EnvironmentAttribute<Float> type) {
/* 39 */     return (Codec<Float>)Codec.FLOAT;
/*    */   }
/*    */   
/*    */   default LerpFunction<Float> argumentKeyframeLerp(EnvironmentAttribute<Float> type) {
/* 44 */     return LerpFunction.ofFloat();
/*    */   }
/*    */ }


/* Location:              C:\Users\karol\AppData\Roaming\ModrinthApp\meta\versions\26.2-0.19.5\26.2-0.19.5.jar!\net\minecraft\world\attribute\modifier\FloatModifier$Simple.class
 * Java compiler version: 25 (69.0)
 * JD-Core Version:       1.1.3
 */