/*     */ package net.minecraft.commands.arguments.blocks;
/*     */ 
/*     */ import com.google.common.collect.Maps;
/*     */ import com.mojang.brigadier.ImmutableStringReader;
/*     */ import com.mojang.brigadier.Message;
/*     */ import com.mojang.brigadier.StringReader;
/*     */ import com.mojang.brigadier.exceptions.CommandSyntaxException;
/*     */ import com.mojang.brigadier.exceptions.Dynamic2CommandExceptionType;
/*     */ import com.mojang.brigadier.exceptions.Dynamic3CommandExceptionType;
/*     */ import com.mojang.brigadier.exceptions.DynamicCommandExceptionType;
/*     */ import com.mojang.brigadier.exceptions.SimpleCommandExceptionType;
/*     */ import com.mojang.brigadier.suggestion.Suggestions;
/*     */ import com.mojang.brigadier.suggestion.SuggestionsBuilder;
/*     */ import com.mojang.datafixers.util.Either;
/*     */ import java.util.Locale;
/*     */ import java.util.Map;
/*     */ import java.util.Optional;
/*     */ import java.util.concurrent.CompletableFuture;
/*     */ import java.util.function.Consumer;
/*     */ import java.util.function.Function;
/*     */ import net.minecraft.commands.SharedSuggestionProvider;
/*     */ import net.minecraft.core.Holder;
/*     */ import net.minecraft.core.HolderLookup;
/*     */ import net.minecraft.core.HolderSet;
/*     */ import net.minecraft.core.registries.Registries;
/*     */ import net.minecraft.nbt.CompoundTag;
/*     */ import net.minecraft.nbt.TagParser;
/*     */ import net.minecraft.network.chat.Component;
/*     */ import net.minecraft.resources.Identifier;
/*     */ import net.minecraft.resources.ResourceKey;
/*     */ import net.minecraft.tags.TagKey;
/*     */ import net.minecraft.world.level.block.Block;
/*     */ import net.minecraft.world.level.block.state.BlockState;
/*     */ import net.minecraft.world.level.block.state.StateDefinition;
/*     */ import net.minecraft.world.level.block.state.properties.Property;
/*     */ 
/*     */ public class BlockStateParser {
/*  38 */   public static final SimpleCommandExceptionType ERROR_NO_TAGS_ALLOWED = new SimpleCommandExceptionType((Message)Component.translatable("argument.block.tag.disallowed"));
/*     */   
/*     */   public static final DynamicCommandExceptionType ERROR_UNKNOWN_BLOCK;
/*     */   
/*     */   public static final Dynamic2CommandExceptionType ERROR_UNKNOWN_PROPERTY;
/*     */   
/*     */   public static final Dynamic2CommandExceptionType ERROR_DUPLICATE_PROPERTY;
/*     */   
/*     */   public static final Dynamic3CommandExceptionType ERROR_INVALID_VALUE;
/*     */   
/*     */   public static final Dynamic2CommandExceptionType ERROR_EXPECTED_VALUE;
/*     */   
/*     */   static {
/*  39 */     ERROR_UNKNOWN_BLOCK = new DynamicCommandExceptionType(id -> Component.translatableEscape("argument.block.id.invalid", new Object[] { id }));
/*  40 */     ERROR_UNKNOWN_PROPERTY = new Dynamic2CommandExceptionType((block, property) -> Component.translatableEscape("argument.block.property.unknown", new Object[] { block, property }));
/*  41 */     ERROR_DUPLICATE_PROPERTY = new Dynamic2CommandExceptionType((block, property) -> Component.translatableEscape("argument.block.property.duplicate", new Object[] { property, block }));
/*  42 */     ERROR_INVALID_VALUE = new Dynamic3CommandExceptionType((block, property, value) -> Component.translatableEscape("argument.block.property.invalid", new Object[] { block, value, property }));
/*  43 */     ERROR_EXPECTED_VALUE = new Dynamic2CommandExceptionType((block, property) -> Component.translatableEscape("argument.block.property.novalue", new Object[] { property, block }));
/*     */   }
/*     */   
/*  44 */   public static final SimpleCommandExceptionType ERROR_EXPECTED_END_OF_PROPERTIES = new SimpleCommandExceptionType((Message)Component.translatable("argument.block.property.unclosed"));
/*     */   
/*     */   public static final DynamicCommandExceptionType ERROR_UNKNOWN_TAG;
/*     */   
/*     */   private static final char SYNTAX_START_PROPERTIES = '[';
/*     */   
/*     */   private static final char SYNTAX_START_NBT = '{';
/*     */   
/*     */   private static final char SYNTAX_END_PROPERTIES = ']';
/*     */   
/*     */   private static final char SYNTAX_EQUALS = '=';
/*     */   
/*     */   private static final char SYNTAX_PROPERTY_SEPARATOR = ',';
/*     */   
/*     */   private static final char SYNTAX_TAG = '#';
/*     */   
/*     */   static {
/*  45 */     ERROR_UNKNOWN_TAG = new DynamicCommandExceptionType(tag -> Component.translatableEscape("arguments.block.tag.unknown", new Object[] { tag }));
/*     */   }
/*     */   
/*  54 */   private static final Function<SuggestionsBuilder, CompletableFuture<Suggestions>> SUGGEST_NOTHING = SuggestionsBuilder::buildFuture;
/*     */   
/*     */   private final HolderLookup<Block> blocks;
/*     */   
/*     */   private final StringReader reader;
/*     */   
/*     */   private final boolean forTesting;
/*     */   
/*     */   private final boolean allowNbt;
/*     */   
/*  60 */   private final Map<Property<?>, Comparable<?>> properties = Maps.newHashMap();
/*     */   
/*  61 */   private final Map<String, String> vagueProperties = Maps.newHashMap();
/*     */   
/*  62 */   private Identifier id = Identifier.withDefaultNamespace("");
/*     */   
/*     */   private StateDefinition<Block, BlockState> definition;
/*     */   
/*     */   private BlockState state;
/*     */   
/*     */   private CompoundTag nbt;
/*     */   
/*     */   private HolderSet<Block> tag;
/*     */   
/*  67 */   private Function<SuggestionsBuilder, CompletableFuture<Suggestions>> suggestions = SUGGEST_NOTHING;
/*     */   
/*     */   private BlockStateParser(HolderLookup<Block> blocks, StringReader reader, boolean forTesting, boolean allowNbt) {
/*  70 */     this.blocks = blocks;
/*  71 */     this.reader = reader;
/*  72 */     this.forTesting = forTesting;
/*  73 */     this.allowNbt = allowNbt;
/*     */   }
/*     */   
/*     */   public static final class BlockResult extends Record {
/*     */     private final BlockState blockState;
/*     */     
/*     */     private final Map<Property<?>, Comparable<?>> properties;
/*     */     
/*     */     private final CompoundTag nbt;
/*     */     
/*     */     public BlockResult(BlockState blockState, Map<Property<?>, Comparable<?>> properties, CompoundTag nbt) {
/*  76 */       this.blockState = blockState;
/*  76 */       this.properties = properties;
/*  76 */       this.nbt = nbt;
/*     */     }
/*     */     
/*     */     public final String toString() {
/*     */       // Byte code:
/*     */       //   0: aload_0
/*     */       //   1: <illegal opcode> toString : (Lnet/minecraft/commands/arguments/blocks/BlockStateParser$BlockResult;)Ljava/lang/String;
/*     */       //   6: areturn
/*     */       // Line number table:
/*     */       //   Java source line number -> byte code offset
/*     */       //   #76	-> 0
/*     */       // Local variable table:
/*     */       //   start	length	slot	name	descriptor
/*     */       //   0	7	0	this	Lnet/minecraft/commands/arguments/blocks/BlockStateParser$BlockResult;
/*     */     }
/*     */     
/*     */     public final int hashCode() {
/*     */       // Byte code:
/*     */       //   0: aload_0
/*     */       //   1: <illegal opcode> hashCode : (Lnet/minecraft/commands/arguments/blocks/BlockStateParser$BlockResult;)I
/*     */       //   6: ireturn
/*     */       // Line number table:
/*     */       //   Java source line number -> byte code offset
/*     */       //   #76	-> 0
/*     */       // Local variable table:
/*     */       //   start	length	slot	name	descriptor
/*     */       //   0	7	0	this	Lnet/minecraft/commands/arguments/blocks/BlockStateParser$BlockResult;
/*     */     }
/*     */     
/*     */     public final boolean equals(Object o) {
/*     */       // Byte code:
/*     */       //   0: aload_0
/*     */       //   1: aload_1
/*     */       //   2: <illegal opcode> equals : (Lnet/minecraft/commands/arguments/blocks/BlockStateParser$BlockResult;Ljava/lang/Object;)Z
/*     */       //   7: ireturn
/*     */       // Line number table:
/*     */       //   Java source line number -> byte code offset
/*     */       //   #76	-> 0
/*     */       // Local variable table:
/*     */       //   start	length	slot	name	descriptor
/*     */       //   0	8	0	this	Lnet/minecraft/commands/arguments/blocks/BlockStateParser$BlockResult;
/*     */       //   0	8	1	o	Ljava/lang/Object;
/*     */     }
/*     */     
/*     */     public BlockState blockState() {
/*  76 */       return this.blockState;
/*     */     }
/*     */     
/*     */     public Map<Property<?>, Comparable<?>> properties() {
/*  76 */       return this.properties;
/*     */     }
/*     */     
/*     */     public CompoundTag nbt() {
/*  76 */       return this.nbt;
/*     */     }
/*     */   }
/*     */   
/*     */   public static final class TagResult extends Record {
/*     */     private final HolderSet<Block> tag;
/*     */     
/*     */     private final Map<String, String> vagueProperties;
/*     */     
/*     */     private final CompoundTag nbt;
/*     */     
/*     */     public TagResult(HolderSet<Block> tag, Map<String, String> vagueProperties, CompoundTag nbt) {
/*  78 */       this.tag = tag;
/*  78 */       this.vagueProperties = vagueProperties;
/*  78 */       this.nbt = nbt;
/*     */     }
/*     */     
/*     */     public final String toString() {
/*     */       // Byte code:
/*     */       //   0: aload_0
/*     */       //   1: <illegal opcode> toString : (Lnet/minecraft/commands/arguments/blocks/BlockStateParser$TagResult;)Ljava/lang/String;
/*     */       //   6: areturn
/*     */       // Line number table:
/*     */       //   Java source line number -> byte code offset
/*     */       //   #78	-> 0
/*     */       // Local variable table:
/*     */       //   start	length	slot	name	descriptor
/*     */       //   0	7	0	this	Lnet/minecraft/commands/arguments/blocks/BlockStateParser$TagResult;
/*     */     }
/*     */     
/*     */     public final int hashCode() {
/*     */       // Byte code:
/*     */       //   0: aload_0
/*     */       //   1: <illegal opcode> hashCode : (Lnet/minecraft/commands/arguments/blocks/BlockStateParser$TagResult;)I
/*     */       //   6: ireturn
/*     */       // Line number table:
/*     */       //   Java source line number -> byte code offset
/*     */       //   #78	-> 0
/*     */       // Local variable table:
/*     */       //   start	length	slot	name	descriptor
/*     */       //   0	7	0	this	Lnet/minecraft/commands/arguments/blocks/BlockStateParser$TagResult;
/*     */     }
/*     */     
/*     */     public final boolean equals(Object o) {
/*     */       // Byte code:
/*     */       //   0: aload_0
/*     */       //   1: aload_1
/*     */       //   2: <illegal opcode> equals : (Lnet/minecraft/commands/arguments/blocks/BlockStateParser$TagResult;Ljava/lang/Object;)Z
/*     */       //   7: ireturn
/*     */       // Line number table:
/*     */       //   Java source line number -> byte code offset
/*     */       //   #78	-> 0
/*     */       // Local variable table:
/*     */       //   start	length	slot	name	descriptor
/*     */       //   0	8	0	this	Lnet/minecraft/commands/arguments/blocks/BlockStateParser$TagResult;
/*     */       //   0	8	1	o	Ljava/lang/Object;
/*     */     }
/*     */     
/*     */     public HolderSet<Block> tag() {
/*  78 */       return this.tag;
/*     */     }
/*     */     
/*     */     public Map<String, String> vagueProperties() {
/*  78 */       return this.vagueProperties;
/*     */     }
/*     */     
/*     */     public CompoundTag nbt() {
/*  78 */       return this.nbt;
/*     */     }
/*     */   }
/*     */   
/*     */   public static BlockResult parseForBlock(HolderLookup<Block> blocks, String value, boolean allowNbt) throws CommandSyntaxException {
/*  81 */     return parseForBlock(blocks, new StringReader(value), allowNbt);
/*     */   }
/*     */   
/*     */   public static BlockResult parseForBlock(HolderLookup<Block> blocks, StringReader reader, boolean allowNbt) throws CommandSyntaxException {
/*  85 */     int cursor = reader.getCursor();
/*     */     try {
/*  87 */       BlockStateParser parser = new BlockStateParser(blocks, reader, false, allowNbt);
/*  88 */       parser.parse();
/*  90 */       return new BlockResult(parser.state, parser.properties, parser.nbt);
/*  91 */     } catch (CommandSyntaxException e) {
/*  92 */       reader.setCursor(cursor);
/*  93 */       throw e;
/*     */     } 
/*     */   }
/*     */   
/*     */   public static Either<BlockResult, TagResult> parseForTesting(HolderLookup<Block> blocks, String value, boolean allowNbt) throws CommandSyntaxException {
/*  98 */     return parseForTesting(blocks, new StringReader(value), allowNbt);
/*     */   }
/*     */   
/*     */   public static Either<BlockResult, TagResult> parseForTesting(HolderLookup<Block> blocks, StringReader reader, boolean allowNbt) throws CommandSyntaxException {
/* 102 */     int cursor = reader.getCursor();
/*     */     try {
/* 104 */       BlockStateParser parser = new BlockStateParser(blocks, reader, true, allowNbt);
/* 105 */       parser.parse();
/* 106 */       if (parser.tag != null)
/* 107 */         return Either.right(new TagResult(parser.tag, parser.vagueProperties, parser.nbt)); 
/* 109 */       return Either.left(new BlockResult(parser.state, parser.properties, parser.nbt));
/* 110 */     } catch (CommandSyntaxException e) {
/* 111 */       reader.setCursor(cursor);
/* 112 */       throw e;
/*     */     } 
/*     */   }
/*     */   
/*     */   public static CompletableFuture<Suggestions> fillSuggestions(HolderLookup<Block> blocks, SuggestionsBuilder builder, boolean forTesting, boolean allowNbt) {
/* 117 */     StringReader reader = new StringReader(builder.getInput());
/* 118 */     reader.setCursor(builder.getStart());
/* 120 */     BlockStateParser parser = new BlockStateParser(blocks, reader, forTesting, allowNbt);
/*     */     try {
/* 122 */       parser.parse();
/* 123 */     } catch (CommandSyntaxException commandSyntaxException) {}
/* 126 */     return parser.suggestions.apply(builder.createOffset(reader.getCursor()));
/*     */   }
/*     */   
/*     */   private void parse() throws CommandSyntaxException {
/* 130 */     if (this.forTesting) {
/* 131 */       this.suggestions = this::suggestBlockIdOrTag;
/*     */     } else {
/* 133 */       this.suggestions = this::suggestItem;
/*     */     } 
/* 135 */     if (this.reader.canRead() && this.reader.peek() == '#') {
/* 136 */       readTag();
/* 137 */       this.suggestions = this::suggestOpenVaguePropertiesOrNbt;
/* 138 */       if (this.reader.canRead() && this.reader.peek() == '[') {
/* 139 */         readVagueProperties();
/* 140 */         this.suggestions = this::suggestOpenNbt;
/*     */       } 
/*     */     } else {
/* 143 */       readBlock();
/* 144 */       this.suggestions = this::suggestOpenPropertiesOrNbt;
/* 145 */       if (this.reader.canRead() && this.reader.peek() == '[') {
/* 146 */         readProperties();
/* 147 */         this.suggestions = this::suggestOpenNbt;
/*     */       } 
/*     */     } 
/* 150 */     if (this.allowNbt && this.reader.canRead() && this.reader.peek() == '{') {
/* 151 */       this.suggestions = SUGGEST_NOTHING;
/* 152 */       readNbt();
/*     */     } 
/*     */   }
/*     */   
/*     */   private CompletableFuture<Suggestions> suggestPropertyNameOrEnd(SuggestionsBuilder builder) {
/* 157 */     if (builder.getRemaining().isEmpty())
/* 158 */       builder.suggest(String.valueOf(']')); 
/* 161 */     return suggestPropertyName(builder);
/*     */   }
/*     */   
/*     */   private CompletableFuture<Suggestions> suggestVaguePropertyNameOrEnd(SuggestionsBuilder builder) {
/* 165 */     if (builder.getRemaining().isEmpty())
/* 166 */       builder.suggest(String.valueOf(']')); 
/* 168 */     return suggestVaguePropertyName(builder);
/*     */   }
/*     */   
/*     */   private CompletableFuture<Suggestions> suggestPropertyName(SuggestionsBuilder builder) {
/* 172 */     String prefix = builder.getRemaining().toLowerCase(Locale.ROOT);
/* 173 */     for (Property<?> property : (Iterable<Property<?>>)this.state.getProperties()) {
/* 174 */       if (!this.properties.containsKey(property) && property.getName().startsWith(prefix))
/* 175 */         builder.suggest(property.getName() + "="); 
/*     */     } 
/* 178 */     return builder.buildFuture();
/*     */   }
/*     */   
/*     */   private CompletableFuture<Suggestions> suggestVaguePropertyName(SuggestionsBuilder builder) {
/* 182 */     String prefix = builder.getRemaining().toLowerCase(Locale.ROOT);
/* 183 */     if (this.tag != null)
/* 184 */       for (Holder<Block> block : this.tag) {
/* 185 */         for (Property<?> property : (Iterable<Property<?>>)((Block)block.value()).getStateDefinition().getProperties()) {
/* 186 */           if (!this.vagueProperties.containsKey(property.getName()) && property.getName().startsWith(prefix))
/* 187 */             builder.suggest(property.getName() + "="); 
/*     */         } 
/*     */       }  
/* 192 */     return builder.buildFuture();
/*     */   }
/*     */   
/*     */   private CompletableFuture<Suggestions> suggestOpenNbt(SuggestionsBuilder builder) {
/* 196 */     if (builder.getRemaining().isEmpty() && hasBlockEntity())
/* 197 */       builder.suggest(String.valueOf('{')); 
/* 199 */     return builder.buildFuture();
/*     */   }
/*     */   
/*     */   private boolean hasBlockEntity() {
/* 203 */     if (this.state != null)
/* 204 */       return this.state.hasBlockEntity(); 
/* 207 */     if (this.tag != null)
/* 208 */       for (Holder<Block> block : this.tag) {
/* 209 */         if (((Block)block.value()).defaultBlockState().hasBlockEntity())
/* 210 */           return true; 
/*     */       }  
/* 215 */     return false;
/*     */   }
/*     */   
/*     */   private CompletableFuture<Suggestions> suggestEquals(SuggestionsBuilder builder) {
/* 219 */     if (builder.getRemaining().isEmpty())
/* 220 */       builder.suggest(String.valueOf('=')); 
/* 222 */     return builder.buildFuture();
/*     */   }
/*     */   
/*     */   private CompletableFuture<Suggestions> suggestNextPropertyOrEnd(SuggestionsBuilder builder) {
/* 226 */     if (builder.getRemaining().isEmpty())
/* 227 */       builder.suggest(String.valueOf(']')); 
/* 229 */     if (builder.getRemaining().isEmpty() && this.properties.size() < this.state.getProperties().size())
/* 230 */       builder.suggest(String.valueOf(',')); 
/* 232 */     return builder.buildFuture();
/*     */   }
/*     */   
/*     */   private static <T extends Comparable<T>> SuggestionsBuilder addSuggestions(SuggestionsBuilder builder, Property<T> property) {
/* 236 */     for (Comparable comparable : property.getPossibleValues()) {
/* 237 */       if (comparable instanceof Integer) {
/* 237 */         Integer v = (Integer)comparable;
/* 238 */         builder.suggest(v.intValue());
/*     */         continue;
/*     */       } 
/* 240 */       builder.suggest(property.getName(comparable));
/*     */     } 
/* 243 */     return builder;
/*     */   }
/*     */   
/*     */   private CompletableFuture<Suggestions> suggestVaguePropertyValue(SuggestionsBuilder builder, String key) {
/* 247 */     boolean hasMoreProperties = false;
/* 248 */     if (this.tag != null)
/* 249 */       for (Holder<Block> blockHolder : this.tag) {
/* 250 */         Block block = (Block)blockHolder.value();
/* 251 */         Property<?> property = block.getStateDefinition().getProperty(key);
/* 252 */         if (property != null)
/* 253 */           addSuggestions(builder, property); 
/* 255 */         if (!hasMoreProperties)
/* 256 */           for (Property<?> prop : (Iterable<Property<?>>)block.getStateDefinition().getProperties()) {
/* 257 */             if (!this.vagueProperties.containsKey(prop.getName()))
/* 258 */               hasMoreProperties = true; 
/*     */           }  
/*     */       }  
/* 265 */     if (hasMoreProperties)
/* 266 */       builder.suggest(String.valueOf(',')); 
/* 268 */     builder.suggest(String.valueOf(']'));
/* 269 */     return builder.buildFuture();
/*     */   }
/*     */   
/*     */   private CompletableFuture<Suggestions> suggestOpenVaguePropertiesOrNbt(SuggestionsBuilder builder) {
/* 273 */     if (builder.getRemaining().isEmpty() && 
/* 274 */       this.tag != null) {
/*     */       int i;
/* 275 */       boolean hasProperties = false;
/* 276 */       boolean hasEntity = false;
/* 278 */       for (Holder<Block> blockHolder : this.tag) {
/* 279 */         Block block = (Block)blockHolder.value();
/* 280 */         i = hasProperties | (!block.getStateDefinition().getProperties().isEmpty() ? 1 : 0);
/* 281 */         hasEntity |= block.defaultBlockState().hasBlockEntity();
/* 283 */         if (i != 0 && hasEntity)
/*     */           break; 
/*     */       } 
/* 288 */       if (i != 0)
/* 289 */         builder.suggest(String.valueOf('[')); 
/* 292 */       if (hasEntity)
/* 293 */         builder.suggest(String.valueOf('{')); 
/*     */     } 
/* 297 */     return builder.buildFuture();
/*     */   }
/*     */   
/*     */   private CompletableFuture<Suggestions> suggestOpenPropertiesOrNbt(SuggestionsBuilder builder) {
/* 301 */     if (builder.getRemaining().isEmpty()) {
/* 302 */       if (!this.definition.getProperties().isEmpty())
/* 303 */         builder.suggest(String.valueOf('[')); 
/* 305 */       if (this.state.hasBlockEntity())
/* 306 */         builder.suggest(String.valueOf('{')); 
/*     */     } 
/* 309 */     return builder.buildFuture();
/*     */   }
/*     */   
/*     */   private CompletableFuture<Suggestions> suggestTag(SuggestionsBuilder builder) {
/* 313 */     return SharedSuggestionProvider.suggestResource(this.blocks.listTagIds().map(TagKey::location), builder, String.valueOf('#'));
/*     */   }
/*     */   
/*     */   private CompletableFuture<Suggestions> suggestItem(SuggestionsBuilder builder) {
/* 317 */     return SharedSuggestionProvider.suggestResource(this.blocks.listElementIds().map(ResourceKey::identifier), builder);
/*     */   }
/*     */   
/*     */   private CompletableFuture<Suggestions> suggestBlockIdOrTag(SuggestionsBuilder builder) {
/* 321 */     suggestTag(builder);
/* 322 */     suggestItem(builder);
/* 323 */     return builder.buildFuture();
/*     */   }
/*     */   
/*     */   private void readBlock() throws CommandSyntaxException {
/* 327 */     int start = this.reader.getCursor();
/* 328 */     this.id = Identifier.read(this.reader);
/* 333 */     Block block = (Block)((Holder.Reference)this.blocks.get(ResourceKey.create(Registries.BLOCK, this.id)).orElseThrow(() -> {
/*     */           this.reader.setCursor(start);
/*     */           return ERROR_UNKNOWN_BLOCK.createWithContext((ImmutableStringReader)this.reader, this.id.toString());
/* 333 */         })).value();
/* 335 */     this.definition = block.getStateDefinition();
/* 336 */     this.state = block.defaultBlockState();
/*     */   }
/*     */   
/*     */   private void readTag() throws CommandSyntaxException {
/* 340 */     if (!this.forTesting)
/* 341 */       throw ERROR_NO_TAGS_ALLOWED.createWithContext(this.reader); 
/* 344 */     int start = this.reader.getCursor();
/* 345 */     this.reader.expect('#');
/* 346 */     this.suggestions = this::suggestTag;
/* 347 */     Identifier id = Identifier.read(this.reader);
/* 348 */     this.tag = (HolderSet<Block>)this.blocks.get(TagKey.create(Registries.BLOCK, id)).orElseThrow(() -> {
/*     */           this.reader.setCursor(start);
/*     */           return ERROR_UNKNOWN_TAG.createWithContext((ImmutableStringReader)this.reader, id.toString());
/*     */         });
/*     */   }
/*     */   
/*     */   private void readProperties() throws CommandSyntaxException {
/* 355 */     this.reader.skip();
/* 356 */     this.suggestions = this::suggestPropertyNameOrEnd;
/* 358 */     this.reader.skipWhitespace();
/* 359 */     while (this.reader.canRead() && this.reader.peek() != ']') {
/* 360 */       this.reader.skipWhitespace();
/* 361 */       int keyStart = this.reader.getCursor();
/* 362 */       String key = this.reader.readString();
/* 363 */       Property<?> property = this.definition.getProperty(key);
/* 364 */       if (property == null) {
/* 365 */         this.reader.setCursor(keyStart);
/* 366 */         throw ERROR_UNKNOWN_PROPERTY.createWithContext(this.reader, this.id.toString(), key);
/*     */       } 
/* 368 */       if (this.properties.containsKey(property)) {
/* 369 */         this.reader.setCursor(keyStart);
/* 370 */         throw ERROR_DUPLICATE_PROPERTY.createWithContext(this.reader, this.id.toString(), key);
/*     */       } 
/* 373 */       this.reader.skipWhitespace();
/* 374 */       this.suggestions = this::suggestEquals;
/* 375 */       if (!this.reader.canRead() || this.reader.peek() != '=')
/* 376 */         throw ERROR_EXPECTED_VALUE.createWithContext(this.reader, this.id.toString(), key); 
/* 378 */       this.reader.skip();
/* 379 */       this.reader.skipWhitespace();
/* 381 */       this.suggestions = (builder -> addSuggestions(builder, property).buildFuture());
/* 382 */       int start = this.reader.getCursor();
/* 383 */       setValue(property, this.reader.readString(), start);
/* 385 */       this.suggestions = this::suggestNextPropertyOrEnd;
/* 386 */       this.reader.skipWhitespace();
/* 387 */       if (this.reader.canRead()) {
/* 388 */         if (this.reader.peek() == ',') {
/* 389 */           this.reader.skip();
/* 390 */           this.suggestions = this::suggestPropertyName;
/*     */           continue;
/*     */         } 
/* 391 */         if (this.reader.peek() == ']')
/*     */           break; 
/* 394 */         throw ERROR_EXPECTED_END_OF_PROPERTIES.createWithContext(this.reader);
/*     */       } 
/*     */     } 
/* 399 */     if (this.reader.canRead()) {
/* 400 */       this.reader.skip();
/*     */     } else {
/* 402 */       throw ERROR_EXPECTED_END_OF_PROPERTIES.createWithContext(this.reader);
/*     */     } 
/*     */   }
/*     */   
/*     */   private void readVagueProperties() throws CommandSyntaxException {
/* 407 */     this.reader.skip();
/* 408 */     this.suggestions = this::suggestVaguePropertyNameOrEnd;
/* 409 */     int valueStart = -1;
/* 411 */     this.reader.skipWhitespace();
/* 412 */     while (this.reader.canRead() && this.reader.peek() != ']') {
/* 413 */       this.reader.skipWhitespace();
/* 414 */       int keyStart = this.reader.getCursor();
/* 415 */       String key = this.reader.readString();
/* 416 */       if (this.vagueProperties.containsKey(key)) {
/* 417 */         this.reader.setCursor(keyStart);
/* 418 */         throw ERROR_DUPLICATE_PROPERTY.createWithContext(this.reader, this.id.toString(), key);
/*     */       } 
/* 421 */       this.reader.skipWhitespace();
/* 422 */       if (!this.reader.canRead() || this.reader.peek() != '=') {
/* 423 */         this.reader.setCursor(keyStart);
/* 424 */         throw ERROR_EXPECTED_VALUE.createWithContext(this.reader, this.id.toString(), key);
/*     */       } 
/* 426 */       this.reader.skip();
/* 428 */       this.reader.skipWhitespace();
/* 429 */       this.suggestions = (builder -> suggestVaguePropertyValue(builder, key));
/* 430 */       valueStart = this.reader.getCursor();
/* 431 */       String value = this.reader.readString();
/* 432 */       this.vagueProperties.put(key, value);
/* 434 */       this.reader.skipWhitespace();
/* 435 */       if (this.reader.canRead()) {
/* 436 */         valueStart = -1;
/* 437 */         if (this.reader.peek() == ',') {
/* 438 */           this.reader.skip();
/* 439 */           this.suggestions = this::suggestVaguePropertyName;
/*     */           continue;
/*     */         } 
/* 440 */         if (this.reader.peek() == ']')
/*     */           break; 
/* 443 */         throw ERROR_EXPECTED_END_OF_PROPERTIES.createWithContext(this.reader);
/*     */       } 
/*     */     } 
/* 448 */     if (this.reader.canRead()) {
/* 449 */       this.reader.skip();
/*     */     } else {
/* 451 */       if (valueStart >= 0)
/* 452 */         this.reader.setCursor(valueStart); 
/* 454 */       throw ERROR_EXPECTED_END_OF_PROPERTIES.createWithContext(this.reader);
/*     */     } 
/*     */   }
/*     */   
/*     */   private void readNbt() throws CommandSyntaxException {
/* 459 */     this.nbt = TagParser.parseCompoundAsArgument(this.reader);
/*     */   }
/*     */   
/*     */   private <T extends Comparable<T>> void setValue(Property<T> property, String raw, int start) throws CommandSyntaxException {
/* 463 */     Optional<T> value = property.getValue(raw);
/* 464 */     if (value.isPresent()) {
/* 465 */       this.state = (BlockState)this.state.setValue(property, (Comparable)value.get());
/* 466 */       this.properties.put(property, (Comparable)value.get());
/*     */     } else {
/* 468 */       this.reader.setCursor(start);
/* 469 */       throw ERROR_INVALID_VALUE.createWithContext(this.reader, this.id.toString(), property.getName(), raw);
/*     */     } 
/*     */   }
/*     */   
/*     */   public static String serialize(BlockState state) {
/* 474 */     final StringBuilder result = new StringBuilder(state.typeHolder().unwrapKey().map(r -> r.identifier().toString()).orElse("air"));
/* 475 */     if (!state.isSingletonState()) {
/* 476 */       result.append('[');
/* 478 */       state.getValues().forEach(new Consumer<Property.Value<?>>() {
/*     */             private boolean separate = false;
/*     */             
/*     */             public void accept(Property.Value<?> value) {
/* 483 */               if (this.separate)
/* 484 */                 result.append(','); 
/* 487 */               result.append(value.property().getName());
/* 488 */               result.append('=');
/* 489 */               result.append(value.valueName());
/* 490 */               this.separate = true;
/*     */             }
/*     */           });
/* 493 */       result.append(']');
/*     */     } 
/* 495 */     return result.toString();
/*     */   }
/*     */ }


/* Location:              C:\Users\karol\AppData\Roaming\ModrinthApp\meta\versions\26.2-0.19.5\26.2-0.19.5.jar!\net\minecraft\commands\arguments\blocks\BlockStateParser.class
 * Java compiler version: 25 (69.0)
 * JD-Core Version:       1.1.3
 */