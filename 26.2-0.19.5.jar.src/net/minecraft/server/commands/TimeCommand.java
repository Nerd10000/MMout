/*     */ package net.minecraft.server.commands;
/*     */ 
/*     */ import com.mojang.brigadier.CommandDispatcher;
/*     */ import com.mojang.brigadier.Message;
/*     */ import com.mojang.brigadier.arguments.ArgumentType;
/*     */ import com.mojang.brigadier.arguments.FloatArgumentType;
/*     */ import com.mojang.brigadier.arguments.IntegerArgumentType;
/*     */ import com.mojang.brigadier.builder.ArgumentBuilder;
/*     */ import com.mojang.brigadier.builder.LiteralArgumentBuilder;
/*     */ import com.mojang.brigadier.builder.RequiredArgumentBuilder;
/*     */ import com.mojang.brigadier.context.CommandContext;
/*     */ import com.mojang.brigadier.exceptions.CommandSyntaxException;
/*     */ import com.mojang.brigadier.exceptions.Dynamic2CommandExceptionType;
/*     */ import com.mojang.brigadier.exceptions.DynamicCommandExceptionType;
/*     */ import com.mojang.brigadier.suggestion.Suggestions;
/*     */ import com.mojang.brigadier.suggestion.SuggestionsBuilder;
/*     */ import java.util.concurrent.CompletableFuture;
/*     */ import java.util.function.Predicate;
/*     */ import java.util.stream.Stream;
/*     */ import net.minecraft.commands.CommandBuildContext;
/*     */ import net.minecraft.commands.CommandSourceStack;
/*     */ import net.minecraft.commands.Commands;
/*     */ import net.minecraft.commands.SharedSuggestionProvider;
/*     */ import net.minecraft.commands.arguments.IdentifierArgument;
/*     */ import net.minecraft.commands.arguments.ResourceArgument;
/*     */ import net.minecraft.commands.arguments.TimeArgument;
/*     */ import net.minecraft.core.Holder;
/*     */ import net.minecraft.core.registries.Registries;
/*     */ import net.minecraft.network.chat.Component;
/*     */ import net.minecraft.resources.ResourceKey;
/*     */ import net.minecraft.world.clock.ClockManager;
/*     */ import net.minecraft.world.clock.ClockTimeMarker;
/*     */ import net.minecraft.world.clock.ClockTimeMarkers;
/*     */ import net.minecraft.world.clock.ServerClockManager;
/*     */ import net.minecraft.world.clock.WorldClock;
/*     */ import net.minecraft.world.level.dimension.DimensionType;
/*     */ import net.minecraft.world.timeline.Timeline;
/*     */ 
/*     */ public class TimeCommand {
/*     */   private static final DynamicCommandExceptionType ERROR_NO_DEFAULT_CLOCK;
/*     */   
/*     */   private static final Dynamic2CommandExceptionType ERROR_NO_TIME_MARKER_FOUND;
/*     */   
/*     */   private static final Dynamic2CommandExceptionType ERROR_WRONG_TIMELINE_FOR_CLOCK;
/*     */   
/*     */   private static final int MAX_CLOCK_RATE = 1000;
/*     */   
/*     */   static {
/*  44 */     ERROR_NO_DEFAULT_CLOCK = new DynamicCommandExceptionType(dimension -> Component.translatableEscape("commands.time.no_default_clock", new Object[] { dimension }));
/*  45 */     ERROR_NO_TIME_MARKER_FOUND = new Dynamic2CommandExceptionType((clock, timeMarker) -> Component.translatableEscape("commands.time.no_time_marker_found", new Object[] { timeMarker, clock }));
/*  46 */     ERROR_WRONG_TIMELINE_FOR_CLOCK = new Dynamic2CommandExceptionType((clock, timeline) -> Component.translatableEscape("commands.time.wrong_timeline_for_clock", new Object[] { timeline, clock }));
/*     */   }
/*     */   
/*     */   public static void register(CommandDispatcher<CommandSourceStack> dispatcher, CommandBuildContext context) {
/*  51 */     LiteralArgumentBuilder<CommandSourceStack> baseCommand = (LiteralArgumentBuilder<CommandSourceStack>)Commands.literal("time").requires((Predicate)Commands.hasPermission(Commands.LEVEL_GAMEMASTERS));
/*  53 */     dispatcher.register(addClockNodes(context, baseCommand, c -> getDefaultClock((CommandSourceStack)c.getSource())));
/*  58 */     dispatcher.register((LiteralArgumentBuilder)((LiteralArgumentBuilder)baseCommand
/*  59 */         .then(Commands.literal("query")
/*  60 */           .then(Commands.literal("gametime")
/*  61 */             .executes(c -> queryGameTime((CommandSourceStack)c.getSource())))))
/*     */         
/*  64 */         .then(Commands.literal("of")
/*  65 */           .then((ArgumentBuilder)addClockNodes(context, 
/*  66 */               Commands.argument("clock", (ArgumentType)ResourceArgument.resource(context, Registries.WORLD_CLOCK)), c -> ResourceArgument.getClock(c, "clock")))));
/*     */   }
/*     */   
/*     */   private static <A extends ArgumentBuilder<CommandSourceStack, A>> A addClockNodes(CommandBuildContext context, A node, ClockGetter clockGetter) {
/*  74 */     return (A)node
/*  75 */       .then(((LiteralArgumentBuilder)Commands.literal("set")
/*  76 */         .then(Commands.argument("time", (ArgumentType)TimeArgument.time())
/*  77 */           .executes(c -> setTotalTicks((CommandSourceStack)c.getSource(), clockGetter.getClock(c), IntegerArgumentType.getInteger(c, "time")))))
/*     */         
/*  79 */         .then(Commands.argument("timemarker", (ArgumentType)IdentifierArgument.id())
/*  80 */           .suggests((c, p) -> suggestTimeMarkers((CommandSourceStack)c.getSource(), p, clockGetter.getClock(c)))
/*  81 */           .executes(c -> setTimeToTimeMarker((CommandSourceStack)c.getSource(), clockGetter.getClock(c), ResourceKey.create(ClockTimeMarkers.ROOT_ID, IdentifierArgument.getId(c, "timemarker"))))))
/*     */       
/*  84 */       .then(Commands.literal("add")
/*  85 */         .then(Commands.argument("time", (ArgumentType)TimeArgument.time(-2147483648))
/*  86 */           .executes(c -> addTime((CommandSourceStack)c.getSource(), clockGetter.getClock(c), IntegerArgumentType.getInteger(c, "time")))))
/*     */       
/*  89 */       .then(Commands.literal("pause")
/*  90 */         .executes(c -> setPaused((CommandSourceStack)c.getSource(), clockGetter.getClock(c), true)))
/*     */       
/*  92 */       .then(Commands.literal("resume")
/*  93 */         .executes(c -> setPaused((CommandSourceStack)c.getSource(), clockGetter.getClock(c), false)))
/*     */       
/*  95 */       .then(Commands.literal("rate")
/*  96 */         .then(Commands.argument("rate", (ArgumentType)FloatArgumentType.floatArg(1.0E-5F, 1000.0F))
/*  97 */           .executes(c -> setRate((CommandSourceStack)c.getSource(), clockGetter.getClock(c), FloatArgumentType.getFloat(c, "rate")))))
/*     */       
/* 100 */       .then(((LiteralArgumentBuilder)Commands.literal("query")
/* 101 */         .then(Commands.literal("time")
/* 102 */           .executes(c -> queryTime((CommandSourceStack)c.getSource(), clockGetter.getClock(c)))))
/*     */         
/* 104 */         .then(((RequiredArgumentBuilder)Commands.argument("timeline", (ArgumentType)ResourceArgument.resource(context, Registries.TIMELINE))
/* 105 */           .suggests((c, p) -> suggestTimelines((CommandSourceStack)c.getSource(), p, clockGetter.getClock(c)))
/* 106 */           .executes(c -> queryTimelineTicks((CommandSourceStack)c.getSource(), clockGetter.getClock(c), (Holder<Timeline>)ResourceArgument.getTimeline(c, "timeline"))))
/* 107 */           .then(Commands.literal("repetition")
/* 108 */             .executes(c -> queryTimelineRepetitions((CommandSourceStack)c.getSource(), clockGetter.getClock(c), (Holder<Timeline>)ResourceArgument.getTimeline(c, "timeline"))))));
/*     */   }
/*     */   
/*     */   private static CompletableFuture<Suggestions> suggestTimeMarkers(CommandSourceStack source, SuggestionsBuilder builder, Holder<WorldClock> clock) {
/* 115 */     return SharedSuggestionProvider.suggestResource(source.getServer().clockManager().commandTimeMarkersForClock(clock).map(ResourceKey::identifier), builder);
/*     */   }
/*     */   
/*     */   private static CompletableFuture<Suggestions> suggestTimelines(CommandSourceStack source, SuggestionsBuilder builder, Holder<WorldClock> clock) {
/* 121 */     Stream<ResourceKey<Timeline>> timelines = source.registryAccess().lookupOrThrow(Registries.TIMELINE).listElements().filter(timeline -> ((Timeline)timeline.value()).clock().equals(clock)).map(Holder.Reference::key);
/* 123 */     return SharedSuggestionProvider.suggestResource(timelines.map(ResourceKey::identifier), builder);
/*     */   }
/*     */   
/*     */   private static int queryGameTime(CommandSourceStack source) {
/* 127 */     long gameTime = source.getLevel().getGameTime();
/* 128 */     source.sendSuccess(() -> Component.translatable("commands.time.query.gametime", new Object[] { Long.valueOf(gameTime) }), false);
/* 129 */     return wrapTime(gameTime);
/*     */   }
/*     */   
/*     */   private static int queryTime(CommandSourceStack source, Holder<WorldClock> clock) {
/* 133 */     ServerClockManager clockManager = source.getServer().clockManager();
/* 134 */     long totalTicks = clockManager.getTotalTicks(clock);
/* 135 */     source.sendSuccess(() -> Component.translatable("commands.time.query.absolute", new Object[] { clock.getRegisteredName(), Long.valueOf(totalTicks) }), false);
/* 136 */     return wrapTime(totalTicks);
/*     */   }
/*     */   
/*     */   private static int queryTimelineTicks(CommandSourceStack source, Holder<WorldClock> clock, Holder<Timeline> timeline) throws CommandSyntaxException {
/* 140 */     if (!clock.equals(((Timeline)timeline.value()).clock()))
/* 141 */       throw ERROR_WRONG_TIMELINE_FOR_CLOCK.create(clock.getRegisteredName(), timeline.getRegisteredName()); 
/* 143 */     ServerClockManager clockManager = source.getServer().clockManager();
/* 144 */     long currentTicks = ((Timeline)timeline.value()).getCurrentTicks((ClockManager)clockManager);
/* 145 */     source.sendSuccess(() -> Component.translatable("commands.time.query.timeline", new Object[] { timeline.getRegisteredName(), Long.valueOf(currentTicks) }), false);
/* 146 */     return wrapTime(currentTicks);
/*     */   }
/*     */   
/*     */   private static int queryTimelineRepetitions(CommandSourceStack source, Holder<WorldClock> clock, Holder<Timeline> timeline) throws CommandSyntaxException {
/* 150 */     if (!clock.equals(((Timeline)timeline.value()).clock()))
/* 151 */       throw ERROR_WRONG_TIMELINE_FOR_CLOCK.create(clock.getRegisteredName(), timeline.getRegisteredName()); 
/* 153 */     ServerClockManager clockManager = source.getServer().clockManager();
/* 154 */     long repetitions = ((Timeline)timeline.value()).getPeriodCount((ClockManager)clockManager);
/* 155 */     source.sendSuccess(() -> Component.translatable("commands.time.query.timeline.repetitions", new Object[] { timeline.getRegisteredName(), Long.valueOf(repetitions) }), false);
/* 156 */     return wrapTime(repetitions);
/*     */   }
/*     */   
/*     */   private static int setTotalTicks(CommandSourceStack source, Holder<WorldClock> clock, int totalTicks) {
/* 160 */     ServerClockManager clockManager = source.getServer().clockManager();
/* 161 */     clockManager.setTotalTicks(clock, totalTicks);
/* 162 */     source.sendSuccess(() -> Component.translatable("commands.time.set.absolute", new Object[] { clock.getRegisteredName(), Integer.valueOf(totalTicks) }), true);
/* 163 */     return totalTicks;
/*     */   }
/*     */   
/*     */   private static int addTime(CommandSourceStack source, Holder<WorldClock> clock, int time) {
/* 167 */     ServerClockManager clockManager = source.getServer().clockManager();
/* 168 */     clockManager.addTicks(clock, time);
/* 169 */     long totalTicks = clockManager.getTotalTicks(clock);
/* 170 */     source.sendSuccess(() -> Component.translatable("commands.time.set.absolute", new Object[] { clock.getRegisteredName(), Long.valueOf(totalTicks) }), true);
/* 171 */     return wrapTime(totalTicks);
/*     */   }
/*     */   
/*     */   private static int setTimeToTimeMarker(CommandSourceStack source, Holder<WorldClock> clock, ResourceKey<ClockTimeMarker> timeMarkerId) throws CommandSyntaxException {
/* 175 */     ServerClockManager clockManager = source.getServer().clockManager();
/* 176 */     if (!clockManager.moveToTimeMarker(clock, timeMarkerId))
/* 177 */       throw ERROR_NO_TIME_MARKER_FOUND.create(clock.getRegisteredName(), timeMarkerId); 
/* 179 */     source.sendSuccess(() -> Component.translatable("commands.time.set.time_marker", new Object[] { clock.getRegisteredName(), timeMarkerId.identifier().toString() }), true);
/* 180 */     return wrapTime(clockManager.getTotalTicks(clock));
/*     */   }
/*     */   
/*     */   private static int setPaused(CommandSourceStack source, Holder<WorldClock> clock, boolean paused) {
/* 184 */     source.getServer().clockManager().setPaused(clock, paused);
/* 185 */     source.sendSuccess(() -> Component.translatable(paused ? "commands.time.pause" : "commands.time.resume", new Object[] { clock.getRegisteredName() }), true);
/* 186 */     return 1;
/*     */   }
/*     */   
/*     */   private static int setRate(CommandSourceStack source, Holder<WorldClock> clock, float rate) {
/* 190 */     source.getServer().clockManager().setRate(clock, rate);
/* 191 */     source.sendSuccess(() -> Component.translatable("commands.time.rate", new Object[] { clock.getRegisteredName(), Float.valueOf(rate) }), true);
/* 192 */     return 1;
/*     */   }
/*     */   
/*     */   private static int wrapTime(long ticks) {
/* 196 */     return Math.toIntExact(ticks % 2147483647L);
/*     */   }
/*     */   
/*     */   private static Holder<WorldClock> getDefaultClock(CommandSourceStack source) throws CommandSyntaxException {
/* 200 */     Holder<DimensionType> dimensionType = source.getLevel().dimensionTypeRegistration();
/* 201 */     return (Holder<WorldClock>)((DimensionType)dimensionType.value()).defaultClock()
/* 202 */       .orElseThrow(() -> ERROR_NO_DEFAULT_CLOCK.create(dimensionType.getRegisteredName()));
/*     */   }
/*     */   
/*     */   private static interface ClockGetter {
/*     */     Holder<WorldClock> getClock(CommandContext<CommandSourceStack> param1CommandContext) throws CommandSyntaxException;
/*     */   }
/*     */ }


/* Location:              C:\Users\karol\AppData\Roaming\ModrinthApp\meta\versions\26.2-0.19.5\26.2-0.19.5.jar!\net\minecraft\server\commands\TimeCommand.class
 * Java compiler version: 25 (69.0)
 * JD-Core Version:       1.1.3
 */