/*    */ package net.minecraft.client.renderer.entity.state;
/*    */ 
/*    */ import net.minecraft.world.InteractionHand;
/*    */ import net.minecraft.world.entity.HumanoidArm;
/*    */ import net.minecraft.world.item.ItemStack;
/*    */ 
/*    */ public class HumanoidRenderState extends ArmedEntityRenderState {
/*    */   public float swimAmount;
/*    */   
/*  9 */   public float speedValue = 1.0F;
/*    */   
/*    */   public float maxCrossbowChargeDuration;
/*    */   
/*    */   public float ticksUsingItem;
/*    */   
/* 12 */   public InteractionHand useItemHand = InteractionHand.MAIN_HAND;
/*    */   
/*    */   public boolean isCrouching;
/*    */   
/*    */   public boolean isFallFlying;
/*    */   
/*    */   public boolean isVisuallySwimming;
/*    */   
/*    */   public boolean isPassenger;
/*    */   
/*    */   public boolean isUsingItem;
/*    */   
/*    */   public float elytraRotX;
/*    */   
/*    */   public float elytraRotY;
/*    */   
/*    */   public float elytraRotZ;
/*    */   
/* 21 */   public ItemStack headEquipment = ItemStack.EMPTY;
/*    */   
/* 22 */   public ItemStack chestEquipment = ItemStack.EMPTY;
/*    */   
/* 23 */   public ItemStack legsEquipment = ItemStack.EMPTY;
/*    */   
/* 24 */   public ItemStack feetEquipment = ItemStack.EMPTY;
/*    */   
/*    */   public float ticksUsingItem(HumanoidArm arm) {
/* 29 */     if (this.isUsingItem)
/* 29 */       if (((this.useItemHand == InteractionHand.MAIN_HAND) ? true : false) == ((arm == this.mainArm) ? true : false))
/* 30 */         return this.ticksUsingItem;  
/* 32 */     return 0.0F;
/*    */   }
/*    */ }


/* Location:              C:\Users\karol\AppData\Roaming\ModrinthApp\meta\versions\26.2-0.19.5\26.2-0.19.5.jar!\net\minecraft\client\renderer\entity\state\HumanoidRenderState.class
 * Java compiler version: 25 (69.0)
 * JD-Core Version:       1.1.3
 */