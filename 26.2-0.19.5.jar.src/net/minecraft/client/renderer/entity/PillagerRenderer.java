/*    */ package net.minecraft.client.renderer.entity;
/*    */ 
/*    */ import net.minecraft.client.model.geom.ModelLayers;
/*    */ import net.minecraft.client.model.monster.illager.IllagerModel;
/*    */ import net.minecraft.client.renderer.entity.layers.ItemInHandLayer;
/*    */ import net.minecraft.client.renderer.entity.layers.RenderLayer;
/*    */ import net.minecraft.client.renderer.entity.state.EntityRenderState;
/*    */ import net.minecraft.client.renderer.entity.state.IllagerRenderState;
/*    */ import net.minecraft.client.renderer.entity.state.LivingEntityRenderState;
/*    */ import net.minecraft.resources.Identifier;
/*    */ import net.minecraft.world.entity.monster.illager.Pillager;
/*    */ 
/*    */ public class PillagerRenderer extends IllagerRenderer<Pillager, IllagerRenderState> {
/* 11 */   private static final Identifier PILLAGER = Identifier.withDefaultNamespace("textures/entity/illager/pillager.png");
/*    */   
/*    */   public PillagerRenderer(EntityRendererProvider.Context context) {
/* 14 */     super(context, new IllagerModel(context.bakeLayer(ModelLayers.PILLAGER)), 0.5F);
/* 16 */     addLayer((RenderLayer<IllagerRenderState, IllagerModel<IllagerRenderState>>)new ItemInHandLayer(this));
/*    */   }
/*    */   
/*    */   public Identifier getTextureLocation(IllagerRenderState state) {
/* 21 */     return PILLAGER;
/*    */   }
/*    */   
/*    */   public IllagerRenderState createRenderState() {
/* 26 */     return new IllagerRenderState();
/*    */   }
/*    */ }


/* Location:              C:\Users\karol\AppData\Roaming\ModrinthApp\meta\versions\26.2-0.19.5\26.2-0.19.5.jar!\net\minecraft\client\renderer\entity\PillagerRenderer.class
 * Java compiler version: 25 (69.0)
 * JD-Core Version:       1.1.3
 */