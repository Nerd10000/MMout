/*     */ package net.minecraft.server.commands;
/*     */ 
/*     */ import com.google.common.collect.Iterables;
/*     */ import com.mojang.brigadier.CommandDispatcher;
/*     */ import com.mojang.brigadier.Message;
/*     */ import com.mojang.brigadier.arguments.ArgumentType;
/*     */ import com.mojang.brigadier.arguments.StringArgumentType;
/*     */ import com.mojang.brigadier.builder.LiteralArgumentBuilder;
/*     */ import com.mojang.brigadier.builder.RequiredArgumentBuilder;
/*     */ import com.mojang.brigadier.context.CommandContext;
/*     */ import com.mojang.brigadier.exceptions.CommandSyntaxException;
/*     */ import com.mojang.brigadier.exceptions.Dynamic2CommandExceptionType;
/*     */ import com.mojang.brigadier.exceptions.DynamicCommandExceptionType;
/*     */ import com.mojang.brigadier.suggestion.SuggestionsBuilder;
/*     */ import java.util.ArrayList;
/*     */ import java.util.Collection;
/*     */ import java.util.List;
/*     */ import java.util.concurrent.CompletableFuture;
/*     */ import java.util.function.Predicate;
/*     */ import net.minecraft.advancements.Advancement;
/*     */ import net.minecraft.advancements.AdvancementHolder;
/*     */ import net.minecraft.advancements.AdvancementNode;
/*     */ import net.minecraft.advancements.AdvancementProgress;
/*     */ import net.minecraft.advancements.AdvancementTree;
/*     */ import net.minecraft.commands.CommandSourceStack;
/*     */ import net.minecraft.commands.Commands;
/*     */ import net.minecraft.commands.SharedSuggestionProvider;
/*     */ import net.minecraft.commands.arguments.EntityArgument;
/*     */ import net.minecraft.commands.arguments.ResourceKeyArgument;
/*     */ import net.minecraft.core.registries.Registries;
/*     */ import net.minecraft.network.chat.Component;
/*     */ import net.minecraft.server.level.ServerPlayer;
/*     */ 
/*     */ public class AdvancementCommands {
/*     */   private static final DynamicCommandExceptionType ERROR_NO_ACTION_PERFORMED;
/*     */   
/*     */   private static final Dynamic2CommandExceptionType ERROR_CRITERION_NOT_FOUND;
/*     */   
/*     */   static {
/*  35 */     ERROR_NO_ACTION_PERFORMED = new DynamicCommandExceptionType(msg -> (Message)msg);
/*  36 */     ERROR_CRITERION_NOT_FOUND = new Dynamic2CommandExceptionType((name, criterion) -> Component.translatableEscape("commands.advancement.criterionNotFound", new Object[] { name, criterion }));
/*     */   }
/*     */   
/*     */   public static void register(CommandDispatcher<CommandSourceStack> dispatcher) {
/*  39 */     dispatcher.register(
/*  40 */         (LiteralArgumentBuilder)((LiteralArgumentBuilder)((LiteralArgumentBuilder)Commands.literal("advancement")
/*  41 */         .requires((Predicate)Commands.hasPermission(Commands.LEVEL_GAMEMASTERS)))
/*  42 */         .then(
/*  43 */           Commands.literal("grant")
/*  44 */           .then((
/*  45 */             (RequiredArgumentBuilder)((RequiredArgumentBuilder)((RequiredArgumentBuilder)((RequiredArgumentBuilder)Commands.argument("targets", (ArgumentType)EntityArgument.players())
/*  46 */             .then(
/*  47 */               Commands.literal("only")
/*  48 */               .then((
/*  49 */                 (RequiredArgumentBuilder)Commands.argument("advancement", (ArgumentType)ResourceKeyArgument.key(Registries.ADVANCEMENT))
/*  50 */                 .executes(c -> perform((CommandSourceStack)c.getSource(), EntityArgument.getPlayers(c, "targets"), Action.GRANT, getAdvancements(c, ResourceKeyArgument.getAdvancement(c, "advancement"), Mode.ONLY))))
/*  51 */                 .then(
/*  52 */                   Commands.argument("criterion", (ArgumentType)StringArgumentType.greedyString())
/*  53 */                   .suggests((c, p) -> SharedSuggestionProvider.suggest(ResourceKeyArgument.getAdvancement(c, "advancement").value().criteria().keySet(), p))
/*  54 */                   .executes(c -> performCriterion((CommandSourceStack)c.getSource(), EntityArgument.getPlayers(c, "targets"), Action.GRANT, ResourceKeyArgument.getAdvancement(c, "advancement"), StringArgumentType.getString(c, "criterion")))))))
/*     */             
/*  58 */             .then(
/*  59 */               Commands.literal("from")
/*  60 */               .then(
/*  61 */                 Commands.argument("advancement", (ArgumentType)ResourceKeyArgument.key(Registries.ADVANCEMENT))
/*  62 */                 .executes(c -> perform((CommandSourceStack)c.getSource(), EntityArgument.getPlayers(c, "targets"), Action.GRANT, getAdvancements(c, ResourceKeyArgument.getAdvancement(c, "advancement"), Mode.FROM))))))
/*     */             
/*  65 */             .then(
/*  66 */               Commands.literal("until")
/*  67 */               .then(
/*  68 */                 Commands.argument("advancement", (ArgumentType)ResourceKeyArgument.key(Registries.ADVANCEMENT))
/*  69 */                 .executes(c -> perform((CommandSourceStack)c.getSource(), EntityArgument.getPlayers(c, "targets"), Action.GRANT, getAdvancements(c, ResourceKeyArgument.getAdvancement(c, "advancement"), Mode.UNTIL))))))
/*     */             
/*  72 */             .then(
/*  73 */               Commands.literal("through")
/*  74 */               .then(
/*  75 */                 Commands.argument("advancement", (ArgumentType)ResourceKeyArgument.key(Registries.ADVANCEMENT))
/*  76 */                 .executes(c -> perform((CommandSourceStack)c.getSource(), EntityArgument.getPlayers(c, "targets"), Action.GRANT, getAdvancements(c, ResourceKeyArgument.getAdvancement(c, "advancement"), Mode.THROUGH))))))
/*     */             
/*  79 */             .then(
/*  80 */               Commands.literal("everything")
/*  81 */               .executes(c -> perform((CommandSourceStack)c.getSource(), EntityArgument.getPlayers(c, "targets"), Action.GRANT, ((CommandSourceStack)c.getSource()).getServer().getAdvancements().getAllAdvancements(), false))))))
/*     */         
/*  85 */         .then(
/*  86 */           Commands.literal("revoke")
/*  87 */           .then((
/*  88 */             (RequiredArgumentBuilder)((RequiredArgumentBuilder)((RequiredArgumentBuilder)((RequiredArgumentBuilder)Commands.argument("targets", (ArgumentType)EntityArgument.players())
/*  89 */             .then(
/*  90 */               Commands.literal("only")
/*  91 */               .then((
/*  92 */                 (RequiredArgumentBuilder)Commands.argument("advancement", (ArgumentType)ResourceKeyArgument.key(Registries.ADVANCEMENT))
/*  93 */                 .executes(c -> perform((CommandSourceStack)c.getSource(), EntityArgument.getPlayers(c, "targets"), Action.REVOKE, getAdvancements(c, ResourceKeyArgument.getAdvancement(c, "advancement"), Mode.ONLY))))
/*  94 */                 .then(
/*  95 */                   Commands.argument("criterion", (ArgumentType)StringArgumentType.greedyString())
/*  96 */                   .suggests((c, p) -> SharedSuggestionProvider.suggest(ResourceKeyArgument.getAdvancement(c, "advancement").value().criteria().keySet(), p))
/*  97 */                   .executes(c -> performCriterion((CommandSourceStack)c.getSource(), EntityArgument.getPlayers(c, "targets"), Action.REVOKE, ResourceKeyArgument.getAdvancement(c, "advancement"), StringArgumentType.getString(c, "criterion")))))))
/*     */             
/* 101 */             .then(
/* 102 */               Commands.literal("from")
/* 103 */               .then(
/* 104 */                 Commands.argument("advancement", (ArgumentType)ResourceKeyArgument.key(Registries.ADVANCEMENT))
/* 105 */                 .executes(c -> perform((CommandSourceStack)c.getSource(), EntityArgument.getPlayers(c, "targets"), Action.REVOKE, getAdvancements(c, ResourceKeyArgument.getAdvancement(c, "advancement"), Mode.FROM))))))
/*     */             
/* 108 */             .then(
/* 109 */               Commands.literal("until")
/* 110 */               .then(
/* 111 */                 Commands.argument("advancement", (ArgumentType)ResourceKeyArgument.key(Registries.ADVANCEMENT))
/* 112 */                 .executes(c -> perform((CommandSourceStack)c.getSource(), EntityArgument.getPlayers(c, "targets"), Action.REVOKE, getAdvancements(c, ResourceKeyArgument.getAdvancement(c, "advancement"), Mode.UNTIL))))))
/*     */             
/* 115 */             .then(
/* 116 */               Commands.literal("through")
/* 117 */               .then(
/* 118 */                 Commands.argument("advancement", (ArgumentType)ResourceKeyArgument.key(Registries.ADVANCEMENT))
/* 119 */                 .executes(c -> perform((CommandSourceStack)c.getSource(), EntityArgument.getPlayers(c, "targets"), Action.REVOKE, getAdvancements(c, ResourceKeyArgument.getAdvancement(c, "advancement"), Mode.THROUGH))))))
/*     */             
/* 122 */             .then(
/* 123 */               Commands.literal("everything")
/* 124 */               .executes(c -> perform((CommandSourceStack)c.getSource(), EntityArgument.getPlayers(c, "targets"), Action.REVOKE, ((CommandSourceStack)c.getSource()).getServer().getAdvancements().getAllAdvancements()))))));
/*     */   }
/*     */   
/*     */   private static int perform(CommandSourceStack source, Collection<ServerPlayer> players, Action action, Collection<AdvancementHolder> advancements) throws CommandSyntaxException {
/* 132 */     return perform(source, players, action, advancements, true);
/*     */   }
/*     */   
/*     */   private static int perform(CommandSourceStack source, Collection<ServerPlayer> players, Action action, Collection<AdvancementHolder> advancements, boolean showAdvancements) throws CommandSyntaxException {
/* 136 */     int advancementCount = 0;
/* 137 */     int playerCount = 0;
/* 138 */     for (ServerPlayer player : players) {
/* 139 */       int changedAdvancements = action.perform(player, advancements, showAdvancements);
/* 140 */       if (changedAdvancements > 0)
/* 141 */         playerCount++; 
/* 143 */       advancementCount += changedAdvancements;
/*     */     } 
/* 146 */     if (advancementCount == 0) {
/* 147 */       if (advancements.size() == 1) {
/* 148 */         Component advancementName = Advancement.name((AdvancementHolder)Iterables.getOnlyElement(advancements));
/* 149 */         if (players.size() == 1)
/* 150 */           throw ERROR_NO_ACTION_PERFORMED.create(Component.translatable(action.getKey() + ".one.to.one.failure", new Object[] { advancementName, ((ServerPlayer)Iterables.getOnlyElement(players)).getDisplayName() })); 
/* 152 */         throw ERROR_NO_ACTION_PERFORMED.create(Component.translatable(action.getKey() + ".one.to.many.failure", new Object[] { advancementName, Integer.valueOf(players.size()) }));
/*     */       } 
/* 155 */       if (players.size() == 1)
/* 156 */         throw ERROR_NO_ACTION_PERFORMED.create(Component.translatable(action.getKey() + ".many.to.one.failure", new Object[] { Integer.valueOf(advancements.size()), ((ServerPlayer)Iterables.getOnlyElement(players)).getDisplayName() })); 
/* 158 */       throw ERROR_NO_ACTION_PERFORMED.create(Component.translatable(action.getKey() + ".many.to.many.failure", new Object[] { Integer.valueOf(advancements.size()), Integer.valueOf(players.size()) }));
/*     */     } 
/* 163 */     if (advancements.size() == 1) {
/* 164 */       Component advancementName = Advancement.name((AdvancementHolder)Iterables.getOnlyElement(advancements));
/* 165 */       if (players.size() == 1) {
/* 166 */         source.sendSuccess(() -> Component.translatable(action.getKey() + ".one.to.one.success", new Object[] { advancementName, ((ServerPlayer)Iterables.getOnlyElement(players)).getDisplayName() }), true);
/*     */       } else {
/* 168 */         int finalPlayerCount = playerCount;
/* 169 */         source.sendSuccess(() -> Component.translatable(action.getKey() + ".one.to.many.success", new Object[] { advancementName, Integer.valueOf(finalPlayerCount) }), true);
/*     */       } 
/*     */     } else {
/* 172 */       int finalAdvancementCount = advancementCount;
/* 173 */       if (players.size() == 1) {
/* 174 */         source.sendSuccess(() -> Component.translatable(action.getKey() + ".many.to.one.success", new Object[] { Integer.valueOf(finalAdvancementCount), ((ServerPlayer)Iterables.getOnlyElement(players)).getDisplayName() }), true);
/*     */       } else {
/* 176 */         int finalPlayerCount = playerCount;
/* 177 */         source.sendSuccess(() -> Component.translatable(action.getKey() + ".many.to.many.success", new Object[] { Integer.valueOf(finalAdvancementCount), Integer.valueOf(finalPlayerCount) }), true);
/*     */       } 
/*     */     } 
/* 181 */     return advancementCount;
/*     */   }
/*     */   
/*     */   private static int performCriterion(CommandSourceStack source, Collection<ServerPlayer> players, Action action, AdvancementHolder holder, String criterion) throws CommandSyntaxException {
/* 185 */     int playerCount = 0;
/* 187 */     Advancement advancement = holder.value();
/* 188 */     if (!advancement.criteria().containsKey(criterion))
/* 189 */       throw ERROR_CRITERION_NOT_FOUND.create(Advancement.name(holder), criterion); 
/* 192 */     for (ServerPlayer player : players) {
/* 193 */       if (action.performCriterion(player, holder, criterion))
/* 194 */         playerCount++; 
/*     */     } 
/* 198 */     if (playerCount == 0) {
/* 199 */       if (players.size() == 1)
/* 200 */         throw ERROR_NO_ACTION_PERFORMED.create(Component.translatable(action.getKey() + ".criterion.to.one.failure", new Object[] { criterion, Advancement.name(holder), ((ServerPlayer)Iterables.getOnlyElement(players)).getDisplayName() })); 
/* 202 */       throw ERROR_NO_ACTION_PERFORMED.create(Component.translatable(action.getKey() + ".criterion.to.many.failure", new Object[] { criterion, Advancement.name(holder), Integer.valueOf(players.size()) }));
/*     */     } 
/* 206 */     if (players.size() == 1) {
/* 207 */       source.sendSuccess(() -> Component.translatable(action.getKey() + ".criterion.to.one.success", new Object[] { criterion, Advancement.name(holder), ((ServerPlayer)Iterables.getOnlyElement(players)).getDisplayName() }), true);
/*     */     } else {
/* 209 */       int finalPlayerCount = playerCount;
/* 210 */       source.sendSuccess(() -> Component.translatable(action.getKey() + ".criterion.to.many.success", new Object[] { criterion, Advancement.name(holder), Integer.valueOf(finalPlayerCount) }), true);
/*     */     } 
/* 213 */     return playerCount;
/*     */   }
/*     */   
/*     */   private static List<AdvancementHolder> getAdvancements(CommandContext<CommandSourceStack> context, AdvancementHolder target, Mode mode) {
/* 217 */     AdvancementTree advancementTree = ((CommandSourceStack)context.getSource()).getServer().getAdvancements().tree();
/* 218 */     AdvancementNode targetNode = advancementTree.get(target);
/* 219 */     if (targetNode == null)
/* 220 */       return List.of(target); 
/* 222 */     List<AdvancementHolder> advancements = new ArrayList<>();
/* 223 */     if (mode.parents) {
/* 224 */       AdvancementNode parent = targetNode.parent();
/* 225 */       while (parent != null) {
/* 226 */         advancements.add(parent.holder());
/* 227 */         parent = parent.parent();
/*     */       } 
/*     */     } 
/* 230 */     advancements.add(target);
/* 231 */     if (mode.children)
/* 232 */       addChildren(targetNode, advancements); 
/* 234 */     return advancements;
/*     */   }
/*     */   
/*     */   private static void addChildren(AdvancementNode parent, List<AdvancementHolder> output) {
/* 238 */     for (AdvancementNode child : parent.children()) {
/* 239 */       output.add(child.holder());
/* 240 */       addChildren(child, output);
/*     */     } 
/*     */   }
/*     */   
/*     */   private enum Action {
/* 245 */     GRANT("grant") {
/*     */       protected boolean perform(ServerPlayer player, AdvancementHolder advancement) {
/* 248 */         AdvancementProgress progress = player.getAdvancements().getOrStartProgress(advancement);
/* 249 */         if (progress.isDone())
/* 250 */           return false; 
/* 252 */         for (String criterion : progress.getRemainingCriteria())
/* 253 */           player.getAdvancements().award(advancement, criterion); 
/* 255 */         return true;
/*     */       }
/*     */       
/*     */       protected boolean performCriterion(ServerPlayer player, AdvancementHolder advancement, String criterion) {
/* 260 */         return player.getAdvancements().award(advancement, criterion);
/*     */       }
/*     */     },
/* 263 */     REVOKE("revoke") {
/*     */       protected boolean perform(ServerPlayer player, AdvancementHolder advancement) {
/* 266 */         AdvancementProgress progress = player.getAdvancements().getOrStartProgress(advancement);
/* 267 */         if (!progress.hasProgress())
/* 268 */           return false; 
/* 270 */         for (String criterion : progress.getCompletedCriteria())
/* 271 */           player.getAdvancements().revoke(advancement, criterion); 
/* 273 */         return true;
/*     */       }
/*     */       
/*     */       protected boolean performCriterion(ServerPlayer player, AdvancementHolder advancement, String criterion) {
/* 278 */         return player.getAdvancements().revoke(advancement, criterion);
/*     */       }
/*     */     };
/*     */     
/*     */     private final String key;
/*     */     
/*     */     Action(String key) {
/* 286 */       this.key = "commands.advancement." + key;
/*     */     }
/*     */     
/*     */     public int perform(ServerPlayer player, Iterable<AdvancementHolder> advancements, boolean showAdvancements) {
/* 290 */       int count = 0;
/* 291 */       if (!showAdvancements)
/* 293 */         player.getAdvancements().flushDirty(player, true); 
/* 295 */       for (AdvancementHolder advancement : advancements) {
/* 296 */         if (perform(player, advancement))
/* 297 */           count++; 
/*     */       } 
/* 300 */       if (!showAdvancements)
/* 301 */         player.getAdvancements().flushDirty(player, false); 
/* 303 */       return count;
/*     */     }
/*     */     
/*     */     protected String getKey() {
/* 311 */       return this.key;
/*     */     }
/*     */     
/*     */     protected abstract boolean perform(ServerPlayer param1ServerPlayer, AdvancementHolder param1AdvancementHolder);
/*     */     
/*     */     protected abstract boolean performCriterion(ServerPlayer param1ServerPlayer, AdvancementHolder param1AdvancementHolder, String param1String);
/*     */   }
/*     */   
/*     */   enum null {
/*     */     protected boolean perform(ServerPlayer player, AdvancementHolder advancement) {
/*     */       AdvancementProgress progress = player.getAdvancements().getOrStartProgress(advancement);
/*     */       if (progress.isDone())
/*     */         return false; 
/*     */       for (String criterion : progress.getRemainingCriteria())
/*     */         player.getAdvancements().award(advancement, criterion); 
/*     */       return true;
/*     */     }
/*     */     
/*     */     protected boolean performCriterion(ServerPlayer player, AdvancementHolder advancement, String criterion) {
/*     */       return player.getAdvancements().award(advancement, criterion);
/*     */     }
/*     */   }
/*     */   
/*     */   enum null {
/*     */     protected boolean perform(ServerPlayer player, AdvancementHolder advancement) {
/*     */       AdvancementProgress progress = player.getAdvancements().getOrStartProgress(advancement);
/*     */       if (!progress.hasProgress())
/*     */         return false; 
/*     */       for (String criterion : progress.getCompletedCriteria())
/*     */         player.getAdvancements().revoke(advancement, criterion); 
/*     */       return true;
/*     */     }
/*     */     
/*     */     protected boolean performCriterion(ServerPlayer player, AdvancementHolder advancement, String criterion) {
/*     */       return player.getAdvancements().revoke(advancement, criterion);
/*     */     }
/*     */   }
/*     */   
/*     */   private enum Mode {
/* 316 */     ONLY(false, false),
/* 317 */     THROUGH(true, true),
/* 318 */     FROM(false, true),
/* 319 */     UNTIL(true, false),
/* 320 */     EVERYTHING(true, true);
/*     */     
/*     */     private final boolean parents;
/*     */     
/*     */     private final boolean children;
/*     */     
/*     */     Mode(boolean parents, boolean children) {
/* 327 */       this.parents = parents;
/* 328 */       this.children = children;
/*     */     }
/*     */   }
/*     */ }


/* Location:              C:\Users\karol\AppData\Roaming\ModrinthApp\meta\versions\26.2-0.19.5\26.2-0.19.5.jar!\net\minecraft\server\commands\AdvancementCommands.class
 * Java compiler version: 25 (69.0)
 * JD-Core Version:       1.1.3
 */