/*    */ package net.minecraft.commands;
/*    */ 
/*    */ import net.minecraft.network.chat.Component;
/*    */ 
/*    */ public class FunctionInstantiationException extends Exception {
/*    */   private final Component messageComponent;
/*    */   
/*    */   public FunctionInstantiationException(Component messageComponent) {
/*  9 */     super(messageComponent.getString());
/* 10 */     this.messageComponent = messageComponent;
/*    */   }
/*    */   
/*    */   public Component messageComponent() {
/* 14 */     return this.messageComponent;
/*    */   }
/*    */ }


/* Location:              C:\Users\karol\AppData\Roaming\ModrinthApp\meta\versions\26.2-0.19.5\26.2-0.19.5.jar!\net\minecraft\commands\FunctionInstantiationException.class
 * Java compiler version: 25 (69.0)
 * JD-Core Version:       1.1.3
 */