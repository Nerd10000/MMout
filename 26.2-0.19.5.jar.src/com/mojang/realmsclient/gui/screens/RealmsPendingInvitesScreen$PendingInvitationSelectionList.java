/*     */ package com.mojang.realmsclient.gui.screens;
/*     */ 
/*     */ import net.minecraft.client.Minecraft;
/*     */ import net.minecraft.client.gui.components.AbstractSelectionList;
/*     */ import net.minecraft.client.gui.components.ContainerObjectSelectionList;
/*     */ 
/*     */ class PendingInvitationSelectionList extends ContainerObjectSelectionList<RealmsPendingInvitesScreen.Entry> {
/*     */   public static final int ITEM_HEIGHT = 36;
/*     */   
/*     */   public PendingInvitationSelectionList(RealmsPendingInvitesScreen paramRealmsPendingInvitesScreen, Minecraft minecraft) {
/* 107 */     super(minecraft, paramRealmsPendingInvitesScreen.width, paramRealmsPendingInvitesScreen.layout.getContentHeight(), paramRealmsPendingInvitesScreen.layout.getHeaderHeight(), 36);
/*     */   }
/*     */   
/*     */   public int getRowWidth() {
/* 112 */     return 280;
/*     */   }
/*     */   
/*     */   public boolean hasPendingInvites() {
/* 116 */     return (getItemCount() == 0);
/*     */   }
/*     */   
/*     */   public void removeInvitation(RealmsPendingInvitesScreen.Entry entry) {
/* 120 */     removeEntry((AbstractSelectionList.Entry)entry);
/*     */   }
/*     */ }


/* Location:              C:\Users\karol\AppData\Roaming\ModrinthApp\meta\versions\26.2-0.19.5\26.2-0.19.5.jar!\com\mojang\realmsclient\gui\screens\RealmsPendingInvitesScreen$PendingInvitationSelectionList.class
 * Java compiler version: 25 (69.0)
 * JD-Core Version:       1.1.3
 */