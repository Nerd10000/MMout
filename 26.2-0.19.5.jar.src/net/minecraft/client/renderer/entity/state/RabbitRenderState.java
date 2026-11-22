/*    */ package net.minecraft.client.renderer.entity.state;
/*    */ 
/*    */ import net.minecraft.world.entity.AnimationState;
/*    */ import net.minecraft.world.entity.animal.rabbit.Rabbit;
/*    */ 
/*    */ public class RabbitRenderState extends LivingEntityRenderState {
/*    */   public float jumpCompletion;
/*    */   
/*    */   public boolean isToast;
/*    */   
/*  9 */   public Rabbit.Variant variant = Rabbit.Variant.DEFAULT;
/*    */   
/* 11 */   public final AnimationState hopAnimationState = new AnimationState();
/*    */   
/* 12 */   public final AnimationState idleHeadTiltAnimationState = new AnimationState();
/*    */ }


/* Location:              C:\Users\karol\AppData\Roaming\ModrinthApp\meta\versions\26.2-0.19.5\26.2-0.19.5.jar!\net\minecraft\client\renderer\entity\state\RabbitRenderState.class
 * Java compiler version: 25 (69.0)
 * JD-Core Version:       1.1.3
 */