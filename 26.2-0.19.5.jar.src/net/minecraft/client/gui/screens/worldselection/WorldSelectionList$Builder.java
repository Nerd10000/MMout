/*     */ package net.minecraft.client.gui.screens.worldselection;
/*     */ 
/*     */ import java.util.function.Consumer;
/*     */ import net.minecraft.client.Minecraft;
/*     */ import net.minecraft.client.gui.screens.Screen;
/*     */ import net.minecraft.world.level.storage.LevelSummary;
/*     */ 
/*     */ public class Builder {
/*     */   private final Minecraft minecraft;
/*     */   
/*     */   private final Screen screen;
/*     */   
/*     */   private int width;
/*     */   
/*     */   private int height;
/*     */   
/* 694 */   private String filter = "";
/*     */   
/* 695 */   private WorldSelectionList.EntryType type = WorldSelectionList.EntryType.SINGLEPLAYER;
/*     */   
/* 697 */   private WorldSelectionList oldList = null;
/*     */   
/* 698 */   private Consumer<LevelSummary> onEntrySelect = null;
/*     */   
/* 699 */   private Consumer<WorldSelectionList.WorldListEntry> onEntryInteract = null;
/*     */   
/*     */   public Builder(Minecraft minecraft, Screen screen) {
/* 702 */     this.minecraft = minecraft;
/* 703 */     this.screen = screen;
/*     */   }
/*     */   
/*     */   public Builder width(int width) {
/* 707 */     this.width = width;
/* 708 */     return this;
/*     */   }
/*     */   
/*     */   public Builder height(int height) {
/* 712 */     this.height = height;
/* 713 */     return this;
/*     */   }
/*     */   
/*     */   public Builder filter(String filter) {
/* 717 */     this.filter = filter;
/* 718 */     return this;
/*     */   }
/*     */   
/*     */   public Builder oldList(WorldSelectionList oldList) {
/* 722 */     this.oldList = oldList;
/* 723 */     return this;
/*     */   }
/*     */   
/*     */   public Builder onEntrySelect(Consumer<LevelSummary> onEntrySelect) {
/* 727 */     this.onEntrySelect = onEntrySelect;
/* 728 */     return this;
/*     */   }
/*     */   
/*     */   public Builder onEntryInteract(Consumer<WorldSelectionList.WorldListEntry> onEntryInteract) {
/* 732 */     this.onEntryInteract = onEntryInteract;
/* 733 */     return this;
/*     */   }
/*     */   
/*     */   public Builder uploadWorld() {
/* 737 */     this.type = WorldSelectionList.EntryType.UPLOAD_WORLD;
/* 738 */     return this;
/*     */   }
/*     */   
/*     */   public WorldSelectionList build() {
/* 742 */     return new WorldSelectionList(this.screen, this.minecraft, this.width, this.height, this.filter, this.oldList, this.onEntrySelect, this.onEntryInteract, this.type);
/*     */   }
/*     */ }


/* Location:              C:\Users\karol\AppData\Roaming\ModrinthApp\meta\versions\26.2-0.19.5\26.2-0.19.5.jar!\net\minecraft\client\gui\screens\worldselection\WorldSelectionList$Builder.class
 * Java compiler version: 25 (69.0)
 * JD-Core Version:       1.1.3
 */