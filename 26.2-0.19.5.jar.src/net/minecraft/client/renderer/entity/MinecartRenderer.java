/*    */ package net.minecraft.client.renderer.entity;
/*    */ 
/*    */ import net.minecraft.client.model.geom.ModelLayerLocation;
/*    */ import net.minecraft.client.renderer.entity.state.EntityRenderState;
/*    */ import net.minecraft.client.renderer.entity.state.MinecartRenderState;
/*    */ import net.minecraft.world.entity.vehicle.minecart.AbstractMinecart;
/*    */ 
/*    */ public class MinecartRenderer extends AbstractMinecartRenderer<AbstractMinecart, MinecartRenderState> {
/*    */   public MinecartRenderer(EntityRendererProvider.Context context, ModelLayerLocation model) {
/*  9 */     super(context, model);
/*    */   }
/*    */   
/*    */   public MinecartRenderState createRenderState() {
/* 14 */     return new MinecartRenderState();
/*    */   }
/*    */ }


/* Location:              C:\Users\karol\AppData\Roaming\ModrinthApp\meta\versions\26.2-0.19.5\26.2-0.19.5.jar!\net\minecraft\client\renderer\entity\MinecartRenderer.class
 * Java compiler version: 25 (69.0)
 * JD-Core Version:       1.1.3
 */