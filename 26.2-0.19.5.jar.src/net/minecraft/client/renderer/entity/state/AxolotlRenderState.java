/*    */ package net.minecraft.client.renderer.entity.state;
/*    */ 
/*    */ import net.minecraft.world.entity.AnimationState;
/*    */ import net.minecraft.world.entity.animal.axolotl.Axolotl;
/*    */ 
/*    */ public class AxolotlRenderState extends LivingEntityRenderState {
/*  7 */   public Axolotl.Variant variant = Axolotl.Variant.DEFAULT;
/*    */   
/*    */   public float playingDeadFactor;
/*    */   
/*    */   public float movingFactor;
/*    */   
/* 10 */   public float inWaterFactor = 1.0F;
/*    */   
/*    */   public float onGroundFactor;
/*    */   
/* 12 */   public final AnimationState swimAnimation = new AnimationState();
/*    */   
/* 13 */   public final AnimationState walkAnimationState = new AnimationState();
/*    */   
/* 14 */   public final AnimationState walkUnderWaterAnimationState = new AnimationState();
/*    */   
/* 15 */   public final AnimationState idleUnderWaterAnimationState = new AnimationState();
/*    */   
/* 16 */   public final AnimationState idleUnderWaterOnGroundAnimationState = new AnimationState();
/*    */   
/* 17 */   public final AnimationState idleOnGroundAnimationState = new AnimationState();
/*    */   
/* 18 */   public final AnimationState playDeadAnimationState = new AnimationState();
/*    */ }


/* Location:              C:\Users\karol\AppData\Roaming\ModrinthApp\meta\versions\26.2-0.19.5\26.2-0.19.5.jar!\net\minecraft\client\renderer\entity\state\AxolotlRenderState.class
 * Java compiler version: 25 (69.0)
 * JD-Core Version:       1.1.3
 */