/*    */ package net.minecraft.client.renderer.entity;
/*    */ 
/*    */ import net.minecraft.client.model.geom.ModelLayers;
/*    */ import net.minecraft.client.model.monster.slime.MagmaCubeModel;
/*    */ import net.minecraft.client.renderer.entity.state.EntityRenderState;
/*    */ import net.minecraft.client.renderer.entity.state.LivingEntityRenderState;
/*    */ import net.minecraft.client.renderer.entity.state.SlimeRenderState;
/*    */ import net.minecraft.core.BlockPos;
/*    */ import net.minecraft.resources.Identifier;
/*    */ import net.minecraft.world.entity.Entity;
/*    */ import net.minecraft.world.entity.monster.cubemob.MagmaCube;
/*    */ 
/*    */ public class MagmaCubeRenderer extends AbstractCubeMobRenderer<MagmaCube, SlimeRenderState, MagmaCubeModel> {
/* 12 */   private static final Identifier MAGMACUBE_LOCATION = Identifier.withDefaultNamespace("textures/entity/slime/magmacube.png");
/*    */   
/*    */   public MagmaCubeRenderer(EntityRendererProvider.Context context) {
/* 15 */     super(context, new MagmaCubeModel(context.bakeLayer(ModelLayers.MAGMA_CUBE)));
/*    */   }
/*    */   
/*    */   protected int getBlockLightLevel(MagmaCube entity, BlockPos blockPos) {
/* 20 */     return 15;
/*    */   }
/*    */   
/*    */   public Identifier getTextureLocation(SlimeRenderState state) {
/* 25 */     return MAGMACUBE_LOCATION;
/*    */   }
/*    */   
/*    */   public SlimeRenderState createRenderState() {
/* 30 */     return new SlimeRenderState();
/*    */   }
/*    */ }


/* Location:              C:\Users\karol\AppData\Roaming\ModrinthApp\meta\versions\26.2-0.19.5\26.2-0.19.5.jar!\net\minecraft\client\renderer\entity\MagmaCubeRenderer.class
 * Java compiler version: 25 (69.0)
 * JD-Core Version:       1.1.3
 */