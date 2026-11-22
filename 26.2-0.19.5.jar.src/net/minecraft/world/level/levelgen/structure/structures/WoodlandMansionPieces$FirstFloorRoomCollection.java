/*      */ package net.minecraft.world.level.levelgen.structure.structures;
/*      */ 
/*      */ import net.minecraft.util.RandomSource;
/*      */ 
/*      */ class FirstFloorRoomCollection extends WoodlandMansionPieces.FloorRoomCollection {
/*      */   public String get1x1(RandomSource random) {
/* 1075 */     return "1x1_a" + random.nextInt(5) + 1;
/*      */   }
/*      */   
/*      */   public String get1x1Secret(RandomSource random) {
/* 1080 */     return "1x1_as" + random.nextInt(4) + 1;
/*      */   }
/*      */   
/*      */   public String get1x2SideEntrance(RandomSource random, boolean isStairsRoom) {
/* 1085 */     return "1x2_a" + random.nextInt(9) + 1;
/*      */   }
/*      */   
/*      */   public String get1x2FrontEntrance(RandomSource random, boolean isStairsRoom) {
/* 1090 */     return "1x2_b" + random.nextInt(5) + 1;
/*      */   }
/*      */   
/*      */   public String get1x2Secret(RandomSource random) {
/* 1095 */     return "1x2_s" + random.nextInt(2) + 1;
/*      */   }
/*      */   
/*      */   public String get2x2(RandomSource random) {
/* 1100 */     return "2x2_a" + random.nextInt(4) + 1;
/*      */   }
/*      */   
/*      */   public String get2x2Secret(RandomSource random) {
/* 1105 */     return "2x2_s1";
/*      */   }
/*      */ }


/* Location:              C:\Users\karol\AppData\Roaming\ModrinthApp\meta\versions\26.2-0.19.5\26.2-0.19.5.jar!\net\minecraft\world\level\levelgen\structure\structures\WoodlandMansionPieces$FirstFloorRoomCollection.class
 * Java compiler version: 25 (69.0)
 * JD-Core Version:       1.1.3
 */