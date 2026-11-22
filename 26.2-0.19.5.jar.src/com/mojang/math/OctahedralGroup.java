/*     */ package com.mojang.math;
/*     */ 
/*     */ import java.util.Arrays;
/*     */ import java.util.Map;
/*     */ import java.util.stream.Collectors;
/*     */ import net.minecraft.core.Direction;
/*     */ import net.minecraft.core.FrontAndTop;
/*     */ import net.minecraft.util.StringRepresentable;
/*     */ import net.minecraft.util.Util;
/*     */ import org.joml.Matrix3f;
/*     */ import org.joml.Matrix3fc;
/*     */ import org.joml.Vector3i;
/*     */ 
/*     */ public enum OctahedralGroup implements StringRepresentable {
/*  38 */   IDENTITY("identity", SymmetricGroup3.P123, false, false, false),
/*  41 */   ROT_180_FACE_XY("rot_180_face_xy", SymmetricGroup3.P123, true, true, false),
/*  42 */   ROT_180_FACE_XZ("rot_180_face_xz", SymmetricGroup3.P123, true, false, true),
/*  43 */   ROT_180_FACE_YZ("rot_180_face_yz", SymmetricGroup3.P123, false, true, true),
/*  46 */   ROT_120_NNN("rot_120_nnn", SymmetricGroup3.P231, false, false, false),
/*  47 */   ROT_120_NNP("rot_120_nnp", SymmetricGroup3.P312, true, false, true),
/*  48 */   ROT_120_NPN("rot_120_npn", SymmetricGroup3.P312, false, true, true),
/*  49 */   ROT_120_NPP("rot_120_npp", SymmetricGroup3.P231, true, false, true),
/*  50 */   ROT_120_PNN("rot_120_pnn", SymmetricGroup3.P312, true, true, false),
/*  51 */   ROT_120_PNP("rot_120_pnp", SymmetricGroup3.P231, true, true, false),
/*  52 */   ROT_120_PPN("rot_120_ppn", SymmetricGroup3.P231, false, true, true),
/*  53 */   ROT_120_PPP("rot_120_ppp", SymmetricGroup3.P312, false, false, false),
/*  56 */   ROT_180_EDGE_XY_NEG("rot_180_edge_xy_neg", SymmetricGroup3.P213, true, true, true),
/*  57 */   ROT_180_EDGE_XY_POS("rot_180_edge_xy_pos", SymmetricGroup3.P213, false, false, true),
/*  58 */   ROT_180_EDGE_XZ_NEG("rot_180_edge_xz_neg", SymmetricGroup3.P321, true, true, true),
/*  59 */   ROT_180_EDGE_XZ_POS("rot_180_edge_xz_pos", SymmetricGroup3.P321, false, true, false),
/*  60 */   ROT_180_EDGE_YZ_NEG("rot_180_edge_yz_neg", SymmetricGroup3.P132, true, true, true),
/*  61 */   ROT_180_EDGE_YZ_POS("rot_180_edge_yz_pos", SymmetricGroup3.P132, true, false, false),
/*  64 */   ROT_90_X_NEG("rot_90_x_neg", SymmetricGroup3.P132, false, false, true),
/*  65 */   ROT_90_X_POS("rot_90_x_pos", SymmetricGroup3.P132, false, true, false),
/*  66 */   ROT_90_Y_NEG("rot_90_y_neg", SymmetricGroup3.P321, true, false, false),
/*  67 */   ROT_90_Y_POS("rot_90_y_pos", SymmetricGroup3.P321, false, false, true),
/*  68 */   ROT_90_Z_NEG("rot_90_z_neg", SymmetricGroup3.P213, false, true, false),
/*  69 */   ROT_90_Z_POS("rot_90_z_pos", SymmetricGroup3.P213, true, false, false),
/*  72 */   INVERSION("inversion", SymmetricGroup3.P123, true, true, true),
/*  75 */   INVERT_X("invert_x", SymmetricGroup3.P123, true, false, false),
/*  76 */   INVERT_Y("invert_y", SymmetricGroup3.P123, false, true, false),
/*  77 */   INVERT_Z("invert_z", SymmetricGroup3.P123, false, false, true),
/*  80 */   ROT_60_REF_NNN("rot_60_ref_nnn", SymmetricGroup3.P312, true, true, true),
/*  81 */   ROT_60_REF_NNP("rot_60_ref_nnp", SymmetricGroup3.P231, true, false, false),
/*  82 */   ROT_60_REF_NPN("rot_60_ref_npn", SymmetricGroup3.P231, false, false, true),
/*  83 */   ROT_60_REF_NPP("rot_60_ref_npp", SymmetricGroup3.P312, false, false, true),
/*  84 */   ROT_60_REF_PNN("rot_60_ref_pnn", SymmetricGroup3.P231, false, true, false),
/*  85 */   ROT_60_REF_PNP("rot_60_ref_pnp", SymmetricGroup3.P312, true, false, false),
/*  86 */   ROT_60_REF_PPN("rot_60_ref_ppn", SymmetricGroup3.P312, false, true, false),
/*  87 */   ROT_60_REF_PPP("rot_60_ref_ppp", SymmetricGroup3.P231, true, true, true),
/*  90 */   SWAP_XY("swap_xy", SymmetricGroup3.P213, false, false, false),
/*  91 */   SWAP_YZ("swap_yz", SymmetricGroup3.P132, false, false, false),
/*  92 */   SWAP_XZ("swap_xz", SymmetricGroup3.P321, false, false, false),
/*  95 */   SWAP_NEG_XY("swap_neg_xy", SymmetricGroup3.P213, true, true, false),
/*  96 */   SWAP_NEG_YZ("swap_neg_yz", SymmetricGroup3.P132, false, true, true),
/*  97 */   SWAP_NEG_XZ("swap_neg_xz", SymmetricGroup3.P321, true, false, true),
/* 100 */   ROT_90_REF_X_NEG("rot_90_ref_x_neg", SymmetricGroup3.P132, true, false, true),
/* 101 */   ROT_90_REF_X_POS("rot_90_ref_x_pos", SymmetricGroup3.P132, true, true, false),
/* 102 */   ROT_90_REF_Y_NEG("rot_90_ref_y_neg", SymmetricGroup3.P321, true, true, false),
/* 103 */   ROT_90_REF_Y_POS("rot_90_ref_y_pos", SymmetricGroup3.P321, false, true, true),
/* 104 */   ROT_90_REF_Z_NEG("rot_90_ref_z_neg", SymmetricGroup3.P213, false, true, true),
/* 105 */   ROT_90_REF_Z_POS("rot_90_ref_z_pos", SymmetricGroup3.P213, true, false, true);
/*     */   
/*     */   public static final OctahedralGroup BLOCK_ROT_X_270;
/*     */   
/*     */   public static final OctahedralGroup BLOCK_ROT_X_180;
/*     */   
/*     */   public static final OctahedralGroup BLOCK_ROT_X_90;
/*     */   
/*     */   public static final OctahedralGroup BLOCK_ROT_Y_270;
/*     */   
/*     */   public static final OctahedralGroup BLOCK_ROT_Y_180;
/*     */   
/*     */   public static final OctahedralGroup BLOCK_ROT_Y_90;
/*     */   
/*     */   public static final OctahedralGroup BLOCK_ROT_Z_270;
/*     */   
/*     */   public static final OctahedralGroup BLOCK_ROT_Z_180;
/*     */   
/*     */   public static final OctahedralGroup BLOCK_ROT_Z_90;
/*     */   
/*     */   private final Matrix3fc transformation;
/*     */   
/*     */   private final String name;
/*     */   
/*     */   private Map<Direction, Direction> rotatedDirections;
/*     */   
/*     */   private final boolean invertX;
/*     */   
/*     */   private final boolean invertY;
/*     */   
/*     */   private final boolean invertZ;
/*     */   
/*     */   private final SymmetricGroup3 permutation;
/*     */   
/*     */   private static final OctahedralGroup[][] CAYLEY_TABLE;
/*     */   
/*     */   private static final OctahedralGroup[] INVERSE_TABLE;
/*     */   
/*     */   static {
/* 110 */     BLOCK_ROT_X_270 = ROT_90_X_POS;
/* 111 */     BLOCK_ROT_X_180 = ROT_180_FACE_YZ;
/* 112 */     BLOCK_ROT_X_90 = ROT_90_X_NEG;
/* 114 */     BLOCK_ROT_Y_270 = ROT_90_Y_POS;
/* 115 */     BLOCK_ROT_Y_180 = ROT_180_FACE_XZ;
/* 116 */     BLOCK_ROT_Y_90 = ROT_90_Y_NEG;
/* 118 */     BLOCK_ROT_Z_270 = ROT_90_Z_POS;
/* 119 */     BLOCK_ROT_Z_180 = ROT_180_FACE_XY;
/* 120 */     BLOCK_ROT_Z_90 = ROT_90_Z_NEG;
/* 158 */     CAYLEY_TABLE = (OctahedralGroup[][])Util.make(() -> {
/*     */           OctahedralGroup[] values = values();
/*     */           OctahedralGroup[][] table = new OctahedralGroup[values.length][values.length];
/*     */           Map<Integer, OctahedralGroup> fingerprints = (Map<Integer, OctahedralGroup>)Arrays.<OctahedralGroup>stream(values).collect(Collectors.toMap(OctahedralGroup::trace, ()));
/*     */           for (OctahedralGroup first : values) {
/*     */             for (OctahedralGroup second : values) {
/*     */               SymmetricGroup3 composedPermutation = second.permutation.compose(first.permutation);
/*     */               int i = first.inverts(Direction.Axis.X) ^ second.inverts(first.permutation.permuteAxis(Direction.Axis.X));
/*     */               int j = first.inverts(Direction.Axis.Y) ^ second.inverts(first.permutation.permuteAxis(Direction.Axis.Y));
/*     */               int k = first.inverts(Direction.Axis.Z) ^ second.inverts(first.permutation.permuteAxis(Direction.Axis.Z));
/*     */               table[first.ordinal()][second.ordinal()] = fingerprints.get(Integer.valueOf(trace(i, j, k, composedPermutation)));
/*     */             } 
/*     */           } 
/*     */           return table;
/*     */         });
/* 177 */     INVERSE_TABLE = (OctahedralGroup[])Arrays.<OctahedralGroup>stream(values()).map(f -> (OctahedralGroup)Arrays.<OctahedralGroup>stream(values()).filter(()).findAny().get()).toArray(x$0 -> new OctahedralGroup[x$0]);
/*     */   }
/*     */   
/*     */   OctahedralGroup(String name, SymmetricGroup3 permutation, boolean invertX, boolean invertY, boolean invertZ) {
/*     */     this.name = name;
/*     */     this.invertX = invertX;
/*     */     this.invertY = invertY;
/*     */     this.invertZ = invertZ;
/*     */     this.permutation = permutation;
/*     */     this.transformation = (Matrix3fc)(new Matrix3f()).scaling(invertX ? -1.0F : 1.0F, invertY ? -1.0F : 1.0F, invertZ ? -1.0F : 1.0F).mul(permutation.transformation());
/*     */   }
/*     */   
/*     */   private static int trace(boolean invertX, boolean invertY, boolean invertZ, SymmetricGroup3 permutation) {
/*     */     int inversionIndex = (invertZ ? 4 : 0) + (invertY ? 2 : 0) + (invertX ? 1 : 0);
/*     */     return permutation.ordinal() << 3 | inversionIndex;
/*     */   }
/*     */   
/*     */   private int trace() {
/*     */     return trace(this.invertX, this.invertY, this.invertZ, this.permutation);
/*     */   }
/*     */   
/*     */   public OctahedralGroup compose(OctahedralGroup that) {
/* 180 */     return CAYLEY_TABLE[ordinal()][that.ordinal()];
/*     */   }
/*     */   
/*     */   public OctahedralGroup inverse() {
/* 184 */     return INVERSE_TABLE[ordinal()];
/*     */   }
/*     */   
/*     */   public Matrix3fc transformation() {
/* 188 */     return this.transformation;
/*     */   }
/*     */   
/*     */   public String toString() {
/* 193 */     return this.name;
/*     */   }
/*     */   
/*     */   public String getSerializedName() {
/* 198 */     return this.name;
/*     */   }
/*     */   
/*     */   public Direction rotate(Direction direction) {
/* 203 */     if (this.rotatedDirections == null)
/* 204 */       this.rotatedDirections = Util.makeEnumMap(Direction.class, facing -> {
/*     */             Direction.Axis oldAxis = facing.getAxis();
/*     */             Direction.AxisDirection oldDirection = facing.getAxisDirection();
/*     */             Direction.Axis newAxis = this.permutation.inverse().permuteAxis(oldAxis);
/*     */             Direction.AxisDirection newDirection = inverts(newAxis) ? oldDirection.opposite() : oldDirection;
/*     */             return Direction.fromAxisAndDirection(newAxis, newDirection);
/*     */           }); 
/* 218 */     return this.rotatedDirections.get(direction);
/*     */   }
/*     */   
/*     */   public Vector3i rotate(Vector3i v) {
/* 222 */     this.permutation.permuteVector(v);
/* 223 */     v.x *= this.invertX ? -1 : 1;
/* 224 */     v.y *= this.invertY ? -1 : 1;
/* 225 */     v.z *= this.invertZ ? -1 : 1;
/* 226 */     return v;
/*     */   }
/*     */   
/*     */   public boolean inverts(Direction.Axis axis) {
/* 230 */     switch (axis) {
/*     */       default:
/* 230 */         throw new MatchException(null, null);
/*     */       case X:
/*     */       
/*     */       case Y:
/*     */       
/*     */       case Z:
/*     */         break;
/*     */     } 
/* 230 */     return 
/*     */       
/* 233 */       this.invertZ;
/*     */   }
/*     */   
/*     */   public SymmetricGroup3 permutation() {
/* 238 */     return this.permutation;
/*     */   }
/*     */   
/*     */   public FrontAndTop rotate(FrontAndTop input) {
/* 242 */     return FrontAndTop.fromFrontAndTop(rotate(input.front()), rotate(input.top()));
/*     */   }
/*     */ }


/* Location:              C:\Users\karol\AppData\Roaming\ModrinthApp\meta\versions\26.2-0.19.5\26.2-0.19.5.jar!\com\mojang\math\OctahedralGroup.class
 * Java compiler version: 25 (69.0)
 * JD-Core Version:       1.1.3
 */