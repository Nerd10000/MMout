/*     */ package net.minecraft.client.renderer.state.level;
/*     */ 
/*     */ import java.util.Arrays;
/*     */ 
/*     */ class Storage {
/* 121 */   private int capacity = 1024;
/*     */   
/* 122 */   private float[] floatValues = new float[12288];
/*     */   
/* 123 */   private int[] intValues = new int[2048];
/*     */   
/*     */   private int currentParticleIndex;
/*     */   
/*     */   public void add(float x, float y, float z, float xRot, float yRot, float zRot, float wRot, float scale, float u0, float u1, float v0, float v1, int color, int lightCoords) {
/* 141 */     if (this.currentParticleIndex >= this.capacity)
/* 142 */       grow(); 
/* 144 */     int index = this.currentParticleIndex * 12;
/* 145 */     this.floatValues[index++] = x;
/* 146 */     this.floatValues[index++] = y;
/* 147 */     this.floatValues[index++] = z;
/* 148 */     this.floatValues[index++] = xRot;
/* 149 */     this.floatValues[index++] = yRot;
/* 150 */     this.floatValues[index++] = zRot;
/* 151 */     this.floatValues[index++] = wRot;
/* 152 */     this.floatValues[index++] = scale;
/* 153 */     this.floatValues[index++] = u0;
/* 154 */     this.floatValues[index++] = u1;
/* 155 */     this.floatValues[index++] = v0;
/* 156 */     this.floatValues[index] = v1;
/* 157 */     index = this.currentParticleIndex * 2;
/* 158 */     this.intValues[index++] = color;
/* 159 */     this.intValues[index] = lightCoords;
/* 160 */     this.currentParticleIndex++;
/*     */   }
/*     */   
/*     */   public void forEachParticle(QuadParticleRenderState.ParticleConsumer consumer) {
/* 164 */     for (int particleIndex = 0; particleIndex < this.currentParticleIndex; particleIndex++) {
/* 165 */       int floatIndex = particleIndex * 12;
/* 166 */       int intIndex = particleIndex * 2;
/* 167 */       consumer.consume(this.floatValues[floatIndex++], this.floatValues[floatIndex++], this.floatValues[floatIndex++], this.floatValues[floatIndex++], this.floatValues[floatIndex++], this.floatValues[floatIndex++], this.floatValues[floatIndex++], this.floatValues[floatIndex++], this.floatValues[floatIndex++], this.floatValues[floatIndex++], this.floatValues[floatIndex++], this.floatValues[floatIndex], this.intValues[intIndex++], this.intValues[intIndex]);
/*     */     } 
/*     */   }
/*     */   
/*     */   public void clear() {
/* 187 */     this.currentParticleIndex = 0;
/*     */   }
/*     */   
/*     */   private void grow() {
/* 191 */     this.capacity *= 2;
/* 192 */     this.floatValues = Arrays.copyOf(this.floatValues, this.capacity * 12);
/* 193 */     this.intValues = Arrays.copyOf(this.intValues, this.capacity * 2);
/*     */   }
/*     */   
/*     */   public int count() {
/* 197 */     return this.currentParticleIndex;
/*     */   }
/*     */ }


/* Location:              C:\Users\karol\AppData\Roaming\ModrinthApp\meta\versions\26.2-0.19.5\26.2-0.19.5.jar!\net\minecraft\client\renderer\state\level\QuadParticleRenderState$Storage.class
 * Java compiler version: 25 (69.0)
 * JD-Core Version:       1.1.3
 */