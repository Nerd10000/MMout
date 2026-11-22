/*     */ package net.minecraft.client.renderer;
/*     */ 
/*     */ import com.mojang.blaze3d.PrimitiveTopology;
/*     */ import com.mojang.blaze3d.buffers.GpuBuffer;
/*     */ import com.mojang.blaze3d.buffers.GpuBufferSlice;
/*     */ import com.mojang.blaze3d.buffers.Std140Builder;
/*     */ import com.mojang.blaze3d.buffers.Std140SizeCalculator;
/*     */ import com.mojang.blaze3d.pipeline.RenderPipeline;
/*     */ import com.mojang.blaze3d.pipeline.RenderTarget;
/*     */ import com.mojang.blaze3d.platform.NativeImage;
/*     */ import com.mojang.blaze3d.systems.RenderPass;
/*     */ import com.mojang.blaze3d.systems.RenderSystem;
/*     */ import com.mojang.blaze3d.textures.GpuTextureView;
/*     */ import com.mojang.logging.LogUtils;
/*     */ import java.io.IOException;
/*     */ import java.io.InputStream;
/*     */ import java.nio.ByteBuffer;
/*     */ import java.util.Optional;
/*     */ import java.util.OptionalDouble;
/*     */ import net.minecraft.client.CloudStatus;
/*     */ import net.minecraft.client.Minecraft;
/*     */ import net.minecraft.core.Direction;
/*     */ import net.minecraft.resources.Identifier;
/*     */ import net.minecraft.server.packs.resources.ResourceManager;
/*     */ import net.minecraft.server.packs.resources.SimplePreparableReloadListener;
/*     */ import net.minecraft.util.ARGB;
/*     */ import net.minecraft.util.Mth;
/*     */ import net.minecraft.util.profiling.ProfilerFiller;
/*     */ import net.minecraft.world.phys.Vec3;
/*     */ import org.joml.Vector4fc;
/*     */ import org.slf4j.Logger;
/*     */ 
/*     */ public class CloudRenderer extends SimplePreparableReloadListener<Optional<CloudRenderer.TextureData>> implements AutoCloseable {
/*     */   private static final int FLAG_INSIDE_FACE = 16;
/*     */   
/*     */   private static final int FLAG_USE_TOP_COLOR = 32;
/*     */   
/*     */   private static final float CELL_SIZE_IN_BLOCKS = 12.0F;
/*     */   
/*     */   private static final int TICKS_PER_CELL = 400;
/*     */   
/*     */   private static final float BLOCKS_PER_SECOND = 0.6F;
/*     */   
/*  44 */   private static final int UBO_SIZE = (new Std140SizeCalculator())
/*  45 */     .putVec4()
/*  46 */     .putVec3()
/*  47 */     .putVec3()
/*  48 */     .get();
/*     */   
/*  50 */   private static final Logger LOGGER = LogUtils.getLogger();
/*     */   
/*  51 */   private static final Identifier TEXTURE_LOCATION = Identifier.withDefaultNamespace("textures/environment/clouds.png");
/*     */   
/*     */   private static final long EMPTY_CELL = 0L;
/*     */   
/*     */   private static final int COLOR_OFFSET = 4;
/*     */   
/*     */   private static final int NORTH_OFFSET = 3;
/*     */   
/*     */   private static final int EAST_OFFSET = 2;
/*     */   
/*     */   private static final int SOUTH_OFFSET = 1;
/*     */   
/*     */   private static final int WEST_OFFSET = 0;
/*     */   
/*     */   private boolean needsRebuild = true;
/*     */   
/*  62 */   private int prevCellX = Integer.MIN_VALUE;
/*     */   
/*  63 */   private int prevCellZ = Integer.MIN_VALUE;
/*     */   
/*  64 */   private RelativeCameraPos prevRelativeCameraPos = RelativeCameraPos.INSIDE_CLOUDS;
/*     */   
/*     */   private CloudStatus prevCloudStatus;
/*     */   
/*     */   private TextureData texture;
/*     */   
/*  68 */   private int quadCount = 0;
/*     */   
/*  69 */   private final MappableRingBuffer ubo = new MappableRingBuffer(() -> "Cloud UBO", 130, UBO_SIZE);
/*     */   
/*     */   private MappableRingBuffer utb;
/*     */   
/*     */   protected Optional<TextureData> prepare(ResourceManager manager, ProfilerFiller profiler) {
/*     */     try {
/*  75 */       InputStream input = manager.open(TEXTURE_LOCATION);
/*     */       try {
/*  76 */         NativeImage texture = NativeImage.read(input);
/*     */         try {
/*  78 */           int width = texture.getWidth();
/*  79 */           int height = texture.getHeight();
/*  80 */           long[] cells = new long[width * height];
/*  81 */           for (int y = 0; y < height; y++) {
/*  82 */             for (int x = 0; x < width; x++) {
/*  83 */               int color = texture.getPixel(x, y);
/*  84 */               if (isCellEmpty(color)) {
/*  85 */                 cells[x + y * width] = 0L;
/*     */               } else {
/*  88 */                 boolean north = isCellEmpty(texture.getPixel(x, Math.floorMod(y - 1, height)));
/*  89 */                 boolean east = isCellEmpty(texture.getPixel(Math.floorMod(x + 1, height), y));
/*  90 */                 boolean south = isCellEmpty(texture.getPixel(x, Math.floorMod(y + 1, height)));
/*  91 */                 boolean west = isCellEmpty(texture.getPixel(Math.floorMod(x - 1, height), y));
/*  92 */                 cells[x + y * width] = packCellData(color, north, east, south, west);
/*     */               } 
/*     */             } 
/*     */           } 
/*  95 */           Optional<TextureData> optional = Optional.of(new TextureData(cells, width, height));
/*  96 */           if (texture != null)
/*  96 */             texture.close(); 
/*  96 */           if (input != null)
/*  96 */             input.close(); 
/*     */           return optional;
/*     */         } catch (Throwable throwable) {
/*     */           if (texture != null)
/*     */             try {
/*     */               texture.close();
/*     */             } catch (Throwable throwable1) {
/*     */               throwable.addSuppressed(throwable1);
/*     */             }  
/*     */           throw throwable;
/*     */         } 
/*     */       } catch (Throwable throwable) {
/*     */         if (input != null)
/*     */           try {
/*     */             input.close();
/*     */           } catch (Throwable throwable1) {
/*     */             throwable.addSuppressed(throwable1);
/*     */           }  
/*     */         throw throwable;
/*     */       } 
/*  96 */     } catch (IOException e) {
/*  97 */       LOGGER.error("Failed to load cloud texture", e);
/*  98 */       return Optional.empty();
/*     */     } 
/*     */   }
/*     */   
/*     */   private static int getSizeForCloudDistance(int radiusCells) {
/* 107 */     int maxFacesPerCell = 4;
/* 108 */     int maxCells = (radiusCells + 1) * 2 * (radiusCells + 1) * 2 / 2;
/* 109 */     int maxFaces = maxCells * 4 + 54;
/* 110 */     return maxFaces * 3;
/*     */   }
/*     */   
/*     */   protected void apply(Optional<TextureData> preparations, ResourceManager manager, ProfilerFiller profiler) {
/* 115 */     this.texture = preparations.orElse(null);
/* 116 */     this.needsRebuild = true;
/*     */   }
/*     */   
/*     */   private static boolean isCellEmpty(int color) {
/* 120 */     return (ARGB.alpha(color) < 10);
/*     */   }
/*     */   
/*     */   private static long packCellData(int color, boolean north, boolean east, boolean south, boolean west) {
/* 124 */     return color << 4L | ((north ? 1 : 0) << 3) | ((east ? 1 : 0) << 2) | ((south ? 1 : 0) << 1) | ((west ? 1 : 0) << 0);
/*     */   }
/*     */   
/*     */   private static boolean isNorthEmpty(long cellData) {
/* 128 */     return ((cellData >> 3L & 0x1L) != 0L);
/*     */   }
/*     */   
/*     */   private static boolean isEastEmpty(long cellData) {
/* 132 */     return ((cellData >> 2L & 0x1L) != 0L);
/*     */   }
/*     */   
/*     */   private static boolean isSouthEmpty(long cellData) {
/* 136 */     return ((cellData >> 1L & 0x1L) != 0L);
/*     */   }
/*     */   
/*     */   private static boolean isWestEmpty(long cellData) {
/* 140 */     return ((cellData >> 0L & 0x1L) != 0L);
/*     */   }
/*     */   
/*     */   public void render(int color, CloudStatus cloudStatus, float bottomY, int range, Vec3 cameraPosition, long gameTime, float partialTicks) {
/*     */     RelativeCameraPos relativeCameraPos;
/*     */     GpuTextureView colorTexture, depthTexture;
/* 144 */     if (this.texture == null)
/*     */       return; 
/* 147 */     int radiusBlocks = range * 16;
/* 148 */     int radiusCells = Mth.ceil(radiusBlocks / 12.0F);
/* 149 */     int utbSize = getSizeForCloudDistance(radiusCells);
/* 150 */     if (this.utb == null || this.utb.currentBuffer().size() != utbSize) {
/* 151 */       if (this.utb != null)
/* 152 */         this.utb.close(); 
/* 154 */       this.utb = new MappableRingBuffer(() -> "Cloud UTB", 258, utbSize);
/*     */     } 
/* 157 */     float relativeBottomY = (float)(bottomY - cameraPosition.y);
/* 158 */     float relativeTopY = relativeBottomY + 4.0F;
/* 160 */     if (relativeTopY < 0.0F) {
/* 161 */       relativeCameraPos = RelativeCameraPos.ABOVE_CLOUDS;
/* 162 */     } else if (relativeBottomY > 0.0F) {
/* 163 */       relativeCameraPos = RelativeCameraPos.BELOW_CLOUDS;
/*     */     } else {
/* 165 */       relativeCameraPos = RelativeCameraPos.INSIDE_CLOUDS;
/*     */     } 
/* 168 */     float cloudOffset = (float)(gameTime % this.texture.width * 400L) + partialTicks;
/* 171 */     double cloudX = cameraPosition.x + (cloudOffset * 0.030000001F);
/* 172 */     double cloudZ = cameraPosition.z + 3.9600000381469727D;
/* 175 */     double textureWidthBlocks = this.texture.width * 12.0D;
/* 176 */     double textureHeightBlocks = this.texture.height * 12.0D;
/* 177 */     cloudX -= Mth.floor(cloudX / textureWidthBlocks) * textureWidthBlocks;
/* 178 */     cloudZ -= Mth.floor(cloudZ / textureHeightBlocks) * textureHeightBlocks;
/* 180 */     int cellX = Mth.floor(cloudX / 12.0D);
/* 181 */     int cellZ = Mth.floor(cloudZ / 12.0D);
/* 183 */     float xInCell = (float)(cloudX - (cellX * 12.0F));
/* 184 */     float zInCell = (float)(cloudZ - (cellZ * 12.0F));
/* 186 */     boolean fancyClouds = (cloudStatus == CloudStatus.FANCY);
/* 187 */     RenderPipeline renderPipeline = fancyClouds ? RenderPipelines.CLOUDS : RenderPipelines.FLAT_CLOUDS;
/* 189 */     if (this.needsRebuild || cellX != this.prevCellX || cellZ != this.prevCellZ || relativeCameraPos != this.prevRelativeCameraPos || cloudStatus != this.prevCloudStatus) {
/* 194 */       this.needsRebuild = false;
/* 195 */       this.prevCellX = cellX;
/* 196 */       this.prevCellZ = cellZ;
/* 197 */       this.prevRelativeCameraPos = relativeCameraPos;
/* 198 */       this.prevCloudStatus = cloudStatus;
/* 199 */       this.utb.rotate();
/* 200 */       GpuBufferSlice.MappedView mappedView = this.utb.currentBuffer().map(false, true);
/*     */       try {
/* 201 */         buildMesh(relativeCameraPos, mappedView.data(), cellX, cellZ, fancyClouds, radiusCells);
/* 202 */         this.quadCount = mappedView.data().position() / 3;
/* 203 */         if (mappedView != null)
/* 203 */           mappedView.close(); 
/*     */       } catch (Throwable throwable) {
/*     */         if (mappedView != null)
/*     */           try {
/*     */             mappedView.close();
/*     */           } catch (Throwable throwable1) {
/*     */             throwable.addSuppressed(throwable1);
/*     */           }  
/*     */         throw throwable;
/*     */       } 
/*     */     } 
/* 206 */     if (this.quadCount == 0)
/*     */       return; 
/* 210 */     GpuBufferSlice.MappedView view = this.ubo.currentBuffer().map(false, true);
/*     */     try {
/* 211 */       Std140Builder.intoBuffer(view.data())
/* 212 */         .putVec4((Vector4fc)ARGB.vector4fFromARGB32(color))
/* 213 */         .putVec3(-xInCell, relativeBottomY, -zInCell)
/* 214 */         .putVec3(12.0F, 4.0F, 12.0F);
/* 215 */       if (view != null)
/* 215 */         view.close(); 
/*     */     } catch (Throwable throwable) {
/*     */       if (view != null)
/*     */         try {
/*     */           view.close();
/*     */         } catch (Throwable throwable1) {
/*     */           throwable.addSuppressed(throwable1);
/*     */         }  
/*     */       throw throwable;
/*     */     } 
/* 216 */     GpuBufferSlice dynamicTransforms = RenderSystem.getDynamicUniforms().writeTransform(RenderSystem.getModelViewMatrixCopy());
/* 218 */     RenderTarget mainRenderTarget = (Minecraft.getInstance()).gameRenderer.mainRenderTarget();
/* 219 */     RenderTarget cloudTarget = (Minecraft.getInstance()).levelRenderer.cloudsTarget();
/* 222 */     RenderSystem.AutoStorageIndexBuffer indices = RenderSystem.getSequentialBuffer(PrimitiveTopology.QUADS);
/* 223 */     GpuBuffer indexBuffer = indices.getBuffer(6 * this.quadCount);
/* 224 */     if (cloudTarget != null) {
/* 225 */       colorTexture = cloudTarget.getColorTextureView();
/* 226 */       depthTexture = cloudTarget.getDepthTextureView();
/*     */     } else {
/* 228 */       colorTexture = mainRenderTarget.getColorTextureView();
/* 229 */       depthTexture = mainRenderTarget.getDepthTextureView();
/*     */     } 
/* 232 */     RenderPass renderPass = RenderSystem.getDevice().createCommandEncoder().createRenderPass(() -> "Clouds", colorTexture, Optional.empty(), depthTexture, OptionalDouble.empty());
/*     */     try {
/* 233 */       renderPass.setPipeline(renderPipeline);
/* 234 */       RenderSystem.bindDefaultUniforms(renderPass);
/* 235 */       renderPass.setUniform("DynamicTransforms", dynamicTransforms);
/* 236 */       renderPass.setIndexBuffer(indexBuffer, indices.type());
/* 237 */       renderPass.setUniform("CloudInfo", this.ubo.currentBuffer());
/* 238 */       renderPass.setUniform("CloudFaces", this.utb.currentBuffer());
/* 239 */       renderPass.drawIndexed(6 * this.quadCount, 1, 0, 0, 0);
/* 240 */       if (renderPass != null)
/* 240 */         renderPass.close(); 
/*     */     } catch (Throwable throwable) {
/*     */       if (renderPass != null)
/*     */         try {
/*     */           renderPass.close();
/*     */         } catch (Throwable throwable1) {
/*     */           throwable.addSuppressed(throwable1);
/*     */         }  
/*     */       throw throwable;
/*     */     } 
/*     */   }
/*     */   
/*     */   private void buildMesh(RelativeCameraPos relativePos, ByteBuffer faceBuffer, int centerCellX, int centerCellZ, boolean extrude, int radiusCells) {
/* 244 */     if (this.texture == null)
/*     */       return; 
/* 247 */     long[] cells = this.texture.cells;
/* 248 */     int textureWidth = this.texture.width;
/* 249 */     int textureHeight = this.texture.height;
/* 252 */     for (int ring = 0; ring <= 2 * radiusCells; ring++) {
/* 253 */       for (int relativeCellX = -ring; relativeCellX <= ring; relativeCellX++) {
/* 254 */         int relativeCellZ = ring - Math.abs(relativeCellX);
/* 255 */         if (relativeCellZ >= 0 && relativeCellZ <= radiusCells)
/* 258 */           if (relativeCellX * relativeCellX + relativeCellZ * relativeCellZ <= radiusCells * radiusCells) {
/* 263 */             if (relativeCellZ != 0)
/* 264 */               tryBuildCell(relativePos, faceBuffer, centerCellX, centerCellZ, extrude, relativeCellX, textureWidth, -relativeCellZ, textureHeight, cells); 
/* 266 */             tryBuildCell(relativePos, faceBuffer, centerCellX, centerCellZ, extrude, relativeCellX, textureWidth, relativeCellZ, textureHeight, cells);
/*     */           }  
/*     */       } 
/*     */     } 
/*     */   }
/*     */   
/*     */   private void tryBuildCell(RelativeCameraPos relativePos, ByteBuffer faceBuffer, int cellX, int cellZ, boolean extrude, int relativeCellX, int textureWidth, int relativeCellZ, int textureHeight, long[] cells) {
/* 272 */     int indexX = Math.floorMod(cellX + relativeCellX, textureWidth);
/* 273 */     int indexY = Math.floorMod(cellZ + relativeCellZ, textureHeight);
/* 274 */     long cellData = cells[indexX + indexY * textureWidth];
/* 275 */     if (cellData == 0L)
/*     */       return; 
/* 278 */     if (extrude) {
/* 279 */       buildExtrudedCell(relativePos, faceBuffer, relativeCellX, relativeCellZ, cellData);
/*     */     } else {
/* 281 */       buildFlatCell(faceBuffer, relativeCellX, relativeCellZ);
/*     */     } 
/*     */   }
/*     */   
/*     */   private void buildFlatCell(ByteBuffer faceBuffer, int x, int z) {
/* 286 */     encodeFace(faceBuffer, x, z, Direction.DOWN, 32);
/*     */   }
/*     */   
/*     */   private void encodeFace(ByteBuffer faceBuffer, int x, int z, Direction direction, int flags) {
/* 290 */     int dirAndFlags = direction.get3DDataValue() | flags;
/* 291 */     dirAndFlags |= (x & 0x1) << 7;
/* 292 */     dirAndFlags |= (z & 0x1) << 6;
/* 293 */     faceBuffer.put((byte)(x >> 1)).put((byte)(z >> 1)).put((byte)dirAndFlags);
/*     */   }
/*     */   
/*     */   private void buildExtrudedCell(RelativeCameraPos relativePos, ByteBuffer faceBuffer, int x, int z, long cellData) {
/* 298 */     if (relativePos != RelativeCameraPos.BELOW_CLOUDS)
/* 299 */       encodeFace(faceBuffer, x, z, Direction.UP, 0); 
/* 303 */     if (relativePos != RelativeCameraPos.ABOVE_CLOUDS)
/* 304 */       encodeFace(faceBuffer, x, z, Direction.DOWN, 0); 
/* 307 */     if (isNorthEmpty(cellData) && z > 0)
/* 308 */       encodeFace(faceBuffer, x, z, Direction.NORTH, 0); 
/* 311 */     if (isSouthEmpty(cellData) && z < 0)
/* 312 */       encodeFace(faceBuffer, x, z, Direction.SOUTH, 0); 
/* 315 */     if (isWestEmpty(cellData) && x > 0)
/* 316 */       encodeFace(faceBuffer, x, z, Direction.WEST, 0); 
/* 319 */     if (isEastEmpty(cellData) && x < 0)
/* 320 */       encodeFace(faceBuffer, x, z, Direction.EAST, 0); 
/* 324 */     boolean addInteriorFaces = (Math.abs(x) <= 1 && Math.abs(z) <= 1);
/* 325 */     if (addInteriorFaces)
/* 326 */       for (Direction direction : Direction.values())
/* 327 */         encodeFace(faceBuffer, x, z, direction, 16);  
/*     */   }
/*     */   
/*     */   public void markForRebuild() {
/* 333 */     this.needsRebuild = true;
/*     */   }
/*     */   
/*     */   public void endFrame() {
/* 337 */     this.ubo.rotate();
/*     */   }
/*     */   
/*     */   public void close() {
/* 342 */     this.ubo.close();
/* 343 */     if (this.utb != null)
/* 344 */       this.utb.close(); 
/*     */   }
/*     */   
/*     */   private enum RelativeCameraPos {
/* 349 */     ABOVE_CLOUDS, INSIDE_CLOUDS, BELOW_CLOUDS;
/*     */   }
/*     */   
/*     */   public static final class TextureData extends Record {
/*     */     private final long[] cells;
/*     */     
/*     */     private final int width;
/*     */     
/*     */     private final int height;
/*     */     
/*     */     public TextureData(long[] cells, int width, int height) {
/* 354 */       this.cells = cells;
/* 354 */       this.width = width;
/* 354 */       this.height = height;
/*     */     }
/*     */     
/*     */     public final String toString() {
/*     */       // Byte code:
/*     */       //   0: aload_0
/*     */       //   1: <illegal opcode> toString : (Lnet/minecraft/client/renderer/CloudRenderer$TextureData;)Ljava/lang/String;
/*     */       //   6: areturn
/*     */       // Line number table:
/*     */       //   Java source line number -> byte code offset
/*     */       //   #354	-> 0
/*     */       // Local variable table:
/*     */       //   start	length	slot	name	descriptor
/*     */       //   0	7	0	this	Lnet/minecraft/client/renderer/CloudRenderer$TextureData;
/*     */     }
/*     */     
/*     */     public final int hashCode() {
/*     */       // Byte code:
/*     */       //   0: aload_0
/*     */       //   1: <illegal opcode> hashCode : (Lnet/minecraft/client/renderer/CloudRenderer$TextureData;)I
/*     */       //   6: ireturn
/*     */       // Line number table:
/*     */       //   Java source line number -> byte code offset
/*     */       //   #354	-> 0
/*     */       // Local variable table:
/*     */       //   start	length	slot	name	descriptor
/*     */       //   0	7	0	this	Lnet/minecraft/client/renderer/CloudRenderer$TextureData;
/*     */     }
/*     */     
/*     */     public final boolean equals(Object o) {
/*     */       // Byte code:
/*     */       //   0: aload_0
/*     */       //   1: aload_1
/*     */       //   2: <illegal opcode> equals : (Lnet/minecraft/client/renderer/CloudRenderer$TextureData;Ljava/lang/Object;)Z
/*     */       //   7: ireturn
/*     */       // Line number table:
/*     */       //   Java source line number -> byte code offset
/*     */       //   #354	-> 0
/*     */       // Local variable table:
/*     */       //   start	length	slot	name	descriptor
/*     */       //   0	8	0	this	Lnet/minecraft/client/renderer/CloudRenderer$TextureData;
/*     */       //   0	8	1	o	Ljava/lang/Object;
/*     */     }
/*     */     
/*     */     public long[] cells() {
/* 354 */       return this.cells;
/*     */     }
/*     */     
/*     */     public int width() {
/* 354 */       return this.width;
/*     */     }
/*     */     
/*     */     public int height() {
/* 354 */       return this.height;
/*     */     }
/*     */   }
/*     */ }


/* Location:              C:\Users\karol\AppData\Roaming\ModrinthApp\meta\versions\26.2-0.19.5\26.2-0.19.5.jar!\net\minecraft\client\renderer\CloudRenderer.class
 * Java compiler version: 25 (69.0)
 * JD-Core Version:       1.1.3
 */