/*     */ package net.minecraft.client.gui.layouts;
/*     */ 
/*     */ import net.minecraft.util.Mth;
/*     */ 
/*     */ public final class RowHelper {
/*     */   private final int columns;
/*     */   
/*     */   private int index;
/*     */   
/*     */   private RowHelper(int columns) {
/* 180 */     this.columns = columns;
/*     */   }
/*     */   
/*     */   public <T extends LayoutElement> T addChild(T widget) {
/* 184 */     return addChild(widget, 1);
/*     */   }
/*     */   
/*     */   public <T extends LayoutElement> T addChild(T widget, int columnWidth) {
/* 188 */     return addChild(widget, columnWidth, defaultCellSetting());
/*     */   }
/*     */   
/*     */   public <T extends LayoutElement> T addChild(T widget, LayoutSettings layoutSettings) {
/* 192 */     return addChild(widget, 1, layoutSettings);
/*     */   }
/*     */   
/*     */   public <T extends LayoutElement> T addChild(T widget, int columnWidth, LayoutSettings layoutSettings) {
/* 196 */     int row = this.index / this.columns;
/* 197 */     int columnBegin = this.index % this.columns;
/* 199 */     if (columnBegin + columnWidth > this.columns) {
/* 200 */       row++;
/* 201 */       columnBegin = 0;
/* 202 */       this.index = Mth.roundToward(this.index, this.columns);
/*     */     } 
/* 204 */     this.index += columnWidth;
/* 206 */     return GridLayout.this.addChild(widget, row, columnBegin, 1, columnWidth, layoutSettings);
/*     */   }
/*     */   
/*     */   public GridLayout getGrid() {
/* 210 */     return GridLayout.this;
/*     */   }
/*     */   
/*     */   public LayoutSettings newCellSettings() {
/* 214 */     return GridLayout.this.newCellSettings();
/*     */   }
/*     */   
/*     */   public LayoutSettings defaultCellSetting() {
/* 218 */     return GridLayout.this.defaultCellSetting();
/*     */   }
/*     */ }


/* Location:              C:\Users\karol\AppData\Roaming\ModrinthApp\meta\versions\26.2-0.19.5\26.2-0.19.5.jar!\net\minecraft\client\gui\layouts\GridLayout$RowHelper.class
 * Java compiler version: 25 (69.0)
 * JD-Core Version:       1.1.3
 */