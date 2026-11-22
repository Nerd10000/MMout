/*     */ package net.minecraft.world.item;
/*     */ 
/*     */ import net.minecraft.core.HolderLookup;
/*     */ import net.minecraft.world.Difficulty;
/*     */ import net.minecraft.world.level.Level;
/*     */ import net.minecraft.world.level.saveddata.maps.MapId;
/*     */ import net.minecraft.world.level.saveddata.maps.MapItemSavedData;
/*     */ 
/*     */ class null implements Item.TooltipContext {
/*     */   public HolderLookup.Provider registries() {
/* 740 */     return (HolderLookup.Provider)level.registryAccess();
/*     */   }
/*     */   
/*     */   public float tickRate() {
/* 745 */     return level.tickRateManager().tickrate();
/*     */   }
/*     */   
/*     */   public MapItemSavedData mapData(MapId id) {
/* 750 */     return level.getMapData(id);
/*     */   }
/*     */   
/*     */   public boolean isPeaceful() {
/* 755 */     return (level.getDifficulty() == Difficulty.PEACEFUL);
/*     */   }
/*     */ }


/* Location:              C:\Users\karol\AppData\Roaming\ModrinthApp\meta\versions\26.2-0.19.5\26.2-0.19.5.jar!\net\minecraft\world\item\Item$TooltipContext$2.class
 * Java compiler version: 25 (69.0)
 * JD-Core Version:       1.1.3
 */