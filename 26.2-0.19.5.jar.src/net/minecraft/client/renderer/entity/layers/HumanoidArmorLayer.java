/*    */ package net.minecraft.client.renderer.entity.layers;
/*    */ 
/*    */ import com.mojang.blaze3d.vertex.PoseStack;
/*    */ import net.minecraft.client.model.HumanoidModel;
/*    */ import net.minecraft.client.model.Model;
/*    */ import net.minecraft.client.renderer.SubmitNodeCollector;
/*    */ import net.minecraft.client.renderer.entity.ArmorModelSet;
/*    */ import net.minecraft.client.renderer.entity.RenderLayerParent;
/*    */ import net.minecraft.client.renderer.entity.state.EntityRenderState;
/*    */ import net.minecraft.client.renderer.entity.state.HumanoidRenderState;
/*    */ import net.minecraft.client.resources.model.EquipmentClientInfo;
/*    */ import net.minecraft.core.component.DataComponents;
/*    */ import net.minecraft.world.entity.EntityTypes;
/*    */ import net.minecraft.world.entity.EquipmentSlot;
/*    */ import net.minecraft.world.item.ItemStack;
/*    */ import net.minecraft.world.item.equipment.Equippable;
/*    */ 
/*    */ public class HumanoidArmorLayer<S extends HumanoidRenderState, M extends HumanoidModel<S>, A extends HumanoidModel<S>> extends RenderLayer<S, M> {
/*    */   private final ArmorModelSet<A> modelSet;
/*    */   
/*    */   private final ArmorModelSet<A> babyModelSet;
/*    */   
/*    */   private final EquipmentLayerRenderer equipmentRenderer;
/*    */   
/*    */   public HumanoidArmorLayer(RenderLayerParent<S, M> renderer, ArmorModelSet<A> modelSet, EquipmentLayerRenderer equipmentRenderer) {
/* 22 */     this(renderer, modelSet, modelSet, equipmentRenderer);
/*    */   }
/*    */   
/*    */   public HumanoidArmorLayer(RenderLayerParent<S, M> renderer, ArmorModelSet<A> modelSet, ArmorModelSet<A> babyModelSet, EquipmentLayerRenderer equipmentRenderer) {
/* 26 */     super(renderer);
/* 27 */     this.modelSet = modelSet;
/* 28 */     this.babyModelSet = babyModelSet;
/* 29 */     this.equipmentRenderer = equipmentRenderer;
/*    */   }
/*    */   
/*    */   public static boolean shouldRender(ItemStack itemStack, EquipmentSlot slot) {
/* 33 */     Equippable equippable = (Equippable)itemStack.get(DataComponents.EQUIPPABLE);
/* 34 */     return (equippable != null && shouldRender(equippable, slot));
/*    */   }
/*    */   
/*    */   private static boolean shouldRender(Equippable equippable, EquipmentSlot slot) {
/* 38 */     return (equippable.assetId().isPresent() && equippable.slot() == slot);
/*    */   }
/*    */   
/*    */   public void submit(PoseStack poseStack, SubmitNodeCollector submitNodeCollector, int lightCoords, S state, float yRot, float xRot) {
/* 43 */     renderArmorPiece(poseStack, submitNodeCollector, ((HumanoidRenderState)state).chestEquipment, EquipmentSlot.CHEST, lightCoords, state);
/* 44 */     renderArmorPiece(poseStack, submitNodeCollector, ((HumanoidRenderState)state).legsEquipment, EquipmentSlot.LEGS, lightCoords, state);
/* 45 */     renderArmorPiece(poseStack, submitNodeCollector, ((HumanoidRenderState)state).feetEquipment, EquipmentSlot.FEET, lightCoords, state);
/* 46 */     renderArmorPiece(poseStack, submitNodeCollector, ((HumanoidRenderState)state).headEquipment, EquipmentSlot.HEAD, lightCoords, state);
/*    */   }
/*    */   
/*    */   private void renderArmorPiece(PoseStack poseStack, SubmitNodeCollector submitNodeCollector, ItemStack itemStack, EquipmentSlot slot, int lightCoords, S state) {
/* 50 */     Equippable equippable = (Equippable)itemStack.get(DataComponents.EQUIPPABLE);
/* 51 */     if (equippable == null || !shouldRender(equippable, slot))
/*    */       return; 
/* 55 */     A model = getArmorModel(state, slot);
/* 58 */     EquipmentClientInfo.LayerType layerType = (((HumanoidRenderState)state).isBaby && ((HumanoidRenderState)state).entityType != EntityTypes.ARMOR_STAND) ? EquipmentClientInfo.LayerType.HUMANOID_BABY : (usesInnerModel(slot) ? EquipmentClientInfo.LayerType.HUMANOID_LEGGINGS : EquipmentClientInfo.LayerType.HUMANOID);
/* 59 */     this.equipmentRenderer.renderLayers(layerType, equippable.assetId().orElseThrow(), (Model<? super S>)model, state, itemStack, poseStack, submitNodeCollector, lightCoords, ((HumanoidRenderState)state).outlineColor);
/*    */   }
/*    */   
/*    */   private A getArmorModel(S state, EquipmentSlot slot) {
/* 63 */     return (A)(((HumanoidRenderState)state).isBaby ? this.babyModelSet : this.modelSet).get(slot);
/*    */   }
/*    */   
/*    */   private boolean usesInnerModel(EquipmentSlot slot) {
/* 67 */     return (slot == EquipmentSlot.LEGS);
/*    */   }
/*    */ }


/* Location:              C:\Users\karol\AppData\Roaming\ModrinthApp\meta\versions\26.2-0.19.5\26.2-0.19.5.jar!\net\minecraft\client\renderer\entity\layers\HumanoidArmorLayer.class
 * Java compiler version: 25 (69.0)
 * JD-Core Version:       1.1.3
 */