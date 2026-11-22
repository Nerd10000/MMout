/*     */ package net.minecraft.client.gui.components;
/*     */ 
/*     */ import com.google.common.collect.ImmutableList;
/*     */ import java.util.Collection;
/*     */ import java.util.List;
/*     */ import java.util.function.BooleanSupplier;
/*     */ 
/*     */ public interface ValueListSupplier<T> {
/*     */   List<T> getSelectedList();
/*     */   
/*     */   List<T> getDefaultList();
/*     */   
/*     */   static <T> ValueListSupplier<T> create(Collection<T> values) {
/* 279 */     final ImmutableList copy = ImmutableList.copyOf(values);
/* 280 */     return new ValueListSupplier<T>() {
/*     */         public List<T> getSelectedList() {
/* 283 */           return copy;
/*     */         }
/*     */         
/*     */         public List<T> getDefaultList() {
/* 288 */           return copy;
/*     */         }
/*     */       };
/*     */   }
/*     */   
/*     */   static <T> ValueListSupplier<T> create(final BooleanSupplier altSelector, List<T> defaultList, List<T> altList) {
/* 294 */     final ImmutableList defaultCopy = ImmutableList.copyOf(defaultList);
/* 295 */     final ImmutableList altCopy = ImmutableList.copyOf(altList);
/* 296 */     return new ValueListSupplier<T>() {
/*     */         public List<T> getSelectedList() {
/* 299 */           return altSelector.getAsBoolean() ? altCopy : defaultCopy;
/*     */         }
/*     */         
/*     */         public List<T> getDefaultList() {
/* 304 */           return defaultCopy;
/*     */         }
/*     */       };
/*     */   }
/*     */ }


/* Location:              C:\Users\karol\AppData\Roaming\ModrinthApp\meta\versions\26.2-0.19.5\26.2-0.19.5.jar!\net\minecraft\client\gui\components\CycleButton$ValueListSupplier.class
 * Java compiler version: 25 (69.0)
 * JD-Core Version:       1.1.3
 */