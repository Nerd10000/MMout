/*    */ package net.minecraft.client.particle;
/*    */ 
/*    */ import net.minecraft.client.multiplayer.ClientLevel;
/*    */ import net.minecraft.core.particles.ParticleOptions;
/*    */ import net.minecraft.core.particles.SimpleParticleType;
/*    */ import net.minecraft.util.LightCoordsUtil;
/*    */ import net.minecraft.util.RandomSource;
/*    */ 
/*    */ public class SculkChargePopParticle extends SingleQuadParticle {
/*    */   private final SpriteSet sprites;
/*    */   
/*    */   private SculkChargePopParticle(ClientLevel level, double x, double y, double z, double xd, double yd, double zd, SpriteSet sprites) {
/* 13 */     super(level, x, y, z, xd, yd, zd, sprites.first());
/* 14 */     this.friction = 0.96F;
/* 16 */     this.sprites = sprites;
/* 17 */     scale(1.0F);
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
/*    */   public static final class Provider extends Record implements ParticleProvider<SimpleParticleType> {
/*    */     private final SpriteSet sprite;
/*    */     
/*    */     public Provider(SpriteSet sprite) {
/* 40 */       this.sprite = sprite;
/*    */     }
/*    */     
/*    */     public final String toString() {
/*    */       // Byte code:
/*    */       //   0: aload_0
/*    */       //   1: <illegal opcode> toString : (Lnet/minecraft/client/particle/SculkChargePopParticle$Provider;)Ljava/lang/String;
/*    */       //   6: areturn
/*    */       // Line number table:
/*    */       //   Java source line number -> byte code offset
/*    */       //   #40	-> 0
/*    */       // Local variable table:
/*    */       //   start	length	slot	name	descriptor
/*    */       //   0	7	0	this	Lnet/minecraft/client/particle/SculkChargePopParticle$Provider;
/*    */     }
/*    */     
/*    */     public final int hashCode() {
/*    */       // Byte code:
/*    */       //   0: aload_0
/*    */       //   1: <illegal opcode> hashCode : (Lnet/minecraft/client/particle/SculkChargePopParticle$Provider;)I
/*    */       //   6: ireturn
/*    */       // Line number table:
/*    */       //   Java source line number -> byte code offset
/*    */       //   #40	-> 0
/*    */       // Local variable table:
/*    */       //   start	length	slot	name	descriptor
/*    */       //   0	7	0	this	Lnet/minecraft/client/particle/SculkChargePopParticle$Provider;
/*    */     }
/*    */     
/*    */     public final boolean equals(Object o) {
/*    */       // Byte code:
/*    */       //   0: aload_0
/*    */       //   1: aload_1
/*    */       //   2: <illegal opcode> equals : (Lnet/minecraft/client/particle/SculkChargePopParticle$Provider;Ljava/lang/Object;)Z
/*    */       //   7: ireturn
/*    */       // Line number table:
/*    */       //   Java source line number -> byte code offset
/*    */       //   #40	-> 0
/*    */       // Local variable table:
/*    */       //   start	length	slot	name	descriptor
/*    */       //   0	8	0	this	Lnet/minecraft/client/particle/SculkChargePopParticle$Provider;
/*    */       //   0	8	1	o	Ljava/lang/Object;
/*    */     }
/*    */     
/*    */     public SpriteSet sprite() {
/* 40 */       return this.sprite;
/*    */     }
/*    */     
/*    */     public Particle createParticle(SimpleParticleType options, ClientLevel level, double x, double y, double z, double xAux, double yAux, double zAux, RandomSource random) {
/* 43 */       SculkChargePopParticle particle = new SculkChargePopParticle(level, x, y, z, xAux, yAux, zAux, this.sprite);
/* 44 */       particle.setAlpha(1.0F);
/* 45 */       particle.setParticleSpeed(xAux, yAux, zAux);
/* 46 */       particle.setLifetime(random.nextInt(4) + 6);
/* 47 */       return particle;
/*    */     }
/*    */   }
/*    */ }


/* Location:              C:\Users\karol\AppData\Roaming\ModrinthApp\meta\versions\26.2-0.19.5\26.2-0.19.5.jar!\net\minecraft\client\particle\SculkChargePopParticle.class
 * Java compiler version: 25 (69.0)
 * JD-Core Version:       1.1.3
 */