/*      */ package net.minecraft.world.level.levelgen.structure.structures;
/*      */ 
/*      */ import net.minecraft.core.Direction;
/*      */ import net.minecraft.util.RandomSource;
/*      */ 
/*      */ class FitSimpleTopRoom implements OceanMonumentPieces.MonumentRoomFitter {
/*      */   public boolean fits(OceanMonumentPieces.RoomDefinition definition) {
/* 1853 */     return (!definition.hasOpening[Direction.WEST.get3DDataValue()] && !definition.hasOpening[Direction.EAST.get3DDataValue()] && !definition.hasOpening[Direction.NORTH.get3DDataValue()] && !definition.hasOpening[Direction.SOUTH.get3DDataValue()] && !definition.hasOpening[Direction.UP.get3DDataValue()]);
/*      */   }
/*      */   
/*      */   public OceanMonumentPieces.OceanMonumentPiece create(Direction orientation, OceanMonumentPieces.RoomDefinition definition, RandomSource random) {
/* 1858 */     definition.claimed = true;
/* 1859 */     return new OceanMonumentPieces.OceanMonumentSimpleTopRoom(orientation, definition);
/*      */   }
/*      */ }


/* Location:              C:\Users\karol\AppData\Roaming\ModrinthApp\meta\versions\26.2-0.19.5\26.2-0.19.5.jar!\net\minecraft\world\level\levelgen\structure\structures\OceanMonumentPieces$FitSimpleTopRoom.class
 * Java compiler version: 25 (69.0)
 * JD-Core Version:       1.1.3
 */