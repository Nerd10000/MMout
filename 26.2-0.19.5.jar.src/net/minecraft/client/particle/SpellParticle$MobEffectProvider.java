/*    */ package net.minecraft.client.particle;
/*    */ 
/*    */ import net.minecraft.client.multiplayer.ClientLevel;
/*    */ import net.minecraft.core.particles.ColorParticleOption;
/*    */ import net.minecraft.core.particles.ParticleOptions;
/*    */ import net.minecraft.util.RandomSource;
/*    */ 
/*    */ public class MobEffectProvider implements ParticleProvider<ColorParticleOption> {
/*    */   private final SpriteSet sprite;
/*    */   
/*    */   public MobEffectProvider(SpriteSet sprite) {
/* 88 */     this.sprite = sprite;
/*    */   }
/*    */   
/*    */   public Particle createParticle(ColorParticleOption options, ClientLevel level, double x, double y, double z, double xAux, double yAux, double zAux, RandomSource random) {
/* 93 */     SpellParticle particle = new SpellParticle(level, x, y, z, xAux, yAux, zAux, this.sprite);
/* 95 */     particle.setColor(options.getRed(), options.getGreen(), options.getBlue());
/* 96 */     particle.setAlpha(options.getAlpha());
/* 97 */     return particle;
/*    */   }
/*    */ }


/* Location:              C:\Users\karol\AppData\Roaming\ModrinthApp\meta\versions\26.2-0.19.5\26.2-0.19.5.jar!\net\minecraft\client\particle\SpellParticle$MobEffectProvider.class
 * Java compiler version: 25 (69.0)
 * JD-Core Version:       1.1.3
 */