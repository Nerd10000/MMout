/*     */ package net.minecraft.client.resources.model.cuboid;
/*     */ 
/*     */ import com.google.common.annotations.VisibleForTesting;
/*     */ import com.mojang.blaze3d.platform.Transparency;
/*     */ import com.mojang.math.MatrixUtil;
/*     */ import com.mojang.math.Quadrant;
/*     */ import com.mojang.math.Transformation;
/*     */ import java.util.Objects;
/*     */ import net.minecraft.client.model.geom.builders.UVPair;
/*     */ import net.minecraft.client.renderer.FaceInfo;
/*     */ import net.minecraft.client.renderer.block.dispatch.ModelState;
/*     */ import net.minecraft.client.resources.model.ModelBaker;
/*     */ import net.minecraft.client.resources.model.geometry.BakedQuad;
/*     */ import net.minecraft.client.resources.model.sprite.Material;
/*     */ import net.minecraft.core.Direction;
/*     */ import org.joml.GeometryUtils;
/*     */ import org.joml.Matrix4fc;
/*     */ import org.joml.Vector3f;
/*     */ import org.joml.Vector3fc;
/*     */ 
/*     */ public class FaceBakery {
/*  27 */   private static final Vector3fc BLOCK_MIDDLE = (Vector3fc)new Vector3f(0.5F, 0.5F, 0.5F);
/*     */   
/*     */   @VisibleForTesting
/*     */   static CuboidFace.UVs defaultFaceUV(Vector3fc from, Vector3fc to, Direction facing) {
/*  32 */     switch (facing) {
/*     */       default:
/*  32 */         throw new MatchException(null, null);
/*     */       case DOWN:
/*     */       
/*     */       case UP:
/*     */       
/*     */       case NORTH:
/*     */       
/*     */       case SOUTH:
/*     */       
/*     */       case WEST:
/*     */       
/*     */       case EAST:
/*     */         break;
/*     */     } 
/*  32 */     return 
/*     */       
/*  38 */       new CuboidFace.UVs(16.0F - to.z(), 16.0F - to.y(), 16.0F - from.z(), 16.0F - from.y());
/*     */   }
/*     */   
/*     */   private static Transparency computeMaterialTransparency(Material.Baked material, CuboidFace.UVs uvs) {
/*  43 */     if (material.forceTranslucent())
/*  44 */       return Transparency.TRANSLUCENT; 
/*  46 */     return material.sprite().contents().computeTransparency(
/*  47 */         Math.min(uvs.minU(), uvs.maxU()) / 16.0F, 
/*  48 */         Math.min(uvs.minV(), uvs.maxV()) / 16.0F, 
/*  49 */         Math.max(uvs.minU(), uvs.maxU()) / 16.0F, 
/*  50 */         Math.max(uvs.minV(), uvs.maxV()) / 16.0F);
/*     */   }
/*     */   
/*     */   public static BakedQuad bakeQuad(ModelBaker modelBaker, Vector3fc from, Vector3fc to, CuboidFace face, Material.Baked material, Direction facing, ModelState modelState, CuboidRotation elementRotation, boolean shade, int lightEmission) {
/*  55 */     CuboidFace.UVs uvs = face.uvs();
/*  56 */     if (uvs == null)
/*  57 */       uvs = defaultFaceUV(from, to, facing); 
/*  59 */     Transparency transparency = computeMaterialTransparency(material, uvs);
/*  60 */     ModelBaker.Interner interner = modelBaker.interner();
/*  61 */     BakedQuad.MaterialInfo materialInfo = interner.materialInfo(BakedQuad.MaterialInfo.of(material, transparency, face.tintIndex(), shade, lightEmission));
/*  62 */     return bakeQuad(interner, from, to, uvs, face.rotation(), materialInfo, facing, modelState, elementRotation);
/*     */   }
/*     */   
/*     */   public static BakedQuad bakeQuad(ModelBaker.Interner interner, Vector3fc from, Vector3fc to, CuboidFace.UVs uvs, Quadrant uvRotation, BakedQuad.MaterialInfo materialInfo, Direction facing, ModelState modelState, CuboidRotation elementRotation) {
/*  67 */     Matrix4fc uvTransform = modelState.inverseFaceTransformation(facing);
/*  69 */     Vector3fc[] vertexPositions = new Vector3fc[4];
/*  70 */     long[] vertexPackedUvs = new long[4];
/*  72 */     FaceInfo faceInfo = FaceInfo.fromFacing(facing);
/*  73 */     for (int i = 0; i < 4; i++)
/*  74 */       bakeVertex(i, faceInfo, uvs, uvRotation, uvTransform, from, to, materialInfo, modelState.transformation(), elementRotation, vertexPositions, vertexPackedUvs, interner); 
/*  77 */     Direction finalDirection = calculateFacing(vertexPositions);
/*  79 */     if (elementRotation == null && finalDirection != null)
/*  81 */       recalculateWinding(vertexPositions, vertexPackedUvs, finalDirection); 
/*  84 */     return new BakedQuad(vertexPositions[0], vertexPositions[1], vertexPositions[2], vertexPositions[3], vertexPackedUvs[0], vertexPackedUvs[1], vertexPackedUvs[2], vertexPackedUvs[3], 
/*     */         
/*  93 */         Objects.<Direction>requireNonNullElse(finalDirection, Direction.UP), materialInfo);
/*     */   }
/*     */   
/*     */   private static void bakeVertex(int index, FaceInfo faceInfo, CuboidFace.UVs uvs, Quadrant uvRotation, Matrix4fc uvTransform, Vector3fc from, Vector3fc to, BakedQuad.MaterialInfo materialInfo, Transformation rotation, CuboidRotation elementRotation, Vector3fc[] positionOutput, long[] uvOutput, ModelBaker.Interner interner) {
/*     */     float transformedV, transformedU;
/*  99 */     FaceInfo.VertexInfo vertexInfo = faceInfo.getVertexInfo(index);
/* 100 */     Vector3f vertex = vertexInfo.select(from, to).div(16.0F);
/* 102 */     if (elementRotation != null)
/* 103 */       rotateVertexBy(vertex, elementRotation.origin(), elementRotation.transform()); 
/* 106 */     if (rotation != Transformation.IDENTITY)
/* 107 */       rotateVertexBy(vertex, BLOCK_MIDDLE, rotation.getMatrix()); 
/* 110 */     float rawU = CuboidFace.getU(uvs, uvRotation, index);
/* 111 */     float rawV = CuboidFace.getV(uvs, uvRotation, index);
/* 115 */     if (MatrixUtil.isIdentity(uvTransform)) {
/* 116 */       transformedU = rawU;
/* 117 */       transformedV = rawV;
/*     */     } else {
/* 119 */       Vector3f transformedUV = uvTransform.transformPosition(new Vector3f(cornerToCenter(rawU), cornerToCenter(rawV), 0.0F));
/* 120 */       transformedU = centerToCorner(transformedUV.x);
/* 121 */       transformedV = centerToCorner(transformedUV.y);
/*     */     } 
/* 123 */     positionOutput[index] = interner.vector((Vector3fc)vertex);
/* 124 */     uvOutput[index] = UVPair.pack(materialInfo.sprite().getU(transformedU), materialInfo.sprite().getV(transformedV));
/*     */   }
/*     */   
/*     */   private static float cornerToCenter(float value) {
/* 128 */     return value - 0.5F;
/*     */   }
/*     */   
/*     */   private static float centerToCorner(float value) {
/* 132 */     return value + 0.5F;
/*     */   }
/*     */   
/*     */   private static void rotateVertexBy(Vector3f vertex, Vector3fc origin, Matrix4fc transformation) {
/* 136 */     vertex.sub(origin);
/* 137 */     transformation.transformPosition(vertex);
/* 138 */     vertex.add(origin);
/*     */   }
/*     */   
/*     */   private static Direction calculateFacing(Vector3fc[] positions) {
/* 142 */     Vector3f normal = new Vector3f();
/* 143 */     GeometryUtils.normal(positions[0], positions[1], positions[2], normal);
/* 144 */     return findClosestDirection(normal);
/*     */   }
/*     */   
/*     */   private static Direction findClosestDirection(Vector3f direction) {
/* 148 */     if (!direction.isFinite())
/* 149 */       return null; 
/* 153 */     Direction best = null;
/* 154 */     float closestProduct = 0.0F;
/* 155 */     for (Direction candidate : Direction.values()) {
/* 156 */       float product = direction.dot(candidate.getUnitVec3f());
/* 158 */       if (product >= 0.0F && product > closestProduct) {
/* 159 */         closestProduct = product;
/* 160 */         best = candidate;
/*     */       } 
/*     */     } 
/* 163 */     return best;
/*     */   }
/*     */   
/*     */   private static void recalculateWinding(Vector3fc[] positions, long[] uvs, Direction direction) {
/* 167 */     float minX = 999.0F;
/* 168 */     float minY = 999.0F;
/* 169 */     float minZ = 999.0F;
/* 170 */     float maxX = -999.0F;
/* 171 */     float maxY = -999.0F;
/* 172 */     float maxZ = -999.0F;
/* 173 */     for (int i = 0; i < 4; i++) {
/* 174 */       Vector3fc position = positions[i];
/* 175 */       float x = position.x();
/* 176 */       float y = position.y();
/* 177 */       float z = position.z();
/* 178 */       if (x < minX)
/* 179 */         minX = x; 
/* 181 */       if (y < minY)
/* 182 */         minY = y; 
/* 184 */       if (z < minZ)
/* 185 */         minZ = z; 
/* 187 */       if (x > maxX)
/* 188 */         maxX = x; 
/* 190 */       if (y > maxY)
/* 191 */         maxY = y; 
/* 193 */       if (z > maxZ)
/* 194 */         maxZ = z; 
/*     */     } 
/* 198 */     FaceInfo info = FaceInfo.fromFacing(direction);
/* 199 */     for (int vertex = 0; vertex < 4; vertex++) {
/* 200 */       FaceInfo.VertexInfo vertInfo = info.getVertexInfo(vertex);
/* 202 */       float newX = vertInfo.xFace().select(minX, minY, minZ, maxX, maxY, maxZ);
/* 203 */       float newY = vertInfo.yFace().select(minX, minY, minZ, maxX, maxY, maxZ);
/* 204 */       float newZ = vertInfo.zFace().select(minX, minY, minZ, maxX, maxY, maxZ);
/* 206 */       int vertexToSwap = findVertex(positions, vertex, newX, newY, newZ);
/* 207 */       if (vertexToSwap == -1)
/* 209 */         throw new IllegalStateException("Can't find vertex to swap"); 
/* 212 */       if (vertexToSwap != vertex) {
/* 213 */         swap(positions, vertexToSwap, vertex);
/* 214 */         swap(uvs, vertexToSwap, vertex);
/*     */       } 
/*     */     } 
/*     */   }
/*     */   
/*     */   private static int findVertex(Vector3fc[] positions, int start, float x, float y, float z) {
/* 220 */     for (int i = start; i < 4; i++) {
/* 221 */       Vector3fc position = positions[i];
/* 223 */       if (x == position.x() && y == position.y() && z == position.z())
/* 224 */         return i; 
/*     */     } 
/* 227 */     return -1;
/*     */   }
/*     */   
/*     */   private static void swap(Vector3fc[] array, int indexA, int indexB) {
/* 231 */     Vector3fc tmp = array[indexA];
/* 232 */     array[indexA] = array[indexB];
/* 233 */     array[indexB] = tmp;
/*     */   }
/*     */   
/*     */   private static void swap(long[] array, int indexA, int indexB) {
/* 237 */     long tmp = array[indexA];
/* 238 */     array[indexA] = array[indexB];
/* 239 */     array[indexB] = tmp;
/*     */   }
/*     */ }


/* Location:              C:\Users\karol\AppData\Roaming\ModrinthApp\meta\versions\26.2-0.19.5\26.2-0.19.5.jar!\net\minecraft\client\resources\model\cuboid\FaceBakery.class
 * Java compiler version: 25 (69.0)
 * JD-Core Version:       1.1.3
 */