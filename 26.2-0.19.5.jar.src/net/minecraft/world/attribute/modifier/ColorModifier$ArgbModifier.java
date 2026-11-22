/*    */ package net.minecraft.world.attribute.modifier;
/*    */ 
/*    */ import com.mojang.datafixers.util.Either;
/*    */ import com.mojang.serialization.Codec;
/*    */ import net.minecraft.util.ARGB;
/*    */ import net.minecraft.util.ExtraCodecs;
/*    */ import net.minecraft.world.attribute.EnvironmentAttribute;
/*    */ import net.minecraft.world.attribute.LerpFunction;
/*    */ 
/*    */ @FunctionalInterface
/*    */ public interface ArgbModifier extends ColorModifier<Integer> {
/*    */   default Codec<Integer> argumentCodec(EnvironmentAttribute<Integer> type) {
/* 52 */     return Codec.either(ExtraCodecs.STRING_ARGB_COLOR, ExtraCodecs.RGB_COLOR_CODEC).xmap(Either::unwrap, color -> (ARGB.alpha(color.intValue()) == 255) ? Either.right(color) : Either.left(color));
/*    */   }
/*    */   
/*    */   default LerpFunction<Integer> argumentKeyframeLerp(EnvironmentAttribute<Integer> type) {
/* 60 */     return LerpFunction.ofColor();
/*    */   }
/*    */ }


/* Location:              C:\Users\karol\AppData\Roaming\ModrinthApp\meta\versions\26.2-0.19.5\26.2-0.19.5.jar!\net\minecraft\world\attribute\modifier\ColorModifier$ArgbModifier.class
 * Java compiler version: 25 (69.0)
 * JD-Core Version:       1.1.3
 */