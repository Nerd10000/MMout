/*     */ package net.minecraft.commands.arguments.selector;
/*     */ 
/*     */ import com.google.common.primitives.Doubles;
/*     */ import com.mojang.brigadier.Message;
/*     */ import com.mojang.brigadier.StringReader;
/*     */ import com.mojang.brigadier.exceptions.CommandSyntaxException;
/*     */ import com.mojang.brigadier.exceptions.DynamicCommandExceptionType;
/*     */ import com.mojang.brigadier.exceptions.SimpleCommandExceptionType;
/*     */ import com.mojang.brigadier.suggestion.Suggestions;
/*     */ import com.mojang.brigadier.suggestion.SuggestionsBuilder;
/*     */ import java.util.ArrayList;
/*     */ import java.util.Collections;
/*     */ import java.util.List;
/*     */ import java.util.UUID;
/*     */ import java.util.concurrent.CompletableFuture;
/*     */ import java.util.function.BiConsumer;
/*     */ import java.util.function.BiFunction;
/*     */ import java.util.function.Consumer;
/*     */ import java.util.function.Function;
/*     */ import java.util.function.Predicate;
/*     */ import net.minecraft.advancements.predicates.MinMaxBounds;
/*     */ import net.minecraft.commands.arguments.selector.options.EntitySelectorOptions;
/*     */ import net.minecraft.commands.arguments.selector.options.InvertableSetOptionState;
/*     */ import net.minecraft.commands.arguments.selector.options.SetOnceOptionState;
/*     */ import net.minecraft.network.chat.Component;
/*     */ import net.minecraft.server.level.ServerPlayer;
/*     */ import net.minecraft.server.permissions.PermissionSetSupplier;
/*     */ import net.minecraft.server.permissions.Permissions;
/*     */ import net.minecraft.util.Mth;
/*     */ import net.minecraft.util.ToFloatFunction;
/*     */ import net.minecraft.world.entity.Entity;
/*     */ import net.minecraft.world.entity.EntityType;
/*     */ import net.minecraft.world.entity.EntityTypes;
/*     */ import net.minecraft.world.phys.AABB;
/*     */ import net.minecraft.world.phys.Vec3;
/*     */ 
/*     */ public class EntitySelectorParser {
/*     */   public static final char SYNTAX_SELECTOR_START = '@';
/*     */   
/*     */   private static final char SYNTAX_OPTIONS_START = '[';
/*     */   
/*     */   private static final char SYNTAX_OPTIONS_END = ']';
/*     */   
/*     */   public static final char SYNTAX_OPTIONS_KEY_VALUE_SEPARATOR = '=';
/*     */   
/*     */   private static final char SYNTAX_OPTIONS_SEPARATOR = ',';
/*     */   
/*     */   public static final char SYNTAX_NOT = '!';
/*     */   
/*     */   public static final char SYNTAX_TAG = '#';
/*     */   
/*     */   private static final char SELECTOR_NEAREST_PLAYER = 'p';
/*     */   
/*     */   private static final char SELECTOR_ALL_PLAYERS = 'a';
/*     */   
/*     */   private static final char SELECTOR_RANDOM_PLAYERS = 'r';
/*     */   
/*     */   private static final char SELECTOR_CURRENT_ENTITY = 's';
/*     */   
/*     */   private static final char SELECTOR_ALL_ENTITIES = 'e';
/*     */   
/*     */   private static final char SELECTOR_NEAREST_ENTITY = 'n';
/*     */   
/*  55 */   public static final SimpleCommandExceptionType ERROR_INVALID_NAME_OR_UUID = new SimpleCommandExceptionType((Message)Component.translatable("argument.entity.invalid"));
/*     */   
/*     */   public static final DynamicCommandExceptionType ERROR_UNKNOWN_SELECTOR_TYPE;
/*     */   
/*     */   static {
/*  56 */     ERROR_UNKNOWN_SELECTOR_TYPE = new DynamicCommandExceptionType(type -> Component.translatableEscape("argument.entity.selector.unknown", new Object[] { type }));
/*     */   }
/*     */   
/*  57 */   public static final SimpleCommandExceptionType ERROR_SELECTORS_NOT_ALLOWED = new SimpleCommandExceptionType((Message)Component.translatable("argument.entity.selector.not_allowed"));
/*     */   
/*  58 */   public static final SimpleCommandExceptionType ERROR_MISSING_SELECTOR_TYPE = new SimpleCommandExceptionType((Message)Component.translatable("argument.entity.selector.missing"));
/*     */   
/*  59 */   public static final SimpleCommandExceptionType ERROR_EXPECTED_END_OF_OPTIONS = new SimpleCommandExceptionType((Message)Component.translatable("argument.entity.options.unterminated"));
/*     */   
/*     */   public static final DynamicCommandExceptionType ERROR_EXPECTED_OPTION_VALUE;
/*     */   
/*     */   public static final BiConsumer<Vec3, List<? extends Entity>> ORDER_NEAREST;
/*     */   
/*     */   public static final BiConsumer<Vec3, List<? extends Entity>> ORDER_FURTHEST;
/*     */   
/*     */   public static final BiConsumer<Vec3, List<? extends Entity>> ORDER_RANDOM;
/*     */   
/*     */   public static final BiFunction<SuggestionsBuilder, Consumer<SuggestionsBuilder>, CompletableFuture<Suggestions>> SUGGEST_NOTHING;
/*     */   
/*     */   private final StringReader reader;
/*     */   
/*     */   private final boolean allowSelectors;
/*     */   
/*     */   private int maxResults;
/*     */   
/*     */   private boolean includesEntities;
/*     */   
/*     */   private boolean worldLimited;
/*     */   
/*     */   private MinMaxBounds.Doubles distance;
/*     */   
/*     */   private MinMaxBounds.Ints level;
/*     */   
/*     */   private Double x;
/*     */   
/*     */   private Double y;
/*     */   
/*     */   private Double z;
/*     */   
/*     */   private Double deltaX;
/*     */   
/*     */   private Double deltaY;
/*     */   
/*     */   private Double deltaZ;
/*     */   
/*     */   private MinMaxBounds.FloatDegrees rotX;
/*     */   
/*     */   private MinMaxBounds.FloatDegrees rotY;
/*     */   
/*     */   static {
/*  60 */     ERROR_EXPECTED_OPTION_VALUE = new DynamicCommandExceptionType(name -> Component.translatableEscape("argument.entity.options.valueless", new Object[] { name }));
/*  62 */     ORDER_NEAREST = ((p, c) -> c.sort(()));
/*  63 */     ORDER_FURTHEST = ((p, c) -> c.sort(()));
/*  64 */     ORDER_RANDOM = ((p, c) -> Collections.shuffle(c));
/*  66 */     SUGGEST_NOTHING = ((b, s) -> b.buildFuture());
/*     */   }
/*     */   
/*  83 */   private final List<Predicate<Entity>> predicates = new ArrayList<>();
/*     */   
/*  84 */   private BiConsumer<Vec3, List<? extends Entity>> order = EntitySelector.ORDER_ARBITRARY;
/*     */   
/*     */   private boolean currentEntity;
/*     */   
/*     */   private String playerName;
/*     */   
/*     */   private int startPosition;
/*     */   
/*     */   private UUID entityUUID;
/*     */   
/*  89 */   private BiFunction<SuggestionsBuilder, Consumer<SuggestionsBuilder>, CompletableFuture<Suggestions>> suggestions = SUGGEST_NOTHING;
/*     */   
/*  90 */   private final InvertableSetOptionState nameOption = new InvertableSetOptionState();
/*     */   
/*  91 */   private final SetOnceOptionState limitedOption = new SetOnceOptionState();
/*     */   
/*  92 */   private final SetOnceOptionState sortedOption = new SetOnceOptionState();
/*     */   
/*  93 */   private final InvertableSetOptionState gamemodeOption = new InvertableSetOptionState();
/*     */   
/*  94 */   private final InvertableSetOptionState teamOption = new InvertableSetOptionState();
/*     */   
/*     */   private EntityType<?> type;
/*     */   
/*  96 */   private final InvertableSetOptionState typeOption = new InvertableSetOptionState();
/*     */   
/*  97 */   private final SetOnceOptionState scoresOption = new SetOnceOptionState();
/*     */   
/*  98 */   private final SetOnceOptionState advancementsOption = new SetOnceOptionState();
/*     */   
/*     */   private boolean usesSelectors;
/*     */   
/*     */   public EntitySelectorParser(StringReader reader, boolean allowSelectors) {
/* 102 */     this.reader = reader;
/* 103 */     this.allowSelectors = allowSelectors;
/*     */   }
/*     */   
/*     */   public static <S> boolean allowSelectors(S source) {
/* 109 */     if (source instanceof PermissionSetSupplier) {
/* 109 */       PermissionSetSupplier sender = (PermissionSetSupplier)source;
/* 109 */       if (sender.permissions().hasPermission(Permissions.COMMANDS_ENTITY_SELECTORS));
/*     */     } 
/* 109 */     return false;
/*     */   }
/*     */   
/*     */   @Deprecated
/*     */   public static boolean allowSelectors(PermissionSetSupplier source) {
/* 115 */     return source.permissions().hasPermission(Permissions.COMMANDS_ENTITY_SELECTORS);
/*     */   }
/*     */   
/*     */   public EntitySelector getSelector() {
/*     */     AABB aabb;
/*     */     Function<Vec3, Vec3> position;
/* 120 */     if (this.deltaX != null || this.deltaY != null || this.deltaZ != null) {
/* 121 */       aabb = createAabb((this.deltaX == null) ? 0.0D : this.deltaX.doubleValue(), (this.deltaY == null) ? 0.0D : this.deltaY.doubleValue(), (this.deltaZ == null) ? 0.0D : this.deltaZ.doubleValue());
/* 122 */     } else if (this.distance != null && this.distance.max().isPresent()) {
/* 123 */       double maxRange = ((Double)this.distance.max().get()).doubleValue();
/* 124 */       aabb = new AABB(-maxRange, -maxRange, -maxRange, maxRange + 1.0D, maxRange + 1.0D, maxRange + 1.0D);
/*     */     } else {
/* 126 */       aabb = null;
/*     */     } 
/* 129 */     if (this.x == null && this.y == null && this.z == null) {
/* 130 */       position = (o -> o);
/*     */     } else {
/* 132 */       position = (o -> new Vec3((this.x == null) ? o.x : this.x.doubleValue(), (this.y == null) ? o.y : this.y.doubleValue(), (this.z == null) ? o.z : this.z.doubleValue()));
/*     */     } 
/* 134 */     return new EntitySelector(this.maxResults, this.includesEntities, this.worldLimited, List.copyOf(this.predicates), this.distance, position, aabb, this.order, this.currentEntity, this.playerName, this.entityUUID, this.type, this.usesSelectors);
/*     */   }
/*     */   
/*     */   private AABB createAabb(double x, double y, double z) {
/* 138 */     boolean xNeg = (x < 0.0D);
/* 139 */     boolean yNeg = (y < 0.0D);
/* 140 */     boolean zNeg = (z < 0.0D);
/* 141 */     double xMin = xNeg ? x : 0.0D;
/* 142 */     double yMin = yNeg ? y : 0.0D;
/* 143 */     double zMin = zNeg ? z : 0.0D;
/* 144 */     double xMax = (xNeg ? 0.0D : x) + 1.0D;
/* 145 */     double yMax = (yNeg ? 0.0D : y) + 1.0D;
/* 146 */     double zMax = (zNeg ? 0.0D : z) + 1.0D;
/* 147 */     return new AABB(xMin, yMin, zMin, xMax, yMax, zMax);
/*     */   }
/*     */   
/*     */   private void finalizePredicates() {
/* 151 */     if (this.rotX != null)
/* 152 */       this.predicates.add(createRotationPredicate(this.rotX, Entity::getXRot)); 
/* 154 */     if (this.rotY != null)
/* 155 */       this.predicates.add(createRotationPredicate(this.rotY, Entity::getYRot)); 
/* 157 */     if (this.level != null)
/* 158 */       this.predicates.add(e -> {
/*     */             if (e instanceof ServerPlayer) {
/*     */               ServerPlayer serverPlayer = (ServerPlayer)e;
/*     */               if (this.level.matches(serverPlayer.experienceLevel));
/*     */             } 
/*     */             return false;
/*     */           }); 
/*     */   }
/*     */   
/*     */   private Predicate<Entity> createRotationPredicate(MinMaxBounds.FloatDegrees range, ToFloatFunction<Entity> function) {
/* 164 */     float min = Mth.wrapDegrees(((Float)range.min().orElse(Float.valueOf(0.0F))).floatValue());
/* 165 */     float max = Mth.wrapDegrees(((Float)range.max().orElse(Float.valueOf(359.0F))).floatValue());
/* 166 */     return e -> {
/*     */         float rotation = Mth.wrapDegrees(function.applyAsFloat(e));
/* 169 */         return (min > max) ? ((rotation >= min || rotation <= max)) : (
/*     */           
/* 171 */           (rotation >= min && rotation <= max));
/*     */       };
/*     */   }
/*     */   
/*     */   protected void parseSelector() throws CommandSyntaxException {
/*     */     boolean selectOnlyAlive;
/* 176 */     this.usesSelectors = true;
/* 177 */     this.suggestions = this::suggestSelector;
/* 178 */     if (!this.reader.canRead())
/* 179 */       throw ERROR_MISSING_SELECTOR_TYPE.createWithContext(this.reader); 
/* 181 */     int start = this.reader.getCursor();
/* 182 */     char type = this.reader.read();
/* 185 */     switch (type) {
/*     */       case 'p':
/* 187 */         this.maxResults = 1;
/* 188 */         this.includesEntities = false;
/* 189 */         this.order = ORDER_NEAREST;
/* 190 */         limitToType(EntityTypes.PLAYER);
/* 191 */         selectOnlyAlive = false;
/*     */         break;
/*     */       case 'a':
/* 194 */         this.maxResults = Integer.MAX_VALUE;
/* 195 */         this.includesEntities = false;
/* 196 */         this.order = EntitySelector.ORDER_ARBITRARY;
/* 197 */         limitToType(EntityTypes.PLAYER);
/* 198 */         selectOnlyAlive = false;
/*     */         break;
/*     */       case 'r':
/* 201 */         this.maxResults = 1;
/* 202 */         this.includesEntities = false;
/* 203 */         this.order = ORDER_RANDOM;
/* 204 */         limitToType(EntityTypes.PLAYER);
/* 205 */         selectOnlyAlive = false;
/*     */         break;
/*     */       case 's':
/* 208 */         this.maxResults = 1;
/* 209 */         this.includesEntities = true;
/* 210 */         this.currentEntity = true;
/* 212 */         selectOnlyAlive = false;
/*     */         break;
/*     */       case 'e':
/* 215 */         this.maxResults = Integer.MAX_VALUE;
/* 216 */         this.includesEntities = true;
/* 217 */         this.order = EntitySelector.ORDER_ARBITRARY;
/* 218 */         selectOnlyAlive = true;
/*     */         break;
/*     */       case 'n':
/* 221 */         this.maxResults = 1;
/* 222 */         this.includesEntities = true;
/* 223 */         this.order = ORDER_NEAREST;
/* 224 */         selectOnlyAlive = true;
/*     */         break;
/*     */       default:
/* 227 */         this.reader.setCursor(start);
/* 228 */         throw ERROR_UNKNOWN_SELECTOR_TYPE.createWithContext(this.reader, "@" + String.valueOf(type));
/*     */     } 
/* 232 */     if (selectOnlyAlive)
/* 233 */       this.predicates.add(Entity::isAlive); 
/* 235 */     this.suggestions = this::suggestOpenOptions;
/* 236 */     if (this.reader.canRead() && this.reader.peek() == '[') {
/* 237 */       this.reader.skip();
/* 238 */       this.suggestions = this::suggestOptionsKeyOrClose;
/* 239 */       parseOptions();
/*     */     } 
/*     */   }
/*     */   
/*     */   protected void parseNameOrUUID() throws CommandSyntaxException {
/* 244 */     if (this.reader.canRead())
/* 245 */       this.suggestions = this::suggestName; 
/* 247 */     int start = this.reader.getCursor();
/* 248 */     String name = this.reader.readString();
/*     */     try {
/* 251 */       this.entityUUID = UUID.fromString(name);
/* 252 */       this.includesEntities = true;
/* 253 */     } catch (IllegalArgumentException ex) {
/* 254 */       if (name.isEmpty() || name.length() > 16) {
/* 255 */         this.reader.setCursor(start);
/* 256 */         throw ERROR_INVALID_NAME_OR_UUID.createWithContext(this.reader);
/*     */       } 
/* 258 */       this.includesEntities = false;
/* 259 */       this.playerName = name;
/*     */     } 
/* 262 */     this.maxResults = 1;
/*     */   }
/*     */   
/*     */   protected void parseOptions() throws CommandSyntaxException {
/* 266 */     this.suggestions = this::suggestOptionsKey;
/* 267 */     this.reader.skipWhitespace();
/* 268 */     while (this.reader.canRead() && this.reader.peek() != ']') {
/* 269 */       this.reader.skipWhitespace();
/* 270 */       int start = this.reader.getCursor();
/* 271 */       String key = this.reader.readString();
/* 272 */       EntitySelectorOptions.Modifier modifier = EntitySelectorOptions.get(this, key, start);
/* 273 */       this.reader.skipWhitespace();
/* 274 */       if (!this.reader.canRead() || this.reader.peek() != '=') {
/* 275 */         this.reader.setCursor(start);
/* 276 */         throw ERROR_EXPECTED_OPTION_VALUE.createWithContext(this.reader, key);
/*     */       } 
/* 278 */       this.reader.skip();
/* 279 */       this.reader.skipWhitespace();
/* 281 */       this.suggestions = SUGGEST_NOTHING;
/* 282 */       modifier.handle(this);
/* 283 */       this.reader.skipWhitespace();
/* 285 */       this.suggestions = this::suggestOptionsNextOrClose;
/* 286 */       if (this.reader.canRead()) {
/* 287 */         if (this.reader.peek() == ',') {
/* 288 */           this.reader.skip();
/* 289 */           this.suggestions = this::suggestOptionsKey;
/*     */           continue;
/*     */         } 
/* 290 */         if (this.reader.peek() == ']')
/*     */           break; 
/* 293 */         throw ERROR_EXPECTED_END_OF_OPTIONS.createWithContext(this.reader);
/*     */       } 
/*     */     } 
/* 298 */     if (this.reader.canRead()) {
/* 299 */       this.reader.skip();
/* 300 */       this.suggestions = SUGGEST_NOTHING;
/*     */     } else {
/* 302 */       throw ERROR_EXPECTED_END_OF_OPTIONS.createWithContext(this.reader);
/*     */     } 
/*     */   }
/*     */   
/*     */   public boolean shouldInvertValue() {
/* 307 */     this.reader.skipWhitespace();
/* 308 */     if (this.reader.canRead() && this.reader.peek() == '!') {
/* 309 */       this.reader.skip();
/* 310 */       this.reader.skipWhitespace();
/* 311 */       return true;
/*     */     } 
/* 313 */     return false;
/*     */   }
/*     */   
/*     */   public boolean isTag() {
/* 317 */     this.reader.skipWhitespace();
/* 318 */     if (this.reader.canRead() && this.reader.peek() == '#') {
/* 319 */       this.reader.skip();
/* 320 */       this.reader.skipWhitespace();
/* 321 */       return true;
/*     */     } 
/* 323 */     return false;
/*     */   }
/*     */   
/*     */   public StringReader getReader() {
/* 327 */     return this.reader;
/*     */   }
/*     */   
/*     */   public void addPredicate(Predicate<Entity> predicate) {
/* 331 */     this.predicates.add(predicate);
/*     */   }
/*     */   
/*     */   public void setWorldLimited() {
/* 335 */     this.worldLimited = true;
/*     */   }
/*     */   
/*     */   public MinMaxBounds.Doubles getDistance() {
/* 339 */     return this.distance;
/*     */   }
/*     */   
/*     */   public void setDistance(MinMaxBounds.Doubles distance) {
/* 343 */     this.distance = distance;
/*     */   }
/*     */   
/*     */   public MinMaxBounds.Ints getLevel() {
/* 347 */     return this.level;
/*     */   }
/*     */   
/*     */   public void setLevel(MinMaxBounds.Ints level) {
/* 351 */     this.level = level;
/*     */   }
/*     */   
/*     */   public MinMaxBounds.FloatDegrees getRotX() {
/* 355 */     return this.rotX;
/*     */   }
/*     */   
/*     */   public void setRotX(MinMaxBounds.FloatDegrees rotX) {
/* 359 */     this.rotX = rotX;
/*     */   }
/*     */   
/*     */   public MinMaxBounds.FloatDegrees getRotY() {
/* 363 */     return this.rotY;
/*     */   }
/*     */   
/*     */   public void setRotY(MinMaxBounds.FloatDegrees rotY) {
/* 367 */     this.rotY = rotY;
/*     */   }
/*     */   
/*     */   public Double getX() {
/* 371 */     return this.x;
/*     */   }
/*     */   
/*     */   public Double getY() {
/* 375 */     return this.y;
/*     */   }
/*     */   
/*     */   public Double getZ() {
/* 379 */     return this.z;
/*     */   }
/*     */   
/*     */   public void setX(double x) {
/* 383 */     this.x = Double.valueOf(x);
/*     */   }
/*     */   
/*     */   public void setY(double y) {
/* 387 */     this.y = Double.valueOf(y);
/*     */   }
/*     */   
/*     */   public void setZ(double z) {
/* 391 */     this.z = Double.valueOf(z);
/*     */   }
/*     */   
/*     */   public void setDeltaX(double deltaX) {
/* 395 */     this.deltaX = Double.valueOf(deltaX);
/*     */   }
/*     */   
/*     */   public void setDeltaY(double deltaY) {
/* 399 */     this.deltaY = Double.valueOf(deltaY);
/*     */   }
/*     */   
/*     */   public void setDeltaZ(double deltaZ) {
/* 403 */     this.deltaZ = Double.valueOf(deltaZ);
/*     */   }
/*     */   
/*     */   public Double getDeltaX() {
/* 407 */     return this.deltaX;
/*     */   }
/*     */   
/*     */   public Double getDeltaY() {
/* 411 */     return this.deltaY;
/*     */   }
/*     */   
/*     */   public Double getDeltaZ() {
/* 415 */     return this.deltaZ;
/*     */   }
/*     */   
/*     */   public void setMaxResults(int maxResults) {
/* 419 */     this.maxResults = maxResults;
/*     */   }
/*     */   
/*     */   public void setIncludesEntities(boolean includesEntities) {
/* 423 */     this.includesEntities = includesEntities;
/*     */   }
/*     */   
/*     */   public BiConsumer<Vec3, List<? extends Entity>> getOrder() {
/* 427 */     return this.order;
/*     */   }
/*     */   
/*     */   public void setOrder(BiConsumer<Vec3, List<? extends Entity>> order) {
/* 431 */     this.order = order;
/*     */   }
/*     */   
/*     */   public EntitySelector parse() throws CommandSyntaxException {
/* 435 */     this.startPosition = this.reader.getCursor();
/* 436 */     this.suggestions = this::suggestNameOrSelector;
/* 437 */     if (this.reader.canRead() && this.reader.peek() == '@') {
/* 438 */       if (!this.allowSelectors)
/* 439 */         throw ERROR_SELECTORS_NOT_ALLOWED.createWithContext(this.reader); 
/* 441 */       this.reader.skip();
/* 442 */       parseSelector();
/*     */     } else {
/* 444 */       parseNameOrUUID();
/*     */     } 
/* 446 */     finalizePredicates();
/* 447 */     return getSelector();
/*     */   }
/*     */   
/*     */   private static void fillSelectorSuggestions(SuggestionsBuilder builder) {
/* 451 */     builder.suggest("@p", (Message)Component.translatable("argument.entity.selector.nearestPlayer"));
/* 452 */     builder.suggest("@a", (Message)Component.translatable("argument.entity.selector.allPlayers"));
/* 453 */     builder.suggest("@r", (Message)Component.translatable("argument.entity.selector.randomPlayer"));
/* 454 */     builder.suggest("@s", (Message)Component.translatable("argument.entity.selector.self"));
/* 455 */     builder.suggest("@e", (Message)Component.translatable("argument.entity.selector.allEntities"));
/* 456 */     builder.suggest("@n", (Message)Component.translatable("argument.entity.selector.nearestEntity"));
/*     */   }
/*     */   
/*     */   private CompletableFuture<Suggestions> suggestNameOrSelector(SuggestionsBuilder builder, Consumer<SuggestionsBuilder> names) {
/* 460 */     names.accept(builder);
/* 461 */     if (this.allowSelectors)
/* 462 */       fillSelectorSuggestions(builder); 
/* 464 */     return builder.buildFuture();
/*     */   }
/*     */   
/*     */   private CompletableFuture<Suggestions> suggestName(SuggestionsBuilder builder, Consumer<SuggestionsBuilder> names) {
/* 468 */     SuggestionsBuilder sub = builder.createOffset(this.startPosition);
/* 469 */     names.accept(sub);
/* 470 */     return builder.add(sub).buildFuture();
/*     */   }
/*     */   
/*     */   private CompletableFuture<Suggestions> suggestSelector(SuggestionsBuilder builder, Consumer<SuggestionsBuilder> names) {
/* 474 */     SuggestionsBuilder sub = builder.createOffset(builder.getStart() - 1);
/* 475 */     fillSelectorSuggestions(sub);
/* 476 */     builder.add(sub);
/* 477 */     return builder.buildFuture();
/*     */   }
/*     */   
/*     */   private CompletableFuture<Suggestions> suggestOpenOptions(SuggestionsBuilder builder, Consumer<SuggestionsBuilder> names) {
/* 481 */     builder.suggest(String.valueOf('['));
/* 482 */     return builder.buildFuture();
/*     */   }
/*     */   
/*     */   private CompletableFuture<Suggestions> suggestOptionsKeyOrClose(SuggestionsBuilder builder, Consumer<SuggestionsBuilder> names) {
/* 486 */     builder.suggest(String.valueOf(']'));
/* 487 */     EntitySelectorOptions.suggestNames(this, builder);
/* 488 */     return builder.buildFuture();
/*     */   }
/*     */   
/*     */   private CompletableFuture<Suggestions> suggestOptionsKey(SuggestionsBuilder builder, Consumer<SuggestionsBuilder> names) {
/* 492 */     EntitySelectorOptions.suggestNames(this, builder);
/* 493 */     return builder.buildFuture();
/*     */   }
/*     */   
/*     */   private CompletableFuture<Suggestions> suggestOptionsNextOrClose(SuggestionsBuilder builder, Consumer<SuggestionsBuilder> names) {
/* 497 */     builder.suggest(String.valueOf(','));
/* 498 */     builder.suggest(String.valueOf(']'));
/* 499 */     return builder.buildFuture();
/*     */   }
/*     */   
/*     */   private CompletableFuture<Suggestions> suggestEquals(SuggestionsBuilder builder, Consumer<SuggestionsBuilder> names) {
/* 503 */     builder.suggest(String.valueOf('='));
/* 504 */     return builder.buildFuture();
/*     */   }
/*     */   
/*     */   public boolean isCurrentEntity() {
/* 508 */     return this.currentEntity;
/*     */   }
/*     */   
/*     */   public void setSuggestions(BiFunction<SuggestionsBuilder, Consumer<SuggestionsBuilder>, CompletableFuture<Suggestions>> suggestions) {
/* 512 */     this.suggestions = suggestions;
/*     */   }
/*     */   
/*     */   public CompletableFuture<Suggestions> fillSuggestions(SuggestionsBuilder builder, Consumer<SuggestionsBuilder> names) {
/* 516 */     return this.suggestions.apply(builder.createOffset(this.reader.getCursor()), names);
/*     */   }
/*     */   
/*     */   public InvertableSetOptionState nameOption() {
/* 520 */     return this.nameOption;
/*     */   }
/*     */   
/*     */   public SetOnceOptionState limitedOption() {
/* 524 */     return this.limitedOption;
/*     */   }
/*     */   
/*     */   public SetOnceOptionState sortedOption() {
/* 528 */     return this.sortedOption;
/*     */   }
/*     */   
/*     */   public InvertableSetOptionState gamemodeOption() {
/* 532 */     return this.gamemodeOption;
/*     */   }
/*     */   
/*     */   public InvertableSetOptionState teamOption() {
/* 536 */     return this.teamOption;
/*     */   }
/*     */   
/*     */   public void limitToType(EntityType<?> type) {
/* 540 */     this.type = type;
/*     */   }
/*     */   
/*     */   public InvertableSetOptionState typeOption() {
/* 544 */     return this.typeOption;
/*     */   }
/*     */   
/*     */   public SetOnceOptionState scoresOption() {
/* 548 */     return this.scoresOption;
/*     */   }
/*     */   
/*     */   public SetOnceOptionState advancementsOption() {
/* 552 */     return this.advancementsOption;
/*     */   }
/*     */ }


/* Location:              C:\Users\karol\AppData\Roaming\ModrinthApp\meta\versions\26.2-0.19.5\26.2-0.19.5.jar!\net\minecraft\commands\arguments\selector\EntitySelectorParser.class
 * Java compiler version: 25 (69.0)
 * JD-Core Version:       1.1.3
 */