/*     */ package net.minecraft.world.level.levelgen.structure.structures;
/*     */ 
/*     */ import com.google.common.collect.Lists;
/*     */ import java.util.List;
/*     */ import net.minecraft.core.BlockPos;
/*     */ import net.minecraft.nbt.CompoundTag;
/*     */ import net.minecraft.util.RandomSource;
/*     */ import net.minecraft.world.level.levelgen.structure.StructurePiece;
/*     */ import net.minecraft.world.level.levelgen.structure.pieces.StructurePieceType;
/*     */ 
/*     */ public class StartPiece extends StrongholdPieces.StairsDown {
/*     */   public StrongholdPieces.PieceWeight previousPiece;
/*     */   
/*     */   public StrongholdPieces.PortalRoom portalRoomPiece;
/*     */   
/* 535 */   public final List<StructurePiece> pendingChildren = Lists.newArrayList();
/*     */   
/*     */   public StartPiece(RandomSource random, int west, int north) {
/* 538 */     super(StructurePieceType.STRONGHOLD_START, 0, west, north, getRandomHorizontalDirection(random));
/*     */   }
/*     */   
/*     */   public StartPiece(CompoundTag tag) {
/* 542 */     super(StructurePieceType.STRONGHOLD_START, tag);
/*     */   }
/*     */   
/*     */   public BlockPos getLocatorPosition() {
/* 547 */     if (this.portalRoomPiece != null)
/* 548 */       return this.portalRoomPiece.getLocatorPosition(); 
/* 550 */     return super.getLocatorPosition();
/*     */   }
/*     */ }


/* Location:              C:\Users\karol\AppData\Roaming\ModrinthApp\meta\versions\26.2-0.19.5\26.2-0.19.5.jar!\net\minecraft\world\level\levelgen\structure\structures\StrongholdPieces$StartPiece.class
 * Java compiler version: 25 (69.0)
 * JD-Core Version:       1.1.3
 */