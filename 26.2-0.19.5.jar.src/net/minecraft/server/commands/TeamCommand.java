/*     */ package net.minecraft.server.commands;
/*     */ 
/*     */ import com.google.common.collect.Lists;
/*     */ import com.mojang.brigadier.CommandDispatcher;
/*     */ import com.mojang.brigadier.Message;
/*     */ import com.mojang.brigadier.arguments.ArgumentType;
/*     */ import com.mojang.brigadier.arguments.BoolArgumentType;
/*     */ import com.mojang.brigadier.arguments.StringArgumentType;
/*     */ import com.mojang.brigadier.builder.LiteralArgumentBuilder;
/*     */ import com.mojang.brigadier.builder.RequiredArgumentBuilder;
/*     */ import com.mojang.brigadier.context.CommandContext;
/*     */ import com.mojang.brigadier.exceptions.CommandSyntaxException;
/*     */ import com.mojang.brigadier.exceptions.SimpleCommandExceptionType;
/*     */ import java.util.Collection;
/*     */ import java.util.Collections;
/*     */ import java.util.Optional;
/*     */ import java.util.function.Predicate;
/*     */ import net.minecraft.commands.CommandBuildContext;
/*     */ import net.minecraft.commands.CommandSourceStack;
/*     */ import net.minecraft.commands.Commands;
/*     */ import net.minecraft.commands.arguments.ComponentArgument;
/*     */ import net.minecraft.commands.arguments.ScoreHolderArgument;
/*     */ import net.minecraft.commands.arguments.TeamArgument;
/*     */ import net.minecraft.commands.arguments.TeamColorArgument;
/*     */ import net.minecraft.network.chat.Component;
/*     */ import net.minecraft.network.chat.ComponentUtils;
/*     */ import net.minecraft.server.ServerScoreboard;
/*     */ import net.minecraft.world.scores.PlayerTeam;
/*     */ import net.minecraft.world.scores.ScoreHolder;
/*     */ import net.minecraft.world.scores.Team;
/*     */ import net.minecraft.world.scores.TeamColor;
/*     */ 
/*     */ public class TeamCommand {
/*  39 */   private static final SimpleCommandExceptionType ERROR_TEAM_ALREADY_EXISTS = new SimpleCommandExceptionType((Message)Component.translatable("commands.team.add.duplicate"));
/*     */   
/*  40 */   private static final SimpleCommandExceptionType ERROR_TEAM_ALREADY_EMPTY = new SimpleCommandExceptionType((Message)Component.translatable("commands.team.empty.unchanged"));
/*     */   
/*  41 */   private static final SimpleCommandExceptionType ERROR_TEAM_ALREADY_NAME = new SimpleCommandExceptionType((Message)Component.translatable("commands.team.option.name.unchanged"));
/*     */   
/*  42 */   private static final SimpleCommandExceptionType ERROR_TEAM_ALREADY_COLOR = new SimpleCommandExceptionType((Message)Component.translatable("commands.team.option.color.unchanged"));
/*     */   
/*  43 */   private static final SimpleCommandExceptionType ERROR_TEAM_ALREADY_FRIENDLYFIRE_ENABLED = new SimpleCommandExceptionType((Message)Component.translatable("commands.team.option.friendlyfire.alreadyEnabled"));
/*     */   
/*  44 */   private static final SimpleCommandExceptionType ERROR_TEAM_ALREADY_FRIENDLYFIRE_DISABLED = new SimpleCommandExceptionType((Message)Component.translatable("commands.team.option.friendlyfire.alreadyDisabled"));
/*     */   
/*  45 */   private static final SimpleCommandExceptionType ERROR_TEAM_ALREADY_FRIENDLYINVISIBLES_ENABLED = new SimpleCommandExceptionType((Message)Component.translatable("commands.team.option.seeFriendlyInvisibles.alreadyEnabled"));
/*     */   
/*  46 */   private static final SimpleCommandExceptionType ERROR_TEAM_ALREADY_FRIENDLYINVISIBLES_DISABLED = new SimpleCommandExceptionType((Message)Component.translatable("commands.team.option.seeFriendlyInvisibles.alreadyDisabled"));
/*     */   
/*  47 */   private static final SimpleCommandExceptionType ERROR_TEAM_NAMETAG_VISIBLITY_UNCHANGED = new SimpleCommandExceptionType((Message)Component.translatable("commands.team.option.nametagVisibility.unchanged"));
/*     */   
/*  48 */   private static final SimpleCommandExceptionType ERROR_TEAM_DEATH_MESSAGE_VISIBLITY_UNCHANGED = new SimpleCommandExceptionType((Message)Component.translatable("commands.team.option.deathMessageVisibility.unchanged"));
/*     */   
/*  49 */   private static final SimpleCommandExceptionType ERROR_TEAM_COLLISION_UNCHANGED = new SimpleCommandExceptionType((Message)Component.translatable("commands.team.option.collisionRule.unchanged"));
/*     */   
/*     */   public static void register(CommandDispatcher<CommandSourceStack> dispatcher, CommandBuildContext context) {
/*  52 */     dispatcher.register(
/*  53 */         (LiteralArgumentBuilder)((LiteralArgumentBuilder)((LiteralArgumentBuilder)((LiteralArgumentBuilder)((LiteralArgumentBuilder)((LiteralArgumentBuilder)((LiteralArgumentBuilder)((LiteralArgumentBuilder)Commands.literal("team")
/*  54 */         .requires((Predicate)Commands.hasPermission(Commands.LEVEL_GAMEMASTERS)))
/*  55 */         .then((
/*  56 */           (LiteralArgumentBuilder)Commands.literal("list")
/*  57 */           .executes(c -> listTeams((CommandSourceStack)c.getSource())))
/*  58 */           .then(
/*  59 */             Commands.argument("team", (ArgumentType)TeamArgument.team())
/*  60 */             .executes(c -> listMembers((CommandSourceStack)c.getSource(), TeamArgument.getTeam(c, "team"))))))
/*     */         
/*  63 */         .then(
/*  64 */           Commands.literal("add")
/*  65 */           .then((
/*  66 */             (RequiredArgumentBuilder)Commands.argument("team", (ArgumentType)StringArgumentType.word())
/*  67 */             .executes(c -> createTeam((CommandSourceStack)c.getSource(), StringArgumentType.getString(c, "team"))))
/*  68 */             .then(
/*  69 */               Commands.argument("displayName", (ArgumentType)ComponentArgument.textComponent(context))
/*  70 */               .executes(c -> createTeam((CommandSourceStack)c.getSource(), StringArgumentType.getString(c, "team"), ComponentArgument.getResolvedComponent(c, "displayName")))))))
/*     */         
/*  74 */         .then(
/*  75 */           Commands.literal("remove")
/*  76 */           .then(
/*  77 */             Commands.argument("team", (ArgumentType)TeamArgument.team())
/*  78 */             .executes(c -> deleteTeam((CommandSourceStack)c.getSource(), TeamArgument.getTeam(c, "team"))))))
/*     */         
/*  81 */         .then(
/*  82 */           Commands.literal("empty")
/*  83 */           .then(
/*  84 */             Commands.argument("team", (ArgumentType)TeamArgument.team())
/*  85 */             .executes(c -> emptyTeam((CommandSourceStack)c.getSource(), TeamArgument.getTeam(c, "team"))))))
/*     */         
/*  88 */         .then(
/*  89 */           Commands.literal("join")
/*  90 */           .then((
/*  91 */             (RequiredArgumentBuilder)Commands.argument("team", (ArgumentType)TeamArgument.team())
/*  92 */             .executes(c -> joinTeam((CommandSourceStack)c.getSource(), TeamArgument.getTeam(c, "team"), (Collection)Collections.singleton(((CommandSourceStack)c.getSource()).getEntityOrException()))))
/*  93 */             .then(
/*  94 */               Commands.argument("members", (ArgumentType)ScoreHolderArgument.scoreHolders())
/*  95 */               .suggests(ScoreHolderArgument.SUGGEST_SCORE_HOLDERS)
/*  96 */               .executes(c -> joinTeam((CommandSourceStack)c.getSource(), TeamArgument.getTeam(c, "team"), ScoreHolderArgument.getNamesWithDefaultWildcard(c, "members")))))))
/*     */         
/* 100 */         .then(
/* 101 */           Commands.literal("leave")
/* 102 */           .then(
/* 103 */             Commands.argument("members", (ArgumentType)ScoreHolderArgument.scoreHolders())
/* 104 */             .suggests(ScoreHolderArgument.SUGGEST_SCORE_HOLDERS)
/* 105 */             .executes(c -> leaveTeam((CommandSourceStack)c.getSource(), ScoreHolderArgument.getNamesWithDefaultWildcard(c, "members"))))))
/*     */         
/* 108 */         .then(
/* 109 */           Commands.literal("modify")
/* 110 */           .then((
/* 111 */             (RequiredArgumentBuilder)((RequiredArgumentBuilder)((RequiredArgumentBuilder)((RequiredArgumentBuilder)((RequiredArgumentBuilder)((RequiredArgumentBuilder)((RequiredArgumentBuilder)((RequiredArgumentBuilder)Commands.argument("team", (ArgumentType)TeamArgument.team())
/* 112 */             .then(
/* 113 */               Commands.literal("displayName")
/* 114 */               .then(
/* 115 */                 Commands.argument("displayName", (ArgumentType)ComponentArgument.textComponent(context))
/* 116 */                 .executes(c -> setDisplayName((CommandSourceStack)c.getSource(), TeamArgument.getTeam(c, "team"), ComponentArgument.getResolvedComponent(c, "displayName"))))))
/*     */             
/* 119 */             .then((
/* 120 */               (LiteralArgumentBuilder)Commands.literal("color")
/* 121 */               .then(
/* 122 */                 Commands.literal("reset")
/* 123 */                 .executes(c -> clearColor((CommandSourceStack)c.getSource(), TeamArgument.getTeam(c, "team")))))
/*     */               
/* 125 */               .then(
/* 126 */                 Commands.argument("value", (ArgumentType)TeamColorArgument.teamColor())
/* 127 */                 .executes(c -> setColor((CommandSourceStack)c.getSource(), TeamArgument.getTeam(c, "team"), TeamColorArgument.getTeamColor(c, "value"))))))
/*     */             
/* 130 */             .then(
/* 131 */               Commands.literal("friendlyFire")
/* 132 */               .then(
/* 133 */                 Commands.argument("allowed", (ArgumentType)BoolArgumentType.bool())
/* 134 */                 .executes(c -> setFriendlyFire((CommandSourceStack)c.getSource(), TeamArgument.getTeam(c, "team"), BoolArgumentType.getBool(c, "allowed"))))))
/*     */             
/* 137 */             .then(
/* 138 */               Commands.literal("seeFriendlyInvisibles")
/* 139 */               .then(
/* 140 */                 Commands.argument("allowed", (ArgumentType)BoolArgumentType.bool())
/* 141 */                 .executes(c -> setFriendlySight((CommandSourceStack)c.getSource(), TeamArgument.getTeam(c, "team"), BoolArgumentType.getBool(c, "allowed"))))))
/*     */             
/* 144 */             .then((
/* 145 */               (LiteralArgumentBuilder)((LiteralArgumentBuilder)((LiteralArgumentBuilder)Commands.literal("nametagVisibility")
/* 146 */               .then(Commands.literal("never").executes(c -> setNameTagVisibility((CommandSourceStack)c.getSource(), TeamArgument.getTeam(c, "team"), Team.Visibility.NEVER))))
/* 147 */               .then(Commands.literal("hideForOtherTeams").executes(c -> setNameTagVisibility((CommandSourceStack)c.getSource(), TeamArgument.getTeam(c, "team"), Team.Visibility.HIDE_FOR_OTHER_TEAMS))))
/* 148 */               .then(Commands.literal("hideForOwnTeam").executes(c -> setNameTagVisibility((CommandSourceStack)c.getSource(), TeamArgument.getTeam(c, "team"), Team.Visibility.HIDE_FOR_OWN_TEAM))))
/* 149 */               .then(Commands.literal("always").executes(c -> setNameTagVisibility((CommandSourceStack)c.getSource(), TeamArgument.getTeam(c, "team"), Team.Visibility.ALWAYS)))))
/*     */             
/* 151 */             .then((
/* 152 */               (LiteralArgumentBuilder)((LiteralArgumentBuilder)((LiteralArgumentBuilder)Commands.literal("deathMessageVisibility")
/* 153 */               .then(Commands.literal("never").executes(c -> setDeathMessageVisibility((CommandSourceStack)c.getSource(), TeamArgument.getTeam(c, "team"), Team.Visibility.NEVER))))
/* 154 */               .then(Commands.literal("hideForOtherTeams").executes(c -> setDeathMessageVisibility((CommandSourceStack)c.getSource(), TeamArgument.getTeam(c, "team"), Team.Visibility.HIDE_FOR_OTHER_TEAMS))))
/* 155 */               .then(Commands.literal("hideForOwnTeam").executes(c -> setDeathMessageVisibility((CommandSourceStack)c.getSource(), TeamArgument.getTeam(c, "team"), Team.Visibility.HIDE_FOR_OWN_TEAM))))
/* 156 */               .then(Commands.literal("always").executes(c -> setDeathMessageVisibility((CommandSourceStack)c.getSource(), TeamArgument.getTeam(c, "team"), Team.Visibility.ALWAYS)))))
/*     */             
/* 158 */             .then((
/* 159 */               (LiteralArgumentBuilder)((LiteralArgumentBuilder)((LiteralArgumentBuilder)Commands.literal("collisionRule")
/* 160 */               .then(Commands.literal("never").executes(c -> setCollision((CommandSourceStack)c.getSource(), TeamArgument.getTeam(c, "team"), Team.CollisionRule.NEVER))))
/* 161 */               .then(Commands.literal("pushOwnTeam").executes(c -> setCollision((CommandSourceStack)c.getSource(), TeamArgument.getTeam(c, "team"), Team.CollisionRule.PUSH_OWN_TEAM))))
/* 162 */               .then(Commands.literal("pushOtherTeams").executes(c -> setCollision((CommandSourceStack)c.getSource(), TeamArgument.getTeam(c, "team"), Team.CollisionRule.PUSH_OTHER_TEAMS))))
/* 163 */               .then(Commands.literal("always").executes(c -> setCollision((CommandSourceStack)c.getSource(), TeamArgument.getTeam(c, "team"), Team.CollisionRule.ALWAYS)))))
/*     */             
/* 165 */             .then(
/* 166 */               Commands.literal("prefix")
/* 167 */               .then(
/* 168 */                 Commands.argument("prefix", (ArgumentType)ComponentArgument.textComponent(context))
/* 169 */                 .executes(c -> setPrefix((CommandSourceStack)c.getSource(), TeamArgument.getTeam(c, "team"), ComponentArgument.getResolvedComponent(c, "prefix"))))))
/*     */             
/* 172 */             .then(
/* 173 */               Commands.literal("suffix")
/* 174 */               .then(
/* 175 */                 Commands.argument("suffix", (ArgumentType)ComponentArgument.textComponent(context))
/* 176 */                 .executes(c -> setSuffix((CommandSourceStack)c.getSource(), TeamArgument.getTeam(c, "team"), ComponentArgument.getResolvedComponent(c, "suffix"))))))));
/*     */   }
/*     */   
/*     */   private static Component getFirstMemberName(Collection<ScoreHolder> members) {
/* 185 */     return ((ScoreHolder)members.iterator().next()).getFeedbackDisplayName();
/*     */   }
/*     */   
/*     */   private static int leaveTeam(CommandSourceStack source, Collection<ScoreHolder> members) {
/* 189 */     ServerScoreboard serverScoreboard = source.getServer().getScoreboard();
/* 191 */     for (ScoreHolder member : members)
/* 192 */       serverScoreboard.removePlayerFromTeam(member.getScoreboardName()); 
/* 195 */     if (members.size() == 1) {
/* 196 */       source.sendSuccess(() -> Component.translatable("commands.team.leave.success.single", new Object[] { getFirstMemberName(members) }), true);
/*     */     } else {
/* 198 */       source.sendSuccess(() -> Component.translatable("commands.team.leave.success.multiple", new Object[] { Integer.valueOf(members.size()) }), true);
/*     */     } 
/* 201 */     return members.size();
/*     */   }
/*     */   
/*     */   private static int joinTeam(CommandSourceStack source, PlayerTeam team, Collection<ScoreHolder> members) {
/* 205 */     ServerScoreboard serverScoreboard = source.getServer().getScoreboard();
/* 207 */     for (ScoreHolder member : members)
/* 208 */       serverScoreboard.addPlayerToTeam(member.getScoreboardName(), team); 
/* 211 */     if (members.size() == 1) {
/* 212 */       source.sendSuccess(() -> Component.translatable("commands.team.join.success.single", new Object[] { getFirstMemberName(members), team.getFormattedDisplayName() }), true);
/*     */     } else {
/* 214 */       source.sendSuccess(() -> Component.translatable("commands.team.join.success.multiple", new Object[] { Integer.valueOf(members.size()), team.getFormattedDisplayName() }), true);
/*     */     } 
/* 217 */     return members.size();
/*     */   }
/*     */   
/*     */   private static int setNameTagVisibility(CommandSourceStack source, PlayerTeam team, Team.Visibility visibility) throws CommandSyntaxException {
/* 221 */     if (team.getNameTagVisibility() == visibility)
/* 222 */       throw ERROR_TEAM_NAMETAG_VISIBLITY_UNCHANGED.create(); 
/* 224 */     team.setNameTagVisibility(visibility);
/* 225 */     source.sendSuccess(() -> Component.translatable("commands.team.option.nametagVisibility.success", new Object[] { team.getFormattedDisplayName(), visibility.getDisplayName() }), true);
/* 226 */     return 0;
/*     */   }
/*     */   
/*     */   private static int setDeathMessageVisibility(CommandSourceStack source, PlayerTeam team, Team.Visibility visibility) throws CommandSyntaxException {
/* 230 */     if (team.getDeathMessageVisibility() == visibility)
/* 231 */       throw ERROR_TEAM_DEATH_MESSAGE_VISIBLITY_UNCHANGED.create(); 
/* 233 */     team.setDeathMessageVisibility(visibility);
/* 234 */     source.sendSuccess(() -> Component.translatable("commands.team.option.deathMessageVisibility.success", new Object[] { team.getFormattedDisplayName(), visibility.getDisplayName() }), true);
/* 235 */     return 0;
/*     */   }
/*     */   
/*     */   private static int setCollision(CommandSourceStack source, PlayerTeam team, Team.CollisionRule collision) throws CommandSyntaxException {
/* 239 */     if (team.getCollisionRule() == collision)
/* 240 */       throw ERROR_TEAM_COLLISION_UNCHANGED.create(); 
/* 242 */     team.setCollisionRule(collision);
/* 243 */     source.sendSuccess(() -> Component.translatable("commands.team.option.collisionRule.success", new Object[] { team.getFormattedDisplayName(), collision.getDisplayName() }), true);
/* 244 */     return 0;
/*     */   }
/*     */   
/*     */   private static int setFriendlySight(CommandSourceStack source, PlayerTeam team, boolean allowed) throws CommandSyntaxException {
/* 248 */     if (team.canSeeFriendlyInvisibles() == allowed) {
/* 249 */       if (allowed)
/* 250 */         throw ERROR_TEAM_ALREADY_FRIENDLYINVISIBLES_ENABLED.create(); 
/* 252 */       throw ERROR_TEAM_ALREADY_FRIENDLYINVISIBLES_DISABLED.create();
/*     */     } 
/* 256 */     team.setSeeFriendlyInvisibles(allowed);
/* 257 */     source.sendSuccess(() -> Component.translatable("commands.team.option.seeFriendlyInvisibles." + (allowed ? "enabled" : "disabled"), new Object[] { team.getFormattedDisplayName() }), true);
/* 259 */     return 0;
/*     */   }
/*     */   
/*     */   private static int setFriendlyFire(CommandSourceStack source, PlayerTeam team, boolean allowed) throws CommandSyntaxException {
/* 263 */     if (team.isAllowFriendlyFire() == allowed) {
/* 264 */       if (allowed)
/* 265 */         throw ERROR_TEAM_ALREADY_FRIENDLYFIRE_ENABLED.create(); 
/* 267 */       throw ERROR_TEAM_ALREADY_FRIENDLYFIRE_DISABLED.create();
/*     */     } 
/* 271 */     team.setAllowFriendlyFire(allowed);
/* 272 */     source.sendSuccess(() -> Component.translatable("commands.team.option.friendlyfire." + (allowed ? "enabled" : "disabled"), new Object[] { team.getFormattedDisplayName() }), true);
/* 274 */     return 0;
/*     */   }
/*     */   
/*     */   private static int setDisplayName(CommandSourceStack source, PlayerTeam team, Component displayName) throws CommandSyntaxException {
/* 278 */     if (team.getDisplayName().equals(displayName))
/* 279 */       throw ERROR_TEAM_ALREADY_NAME.create(); 
/* 282 */     team.setDisplayName(displayName);
/* 283 */     source.sendSuccess(() -> Component.translatable("commands.team.option.name.success", new Object[] { team.getFormattedDisplayName() }), true);
/* 284 */     return 0;
/*     */   }
/*     */   
/*     */   private static int setColor(CommandSourceStack source, PlayerTeam team, TeamColor color) throws CommandSyntaxException {
/* 288 */     Optional<TeamColor> currentColor = team.getColor();
/* 289 */     if (currentColor.isPresent() && ((TeamColor)currentColor.get()).equals(color))
/* 290 */       throw ERROR_TEAM_ALREADY_COLOR.create(); 
/* 292 */     team.setColor(Optional.of(color));
/* 293 */     source.sendSuccess(() -> Component.translatable("commands.team.option.color.success", new Object[] { team.getFormattedDisplayName(), color.getSerializedName() }), true);
/* 294 */     return 0;
/*     */   }
/*     */   
/*     */   private static int clearColor(CommandSourceStack source, PlayerTeam team) throws CommandSyntaxException {
/* 298 */     Optional<TeamColor> currentColor = team.getColor();
/* 299 */     if (currentColor.isEmpty())
/* 300 */       throw ERROR_TEAM_ALREADY_COLOR.create(); 
/* 302 */     team.setColor(Optional.empty());
/* 303 */     source.sendSuccess(() -> Component.translatable("commands.team.option.color.clear.success", new Object[] { team.getFormattedDisplayName() }), true);
/* 304 */     return 0;
/*     */   }
/*     */   
/*     */   private static int emptyTeam(CommandSourceStack source, PlayerTeam team) throws CommandSyntaxException {
/* 308 */     ServerScoreboard serverScoreboard = source.getServer().getScoreboard();
/* 309 */     Collection<String> members = Lists.newArrayList(team.getPlayers());
/* 311 */     if (members.isEmpty())
/* 312 */       throw ERROR_TEAM_ALREADY_EMPTY.create(); 
/* 315 */     for (String member : members)
/* 316 */       serverScoreboard.removePlayerFromTeam(member, team); 
/* 319 */     source.sendSuccess(() -> Component.translatable("commands.team.empty.success", new Object[] { Integer.valueOf(members.size()), team.getFormattedDisplayName() }), true);
/* 321 */     return members.size();
/*     */   }
/*     */   
/*     */   private static int deleteTeam(CommandSourceStack source, PlayerTeam team) {
/* 325 */     ServerScoreboard serverScoreboard = source.getServer().getScoreboard();
/* 326 */     serverScoreboard.removePlayerTeam(team);
/* 327 */     source.sendSuccess(() -> Component.translatable("commands.team.remove.success", new Object[] { team.getFormattedDisplayName() }), true);
/* 328 */     return serverScoreboard.getPlayerTeams().size();
/*     */   }
/*     */   
/*     */   private static int createTeam(CommandSourceStack source, String name) throws CommandSyntaxException {
/* 332 */     return createTeam(source, name, (Component)Component.literal(name));
/*     */   }
/*     */   
/*     */   private static int createTeam(CommandSourceStack source, String name, Component displayName) throws CommandSyntaxException {
/* 336 */     ServerScoreboard serverScoreboard = source.getServer().getScoreboard();
/* 337 */     if (serverScoreboard.getPlayerTeam(name) != null)
/* 338 */       throw ERROR_TEAM_ALREADY_EXISTS.create(); 
/* 341 */     PlayerTeam team = serverScoreboard.addPlayerTeam(name);
/* 342 */     team.setDisplayName(displayName);
/* 344 */     source.sendSuccess(() -> Component.translatable("commands.team.add.success", new Object[] { team.getFormattedDisplayName() }), true);
/* 346 */     return serverScoreboard.getPlayerTeams().size();
/*     */   }
/*     */   
/*     */   private static int listMembers(CommandSourceStack source, PlayerTeam team) {
/* 350 */     Collection<String> members = team.getPlayers();
/* 351 */     if (members.isEmpty()) {
/* 352 */       source.sendSuccess(() -> Component.translatable("commands.team.list.members.empty", new Object[] { team.getFormattedDisplayName() }), false);
/*     */     } else {
/* 354 */       source.sendSuccess(() -> Component.translatable("commands.team.list.members.success", new Object[] { team.getFormattedDisplayName(), Integer.valueOf(members.size()), ComponentUtils.formatList(members) }), false);
/*     */     } 
/* 356 */     return members.size();
/*     */   }
/*     */   
/*     */   private static int listTeams(CommandSourceStack source) {
/* 360 */     Collection<PlayerTeam> teams = source.getServer().getScoreboard().getPlayerTeams();
/* 361 */     if (teams.isEmpty()) {
/* 362 */       source.sendSuccess(() -> Component.translatable("commands.team.list.teams.empty"), false);
/*     */     } else {
/* 364 */       source.sendSuccess(() -> Component.translatable("commands.team.list.teams.success", new Object[] { Integer.valueOf(teams.size()), ComponentUtils.formatList(teams, PlayerTeam::getFormattedDisplayName) }), false);
/*     */     } 
/* 366 */     return teams.size();
/*     */   }
/*     */   
/*     */   private static int setPrefix(CommandSourceStack source, PlayerTeam team, Component prefix) {
/* 370 */     team.setPlayerPrefix(prefix);
/* 371 */     source.sendSuccess(() -> Component.translatable("commands.team.option.prefix.success", new Object[] { prefix }), false);
/* 372 */     return 1;
/*     */   }
/*     */   
/*     */   private static int setSuffix(CommandSourceStack source, PlayerTeam team, Component suffix) {
/* 376 */     team.setPlayerSuffix(suffix);
/* 377 */     source.sendSuccess(() -> Component.translatable("commands.team.option.suffix.success", new Object[] { suffix }), false);
/* 378 */     return 1;
/*     */   }
/*     */ }


/* Location:              C:\Users\karol\AppData\Roaming\ModrinthApp\meta\versions\26.2-0.19.5\26.2-0.19.5.jar!\net\minecraft\server\commands\TeamCommand.class
 * Java compiler version: 25 (69.0)
 * JD-Core Version:       1.1.3
 */