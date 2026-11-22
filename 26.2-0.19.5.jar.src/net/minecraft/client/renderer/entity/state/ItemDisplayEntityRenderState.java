/*    */ package net.minecraft.client.renderer.entity.state;
/*    */ 
/*    */ import net.minecraft.client.renderer.item.ItemStackRenderState;
/*    */ 
/*    */ public class ItemDisplayEntityRenderState extends DisplayEntityRenderState {
/*  6 */   public final ItemStackRenderState item = new ItemStackRenderState();
/*    */   
/*    */   public boolean hasSubState() {
/* 10 */     return !this.item.isEmpty();
/*    */   }
/*    */ }


/* Location:              C:\Users\karol\AppData\Roaming\ModrinthApp\meta\versions\26.2-0.19.5\26.2-0.19.5.jar!\net\minecraft\client\renderer\entity\state\ItemDisplayEntityRenderState.class
 * Java compiler version: 25 (69.0)
 * JD-Core Version:       1.1.3
 */