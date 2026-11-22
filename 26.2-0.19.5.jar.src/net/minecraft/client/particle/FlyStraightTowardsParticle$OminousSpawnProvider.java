/*    */ package net.minecraft.client.particle;
/*    */ 
/*    */ import net.minecraft.client.multiplayer.ClientLevel;
/*    */ import net.minecraft.core.particles.ParticleOptions;
/*    */ import net.minecraft.core.particles.SimpleParticleType;
/*    */ import net.minecraft.util.Mth;
/*    */ import net.minecraft.util.RandomSource;
/*    */ 
/*    */ public class OminousSpawnProvider implements ParticleProvider<SimpleParticleType> {
/*    */   private final SpriteSet sprite;
/*    */   
/*    */   public OminousSpawnProvider(SpriteSet sprite) {
/* 92 */     this.sprite = sprite;
/*    */   }
/*    */   
/*    */   public Particle createParticle(SimpleParticleType options, ClientLevel level, double x, double y, double z, double xAux, double yAux, double zAux, RandomSource random) {
/* 97 */     FlyStraightTowardsParticle particle = new FlyStraightTowardsParticle(level, x, y, z, xAux, yAux, zAux, -12210434, -1, this.sprite.get(random));
/* 98 */     particle.scale(Mth.randomBetween(level.getRandom(), 3.0F, 5.0F));
/* 99 */     return particle;
/*    */   }
/*    */ }


/* Location:              C:\Users\karol\AppData\Roaming\ModrinthApp\meta\versions\26.2-0.19.5\26.2-0.19.5.jar!\net\minecraft\client\particle\FlyStraightTowardsParticle$OminousSpawnProvider.class
 * Java compiler version: 25 (69.0)
 * JD-Core Version:       1.1.3
 */