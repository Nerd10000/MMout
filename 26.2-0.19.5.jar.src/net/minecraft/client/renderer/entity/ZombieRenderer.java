/*    */ package net.minecraft.client.renderer.entity;
/*    */ 
/*    */ import java.util.function.Function;
/*    */ import net.minecraft.client.model.geom.ModelLayerLocation;
/*    */ import net.minecraft.client.model.geom.ModelLayers;
/*    */ import net.minecraft.client.model.monster.zombie.BabyZombieModel;
/*    */ import net.minecraft.client.model.monster.zombie.ZombieModel;
/*    */ import net.minecraft.client.renderer.entity.state.EntityRenderState;
/*    */ import net.minecraft.client.renderer.entity.state.ZombieRenderState;
/*    */ import net.minecraft.world.entity.monster.zombie.Zombie;
/*    */ 
/*    */ public class ZombieRenderer extends AbstractZombieRenderer<Zombie, ZombieRenderState, ZombieModel<ZombieRenderState>> {
/*    */   public ZombieRenderer(EntityRendererProvider.Context context) {
/* 13 */     this(context, ModelLayers.ZOMBIE, ModelLayers.ZOMBIE_BABY, ModelLayers.ZOMBIE_ARMOR, ModelLayers.ZOMBIE_BABY_ARMOR);
/*    */   }
/*    */   
/*    */   public ZombieRenderState createRenderState() {
/* 18 */     return new ZombieRenderState();
/*    */   }
/*    */   
/*    */   public ZombieRenderer(EntityRendererProvider.Context context, ModelLayerLocation body, ModelLayerLocation babyBody, ArmorModelSet<ModelLayerLocation> armorSet, ArmorModelSet<ModelLayerLocation> babyArmorSet) {
/* 22 */     super(context, new ZombieModel(context
/* 23 */           .bakeLayer(body)), new BabyZombieModel(context
/* 24 */           .bakeLayer(babyBody)), 
/* 25 */         ArmorModelSet.bake(armorSet, context.getModelSet(), ZombieModel::new), 
/* 26 */         ArmorModelSet.bake(babyArmorSet, context.getModelSet(), BabyZombieModel::new));
/*    */   }
/*    */ }


/* Location:              C:\Users\karol\AppData\Roaming\ModrinthApp\meta\versions\26.2-0.19.5\26.2-0.19.5.jar!\net\minecraft\client\renderer\entity\ZombieRenderer.class
 * Java compiler version: 25 (69.0)
 * JD-Core Version:       1.1.3
 */