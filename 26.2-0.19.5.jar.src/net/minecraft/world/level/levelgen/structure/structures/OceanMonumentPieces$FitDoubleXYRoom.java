/*      */ package net.minecraft.world.level.levelgen.structure.structures;
/*      */ 
/*      */ import net.minecraft.core.Direction;
/*      */ import net.minecraft.util.RandomSource;
/*      */ 
/*      */ class FitDoubleXYRoom implements OceanMonumentPieces.MonumentRoomFitter {
/*      */   public boolean fits(OceanMonumentPieces.RoomDefinition definition) {
/* 1912 */     if (definition.hasOpening[Direction.EAST.get3DDataValue()] && !(definition.connections[Direction.EAST.get3DDataValue()]).claimed && 
/* 1913 */       definition.hasOpening[Direction.UP.get3DDataValue()] && !(definition.connections[Direction.UP.get3DDataValue()]).claimed) {
/* 1914 */       OceanMonumentPieces.RoomDefinition east = definition.connections[Direction.EAST.get3DDataValue()];
/* 1916 */       return (east.hasOpening[Direction.UP.get3DDataValue()] && !(east.connections[Direction.UP.get3DDataValue()]).claimed);
/*      */     } 
/* 1919 */     return false;
/*      */   }
/*      */   
/*      */   public OceanMonumentPieces.OceanMonumentPiece create(Direction orientation, OceanMonumentPieces.RoomDefinition definition, RandomSource random) {
/* 1924 */     definition.claimed = true;
/* 1925 */     (definition.connections[Direction.EAST.get3DDataValue()]).claimed = true;
/* 1926 */     (definition.connections[Direction.UP.get3DDataValue()]).claimed = true;
/* 1927 */     ((definition.connections[Direction.EAST.get3DDataValue()]).connections[Direction.UP.get3DDataValue()]).claimed = true;
/* 1928 */     return new OceanMonumentPieces.OceanMonumentDoubleXYRoom(orientation, definition);
/*      */   }
/*      */ }


/* Location:              C:\Users\karol\AppData\Roaming\ModrinthApp\meta\versions\26.2-0.19.5\26.2-0.19.5.jar!\net\minecraft\world\level\levelgen\structure\structures\OceanMonumentPieces$FitDoubleXYRoom.class
 * Java compiler version: 25 (69.0)
 * JD-Core Version:       1.1.3
 */