/*     */ package net.minecraft.client.gui.font.providers;
/*     */ 
/*     */ public interface LineData {
/*     */   int line(int paramInt);
/*     */   
/*     */   int bitWidth();
/*     */   
/*     */   default int mask() {
/* 190 */     int mask = 0;
/* 191 */     for (int i = 0; i < 16; i++)
/* 192 */       mask |= line(i); 
/* 194 */     return mask;
/*     */   }
/*     */   
/*     */   default int calculateWidth() {
/* 198 */     int left, right, mask = mask();
/* 199 */     int bitWidth = bitWidth();
/* 203 */     if (mask == 0) {
/* 205 */       left = 0;
/* 206 */       right = bitWidth;
/*     */     } else {
/* 208 */       left = Integer.numberOfLeadingZeros(mask);
/* 209 */       right = 32 - Integer.numberOfTrailingZeros(mask) - 1;
/*     */     } 
/* 211 */     return UnihexProvider.Dimensions.pack(left, right);
/*     */   }
/*     */ }


/* Location:              C:\Users\karol\AppData\Roaming\ModrinthApp\meta\versions\26.2-0.19.5\26.2-0.19.5.jar!\net\minecraft\client\gui\font\providers\UnihexProvider$LineData.class
 * Java compiler version: 25 (69.0)
 * JD-Core Version:       1.1.3
 */