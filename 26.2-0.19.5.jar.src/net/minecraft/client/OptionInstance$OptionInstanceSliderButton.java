/*     */ package net.minecraft.client;
/*     */ 
/*     */ import java.util.Objects;
/*     */ import java.util.Optional;
/*     */ import net.minecraft.client.gui.GuiGraphicsExtractor;
/*     */ import net.minecraft.client.gui.components.AbstractOptionSliderButton;
/*     */ import net.minecraft.client.gui.components.ResettableOptionWidget;
/*     */ import net.minecraft.client.input.KeyEvent;
/*     */ import net.minecraft.client.input.MouseButtonEvent;
/*     */ import net.minecraft.util.Util;
/*     */ 
/*     */ public final class OptionInstanceSliderButton<N> extends AbstractOptionSliderButton implements ResettableOptionWidget {
/*     */   private final OptionInstance<N> instance;
/*     */   
/*     */   private final OptionInstance.SliderableValueSet<N> values;
/*     */   
/*     */   private final OptionInstance.TooltipSupplier<N> tooltipSupplier;
/*     */   
/*     */   private final OptionInstance.ValueUpdateListener<? super N> onValueChanged;
/*     */   
/*     */   private Long delayedApplyAt;
/*     */   
/*     */   private final boolean applyValueImmediately;
/*     */   
/*     */   private OptionInstanceSliderButton(Options options, int x, int y, int width, int height, OptionInstance<N> instance, OptionInstance.SliderableValueSet<N> values, OptionInstance.TooltipSupplier<N> tooltipSupplier, OptionInstance.ValueUpdateListener<? super N> onValueChanged, boolean applyValueImmediately) {
/* 284 */     super(options, x, y, width, height, values.toSliderValue(instance.get()));
/* 285 */     this.instance = instance;
/* 286 */     this.values = values;
/* 287 */     this.tooltipSupplier = tooltipSupplier;
/* 288 */     this.onValueChanged = onValueChanged;
/* 289 */     this.applyValueImmediately = applyValueImmediately;
/* 290 */     updateMessage();
/*     */   }
/*     */   
/*     */   protected void updateMessage() {
/* 295 */     setMessage(this.instance.toString.apply(this.values.fromSliderValue(this.value)));
/* 296 */     setTooltip(this.tooltipSupplier.apply(this.values.fromSliderValue(this.value)));
/*     */   }
/*     */   
/*     */   protected void applyValue() {
/* 301 */     if (this.applyValueImmediately) {
/* 302 */       applyUnsavedValue();
/*     */     } else {
/* 304 */       this.delayedApplyAt = Long.valueOf(Util.getMillis() + 600L);
/*     */     } 
/*     */   }
/*     */   
/*     */   public void applyUnsavedValue() {
/* 309 */     N sliderValue = this.values.fromSliderValue(this.value);
/* 310 */     if (!Objects.equals(sliderValue, this.instance.get())) {
/* 311 */       this.instance.set(sliderValue);
/* 312 */       this.onValueChanged.valueChanged(this.instance.get());
/*     */     } 
/*     */   }
/*     */   
/*     */   public void resetValue() {
/* 318 */     if (this.value != this.values.toSliderValue(this.instance.get())) {
/* 319 */       this.value = this.values.toSliderValue(this.instance.get());
/* 320 */       this.delayedApplyAt = null;
/* 321 */       updateMessage();
/*     */     } 
/*     */   }
/*     */   
/*     */   public void extractWidgetRenderState(GuiGraphicsExtractor graphics, int mouseX, int mouseY, float a) {
/* 327 */     super.extractWidgetRenderState(graphics, mouseX, mouseY, a);
/* 329 */     if (this.delayedApplyAt != null && Util.getMillis() >= this.delayedApplyAt.longValue()) {
/* 330 */       this.delayedApplyAt = null;
/* 331 */       applyUnsavedValue();
/* 332 */       resetValue();
/*     */     } 
/*     */   }
/*     */   
/*     */   public void onRelease(MouseButtonEvent event) {
/* 338 */     super.onRelease(event);
/* 339 */     if (this.applyValueImmediately)
/* 340 */       resetValue(); 
/*     */   }
/*     */   
/*     */   public boolean keyPressed(KeyEvent event) {
/* 346 */     if (event.isSelection()) {
/* 347 */       this.canChangeValue = !this.canChangeValue;
/* 348 */       return true;
/*     */     } 
/* 350 */     if (this.canChangeValue) {
/* 351 */       boolean left = event.isLeft();
/* 352 */       boolean right = event.isRight();
/* 353 */       if (left) {
/* 354 */         Optional<N> previous = this.values.previous(this.values.fromSliderValue(this.value));
/* 355 */         if (previous.isPresent()) {
/* 356 */           setValue(this.values.toSliderValue(previous.get()));
/* 357 */           return true;
/*     */         } 
/*     */       } 
/* 360 */       if (right) {
/* 361 */         Optional<N> next = this.values.next(this.values.fromSliderValue(this.value));
/* 362 */         if (next.isPresent()) {
/* 363 */           setValue(this.values.toSliderValue(next.get()));
/* 364 */           return true;
/*     */         } 
/*     */       } 
/* 367 */       if (left || right) {
/* 368 */         float direction = left ? -1.0F : 1.0F;
/* 369 */         setValue(this.value + (direction / (this.width - 8)));
/* 370 */         return true;
/*     */       } 
/*     */     } 
/* 373 */     return false;
/*     */   }
/*     */ }


/* Location:              C:\Users\karol\AppData\Roaming\ModrinthApp\meta\versions\26.2-0.19.5\26.2-0.19.5.jar!\net\minecraft\client\OptionInstance$OptionInstanceSliderButton.class
 * Java compiler version: 25 (69.0)
 * JD-Core Version:       1.1.3
 */