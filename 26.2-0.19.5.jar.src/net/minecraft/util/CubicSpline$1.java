/*    */ package net.minecraft.util;
/*    */ 
/*    */ class null implements BoundedFloatFunction<C> {
/*    */   public float apply(C c) {
/* 43 */     return CubicSpline.Multipoint.sample(multipoint, c);
/*    */   }
/*    */   
/*    */   public float minValue() {
/* 48 */     return multipoint.minValue();
/*    */   }
/*    */   
/*    */   public float maxValue() {
/* 53 */     return multipoint.maxValue();
/*    */   }
/*    */ }


/* Location:              C:\Users\karol\AppData\Roaming\ModrinthApp\meta\versions\26.2-0.19.5\26.2-0.19.5.jar!\net\minecraf\\util\CubicSpline$1.class
 * Java compiler version: 25 (69.0)
 * JD-Core Version:       1.1.3
 */