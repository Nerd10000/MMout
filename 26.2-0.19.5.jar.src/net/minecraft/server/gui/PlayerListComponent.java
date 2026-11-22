/*    */ package net.minecraft.server.gui;
/*    */ 
/*    */ import java.util.Vector;
/*    */ import javax.swing.JList;
/*    */ import net.minecraft.server.MinecraftServer;
/*    */ import net.minecraft.server.level.ServerPlayer;
/*    */ 
/*    */ public class PlayerListComponent extends JList<String> {
/*    */   private final MinecraftServer server;
/*    */   
/*    */   private int tickCount;
/*    */   
/*    */   public PlayerListComponent(MinecraftServer server) {
/* 13 */     this.server = server;
/* 14 */     server.addTickable(this::tick);
/*    */   }
/*    */   
/*    */   public void tick() {
/* 18 */     if (this.tickCount++ % 20 == 0) {
/* 19 */       Vector<String> players = new Vector<>();
/* 20 */       for (int i = 0; i < this.server.getPlayerList().getPlayers().size(); i++)
/* 21 */         players.add(((ServerPlayer)this.server.getPlayerList().getPlayers().get(i)).getGameProfile().name()); 
/* 23 */       setListData(players);
/*    */     } 
/*    */   }
/*    */ }


/* Location:              C:\Users\karol\AppData\Roaming\ModrinthApp\meta\versions\26.2-0.19.5\26.2-0.19.5.jar!\net\minecraft\server\gui\PlayerListComponent.class
 * Java compiler version: 25 (69.0)
 * JD-Core Version:       1.1.3
 */