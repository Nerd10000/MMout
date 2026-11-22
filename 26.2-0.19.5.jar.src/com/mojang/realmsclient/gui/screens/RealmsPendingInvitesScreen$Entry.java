/*     */ package com.mojang.realmsclient.gui.screens;
/*     */ 
/*     */ import com.mojang.realmsclient.client.RealmsClient;
/*     */ import com.mojang.realmsclient.dto.PendingInvite;
/*     */ import com.mojang.realmsclient.exception.RealmsServiceException;
/*     */ import com.mojang.realmsclient.gui.RealmsDataFetcher;
/*     */ import com.mojang.realmsclient.util.RealmsUtil;
/*     */ import java.util.ArrayList;
/*     */ import java.util.Collection;
/*     */ import java.util.List;
/*     */ import java.util.concurrent.CompletableFuture;
/*     */ import java.util.concurrent.Executor;
/*     */ import java.util.function.Supplier;
/*     */ import net.minecraft.client.gui.GuiGraphicsExtractor;
/*     */ import net.minecraft.client.gui.components.AbstractWidget;
/*     */ import net.minecraft.client.gui.components.Button;
/*     */ import net.minecraft.client.gui.components.ContainerObjectSelectionList;
/*     */ import net.minecraft.client.gui.components.SpriteIconButton;
/*     */ import net.minecraft.client.gui.components.StringWidget;
/*     */ import net.minecraft.client.gui.components.WidgetSprites;
/*     */ import net.minecraft.client.gui.components.events.GuiEventListener;
/*     */ import net.minecraft.client.gui.narration.NarratableEntry;
/*     */ import net.minecraft.network.chat.CommonComponents;
/*     */ import net.minecraft.network.chat.Component;
/*     */ import net.minecraft.network.chat.ComponentUtils;
/*     */ import net.minecraft.network.chat.MutableComponent;
/*     */ import net.minecraft.network.chat.Style;
/*     */ import net.minecraft.resources.Identifier;
/*     */ import net.minecraft.util.Util;
/*     */ 
/*     */ class Entry extends ContainerObjectSelectionList.Entry<RealmsPendingInvitesScreen.Entry> {
/* 125 */   private static final Component ACCEPT_INVITE = (Component)Component.translatable("mco.invites.button.accept");
/*     */   
/* 126 */   private static final Component REJECT_INVITE = (Component)Component.translatable("mco.invites.button.reject");
/*     */   
/* 128 */   private static final WidgetSprites ACCEPT_SPRITE = new WidgetSprites(
/* 129 */       Identifier.withDefaultNamespace("pending_invite/accept"), 
/* 130 */       Identifier.withDefaultNamespace("pending_invite/accept_highlighted"));
/*     */   
/* 132 */   private static final WidgetSprites REJECT_SPRITE = new WidgetSprites(
/* 133 */       Identifier.withDefaultNamespace("pending_invite/reject"), 
/* 134 */       Identifier.withDefaultNamespace("pending_invite/reject_highlighted"));
/*     */   
/*     */   private static final int SPRITE_TEXTURE_SIZE = 18;
/*     */   
/*     */   private static final int SPRITE_SIZE = 21;
/*     */   
/*     */   private static final int TEXT_LEFT = 38;
/*     */   
/*     */   private final PendingInvite pendingInvite;
/*     */   
/* 143 */   private final List<AbstractWidget> children = new ArrayList<>();
/*     */   
/*     */   private final SpriteIconButton acceptButton;
/*     */   
/*     */   private final SpriteIconButton rejectButton;
/*     */   
/*     */   private final StringWidget realmName;
/*     */   
/*     */   private final StringWidget realmOwnerName;
/*     */   
/*     */   private final StringWidget inviteDate;
/*     */   
/*     */   public Entry(PendingInvite pendingInvite) {
/* 152 */     this.pendingInvite = pendingInvite;
/* 154 */     int maxTextWidth = paramRealmsPendingInvitesScreen.pendingInvitationSelectionList.getRowWidth() - 32 - 32 - 42;
/* 155 */     this
/* 156 */       .realmName = (new StringWidget((Component)Component.literal(pendingInvite.realmName()), RealmsPendingInvitesScreen.access$000(paramRealmsPendingInvitesScreen))).setMaxWidth(maxTextWidth);
/* 157 */     this
/* 158 */       .realmOwnerName = (new StringWidget((Component)Component.literal(pendingInvite.realmOwnerName()).withColor(-6250336), RealmsPendingInvitesScreen.access$100(paramRealmsPendingInvitesScreen))).setMaxWidth(maxTextWidth);
/* 159 */     this
/* 160 */       .inviteDate = (new StringWidget(ComponentUtils.mergeStyles(RealmsUtil.convertToAgePresentationFromInstant(pendingInvite.date()), Style.EMPTY.withColor(-6250336)), RealmsPendingInvitesScreen.access$200(paramRealmsPendingInvitesScreen))).setMaxWidth(maxTextWidth);
/* 162 */     Button.CreateNarration narration = getCreateNarration(pendingInvite);
/* 163 */     this
/*     */       
/* 168 */       .acceptButton = SpriteIconButton.builder(ACCEPT_INVITE, button -> handleInvitation(true), false).sprite(ACCEPT_SPRITE, 18, 18).size(21, 21).narration(narration).withTootip().build();
/* 170 */     this
/*     */       
/* 175 */       .rejectButton = SpriteIconButton.builder(REJECT_INVITE, button -> handleInvitation(false), false).sprite(REJECT_SPRITE, 18, 18).size(21, 21).narration(narration).withTootip().build();
/* 176 */     this.children.addAll((Collection)List.of(this.acceptButton, this.rejectButton));
/*     */   }
/*     */   
/*     */   private Button.CreateNarration getCreateNarration(PendingInvite pendingInvite) {
/* 180 */     return defaultNarrationSupplier -> {
/*     */         MutableComponent narration = CommonComponents.joinForNarration(new Component[] { defaultNarrationSupplier.get(), (Component)Component.literal(pendingInvite.realmName()), (Component)Component.literal(pendingInvite.realmOwnerName()), RealmsUtil.convertToAgePresentationFromInstant(pendingInvite.date()) });
/*     */         return Component.translatable("narrator.select", new Object[] { narration });
/*     */       };
/*     */   }
/*     */   
/*     */   public List<? extends GuiEventListener> children() {
/* 191 */     return (List)this.children;
/*     */   }
/*     */   
/*     */   public List<? extends NarratableEntry> narratables() {
/* 196 */     return (List)this.children;
/*     */   }
/*     */   
/*     */   public void extractContent(GuiGraphicsExtractor graphics, int mouseX, int mouseY, boolean hovered, float a) {
/* 201 */     int x = getContentX();
/* 202 */     int y = getContentY();
/* 203 */     int textX = x + 38;
/* 204 */     RealmsUtil.extractPlayerFace(graphics, x, y, 32, this.pendingInvite.realmOwnerUuid());
/* 205 */     this.realmName.setPosition(textX, y + 1);
/* 206 */     this.realmName.extractWidgetRenderState(graphics, mouseX, mouseY, x);
/* 207 */     this.realmOwnerName.setPosition(textX, y + 12);
/* 208 */     this.realmOwnerName.extractWidgetRenderState(graphics, mouseX, mouseY, x);
/* 209 */     this.inviteDate.setPosition(textX, y + 24);
/* 210 */     this.inviteDate.extractWidgetRenderState(graphics, mouseX, mouseY, x);
/* 212 */     int buttonY = y + getContentHeight() / 2 - 10;
/* 213 */     this.acceptButton.setPosition(x + getContentWidth() - 16 - 42, buttonY);
/* 214 */     this.acceptButton.extractRenderState(graphics, mouseX, mouseY, a);
/* 215 */     this.rejectButton.setPosition(x + getContentWidth() - 8 - 21, buttonY);
/* 216 */     this.rejectButton.extractRenderState(graphics, mouseX, mouseY, a);
/*     */   }
/*     */   
/*     */   private void handleInvitation(boolean accept) {
/* 220 */     String invitationId = this.pendingInvite.invitationId();
/* 221 */     CompletableFuture.supplyAsync(() -> {
/*     */           try {
/*     */             RealmsClient client = RealmsClient.getOrCreate();
/*     */             if (accept) {
/*     */               client.acceptInvitation(invitationId);
/*     */             } else {
/*     */               client.rejectInvitation(invitationId);
/*     */             } 
/*     */             return Boolean.valueOf(true);
/* 230 */           } catch (RealmsServiceException e) {
/*     */             RealmsPendingInvitesScreen.LOGGER.error("Couldn't handle invite", (Throwable)e);
/*     */             return Boolean.valueOf(false);
/*     */           } 
/* 234 */         }(Executor)Util.ioPool()).thenAcceptAsync(result -> {
/*     */           if (result.booleanValue()) {
/*     */             RealmsPendingInvitesScreen.this.pendingInvitationSelectionList.removeInvitation(this);
/*     */             RealmsDataFetcher dataFetcher = RealmsPendingInvitesScreen.access$300(RealmsPendingInvitesScreen.this).realmsDataFetcher();
/*     */             if (accept)
/*     */               dataFetcher.serverListUpdateTask.reset(); 
/*     */             dataFetcher.pendingInvitesTask.reset();
/*     */           } 
/* 243 */         }RealmsPendingInvitesScreen.access$400(RealmsPendingInvitesScreen.this));
/*     */   }
/*     */ }


/* Location:              C:\Users\karol\AppData\Roaming\ModrinthApp\meta\versions\26.2-0.19.5\26.2-0.19.5.jar!\com\mojang\realmsclient\gui\screens\RealmsPendingInvitesScreen$Entry.class
 * Java compiler version: 25 (69.0)
 * JD-Core Version:       1.1.3
 */