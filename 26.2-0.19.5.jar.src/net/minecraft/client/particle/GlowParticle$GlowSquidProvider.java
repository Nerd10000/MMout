/*    */ package net.minecraft.client.particle;
/*    */ 
/*    */ import net.minecraft.client.multiplayer.ClientLevel;
/*    */ import net.minecraft.core.particles.ParticleOptions;
/*    */ import net.minecraft.core.particles.SimpleParticleType;
/*    */ import net.minecraft.util.RandomSource;
/*    */ 
/*    */ public class GlowSquidProvider implements ParticleProvider<SimpleParticleType> {
/*    */   private final SpriteSet sprite;
/*    */   
/*    */   public GlowSquidProvider(SpriteSet sprite) {
/* 44 */     this.sprite = sprite;
/*    */   }
/*    */   
/*    */   public Particle createParticle(SimpleParticleType options, ClientLevel level, double x, double y, double z, double xAux, double yAux, double zAux, RandomSource random) {
/* 49 */     GlowParticle glowParticle = new GlowParticle(level, x, y, z, 0.5D - random.nextDouble(), yAux, 0.5D - random.nextDouble(), this.sprite);
/* 50 */     if (random.nextBoolean()) {
/* 51 */       glowParticle.setColor(0.6F, 1.0F, 0.8F);
/*    */     } else {
/* 53 */       glowParticle.setColor(0.08F, 0.4F, 0.4F);
/*    */     } 
/* 55 */     glowParticle.yd *= 0.20000000298023224D;
/* 56 */     if (xAux == 0.0D && zAux == 0.0D) {
/* 57 */       glowParticle.xd *= 0.10000000149011612D;
/* 58 */       glowParticle.zd *= 0.10000000149011612D;
/*    */     } 
/* 60 */     glowParticle.setLifetime((int)(8.0D / (random.nextDouble() * 0.8D + 0.2D)));
/* 61 */     return glowParticle;
/*    */   }
/*    */ }


/* Location:              C:\Users\karol\AppData\Roaming\ModrinthApp\meta\versions\26.2-0.19.5\26.2-0.19.5.jar!\net\minecraft\client\particle\GlowParticle$GlowSquidProvider.class
 * Java compiler version: 25 (69.0)
 * JD-Core Version:       1.1.3
 */