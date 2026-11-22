/*    */ package net.minecraft.world.item;
/*    */ 
/*    */ import net.minecraft.network.protocol.Packet;
/*    */ import net.minecraft.network.protocol.game.ClientboundCooldownPacket;
/*    */ import net.minecraft.resources.Identifier;
/*    */ import net.minecraft.server.level.ServerPlayer;
/*    */ 
/*    */ public class ServerItemCooldowns extends ItemCooldowns {
/*    */   private final ServerPlayer player;
/*    */   
/*    */   public ServerItemCooldowns(ServerPlayer player) {
/* 11 */     this.player = player;
/*    */   }
/*    */   
/*    */   protected void onCooldownStarted(Identifier cooldownGroup, int duration) {
/* 16 */     super.onCooldownStarted(cooldownGroup, duration);
/* 17 */     this.player.connection.send((Packet)new ClientboundCooldownPacket(cooldownGroup, duration));
/*    */   }
/*    */   
/*    */   protected void onCooldownEnded(Identifier cooldownGroup) {
/* 22 */     super.onCooldownEnded(cooldownGroup);
/* 23 */     this.player.connection.send((Packet)new ClientboundCooldownPacket(cooldownGroup, 0));
/*    */   }
/*    */ }


/* Location:              C:\Users\karol\AppData\Roaming\ModrinthApp\meta\versions\26.2-0.19.5\26.2-0.19.5.jar!\net\minecraft\world\item\ServerItemCooldowns.class
 * Java compiler version: 25 (69.0)
 * JD-Core Version:       1.1.3
 */