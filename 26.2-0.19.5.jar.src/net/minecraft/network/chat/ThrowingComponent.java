/*    */ package net.minecraft.network.chat;
/*    */ 
/*    */ public class ThrowingComponent extends Exception {
/*    */   private final Component component;
/*    */   
/*    */   public ThrowingComponent(Component component) {
/*  7 */     super(component.getString());
/*  8 */     this.component = component;
/*    */   }
/*    */   
/*    */   public ThrowingComponent(Component component, Throwable cause) {
/* 12 */     super(component.getString(), cause);
/* 13 */     this.component = component;
/*    */   }
/*    */   
/*    */   public Component getComponent() {
/* 17 */     return this.component;
/*    */   }
/*    */ }


/* Location:              C:\Users\karol\AppData\Roaming\ModrinthApp\meta\versions\26.2-0.19.5\26.2-0.19.5.jar!\net\minecraft\network\chat\ThrowingComponent.class
 * Java compiler version: 25 (69.0)
 * JD-Core Version:       1.1.3
 */