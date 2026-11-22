/*    */ package net.minecraft.client.renderer.entity.state;
/*    */ 
/*    */ import net.minecraft.client.renderer.block.BlockModelRenderState;
/*    */ import net.minecraft.world.entity.AnimationState;
/*    */ import net.minecraft.world.entity.animal.golem.CopperGolemState;
/*    */ import net.minecraft.world.level.block.WeatheringCopper;
/*    */ 
/*    */ public class CopperGolemRenderState extends ArmedEntityRenderState {
/*  9 */   public WeatheringCopper.WeatherState weathering = WeatheringCopper.WeatherState.UNAFFECTED;
/*    */   
/* 10 */   public CopperGolemState copperGolemState = CopperGolemState.IDLE;
/*    */   
/* 11 */   public final AnimationState idleAnimationState = new AnimationState();
/*    */   
/* 12 */   public final AnimationState interactionGetItem = new AnimationState();
/*    */   
/* 13 */   public final AnimationState interactionGetNoItem = new AnimationState();
/*    */   
/* 14 */   public final AnimationState interactionDropItem = new AnimationState();
/*    */   
/* 15 */   public final AnimationState interactionDropNoItem = new AnimationState();
/*    */   
/* 16 */   public final BlockModelRenderState blockOnAntenna = new BlockModelRenderState();
/*    */ }


/* Location:              C:\Users\karol\AppData\Roaming\ModrinthApp\meta\versions\26.2-0.19.5\26.2-0.19.5.jar!\net\minecraft\client\renderer\entity\state\CopperGolemRenderState.class
 * Java compiler version: 25 (69.0)
 * JD-Core Version:       1.1.3
 */