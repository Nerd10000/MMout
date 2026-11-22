/*     */ package net.minecraft.commands.arguments.selector.options;
/*     */ 
/*     */ import com.google.common.collect.Maps;
/*     */ import com.mojang.brigadier.ImmutableStringReader;
/*     */ import com.mojang.brigadier.Message;
/*     */ import com.mojang.brigadier.StringReader;
/*     */ import com.mojang.brigadier.exceptions.CommandSyntaxException;
/*     */ import com.mojang.brigadier.exceptions.DynamicCommandExceptionType;
/*     */ import com.mojang.brigadier.exceptions.SimpleCommandExceptionType;
/*     */ import com.mojang.brigadier.suggestion.SuggestionsBuilder;
/*     */ import com.mojang.logging.LogUtils;
/*     */ import java.util.Arrays;
/*     */ import java.util.List;
/*     */ import java.util.Locale;
/*     */ import java.util.Map;
/*     */ import java.util.Objects;
/*     */ import java.util.Optional;
/*     */ import java.util.concurrent.CompletableFuture;
/*     */ import java.util.function.Consumer;
/*     */ import java.util.function.Predicate;
/*     */ import net.minecraft.advancements.AdvancementHolder;
/*     */ import net.minecraft.advancements.AdvancementProgress;
/*     */ import net.minecraft.advancements.CriterionProgress;
/*     */ import net.minecraft.advancements.predicates.MinMaxBounds;
/*     */ import net.minecraft.commands.SharedSuggestionProvider;
/*     */ import net.minecraft.commands.arguments.selector.EntitySelectorParser;
/*     */ import net.minecraft.core.Holder;
/*     */ import net.minecraft.core.HolderLookup;
/*     */ import net.minecraft.core.HolderSet;
/*     */ import net.minecraft.core.registries.BuiltInRegistries;
/*     */ import net.minecraft.core.registries.Registries;
/*     */ import net.minecraft.nbt.CompoundTag;
/*     */ import net.minecraft.nbt.NbtUtils;
/*     */ import net.minecraft.nbt.Tag;
/*     */ import net.minecraft.nbt.TagParser;
/*     */ import net.minecraft.network.chat.Component;
/*     */ import net.minecraft.resources.Identifier;
/*     */ import net.minecraft.resources.ResourceKey;
/*     */ import net.minecraft.server.PlayerAdvancements;
/*     */ import net.minecraft.server.ServerAdvancementManager;
/*     */ import net.minecraft.server.ServerScoreboard;
/*     */ import net.minecraft.server.level.ServerLevel;
/*     */ import net.minecraft.server.level.ServerPlayer;
/*     */ import net.minecraft.tags.TagKey;
/*     */ import net.minecraft.util.ProblemReporter;
/*     */ import net.minecraft.world.entity.Entity;
/*     */ import net.minecraft.world.entity.EntityType;
/*     */ import net.minecraft.world.entity.EntityTypes;
/*     */ import net.minecraft.world.item.ItemStack;
/*     */ import net.minecraft.world.level.GameType;
/*     */ import net.minecraft.world.level.Level;
/*     */ import net.minecraft.world.level.storage.TagValueOutput;
/*     */ import net.minecraft.world.level.storage.ValueOutput;
/*     */ import net.minecraft.world.level.storage.loot.LootContext;
/*     */ import net.minecraft.world.level.storage.loot.LootParams;
/*     */ import net.minecraft.world.level.storage.loot.parameters.LootContextParamSets;
/*     */ import net.minecraft.world.level.storage.loot.parameters.LootContextParams;
/*     */ import net.minecraft.world.level.storage.loot.predicates.LootItemCondition;
/*     */ import net.minecraft.world.scores.Objective;
/*     */ import net.minecraft.world.scores.PlayerTeam;
/*     */ import net.minecraft.world.scores.ReadOnlyScoreInfo;
/*     */ import net.minecraft.world.scores.ScoreHolder;
/*     */ import org.slf4j.Logger;
/*     */ 
/*     */ public class EntitySelectorOptions {
/*  58 */   private static final Logger LOGGER = LogUtils.getLogger();
/*     */   
/*  59 */   private static final Map<String, Option> OPTIONS = Maps.newHashMap();
/*     */   
/*     */   private static final Predicate<EntitySelectorParser> ALWAYS_AVAILABLE = paramEntitySelectorParser -> true;
/*     */   
/*     */   public static final DynamicCommandExceptionType ERROR_UNKNOWN_OPTION;
/*     */   
/*     */   public static final DynamicCommandExceptionType ERROR_INAPPLICABLE_OPTION;
/*     */   
/*     */   static {
/*  63 */     ERROR_UNKNOWN_OPTION = new DynamicCommandExceptionType(name -> Component.translatableEscape("argument.entity.options.unknown", new Object[] { name }));
/*  64 */     ERROR_INAPPLICABLE_OPTION = new DynamicCommandExceptionType(name -> Component.translatableEscape("argument.entity.options.inapplicable", new Object[] { name }));
/*     */   }
/*     */   
/*  65 */   public static final SimpleCommandExceptionType ERROR_RANGE_NEGATIVE = new SimpleCommandExceptionType((Message)Component.translatable("argument.entity.options.distance.negative"));
/*     */   
/*  66 */   public static final SimpleCommandExceptionType ERROR_LEVEL_NEGATIVE = new SimpleCommandExceptionType((Message)Component.translatable("argument.entity.options.level.negative"));
/*     */   
/*  67 */   public static final SimpleCommandExceptionType ERROR_LIMIT_TOO_SMALL = new SimpleCommandExceptionType((Message)Component.translatable("argument.entity.options.limit.toosmall"));
/*     */   
/*     */   public static final DynamicCommandExceptionType ERROR_SORT_UNKNOWN;
/*     */   
/*     */   public static final DynamicCommandExceptionType ERROR_GAME_MODE_INVALID;
/*     */   
/*     */   public static final DynamicCommandExceptionType ERROR_ENTITY_TYPE_INVALID;
/*     */   
/*     */   static {
/*  68 */     ERROR_SORT_UNKNOWN = new DynamicCommandExceptionType(name -> Component.translatableEscape("argument.entity.options.sort.irreversible", new Object[] { name }));
/*  69 */     ERROR_GAME_MODE_INVALID = new DynamicCommandExceptionType(name -> Component.translatableEscape("argument.entity.options.mode.invalid", new Object[] { name }));
/*  70 */     ERROR_ENTITY_TYPE_INVALID = new DynamicCommandExceptionType(type -> Component.translatableEscape("argument.entity.options.type.invalid", new Object[] { type }));
/*     */   }
/*     */   
/*     */   private static void register(String name, Modifier modifier, Predicate<EntitySelectorParser> predicate, Component description) {
/*  73 */     OPTIONS.put(name, new Option(modifier, predicate, description));
/*     */   }
/*     */   
/*     */   public static void bootStrap() {
/*  77 */     if (!OPTIONS.isEmpty())
/*     */       return; 
/*  81 */     register("name", parser -> {
/*     */           int start = parser.getReader().getCursor();
/*     */           boolean inverted = parser.shouldInvertValue();
/*     */           String name = parser.getReader().readString();
/*     */           InvertableSetOptionState state = parser.nameOption();
/*     */           if (!state.canParseElement(inverted))
/*     */             throw rollbackAndThrow(parser, start, ERROR_INAPPLICABLE_OPTION, "name"); 
/*     */           state.markParsedElement(inverted);
/*     */           parser.addPredicate(());
/*  91 */         }s -> s.nameOption().canParseAny(), (Component)Component.translatable("argument.entity.options.name.description"));
/*  93 */     register("distance", parser -> {
/*     */           int start = parser.getReader().getCursor();
/*     */           MinMaxBounds.Doubles value = MinMaxBounds.Doubles.fromReader(parser.getReader());
/*     */           if ((value.min().isPresent() && ((Double)value.min().get()).doubleValue() < 0.0D) || (value.max().isPresent() && ((Double)value.max().get()).doubleValue() < 0.0D))
/*     */             throw rollbackAndThrow(parser, start, ERROR_RANGE_NEGATIVE); 
/*     */           parser.setDistance(value);
/*     */           parser.setWorldLimited();
/* 101 */         }s -> (s.getDistance() == null), (Component)Component.translatable("argument.entity.options.distance.description"));
/* 103 */     register("level", parser -> {
/*     */           int start = parser.getReader().getCursor();
/*     */           MinMaxBounds.Ints value = MinMaxBounds.Ints.fromReader(parser.getReader());
/*     */           if ((value.min().isPresent() && ((Integer)value.min().get()).intValue() < 0) || (value.max().isPresent() && ((Integer)value.max().get()).intValue() < 0))
/*     */             throw rollbackAndThrow(parser, start, ERROR_LEVEL_NEGATIVE); 
/*     */           parser.setLevel(value);
/*     */           parser.setIncludesEntities(false);
/* 111 */         }s -> (s.getLevel() == null), (Component)Component.translatable("argument.entity.options.level.description"));
/* 113 */     register("x", parser -> {
/*     */           parser.setWorldLimited();
/*     */           parser.setX(parser.getReader().readDouble());
/* 116 */         }s -> (s.getX() == null), (Component)Component.translatable("argument.entity.options.x.description"));
/* 118 */     register("y", parser -> {
/*     */           parser.setWorldLimited();
/*     */           parser.setY(parser.getReader().readDouble());
/* 121 */         }s -> (s.getY() == null), (Component)Component.translatable("argument.entity.options.y.description"));
/* 123 */     register("z", parser -> {
/*     */           parser.setWorldLimited();
/*     */           parser.setZ(parser.getReader().readDouble());
/* 126 */         }s -> (s.getZ() == null), (Component)Component.translatable("argument.entity.options.z.description"));
/* 128 */     register("dx", parser -> {
/*     */           parser.setWorldLimited();
/*     */           parser.setDeltaX(parser.getReader().readDouble());
/* 131 */         }s -> (s.getDeltaX() == null), (Component)Component.translatable("argument.entity.options.dx.description"));
/* 133 */     register("dy", parser -> {
/*     */           parser.setWorldLimited();
/*     */           parser.setDeltaY(parser.getReader().readDouble());
/* 136 */         }s -> (s.getDeltaY() == null), (Component)Component.translatable("argument.entity.options.dy.description"));
/* 138 */     register("dz", parser -> {
/*     */           parser.setWorldLimited();
/*     */           parser.setDeltaZ(parser.getReader().readDouble());
/* 141 */         }s -> (s.getDeltaZ() == null), (Component)Component.translatable("argument.entity.options.dz.description"));
/* 143 */     register("x_rotation", parser -> parser.setRotX(MinMaxBounds.FloatDegrees.fromReader(parser.getReader())), s -> (s.getRotX() == null), 
/*     */         
/* 145 */         (Component)Component.translatable("argument.entity.options.x_rotation.description"));
/* 147 */     register("y_rotation", parser -> parser.setRotY(MinMaxBounds.FloatDegrees.fromReader(parser.getReader())), s -> (s.getRotY() == null), 
/*     */         
/* 149 */         (Component)Component.translatable("argument.entity.options.y_rotation.description"));
/* 151 */     register("limit", parser -> {
/*     */           int start = parser.getReader().getCursor();
/*     */           int count = parser.getReader().readInt();
/*     */           if (count < 1)
/*     */             throw rollbackAndThrow(parser, start, ERROR_LIMIT_TOO_SMALL); 
/*     */           parser.setMaxResults(count);
/*     */           parser.limitedOption().markParsed();
/* 159 */         }s -> (!s.isCurrentEntity() && s.limitedOption().canParse()), (Component)Component.translatable("argument.entity.options.limit.description"));
/* 161 */     register("sort", parser -> {
/*     */           // Byte code:
/*     */           //   0: aload_0
/*     */           //   1: invokevirtual getReader : ()Lcom/mojang/brigadier/StringReader;
/*     */           //   4: invokevirtual getCursor : ()I
/*     */           //   7: istore_1
/*     */           //   8: aload_0
/*     */           //   9: invokevirtual getReader : ()Lcom/mojang/brigadier/StringReader;
/*     */           //   12: invokevirtual readUnquotedString : ()Ljava/lang/String;
/*     */           //   15: astore_2
/*     */           //   16: aload_0
/*     */           //   17: <illegal opcode> apply : ()Ljava/util/function/BiFunction;
/*     */           //   22: invokevirtual setSuggestions : (Ljava/util/function/BiFunction;)V
/*     */           //   25: aload_0
/*     */           //   26: aload_2
/*     */           //   27: astore_3
/*     */           //   28: iconst_m1
/*     */           //   29: istore #4
/*     */           //   31: aload_3
/*     */           //   32: invokevirtual hashCode : ()I
/*     */           //   35: lookupswitch default -> 137, -938285885 -> 108, 1510793967 -> 92, 1780188658 -> 124, 1825779806 -> 76
/*     */           //   76: aload_3
/*     */           //   77: ldc_w 'nearest'
/*     */           //   80: invokevirtual equals : (Ljava/lang/Object;)Z
/*     */           //   83: ifeq -> 137
/*     */           //   86: iconst_0
/*     */           //   87: istore #4
/*     */           //   89: goto -> 137
/*     */           //   92: aload_3
/*     */           //   93: ldc_w 'furthest'
/*     */           //   96: invokevirtual equals : (Ljava/lang/Object;)Z
/*     */           //   99: ifeq -> 137
/*     */           //   102: iconst_1
/*     */           //   103: istore #4
/*     */           //   105: goto -> 137
/*     */           //   108: aload_3
/*     */           //   109: ldc_w 'random'
/*     */           //   112: invokevirtual equals : (Ljava/lang/Object;)Z
/*     */           //   115: ifeq -> 137
/*     */           //   118: iconst_2
/*     */           //   119: istore #4
/*     */           //   121: goto -> 137
/*     */           //   124: aload_3
/*     */           //   125: ldc_w 'arbitrary'
/*     */           //   128: invokevirtual equals : (Ljava/lang/Object;)Z
/*     */           //   131: ifeq -> 137
/*     */           //   134: iconst_3
/*     */           //   135: istore #4
/*     */           //   137: iload #4
/*     */           //   139: tableswitch default -> 192, 0 -> 168, 1 -> 174, 2 -> 180, 3 -> 186
/*     */           //   168: getstatic net/minecraft/commands/arguments/selector/EntitySelectorParser.ORDER_NEAREST : Ljava/util/function/BiConsumer;
/*     */           //   171: goto -> 202
/*     */           //   174: getstatic net/minecraft/commands/arguments/selector/EntitySelectorParser.ORDER_FURTHEST : Ljava/util/function/BiConsumer;
/*     */           //   177: goto -> 202
/*     */           //   180: getstatic net/minecraft/commands/arguments/selector/EntitySelectorParser.ORDER_RANDOM : Ljava/util/function/BiConsumer;
/*     */           //   183: goto -> 202
/*     */           //   186: getstatic net/minecraft/commands/arguments/selector/EntitySelector.ORDER_ARBITRARY : Ljava/util/function/BiConsumer;
/*     */           //   189: goto -> 202
/*     */           //   192: aload_0
/*     */           //   193: iload_1
/*     */           //   194: getstatic net/minecraft/commands/arguments/selector/options/EntitySelectorOptions.ERROR_SORT_UNKNOWN : Lcom/mojang/brigadier/exceptions/DynamicCommandExceptionType;
/*     */           //   197: aload_2
/*     */           //   198: invokestatic rollbackAndThrow : (Lnet/minecraft/commands/arguments/selector/EntitySelectorParser;ILcom/mojang/brigadier/exceptions/DynamicCommandExceptionType;Ljava/lang/String;)Lcom/mojang/brigadier/exceptions/CommandSyntaxException;
/*     */           //   201: athrow
/*     */           //   202: invokevirtual setOrder : (Ljava/util/function/BiConsumer;)V
/*     */           //   205: aload_0
/*     */           //   206: invokevirtual sortedOption : ()Lnet/minecraft/commands/arguments/selector/options/SetOnceOptionState;
/*     */           //   209: invokevirtual markParsed : ()V
/*     */           //   212: return
/*     */           // Line number table:
/*     */           //   Java source line number -> byte code offset
/*     */           //   #162	-> 0
/*     */           //   #163	-> 8
/*     */           //   #164	-> 16
/*     */           //   #165	-> 25
/*     */           //   #166	-> 168
/*     */           //   #167	-> 174
/*     */           //   #168	-> 180
/*     */           //   #169	-> 186
/*     */           //   #171	-> 192
/*     */           //   #165	-> 202
/*     */           //   #174	-> 205
/*     */           //   #175	-> 212
/*     */           // Local variable table:
/*     */           //   start	length	slot	name	descriptor
/*     */           //   28	174	3	s0$	Ljava/lang/String;
/*     */           //   31	171	4	tmp1$	I
/*     */           //   0	213	0	parser	Lnet/minecraft/commands/arguments/selector/EntitySelectorParser;
/*     */           //   8	205	1	start	I
/*     */           //   16	197	2	name	Ljava/lang/String;
/* 175 */         }s -> (!s.isCurrentEntity() && s.sortedOption().canParse()), (Component)Component.translatable("argument.entity.options.sort.description"));
/* 177 */     register("gamemode", parser -> {
/*     */           InvertableSetOptionState state = parser.gamemodeOption();
/*     */           parser.setSuggestions(());
/*     */           int start = parser.getReader().getCursor();
/*     */           boolean inverted = parser.shouldInvertValue();
/*     */           if (!state.canParseElement(inverted))
/*     */             throw rollbackAndThrow(parser, start, ERROR_INAPPLICABLE_OPTION, "gamemode"); 
/*     */           String name = parser.getReader().readUnquotedString();
/*     */           GameType expected = GameType.byName(name, null);
/*     */           if (expected == null)
/*     */             throw rollbackAndThrow(parser, start, ERROR_GAME_MODE_INVALID, name); 
/*     */           parser.setIncludesEntities(false);
/*     */           parser.addPredicate(());
/*     */           state.markParsedElement(inverted);
/* 226 */         }s -> s.gamemodeOption().canParseAny(), (Component)Component.translatable("argument.entity.options.gamemode.description"));
/* 228 */     register("team", parser -> {
/*     */           InvertableSetOptionState state = parser.teamOption();
/*     */           int start = parser.getReader().getCursor();
/*     */           boolean inverted = parser.shouldInvertValue();
/*     */           String expected = parser.getReader().readUnquotedString();
/*     */           if (!state.canParseElement(inverted))
/*     */             throw rollbackAndThrow(parser, start, ERROR_INAPPLICABLE_OPTION, "team"); 
/*     */           parser.addPredicate(());
/*     */           state.markParsedElement(inverted);
/* 245 */         }s -> s.teamOption().canParseAny(), (Component)Component.translatable("argument.entity.options.team.description"));
/* 247 */     register("type", parser -> {
/*     */           InvertableSetOptionState state = parser.typeOption();
/*     */           parser.setSuggestions(());
/*     */           int start = parser.getReader().getCursor();
/*     */           boolean inverted = parser.shouldInvertValue();
/*     */           if (parser.isTag()) {
/*     */             if (!state.canParseAnyTag())
/*     */               throw rollbackAndThrow(parser, start, ERROR_INAPPLICABLE_OPTION, "type"); 
/*     */             Identifier id = Identifier.read(parser.getReader());
/*     */             if (!state.canParseTag(id))
/*     */               throw rollbackAndThrow(parser, start, ERROR_INAPPLICABLE_OPTION, "type"); 
/*     */             TagKey<EntityType<?>> key = TagKey.create(Registries.ENTITY_TYPE, id);
/*     */             parser.addPredicate(());
/*     */             state.markParsedTag(id);
/*     */           } else {
/*     */             if (!state.canParseElement(inverted))
/*     */               throw rollbackAndThrow(parser, start, ERROR_INAPPLICABLE_OPTION, "type"); 
/*     */             Identifier id = Identifier.read(parser.getReader());
/*     */             EntityType<?> type = (EntityType)BuiltInRegistries.ENTITY_TYPE.getOptional(id).orElseThrow(());
/*     */             if (Objects.equals(EntityTypes.PLAYER, type) && !inverted)
/*     */               parser.setIncludesEntities(false); 
/*     */             parser.addPredicate(());
/*     */             if (!inverted)
/*     */               parser.limitToType(type); 
/*     */             state.markParsedElement(inverted);
/*     */           } 
/* 299 */         }s -> s.typeOption().canParseAny(), (Component)Component.translatable("argument.entity.options.type.description"));
/* 301 */     register("tag", parser -> {
/*     */           boolean inverted = parser.shouldInvertValue();
/*     */           String tag = parser.getReader().readUnquotedString();
/*     */           parser.addPredicate(());
/* 301 */         }ALWAYS_AVAILABLE, 
/*     */         
/* 312 */         (Component)Component.translatable("argument.entity.options.tag.description"));
/* 314 */     register("nbt", parser -> {
/*     */           boolean inverted = parser.shouldInvertValue();
/*     */           CompoundTag tag = TagParser.parseCompoundAsArgument(parser.getReader());
/*     */           parser.addPredicate(());
/* 314 */         }ALWAYS_AVAILABLE, 
/*     */         
/* 331 */         (Component)Component.translatable("argument.entity.options.nbt.description"));
/* 333 */     register("scores", parser -> {
/*     */           StringReader reader = parser.getReader();
/*     */           Map<String, MinMaxBounds.Ints> expected = Maps.newHashMap();
/*     */           reader.expect('{');
/*     */           reader.skipWhitespace();
/*     */           while (reader.canRead() && reader.peek() != '}') {
/*     */             reader.skipWhitespace();
/*     */             String name = reader.readUnquotedString();
/*     */             reader.skipWhitespace();
/*     */             reader.expect('=');
/*     */             reader.skipWhitespace();
/*     */             MinMaxBounds.Ints value = MinMaxBounds.Ints.fromReader(reader);
/*     */             expected.put(name, value);
/*     */             reader.skipWhitespace();
/*     */             if (reader.canRead() && reader.peek() == ',')
/*     */               reader.skip(); 
/*     */           } 
/*     */           reader.expect('}');
/*     */           if (!expected.isEmpty())
/*     */             parser.addPredicate(()); 
/*     */           parser.scoresOption().markParsed();
/* 376 */         }s -> s.scoresOption().canParse(), (Component)Component.translatable("argument.entity.options.scores.description"));
/* 378 */     register("advancements", parser -> {
/*     */           StringReader reader = parser.getReader();
/*     */           Map<Identifier, Predicate<AdvancementProgress>> expected = Maps.newHashMap();
/*     */           reader.expect('{');
/*     */           reader.skipWhitespace();
/*     */           while (reader.canRead() && reader.peek() != '}') {
/*     */             reader.skipWhitespace();
/*     */             Identifier name = Identifier.read(reader);
/*     */             reader.skipWhitespace();
/*     */             reader.expect('=');
/*     */             reader.skipWhitespace();
/*     */             if (reader.canRead() && reader.peek() == '{') {
/*     */               Map<String, Predicate<CriterionProgress>> progress = Maps.newHashMap();
/*     */               reader.skipWhitespace();
/*     */               reader.expect('{');
/*     */               reader.skipWhitespace();
/*     */               while (reader.canRead() && reader.peek() != '}') {
/*     */                 reader.skipWhitespace();
/*     */                 String criterion = reader.readUnquotedString();
/*     */                 reader.skipWhitespace();
/*     */                 reader.expect('=');
/*     */                 reader.skipWhitespace();
/*     */                 boolean value = reader.readBoolean();
/*     */                 progress.put(criterion, ());
/*     */                 reader.skipWhitespace();
/*     */                 if (reader.canRead() && reader.peek() == ',')
/*     */                   reader.skip(); 
/*     */               } 
/*     */               reader.skipWhitespace();
/*     */               reader.expect('}');
/*     */               reader.skipWhitespace();
/*     */               expected.put(name, ());
/*     */             } else {
/*     */               boolean value = reader.readBoolean();
/*     */               expected.put(name, ());
/*     */             } 
/*     */             reader.skipWhitespace();
/*     */             if (reader.canRead() && reader.peek() == ',')
/*     */               reader.skip(); 
/*     */           } 
/*     */           reader.expect('}');
/*     */           if (!expected.isEmpty()) {
/*     */             parser.addPredicate(());
/*     */             parser.setIncludesEntities(false);
/*     */           } 
/*     */           parser.advancementsOption().markParsed();
/* 455 */         }s -> s.advancementsOption().canParse(), (Component)Component.translatable("argument.entity.options.advancements.description"));
/* 457 */     register("predicate", parser -> {
/*     */           boolean inverted = parser.shouldInvertValue();
/*     */           ResourceKey<LootItemCondition> id = ResourceKey.create(Registries.PREDICATE, Identifier.read(parser.getReader()));
/*     */           parser.addPredicate(());
/* 457 */         }ALWAYS_AVAILABLE, 
/*     */         
/* 477 */         (Component)Component.translatable("argument.entity.options.predicate.description"));
/*     */   }
/*     */   
/*     */   private static CommandSyntaxException rollbackAndThrow(EntitySelectorParser parser, int start, SimpleCommandExceptionType type) {
/* 481 */     parser.getReader().setCursor(start);
/* 482 */     return type.createWithContext((ImmutableStringReader)parser.getReader());
/*     */   }
/*     */   
/*     */   private static CommandSyntaxException rollbackAndThrow(EntitySelectorParser parser, int start, DynamicCommandExceptionType type, String argument) {
/* 486 */     parser.getReader().setCursor(start);
/* 487 */     return type.createWithContext((ImmutableStringReader)parser.getReader(), argument);
/*     */   }
/*     */   
/*     */   public static Modifier get(EntitySelectorParser parser, String key, int start) throws CommandSyntaxException {
/* 491 */     Option option = OPTIONS.get(key);
/* 492 */     if (option != null) {
/* 493 */       if (option.canUse.test(parser))
/* 494 */         return option.modifier; 
/* 496 */       throw rollbackAndThrow(parser, start, ERROR_INAPPLICABLE_OPTION, key);
/*     */     } 
/* 499 */     throw rollbackAndThrow(parser, start, ERROR_UNKNOWN_OPTION, key);
/*     */   }
/*     */   
/*     */   public static void suggestNames(EntitySelectorParser parser, SuggestionsBuilder builder) {
/* 504 */     String lowerPrefix = builder.getRemaining().toLowerCase(Locale.ROOT);
/* 505 */     for (Map.Entry<String, Option> entry : OPTIONS.entrySet()) {
/* 506 */       if (((Option)entry.getValue()).canUse.test(parser) && ((String)entry.getKey()).toLowerCase(Locale.ROOT).startsWith(lowerPrefix))
/* 507 */         builder.suggest((String)entry.getKey() + "=", (Message)((Option)entry.getValue()).description); 
/*     */     } 
/*     */   }
/*     */   
/*     */   private static final class Option extends Record {
/*     */     private final EntitySelectorOptions.Modifier modifier;
/*     */     
/*     */     private final Predicate<EntitySelectorParser> canUse;
/*     */     
/*     */     private final Component description;
/*     */     
/*     */     private Option(EntitySelectorOptions.Modifier modifier, Predicate<EntitySelectorParser> canUse, Component description) {
/* 517 */       this.modifier = modifier;
/* 517 */       this.canUse = canUse;
/* 517 */       this.description = description;
/*     */     }
/*     */     
/*     */     public final String toString() {
/*     */       // Byte code:
/*     */       //   0: aload_0
/*     */       //   1: <illegal opcode> toString : (Lnet/minecraft/commands/arguments/selector/options/EntitySelectorOptions$Option;)Ljava/lang/String;
/*     */       //   6: areturn
/*     */       // Line number table:
/*     */       //   Java source line number -> byte code offset
/*     */       //   #517	-> 0
/*     */       // Local variable table:
/*     */       //   start	length	slot	name	descriptor
/*     */       //   0	7	0	this	Lnet/minecraft/commands/arguments/selector/options/EntitySelectorOptions$Option;
/*     */     }
/*     */     
/*     */     public final int hashCode() {
/*     */       // Byte code:
/*     */       //   0: aload_0
/*     */       //   1: <illegal opcode> hashCode : (Lnet/minecraft/commands/arguments/selector/options/EntitySelectorOptions$Option;)I
/*     */       //   6: ireturn
/*     */       // Line number table:
/*     */       //   Java source line number -> byte code offset
/*     */       //   #517	-> 0
/*     */       // Local variable table:
/*     */       //   start	length	slot	name	descriptor
/*     */       //   0	7	0	this	Lnet/minecraft/commands/arguments/selector/options/EntitySelectorOptions$Option;
/*     */     }
/*     */     
/*     */     public final boolean equals(Object o) {
/*     */       // Byte code:
/*     */       //   0: aload_0
/*     */       //   1: aload_1
/*     */       //   2: <illegal opcode> equals : (Lnet/minecraft/commands/arguments/selector/options/EntitySelectorOptions$Option;Ljava/lang/Object;)Z
/*     */       //   7: ireturn
/*     */       // Line number table:
/*     */       //   Java source line number -> byte code offset
/*     */       //   #517	-> 0
/*     */       // Local variable table:
/*     */       //   start	length	slot	name	descriptor
/*     */       //   0	8	0	this	Lnet/minecraft/commands/arguments/selector/options/EntitySelectorOptions$Option;
/*     */       //   0	8	1	o	Ljava/lang/Object;
/*     */     }
/*     */     
/*     */     public EntitySelectorOptions.Modifier modifier() {
/* 517 */       return this.modifier;
/*     */     }
/*     */     
/*     */     public Predicate<EntitySelectorParser> canUse() {
/* 517 */       return this.canUse;
/*     */     }
/*     */     
/*     */     public Component description() {
/* 517 */       return this.description;
/*     */     }
/*     */   }
/*     */   
/*     */   @FunctionalInterface
/*     */   public static interface Modifier {
/*     */     void handle(EntitySelectorParser param1EntitySelectorParser) throws CommandSyntaxException;
/*     */   }
/*     */ }


/* Location:              C:\Users\karol\AppData\Roaming\ModrinthApp\meta\versions\26.2-0.19.5\26.2-0.19.5.jar!\net\minecraft\commands\arguments\selector\options\EntitySelectorOptions.class
 * Java compiler version: 25 (69.0)
 * JD-Core Version:       1.1.3
 */