/*    */ package net.minecraft.client.renderer.blockentity.state;
/*    */ 
/*    */ import net.minecraft.core.Direction;
/*    */ import net.minecraft.world.level.block.CopperGolemStatueBlock;
/*    */ import net.minecraft.world.level.block.WeatheringCopper;
/*    */ 
/*    */ public class CopperGolemStatueRenderState extends BlockEntityRenderState {
/*  8 */   public CopperGolemStatueBlock.Pose pose = CopperGolemStatueBlock.Pose.STANDING;
/*    */   
/*  9 */   public Direction direction = Direction.NORTH;
/*    */   
/* 10 */   public WeatheringCopper.WeatherState oxidationState = WeatheringCopper.WeatherState.UNAFFECTED;
/*    */ }


/* Location:              C:\Users\karol\AppData\Roaming\ModrinthApp\meta\versions\26.2-0.19.5\26.2-0.19.5.jar!\net\minecraft\client\renderer\blockentity\state\CopperGolemStatueRenderState.class
 * Java compiler version: 25 (69.0)
 * JD-Core Version:       1.1.3
 */