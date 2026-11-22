/*    */ package net.minecraft.client.renderer.entity.state;
/*    */ 
/*    */ import net.minecraft.client.renderer.item.ItemModelResolver;
/*    */ import net.minecraft.client.renderer.item.ItemStackRenderState;
/*    */ import net.minecraft.world.entity.LivingEntity;
/*    */ import net.minecraft.world.item.ItemDisplayContext;
/*    */ 
/*    */ public class HoldingEntityRenderState extends LivingEntityRenderState {
/* 10 */   public final ItemStackRenderState heldItem = new ItemStackRenderState();
/*    */   
/*    */   public static void extractHoldingEntityRenderState(LivingEntity entity, HoldingEntityRenderState state, ItemModelResolver itemModelResolver) {
/* 13 */     itemModelResolver.updateForLiving(state.heldItem, entity.getMainHandItem(), ItemDisplayContext.GROUND, entity);
/*    */   }
/*    */ }


/* Location:              C:\Users\karol\AppData\Roaming\ModrinthApp\meta\versions\26.2-0.19.5\26.2-0.19.5.jar!\net\minecraft\client\renderer\entity\state\HoldingEntityRenderState.class
 * Java compiler version: 25 (69.0)
 * JD-Core Version:       1.1.3
 */