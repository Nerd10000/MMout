/*    */ package net.minecraft.client.particle;
/*    */ 
/*    */ import net.minecraft.client.multiplayer.ClientLevel;
/*    */ import net.minecraft.core.particles.GeyserBaseParticleOptions;
/*    */ import net.minecraft.core.particles.ParticleOptions;
/*    */ import net.minecraft.util.RandomSource;
/*    */ 
/*    */ public class Provider implements ParticleProvider<GeyserBaseParticleOptions> {
/*    */   private final SpriteSet sprites;
/*    */   
/*    */   public Provider(SpriteSet sprites) {
/* 39 */     this.sprites = sprites;
/*    */   }
/*    */   
/*    */   public Particle createParticle(GeyserBaseParticleOptions options, ClientLevel level, double x, double y, double z, double xAux, double yAux, double zAux, RandomSource random) {
/* 44 */     double randomX = x + ((random.nextFloat() - 0.5F) * 0.5F);
/* 45 */     double randomY = y + ((random.nextFloat() - 0.5F) * 0.5F) + 0.20000000298023224D;
/* 46 */     double randomZ = z + ((random.nextFloat() - 0.5F) * 0.5F);
/* 47 */     return new GeyserBaseParticle(level, randomX, randomY, randomZ, xAux, yAux, zAux, options.waterBlocks(), options.burstImpulseBase(), this.sprites);
/*    */   }
/*    */ }


/* Location:              C:\Users\karol\AppData\Roaming\ModrinthApp\meta\versions\26.2-0.19.5\26.2-0.19.5.jar!\net\minecraft\client\particle\GeyserBaseParticle$Provider.class
 * Java compiler version: 25 (69.0)
 * JD-Core Version:       1.1.3
 */