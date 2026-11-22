/*     */ package net.minecraft.client.particle;
/*     */ 
/*     */ import net.minecraft.client.multiplayer.ClientLevel;
/*     */ import net.minecraft.client.renderer.texture.TextureAtlasSprite;
/*     */ import net.minecraft.core.particles.ColorParticleOption;
/*     */ import net.minecraft.core.particles.ParticleOptions;
/*     */ import net.minecraft.core.particles.SimpleParticleType;
/*     */ import net.minecraft.util.RandomSource;
/*     */ 
/*     */ public class FallingLeavesParticle extends SingleQuadParticle {
/*     */   private static final float ACCELERATION_SCALE = 0.0025F;
/*     */   
/*     */   private static final int INITIAL_LIFETIME = 300;
/*     */   
/*     */   private static final int CURVE_ENDPOINT_TIME = 300;
/*     */   
/*     */   private float rotSpeed;
/*     */   
/*     */   private final float spinAcceleration;
/*     */   
/*     */   private final float windBig;
/*     */   
/*     */   private final boolean swirl;
/*     */   
/*     */   private final boolean flowAway;
/*     */   
/*     */   private final double xaFlowScale;
/*     */   
/*     */   private final double zaFlowScale;
/*     */   
/*     */   private final double swirlPeriod;
/*     */   
/*     */   protected FallingLeavesParticle(ClientLevel level, double x, double y, double z, TextureAtlasSprite sprite, float fallAcceleration, float sideAcceleration, boolean swirl, boolean flowAway, float scale, float startVelocity) {
/*  33 */     super(level, x, y, z, sprite);
/*  36 */     this.rotSpeed = (float)Math.toRadians(this.random.nextBoolean() ? -30.0D : 30.0D);
/*  37 */     this.spinAcceleration = (float)Math.toRadians(this.random.nextBoolean() ? -5.0D : 5.0D);
/*  38 */     this.windBig = sideAcceleration;
/*  39 */     this.swirl = swirl;
/*  40 */     this.flowAway = flowAway;
/*  43 */     this.lifetime = 300;
/*  45 */     this.gravity = fallAcceleration * 1.2F * 0.0025F;
/*  48 */     float size = scale * (this.random.nextBoolean() ? 0.05F : 0.075F);
/*  49 */     this.quadSize = size;
/*  50 */     setSize(size, size);
/*  53 */     this.friction = 1.0F;
/*  54 */     this.yd = -startVelocity;
/*  57 */     float particleRandom = this.random.nextFloat();
/*  58 */     this.xaFlowScale = Math.cos(Math.toRadians((particleRandom * 60.0F))) * this.windBig;
/*  59 */     this.zaFlowScale = Math.sin(Math.toRadians((particleRandom * 60.0F))) * this.windBig;
/*  60 */     this.swirlPeriod = Math.toRadians((1000.0F + particleRandom * 3000.0F));
/*     */   }
/*     */   
/*     */   public SingleQuadParticle.Layer getLayer() {
/*  65 */     return SingleQuadParticle.Layer.OPAQUE;
/*     */   }
/*     */   
/*     */   public void tick() {
/*  71 */     this.xo = this.x;
/*  72 */     this.yo = this.y;
/*  73 */     this.zo = this.z;
/*  75 */     if (this.lifetime-- <= 0)
/*  76 */       remove(); 
/*  78 */     if (this.removed)
/*     */       return; 
/*  83 */     float aliveTicks = (300 - this.lifetime);
/*  84 */     float relativeAge = Math.min(aliveTicks / 300.0F, 1.0F);
/*  86 */     double xa = 0.0D;
/*  87 */     double za = 0.0D;
/*  88 */     if (this.flowAway) {
/*  89 */       xa += this.xaFlowScale * Math.pow(relativeAge, 1.25D);
/*  90 */       za += this.zaFlowScale * Math.pow(relativeAge, 1.25D);
/*     */     } 
/*  92 */     if (this.swirl) {
/*  93 */       xa += relativeAge * Math.cos(relativeAge * this.swirlPeriod) * this.windBig;
/*  94 */       za += relativeAge * Math.sin(relativeAge * this.swirlPeriod) * this.windBig;
/*     */     } 
/*  97 */     this.xd += xa * 0.0024999999441206455D;
/*  98 */     this.zd += za * 0.0024999999441206455D;
/* 101 */     this.yd -= this.gravity;
/* 103 */     this.rotSpeed += this.spinAcceleration / 20.0F;
/* 105 */     this.oRoll = this.roll;
/* 106 */     this.roll += this.rotSpeed / 20.0F;
/* 109 */     move(this.xd, this.yd, this.zd);
/* 112 */     if (this.onGround || (this.lifetime < 299 && (this.xd == 0.0D || this.zd == 0.0D)))
/* 113 */       remove(); 
/* 115 */     if (this.removed)
/*     */       return; 
/* 119 */     this.xd *= this.friction;
/* 120 */     this.yd *= this.friction;
/* 121 */     this.zd *= this.friction;
/*     */   }
/*     */   
/*     */   public static class CherryProvider implements ParticleProvider<SimpleParticleType> {
/*     */     private final SpriteSet sprites;
/*     */     
/*     */     public CherryProvider(SpriteSet sprites) {
/* 128 */       this.sprites = sprites;
/*     */     }
/*     */     
/*     */     public Particle createParticle(SimpleParticleType options, ClientLevel level, double x, double y, double z, double xAux, double yAux, double zAux, RandomSource random) {
/* 133 */       return new FallingLeavesParticle(level, x, y, z, this.sprites.get(random), 0.25F, 2.0F, false, true, 1.0F, 0.0F);
/*     */     }
/*     */   }
/*     */   
/*     */   public static class PaleOakProvider implements ParticleProvider<SimpleParticleType> {
/*     */     private final SpriteSet sprites;
/*     */     
/*     */     public PaleOakProvider(SpriteSet sprites) {
/* 141 */       this.sprites = sprites;
/*     */     }
/*     */     
/*     */     public Particle createParticle(SimpleParticleType options, ClientLevel level, double x, double y, double z, double xAux, double yAux, double zAux, RandomSource random) {
/* 146 */       return new FallingLeavesParticle(level, x, y, z, this.sprites.get(random), 0.07F, 10.0F, true, false, 2.0F, 0.021F);
/*     */     }
/*     */   }
/*     */   
/*     */   public static class TintedLeavesProvider implements ParticleProvider<ColorParticleOption> {
/*     */     private final SpriteSet sprites;
/*     */     
/*     */     public TintedLeavesProvider(SpriteSet sprites) {
/* 154 */       this.sprites = sprites;
/*     */     }
/*     */     
/*     */     public Particle createParticle(ColorParticleOption options, ClientLevel level, double x, double y, double z, double xAux, double yAux, double zAux, RandomSource random) {
/* 159 */       FallingLeavesParticle particle = new FallingLeavesParticle(level, x, y, z, this.sprites.get(random), 0.07F, 10.0F, true, false, 2.0F, 0.021F);
/* 160 */       particle.setColor(options.getRed(), options.getGreen(), options.getBlue());
/* 161 */       return particle;
/*     */     }
/*     */   }
/*     */ }


/* Location:              C:\Users\karol\AppData\Roaming\ModrinthApp\meta\versions\26.2-0.19.5\26.2-0.19.5.jar!\net\minecraft\client\particle\FallingLeavesParticle.class
 * Java compiler version: 25 (69.0)
 * JD-Core Version:       1.1.3
 */