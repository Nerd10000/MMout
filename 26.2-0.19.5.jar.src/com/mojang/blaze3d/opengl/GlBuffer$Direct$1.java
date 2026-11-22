/*     */ package com.mojang.blaze3d.opengl;
/*     */ 
/*     */ class null implements Runnable {
/*     */   private boolean closed = false;
/*     */   
/*     */   public void run() {
/* 135 */     if (this.closed)
/*     */       return; 
/* 138 */     this.closed = true;
/* 139 */     if ((GlBuffer.Direct.this.mappingFlags & 0x10) != 0)
/* 140 */       GlBuffer.Direct.this.dsa.flushMappedBufferRange(GlBuffer.Direct.this.handle(), GlBuffer.Direct.this.slice().offset(), GlBuffer.Direct.this.slice().length(), GlBuffer.Direct.this.usage()); 
/* 142 */     GlBuffer.Direct.this.unmap();
/*     */   }
/*     */ }


/* Location:              C:\Users\karol\AppData\Roaming\ModrinthApp\meta\versions\26.2-0.19.5\26.2-0.19.5.jar!\com\mojang\blaze3d\opengl\GlBuffer$Direct$1.class
 * Java compiler version: 25 (69.0)
 * JD-Core Version:       1.1.3
 */