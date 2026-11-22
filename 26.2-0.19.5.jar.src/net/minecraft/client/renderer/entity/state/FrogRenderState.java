/*    */ package net.minecraft.client.renderer.entity.state;
/*    */ 
/*    */ import net.minecraft.resources.Identifier;
/*    */ import net.minecraft.world.entity.AnimationState;
/*    */ 
/*    */ public class FrogRenderState extends LivingEntityRenderState {
/*  7 */   private static final Identifier DEFAULT_TEXTURE = Identifier.withDefaultNamespace("textures/entity/frog/frog_temperate.png");
/*    */   
/*    */   public boolean isSwimming;
/*    */   
/* 10 */   public final AnimationState jumpAnimationState = new AnimationState();
/*    */   
/* 11 */   public final AnimationState croakAnimationState = new AnimationState();
/*    */   
/* 12 */   public final AnimationState tongueAnimationState = new AnimationState();
/*    */   
/* 13 */   public final AnimationState swimIdleAnimationState = new AnimationState();
/*    */   
/* 14 */   public Identifier texture = DEFAULT_TEXTURE;
/*    */ }


/* Location:              C:\Users\karol\AppData\Roaming\ModrinthApp\meta\versions\26.2-0.19.5\26.2-0.19.5.jar!\net\minecraft\client\renderer\entity\state\FrogRenderState.class
 * Java compiler version: 25 (69.0)
 * JD-Core Version:       1.1.3
 */