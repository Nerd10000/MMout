/*    */ package net.minecraft.client.renderer.item;
/*    */ 
/*    */ import java.util.ArrayList;
/*    */ import java.util.List;
/*    */ 
/*    */ public class TrackingItemStackRenderState extends ItemStackRenderState {
/*  8 */   private final List<Object> modelIdentityElements = new ArrayList();
/*    */   
/*    */   public void appendModelIdentityElement(Object element) {
/* 12 */     this.modelIdentityElements.add(element);
/*    */   }
/*    */   
/*    */   public Object getModelIdentity() {
/* 16 */     return this.modelIdentityElements;
/*    */   }
/*    */ }


/* Location:              C:\Users\karol\AppData\Roaming\ModrinthApp\meta\versions\26.2-0.19.5\26.2-0.19.5.jar!\net\minecraft\client\renderer\item\TrackingItemStackRenderState.class
 * Java compiler version: 25 (69.0)
 * JD-Core Version:       1.1.3
 */