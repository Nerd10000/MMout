/*    */ package net.minecraft.util;
/*    */ 
/*    */ import java.util.function.Function;
/*    */ 
/*    */ public interface BoundedFloatFunction<C> {
/*    */   float apply(C paramC);
/*    */   
/*    */   float minValue();
/*    */   
/*    */   float maxValue();
/*    */   
/*    */   static <C> BoundedFloatFunction<C> constant(final float value) {
/* 13 */     return new BoundedFloatFunction<C>() {
/*    */         public float apply(C c) {
/* 16 */           return value;
/*    */         }
/*    */         
/*    */         public float minValue() {
/* 21 */           return value;
/*    */         }
/*    */         
/*    */         public float maxValue() {
/* 26 */           return value;
/*    */         }
/*    */       };
/*    */   }
/*    */   
/* 31 */   public static final BoundedFloatFunction<Float> IDENTITY = new BoundedFloatFunction<Float>() {
/*    */       public float apply(Float value) {
/* 34 */         return value.floatValue();
/*    */       }
/*    */       
/*    */       public float minValue() {
/* 39 */         return Float.NEGATIVE_INFINITY;
/*    */       }
/*    */       
/*    */       public float maxValue() {
/* 44 */         return Float.POSITIVE_INFINITY;
/*    */       }
/*    */     };
/*    */   
/*    */   default <C2> BoundedFloatFunction<C2> comap(final Function<C2, C> function) {
/* 49 */     final BoundedFloatFunction<C> outer = this;
/* 50 */     return new BoundedFloatFunction<C2>(this) {
/*    */         public float apply(C2 c2) {
/* 53 */           return outer.apply(function.apply(c2));
/*    */         }
/*    */         
/*    */         public float minValue() {
/* 58 */           return outer.minValue();
/*    */         }
/*    */         
/*    */         public float maxValue() {
/* 63 */           return outer.maxValue();
/*    */         }
/*    */       };
/*    */   }
/*    */ }


/* Location:              C:\Users\karol\AppData\Roaming\ModrinthApp\meta\versions\26.2-0.19.5\26.2-0.19.5.jar!\net\minecraf\\util\BoundedFloatFunction.class
 * Java compiler version: 25 (69.0)
 * JD-Core Version:       1.1.3
 */