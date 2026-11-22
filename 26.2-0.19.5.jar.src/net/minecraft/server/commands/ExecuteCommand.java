/*      */ package net.minecraft.server.commands;
/*      */ 
/*      */ import com.google.common.annotations.VisibleForTesting;
/*      */ import com.google.common.collect.Lists;
/*      */ import com.mojang.brigadier.Command;
/*      */ import com.mojang.brigadier.CommandDispatcher;
/*      */ import com.mojang.brigadier.Message;
/*      */ import com.mojang.brigadier.RedirectModifier;
/*      */ import com.mojang.brigadier.arguments.ArgumentType;
/*      */ import com.mojang.brigadier.arguments.DoubleArgumentType;
/*      */ import com.mojang.brigadier.builder.ArgumentBuilder;
/*      */ import com.mojang.brigadier.builder.LiteralArgumentBuilder;
/*      */ import com.mojang.brigadier.builder.RequiredArgumentBuilder;
/*      */ import com.mojang.brigadier.context.CommandContext;
/*      */ import com.mojang.brigadier.context.ContextChain;
/*      */ import com.mojang.brigadier.exceptions.CommandSyntaxException;
/*      */ import com.mojang.brigadier.exceptions.Dynamic2CommandExceptionType;
/*      */ import com.mojang.brigadier.exceptions.DynamicCommandExceptionType;
/*      */ import com.mojang.brigadier.exceptions.SimpleCommandExceptionType;
/*      */ import com.mojang.brigadier.tree.CommandNode;
/*      */ import com.mojang.brigadier.tree.LiteralCommandNode;
/*      */ import com.mojang.logging.LogUtils;
/*      */ import it.unimi.dsi.fastutil.ints.IntList;
/*      */ import java.util.ArrayList;
/*      */ import java.util.Collection;
/*      */ import java.util.Collections;
/*      */ import java.util.Iterator;
/*      */ import java.util.List;
/*      */ import java.util.Objects;
/*      */ import java.util.Optional;
/*      */ import java.util.OptionalInt;
/*      */ import java.util.function.Function;
/*      */ import java.util.function.IntFunction;
/*      */ import java.util.function.IntPredicate;
/*      */ import java.util.function.Predicate;
/*      */ import java.util.stream.Stream;
/*      */ import net.minecraft.advancements.predicates.MinMaxBounds;
/*      */ import net.minecraft.commands.CommandBuildContext;
/*      */ import net.minecraft.commands.CommandResultCallback;
/*      */ import net.minecraft.commands.CommandSourceStack;
/*      */ import net.minecraft.commands.Commands;
/*      */ import net.minecraft.commands.ExecutionCommandSource;
/*      */ import net.minecraft.commands.FunctionInstantiationException;
/*      */ import net.minecraft.commands.arguments.DimensionArgument;
/*      */ import net.minecraft.commands.arguments.EntityAnchorArgument;
/*      */ import net.minecraft.commands.arguments.EntityArgument;
/*      */ import net.minecraft.commands.arguments.HeightmapTypeArgument;
/*      */ import net.minecraft.commands.arguments.IdentifierArgument;
/*      */ import net.minecraft.commands.arguments.NbtPathArgument;
/*      */ import net.minecraft.commands.arguments.ObjectiveArgument;
/*      */ import net.minecraft.commands.arguments.RangeArgument;
/*      */ import net.minecraft.commands.arguments.ResourceArgument;
/*      */ import net.minecraft.commands.arguments.ResourceOrIdArgument;
/*      */ import net.minecraft.commands.arguments.ResourceOrTagArgument;
/*      */ import net.minecraft.commands.arguments.ScoreHolderArgument;
/*      */ import net.minecraft.commands.arguments.SlotsArgument;
/*      */ import net.minecraft.commands.arguments.blocks.BlockPredicateArgument;
/*      */ import net.minecraft.commands.arguments.coordinates.BlockPosArgument;
/*      */ import net.minecraft.commands.arguments.coordinates.RotationArgument;
/*      */ import net.minecraft.commands.arguments.coordinates.SwizzleArgument;
/*      */ import net.minecraft.commands.arguments.coordinates.Vec3Argument;
/*      */ import net.minecraft.commands.arguments.item.FunctionArgument;
/*      */ import net.minecraft.commands.arguments.item.ItemPredicateArgument;
/*      */ import net.minecraft.commands.execution.ChainModifiers;
/*      */ import net.minecraft.commands.execution.CustomModifierExecutor;
/*      */ import net.minecraft.commands.execution.EntryAction;
/*      */ import net.minecraft.commands.execution.ExecutionControl;
/*      */ import net.minecraft.commands.execution.tasks.BuildContexts;
/*      */ import net.minecraft.commands.execution.tasks.CallFunction;
/*      */ import net.minecraft.commands.execution.tasks.FallthroughTask;
/*      */ import net.minecraft.commands.execution.tasks.IsolatedCall;
/*      */ import net.minecraft.commands.functions.CommandFunction;
/*      */ import net.minecraft.commands.functions.InstantiatedFunction;
/*      */ import net.minecraft.commands.synchronization.SuggestionProviders;
/*      */ import net.minecraft.core.BlockPos;
/*      */ import net.minecraft.core.Holder;
/*      */ import net.minecraft.core.HolderLookup;
/*      */ import net.minecraft.core.RegistryAccess;
/*      */ import net.minecraft.core.SectionPos;
/*      */ import net.minecraft.core.Vec3i;
/*      */ import net.minecraft.core.registries.Registries;
/*      */ import net.minecraft.nbt.ByteTag;
/*      */ import net.minecraft.nbt.CompoundTag;
/*      */ import net.minecraft.nbt.DoubleTag;
/*      */ import net.minecraft.nbt.FloatTag;
/*      */ import net.minecraft.nbt.IntTag;
/*      */ import net.minecraft.nbt.LongTag;
/*      */ import net.minecraft.nbt.ShortTag;
/*      */ import net.minecraft.nbt.Tag;
/*      */ import net.minecraft.network.chat.Component;
/*      */ import net.minecraft.resources.Identifier;
/*      */ import net.minecraft.server.ServerScoreboard;
/*      */ import net.minecraft.server.bossevents.CustomBossEvent;
/*      */ import net.minecraft.server.commands.data.DataAccessor;
/*      */ import net.minecraft.server.commands.data.DataCommands;
/*      */ import net.minecraft.server.level.FullChunkStatus;
/*      */ import net.minecraft.server.level.ServerLevel;
/*      */ import net.minecraft.util.Mth;
/*      */ import net.minecraft.util.ProblemReporter;
/*      */ import net.minecraft.world.Container;
/*      */ import net.minecraft.world.Stopwatch;
/*      */ import net.minecraft.world.Stopwatches;
/*      */ import net.minecraft.world.entity.Attackable;
/*      */ import net.minecraft.world.entity.Entity;
/*      */ import net.minecraft.world.entity.EntityType;
/*      */ import net.minecraft.world.entity.Leashable;
/*      */ import net.minecraft.world.entity.LivingEntity;
/*      */ import net.minecraft.world.entity.OwnableEntity;
/*      */ import net.minecraft.world.entity.SlotAccess;
/*      */ import net.minecraft.world.entity.SlotProvider;
/*      */ import net.minecraft.world.entity.Targeting;
/*      */ import net.minecraft.world.entity.TraceableEntity;
/*      */ import net.minecraft.world.inventory.SlotRange;
/*      */ import net.minecraft.world.item.ItemStack;
/*      */ import net.minecraft.world.level.ChunkPos;
/*      */ import net.minecraft.world.level.LevelReader;
/*      */ import net.minecraft.world.level.block.Blocks;
/*      */ import net.minecraft.world.level.block.entity.BlockEntity;
/*      */ import net.minecraft.world.level.block.state.BlockState;
/*      */ import net.minecraft.world.level.block.state.pattern.BlockInWorld;
/*      */ import net.minecraft.world.level.chunk.LevelChunk;
/*      */ import net.minecraft.world.level.levelgen.structure.BoundingBox;
/*      */ import net.minecraft.world.level.storage.TagValueOutput;
/*      */ import net.minecraft.world.level.storage.ValueOutput;
/*      */ import net.minecraft.world.level.storage.loot.LootContext;
/*      */ import net.minecraft.world.level.storage.loot.LootParams;
/*      */ import net.minecraft.world.level.storage.loot.parameters.LootContextParamSets;
/*      */ import net.minecraft.world.level.storage.loot.parameters.LootContextParams;
/*      */ import net.minecraft.world.level.storage.loot.predicates.LootItemCondition;
/*      */ import net.minecraft.world.phys.Vec3;
/*      */ import net.minecraft.world.scores.Objective;
/*      */ import net.minecraft.world.scores.ReadOnlyScoreInfo;
/*      */ import net.minecraft.world.scores.ScoreAccess;
/*      */ import net.minecraft.world.scores.ScoreHolder;
/*      */ import net.minecraft.world.scores.Scoreboard;
/*      */ import org.slf4j.Logger;
/*      */ 
/*      */ public class ExecuteCommand {
/*  164 */   private static final Logger LOGGER = LogUtils.getLogger();
/*      */   
/*      */   private static final int MAX_TEST_AREA = 32768;
/*      */   
/*      */   private static final Dynamic2CommandExceptionType ERROR_AREA_TOO_LARGE;
/*      */   
/*      */   static {
/*  167 */     ERROR_AREA_TOO_LARGE = new Dynamic2CommandExceptionType((max, count) -> Component.translatableEscape("commands.execute.blocks.toobig", new Object[] { max, count }));
/*      */   }
/*      */   
/*  169 */   private static final SimpleCommandExceptionType ERROR_CONDITIONAL_FAILED = new SimpleCommandExceptionType((Message)Component.translatable("commands.execute.conditional.fail"));
/*      */   
/*      */   private static final DynamicCommandExceptionType ERROR_CONDITIONAL_FAILED_COUNT;
/*      */   
/*      */   @VisibleForTesting
/*      */   public static final Dynamic2CommandExceptionType ERROR_FUNCTION_CONDITION_INSTANTATION_FAILURE;
/*      */   
/*      */   static {
/*  170 */     ERROR_CONDITIONAL_FAILED_COUNT = new DynamicCommandExceptionType(count -> Component.translatableEscape("commands.execute.conditional.fail_count", new Object[] { count }));
/*  173 */     ERROR_FUNCTION_CONDITION_INSTANTATION_FAILURE = new Dynamic2CommandExceptionType((id, reason) -> Component.translatableEscape("commands.execute.function.instantiationFailure", new Object[] { id, reason }));
/*      */   }
/*      */   
/*      */   public static void register(CommandDispatcher<CommandSourceStack> dispatcher, CommandBuildContext context) {
/*  186 */     LiteralCommandNode<CommandSourceStack> execute = dispatcher.register((LiteralArgumentBuilder)Commands.literal("execute")
/*  187 */         .requires((Predicate)Commands.hasPermission(Commands.LEVEL_GAMEMASTERS)));
/*  189 */     dispatcher.register(
/*  190 */         (LiteralArgumentBuilder)((LiteralArgumentBuilder)((LiteralArgumentBuilder)((LiteralArgumentBuilder)((LiteralArgumentBuilder)((LiteralArgumentBuilder)((LiteralArgumentBuilder)((LiteralArgumentBuilder)((LiteralArgumentBuilder)((LiteralArgumentBuilder)((LiteralArgumentBuilder)((LiteralArgumentBuilder)((LiteralArgumentBuilder)((LiteralArgumentBuilder)((LiteralArgumentBuilder)Commands.literal("execute")
/*  191 */         .requires((Predicate)Commands.hasPermission(Commands.LEVEL_GAMEMASTERS)))
/*  192 */         .then(
/*  193 */           Commands.literal("run")
/*  194 */           .redirect((CommandNode)dispatcher.getRoot())))
/*      */         
/*  196 */         .then(
/*  197 */           addConditionals((CommandNode<CommandSourceStack>)execute, Commands.literal("if"), true, context)))
/*      */         
/*  199 */         .then(
/*  200 */           addConditionals((CommandNode<CommandSourceStack>)execute, Commands.literal("unless"), false, context)))
/*      */         
/*  202 */         .then(
/*  203 */           Commands.literal("as")
/*  204 */           .then(
/*  205 */             Commands.argument("targets", (ArgumentType)EntityArgument.entities())
/*  206 */             .fork((CommandNode)execute, c -> {
/*      */                 List<CommandSourceStack> result = Lists.newArrayList();
/*      */                 for (Entity entity : EntityArgument.getOptionalEntities(c, "targets"))
/*      */                   result.add(((CommandSourceStack)c.getSource()).withEntity(entity)); 
/*      */                 return result;
/*  215 */               })))).then(
/*  216 */           Commands.literal("at")
/*  217 */           .then(
/*  218 */             Commands.argument("targets", (ArgumentType)EntityArgument.entities())
/*  219 */             .fork((CommandNode)execute, c -> {
/*      */                 List<CommandSourceStack> result = Lists.newArrayList();
/*      */                 for (Entity entity : EntityArgument.getOptionalEntities(c, "targets"))
/*      */                   result.add(((CommandSourceStack)c.getSource()).withLevel((ServerLevel)entity.level()).withPosition(entity.position()).withRotation(entity.getRotationVector())); 
/*      */                 return result;
/*  228 */               })))).then((
/*  229 */           (LiteralArgumentBuilder)Commands.literal("store")
/*  230 */           .then(wrapStores(execute, Commands.literal("result"), true)))
/*  231 */           .then(wrapStores(execute, Commands.literal("success"), false))))
/*      */         
/*  233 */         .then((
/*  234 */           (LiteralArgumentBuilder)((LiteralArgumentBuilder)Commands.literal("positioned")
/*  235 */           .then(
/*  236 */             Commands.argument("pos", (ArgumentType)Vec3Argument.vec3())
/*  237 */             .redirect((CommandNode)execute, c -> ((CommandSourceStack)c.getSource()).withPosition(Vec3Argument.getVec3(c, "pos")).withAnchor(EntityAnchorArgument.Anchor.FEET))))
/*      */           
/*  239 */           .then(
/*  240 */             Commands.literal("as")
/*  241 */             .then(
/*  242 */               Commands.argument("targets", (ArgumentType)EntityArgument.entities())
/*  243 */               .fork((CommandNode)execute, c -> {
/*      */                   List<CommandSourceStack> result = Lists.newArrayList();
/*      */                   for (Entity entity : EntityArgument.getOptionalEntities(c, "targets"))
/*      */                     result.add(((CommandSourceStack)c.getSource()).withPosition(entity.position())); 
/*      */                   return result;
/*  252 */                 })))).then(
/*  253 */             Commands.literal("over")
/*  254 */             .then(
/*  255 */               Commands.argument("heightmap", (ArgumentType)HeightmapTypeArgument.heightmap())
/*  256 */               .redirect((CommandNode)execute, c -> {
/*      */                   Vec3 position = ((CommandSourceStack)c.getSource()).getPosition();
/*      */                   ServerLevel level = ((CommandSourceStack)c.getSource()).getLevel();
/*      */                   double x = position.x();
/*      */                   double z = position.z();
/*      */                   if (!level.hasChunk(SectionPos.blockToSectionCoord(x), SectionPos.blockToSectionCoord(z)))
/*      */                     throw BlockPosArgument.ERROR_NOT_LOADED.create(); 
/*      */                   int height = level.getHeight(HeightmapTypeArgument.getHeightmap(c, "heightmap"), Mth.floor(x), Mth.floor(z));
/*      */                   return ((CommandSourceStack)c.getSource()).withPosition(new Vec3(x, height, z));
/*  273 */                 }))))).then((
/*  274 */           (LiteralArgumentBuilder)Commands.literal("rotated")
/*  275 */           .then(
/*  276 */             Commands.argument("rot", (ArgumentType)RotationArgument.rotation())
/*  277 */             .redirect((CommandNode)execute, c -> ((CommandSourceStack)c.getSource()).withRotation(RotationArgument.getRotation(c, "rot").getRotation((CommandSourceStack)c.getSource())))))
/*      */           
/*  279 */           .then(
/*  280 */             Commands.literal("as")
/*  281 */             .then(
/*  282 */               Commands.argument("targets", (ArgumentType)EntityArgument.entities())
/*  283 */               .fork((CommandNode)execute, c -> {
/*      */                   List<CommandSourceStack> result = Lists.newArrayList();
/*      */                   for (Entity entity : EntityArgument.getOptionalEntities(c, "targets"))
/*      */                     result.add(((CommandSourceStack)c.getSource()).withRotation(entity.getRotationVector())); 
/*      */                   return result;
/*  293 */                 }))))).then((
/*  294 */           (LiteralArgumentBuilder)Commands.literal("facing")
/*  295 */           .then(
/*  296 */             Commands.literal("entity")
/*  297 */             .then(
/*  298 */               Commands.argument("targets", (ArgumentType)EntityArgument.entities())
/*  299 */               .then(
/*  300 */                 Commands.argument("anchor", (ArgumentType)EntityAnchorArgument.anchor())
/*  301 */                 .fork((CommandNode)execute, c -> {
/*      */                     List<CommandSourceStack> result = Lists.newArrayList();
/*      */                     EntityAnchorArgument.Anchor anchor = EntityAnchorArgument.getAnchor(c, "anchor");
/*      */                     for (Entity entity : EntityArgument.getOptionalEntities(c, "targets"))
/*      */                       result.add(((CommandSourceStack)c.getSource()).facing(entity, anchor)); 
/*      */                     return result;
/*  312 */                   }))))).then(
/*  313 */             Commands.argument("pos", (ArgumentType)Vec3Argument.vec3())
/*  314 */             .redirect((CommandNode)execute, c -> ((CommandSourceStack)c.getSource()).facing(Vec3Argument.getVec3(c, "pos"))))))
/*      */         
/*  317 */         .then(
/*  318 */           Commands.literal("align")
/*  319 */           .then(
/*  320 */             Commands.argument("axes", (ArgumentType)SwizzleArgument.swizzle())
/*  321 */             .redirect((CommandNode)execute, c -> ((CommandSourceStack)c.getSource()).withPosition(((CommandSourceStack)c.getSource()).getPosition().align(SwizzleArgument.getSwizzle(c, "axes")))))))
/*      */         
/*  324 */         .then(
/*  325 */           Commands.literal("anchored")
/*  326 */           .then(
/*  327 */             Commands.argument("anchor", (ArgumentType)EntityAnchorArgument.anchor())
/*  328 */             .redirect((CommandNode)execute, c -> ((CommandSourceStack)c.getSource()).withAnchor(EntityAnchorArgument.getAnchor(c, "anchor"))))))
/*      */         
/*  331 */         .then(
/*  332 */           Commands.literal("in")
/*  333 */           .then(
/*  334 */             Commands.argument("dimension", (ArgumentType)DimensionArgument.dimension())
/*  335 */             .redirect((CommandNode)execute, c -> ((CommandSourceStack)c.getSource()).withLevel(DimensionArgument.getDimension(c, "dimension"))))))
/*      */         
/*  338 */         .then(
/*  339 */           Commands.literal("summon")
/*  340 */           .then(
/*  341 */             Commands.argument("entity", (ArgumentType)ResourceArgument.resource(context, Registries.ENTITY_TYPE))
/*  342 */             .suggests(SuggestionProviders.cast(SuggestionProviders.SUMMONABLE_ENTITIES))
/*  343 */             .redirect((CommandNode)execute, c -> spawnEntityAndRedirect((CommandSourceStack)c.getSource(), ResourceArgument.getSummonableEntityType(c, "entity"))))))
/*      */         
/*  346 */         .then(
/*  347 */           (ArgumentBuilder)createRelationOperations((CommandNode<CommandSourceStack>)execute, Commands.literal("on"))));
/*      */   }
/*      */   
/*      */   private static ArgumentBuilder<CommandSourceStack, ?> wrapStores(LiteralCommandNode<CommandSourceStack> execute, LiteralArgumentBuilder<CommandSourceStack> literal, boolean storeResult) {
/*  353 */     literal.then(
/*  354 */         Commands.literal("score")
/*  355 */         .then(
/*  356 */           Commands.argument("targets", (ArgumentType)ScoreHolderArgument.scoreHolders())
/*  357 */           .suggests(ScoreHolderArgument.SUGGEST_SCORE_HOLDERS)
/*  358 */           .then(
/*  359 */             Commands.argument("objective", (ArgumentType)ObjectiveArgument.objective())
/*  360 */             .redirect((CommandNode)execute, c -> storeValue((CommandSourceStack)c.getSource(), ScoreHolderArgument.getNamesWithDefaultWildcard(c, "targets"), ObjectiveArgument.getObjective(c, "objective"), storeResult)))));
/*  365 */     literal.then(
/*  366 */         Commands.literal("bossbar")
/*  367 */         .then((
/*  368 */           (RequiredArgumentBuilder)Commands.argument("id", (ArgumentType)IdentifierArgument.id())
/*  369 */           .suggests(BossBarCommands.SUGGEST_BOSS_BAR)
/*  370 */           .then(
/*  371 */             Commands.literal("value")
/*  372 */             .redirect((CommandNode)execute, c -> storeValue((CommandSourceStack)c.getSource(), BossBarCommands.getBossBar(c), true, storeResult))))
/*      */           
/*  374 */           .then(
/*  375 */             Commands.literal("max")
/*  376 */             .redirect((CommandNode)execute, c -> storeValue((CommandSourceStack)c.getSource(), BossBarCommands.getBossBar(c), false, storeResult)))));
/*  381 */     for (Iterator<DataCommands.DataProvider> iterator = DataCommands.TARGET_PROVIDERS.iterator(); iterator.hasNext(); ) {
/*  381 */       DataCommands.DataProvider provider = iterator.next();
/*  382 */       provider.wrap((ArgumentBuilder)literal, p -> p.then(((RequiredArgumentBuilder)((RequiredArgumentBuilder)((RequiredArgumentBuilder)((RequiredArgumentBuilder)((RequiredArgumentBuilder)Commands.argument("path", (ArgumentType)NbtPathArgument.nbtPath()).then(Commands.literal("int").then(Commands.argument("scale", (ArgumentType)DoubleArgumentType.doubleArg()).redirect((CommandNode)execute, ())))).then(Commands.literal("float").then(Commands.argument("scale", (ArgumentType)DoubleArgumentType.doubleArg()).redirect((CommandNode)execute, ())))).then(Commands.literal("short").then(Commands.argument("scale", (ArgumentType)DoubleArgumentType.doubleArg()).redirect((CommandNode)execute, ())))).then(Commands.literal("long").then(Commands.argument("scale", (ArgumentType)DoubleArgumentType.doubleArg()).redirect((CommandNode)execute, ())))).then(Commands.literal("double").then(Commands.argument("scale", (ArgumentType)DoubleArgumentType.doubleArg()).redirect((CommandNode)execute, ())))).then(Commands.literal("byte").then(Commands.argument("scale", (ArgumentType)DoubleArgumentType.doubleArg()).redirect((CommandNode)execute, ())))));
/*      */     } 
/*  436 */     return (ArgumentBuilder)literal;
/*      */   }
/*      */   
/*      */   private static CommandSourceStack storeValue(CommandSourceStack source, Collection<ScoreHolder> names, Objective objective, boolean storeResult) {
/*  440 */     ServerScoreboard serverScoreboard = source.getServer().getScoreboard();
/*  442 */     return source.withCallback((success, result) -> {
/*      */           for (ScoreHolder name : names) {
/*      */             ScoreAccess score = scoreboard.getOrCreatePlayerScore(name, objective);
/*      */             int value = storeResult ? result : (success ? 1 : 0);
/*      */             score.set(value);
/*      */           } 
/*  442 */         }CommandResultCallback::chain);
/*      */   }
/*      */   
/*      */   private static CommandSourceStack storeValue(CommandSourceStack source, CustomBossEvent event, boolean storeIntoValue, boolean storeResult) {
/*  452 */     return source.withCallback((success, result) -> {
/*      */           int value = storeResult ? result : (success ? 1 : 0);
/*      */           if (storeIntoValue) {
/*      */             event.setValue(value);
/*      */           } else {
/*      */             event.setMax(value);
/*      */           } 
/*  452 */         }CommandResultCallback::chain);
/*      */   }
/*      */   
/*      */   private static CommandSourceStack storeData(CommandSourceStack source, DataAccessor accessor, NbtPathArgument.NbtPath path, IntFunction<Tag> constructor, boolean storeResult) {
/*  463 */     return source.withCallback((success, result) -> {
/*      */           try {
/*      */             CompoundTag data = accessor.getData();
/*      */             int value = storeResult ? result : (success ? 1 : 0);
/*      */             path.set((Tag)data, constructor.apply(value));
/*      */             accessor.setData(data);
/*  469 */           } catch (CommandSyntaxException commandSyntaxException) {}
/*      */         }CommandResultCallback::chain);
/*      */   }
/*      */   
/*      */   private static boolean isChunkLoaded(ServerLevel level, BlockPos pos) {
/*  475 */     ChunkPos chunkPos = ChunkPos.containing(pos);
/*  477 */     LevelChunk chunk = level.getChunkSource().getChunkNow(chunkPos.x(), chunkPos.z());
/*  478 */     if (chunk != null)
/*  479 */       return (chunk.getFullStatus() == FullChunkStatus.ENTITY_TICKING && level.areEntitiesLoaded(chunkPos.pack())); 
/*  481 */     return false;
/*      */   }
/*      */   
/*      */   private static ArgumentBuilder<CommandSourceStack, ?> addConditionals(CommandNode<CommandSourceStack> execute, LiteralArgumentBuilder<CommandSourceStack> parent, boolean expected, CommandBuildContext context) {
/*  485 */     ((LiteralArgumentBuilder)((LiteralArgumentBuilder)((LiteralArgumentBuilder)((LiteralArgumentBuilder)((LiteralArgumentBuilder)((LiteralArgumentBuilder)((LiteralArgumentBuilder)((LiteralArgumentBuilder)((LiteralArgumentBuilder)((LiteralArgumentBuilder)parent
/*  486 */       .then(
/*  487 */         Commands.literal("block")
/*  488 */         .then(
/*  489 */           Commands.argument("pos", (ArgumentType)BlockPosArgument.blockPos())
/*  490 */           .then(
/*  491 */             addConditional(execute, (ArgumentBuilder<CommandSourceStack, ?>)Commands.argument("block", (ArgumentType)BlockPredicateArgument.blockPredicate(context)), expected, c -> BlockPredicateArgument.getBlockPredicate(c, "block").test(new BlockInWorld((LevelReader)((CommandSourceStack)c.getSource()).getLevel(), BlockPosArgument.getLoadedBlockPos(c, "pos"), true)))))))
/*      */       
/*  495 */       .then(
/*  496 */         Commands.literal("biome")
/*  497 */         .then(
/*  498 */           Commands.argument("pos", (ArgumentType)BlockPosArgument.blockPos())
/*  499 */           .then(
/*  500 */             addConditional(execute, (ArgumentBuilder<CommandSourceStack, ?>)Commands.argument("biome", (ArgumentType)ResourceOrTagArgument.resourceOrTag(context, Registries.BIOME)), expected, c -> ResourceOrTagArgument.getResourceOrTag(c, "biome", Registries.BIOME).test(((CommandSourceStack)c.getSource()).getLevel().getBiome(BlockPosArgument.getLoadedBlockPos(c, "pos"))))))))
/*      */       
/*  504 */       .then(
/*  505 */         Commands.literal("loaded")
/*  506 */         .then(
/*  507 */           addConditional(execute, (ArgumentBuilder<CommandSourceStack, ?>)Commands.argument("pos", (ArgumentType)BlockPosArgument.blockPos()), expected, c -> isChunkLoaded(((CommandSourceStack)c.getSource()).getLevel(), BlockPosArgument.getBlockPos(c, "pos"))))))
/*      */       
/*  510 */       .then(
/*  511 */         Commands.literal("dimension")
/*  512 */         .then(
/*  513 */           addConditional(execute, (ArgumentBuilder<CommandSourceStack, ?>)Commands.argument("dimension", (ArgumentType)DimensionArgument.dimension()), expected, c -> (DimensionArgument.getDimension(c, "dimension") == ((CommandSourceStack)c.getSource()).getLevel())))))
/*      */       
/*  518 */       .then(
/*  519 */         Commands.literal("score")
/*  520 */         .then(
/*  521 */           Commands.argument("target", (ArgumentType)ScoreHolderArgument.scoreHolder())
/*  522 */           .suggests(ScoreHolderArgument.SUGGEST_SCORE_HOLDERS)
/*  523 */           .then((
/*  524 */             (RequiredArgumentBuilder)((RequiredArgumentBuilder)((RequiredArgumentBuilder)((RequiredArgumentBuilder)((RequiredArgumentBuilder)Commands.argument("targetObjective", (ArgumentType)ObjectiveArgument.objective())
/*  525 */             .then(
/*  526 */               Commands.literal("=")
/*  527 */               .then(
/*  528 */                 Commands.argument("source", (ArgumentType)ScoreHolderArgument.scoreHolder())
/*  529 */                 .suggests(ScoreHolderArgument.SUGGEST_SCORE_HOLDERS)
/*  530 */                 .then(
/*  531 */                   addConditional(execute, (ArgumentBuilder<CommandSourceStack, ?>)Commands.argument("sourceObjective", (ArgumentType)ObjectiveArgument.objective()), expected, c -> checkScore(c, ()))))))
/*      */             
/*  535 */             .then(
/*  536 */               Commands.literal("<")
/*  537 */               .then(
/*  538 */                 Commands.argument("source", (ArgumentType)ScoreHolderArgument.scoreHolder())
/*  539 */                 .suggests(ScoreHolderArgument.SUGGEST_SCORE_HOLDERS)
/*  540 */                 .then(
/*  541 */                   addConditional(execute, (ArgumentBuilder<CommandSourceStack, ?>)Commands.argument("sourceObjective", (ArgumentType)ObjectiveArgument.objective()), expected, c -> checkScore(c, ()))))))
/*      */             
/*  545 */             .then(
/*  546 */               Commands.literal("<=")
/*  547 */               .then(
/*  548 */                 Commands.argument("source", (ArgumentType)ScoreHolderArgument.scoreHolder())
/*  549 */                 .suggests(ScoreHolderArgument.SUGGEST_SCORE_HOLDERS)
/*  550 */                 .then(
/*  551 */                   addConditional(execute, (ArgumentBuilder<CommandSourceStack, ?>)Commands.argument("sourceObjective", (ArgumentType)ObjectiveArgument.objective()), expected, c -> checkScore(c, ()))))))
/*      */             
/*  555 */             .then(
/*  556 */               Commands.literal(">")
/*  557 */               .then(
/*  558 */                 Commands.argument("source", (ArgumentType)ScoreHolderArgument.scoreHolder())
/*  559 */                 .suggests(ScoreHolderArgument.SUGGEST_SCORE_HOLDERS)
/*  560 */                 .then(
/*  561 */                   addConditional(execute, (ArgumentBuilder<CommandSourceStack, ?>)Commands.argument("sourceObjective", (ArgumentType)ObjectiveArgument.objective()), expected, c -> checkScore(c, ()))))))
/*      */             
/*  565 */             .then(
/*  566 */               Commands.literal(">=")
/*  567 */               .then(
/*  568 */                 Commands.argument("source", (ArgumentType)ScoreHolderArgument.scoreHolder())
/*  569 */                 .suggests(ScoreHolderArgument.SUGGEST_SCORE_HOLDERS)
/*  570 */                 .then(
/*  571 */                   addConditional(execute, (ArgumentBuilder<CommandSourceStack, ?>)Commands.argument("sourceObjective", (ArgumentType)ObjectiveArgument.objective()), expected, c -> checkScore(c, ()))))))
/*      */             
/*  575 */             .then(
/*  576 */               Commands.literal("matches")
/*  577 */               .then(
/*  578 */                 addConditional(execute, (ArgumentBuilder<CommandSourceStack, ?>)Commands.argument("range", (ArgumentType)RangeArgument.intRange()), expected, c -> checkScore(c, RangeArgument.Ints.getRange(c, "range")))))))))
/*      */       
/*  584 */       .then(
/*  585 */         Commands.literal("blocks")
/*  586 */         .then(
/*  587 */           Commands.argument("start", (ArgumentType)BlockPosArgument.blockPos())
/*  588 */           .then(
/*  589 */             Commands.argument("end", (ArgumentType)BlockPosArgument.blockPos())
/*  590 */             .then((
/*  591 */               (RequiredArgumentBuilder)Commands.argument("destination", (ArgumentType)BlockPosArgument.blockPos())
/*  592 */               .then(
/*  593 */                 addIfBlocksConditional(execute, (ArgumentBuilder<CommandSourceStack, ?>)Commands.literal("all"), expected, false)))
/*      */               
/*  595 */               .then(
/*  596 */                 addIfBlocksConditional(execute, (ArgumentBuilder<CommandSourceStack, ?>)Commands.literal("masked"), expected, true)))))))
/*      */       
/*  602 */       .then(
/*  603 */         Commands.literal("entity")
/*  604 */         .then((
/*  605 */           (RequiredArgumentBuilder)Commands.argument("entities", (ArgumentType)EntityArgument.entities())
/*  606 */           .fork(execute, c -> expect(c, expected, !EntityArgument.getOptionalEntities(c, "entities").isEmpty())))
/*  607 */           .executes(createNumericConditionalHandler(expected, c -> EntityArgument.getOptionalEntities(c, "entities").size())))))
/*      */       
/*  611 */       .then(
/*  612 */         Commands.literal("predicate")
/*  613 */         .then(
/*  614 */           addConditional(execute, (ArgumentBuilder<CommandSourceStack, ?>)Commands.argument("predicate", (ArgumentType)ResourceOrIdArgument.lootPredicate(context)), expected, c -> checkCustomPredicate((CommandSourceStack)c.getSource(), ResourceOrIdArgument.getLootPredicate(c, "predicate"))))))
/*      */       
/*  617 */       .then(
/*  618 */         Commands.literal("function")
/*  619 */         .then(
/*  620 */           Commands.argument("name", (ArgumentType)FunctionArgument.functions())
/*  621 */           .suggests(FunctionCommand.SUGGEST_FUNCTION)
/*  622 */           .fork(execute, (RedirectModifier)new ExecuteIfFunctionCustomModifier(expected)))))
/*      */       
/*  625 */       .then((
/*  626 */         (LiteralArgumentBuilder)Commands.literal("items")
/*  627 */         .then(
/*  628 */           Commands.literal("entity")
/*  629 */           .then(
/*  630 */             Commands.argument("entities", (ArgumentType)EntityArgument.entities())
/*  631 */             .then(
/*  632 */               Commands.argument("slots", (ArgumentType)SlotsArgument.slots())
/*  633 */               .then((
/*  634 */                 (RequiredArgumentBuilder)Commands.argument("item_predicate", (ArgumentType)ItemPredicateArgument.itemPredicate(context))
/*  635 */                 .fork(execute, c -> expect(c, expected, (countItems(EntityArgument.getEntities(c, "entities"), SlotsArgument.getSlots(c, "slots"), (Predicate<ItemStack>)ItemPredicateArgument.getItemPredicate(c, "item_predicate")) > 0))))
/*  636 */                 .executes(createNumericConditionalHandler(expected, c -> countItems(EntityArgument.getEntities(c, "entities"), SlotsArgument.getSlots(c, "slots"), (Predicate<ItemStack>)ItemPredicateArgument.getItemPredicate(c, "item_predicate")))))))))
/*      */         
/*  641 */         .then(
/*  642 */           Commands.literal("block")
/*  643 */           .then(
/*  644 */             Commands.argument("pos", (ArgumentType)BlockPosArgument.blockPos())
/*  645 */             .then(
/*  646 */               Commands.argument("slots", (ArgumentType)SlotsArgument.slots())
/*  647 */               .then((
/*  648 */                 (RequiredArgumentBuilder)Commands.argument("item_predicate", (ArgumentType)ItemPredicateArgument.itemPredicate(context))
/*  649 */                 .fork(execute, c -> expect(c, expected, (countItems((CommandSourceStack)c.getSource(), BlockPosArgument.getLoadedBlockPos(c, "pos"), SlotsArgument.getSlots(c, "slots"), (Predicate<ItemStack>)ItemPredicateArgument.getItemPredicate(c, "item_predicate")) > 0))))
/*  650 */                 .executes(createNumericConditionalHandler(expected, c -> countItems((CommandSourceStack)c.getSource(), BlockPosArgument.getLoadedBlockPos(c, "pos"), SlotsArgument.getSlots(c, "slots"), (Predicate<ItemStack>)ItemPredicateArgument.getItemPredicate(c, "item_predicate"))))))))))
/*      */       
/*  656 */       .then(
/*  657 */         Commands.literal("stopwatch")
/*  658 */         .then(
/*  659 */           Commands.argument("id", (ArgumentType)IdentifierArgument.id())
/*  660 */           .suggests(StopwatchCommand.SUGGEST_STOPWATCHES)
/*  661 */           .then(
/*  662 */             addConditional(execute, (ArgumentBuilder<CommandSourceStack, ?>)Commands.argument("range", (ArgumentType)RangeArgument.floatRange()), expected, c -> checkStopwatch(c, RangeArgument.Floats.getRange(c, "range"))))));
/*  668 */     for (DataCommands.DataProvider provider : DataCommands.SOURCE_PROVIDERS)
/*  669 */       parent
/*  670 */         .then(provider
/*  671 */           .wrap((ArgumentBuilder)Commands.literal("data"), p -> p.then(((RequiredArgumentBuilder)Commands.argument("path", (ArgumentType)NbtPathArgument.nbtPath()).fork(execute, ())).executes(createNumericConditionalHandler(expected, ()))))); 
/*  681 */     return (ArgumentBuilder)parent;
/*      */   }
/*      */   
/*      */   private static int countItems(Iterable<? extends SlotProvider> sources, SlotRange slotRange, Predicate<ItemStack> predicate) {
/*  685 */     int count = 0;
/*  686 */     for (SlotProvider slotProvider : sources) {
/*  687 */       IntList slots = slotRange.slots();
/*  688 */       for (int i = 0; i < slots.size(); i++) {
/*  689 */         int slotId = slots.getInt(i);
/*  690 */         SlotAccess slot = slotProvider.getSlot(slotId);
/*  691 */         if (slot != null) {
/*  692 */           ItemStack contents = slot.get();
/*  693 */           if (predicate.test(contents))
/*  694 */             count += contents.getCount(); 
/*      */         } 
/*      */       } 
/*      */     } 
/*  699 */     return count;
/*      */   }
/*      */   
/*      */   private static int countItems(CommandSourceStack source, BlockPos pos, SlotRange slotRange, Predicate<ItemStack> predicate) throws CommandSyntaxException {
/*  703 */     int count = 0;
/*  704 */     Container container = ItemCommands.getContainer(source, pos, ItemCommands.ERROR_SOURCE_NOT_A_CONTAINER);
/*  705 */     int containerSize = container.getContainerSize();
/*  707 */     IntList slots = slotRange.slots();
/*  708 */     for (int i = 0; i < slots.size(); i++) {
/*  709 */       int slotId = slots.getInt(i);
/*  710 */       if (slotId >= 0 && slotId < containerSize) {
/*  713 */         ItemStack contents = container.getItem(slotId);
/*  714 */         if (predicate.test(contents))
/*  715 */           count += contents.getCount(); 
/*      */       } 
/*      */     } 
/*  719 */     return count;
/*      */   }
/*      */   
/*      */   private static Command<CommandSourceStack> createNumericConditionalHandler(boolean expected, CommandNumericPredicate condition) {
/*  723 */     if (expected)
/*  724 */       return c -> {
/*      */           int count = condition.test(c);
/*      */           if (count > 0) {
/*      */             ((CommandSourceStack)c.getSource()).sendSuccess((), false);
/*      */             return count;
/*      */           } 
/*      */           throw ERROR_CONDITIONAL_FAILED.create();
/*      */         }; 
/*  734 */     return c -> {
/*      */         int count = condition.test(c);
/*      */         if (count == 0) {
/*      */           ((CommandSourceStack)c.getSource()).sendSuccess((), false);
/*      */           return 1;
/*      */         } 
/*      */         throw ERROR_CONDITIONAL_FAILED_COUNT.create(Integer.valueOf(count));
/*      */       };
/*      */   }
/*      */   
/*      */   private static int checkMatchingData(DataAccessor accessor, NbtPathArgument.NbtPath path) throws CommandSyntaxException {
/*  747 */     return path.countMatching((Tag)accessor.getData());
/*      */   }
/*      */   
/*      */   private static boolean checkScore(CommandContext<CommandSourceStack> context, IntBiPredicate operation) throws CommandSyntaxException {
/*  756 */     ScoreHolder target = ScoreHolderArgument.getName(context, "target");
/*  757 */     Objective targetObjective = ObjectiveArgument.getObjective(context, "targetObjective");
/*  758 */     ScoreHolder source = ScoreHolderArgument.getName(context, "source");
/*  759 */     Objective sourceObjective = ObjectiveArgument.getObjective(context, "sourceObjective");
/*  761 */     ServerScoreboard serverScoreboard = ((CommandSourceStack)context.getSource()).getServer().getScoreboard();
/*  763 */     ReadOnlyScoreInfo a = serverScoreboard.getPlayerScoreInfo(target, targetObjective);
/*  764 */     ReadOnlyScoreInfo b = serverScoreboard.getPlayerScoreInfo(source, sourceObjective);
/*  766 */     if (a == null || b == null)
/*  767 */       return false; 
/*  770 */     return operation.test(a.value(), b.value());
/*      */   }
/*      */   
/*      */   private static boolean checkScore(CommandContext<CommandSourceStack> context, MinMaxBounds.Ints range) throws CommandSyntaxException {
/*  774 */     ScoreHolder target = ScoreHolderArgument.getName(context, "target");
/*  775 */     Objective targetObjective = ObjectiveArgument.getObjective(context, "targetObjective");
/*  777 */     ServerScoreboard serverScoreboard = ((CommandSourceStack)context.getSource()).getServer().getScoreboard();
/*  779 */     ReadOnlyScoreInfo scoreInfo = serverScoreboard.getPlayerScoreInfo(target, targetObjective);
/*  781 */     if (scoreInfo == null)
/*  782 */       return false; 
/*  785 */     return range.matches(scoreInfo.value());
/*      */   }
/*      */   
/*      */   private static boolean checkStopwatch(CommandContext<CommandSourceStack> context, MinMaxBounds.Doubles range) throws CommandSyntaxException {
/*  789 */     Identifier id = IdentifierArgument.getId(context, "id");
/*  790 */     Stopwatches stopwatches = ((CommandSourceStack)context.getSource()).getServer().getStopwatches();
/*  791 */     Stopwatch stopwatch = stopwatches.get(id);
/*  792 */     if (stopwatch == null)
/*  793 */       throw StopwatchCommand.ERROR_DOES_NOT_EXIST.create(id); 
/*  795 */     long currentTime = Stopwatches.currentTime();
/*  796 */     double elapsedSeconds = stopwatch.elapsedSeconds(currentTime);
/*  797 */     return range.matches(elapsedSeconds);
/*      */   }
/*      */   
/*      */   private static boolean checkCustomPredicate(CommandSourceStack source, Holder<LootItemCondition> predicate) {
/*  801 */     ServerLevel level = source.getLevel();
/*  806 */     LootParams lootParams = (new LootParams.Builder(level)).withParameter(LootContextParams.ORIGIN, source.getPosition()).withOptionalParameter(LootContextParams.THIS_ENTITY, source.getEntity()).create(LootContextParamSets.COMMAND);
/*  807 */     LootContext context = (new LootContext.Builder(lootParams)).create(Optional.empty());
/*  808 */     context.pushVisitedElement(LootContext.createVisitedEntry((LootItemCondition)predicate.value()));
/*  809 */     return ((LootItemCondition)predicate.value()).test(context);
/*      */   }
/*      */   
/*      */   private static Collection<CommandSourceStack> expect(CommandContext<CommandSourceStack> context, boolean expected, boolean result) {
/*  813 */     if (result == expected)
/*  814 */       return Collections.singleton((CommandSourceStack)context.getSource()); 
/*  816 */     return Collections.emptyList();
/*      */   }
/*      */   
/*      */   private static ArgumentBuilder<CommandSourceStack, ?> addConditional(CommandNode<CommandSourceStack> root, ArgumentBuilder<CommandSourceStack, ?> argument, boolean expected, CommandPredicate predicate) {
/*  821 */     return argument
/*  822 */       .fork(root, c -> expect(c, expected, predicate.test(c)))
/*  823 */       .executes(c -> {
/*      */           if (expected == predicate.test(c)) {
/*      */             ((CommandSourceStack)c.getSource()).sendSuccess((), false);
/*      */             return 1;
/*      */           } 
/*      */           throw ERROR_CONDITIONAL_FAILED.create();
/*      */         });
/*      */   }
/*      */   
/*      */   private static ArgumentBuilder<CommandSourceStack, ?> addIfBlocksConditional(CommandNode<CommandSourceStack> root, ArgumentBuilder<CommandSourceStack, ?> argument, boolean expected, boolean skipAir) {
/*  834 */     return argument
/*  835 */       .fork(root, c -> expect(c, expected, checkRegions(c, skipAir).isPresent()))
/*  836 */       .executes(expected ? (c -> checkIfRegions(c, skipAir)) : (c -> checkUnlessRegions(c, skipAir)));
/*      */   }
/*      */   
/*      */   private static int checkIfRegions(CommandContext<CommandSourceStack> context, boolean skipAir) throws CommandSyntaxException {
/*  840 */     OptionalInt count = checkRegions(context, skipAir);
/*  841 */     if (count.isPresent()) {
/*  842 */       ((CommandSourceStack)context.getSource()).sendSuccess(() -> Component.translatable("commands.execute.conditional.pass_count", new Object[] { Integer.valueOf(count.getAsInt()) }), false);
/*  843 */       return count.getAsInt();
/*      */     } 
/*  845 */     throw ERROR_CONDITIONAL_FAILED.create();
/*      */   }
/*      */   
/*      */   private static int checkUnlessRegions(CommandContext<CommandSourceStack> context, boolean skipAir) throws CommandSyntaxException {
/*  850 */     OptionalInt count = checkRegions(context, skipAir);
/*  851 */     if (count.isPresent())
/*  852 */       throw ERROR_CONDITIONAL_FAILED_COUNT.create(Integer.valueOf(count.getAsInt())); 
/*  854 */     ((CommandSourceStack)context.getSource()).sendSuccess(() -> Component.translatable("commands.execute.conditional.pass"), false);
/*  855 */     return 1;
/*      */   }
/*      */   
/*      */   private static OptionalInt checkRegions(CommandContext<CommandSourceStack> context, boolean skipAir) throws CommandSyntaxException {
/*  860 */     return checkRegions(((CommandSourceStack)context.getSource()).getLevel(), BlockPosArgument.getLoadedBlockPos(context, "start"), BlockPosArgument.getLoadedBlockPos(context, "end"), BlockPosArgument.getLoadedBlockPos(context, "destination"), skipAir);
/*      */   }
/*      */   
/*      */   private static OptionalInt checkRegions(ServerLevel level, BlockPos startPos, BlockPos endPos, BlockPos destPos, boolean skipAir) throws CommandSyntaxException {
/*  864 */     BoundingBox from = BoundingBox.fromCorners((Vec3i)startPos, (Vec3i)endPos);
/*  865 */     BoundingBox destination = BoundingBox.fromCorners((Vec3i)destPos, (Vec3i)destPos.offset(from.getLength()));
/*  866 */     BlockPos offset = new BlockPos(destination.minX() - from.minX(), destination.minY() - from.minY(), destination.minZ() - from.minZ());
/*  867 */     long area = from.getXSpan() * from.getYSpan() * from.getZSpan();
/*  869 */     if (area > 32768L)
/*  870 */       throw ERROR_AREA_TOO_LARGE.create(Integer.valueOf(32768), Long.valueOf(area)); 
/*  873 */     int count = 0;
/*  874 */     RegistryAccess registryAccess = level.registryAccess();
/*  875 */     ProblemReporter.ScopedCollector reporter = new ProblemReporter.ScopedCollector(LOGGER);
/*      */     try {
/*  876 */       for (int z = from.minZ(); z <= from.maxZ(); z++) {
/*  877 */         for (int y = from.minY(); y <= from.maxY(); y++) {
/*  878 */           for (int x = from.minX(); x <= from.maxX(); x++) {
/*  879 */             BlockPos sourcePos = new BlockPos(x, y, z);
/*  880 */             BlockPos destinationPos = sourcePos.offset((Vec3i)offset);
/*  882 */             BlockState sourceBlock = level.getBlockState(sourcePos);
/*  883 */             if (!skipAir || !sourceBlock.is(Blocks.AIR)) {
/*  887 */               if (sourceBlock != level.getBlockState(destinationPos)) {
/*  888 */                 OptionalInt optionalInt = OptionalInt.empty();
/*  920 */                 reporter.close();
/*      */                 return optionalInt;
/*      */               } 
/*      */               BlockEntity sourceBlockEntity = level.getBlockEntity(sourcePos);
/*      */               BlockEntity destinationBlockEntity = level.getBlockEntity(destinationPos);
/*      */               if (sourceBlockEntity != null) {
/*      */                 if (destinationBlockEntity == null) {
/*      */                   OptionalInt optionalInt = OptionalInt.empty();
/*  920 */                   reporter.close();
/*      */                   return optionalInt;
/*      */                 } 
/*      */                 if (destinationBlockEntity.getType() != sourceBlockEntity.getType()) {
/*      */                   OptionalInt optionalInt = OptionalInt.empty();
/*  920 */                   reporter.close();
/*      */                   return optionalInt;
/*      */                 } 
/*      */                 if (!sourceBlockEntity.components().equals(destinationBlockEntity.components())) {
/*      */                   OptionalInt optionalInt = OptionalInt.empty();
/*  920 */                   reporter.close();
/*      */                   return optionalInt;
/*      */                 } 
/*      */                 TagValueOutput sourceOutput = TagValueOutput.createWithContext(reporter.forChild(sourceBlockEntity.problemPath()), (HolderLookup.Provider)registryAccess);
/*      */                 sourceBlockEntity.saveCustomOnly((ValueOutput)sourceOutput);
/*      */                 CompoundTag sourceTag = sourceOutput.buildResult();
/*      */                 TagValueOutput destinationOutput = TagValueOutput.createWithContext(reporter.forChild(destinationBlockEntity.problemPath()), (HolderLookup.Provider)registryAccess);
/*      */                 destinationBlockEntity.saveCustomOnly((ValueOutput)destinationOutput);
/*      */                 CompoundTag destinationTag = destinationOutput.buildResult();
/*      */                 if (!sourceTag.equals(destinationTag)) {
/*      */                   OptionalInt optionalInt = OptionalInt.empty();
/*  920 */                   reporter.close();
/*      */                   return optionalInt;
/*      */                 } 
/*      */               } 
/*      */               count++;
/*      */             } 
/*      */           } 
/*      */         } 
/*      */       } 
/*  920 */       reporter.close();
/*      */     } catch (Throwable throwable) {
/*      */       try {
/*      */         reporter.close();
/*      */       } catch (Throwable throwable1) {
/*      */         throwable.addSuppressed(throwable1);
/*      */       } 
/*      */       throw throwable;
/*      */     } 
/*  922 */     return OptionalInt.of(count);
/*      */   }
/*      */   
/*      */   private static RedirectModifier<CommandSourceStack> expandOneToOneEntityRelation(Function<Entity, Optional<Entity>> unpacker) {
/*  926 */     return context -> {
/*      */         CommandSourceStack source = (CommandSourceStack)context.getSource();
/*      */         Entity entity = source.getEntity();
/*      */         return (entity == null) ? List.of() : ((Optional)unpacker.apply(entity)).filter(()).map(()).orElse(List.of());
/*      */       };
/*      */   }
/*      */   
/*      */   private static RedirectModifier<CommandSourceStack> expandOneToManyEntityRelation(Function<Entity, Stream<Entity>> unpacker) {
/*  937 */     return context -> {
/*      */         CommandSourceStack source = (CommandSourceStack)context.getSource();
/*      */         Entity entity = source.getEntity();
/*      */         if (entity == null)
/*      */           return List.of(); 
/*      */         Objects.requireNonNull(source);
/*      */         return ((Stream)unpacker.apply(entity)).filter(()).map(source::withEntity).toList();
/*      */       };
/*      */   }
/*      */   
/*      */   private static LiteralArgumentBuilder<CommandSourceStack> createRelationOperations(CommandNode<CommandSourceStack> execute, LiteralArgumentBuilder<CommandSourceStack> on) {
/*  948 */     return (LiteralArgumentBuilder<CommandSourceStack>)((LiteralArgumentBuilder)((LiteralArgumentBuilder)((LiteralArgumentBuilder)((LiteralArgumentBuilder)((LiteralArgumentBuilder)((LiteralArgumentBuilder)((LiteralArgumentBuilder)on
/*  949 */       .then(
/*  950 */         Commands.literal("owner")
/*  951 */         .fork(execute, expandOneToOneEntityRelation(e -> {
/*      */               OwnableEntity ownableEntity = (OwnableEntity)e;
/*      */               return (e instanceof OwnableEntity) ? Optional.<LivingEntity>ofNullable(ownableEntity.getOwner()) : Optional.empty();
/*  953 */             })))).then(
/*  954 */         Commands.literal("leasher")
/*  955 */         .fork(execute, expandOneToOneEntityRelation(e -> {
/*      */               Leashable leashable = (Leashable)e;
/*      */               return (e instanceof Leashable) ? Optional.<Entity>ofNullable(leashable.getLeashHolder()) : Optional.empty();
/*  957 */             })))).then(
/*  958 */         Commands.literal("target")
/*  959 */         .fork(execute, expandOneToOneEntityRelation(e -> {
/*      */               Targeting targeting = (Targeting)e;
/*      */               return (e instanceof Targeting) ? Optional.<LivingEntity>ofNullable(targeting.getTarget()) : Optional.empty();
/*  961 */             })))).then(
/*  962 */         Commands.literal("attacker")
/*  963 */         .fork(execute, expandOneToOneEntityRelation(e -> {
/*      */               Attackable attackable = (Attackable)e;
/*      */               return (e instanceof Attackable) ? Optional.<LivingEntity>ofNullable(attackable.getLastAttacker()) : Optional.empty();
/*  965 */             })))).then(
/*  966 */         Commands.literal("vehicle")
/*  967 */         .fork(execute, expandOneToOneEntityRelation(e -> Optional.ofNullable(e.getVehicle())))))
/*      */       
/*  969 */       .then(
/*  970 */         Commands.literal("controller")
/*  971 */         .fork(execute, expandOneToOneEntityRelation(e -> Optional.ofNullable(e.getControllingPassenger())))))
/*      */       
/*  973 */       .then(
/*  974 */         Commands.literal("origin")
/*  975 */         .fork(execute, expandOneToOneEntityRelation(e -> {
/*      */               TraceableEntity traceable = (TraceableEntity)e;
/*      */               return (e instanceof TraceableEntity) ? Optional.<Entity>ofNullable(traceable.getOwner()) : Optional.empty();
/*  977 */             })))).then(
/*  978 */         Commands.literal("passengers")
/*  979 */         .fork(execute, expandOneToManyEntityRelation(e -> e.getPassengers().stream())));
/*      */   }
/*      */   
/*      */   private static CommandSourceStack spawnEntityAndRedirect(CommandSourceStack source, Holder.Reference<EntityType<?>> type) throws CommandSyntaxException {
/*  984 */     Entity entity = SummonCommand.createEntity(source, type, source.getPosition(), new CompoundTag(), true);
/*  985 */     return source.withEntity(entity);
/*      */   }
/*      */   
/*      */   @FunctionalInterface
/*      */   private static interface CommandPredicate {
/*      */     boolean test(CommandContext<CommandSourceStack> param1CommandContext) throws CommandSyntaxException;
/*      */   }
/*      */   
/*      */   @FunctionalInterface
/*      */   private static interface CommandNumericPredicate {
/*      */     int test(CommandContext<CommandSourceStack> param1CommandContext) throws CommandSyntaxException;
/*      */   }
/*      */   
/*      */   private static class ExecuteIfFunctionCustomModifier implements CustomModifierExecutor.ModifierAdapter<CommandSourceStack> {
/*      */     private final IntPredicate check;
/*      */     
/*      */     private ExecuteIfFunctionCustomModifier(boolean check) {
/*  992 */       this.check = check ? (value -> (value != 0)) : (value -> (value == 0));
/*      */     }
/*      */     
/*      */     public void apply(CommandSourceStack originalSource, List<CommandSourceStack> currentSources, ContextChain<CommandSourceStack> currentStep, ChainModifiers modifiers, ExecutionControl<CommandSourceStack> output) {
/*  997 */       ExecuteCommand.scheduleFunctionConditionsAndTest(originalSource, currentSources, FunctionCommand::modifySenderForExecution, this.check, currentStep, null, output, c -> FunctionArgument.getFunctions(c, "name"), modifiers);
/*      */     }
/*      */   }
/*      */   
/*      */   public static <T extends ExecutionCommandSource<T>> void scheduleFunctionConditionsAndTest(T originalSource, List<T> currentSources, Function<T, T> functionContextModifier, IntPredicate check, ContextChain<T> currentStep, CompoundTag parameters, ExecutionControl<T> output, InCommandFunction<CommandContext<T>, Collection<CommandFunction<T>>> functionGetter, ChainModifiers modifiers) {
/*      */     Collection<CommandFunction<T>> functionsToRun;
/* 1012 */     List<T> filteredSources = new ArrayList<>(currentSources.size());
/*      */     try {
/* 1017 */       functionsToRun = functionGetter.apply(currentStep.getTopContext().copyFor(originalSource));
/* 1018 */     } catch (CommandSyntaxException e) {
/* 1019 */       originalSource.handleError(e, modifiers.isForked(), output.tracer());
/*      */       return;
/*      */     } 
/* 1023 */     int functionCount = functionsToRun.size();
/* 1024 */     if (functionCount == 0)
/*      */       return; 
/* 1029 */     List<InstantiatedFunction<T>> instantiatedFunctions = new ArrayList<>(functionCount);
/*      */     try {
/* 1032 */       for (CommandFunction<T> function : functionsToRun) {
/*      */         try {
/* 1034 */           instantiatedFunctions.add(function.instantiate(parameters, originalSource.dispatcher()));
/* 1035 */         } catch (FunctionInstantiationException e) {
/* 1036 */           throw ERROR_FUNCTION_CONDITION_INSTANTATION_FAILURE.create(function.id(), e.messageComponent());
/*      */         } 
/*      */       } 
/* 1039 */     } catch (CommandSyntaxException e) {
/* 1040 */       originalSource.handleError(e, modifiers.isForked(), output.tracer());
/*      */     } 
/* 1043 */     for (ExecutionCommandSource executionCommandSource1 : currentSources) {
/* 1044 */       ExecutionCommandSource executionCommandSource2 = (ExecutionCommandSource)functionContextModifier.apply((T)executionCommandSource1.clearCallbacks());
/* 1045 */       CommandResultCallback functionCallback = (success, result) -> {
/*      */           if (check.test(result))
/*      */             filteredSources.add(source); 
/*      */         };
/* 1051 */       output.queueNext((EntryAction)new IsolatedCall(o -> {
/*      */               for (InstantiatedFunction<T> function : (Iterable<InstantiatedFunction<T>>)instantiatedFunctions)
/*      */                 o.queueNext((new CallFunction(function, o.currentFrame().returnValueConsumer(), true)).bind(newFunctionContext)); 
/*      */               o.queueNext(FallthroughTask.instance());
/* 1051 */             }functionCallback));
/*      */     } 
/* 1059 */     ContextChain<T> nextStage = currentStep.nextStage();
/* 1060 */     String input = currentStep.getTopContext().getInput();
/* 1061 */     output.queueNext((EntryAction)new BuildContexts.Continuation(input, nextStage, modifiers, (ExecutionCommandSource)originalSource, filteredSources));
/*      */   }
/*      */   
/*      */   @FunctionalInterface
/*      */   private static interface IntBiPredicate {
/*      */     boolean test(int param1Int1, int param1Int2);
/*      */   }
/*      */ }


/* Location:              C:\Users\karol\AppData\Roaming\ModrinthApp\meta\versions\26.2-0.19.5\26.2-0.19.5.jar!\net\minecraft\server\commands\ExecuteCommand.class
 * Java compiler version: 25 (69.0)
 * JD-Core Version:       1.1.3
 */