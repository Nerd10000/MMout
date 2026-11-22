/*     */ package net.minecraft.client.gui.screens.social;
/*     */ 
/*     */ import com.google.common.collect.ImmutableList;
/*     */ import java.time.Duration;
/*     */ import java.util.ArrayList;
/*     */ import java.util.List;
/*     */ import java.util.Objects;
/*     */ import java.util.UUID;
/*     */ import java.util.function.Supplier;
/*     */ import net.minecraft.ChatFormatting;
/*     */ import net.minecraft.SharedConstants;
/*     */ import net.minecraft.client.Minecraft;
/*     */ import net.minecraft.client.gui.GuiGraphicsExtractor;
/*     */ import net.minecraft.client.gui.components.AbstractWidget;
/*     */ import net.minecraft.client.gui.components.Button;
/*     */ import net.minecraft.client.gui.components.ContainerObjectSelectionList;
/*     */ import net.minecraft.client.gui.components.ImageButton;
/*     */ import net.minecraft.client.gui.components.PlayerFaceExtractor;
/*     */ import net.minecraft.client.gui.components.Tooltip;
/*     */ import net.minecraft.client.gui.components.WidgetSprites;
/*     */ import net.minecraft.client.gui.components.events.GuiEventListener;
/*     */ import net.minecraft.client.gui.narration.NarratableEntry;
/*     */ import net.minecraft.client.gui.screens.Screen;
/*     */ import net.minecraft.client.gui.screens.reporting.ReportPlayerScreen;
/*     */ import net.minecraft.client.multiplayer.chat.report.ReportingContext;
/*     */ import net.minecraft.client.renderer.RenderPipelines;
/*     */ import net.minecraft.network.chat.CommonComponents;
/*     */ import net.minecraft.network.chat.Component;
/*     */ import net.minecraft.network.chat.MutableComponent;
/*     */ import net.minecraft.resources.Identifier;
/*     */ import net.minecraft.util.ARGB;
/*     */ import net.minecraft.world.entity.player.PlayerSkin;
/*     */ 
/*     */ public class PlayerEntry extends ContainerObjectSelectionList.Entry<PlayerEntry> {
/*  35 */   private static final Identifier DRAFT_REPORT_SPRITE = Identifier.withDefaultNamespace("icon/draft_report");
/*     */   
/*  36 */   private static final Duration TOOLTIP_DELAY = Duration.ofMillis(500L);
/*     */   
/*  37 */   private static final WidgetSprites REPORT_BUTTON_SPRITES = new WidgetSprites(
/*  38 */       Identifier.withDefaultNamespace("social_interactions/report_button"), 
/*  39 */       Identifier.withDefaultNamespace("social_interactions/report_button_disabled"), 
/*  40 */       Identifier.withDefaultNamespace("social_interactions/report_button_highlighted"));
/*     */   
/*  42 */   private static final WidgetSprites MUTE_BUTTON_SPRITES = new WidgetSprites(
/*  43 */       Identifier.withDefaultNamespace("social_interactions/mute_button"), 
/*  44 */       Identifier.withDefaultNamespace("social_interactions/mute_button_highlighted"));
/*     */   
/*  46 */   private static final WidgetSprites UNMUTE_BUTTON_SPRITES = new WidgetSprites(
/*  47 */       Identifier.withDefaultNamespace("social_interactions/unmute_button"), 
/*  48 */       Identifier.withDefaultNamespace("social_interactions/unmute_button_highlighted"));
/*     */   
/*     */   private final Minecraft minecraft;
/*     */   
/*     */   private final List<AbstractWidget> children;
/*     */   
/*     */   private final UUID id;
/*     */   
/*     */   private final String playerName;
/*     */   
/*     */   private final Supplier<PlayerSkin> skinGetter;
/*     */   
/*     */   private boolean isRemoved;
/*     */   
/*     */   private boolean hasRecentMessages;
/*     */   
/*     */   private final boolean reportingEnabled;
/*     */   
/*     */   private boolean hasDraftReport;
/*     */   
/*     */   private final boolean chatReportable;
/*     */   
/*     */   private Button hideButton;
/*     */   
/*     */   private Button showButton;
/*     */   
/*     */   private Button reportButton;
/*     */   
/*     */   private float tooltipHoverTime;
/*     */   
/*  69 */   private static final Component HIDDEN = (Component)Component.translatable("gui.socialInteractions.status_hidden").withStyle(ChatFormatting.ITALIC);
/*     */   
/*  70 */   private static final Component BLOCKED = (Component)Component.translatable("gui.socialInteractions.status_blocked").withStyle(ChatFormatting.ITALIC);
/*     */   
/*  71 */   private static final Component OFFLINE = (Component)Component.translatable("gui.socialInteractions.status_offline").withStyle(ChatFormatting.ITALIC);
/*     */   
/*  72 */   private static final Component HIDDEN_OFFLINE = (Component)Component.translatable("gui.socialInteractions.status_hidden_offline").withStyle(ChatFormatting.ITALIC);
/*     */   
/*  73 */   private static final Component BLOCKED_OFFLINE = (Component)Component.translatable("gui.socialInteractions.status_blocked_offline").withStyle(ChatFormatting.ITALIC);
/*     */   
/*  74 */   private static final Component REPORT_DISABLED_TOOLTIP = (Component)Component.translatable("gui.socialInteractions.tooltip.report.disabled");
/*     */   
/*  75 */   private static final Component HIDE_TEXT_TOOLTIP = (Component)Component.translatable("gui.socialInteractions.tooltip.hide");
/*     */   
/*  76 */   private static final Component SHOW_TEXT_TOOLTIP = (Component)Component.translatable("gui.socialInteractions.tooltip.show");
/*     */   
/*  77 */   private static final Component REPORT_PLAYER_TOOLTIP = (Component)Component.translatable("gui.socialInteractions.tooltip.report");
/*     */   
/*     */   private static final int SKIN_SIZE = 24;
/*     */   
/*     */   private static final int PADDING = 4;
/*     */   
/*  81 */   public static final int SKIN_SHADE = ARGB.color(190, 0, 0, 0);
/*     */   
/*     */   private static final int CHAT_TOGGLE_ICON_SIZE = 20;
/*     */   
/*  83 */   public static final int BG_FILL = ARGB.color(255, 74, 74, 74);
/*     */   
/*  84 */   public static final int BG_FILL_REMOVED = ARGB.color(255, 48, 48, 48);
/*     */   
/*  85 */   public static final int PLAYERNAME_COLOR = ARGB.color(255, 255, 255, 255);
/*     */   
/*  86 */   public static final int PLAYER_STATUS_COLOR = ARGB.color(140, 255, 255, 255);
/*     */   
/*     */   public PlayerEntry(Minecraft minecraft, SocialInteractionsScreen socialInteractionsScreen, UUID id, String playerName, Supplier<PlayerSkin> skinGetter, boolean chatReportable) {
/*  89 */     this.minecraft = minecraft;
/*  90 */     this.id = id;
/*  91 */     this.playerName = playerName;
/*  92 */     this.skinGetter = skinGetter;
/*  94 */     ReportingContext reportingContext = minecraft.getReportingContext();
/*  95 */     this.reportingEnabled = reportingContext.sender().isEnabled();
/*  96 */     this.chatReportable = chatReportable;
/*  97 */     refreshHasDraftReport(reportingContext);
/*  99 */     MutableComponent mutableComponent1 = Component.translatable("gui.socialInteractions.narration.hide", new Object[] { playerName });
/* 100 */     MutableComponent mutableComponent2 = Component.translatable("gui.socialInteractions.narration.show", new Object[] { playerName });
/* 102 */     PlayerSocialManager socialManager = minecraft.getPlayerSocialManager();
/* 104 */     boolean chatDisabledOrBlocked = (!minecraft.player.chatAbilities().canReceivePlayerMessages() || socialManager.isBlocked(id));
/* 105 */     boolean notLocalPlayer = !minecraft.player.getUUID().equals(id);
/* 106 */     if (SharedConstants.DEBUG_SOCIAL_INTERACTIONS || notLocalPlayer) {
/* 107 */       this
/*     */         
/* 109 */         .reportButton = (Button)new ImageButton(0, 0, 20, 20, REPORT_BUTTON_SPRITES, button -> reportingContext.draftReportHandled(minecraft, socialInteractionsScreen, (), false), (Component)Component.translatable("gui.socialInteractions.report")) {
/*     */           protected MutableComponent createNarrationMessage() {
/* 112 */             return PlayerEntry.this.getEntryNarationMessage(super.createNarrationMessage());
/*     */           }
/*     */         };
/* 115 */       this.reportButton.active = this.reportingEnabled;
/* 116 */       this.reportButton.setTooltip(createReportButtonTooltip());
/* 117 */       this.reportButton.setTooltipDelay(TOOLTIP_DELAY);
/* 118 */       this
/*     */         
/* 121 */         .hideButton = (Button)new ImageButton(0, 0, 20, 20, MUTE_BUTTON_SPRITES, button -> {
/*     */             socialManager.hidePlayer(id);
/*     */             onHiddenOrShown(true, (Component)Component.translatable("gui.socialInteractions.hidden_in_chat", new Object[] { playerName }));
/* 121 */           }(Component)Component.translatable("gui.socialInteractions.hide")) {
/*     */           protected MutableComponent createNarrationMessage() {
/* 124 */             return PlayerEntry.this.getEntryNarationMessage(super.createNarrationMessage());
/*     */           }
/*     */         };
/* 127 */       this.hideButton.setTooltip(Tooltip.create(HIDE_TEXT_TOOLTIP, (Component)mutableComponent1));
/* 128 */       this.hideButton.setTooltipDelay(TOOLTIP_DELAY);
/* 129 */       this
/*     */         
/* 132 */         .showButton = (Button)new ImageButton(0, 0, 20, 20, UNMUTE_BUTTON_SPRITES, button -> {
/*     */             socialManager.showPlayer(id);
/*     */             onHiddenOrShown(false, (Component)Component.translatable("gui.socialInteractions.shown_in_chat", new Object[] { playerName }));
/* 132 */           }(Component)Component.translatable("gui.socialInteractions.show")) {
/*     */           protected MutableComponent createNarrationMessage() {
/* 135 */             return PlayerEntry.this.getEntryNarationMessage(super.createNarrationMessage());
/*     */           }
/*     */         };
/* 138 */       this.showButton.setTooltip(Tooltip.create(SHOW_TEXT_TOOLTIP, (Component)mutableComponent2));
/* 139 */       this.showButton.setTooltipDelay(TOOLTIP_DELAY);
/* 141 */       this.children = new ArrayList<>();
/* 142 */       this.children.add(this.hideButton);
/* 143 */       this.children.add(this.reportButton);
/* 144 */       updateHideAndShowButton(socialManager.isHidden(this.id));
/*     */     } else {
/* 146 */       this.children = (List<AbstractWidget>)ImmutableList.of();
/*     */     } 
/*     */   }
/*     */   
/*     */   public void refreshHasDraftReport(ReportingContext reportingContext) {
/* 151 */     this.hasDraftReport = reportingContext.hasDraftReportFor(this.id);
/*     */   }
/*     */   
/*     */   private Tooltip createReportButtonTooltip() {
/* 155 */     if (!this.reportingEnabled)
/* 156 */       return Tooltip.create(REPORT_DISABLED_TOOLTIP); 
/* 158 */     return Tooltip.create(REPORT_PLAYER_TOOLTIP, 
/*     */         
/* 160 */         (Component)Component.translatable("gui.socialInteractions.narration.report", new Object[] { this.playerName }));
/*     */   }
/*     */   
/*     */   public void extractContent(GuiGraphicsExtractor graphics, int mouseX, int mouseY, boolean hovered, float a) {
/* 166 */     int textStartY, skinX = getContentX() + 4;
/* 167 */     int skinY = getContentY() + (getContentHeight() - 24) / 2;
/* 168 */     int textStartX = skinX + 24 + 4;
/* 171 */     Component status = getStatusComponent();
/* 172 */     if (status == CommonComponents.EMPTY) {
/* 173 */       graphics.fill(getContentX(), getContentY(), getContentRight(), getContentBottom(), BG_FILL);
/* 174 */       Objects.requireNonNull(this.minecraft.font);
/* 174 */       textStartY = getContentY() + (getContentHeight() - 9) / 2;
/*     */     } else {
/* 176 */       graphics.fill(getContentX(), getContentY(), getContentRight(), getContentBottom(), BG_FILL_REMOVED);
/* 177 */       Objects.requireNonNull(this.minecraft.font);
/* 177 */       Objects.requireNonNull(this.minecraft.font);
/* 177 */       textStartY = getContentY() + (getContentHeight() - 9 + 9) / 2;
/* 178 */       graphics.text(this.minecraft.font, status, textStartX, textStartY + 12, PLAYER_STATUS_COLOR);
/*     */     } 
/* 181 */     PlayerFaceExtractor.extractRenderState(graphics, this.skinGetter.get(), skinX, skinY, 24);
/* 183 */     graphics.text(this.minecraft.font, this.playerName, textStartX, textStartY, PLAYERNAME_COLOR);
/* 185 */     if (this.isRemoved)
/* 186 */       graphics.fill(skinX, skinY, skinX + 24, skinY + 24, SKIN_SHADE); 
/* 189 */     if (this.hideButton != null && this.showButton != null && this.reportButton != null) {
/* 190 */       float lastHoverTime = this.tooltipHoverTime;
/* 192 */       this.hideButton.setX(getContentX() + getContentWidth() - this.hideButton.getWidth() - 4 - 20 - 4);
/* 193 */       this.hideButton.setY(getContentY() + (getContentHeight() - this.hideButton.getHeight()) / 2);
/* 194 */       this.hideButton.extractRenderState(graphics, mouseX, mouseY, a);
/* 195 */       this.showButton.setX(getContentX() + getContentWidth() - this.showButton.getWidth() - 4 - 20 - 4);
/* 196 */       this.showButton.setY(getContentY() + (getContentHeight() - this.showButton.getHeight()) / 2);
/* 197 */       this.showButton.extractRenderState(graphics, mouseX, mouseY, a);
/* 198 */       this.reportButton.setX(getContentX() + getContentWidth() - this.showButton.getWidth() - 4);
/* 199 */       this.reportButton.setY(getContentY() + (getContentHeight() - this.showButton.getHeight()) / 2);
/* 200 */       this.reportButton.extractRenderState(graphics, mouseX, mouseY, a);
/* 202 */       if (lastHoverTime == this.tooltipHoverTime)
/* 203 */         this.tooltipHoverTime = 0.0F; 
/*     */     } 
/* 207 */     if (this.hasDraftReport && this.reportButton != null)
/* 208 */       graphics.blitSprite(RenderPipelines.GUI_TEXTURED, DRAFT_REPORT_SPRITE, this.reportButton.getX() + 5, this.reportButton.getY() + 1, 15, 15); 
/*     */   }
/*     */   
/*     */   public List<? extends GuiEventListener> children() {
/* 214 */     return (List)this.children;
/*     */   }
/*     */   
/*     */   public List<? extends NarratableEntry> narratables() {
/* 219 */     return (List)this.children;
/*     */   }
/*     */   
/*     */   public String getPlayerName() {
/* 223 */     return this.playerName;
/*     */   }
/*     */   
/*     */   public UUID getPlayerId() {
/* 227 */     return this.id;
/*     */   }
/*     */   
/*     */   public Supplier<PlayerSkin> getSkinGetter() {
/* 231 */     return this.skinGetter;
/*     */   }
/*     */   
/*     */   public void setRemoved(boolean isRemoved) {
/* 235 */     this.isRemoved = isRemoved;
/*     */   }
/*     */   
/*     */   public boolean isRemoved() {
/* 239 */     return this.isRemoved;
/*     */   }
/*     */   
/*     */   public void setHasRecentMessages(boolean hasRecentMessages) {
/* 243 */     this.hasRecentMessages = hasRecentMessages;
/*     */   }
/*     */   
/*     */   public boolean hasRecentMessages() {
/* 247 */     return this.hasRecentMessages;
/*     */   }
/*     */   
/*     */   public boolean isChatReportable() {
/* 251 */     return this.chatReportable;
/*     */   }
/*     */   
/*     */   private void onHiddenOrShown(boolean isHidden, Component message) {
/* 255 */     updateHideAndShowButton(isHidden);
/* 256 */     this.minecraft.gui.hud.getChat().addClientSystemMessage(message);
/* 257 */     this.minecraft.getNarrator().saySystemNow(message);
/*     */   }
/*     */   
/*     */   private void updateHideAndShowButton(boolean isHidden) {
/* 261 */     this.showButton.visible = isHidden;
/* 262 */     this.hideButton.visible = !isHidden;
/* 263 */     this.children.set(0, isHidden ? this.showButton : this.hideButton);
/*     */   }
/*     */   
/*     */   private MutableComponent getEntryNarationMessage(MutableComponent buttonNarrationMessage) {
/* 267 */     Component status = getStatusComponent();
/* 268 */     if (status == CommonComponents.EMPTY)
/* 269 */       return Component.literal(this.playerName).append(", ").append((Component)buttonNarrationMessage); 
/* 271 */     return Component.literal(this.playerName).append(", ").append(status).append(", ").append((Component)buttonNarrationMessage);
/*     */   }
/*     */   
/*     */   private Component getStatusComponent() {
/* 276 */     boolean isHidden = this.minecraft.getPlayerSocialManager().isHidden(this.id);
/* 277 */     boolean isBlocked = this.minecraft.getPlayerSocialManager().isBlocked(this.id);
/* 279 */     if (isBlocked && this.isRemoved)
/* 280 */       return BLOCKED_OFFLINE; 
/* 281 */     if (isHidden && this.isRemoved)
/* 282 */       return HIDDEN_OFFLINE; 
/* 283 */     if (isBlocked)
/* 284 */       return BLOCKED; 
/* 285 */     if (isHidden)
/* 286 */       return HIDDEN; 
/* 287 */     if (this.isRemoved)
/* 288 */       return OFFLINE; 
/* 290 */     return CommonComponents.EMPTY;
/*     */   }
/*     */ }


/* Location:              C:\Users\karol\AppData\Roaming\ModrinthApp\meta\versions\26.2-0.19.5\26.2-0.19.5.jar!\net\minecraft\client\gui\screens\social\PlayerEntry.class
 * Java compiler version: 25 (69.0)
 * JD-Core Version:       1.1.3
 */