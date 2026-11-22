/*     */ package net.minecraft.client;
/*     */ 
/*     */ import java.util.function.Function;
/*     */ import net.minecraft.client.gui.components.AbstractWidget;
/*     */ 
/*     */ public interface SliderableOrCyclableValueSet<T> extends OptionInstance.SliderableValueSet<T>, OptionInstance.CycleableValueSet<T> {
/*     */   boolean createCycleButton();
/*     */   
/*     */   default Function<OptionInstance<T>, AbstractWidget> createButton(OptionInstance.TooltipSupplier<T> tooltip, Options options, int x, int y, int width, OptionInstance.ValueUpdateListener<? super T> onValueChanged) {
/* 227 */     if (createCycleButton())
/* 228 */       return super.createButton(tooltip, options, x, y, width, onValueChanged); 
/* 230 */     return super.createButton(tooltip, options, x, y, width, onValueChanged);
/*     */   }
/*     */ }


/* Location:              C:\Users\karol\AppData\Roaming\ModrinthApp\meta\versions\26.2-0.19.5\26.2-0.19.5.jar!\net\minecraft\client\OptionInstance$SliderableOrCyclableValueSet.class
 * Java compiler version: 25 (69.0)
 * JD-Core Version:       1.1.3
 */