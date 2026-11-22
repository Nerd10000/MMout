/*     */ package net.minecraft.client.particle;
/*     */ 
/*     */ import net.minecraft.client.Camera;
/*     */ import net.minecraft.client.multiplayer.ClientLevel;
/*     */ import net.minecraft.client.renderer.state.level.QuadParticleRenderState;
/*     */ 
/*     */ class SparkParticle extends SimpleAnimatedParticle {
/*     */   private boolean trail;
/*     */   
/*     */   private boolean twinkle;
/*     */   
/*     */   private final ParticleEngine engine;
/*     */   
/*     */   private float fadeR;
/*     */   
/*     */   private float fadeG;
/*     */   
/*     */   private float fadeB;
/*     */   
/*     */   private boolean hasFade;
/*     */   
/*     */   private SparkParticle(ClientLevel level, double x, double y, double z, double xa, double ya, double za, ParticleEngine engine, SpriteSet sprites) {
/* 254 */     super(level, x, y, z, sprites, 0.1F);
/* 255 */     this.xd = xa;
/* 256 */     this.yd = ya;
/* 257 */     this.zd = za;
/* 258 */     this.engine = engine;
/* 260 */     this.quadSize *= 0.75F;
/* 262 */     this.lifetime = 48 + this.random.nextInt(12);
/* 263 */     setSpriteFromAge(sprites);
/*     */   }
/*     */   
/*     */   public void setTrail(boolean trail) {
/* 267 */     this.trail = trail;
/*     */   }
/*     */   
/*     */   public void setTwinkle(boolean twinkle) {
/* 271 */     this.twinkle = twinkle;
/*     */   }
/*     */   
/*     */   public void extract(QuadParticleRenderState particleTypeRenderState, Camera camera, float partialTickTime) {
/* 276 */     if (!this.twinkle || this.age < this.lifetime / 3 || (this.age + this.lifetime) / 3 % 2 == 0)
/* 277 */       super.extract(particleTypeRenderState, camera, partialTickTime); 
/*     */   }
/*     */   
/*     */   public void tick() {
/* 283 */     super.tick();
/* 285 */     if (this.trail && this.age < this.lifetime / 2 && (this.age + this.lifetime) % 2 == 0) {
/* 286 */       SparkParticle sparkParticle = new SparkParticle(this.level, this.x, this.y, this.z, 0.0D, 0.0D, 0.0D, this.engine, this.sprites);
/* 287 */       sparkParticle.setAlpha(0.99F);
/* 288 */       sparkParticle.setColor(this.rCol, this.gCol, this.bCol);
/* 289 */       sparkParticle.age = sparkParticle.lifetime / 2;
/* 290 */       if (this.hasFade) {
/* 291 */         sparkParticle.hasFade = true;
/* 292 */         sparkParticle.fadeR = this.fadeR;
/* 293 */         sparkParticle.fadeG = this.fadeG;
/* 294 */         sparkParticle.fadeB = this.fadeB;
/*     */       } 
/* 296 */       sparkParticle.twinkle = this.twinkle;
/* 297 */       this.engine.add(sparkParticle);
/*     */     } 
/*     */   }
/*     */ }


/* Location:              C:\Users\karol\AppData\Roaming\ModrinthApp\meta\versions\26.2-0.19.5\26.2-0.19.5.jar!\net\minecraft\client\particle\FireworkParticles$SparkParticle.class
 * Java compiler version: 25 (69.0)
 * JD-Core Version:       1.1.3
 */