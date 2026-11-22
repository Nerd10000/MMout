/*     */ package net.minecraft.client.particle;
/*     */ 
/*     */ import net.minecraft.client.multiplayer.ClientLevel;
/*     */ import net.minecraft.core.particles.ParticleOptions;
/*     */ import net.minecraft.core.particles.SimpleParticleType;
/*     */ import net.minecraft.util.LightCoordsUtil;
/*     */ import net.minecraft.util.RandomSource;
/*     */ 
/*     */ public class GlowParticle extends SingleQuadParticle {
/*     */   private final SpriteSet sprites;
/*     */   
/*     */   private GlowParticle(ClientLevel level, double x, double y, double z, double xa, double ya, double za, SpriteSet sprites) {
/*  13 */     super(level, x, y, z, xa, ya, za, sprites.first());
/*  14 */     this.friction = 0.96F;
/*  15 */     this.speedUpWhenYMotionIsBlocked = true;
/*  16 */     this.sprites = sprites;
/*  18 */     this.quadSize *= 0.75F;
/*  20 */     this.hasPhysics = false;
/*  21 */     setSpriteFromAge(sprites);
/*     */   }
/*     */   
/*     */   public SingleQuadParticle.Layer getLayer() {
/*  26 */     return SingleQuadParticle.Layer.OPAQUE;
/*     */   }
/*     */   
/*     */   public int getLightCoords(float a) {
/*  31 */     return LightCoordsUtil.addSmoothBlockEmission(super.getLightCoords(a), (this.age + a) / this.lifetime);
/*     */   }
/*     */   
/*     */   public void tick() {
/*  36 */     super.tick();
/*  37 */     setSpriteFromAge(this.sprites);
/*     */   }
/*     */   
/*     */   public static class GlowSquidProvider implements ParticleProvider<SimpleParticleType> {
/*     */     private final SpriteSet sprite;
/*     */     
/*     */     public GlowSquidProvider(SpriteSet sprite) {
/*  44 */       this.sprite = sprite;
/*     */     }
/*     */     
/*     */     public Particle createParticle(SimpleParticleType options, ClientLevel level, double x, double y, double z, double xAux, double yAux, double zAux, RandomSource random) {
/*  49 */       GlowParticle glowParticle = new GlowParticle(level, x, y, z, 0.5D - random.nextDouble(), yAux, 0.5D - random.nextDouble(), this.sprite);
/*  50 */       if (random.nextBoolean()) {
/*  51 */         glowParticle.setColor(0.6F, 1.0F, 0.8F);
/*     */       } else {
/*  53 */         glowParticle.setColor(0.08F, 0.4F, 0.4F);
/*     */       } 
/*  55 */       glowParticle.yd *= 0.20000000298023224D;
/*  56 */       if (xAux == 0.0D && zAux == 0.0D) {
/*  57 */         glowParticle.xd *= 0.10000000149011612D;
/*  58 */         glowParticle.zd *= 0.10000000149011612D;
/*     */       } 
/*  60 */       glowParticle.setLifetime((int)(8.0D / (random.nextDouble() * 0.8D + 0.2D)));
/*  61 */       return glowParticle;
/*     */     }
/*     */   }
/*     */   
/*     */   public static class WaxOnProvider implements ParticleProvider<SimpleParticleType> {
/*     */     private static final double SPEED_FACTOR = 0.01D;
/*     */     
/*     */     private final SpriteSet sprite;
/*     */     
/*     */     public WaxOnProvider(SpriteSet sprite) {
/*  70 */       this.sprite = sprite;
/*     */     }
/*     */     
/*     */     public Particle createParticle(SimpleParticleType options, ClientLevel level, double x, double y, double z, double xAux, double yAux, double zAux, RandomSource random) {
/*  75 */       GlowParticle glowParticle = new GlowParticle(level, x, y, z, 0.0D, 0.0D, 0.0D, this.sprite);
/*  76 */       glowParticle.setColor(0.91F, 0.55F, 0.08F);
/*  78 */       glowParticle.setParticleSpeed(xAux * 0.01D / 2.0D, yAux * 0.01D, zAux * 0.01D / 2.0D);
/*  84 */       int minLifespan = 10;
/*  85 */       int maxLifespan = 40;
/*  86 */       glowParticle.setLifetime(random.nextInt(30) + 10);
/*  87 */       return glowParticle;
/*     */     }
/*     */   }
/*     */   
/*     */   public static class WaxOffProvider implements ParticleProvider<SimpleParticleType> {
/*     */     private static final double SPEED_FACTOR = 0.01D;
/*     */     
/*     */     private final SpriteSet sprite;
/*     */     
/*     */     public WaxOffProvider(SpriteSet sprite) {
/*  96 */       this.sprite = sprite;
/*     */     }
/*     */     
/*     */     public Particle createParticle(SimpleParticleType options, ClientLevel level, double x, double y, double z, double xAux, double yAux, double zAux, RandomSource random) {
/* 101 */       GlowParticle glowParticle = new GlowParticle(level, x, y, z, 0.0D, 0.0D, 0.0D, this.sprite);
/* 102 */       glowParticle.setColor(1.0F, 0.9F, 1.0F);
/* 104 */       glowParticle.setParticleSpeed(xAux * 0.01D / 2.0D, yAux * 0.01D, zAux * 0.01D / 2.0D);
/* 110 */       int minLifespan = 10;
/* 111 */       int maxLifespan = 40;
/* 112 */       glowParticle.setLifetime(random.nextInt(30) + 10);
/* 113 */       return glowParticle;
/*     */     }
/*     */   }
/*     */   
/*     */   public static class ElectricSparkProvider implements ParticleProvider<SimpleParticleType> {
/*     */     private static final double SPEED_FACTOR = 0.25D;
/*     */     
/*     */     private final SpriteSet sprite;
/*     */     
/*     */     public ElectricSparkProvider(SpriteSet sprite) {
/* 122 */       this.sprite = sprite;
/*     */     }
/*     */     
/*     */     public Particle createParticle(SimpleParticleType options, ClientLevel level, double x, double y, double z, double xAux, double yAux, double zAux, RandomSource random) {
/* 127 */       GlowParticle glowParticle = new GlowParticle(level, x, y, z, 0.0D, 0.0D, 0.0D, this.sprite);
/* 128 */       glowParticle.setColor(1.0F, 0.9F, 1.0F);
/* 130 */       glowParticle.setParticleSpeed(xAux * 0.25D, yAux * 0.25D, zAux * 0.25D);
/* 136 */       int minLifespan = 2;
/* 137 */       int maxLifespan = 4;
/* 138 */       glowParticle.setLifetime(random.nextInt(2) + 2);
/* 139 */       return glowParticle;
/*     */     }
/*     */   }
/*     */   
/*     */   public static class ScrapeProvider implements ParticleProvider<SimpleParticleType> {
/*     */     private static final double SPEED_FACTOR = 0.01D;
/*     */     
/*     */     private final SpriteSet sprite;
/*     */     
/*     */     public ScrapeProvider(SpriteSet sprite) {
/* 148 */       this.sprite = sprite;
/*     */     }
/*     */     
/*     */     public Particle createParticle(SimpleParticleType options, ClientLevel level, double x, double y, double z, double xAux, double yAux, double zAux, RandomSource random) {
/* 153 */       GlowParticle glowParticle = new GlowParticle(level, x, y, z, 0.0D, 0.0D, 0.0D, this.sprite);
/* 154 */       if (random.nextBoolean()) {
/* 155 */         glowParticle.setColor(0.29F, 0.58F, 0.51F);
/*     */       } else {
/* 157 */         glowParticle.setColor(0.43F, 0.77F, 0.62F);
/*     */       } 
/* 160 */       glowParticle.setParticleSpeed(xAux * 0.01D, yAux * 0.01D, zAux * 0.01D);
/* 166 */       int minLifespan = 10;
/* 167 */       int maxLifespan = 40;
/* 168 */       glowParticle.setLifetime(random.nextInt(30) + 10);
/* 169 */       return glowParticle;
/*     */     }
/*     */   }
/*     */ }


/* Location:              C:\Users\karol\AppData\Roaming\ModrinthApp\meta\versions\26.2-0.19.5\26.2-0.19.5.jar!\net\minecraft\client\particle\GlowParticle.class
 * Java compiler version: 25 (69.0)
 * JD-Core Version:       1.1.3
 */