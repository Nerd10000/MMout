/*     */ package net.minecraft.world.level.levelgen;
/*     */ 
/*     */ import com.google.common.annotations.VisibleForTesting;
/*     */ import java.util.ArrayList;
/*     */ import java.util.Arrays;
/*     */ import java.util.List;
/*     */ import net.minecraft.core.BlockPos;
/*     */ import net.minecraft.util.Mth;
/*     */ import net.minecraft.util.Util;
/*     */ import net.minecraft.world.level.ChunkPos;
/*     */ import net.minecraft.world.level.StructureManager;
/*     */ import net.minecraft.world.level.levelgen.structure.BoundingBox;
/*     */ import net.minecraft.world.level.levelgen.structure.PoolElementStructurePiece;
/*     */ import net.minecraft.world.level.levelgen.structure.Structure;
/*     */ import net.minecraft.world.level.levelgen.structure.StructurePiece;
/*     */ import net.minecraft.world.level.levelgen.structure.StructureStart;
/*     */ import net.minecraft.world.level.levelgen.structure.TerrainAdjustment;
/*     */ import net.minecraft.world.level.levelgen.structure.pools.JigsawJunction;
/*     */ import net.minecraft.world.level.levelgen.structure.pools.StructureTemplatePool;
/*     */ 
/*     */ public class Beardifier implements DensityFunctions.BeardifierOrMarker {
/*     */   public static final int BEARD_KERNEL_RADIUS = 12;
/*     */   
/*     */   private static final int BEARD_KERNEL_SIZE = 24;
/*     */   
/*     */   private static final float[] BEARD_KERNEL;
/*     */   
/*     */   static {
/*  26 */     BEARD_KERNEL = (float[])Util.make(new float[13824], kernel -> {
/*     */           for (int zi = 0; zi < 24; zi++) {
/*     */             for (int xi = 0; xi < 24; xi++) {
/*     */               for (int yi = 0; yi < 24; yi++)
/*     */                 kernel[zi * 24 * 24 + xi * 24 + yi] = (float)computeBeardContribution(xi - 12, yi - 12, zi - 12); 
/*     */             } 
/*     */           } 
/*     */         });
/*     */   }
/*     */   
/*     */   @VisibleForTesting
/*     */   public static final class Rigid extends Record {
/*     */     private final BoundingBox box;
/*     */     
/*     */     private final TerrainAdjustment terrainAdjustment;
/*     */     
/*     */     private final int groundLevelDelta;
/*     */     
/*     */     public int groundLevelDelta() {
/*  36 */       return this.groundLevelDelta;
/*     */     }
/*     */     
/*     */     public TerrainAdjustment terrainAdjustment() {
/*  36 */       return this.terrainAdjustment;
/*     */     }
/*     */     
/*     */     public BoundingBox box() {
/*  36 */       return this.box;
/*     */     }
/*     */     
/*     */     public final boolean equals(Object o) {
/*     */       // Byte code:
/*     */       //   0: aload_0
/*     */       //   1: aload_1
/*     */       //   2: <illegal opcode> equals : (Lnet/minecraft/world/level/levelgen/Beardifier$Rigid;Ljava/lang/Object;)Z
/*     */       //   7: ireturn
/*     */       // Line number table:
/*     */       //   Java source line number -> byte code offset
/*     */       //   #36	-> 0
/*     */       // Local variable table:
/*     */       //   start	length	slot	name	descriptor
/*     */       //   0	8	0	this	Lnet/minecraft/world/level/levelgen/Beardifier$Rigid;
/*     */       //   0	8	1	o	Ljava/lang/Object;
/*     */     }
/*     */     
/*     */     public final int hashCode() {
/*     */       // Byte code:
/*     */       //   0: aload_0
/*     */       //   1: <illegal opcode> hashCode : (Lnet/minecraft/world/level/levelgen/Beardifier$Rigid;)I
/*     */       //   6: ireturn
/*     */       // Line number table:
/*     */       //   Java source line number -> byte code offset
/*     */       //   #36	-> 0
/*     */       // Local variable table:
/*     */       //   start	length	slot	name	descriptor
/*     */       //   0	7	0	this	Lnet/minecraft/world/level/levelgen/Beardifier$Rigid;
/*     */     }
/*     */     
/*     */     public final String toString() {
/*     */       // Byte code:
/*     */       //   0: aload_0
/*     */       //   1: <illegal opcode> toString : (Lnet/minecraft/world/level/levelgen/Beardifier$Rigid;)Ljava/lang/String;
/*     */       //   6: areturn
/*     */       // Line number table:
/*     */       //   Java source line number -> byte code offset
/*     */       //   #36	-> 0
/*     */       // Local variable table:
/*     */       //   start	length	slot	name	descriptor
/*     */       //   0	7	0	this	Lnet/minecraft/world/level/levelgen/Beardifier$Rigid;
/*     */     }
/*     */     
/*     */     public Rigid(BoundingBox box, TerrainAdjustment terrainAdjustment, int groundLevelDelta) {
/*  37 */       this.box = box;
/*  37 */       this.terrainAdjustment = terrainAdjustment;
/*  37 */       this.groundLevelDelta = groundLevelDelta;
/*     */     }
/*     */   }
/*     */   
/*  39 */   public static final Beardifier EMPTY = new Beardifier(List.of(), List.of(), null);
/*     */   
/*     */   private final List<Rigid> pieces;
/*     */   
/*     */   private final List<JigsawJunction> junctions;
/*     */   
/*     */   private final BoundingBox affectedBox;
/*     */   
/*     */   public static Beardifier forStructuresInChunk(StructureManager structureManager, ChunkPos chunkPos) {
/*  47 */     List<StructureStart> structureStarts = structureManager.startsForStructure(chunkPos, s -> (s.terrainAdaptation() != TerrainAdjustment.NONE));
/*  48 */     if (structureStarts.isEmpty())
/*  49 */       return EMPTY; 
/*  52 */     int chunkStartBlockX = chunkPos.getMinBlockX();
/*  53 */     int chunkStartBlockZ = chunkPos.getMinBlockZ();
/*  55 */     List<Rigid> rigids = new ArrayList<>();
/*  56 */     List<JigsawJunction> junctions = new ArrayList<>();
/*  58 */     BoundingBox anyPieceBoundingBox = null;
/*  61 */     for (StructureStart start : structureStarts) {
/*  62 */       TerrainAdjustment terrainAdjustment = start.getStructure().terrainAdaptation();
/*  63 */       for (StructurePiece piece : start.getPieces()) {
/*  64 */         if (!piece.isCloseToChunk(chunkPos, 12))
/*     */           continue; 
/*  68 */         if (piece instanceof PoolElementStructurePiece) {
/*  68 */           PoolElementStructurePiece poolPiece = (PoolElementStructurePiece)piece;
/*  69 */           StructureTemplatePool.Projection projection = poolPiece.getElement().getProjection();
/*  70 */           if (projection == StructureTemplatePool.Projection.RIGID) {
/*  71 */             rigids.add(new Rigid(poolPiece.getBoundingBox(), terrainAdjustment, poolPiece.getGroundLevelDelta()));
/*  72 */             anyPieceBoundingBox = includeBoundingBox(anyPieceBoundingBox, piece.getBoundingBox());
/*     */           } 
/*  75 */           for (JigsawJunction junction : poolPiece.getJunctions()) {
/*  76 */             int junctionX = junction.getSourceX();
/*  77 */             int junctionZ = junction.getSourceZ();
/*  78 */             if (junctionX <= chunkStartBlockX - 12 || junctionZ <= chunkStartBlockZ - 12 || junctionX >= chunkStartBlockX + 15 + 12 || junctionZ >= chunkStartBlockZ + 15 + 12)
/*     */               continue; 
/*  85 */             junctions.add(junction);
/*  86 */             BoundingBox junctionBox = new BoundingBox(new BlockPos(junctionX, junction.getSourceGroundY(), junctionZ));
/*  87 */             anyPieceBoundingBox = includeBoundingBox(anyPieceBoundingBox, junctionBox);
/*     */           } 
/*     */           continue;
/*     */         } 
/*  90 */         rigids.add(new Rigid(piece.getBoundingBox(), terrainAdjustment, 0));
/*  91 */         anyPieceBoundingBox = includeBoundingBox(anyPieceBoundingBox, piece.getBoundingBox());
/*     */       } 
/*     */     } 
/*  96 */     if (anyPieceBoundingBox == null)
/*  97 */       return EMPTY; 
/* 100 */     BoundingBox affectedBox = anyPieceBoundingBox.inflatedBy(24);
/* 101 */     return new Beardifier(List.copyOf(rigids), List.copyOf(junctions), affectedBox);
/*     */   }
/*     */   
/*     */   private static BoundingBox includeBoundingBox(BoundingBox encompassingBox, BoundingBox newBox) {
/* 105 */     if (encompassingBox == null)
/* 106 */       return newBox; 
/* 108 */     return BoundingBox.encapsulating(encompassingBox, newBox);
/*     */   }
/*     */   
/*     */   @VisibleForTesting
/*     */   public Beardifier(List<Rigid> pieces, List<JigsawJunction> junctions, BoundingBox affectedBox) {
/* 113 */     this.pieces = pieces;
/* 114 */     this.junctions = junctions;
/* 115 */     this.affectedBox = affectedBox;
/*     */   }
/*     */   
/*     */   public void fillArray(double[] output, DensityFunction.ContextProvider contextProvider) {
/* 120 */     if (this.affectedBox == null) {
/* 121 */       Arrays.fill(output, 0.0D);
/*     */     } else {
/* 123 */       super.fillArray(output, contextProvider);
/*     */     } 
/*     */   }
/*     */   
/*     */   public double compute(DensityFunction.FunctionContext context) {
/* 129 */     if (this.affectedBox == null)
/* 130 */       return 0.0D; 
/* 133 */     int blockX = context.blockX();
/* 134 */     int blockY = context.blockY();
/* 135 */     int blockZ = context.blockZ();
/* 136 */     if (!this.affectedBox.isInside(blockX, blockY, blockZ))
/* 137 */       return 0.0D; 
/* 140 */     double noiseValue = 0.0D;
/* 142 */     for (Rigid rigid : this.pieces) {
/* 143 */       BoundingBox box = rigid.box();
/* 144 */       int groundLevelDelta = rigid.groundLevelDelta();
/* 148 */       int dx = Math.max(0, Math.max(box.minX() - blockX, blockX - box.maxX()));
/* 149 */       int dz = Math.max(0, Math.max(box.minZ() - blockZ, blockZ - box.maxZ()));
/* 151 */       int groundY = box.minY() + groundLevelDelta;
/* 152 */       int dyToGround = blockY - groundY;
/* 153 */       switch (rigid.terrainAdjustment()) {
/*     */         default:
/* 153 */           throw new MatchException(null, null);
/*     */         case NONE:
/*     */         
/*     */         case BURY:
/*     */         case BEARD_THIN:
/*     */         
/*     */         case BEARD_BOX:
/*     */         
/*     */         case ENCAPSULATE:
/*     */           break;
/*     */       } 
/* 153 */       int dy = 
/*     */         
/* 157 */         Math.max(0, Math.max(box.minY() - blockY, blockY - box.maxY()));
/* 160 */       switch (rigid.terrainAdjustment()) {
/*     */         default:
/* 160 */           throw new MatchException(null, null);
/*     */         case NONE:
/*     */         
/*     */         case BURY:
/*     */         
/*     */         case BEARD_THIN:
/*     */         case BEARD_BOX:
/*     */         
/*     */         case ENCAPSULATE:
/*     */           break;
/*     */       } 
/* 160 */       noiseValue += 
/*     */         
/* 164 */         getBuryContribution(dx / 2.0D, dy / 2.0D, dz / 2.0D) * 0.8D;
/*     */     } 
/* 168 */     for (JigsawJunction junction : this.junctions) {
/* 169 */       int dx = blockX - junction.getSourceX();
/* 170 */       int dy = blockY - junction.getSourceGroundY();
/* 171 */       int dz = blockZ - junction.getSourceZ();
/* 173 */       noiseValue += getBeardContribution(dx, dy, dz, dy) * 0.4D;
/*     */     } 
/* 176 */     return noiseValue;
/*     */   }
/*     */   
/*     */   public double minValue() {
/* 182 */     return Double.NEGATIVE_INFINITY;
/*     */   }
/*     */   
/*     */   public double maxValue() {
/* 188 */     return Double.POSITIVE_INFINITY;
/*     */   }
/*     */   
/*     */   private static double getBuryContribution(double dx, double dy, double dz) {
/* 193 */     double distance = Mth.length(dx, dy, dz);
/* 194 */     return Mth.clampedMap(distance, 0.0D, 6.0D, 1.0D, 0.0D);
/*     */   }
/*     */   
/*     */   private static double getBeardContribution(int dx, int dy, int dz, int yToGround) {
/* 202 */     int xi = dx + 12;
/* 203 */     int yi = dy + 12;
/* 204 */     int zi = dz + 12;
/* 205 */     if (!isInKernelRange(xi) || !isInKernelRange(yi) || !isInKernelRange(zi))
/* 206 */       return 0.0D; 
/* 209 */     double dyWithOffset = yToGround + 0.5D;
/* 210 */     double distanceSqr = Mth.lengthSquared(dx, dyWithOffset, dz);
/* 211 */     double value = -dyWithOffset * Mth.fastInvSqrt(distanceSqr / 2.0D) / 2.0D;
/* 212 */     return value * BEARD_KERNEL[zi * 24 * 24 + xi * 24 + yi];
/*     */   }
/*     */   
/*     */   private static boolean isInKernelRange(int xi) {
/* 216 */     return (xi >= 0 && xi < 24);
/*     */   }
/*     */   
/*     */   private static double computeBeardContribution(int dx, int dy, int dz) {
/* 221 */     return computeBeardContribution(dx, dy + 0.5D, dz);
/*     */   }
/*     */   
/*     */   private static double computeBeardContribution(int dx, double dy, int dz) {
/* 225 */     double distanceSqr = Mth.lengthSquared(dx, dy, dz);
/* 226 */     double pieceWeight = Math.pow(Math.E, -distanceSqr / 16.0D);
/* 228 */     return pieceWeight;
/*     */   }
/*     */ }


/* Location:              C:\Users\karol\AppData\Roaming\ModrinthApp\meta\versions\26.2-0.19.5\26.2-0.19.5.jar!\net\minecraft\world\level\levelgen\Beardifier.class
 * Java compiler version: 25 (69.0)
 * JD-Core Version:       1.1.3
 */