/*     */ package net.minecraft.world.level.lighting;
/*     */ 
/*     */ public enum SectionType {
/* 345 */   EMPTY("2"),
/* 346 */   LIGHT_ONLY("1"),
/* 347 */   LIGHT_AND_DATA("0");
/*     */   
/*     */   private final String display;
/*     */   
/*     */   SectionType(String display) {
/* 353 */     this.display = display;
/*     */   }
/*     */   
/*     */   public String display() {
/* 357 */     return this.display;
/*     */   }
/*     */ }


/* Location:              C:\Users\karol\AppData\Roaming\ModrinthApp\meta\versions\26.2-0.19.5\26.2-0.19.5.jar!\net\minecraft\world\level\lighting\LayerLightSectionStorage$SectionType.class
 * Java compiler version: 25 (69.0)
 * JD-Core Version:       1.1.3
 */