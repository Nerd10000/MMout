/*     */ package net.minecraft.client.gui.layouts;
/*     */ 
/*     */ class ChildContainer extends AbstractLayout.AbstractChildWrapper {
/*     */   private final int row;
/*     */   
/*     */   private final int column;
/*     */   
/*     */   private final int occupiedRows;
/*     */   
/*     */   private final int occupiedColumns;
/*     */   
/*     */   private ChildContainer(LayoutElement widget, int row, int column, int occupiedRows, int occupiedColumns, LayoutSettings cellSettings) {
/* 159 */     super(widget, cellSettings.getExposed());
/* 160 */     this.row = row;
/* 161 */     this.column = column;
/* 162 */     this.occupiedRows = occupiedRows;
/* 163 */     this.occupiedColumns = occupiedColumns;
/*     */   }
/*     */   
/*     */   public int getLastOccupiedRow() {
/* 167 */     return this.row + this.occupiedRows - 1;
/*     */   }
/*     */   
/*     */   public int getLastOccupiedColumn() {
/* 171 */     return this.column + this.occupiedColumns - 1;
/*     */   }
/*     */ }


/* Location:              C:\Users\karol\AppData\Roaming\ModrinthApp\meta\versions\26.2-0.19.5\26.2-0.19.5.jar!\net\minecraft\client\gui\layouts\GridLayout$ChildContainer.class
 * Java compiler version: 25 (69.0)
 * JD-Core Version:       1.1.3
 */