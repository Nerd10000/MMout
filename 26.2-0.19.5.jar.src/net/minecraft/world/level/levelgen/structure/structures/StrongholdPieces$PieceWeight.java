/*    */ package net.minecraft.world.level.levelgen.structure.structures;
/*    */ 
/*    */ class PieceWeight {
/*    */   public final Class<? extends StrongholdPieces.StrongholdPiece> pieceClass;
/*    */   
/*    */   public final int weight;
/*    */   
/*    */   public int placeCount;
/*    */   
/*    */   public final int maxPlaceCount;
/*    */   
/*    */   public PieceWeight(Class<? extends StrongholdPieces.StrongholdPiece> pieceClass, int weight, int maxPlaceCount) {
/* 62 */     this.pieceClass = pieceClass;
/* 63 */     this.weight = weight;
/* 64 */     this.maxPlaceCount = maxPlaceCount;
/*    */   }
/*    */   
/*    */   public boolean doPlace(int depth) {
/* 68 */     return (this.maxPlaceCount == 0 || this.placeCount < this.maxPlaceCount);
/*    */   }
/*    */   
/*    */   public boolean isValid() {
/* 72 */     return (this.maxPlaceCount == 0 || this.placeCount < this.maxPlaceCount);
/*    */   }
/*    */ }


/* Location:              C:\Users\karol\AppData\Roaming\ModrinthApp\meta\versions\26.2-0.19.5\26.2-0.19.5.jar!\net\minecraft\world\level\levelgen\structure\structures\StrongholdPieces$PieceWeight.class
 * Java compiler version: 25 (69.0)
 * JD-Core Version:       1.1.3
 */