/*    */ package net.minecraft.client.sounds;
/*    */ 
/*    */ import com.mojang.blaze3d.audio.Channel;
/*    */ import java.util.function.Consumer;
/*    */ 
/*    */ public class ChannelHandle {
/*    */   private Channel channel;
/*    */   
/*    */   private boolean stopped;
/*    */   
/*    */   public boolean isStopped() {
/* 22 */     return this.stopped;
/*    */   }
/*    */   
/*    */   public ChannelHandle(Channel channel) {
/* 26 */     this.channel = channel;
/*    */   }
/*    */   
/*    */   public void execute(Consumer<Channel> action) {
/* 30 */     ChannelAccess.this.executor.execute(() -> {
/*    */           if (this.channel != null)
/*    */             action.accept(this.channel); 
/*    */         });
/*    */   }
/*    */   
/*    */   public void release() {
/* 40 */     this.stopped = true;
/* 41 */     ChannelAccess.this.library.releaseChannel(this.channel);
/* 42 */     this.channel = null;
/*    */   }
/*    */ }


/* Location:              C:\Users\karol\AppData\Roaming\ModrinthApp\meta\versions\26.2-0.19.5\26.2-0.19.5.jar!\net\minecraft\client\sounds\ChannelAccess$ChannelHandle.class
 * Java compiler version: 25 (69.0)
 * JD-Core Version:       1.1.3
 */