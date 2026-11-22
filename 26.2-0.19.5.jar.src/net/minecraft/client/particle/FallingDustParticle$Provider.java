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
/*     */ import net.minecraft.world.level.BlockGetter;
/*     */ import net.minecraft.world.level.block.Block;
/*     */ import net.minecraft.world.level.block.FallingBlock;
/*     */ import net.minecraft.world.level.block.RenderShape;
/*     */ import net.minecraft.world.level.block.state.BlockState;
/*     */ 
/*     */ public class Provider implements ParticleProvider<BlockParticleOption> {
/*     */   private final SpriteSet sprite;
/*     */   
/*     */   public Provider(SpriteSet sprite) {
/*  77 */     this.sprite = sprite;
/*     */   }
/*     */   
/*     */   public Particle createParticle(BlockParticleOption options, ClientLevel level, double x, double y, double z, double xAux, double yAux, double zAux, RandomSource random) {
/*     */     int tintColor;
/*  82 */     BlockState blockState = options.getState();
/*  83 */     if (!blockState.isAir() && blockState.getRenderShape() == RenderShape.INVISIBLE)
/*  84 */       return null; 
/*  87 */     BlockPos pos = BlockPos.containing(x, y, z);
/*  90 */     Block block = blockState.getBlock();
/*  90 */     if (block instanceof FallingBlock) {
/*  90 */       FallingBlock fallingBlock = (FallingBlock)block;
/*  91 */       tintColor = fallingBlock.getDustColor(blockState, (BlockGetter)level, pos);
/*     */     } else {
/*  93 */       BlockTintSource tintSource = Minecraft.getInstance().getBlockColors().getTintSource(blockState, 0);
/*  94 */       if (tintSource != null) {
/*  95 */         tintColor = tintSource.colorAsTerrainParticle(blockState, (BlockAndTintGetter)level, pos);
/*     */       } else {
/*  98 */         tintColor = (blockState.getMapColor((BlockGetter)level, pos)).col;
/*     */       } 
/*     */     } 
/* 101 */     float r = (tintColor >> 16 & 0xFF) / 255.0F;
/* 102 */     float g = (tintColor >> 8 & 0xFF) / 255.0F;
/* 103 */     float b = (tintColor & 0xFF) / 255.0F;
/* 105 */     return new FallingDustParticle(level, x, y, z, r, g, b, this.sprite);
/*     */   }
/*     */ }


/* Location:              C:\Users\karol\AppData\Roaming\ModrinthApp\meta\versions\26.2-0.19.5\26.2-0.19.5.jar!\net\minecraft\client\particle\FallingDustParticle$Provider.class
 * Java compiler version: 25 (69.0)
 * JD-Core Version:       1.1.3
 */