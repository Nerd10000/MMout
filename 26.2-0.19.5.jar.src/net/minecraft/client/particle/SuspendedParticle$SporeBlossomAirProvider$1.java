/*    */ package net.minecraft.client.particle;
/*    */ 
/*    */ import java.util.Optional;
/*    */ import net.minecraft.client.multiplayer.ClientLevel;
/*    */ import net.minecraft.client.renderer.texture.TextureAtlasSprite;
/*    */ import net.minecraft.core.particles.ParticleLimit;
/*    */ 
/*    */ class null extends SuspendedParticle {
/*    */   null(SuspendedParticle.SporeBlossomAirProvider this$0, ClientLevel level, double x, double y, double z, double xd, double yd, double zd, TextureAtlasSprite sprite) {
/* 67 */     super(level, x, y, z, xd, yd, zd, sprite);
/*    */   }
/*    */   
/*    */   public Optional<ParticleLimit> getParticleLimit() {
/* 70 */     return Optional.of(ParticleLimit.SPORE_BLOSSOM);
/*    */   }
/*    */ }


/* Location:              C:\Users\karol\AppData\Roaming\ModrinthApp\meta\versions\26.2-0.19.5\26.2-0.19.5.jar!\net\minecraft\client\particle\SuspendedParticle$SporeBlossomAirProvider$1.class
 * Java compiler version: 25 (69.0)
 * JD-Core Version:       1.1.3
 */