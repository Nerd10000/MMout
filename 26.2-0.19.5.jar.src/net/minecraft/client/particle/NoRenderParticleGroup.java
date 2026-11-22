/*    */ package net.minecraft.client.particle;
/*    */ 
/*    */ import net.minecraft.client.Camera;
/*    */ import net.minecraft.client.renderer.SubmitNodeCollector;
/*    */ import net.minecraft.client.renderer.culling.Frustum;
/*    */ import net.minecraft.client.renderer.state.level.CameraRenderState;
/*    */ import net.minecraft.client.renderer.state.level.ParticleGroupRenderState;
/*    */ 
/*    */ public class NoRenderParticleGroup extends ParticleGroup<NoRenderParticle> {
/*    */   private static final ParticleGroupRenderState EMPTY_RENDER_STATE = (ignored, camera) -> {
/*    */     
/*    */     };
/*    */   
/*    */   public NoRenderParticleGroup(ParticleEngine engine) {
/* 11 */     super(engine);
/*    */   }
/*    */   
/*    */   public ParticleGroupRenderState extractRenderState(Frustum frustum, Camera camera, float partialTickTime) {
/* 16 */     return EMPTY_RENDER_STATE;
/*    */   }
/*    */ }


/* Location:              C:\Users\karol\AppData\Roaming\ModrinthApp\meta\versions\26.2-0.19.5\26.2-0.19.5.jar!\net\minecraft\client\particle\NoRenderParticleGroup.class
 * Java compiler version: 25 (69.0)
 * JD-Core Version:       1.1.3
 */