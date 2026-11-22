/*    */ package net.minecraft.world.level.levelgen.structure.structures;
/*    */ 
/*    */ class null extends StrongholdPieces.PieceWeight {
/*    */   null(Class<? extends StrongholdPieces.StrongholdPiece> pieceClass, int weight, int maxPlaceCount) {
/* 86 */     super(pieceClass, weight, maxPlaceCount);
/*    */   }
/*    */   
/*    */   public boolean doPlace(int depth) {
/* 89 */     return (super.doPlace(depth) && depth > 4);
/*    */   }
/*    */ }


/* Location:              C:\Users\karol\AppData\Roaming\ModrinthApp\meta\versions\26.2-0.19.5\26.2-0.19.5.jar!\net\minecraft\world\level\levelgen\structure\structures\StrongholdPieces$1.class
 * Java compiler version: 25 (69.0)
 * JD-Core Version:       1.1.3
 */