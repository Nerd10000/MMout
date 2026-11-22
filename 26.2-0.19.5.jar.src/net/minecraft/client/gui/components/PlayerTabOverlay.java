/*     */ package net.minecraft.client.gui.components;
/*     */ 
/*     */ import com.mojang.authlib.GameProfile;
/*     */ import it.unimi.dsi.fastutil.objects.Object2ObjectOpenHashMap;
/*     */ import java.util.ArrayList;
/*     */ import java.util.Comparator;
/*     */ import java.util.List;
/*     */ import java.util.Map;
/*     */ import java.util.Objects;
/*     */ import java.util.Set;
/*     */ import java.util.UUID;
/*     */ import java.util.stream.Collectors;
/*     */ import net.minecraft.ChatFormatting;
/*     */ import net.minecraft.Optionull;
/*     */ import net.minecraft.client.Minecraft;
/*     */ import net.minecraft.client.gui.GuiGraphicsExtractor;
/*     */ import net.minecraft.client.gui.Hud;
/*     */ import net.minecraft.client.multiplayer.PlayerInfo;
/*     */ import net.minecraft.client.renderer.RenderPipelines;
/*     */ import net.minecraft.client.renderer.entity.player.AvatarRenderer;
/*     */ import net.minecraft.network.chat.Component;
/*     */ import net.minecraft.network.chat.ComponentUtils;
/*     */ import net.minecraft.network.chat.FormattedText;
/*     */ import net.minecraft.network.chat.MutableComponent;
/*     */ import net.minecraft.network.chat.numbers.NumberFormat;
/*     */ import net.minecraft.network.chat.numbers.StyledFormat;
/*     */ import net.minecraft.resources.Identifier;
/*     */ import net.minecraft.util.ARGB;
/*     */ import net.minecraft.util.FormattedCharSequence;
/*     */ import net.minecraft.util.Mth;
/*     */ import net.minecraft.world.entity.player.Player;
/*     */ import net.minecraft.world.level.GameType;
/*     */ import net.minecraft.world.scores.Objective;
/*     */ import net.minecraft.world.scores.PlayerTeam;
/*     */ import net.minecraft.world.scores.ReadOnlyScoreInfo;
/*     */ import net.minecraft.world.scores.ScoreHolder;
/*     */ import net.minecraft.world.scores.Scoreboard;
/*     */ import net.minecraft.world.scores.Team;
/*     */ import net.minecraft.world.scores.criteria.ObjectiveCriteria;
/*     */ 
/*     */ public class PlayerTabOverlay {
/*  43 */   private static final Identifier PING_UNKNOWN_SPRITE = Identifier.withDefaultNamespace("icon/ping_unknown");
/*     */   
/*  44 */   private static final Identifier PING_1_SPRITE = Identifier.withDefaultNamespace("icon/ping_1");
/*     */   
/*  45 */   private static final Identifier PING_2_SPRITE = Identifier.withDefaultNamespace("icon/ping_2");
/*     */   
/*  46 */   private static final Identifier PING_3_SPRITE = Identifier.withDefaultNamespace("icon/ping_3");
/*     */   
/*  47 */   private static final Identifier PING_4_SPRITE = Identifier.withDefaultNamespace("icon/ping_4");
/*     */   
/*  48 */   private static final Identifier PING_5_SPRITE = Identifier.withDefaultNamespace("icon/ping_5");
/*     */   
/*  49 */   private static final Identifier HEART_CONTAINER_BLINKING_SPRITE = Identifier.withDefaultNamespace("hud/heart/container_blinking");
/*     */   
/*  50 */   private static final Identifier HEART_CONTAINER_SPRITE = Identifier.withDefaultNamespace("hud/heart/container");
/*     */   
/*  51 */   private static final Identifier HEART_FULL_BLINKING_SPRITE = Identifier.withDefaultNamespace("hud/heart/full_blinking");
/*     */   
/*  52 */   private static final Identifier HEART_HALF_BLINKING_SPRITE = Identifier.withDefaultNamespace("hud/heart/half_blinking");
/*     */   
/*  53 */   private static final Identifier HEART_ABSORBING_FULL_BLINKING_SPRITE = Identifier.withDefaultNamespace("hud/heart/absorbing_full_blinking");
/*     */   
/*  54 */   private static final Identifier HEART_FULL_SPRITE = Identifier.withDefaultNamespace("hud/heart/full");
/*     */   
/*  55 */   private static final Identifier HEART_ABSORBING_HALF_BLINKING_SPRITE = Identifier.withDefaultNamespace("hud/heart/absorbing_half_blinking");
/*     */   
/*  56 */   private static final Identifier HEART_HALF_SPRITE = Identifier.withDefaultNamespace("hud/heart/half");
/*     */   
/*     */   private static final Comparator<PlayerInfo> PLAYER_COMPARATOR;
/*     */   
/*     */   public static final int MAX_ROWS_PER_COL = 20;
/*     */   
/*     */   private final Minecraft minecraft;
/*     */   
/*     */   private final Hud hud;
/*     */   
/*     */   private Component footer;
/*     */   
/*     */   private Component header;
/*     */   
/*     */   private boolean visible;
/*     */   
/*     */   static {
/*  64 */     PLAYER_COMPARATOR = Comparator.comparingInt(p -> -p.getTabListOrder()).thenComparingInt(p -> (p.getGameMode() == GameType.SPECTATOR) ? 1 : 0).thenComparing(p -> (String)Optionull.mapOrDefault(p.getTeam(), PlayerTeam::getName, "")).thenComparing(p -> p.getProfile().name(), String::compareToIgnoreCase);
/*     */   }
/*     */   
/*  74 */   private final Map<UUID, HealthState> healthStates = (Map<UUID, HealthState>)new Object2ObjectOpenHashMap();
/*     */   
/*     */   public PlayerTabOverlay(Minecraft minecraft, Hud hud) {
/*  77 */     this.minecraft = minecraft;
/*  78 */     this.hud = hud;
/*     */   }
/*     */   
/*     */   public Component getNameForDisplay(PlayerInfo info) {
/*  82 */     if (info.getTabListDisplayName() != null)
/*  83 */       return decorateName(info, info.getTabListDisplayName().copy()); 
/*  85 */     return decorateName(info, PlayerTeam.formatNameForTeam((Team)info.getTeam(), (Component)Component.literal(info.getProfile().name())));
/*     */   }
/*     */   
/*     */   private Component decorateName(PlayerInfo info, MutableComponent name) {
/*  90 */     return (info.getGameMode() == GameType.SPECTATOR) ? (Component)name.withStyle(ChatFormatting.ITALIC) : (Component)name;
/*     */   }
/*     */   
/*     */   public void setVisible(boolean visible) {
/*  94 */     if (this.visible != visible) {
/*  95 */       this.healthStates.clear();
/*  96 */       this.visible = visible;
/*  97 */       if (visible) {
/*  98 */         MutableComponent mutableComponent = ComponentUtils.formatList(getPlayerInfos(), (Component)Component.literal(", "), this::getNameForDisplay);
/*  99 */         this.minecraft.getNarrator().saySystemNow((Component)Component.translatable("multiplayer.player.list.narration", new Object[] { mutableComponent }));
/*     */       } 
/*     */     } 
/*     */   }
/*     */   
/*     */   private List<PlayerInfo> getPlayerInfos() {
/* 105 */     return this.minecraft.player.connection.getListedOnlinePlayers().stream()
/* 106 */       .sorted(PLAYER_COMPARATOR)
/* 107 */       .limit(80L)
/* 108 */       .toList();
/*     */   }
/*     */   
/*     */   private static final class ScoreDisplayEntry extends Record {
/*     */     private final Component name;
/*     */     
/*     */     private final int score;
/*     */     
/*     */     private final Component formattedScore;
/*     */     
/*     */     private final int scoreWidth;
/*     */     
/*     */     private ScoreDisplayEntry(Component name, int score, Component formattedScore, int scoreWidth) {
/* 111 */       this.name = name;
/* 111 */       this.score = score;
/* 111 */       this.formattedScore = formattedScore;
/* 111 */       this.scoreWidth = scoreWidth;
/*     */     }
/*     */     
/*     */     public final String toString() {
/*     */       // Byte code:
/*     */       //   0: aload_0
/*     */       //   1: <illegal opcode> toString : (Lnet/minecraft/client/gui/components/PlayerTabOverlay$ScoreDisplayEntry;)Ljava/lang/String;
/*     */       //   6: areturn
/*     */       // Line number table:
/*     */       //   Java source line number -> byte code offset
/*     */       //   #111	-> 0
/*     */       // Local variable table:
/*     */       //   start	length	slot	name	descriptor
/*     */       //   0	7	0	this	Lnet/minecraft/client/gui/components/PlayerTabOverlay$ScoreDisplayEntry;
/*     */     }
/*     */     
/*     */     public final int hashCode() {
/*     */       // Byte code:
/*     */       //   0: aload_0
/*     */       //   1: <illegal opcode> hashCode : (Lnet/minecraft/client/gui/components/PlayerTabOverlay$ScoreDisplayEntry;)I
/*     */       //   6: ireturn
/*     */       // Line number table:
/*     */       //   Java source line number -> byte code offset
/*     */       //   #111	-> 0
/*     */       // Local variable table:
/*     */       //   start	length	slot	name	descriptor
/*     */       //   0	7	0	this	Lnet/minecraft/client/gui/components/PlayerTabOverlay$ScoreDisplayEntry;
/*     */     }
/*     */     
/*     */     public final boolean equals(Object o) {
/*     */       // Byte code:
/*     */       //   0: aload_0
/*     */       //   1: aload_1
/*     */       //   2: <illegal opcode> equals : (Lnet/minecraft/client/gui/components/PlayerTabOverlay$ScoreDisplayEntry;Ljava/lang/Object;)Z
/*     */       //   7: ireturn
/*     */       // Line number table:
/*     */       //   Java source line number -> byte code offset
/*     */       //   #111	-> 0
/*     */       // Local variable table:
/*     */       //   start	length	slot	name	descriptor
/*     */       //   0	8	0	this	Lnet/minecraft/client/gui/components/PlayerTabOverlay$ScoreDisplayEntry;
/*     */       //   0	8	1	o	Ljava/lang/Object;
/*     */     }
/*     */     
/*     */     public Component name() {
/* 111 */       return this.name;
/*     */     }
/*     */     
/*     */     public int score() {
/* 111 */       return this.score;
/*     */     }
/*     */     
/*     */     public Component formattedScore() {
/* 111 */       return this.formattedScore;
/*     */     }
/*     */     
/*     */     public int scoreWidth() {
/* 111 */       return this.scoreWidth;
/*     */     }
/*     */   }
/*     */   
/*     */   public void extractRenderState(GuiGraphicsExtractor graphics, int screenWidth, Scoreboard scoreboard, Objective displayObjective) {
/*     */     int widthForScore;
/* 114 */     List<PlayerInfo> playerInfos = getPlayerInfos();
/* 115 */     List<ScoreDisplayEntry> entriesToDisplay = new ArrayList<>(playerInfos.size());
/* 117 */     int spacerWidth = this.minecraft.font.width(" ");
/* 119 */     int maxNameWidth = 0;
/* 120 */     int maxScoreWidth = 0;
/* 122 */     for (PlayerInfo info : playerInfos) {
/*     */       MutableComponent mutableComponent;
/* 123 */       Component playerName = getNameForDisplay(info);
/* 124 */       maxNameWidth = Math.max(maxNameWidth, this.minecraft.font.width((FormattedText)playerName));
/* 126 */       int playerScore = 0;
/* 127 */       Component formattedPlayerScore = null;
/* 128 */       int playerScoreWidth = 0;
/* 130 */       if (displayObjective != null) {
/* 131 */         ScoreHolder scoreHolder = ScoreHolder.fromGameProfile(info.getProfile());
/* 132 */         ReadOnlyScoreInfo scoreInfo = scoreboard.getPlayerScoreInfo(scoreHolder, displayObjective);
/* 134 */         if (scoreInfo != null)
/* 135 */           playerScore = scoreInfo.value(); 
/* 138 */         if (displayObjective.getRenderType() != ObjectiveCriteria.RenderType.HEARTS) {
/* 139 */           if (scoreInfo != null) {
/* 140 */             NumberFormat objectiveDefaultFormat = displayObjective.numberFormatOrDefault((NumberFormat)StyledFormat.PLAYER_LIST_DEFAULT);
/* 141 */             mutableComponent = scoreInfo.formatValue(objectiveDefaultFormat);
/* 142 */             playerScoreWidth = this.minecraft.font.width((FormattedText)mutableComponent);
/*     */           } 
/* 144 */           maxScoreWidth = Math.max(maxScoreWidth, (playerScoreWidth > 0) ? (spacerWidth + playerScoreWidth) : 0);
/*     */         } 
/*     */       } 
/* 148 */       entriesToDisplay.add(new ScoreDisplayEntry(playerName, playerScore, (Component)mutableComponent, playerScoreWidth));
/*     */     } 
/* 151 */     if (!this.healthStates.isEmpty()) {
/* 152 */       Set<UUID> playerIds = (Set<UUID>)playerInfos.stream().map(player -> player.getProfile().id()).collect(Collectors.toSet());
/* 153 */       this.healthStates.keySet().removeIf(id -> !playerIds.contains(id));
/*     */     } 
/* 156 */     int slots = playerInfos.size();
/* 157 */     int rows = slots;
/* 158 */     int cols = 1;
/* 159 */     while (rows > 20) {
/* 160 */       cols++;
/* 161 */       rows = (slots + cols - 1) / cols;
/*     */     } 
/* 164 */     boolean showHead = this.minecraft.getConnection().onlineMode();
/* 167 */     if (displayObjective != null) {
/* 168 */       if (displayObjective.getRenderType() == ObjectiveCriteria.RenderType.HEARTS) {
/* 169 */         widthForScore = 90;
/*     */       } else {
/* 171 */         widthForScore = maxScoreWidth;
/*     */       } 
/*     */     } else {
/* 174 */       widthForScore = 0;
/*     */     } 
/* 177 */     int slotWidth = Math.min(cols * ((showHead ? 9 : 0) + maxNameWidth + widthForScore + 13), screenWidth - 50) / cols;
/* 179 */     int xxo = screenWidth / 2 - (slotWidth * cols + (cols - 1) * 5) / 2;
/* 180 */     int yyo = 10;
/* 182 */     int maxLineWidth = slotWidth * cols + (cols - 1) * 5;
/* 184 */     List<FormattedCharSequence> headerLines = null;
/* 185 */     if (this.header != null) {
/* 186 */       headerLines = this.minecraft.font.split((FormattedText)this.header, screenWidth - 50);
/* 187 */       for (FormattedCharSequence line : headerLines)
/* 188 */         maxLineWidth = Math.max(maxLineWidth, this.minecraft.font.width(line)); 
/*     */     } 
/* 192 */     List<FormattedCharSequence> footerLines = null;
/* 193 */     if (this.footer != null) {
/* 194 */       footerLines = this.minecraft.font.split((FormattedText)this.footer, screenWidth - 50);
/* 195 */       for (FormattedCharSequence line : footerLines)
/* 196 */         maxLineWidth = Math.max(maxLineWidth, this.minecraft.font.width(line)); 
/*     */     } 
/* 200 */     if (headerLines != null) {
/* 201 */       Objects.requireNonNull(this.minecraft.font);
/* 201 */       graphics.fill(screenWidth / 2 - maxLineWidth / 2 - 1, yyo - 1, screenWidth / 2 + maxLineWidth / 2 + 1, yyo + headerLines.size() * 9, -2147483648);
/* 202 */       for (FormattedCharSequence line : headerLines) {
/* 203 */         int lineWidth = this.minecraft.font.width(line);
/* 204 */         graphics.text(this.minecraft.font, line, screenWidth / 2 - lineWidth / 2, yyo, -1);
/* 205 */         Objects.requireNonNull(this.minecraft.font);
/* 205 */         yyo += 9;
/*     */       } 
/* 207 */       yyo++;
/*     */     } 
/* 210 */     graphics.fill(screenWidth / 2 - maxLineWidth / 2 - 1, yyo - 1, screenWidth / 2 + maxLineWidth / 2 + 1, yyo + rows * 9, -2147483648);
/* 212 */     int background = this.minecraft.options.getBackgroundColor(553648127);
/* 213 */     for (int i = 0; i < slots; i++) {
/* 214 */       int col = i / rows;
/* 215 */       int row = i % rows;
/* 216 */       int xo = xxo + col * slotWidth + col * 5;
/* 217 */       int yo = yyo + row * 9;
/* 219 */       graphics.fill(xo, yo, xo + slotWidth, yo + 8, background);
/* 221 */       if (i < playerInfos.size()) {
/* 222 */         PlayerInfo info = playerInfos.get(i);
/* 223 */         ScoreDisplayEntry displayInfo = entriesToDisplay.get(i);
/* 225 */         GameProfile profile = info.getProfile();
/* 226 */         if (showHead) {
/* 227 */           Player playerByUUID = this.minecraft.level.getPlayerByUUID(profile.id());
/* 228 */           boolean flip = (playerByUUID != null && AvatarRenderer.isPlayerUpsideDown(playerByUUID));
/* 230 */           PlayerFaceExtractor.extractRenderState(graphics, info.getSkin().body().texturePath(), xo, yo, 8, info.showHat(), flip, -1);
/* 232 */           xo += 9;
/*     */         } 
/* 235 */         graphics.text(this.minecraft.font, displayInfo.name, xo, yo, (info.getGameMode() == GameType.SPECTATOR) ? -1862270977 : -1);
/* 237 */         if (displayObjective != null && info.getGameMode() != GameType.SPECTATOR) {
/* 238 */           int left = xo + maxNameWidth + 1;
/* 239 */           int right = left + widthForScore;
/* 241 */           if (right - left > 5)
/* 242 */             extractTablistScore(displayObjective, yo, displayInfo, left, right, profile.id(), graphics); 
/*     */         } 
/* 246 */         extractPingIcon(graphics, slotWidth, xo - (showHead ? 9 : 0), yo, info);
/*     */       } 
/*     */     } 
/* 250 */     if (footerLines != null) {
/* 251 */       yyo += rows * 9 + 1;
/* 252 */       Objects.requireNonNull(this.minecraft.font);
/* 252 */       graphics.fill(screenWidth / 2 - maxLineWidth / 2 - 1, yyo - 1, screenWidth / 2 + maxLineWidth / 2 + 1, yyo + footerLines.size() * 9, -2147483648);
/* 253 */       for (FormattedCharSequence line : footerLines) {
/* 254 */         int lineWidth = this.minecraft.font.width(line);
/* 255 */         graphics.text(this.minecraft.font, line, screenWidth / 2 - lineWidth / 2, yyo, -1);
/* 256 */         Objects.requireNonNull(this.minecraft.font);
/* 256 */         yyo += 9;
/*     */       } 
/*     */     } 
/*     */   }
/*     */   
/*     */   protected void extractPingIcon(GuiGraphicsExtractor graphics, int slotWidth, int xo, int yo, PlayerInfo info) {
/*     */     Identifier sprite;
/* 263 */     if (info.getLatency() < 0) {
/* 264 */       sprite = PING_UNKNOWN_SPRITE;
/* 265 */     } else if (info.getLatency() < 150) {
/* 266 */       sprite = PING_5_SPRITE;
/* 267 */     } else if (info.getLatency() < 300) {
/* 268 */       sprite = PING_4_SPRITE;
/* 269 */     } else if (info.getLatency() < 600) {
/* 270 */       sprite = PING_3_SPRITE;
/* 271 */     } else if (info.getLatency() < 1000) {
/* 272 */       sprite = PING_2_SPRITE;
/*     */     } else {
/* 274 */       sprite = PING_1_SPRITE;
/*     */     } 
/* 277 */     graphics.blitSprite(RenderPipelines.GUI_TEXTURED, sprite, xo + slotWidth - 11, yo, 10, 8);
/*     */   }
/*     */   
/*     */   private void extractTablistScore(Objective displayObjective, int yo, ScoreDisplayEntry entry, int left, int right, UUID profileId, GuiGraphicsExtractor graphics) {
/* 281 */     if (displayObjective.getRenderType() == ObjectiveCriteria.RenderType.HEARTS) {
/* 282 */       extractTablistHearts(yo, left, right, profileId, graphics, entry.score);
/* 283 */     } else if (entry.formattedScore != null) {
/* 284 */       graphics.text(this.minecraft.font, entry.formattedScore, right - entry.scoreWidth, yo, -1);
/*     */     } 
/*     */   }
/*     */   
/*     */   private void extractTablistHearts(int yo, int left, int right, UUID profileId, GuiGraphicsExtractor graphics, int score) {
/* 289 */     HealthState health = this.healthStates.computeIfAbsent(profileId, id -> new HealthState(score));
/* 290 */     health.update(score, this.hud.getGuiTicks());
/* 292 */     int fullHearts = Mth.positiveCeilDiv(Math.max(score, health.displayedValue()), 2);
/* 293 */     int heartsToRender = Math.max(score, Math.max(health.displayedValue(), 20)) / 2;
/* 294 */     boolean blink = health.isBlinking(this.hud.getGuiTicks());
/* 296 */     if (fullHearts <= 0)
/*     */       return; 
/* 300 */     int widthPerHeart = Mth.floor(Math.min((right - left - 4) / heartsToRender, 9.0F));
/* 301 */     if (widthPerHeart <= 3) {
/*     */       MutableComponent mutableComponent2;
/* 302 */       float pct = Mth.clamp(score / 20.0F, 0.0F, 1.0F);
/* 303 */       int color = (int)((1.0F - pct) * 255.0F) << 16 | (int)(pct * 255.0F) << 8;
/* 304 */       float hearts = score / 2.0F;
/* 305 */       MutableComponent mutableComponent1 = Component.translatable("multiplayer.player.list.hp", new Object[] { Float.valueOf(hearts) });
/* 307 */       if (right - this.minecraft.font.width((FormattedText)mutableComponent1) >= left) {
/* 308 */         mutableComponent2 = mutableComponent1;
/*     */       } else {
/* 310 */         mutableComponent2 = Component.literal(Float.toString(hearts));
/*     */       } 
/* 313 */       graphics.text(this.minecraft.font, (Component)mutableComponent2, (right + left - this.minecraft.font.width((FormattedText)mutableComponent2)) / 2, yo, ARGB.opaque(color));
/*     */       return;
/*     */     } 
/* 318 */     Identifier sprite = blink ? HEART_CONTAINER_BLINKING_SPRITE : HEART_CONTAINER_SPRITE;
/*     */     int heart;
/* 319 */     for (heart = fullHearts; heart < heartsToRender; heart++)
/* 320 */       graphics.blitSprite(RenderPipelines.GUI_TEXTURED, sprite, left + heart * widthPerHeart, yo, 9, 9); 
/* 324 */     for (heart = 0; heart < fullHearts; heart++) {
/* 325 */       graphics.blitSprite(RenderPipelines.GUI_TEXTURED, sprite, left + heart * widthPerHeart, yo, 9, 9);
/* 327 */       if (blink) {
/* 328 */         if (heart * 2 + 1 < health.displayedValue())
/* 329 */           graphics.blitSprite(RenderPipelines.GUI_TEXTURED, HEART_FULL_BLINKING_SPRITE, left + heart * widthPerHeart, yo, 9, 9); 
/* 331 */         if (heart * 2 + 1 == health.displayedValue())
/* 332 */           graphics.blitSprite(RenderPipelines.GUI_TEXTURED, HEART_HALF_BLINKING_SPRITE, left + heart * widthPerHeart, yo, 9, 9); 
/*     */       } 
/* 336 */       if (heart * 2 + 1 < score)
/* 337 */         graphics.blitSprite(RenderPipelines.GUI_TEXTURED, (heart >= 10) ? HEART_ABSORBING_FULL_BLINKING_SPRITE : HEART_FULL_SPRITE, left + heart * widthPerHeart, yo, 9, 9); 
/* 339 */       if (heart * 2 + 1 == score)
/* 340 */         graphics.blitSprite(RenderPipelines.GUI_TEXTURED, (heart >= 10) ? HEART_ABSORBING_HALF_BLINKING_SPRITE : HEART_HALF_SPRITE, left + heart * widthPerHeart, yo, 9, 9); 
/*     */     } 
/*     */   }
/*     */   
/*     */   public void setFooter(Component footer) {
/* 346 */     this.footer = footer;
/*     */   }
/*     */   
/*     */   public void setHeader(Component header) {
/* 350 */     this.header = header;
/*     */   }
/*     */   
/*     */   public void reset() {
/* 354 */     this.header = null;
/* 355 */     this.footer = null;
/*     */   }
/*     */   
/*     */   private static class HealthState {
/*     */     private static final long DISPLAY_UPDATE_DELAY = 20L;
/*     */     
/*     */     private static final long DECREASE_BLINK_DURATION = 20L;
/*     */     
/*     */     private static final long INCREASE_BLINK_DURATION = 10L;
/*     */     
/*     */     private int lastValue;
/*     */     
/*     */     private int displayedValue;
/*     */     
/*     */     private long lastUpdateTick;
/*     */     
/*     */     private long blinkUntilTick;
/*     */     
/*     */     public HealthState(int value) {
/* 369 */       this.displayedValue = value;
/* 370 */       this.lastValue = value;
/*     */     }
/*     */     
/*     */     public void update(int value, long tick) {
/* 374 */       if (value != this.lastValue) {
/* 375 */         long blinkDuration = (value < this.lastValue) ? 20L : 10L;
/* 376 */         this.blinkUntilTick = tick + blinkDuration;
/* 377 */         this.lastValue = value;
/* 378 */         this.lastUpdateTick = tick;
/*     */       } 
/* 381 */       if (tick - this.lastUpdateTick > 20L)
/* 382 */         this.displayedValue = value; 
/*     */     }
/*     */     
/*     */     public int displayedValue() {
/* 387 */       return this.displayedValue;
/*     */     }
/*     */     
/*     */     public boolean isBlinking(long tick) {
/* 391 */       return (this.blinkUntilTick > tick && (this.blinkUntilTick - tick) % 6L >= 3L);
/*     */     }
/*     */   }
/*     */ }


/* Location:              C:\Users\karol\AppData\Roaming\ModrinthApp\meta\versions\26.2-0.19.5\26.2-0.19.5.jar!\net\minecraft\client\gui\components\PlayerTabOverlay.class
 * Java compiler version: 25 (69.0)
 * JD-Core Version:       1.1.3
 */