/*    */ package net.minecraft.client.renderer.entity.state;
/*    */ 
/*    */ import net.minecraft.world.entity.Display;
/*    */ 
/*    */ public class TextDisplayEntityRenderState extends DisplayEntityRenderState {
/*    */   public Display.TextDisplay.TextRenderState textRenderState;
/*    */   
/*    */   public Display.TextDisplay.CachedInfo cachedInfo;
/*    */   
/*    */   public boolean hasSubState() {
/* 13 */     return (this.textRenderState != null);
/*    */   }
/*    */ }


/* Location:              C:\Users\karol\AppData\Roaming\ModrinthApp\meta\versions\26.2-0.19.5\26.2-0.19.5.jar!\net\minecraft\client\renderer\entity\state\TextDisplayEntityRenderState.class
 * Java compiler version: 25 (69.0)
 * JD-Core Version:       1.1.3
 */