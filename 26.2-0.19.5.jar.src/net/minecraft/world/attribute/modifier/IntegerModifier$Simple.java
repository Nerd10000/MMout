/*    */ package net.minecraft.world.attribute.modifier;
/*    */ 
/*    */ import com.mojang.serialization.Codec;
/*    */ import net.minecraft.world.attribute.EnvironmentAttribute;
/*    */ import net.minecraft.world.attribute.LerpFunction;
/*    */ 
/*    */ @FunctionalInterface
/*    */ public interface Simple extends IntegerModifier<Integer> {
/*    */   default Codec<Integer> argumentCodec(EnvironmentAttribute<Integer> type) {
/* 21 */     return (Codec<Integer>)Codec.INT;
/*    */   }
/*    */   
/*    */   default LerpFunction<Integer> argumentKeyframeLerp(EnvironmentAttribute<Integer> type) {
/* 26 */     return LerpFunction.ofInteger();
/*    */   }
/*    */ }


/* Location:              C:\Users\karol\AppData\Roaming\ModrinthApp\meta\versions\26.2-0.19.5\26.2-0.19.5.jar!\net\minecraft\world\attribute\modifier\IntegerModifier$Simple.class
 * Java compiler version: 25 (69.0)
 * JD-Core Version:       1.1.3
 */