/*    */ package net.minecraft.client.gui.spectator;
/*    */ 
/*    */ import net.minecraft.client.Minecraft;
/*    */ import net.minecraft.client.gui.GuiGraphicsExtractor;
/*    */ import net.minecraft.client.gui.components.PlayerFaceExtractor;
/*    */ import net.minecraft.client.multiplayer.PlayerInfo;
/*    */ import net.minecraft.network.chat.Component;
/*    */ import net.minecraft.network.protocol.Packet;
/*    */ import net.minecraft.network.protocol.game.ServerboundTeleportToEntityPacket;
/*    */ import net.minecraft.util.ARGB;
/*    */ import net.minecraft.world.level.GameType;
/*    */ 
/*    */ public class PlayerMenuItem implements SpectatorMenuItem {
/*    */   private final PlayerInfo playerInfo;
/*    */   
/*    */   private final Component name;
/*    */   
/*    */   public PlayerMenuItem(PlayerInfo playerInfo) {
/* 17 */     this.playerInfo = playerInfo;
/* 18 */     this.name = (Component)Component.literal(playerInfo.getProfile().name());
/*    */   }
/*    */   
/*    */   public void selectItem(SpectatorMenu menu) {
/* 23 */     if (isEnabled())
/* 24 */       Minecraft.getInstance().getConnection().send((Packet)new ServerboundTeleportToEntityPacket(this.playerInfo.getProfile().id())); 
/*    */   }
/*    */   
/*    */   public Component getName() {
/* 30 */     return this.name;
/*    */   }
/*    */   
/*    */   public void extractIcon(GuiGraphicsExtractor graphics, float brightness, float alpha) {
/* 35 */     PlayerFaceExtractor.extractRenderState(graphics, this.playerInfo.getSkin(), 2, 2, 12, ARGB.white(alpha));
/*    */   }
/*    */   
/*    */   public boolean isEnabled() {
/* 40 */     return (this.playerInfo.getGameMode() != GameType.SPECTATOR);
/*    */   }
/*    */ }


/* Location:              C:\Users\karol\AppData\Roaming\ModrinthApp\meta\versions\26.2-0.19.5\26.2-0.19.5.jar!\net\minecraft\client\gui\spectator\PlayerMenuItem.class
 * Java compiler version: 25 (69.0)
 * JD-Core Version:       1.1.3
 */