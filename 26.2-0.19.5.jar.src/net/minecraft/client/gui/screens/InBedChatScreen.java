/*    */ package net.minecraft.client.gui.screens;
/*    */ 
/*    */ import net.minecraft.client.gui.components.Button;
/*    */ import net.minecraft.client.multiplayer.ClientPacketListener;
/*    */ import net.minecraft.network.chat.Component;
/*    */ import net.minecraft.network.protocol.Packet;
/*    */ import net.minecraft.network.protocol.game.ServerboundPlayerCommandPacket;
/*    */ import net.minecraft.world.entity.Entity;
/*    */ 
/*    */ public class InBedChatScreen extends ChatScreen {
/*    */   private Button leaveBedButton;
/*    */   
/*    */   public InBedChatScreen(String initial, boolean isDraft) {
/* 15 */     super(initial, isDraft, false);
/*    */   }
/*    */   
/*    */   protected void init() {
/* 20 */     super.init();
/* 22 */     this.leaveBedButton = Button.builder((Component)Component.translatable("multiplayer.stopSleeping"), button -> sendWakeUp()).bounds(this.width / 2 - 100, this.height - 40, 200, 20).build();
/* 23 */     addRenderableWidget(this.leaveBedButton);
/*    */   }
/*    */   
/*    */   public void onClose() {
/* 28 */     sendWakeUp();
/*    */   }
/*    */   
/*    */   private void sendWakeUp() {
/* 32 */     ClientPacketListener connection = this.minecraft.player.connection;
/* 33 */     connection.send((Packet)new ServerboundPlayerCommandPacket((Entity)this.minecraft.player, ServerboundPlayerCommandPacket.Action.STOP_SLEEPING));
/*    */   }
/*    */   
/*    */   public void onPlayerWokeUp() {
/* 37 */     String text = this.input.getValue();
/* 39 */     if (this.isDraft || text.isEmpty()) {
/* 40 */       this.exitReason = ChatScreen.ExitReason.INTERRUPTED;
/* 41 */       this.minecraft.gui.setScreen(null);
/*    */     } else {
/* 43 */       this.exitReason = ChatScreen.ExitReason.DONE;
/* 44 */       this.minecraft.gui.setScreen(new ChatScreen(text, false));
/*    */     } 
/*    */   }
/*    */ }


/* Location:              C:\Users\karol\AppData\Roaming\ModrinthApp\meta\versions\26.2-0.19.5\26.2-0.19.5.jar!\net\minecraft\client\gui\screens\InBedChatScreen.class
 * Java compiler version: 25 (69.0)
 * JD-Core Version:       1.1.3
 */