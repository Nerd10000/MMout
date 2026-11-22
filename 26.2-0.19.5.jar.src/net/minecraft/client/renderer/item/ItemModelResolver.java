/*    */ package net.minecraft.client.renderer.item;
/*    */ 
/*    */ import net.minecraft.client.multiplayer.ClientLevel;
/*    */ import net.minecraft.client.resources.model.ModelManager;
/*    */ import net.minecraft.core.component.DataComponents;
/*    */ import net.minecraft.resources.Identifier;
/*    */ import net.minecraft.world.entity.Entity;
/*    */ import net.minecraft.world.entity.ItemOwner;
/*    */ import net.minecraft.world.entity.LivingEntity;
/*    */ import net.minecraft.world.item.ItemDisplayContext;
/*    */ import net.minecraft.world.item.ItemStack;
/*    */ import net.minecraft.world.level.Level;
/*    */ 
/*    */ public class ItemModelResolver {
/*    */   private final ModelManager modelManager;
/*    */   
/*    */   public ItemModelResolver(ModelManager modelManager) {
/* 19 */     this.modelManager = modelManager;
/*    */   }
/*    */   
/*    */   private ClientItem.Properties getItemProperties(Identifier modelId) {
/* 23 */     return this.modelManager.getItemProperties(modelId);
/*    */   }
/*    */   
/*    */   private ItemModel getItemModel(Identifier modelId) {
/* 27 */     return this.modelManager.getItemModel(modelId);
/*    */   }
/*    */   
/*    */   public void updateForLiving(ItemStackRenderState output, ItemStack item, ItemDisplayContext displayContext, LivingEntity entity) {
/* 32 */     updateForTopItem(output, item, displayContext, entity.level(), (ItemOwner)entity, entity.getId() + displayContext.ordinal());
/*    */   }
/*    */   
/*    */   public void updateForNonLiving(ItemStackRenderState output, ItemStack item, ItemDisplayContext displayContext, Entity entity) {
/* 37 */     updateForTopItem(output, item, displayContext, entity.level(), (ItemOwner)entity, entity.getId());
/*    */   }
/*    */   
/*    */   public void updateForTopItem(ItemStackRenderState output, ItemStack item, ItemDisplayContext displayContext, Level level, ItemOwner owner, int seed) {
/* 41 */     output.clear();
/* 43 */     if (!item.isEmpty()) {
/* 44 */       output.displayContext = displayContext;
/* 45 */       appendItemLayers(output, item, displayContext, level, owner, seed);
/*    */     } 
/*    */   }
/*    */   
/*    */   public void appendItemLayers(ItemStackRenderState output, ItemStack item, ItemDisplayContext displayContext, Level level, ItemOwner owner, int seed) {
/* 50 */     Identifier modelId = (Identifier)item.get(DataComponents.ITEM_MODEL);
/* 51 */     if (modelId == null)
/*    */       return; 
/* 54 */     output.setOversizedInGui(getItemProperties(modelId).oversizedInGui());
/* 55 */     ClientLevel clientLevel = (ClientLevel)level;
/* 55 */     getItemModel(modelId).update(output, item, this, displayContext, (level instanceof ClientLevel) ? clientLevel : null, owner, seed);
/*    */   }
/*    */   
/*    */   public boolean shouldPlaySwapAnimation(ItemStack stack) {
/* 59 */     Identifier modelId = (Identifier)stack.get(DataComponents.ITEM_MODEL);
/* 60 */     if (modelId == null)
/* 61 */       return true; 
/* 63 */     return getItemProperties(modelId).handAnimationOnSwap();
/*    */   }
/*    */   
/*    */   public float swapAnimationScale(ItemStack stack) {
/* 67 */     Identifier modelId = (Identifier)stack.get(DataComponents.ITEM_MODEL);
/* 68 */     if (modelId == null)
/* 69 */       return 1.0F; 
/* 71 */     return getItemProperties(modelId).swapAnimationScale();
/*    */   }
/*    */ }


/* Location:              C:\Users\karol\AppData\Roaming\ModrinthApp\meta\versions\26.2-0.19.5\26.2-0.19.5.jar!\net\minecraft\client\renderer\item\ItemModelResolver.class
 * Java compiler version: 25 (69.0)
 * JD-Core Version:       1.1.3
 */