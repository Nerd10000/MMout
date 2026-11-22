/*     */ package net.minecraft.world.level.levelgen.structure.structures;
/*     */ 
/*     */ import java.util.ArrayList;
/*     */ import java.util.List;
/*     */ import net.minecraft.core.Direction;
/*     */ import net.minecraft.util.RandomSource;
/*     */ import net.minecraft.util.Util;
/*     */ 
/*     */ class MansionGrid {
/*     */   private static final int DEFAULT_SIZE = 11;
/*     */   
/*     */   private static final int CLEAR = 0;
/*     */   
/*     */   private static final int CORRIDOR = 1;
/*     */   
/*     */   private static final int ROOM = 2;
/*     */   
/*     */   private static final int START_ROOM = 3;
/*     */   
/*     */   private static final int TEST_ROOM = 4;
/*     */   
/*     */   private static final int BLOCKED = 5;
/*     */   
/*     */   private static final int ROOM_1x1 = 65536;
/*     */   
/*     */   private static final int ROOM_1x2 = 131072;
/*     */   
/*     */   private static final int ROOM_2x2 = 262144;
/*     */   
/*     */   private static final int ROOM_ORIGIN_FLAG = 1048576;
/*     */   
/*     */   private static final int ROOM_DOOR_FLAG = 2097152;
/*     */   
/*     */   private static final int ROOM_STAIRS_FLAG = 4194304;
/*     */   
/*     */   private static final int ROOM_CORRIDOR_FLAG = 8388608;
/*     */   
/*     */   private static final int ROOM_TYPE_MASK = 983040;
/*     */   
/*     */   private static final int ROOM_ID_MASK = 65535;
/*     */   
/*     */   private final RandomSource random;
/*     */   
/*     */   private final WoodlandMansionPieces.SimpleGrid baseGrid;
/*     */   
/*     */   private final WoodlandMansionPieces.SimpleGrid thirdFloorGrid;
/*     */   
/*     */   private final WoodlandMansionPieces.SimpleGrid[] floorRooms;
/*     */   
/*     */   private final int entranceX;
/*     */   
/*     */   private final int entranceY;
/*     */   
/*     */   public MansionGrid(RandomSource random) {
/* 714 */     this.random = random;
/* 716 */     int houseSize = 11;
/* 717 */     this.entranceX = 7;
/* 718 */     this.entranceY = 4;
/* 720 */     this.baseGrid = new WoodlandMansionPieces.SimpleGrid(11, 11, 5);
/* 721 */     this.baseGrid.set(this.entranceX, this.entranceY, this.entranceX + 1, this.entranceY + 1, 3);
/* 722 */     this.baseGrid.set(this.entranceX - 1, this.entranceY, this.entranceX - 1, this.entranceY + 1, 2);
/* 723 */     this.baseGrid.set(this.entranceX + 2, this.entranceY - 2, this.entranceX + 3, this.entranceY + 3, 5);
/* 724 */     this.baseGrid.set(this.entranceX + 1, this.entranceY - 2, this.entranceX + 1, this.entranceY - 1, 1);
/* 725 */     this.baseGrid.set(this.entranceX + 1, this.entranceY + 2, this.entranceX + 1, this.entranceY + 3, 1);
/* 726 */     this.baseGrid.set(this.entranceX - 1, this.entranceY - 1, 1);
/* 727 */     this.baseGrid.set(this.entranceX - 1, this.entranceY + 2, 1);
/* 729 */     this.baseGrid.set(0, 0, 11, 1, 5);
/* 730 */     this.baseGrid.set(0, 9, 11, 11, 5);
/* 732 */     recursiveCorridor(this.baseGrid, this.entranceX, this.entranceY - 2, Direction.WEST, 6);
/* 733 */     recursiveCorridor(this.baseGrid, this.entranceX, this.entranceY + 3, Direction.WEST, 6);
/* 734 */     recursiveCorridor(this.baseGrid, this.entranceX - 2, this.entranceY - 1, Direction.WEST, 3);
/* 735 */     recursiveCorridor(this.baseGrid, this.entranceX - 2, this.entranceY + 2, Direction.WEST, 3);
/* 736 */     while (cleanEdges(this.baseGrid));
/* 739 */     this.floorRooms = new WoodlandMansionPieces.SimpleGrid[3];
/* 740 */     this.floorRooms[0] = new WoodlandMansionPieces.SimpleGrid(11, 11, 5);
/* 741 */     this.floorRooms[1] = new WoodlandMansionPieces.SimpleGrid(11, 11, 5);
/* 742 */     this.floorRooms[2] = new WoodlandMansionPieces.SimpleGrid(11, 11, 5);
/* 743 */     identifyRooms(this.baseGrid, this.floorRooms[0]);
/* 744 */     identifyRooms(this.baseGrid, this.floorRooms[1]);
/* 747 */     this.floorRooms[0].set(this.entranceX + 1, this.entranceY, this.entranceX + 1, this.entranceY + 1, 8388608);
/* 748 */     this.floorRooms[1].set(this.entranceX + 1, this.entranceY, this.entranceX + 1, this.entranceY + 1, 8388608);
/* 750 */     this.thirdFloorGrid = new WoodlandMansionPieces.SimpleGrid(this.baseGrid.width, this.baseGrid.height, 5);
/* 751 */     setupThirdFloor();
/* 752 */     identifyRooms(this.thirdFloorGrid, this.floorRooms[2]);
/*     */   }
/*     */   
/*     */   public static boolean isHouse(WoodlandMansionPieces.SimpleGrid grid, int x, int y) {
/* 756 */     int value = grid.get(x, y);
/* 757 */     return (value == 1 || value == 2 || value == 3 || value == 4);
/*     */   }
/*     */   
/*     */   public boolean isRoomId(WoodlandMansionPieces.SimpleGrid grid, int x, int y, int floor, int roomId) {
/* 761 */     return ((this.floorRooms[floor].get(x, y) & 0xFFFF) == roomId);
/*     */   }
/*     */   
/*     */   public Direction get1x2RoomDirection(WoodlandMansionPieces.SimpleGrid grid, int x, int y, int floorNum, int roomId) {
/* 765 */     for (Direction direction : Direction.Plane.HORIZONTAL) {
/* 766 */       if (isRoomId(grid, x + direction.getStepX(), y + direction.getStepZ(), floorNum, roomId))
/* 767 */         return direction; 
/*     */     } 
/* 770 */     return null;
/*     */   }
/*     */   
/*     */   private void recursiveCorridor(WoodlandMansionPieces.SimpleGrid grid, int x, int y, Direction heading, int depth) {
/* 774 */     if (depth <= 0)
/*     */       return; 
/* 778 */     grid.set(x, y, 1);
/* 779 */     grid.setif(x + heading.getStepX(), y + heading.getStepZ(), 0, 1);
/* 781 */     for (int attempts = 0; attempts < 8; attempts++) {
/* 782 */       Direction nextDir = Direction.from2DDataValue(this.random.nextInt(4));
/* 783 */       if (nextDir != heading.getOpposite())
/* 786 */         if (nextDir != Direction.EAST || !this.random.nextBoolean()) {
/* 790 */           int nx = x + heading.getStepX();
/* 791 */           int ny = y + heading.getStepZ();
/* 792 */           if (grid.get(nx + nextDir.getStepX(), ny + nextDir.getStepZ()) == 0 && grid.get(nx + nextDir.getStepX() * 2, ny + nextDir.getStepZ() * 2) == 0) {
/* 793 */             recursiveCorridor(grid, x + heading.getStepX() + nextDir.getStepX(), y + heading.getStepZ() + nextDir.getStepZ(), nextDir, depth - 1);
/*     */             break;
/*     */           } 
/*     */         }  
/*     */     } 
/* 797 */     Direction cw = heading.getClockWise();
/* 798 */     Direction ccw = heading.getCounterClockWise();
/* 799 */     grid.setif(x + cw.getStepX(), y + cw.getStepZ(), 0, 2);
/* 800 */     grid.setif(x + ccw.getStepX(), y + ccw.getStepZ(), 0, 2);
/* 802 */     grid.setif(x + heading.getStepX() + cw.getStepX(), y + heading.getStepZ() + cw.getStepZ(), 0, 2);
/* 803 */     grid.setif(x + heading.getStepX() + ccw.getStepX(), y + heading.getStepZ() + ccw.getStepZ(), 0, 2);
/* 804 */     grid.setif(x + heading.getStepX() * 2, y + heading.getStepZ() * 2, 0, 2);
/* 805 */     grid.setif(x + cw.getStepX() * 2, y + cw.getStepZ() * 2, 0, 2);
/* 806 */     grid.setif(x + ccw.getStepX() * 2, y + ccw.getStepZ() * 2, 0, 2);
/*     */   }
/*     */   
/*     */   private boolean cleanEdges(WoodlandMansionPieces.SimpleGrid grid) {
/* 810 */     boolean touched = false;
/* 811 */     for (int y = 0; y < grid.height; y++) {
/* 812 */       for (int x = 0; x < grid.width; x++) {
/* 813 */         if (grid.get(x, y) == 0) {
/* 814 */           int directNeighbors = 0;
/* 815 */           directNeighbors += isHouse(grid, x + 1, y) ? 1 : 0;
/* 816 */           directNeighbors += isHouse(grid, x - 1, y) ? 1 : 0;
/* 817 */           directNeighbors += isHouse(grid, x, y + 1) ? 1 : 0;
/* 818 */           directNeighbors += isHouse(grid, x, y - 1) ? 1 : 0;
/* 820 */           if (directNeighbors >= 3) {
/* 822 */             grid.set(x, y, 2);
/* 823 */             touched = true;
/* 824 */           } else if (directNeighbors == 2) {
/* 826 */             int diagonalNeighbors = 0;
/* 827 */             diagonalNeighbors += isHouse(grid, x + 1, y + 1) ? 1 : 0;
/* 828 */             diagonalNeighbors += isHouse(grid, x - 1, y + 1) ? 1 : 0;
/* 829 */             diagonalNeighbors += isHouse(grid, x + 1, y - 1) ? 1 : 0;
/* 830 */             diagonalNeighbors += isHouse(grid, x - 1, y - 1) ? 1 : 0;
/* 831 */             if (diagonalNeighbors <= 1) {
/* 832 */               grid.set(x, y, 2);
/* 833 */               touched = true;
/*     */             } 
/*     */           } 
/*     */         } 
/*     */       } 
/*     */     } 
/* 839 */     return touched;
/*     */   }
/*     */   
/*     */   private static final class GridPos extends Record {
/*     */     private final int x;
/*     */     
/*     */     private final int y;
/*     */     
/*     */     private GridPos(int x, int y) {
/* 842 */       this.x = x;
/* 842 */       this.y = y;
/*     */     }
/*     */     
/*     */     public final String toString() {
/*     */       // Byte code:
/*     */       //   0: aload_0
/*     */       //   1: <illegal opcode> toString : (Lnet/minecraft/world/level/levelgen/structure/structures/WoodlandMansionPieces$MansionGrid$GridPos;)Ljava/lang/String;
/*     */       //   6: areturn
/*     */       // Line number table:
/*     */       //   Java source line number -> byte code offset
/*     */       //   #842	-> 0
/*     */       // Local variable table:
/*     */       //   start	length	slot	name	descriptor
/*     */       //   0	7	0	this	Lnet/minecraft/world/level/levelgen/structure/structures/WoodlandMansionPieces$MansionGrid$GridPos;
/*     */     }
/*     */     
/*     */     public final int hashCode() {
/*     */       // Byte code:
/*     */       //   0: aload_0
/*     */       //   1: <illegal opcode> hashCode : (Lnet/minecraft/world/level/levelgen/structure/structures/WoodlandMansionPieces$MansionGrid$GridPos;)I
/*     */       //   6: ireturn
/*     */       // Line number table:
/*     */       //   Java source line number -> byte code offset
/*     */       //   #842	-> 0
/*     */       // Local variable table:
/*     */       //   start	length	slot	name	descriptor
/*     */       //   0	7	0	this	Lnet/minecraft/world/level/levelgen/structure/structures/WoodlandMansionPieces$MansionGrid$GridPos;
/*     */     }
/*     */     
/*     */     public final boolean equals(Object o) {
/*     */       // Byte code:
/*     */       //   0: aload_0
/*     */       //   1: aload_1
/*     */       //   2: <illegal opcode> equals : (Lnet/minecraft/world/level/levelgen/structure/structures/WoodlandMansionPieces$MansionGrid$GridPos;Ljava/lang/Object;)Z
/*     */       //   7: ireturn
/*     */       // Line number table:
/*     */       //   Java source line number -> byte code offset
/*     */       //   #842	-> 0
/*     */       // Local variable table:
/*     */       //   start	length	slot	name	descriptor
/*     */       //   0	8	0	this	Lnet/minecraft/world/level/levelgen/structure/structures/WoodlandMansionPieces$MansionGrid$GridPos;
/*     */       //   0	8	1	o	Ljava/lang/Object;
/*     */     }
/*     */     
/*     */     public int x() {
/* 842 */       return this.x;
/*     */     }
/*     */     
/*     */     public int y() {
/* 842 */       return this.y;
/*     */     }
/*     */   }
/*     */   
/*     */   private void setupThirdFloor() {
/* 847 */     List<GridPos> potentialRooms = new ArrayList<>();
/* 848 */     WoodlandMansionPieces.SimpleGrid floor = this.floorRooms[1];
/* 849 */     for (int y = 0; y < this.thirdFloorGrid.height; y++) {
/* 850 */       for (int x = 0; x < this.thirdFloorGrid.width; x++) {
/* 851 */         int j = floor.get(x, y);
/* 852 */         int roomType = j & 0xF0000;
/* 853 */         if (roomType == 131072 && (j & 0x200000) == 2097152)
/* 854 */           potentialRooms.add(new GridPos(x, y)); 
/*     */       } 
/*     */     } 
/* 859 */     if (potentialRooms.isEmpty()) {
/* 861 */       this.thirdFloorGrid.set(0, 0, this.thirdFloorGrid.width, this.thirdFloorGrid.height, 5);
/*     */       return;
/*     */     } 
/* 865 */     GridPos roomPos = potentialRooms.get(this.random.nextInt(potentialRooms.size()));
/* 866 */     int roomData = floor.get(roomPos.x(), roomPos.y());
/* 867 */     floor.set(roomPos.x(), roomPos.y(), roomData | 0x400000);
/* 868 */     Direction roomDir = get1x2RoomDirection(this.baseGrid, roomPos.x(), roomPos.y(), 1, roomData & 0xFFFF);
/* 869 */     int roomEndX = roomPos.x() + roomDir.getStepX();
/* 870 */     int roomEndY = roomPos.y() + roomDir.getStepZ();
/* 872 */     for (int i = 0; i < this.thirdFloorGrid.height; i++) {
/* 873 */       for (int x = 0; x < this.thirdFloorGrid.width; x++) {
/* 874 */         if (!isHouse(this.baseGrid, x, i)) {
/* 875 */           this.thirdFloorGrid.set(x, i, 5);
/* 876 */         } else if (x == roomPos.x() && i == roomPos.y()) {
/* 877 */           this.thirdFloorGrid.set(x, i, 3);
/* 878 */         } else if (x == roomEndX && i == roomEndY) {
/* 879 */           this.thirdFloorGrid.set(x, i, 3);
/* 880 */           this.floorRooms[2].set(x, i, 8388608);
/*     */         } 
/*     */       } 
/*     */     } 
/* 885 */     List<Direction> potentialCorridors = new ArrayList<>();
/* 886 */     for (Direction direction : Direction.Plane.HORIZONTAL) {
/* 887 */       if (this.thirdFloorGrid.get(roomEndX + direction.getStepX(), roomEndY + direction.getStepZ()) == 0)
/* 888 */         potentialCorridors.add(direction); 
/*     */     } 
/* 892 */     if (potentialCorridors.isEmpty()) {
/* 894 */       this.thirdFloorGrid.set(0, 0, this.thirdFloorGrid.width, this.thirdFloorGrid.height, 5);
/* 895 */       floor.set(roomPos.x(), roomPos.y(), roomData);
/*     */       return;
/*     */     } 
/* 898 */     Direction corridorDir = potentialCorridors.get(this.random.nextInt(potentialCorridors.size()));
/* 899 */     recursiveCorridor(this.thirdFloorGrid, roomEndX + corridorDir.getStepX(), roomEndY + corridorDir.getStepZ(), corridorDir, 4);
/* 900 */     while (cleanEdges(this.thirdFloorGrid));
/*     */   }
/*     */   
/*     */   private void identifyRooms(WoodlandMansionPieces.SimpleGrid fromGrid, WoodlandMansionPieces.SimpleGrid roomGrid) {
/* 905 */     List<GridPos> roomPos = new ArrayList<>();
/* 906 */     for (int y = 0; y < fromGrid.height; y++) {
/* 907 */       for (int x = 0; x < fromGrid.width; x++) {
/* 908 */         if (fromGrid.get(x, y) == 2)
/* 909 */           roomPos.add(new GridPos(x, y)); 
/*     */       } 
/*     */     } 
/* 913 */     Util.shuffle(roomPos, this.random);
/* 915 */     int roomId = 10;
/* 916 */     for (GridPos pos : roomPos) {
/* 917 */       int x = pos.x();
/* 918 */       int i = pos.y();
/* 920 */       if (roomGrid.get(x, i) == 0) {
/* 921 */         int x0 = x;
/* 922 */         int x1 = x;
/* 923 */         int y0 = i;
/* 924 */         int y1 = i;
/* 925 */         int type = 65536;
/* 926 */         if (roomGrid.get(x + 1, i) == 0 && roomGrid.get(x, i + 1) == 0 && roomGrid.get(x + 1, i + 1) == 0 && fromGrid
/* 927 */           .get(x + 1, i) == 2 && fromGrid.get(x, i + 1) == 2 && fromGrid.get(x + 1, i + 1) == 2) {
/* 929 */           x1++;
/* 930 */           y1++;
/* 931 */           type = 262144;
/* 932 */         } else if (roomGrid.get(x - 1, i) == 0 && roomGrid.get(x, i + 1) == 0 && roomGrid.get(x - 1, i + 1) == 0 && fromGrid
/* 933 */           .get(x - 1, i) == 2 && fromGrid.get(x, i + 1) == 2 && fromGrid.get(x - 1, i + 1) == 2) {
/* 935 */           x0--;
/* 936 */           y1++;
/* 937 */           type = 262144;
/* 938 */         } else if (roomGrid.get(x - 1, i) == 0 && roomGrid.get(x, i - 1) == 0 && roomGrid.get(x - 1, i - 1) == 0 && fromGrid
/* 939 */           .get(x - 1, i) == 2 && fromGrid.get(x, i - 1) == 2 && fromGrid.get(x - 1, i - 1) == 2) {
/* 941 */           x0--;
/* 942 */           y0--;
/* 943 */           type = 262144;
/* 944 */         } else if (roomGrid.get(x + 1, i) == 0 && fromGrid.get(x + 1, i) == 2) {
/* 945 */           x1++;
/* 946 */           type = 131072;
/* 947 */         } else if (roomGrid.get(x, i + 1) == 0 && fromGrid.get(x, i + 1) == 2) {
/* 948 */           y1++;
/* 949 */           type = 131072;
/* 950 */         } else if (roomGrid.get(x - 1, i) == 0 && fromGrid.get(x - 1, i) == 2) {
/* 951 */           x0--;
/* 952 */           type = 131072;
/* 953 */         } else if (roomGrid.get(x, i - 1) == 0 && fromGrid.get(x, i - 1) == 2) {
/* 954 */           y0--;
/* 955 */           type = 131072;
/*     */         } 
/* 959 */         int doorX = this.random.nextBoolean() ? x0 : x1;
/* 960 */         int doorY = this.random.nextBoolean() ? y0 : y1;
/* 961 */         int doorFlag = 2097152;
/* 962 */         if (!fromGrid.edgesTo(doorX, doorY, 1)) {
/* 963 */           doorX = (doorX == x0) ? x1 : x0;
/* 964 */           doorY = (doorY == y0) ? y1 : y0;
/* 965 */           if (!fromGrid.edgesTo(doorX, doorY, 1)) {
/* 966 */             doorY = (doorY == y0) ? y1 : y0;
/* 967 */             if (!fromGrid.edgesTo(doorX, doorY, 1)) {
/* 968 */               doorX = (doorX == x0) ? x1 : x0;
/* 969 */               doorY = (doorY == y0) ? y1 : y0;
/* 970 */               if (!fromGrid.edgesTo(doorX, doorY, 1)) {
/* 972 */                 doorFlag = 0;
/* 973 */                 doorX = x0;
/* 974 */                 doorY = y0;
/*     */               } 
/*     */             } 
/*     */           } 
/*     */         } 
/* 979 */         for (int ry = y0; ry <= y1; ry++) {
/* 980 */           for (int rx = x0; rx <= x1; rx++) {
/* 981 */             if (rx == doorX && ry == doorY) {
/* 982 */               roomGrid.set(rx, ry, 0x100000 | doorFlag | type | roomId);
/*     */             } else {
/* 984 */               roomGrid.set(rx, ry, type | roomId);
/*     */             } 
/*     */           } 
/*     */         } 
/* 989 */         roomId++;
/*     */       } 
/*     */     } 
/*     */   }
/*     */ }


/* Location:              C:\Users\karol\AppData\Roaming\ModrinthApp\meta\versions\26.2-0.19.5\26.2-0.19.5.jar!\net\minecraft\world\level\levelgen\structure\structures\WoodlandMansionPieces$MansionGrid.class
 * Java compiler version: 25 (69.0)
 * JD-Core Version:       1.1.3
 */