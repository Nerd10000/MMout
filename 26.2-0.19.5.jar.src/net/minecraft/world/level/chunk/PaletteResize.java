/*   */ package net.minecraft.world.level.chunk;
/*   */ 
/*   */ public interface PaletteResize<T> {
/*   */   int onResize(int paramInt, T paramT);
/*   */   
/*   */   static <T> PaletteResize<T> noResizeExpected() {
/* 8 */     return (bits, lastAddedValue) -> {
/*   */         throw new IllegalArgumentException("Unexpected palette resize, bits = " + bits + ", added value = " + String.valueOf(lastAddedValue));
/*   */       };
/*   */   }
/*   */ }


/* Location:              C:\Users\karol\AppData\Roaming\ModrinthApp\meta\versions\26.2-0.19.5\26.2-0.19.5.jar!\net\minecraft\world\level\chunk\PaletteResize.class
 * Java compiler version: 25 (69.0)
 * JD-Core Version:       1.1.3
 */