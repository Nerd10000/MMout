/*     */ package net.minecraft.client.gui.components.toasts;
/*     */ 
/*     */ import java.util.List;
/*     */ import java.util.UUID;
/*     */ import net.minecraft.client.Minecraft;
/*     */ import net.minecraft.client.gui.Font;
/*     */ import net.minecraft.client.gui.GuiGraphicsExtractor;
/*     */ import net.minecraft.client.gui.components.PlayerFaceExtractor;
/*     */ import net.minecraft.client.gui.components.WidgetSprites;
/*     */ import net.minecraft.client.renderer.RenderPipelines;
/*     */ import net.minecraft.network.chat.Component;
/*     */ import net.minecraft.network.chat.FormattedText;
/*     */ import net.minecraft.resources.Identifier;
/*     */ import net.minecraft.util.FormattedCharSequence;
/*     */ import net.minecraft.world.item.component.ResolvableProfile;
/*     */ 
/*     */ public class FriendToast implements Toast {
/*  20 */   private static final WidgetSprites BACKGROUND_SPRITE = new WidgetSprites(Identifier.withDefaultNamespace("friends/toast_background"));
/*     */   
/*     */   private static final int FACE_SIZE = 20;
/*     */   
/*     */   private static final int TEXT_LEFT_WITH_FACE = 30;
/*     */   
/*     */   private static final int TEXT_LEFT_NO_FACE = 7;
/*     */   
/*     */   private static final int PADDING_TOP = 7;
/*     */   
/*     */   private static final int PADDING_BOTTOM = 3;
/*     */   
/*     */   private static final int LINE_SPACING = 11;
/*     */   
/*     */   private static final long DEFAULT_DISPLAY_TIME_MS = 5000L;
/*     */   
/*     */   private final ResolvableProfile skinProfile;
/*     */   
/*     */   private final List<FormattedCharSequence> messageLines;
/*     */   
/*     */   private final long displayTimeMs;
/*     */   
/*  32 */   private Toast.Visibility visibility = Toast.Visibility.SHOW;
/*     */   
/*     */   public FriendToast(Font font, ResolvableProfile skinProfile, Component message) {
/*  35 */     this(font, skinProfile, message, 5000L);
/*     */   }
/*     */   
/*     */   public FriendToast(Font font, ResolvableProfile skinProfile, Component message, long displayTimeMs) {
/*  39 */     this.skinProfile = skinProfile;
/*  40 */     int textLeft = (skinProfile != null) ? 30 : 7;
/*  41 */     this.messageLines = font.split((FormattedText)message, 160 - textLeft - 4);
/*  42 */     this.displayTimeMs = displayTimeMs;
/*     */   }
/*     */   
/*     */   public Toast.Visibility getWantedVisibility() {
/*  47 */     return this.visibility;
/*     */   }
/*     */   
/*     */   public void update(ToastManager manager, long fullyVisibleForMs) {
/*  52 */     if (fullyVisibleForMs >= this.displayTimeMs * manager.getNotificationDisplayTimeMultiplier())
/*  53 */       this.visibility = Toast.Visibility.HIDE; 
/*     */   }
/*     */   
/*     */   public int height() {
/*  59 */     return 7 + contentHeight() + 3;
/*     */   }
/*     */   
/*     */   private int contentHeight() {
/*  63 */     return Math.max(this.messageLines.size(), 2) * 11;
/*     */   }
/*     */   
/*     */   public void extractRenderState(GuiGraphicsExtractor graphics, Font font, long fullyVisibleForMs) {
/*  68 */     int textLeft, height = height();
/*  69 */     graphics.blitSprite(RenderPipelines.GUI_TEXTURED, BACKGROUND_SPRITE.get(true, false), 0, 0, width(), height);
/*  71 */     if (this.skinProfile != null) {
/*  72 */       PlayerFaceExtractor.extractRenderState(graphics, this.skinProfile, 6, 6, 20);
/*  73 */       textLeft = 30;
/*     */     } else {
/*  75 */       textLeft = 7;
/*     */     } 
/*  77 */     int totalTextHeight = this.messageLines.size() * 11;
/*  78 */     int textTop = 7 + (contentHeight() - totalTextHeight) / 2;
/*  79 */     for (int i = 0; i < this.messageLines.size(); i++)
/*  80 */       graphics.text(font, this.messageLines.get(i), textLeft, textTop + i * 11, -1, false); 
/*     */   }
/*     */   
/*     */   public void hide() {
/*  85 */     this.visibility = Toast.Visibility.HIDE;
/*     */   }
/*     */   
/*     */   public static void add(ToastManager toastManager, Font font, ResolvableProfile skinProfile, Component message) {
/*  89 */     toastManager.addToast(new FriendToast(font, skinProfile, message));
/*     */   }
/*     */   
/*     */   private static void add(Minecraft minecraft, ResolvableProfile skinProfile, Component message) {
/*  93 */     add(minecraft.gui
/*  94 */         .toastManager(), minecraft.font, skinProfile, message);
/*     */   }
/*     */   
/*     */   private static void addWithSkin(Minecraft minecraft, UUID playerId, Component message) {
/* 102 */     ResolvableProfile skinProfile = ResolvableProfile.createUnresolved(playerId);
/* 103 */     add(minecraft, skinProfile, message);
/*     */   }
/*     */   
/*     */   public static void showFriendRequestSent(Minecraft minecraft, String nickname) {
/* 107 */     add(minecraft, null, 
/* 108 */         (Component)Component.translatable("gui.friends.toast.request_sent.message", new Object[] { nickname }));
/*     */   }
/*     */   
/*     */   public static void showFriendRequestReceived(Minecraft minecraft, String nickname, UUID playerId) {
/* 113 */     addWithSkin(minecraft, playerId, 
/* 114 */         (Component)Component.translatable("gui.friends.toast.request_received.message", new Object[] { nickname }));
/*     */   }
/*     */   
/*     */   public static void showFriendRequestAccepted(Minecraft minecraft, String nickname, UUID playerId) {
/* 119 */     addWithSkin(minecraft, playerId, 
/* 120 */         (Component)Component.translatable("gui.friends.toast.request_accepted.message", new Object[] { nickname }));
/*     */   }
/*     */   
/*     */   public static void showFriendAdded(Minecraft minecraft, String nickname, UUID playerId) {
/* 125 */     addWithSkin(minecraft, playerId, 
/* 126 */         (Component)Component.translatable("gui.friends.toast.friend_added.message", new Object[] { nickname }));
/*     */   }
/*     */   
/*     */   @FunctionalInterface
/*     */   public static interface SkinToastEmitter {
/*     */     void emit(Minecraft param1Minecraft, String param1String, UUID param1UUID);
/*     */   }
/*     */ }


/* Location:              C:\Users\karol\AppData\Roaming\ModrinthApp\meta\versions\26.2-0.19.5\26.2-0.19.5.jar!\net\minecraft\client\gui\components\toasts\FriendToast.class
 * Java compiler version: 25 (69.0)
 * JD-Core Version:       1.1.3
 */