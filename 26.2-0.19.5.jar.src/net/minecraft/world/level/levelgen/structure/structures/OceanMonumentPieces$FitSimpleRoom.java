/*      */ package net.minecraft.world.level.levelgen.structure.structures;
/*      */ 
/*      */ import net.minecraft.core.Direction;
/*      */ import net.minecraft.util.RandomSource;
/*      */ 
/*      */ class FitSimpleRoom implements OceanMonumentPieces.MonumentRoomFitter {
/*      */   public boolean fits(OceanMonumentPieces.RoomDefinition definition) {
/* 1840 */     return true;
/*      */   }
/*      */   
/*      */   public OceanMonumentPieces.OceanMonumentPiece create(Direction orientation, OceanMonumentPieces.RoomDefinition definition, RandomSource random) {
/* 1845 */     definition.claimed = true;
/* 1846 */     return new OceanMonumentPieces.OceanMonumentSimpleRoom(orientation, definition, random);
/*      */   }
/*      */ }


/* Location:              C:\Users\karol\AppData\Roaming\ModrinthApp\meta\versions\26.2-0.19.5\26.2-0.19.5.jar!\net\minecraft\world\level\levelgen\structure\structures\OceanMonumentPieces$FitSimpleRoom.class
 * Java compiler version: 25 (69.0)
 * JD-Core Version:       1.1.3
 */