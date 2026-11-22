/*    */ package net.minecraft.client.particle;
/*    */ 
/*    */ import net.minecraft.client.multiplayer.ClientLevel;
/*    */ import net.minecraft.core.particles.GeyserBaseParticleOptions;
/*    */ import net.minecraft.core.particles.GeyserParticleOptions;
/*    */ import net.minecraft.core.particles.ParticleOptions;
/*    */ import net.minecraft.core.particles.ParticleTypes;
/*    */ import net.minecraft.util.RandomSource;
/*    */ 
/*    */ public class GeyserEruptionParticle extends NoRenderParticle {
/*    */   public static final int BASE_PARTICLE_FREQUENCY = 2;
/*    */   
/*    */   public static final int BASE_PARTICLE_AMOUNT = 2;
/*    */   
/*    */   public static final int POOF_PARTICLE_FREQUENCY = 10;
/*    */   
/*    */   public static final int POOF_PARTICLE_AMOUNT = 20;
/*    */   
/*    */   private static final float BASE_BURST_IMPULSE = 1.5F;
/*    */   
/*    */   private static final float POOF_BURST_IMPULSE = 2.0F;
/*    */   
/*    */   private final int waterBlocks;
/*    */   
/*    */   private final double xa;
/*    */   
/*    */   private final double ya;
/*    */   
/*    */   private final double za;
/*    */   
/*    */   private final GeyserParticleOptions plumeParticle;
/*    */   
/*    */   private final GeyserBaseParticleOptions baseParticle;
/*    */   
/*    */   private final GeyserBaseParticleOptions poofParticle;
/*    */   
/*    */   protected GeyserEruptionParticle(ClientLevel level, double x, double y, double z, double xAux, double yAux, double zAux, GeyserParticleOptions options) {
/* 31 */     super(level, x, y, z);
/* 32 */     this.xa = xAux;
/* 33 */     this.ya = yAux;
/* 34 */     this.za = zAux;
/* 35 */     this.waterBlocks = options.waterBlocks();
/* 36 */     this.lifetime = 20;
/* 37 */     this.plumeParticle = new GeyserParticleOptions(ParticleTypes.GEYSER_PLUME, this.waterBlocks);
/* 38 */     this.baseParticle = new GeyserBaseParticleOptions(ParticleTypes.GEYSER_BASE, this.waterBlocks, 1.5F);
/* 39 */     this.poofParticle = new GeyserBaseParticleOptions(ParticleTypes.GEYSER_POOF, this.waterBlocks, 2.0F);
/*    */   }
/*    */   
/*    */   public void tick() {
/* 44 */     super.tick();
/* 46 */     if (this.age % 2 == 0)
/* 47 */       for (int j = 0; j < 2; j++)
/* 48 */         this.level.addParticle((ParticleOptions)this.baseParticle, this.x, this.y, this.z, this.xa, this.ya, this.za);  
/*    */     int i;
/* 52 */     for (i = 0; i < this.waterBlocks + 2; i++)
/* 53 */       this.level.addParticle((ParticleOptions)this.plumeParticle, this.x, this.y, this.z, this.xa, this.ya, this.za); 
/* 56 */     if (this.age % 10 == 0)
/* 57 */       for (i = 0; i < 20; i++)
/* 58 */         this.level.addParticle((ParticleOptions)this.poofParticle, this.x, this.y, this.z, this.xa, this.ya, this.za);  
/*    */   }
/*    */   
/*    */   public static class Provider implements ParticleProvider<GeyserParticleOptions> {
/*    */     public Particle createParticle(GeyserParticleOptions options, ClientLevel level, double x, double y, double z, double xAux, double yAux, double zAux, RandomSource random) {
/* 66 */       return new GeyserEruptionParticle(level, x, y, z, xAux, yAux, zAux, options);
/*    */     }
/*    */   }
/*    */ }


/* Location:              C:\Users\karol\AppData\Roaming\ModrinthApp\meta\versions\26.2-0.19.5\26.2-0.19.5.jar!\net\minecraft\client\particle\GeyserEruptionParticle.class
 * Java compiler version: 25 (69.0)
 * JD-Core Version:       1.1.3
 */