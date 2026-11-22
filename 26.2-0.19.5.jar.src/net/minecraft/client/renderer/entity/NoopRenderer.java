/*    */ package net.minecraft.client.renderer.entity;
/*    */ 
/*    */ import net.minecraft.client.renderer.entity.state.EntityRenderState;
/*    */ import net.minecraft.world.entity.Entity;
/*    */ 
/*    */ public class NoopRenderer<T extends Entity> extends EntityRenderer<T, EntityRenderState> {
/*    */   public NoopRenderer(EntityRendererProvider.Context context) {
/*  8 */     super(context);
/*    */   }
/*    */   
/*    */   public EntityRenderState createRenderState() {
/* 13 */     return new EntityRenderState();
/*    */   }
/*    */ }


/* Location:              C:\Users\karol\AppData\Roaming\ModrinthApp\meta\versions\26.2-0.19.5\26.2-0.19.5.jar!\net\minecraft\client\renderer\entity\NoopRenderer.class
 * Java compiler version: 25 (69.0)
 * JD-Core Version:       1.1.3
 */