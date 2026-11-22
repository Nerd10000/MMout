/*     */ package net.minecraft.server.level;
/*     */ 
/*     */ import com.google.common.base.Objects;
/*     */ import com.google.common.collect.Lists;
/*     */ import com.google.common.collect.Sets;
/*     */ import java.util.Collection;
/*     */ import java.util.Collections;
/*     */ import java.util.Set;
/*     */ import java.util.UUID;
/*     */ import java.util.function.Function;
/*     */ import net.minecraft.network.chat.Component;
/*     */ import net.minecraft.network.protocol.Packet;
/*     */ import net.minecraft.network.protocol.game.ClientboundBossEventPacket;
/*     */ import net.minecraft.world.BossEvent;
/*     */ 
/*     */ public class ServerBossEvent extends BossEvent {
/*  17 */   private final Set<ServerPlayer> players = Sets.newHashSet();
/*     */   
/*  18 */   private final Set<ServerPlayer> unmodifiablePlayers = Collections.unmodifiableSet(this.players);
/*     */   
/*     */   private boolean visible = true;
/*     */   
/*     */   public ServerBossEvent(UUID id, Component name, BossEvent.BossBarColor color, BossEvent.BossBarOverlay overlay) {
/*  22 */     super(id, name, color, overlay);
/*     */   }
/*     */   
/*     */   public void setProgress(float progress) {
/*  27 */     if (progress != this.progress) {
/*  28 */       super.setProgress(progress);
/*  29 */       setDirty();
/*  30 */       broadcast(ClientboundBossEventPacket::createUpdateProgressPacket);
/*     */     } 
/*     */   }
/*     */   
/*     */   public void setColor(BossEvent.BossBarColor color) {
/*  36 */     if (color != this.color) {
/*  37 */       super.setColor(color);
/*  38 */       setDirty();
/*  39 */       broadcast(ClientboundBossEventPacket::createUpdateStylePacket);
/*     */     } 
/*     */   }
/*     */   
/*     */   public void setOverlay(BossEvent.BossBarOverlay overlay) {
/*  45 */     if (overlay != this.overlay) {
/*  46 */       super.setOverlay(overlay);
/*  47 */       setDirty();
/*  48 */       broadcast(ClientboundBossEventPacket::createUpdateStylePacket);
/*     */     } 
/*     */   }
/*     */   
/*     */   public BossEvent setDarkenScreen(boolean darkenScreen) {
/*  54 */     if (darkenScreen != this.darkenScreen) {
/*  55 */       super.setDarkenScreen(darkenScreen);
/*  56 */       setDirty();
/*  57 */       broadcast(ClientboundBossEventPacket::createUpdatePropertiesPacket);
/*     */     } 
/*  59 */     return this;
/*     */   }
/*     */   
/*     */   public BossEvent setPlayBossMusic(boolean playBossMusic) {
/*  64 */     if (playBossMusic != this.playBossMusic) {
/*  65 */       super.setPlayBossMusic(playBossMusic);
/*  66 */       setDirty();
/*  67 */       broadcast(ClientboundBossEventPacket::createUpdatePropertiesPacket);
/*     */     } 
/*  69 */     return this;
/*     */   }
/*     */   
/*     */   public BossEvent setCreateWorldFog(boolean createWorldFog) {
/*  74 */     if (createWorldFog != this.createWorldFog) {
/*  75 */       super.setCreateWorldFog(createWorldFog);
/*  76 */       setDirty();
/*  77 */       broadcast(ClientboundBossEventPacket::createUpdatePropertiesPacket);
/*     */     } 
/*  79 */     return this;
/*     */   }
/*     */   
/*     */   public void setName(Component name) {
/*  84 */     if (!Objects.equal(name, this.name)) {
/*  85 */       super.setName(name);
/*  86 */       setDirty();
/*  87 */       broadcast(ClientboundBossEventPacket::createUpdateNamePacket);
/*     */     } 
/*     */   }
/*     */   
/*     */   private void broadcast(Function<BossEvent, ClientboundBossEventPacket> factory) {
/*  92 */     if (this.visible) {
/*  93 */       ClientboundBossEventPacket packet = factory.apply(this);
/*  94 */       for (ServerPlayer player : this.players)
/*  95 */         player.connection.send((Packet)packet); 
/*     */     } 
/*     */   }
/*     */   
/*     */   public void addPlayer(ServerPlayer player) {
/* 101 */     if (this.players.add(player) && this.visible)
/* 102 */       player.connection.send((Packet)ClientboundBossEventPacket.createAddPacket(this)); 
/*     */   }
/*     */   
/*     */   public void removePlayer(ServerPlayer player) {
/* 107 */     if (this.players.remove(player) && this.visible)
/* 108 */       player.connection.send((Packet)ClientboundBossEventPacket.createRemovePacket(getId())); 
/*     */   }
/*     */   
/*     */   public void removeAllPlayers() {
/* 113 */     if (!this.players.isEmpty())
/* 114 */       for (ServerPlayer player : Lists.newArrayList(this.players))
/* 115 */         removePlayer(player);  
/*     */   }
/*     */   
/*     */   public boolean isVisible() {
/* 121 */     return this.visible;
/*     */   }
/*     */   
/*     */   public void setVisible(boolean visible) {
/* 125 */     if (visible != this.visible) {
/* 126 */       this.visible = visible;
/* 127 */       setDirty();
/* 128 */       for (ServerPlayer player : this.players)
/* 129 */         player.connection.send(visible ? (Packet)ClientboundBossEventPacket.createAddPacket(this) : (Packet)ClientboundBossEventPacket.createRemovePacket(getId())); 
/*     */     } 
/*     */   }
/*     */   
/*     */   public Collection<ServerPlayer> getPlayers() {
/* 135 */     return this.unmodifiablePlayers;
/*     */   }
/*     */   
/*     */   protected void setDirty() {}
/*     */ }


/* Location:              C:\Users\karol\AppData\Roaming\ModrinthApp\meta\versions\26.2-0.19.5\26.2-0.19.5.jar!\net\minecraft\server\level\ServerBossEvent.class
 * Java compiler version: 25 (69.0)
 * JD-Core Version:       1.1.3
 */