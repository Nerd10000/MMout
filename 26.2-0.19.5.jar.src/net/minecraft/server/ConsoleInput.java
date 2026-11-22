/*    */ package net.minecraft.server;
/*    */ 
/*    */ import net.minecraft.commands.CommandSourceStack;
/*    */ 
/*    */ public class ConsoleInput {
/*    */   public final String msg;
/*    */   
/*    */   public final CommandSourceStack source;
/*    */   
/*    */   public ConsoleInput(String msg, CommandSourceStack source) {
/* 10 */     this.msg = msg;
/* 11 */     this.source = source;
/*    */   }
/*    */ }


/* Location:              C:\Users\karol\AppData\Roaming\ModrinthApp\meta\versions\26.2-0.19.5\26.2-0.19.5.jar!\net\minecraft\server\ConsoleInput.class
 * Java compiler version: 25 (69.0)
 * JD-Core Version:       1.1.3
 */