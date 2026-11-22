/*    */ package net.minecraft.client.particle;
/*    */ 
/*    */ import net.minecraft.client.Minecraft;
/*    */ import net.minecraft.client.model.geom.ModelLayers;
/*    */ import net.minecraft.client.model.monster.guardian.GuardianParticleModel;
/*    */ import net.minecraft.client.multiplayer.ClientLevel;
/*    */ import net.minecraft.client.renderer.entity.ElderGuardianRenderer;
/*    */ import net.minecraft.client.renderer.rendertype.RenderType;
/*    */ import net.minecraft.client.renderer.rendertype.RenderTypes;
/*    */ import net.minecraft.core.particles.ParticleOptions;
/*    */ import net.minecraft.core.particles.SimpleParticleType;
/*    */ import net.minecraft.util.RandomSource;
/*    */ 
/*    */ public class ElderGuardianParticle extends Particle {
/*    */   protected final GuardianParticleModel model;
/*    */   
/* 15 */   protected final RenderType renderType = RenderTypes.entityTranslucent(ElderGuardianRenderer.GUARDIAN_ELDER_LOCATION);
/*    */   
/*    */   private ElderGuardianParticle(ClientLevel level, double x, double y, double z) {
/* 18 */     super(level, x, y, z);
/* 19 */     this.model = new GuardianParticleModel(Minecraft.getInstance().getEntityModels().bakeLayer(ModelLayers.ELDER_GUARDIAN));
/* 20 */     this.gravity = 0.0F;
/* 21 */     this.lifetime = 30;
/*    */   }
/*    */   
/*    */   public ParticleRenderType getGroup() {
/* 26 */     return ParticleRenderType.ELDER_GUARDIANS;
/*    */   }
/*    */   
/*    */   public static class Provider implements ParticleProvider<SimpleParticleType> {
/*    */     public Particle createParticle(SimpleParticleType options, ClientLevel level, double x, double y, double z, double xAux, double yAux, double zAux, RandomSource random) {
/* 32 */       return new ElderGuardianParticle(level, x, y, z);
/*    */     }
/*    */   }
/*    */ }


/* Location:              C:\Users\karol\AppData\Roaming\ModrinthApp\meta\versions\26.2-0.19.5\26.2-0.19.5.jar!\net\minecraft\client\particle\ElderGuardianParticle.class
 * Java compiler version: 25 (69.0)
 * JD-Core Version:       1.1.3
 */