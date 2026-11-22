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
/*    */ class OutgoingEntry extends AbstractFriendsEntryContainerWidget {
/* 15 */   private static final WidgetSprites REVOKE_SPRITE = new WidgetSprites(
/* 16 */       Identifier.withDefaultNamespace("friends/cancel"));
/*    */   
/* 18 */   private static final Component REVOKE_INVITE = (Component)Component.translatable("gui.friends.cancel_request");
/*    */   
/*    */   private final SpriteIconButton revokeButton;
/*    */   
/*    */   public OutgoingEntry(Minecraft minecraft, FriendsOverlayScreen screen, PlayerSocialManager.PlayerData playerData, Runnable revokeAction) {
/* 22 */     super(minecraft, screen, 0, 0, screen.getOverlayWidth() - 16, 28, playerData);
/* 23 */     Button.CreateNarration narration = getSpriteIconNarration(
/* 24 */         (Component)Component.translatable("gui.friends.narration.button.cancel_request", new Object[] { playerData.name() }));
/* 27 */     this
/*    */       
/* 36 */       .revokeButton = SpriteIconButton.builder(REVOKE_INVITE, paramButton -> {
/*    */           screen.startFriendAction();
/*    */           revokeAction.run();
/* 36 */         }true).size(20, 20).sprite(REVOKE_SPRITE, 12, 12).tooltip(REVOKE_INVITE).narration(narration).switchToLoadingAfterPress().build();
/* 37 */     addChild((AbstractWidget)this.revokeButton);
/*    */   }
/*    */   
/*    */   void disable() {
/* 43 */     this.revokeButton.active = false;
/*    */   }
/*    */   
/*    */   protected Component getEntryNarration() {
/* 48 */     return (Component)Component.translatable("gui.friends.narration.entry.outgoing", new Object[] { this.playerName });
/*    */   }
/*    */   
/*    */   protected void extractWidgetRenderState(GuiGraphicsExtractor graphics, int mouseX, int mouseY, float a) {
/* 53 */     super.extractWidgetRenderState(graphics, mouseX, mouseY, a);
/* 54 */     this.revokeButton.setPosition(getX() + getWidth() - this.revokeButton.getWidth(), getY() + (getHeight() - this.revokeButton.getHeight()) / 2);
/* 55 */     this.revokeButton.extractRenderState(graphics, mouseX, mouseY, a);
/*    */   }
/*    */ }


/* Location:              C:\Users\karol\AppData\Roaming\ModrinthApp\meta\versions\26.2-0.19.5\26.2-0.19.5.jar!\net\minecraft\client\gui\screens\friends\OutgoingEntry.class
 * Java compiler version: 25 (69.0)
 * JD-Core Version:       1.1.3
 */