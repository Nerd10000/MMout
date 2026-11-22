/*     */ package com.mojang.realmsclient;
/*     */ 
/*     */ import com.mojang.realmsclient.dto.RealmsNotification;
/*     */ import com.mojang.realmsclient.dto.RealmsServer;
/*     */ import java.util.List;
/*     */ import net.minecraft.client.Minecraft;
/*     */ import net.minecraft.client.gui.components.AbstractSelectionList;
/*     */ import net.minecraft.client.gui.components.ObjectSelectionList;
/*     */ import net.minecraft.network.chat.Component;
/*     */ import net.minecraft.network.chat.FormattedText;
/*     */ 
/*     */ class RealmSelectionList extends ObjectSelectionList<RealmsMainScreen.Entry> {
/*     */   public RealmSelectionList() {
/* 689 */     super(Minecraft.getInstance(), paramRealmsMainScreen.width, paramRealmsMainScreen.height, 0, 36);
/*     */   }
/*     */   
/*     */   public void setSelected(RealmsMainScreen.Entry selected) {
/* 694 */     super.setSelected((AbstractSelectionList.Entry)selected);
/* 695 */     RealmsMainScreen.this.updateButtonStates();
/*     */   }
/*     */   
/*     */   public int getRowWidth() {
/* 700 */     return 300;
/*     */   }
/*     */   
/*     */   private void refreshEntries(RealmsMainScreen realmsMainScreen) {
/* 704 */     RealmsMainScreen.Entry previouslySelected = (RealmsMainScreen.Entry)getSelected();
/* 705 */     clearEntries();
/* 707 */     for (RealmsNotification notification : RealmsMainScreen.this.notifications) {
/* 708 */       if (notification instanceof RealmsNotification.VisitUrl) {
/* 708 */         RealmsNotification.VisitUrl visitUrl = (RealmsNotification.VisitUrl)notification;
/* 709 */         addEntriesForNotification(visitUrl, realmsMainScreen, previouslySelected);
/* 710 */         RealmsMainScreen.this.markNotificationsAsSeen(List.of(notification));
/*     */         break;
/*     */       } 
/*     */     } 
/* 716 */     refreshServerEntries(previouslySelected);
/*     */   }
/*     */   
/*     */   private void addEntriesForNotification(RealmsNotification.VisitUrl visitUrl, RealmsMainScreen realmsMainScreen, RealmsMainScreen.Entry previouslySelected) {
/* 720 */     Component message = visitUrl.getMessage();
/* 721 */     int messageHeight = RealmsMainScreen.access$300(RealmsMainScreen.this).wordWrapHeight((FormattedText)message, RealmsMainScreen.NotificationMessageEntry.textWidth(getRowWidth()));
/* 722 */     RealmsMainScreen.NotificationMessageEntry entry = new RealmsMainScreen.NotificationMessageEntry(RealmsMainScreen.this, realmsMainScreen, messageHeight, message, visitUrl);
/* 723 */     addEntry((AbstractSelectionList.Entry)entry, 38 + messageHeight);
/* 724 */     if (previouslySelected instanceof RealmsMainScreen.NotificationMessageEntry) {
/* 724 */       RealmsMainScreen.NotificationMessageEntry notificationMessageEntry = (RealmsMainScreen.NotificationMessageEntry)previouslySelected;
/* 724 */       if (notificationMessageEntry.getText().equals(message))
/* 725 */         setSelected(entry); 
/*     */     } 
/*     */   }
/*     */   
/*     */   private void refreshServerEntries(RealmsMainScreen.Entry previouslySelected) {
/* 730 */     for (RealmsServer eligibleForSnapshotServer : RealmsMainScreen.this.availableSnapshotServers)
/* 731 */       addEntry((AbstractSelectionList.Entry)new RealmsMainScreen.AvailableSnapshotEntry(RealmsMainScreen.this, eligibleForSnapshotServer)); 
/* 734 */     for (RealmsServer server : RealmsMainScreen.this.serverList) {
/*     */       RealmsMainScreen.Entry entry;
/* 736 */       if (RealmsMainScreen.isSnapshot() && !server.isSnapshotRealm()) {
/* 737 */         if (server.state == RealmsServer.State.UNINITIALIZED)
/*     */           continue; 
/* 740 */         entry = new RealmsMainScreen.ParentEntry(RealmsMainScreen.this, server);
/*     */       } else {
/* 742 */         entry = new RealmsMainScreen.ServerEntry(RealmsMainScreen.this, server);
/*     */       } 
/* 744 */       addEntry((AbstractSelectionList.Entry)entry);
/* 745 */       if (previouslySelected instanceof RealmsMainScreen.ServerEntry) {
/* 745 */         RealmsMainScreen.ServerEntry serverEntry = (RealmsMainScreen.ServerEntry)previouslySelected;
/* 745 */         if (serverEntry.serverData.id == server.id)
/* 746 */           setSelected(entry); 
/*     */       } 
/*     */     } 
/*     */   }
/*     */ }


/* Location:              C:\Users\karol\AppData\Roaming\ModrinthApp\meta\versions\26.2-0.19.5\26.2-0.19.5.jar!\com\mojang\realmsclient\RealmsMainScreen$RealmSelectionList.class
 * Java compiler version: 25 (69.0)
 * JD-Core Version:       1.1.3
 */