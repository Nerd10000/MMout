/*    */ package net.minecraft.world.level.levelgen.structure.pieces;
/*    */ 
/*    */ import com.google.common.collect.Lists;
/*    */ import java.util.List;
/*    */ import net.minecraft.util.RandomSource;
/*    */ import net.minecraft.world.level.levelgen.structure.BoundingBox;
/*    */ import net.minecraft.world.level.levelgen.structure.StructurePiece;
/*    */ import net.minecraft.world.level.levelgen.structure.StructurePieceAccessor;
/*    */ 
/*    */ public class StructurePiecesBuilder implements StructurePieceAccessor {
/* 13 */   private final List<StructurePiece> pieces = Lists.newArrayList();
/*    */   
/*    */   public void addPiece(StructurePiece piece) {
/* 17 */     this.pieces.add(piece);
/*    */   }
/*    */   
/*    */   public StructurePiece findCollisionPiece(BoundingBox box) {
/* 22 */     return StructurePiece.findCollisionPiece(this.pieces, box);
/*    */   }
/*    */   
/*    */   @Deprecated
/*    */   public void offsetPiecesVertically(int dy) {
/* 28 */     for (StructurePiece piece : this.pieces)
/* 29 */       piece.move(0, dy, 0); 
/*    */   }
/*    */   
/*    */   @Deprecated
/*    */   public int moveBelowSeaLevel(int seaLevel, int minY, RandomSource random, int offset) {
/* 36 */     int maxY = seaLevel - offset;
/* 39 */     BoundingBox boundingBox = getBoundingBox();
/* 40 */     int y1Pos = boundingBox.getYSpan() + minY + 1;
/* 42 */     if (y1Pos < maxY)
/* 43 */       y1Pos += random.nextInt(maxY - y1Pos); 
/* 47 */     int dy = y1Pos - boundingBox.maxY();
/* 48 */     offsetPiecesVertically(dy);
/* 49 */     return dy;
/*    */   }
/*    */   
/*    */   public void moveInsideHeights(RandomSource random, int lowestAllowed, int highestAllowed) {
/*    */     int y0Pos;
/* 54 */     BoundingBox boundingBox = getBoundingBox();
/* 55 */     int heightSpan = highestAllowed - lowestAllowed + 1 - boundingBox.getYSpan();
/* 58 */     if (heightSpan > 1) {
/* 59 */       y0Pos = lowestAllowed + random.nextInt(heightSpan);
/*    */     } else {
/* 61 */       y0Pos = lowestAllowed;
/*    */     } 
/* 65 */     int dy = y0Pos - boundingBox.minY();
/* 66 */     offsetPiecesVertically(dy);
/*    */   }
/*    */   
/*    */   public PiecesContainer build() {
/* 70 */     return new PiecesContainer(this.pieces);
/*    */   }
/*    */   
/*    */   public void clear() {
/* 75 */     this.pieces.clear();
/*    */   }
/*    */   
/*    */   public boolean isEmpty() {
/* 79 */     return this.pieces.isEmpty();
/*    */   }
/*    */   
/*    */   public BoundingBox getBoundingBox() {
/* 83 */     return StructurePiece.createBoundingBox(this.pieces.stream());
/*    */   }
/*    */ }


/* Location:              C:\Users\karol\AppData\Roaming\ModrinthApp\meta\versions\26.2-0.19.5\26.2-0.19.5.jar!\net\minecraft\world\level\levelgen\structure\pieces\StructurePiecesBuilder.class
 * Java compiler version: 25 (69.0)
 * JD-Core Version:       1.1.3
 */