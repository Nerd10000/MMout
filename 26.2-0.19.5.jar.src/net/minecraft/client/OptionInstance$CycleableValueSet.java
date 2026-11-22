/*     */ package net.minecraft.client;
/*     */ 
/*     */ import java.util.Objects;
/*     */ import java.util.function.Function;
/*     */ import net.minecraft.client.gui.components.AbstractWidget;
/*     */ import net.minecraft.client.gui.components.CycleButton;
/*     */ 
/*     */ public interface CycleableValueSet<T> extends OptionInstance.ValueSet<T> {
/*     */   CycleButton.ValueListSupplier<T> valueListSupplier();
/*     */   
/*     */   default ValueSetter<T> valueSetter() {
/* 206 */     return OptionInstance::set;
/*     */   }
/*     */   
/*     */   default Function<OptionInstance<T>, AbstractWidget> createButton(OptionInstance.TooltipSupplier<T> tooltip, Options options, int x, int y, int width, OptionInstance.ValueUpdateListener<? super T> onValueChanged) {
/* 211 */     return instance -> {
/*     */         Objects.requireNonNull(instance);
/*     */         return (AbstractWidget)CycleButton.builder(instance.toString, instance::get).withValues(valueListSupplier()).withTooltip(tooltip).create(x, y, width, 20, instance.caption, ());
/*     */       };
/*     */   }
/*     */   
/*     */   public static interface ValueSetter<T> {
/*     */     void set(OptionInstance<T> param2OptionInstance, T param2T);
/*     */   }
/*     */ }


/* Location:              C:\Users\karol\AppData\Roaming\ModrinthApp\meta\versions\26.2-0.19.5\26.2-0.19.5.jar!\net\minecraft\client\OptionInstance$CycleableValueSet.class
 * Java compiler version: 25 (69.0)
 * JD-Core Version:       1.1.3
 */