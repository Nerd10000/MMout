/*     */ package net.minecraft.server.commands;
/*     */ 
/*     */ import com.mojang.brigadier.CommandDispatcher;
/*     */ import com.mojang.brigadier.Message;
/*     */ import com.mojang.brigadier.arguments.ArgumentType;
/*     */ import com.mojang.brigadier.arguments.BoolArgumentType;
/*     */ import com.mojang.brigadier.arguments.IntegerArgumentType;
/*     */ import com.mojang.brigadier.builder.LiteralArgumentBuilder;
/*     */ import com.mojang.brigadier.builder.RequiredArgumentBuilder;
/*     */ import com.mojang.brigadier.context.CommandContext;
/*     */ import com.mojang.brigadier.exceptions.CommandSyntaxException;
/*     */ import com.mojang.brigadier.exceptions.DynamicCommandExceptionType;
/*     */ import com.mojang.brigadier.exceptions.SimpleCommandExceptionType;
/*     */ import com.mojang.brigadier.suggestion.SuggestionProvider;
/*     */ import com.mojang.brigadier.suggestion.SuggestionsBuilder;
/*     */ import java.util.Collection;
/*     */ import java.util.Collections;
/*     */ import java.util.concurrent.CompletableFuture;
/*     */ import java.util.function.Predicate;
/*     */ import net.minecraft.commands.CommandBuildContext;
/*     */ import net.minecraft.commands.CommandSourceStack;
/*     */ import net.minecraft.commands.Commands;
/*     */ import net.minecraft.commands.SharedSuggestionProvider;
/*     */ import net.minecraft.commands.arguments.ComponentArgument;
/*     */ import net.minecraft.commands.arguments.EntityArgument;
/*     */ import net.minecraft.commands.arguments.IdentifierArgument;
/*     */ import net.minecraft.network.chat.Component;
/*     */ import net.minecraft.network.chat.ComponentUtils;
/*     */ import net.minecraft.network.chat.MutableComponent;
/*     */ import net.minecraft.network.chat.ResolutionContext;
/*     */ import net.minecraft.resources.Identifier;
/*     */ import net.minecraft.server.bossevents.CustomBossEvent;
/*     */ import net.minecraft.server.bossevents.CustomBossEvents;
/*     */ import net.minecraft.server.level.ServerPlayer;
/*     */ import net.minecraft.world.BossEvent;
/*     */ import net.minecraft.world.entity.player.Player;
/*     */ 
/*     */ public class BossBarCommands {
/*     */   private static final DynamicCommandExceptionType ERROR_ALREADY_EXISTS;
/*     */   
/*     */   private static final DynamicCommandExceptionType ERROR_DOESNT_EXIST;
/*     */   
/*     */   static {
/*  40 */     ERROR_ALREADY_EXISTS = new DynamicCommandExceptionType(id -> Component.translatableEscape("commands.bossbar.create.failed", new Object[] { id }));
/*  41 */     ERROR_DOESNT_EXIST = new DynamicCommandExceptionType(id -> Component.translatableEscape("commands.bossbar.unknown", new Object[] { id }));
/*     */   }
/*     */   
/*  42 */   private static final SimpleCommandExceptionType ERROR_NO_PLAYER_CHANGE = new SimpleCommandExceptionType((Message)Component.translatable("commands.bossbar.set.players.unchanged"));
/*     */   
/*  43 */   private static final SimpleCommandExceptionType ERROR_NO_NAME_CHANGE = new SimpleCommandExceptionType((Message)Component.translatable("commands.bossbar.set.name.unchanged"));
/*     */   
/*  44 */   private static final SimpleCommandExceptionType ERROR_NO_COLOR_CHANGE = new SimpleCommandExceptionType((Message)Component.translatable("commands.bossbar.set.color.unchanged"));
/*     */   
/*  45 */   private static final SimpleCommandExceptionType ERROR_NO_STYLE_CHANGE = new SimpleCommandExceptionType((Message)Component.translatable("commands.bossbar.set.style.unchanged"));
/*     */   
/*  46 */   private static final SimpleCommandExceptionType ERROR_NO_VALUE_CHANGE = new SimpleCommandExceptionType((Message)Component.translatable("commands.bossbar.set.value.unchanged"));
/*     */   
/*  47 */   private static final SimpleCommandExceptionType ERROR_NO_MAX_CHANGE = new SimpleCommandExceptionType((Message)Component.translatable("commands.bossbar.set.max.unchanged"));
/*     */   
/*  48 */   private static final SimpleCommandExceptionType ERROR_ALREADY_HIDDEN = new SimpleCommandExceptionType((Message)Component.translatable("commands.bossbar.set.visibility.unchanged.hidden"));
/*     */   
/*  49 */   private static final SimpleCommandExceptionType ERROR_ALREADY_VISIBLE = new SimpleCommandExceptionType((Message)Component.translatable("commands.bossbar.set.visibility.unchanged.visible"));
/*     */   
/*     */   public static final SuggestionProvider<CommandSourceStack> SUGGEST_BOSS_BAR;
/*     */   
/*     */   static {
/*  50 */     SUGGEST_BOSS_BAR = ((c, b) -> SharedSuggestionProvider.suggestResource(((CommandSourceStack)c.getSource()).getServer().getCustomBossEvents().getIds(), b));
/*     */   }
/*     */   
/*     */   public static void register(CommandDispatcher<CommandSourceStack> dispatcher, CommandBuildContext context) {
/*  53 */     dispatcher.register(
/*  54 */         (LiteralArgumentBuilder)((LiteralArgumentBuilder)((LiteralArgumentBuilder)((LiteralArgumentBuilder)((LiteralArgumentBuilder)((LiteralArgumentBuilder)Commands.literal("bossbar")
/*  55 */         .requires((Predicate)Commands.hasPermission(Commands.LEVEL_GAMEMASTERS)))
/*  56 */         .then(
/*  57 */           Commands.literal("add")
/*  58 */           .then(
/*  59 */             Commands.argument("id", (ArgumentType)IdentifierArgument.id())
/*  60 */             .then(
/*  61 */               Commands.argument("name", (ArgumentType)ComponentArgument.textComponent(context))
/*  62 */               .executes(c -> createBar((CommandSourceStack)c.getSource(), IdentifierArgument.getId(c, "id"), ComponentArgument.getResolvedComponent(c, "name")))))))
/*     */         
/*  66 */         .then(
/*  67 */           Commands.literal("remove")
/*  68 */           .then(
/*  69 */             Commands.argument("id", (ArgumentType)IdentifierArgument.id())
/*  70 */             .suggests(SUGGEST_BOSS_BAR)
/*  71 */             .executes(c -> removeBar((CommandSourceStack)c.getSource(), getBossBar(c))))))
/*     */         
/*  74 */         .then(
/*  75 */           Commands.literal("list")
/*  76 */           .executes(c -> listBars((CommandSourceStack)c.getSource()))))
/*     */         
/*  78 */         .then(
/*  79 */           Commands.literal("set")
/*  80 */           .then((
/*  81 */             (RequiredArgumentBuilder)((RequiredArgumentBuilder)((RequiredArgumentBuilder)((RequiredArgumentBuilder)((RequiredArgumentBuilder)((RequiredArgumentBuilder)Commands.argument("id", (ArgumentType)IdentifierArgument.id())
/*  82 */             .suggests(SUGGEST_BOSS_BAR)
/*  83 */             .then(
/*  84 */               Commands.literal("name")
/*  85 */               .then(
/*  86 */                 Commands.argument("name", (ArgumentType)ComponentArgument.textComponent(context))
/*  87 */                 .executes(c -> setName((CommandSourceStack)c.getSource(), getBossBar(c), ComponentArgument.getResolvedComponent(c, "name"))))))
/*     */             
/*  90 */             .then((
/*  91 */               (LiteralArgumentBuilder)((LiteralArgumentBuilder)((LiteralArgumentBuilder)((LiteralArgumentBuilder)((LiteralArgumentBuilder)((LiteralArgumentBuilder)Commands.literal("color")
/*  92 */               .then(
/*  93 */                 Commands.literal("pink")
/*  94 */                 .executes(c -> setColor((CommandSourceStack)c.getSource(), getBossBar(c), BossEvent.BossBarColor.PINK))))
/*     */               
/*  96 */               .then(
/*  97 */                 Commands.literal("blue")
/*  98 */                 .executes(c -> setColor((CommandSourceStack)c.getSource(), getBossBar(c), BossEvent.BossBarColor.BLUE))))
/*     */               
/* 100 */               .then(
/* 101 */                 Commands.literal("red")
/* 102 */                 .executes(c -> setColor((CommandSourceStack)c.getSource(), getBossBar(c), BossEvent.BossBarColor.RED))))
/*     */               
/* 104 */               .then(
/* 105 */                 Commands.literal("green")
/* 106 */                 .executes(c -> setColor((CommandSourceStack)c.getSource(), getBossBar(c), BossEvent.BossBarColor.GREEN))))
/*     */               
/* 108 */               .then(
/* 109 */                 Commands.literal("yellow")
/* 110 */                 .executes(c -> setColor((CommandSourceStack)c.getSource(), getBossBar(c), BossEvent.BossBarColor.YELLOW))))
/*     */               
/* 112 */               .then(
/* 113 */                 Commands.literal("purple")
/* 114 */                 .executes(c -> setColor((CommandSourceStack)c.getSource(), getBossBar(c), BossEvent.BossBarColor.PURPLE))))
/*     */               
/* 116 */               .then(
/* 117 */                 Commands.literal("white")
/* 118 */                 .executes(c -> setColor((CommandSourceStack)c.getSource(), getBossBar(c), BossEvent.BossBarColor.WHITE)))))
/*     */             
/* 121 */             .then((
/* 122 */               (LiteralArgumentBuilder)((LiteralArgumentBuilder)((LiteralArgumentBuilder)((LiteralArgumentBuilder)Commands.literal("style")
/* 123 */               .then(
/* 124 */                 Commands.literal("progress")
/* 125 */                 .executes(c -> setStyle((CommandSourceStack)c.getSource(), getBossBar(c), BossEvent.BossBarOverlay.PROGRESS))))
/*     */               
/* 127 */               .then(
/* 128 */                 Commands.literal("notched_6")
/* 129 */                 .executes(c -> setStyle((CommandSourceStack)c.getSource(), getBossBar(c), BossEvent.BossBarOverlay.NOTCHED_6))))
/*     */               
/* 131 */               .then(
/* 132 */                 Commands.literal("notched_10")
/* 133 */                 .executes(c -> setStyle((CommandSourceStack)c.getSource(), getBossBar(c), BossEvent.BossBarOverlay.NOTCHED_10))))
/*     */               
/* 135 */               .then(
/* 136 */                 Commands.literal("notched_12")
/* 137 */                 .executes(c -> setStyle((CommandSourceStack)c.getSource(), getBossBar(c), BossEvent.BossBarOverlay.NOTCHED_12))))
/*     */               
/* 139 */               .then(
/* 140 */                 Commands.literal("notched_20")
/* 141 */                 .executes(c -> setStyle((CommandSourceStack)c.getSource(), getBossBar(c), BossEvent.BossBarOverlay.NOTCHED_20)))))
/*     */             
/* 144 */             .then(
/* 145 */               Commands.literal("value")
/* 146 */               .then(
/* 147 */                 Commands.argument("value", (ArgumentType)IntegerArgumentType.integer(0))
/* 148 */                 .executes(c -> setValue((CommandSourceStack)c.getSource(), getBossBar(c), IntegerArgumentType.getInteger(c, "value"))))))
/*     */             
/* 151 */             .then(
/* 152 */               Commands.literal("max")
/* 153 */               .then(
/* 154 */                 Commands.argument("max", (ArgumentType)IntegerArgumentType.integer(1))
/* 155 */                 .executes(c -> setMax((CommandSourceStack)c.getSource(), getBossBar(c), IntegerArgumentType.getInteger(c, "max"))))))
/*     */             
/* 158 */             .then(
/* 159 */               Commands.literal("visible")
/* 160 */               .then(
/* 161 */                 Commands.argument("visible", (ArgumentType)BoolArgumentType.bool())
/* 162 */                 .executes(c -> setVisible((CommandSourceStack)c.getSource(), getBossBar(c), BoolArgumentType.getBool(c, "visible"))))))
/*     */             
/* 165 */             .then((
/* 166 */               (LiteralArgumentBuilder)Commands.literal("players")
/* 167 */               .executes(c -> setPlayers((CommandSourceStack)c.getSource(), getBossBar(c), Collections.emptyList())))
/* 168 */               .then(
/* 169 */                 Commands.argument("targets", (ArgumentType)EntityArgument.players())
/* 170 */                 .executes(c -> setPlayers((CommandSourceStack)c.getSource(), getBossBar(c), EntityArgument.getOptionalPlayers(c, "targets"))))))))
/*     */         
/* 175 */         .then(
/* 176 */           Commands.literal("get")
/* 177 */           .then((
/* 178 */             (RequiredArgumentBuilder)((RequiredArgumentBuilder)((RequiredArgumentBuilder)Commands.argument("id", (ArgumentType)IdentifierArgument.id())
/* 179 */             .suggests(SUGGEST_BOSS_BAR)
/* 180 */             .then(
/* 181 */               Commands.literal("value")
/* 182 */               .executes(c -> getValue((CommandSourceStack)c.getSource(), getBossBar(c)))))
/*     */             
/* 184 */             .then(
/* 185 */               Commands.literal("max")
/* 186 */               .executes(c -> getMax((CommandSourceStack)c.getSource(), getBossBar(c)))))
/*     */             
/* 188 */             .then(
/* 189 */               Commands.literal("visible")
/* 190 */               .executes(c -> getVisible((CommandSourceStack)c.getSource(), getBossBar(c)))))
/*     */             
/* 192 */             .then(
/* 193 */               Commands.literal("players")
/* 194 */               .executes(c -> getPlayers((CommandSourceStack)c.getSource(), getBossBar(c)))))));
/*     */   }
/*     */   
/*     */   private static int getValue(CommandSourceStack source, CustomBossEvent bossBar) {
/* 202 */     source.sendSuccess(() -> Component.translatable("commands.bossbar.get.value", new Object[] { bossBar.getDisplayName(), Integer.valueOf(bossBar.value()) }), true);
/* 203 */     return bossBar.value();
/*     */   }
/*     */   
/*     */   private static int getMax(CommandSourceStack source, CustomBossEvent bossBar) {
/* 207 */     source.sendSuccess(() -> Component.translatable("commands.bossbar.get.max", new Object[] { bossBar.getDisplayName(), Integer.valueOf(bossBar.max()) }), true);
/* 208 */     return bossBar.max();
/*     */   }
/*     */   
/*     */   private static int getVisible(CommandSourceStack source, CustomBossEvent bossBar) {
/* 212 */     if (bossBar.isVisible()) {
/* 213 */       source.sendSuccess(() -> Component.translatable("commands.bossbar.get.visible.visible", new Object[] { bossBar.getDisplayName() }), true);
/* 214 */       return 1;
/*     */     } 
/* 216 */     source.sendSuccess(() -> Component.translatable("commands.bossbar.get.visible.hidden", new Object[] { bossBar.getDisplayName() }), true);
/* 217 */     return 0;
/*     */   }
/*     */   
/*     */   private static int getPlayers(CommandSourceStack source, CustomBossEvent bossBar) {
/* 222 */     if (bossBar.getPlayers().isEmpty()) {
/* 223 */       source.sendSuccess(() -> Component.translatable("commands.bossbar.get.players.none", new Object[] { bossBar.getDisplayName() }), true);
/*     */     } else {
/* 225 */       source.sendSuccess(() -> Component.translatable("commands.bossbar.get.players.some", new Object[] { bossBar.getDisplayName(), Integer.valueOf(bossBar.getPlayers().size()), ComponentUtils.formatList(bossBar.getPlayers(), Player::getDisplayName) }), true);
/*     */     } 
/* 227 */     return bossBar.getPlayers().size();
/*     */   }
/*     */   
/*     */   private static int setVisible(CommandSourceStack source, CustomBossEvent bossBar, boolean visible) throws CommandSyntaxException {
/* 231 */     if (bossBar.isVisible() == visible) {
/* 232 */       if (visible)
/* 233 */         throw ERROR_ALREADY_VISIBLE.create(); 
/* 235 */       throw ERROR_ALREADY_HIDDEN.create();
/*     */     } 
/* 238 */     bossBar.setVisible(visible);
/* 239 */     if (visible) {
/* 240 */       source.sendSuccess(() -> Component.translatable("commands.bossbar.set.visible.success.visible", new Object[] { bossBar.getDisplayName() }), true);
/*     */     } else {
/* 242 */       source.sendSuccess(() -> Component.translatable("commands.bossbar.set.visible.success.hidden", new Object[] { bossBar.getDisplayName() }), true);
/*     */     } 
/* 244 */     return 0;
/*     */   }
/*     */   
/*     */   private static int setValue(CommandSourceStack source, CustomBossEvent bossBar, int value) throws CommandSyntaxException {
/* 248 */     if (bossBar.value() == value)
/* 249 */       throw ERROR_NO_VALUE_CHANGE.create(); 
/* 251 */     bossBar.setValue(value);
/* 252 */     source.sendSuccess(() -> Component.translatable("commands.bossbar.set.value.success", new Object[] { bossBar.getDisplayName(), Integer.valueOf(value) }), true);
/* 253 */     return value;
/*     */   }
/*     */   
/*     */   private static int setMax(CommandSourceStack source, CustomBossEvent bossBar, int value) throws CommandSyntaxException {
/* 257 */     if (bossBar.max() == value)
/* 258 */       throw ERROR_NO_MAX_CHANGE.create(); 
/* 260 */     bossBar.setMax(value);
/* 261 */     source.sendSuccess(() -> Component.translatable("commands.bossbar.set.max.success", new Object[] { bossBar.getDisplayName(), Integer.valueOf(value) }), true);
/* 262 */     return value;
/*     */   }
/*     */   
/*     */   private static int setColor(CommandSourceStack source, CustomBossEvent bossBar, BossEvent.BossBarColor color) throws CommandSyntaxException {
/* 266 */     if (bossBar.getColor().equals(color))
/* 267 */       throw ERROR_NO_COLOR_CHANGE.create(); 
/* 269 */     bossBar.setColor(color);
/* 270 */     source.sendSuccess(() -> Component.translatable("commands.bossbar.set.color.success", new Object[] { bossBar.getDisplayName() }), true);
/* 271 */     return 0;
/*     */   }
/*     */   
/*     */   private static int setStyle(CommandSourceStack source, CustomBossEvent bossBar, BossEvent.BossBarOverlay style) throws CommandSyntaxException {
/* 275 */     if (bossBar.getOverlay().equals(style))
/* 276 */       throw ERROR_NO_STYLE_CHANGE.create(); 
/* 278 */     bossBar.setOverlay(style);
/* 279 */     source.sendSuccess(() -> Component.translatable("commands.bossbar.set.style.success", new Object[] { bossBar.getDisplayName() }), true);
/* 280 */     return 0;
/*     */   }
/*     */   
/*     */   private static int setName(CommandSourceStack source, CustomBossEvent bossBar, Component name) throws CommandSyntaxException {
/* 284 */     MutableComponent mutableComponent = ComponentUtils.resolve(ResolutionContext.builder().withSource(source).withEntityOverride(null).build(), name);
/* 285 */     if (bossBar.getName().equals(mutableComponent))
/* 286 */       throw ERROR_NO_NAME_CHANGE.create(); 
/* 288 */     bossBar.setName((Component)mutableComponent);
/* 289 */     source.sendSuccess(() -> Component.translatable("commands.bossbar.set.name.success", new Object[] { bossBar.getDisplayName() }), true);
/* 290 */     return 0;
/*     */   }
/*     */   
/*     */   private static int setPlayers(CommandSourceStack source, CustomBossEvent bossBar, Collection<ServerPlayer> targets) throws CommandSyntaxException {
/* 294 */     boolean changed = bossBar.setPlayers(targets);
/* 295 */     if (!changed)
/* 296 */       throw ERROR_NO_PLAYER_CHANGE.create(); 
/* 298 */     if (bossBar.getPlayers().isEmpty()) {
/* 299 */       source.sendSuccess(() -> Component.translatable("commands.bossbar.set.players.success.none", new Object[] { bossBar.getDisplayName() }), true);
/*     */     } else {
/* 301 */       source.sendSuccess(() -> Component.translatable("commands.bossbar.set.players.success.some", new Object[] { bossBar.getDisplayName(), Integer.valueOf(targets.size()), ComponentUtils.formatList(targets, Player::getDisplayName) }), true);
/*     */     } 
/* 303 */     return bossBar.getPlayers().size();
/*     */   }
/*     */   
/*     */   private static int listBars(CommandSourceStack source) {
/* 307 */     Collection<CustomBossEvent> events = source.getServer().getCustomBossEvents().getEvents();
/* 308 */     if (events.isEmpty()) {
/* 309 */       source.sendSuccess(() -> Component.translatable("commands.bossbar.list.bars.none"), false);
/*     */     } else {
/* 311 */       source.sendSuccess(() -> Component.translatable("commands.bossbar.list.bars.some", new Object[] { Integer.valueOf(events.size()), ComponentUtils.formatList(events, CustomBossEvent::getDisplayName) }), false);
/*     */     } 
/* 313 */     return events.size();
/*     */   }
/*     */   
/*     */   private static int createBar(CommandSourceStack source, Identifier id, Component name) throws CommandSyntaxException {
/* 317 */     CustomBossEvents events = source.getServer().getCustomBossEvents();
/* 318 */     if (events.get(id) != null)
/* 319 */       throw ERROR_ALREADY_EXISTS.create(id.toString()); 
/* 321 */     CustomBossEvent event = events.create(source.getLevel().getRandom(), id, (Component)ComponentUtils.resolve(ResolutionContext.builder().withSource(source).withEntityOverride(null).build(), name));
/* 322 */     source.sendSuccess(() -> Component.translatable("commands.bossbar.create.success", new Object[] { event.getDisplayName() }), true);
/* 323 */     return events.getEvents().size();
/*     */   }
/*     */   
/*     */   private static int removeBar(CommandSourceStack source, CustomBossEvent bossBar) {
/* 327 */     CustomBossEvents events = source.getServer().getCustomBossEvents();
/* 328 */     bossBar.removeAllPlayers();
/* 329 */     events.remove(bossBar);
/* 330 */     source.sendSuccess(() -> Component.translatable("commands.bossbar.remove.success", new Object[] { bossBar.getDisplayName() }), true);
/* 331 */     return events.getEvents().size();
/*     */   }
/*     */   
/*     */   public static CustomBossEvent getBossBar(CommandContext<CommandSourceStack> context) throws CommandSyntaxException {
/* 335 */     Identifier id = IdentifierArgument.getId(context, "id");
/* 336 */     CustomBossEvent event = ((CommandSourceStack)context.getSource()).getServer().getCustomBossEvents().get(id);
/* 337 */     if (event == null)
/* 338 */       throw ERROR_DOESNT_EXIST.create(id.toString()); 
/* 340 */     return event;
/*     */   }
/*     */ }


/* Location:              C:\Users\karol\AppData\Roaming\ModrinthApp\meta\versions\26.2-0.19.5\26.2-0.19.5.jar!\net\minecraft\server\commands\BossBarCommands.class
 * Java compiler version: 25 (69.0)
 * JD-Core Version:       1.1.3
 */