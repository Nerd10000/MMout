/*     */ package com.mojang.blaze3d.audio;
/*     */ 
/*     */ import com.google.common.collect.Sets;
/*     */ import java.util.Set;
/*     */ import net.minecraft.SharedConstants;
/*     */ 
/*     */ class CountingChannelPool implements Library.ChannelPool {
/*     */   private final int limit;
/*     */   
/*  90 */   private final Set<Channel> activeChannels = Sets.newIdentityHashSet();
/*     */   
/*     */   public CountingChannelPool(int limit) {
/*  93 */     this.limit = limit;
/*     */   }
/*     */   
/*     */   public Channel acquire() {
/*  98 */     if (this.activeChannels.size() >= this.limit) {
/*  99 */       if (SharedConstants.IS_RUNNING_IN_IDE)
/* 100 */         Library.LOGGER.warn("Maximum sound pool size {} reached", Integer.valueOf(this.limit)); 
/* 102 */       return null;
/*     */     } 
/* 105 */     Channel channel = Channel.create();
/* 106 */     if (channel != null)
/* 107 */       this.activeChannels.add(channel); 
/* 110 */     return channel;
/*     */   }
/*     */   
/*     */   public boolean release(Channel channel) {
/* 115 */     if (!this.activeChannels.remove(channel))
/* 116 */       return false; 
/* 118 */     channel.destroy();
/* 119 */     return true;
/*     */   }
/*     */   
/*     */   public void cleanup() {
/* 124 */     this.activeChannels.forEach(Channel::destroy);
/* 125 */     this.activeChannels.clear();
/*     */   }
/*     */   
/*     */   public int getMaxCount() {
/* 130 */     return this.limit;
/*     */   }
/*     */   
/*     */   public int getUsedCount() {
/* 135 */     return this.activeChannels.size();
/*     */   }
/*     */ }


/* Location:              C:\Users\karol\AppData\Roaming\ModrinthApp\meta\versions\26.2-0.19.5\26.2-0.19.5.jar!\com\mojang\blaze3d\audio\Library$CountingChannelPool.class
 * Java compiler version: 25 (69.0)
 * JD-Core Version:       1.1.3
 */