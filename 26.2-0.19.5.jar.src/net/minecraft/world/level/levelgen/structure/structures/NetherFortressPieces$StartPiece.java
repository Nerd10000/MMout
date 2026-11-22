/*     */ package net.minecraft.world.level.levelgen.structure.structures;
/*     */ 
/*     */ import com.google.common.collect.Lists;
/*     */ import java.util.ArrayList;
/*     */ import java.util.List;
/*     */ import net.minecraft.nbt.CompoundTag;
/*     */ import net.minecraft.util.RandomSource;
/*     */ import net.minecraft.world.level.levelgen.structure.StructurePiece;
/*     */ import net.minecraft.world.level.levelgen.structure.pieces.StructurePieceType;
/*     */ 
/*     */ public class StartPiece extends NetherFortressPieces.BridgeCrossing {
/*     */   private NetherFortressPieces.PieceWeight previousPiece;
/*     */   
/* 261 */   private final List<NetherFortressPieces.PieceWeight> availableBridgePieces = new ArrayList<>();
/*     */   
/* 262 */   private final List<NetherFortressPieces.PieceWeight> availableCastlePieces = new ArrayList<>();
/*     */   
/* 265 */   public final List<StructurePiece> pendingChildren = Lists.newArrayList();
/*     */   
/*     */   public StartPiece(RandomSource random, int west, int north) {
/* 268 */     super(west, north, getRandomHorizontalDirection(random));
/* 270 */     for (NetherFortressPieces.PieceWeight piece : NetherFortressPieces.BRIDGE_PIECE_WEIGHTS) {
/* 271 */       piece.placeCount = 0;
/* 272 */       this.availableBridgePieces.add(piece);
/*     */     } 
/* 275 */     for (NetherFortressPieces.PieceWeight piece : NetherFortressPieces.CASTLE_PIECE_WEIGHTS) {
/* 276 */       piece.placeCount = 0;
/* 277 */       this.availableCastlePieces.add(piece);
/*     */     } 
/*     */   }
/*     */   
/*     */   public StartPiece(CompoundTag tag) {
/* 282 */     super(StructurePieceType.NETHER_FORTRESS_START, tag);
/*     */   }
/*     */ }


/* Location:              C:\Users\karol\AppData\Roaming\ModrinthApp\meta\versions\26.2-0.19.5\26.2-0.19.5.jar!\net\minecraft\world\level\levelgen\structure\structures\NetherFortressPieces$StartPiece.class
 * Java compiler version: 25 (69.0)
 * JD-Core Version:       1.1.3
 */