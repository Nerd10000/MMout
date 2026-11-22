/*     */ package net.minecraft.world.item;
/*     */ 
/*     */ import net.minecraft.core.HolderLookup;
/*     */ import net.minecraft.world.Difficulty;
/*     */ import net.minecraft.world.level.Level;
/*     */ import net.minecraft.world.level.saveddata.maps.MapId;
/*     */ import net.minecraft.world.level.saveddata.maps.MapItemSavedData;
/*     */ 
/*     */ public interface TooltipContext {
/* 702 */   public static final TooltipContext EMPTY = new TooltipContext() {
/*     */       public HolderLookup.Provider registries() {
/* 705 */         return null;
/*     */       }
/*     */       
/*     */       public float tickRate() {
/* 710 */         return 20.0F;
/*     */       }
/*     */       
/*     */       public MapItemSavedData mapData(MapId id) {
/* 715 */         return null;
/*     */       }
/*     */       
/*     */       public boolean isPeaceful() {
/* 720 */         return false;
/*     */       }
/*     */     };
/*     */   
/*     */   HolderLookup.Provider registries();
/*     */   
/*     */   float tickRate();
/*     */   
/*     */   MapItemSavedData mapData(MapId paramMapId);
/*     */   
/*     */   boolean isPeaceful();
/*     */   
/*     */   static TooltipContext of(final Level level) {
/* 733 */     if (level == null)
/* 734 */       return EMPTY; 
/* 737 */     return new TooltipContext() {
/*     */         public HolderLookup.Provider registries() {
/* 740 */           return (HolderLookup.Provider)level.registryAccess();
/*     */         }
/*     */         
/*     */         public float tickRate() {
/* 745 */           return level.tickRateManager().tickrate();
/*     */         }
/*     */         
/*     */         public MapItemSavedData mapData(MapId id) {
/* 750 */           return level.getMapData(id);
/*     */         }
/*     */         
/*     */         public boolean isPeaceful() {
/* 755 */           return (level.getDifficulty() == Difficulty.PEACEFUL);
/*     */         }
/*     */       };
/*     */   }
/*     */   
/*     */   static TooltipContext of(final HolderLookup.Provider registries) {
/* 761 */     return new TooltipContext() {
/*     */         public HolderLookup.Provider registries() {
/* 764 */           return registries;
/*     */         }
/*     */         
/*     */         public float tickRate() {
/* 769 */           return 20.0F;
/*     */         }
/*     */         
/*     */         public MapItemSavedData mapData(MapId id) {
/* 774 */           return null;
/*     */         }
/*     */         
/*     */         public boolean isPeaceful() {
/* 779 */           return false;
/*     */         }
/*     */       };
/*     */   }
/*     */ }


/* Location:              C:\Users\karol\AppData\Roaming\ModrinthApp\meta\versions\26.2-0.19.5\26.2-0.19.5.jar!\net\minecraft\world\item\Item$TooltipContext.class
 * Java compiler version: 25 (69.0)
 * JD-Core Version:       1.1.3
 */