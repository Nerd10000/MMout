/*     */ package net.minecraft.client.particle;
/*     */ 
/*     */ import net.minecraft.client.Minecraft;
/*     */ import net.minecraft.client.color.block.BlockTintSource;
/*     */ import net.minecraft.client.multiplayer.ClientLevel;
/*     */ import net.minecraft.client.renderer.block.BlockAndTintGetter;
/*     */ import net.minecraft.core.BlockPos;
/*     */ import net.minecraft.core.particles.BlockParticleOption;
/*     */ import net.minecraft.core.particles.ParticleOptions;
/*     */ import net.minecraft.util.Mth;
/*     */ import net.minecraft.util.RandomSource;
/*     */ import net.minecraft.world.level.BlockGetter;
/*     */ import net.minecraft.world.level.block.Block;
/*     */ import net.minecraft.world.level.block.FallingBlock;
/*     */ import net.minecraft.world.level.block.RenderShape;
/*     */ import net.minecraft.world.level.block.state.BlockState;
/*     */ 
/*     */ public class FallingDustParticle extends SingleQuadParticle {
/*     */   private final float rotSpeed;
/*     */   
/*     */   private final SpriteSet sprites;
/*     */   
/*     */   private FallingDustParticle(ClientLevel level, double x, double y, double z, float r, float g, float b, SpriteSet sprites) {
/*  20 */     super(level, x, y, z, sprites.first());
/*  21 */     this.sprites = sprites;
/*  23 */     this.rCol = r;
/*  24 */     this.gCol = g;
/*  25 */     this.bCol = b;
/*  27 */     float scale = 0.9F;
/*  29 */     this.quadSize *= 0.67499995F;
/*  31 */     int baseLifetime = (int)(32.0D / (this.random.nextFloat() * 0.8D + 0.2D));
/*  32 */     this.lifetime = (int)Math.max(baseLifetime * 0.9F, 1.0F);
/*  33 */     setSpriteFromAge(sprites);
/*  35 */     this.rotSpeed = (this.random.nextFloat() - 0.5F) * 0.1F;
/*  36 */     this.roll = this.random.nextFloat() * 6.2831855F;
/*     */   }
/*     */   
/*     */   public SingleQuadParticle.Layer getLayer() {
/*  41 */     return SingleQuadParticle.Layer.OPAQUE;
/*     */   }
/*     */   
/*     */   public float getQuadSize(float a) {
/*  46 */     return this.quadSize * Mth.clamp((this.age + a) / this.lifetime * 32.0F, 0.0F, 1.0F);
/*     */   }
/*     */   
/*     */   public void tick() {
/*  51 */     this.xo = this.x;
/*  52 */     this.yo = this.y;
/*  53 */     this.zo = this.z;
/*  55 */     if (this.age++ >= this.lifetime) {
/*  56 */       remove();
/*     */       return;
/*     */     } 
/*  60 */     setSpriteFromAge(this.sprites);
/*  62 */     this.oRoll = this.roll;
/*  63 */     this.roll += 3.1415927F * this.rotSpeed * 2.0F;
/*  64 */     if (this.onGround)
/*  65 */       this.oRoll = this.roll = 0.0F; 
/*  68 */     move(this.xd, this.yd, this.zd);
/*  69 */     this.yd -= 0.003000000026077032D;
/*  70 */     this.yd = Math.max(this.yd, -0.14000000059604645D);
/*     */   }
/*     */   
/*     */   public static class Provider implements ParticleProvider<BlockParticleOption> {
/*     */     private final SpriteSet sprite;
/*     */     
/*     */     public Provider(SpriteSet sprite) {
/*  77 */       this.sprite = sprite;
/*     */     }
/*     */     
/*     */     public Particle createParticle(BlockParticleOption options, ClientLevel level, double x, double y, double z, double xAux, double yAux, double zAux, RandomSource random) {
/*     */       int tintColor;
/*  82 */       BlockState blockState = options.getState();
/*  83 */       if (!blockState.isAir() && blockState.getRenderShape() == RenderShape.INVISIBLE)
/*  84 */         return null; 
/*  87 */       BlockPos pos = BlockPos.containing(x, y, z);
/*  90 */       Block block = blockState.getBlock();
/*  90 */       if (block instanceof FallingBlock) {
/*  90 */         FallingBlock fallingBlock = (FallingBlock)block;
/*  91 */         tintColor = fallingBlock.getDustColor(blockState, (BlockGetter)level, pos);
/*     */       } else {
/*  93 */         BlockTintSource tintSource = Minecraft.getInstance().getBlockColors().getTintSource(blockState, 0);
/*  94 */         if (tintSource != null) {
/*  95 */           tintColor = tintSource.colorAsTerrainParticle(blockState, (BlockAndTintGetter)level, pos);
/*     */         } else {
/*  98 */           tintColor = (blockState.getMapColor((BlockGetter)level, pos)).col;
/*     */         } 
/*     */       } 
/* 101 */       float r = (tintColor >> 16 & 0xFF) / 255.0F;
/* 102 */       float g = (tintColor >> 8 & 0xFF) / 255.0F;
/* 103 */       float b = (tintColor & 0xFF) / 255.0F;
/* 105 */       return new FallingDustParticle(level, x, y, z, r, g, b, this.sprite);
/*     */     }
/*     */   }
/*     */ }


/* Location:              C:\Users\karol\AppData\Roaming\ModrinthApp\meta\versions\26.2-0.19.5\26.2-0.19.5.jar!\net\minecraft\client\particle\FallingDustParticle.class
 * Java compiler version: 25 (69.0)
 * JD-Core Version:       1.1.3
 */