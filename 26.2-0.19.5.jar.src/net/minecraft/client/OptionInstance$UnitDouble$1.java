/*     */ package net.minecraft.client;
/*     */ 
/*     */ import com.mojang.serialization.Codec;
/*     */ import java.util.Objects;
/*     */ import java.util.Optional;
/*     */ import java.util.function.DoubleFunction;
/*     */ import java.util.function.ToDoubleFunction;
/*     */ 
/*     */ class null implements OptionInstance.SliderableValueSet<R> {
/*     */   public Optional<R> validateValue(R value) {
/* 570 */     Objects.requireNonNull(to);
/* 570 */     return OptionInstance.UnitDouble.this.validateValue(Double.valueOf(from.applyAsDouble(value))).map(to::apply);
/*     */   }
/*     */   
/*     */   public double toSliderValue(R value) {
/* 575 */     return OptionInstance.UnitDouble.this.toSliderValue(Double.valueOf(from.applyAsDouble(value)));
/*     */   }
/*     */   
/*     */   public R fromSliderValue(double slider) {
/* 580 */     return to.apply(OptionInstance.UnitDouble.this.fromSliderValue(slider).doubleValue());
/*     */   }
/*     */   
/*     */   public Codec<R> codec() {
/* 585 */     Objects.requireNonNull(to);
/* 585 */     Objects.requireNonNull(from);
/* 585 */     return OptionInstance.UnitDouble.this.codec().xmap(to::apply, from::applyAsDouble);
/*     */   }
/*     */ }


/* Location:              C:\Users\karol\AppData\Roaming\ModrinthApp\meta\versions\26.2-0.19.5\26.2-0.19.5.jar!\net\minecraft\client\OptionInstance$UnitDouble$1.class
 * Java compiler version: 25 (69.0)
 * JD-Core Version:       1.1.3
 */