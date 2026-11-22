/*     */ package net.minecraft.client.gui.screens.worldselection;
/*     */ 
/*     */ import net.minecraft.client.gui.components.ObjectSelectionList;
/*     */ import net.minecraft.world.level.storage.LevelSummary;
/*     */ 
/*     */ public abstract class Entry extends ObjectSelectionList.Entry<WorldSelectionList.Entry> implements AutoCloseable {
/*     */   public void close() {}
/*     */   
/*     */   public LevelSummary getLevelSummary() {
/* 288 */     return null;
/*     */   }
/*     */ }


/* Location:              C:\Users\karol\AppData\Roaming\ModrinthApp\meta\versions\26.2-0.19.5\26.2-0.19.5.jar!\net\minecraft\client\gui\screens\worldselection\WorldSelectionList$Entry.class
 * Java compiler version: 25 (69.0)
 * JD-Core Version:       1.1.3
 */