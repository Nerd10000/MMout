/*     */ package net.minecraft.client;
/*     */ 
/*     */ import com.mojang.serialization.Codec;
/*     */ import java.util.Objects;
/*     */ import java.util.Optional;
/*     */ import java.util.function.DoubleFunction;
/*     */ import java.util.function.ToDoubleFunction;
/*     */ 
/*     */ public enum UnitDouble implements OptionInstance.SliderableValueSet<Double> {
/* 549 */   INSTANCE;
/*     */   
/*     */   public Optional<Double> validateValue(Double value) {
/* 553 */     return (value.doubleValue() >= 0.0D && value.doubleValue() <= 1.0D) ? Optional.<Double>of(value) : Optional.<Double>empty();
/*     */   }
/*     */   
/*     */   public double toSliderValue(Double value) {
/* 558 */     return value.doubleValue();
/*     */   }
/*     */   
/*     */   public Double fromSliderValue(double slider) {
/* 563 */     return Double.valueOf(slider);
/*     */   }
/*     */   
/*     */   public <R> OptionInstance.SliderableValueSet<R> xmap(final DoubleFunction<? extends R> to, final ToDoubleFunction<? super R> from) {
/* 567 */     return new OptionInstance.SliderableValueSet<R>() {
/*     */         public Optional<R> validateValue(R value) {
/* 570 */           Objects.requireNonNull(to);
/* 570 */           return OptionInstance.UnitDouble.this.validateValue(Double.valueOf(from.applyAsDouble(value))).map(to::apply);
/*     */         }
/*     */         
/*     */         public double toSliderValue(R value) {
/* 575 */           return OptionInstance.UnitDouble.this.toSliderValue(Double.valueOf(from.applyAsDouble(value)));
/*     */         }
/*     */         
/*     */         public R fromSliderValue(double slider) {
/* 580 */           return to.apply(OptionInstance.UnitDouble.this.fromSliderValue(slider).doubleValue());
/*     */         }
/*     */         
/*     */         public Codec<R> codec() {
/* 585 */           Objects.requireNonNull(to);
/* 585 */           Objects.requireNonNull(from);
/* 585 */           return OptionInstance.UnitDouble.this.codec().xmap(to::apply, from::applyAsDouble);
/*     */         }
/*     */       };
/*     */   }
/*     */   
/*     */   public Codec<Double> codec() {
/* 593 */     return Codec.withAlternative(
/* 594 */         Codec.doubleRange(0.0D, 1.0D), (Codec)Codec.BOOL, b -> Double.valueOf(b.booleanValue() ? 1.0D : 0.0D));
/*     */   }
/*     */ }


/* Location:              C:\Users\karol\AppData\Roaming\ModrinthApp\meta\versions\26.2-0.19.5\26.2-0.19.5.jar!\net\minecraft\client\OptionInstance$UnitDouble.class
 * Java compiler version: 25 (69.0)
 * JD-Core Version:       1.1.3
 */