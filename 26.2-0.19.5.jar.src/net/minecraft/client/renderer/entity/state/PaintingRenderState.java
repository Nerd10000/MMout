/*    */ package net.minecraft.client.renderer.entity.state;
/*    */ 
/*    */ import net.minecraft.core.Direction;
/*    */ import net.minecraft.world.entity.decoration.painting.PaintingVariant;
/*    */ 
/*    */ public class PaintingRenderState extends EntityRenderState {
/*  8 */   public Direction direction = Direction.NORTH;
/*    */   
/*    */   public PaintingVariant variant;
/*    */   
/* 10 */   public int[] lightCoordsPerBlock = new int[0];
/*    */ }


/* Location:              C:\Users\karol\AppData\Roaming\ModrinthApp\meta\versions\26.2-0.19.5\26.2-0.19.5.jar!\net\minecraft\client\renderer\entity\state\PaintingRenderState.class
 * Java compiler version: 25 (69.0)
 * JD-Core Version:       1.1.3
 */