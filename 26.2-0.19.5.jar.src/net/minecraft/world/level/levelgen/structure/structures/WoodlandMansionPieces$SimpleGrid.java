/*      */ package net.minecraft.world.level.levelgen.structure.structures;
/*      */ 
/*      */ class SimpleGrid {
/*      */   private final int[][] grid;
/*      */   
/*      */   private final int width;
/*      */   
/*      */   private final int height;
/*      */   
/*      */   private final int valueIfOutside;
/*      */   
/*      */   public SimpleGrid(int width, int height, int valueIfOutside) {
/* 1002 */     this.width = width;
/* 1003 */     this.height = height;
/* 1004 */     this.valueIfOutside = valueIfOutside;
/* 1005 */     this.grid = new int[width][height];
/*      */   }
/*      */   
/*      */   public void set(int x, int y, int value) {
/* 1009 */     if (x >= 0 && x < this.width && y >= 0 && y < this.height)
/* 1010 */       this.grid[x][y] = value; 
/*      */   }
/*      */   
/*      */   public void set(int x0, int y0, int x1, int y1, int value) {
/* 1015 */     for (int y = y0; y <= y1; y++) {
/* 1016 */       for (int x = x0; x <= x1; x++)
/* 1017 */         set(x, y, value); 
/*      */     } 
/*      */   }
/*      */   
/*      */   public int get(int x, int y) {
/* 1023 */     if (x >= 0 && x < this.width && y >= 0 && y < this.height)
/* 1024 */       return this.grid[x][y]; 
/* 1026 */     return this.valueIfOutside;
/*      */   }
/*      */   
/*      */   public void setif(int x, int y, int ifValue, int value) {
/* 1030 */     if (get(x, y) == ifValue)
/* 1031 */       set(x, y, value); 
/*      */   }
/*      */   
/*      */   public boolean edgesTo(int x, int y, int ifValue) {
/* 1036 */     return (get(x - 1, y) == ifValue || get(x + 1, y) == ifValue || get(x, y + 1) == ifValue || get(x, y - 1) == ifValue);
/*      */   }
/*      */ }


/* Location:              C:\Users\karol\AppData\Roaming\ModrinthApp\meta\versions\26.2-0.19.5\26.2-0.19.5.jar!\net\minecraft\world\level\levelgen\structure\structures\WoodlandMansionPieces$SimpleGrid.class
 * Java compiler version: 25 (69.0)
 * JD-Core Version:       1.1.3
 */