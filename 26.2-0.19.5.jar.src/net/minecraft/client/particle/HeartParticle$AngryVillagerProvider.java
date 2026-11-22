/*    */ package net.minecraft.client.particle;
/*    */ 
/*    */ import net.minecraft.client.multiplayer.ClientLevel;
/*    */ import net.minecraft.core.particles.ParticleOptions;
/*    */ import net.minecraft.core.particles.SimpleParticleType;
/*    */ import net.minecraft.util.RandomSource;
/*    */ 
/*    */ public class AngryVillagerProvider implements ParticleProvider<SimpleParticleType> {
/*    */   private final SpriteSet sprite;
/*    */   
/*    */   public AngryVillagerProvider(SpriteSet sprite) {
/* 52 */     this.sprite = sprite;
/*    */   }
/*    */   
/*    */   public Particle createParticle(SimpleParticleType options, ClientLevel level, double x, double y, double z, double xAux, double yAux, double zAux, RandomSource random) {
/* 57 */     HeartParticle particle = new HeartParticle(level, x, y + 0.5D, z, this.sprite.get(random));
/* 58 */     particle.setColor(1.0F, 1.0F, 1.0F);
/* 59 */     return particle;
/*    */   }
/*    */ }


/* Location:              C:\Users\karol\AppData\Roaming\ModrinthApp\meta\versions\26.2-0.19.5\26.2-0.19.5.jar!\net\minecraft\client\particle\HeartParticle$AngryVillagerProvider.class
 * Java compiler version: 25 (69.0)
 * JD-Core Version:       1.1.3
 */