/*    */ package net.minecraft.client.particle;
/*    */ 
/*    */ import net.minecraft.client.multiplayer.ClientLevel;
/*    */ import net.minecraft.core.particles.ParticleOptions;
/*    */ import net.minecraft.core.particles.SculkChargeParticleOptions;
/*    */ import net.minecraft.util.LightCoordsUtil;
/*    */ import net.minecraft.util.RandomSource;
/*    */ 
/*    */ public class SculkChargeParticle extends SingleQuadParticle {
/*    */   private final SpriteSet sprites;
/*    */   
/*    */   private SculkChargeParticle(ClientLevel level, double x, double y, double z, double xd, double yd, double zd, SpriteSet sprites) {
/* 13 */     super(level, x, y, z, xd, yd, zd, sprites.first());
/* 14 */     this.friction = 0.96F;
/* 16 */     this.sprites = sprites;
/* 17 */     scale(1.5F);
/* 19 */     this.hasPhysics = false;
/* 21 */     setSpriteFromAge(sprites);
/*    */   }
/*    */   
/*    */   public int getLightCoords(float a) {
/* 26 */     return LightCoordsUtil.withBlock(super.getLightCoords(a), 15);
/*    */   }
/*    */   
/*    */   public SingleQuadParticle.Layer getLayer() {
/* 31 */     return SingleQuadParticle.Layer.TRANSLUCENT;
/*    */   }
/*    */   
/*    */   public void tick() {
/* 36 */     super.tick();
/* 37 */     setSpriteFromAge(this.sprites);
/*    */   }
/*    */   
/*    */   public static final class Provider extends Record implements ParticleProvider<SculkChargeParticleOptions> {
/*    */     private final SpriteSet sprite;
/*    */     
/*    */     public Provider(SpriteSet sprite) {
/* 40 */       this.sprite = sprite;
/*    */     }
/*    */     
/*    */     public final String toString() {
/*    */       // Byte code:
/*    */       //   0: aload_0
/*    */       //   1: <illegal opcode> toString : (Lnet/minecraft/client/particle/SculkChargeParticle$Provider;)Ljava/lang/String;
/*    */       //   6: areturn
/*    */       // Line number table:
/*    */       //   Java source line number -> byte code offset
/*    */       //   #40	-> 0
/*    */       // Local variable table:
/*    */       //   start	length	slot	name	descriptor
/*    */       //   0	7	0	this	Lnet/minecraft/client/particle/SculkChargeParticle$Provider;
/*    */     }
/*    */     
/*    */     public final int hashCode() {
/*    */       // Byte code:
/*    */       //   0: aload_0
/*    */       //   1: <illegal opcode> hashCode : (Lnet/minecraft/client/particle/SculkChargeParticle$Provider;)I
/*    */       //   6: ireturn
/*    */       // Line number table:
/*    */       //   Java source line number -> byte code offset
/*    */       //   #40	-> 0
/*    */       // Local variable table:
/*    */       //   start	length	slot	name	descriptor
/*    */       //   0	7	0	this	Lnet/minecraft/client/particle/SculkChargeParticle$Provider;
/*    */     }
/*    */     
/*    */     public final boolean equals(Object o) {
/*    */       // Byte code:
/*    */       //   0: aload_0
/*    */       //   1: aload_1
/*    */       //   2: <illegal opcode> equals : (Lnet/minecraft/client/particle/SculkChargeParticle$Provider;Ljava/lang/Object;)Z
/*    */       //   7: ireturn
/*    */       // Line number table:
/*    */       //   Java source line number -> byte code offset
/*    */       //   #40	-> 0
/*    */       // Local variable table:
/*    */       //   start	length	slot	name	descriptor
/*    */       //   0	8	0	this	Lnet/minecraft/client/particle/SculkChargeParticle$Provider;
/*    */       //   0	8	1	o	Ljava/lang/Object;
/*    */     }
/*    */     
/*    */     public SpriteSet sprite() {
/* 40 */       return this.sprite;
/*    */     }
/*    */     
/*    */     public Particle createParticle(SculkChargeParticleOptions options, ClientLevel level, double x, double y, double z, double xAux, double yAux, double zAux, RandomSource random) {
/* 43 */       SculkChargeParticle particle = new SculkChargeParticle(level, x, y, z, xAux, yAux, zAux, this.sprite);
/* 44 */       particle.setAlpha(1.0F);
/* 45 */       particle.setParticleSpeed(xAux, yAux, zAux);
/* 46 */       particle.oRoll = options.roll();
/* 47 */       particle.roll = options.roll();
/* 48 */       particle.setLifetime(random.nextInt(12) + 8);
/* 49 */       return particle;
/*    */     }
/*    */   }
/*    */ }


/* Location:              C:\Users\karol\AppData\Roaming\ModrinthApp\meta\versions\26.2-0.19.5\26.2-0.19.5.jar!\net\minecraft\client\particle\SculkChargeParticle.class
 * Java compiler version: 25 (69.0)
 * JD-Core Version:       1.1.3
 */