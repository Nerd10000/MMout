/*     */ package net.minecraft.client.gui.spectator.categories;
/*     */ 
/*     */ import java.util.ArrayList;
/*     */ import java.util.List;
/*     */ import java.util.Objects;
/*     */ import java.util.Optional;
/*     */ import java.util.function.Supplier;
/*     */ import java.util.stream.Stream;
/*     */ import net.minecraft.client.Minecraft;
/*     */ import net.minecraft.client.gui.GuiGraphicsExtractor;
/*     */ import net.minecraft.client.gui.components.PlayerFaceExtractor;
/*     */ import net.minecraft.client.gui.spectator.SpectatorMenu;
/*     */ import net.minecraft.client.gui.spectator.SpectatorMenuCategory;
/*     */ import net.minecraft.client.gui.spectator.SpectatorMenuItem;
/*     */ import net.minecraft.client.multiplayer.PlayerInfo;
/*     */ import net.minecraft.client.renderer.RenderPipelines;
/*     */ import net.minecraft.network.chat.Component;
/*     */ import net.minecraft.resources.Identifier;
/*     */ import net.minecraft.util.ARGB;
/*     */ import net.minecraft.util.RandomSource;
/*     */ import net.minecraft.world.entity.player.PlayerSkin;
/*     */ import net.minecraft.world.level.GameType;
/*     */ import net.minecraft.world.scores.PlayerTeam;
/*     */ import net.minecraft.world.scores.Scoreboard;
/*     */ import net.minecraft.world.scores.TeamColor;
/*     */ 
/*     */ public class TeleportToTeamMenuCategory implements SpectatorMenuCategory, SpectatorMenuItem {
/*  27 */   private static final Identifier TELEPORT_TO_TEAM_SPRITE = Identifier.withDefaultNamespace("spectator/teleport_to_team");
/*     */   
/*  28 */   private static final Component TELEPORT_TEXT = (Component)Component.translatable("spectatorMenu.team_teleport");
/*     */   
/*  29 */   private static final Component TELEPORT_PROMPT = (Component)Component.translatable("spectatorMenu.team_teleport.prompt");
/*     */   
/*     */   private final List<SpectatorMenuItem> items;
/*     */   
/*     */   public TeleportToTeamMenuCategory() {
/*  34 */     Minecraft minecraft = Minecraft.getInstance();
/*  35 */     this.items = createTeamEntries(minecraft, minecraft.level.getScoreboard());
/*     */   }
/*     */   
/*     */   private static List<SpectatorMenuItem> createTeamEntries(Minecraft minecraft, Scoreboard scoreboard) {
/*  39 */     return scoreboard.getPlayerTeams().stream().flatMap(team -> TeamSelectionItem.create(minecraft, team).stream()).toList();
/*     */   }
/*     */   
/*     */   public List<SpectatorMenuItem> getItems() {
/*  44 */     return this.items;
/*     */   }
/*     */   
/*     */   public Component getPrompt() {
/*  49 */     return TELEPORT_PROMPT;
/*     */   }
/*     */   
/*     */   public void selectItem(SpectatorMenu menu) {
/*  54 */     menu.selectCategory(this);
/*     */   }
/*     */   
/*     */   public Component getName() {
/*  59 */     return TELEPORT_TEXT;
/*     */   }
/*     */   
/*     */   public void extractIcon(GuiGraphicsExtractor graphics, float brightness, float alpha) {
/*  64 */     graphics.blitSprite(RenderPipelines.GUI_TEXTURED, TELEPORT_TO_TEAM_SPRITE, 0, 0, 16, 16, ARGB.colorFromFloat(alpha, brightness, brightness, brightness));
/*     */   }
/*     */   
/*     */   public boolean isEnabled() {
/*  69 */     return !this.items.isEmpty();
/*     */   }
/*     */   
/*     */   private static class TeamSelectionItem implements SpectatorMenuItem {
/*     */     private final PlayerTeam team;
/*     */     
/*     */     private final Supplier<PlayerSkin> iconSkin;
/*     */     
/*     */     private final List<PlayerInfo> players;
/*     */     
/*     */     private TeamSelectionItem(PlayerTeam team, List<PlayerInfo> players, Supplier<PlayerSkin> iconSkin) {
/*  78 */       this.team = team;
/*  79 */       this.players = players;
/*  80 */       this.iconSkin = iconSkin;
/*     */     }
/*     */     
/*     */     public static Optional<SpectatorMenuItem> create(Minecraft minecraft, PlayerTeam team) {
/*  84 */       List<PlayerInfo> players = new ArrayList<>();
/*  85 */       for (String name : team.getPlayers()) {
/*  86 */         PlayerInfo info = minecraft.getConnection().getPlayerInfo(name);
/*  89 */         if (info != null && info.getGameMode() != GameType.SPECTATOR)
/*  90 */           players.add(info); 
/*     */       } 
/*  94 */       if (players.isEmpty())
/*  95 */         return Optional.empty(); 
/*  98 */       PlayerInfo playerInfo = players.get(RandomSource.createThreadLocalInstance().nextInt(players.size()));
/*  99 */       Objects.requireNonNull(playerInfo);
/*  99 */       return Optional.of(new TeamSelectionItem(team, players, playerInfo::getSkin));
/*     */     }
/*     */     
/*     */     public void selectItem(SpectatorMenu menu) {
/* 104 */       menu.selectCategory(new TeleportToPlayerMenuCategory(this.players));
/*     */     }
/*     */     
/*     */     public Component getName() {
/* 109 */       return this.team.getDisplayName();
/*     */     }
/*     */     
/*     */     public void extractIcon(GuiGraphicsExtractor graphics, float brightness, float alpha) {
/* 114 */       Optional<TeamColor> teamColor = this.team.getColor();
/* 116 */       if (teamColor.isPresent())
/* 117 */         graphics.fill(1, 1, 15, 15, ARGB.scaleRGB(((TeamColor)teamColor.get()).rgb(), brightness)); 
/* 120 */       PlayerFaceExtractor.extractRenderState(graphics, this.iconSkin.get(), 2, 2, 12, ARGB.colorFromFloat(alpha, brightness, brightness, brightness));
/*     */     }
/*     */     
/*     */     public boolean isEnabled() {
/* 125 */       return true;
/*     */     }
/*     */   }
/*     */ }


/* Location:              C:\Users\karol\AppData\Roaming\ModrinthApp\meta\versions\26.2-0.19.5\26.2-0.19.5.jar!\net\minecraft\client\gui\spectator\categories\TeleportToTeamMenuCategory.class
 * Java compiler version: 25 (69.0)
 * JD-Core Version:       1.1.3
 */