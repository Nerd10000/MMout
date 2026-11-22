/*    */ package net.minecraft.util;
/*    */ 
/*    */ class null implements BoundedFloatFunction<Float> {
/*    */   public float apply(Float value) {
/* 34 */     return value.floatValue();
/*    */   }
/*    */   
/*    */   public float minValue() {
/* 39 */     return Float.NEGATIVE_INFINITY;
/*    */   }
/*    */   
/*    */   public float maxValue() {
/* 44 */     return Float.POSITIVE_INFINITY;
/*    */   }
/*    */ }


/* Location:              C:\Users\karol\AppData\Roaming\ModrinthApp\meta\versions\26.2-0.19.5\26.2-0.19.5.jar!\net\minecraf\\util\BoundedFloatFunction$2.class
 * Java compiler version: 25 (69.0)
 * JD-Core Version:       1.1.3
 */