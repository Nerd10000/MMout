/*    */ package net.minecraft.client.renderer.entity.state;
/*    */ 
/*    */ import net.minecraft.world.entity.HumanoidArm;
/*    */ import net.minecraft.world.entity.monster.illager.AbstractIllager;
/*    */ 
/*    */ public class IllagerRenderState extends UndeadRenderState {
/*    */   public boolean isRiding;
/*    */   
/*    */   public boolean isAggressive;
/*    */   
/*  9 */   public HumanoidArm mainArm = HumanoidArm.RIGHT;
/*    */   
/* 10 */   public AbstractIllager.IllagerArmPose armPose = AbstractIllager.IllagerArmPose.NEUTRAL;
/*    */   
/*    */   public int maxCrossbowChargeDuration;
/*    */   
/*    */   public float ticksUsingItem;
/*    */   
/*    */   public float attackAnim;
/*    */ }


/* Location:              C:\Users\karol\AppData\Roaming\ModrinthApp\meta\versions\26.2-0.19.5\26.2-0.19.5.jar!\net\minecraft\client\renderer\entity\state\IllagerRenderState.class
 * Java compiler version: 25 (69.0)
 * JD-Core Version:       1.1.3
 */