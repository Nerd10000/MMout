/*     */ package net.minecraft.client.gui.screens.packs;
/*     */ 
/*     */ import java.util.List;
/*     */ import net.minecraft.server.packs.repository.Pack;
/*     */ 
/*     */ class SelectedPackEntry extends PackSelectionModel.EntryBase {
/*     */   public SelectedPackEntry(Pack pack) {
/* 214 */     super(pack);
/*     */   }
/*     */   
/*     */   protected List<Pack> getSelfList() {
/* 219 */     return PackSelectionModel.this.selected;
/*     */   }
/*     */   
/*     */   protected List<Pack> getOtherList() {
/* 224 */     return PackSelectionModel.this.unselected;
/*     */   }
/*     */   
/*     */   public boolean isSelected() {
/* 229 */     return true;
/*     */   }
/*     */   
/*     */   public void select() {}
/*     */   
/*     */   public void unselect() {
/* 238 */     toggleSelection();
/*     */   }
/*     */ }


/* Location:              C:\Users\karol\AppData\Roaming\ModrinthApp\meta\versions\26.2-0.19.5\26.2-0.19.5.jar!\net\minecraft\client\gui\screens\packs\PackSelectionModel$SelectedPackEntry.class
 * Java compiler version: 25 (69.0)
 * JD-Core Version:       1.1.3
 */