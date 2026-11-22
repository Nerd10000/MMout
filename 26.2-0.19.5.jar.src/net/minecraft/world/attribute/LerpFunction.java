/*    */ package net.minecraft.world.attribute;
/*    */ 
/*    */ import net.minecraft.util.ARGB;
/*    */ import net.minecraft.util.Mth;
/*    */ 
/*    */ public interface LerpFunction<T> {
/*  7 */   public static final LerpFunction<?> CONSTANT = ofStep(1.0F);
/*    */   
/*    */   static LerpFunction<Float> ofFloat() {
/* 10 */     return Mth::lerp;
/*    */   }
/*    */   
/*    */   static LerpFunction<Integer> ofInteger() {
/* 14 */     return Mth::lerpInt;
/*    */   }
/*    */   
/*    */   static LerpFunction<Float> ofDegrees(float maxDelta) {
/* 18 */     return (alpha, from, to) -> {
/*    */         float delta = Mth.wrapDegrees(to.floatValue() - from.floatValue());
/*    */         return (Math.abs(delta) >= maxDelta) ? to : Float.valueOf(from.floatValue() + alpha * delta);
/*    */       };
/*    */   }
/*    */   
/*    */   static <T> LerpFunction<T> ofConstant() {
/* 29 */     return (LerpFunction)CONSTANT;
/*    */   }
/*    */   
/*    */   static <T> LerpFunction<T> ofStep(float threshold) {
/* 33 */     return (alpha, from, to) -> (alpha >= threshold) ? to : from;
/*    */   }
/*    */   
/*    */   static LerpFunction<Integer> ofColor() {
/* 37 */     return ARGB::srgbLerp;
/*    */   }
/*    */   
/*    */   T apply(float paramFloat, T paramT1, T paramT2);
/*    */ }


/* Location:              C:\Users\karol\AppData\Roaming\ModrinthApp\meta\versions\26.2-0.19.5\26.2-0.19.5.jar!\net\minecraft\world\attribute\LerpFunction.class
 * Java compiler version: 25 (69.0)
 * JD-Core Version:       1.1.3
 */