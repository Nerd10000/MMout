/*    */ package net.minecraft.client.gui.screens.friends;
/*    */ 
/*    */ import net.minecraft.client.Minecraft;
/*    */ import net.minecraft.client.gui.GuiGraphicsExtractor;
/*    */ import net.minecraft.client.gui.components.AbstractWidget;
/*    */ import net.minecraft.client.gui.components.Button;
/*    */ import net.minecraft.client.gui.components.SpriteIconButton;
/*    */ import net.minecraft.client.gui.components.WidgetSprites;
/*    */ import net.minecraft.client.gui.screens.social.PlayerSocialManager;
/*    */ import net.minecraft.network.chat.Component;
/*    */ import net.minecraft.resources.Identifier;
/*    */ 
/*    */ class IncomingEntry extends AbstractFriendsEntryContainerWidget {
/* 15 */   private static final WidgetSprites ACCEPT_SPRITE = new WidgetSprites(
/* 16 */       Identifier.withDefaultNamespace("friends/accept"), 
/* 17 */       Identifier.withDefaultNamespace("friends/accept_highlighted"));
/*    */   
/* 19 */   private static final WidgetSprites REJECT_SPRITE = new WidgetSprites(
/* 20 */       Identifier.withDefaultNamespace("friends/reject"), 
/* 21 */       Identifier.withDefaultNamespace("friends/reject_highlighted"));
/*    */   
/* 23 */   private static final Component ACCEPT_INVITE = (Component)Component.translatable("gui.friends.accept");
/*    */   
/* 24 */   private static final Component REJECT_INVITE = (Component)Component.translatable("gui.friends.decline");
/*    */   
/*    */   private final SpriteIconButton acceptButton;
/*    */   
/*    */   private final SpriteIconButton rejectButton;
/*    */   
/*    */   public IncomingEntry(Minecraft minecraft, FriendsOverlayScreen screen, PlayerSocialManager.PlayerData playerData, Runnable acceptAction, Runnable declineAction) {
/* 29 */     super(minecraft, screen, 0, 0, screen.getOverlayWidth() - 16, 28, playerData);
/* 30 */     Button.CreateNarration acceptNarration = getSpriteIconNarration(
/* 31 */         (Component)Component.translatable("gui.friends.narration.button.accept", new Object[] { playerData.name() }));
/* 33 */     Button.CreateNarration rejectNarration = getSpriteIconNarration(
/* 34 */         (Component)Component.translatable("gui.friends.narration.button.decline", new Object[] { playerData.name() }));
/* 36 */     this
/*    */       
/* 45 */       .acceptButton = SpriteIconButton.builder(ACCEPT_INVITE, paramButton -> {
/*    */           screen.startFriendAction();
/*    */           acceptAction.run();
/* 45 */         }true).size(20, 20).sprite(ACCEPT_SPRITE, 18, 18).tooltip(ACCEPT_INVITE).narration(acceptNarration).switchToLoadingAfterPress().build();
/* 46 */     addChild((AbstractWidget)this.acceptButton);
/* 48 */     this
/*    */       
/* 57 */       .rejectButton = SpriteIconButton.builder(REJECT_INVITE, paramButton -> {
/*    */           screen.startFriendAction();
/*    */           declineAction.run();
/* 57 */         }true).size(20, 20).sprite(REJECT_SPRITE, 18, 18).tooltip(REJECT_INVITE).narration(rejectNarration).switchToLoadingAfterPress().build();
/* 58 */     addChild((AbstractWidget)this.rejectButton);
/*    */   }
/*    */   
/*    */   void disable() {
/* 64 */     this.acceptButton.active = false;
/* 65 */     this.rejectButton.active = false;
/*    */   }
/*    */   
/*    */   protected Component getEntryNarration() {
/* 70 */     return (Component)Component.translatable("gui.friends.narration.entry.incoming", new Object[] { this.playerName });
/*    */   }
/*    */   
/*    */   protected void extractWidgetRenderState(GuiGraphicsExtractor graphics, int mouseX, int mouseY, float a) {
/* 75 */     super.extractWidgetRenderState(graphics, mouseX, mouseY, a);
/* 76 */     this.rejectButton.setPosition(getX() + getWidth() - this.rejectButton.getWidth(), getY() + (getHeight() - this.rejectButton.getHeight()) / 2);
/* 77 */     this.rejectButton.extractRenderState(graphics, mouseX, mouseY, a);
/* 78 */     this.acceptButton.setPosition(this.rejectButton.getX() - this.acceptButton.getWidth() - 4, getY() + (getHeight() - this.acceptButton.getHeight()) / 2);
/* 79 */     this.acceptButton.extractRenderState(graphics, mouseX, mouseY, a);
/*    */   }
/*    */ }


/* Location:              C:\Users\karol\AppData\Roaming\ModrinthApp\meta\versions\26.2-0.19.5\26.2-0.19.5.jar!\net\minecraft\client\gui\screens\friends\IncomingEntry.class
 * Java compiler version: 25 (69.0)
 * JD-Core Version:       1.1.3
 */