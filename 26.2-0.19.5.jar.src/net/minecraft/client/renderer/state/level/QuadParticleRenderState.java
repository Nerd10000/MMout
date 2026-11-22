/*     */ package net.minecraft.client.renderer.state.level;
/*     */ 
/*     */ import com.mojang.blaze3d.vertex.VertexConsumer;
/*     */ import java.util.Arrays;
/*     */ import java.util.HashMap;
/*     */ import java.util.Map;
/*     */ import java.util.Set;
/*     */ import net.minecraft.client.particle.SingleQuadParticle;
/*     */ import net.minecraft.client.renderer.SubmitNodeCollector;
/*     */ import org.joml.Quaternionf;
/*     */ import org.joml.Quaternionfc;
/*     */ import org.joml.Vector3f;
/*     */ 
/*     */ public class QuadParticleRenderState implements ParticleGroupRenderState {
/*     */   private static final int INITIAL_PARTICLE_CAPACITY = 1024;
/*     */   
/*     */   private static final int FLOATS_PER_PARTICLE = 12;
/*     */   
/*     */   private static final int INTS_PER_PARTICLE = 2;
/*     */   
/*  19 */   private final Map<SingleQuadParticle.Layer, Storage> particles = new HashMap<>();
/*     */   
/*     */   private int particleCount;
/*     */   
/*     */   public void add(SingleQuadParticle.Layer layer, float x, float y, float z, float xRot, float yRot, float zRot, float wRot, float scale, float u0, float u1, float v0, float v1, int color, int lightCoords) {
/*  39 */     ((Storage)this.particles.computeIfAbsent(layer, ignored -> new Storage())).add(x, y, z, xRot, yRot, zRot, wRot, scale, u0, u1, v0, v1, color, lightCoords);
/*  40 */     this.particleCount++;
/*     */   }
/*     */   
/*     */   public void clear() {
/*  45 */     this.particles.values().forEach(Storage::clear);
/*  46 */     this.particleCount = 0;
/*     */   }
/*     */   
/*     */   public boolean isEmpty() {
/*  50 */     return (this.particleCount == 0);
/*     */   }
/*     */   
/*     */   public void buildLayer(SingleQuadParticle.Layer layer, VertexConsumer bufferBuilder) {
/*  54 */     Storage storage = this.particles.get(layer);
/*  55 */     if (storage == null)
/*     */       return; 
/*  58 */     storage.forEachParticle((x, y, z, xRot, yRot, zRot, wRot, scale, u0, u1, v0, v1, color, lightCoords) -> renderRotatedQuad(bufferBuilder, x, y, z, xRot, yRot, zRot, wRot, scale, u0, u1, v0, v1, color, lightCoords));
/*     */   }
/*     */   
/*     */   public Set<SingleQuadParticle.Layer> layers() {
/*  64 */     return this.particles.keySet();
/*     */   }
/*     */   
/*     */   protected void renderRotatedQuad(VertexConsumer builder, float x, float y, float z, float xRot, float yRot, float zRot, float wRot, float scale, float u0, float u1, float v0, float v1, int color, int lightCoords) {
/*  82 */     Quaternionf rotation = new Quaternionf(xRot, yRot, zRot, wRot);
/*  84 */     renderVertex(builder, rotation, x, y, z, 1.0F, -1.0F, scale, u1, v1, color, lightCoords);
/*  85 */     renderVertex(builder, rotation, x, y, z, 1.0F, 1.0F, scale, u1, v0, color, lightCoords);
/*  86 */     renderVertex(builder, rotation, x, y, z, -1.0F, 1.0F, scale, u0, v0, color, lightCoords);
/*  87 */     renderVertex(builder, rotation, x, y, z, -1.0F, -1.0F, scale, u0, v1, color, lightCoords);
/*     */   }
/*     */   
/*     */   private void renderVertex(VertexConsumer builder, Quaternionf rotation, float x, float y, float z, float nx, float ny, float scale, float u, float v, int color, int lightCoords) {
/*  91 */     Vector3f scratch = (new Vector3f(nx, ny, 0.0F)).rotate((Quaternionfc)rotation).mul(scale).add(x, y, z);
/*  92 */     builder.addVertex(scratch.x(), scratch.y(), scratch.z()).setUv(u, v).setColor(color).setLight(lightCoords);
/*     */   }
/*     */   
/*     */   public void submit(SubmitNodeCollector submitNodeCollector, CameraRenderState camera) {
/*  97 */     if (this.particleCount > 0)
/*  98 */       submitNodeCollector.submitQuadParticleGroup(this); 
/*     */   }
/*     */   
/*     */   private static class Storage {
/* 121 */     private int capacity = 1024;
/*     */     
/* 122 */     private float[] floatValues = new float[12288];
/*     */     
/* 123 */     private int[] intValues = new int[2048];
/*     */     
/*     */     private int currentParticleIndex;
/*     */     
/*     */     public void add(float x, float y, float z, float xRot, float yRot, float zRot, float wRot, float scale, float u0, float u1, float v0, float v1, int color, int lightCoords) {
/* 141 */       if (this.currentParticleIndex >= this.capacity)
/* 142 */         grow(); 
/* 144 */       int index = this.currentParticleIndex * 12;
/* 145 */       this.floatValues[index++] = x;
/* 146 */       this.floatValues[index++] = y;
/* 147 */       this.floatValues[index++] = z;
/* 148 */       this.floatValues[index++] = xRot;
/* 149 */       this.floatValues[index++] = yRot;
/* 150 */       this.floatValues[index++] = zRot;
/* 151 */       this.floatValues[index++] = wRot;
/* 152 */       this.floatValues[index++] = scale;
/* 153 */       this.floatValues[index++] = u0;
/* 154 */       this.floatValues[index++] = u1;
/* 155 */       this.floatValues[index++] = v0;
/* 156 */       this.floatValues[index] = v1;
/* 157 */       index = this.currentParticleIndex * 2;
/* 158 */       this.intValues[index++] = color;
/* 159 */       this.intValues[index] = lightCoords;
/* 160 */       this.currentParticleIndex++;
/*     */     }
/*     */     
/*     */     public void forEachParticle(QuadParticleRenderState.ParticleConsumer consumer) {
/* 164 */       for (int particleIndex = 0; particleIndex < this.currentParticleIndex; particleIndex++) {
/* 165 */         int floatIndex = particleIndex * 12;
/* 166 */         int intIndex = particleIndex * 2;
/* 167 */         consumer.consume(this.floatValues[floatIndex++], this.floatValues[floatIndex++], this.floatValues[floatIndex++], this.floatValues[floatIndex++], this.floatValues[floatIndex++], this.floatValues[floatIndex++], this.floatValues[floatIndex++], this.floatValues[floatIndex++], this.floatValues[floatIndex++], this.floatValues[floatIndex++], this.floatValues[floatIndex++], this.floatValues[floatIndex], this.intValues[intIndex++], this.intValues[intIndex]);
/*     */       } 
/*     */     }
/*     */     
/*     */     public void clear() {
/* 187 */       this.currentParticleIndex = 0;
/*     */     }
/*     */     
/*     */     private void grow() {
/* 191 */       this.capacity *= 2;
/* 192 */       this.floatValues = Arrays.copyOf(this.floatValues, this.capacity * 12);
/* 193 */       this.intValues = Arrays.copyOf(this.intValues, this.capacity * 2);
/*     */     }
/*     */     
/*     */     public int count() {
/* 197 */       return this.currentParticleIndex;
/*     */     }
/*     */   }
/*     */   
/*     */   @FunctionalInterface
/*     */   public static interface ParticleConsumer {
/*     */     void consume(float param1Float1, float param1Float2, float param1Float3, float param1Float4, float param1Float5, float param1Float6, float param1Float7, float param1Float8, float param1Float9, float param1Float10, float param1Float11, float param1Float12, int param1Int1, int param1Int2);
/*     */   }
/*     */ }


/* Location:              C:\Users\karol\AppData\Roaming\ModrinthApp\meta\versions\26.2-0.19.5\26.2-0.19.5.jar!\net\minecraft\client\renderer\state\level\QuadParticleRenderState.class
 * Java compiler version: 25 (69.0)
 * JD-Core Version:       1.1.3
 */