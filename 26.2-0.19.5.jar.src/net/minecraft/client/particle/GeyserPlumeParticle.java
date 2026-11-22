/*     */ package net.minecraft.client.particle;
/*     */ 
/*     */ import net.minecraft.client.multiplayer.ClientLevel;
/*     */ import net.minecraft.core.particles.GeyserParticleOptions;
/*     */ import net.minecraft.core.particles.ParticleOptions;
/*     */ import net.minecraft.util.RandomSource;
/*     */ 
/*     */ public class GeyserPlumeParticle extends SingleQuadParticle {
/*     */   private static final float NO_FRICTION = 1.0F;
/*     */   
/*     */   private static final float MAX_FRICTION = 0.0F;
/*     */   
/*     */   private static final float INITIAL_PROPULSION_FACTOR = 1.45F;
/*     */   
/*     */   private static final float GRAVITY_EXPONENT = 3.0F;
/*     */   
/*     */   private static final float GRADUAL_GRAVITY_FACTOR = 0.12F;
/*     */   
/*     */   private static final float INITIAL_SPRAY_SPREAD = 0.2F;
/*     */   
/*     */   private static final float LINEAR_SPRAY_SPREAD = 0.2F;
/*     */   
/*     */   private static final int REMAINING_FRAMES_AFTER_REACHING_MAX_HEIGHT = 5;
/*     */   
/*     */   private static final float MIN_SCALE_FACTOR = 2.0F;
/*     */   
/*     */   private static final float MAX_SCALE_FACTOR = 3.0F;
/*     */   
/*     */   private final SpriteSet sprites;
/*     */   
/*     */   private final double startY;
/*     */   
/*     */   private final double maxY;
/*     */   
/*     */   private final float initialPropulsion;
/*     */   
/*     */   private final float horizontalSprayX;
/*     */   
/*     */   private final float horizontalSprayZ;
/*     */   
/*     */   private final float minSize;
/*     */   
/*     */   private final float maxSize;
/*     */   
/*     */   private boolean done;
/*     */   
/*     */   private GeyserPlumeParticle(ClientLevel level, double x, double y, double z, double xa, double ya, double za, GeyserParticleOptions options, SpriteSet sprites) {
/*  38 */     super(level, x, y, z, xa, ya, za, sprites.first());
/*  39 */     int plumeHeight = 5 * Math.max(1, options.waterBlocks());
/*  41 */     this.hasPhysics = true;
/*  42 */     this.speedUpWhenYMotionIsBlocked = true;
/*  43 */     this.lifetime = plumeHeight * 5;
/*  45 */     this.yd = 0.0D;
/*  46 */     this.startY = y;
/*  47 */     this.maxY = this.startY + plumeHeight - 1.0D;
/*  49 */     this.horizontalSprayX = (level.getRandom().nextFloat() - 0.5F) * 0.2F;
/*  50 */     this.horizontalSprayZ = (level.getRandom().nextFloat() - 0.5F) * 0.2F;
/*  52 */     this.friction = 1.0F;
/*  53 */     this.initialPropulsion = ((options.waterBlocks() == 1) ? 1.5F : 1.0F) * plumeHeight * 1.45F;
/*  54 */     this.gravity = -this.initialPropulsion;
/*  56 */     float initiallyRandomizedSize = this.quadSize * 0.75F;
/*  57 */     this.minSize = initiallyRandomizedSize * (2.0F + plumeHeight / 8.0F);
/*  58 */     this.maxSize = initiallyRandomizedSize * (3.0F + plumeHeight / 8.0F);
/*  59 */     this.quadSize = this.minSize;
/*  61 */     this.sprites = sprites;
/*  62 */     setSpriteFromAge(sprites);
/*     */   }
/*     */   
/*     */   public void tick() {
/*  67 */     super.tick();
/*  70 */     if (!this.done && (this.yd < 0.0D || this.y > this.maxY || this.y == this.yo)) {
/*  71 */       this.lifetime = Math.min(this.lifetime, this.age + 5);
/*  72 */       this.friction = 0.0F;
/*  73 */       this.done = true;
/*     */     } 
/*  76 */     double yProgressLinear = Math.clamp((this.y - this.startY) / (this.maxY - this.startY), 0.0D, 1.0D);
/*  77 */     double yProgressExponential = Math.pow(yProgressLinear, 3.0D);
/*  80 */     this.gravity = this.initialPropulsion * (float)yProgressExponential * 0.12F;
/*  83 */     this.xd = yProgressLinear * this.horizontalSprayX;
/*  84 */     this.zd = yProgressLinear * this.horizontalSprayZ;
/*  87 */     setSpriteFromAge(this.sprites);
/*  88 */     this.quadSize = this.minSize + (float)(yProgressLinear * (this.maxSize - this.minSize));
/*     */   }
/*     */   
/*     */   protected SingleQuadParticle.Layer getLayer() {
/*  93 */     return SingleQuadParticle.Layer.OPAQUE;
/*     */   }
/*     */   
/*     */   public static class Provider implements ParticleProvider<GeyserParticleOptions> {
/*     */     private final SpriteSet sprites;
/*     */     
/*     */     public Provider(SpriteSet sprites) {
/* 100 */       this.sprites = sprites;
/*     */     }
/*     */     
/*     */     public Particle createParticle(GeyserParticleOptions options, ClientLevel level, double x, double y, double z, double xAux, double yAux, double zAux, RandomSource random) {
/* 105 */       double randomX = x + ((random.nextFloat() - 0.5F) * 0.2F);
/* 106 */       double randomY = y + random.nextFloat();
/* 107 */       double randomZ = z + ((random.nextFloat() - 0.5F) * 0.2F);
/* 108 */       return new GeyserPlumeParticle(level, randomX, randomY, randomZ, xAux, yAux, zAux, options, this.sprites);
/*     */     }
/*     */   }
/*     */ }


/* Location:              C:\Users\karol\AppData\Roaming\ModrinthApp\meta\versions\26.2-0.19.5\26.2-0.19.5.jar!\net\minecraft\client\particle\GeyserPlumeParticle.class
 * Java compiler version: 25 (69.0)
 * JD-Core Version:       1.1.3
 */