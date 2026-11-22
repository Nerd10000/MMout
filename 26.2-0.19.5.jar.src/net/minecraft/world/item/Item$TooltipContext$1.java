/*     */ package net.minecraft.world.item;
/*     */ 
/*     */ import net.minecraft.core.HolderLookup;
/*     */ import net.minecraft.world.level.saveddata.maps.MapId;
/*     */ import net.minecraft.world.level.saveddata.maps.MapItemSavedData;
/*     */ 
/*     */ class null implements Item.TooltipContext {
/*     */   public HolderLookup.Provider registries() {
/* 705 */     return null;
/*     */   }
/*     */   
/*     */   public float tickRate() {
/* 710 */     return 20.0F;
/*     */   }
/*     */   
/*     */   public MapItemSavedData mapData(MapId id) {
/* 715 */     return null;
/*     */   }
/*     */   
/*     */   public boolean isPeaceful() {
/* 720 */     return false;
/*     */   }
/*     */ }


/* Location:              C:\Users\karol\AppData\Roaming\ModrinthApp\meta\versions\26.2-0.19.5\26.2-0.19.5.jar!\net\minecraft\world\item\Item$TooltipContext$1.class
 * Java compiler version: 25 (69.0)
 * JD-Core Version:       1.1.3
 */