/*    */ package net.minecraft.client.renderer.block.dispatch;
/*    */ 
/*    */ import com.mojang.math.Quadrant;
/*    */ import java.util.function.UnaryOperator;
/*    */ import net.minecraft.resources.Identifier;
/*    */ 
/*    */ @FunctionalInterface
/*    */ public interface VariantMutator extends UnaryOperator<Variant> {
/*    */   @FunctionalInterface
/*    */   public static interface VariantProperty<T> {
/*    */     Variant apply(Variant param1Variant, T param1T);
/*    */     
/*    */     default VariantMutator withValue(T value) {
/* 15 */       return variant -> apply(variant, (T)value);
/*    */     }
/*    */   }
/*    */   
/* 19 */   public static final VariantProperty<Quadrant> X_ROT = Variant::withXRot;
/*    */   
/* 20 */   public static final VariantProperty<Quadrant> Y_ROT = Variant::withYRot;
/*    */   
/* 21 */   public static final VariantProperty<Quadrant> Z_ROT = Variant::withZRot;
/*    */   
/* 22 */   public static final VariantProperty<Identifier> MODEL = Variant::withModel;
/*    */   
/* 23 */   public static final VariantProperty<Boolean> UV_LOCK = Variant::withUvLock;
/*    */   
/*    */   default VariantMutator then(VariantMutator other) {
/* 26 */     return variant -> other.apply(apply(variant));
/*    */   }
/*    */ }


/* Location:              C:\Users\karol\AppData\Roaming\ModrinthApp\meta\versions\26.2-0.19.5\26.2-0.19.5.jar!\net\minecraft\client\renderer\block\dispatch\VariantMutator.class
 * Java compiler version: 25 (69.0)
 * JD-Core Version:       1.1.3
 */