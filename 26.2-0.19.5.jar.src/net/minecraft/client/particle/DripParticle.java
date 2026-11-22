/*     */ package net.minecraft.client.particle;
/*     */ 
/*     */ import net.minecraft.client.multiplayer.ClientLevel;
/*     */ import net.minecraft.client.renderer.texture.TextureAtlasSprite;
/*     */ import net.minecraft.core.BlockPos;
/*     */ import net.minecraft.core.particles.ParticleOptions;
/*     */ import net.minecraft.core.particles.ParticleTypes;
/*     */ import net.minecraft.core.particles.SimpleParticleType;
/*     */ import net.minecraft.sounds.SoundEvent;
/*     */ import net.minecraft.sounds.SoundEvents;
/*     */ import net.minecraft.sounds.SoundSource;
/*     */ import net.minecraft.util.LightCoordsUtil;
/*     */ import net.minecraft.util.Mth;
/*     */ import net.minecraft.util.RandomSource;
/*     */ import net.minecraft.world.level.BlockGetter;
/*     */ import net.minecraft.world.level.material.Fluid;
/*     */ import net.minecraft.world.level.material.FluidState;
/*     */ import net.minecraft.world.level.material.Fluids;
/*     */ 
/*     */ public class DripParticle extends SingleQuadParticle {
/*     */   private final Fluid type;
/*     */   
/*     */   protected boolean isGlowing;
/*     */   
/*     */   private static class DripHangParticle extends DripParticle {
/*     */     private final ParticleOptions fallingParticle;
/*     */     
/*     */     private DripHangParticle(ClientLevel level, double x, double y, double z, Fluid type, ParticleOptions fallingParticle, TextureAtlasSprite sprite) {
/*  26 */       super(level, x, y, z, type, sprite);
/*  27 */       this.fallingParticle = fallingParticle;
/*  28 */       this.gravity *= 0.02F;
/*  29 */       this.lifetime = 40;
/*     */     }
/*     */     
/*     */     protected void preMoveUpdate() {
/*  34 */       if (this.lifetime-- <= 0) {
/*  35 */         remove();
/*  36 */         this.level.addParticle(this.fallingParticle, this.x, this.y, this.z, this.xd, this.yd, this.zd);
/*     */       } 
/*     */     }
/*     */     
/*     */     protected void postMoveUpdate() {
/*  42 */       this.xd *= 0.02D;
/*  43 */       this.yd *= 0.02D;
/*  44 */       this.zd *= 0.02D;
/*     */     }
/*     */   }
/*     */   
/*     */   private static class CoolingDripHangParticle extends DripHangParticle {
/*     */     private CoolingDripHangParticle(ClientLevel level, double x, double y, double z, Fluid type, ParticleOptions fallingParticle, TextureAtlasSprite sprite) {
/*  50 */       super(level, x, y, z, type, fallingParticle, sprite);
/*     */     }
/*     */     
/*     */     protected void preMoveUpdate() {
/*  55 */       this.rCol = 1.0F;
/*  56 */       this.gCol = 16.0F / (40 - this.lifetime + 16);
/*  57 */       this.bCol = 4.0F / (40 - this.lifetime + 8);
/*  58 */       super.preMoveUpdate();
/*     */     }
/*     */   }
/*     */   
/*     */   private static class FallAndLandParticle extends FallingParticle {
/*     */     protected final ParticleOptions landParticle;
/*     */     
/*     */     private FallAndLandParticle(ClientLevel level, double x, double y, double z, Fluid type, ParticleOptions landParticle, TextureAtlasSprite sprite) {
/*  66 */       super(level, x, y, z, type, sprite);
/*  67 */       this.lifetime = (int)(64.0D / (this.random.nextFloat() * 0.8D + 0.2D));
/*  68 */       this.landParticle = landParticle;
/*     */     }
/*     */     
/*     */     protected void postMoveUpdate() {
/*  73 */       if (this.onGround) {
/*  74 */         remove();
/*  75 */         this.level.addParticle(this.landParticle, this.x, this.y, this.z, 0.0D, 0.0D, 0.0D);
/*     */       } 
/*     */     }
/*     */   }
/*     */   
/*     */   private static class HoneyFallAndLandParticle extends FallAndLandParticle {
/*     */     private HoneyFallAndLandParticle(ClientLevel level, double x, double y, double z, Fluid type, ParticleOptions landParticle, TextureAtlasSprite sprite) {
/*  82 */       super(level, x, y, z, type, landParticle, sprite);
/*     */     }
/*     */     
/*     */     protected void postMoveUpdate() {
/*  87 */       if (this.onGround) {
/*  88 */         remove();
/*  89 */         this.level.addParticle(this.landParticle, this.x, this.y, this.z, 0.0D, 0.0D, 0.0D);
/*  90 */         float volume = Mth.randomBetween(this.random, 0.3F, 1.0F);
/*  91 */         this.level.playLocalSound(this.x, this.y, this.z, SoundEvents.BEEHIVE_DRIP, SoundSource.BLOCKS, volume, 1.0F, false);
/*     */       } 
/*     */     }
/*     */   }
/*     */   
/*     */   private static class DripstoneFallAndLandParticle extends FallAndLandParticle {
/*     */     private DripstoneFallAndLandParticle(ClientLevel level, double x, double y, double z, Fluid type, ParticleOptions landParticle, TextureAtlasSprite sprite) {
/*  98 */       super(level, x, y, z, type, landParticle, sprite);
/*     */     }
/*     */     
/*     */     protected void postMoveUpdate() {
/* 103 */       if (this.onGround) {
/* 104 */         remove();
/* 105 */         this.level.addParticle(this.landParticle, this.x, this.y, this.z, 0.0D, 0.0D, 0.0D);
/* 106 */         SoundEvent sound = (getType() == Fluids.LAVA) ? SoundEvents.POINTED_DRIPSTONE_DRIP_LAVA : SoundEvents.POINTED_DRIPSTONE_DRIP_WATER;
/* 107 */         float volume = Mth.randomBetween(this.random, 0.3F, 1.0F);
/* 108 */         this.level.playLocalSound(this.x, this.y, this.z, sound, SoundSource.BLOCKS, volume, 1.0F, false);
/*     */       } 
/*     */     }
/*     */   }
/*     */   
/*     */   private static class FallingParticle extends DripParticle {
/*     */     private FallingParticle(ClientLevel level, double x, double y, double z, Fluid type, TextureAtlasSprite sprite) {
/* 115 */       super(level, x, y, z, type, sprite);
/*     */     }
/*     */     
/*     */     protected void postMoveUpdate() {
/* 120 */       if (this.onGround)
/* 121 */         remove(); 
/*     */     }
/*     */   }
/*     */   
/*     */   private static class DripLandParticle extends DripParticle {
/*     */     private DripLandParticle(ClientLevel level, double x, double y, double z, Fluid type, TextureAtlasSprite sprite) {
/* 128 */       super(level, x, y, z, type, sprite);
/* 129 */       this.lifetime = (int)(16.0D / (this.random.nextFloat() * 0.8D + 0.2D));
/*     */     }
/*     */   }
/*     */   
/*     */   private DripParticle(ClientLevel level, double x, double y, double z, Fluid type, TextureAtlasSprite sprite) {
/* 137 */     super(level, x, y, z, sprite);
/* 138 */     setSize(0.01F, 0.01F);
/* 139 */     this.gravity = 0.06F;
/* 140 */     this.type = type;
/*     */   }
/*     */   
/*     */   protected Fluid getType() {
/* 144 */     return this.type;
/*     */   }
/*     */   
/*     */   public SingleQuadParticle.Layer getLayer() {
/* 149 */     return SingleQuadParticle.Layer.OPAQUE;
/*     */   }
/*     */   
/*     */   public int getLightCoords(float a) {
/* 154 */     if (this.isGlowing)
/* 155 */       return LightCoordsUtil.withBlock(super.getLightCoords(a), 15); 
/* 158 */     return super.getLightCoords(a);
/*     */   }
/*     */   
/*     */   public void tick() {
/* 163 */     this.xo = this.x;
/* 164 */     this.yo = this.y;
/* 165 */     this.zo = this.z;
/* 167 */     preMoveUpdate();
/* 168 */     if (this.removed)
/*     */       return; 
/* 172 */     this.yd -= this.gravity;
/* 173 */     move(this.xd, this.yd, this.zd);
/* 174 */     postMoveUpdate();
/* 175 */     if (this.removed)
/*     */       return; 
/* 179 */     this.xd *= 0.9800000190734863D;
/* 180 */     this.yd *= 0.9800000190734863D;
/* 181 */     this.zd *= 0.9800000190734863D;
/* 183 */     if (this.type == Fluids.EMPTY)
/*     */       return; 
/* 187 */     BlockPos pos = BlockPos.containing(this.x, this.y, this.z);
/* 188 */     FluidState fluidState = this.level.getFluidState(pos);
/* 189 */     if (fluidState.is(this.type) && 
/* 190 */       this.y < (pos.getY() + fluidState.getHeight((BlockGetter)this.level, pos)))
/* 191 */       remove(); 
/*     */   }
/*     */   
/*     */   protected void preMoveUpdate() {
/* 197 */     if (this.lifetime-- <= 0)
/* 198 */       remove(); 
/*     */   }
/*     */   
/*     */   protected void postMoveUpdate() {}
/*     */   
/*     */   public static class WaterHangProvider implements ParticleProvider<SimpleParticleType> {
/*     */     private final SpriteSet sprite;
/*     */     
/*     */     public WaterHangProvider(SpriteSet sprite) {
/* 209 */       this.sprite = sprite;
/*     */     }
/*     */     
/*     */     public Particle createParticle(SimpleParticleType options, ClientLevel level, double x, double y, double z, double xAux, double yAux, double zAux, RandomSource random) {
/* 214 */       DripParticle particle = new DripParticle.DripHangParticle(level, x, y, z, (Fluid)Fluids.WATER, (ParticleOptions)ParticleTypes.FALLING_WATER, this.sprite.get(random));
/* 215 */       particle.setColor(0.2F, 0.3F, 1.0F);
/* 216 */       return particle;
/*     */     }
/*     */   }
/*     */   
/*     */   public static class WaterFallProvider implements ParticleProvider<SimpleParticleType> {
/*     */     private final SpriteSet sprite;
/*     */     
/*     */     public WaterFallProvider(SpriteSet sprite) {
/* 224 */       this.sprite = sprite;
/*     */     }
/*     */     
/*     */     public Particle createParticle(SimpleParticleType options, ClientLevel level, double x, double y, double z, double xAux, double yAux, double zAux, RandomSource random) {
/* 229 */       DripParticle particle = new DripParticle.FallAndLandParticle(level, x, y, z, (Fluid)Fluids.WATER, (ParticleOptions)ParticleTypes.SPLASH, this.sprite.get(random));
/* 230 */       particle.setColor(0.2F, 0.3F, 1.0F);
/* 231 */       return particle;
/*     */     }
/*     */   }
/*     */   
/*     */   public static class LavaHangProvider implements ParticleProvider<SimpleParticleType> {
/*     */     private final SpriteSet sprite;
/*     */     
/*     */     public LavaHangProvider(SpriteSet sprite) {
/* 239 */       this.sprite = sprite;
/*     */     }
/*     */     
/*     */     public Particle createParticle(SimpleParticleType options, ClientLevel level, double x, double y, double z, double xAux, double yAux, double zAux, RandomSource random) {
/* 244 */       DripParticle.CoolingDripHangParticle particle = new DripParticle.CoolingDripHangParticle(level, x, y, z, (Fluid)Fluids.LAVA, (ParticleOptions)ParticleTypes.FALLING_LAVA, this.sprite.get(random));
/* 245 */       return particle;
/*     */     }
/*     */   }
/*     */   
/*     */   public static class LavaFallProvider implements ParticleProvider<SimpleParticleType> {
/*     */     private final SpriteSet sprite;
/*     */     
/*     */     public LavaFallProvider(SpriteSet sprite) {
/* 253 */       this.sprite = sprite;
/*     */     }
/*     */     
/*     */     public Particle createParticle(SimpleParticleType options, ClientLevel level, double x, double y, double z, double xAux, double yAux, double zAux, RandomSource random) {
/* 258 */       DripParticle particle = new DripParticle.FallAndLandParticle(level, x, y, z, (Fluid)Fluids.LAVA, (ParticleOptions)ParticleTypes.LANDING_LAVA, this.sprite.get(random));
/* 259 */       particle.setColor(1.0F, 0.2857143F, 0.083333336F);
/* 260 */       return particle;
/*     */     }
/*     */   }
/*     */   
/*     */   public static class LavaLandProvider implements ParticleProvider<SimpleParticleType> {
/*     */     private final SpriteSet sprite;
/*     */     
/*     */     public LavaLandProvider(SpriteSet sprite) {
/* 268 */       this.sprite = sprite;
/*     */     }
/*     */     
/*     */     public Particle createParticle(SimpleParticleType options, ClientLevel level, double x, double y, double z, double xAux, double yAux, double zAux, RandomSource random) {
/* 273 */       DripParticle particle = new DripParticle.DripLandParticle(level, x, y, z, (Fluid)Fluids.LAVA, this.sprite.get(random));
/* 274 */       particle.setColor(1.0F, 0.2857143F, 0.083333336F);
/* 275 */       return particle;
/*     */     }
/*     */   }
/*     */   
/*     */   public static class HoneyHangProvider implements ParticleProvider<SimpleParticleType> {
/*     */     private final SpriteSet sprite;
/*     */     
/*     */     public HoneyHangProvider(SpriteSet sprite) {
/* 283 */       this.sprite = sprite;
/*     */     }
/*     */     
/*     */     public Particle createParticle(SimpleParticleType options, ClientLevel level, double x, double y, double z, double xAux, double yAux, double zAux, RandomSource random) {
/* 288 */       DripParticle.DripHangParticle particle = new DripParticle.DripHangParticle(level, x, y, z, Fluids.EMPTY, (ParticleOptions)ParticleTypes.FALLING_HONEY, this.sprite.get(random));
/* 289 */       particle.gravity *= 0.01F;
/* 290 */       particle.lifetime = 100;
/* 291 */       particle.setColor(0.622F, 0.508F, 0.082F);
/* 292 */       return particle;
/*     */     }
/*     */   }
/*     */   
/*     */   public static class HoneyFallProvider implements ParticleProvider<SimpleParticleType> {
/*     */     private final SpriteSet sprite;
/*     */     
/*     */     public HoneyFallProvider(SpriteSet sprite) {
/* 300 */       this.sprite = sprite;
/*     */     }
/*     */     
/*     */     public Particle createParticle(SimpleParticleType options, ClientLevel level, double x, double y, double z, double xAux, double yAux, double zAux, RandomSource random) {
/* 305 */       DripParticle particle = new DripParticle.HoneyFallAndLandParticle(level, x, y, z, Fluids.EMPTY, (ParticleOptions)ParticleTypes.LANDING_HONEY, this.sprite.get(random));
/* 306 */       particle.gravity = 0.01F;
/* 307 */       particle.setColor(0.582F, 0.448F, 0.082F);
/* 308 */       return particle;
/*     */     }
/*     */   }
/*     */   
/*     */   public static class HoneyLandProvider implements ParticleProvider<SimpleParticleType> {
/*     */     private final SpriteSet sprite;
/*     */     
/*     */     public HoneyLandProvider(SpriteSet sprite) {
/* 316 */       this.sprite = sprite;
/*     */     }
/*     */     
/*     */     public Particle createParticle(SimpleParticleType options, ClientLevel level, double x, double y, double z, double xAux, double yAux, double zAux, RandomSource random) {
/* 321 */       DripParticle particle = new DripParticle.DripLandParticle(level, x, y, z, Fluids.EMPTY, this.sprite.get(random));
/* 322 */       particle.lifetime = (int)(128.0D / (random.nextFloat() * 0.8D + 0.2D));
/* 323 */       particle.setColor(0.522F, 0.408F, 0.082F);
/* 324 */       return particle;
/*     */     }
/*     */   }
/*     */   
/*     */   public static class DripstoneWaterHangProvider implements ParticleProvider<SimpleParticleType> {
/*     */     private final SpriteSet sprite;
/*     */     
/*     */     public DripstoneWaterHangProvider(SpriteSet sprite) {
/* 332 */       this.sprite = sprite;
/*     */     }
/*     */     
/*     */     public Particle createParticle(SimpleParticleType options, ClientLevel level, double x, double y, double z, double xAux, double yAux, double zAux, RandomSource random) {
/* 337 */       DripParticle particle = new DripParticle.DripHangParticle(level, x, y, z, (Fluid)Fluids.WATER, (ParticleOptions)ParticleTypes.FALLING_DRIPSTONE_WATER, this.sprite.get(random));
/* 338 */       particle.setColor(0.2F, 0.3F, 1.0F);
/* 339 */       return particle;
/*     */     }
/*     */   }
/*     */   
/*     */   public static class DripstoneWaterFallProvider implements ParticleProvider<SimpleParticleType> {
/*     */     private final SpriteSet sprite;
/*     */     
/*     */     public DripstoneWaterFallProvider(SpriteSet sprite) {
/* 347 */       this.sprite = sprite;
/*     */     }
/*     */     
/*     */     public Particle createParticle(SimpleParticleType options, ClientLevel level, double x, double y, double z, double xAux, double yAux, double zAux, RandomSource random) {
/* 352 */       DripParticle particle = new DripParticle.DripstoneFallAndLandParticle(level, x, y, z, (Fluid)Fluids.WATER, (ParticleOptions)ParticleTypes.SPLASH, this.sprite.get(random));
/* 353 */       particle.setColor(0.2F, 0.3F, 1.0F);
/* 354 */       return particle;
/*     */     }
/*     */   }
/*     */   
/*     */   public static class DripstoneLavaHangProvider implements ParticleProvider<SimpleParticleType> {
/*     */     private final SpriteSet sprite;
/*     */     
/*     */     public DripstoneLavaHangProvider(SpriteSet sprite) {
/* 362 */       this.sprite = sprite;
/*     */     }
/*     */     
/*     */     public Particle createParticle(SimpleParticleType options, ClientLevel level, double x, double y, double z, double xAux, double yAux, double zAux, RandomSource random) {
/* 367 */       DripParticle.CoolingDripHangParticle particle = new DripParticle.CoolingDripHangParticle(level, x, y, z, (Fluid)Fluids.LAVA, (ParticleOptions)ParticleTypes.FALLING_DRIPSTONE_LAVA, this.sprite.get(random));
/* 368 */       return particle;
/*     */     }
/*     */   }
/*     */   
/*     */   public static class DripstoneLavaFallProvider implements ParticleProvider<SimpleParticleType> {
/*     */     private final SpriteSet sprite;
/*     */     
/*     */     public DripstoneLavaFallProvider(SpriteSet sprite) {
/* 376 */       this.sprite = sprite;
/*     */     }
/*     */     
/*     */     public Particle createParticle(SimpleParticleType options, ClientLevel level, double x, double y, double z, double xAux, double yAux, double zAux, RandomSource random) {
/* 381 */       DripParticle particle = new DripParticle.DripstoneFallAndLandParticle(level, x, y, z, (Fluid)Fluids.LAVA, (ParticleOptions)ParticleTypes.LANDING_LAVA, this.sprite.get(random));
/* 382 */       particle.setColor(1.0F, 0.2857143F, 0.083333336F);
/* 383 */       return particle;
/*     */     }
/*     */   }
/*     */   
/*     */   public static class NectarFallProvider implements ParticleProvider<SimpleParticleType> {
/*     */     private final SpriteSet sprite;
/*     */     
/*     */     public NectarFallProvider(SpriteSet sprite) {
/* 391 */       this.sprite = sprite;
/*     */     }
/*     */     
/*     */     public Particle createParticle(SimpleParticleType options, ClientLevel level, double x, double y, double z, double xAux, double yAux, double zAux, RandomSource random) {
/* 396 */       DripParticle particle = new DripParticle.FallingParticle(level, x, y, z, Fluids.EMPTY, this.sprite.get(random));
/* 397 */       particle.lifetime = (int)(16.0D / (random.nextFloat() * 0.8D + 0.2D));
/* 398 */       particle.gravity = 0.007F;
/* 399 */       particle.setColor(0.92F, 0.782F, 0.72F);
/* 400 */       return particle;
/*     */     }
/*     */   }
/*     */   
/*     */   public static class SporeBlossomFallProvider implements ParticleProvider<SimpleParticleType> {
/*     */     private final SpriteSet sprite;
/*     */     
/*     */     public SporeBlossomFallProvider(SpriteSet sprite) {
/* 408 */       this.sprite = sprite;
/*     */     }
/*     */     
/*     */     public Particle createParticle(SimpleParticleType options, ClientLevel level, double x, double y, double z, double xAux, double yAux, double zAux, RandomSource random) {
/* 413 */       DripParticle particle = new DripParticle.FallingParticle(level, x, y, z, Fluids.EMPTY, this.sprite.get(random));
/* 414 */       particle.lifetime = (int)(64.0F / Mth.randomBetween(particle.random, 0.1F, 0.9F));
/* 415 */       particle.gravity = 0.005F;
/* 416 */       particle.setColor(0.32F, 0.5F, 0.22F);
/* 417 */       return particle;
/*     */     }
/*     */   }
/*     */   
/*     */   public static class ObsidianTearHangProvider implements ParticleProvider<SimpleParticleType> {
/*     */     private final SpriteSet sprite;
/*     */     
/*     */     public ObsidianTearHangProvider(SpriteSet sprite) {
/* 425 */       this.sprite = sprite;
/*     */     }
/*     */     
/*     */     public Particle createParticle(SimpleParticleType options, ClientLevel level, double x, double y, double z, double xAux, double yAux, double zAux, RandomSource random) {
/* 430 */       DripParticle.DripHangParticle particle = new DripParticle.DripHangParticle(level, x, y, z, Fluids.EMPTY, (ParticleOptions)ParticleTypes.FALLING_OBSIDIAN_TEAR, this.sprite.get(random));
/* 431 */       particle.isGlowing = true;
/* 432 */       particle.gravity *= 0.01F;
/* 433 */       particle.lifetime = 100;
/* 434 */       particle.setColor(0.51171875F, 0.03125F, 0.890625F);
/* 435 */       return particle;
/*     */     }
/*     */   }
/*     */   
/*     */   public static class ObsidianTearFallProvider implements ParticleProvider<SimpleParticleType> {
/*     */     private final SpriteSet sprite;
/*     */     
/*     */     public ObsidianTearFallProvider(SpriteSet sprite) {
/* 443 */       this.sprite = sprite;
/*     */     }
/*     */     
/*     */     public Particle createParticle(SimpleParticleType options, ClientLevel level, double x, double y, double z, double xAux, double yAux, double zAux, RandomSource random) {
/* 448 */       DripParticle particle = new DripParticle.FallAndLandParticle(level, x, y, z, Fluids.EMPTY, (ParticleOptions)ParticleTypes.LANDING_OBSIDIAN_TEAR, this.sprite.get(random));
/* 449 */       particle.isGlowing = true;
/* 450 */       particle.gravity = 0.01F;
/* 451 */       particle.setColor(0.51171875F, 0.03125F, 0.890625F);
/* 452 */       return particle;
/*     */     }
/*     */   }
/*     */   
/*     */   public static class ObsidianTearLandProvider implements ParticleProvider<SimpleParticleType> {
/*     */     private final SpriteSet sprite;
/*     */     
/*     */     public ObsidianTearLandProvider(SpriteSet sprite) {
/* 460 */       this.sprite = sprite;
/*     */     }
/*     */     
/*     */     public Particle createParticle(SimpleParticleType options, ClientLevel level, double x, double y, double z, double xAux, double yAux, double zAux, RandomSource random) {
/* 465 */       DripParticle particle = new DripParticle.DripLandParticle(level, x, y, z, Fluids.EMPTY, this.sprite.get(random));
/* 466 */       particle.isGlowing = true;
/* 467 */       particle.lifetime = (int)(28.0D / (random.nextFloat() * 0.8D + 0.2D));
/* 468 */       particle.setColor(0.51171875F, 0.03125F, 0.890625F);
/* 469 */       return particle;
/*     */     }
/*     */   }
/*     */ }


/* Location:              C:\Users\karol\AppData\Roaming\ModrinthApp\meta\versions\26.2-0.19.5\26.2-0.19.5.jar!\net\minecraft\client\particle\DripParticle.class
 * Java compiler version: 25 (69.0)
 * JD-Core Version:       1.1.3
 */