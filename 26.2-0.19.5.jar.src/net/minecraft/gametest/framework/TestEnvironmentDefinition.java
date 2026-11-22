/*     */ package net.minecraft.gametest.framework;
/*     */ 
/*     */ import com.mojang.datafixers.kinds.App;
/*     */ import com.mojang.datafixers.kinds.Applicative;
/*     */ import com.mojang.logging.LogUtils;
/*     */ import com.mojang.serialization.Codec;
/*     */ import com.mojang.serialization.MapCodec;
/*     */ import com.mojang.serialization.codecs.RecordCodecBuilder;
/*     */ import java.util.Arrays;
/*     */ import java.util.List;
/*     */ import java.util.Optional;
/*     */ import java.util.function.BiFunction;
/*     */ import java.util.function.Function;
/*     */ import net.minecraft.commands.CommandSourceStack;
/*     */ import net.minecraft.commands.functions.CommandFunction;
/*     */ import net.minecraft.core.Holder;
/*     */ import net.minecraft.core.Registry;
/*     */ import net.minecraft.core.registries.BuiltInRegistries;
/*     */ import net.minecraft.core.registries.Registries;
/*     */ import net.minecraft.resources.Identifier;
/*     */ import net.minecraft.resources.RegistryFileCodec;
/*     */ import net.minecraft.server.MinecraftServer;
/*     */ import net.minecraft.server.ServerFunctionManager;
/*     */ import net.minecraft.server.level.ServerLevel;
/*     */ import net.minecraft.server.permissions.LevelBasedPermissionSet;
/*     */ import net.minecraft.server.permissions.PermissionSet;
/*     */ import net.minecraft.util.ExtraCodecs;
/*     */ import net.minecraft.util.StringRepresentable;
/*     */ import net.minecraft.util.Unit;
/*     */ import net.minecraft.world.Difficulty;
/*     */ import net.minecraft.world.attribute.EnvironmentAttributeSystem;
/*     */ import net.minecraft.world.clock.ClockManager;
/*     */ import net.minecraft.world.clock.WorldClock;
/*     */ import net.minecraft.world.level.Level;
/*     */ import net.minecraft.world.level.gamerules.GameRule;
/*     */ import net.minecraft.world.level.gamerules.GameRuleMap;
/*     */ import net.minecraft.world.level.gamerules.GameRules;
/*     */ import net.minecraft.world.timeline.Timeline;
/*     */ import org.slf4j.Logger;
/*     */ 
/*     */ public interface TestEnvironmentDefinition<SavedDataType> {
/*     */   public static final Codec<TestEnvironmentDefinition<?>> DIRECT_CODEC;
/*     */   
/*     */   static MapCodec<? extends TestEnvironmentDefinition<?>> bootstrap(Registry<MapCodec<? extends TestEnvironmentDefinition<?>>> registry) {
/*  38 */     Registry.register(registry, "all_of", AllOf.CODEC);
/*  39 */     Registry.register(registry, "clock_time", ClockTime.CODEC);
/*  40 */     Registry.register(registry, "difficulty", SetDifficulty.CODEC);
/*  41 */     Registry.register(registry, "function", Functions.CODEC);
/*  42 */     Registry.register(registry, "game_rules", SetGameRules.CODEC);
/*  43 */     Registry.register(registry, "timeline_attributes", Timelines.CODEC);
/*  44 */     return (MapCodec<? extends TestEnvironmentDefinition<?>>)Registry.register(registry, "weather", Weather.CODEC);
/*     */   }
/*     */   
/*     */   static {
/*  47 */     DIRECT_CODEC = BuiltInRegistries.TEST_ENVIRONMENT_DEFINITION_TYPE.byNameCodec().dispatch(TestEnvironmentDefinition::codec, c -> c);
/*     */   }
/*     */   
/*  49 */   public static final Codec<Holder<TestEnvironmentDefinition<?>>> CODEC = (Codec<Holder<TestEnvironmentDefinition<?>>>)RegistryFileCodec.create(Registries.TEST_ENVIRONMENT, DIRECT_CODEC);
/*     */   
/*     */   public static class Activation<T> {
/*     */     private final T value;
/*     */     
/*     */     private final TestEnvironmentDefinition<T> definition;
/*     */     
/*     */     private final ServerLevel level;
/*     */     
/*     */     private Activation(T value, TestEnvironmentDefinition<T> definition, ServerLevel level) {
/*  64 */       this.value = value;
/*  65 */       this.definition = definition;
/*  66 */       this.level = level;
/*     */     }
/*     */     
/*     */     public void teardown() {
/*  70 */       this.definition.teardown(this.level, this.value);
/*     */     }
/*     */   }
/*     */   
/*     */   static <T> Activation<T> activate(TestEnvironmentDefinition<T> environment, ServerLevel level) {
/*  75 */     return new Activation<>(environment.setup(level), environment, level);
/*     */   }
/*     */   
/*     */   SavedDataType setup(ServerLevel paramServerLevel);
/*     */   
/*     */   void teardown(ServerLevel paramServerLevel, SavedDataType paramSavedDataType);
/*     */   
/*     */   MapCodec<? extends TestEnvironmentDefinition<SavedDataType>> codec();
/*     */   
/*     */   public static final class Weather extends Record implements TestEnvironmentDefinition<Weather.Type> {
/*     */     private final Type weather;
/*     */     
/*     */     public static final MapCodec<Weather> CODEC;
/*     */     
/*     */     public Weather(Type weather) {
/*  78 */       this.weather = weather;
/*     */     }
/*     */     
/*     */     public final String toString() {
/*     */       // Byte code:
/*     */       //   0: aload_0
/*     */       //   1: <illegal opcode> toString : (Lnet/minecraft/gametest/framework/TestEnvironmentDefinition$Weather;)Ljava/lang/String;
/*     */       //   6: areturn
/*     */       // Line number table:
/*     */       //   Java source line number -> byte code offset
/*     */       //   #78	-> 0
/*     */       // Local variable table:
/*     */       //   start	length	slot	name	descriptor
/*     */       //   0	7	0	this	Lnet/minecraft/gametest/framework/TestEnvironmentDefinition$Weather;
/*     */     }
/*     */     
/*     */     public final int hashCode() {
/*     */       // Byte code:
/*     */       //   0: aload_0
/*     */       //   1: <illegal opcode> hashCode : (Lnet/minecraft/gametest/framework/TestEnvironmentDefinition$Weather;)I
/*     */       //   6: ireturn
/*     */       // Line number table:
/*     */       //   Java source line number -> byte code offset
/*     */       //   #78	-> 0
/*     */       // Local variable table:
/*     */       //   start	length	slot	name	descriptor
/*     */       //   0	7	0	this	Lnet/minecraft/gametest/framework/TestEnvironmentDefinition$Weather;
/*     */     }
/*     */     
/*     */     public final boolean equals(Object o) {
/*     */       // Byte code:
/*     */       //   0: aload_0
/*     */       //   1: aload_1
/*     */       //   2: <illegal opcode> equals : (Lnet/minecraft/gametest/framework/TestEnvironmentDefinition$Weather;Ljava/lang/Object;)Z
/*     */       //   7: ireturn
/*     */       // Line number table:
/*     */       //   Java source line number -> byte code offset
/*     */       //   #78	-> 0
/*     */       // Local variable table:
/*     */       //   start	length	slot	name	descriptor
/*     */       //   0	8	0	this	Lnet/minecraft/gametest/framework/TestEnvironmentDefinition$Weather;
/*     */       //   0	8	1	o	Ljava/lang/Object;
/*     */     }
/*     */     
/*     */     public Type weather() {
/*  78 */       return this.weather;
/*     */     }
/*     */     
/*     */     public enum Type implements StringRepresentable {
/*  80 */       CLEAR("clear", 100000, 0, false, false),
/*  81 */       RAIN("rain", 0, 100000, true, false),
/*  82 */       THUNDER("thunder", 0, 100000, true, true);
/*     */       
/*  84 */       public static final Codec<Type> CODEC = (Codec<Type>)StringRepresentable.fromEnum(Type::values);
/*     */       
/*     */       private final String id;
/*     */       
/*     */       private final int clearTime;
/*     */       
/*     */       private final int rainTime;
/*     */       
/*     */       private final boolean raining;
/*     */       
/*     */       private final boolean thundering;
/*     */       
/*     */       static {
/*     */       
/*     */       }
/*     */       
/*     */       Type(String id, int clearTime, int rainTime, boolean raining, boolean thundering) {
/*  93 */         this.id = id;
/*  94 */         this.clearTime = clearTime;
/*  95 */         this.rainTime = rainTime;
/*  96 */         this.raining = raining;
/*  97 */         this.thundering = thundering;
/*     */       }
/*     */       
/*     */       public void apply(ServerLevel level) {
/* 101 */         level.getServer().setWeatherParameters(this.clearTime, this.rainTime, this.raining, this.thundering);
/*     */       }
/*     */       
/*     */       public String getSerializedName() {
/* 106 */         return this.id;
/*     */       }
/*     */     }
/*     */     
/*     */     static {
/* 110 */       CODEC = RecordCodecBuilder.mapCodec(i -> i.group((App)Type.CODEC.fieldOf("weather").forGetter(Weather::weather)).apply((Applicative)i, Weather::new));
/*     */     }
/*     */     
/*     */     public Type setup(ServerLevel level) {
/*     */       Type previous;
/* 117 */       if (level.isThundering()) {
/* 118 */         previous = Type.THUNDER;
/* 119 */       } else if (level.isRaining()) {
/* 120 */         previous = Type.RAIN;
/*     */       } else {
/* 122 */         previous = Type.CLEAR;
/*     */       } 
/* 124 */       this.weather.apply(level);
/* 125 */       return previous;
/*     */     }
/*     */     
/*     */     public void teardown(ServerLevel level, Type saveData) {
/* 130 */       level.resetWeatherCycle();
/* 131 */       saveData.apply(level);
/*     */     }
/*     */     
/*     */     public MapCodec<Weather> codec() {
/* 136 */       return CODEC;
/*     */     }
/*     */   }
/*     */   
/*     */   public enum Type implements StringRepresentable {
/*     */     CLEAR("clear", 100000, 0, false, false),
/*     */     RAIN("rain", 0, 100000, true, false),
/*     */     THUNDER("thunder", 0, 100000, true, true);
/*     */     
/*     */     public static final Codec<Type> CODEC = (Codec<Type>)StringRepresentable.fromEnum(Type::values);
/*     */     
/*     */     private final String id;
/*     */     
/*     */     private final int clearTime;
/*     */     
/*     */     private final int rainTime;
/*     */     
/*     */     private final boolean raining;
/*     */     
/*     */     private final boolean thundering;
/*     */     
/*     */     static {
/*     */     
/*     */     }
/*     */     
/*     */     Type(String id, int clearTime, int rainTime, boolean raining, boolean thundering) {
/*     */       this.id = id;
/*     */       this.clearTime = clearTime;
/*     */       this.rainTime = rainTime;
/*     */       this.raining = raining;
/*     */       this.thundering = thundering;
/*     */     }
/*     */     
/*     */     public void apply(ServerLevel level) {
/*     */       level.getServer().setWeatherParameters(this.clearTime, this.rainTime, this.raining, this.thundering);
/*     */     }
/*     */     
/*     */     public String getSerializedName() {
/*     */       return this.id;
/*     */     }
/*     */   }
/*     */   
/*     */   public static final class ClockTime extends Record implements TestEnvironmentDefinition<Long> {
/*     */     private final Holder<WorldClock> clock;
/*     */     
/*     */     private final int time;
/*     */     
/*     */     public static final MapCodec<ClockTime> CODEC;
/*     */     
/*     */     public ClockTime(Holder<WorldClock> clock, int time) {
/* 140 */       this.clock = clock;
/* 140 */       this.time = time;
/*     */     }
/*     */     
/*     */     public final String toString() {
/*     */       // Byte code:
/*     */       //   0: aload_0
/*     */       //   1: <illegal opcode> toString : (Lnet/minecraft/gametest/framework/TestEnvironmentDefinition$ClockTime;)Ljava/lang/String;
/*     */       //   6: areturn
/*     */       // Line number table:
/*     */       //   Java source line number -> byte code offset
/*     */       //   #140	-> 0
/*     */       // Local variable table:
/*     */       //   start	length	slot	name	descriptor
/*     */       //   0	7	0	this	Lnet/minecraft/gametest/framework/TestEnvironmentDefinition$ClockTime;
/*     */     }
/*     */     
/*     */     public final int hashCode() {
/*     */       // Byte code:
/*     */       //   0: aload_0
/*     */       //   1: <illegal opcode> hashCode : (Lnet/minecraft/gametest/framework/TestEnvironmentDefinition$ClockTime;)I
/*     */       //   6: ireturn
/*     */       // Line number table:
/*     */       //   Java source line number -> byte code offset
/*     */       //   #140	-> 0
/*     */       // Local variable table:
/*     */       //   start	length	slot	name	descriptor
/*     */       //   0	7	0	this	Lnet/minecraft/gametest/framework/TestEnvironmentDefinition$ClockTime;
/*     */     }
/*     */     
/*     */     public final boolean equals(Object o) {
/*     */       // Byte code:
/*     */       //   0: aload_0
/*     */       //   1: aload_1
/*     */       //   2: <illegal opcode> equals : (Lnet/minecraft/gametest/framework/TestEnvironmentDefinition$ClockTime;Ljava/lang/Object;)Z
/*     */       //   7: ireturn
/*     */       // Line number table:
/*     */       //   Java source line number -> byte code offset
/*     */       //   #140	-> 0
/*     */       // Local variable table:
/*     */       //   start	length	slot	name	descriptor
/*     */       //   0	8	0	this	Lnet/minecraft/gametest/framework/TestEnvironmentDefinition$ClockTime;
/*     */       //   0	8	1	o	Ljava/lang/Object;
/*     */     }
/*     */     
/*     */     public Holder<WorldClock> clock() {
/* 140 */       return this.clock;
/*     */     }
/*     */     
/*     */     public int time() {
/* 140 */       return this.time;
/*     */     }
/*     */     
/*     */     static {
/* 144 */       CODEC = RecordCodecBuilder.mapCodec(i -> i.group((App)WorldClock.CODEC.fieldOf("clock").forGetter(ClockTime::clock), (App)ExtraCodecs.NON_NEGATIVE_INT.fieldOf("time").forGetter(ClockTime::time)).apply((Applicative)i, ClockTime::new));
/*     */     }
/*     */     
/*     */     public Long setup(ServerLevel level) {
/* 151 */       MinecraftServer server = level.getServer();
/* 152 */       long previous = server.clockManager().getTotalTicks(this.clock);
/* 153 */       server.clockManager().setTotalTicks(this.clock, this.time);
/* 154 */       return Long.valueOf(previous);
/*     */     }
/*     */     
/*     */     public void teardown(ServerLevel level, Long saveData) {
/* 159 */       MinecraftServer server = level.getServer();
/* 160 */       server.clockManager().setTotalTicks(this.clock, saveData.longValue());
/*     */     }
/*     */     
/*     */     public MapCodec<ClockTime> codec() {
/* 165 */       return CODEC;
/*     */     }
/*     */   }
/*     */   
/*     */   public static final class SetGameRules extends Record implements TestEnvironmentDefinition<GameRuleMap> {
/*     */     private final GameRuleMap gameRulesMap;
/*     */     
/*     */     public static final MapCodec<SetGameRules> CODEC;
/*     */     
/*     */     public SetGameRules(GameRuleMap gameRulesMap) {
/* 169 */       this.gameRulesMap = gameRulesMap;
/*     */     }
/*     */     
/*     */     public final String toString() {
/*     */       // Byte code:
/*     */       //   0: aload_0
/*     */       //   1: <illegal opcode> toString : (Lnet/minecraft/gametest/framework/TestEnvironmentDefinition$SetGameRules;)Ljava/lang/String;
/*     */       //   6: areturn
/*     */       // Line number table:
/*     */       //   Java source line number -> byte code offset
/*     */       //   #169	-> 0
/*     */       // Local variable table:
/*     */       //   start	length	slot	name	descriptor
/*     */       //   0	7	0	this	Lnet/minecraft/gametest/framework/TestEnvironmentDefinition$SetGameRules;
/*     */     }
/*     */     
/*     */     public final int hashCode() {
/*     */       // Byte code:
/*     */       //   0: aload_0
/*     */       //   1: <illegal opcode> hashCode : (Lnet/minecraft/gametest/framework/TestEnvironmentDefinition$SetGameRules;)I
/*     */       //   6: ireturn
/*     */       // Line number table:
/*     */       //   Java source line number -> byte code offset
/*     */       //   #169	-> 0
/*     */       // Local variable table:
/*     */       //   start	length	slot	name	descriptor
/*     */       //   0	7	0	this	Lnet/minecraft/gametest/framework/TestEnvironmentDefinition$SetGameRules;
/*     */     }
/*     */     
/*     */     public final boolean equals(Object o) {
/*     */       // Byte code:
/*     */       //   0: aload_0
/*     */       //   1: aload_1
/*     */       //   2: <illegal opcode> equals : (Lnet/minecraft/gametest/framework/TestEnvironmentDefinition$SetGameRules;Ljava/lang/Object;)Z
/*     */       //   7: ireturn
/*     */       // Line number table:
/*     */       //   Java source line number -> byte code offset
/*     */       //   #169	-> 0
/*     */       // Local variable table:
/*     */       //   start	length	slot	name	descriptor
/*     */       //   0	8	0	this	Lnet/minecraft/gametest/framework/TestEnvironmentDefinition$SetGameRules;
/*     */       //   0	8	1	o	Ljava/lang/Object;
/*     */     }
/*     */     
/*     */     public GameRuleMap gameRulesMap() {
/* 169 */       return this.gameRulesMap;
/*     */     }
/*     */     
/*     */     static {
/* 170 */       CODEC = RecordCodecBuilder.mapCodec(i -> i.group((App)GameRuleMap.CODEC.fieldOf("rules").forGetter(SetGameRules::gameRulesMap)).apply((Applicative)i, SetGameRules::new));
/*     */     }
/*     */     
/*     */     public GameRuleMap setup(ServerLevel level) {
/* 176 */       GameRuleMap originalState = GameRuleMap.of();
/* 177 */       GameRules gameRules = level.getGameRules();
/* 178 */       this.gameRulesMap.keySet().forEach(rule -> setFromActive(originalState, rule, gameRules));
/* 179 */       gameRules.setAll(this.gameRulesMap, level.getServer());
/* 181 */       return originalState;
/*     */     }
/*     */     
/*     */     private static <T> void setFromActive(GameRuleMap map, GameRule<T> rule, GameRules rules) {
/* 185 */       map.set(rule, rules.get(rule));
/*     */     }
/*     */     
/*     */     public void teardown(ServerLevel level, GameRuleMap saveData) {
/* 190 */       level.getGameRules().setAll(saveData, level.getServer());
/*     */     }
/*     */     
/*     */     public MapCodec<SetGameRules> codec() {
/* 195 */       return CODEC;
/*     */     }
/*     */   }
/*     */   
/*     */   public static final class Functions extends Record implements TestEnvironmentDefinition<Unit> {
/*     */     private final Optional<Identifier> setupFunction;
/*     */     
/*     */     private final Optional<Identifier> teardownFunction;
/*     */     
/*     */     public Functions(Optional<Identifier> setupFunction, Optional<Identifier> teardownFunction) {
/* 199 */       this.setupFunction = setupFunction;
/* 199 */       this.teardownFunction = teardownFunction;
/*     */     }
/*     */     
/*     */     public final String toString() {
/*     */       // Byte code:
/*     */       //   0: aload_0
/*     */       //   1: <illegal opcode> toString : (Lnet/minecraft/gametest/framework/TestEnvironmentDefinition$Functions;)Ljava/lang/String;
/*     */       //   6: areturn
/*     */       // Line number table:
/*     */       //   Java source line number -> byte code offset
/*     */       //   #199	-> 0
/*     */       // Local variable table:
/*     */       //   start	length	slot	name	descriptor
/*     */       //   0	7	0	this	Lnet/minecraft/gametest/framework/TestEnvironmentDefinition$Functions;
/*     */     }
/*     */     
/*     */     public final int hashCode() {
/*     */       // Byte code:
/*     */       //   0: aload_0
/*     */       //   1: <illegal opcode> hashCode : (Lnet/minecraft/gametest/framework/TestEnvironmentDefinition$Functions;)I
/*     */       //   6: ireturn
/*     */       // Line number table:
/*     */       //   Java source line number -> byte code offset
/*     */       //   #199	-> 0
/*     */       // Local variable table:
/*     */       //   start	length	slot	name	descriptor
/*     */       //   0	7	0	this	Lnet/minecraft/gametest/framework/TestEnvironmentDefinition$Functions;
/*     */     }
/*     */     
/*     */     public final boolean equals(Object o) {
/*     */       // Byte code:
/*     */       //   0: aload_0
/*     */       //   1: aload_1
/*     */       //   2: <illegal opcode> equals : (Lnet/minecraft/gametest/framework/TestEnvironmentDefinition$Functions;Ljava/lang/Object;)Z
/*     */       //   7: ireturn
/*     */       // Line number table:
/*     */       //   Java source line number -> byte code offset
/*     */       //   #199	-> 0
/*     */       // Local variable table:
/*     */       //   start	length	slot	name	descriptor
/*     */       //   0	8	0	this	Lnet/minecraft/gametest/framework/TestEnvironmentDefinition$Functions;
/*     */       //   0	8	1	o	Ljava/lang/Object;
/*     */     }
/*     */     
/*     */     public Optional<Identifier> setupFunction() {
/* 199 */       return this.setupFunction;
/*     */     }
/*     */     
/*     */     public Optional<Identifier> teardownFunction() {
/* 199 */       return this.teardownFunction;
/*     */     }
/*     */     
/* 200 */     private static final Logger LOGGER = LogUtils.getLogger();
/*     */     
/*     */     public static final MapCodec<Functions> CODEC;
/*     */     
/*     */     static {
/* 202 */       CODEC = RecordCodecBuilder.mapCodec(i -> i.group((App)Identifier.CODEC.optionalFieldOf("setup").forGetter(Functions::setupFunction), (App)Identifier.CODEC.optionalFieldOf("teardown").forGetter(Functions::teardownFunction)).apply((Applicative)i, Functions::new));
/*     */     }
/*     */     
/*     */     public Unit setup(ServerLevel level) {
/* 209 */       this.setupFunction.ifPresent(p -> run(level, p));
/* 210 */       return Unit.INSTANCE;
/*     */     }
/*     */     
/*     */     public void teardown(ServerLevel level, Unit saveData) {
/* 215 */       this.teardownFunction.ifPresent(p -> run(level, p));
/*     */     }
/*     */     
/*     */     private static void run(ServerLevel level, Identifier functionId) {
/* 219 */       MinecraftServer server = level.getServer();
/* 220 */       ServerFunctionManager functions = server.getFunctions();
/* 221 */       Optional<CommandFunction<CommandSourceStack>> function = functions.get(functionId);
/* 222 */       if (function.isPresent()) {
/* 226 */         CommandSourceStack source = server.createCommandSourceStack().withPermission((PermissionSet)LevelBasedPermissionSet.GAMEMASTER).withSuppressedOutput().withLevel(level);
/* 227 */         functions.execute(function.get(), source);
/*     */       } else {
/* 229 */         LOGGER.error("Test Batch failed for non-existent function {}", functionId);
/*     */       } 
/*     */     }
/*     */     
/*     */     public MapCodec<Functions> codec() {
/* 235 */       return CODEC;
/*     */     }
/*     */   }
/*     */   
/*     */   public static final class AllOf extends Record implements TestEnvironmentDefinition<List<? extends Activation<?>>> {
/*     */     private final List<Holder<TestEnvironmentDefinition<?>>> definitions;
/*     */     
/*     */     public static final MapCodec<AllOf> CODEC;
/*     */     
/*     */     public AllOf(List<Holder<TestEnvironmentDefinition<?>>> definitions) {
/* 239 */       this.definitions = definitions;
/*     */     }
/*     */     
/*     */     public final String toString() {
/*     */       // Byte code:
/*     */       //   0: aload_0
/*     */       //   1: <illegal opcode> toString : (Lnet/minecraft/gametest/framework/TestEnvironmentDefinition$AllOf;)Ljava/lang/String;
/*     */       //   6: areturn
/*     */       // Line number table:
/*     */       //   Java source line number -> byte code offset
/*     */       //   #239	-> 0
/*     */       // Local variable table:
/*     */       //   start	length	slot	name	descriptor
/*     */       //   0	7	0	this	Lnet/minecraft/gametest/framework/TestEnvironmentDefinition$AllOf;
/*     */     }
/*     */     
/*     */     public final int hashCode() {
/*     */       // Byte code:
/*     */       //   0: aload_0
/*     */       //   1: <illegal opcode> hashCode : (Lnet/minecraft/gametest/framework/TestEnvironmentDefinition$AllOf;)I
/*     */       //   6: ireturn
/*     */       // Line number table:
/*     */       //   Java source line number -> byte code offset
/*     */       //   #239	-> 0
/*     */       // Local variable table:
/*     */       //   start	length	slot	name	descriptor
/*     */       //   0	7	0	this	Lnet/minecraft/gametest/framework/TestEnvironmentDefinition$AllOf;
/*     */     }
/*     */     
/*     */     public final boolean equals(Object o) {
/*     */       // Byte code:
/*     */       //   0: aload_0
/*     */       //   1: aload_1
/*     */       //   2: <illegal opcode> equals : (Lnet/minecraft/gametest/framework/TestEnvironmentDefinition$AllOf;Ljava/lang/Object;)Z
/*     */       //   7: ireturn
/*     */       // Line number table:
/*     */       //   Java source line number -> byte code offset
/*     */       //   #239	-> 0
/*     */       // Local variable table:
/*     */       //   start	length	slot	name	descriptor
/*     */       //   0	8	0	this	Lnet/minecraft/gametest/framework/TestEnvironmentDefinition$AllOf;
/*     */       //   0	8	1	o	Ljava/lang/Object;
/*     */     }
/*     */     
/*     */     public List<Holder<TestEnvironmentDefinition<?>>> definitions() {
/* 239 */       return this.definitions;
/*     */     }
/*     */     
/*     */     static {
/* 240 */       CODEC = RecordCodecBuilder.mapCodec(i -> i.group((App)TestEnvironmentDefinition.CODEC.listOf().fieldOf("definitions").forGetter(AllOf::definitions)).apply((Applicative)i, AllOf::new));
/*     */     }
/*     */     
/*     */     public AllOf(TestEnvironmentDefinition<?>... defs) {
/* 245 */       this(Arrays.<TestEnvironmentDefinition<?>>stream(defs).map(AllOf::holder).toList());
/*     */     }
/*     */     
/*     */     private static Holder<TestEnvironmentDefinition<?>> holder(TestEnvironmentDefinition<?> holder) {
/* 249 */       return Holder.direct(holder);
/*     */     }
/*     */     
/*     */     public List<? extends TestEnvironmentDefinition.Activation<?>> setup(ServerLevel level) {
/* 254 */       return this.definitions.stream().map(b -> TestEnvironmentDefinition.activate((TestEnvironmentDefinition)b.value(), level)).toList();
/*     */     }
/*     */     
/*     */     public void teardown(ServerLevel level, List<? extends TestEnvironmentDefinition.Activation<?>> activations) {
/* 259 */       activations.reversed().forEach(TestEnvironmentDefinition.Activation::teardown);
/*     */     }
/*     */     
/*     */     public MapCodec<AllOf> codec() {
/* 264 */       return CODEC;
/*     */     }
/*     */   }
/*     */   
/*     */   public static final class Timelines extends Record implements TestEnvironmentDefinition<EnvironmentAttributeSystem> {
/*     */     private final List<Holder<Timeline>> timelines;
/*     */     
/*     */     public static final MapCodec<Timelines> CODEC;
/*     */     
/*     */     public Timelines(List<Holder<Timeline>> timelines) {
/* 272 */       this.timelines = timelines;
/*     */     }
/*     */     
/*     */     public final String toString() {
/*     */       // Byte code:
/*     */       //   0: aload_0
/*     */       //   1: <illegal opcode> toString : (Lnet/minecraft/gametest/framework/TestEnvironmentDefinition$Timelines;)Ljava/lang/String;
/*     */       //   6: areturn
/*     */       // Line number table:
/*     */       //   Java source line number -> byte code offset
/*     */       //   #272	-> 0
/*     */       // Local variable table:
/*     */       //   start	length	slot	name	descriptor
/*     */       //   0	7	0	this	Lnet/minecraft/gametest/framework/TestEnvironmentDefinition$Timelines;
/*     */     }
/*     */     
/*     */     public final int hashCode() {
/*     */       // Byte code:
/*     */       //   0: aload_0
/*     */       //   1: <illegal opcode> hashCode : (Lnet/minecraft/gametest/framework/TestEnvironmentDefinition$Timelines;)I
/*     */       //   6: ireturn
/*     */       // Line number table:
/*     */       //   Java source line number -> byte code offset
/*     */       //   #272	-> 0
/*     */       // Local variable table:
/*     */       //   start	length	slot	name	descriptor
/*     */       //   0	7	0	this	Lnet/minecraft/gametest/framework/TestEnvironmentDefinition$Timelines;
/*     */     }
/*     */     
/*     */     public final boolean equals(Object o) {
/*     */       // Byte code:
/*     */       //   0: aload_0
/*     */       //   1: aload_1
/*     */       //   2: <illegal opcode> equals : (Lnet/minecraft/gametest/framework/TestEnvironmentDefinition$Timelines;Ljava/lang/Object;)Z
/*     */       //   7: ireturn
/*     */       // Line number table:
/*     */       //   Java source line number -> byte code offset
/*     */       //   #272	-> 0
/*     */       // Local variable table:
/*     */       //   start	length	slot	name	descriptor
/*     */       //   0	8	0	this	Lnet/minecraft/gametest/framework/TestEnvironmentDefinition$Timelines;
/*     */       //   0	8	1	o	Ljava/lang/Object;
/*     */     }
/*     */     
/*     */     public List<Holder<Timeline>> timelines() {
/* 272 */       return this.timelines;
/*     */     }
/*     */     
/*     */     static {
/* 273 */       CODEC = RecordCodecBuilder.mapCodec(i -> i.group((App)Timeline.CODEC.listOf().fieldOf("timelines").forGetter(Timelines::timelines)).apply((Applicative)i, Timelines::new));
/*     */     }
/*     */     
/*     */     public EnvironmentAttributeSystem setup(ServerLevel level) {
/* 279 */       EnvironmentAttributeSystem.Builder builder = EnvironmentAttributeSystem.builder().addDefaultLayers((Level)level);
/* 281 */       for (Holder<Timeline> timeline : this.timelines)
/* 282 */         builder.addTimelineLayer(timeline, (ClockManager)level.clockManager()); 
/* 285 */       return level.setEnvironmentAttributes(builder.build());
/*     */     }
/*     */     
/*     */     public void teardown(ServerLevel level, EnvironmentAttributeSystem saveData) {
/* 290 */       level.setEnvironmentAttributes(saveData);
/*     */     }
/*     */     
/*     */     public MapCodec<Timelines> codec() {
/* 295 */       return CODEC;
/*     */     }
/*     */   }
/*     */   
/*     */   public static final class SetDifficulty extends Record implements TestEnvironmentDefinition<Difficulty> {
/*     */     private final Difficulty difficulty;
/*     */     
/*     */     public static final MapCodec<SetDifficulty> CODEC;
/*     */     
/*     */     public SetDifficulty(Difficulty difficulty) {
/* 299 */       this.difficulty = difficulty;
/*     */     }
/*     */     
/*     */     public final String toString() {
/*     */       // Byte code:
/*     */       //   0: aload_0
/*     */       //   1: <illegal opcode> toString : (Lnet/minecraft/gametest/framework/TestEnvironmentDefinition$SetDifficulty;)Ljava/lang/String;
/*     */       //   6: areturn
/*     */       // Line number table:
/*     */       //   Java source line number -> byte code offset
/*     */       //   #299	-> 0
/*     */       // Local variable table:
/*     */       //   start	length	slot	name	descriptor
/*     */       //   0	7	0	this	Lnet/minecraft/gametest/framework/TestEnvironmentDefinition$SetDifficulty;
/*     */     }
/*     */     
/*     */     public final int hashCode() {
/*     */       // Byte code:
/*     */       //   0: aload_0
/*     */       //   1: <illegal opcode> hashCode : (Lnet/minecraft/gametest/framework/TestEnvironmentDefinition$SetDifficulty;)I
/*     */       //   6: ireturn
/*     */       // Line number table:
/*     */       //   Java source line number -> byte code offset
/*     */       //   #299	-> 0
/*     */       // Local variable table:
/*     */       //   start	length	slot	name	descriptor
/*     */       //   0	7	0	this	Lnet/minecraft/gametest/framework/TestEnvironmentDefinition$SetDifficulty;
/*     */     }
/*     */     
/*     */     public final boolean equals(Object o) {
/*     */       // Byte code:
/*     */       //   0: aload_0
/*     */       //   1: aload_1
/*     */       //   2: <illegal opcode> equals : (Lnet/minecraft/gametest/framework/TestEnvironmentDefinition$SetDifficulty;Ljava/lang/Object;)Z
/*     */       //   7: ireturn
/*     */       // Line number table:
/*     */       //   Java source line number -> byte code offset
/*     */       //   #299	-> 0
/*     */       // Local variable table:
/*     */       //   start	length	slot	name	descriptor
/*     */       //   0	8	0	this	Lnet/minecraft/gametest/framework/TestEnvironmentDefinition$SetDifficulty;
/*     */       //   0	8	1	o	Ljava/lang/Object;
/*     */     }
/*     */     
/*     */     public Difficulty difficulty() {
/* 299 */       return this.difficulty;
/*     */     }
/*     */     
/*     */     static {
/* 300 */       CODEC = RecordCodecBuilder.mapCodec(i -> i.group((App)Difficulty.CODEC.fieldOf("difficulty").forGetter(SetDifficulty::difficulty)).apply((Applicative)i, SetDifficulty::new));
/*     */     }
/*     */     
/*     */     public Difficulty setup(ServerLevel level) {
/* 306 */       Difficulty oldDifficulty = level.getDifficulty();
/* 307 */       level.getServer().setDifficulty(this.difficulty, true);
/* 308 */       return oldDifficulty;
/*     */     }
/*     */     
/*     */     public void teardown(ServerLevel level, Difficulty saveData) {
/* 313 */       level.getServer().setDifficulty(saveData, true);
/*     */     }
/*     */     
/*     */     public MapCodec<SetDifficulty> codec() {
/* 318 */       return CODEC;
/*     */     }
/*     */   }
/*     */ }


/* Location:              C:\Users\karol\AppData\Roaming\ModrinthApp\meta\versions\26.2-0.19.5\26.2-0.19.5.jar!\net\minecraft\gametest\framework\TestEnvironmentDefinition.class
 * Java compiler version: 25 (69.0)
 * JD-Core Version:       1.1.3
 */