/*      */ package net.minecraft.world.level.levelgen.structure.structures;
/*      */ 
/*      */ import net.minecraft.core.Direction;
/*      */ import net.minecraft.util.RandomSource;
/*      */ 
/*      */ class FitDoubleYZRoom implements OceanMonumentPieces.MonumentRoomFitter {
/*      */   public boolean fits(OceanMonumentPieces.RoomDefinition definition) {
/* 1935 */     if (definition.hasOpening[Direction.NORTH.get3DDataValue()] && !(definition.connections[Direction.NORTH.get3DDataValue()]).claimed && 
/* 1936 */       definition.hasOpening[Direction.UP.get3DDataValue()] && !(definition.connections[Direction.UP.get3DDataValue()]).claimed) {
/* 1937 */       OceanMonumentPieces.RoomDefinition north = definition.connections[Direction.NORTH.get3DDataValue()];
/* 1939 */       return (north.hasOpening[Direction.UP.get3DDataValue()] && !(north.connections[Direction.UP.get3DDataValue()]).claimed);
/*      */     } 
/* 1942 */     return false;
/*      */   }
/*      */   
/*      */   public OceanMonumentPieces.OceanMonumentPiece create(Direction orientation, OceanMonumentPieces.RoomDefinition definition, RandomSource random) {
/* 1947 */     definition.claimed = true;
/* 1948 */     (definition.connections[Direction.NORTH.get3DDataValue()]).claimed = true;
/* 1949 */     (definition.connections[Direction.UP.get3DDataValue()]).claimed = true;
/* 1950 */     ((definition.connections[Direction.NORTH.get3DDataValue()]).connections[Direction.UP.get3DDataValue()]).claimed = true;
/* 1951 */     return new OceanMonumentPieces.OceanMonumentDoubleYZRoom(orientation, definition);
/*      */   }
/*      */ }


/* Location:              C:\Users\karol\AppData\Roaming\ModrinthApp\meta\versions\26.2-0.19.5\26.2-0.19.5.jar!\net\minecraft\world\level\levelgen\structure\structures\OceanMonumentPieces$FitDoubleYZRoom.class
 * Java compiler version: 25 (69.0)
 * JD-Core Version:       1.1.3
 */