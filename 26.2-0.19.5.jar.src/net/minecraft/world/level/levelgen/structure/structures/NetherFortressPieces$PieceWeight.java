/*    */ package net.minecraft.world.level.levelgen.structure.structures;
/*    */ 
/*    */ class PieceWeight {
/*    */   public final Class<? extends NetherFortressPieces.NetherBridgePiece> pieceClass;
/*    */   
/*    */   public final int weight;
/*    */   
/*    */   public int placeCount;
/*    */   
/*    */   public final int maxPlaceCount;
/*    */   
/*    */   public final boolean allowInRow;
/*    */   
/*    */   public PieceWeight(Class<? extends NetherFortressPieces.NetherBridgePiece> pieceClass, int weight, int maxPlaceCount, boolean allowInRow) {
/* 46 */     this.pieceClass = pieceClass;
/* 47 */     this.weight = weight;
/* 48 */     this.maxPlaceCount = maxPlaceCount;
/* 49 */     this.allowInRow = allowInRow;
/*    */   }
/*    */   
/*    */   public PieceWeight(Class<? extends NetherFortressPieces.NetherBridgePiece> pieceClass, int weight, int maxPlaceCount) {
/* 53 */     this(pieceClass, weight, maxPlaceCount, false);
/*    */   }
/*    */   
/*    */   public boolean doPlace(int depth) {
/* 57 */     return (this.maxPlaceCount == 0 || this.placeCount < this.maxPlaceCount);
/*    */   }
/*    */   
/*    */   public boolean isValid() {
/* 61 */     return (this.maxPlaceCount == 0 || this.placeCount < this.maxPlaceCount);
/*    */   }
/*    */ }


/* Location:              C:\Users\karol\AppData\Roaming\ModrinthApp\meta\versions\26.2-0.19.5\26.2-0.19.5.jar!\net\minecraft\world\level\levelgen\structure\structures\NetherFortressPieces$PieceWeight.class
 * Java compiler version: 25 (69.0)
 * JD-Core Version:       1.1.3
 */