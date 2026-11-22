/*     */ package net.minecraft.client;
/*     */ 
/*     */ import com.mojang.serialization.Codec;
/*     */ import java.util.Objects;
/*     */ import java.util.Optional;
/*     */ import java.util.function.IntFunction;
/*     */ import java.util.function.ToIntFunction;
/*     */ 
/*     */ class null implements OptionInstance.SliderableValueSet<R> {
/*     */   public Optional<R> validateValue(R value) {
/* 416 */     Objects.requireNonNull(to);
/* 416 */     return OptionInstance.IntRangeBase.this.validateValue(Integer.valueOf(from.applyAsInt(value))).map(to::apply);
/*     */   }
/*     */   
/*     */   public double toSliderValue(R value) {
/* 421 */     return OptionInstance.IntRangeBase.this.toSliderValue(Integer.valueOf(from.applyAsInt(value)));
/*     */   }
/*     */   
/*     */   public Optional<R> next(R current) {
/* 426 */     if (!discrete)
/* 427 */       return Optional.empty(); 
/* 429 */     int currentIndex = from.applyAsInt(current);
/* 430 */     return Optional.of(to.apply(((Integer)OptionInstance.IntRangeBase.this.validateValue(Integer.valueOf(currentIndex + 1)).orElse(Integer.valueOf(currentIndex))).intValue()));
/*     */   }
/*     */   
/*     */   public Optional<R> previous(R current) {
/* 435 */     if (!discrete)
/* 436 */       return Optional.empty(); 
/* 438 */     int currentIndex = from.applyAsInt(current);
/* 439 */     return Optional.of(to.apply(((Integer)OptionInstance.IntRangeBase.this.validateValue(Integer.valueOf(currentIndex - 1)).orElse(Integer.valueOf(currentIndex))).intValue()));
/*     */   }
/*     */   
/*     */   public R fromSliderValue(double slider) {
/* 444 */     return to.apply(OptionInstance.IntRangeBase.this.fromSliderValue(slider).intValue());
/*     */   }
/*     */   
/*     */   public Codec<R> codec() {
/* 449 */     Objects.requireNonNull(to);
/* 449 */     Objects.requireNonNull(from);
/* 449 */     return OptionInstance.IntRangeBase.this.codec().xmap(to::apply, from::applyAsInt);
/*     */   }
/*     */ }


/* Location:              C:\Users\karol\AppData\Roaming\ModrinthApp\meta\versions\26.2-0.19.5\26.2-0.19.5.jar!\net\minecraft\client\OptionInstance$IntRangeBase$1.class
 * Java compiler version: 25 (69.0)
 * JD-Core Version:       1.1.3
 */