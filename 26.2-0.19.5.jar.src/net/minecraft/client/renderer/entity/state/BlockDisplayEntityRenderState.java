/*    */ package net.minecraft.client.renderer.entity.state;
/*    */ 
/*    */ import net.minecraft.client.renderer.block.BlockModelRenderState;
/*    */ 
/*    */ public class BlockDisplayEntityRenderState extends DisplayEntityRenderState {
/*  6 */   public final BlockModelRenderState blockModel = new BlockModelRenderState();
/*    */   
/*    */   public boolean hasSubState() {
/* 10 */     return !this.blockModel.isEmpty();
/*    */   }
/*    */ }


/* Location:              C:\Users\karol\AppData\Roaming\ModrinthApp\meta\versions\26.2-0.19.5\26.2-0.19.5.jar!\net\minecraft\client\renderer\entity\state\BlockDisplayEntityRenderState.class
 * Java compiler version: 25 (69.0)
 * JD-Core Version:       1.1.3
 */