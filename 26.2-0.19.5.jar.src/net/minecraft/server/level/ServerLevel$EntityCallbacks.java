/*      */ package net.minecraft.server.level;
/*      */ 
/*      */ import net.minecraft.util.Util;
/*      */ import net.minecraft.world.entity.Entity;
/*      */ import net.minecraft.world.entity.Mob;
/*      */ import net.minecraft.world.entity.boss.enderdragon.EnderDragon;
/*      */ import net.minecraft.world.entity.boss.enderdragon.EnderDragonPart;
/*      */ import net.minecraft.world.level.entity.LevelCallback;
/*      */ import net.minecraft.world.level.gameevent.DynamicGameEventListener;
/*      */ import net.minecraft.world.waypoints.WaypointTransmitter;
/*      */ 
/*      */ final class EntityCallbacks implements LevelCallback<Entity> {
/*      */   public void onCreated(Entity entity) {
/* 1720 */     if (entity instanceof WaypointTransmitter) {
/* 1720 */       WaypointTransmitter waypoint = (WaypointTransmitter)entity;
/* 1720 */       if (waypoint.isTransmittingWaypoint())
/* 1721 */         ServerLevel.this.getWaypointManager().trackWaypoint(waypoint); 
/*      */     } 
/*      */   }
/*      */   
/*      */   public void onDestroyed(Entity entity) {
/* 1727 */     if (entity instanceof WaypointTransmitter) {
/* 1727 */       WaypointTransmitter waypoint = (WaypointTransmitter)entity;
/* 1728 */       ServerLevel.this.getWaypointManager().untrackWaypoint(waypoint);
/*      */     } 
/* 1730 */     ServerLevel.this.getScoreboard().entityRemoved(entity);
/*      */   }
/*      */   
/*      */   public void onTickingStart(Entity entity) {
/* 1735 */     ServerLevel.this.entityTickList.add(entity);
/*      */   }
/*      */   
/*      */   public void onTickingEnd(Entity entity) {
/* 1740 */     ServerLevel.this.entityTickList.remove(entity);
/*      */   }
/*      */   
/*      */   public void onTrackingStart(Entity entity) {
/* 1745 */     ServerLevel.this.getChunkSource().addEntity(entity);
/* 1746 */     if (entity instanceof ServerPlayer) {
/* 1746 */       ServerPlayer player = (ServerPlayer)entity;
/* 1747 */       ServerLevel.this.players.add(player);
/* 1748 */       if (player.isReceivingWaypoints())
/* 1749 */         ServerLevel.this.getWaypointManager().addPlayer(player); 
/* 1751 */       ServerLevel.this.updateSleepingPlayerList();
/*      */     } 
/* 1753 */     if (entity instanceof WaypointTransmitter) {
/* 1753 */       WaypointTransmitter waypoint = (WaypointTransmitter)entity;
/* 1753 */       if (waypoint.isTransmittingWaypoint())
/* 1754 */         ServerLevel.this.getWaypointManager().trackWaypoint(waypoint); 
/*      */     } 
/* 1756 */     if (entity instanceof Mob) {
/* 1756 */       Mob mob = (Mob)entity;
/* 1757 */       if (ServerLevel.this.isUpdatingNavigations) {
/* 1758 */         String message = "onTrackingStart called during navigation iteration";
/* 1759 */         Util.logAndPauseIfInIde("onTrackingStart called during navigation iteration", new IllegalStateException("onTrackingStart called during navigation iteration"));
/*      */       } 
/* 1761 */       ServerLevel.this.navigatingMobs.add(mob);
/*      */     } 
/* 1763 */     if (entity instanceof EnderDragon) {
/* 1763 */       EnderDragon dragon = (EnderDragon)entity;
/* 1764 */       for (EnderDragonPart subEntity : dragon.getSubEntities())
/* 1765 */         ServerLevel.this.dragonParts.put(subEntity.getId(), subEntity); 
/*      */     } 
/* 1769 */     entity.updateDynamicGameEventListener(DynamicGameEventListener::add);
/*      */   }
/*      */   
/*      */   public void onTrackingEnd(Entity entity) {
/* 1774 */     ServerLevel.this.getChunkSource().removeEntity(entity);
/* 1775 */     if (entity instanceof ServerPlayer) {
/* 1775 */       ServerPlayer player = (ServerPlayer)entity;
/* 1776 */       ServerLevel.this.players.remove(player);
/* 1777 */       ServerLevel.this.getWaypointManager().removePlayer(player);
/* 1778 */       ServerLevel.this.updateSleepingPlayerList();
/*      */     } 
/* 1780 */     if (entity instanceof Mob) {
/* 1780 */       Mob mob = (Mob)entity;
/* 1781 */       if (ServerLevel.this.isUpdatingNavigations) {
/* 1782 */         String message = "onTrackingStart called during navigation iteration";
/* 1783 */         Util.logAndPauseIfInIde("onTrackingStart called during navigation iteration", new IllegalStateException("onTrackingStart called during navigation iteration"));
/*      */       } 
/* 1785 */       ServerLevel.this.navigatingMobs.remove(mob);
/*      */     } 
/* 1787 */     if (entity instanceof EnderDragon) {
/* 1787 */       EnderDragon dragon = (EnderDragon)entity;
/* 1788 */       for (EnderDragonPart subEntity : dragon.getSubEntities())
/* 1789 */         ServerLevel.this.dragonParts.remove(subEntity.getId()); 
/*      */     } 
/* 1793 */     entity.updateDynamicGameEventListener(DynamicGameEventListener::remove);
/* 1795 */     ServerLevel.this.debugSynchronizers.dropEntity(entity);
/*      */   }
/*      */   
/*      */   public void onSectionChange(Entity entity) {
/* 1800 */     entity.updateDynamicGameEventListener(DynamicGameEventListener::move);
/*      */   }
/*      */ }


/* Location:              C:\Users\karol\AppData\Roaming\ModrinthApp\meta\versions\26.2-0.19.5\26.2-0.19.5.jar!\net\minecraft\server\level\ServerLevel$EntityCallbacks.class
 * Java compiler version: 25 (69.0)
 * JD-Core Version:       1.1.3
 */