/*     */ package net.minecraft.client;
/*     */ 
/*     */ import java.util.Optional;
/*     */ import java.util.function.Function;
/*     */ import net.minecraft.client.gui.components.AbstractWidget;
/*     */ 
/*     */ public interface SliderableValueSet<T> extends OptionInstance.ValueSet<T> {
/*     */   double toSliderValue(T paramT);
/*     */   
/*     */   default Optional<T> next(T current) {
/* 179 */     return Optional.empty();
/*     */   }
/*     */   
/*     */   default Optional<T> previous(T current) {
/* 183 */     return Optional.empty();
/*     */   }
/*     */   
/*     */   T fromSliderValue(double paramDouble);
/*     */   
/*     */   default boolean applyValueImmediately() {
/* 189 */     return true;
/*     */   }
/*     */   
/*     */   default Function<OptionInstance<T>, AbstractWidget> createButton(OptionInstance.TooltipSupplier<T> tooltip, Options options, int x, int y, int width, OptionInstance.ValueUpdateListener<? super T> onValueChanged) {
/* 194 */     return instance -> new OptionInstance.OptionInstanceSliderButton<>(options, x, y, width, 20, instance, this, tooltip, onValueChanged, applyValueImmediately());
/*     */   }
/*     */ }


/* Location:              C:\Users\karol\AppData\Roaming\ModrinthApp\meta\versions\26.2-0.19.5\26.2-0.19.5.jar!\net\minecraft\client\OptionInstance$SliderableValueSet.class
 * Java compiler version: 25 (69.0)
 * JD-Core Version:       1.1.3
 */