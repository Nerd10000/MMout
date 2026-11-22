/*      */ package net.minecraft.world.level.levelgen.structure.structures;
/*      */ 
/*      */ import net.minecraft.core.Direction;
/*      */ import net.minecraft.util.RandomSource;
/*      */ 
/*      */ class FitDoubleZRoom implements OceanMonumentPieces.MonumentRoomFitter {
/*      */   public boolean fits(OceanMonumentPieces.RoomDefinition definition) {
/* 1894 */     return (definition.hasOpening[Direction.NORTH.get3DDataValue()] && !(definition.connections[Direction.NORTH.get3DDataValue()]).claimed);
/*      */   }
/*      */   
/*      */   public OceanMonumentPieces.OceanMonumentPiece create(Direction orientation, OceanMonumentPieces.RoomDefinition definition, RandomSource random) {
/* 1899 */     OceanMonumentPieces.RoomDefinition source = definition;
/* 1900 */     if (!definition.hasOpening[Direction.NORTH.get3DDataValue()] || (definition.connections[Direction.NORTH.get3DDataValue()]).claimed)
/* 1901 */       source = definition.connections[Direction.SOUTH.get3DDataValue()]; 
/* 1903 */     source.claimed = true;
/* 1904 */     (source.connections[Direction.NORTH.get3DDataValue()]).claimed = true;
/* 1905 */     return new OceanMonumentPieces.OceanMonumentDoubleZRoom(orientation, source);
/*      */   }
/*      */ }


/* Location:              C:\Users\karol\AppData\Roaming\ModrinthApp\meta\versions\26.2-0.19.5\26.2-0.19.5.jar!\net\minecraft\world\level\levelgen\structure\structures\OceanMonumentPieces$FitDoubleZRoom.class
 * Java compiler version: 25 (69.0)
 * JD-Core Version:       1.1.3
 */