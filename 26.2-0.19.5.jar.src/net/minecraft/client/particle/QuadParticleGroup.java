/*    */ package net.minecraft.client.particle;
/*    */ 
/*    */ import java.util.Objects;
/*    */ import net.minecraft.CrashReport;
/*    */ import net.minecraft.CrashReportCategory;
/*    */ import net.minecraft.ReportedException;
/*    */ import net.minecraft.client.Camera;
/*    */ import net.minecraft.client.renderer.culling.Frustum;
/*    */ import net.minecraft.client.renderer.state.level.ParticleGroupRenderState;
/*    */ import net.minecraft.client.renderer.state.level.QuadParticleRenderState;
/*    */ 
/*    */ public class QuadParticleGroup extends ParticleGroup<SingleQuadParticle> {
/*    */   private final ParticleRenderType particleType;
/*    */   
/* 13 */   private final QuadParticleRenderState particleTypeRenderState = new QuadParticleRenderState();
/*    */   
/*    */   public QuadParticleGroup(ParticleEngine engine, ParticleRenderType particleType) {
/* 16 */     super(engine);
/* 17 */     this.particleType = particleType;
/*    */   }
/*    */   
/*    */   public ParticleGroupRenderState extractRenderState(Frustum frustum, Camera camera, float partialTickTime) {
/* 22 */     for (SingleQuadParticle particle : this.particles) {
/* 23 */       if (frustum.pointInFrustum(particle.x, particle.y, particle.z))
/*    */         try {
/* 25 */           particle.extract(this.particleTypeRenderState, camera, partialTickTime);
/* 26 */         } catch (Throwable throwable) {
/* 27 */           CrashReport report = CrashReport.forThrowable(throwable, "Rendering Particle");
/* 28 */           CrashReportCategory category = report.addCategory("Particle being rendered");
/* 29 */           Objects.requireNonNull(particle);
/* 29 */           category.setDetail("Particle", particle::toString);
/* 30 */           Objects.requireNonNull(this.particleType);
/* 30 */           category.setDetail("Particle Type", this.particleType::toString);
/* 31 */           throw new ReportedException(report);
/*    */         }  
/*    */     } 
/* 35 */     return (ParticleGroupRenderState)this.particleTypeRenderState;
/*    */   }
/*    */ }


/* Location:              C:\Users\karol\AppData\Roaming\ModrinthApp\meta\versions\26.2-0.19.5\26.2-0.19.5.jar!\net\minecraft\client\particle\QuadParticleGroup.class
 * Java compiler version: 25 (69.0)
 * JD-Core Version:       1.1.3
 */