/*    */ package net.minecraft.client.renderer.state.level;
/*    */ 
/*    */ import java.util.ArrayList;
/*    */ import java.util.List;
/*    */ import net.minecraft.client.renderer.SubmitNodeCollector;
/*    */ 
/*    */ public class ParticlesRenderState {
/*  9 */   public final List<ParticleGroupRenderState> particles = new ArrayList<>();
/*    */   
/*    */   public void reset() {
/* 12 */     this.particles.forEach(ParticleGroupRenderState::clear);
/* 13 */     this.particles.clear();
/*    */   }
/*    */   
/*    */   public void add(ParticleGroupRenderState state) {
/* 17 */     this.particles.add(state);
/*    */   }
/*    */   
/*    */   public void submit(SubmitNodeCollector submitNodeCollector, CameraRenderState camera) {
/* 21 */     for (ParticleGroupRenderState particle : this.particles)
/* 22 */       particle.submit(submitNodeCollector, camera); 
/*    */   }
/*    */ }


/* Location:              C:\Users\karol\AppData\Roaming\ModrinthApp\meta\versions\26.2-0.19.5\26.2-0.19.5.jar!\net\minecraft\client\renderer\state\level\ParticlesRenderState.class
 * Java compiler version: 25 (69.0)
 * JD-Core Version:       1.1.3
 */