/*    */ package net.minecraft.client.gui.components;
/*    */ 
/*    */ import java.util.function.Supplier;
/*    */ import net.minecraft.client.Minecraft;
/*    */ import net.minecraft.client.gui.GuiGraphicsExtractor;
/*    */ import net.minecraft.client.gui.screens.social.PlayerSocialManager;
/*    */ import net.minecraft.client.renderer.RenderPipelines;
/*    */ import net.minecraft.network.chat.Component;
/*    */ import net.minecraft.network.chat.MutableComponent;
/*    */ import net.minecraft.resources.Identifier;
/*    */ 
/*    */ public class FriendsButton extends SpriteIconButton.CenteredIcon {
/* 11 */   private static final Component TOOLTIP = (Component)Component.translatable("gui.friends.open.tooltip");
/*    */   
/* 12 */   private static final Component MESSAGE = (Component)Component.translatable("gui.friends.open");
/*    */   
/*    */   private static final Button.CreateNarration NARRATION = paramSupplier -> Component.translatable("gui.friends.open.narration");
/*    */   
/* 15 */   private static final Identifier[] NOTIFICATION_ICONS = new Identifier[] { Identifier.withDefaultNamespace("notification/1"), 
/* 17 */       Identifier.withDefaultNamespace("notification/2"), 
/* 18 */       Identifier.withDefaultNamespace("notification/3"), 
/* 19 */       Identifier.withDefaultNamespace("notification/4"), 
/* 20 */       Identifier.withDefaultNamespace("notification/5"), 
/* 21 */       Identifier.withDefaultNamespace("notification/more") };
/*    */   
/*    */   private static final int SPRITE_SIZE = 15;
/*    */   
/*    */   private int incomingRequestCount;
/*    */   
/*    */   public FriendsButton(int width, Button.OnPress onPress, boolean friendsAvailable) {
/* 29 */     super(width, 20, MESSAGE, 15, 15, 0, 0, new WidgetSprites(
/* 30 */           Identifier.withDefaultNamespace("friends/friends")), onPress, 
/* 31 */         friendsAvailable ? TOOLTIP : null, NARRATION, false);
/* 32 */     this.active = friendsAvailable;
/* 33 */     refreshIncomingRequestCount();
/*    */   }
/*    */   
/*    */   public void refreshIncomingRequestCount() {
/* 37 */     Minecraft minecraft = Minecraft.getInstance();
/* 38 */     PlayerSocialManager playerSocialManager = minecraft.getPlayerSocialManager();
/* 39 */     if (!playerSocialManager.isFriendListEnabled()) {
/* 40 */       this.incomingRequestCount = 0;
/*    */       return;
/*    */     } 
/* 43 */     this.incomingRequestCount = playerSocialManager.getIncomingRequests().size();
/*    */   }
/*    */   
/*    */   public void extractContents(GuiGraphicsExtractor graphics, int mouseX, int mouseY, float a) {
/* 48 */     super.extractContents(graphics, mouseX, mouseY, a);
/* 49 */     if (isActive() && this.incomingRequestCount > 0) {
/* 50 */       int iconIndex = Math.min(this.incomingRequestCount, 6) - 1;
/* 51 */       graphics.blitSprite(RenderPipelines.GUI_TEXTURED, NOTIFICATION_ICONS[iconIndex], 
/* 52 */           getX() + getWidth() - 5, getY() - 3, 8, 8, this.alpha);
/*    */     } 
/*    */   }
/*    */ }


/* Location:              C:\Users\karol\AppData\Roaming\ModrinthApp\meta\versions\26.2-0.19.5\26.2-0.19.5.jar!\net\minecraft\client\gui\components\FriendsButton.class
 * Java compiler version: 25 (69.0)
 * JD-Core Version:       1.1.3
 */