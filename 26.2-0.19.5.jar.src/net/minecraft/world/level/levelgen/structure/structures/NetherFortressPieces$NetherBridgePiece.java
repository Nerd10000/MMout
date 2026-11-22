/*     */ package net.minecraft.world.level.levelgen.structure.structures;
/*     */ 
/*     */ import java.util.List;
/*     */ import net.minecraft.core.Direction;
/*     */ import net.minecraft.nbt.CompoundTag;
/*     */ import net.minecraft.util.RandomSource;
/*     */ import net.minecraft.world.level.levelgen.structure.BoundingBox;
/*     */ import net.minecraft.world.level.levelgen.structure.StructurePiece;
/*     */ import net.minecraft.world.level.levelgen.structure.StructurePieceAccessor;
/*     */ import net.minecraft.world.level.levelgen.structure.pieces.StructurePieceSerializationContext;
/*     */ import net.minecraft.world.level.levelgen.structure.pieces.StructurePieceType;
/*     */ 
/*     */ abstract class NetherBridgePiece extends StructurePiece {
/*     */   protected NetherBridgePiece(StructurePieceType type, int genDepth, BoundingBox boundingBox) {
/* 119 */     super(type, genDepth, boundingBox);
/*     */   }
/*     */   
/*     */   public NetherBridgePiece(StructurePieceType type, CompoundTag tag) {
/* 123 */     super(type, tag);
/*     */   }
/*     */   
/*     */   protected void addAdditionalSaveData(StructurePieceSerializationContext context, CompoundTag tag) {}
/*     */   
/*     */   private int updatePieceWeight(List<NetherFortressPieces.PieceWeight> currentPieces) {
/* 131 */     boolean hasAnyPieces = false;
/* 132 */     int totalWeight = 0;
/* 133 */     for (NetherFortressPieces.PieceWeight piece : currentPieces) {
/* 134 */       if (piece.maxPlaceCount > 0 && piece.placeCount < piece.maxPlaceCount)
/* 135 */         hasAnyPieces = true; 
/* 137 */       totalWeight += piece.weight;
/*     */     } 
/* 139 */     return hasAnyPieces ? totalWeight : -1;
/*     */   }
/*     */   
/*     */   private NetherBridgePiece generatePiece(NetherFortressPieces.StartPiece startPiece, List<NetherFortressPieces.PieceWeight> currentPieces, StructurePieceAccessor structurePieceAccessor, RandomSource random, int footX, int footY, int footZ, Direction direction, int depth) {
/* 143 */     int totalWeight = updatePieceWeight(currentPieces);
/* 144 */     boolean doStuff = (totalWeight > 0 && depth <= 30);
/* 146 */     int numAttempts = 0;
/* 147 */     while (numAttempts < 5 && doStuff) {
/* 148 */       numAttempts++;
/* 150 */       int weightSelection = random.nextInt(totalWeight);
/* 151 */       for (NetherFortressPieces.PieceWeight piece : currentPieces) {
/* 152 */         weightSelection -= piece.weight;
/* 153 */         if (weightSelection < 0) {
/* 154 */           if (!piece.doPlace(depth) || (piece == startPiece.previousPiece && !piece.allowInRow))
/*     */             break; 
/* 158 */           NetherBridgePiece structurePiece = NetherFortressPieces.findAndCreateBridgePieceFactory(piece, structurePieceAccessor, random, footX, footY, footZ, direction, depth);
/* 159 */           if (structurePiece != null) {
/* 160 */             piece.placeCount++;
/* 161 */             startPiece.previousPiece = piece;
/* 163 */             if (!piece.isValid())
/* 164 */               currentPieces.remove(piece); 
/* 166 */             return structurePiece;
/*     */           } 
/*     */         } 
/*     */       } 
/*     */     } 
/* 171 */     return NetherFortressPieces.BridgeEndFiller.createPiece(structurePieceAccessor, random, footX, footY, footZ, direction, depth);
/*     */   }
/*     */   
/*     */   private StructurePiece generateAndAddPiece(NetherFortressPieces.StartPiece startPiece, StructurePieceAccessor structurePieceAccessor, RandomSource random, int footX, int footY, int footZ, Direction direction, int depth, boolean isCastle) {
/* 175 */     if (Math.abs(footX - startPiece.getBoundingBox().minX()) > 112 || Math.abs(footZ - startPiece.getBoundingBox().minZ()) > 112)
/* 176 */       return NetherFortressPieces.BridgeEndFiller.createPiece(structurePieceAccessor, random, footX, footY, footZ, direction, depth); 
/* 178 */     List<NetherFortressPieces.PieceWeight> availablePieces = startPiece.availableBridgePieces;
/* 179 */     if (isCastle)
/* 180 */       availablePieces = startPiece.availableCastlePieces; 
/* 182 */     StructurePiece newPiece = generatePiece(startPiece, availablePieces, structurePieceAccessor, random, footX, footY, footZ, direction, depth + 1);
/* 183 */     if (newPiece != null) {
/* 184 */       structurePieceAccessor.addPiece(newPiece);
/* 185 */       startPiece.pendingChildren.add(newPiece);
/*     */     } 
/* 187 */     return newPiece;
/*     */   }
/*     */   
/*     */   protected StructurePiece generateChildForward(NetherFortressPieces.StartPiece startPiece, StructurePieceAccessor structurePieceAccessor, RandomSource random, int xOff, int yOff, boolean isCastle) {
/* 191 */     Direction orientation = getOrientation();
/* 192 */     if (orientation != null)
/* 193 */       switch (NetherFortressPieces.null.$SwitchMap$net$minecraft$core$Direction[orientation.ordinal()]) {
/*     */         case 1:
/* 195 */           return generateAndAddPiece(startPiece, structurePieceAccessor, random, this.boundingBox.minX() + xOff, this.boundingBox.minY() + yOff, this.boundingBox.minZ() - 1, orientation, getGenDepth(), isCastle);
/*     */         case 2:
/* 198 */           return generateAndAddPiece(startPiece, structurePieceAccessor, random, this.boundingBox.minX() + xOff, this.boundingBox.minY() + yOff, this.boundingBox.maxZ() + 1, orientation, getGenDepth(), isCastle);
/*     */         case 3:
/* 201 */           return generateAndAddPiece(startPiece, structurePieceAccessor, random, this.boundingBox.minX() - 1, this.boundingBox.minY() + yOff, this.boundingBox.minZ() + xOff, orientation, getGenDepth(), isCastle);
/*     */         case 4:
/* 204 */           return generateAndAddPiece(startPiece, structurePieceAccessor, random, this.boundingBox.maxX() + 1, this.boundingBox.minY() + yOff, this.boundingBox.minZ() + xOff, orientation, getGenDepth(), isCastle);
/*     */       }  
/* 208 */     return null;
/*     */   }
/*     */   
/*     */   protected StructurePiece generateChildLeft(NetherFortressPieces.StartPiece startPiece, StructurePieceAccessor structurePieceAccessor, RandomSource random, int yOff, int zOff, boolean isCastle) {
/* 212 */     Direction orientation = getOrientation();
/* 213 */     if (orientation != null)
/* 214 */       switch (NetherFortressPieces.null.$SwitchMap$net$minecraft$core$Direction[orientation.ordinal()]) {
/*     */         case 1:
/* 216 */           return generateAndAddPiece(startPiece, structurePieceAccessor, random, this.boundingBox.minX() - 1, this.boundingBox.minY() + yOff, this.boundingBox.minZ() + zOff, Direction.WEST, getGenDepth(), isCastle);
/*     */         case 2:
/* 219 */           return generateAndAddPiece(startPiece, structurePieceAccessor, random, this.boundingBox.minX() - 1, this.boundingBox.minY() + yOff, this.boundingBox.minZ() + zOff, Direction.WEST, getGenDepth(), isCastle);
/*     */         case 3:
/* 222 */           return generateAndAddPiece(startPiece, structurePieceAccessor, random, this.boundingBox.minX() + zOff, this.boundingBox.minY() + yOff, this.boundingBox.minZ() - 1, Direction.NORTH, getGenDepth(), isCastle);
/*     */         case 4:
/* 225 */           return generateAndAddPiece(startPiece, structurePieceAccessor, random, this.boundingBox.minX() + zOff, this.boundingBox.minY() + yOff, this.boundingBox.minZ() - 1, Direction.NORTH, getGenDepth(), isCastle);
/*     */       }  
/* 229 */     return null;
/*     */   }
/*     */   
/*     */   protected StructurePiece generateChildRight(NetherFortressPieces.StartPiece startPiece, StructurePieceAccessor structurePieceAccessor, RandomSource random, int yOff, int zOff, boolean isCastle) {
/* 233 */     Direction orientation = getOrientation();
/* 234 */     if (orientation != null)
/* 235 */       switch (NetherFortressPieces.null.$SwitchMap$net$minecraft$core$Direction[orientation.ordinal()]) {
/*     */         case 1:
/* 237 */           return generateAndAddPiece(startPiece, structurePieceAccessor, random, this.boundingBox.maxX() + 1, this.boundingBox.minY() + yOff, this.boundingBox.minZ() + zOff, Direction.EAST, getGenDepth(), isCastle);
/*     */         case 2:
/* 240 */           return generateAndAddPiece(startPiece, structurePieceAccessor, random, this.boundingBox.maxX() + 1, this.boundingBox.minY() + yOff, this.boundingBox.minZ() + zOff, Direction.EAST, getGenDepth(), isCastle);
/*     */         case 3:
/* 243 */           return generateAndAddPiece(startPiece, structurePieceAccessor, random, this.boundingBox.minX() + zOff, this.boundingBox.minY() + yOff, this.boundingBox.maxZ() + 1, Direction.SOUTH, getGenDepth(), isCastle);
/*     */         case 4:
/* 246 */           return generateAndAddPiece(startPiece, structurePieceAccessor, random, this.boundingBox.minX() + zOff, this.boundingBox.minY() + yOff, this.boundingBox.maxZ() + 1, Direction.SOUTH, getGenDepth(), isCastle);
/*     */       }  
/* 250 */     return null;
/*     */   }
/*     */   
/*     */   protected static boolean isOkBox(BoundingBox box) {
/* 254 */     return (box.minY() > 10);
/*     */   }
/*     */ }


/* Location:              C:\Users\karol\AppData\Roaming\ModrinthApp\meta\versions\26.2-0.19.5\26.2-0.19.5.jar!\net\minecraft\world\level\levelgen\structure\structures\NetherFortressPieces$NetherBridgePiece.class
 * Java compiler version: 25 (69.0)
 * JD-Core Version:       1.1.3
 */