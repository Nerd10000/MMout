/*    */ package net.minecraft.client.particle;
/*    */ 
/*    */ import net.minecraft.client.multiplayer.ClientLevel;
/*    */ import net.minecraft.core.particles.ParticleOptions;
/*    */ import net.minecraft.core.particles.SimpleParticleType;
/*    */ import net.minecraft.util.RandomSource;
/*    */ 
/*    */ public class HappyVillagerProvider implements ParticleProvider<SimpleParticleType> {
/*    */   private final SpriteSet sprite;
/*    */   
/*    */   public HappyVillagerProvider(SpriteSet sprite) {
/* 72 */     this.sprite = sprite;
/*    */   }
/*    */   
/*    */   public Particle createParticle(SimpleParticleType options, ClientLevel level, double x, double y, double z, double xAux, double yAux, double zAux, RandomSource random) {
/* 77 */     SuspendedTownParticle particle = new SuspendedTownParticle(level, x, y, z, xAux, yAux, zAux, this.sprite.get(random));
/* 78 */     particle.setColor(1.0F, 1.0F, 1.0F);
/* 79 */     return particle;
/*    */   }
/*    */ }


/* Location:              C:\Users\karol\AppData\Roaming\ModrinthApp\meta\versions\26.2-0.19.5\26.2-0.19.5.jar!\net\minecraft\client\particle\SuspendedTownParticle$HappyVillagerProvider.class
 * Java compiler version: 25 (69.0)
 * JD-Core Version:       1.1.3
 */