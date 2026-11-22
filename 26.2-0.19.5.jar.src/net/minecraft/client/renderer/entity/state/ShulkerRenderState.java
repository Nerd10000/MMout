/*    */ package net.minecraft.client.renderer.entity.state;
/*    */ 
/*    */ import net.minecraft.core.Direction;
/*    */ import net.minecraft.world.item.DyeColor;
/*    */ import net.minecraft.world.phys.Vec3;
/*    */ 
/*    */ public class ShulkerRenderState extends LivingEntityRenderState {
/*  9 */   public Vec3 renderOffset = Vec3.ZERO;
/*    */   
/*    */   public DyeColor color;
/*    */   
/*    */   public float peekAmount;
/*    */   
/*    */   public float yHeadRot;
/*    */   
/*    */   public float yBodyRot;
/*    */   
/* 14 */   public Direction attachFace = Direction.DOWN;
/*    */ }


/* Location:              C:\Users\karol\AppData\Roaming\ModrinthApp\meta\versions\26.2-0.19.5\26.2-0.19.5.jar!\net\minecraft\client\renderer\entity\state\ShulkerRenderState.class
 * Java compiler version: 25 (69.0)
 * JD-Core Version:       1.1.3
 */