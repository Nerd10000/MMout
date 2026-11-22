/*     */ package net.minecraft.client;
/*     */ 
/*     */ import com.mojang.serialization.Codec;
/*     */ import java.util.Objects;
/*     */ import java.util.Optional;
/*     */ import java.util.function.IntFunction;
/*     */ import java.util.function.ToIntFunction;
/*     */ import net.minecraft.util.Mth;
/*     */ 
/*     */ public interface IntRangeBase extends OptionInstance.SliderableValueSet<Integer> {
/*     */   default Optional<Integer> next(Integer current) {
/* 384 */     return Optional.of(Integer.valueOf(current.intValue() + 1));
/*     */   }
/*     */   
/*     */   default Optional<Integer> previous(Integer current) {
/* 389 */     return Optional.of(Integer.valueOf(current.intValue() - 1));
/*     */   }
/*     */   
/*     */   default double toSliderValue(Integer value) {
/* 396 */     if (value.intValue() == minInclusive())
/* 397 */       return 0.0D; 
/* 398 */     if (value.intValue() == maxInclusive())
/* 399 */       return 1.0D; 
/* 401 */     return Mth.map(value.intValue() + 0.5D, minInclusive(), maxInclusive() + 1.0D, 0.0D, 1.0D);
/*     */   }
/*     */   
/*     */   default Integer fromSliderValue(double slider) {
/* 406 */     if (slider >= 1.0D)
/* 407 */       slider = 0.9999899864196777D; 
/* 409 */     return Integer.valueOf(Mth.floor(Mth.map(slider, 0.0D, 1.0D, minInclusive(), maxInclusive() + 1.0D)));
/*     */   }
/*     */   
/*     */   default <R> OptionInstance.SliderableValueSet<R> xmap(final IntFunction<? extends R> to, final ToIntFunction<? super R> from, final boolean discrete) {
/* 413 */     return new OptionInstance.SliderableValueSet<R>() {
/*     */         public Optional<R> validateValue(R value) {
/* 416 */           Objects.requireNonNull(to);
/* 416 */           return OptionInstance.IntRangeBase.this.validateValue(Integer.valueOf(from.applyAsInt(value))).map(to::apply);
/*     */         }
/*     */         
/*     */         public double toSliderValue(R value) {
/* 421 */           return OptionInstance.IntRangeBase.this.toSliderValue(Integer.valueOf(from.applyAsInt(value)));
/*     */         }
/*     */         
/*     */         public Optional<R> next(R current) {
/* 426 */           if (!discrete)
/* 427 */             return Optional.empty(); 
/* 429 */           int currentIndex = from.applyAsInt(current);
/* 430 */           return Optional.of(to.apply(((Integer)OptionInstance.IntRangeBase.this.validateValue(Integer.valueOf(currentIndex + 1)).orElse(Integer.valueOf(currentIndex))).intValue()));
/*     */         }
/*     */         
/*     */         public Optional<R> previous(R current) {
/* 435 */           if (!discrete)
/* 436 */             return Optional.empty(); 
/* 438 */           int currentIndex = from.applyAsInt(current);
/* 439 */           return Optional.of(to.apply(((Integer)OptionInstance.IntRangeBase.this.validateValue(Integer.valueOf(currentIndex - 1)).orElse(Integer.valueOf(currentIndex))).intValue()));
/*     */         }
/*     */         
/*     */         public R fromSliderValue(double slider) {
/* 444 */           return to.apply(OptionInstance.IntRangeBase.this.fromSliderValue(slider).intValue());
/*     */         }
/*     */         
/*     */         public Codec<R> codec() {
/* 449 */           Objects.requireNonNull(to);
/* 449 */           Objects.requireNonNull(from);
/* 449 */           return OptionInstance.IntRangeBase.this.codec().xmap(to::apply, from::applyAsInt);
/*     */         }
/*     */       };
/*     */   }
/*     */   
/*     */   int minInclusive();
/*     */   
/*     */   int maxInclusive();
/*     */ }


/* Location:              C:\Users\karol\AppData\Roaming\ModrinthApp\meta\versions\26.2-0.19.5\26.2-0.19.5.jar!\net\minecraft\client\OptionInstance$IntRangeBase.class
 * Java compiler version: 25 (69.0)
 * JD-Core Version:       1.1.3
 */