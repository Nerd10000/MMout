/*     */ package net.minecraft.util.profiling;
/*     */ 
/*     */ import com.mojang.jtracy.Plot;
/*     */ import com.mojang.jtracy.TracyClient;
/*     */ 
/*     */ final class PlotAndValue {
/*     */   private final Plot plot;
/*     */   
/*     */   private int value;
/*     */   
/*     */   private PlotAndValue(String name) {
/* 128 */     this.plot = TracyClient.createPlot(name);
/* 129 */     this.value = 0;
/*     */   }
/*     */   
/*     */   public void set(int value) {
/* 133 */     this.value = value;
/* 134 */     this.plot.setValue(value);
/*     */   }
/*     */   
/*     */   public void add(int amount) {
/* 138 */     set(this.value + amount);
/*     */   }
/*     */ }


/* Location:              C:\Users\karol\AppData\Roaming\ModrinthApp\meta\versions\26.2-0.19.5\26.2-0.19.5.jar!\net\minecraf\\util\profiling\TracyZoneFiller$PlotAndValue.class
 * Java compiler version: 25 (69.0)
 * JD-Core Version:       1.1.3
 */