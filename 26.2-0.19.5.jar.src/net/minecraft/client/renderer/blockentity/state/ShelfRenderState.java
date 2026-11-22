/*    */ package net.minecraft.client.renderer.blockentity.state;
/*    */ 
/*    */ import net.minecraft.client.renderer.item.ItemStackRenderState;
/*    */ import net.minecraft.core.Direction;
/*    */ 
/*    */ public class ShelfRenderState extends BlockEntityRenderState {
/*  8 */   public final ItemStackRenderState[] items = new ItemStackRenderState[3];
/*    */   
/*    */   public boolean alignToBottom;
/*    */   
/* 10 */   public Direction facing = Direction.NORTH;
/*    */ }


/* Location:              C:\Users\karol\AppData\Roaming\ModrinthApp\meta\versions\26.2-0.19.5\26.2-0.19.5.jar!\net\minecraft\client\renderer\blockentity\state\ShelfRenderState.class
 * Java compiler version: 25 (69.0)
 * JD-Core Version:       1.1.3
 */