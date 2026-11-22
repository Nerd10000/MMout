/*    */ package net.minecraft.util.profiling;
/*    */ 
/*    */ import java.util.function.Supplier;
/*    */ 
/*    */ public class Zone implements AutoCloseable {
/*  9 */   public static final Zone INACTIVE = new Zone(null);
/*    */   
/*    */   private final ProfilerFiller profiler;
/*    */   
/*    */   Zone(ProfilerFiller profiler) {
/* 15 */     this.profiler = profiler;
/*    */   }
/*    */   
/*    */   public Zone addText(String text) {
/* 19 */     if (this.profiler != null)
/* 20 */       this.profiler.addZoneText(text); 
/* 22 */     return this;
/*    */   }
/*    */   
/*    */   public Zone addText(Supplier<String> text) {
/* 26 */     if (this.profiler != null)
/* 27 */       this.profiler.addZoneText(text.get()); 
/* 29 */     return this;
/*    */   }
/*    */   
/*    */   public Zone addValue(long value) {
/* 33 */     if (this.profiler != null)
/* 34 */       this.profiler.addZoneValue(value); 
/* 36 */     return this;
/*    */   }
/*    */   
/*    */   public Zone setColor(int color) {
/* 40 */     if (this.profiler != null)
/* 41 */       this.profiler.setZoneColor(color); 
/* 43 */     return this;
/*    */   }
/*    */   
/*    */   public void close() {
/* 48 */     if (this.profiler != null)
/* 49 */       this.profiler.pop(); 
/*    */   }
/*    */ }


/* Location:              C:\Users\karol\AppData\Roaming\ModrinthApp\meta\versions\26.2-0.19.5\26.2-0.19.5.jar!\net\minecraf\\util\profiling\Zone.class
 * Java compiler version: 25 (69.0)
 * JD-Core Version:       1.1.3
 */