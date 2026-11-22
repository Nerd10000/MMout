/*    */ package net.minecraft.client.particle;
/*    */ 
/*    */ import net.minecraft.client.multiplayer.ClientLevel;
/*    */ import net.minecraft.core.particles.ParticleOptions;
/*    */ import net.minecraft.core.particles.SimpleParticleType;
/*    */ import net.minecraft.util.RandomSource;
/*    */ import net.minecraft.world.item.ItemStackTemplate;
/*    */ import net.minecraft.world.item.Items;
/*    */ 
/*    */ public class SlimeProvider extends BreakingItemParticle.ItemParticleProvider<SimpleParticleType> {
/*    */   public Particle createParticle(SimpleParticleType options, ClientLevel level, double x, double y, double z, double xAux, double yAux, double zAux, RandomSource random) {
/* 87 */     return new BreakingItemParticle(level, x, y, z, getSprite(new ItemStackTemplate(Items.SLIME_BALL), level, random));
/*    */   }
/*    */ }


/* Location:              C:\Users\karol\AppData\Roaming\ModrinthApp\meta\versions\26.2-0.19.5\26.2-0.19.5.jar!\net\minecraft\client\particle\BreakingItemParticle$SlimeProvider.class
 * Java compiler version: 25 (69.0)
 * JD-Core Version:       1.1.3
 */