/*      */ package net.minecraft.world.level.levelgen.structure.structures;
/*      */ 
/*      */ import net.minecraft.util.RandomSource;
/*      */ 
/*      */ class SecondFloorRoomCollection extends WoodlandMansionPieces.FloorRoomCollection {
/*      */   public String get1x1(RandomSource random) {
/* 1112 */     return "1x1_b" + random.nextInt(5) + 1;
/*      */   }
/*      */   
/*      */   public String get1x1Secret(RandomSource random) {
/* 1117 */     return "1x1_as" + random.nextInt(4) + 1;
/*      */   }
/*      */   
/*      */   public String get1x2SideEntrance(RandomSource random, boolean isStairsRoom) {
/* 1122 */     if (isStairsRoom)
/* 1123 */       return "1x2_c_stairs"; 
/* 1125 */     return "1x2_c" + random.nextInt(4) + 1;
/*      */   }
/*      */   
/*      */   public String get1x2FrontEntrance(RandomSource random, boolean isStairsRoom) {
/* 1130 */     if (isStairsRoom)
/* 1131 */       return "1x2_d_stairs"; 
/* 1133 */     return "1x2_d" + random.nextInt(5) + 1;
/*      */   }
/*      */   
/*      */   public String get1x2Secret(RandomSource random) {
/* 1138 */     return "1x2_se" + random.nextInt(1) + 1;
/*      */   }
/*      */   
/*      */   public String get2x2(RandomSource random) {
/* 1143 */     return "2x2_b" + random.nextInt(5) + 1;
/*      */   }
/*      */   
/*      */   public String get2x2Secret(RandomSource random) {
/* 1148 */     return "2x2_s1";
/*      */   }
/*      */ }


/* Location:              C:\Users\karol\AppData\Roaming\ModrinthApp\meta\versions\26.2-0.19.5\26.2-0.19.5.jar!\net\minecraft\world\level\levelgen\structure\structures\WoodlandMansionPieces$SecondFloorRoomCollection.class
 * Java compiler version: 25 (69.0)
 * JD-Core Version:       1.1.3
 */