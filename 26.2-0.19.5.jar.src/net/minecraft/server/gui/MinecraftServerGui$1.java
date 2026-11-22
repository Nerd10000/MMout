/*    */ package net.minecraft.server.gui;
/*    */ 
/*    */ import java.awt.event.WindowAdapter;
/*    */ import java.awt.event.WindowEvent;
/*    */ import javax.swing.JFrame;
/*    */ import net.minecraft.server.dedicated.DedicatedServer;
/*    */ 
/*    */ class null extends WindowAdapter {
/*    */   public void windowClosing(WindowEvent event) {
/* 62 */     if (!gui.isClosing.getAndSet(true)) {
/* 63 */       frame.setTitle("Minecraft server - shutting down!");
/* 64 */       server.halt(true);
/* 65 */       gui.runFinalizers();
/*    */     } 
/*    */   }
/*    */ }


/* Location:              C:\Users\karol\AppData\Roaming\ModrinthApp\meta\versions\26.2-0.19.5\26.2-0.19.5.jar!\net\minecraft\server\gui\MinecraftServerGui$1.class
 * Java compiler version: 25 (69.0)
 * JD-Core Version:       1.1.3
 */