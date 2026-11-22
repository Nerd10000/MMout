/*    */ package net.minecraft.client.renderer.entity;
/*    */ 
/*    */ import net.minecraft.client.model.geom.ModelLayers;
/*    */ import net.minecraft.client.renderer.entity.state.LivingEntityRenderState;
/*    */ import net.minecraft.resources.Identifier;
/*    */ import net.minecraft.world.entity.monster.spider.CaveSpider;
/*    */ 
/*    */ public class CaveSpiderRenderer extends SpiderRenderer<CaveSpider> {
/*  9 */   private static final Identifier CAVE_SPIDER_LOCATION = Identifier.withDefaultNamespace("textures/entity/spider/cave_spider.png");
/*    */   
/*    */   public CaveSpiderRenderer(EntityRendererProvider.Context context) {
/* 12 */     super(context, ModelLayers.CAVE_SPIDER);
/* 13 */     this.shadowRadius = 0.56F;
/*    */   }
/*    */   
/*    */   public Identifier getTextureLocation(LivingEntityRenderState state) {
/* 18 */     return CAVE_SPIDER_LOCATION;
/*    */   }
/*    */ }


/* Location:              C:\Users\karol\AppData\Roaming\ModrinthApp\meta\versions\26.2-0.19.5\26.2-0.19.5.jar!\net\minecraft\client\renderer\entity\CaveSpiderRenderer.class
 * Java compiler version: 25 (69.0)
 * JD-Core Version:       1.1.3
 */