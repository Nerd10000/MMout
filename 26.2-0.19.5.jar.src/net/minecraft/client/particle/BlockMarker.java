/*    */ package net.minecraft.client.particle;
/*    */ 
/*    */ import net.minecraft.client.Minecraft;
/*    */ import net.minecraft.client.multiplayer.ClientLevel;
/*    */ import net.minecraft.core.particles.BlockParticleOption;
/*    */ import net.minecraft.core.particles.ParticleOptions;
/*    */ import net.minecraft.util.RandomSource;
/*    */ import net.minecraft.world.level.block.state.BlockState;
/*    */ 
/*    */ public class BlockMarker extends SingleQuadParticle {
/*    */   private final SingleQuadParticle.Layer layer;
/*    */   
/*    */   private BlockMarker(ClientLevel level, double x, double y, double z, BlockState state) {
/* 13 */     super(level, x, y, z, Minecraft.getInstance().getModelManager().getBlockStateModelSet().getParticleMaterial(state).sprite());
/* 14 */     this.gravity = 0.0F;
/* 15 */     this.lifetime = 80;
/* 16 */     this.hasPhysics = false;
/* 17 */     this.layer = SingleQuadParticle.Layer.bySprite(this.sprite);
/*    */   }
/*    */   
/*    */   public SingleQuadParticle.Layer getLayer() {
/* 22 */     return this.layer;
/*    */   }
/*    */   
/*    */   public float getQuadSize(float a) {
/* 27 */     return 0.5F;
/*    */   }
/*    */   
/*    */   public static class Provider implements ParticleProvider<BlockParticleOption> {
/*    */     public Particle createParticle(BlockParticleOption option, ClientLevel level, double x, double y, double z, double xAux, double yAux, double zAux, RandomSource random) {
/* 33 */       return new BlockMarker(level, x, y, z, option.getState());
/*    */     }
/*    */   }
/*    */ }


/* Location:              C:\Users\karol\AppData\Roaming\ModrinthApp\meta\versions\26.2-0.19.5\26.2-0.19.5.jar!\net\minecraft\client\particle\BlockMarker.class
 * Java compiler version: 25 (69.0)
 * JD-Core Version:       1.1.3
 */