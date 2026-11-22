/*     */ package net.minecraft.client.gui.screens.packs;
/*     */ 
/*     */ import java.util.List;
/*     */ import net.minecraft.server.packs.repository.Pack;
/*     */ 
/*     */ class UnselectedPackEntry extends PackSelectionModel.EntryBase {
/*     */   public UnselectedPackEntry(Pack pack) {
/* 244 */     super(pack);
/*     */   }
/*     */   
/*     */   protected List<Pack> getSelfList() {
/* 249 */     return PackSelectionModel.this.unselected;
/*     */   }
/*     */   
/*     */   protected List<Pack> getOtherList() {
/* 254 */     return PackSelectionModel.this.selected;
/*     */   }
/*     */   
/*     */   public boolean isSelected() {
/* 259 */     return false;
/*     */   }
/*     */   
/*     */   public void select() {
/* 264 */     toggleSelection();
/*     */   }
/*     */   
/*     */   public void unselect() {}
/*     */ }


/* Location:              C:\Users\karol\AppData\Roaming\ModrinthApp\meta\versions\26.2-0.19.5\26.2-0.19.5.jar!\net\minecraft\client\gui\screens\packs\PackSelectionModel$UnselectedPackEntry.class
 * Java compiler version: 25 (69.0)
 * JD-Core Version:       1.1.3
 */