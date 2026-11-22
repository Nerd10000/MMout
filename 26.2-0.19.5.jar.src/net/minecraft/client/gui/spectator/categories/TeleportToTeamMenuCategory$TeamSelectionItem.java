/*     */ package net.minecraft.client.gui.spectator.categories;
/*     */ 
/*     */ import java.util.ArrayList;
/*     */ import java.util.List;
/*     */ import java.util.Objects;
/*     */ import java.util.Optional;
/*     */ import java.util.function.Supplier;
/*     */ import net.minecraft.client.Minecraft;
/*     */ import net.minecraft.client.gui.GuiGraphicsExtractor;
/*     */ import net.minecraft.client.gui.components.PlayerFaceExtractor;
/*     */ import net.minecraft.client.gui.spectator.SpectatorMenu;
/*     */ import net.minecraft.client.gui.spectator.SpectatorMenuItem;
/*     */ import net.minecraft.client.multiplayer.PlayerInfo;
/*     */ import net.minecraft.network.chat.Component;
/*     */ import net.minecraft.util.ARGB;
/*     */ import net.minecraft.util.RandomSource;
/*     */ import net.minecraft.world.entity.player.PlayerSkin;
/*     */ import net.minecraft.world.level.GameType;
/*     */ import net.minecraft.world.scores.PlayerTeam;
/*     */ import net.minecraft.world.scores.TeamColor;
/*     */ 
/*     */ class TeamSelectionItem implements SpectatorMenuItem {
/*     */   private final PlayerTeam team;
/*     */   
/*     */   private final Supplier<PlayerSkin> iconSkin;
/*     */   
/*     */   private final List<PlayerInfo> players;
/*     */   
/*     */   private TeamSelectionItem(PlayerTeam team, List<PlayerInfo> players, Supplier<PlayerSkin> iconSkin) {
/*  78 */     this.team = team;
/*  79 */     this.players = players;
/*  80 */     this.iconSkin = iconSkin;
/*     */   }
/*     */   
/*     */   public static Optional<SpectatorMenuItem> create(Minecraft minecraft, PlayerTeam team) {
/*  84 */     List<PlayerInfo> players = new ArrayList<>();
/*  85 */     for (String name : team.getPlayers()) {
/*  86 */       PlayerInfo info = minecraft.getConnection().getPlayerInfo(name);
/*  89 */       if (info != null && info.getGameMode() != GameType.SPECTATOR)
/*  90 */         players.add(info); 
/*     */     } 
/*  94 */     if (players.isEmpty())
/*  95 */       return Optional.empty(); 
/*  98 */     PlayerInfo playerInfo = players.get(RandomSource.createThreadLocalInstance().nextInt(players.size()));
/*  99 */     Objects.requireNonNull(playerInfo);
/*  99 */     return Optional.of(new TeamSelectionItem(team, players, playerInfo::getSkin));
/*     */   }
/*     */   
/*     */   public void selectItem(SpectatorMenu menu) {
/* 104 */     menu.selectCategory(new TeleportToPlayerMenuCategory(this.players));
/*     */   }
/*     */   
/*     */   public Component getName() {
/* 109 */     return this.team.getDisplayName();
/*     */   }
/*     */   
/*     */   public void extractIcon(GuiGraphicsExtractor graphics, float brightness, float alpha) {
/* 114 */     Optional<TeamColor> teamColor = this.team.getColor();
/* 116 */     if (teamColor.isPresent())
/* 117 */       graphics.fill(1, 1, 15, 15, ARGB.scaleRGB(((TeamColor)teamColor.get()).rgb(), brightness)); 
/* 120 */     PlayerFaceExtractor.extractRenderState(graphics, this.iconSkin.get(), 2, 2, 12, ARGB.colorFromFloat(alpha, brightness, brightness, brightness));
/*     */   }
/*     */   
/*     */   public boolean isEnabled() {
/* 125 */     return true;
/*     */   }
/*     */ }


/* Location:              C:\Users\karol\AppData\Roaming\ModrinthApp\meta\versions\26.2-0.19.5\26.2-0.19.5.jar!\net\minecraft\client\gui\spectator\categories\TeleportToTeamMenuCategory$TeamSelectionItem.class
 * Java compiler version: 25 (69.0)
 * JD-Core Version:       1.1.3
 */