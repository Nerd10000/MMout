/*     */ package com.mojang.realmsclient.gui.screens;
/*     */ 
/*     */ import com.mojang.realmsclient.gui.RealmsDataFetcher;
/*     */ import com.mojang.realmsclient.gui.task.DataFetcher;
/*     */ 
/*     */ class null implements RealmsNotificationsScreen.DataFetcherConfiguration {
/*     */   public DataFetcher.Subscription initDataFetcher(RealmsDataFetcher dataSource) {
/* 164 */     DataFetcher.Subscription result = dataSource.dataFetcher.createSubscription();
/* 166 */     RealmsNotificationsScreen.this.addNewsAndInvitesSubscriptions(dataSource, result);
/* 167 */     RealmsNotificationsScreen.this.addNotificationsSubscriptions(dataSource, result);
/* 169 */     return result;
/*     */   }
/*     */   
/*     */   public boolean showOldNotifications() {
/* 174 */     return true;
/*     */   }
/*     */ }


/* Location:              C:\Users\karol\AppData\Roaming\ModrinthApp\meta\versions\26.2-0.19.5\26.2-0.19.5.jar!\com\mojang\realmsclient\gui\screens\RealmsNotificationsScreen$1.class
 * Java compiler version: 25 (69.0)
 * JD-Core Version:       1.1.3
 */