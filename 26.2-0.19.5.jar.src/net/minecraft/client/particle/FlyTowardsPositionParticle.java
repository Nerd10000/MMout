/*     */ package net.minecraft.client.particle;
/*     */ 
/*     */ import net.minecraft.client.Camera;
/*     */ import net.minecraft.client.multiplayer.ClientLevel;
/*     */ import net.minecraft.client.renderer.state.level.QuadParticleRenderState;
/*     */ import net.minecraft.client.renderer.texture.TextureAtlasSprite;
/*     */ import net.minecraft.core.particles.ParticleOptions;
/*     */ import net.minecraft.core.particles.SimpleParticleType;
/*     */ import net.minecraft.util.LightCoordsUtil;
/*     */ import net.minecraft.util.RandomSource;
/*     */ 
/*     */ public class FlyTowardsPositionParticle extends SingleQuadParticle {
/*     */   private final double xStart;
/*     */   
/*     */   private final double yStart;
/*     */   
/*     */   private final double zStart;
/*     */   
/*     */   private final boolean isGlowing;
/*     */   
/*     */   private final Particle.LifetimeAlpha lifetimeAlpha;
/*     */   
/*     */   private FlyTowardsPositionParticle(ClientLevel level, double x, double y, double z, double xd, double yd, double zd, TextureAtlasSprite sprite) {
/*  20 */     this(level, x, y, z, xd, yd, zd, false, Particle.LifetimeAlpha.ALWAYS_OPAQUE, sprite);
/*     */   }
/*     */   
/*     */   private FlyTowardsPositionParticle(ClientLevel level, double x, double y, double z, double xd, double yd, double zd, boolean isGlowing, Particle.LifetimeAlpha lifetimeAlpha, TextureAtlasSprite sprite) {
/*  24 */     super(level, x, y, z, sprite);
/*  25 */     this.isGlowing = isGlowing;
/*  26 */     this.lifetimeAlpha = lifetimeAlpha;
/*  27 */     setAlpha(lifetimeAlpha.startAlpha());
/*  29 */     this.xd = xd;
/*  30 */     this.yd = yd;
/*  31 */     this.zd = zd;
/*  32 */     this.xStart = x;
/*  33 */     this.yStart = y;
/*  34 */     this.zStart = z;
/*  35 */     this.xo = x + xd;
/*  36 */     this.yo = y + yd;
/*  37 */     this.zo = z + zd;
/*  38 */     this.x = this.xo;
/*  39 */     this.y = this.yo;
/*  40 */     this.z = this.zo;
/*  42 */     this.quadSize = 0.1F * (this.random.nextFloat() * 0.5F + 0.2F);
/*  44 */     float br = this.random.nextFloat() * 0.6F + 0.4F;
/*  45 */     this.rCol = 0.9F * br;
/*  46 */     this.gCol = 0.9F * br;
/*  47 */     this.bCol = br;
/*  49 */     this.hasPhysics = false;
/*  51 */     this.lifetime = (int)(this.random.nextFloat() * 10.0F) + 30;
/*     */   }
/*     */   
/*     */   public SingleQuadParticle.Layer getLayer() {
/*  56 */     if (this.lifetimeAlpha.isOpaque())
/*  57 */       return SingleQuadParticle.Layer.OPAQUE; 
/*  59 */     return SingleQuadParticle.Layer.TRANSLUCENT;
/*     */   }
/*     */   
/*     */   public void move(double xa, double ya, double za) {
/*  64 */     setBoundingBox(getBoundingBox().move(xa, ya, za));
/*  65 */     setLocationFromBoundingbox();
/*     */   }
/*     */   
/*     */   public int getLightCoords(float a) {
/*  70 */     if (this.isGlowing)
/*  71 */       return LightCoordsUtil.withBlock(super.getLightCoords(a), 15); 
/*  73 */     float brightness = this.age / this.lifetime;
/*  74 */     brightness *= brightness;
/*  75 */     brightness *= brightness;
/*  76 */     return LightCoordsUtil.addSmoothBlockEmission(super.getLightCoords(a), brightness);
/*     */   }
/*     */   
/*     */   public void tick() {
/*  81 */     this.xo = this.x;
/*  82 */     this.yo = this.y;
/*  83 */     this.zo = this.z;
/*  85 */     if (this.age++ >= this.lifetime) {
/*  86 */       remove();
/*     */       return;
/*     */     } 
/*  90 */     float pos = this.age / this.lifetime;
/*  91 */     pos = 1.0F - pos;
/*  93 */     float pp = 1.0F - pos;
/*  94 */     pp *= pp;
/*  95 */     pp *= pp;
/*  96 */     this.x = this.xStart + this.xd * pos;
/*  97 */     this.y = this.yStart + this.yd * pos - (pp * 1.2F);
/*  98 */     this.z = this.zStart + this.zd * pos;
/*     */   }
/*     */   
/*     */   public void extract(QuadParticleRenderState particleTypeRenderState, Camera camera, float partialTickTime) {
/* 103 */     setAlpha(this.lifetimeAlpha.currentAlphaForAge(this.age, this.lifetime, partialTickTime));
/* 104 */     super.extract(particleTypeRenderState, camera, partialTickTime);
/*     */   }
/*     */   
/*     */   public static class EnchantProvider implements ParticleProvider<SimpleParticleType> {
/*     */     private final SpriteSet sprite;
/*     */     
/*     */     public EnchantProvider(SpriteSet sprite) {
/* 111 */       this.sprite = sprite;
/*     */     }
/*     */     
/*     */     public Particle createParticle(SimpleParticleType options, ClientLevel level, double x, double y, double z, double xAux, double yAux, double zAux, RandomSource random) {
/* 116 */       FlyTowardsPositionParticle particle = new FlyTowardsPositionParticle(level, x, y, z, xAux, yAux, zAux, this.sprite.get(random));
/* 117 */       return particle;
/*     */     }
/*     */   }
/*     */   
/*     */   public static class NautilusProvider implements ParticleProvider<SimpleParticleType> {
/*     */     private final SpriteSet sprite;
/*     */     
/*     */     public NautilusProvider(SpriteSet sprite) {
/* 125 */       this.sprite = sprite;
/*     */     }
/*     */     
/*     */     public Particle createParticle(SimpleParticleType options, ClientLevel level, double x, double y, double z, double xAux, double yAux, double zAux, RandomSource random) {
/* 130 */       FlyTowardsPositionParticle particle = new FlyTowardsPositionParticle(level, x, y, z, xAux, yAux, zAux, this.sprite.get(random));
/* 131 */       return particle;
/*     */     }
/*     */   }
/*     */   
/*     */   public static class VaultConnectionProvider implements ParticleProvider<SimpleParticleType> {
/*     */     private final SpriteSet sprite;
/*     */     
/*     */     public VaultConnectionProvider(SpriteSet sprite) {
/* 139 */       this.sprite = sprite;
/*     */     }
/*     */     
/*     */     public Particle createParticle(SimpleParticleType options, ClientLevel level, double x, double y, double z, double xAux, double yAux, double zAux, RandomSource random) {
/* 144 */       FlyTowardsPositionParticle particle = new FlyTowardsPositionParticle(level, x, y, z, xAux, yAux, zAux, true, new Particle.LifetimeAlpha(0.0F, 0.6F, 0.25F, 1.0F), this.sprite.get(random));
/* 145 */       particle.scale(1.5F);
/* 146 */       return particle;
/*     */     }
/*     */   }
/*     */ }


/* Location:              C:\Users\karol\AppData\Roaming\ModrinthApp\meta\versions\26.2-0.19.5\26.2-0.19.5.jar!\net\minecraft\client\particle\FlyTowardsPositionParticle.class
 * Java compiler version: 25 (69.0)
 * JD-Core Version:       1.1.3
 */