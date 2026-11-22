/*    */ package net.minecraft.server.level;
/*    */ 
/*    */ import it.unimi.dsi.fastutil.objects.Object2BooleanMap;
/*    */ import it.unimi.dsi.fastutil.objects.Object2BooleanOpenHashMap;
/*    */ import java.util.Set;
/*    */ 
/*    */ public final class PlayerMap {
/*  9 */   private final Object2BooleanMap<ServerPlayer> players = (Object2BooleanMap<ServerPlayer>)new Object2BooleanOpenHashMap();
/*    */   
/*    */   public Set<ServerPlayer> getAllPlayers() {
/* 12 */     return (Set<ServerPlayer>)this.players.keySet();
/*    */   }
/*    */   
/*    */   public void addPlayer(ServerPlayer player, boolean ignored) {
/* 16 */     this.players.put(player, ignored);
/*    */   }
/*    */   
/*    */   public void removePlayer(ServerPlayer player) {
/* 20 */     this.players.removeBoolean(player);
/*    */   }
/*    */   
/*    */   public void ignorePlayer(ServerPlayer player) {
/* 24 */     this.players.replace(player, true);
/*    */   }
/*    */   
/*    */   public void unIgnorePlayer(ServerPlayer player) {
/* 28 */     this.players.replace(player, false);
/*    */   }
/*    */   
/*    */   public boolean ignoredOrUnknown(ServerPlayer player) {
/* 32 */     return this.players.getOrDefault(player, true);
/*    */   }
/*    */   
/*    */   public boolean ignored(ServerPlayer player) {
/* 36 */     return this.players.getBoolean(player);
/*    */   }
/*    */ }


/* Location:              C:\Users\karol\AppData\Roaming\ModrinthApp\meta\versions\26.2-0.19.5\26.2-0.19.5.jar!\net\minecraft\server\level\PlayerMap.class
 * Java compiler version: 25 (69.0)
 * JD-Core Version:       1.1.3
 */