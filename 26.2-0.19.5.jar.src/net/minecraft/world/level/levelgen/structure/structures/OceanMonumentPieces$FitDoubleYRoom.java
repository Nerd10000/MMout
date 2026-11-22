/*      */ package net.minecraft.world.level.levelgen.structure.structures;
/*      */ 
/*      */ import net.minecraft.core.Direction;
/*      */ import net.minecraft.util.RandomSource;
/*      */ 
/*      */ class FitDoubleYRoom implements OceanMonumentPieces.MonumentRoomFitter {
/*      */   public boolean fits(OceanMonumentPieces.RoomDefinition definition) {
/* 1866 */     return (definition.hasOpening[Direction.UP.get3DDataValue()] && !(definition.connections[Direction.UP.get3DDataValue()]).claimed);
/*      */   }
/*      */   
/*      */   public OceanMonumentPieces.OceanMonumentPiece create(Direction orientation, OceanMonumentPieces.RoomDefinition definition, RandomSource random) {
/* 1871 */     definition.claimed = true;
/* 1872 */     (definition.connections[Direction.UP.get3DDataValue()]).claimed = true;
/* 1873 */     return new OceanMonumentPieces.OceanMonumentDoubleYRoom(orientation, definition);
/*      */   }
/*      */ }


/* Location:              C:\Users\karol\AppData\Roaming\ModrinthApp\meta\versions\26.2-0.19.5\26.2-0.19.5.jar!\net\minecraft\world\level\levelgen\structure\structures\OceanMonumentPieces$FitDoubleYRoom.class
 * Java compiler version: 25 (69.0)
 * JD-Core Version:       1.1.3
 */