/*     */ package net.minecraft.client.gui.layouts;
/*     */ 
/*     */ import com.mojang.math.Divisor;
/*     */ import java.util.ArrayList;
/*     */ import java.util.List;
/*     */ import java.util.function.Consumer;
/*     */ import net.minecraft.util.Mth;
/*     */ import net.minecraft.util.Util;
/*     */ 
/*     */ public class GridLayout extends AbstractLayout {
/*  12 */   private final List<ChildContainer> children = new ArrayList<>();
/*     */   
/*  13 */   private final LayoutSettings defaultCellSettings = LayoutSettings.defaults();
/*     */   
/*  14 */   private int rowSpacing = 0;
/*     */   
/*  15 */   private int columnSpacing = 0;
/*     */   
/*     */   public GridLayout() {
/*  18 */     this(0, 0);
/*     */   }
/*     */   
/*     */   public GridLayout(int x, int y) {
/*  22 */     super(x, y, 0, 0);
/*     */   }
/*     */   
/*     */   public void arrangeElements() {
/*  27 */     super.arrangeElements();
/*  29 */     int maxRow = 0;
/*  30 */     int maxColumn = 0;
/*  32 */     for (ChildContainer child : this.children) {
/*  33 */       maxRow = Math.max(child.getLastOccupiedRow(), maxRow);
/*  34 */       maxColumn = Math.max(child.getLastOccupiedColumn(), maxColumn);
/*     */     } 
/*  37 */     int[] maxColumnWidths = new int[maxColumn + 1];
/*  38 */     int[] maxRowHeights = new int[maxRow + 1];
/*  40 */     for (ChildContainer child : this.children) {
/*  41 */       int childHeight = child.getHeight() - (child.occupiedRows - 1) * this.rowSpacing;
/*  42 */       Divisor heightDivisor = new Divisor(childHeight, child.occupiedRows);
/*  43 */       for (int i = child.row; i <= child.getLastOccupiedRow(); i++)
/*  44 */         maxRowHeights[i] = Math.max(maxRowHeights[i], heightDivisor.nextInt()); 
/*  46 */       int childWidth = child.getWidth() - (child.occupiedColumns - 1) * this.columnSpacing;
/*  47 */       Divisor widthDivisor = new Divisor(childWidth, child.occupiedColumns);
/*  48 */       for (int j = child.column; j <= child.getLastOccupiedColumn(); j++)
/*  49 */         maxColumnWidths[j] = Math.max(maxColumnWidths[j], widthDivisor.nextInt()); 
/*     */     } 
/*  53 */     int[] columnXOffsets = new int[maxColumn + 1];
/*  54 */     int[] rowYOffsets = new int[maxRow + 1];
/*  56 */     columnXOffsets[0] = 0;
/*  57 */     for (int column = 1; column <= maxColumn; column++)
/*  58 */       columnXOffsets[column] = columnXOffsets[column - 1] + maxColumnWidths[column - 1] + this.columnSpacing; 
/*  60 */     rowYOffsets[0] = 0;
/*  61 */     for (int row = 1; row <= maxRow; row++)
/*  62 */       rowYOffsets[row] = rowYOffsets[row - 1] + maxRowHeights[row - 1] + this.rowSpacing; 
/*  65 */     for (ChildContainer child : this.children) {
/*  66 */       int availableWidth = 0;
/*  67 */       for (int i = child.column; i <= child.getLastOccupiedColumn(); i++)
/*  68 */         availableWidth += maxColumnWidths[i]; 
/*  70 */       availableWidth += this.columnSpacing * (child.occupiedColumns - 1);
/*  71 */       child.setX(getX() + columnXOffsets[child.column], availableWidth);
/*  73 */       int availableHeight = 0;
/*  74 */       for (int j = child.row; j <= child.getLastOccupiedRow(); j++)
/*  75 */         availableHeight += maxRowHeights[j]; 
/*  77 */       availableHeight += this.rowSpacing * (child.occupiedRows - 1);
/*  78 */       child.setY(getY() + rowYOffsets[child.row], availableHeight);
/*     */     } 
/*  81 */     this.width = columnXOffsets[maxColumn] + maxColumnWidths[maxColumn];
/*  82 */     this.height = rowYOffsets[maxRow] + maxRowHeights[maxRow];
/*     */   }
/*     */   
/*     */   public <T extends LayoutElement> T addChild(T child, int row, int column) {
/*  86 */     return addChild(child, row, column, newCellSettings());
/*     */   }
/*     */   
/*     */   public <T extends LayoutElement> T addChild(T child, int row, int column, LayoutSettings cellSettings) {
/*  90 */     return addChild(child, row, column, 1, 1, cellSettings);
/*     */   }
/*     */   
/*     */   public <T extends LayoutElement> T addChild(T child, int row, int column, Consumer<LayoutSettings> layoutSettingsAdjustments) {
/*  94 */     return addChild(child, row, column, 1, 1, (LayoutSettings)Util.make(newCellSettings(), layoutSettingsAdjustments));
/*     */   }
/*     */   
/*     */   public <T extends LayoutElement> T addChild(T child, int row, int column, int rows, int columns) {
/*  98 */     return addChild(child, row, column, rows, columns, newCellSettings());
/*     */   }
/*     */   
/*     */   public <T extends LayoutElement> T addChild(T child, int row, int column, int rows, int columns, LayoutSettings cellSettings) {
/* 102 */     if (rows < 1)
/* 103 */       throw new IllegalArgumentException("Occupied rows must be at least 1"); 
/* 105 */     if (columns < 1)
/* 106 */       throw new IllegalArgumentException("Occupied columns must be at least 1"); 
/* 108 */     this.children.add(new ChildContainer((LayoutElement)child, row, column, rows, columns, cellSettings));
/* 109 */     return child;
/*     */   }
/*     */   
/*     */   public <T extends LayoutElement> T addChild(T child, int row, int column, int rows, int columns, Consumer<LayoutSettings> layoutSettingsAdjustments) {
/* 113 */     return addChild(child, row, column, rows, columns, (LayoutSettings)Util.make(newCellSettings(), layoutSettingsAdjustments));
/*     */   }
/*     */   
/*     */   public GridLayout columnSpacing(int columnSpacing) {
/* 117 */     this.columnSpacing = columnSpacing;
/* 118 */     return this;
/*     */   }
/*     */   
/*     */   public GridLayout rowSpacing(int rowSpacing) {
/* 122 */     this.rowSpacing = rowSpacing;
/* 123 */     return this;
/*     */   }
/*     */   
/*     */   public GridLayout spacing(int spacing) {
/* 127 */     return columnSpacing(spacing).rowSpacing(spacing);
/*     */   }
/*     */   
/*     */   public void visitChildren(Consumer<LayoutElement> layoutElementVisitor) {
/* 132 */     this.children.forEach(child -> layoutElementVisitor.accept(child.child));
/*     */   }
/*     */   
/*     */   public void removeChildren() {
/* 137 */     this.children.clear();
/*     */   }
/*     */   
/*     */   public LayoutSettings newCellSettings() {
/* 141 */     return this.defaultCellSettings.copy();
/*     */   }
/*     */   
/*     */   public LayoutSettings defaultCellSetting() {
/* 145 */     return this.defaultCellSettings;
/*     */   }
/*     */   
/*     */   public RowHelper createRowHelper(int columns) {
/* 149 */     return new RowHelper(columns);
/*     */   }
/*     */   
/*     */   private static class ChildContainer extends AbstractLayout.AbstractChildWrapper {
/*     */     private final int row;
/*     */     
/*     */     private final int column;
/*     */     
/*     */     private final int occupiedRows;
/*     */     
/*     */     private final int occupiedColumns;
/*     */     
/*     */     private ChildContainer(LayoutElement widget, int row, int column, int occupiedRows, int occupiedColumns, LayoutSettings cellSettings) {
/* 159 */       super(widget, cellSettings.getExposed());
/* 160 */       this.row = row;
/* 161 */       this.column = column;
/* 162 */       this.occupiedRows = occupiedRows;
/* 163 */       this.occupiedColumns = occupiedColumns;
/*     */     }
/*     */     
/*     */     public int getLastOccupiedRow() {
/* 167 */       return this.row + this.occupiedRows - 1;
/*     */     }
/*     */     
/*     */     public int getLastOccupiedColumn() {
/* 171 */       return this.column + this.occupiedColumns - 1;
/*     */     }
/*     */   }
/*     */   
/*     */   public final class RowHelper {
/*     */     private final int columns;
/*     */     
/*     */     private int index;
/*     */     
/*     */     private RowHelper(int columns) {
/* 180 */       this.columns = columns;
/*     */     }
/*     */     
/*     */     public <T extends LayoutElement> T addChild(T widget) {
/* 184 */       return addChild(widget, 1);
/*     */     }
/*     */     
/*     */     public <T extends LayoutElement> T addChild(T widget, int columnWidth) {
/* 188 */       return addChild(widget, columnWidth, defaultCellSetting());
/*     */     }
/*     */     
/*     */     public <T extends LayoutElement> T addChild(T widget, LayoutSettings layoutSettings) {
/* 192 */       return addChild(widget, 1, layoutSettings);
/*     */     }
/*     */     
/*     */     public <T extends LayoutElement> T addChild(T widget, int columnWidth, LayoutSettings layoutSettings) {
/* 196 */       int row = this.index / this.columns;
/* 197 */       int columnBegin = this.index % this.columns;
/* 199 */       if (columnBegin + columnWidth > this.columns) {
/* 200 */         row++;
/* 201 */         columnBegin = 0;
/* 202 */         this.index = Mth.roundToward(this.index, this.columns);
/*     */       } 
/* 204 */       this.index += columnWidth;
/* 206 */       return GridLayout.this.addChild(widget, row, columnBegin, 1, columnWidth, layoutSettings);
/*     */     }
/*     */     
/*     */     public GridLayout getGrid() {
/* 210 */       return GridLayout.this;
/*     */     }
/*     */     
/*     */     public LayoutSettings newCellSettings() {
/* 214 */       return GridLayout.this.newCellSettings();
/*     */     }
/*     */     
/*     */     public LayoutSettings defaultCellSetting() {
/* 218 */       return GridLayout.this.defaultCellSetting();
/*     */     }
/*     */   }
/*     */ }


/* Location:              C:\Users\karol\AppData\Roaming\ModrinthApp\meta\versions\26.2-0.19.5\26.2-0.19.5.jar!\net\minecraft\client\gui\layouts\GridLayout.class
 * Java compiler version: 25 (69.0)
 * JD-Core Version:       1.1.3
 */