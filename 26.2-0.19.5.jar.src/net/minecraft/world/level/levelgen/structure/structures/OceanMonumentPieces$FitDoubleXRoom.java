/*      */ package net.minecraft.world.level.levelgen.structure.structures;
/*      */ 
/*      */ import net.minecraft.core.Direction;
/*      */ import net.minecraft.util.RandomSource;
/*      */ 
/*      */ class FitDoubleXRoom implements OceanMonumentPieces.MonumentRoomFitter {
/*      */   public boolean fits(OceanMonumentPieces.RoomDefinition definition) {
/* 1880 */     return (definition.hasOpening[Direction.EAST.get3DDataValue()] && !(definition.connections[Direction.EAST.get3DDataValue()]).claimed);
/*      */   }
/*      */   
/*      */   public OceanMonumentPieces.OceanMonumentPiece create(Direction orientation, OceanMonumentPieces.RoomDefinition definition, RandomSource random) {
/* 1885 */     definition.claimed = true;
/* 1886 */     (definition.connections[Direction.EAST.get3DDataValue()]).claimed = true;
/* 1887 */     return new OceanMonumentPieces.OceanMonumentDoubleXRoom(orientation, definition);
/*      */   }
/*      */ }


/* Location:              C:\Users\karol\AppData\Roaming\ModrinthApp\meta\versions\26.2-0.19.5\26.2-0.19.5.jar!\net\minecraft\world\level\levelgen\structure\structures\OceanMonumentPieces$FitDoubleXRoom.class
 * Java compiler version: 25 (69.0)
 * JD-Core Version:       1.1.3
 */