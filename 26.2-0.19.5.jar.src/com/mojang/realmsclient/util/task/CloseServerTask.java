/*    */ package com.mojang.realmsclient.util.task;
/*    */ 
/*    */ import com.mojang.logging.LogUtils;
/*    */ import com.mojang.realmsclient.client.RealmsClient;
/*    */ import com.mojang.realmsclient.dto.RealmsServer;
/*    */ import com.mojang.realmsclient.exception.RetryCallException;
/*    */ import com.mojang.realmsclient.gui.screens.configuration.RealmsConfigureWorldScreen;
/*    */ import net.minecraft.client.gui.screens.Screen;
/*    */ import net.minecraft.network.chat.Component;
/*    */ import org.slf4j.Logger;
/*    */ 
/*    */ public class CloseServerTask extends LongRunningTask {
/* 12 */   private static final Logger LOGGER = LogUtils.getLogger();
/*    */   
/* 14 */   private static final Component TITLE = (Component)Component.translatable("mco.configure.world.closing");
/*    */   
/*    */   private final RealmsServer serverData;
/*    */   
/*    */   private final RealmsConfigureWorldScreen configureScreen;
/*    */   
/*    */   public CloseServerTask(RealmsServer realmsServer, RealmsConfigureWorldScreen configureWorldScreen) {
/* 20 */     this.serverData = realmsServer;
/* 21 */     this.configureScreen = configureWorldScreen;
/*    */   }
/*    */   
/*    */   public void run() {
/* 26 */     RealmsClient client = RealmsClient.getOrCreate();
/* 27 */     for (int i = 0; i < 25; i++) {
/* 28 */       if (aborted())
/*    */         return; 
/*    */       try {
/* 33 */         boolean closeResult = client.close(this.serverData.id).booleanValue();
/* 34 */         if (closeResult) {
/* 35 */           this.configureScreen.stateChanged();
/* 36 */           this.serverData.state = RealmsServer.State.CLOSED;
/* 37 */           setScreen((Screen)this.configureScreen);
/*    */           break;
/*    */         } 
/* 40 */       } catch (RetryCallException e) {
/* 41 */         if (aborted())
/*    */           return; 
/* 44 */         pause(e.delaySeconds);
/* 45 */       } catch (Exception e) {
/* 46 */         if (aborted())
/*    */           return; 
/* 49 */         LOGGER.error("Failed to close server", e);
/* 50 */         error(e);
/*    */       } 
/*    */     } 
/*    */   }
/*    */   
/*    */   public Component getTitle() {
/* 57 */     return TITLE;
/*    */   }
/*    */ }


/* Location:              C:\Users\karol\AppData\Roaming\ModrinthApp\meta\versions\26.2-0.19.5\26.2-0.19.5.jar!\com\mojang\realmsclien\\util\task\CloseServerTask.class
 * Java compiler version: 25 (69.0)
 * JD-Core Version:       1.1.3
 */