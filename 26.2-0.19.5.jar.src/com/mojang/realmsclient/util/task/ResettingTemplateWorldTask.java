/*    */ package com.mojang.realmsclient.util.task;
/*    */ 
/*    */ import com.mojang.realmsclient.client.RealmsClient;
/*    */ import com.mojang.realmsclient.dto.WorldTemplate;
/*    */ import com.mojang.realmsclient.exception.RealmsServiceException;
/*    */ import net.minecraft.network.chat.Component;
/*    */ 
/*    */ public class ResettingTemplateWorldTask extends ResettingWorldTask {
/*    */   private final WorldTemplate template;
/*    */   
/*    */   public ResettingTemplateWorldTask(WorldTemplate template, long serverId, Component title, Runnable callback) {
/* 12 */     super(serverId, title, callback);
/* 13 */     this.template = template;
/*    */   }
/*    */   
/*    */   protected void sendResetRequest(RealmsClient client, long serverId) throws RealmsServiceException {
/* 18 */     client.resetWorldWithTemplate(serverId, this.template.id());
/*    */   }
/*    */ }


/* Location:              C:\Users\karol\AppData\Roaming\ModrinthApp\meta\versions\26.2-0.19.5\26.2-0.19.5.jar!\com\mojang\realmsclien\\util\task\ResettingTemplateWorldTask.class
 * Java compiler version: 25 (69.0)
 * JD-Core Version:       1.1.3
 */