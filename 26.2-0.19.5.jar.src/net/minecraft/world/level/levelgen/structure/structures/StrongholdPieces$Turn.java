/*     */ package net.minecraft.world.level.levelgen.structure.structures;
/*     */ 
/*     */ import net.minecraft.nbt.CompoundTag;
/*     */ import net.minecraft.world.level.levelgen.structure.BoundingBox;
/*     */ import net.minecraft.world.level.levelgen.structure.pieces.StructurePieceType;
/*     */ 
/*     */ public abstract class Turn extends StrongholdPieces.StrongholdPiece {
/*     */   protected static final int WIDTH = 5;
/*     */   
/*     */   protected static final int HEIGHT = 5;
/*     */   
/*     */   protected static final int DEPTH = 5;
/*     */   
/*     */   protected Turn(StructurePieceType type, int genDepth, BoundingBox boundingBox) {
/* 761 */     super(type, genDepth, boundingBox);
/*     */   }
/*     */   
/*     */   public Turn(StructurePieceType type, CompoundTag tag) {
/* 765 */     super(type, tag);
/*     */   }
/*     */ }


/* Location:              C:\Users\karol\AppData\Roaming\ModrinthApp\meta\versions\26.2-0.19.5\26.2-0.19.5.jar!\net\minecraft\world\level\levelgen\structure\structures\StrongholdPieces$Turn.class
 * Java compiler version: 25 (69.0)
 * JD-Core Version:       1.1.3
 */