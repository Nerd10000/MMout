/*     */ package net.minecraft.client.multiplayer;
/*     */ 
/*     */ import com.mojang.authlib.GameProfile;
/*     */ import java.util.function.Supplier;
/*     */ import net.minecraft.client.Minecraft;
/*     */ import net.minecraft.network.chat.Component;
/*     */ import net.minecraft.network.chat.RemoteChatSession;
/*     */ import net.minecraft.network.chat.SignedMessageValidator;
/*     */ import net.minecraft.world.entity.player.PlayerSkin;
/*     */ import net.minecraft.world.entity.player.ProfilePublicKey;
/*     */ import net.minecraft.world.level.GameType;
/*     */ import net.minecraft.world.scores.PlayerTeam;
/*     */ 
/*     */ public class PlayerInfo {
/*     */   private final GameProfile profile;
/*     */   
/*     */   private Supplier<PlayerSkin> skinLookup;
/*     */   
/*  20 */   private GameType gameMode = GameType.DEFAULT_MODE;
/*     */   
/*     */   private int latency;
/*     */   
/*     */   private Component tabListDisplayName;
/*     */   
/*     */   private boolean showHat = true;
/*     */   
/*     */   private RemoteChatSession chatSession;
/*     */   
/*     */   private SignedMessageValidator messageValidator;
/*     */   
/*     */   private int tabListOrder;
/*     */   
/*     */   public PlayerInfo(GameProfile profile, boolean enforcesSecureChat) {
/*  30 */     this.profile = profile;
/*  31 */     this.messageValidator = fallbackMessageValidator(enforcesSecureChat);
/*     */   }
/*     */   
/*     */   private static Supplier<PlayerSkin> createSkinLookup(GameProfile profile) {
/*  35 */     Minecraft minecraft = Minecraft.getInstance();
/*  38 */     boolean requireSecure = !minecraft.isLocalPlayer(profile.id());
/*  39 */     return minecraft.getSkinManager().createLookup(profile, requireSecure);
/*     */   }
/*     */   
/*     */   public GameProfile getProfile() {
/*  43 */     return this.profile;
/*     */   }
/*     */   
/*     */   public RemoteChatSession getChatSession() {
/*  47 */     return this.chatSession;
/*     */   }
/*     */   
/*     */   public SignedMessageValidator getMessageValidator() {
/*  51 */     return this.messageValidator;
/*     */   }
/*     */   
/*     */   public boolean hasVerifiableChat() {
/*  55 */     return (this.chatSession != null);
/*     */   }
/*     */   
/*     */   protected void setChatSession(RemoteChatSession chatSession) {
/*  59 */     this.chatSession = chatSession;
/*  60 */     this.messageValidator = chatSession.createMessageValidator(ProfilePublicKey.EXPIRY_GRACE_PERIOD);
/*     */   }
/*     */   
/*     */   protected void clearChatSession(boolean enforcesSecureChat) {
/*  64 */     this.chatSession = null;
/*  65 */     this.messageValidator = fallbackMessageValidator(enforcesSecureChat);
/*     */   }
/*     */   
/*     */   private static SignedMessageValidator fallbackMessageValidator(boolean enforcesSecureChat) {
/*  69 */     return enforcesSecureChat ? SignedMessageValidator.REJECT_ALL : SignedMessageValidator.ACCEPT_UNSIGNED;
/*     */   }
/*     */   
/*     */   public GameType getGameMode() {
/*  73 */     return this.gameMode;
/*     */   }
/*     */   
/*     */   protected void setGameMode(GameType gameMode) {
/*  77 */     this.gameMode = gameMode;
/*     */   }
/*     */   
/*     */   public int getLatency() {
/*  81 */     return this.latency;
/*     */   }
/*     */   
/*     */   protected void setLatency(int latency) {
/*  85 */     this.latency = latency;
/*     */   }
/*     */   
/*     */   public PlayerSkin getSkin() {
/*  89 */     if (this.skinLookup == null)
/*  90 */       this.skinLookup = createSkinLookup(this.profile); 
/*  92 */     return this.skinLookup.get();
/*     */   }
/*     */   
/*     */   public PlayerTeam getTeam() {
/*  96 */     return (Minecraft.getInstance()).level.getScoreboard().getPlayersTeam(getProfile().name());
/*     */   }
/*     */   
/*     */   public void setTabListDisplayName(Component tabListDisplayName) {
/* 100 */     this.tabListDisplayName = tabListDisplayName;
/*     */   }
/*     */   
/*     */   public Component getTabListDisplayName() {
/* 104 */     return this.tabListDisplayName;
/*     */   }
/*     */   
/*     */   public void setShowHat(boolean showHat) {
/* 108 */     this.showHat = showHat;
/*     */   }
/*     */   
/*     */   public boolean showHat() {
/* 112 */     return this.showHat;
/*     */   }
/*     */   
/*     */   public void setTabListOrder(int tabListOrder) {
/* 116 */     this.tabListOrder = tabListOrder;
/*     */   }
/*     */   
/*     */   public int getTabListOrder() {
/* 120 */     return this.tabListOrder;
/*     */   }
/*     */ }


/* Location:              C:\Users\karol\AppData\Roaming\ModrinthApp\meta\versions\26.2-0.19.5\26.2-0.19.5.jar!\net\minecraft\client\multiplayer\PlayerInfo.class
 * Java compiler version: 25 (69.0)
 * JD-Core Version:       1.1.3
 */