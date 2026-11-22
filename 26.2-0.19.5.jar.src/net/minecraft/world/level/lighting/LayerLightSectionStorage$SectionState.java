/*     */ package net.minecraft.world.level.lighting;
/*     */ 
/*     */ public class SectionState {
/*     */   public static final byte EMPTY = 0;
/*     */   
/*     */   private static final int MIN_NEIGHBORS = 0;
/*     */   
/*     */   private static final int MAX_NEIGHBORS = 26;
/*     */   
/*     */   private static final byte HAS_DATA_BIT = 32;
/*     */   
/*     */   private static final byte NEIGHBOR_COUNT_BITS = 31;
/*     */   
/*     */   public static byte hasData(byte state, boolean hasData) {
/* 315 */     return (byte)(hasData ? (state | 0x20) : (state & 0xFFFFFFDF));
/*     */   }
/*     */   
/*     */   public static byte neighborCount(byte state, int neighborCount) {
/* 319 */     if (neighborCount < 0 || neighborCount > 26)
/* 320 */       throw new IllegalArgumentException("Neighbor count was not within range [0; 26]"); 
/* 322 */     return (byte)(state & 0xFFFFFFE0 | neighborCount & 0x1F);
/*     */   }
/*     */   
/*     */   public static boolean hasData(byte state) {
/* 326 */     return ((state & 0x20) != 0);
/*     */   }
/*     */   
/*     */   public static int neighborCount(byte state) {
/* 330 */     return state & 0x1F;
/*     */   }
/*     */   
/*     */   public static LayerLightSectionStorage.SectionType type(byte state) {
/* 334 */     if (state == 0)
/* 335 */       return LayerLightSectionStorage.SectionType.EMPTY; 
/* 337 */     if (hasData(state))
/* 338 */       return LayerLightSectionStorage.SectionType.LIGHT_AND_DATA; 
/* 340 */     return LayerLightSectionStorage.SectionType.LIGHT_ONLY;
/*     */   }
/*     */ }


/* Location:              C:\Users\karol\AppData\Roaming\ModrinthApp\meta\versions\26.2-0.19.5\26.2-0.19.5.jar!\net\minecraft\world\level\lighting\LayerLightSectionStorage$SectionState.class
 * Java compiler version: 25 (69.0)
 * JD-Core Version:       1.1.3
 */