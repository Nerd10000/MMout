/*     */ package net.minecraft.client.particle;
/*     */ 
/*     */ import net.minecraft.client.Minecraft;
/*     */ import net.minecraft.client.color.block.BlockTintSource;
/*     */ import net.minecraft.client.multiplayer.ClientLevel;
/*     */ import net.minecraft.client.renderer.block.BlockAndTintGetter;
/*     */ import net.minecraft.core.BlockPos;
/*     */ import net.minecraft.core.particles.BlockParticleOption;
/*     */ import net.minecraft.core.particles.ParticleOptions;
/*     */ import net.minecraft.util.RandomSource;
/*     */ import net.minecraft.world.level.block.Blocks;
/*     */ import net.minecraft.world.level.block.state.BlockState;
/*     */ 
/*     */ public class TerrainParticle extends SingleQuadParticle {
/*     */   private final SingleQuadParticle.Layer layer;
/*     */   
/*     */   private final BlockPos pos;
/*     */   
/*     */   private final float uo;
/*     */   
/*     */   private final float vo;
/*     */   
/*     */   public TerrainParticle(ClientLevel level, double x, double y, double z, double xa, double ya, double za, BlockState blockState) {
/*  22 */     this(level, x, y, z, xa, ya, za, blockState, BlockPos.containing(x, y, z));
/*     */   }
/*     */   
/*     */   public TerrainParticle(ClientLevel level, double x, double y, double z, double xa, double ya, double za, BlockState blockState, BlockPos pos) {
/*  26 */     super(level, x, y, z, xa, ya, za, Minecraft.getInstance().getModelManager().getBlockStateModelSet().getParticleMaterial(blockState).sprite());
/*  27 */     this.pos = pos;
/*  28 */     this.gravity = 1.0F;
/*  29 */     this.rCol = 0.6F;
/*  30 */     this.gCol = 0.6F;
/*  31 */     this.bCol = 0.6F;
/*  33 */     BlockTintSource tintSource = Minecraft.getInstance().getBlockColors().getTintSource(blockState, 0);
/*  34 */     if (tintSource != null) {
/*  35 */       int col = tintSource.colorAsTerrainParticle(blockState, (BlockAndTintGetter)level, pos);
/*  36 */       this.rCol *= (col >> 16 & 0xFF) / 255.0F;
/*  37 */       this.gCol *= (col >> 8 & 0xFF) / 255.0F;
/*  38 */       this.bCol *= (col & 0xFF) / 255.0F;
/*     */     } 
/*  41 */     this.quadSize /= 2.0F;
/*  43 */     this.uo = this.random.nextFloat() * 3.0F;
/*  44 */     this.vo = this.random.nextFloat() * 3.0F;
/*  45 */     this.layer = SingleQuadParticle.Layer.bySprite(this.sprite);
/*     */   }
/*     */   
/*     */   public SingleQuadParticle.Layer getLayer() {
/*  50 */     return this.layer;
/*     */   }
/*     */   
/*     */   protected float getU0() {
/*  55 */     return this.sprite.getU((this.uo + 1.0F) / 4.0F);
/*     */   }
/*     */   
/*     */   protected float getU1() {
/*  60 */     return this.sprite.getU(this.uo / 4.0F);
/*     */   }
/*     */   
/*     */   protected float getV0() {
/*  65 */     return this.sprite.getV(this.vo / 4.0F);
/*     */   }
/*     */   
/*     */   protected float getV1() {
/*  70 */     return this.sprite.getV((this.vo + 1.0F) / 4.0F);
/*     */   }
/*     */   
/*     */   public static class Provider implements ParticleProvider<BlockParticleOption> {
/*     */     public Particle createParticle(BlockParticleOption options, ClientLevel level, double x, double y, double z, double xAux, double yAux, double zAux, RandomSource random) {
/*  76 */       return TerrainParticle.createTerrainParticle(options, level, x, y, z, xAux, yAux, zAux);
/*     */     }
/*     */   }
/*     */   
/*     */   public static class DustPillarProvider implements ParticleProvider<BlockParticleOption> {
/*     */     public Particle createParticle(BlockParticleOption options, ClientLevel level, double x, double y, double z, double xAux, double yAux, double zAux, RandomSource random) {
/*  83 */       Particle particle = TerrainParticle.createTerrainParticle(options, level, x, y, z, xAux, yAux, zAux);
/*  84 */       if (particle != null) {
/*  85 */         particle.setParticleSpeed(random.nextGaussian() / 30.0D, yAux + random.nextGaussian() / 2.0D, random.nextGaussian() / 30.0D);
/*  86 */         particle.setLifetime(random.nextInt(20) + 20);
/*     */       } 
/*  88 */       return particle;
/*     */     }
/*     */   }
/*     */   
/*     */   public static class CrumblingProvider implements ParticleProvider<BlockParticleOption> {
/*     */     public Particle createParticle(BlockParticleOption options, ClientLevel level, double x, double y, double z, double xAux, double yAux, double zAux, RandomSource random) {
/*  95 */       Particle particle = TerrainParticle.createTerrainParticle(options, level, x, y, z, xAux, yAux, zAux);
/*  96 */       if (particle != null) {
/*  97 */         particle.setParticleSpeed(0.0D, 0.0D, 0.0D);
/*  98 */         particle.setLifetime(random.nextInt(10) + 1);
/*     */       } 
/* 100 */       return particle;
/*     */     }
/*     */   }
/*     */   
/*     */   private static TerrainParticle createTerrainParticle(BlockParticleOption options, ClientLevel level, double x, double y, double z, double xAux, double yAux, double zAux) {
/* 105 */     BlockState state = options.getState();
/* 106 */     if (state.isAir() || state.is(Blocks.MOVING_PISTON) || !state.shouldSpawnTerrainParticles())
/* 107 */       return null; 
/* 109 */     return new TerrainParticle(level, x, y, z, xAux, yAux, zAux, state);
/*     */   }
/*     */ }


/* Location:              C:\Users\karol\AppData\Roaming\ModrinthApp\meta\versions\26.2-0.19.5\26.2-0.19.5.jar!\net\minecraft\client\particle\TerrainParticle.class
 * Java compiler version: 25 (69.0)
 * JD-Core Version:       1.1.3
 */