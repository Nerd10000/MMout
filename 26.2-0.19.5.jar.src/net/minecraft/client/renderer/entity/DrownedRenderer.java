/*    */ package net.minecraft.client.renderer.entity;
/*    */ 
/*    */ import com.mojang.blaze3d.vertex.PoseStack;
/*    */ import com.mojang.math.Axis;
/*    */ import java.util.function.Function;
/*    */ import net.minecraft.client.model.HumanoidModel;
/*    */ import net.minecraft.client.model.geom.ModelLayers;
/*    */ import net.minecraft.client.model.monster.zombie.BabyDrownedModel;
/*    */ import net.minecraft.client.model.monster.zombie.DrownedModel;
/*    */ import net.minecraft.client.renderer.entity.layers.DrownedOuterLayer;
/*    */ import net.minecraft.client.renderer.entity.layers.RenderLayer;
/*    */ import net.minecraft.client.renderer.entity.state.EntityRenderState;
/*    */ import net.minecraft.client.renderer.entity.state.LivingEntityRenderState;
/*    */ import net.minecraft.client.renderer.entity.state.ZombieRenderState;
/*    */ import net.minecraft.resources.Identifier;
/*    */ import net.minecraft.util.Mth;
/*    */ import net.minecraft.world.entity.HumanoidArm;
/*    */ import net.minecraft.world.entity.Mob;
/*    */ import net.minecraft.world.entity.monster.zombie.Drowned;
/*    */ import net.minecraft.world.entity.monster.zombie.Zombie;
/*    */ import net.minecraft.world.item.ItemStack;
/*    */ import net.minecraft.world.item.Items;
/*    */ import org.joml.Quaternionfc;
/*    */ 
/*    */ public class DrownedRenderer extends AbstractZombieRenderer<Drowned, ZombieRenderState, DrownedModel> {
/* 19 */   private static final Identifier DROWNED_LOCATION = Identifier.withDefaultNamespace("textures/entity/zombie/drowned.png");
/*    */   
/* 20 */   private static final Identifier BABY_DROWNED_LOCATION = Identifier.withDefaultNamespace("textures/entity/zombie/drowned_baby.png");
/*    */   
/*    */   public DrownedRenderer(EntityRendererProvider.Context context) {
/* 23 */     super(context, new DrownedModel(context
/* 24 */           .bakeLayer(ModelLayers.DROWNED)), new BabyDrownedModel(context
/* 25 */           .bakeLayer(ModelLayers.DROWNED_BABY)), 
/* 26 */         ArmorModelSet.bake(ModelLayers.DROWNED_ARMOR, context.getModelSet(), DrownedModel::new), 
/* 27 */         ArmorModelSet.bake(ModelLayers.DROWNED_BABY_ARMOR, context.getModelSet(), BabyDrownedModel::new));
/* 30 */     addLayer((RenderLayer<ZombieRenderState, DrownedModel>)new DrownedOuterLayer(this, context.getModelSet()));
/*    */   }
/*    */   
/*    */   public ZombieRenderState createRenderState() {
/* 35 */     return new ZombieRenderState();
/*    */   }
/*    */   
/*    */   public Identifier getTextureLocation(ZombieRenderState state) {
/* 40 */     return state.isBaby ? BABY_DROWNED_LOCATION : DROWNED_LOCATION;
/*    */   }
/*    */   
/*    */   protected void setupRotations(ZombieRenderState state, PoseStack poseStack, float bodyRot, float entityScale) {
/* 45 */     super.setupRotations(state, poseStack, bodyRot, entityScale);
/* 47 */     float swimAmount = state.swimAmount;
/* 48 */     if (swimAmount > 0.0F) {
/* 49 */       float targetRotationX = -10.0F - state.xRot;
/* 50 */       float rotationX = Mth.lerp(swimAmount, 0.0F, targetRotationX);
/* 51 */       poseStack.rotateAround((Quaternionfc)Axis.XP.rotationDegrees(rotationX), 0.0F, state.boundingBoxHeight / 2.0F / entityScale, 0.0F);
/*    */     } 
/*    */   }
/*    */   
/*    */   protected HumanoidModel.ArmPose getArmPose(Drowned mob, HumanoidArm arm) {
/* 57 */     ItemStack item = mob.getItemHeldByArm(arm);
/* 58 */     if (mob.getMainArm() == arm && mob.isAggressive() && item.is(Items.TRIDENT))
/* 59 */       return HumanoidModel.ArmPose.THROW_TRIDENT; 
/* 61 */     return super.getArmPose(mob, arm);
/*    */   }
/*    */ }


/* Location:              C:\Users\karol\AppData\Roaming\ModrinthApp\meta\versions\26.2-0.19.5\26.2-0.19.5.jar!\net\minecraft\client\renderer\entity\DrownedRenderer.class
 * Java compiler version: 25 (69.0)
 * JD-Core Version:       1.1.3
 */