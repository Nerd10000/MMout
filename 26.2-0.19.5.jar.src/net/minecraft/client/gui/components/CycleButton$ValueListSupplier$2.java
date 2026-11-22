/*     */ package net.minecraft.client.gui.components;
/*     */ 
/*     */ import java.util.List;
/*     */ import java.util.function.BooleanSupplier;
/*     */ 
/*     */ class null implements CycleButton.ValueListSupplier<T> {
/*     */   public List<T> getSelectedList() {
/* 299 */     return altSelector.getAsBoolean() ? altCopy : defaultCopy;
/*     */   }
/*     */   
/*     */   public List<T> getDefaultList() {
/* 304 */     return defaultCopy;
/*     */   }
/*     */ }


/* Location:              C:\Users\karol\AppData\Roaming\ModrinthApp\meta\versions\26.2-0.19.5\26.2-0.19.5.jar!\net\minecraft\client\gui\components\CycleButton$ValueListSupplier$2.class
 * Java compiler version: 25 (69.0)
 * JD-Core Version:       1.1.3
 */