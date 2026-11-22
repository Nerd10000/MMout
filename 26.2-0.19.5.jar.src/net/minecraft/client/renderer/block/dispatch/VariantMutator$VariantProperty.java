/*    */ package net.minecraft.client.renderer.block.dispatch;
/*    */ 
/*    */ @FunctionalInterface
/*    */ public interface VariantProperty<T> {
/*    */   Variant apply(Variant paramVariant, T paramT);
/*    */   
/*    */   default VariantMutator withValue(T value) {
/* 15 */     return variant -> apply(variant, (T)value);
/*    */   }
/*    */ }


/* Location:              C:\Users\karol\AppData\Roaming\ModrinthApp\meta\versions\26.2-0.19.5\26.2-0.19.5.jar!\net\minecraft\client\renderer\block\dispatch\VariantMutator$VariantProperty.class
 * Java compiler version: 25 (69.0)
 * JD-Core Version:       1.1.3
 */