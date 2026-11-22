/*     */ package net.minecraft.commands;
/*     */ 
/*     */ import com.mojang.brigadier.CommandDispatcher;
/*     */ import com.mojang.brigadier.ParseResults;
/*     */ import com.mojang.brigadier.StringReader;
/*     */ import com.mojang.brigadier.arguments.ArgumentType;
/*     */ import com.mojang.brigadier.builder.ArgumentBuilder;
/*     */ import com.mojang.brigadier.builder.LiteralArgumentBuilder;
/*     */ import com.mojang.brigadier.builder.RequiredArgumentBuilder;
/*     */ import com.mojang.brigadier.context.CommandContextBuilder;
/*     */ import com.mojang.brigadier.context.ContextChain;
/*     */ import com.mojang.brigadier.exceptions.CommandSyntaxException;
/*     */ import com.mojang.brigadier.suggestion.SuggestionProvider;
/*     */ import com.mojang.brigadier.tree.ArgumentCommandNode;
/*     */ import com.mojang.brigadier.tree.CommandNode;
/*     */ import com.mojang.brigadier.tree.RootCommandNode;
/*     */ import com.mojang.logging.LogUtils;
/*     */ import java.util.Collection;
/*     */ import java.util.HashMap;
/*     */ import java.util.Map;
/*     */ import java.util.Optional;
/*     */ import java.util.Set;
/*     */ import java.util.function.Consumer;
/*     */ import java.util.function.Predicate;
/*     */ import java.util.function.UnaryOperator;
/*     */ import java.util.stream.Collectors;
/*     */ import java.util.stream.Stream;
/*     */ import net.minecraft.ChatFormatting;
/*     */ import net.minecraft.SharedConstants;
/*     */ import net.minecraft.commands.execution.ExecutionContext;
/*     */ import net.minecraft.commands.synchronization.ArgumentTypeInfos;
/*     */ import net.minecraft.commands.synchronization.ArgumentUtils;
/*     */ import net.minecraft.commands.synchronization.SuggestionProviders;
/*     */ import net.minecraft.core.HolderLookup;
/*     */ import net.minecraft.core.HolderOwner;
/*     */ import net.minecraft.core.HolderSet;
/*     */ import net.minecraft.core.Registry;
/*     */ import net.minecraft.data.registries.VanillaRegistries;
/*     */ import net.minecraft.gametest.framework.TestCommand;
/*     */ import net.minecraft.network.chat.ClickEvent;
/*     */ import net.minecraft.network.chat.CommonComponents;
/*     */ import net.minecraft.network.chat.Component;
/*     */ import net.minecraft.network.chat.ComponentUtils;
/*     */ import net.minecraft.network.chat.HoverEvent;
/*     */ import net.minecraft.network.chat.MutableComponent;
/*     */ import net.minecraft.network.chat.Style;
/*     */ import net.minecraft.network.protocol.Packet;
/*     */ import net.minecraft.network.protocol.game.ClientboundCommandsPacket;
/*     */ import net.minecraft.resources.Identifier;
/*     */ import net.minecraft.resources.ResourceKey;
/*     */ import net.minecraft.server.commands.AdvancementCommands;
/*     */ import net.minecraft.server.commands.AttributeCommand;
/*     */ import net.minecraft.server.commands.BanIpCommands;
/*     */ import net.minecraft.server.commands.BanListCommands;
/*     */ import net.minecraft.server.commands.BanPlayerCommands;
/*     */ import net.minecraft.server.commands.BossBarCommands;
/*     */ import net.minecraft.server.commands.ChaseCommand;
/*     */ import net.minecraft.server.commands.ClearInventoryCommands;
/*     */ import net.minecraft.server.commands.CloneCommands;
/*     */ import net.minecraft.server.commands.DamageCommand;
/*     */ import net.minecraft.server.commands.DataPackCommand;
/*     */ import net.minecraft.server.commands.DeOpCommands;
/*     */ import net.minecraft.server.commands.DebugCommand;
/*     */ import net.minecraft.server.commands.DebugConfigCommand;
/*     */ import net.minecraft.server.commands.DebugMobSpawningCommand;
/*     */ import net.minecraft.server.commands.DebugPathCommand;
/*     */ import net.minecraft.server.commands.DefaultGameModeCommands;
/*     */ import net.minecraft.server.commands.DialogCommand;
/*     */ import net.minecraft.server.commands.DifficultyCommand;
/*     */ import net.minecraft.server.commands.EffectCommands;
/*     */ import net.minecraft.server.commands.EmoteCommands;
/*     */ import net.minecraft.server.commands.EnchantCommand;
/*     */ import net.minecraft.server.commands.ExecuteCommand;
/*     */ import net.minecraft.server.commands.ExperienceCommand;
/*     */ import net.minecraft.server.commands.FetchProfileCommand;
/*     */ import net.minecraft.server.commands.FillBiomeCommand;
/*     */ import net.minecraft.server.commands.FillCommand;
/*     */ import net.minecraft.server.commands.ForceLoadCommand;
/*     */ import net.minecraft.server.commands.FunctionCommand;
/*     */ import net.minecraft.server.commands.GameModeCommand;
/*     */ import net.minecraft.server.commands.GameRuleCommand;
/*     */ import net.minecraft.server.commands.GiveCommand;
/*     */ import net.minecraft.server.commands.HelpCommand;
/*     */ import net.minecraft.server.commands.ItemCommands;
/*     */ import net.minecraft.server.commands.JfrCommand;
/*     */ import net.minecraft.server.commands.KickCommand;
/*     */ import net.minecraft.server.commands.KillCommand;
/*     */ import net.minecraft.server.commands.ListPlayersCommand;
/*     */ import net.minecraft.server.commands.LocateCommand;
/*     */ import net.minecraft.server.commands.LootCommand;
/*     */ import net.minecraft.server.commands.MsgCommand;
/*     */ import net.minecraft.server.commands.OpCommand;
/*     */ import net.minecraft.server.commands.PardonCommand;
/*     */ import net.minecraft.server.commands.PardonIpCommand;
/*     */ import net.minecraft.server.commands.ParticleCommand;
/*     */ import net.minecraft.server.commands.PerfCommand;
/*     */ import net.minecraft.server.commands.PlaceCommand;
/*     */ import net.minecraft.server.commands.PlaySoundCommand;
/*     */ import net.minecraft.server.commands.PublishCommand;
/*     */ import net.minecraft.server.commands.RaidCommand;
/*     */ import net.minecraft.server.commands.RandomCommand;
/*     */ import net.minecraft.server.commands.RecipeCommand;
/*     */ import net.minecraft.server.commands.ReloadCommand;
/*     */ import net.minecraft.server.commands.ReturnCommand;
/*     */ import net.minecraft.server.commands.RideCommand;
/*     */ import net.minecraft.server.commands.RotateCommand;
/*     */ import net.minecraft.server.commands.SaveAllCommand;
/*     */ import net.minecraft.server.commands.SaveOffCommand;
/*     */ import net.minecraft.server.commands.SaveOnCommand;
/*     */ import net.minecraft.server.commands.SayCommand;
/*     */ import net.minecraft.server.commands.ScheduleCommand;
/*     */ import net.minecraft.server.commands.ScoreboardCommand;
/*     */ import net.minecraft.server.commands.SeedCommand;
/*     */ import net.minecraft.server.commands.ServerPackCommand;
/*     */ import net.minecraft.server.commands.SetBlockCommand;
/*     */ import net.minecraft.server.commands.SetPlayerIdleTimeoutCommand;
/*     */ import net.minecraft.server.commands.SetSpawnCommand;
/*     */ import net.minecraft.server.commands.SetWorldSpawnCommand;
/*     */ import net.minecraft.server.commands.SpawnArmorTrimsCommand;
/*     */ import net.minecraft.server.commands.SpectateCommand;
/*     */ import net.minecraft.server.commands.SpreadPlayersCommand;
/*     */ import net.minecraft.server.commands.StopCommand;
/*     */ import net.minecraft.server.commands.StopSoundCommand;
/*     */ import net.minecraft.server.commands.StopwatchCommand;
/*     */ import net.minecraft.server.commands.SummonCommand;
/*     */ import net.minecraft.server.commands.SwingCommand;
/*     */ import net.minecraft.server.commands.TagCommand;
/*     */ import net.minecraft.server.commands.TeamCommand;
/*     */ import net.minecraft.server.commands.TeamMsgCommand;
/*     */ import net.minecraft.server.commands.TeleportCommand;
/*     */ import net.minecraft.server.commands.TellRawCommand;
/*     */ import net.minecraft.server.commands.TickCommand;
/*     */ import net.minecraft.server.commands.TimeCommand;
/*     */ import net.minecraft.server.commands.TitleCommand;
/*     */ import net.minecraft.server.commands.TransferCommand;
/*     */ import net.minecraft.server.commands.TriggerCommand;
/*     */ import net.minecraft.server.commands.UnpublishCommand;
/*     */ import net.minecraft.server.commands.VersionCommand;
/*     */ import net.minecraft.server.commands.WardenSpawnTrackerCommand;
/*     */ import net.minecraft.server.commands.WaypointCommand;
/*     */ import net.minecraft.server.commands.WeatherCommand;
/*     */ import net.minecraft.server.commands.WhitelistCommand;
/*     */ import net.minecraft.server.commands.WorldBorderCommand;
/*     */ import net.minecraft.server.commands.data.DataCommands;
/*     */ import net.minecraft.server.level.ServerPlayer;
/*     */ import net.minecraft.server.permissions.PermissionCheck;
/*     */ import net.minecraft.server.permissions.PermissionProviderCheck;
/*     */ import net.minecraft.server.permissions.PermissionSet;
/*     */ import net.minecraft.server.permissions.Permissions;
/*     */ import net.minecraft.tags.TagKey;
/*     */ import net.minecraft.util.Util;
/*     */ import net.minecraft.util.profiling.Profiler;
/*     */ import net.minecraft.util.profiling.jfr.JvmProfiler;
/*     */ import net.minecraft.world.flag.FeatureFlagSet;
/*     */ import net.minecraft.world.flag.FeatureFlags;
/*     */ import net.minecraft.world.level.gamerules.GameRules;
/*     */ import net.minecraft.world.phys.Vec2;
/*     */ import net.minecraft.world.phys.Vec3;
/*     */ import org.slf4j.Logger;
/*     */ 
/*     */ public class Commands {
/*     */   public static final String COMMAND_PREFIX = "/";
/*     */   
/* 169 */   private static final ThreadLocal<ExecutionContext<CommandSourceStack>> CURRENT_EXECUTION_CONTEXT = new ThreadLocal<>();
/*     */   
/* 171 */   private static final Logger LOGGER = LogUtils.getLogger();
/*     */   
/* 174 */   public static final PermissionCheck LEVEL_ALL = (PermissionCheck)PermissionCheck.AlwaysPass.INSTANCE;
/*     */   
/* 176 */   public static final PermissionCheck LEVEL_MODERATORS = (PermissionCheck)new PermissionCheck.Require(Permissions.COMMANDS_MODERATOR);
/*     */   
/* 178 */   public static final PermissionCheck LEVEL_GAMEMASTERS = (PermissionCheck)new PermissionCheck.Require(Permissions.COMMANDS_GAMEMASTER);
/*     */   
/* 180 */   public static final PermissionCheck LEVEL_ADMINS = (PermissionCheck)new PermissionCheck.Require(Permissions.COMMANDS_ADMIN);
/*     */   
/* 182 */   public static final PermissionCheck LEVEL_OWNERS = (PermissionCheck)new PermissionCheck.Require(Permissions.COMMANDS_OWNER);
/*     */   
/* 184 */   private static final ClientboundCommandsPacket.NodeInspector<CommandSourceStack> COMMAND_NODE_INSPECTOR = new ClientboundCommandsPacket.NodeInspector<CommandSourceStack>() {
/* 185 */       private final CommandSourceStack noPermissionSource = Commands.createCompilationContext(PermissionSet.NO_PERMISSIONS);
/*     */       
/*     */       public Identifier suggestionId(ArgumentCommandNode<CommandSourceStack, ?> node) {
/* 189 */         SuggestionProvider<CommandSourceStack> suggestionProvider = node.getCustomSuggestions();
/* 190 */         return (suggestionProvider != null) ? SuggestionProviders.getName(suggestionProvider) : null;
/*     */       }
/*     */       
/*     */       public boolean isExecutable(CommandNode<CommandSourceStack> node) {
/* 195 */         return (node.getCommand() != null);
/*     */       }
/*     */       
/*     */       public boolean isRestricted(CommandNode<CommandSourceStack> node) {
/* 200 */         Predicate<CommandSourceStack> requirement = node.getRequirement();
/* 203 */         return !requirement.test(this.noPermissionSource);
/*     */       }
/*     */     };
/*     */   
/* 207 */   private final CommandDispatcher<CommandSourceStack> dispatcher = new CommandDispatcher();
/*     */   
/*     */   public enum CommandSelection {
/* 210 */     ALL(true, true),
/* 211 */     DEDICATED(false, true),
/* 212 */     INTEGRATED(true, false);
/*     */     
/*     */     private final boolean includeIntegrated;
/*     */     
/*     */     private final boolean includeDedicated;
/*     */     
/*     */     CommandSelection(boolean includeIntegrated, boolean includeDedicated) {
/* 219 */       this.includeIntegrated = includeIntegrated;
/* 220 */       this.includeDedicated = includeDedicated;
/*     */     }
/*     */   }
/*     */   
/*     */   public Commands(CommandSelection commandSelection, CommandBuildContext context) {
/* 225 */     AdvancementCommands.register(this.dispatcher);
/* 226 */     AttributeCommand.register(this.dispatcher, context);
/* 227 */     ExecuteCommand.register(this.dispatcher, context);
/* 228 */     BossBarCommands.register(this.dispatcher, context);
/* 229 */     ClearInventoryCommands.register(this.dispatcher, context);
/* 230 */     CloneCommands.register(this.dispatcher, context);
/* 231 */     DamageCommand.register(this.dispatcher, context);
/* 232 */     DataCommands.register(this.dispatcher);
/* 233 */     DataPackCommand.register(this.dispatcher, context);
/* 234 */     DebugCommand.register(this.dispatcher);
/* 235 */     DefaultGameModeCommands.register(this.dispatcher);
/* 236 */     DialogCommand.register(this.dispatcher, context);
/* 237 */     DifficultyCommand.register(this.dispatcher);
/* 238 */     EffectCommands.register(this.dispatcher, context);
/* 239 */     EmoteCommands.register(this.dispatcher);
/* 240 */     EnchantCommand.register(this.dispatcher, context);
/* 241 */     ExperienceCommand.register(this.dispatcher);
/* 242 */     FillCommand.register(this.dispatcher, context);
/* 243 */     FillBiomeCommand.register(this.dispatcher, context);
/* 244 */     ForceLoadCommand.register(this.dispatcher);
/* 245 */     FunctionCommand.register(this.dispatcher);
/* 246 */     GameModeCommand.register(this.dispatcher);
/* 247 */     GameRuleCommand.register(this.dispatcher, context);
/* 248 */     GiveCommand.register(this.dispatcher, context);
/* 249 */     HelpCommand.register(this.dispatcher);
/* 250 */     ItemCommands.register(this.dispatcher, context);
/* 251 */     KickCommand.register(this.dispatcher);
/* 252 */     KillCommand.register(this.dispatcher);
/* 253 */     ListPlayersCommand.register(this.dispatcher);
/* 254 */     LocateCommand.register(this.dispatcher, context);
/* 255 */     LootCommand.register(this.dispatcher, context);
/* 256 */     MsgCommand.register(this.dispatcher);
/* 257 */     SwingCommand.register(this.dispatcher);
/* 258 */     ParticleCommand.register(this.dispatcher, context);
/* 259 */     PlaceCommand.register(this.dispatcher);
/* 260 */     PlaySoundCommand.register(this.dispatcher);
/* 261 */     RandomCommand.register(this.dispatcher);
/* 262 */     ReloadCommand.register(this.dispatcher);
/* 263 */     RecipeCommand.register(this.dispatcher);
/* 264 */     FetchProfileCommand.register(this.dispatcher);
/* 265 */     ReturnCommand.register(this.dispatcher);
/* 266 */     RideCommand.register(this.dispatcher);
/* 267 */     RotateCommand.register(this.dispatcher);
/* 268 */     SayCommand.register(this.dispatcher);
/* 269 */     ScheduleCommand.register(this.dispatcher);
/* 270 */     ScoreboardCommand.register(this.dispatcher, context);
/* 271 */     SeedCommand.register(this.dispatcher, (commandSelection != CommandSelection.INTEGRATED));
/* 272 */     VersionCommand.register(this.dispatcher, (commandSelection != CommandSelection.INTEGRATED));
/* 273 */     SetBlockCommand.register(this.dispatcher, context);
/* 274 */     SetSpawnCommand.register(this.dispatcher);
/* 275 */     SetWorldSpawnCommand.register(this.dispatcher);
/* 276 */     SpectateCommand.register(this.dispatcher);
/* 277 */     SpreadPlayersCommand.register(this.dispatcher);
/* 278 */     StopSoundCommand.register(this.dispatcher);
/* 279 */     StopwatchCommand.register(this.dispatcher);
/* 280 */     SummonCommand.register(this.dispatcher, context);
/* 281 */     TagCommand.register(this.dispatcher);
/* 282 */     TeamCommand.register(this.dispatcher, context);
/* 283 */     TeamMsgCommand.register(this.dispatcher);
/* 284 */     TeleportCommand.register(this.dispatcher);
/* 285 */     TellRawCommand.register(this.dispatcher, context);
/* 286 */     TestCommand.register(this.dispatcher, context);
/* 287 */     TickCommand.register(this.dispatcher);
/* 288 */     TimeCommand.register(this.dispatcher, context);
/* 289 */     TitleCommand.register(this.dispatcher, context);
/* 290 */     TriggerCommand.register(this.dispatcher);
/* 291 */     WaypointCommand.register(this.dispatcher, context);
/* 292 */     WeatherCommand.register(this.dispatcher);
/* 293 */     WorldBorderCommand.register(this.dispatcher);
/* 296 */     if (JvmProfiler.INSTANCE.isAvailable())
/* 297 */       JfrCommand.register(this.dispatcher); 
/* 300 */     if (SharedConstants.DEBUG_CHASE_COMMAND)
/* 302 */       ChaseCommand.register(this.dispatcher); 
/* 305 */     if (SharedConstants.DEBUG_DEV_COMMANDS || SharedConstants.IS_RUNNING_IN_IDE) {
/* 307 */       RaidCommand.register(this.dispatcher, context);
/* 308 */       DebugPathCommand.register(this.dispatcher);
/* 309 */       DebugMobSpawningCommand.register(this.dispatcher);
/* 310 */       WardenSpawnTrackerCommand.register(this.dispatcher);
/* 311 */       SpawnArmorTrimsCommand.register(this.dispatcher);
/* 312 */       ServerPackCommand.register(this.dispatcher);
/* 313 */       if (commandSelection.includeDedicated)
/* 314 */         DebugConfigCommand.register(this.dispatcher, context); 
/*     */     } 
/* 318 */     if (commandSelection.includeDedicated) {
/* 319 */       BanIpCommands.register(this.dispatcher);
/* 320 */       BanListCommands.register(this.dispatcher);
/* 321 */       BanPlayerCommands.register(this.dispatcher);
/* 322 */       DeOpCommands.register(this.dispatcher);
/* 323 */       OpCommand.register(this.dispatcher);
/* 324 */       PardonCommand.register(this.dispatcher);
/* 325 */       PardonIpCommand.register(this.dispatcher);
/* 326 */       PerfCommand.register(this.dispatcher);
/* 327 */       SaveAllCommand.register(this.dispatcher);
/* 328 */       SaveOffCommand.register(this.dispatcher);
/* 329 */       SaveOnCommand.register(this.dispatcher);
/* 330 */       SetPlayerIdleTimeoutCommand.register(this.dispatcher);
/* 331 */       StopCommand.register(this.dispatcher);
/* 332 */       TransferCommand.register(this.dispatcher);
/* 333 */       WhitelistCommand.register(this.dispatcher);
/*     */     } 
/* 336 */     if (commandSelection.includeIntegrated) {
/* 337 */       PublishCommand.register(this.dispatcher);
/* 338 */       UnpublishCommand.register(this.dispatcher);
/*     */     } 
/* 341 */     this.dispatcher.setConsumer(ExecutionCommandSource.resultConsumer());
/*     */   }
/*     */   
/*     */   public static <S> ParseResults<S> mapSource(ParseResults<S> parse, UnaryOperator<S> sourceOperator) {
/* 345 */     CommandContextBuilder<S> context = parse.getContext();
/* 346 */     CommandContextBuilder<S> source = context.withSource(sourceOperator.apply((S)context.getSource()));
/* 347 */     return new ParseResults(source, parse.getReader(), parse.getExceptions());
/*     */   }
/*     */   
/*     */   public void performPrefixedCommand(CommandSourceStack sender, String command) {
/* 351 */     command = trimOptionalPrefix(command);
/* 352 */     performCommand(this.dispatcher.parse(command, sender), command);
/*     */   }
/*     */   
/*     */   public static String trimOptionalPrefix(String command) {
/* 356 */     return command.startsWith("/") ? command.substring(1) : command;
/*     */   }
/*     */   
/*     */   public void performCommand(ParseResults<CommandSourceStack> command, String commandString) {
/* 360 */     CommandSourceStack sender = (CommandSourceStack)command.getContext().getSource();
/* 361 */     Profiler.get().push(() -> "/" + commandString);
/* 362 */     ContextChain<CommandSourceStack> commandChain = finishParsing(command, commandString, sender);
/*     */     try {
/* 365 */       if (commandChain != null)
/* 366 */         executeCommandInContext(sender, executionContext -> ExecutionContext.queueInitialCommandExecution(executionContext, commandString, commandChain, sender, CommandResultCallback.EMPTY)); 
/* 368 */     } catch (Exception e) {
/* 369 */       MutableComponent hover = Component.literal((e.getMessage() == null) ? e.getClass().getName() : e.getMessage());
/* 370 */       if (LOGGER.isDebugEnabled()) {
/* 371 */         LOGGER.error("Command exception: /{}", commandString, e);
/* 372 */         StackTraceElement[] stackTrace = e.getStackTrace();
/* 373 */         for (int i = 0; i < Math.min(stackTrace.length, 3); i++)
/* 374 */           hover.append("\n\n")
/* 375 */             .append(stackTrace[i].getMethodName())
/* 376 */             .append("\n ")
/* 377 */             .append(stackTrace[i].getFileName())
/* 378 */             .append(":")
/* 379 */             .append(String.valueOf(stackTrace[i].getLineNumber())); 
/*     */       } 
/* 382 */       sender.sendFailure((Component)Component.translatable("command.failed").withStyle(s -> s.withHoverEvent((HoverEvent)new HoverEvent.ShowText((Component)hover))));
/* 383 */       if (SharedConstants.DEBUG_VERBOSE_COMMAND_ERRORS || SharedConstants.IS_RUNNING_IN_IDE) {
/* 384 */         sender.sendFailure((Component)Component.literal(Util.describeError(e)));
/* 385 */         LOGGER.error("'/{}' threw an exception", commandString, e);
/*     */       } 
/*     */     } finally {
/* 388 */       Profiler.get().pop();
/*     */     } 
/*     */   }
/*     */   
/*     */   private static ContextChain<CommandSourceStack> finishParsing(ParseResults<CommandSourceStack> command, String commandString, CommandSourceStack sender) {
/*     */     try {
/* 395 */       validateParseResults(command);
/* 397 */       return (ContextChain<CommandSourceStack>)ContextChain.tryFlatten(command.getContext().build(commandString))
/* 398 */         .orElseThrow(() -> CommandSyntaxException.BUILT_IN_EXCEPTIONS.dispatcherUnknownCommand().createWithContext(command.getReader()));
/* 399 */     } catch (CommandSyntaxException e) {
/* 400 */       sender.sendFailure(ComponentUtils.fromMessage(e.getRawMessage()));
/* 402 */       if (e.getInput() != null && e.getCursor() >= 0) {
/* 403 */         int cursor = Math.min(e.getInput().length(), e.getCursor());
/* 404 */         MutableComponent context = Component.empty().withStyle(ChatFormatting.GRAY).withStyle(s -> s.withClickEvent((ClickEvent)new ClickEvent.SuggestCommand("/" + commandString)));
/* 407 */         if (cursor > 10)
/* 408 */           context.append(CommonComponents.ELLIPSIS); 
/* 410 */         context.append(e.getInput().substring(Math.max(0, cursor - 10), cursor));
/* 411 */         if (cursor < e.getInput().length()) {
/* 412 */           MutableComponent mutableComponent = Component.literal(e.getInput().substring(cursor)).withStyle(new ChatFormatting[] { ChatFormatting.RED, ChatFormatting.UNDERLINE });
/* 413 */           context.append((Component)mutableComponent);
/*     */         } 
/* 415 */         context.append((Component)Component.translatable("command.context.here").withStyle(new ChatFormatting[] { ChatFormatting.RED, ChatFormatting.ITALIC }));
/* 416 */         sender.sendFailure((Component)context);
/*     */       } 
/* 418 */       return null;
/*     */     } 
/*     */   }
/*     */   
/*     */   public static void executeCommandInContext(CommandSourceStack context, Consumer<ExecutionContext<CommandSourceStack>> config) {
/* 423 */     ExecutionContext<CommandSourceStack> currentContext = CURRENT_EXECUTION_CONTEXT.get();
/* 424 */     boolean isTopContext = (currentContext == null);
/* 426 */     if (isTopContext) {
/* 428 */       GameRules gameRules = context.getLevel().getGameRules();
/* 429 */       int chainLimit = Math.max(1, ((Integer)gameRules.get(GameRules.MAX_COMMAND_SEQUENCE_LENGTH)).intValue());
/* 430 */       int forkLimit = ((Integer)gameRules.get(GameRules.MAX_COMMAND_FORKS)).intValue();
/*     */       try {
/* 431 */         ExecutionContext<CommandSourceStack> executionContext = new ExecutionContext(chainLimit, forkLimit, Profiler.get());
/*     */         try {
/* 432 */           CURRENT_EXECUTION_CONTEXT.set(executionContext);
/* 433 */           config.accept(executionContext);
/* 434 */           executionContext.runCommandQueue();
/* 435 */           executionContext.close();
/*     */         } catch (Throwable throwable) {
/*     */           try {
/*     */             executionContext.close();
/*     */           } catch (Throwable throwable1) {
/*     */             throwable.addSuppressed(throwable1);
/*     */           } 
/*     */           throw throwable;
/*     */         } 
/*     */       } finally {
/* 436 */         CURRENT_EXECUTION_CONTEXT.set(null);
/*     */       } 
/*     */     } else {
/* 439 */       config.accept(currentContext);
/*     */     } 
/*     */   }
/*     */   
/*     */   public void sendCommands(ServerPlayer player) {
/* 444 */     Map<CommandNode<CommandSourceStack>, CommandNode<CommandSourceStack>> playerCommands = new HashMap<>();
/* 445 */     RootCommandNode<CommandSourceStack> root = new RootCommandNode();
/* 446 */     playerCommands.put(this.dispatcher.getRoot(), root);
/* 447 */     fillUsableCommands((CommandNode<CommandSourceStack>)this.dispatcher.getRoot(), (CommandNode<CommandSourceStack>)root, player.createCommandSourceStack(), playerCommands);
/* 448 */     player.connection.send((Packet)new ClientboundCommandsPacket(root, COMMAND_NODE_INSPECTOR));
/*     */   }
/*     */   
/*     */   private static <S> void fillUsableCommands(CommandNode<S> source, CommandNode<S> target, S commandFilter, Map<CommandNode<S>, CommandNode<S>> converted) {
/* 452 */     for (CommandNode<S> child : (Iterable<CommandNode<S>>)source.getChildren()) {
/* 453 */       if (child.canUse(commandFilter)) {
/* 454 */         ArgumentBuilder<S, ?> builder = child.createBuilder();
/* 455 */         if (builder.getRedirect() != null)
/* 456 */           builder.redirect(converted.get(builder.getRedirect())); 
/* 458 */         CommandNode<S> node = builder.build();
/* 459 */         converted.put(child, node);
/* 460 */         target.addChild(node);
/* 461 */         if (!child.getChildren().isEmpty())
/* 462 */           fillUsableCommands(child, node, commandFilter, converted); 
/*     */       } 
/*     */     } 
/*     */   }
/*     */   
/*     */   public static LiteralArgumentBuilder<CommandSourceStack> literal(String literal) {
/* 469 */     return LiteralArgumentBuilder.literal(literal);
/*     */   }
/*     */   
/*     */   public static <T> RequiredArgumentBuilder<CommandSourceStack, T> argument(String name, ArgumentType<T> type) {
/* 473 */     return RequiredArgumentBuilder.argument(name, type);
/*     */   }
/*     */   
/*     */   public static Predicate<String> createValidator(ParseFunction parser) {
/* 477 */     return value -> {
/*     */         try {
/*     */           parser.parse(new StringReader(value));
/*     */           return true;
/* 481 */         } catch (CommandSyntaxException ignored) {
/*     */           return false;
/*     */         } 
/*     */       };
/*     */   }
/*     */   
/*     */   public CommandDispatcher<CommandSourceStack> getDispatcher() {
/* 488 */     return this.dispatcher;
/*     */   }
/*     */   
/*     */   public static <S> void validateParseResults(ParseResults<S> command) throws CommandSyntaxException {
/* 492 */     CommandSyntaxException parseException = getParseException(command);
/* 493 */     if (parseException != null)
/* 494 */       throw parseException; 
/*     */   }
/*     */   
/*     */   public static <S> CommandSyntaxException getParseException(ParseResults<S> parse) {
/* 499 */     if (!parse.getReader().canRead())
/* 500 */       return null; 
/* 501 */     if (parse.getExceptions().size() == 1)
/* 502 */       return parse.getExceptions().values().iterator().next(); 
/* 503 */     if (parse.getContext().getRange().isEmpty())
/* 504 */       return CommandSyntaxException.BUILT_IN_EXCEPTIONS.dispatcherUnknownCommand().createWithContext(parse.getReader()); 
/* 506 */     return CommandSyntaxException.BUILT_IN_EXCEPTIONS.dispatcherUnknownArgument().createWithContext(parse.getReader());
/*     */   }
/*     */   
/*     */   public static CommandBuildContext createValidationContext(final HolderLookup.Provider registries) {
/* 510 */     return new CommandBuildContext() {
/*     */         public FeatureFlagSet enabledFeatures() {
/* 513 */           return FeatureFlags.REGISTRY.allFlags();
/*     */         }
/*     */         
/*     */         public Stream<ResourceKey<? extends Registry<?>>> listRegistryKeys() {
/* 518 */           return registries.listRegistryKeys();
/*     */         }
/*     */         
/*     */         public <T> Optional<HolderLookup.RegistryLookup<T>> lookup(ResourceKey<? extends Registry<? extends T>> key) {
/* 523 */           return registries.lookup(key).map(this::createLookup);
/*     */         }
/*     */         
/*     */         private <T> HolderLookup.RegistryLookup.Delegate<T> createLookup(final HolderLookup.RegistryLookup<T> original) {
/* 528 */           return new HolderLookup.RegistryLookup.Delegate<T>(this) {
/*     */               public HolderLookup.RegistryLookup<T> parent() {
/* 531 */                 return original;
/*     */               }
/*     */               
/*     */               public Optional<HolderSet.Named<T>> get(TagKey<T> id) {
/* 536 */                 return Optional.of(getOrThrow(id));
/*     */               }
/*     */               
/*     */               public HolderSet.Named<T> getOrThrow(TagKey<T> id) {
/* 541 */                 Optional<HolderSet.Named<T>> tag = parent().get(id);
/* 542 */                 return tag.orElseGet(() -> HolderSet.emptyNamed((HolderOwner)parent(), id));
/*     */               }
/*     */             };
/*     */         }
/*     */       };
/*     */   }
/*     */   
/*     */   public static void validate() {
/* 550 */     CommandBuildContext context = createValidationContext(VanillaRegistries.createLookup());
/* 551 */     CommandDispatcher<CommandSourceStack> dispatcher = (new Commands(CommandSelection.ALL, context)).getDispatcher();
/* 552 */     RootCommandNode<CommandSourceStack> root = dispatcher.getRoot();
/* 554 */     dispatcher.findAmbiguities((parent, child, sibling, ambiguities) -> LOGGER.warn("Ambiguity between arguments {} and {} with inputs: {}", new Object[] { dispatcher.getPath(child), dispatcher.getPath(sibling), ambiguities }));
/* 558 */     Set<ArgumentType<?>> usedArgumentTypes = ArgumentUtils.findUsedArgumentTypes((CommandNode)root);
/* 559 */     Set<ArgumentType<?>> unregisteredTypes = (Set<ArgumentType<?>>)usedArgumentTypes.stream().filter(arg -> !ArgumentTypeInfos.isClassRecognized(arg.getClass())).collect(Collectors.toSet());
/* 560 */     if (!unregisteredTypes.isEmpty()) {
/* 561 */       LOGGER.warn("Missing type registration for following arguments:\n {}", unregisteredTypes.stream().map(arg -> "\t" + String.valueOf(arg)).collect(Collectors.joining(",\n")));
/* 562 */       throw new IllegalStateException("Unregistered argument types");
/*     */     } 
/*     */   }
/*     */   
/*     */   public static <T extends net.minecraft.server.permissions.PermissionSetSupplier> PermissionProviderCheck<T> hasPermission(PermissionCheck permission) {
/* 567 */     return new PermissionProviderCheck(permission);
/*     */   }
/*     */   
/*     */   public static CommandSourceStack createCompilationContext(PermissionSet compilationPermissions) {
/* 573 */     return new CommandSourceStack(CommandSource.NULL, Vec3.ZERO, Vec2.ZERO, null, compilationPermissions, "", CommonComponents.EMPTY, null, null);
/*     */   }
/*     */   
/*     */   @FunctionalInterface
/*     */   public static interface ParseFunction {
/*     */     void parse(StringReader param1StringReader) throws CommandSyntaxException;
/*     */   }
/*     */ }


/* Location:              C:\Users\karol\AppData\Roaming\ModrinthApp\meta\versions\26.2-0.19.5\26.2-0.19.5.jar!\net\minecraft\commands\Commands.class
 * Java compiler version: 25 (69.0)
 * JD-Core Version:       1.1.3
 */