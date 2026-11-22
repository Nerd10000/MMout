/*     */ package net.minecraft.client.gui.screens.packs;
/*     */ 
/*     */ import net.minecraft.network.chat.Component;
/*     */ import net.minecraft.resources.Identifier;
/*     */ import net.minecraft.server.packs.repository.PackCompatibility;
/*     */ import net.minecraft.server.packs.repository.PackSource;
/*     */ 
/*     */ public interface Entry {
/*     */   Identifier getIconTexture();
/*     */   
/*     */   PackCompatibility getCompatibility();
/*     */   
/*     */   String getId();
/*     */   
/*     */   Component getTitle();
/*     */   
/*     */   Component getDescription();
/*     */   
/*     */   PackSource getPackSource();
/*     */   
/*     */   default Component getExtendedDescription() {
/*  83 */     return getPackSource().decorate(getDescription());
/*     */   }
/*     */   
/*     */   boolean isFixedPosition();
/*     */   
/*     */   boolean isRequired();
/*     */   
/*     */   void select();
/*     */   
/*     */   void unselect();
/*     */   
/*     */   void moveUp();
/*     */   
/*     */   void moveDown();
/*     */   
/*     */   boolean isSelected();
/*     */   
/*     */   default boolean canSelect() {
/* 101 */     return !isSelected();
/*     */   }
/*     */   
/*     */   default boolean canUnselect() {
/* 105 */     return (isSelected() && !isRequired());
/*     */   }
/*     */   
/*     */   boolean canMoveUp();
/*     */   
/*     */   boolean canMoveDown();
/*     */ }


/* Location:              C:\Users\karol\AppData\Roaming\ModrinthApp\meta\versions\26.2-0.19.5\26.2-0.19.5.jar!\net\minecraft\client\gui\screens\packs\PackSelectionModel$Entry.class
 * Java compiler version: 25 (69.0)
 * JD-Core Version:       1.1.3
 */