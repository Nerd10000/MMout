/*      */ package net.minecraft.world.level.levelgen.structure.structures;
/*      */ 
/*      */ import net.minecraft.core.Direction;
/*      */ 
/*      */ class RoomDefinition {
/*      */   private final int index;
/*      */   
/* 1780 */   private final RoomDefinition[] connections = new RoomDefinition[6];
/*      */   
/* 1781 */   private final boolean[] hasOpening = new boolean[6];
/*      */   
/*      */   private boolean claimed;
/*      */   
/*      */   private boolean isSource;
/*      */   
/*      */   private int scanIndex;
/*      */   
/*      */   public RoomDefinition(int roomIndex) {
/* 1787 */     this.index = roomIndex;
/*      */   }
/*      */   
/*      */   public void setConnection(Direction direction, RoomDefinition definition) {
/* 1791 */     this.connections[direction.get3DDataValue()] = definition;
/* 1792 */     definition.connections[direction.getOpposite().get3DDataValue()] = this;
/*      */   }
/*      */   
/*      */   public void updateOpenings() {
/* 1796 */     for (int i = 0; i < 6; i++)
/* 1797 */       this.hasOpening[i] = (this.connections[i] != null); 
/*      */   }
/*      */   
/*      */   public boolean findSource(int scanIndex) {
/* 1802 */     if (this.isSource)
/* 1803 */       return true; 
/* 1805 */     this.scanIndex = scanIndex;
/* 1806 */     for (int i = 0; i < 6; i++) {
/* 1807 */       if (this.connections[i] != null && this.hasOpening[i] && 
/* 1808 */         (this.connections[i]).scanIndex != scanIndex && this.connections[i].findSource(scanIndex))
/* 1809 */         return true; 
/*      */     } 
/* 1813 */     return false;
/*      */   }
/*      */   
/*      */   public boolean isSpecial() {
/* 1817 */     return (this.index >= 75);
/*      */   }
/*      */   
/*      */   public int countOpenings() {
/* 1821 */     int c = 0;
/* 1822 */     for (int i = 0; i < 6; i++) {
/* 1823 */       if (this.hasOpening[i])
/* 1824 */         c++; 
/*      */     } 
/* 1827 */     return c;
/*      */   }
/*      */ }


/* Location:              C:\Users\karol\AppData\Roaming\ModrinthApp\meta\versions\26.2-0.19.5\26.2-0.19.5.jar!\net\minecraft\world\level\levelgen\structure\structures\OceanMonumentPieces$RoomDefinition.class
 * Java compiler version: 25 (69.0)
 * JD-Core Version:       1.1.3
 */