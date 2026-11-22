/*     */ package net.minecraft.client.gui.screens.packs;
/*     */ 
/*     */ import java.util.List;
/*     */ import net.minecraft.client.Minecraft;
/*     */ import net.minecraft.client.OptionInstance;
/*     */ import net.minecraft.network.chat.Component;
/*     */ import net.minecraft.resources.Identifier;
/*     */ import net.minecraft.server.packs.repository.Pack;
/*     */ import net.minecraft.server.packs.repository.PackCompatibility;
/*     */ import net.minecraft.server.packs.repository.PackSource;
/*     */ 
/*     */ public abstract class EntryBase implements PackSelectionModel.Entry {
/*     */   private final Pack pack;
/*     */   
/*     */   public EntryBase(Pack pack) {
/* 117 */     this.pack = pack;
/*     */   }
/*     */   
/*     */   protected abstract List<Pack> getSelfList();
/*     */   
/*     */   protected abstract List<Pack> getOtherList();
/*     */   
/*     */   public Identifier getIconTexture() {
/* 126 */     return PackSelectionModel.this.iconGetter.apply(this.pack);
/*     */   }
/*     */   
/*     */   public PackCompatibility getCompatibility() {
/* 131 */     return this.pack.getCompatibility();
/*     */   }
/*     */   
/*     */   public String getId() {
/* 136 */     return this.pack.getId();
/*     */   }
/*     */   
/*     */   public Component getTitle() {
/* 141 */     return this.pack.getTitle();
/*     */   }
/*     */   
/*     */   public Component getDescription() {
/* 146 */     return this.pack.getDescription();
/*     */   }
/*     */   
/*     */   public PackSource getPackSource() {
/* 151 */     return this.pack.getPackSource();
/*     */   }
/*     */   
/*     */   public boolean isFixedPosition() {
/* 156 */     return this.pack.isFixedPosition();
/*     */   }
/*     */   
/*     */   public boolean isRequired() {
/* 161 */     return this.pack.isRequired();
/*     */   }
/*     */   
/*     */   protected void toggleSelection() {
/* 165 */     getSelfList().remove(this.pack);
/* 166 */     this.pack.getDefaultPosition().insert(getOtherList(), this.pack, Pack::selectionConfig, true);
/* 167 */     PackSelectionModel.this.onListChanged.accept(this);
/* 168 */     PackSelectionModel.this.updateRepoSelectedList();
/* 169 */     updateHighContrastOptionInstance();
/*     */   }
/*     */   
/*     */   private void updateHighContrastOptionInstance() {
/* 173 */     if (this.pack.getId().equals("high_contrast")) {
/* 174 */       OptionInstance<Boolean> highContrastMode = (Minecraft.getInstance()).options.highContrast();
/* 175 */       highContrastMode.set(Boolean.valueOf(!((Boolean)highContrastMode.get()).booleanValue()));
/*     */     } 
/*     */   }
/*     */   
/*     */   protected void move(int direction) {
/* 180 */     List<Pack> list = getSelfList();
/* 181 */     int currentPos = list.indexOf(this.pack);
/* 182 */     list.remove(currentPos);
/* 183 */     list.add(currentPos + direction, this.pack);
/* 184 */     PackSelectionModel.this.onListChanged.accept(this);
/*     */   }
/*     */   
/*     */   public boolean canMoveUp() {
/* 189 */     List<Pack> list = getSelfList();
/* 190 */     int index = list.indexOf(this.pack);
/* 191 */     return (index > 0 && !((Pack)list.get(index - 1)).isFixedPosition());
/*     */   }
/*     */   
/*     */   public void moveUp() {
/* 196 */     move(-1);
/*     */   }
/*     */   
/*     */   public boolean canMoveDown() {
/* 201 */     List<Pack> list = getSelfList();
/* 202 */     int index = list.indexOf(this.pack);
/* 203 */     return (index >= 0 && index < list.size() - 1 && !((Pack)list.get(index + 1)).isFixedPosition());
/*     */   }
/*     */   
/*     */   public void moveDown() {
/* 208 */     move(1);
/*     */   }
/*     */ }


/* Location:              C:\Users\karol\AppData\Roaming\ModrinthApp\meta\versions\26.2-0.19.5\26.2-0.19.5.jar!\net\minecraft\client\gui\screens\packs\PackSelectionModel$EntryBase.class
 * Java compiler version: 25 (69.0)
 * JD-Core Version:       1.1.3
 */