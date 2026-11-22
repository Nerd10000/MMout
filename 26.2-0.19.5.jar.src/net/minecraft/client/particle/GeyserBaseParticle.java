/*    */ package net.minecraft.client.particle;
/*    */ 
/*    */ import net.minecraft.client.multiplayer.ClientLevel;
/*    */ import net.minecraft.core.particles.GeyserBaseParticleOptions;
/*    */ import net.minecraft.core.particles.ParticleOptions;
/*    */ import net.minecraft.util.RandomSource;
/*    */ 
/*    */ public class GeyserBaseParticle extends BaseAshSmokeParticle {
/*    */   private static final float BURST_IMPULSE_FACTOR = 0.25F;
/*    */   
/*    */   private static final float PARTICLE_SIZE_BASE = 3.0F;
/*    */   
/*    */   private static final float PARTICLE_SIZE_FACTOR = 0.125F;
/*    */   
/*    */   private static final float SPAWN_OFFSET_Y = 0.2F;
/*    */   
/*    */   private static final float RANDOM_SPAWN_SPREAD = 0.5F;
/*    */   
/*    */   private static final int MAX_LIFETIME = 25;
/*    */   
/*    */   private static final float FRICTION = 0.725F;
/*    */   
/*    */   private GeyserBaseParticle(ClientLevel level, double x, double y, double z, double xAux, double yAux, double zAux, int waterBlocks, float burstImpulseBase, SpriteSet sprites) {
/* 24 */     super(level, x, y, z, burstImpulse, burstImpulse, burstImpulse, xAux, yAux, zAux, size, sprites, 0.0F, 0, 0.0F, true);
/* 25 */     this.friction = 0.725F;
/* 26 */     this.rCol = 1.0F;
/* 27 */     this.gCol = 1.0F;
/* 28 */     this.bCol = 1.0F;
/* 29 */     this.yd = Math.abs(this.yd);
/* 31 */     float lifetimeFactor = 0.8F + 0.2F * level.getRandom().nextFloat();
/* 32 */     this.lifetime = (int)(25.0F * lifetimeFactor);
/*    */   }
/*    */   
/*    */   public static class Provider implements ParticleProvider<GeyserBaseParticleOptions> {
/*    */     private final SpriteSet sprites;
/*    */     
/*    */     public Provider(SpriteSet sprites) {
/* 39 */       this.sprites = sprites;
/*    */     }
/*    */     
/*    */     public Particle createParticle(GeyserBaseParticleOptions options, ClientLevel level, double x, double y, double z, double xAux, double yAux, double zAux, RandomSource random) {
/* 44 */       double randomX = x + ((random.nextFloat() - 0.5F) * 0.5F);
/* 45 */       double randomY = y + ((random.nextFloat() - 0.5F) * 0.5F) + 0.20000000298023224D;
/* 46 */       double randomZ = z + ((random.nextFloat() - 0.5F) * 0.5F);
/* 47 */       return new GeyserBaseParticle(level, randomX, randomY, randomZ, xAux, yAux, zAux, options.waterBlocks(), options.burstImpulseBase(), this.sprites);
/*    */     }
/*    */   }
/*    */ }


/* Location:              C:\Users\karol\AppData\Roaming\ModrinthApp\meta\versions\26.2-0.19.5\26.2-0.19.5.jar!\net\minecraft\client\particle\GeyserBaseParticle.class
 * Java compiler version: 25 (69.0)
 * JD-Core Version:       1.1.3
 */