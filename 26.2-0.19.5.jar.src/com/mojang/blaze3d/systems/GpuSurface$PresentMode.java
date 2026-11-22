/*     */ package com.mojang.blaze3d.systems;
/*     */ 
/*     */ import java.util.Collection;
/*     */ 
/*     */ public enum PresentMode {
/* 109 */   IMMEDIATE, MAILBOX, FIFO, FIFO_RELAXED;
/*     */   
/*     */   private static final PresentMode[] PRESENT_MODES_VSYNC;
/*     */   
/*     */   private static final PresentMode[] PRESENT_MODES_NO_VSYNC;
/*     */   
/*     */   static {
/* 131 */     PRESENT_MODES_VSYNC = new PresentMode[] { FIFO_RELAXED, FIFO };
/* 136 */     PRESENT_MODES_NO_VSYNC = new PresentMode[] { IMMEDIATE, MAILBOX, FIFO };
/*     */   }
/*     */   
/*     */   public static PresentMode getSupportedVsyncMode(Collection<PresentMode> supportedModes, boolean vsync) {
/* 143 */     PresentMode[] preferred = vsync ? PRESENT_MODES_VSYNC : PRESENT_MODES_NO_VSYNC;
/* 144 */     for (PresentMode mode : preferred) {
/* 145 */       if (supportedModes.contains(mode))
/* 146 */         return mode; 
/*     */     } 
/* 150 */     throw new IllegalStateException("No supported presentation mode was found");
/*     */   }
/*     */ }


/* Location:              C:\Users\karol\AppData\Roaming\ModrinthApp\meta\versions\26.2-0.19.5\26.2-0.19.5.jar!\com\mojang\blaze3d\systems\GpuSurface$PresentMode.class
 * Java compiler version: 25 (69.0)
 * JD-Core Version:       1.1.3
 */