/*     */ package net.minecraft.advancements.predicates.entity;
/*     */ 
/*     */ import com.google.common.base.Suppliers;
/*     */ import com.google.common.collect.ImmutableList;
/*     */ import com.google.common.collect.Maps;
/*     */ import com.mojang.datafixers.kinds.App;
/*     */ import com.mojang.datafixers.kinds.Applicative;
/*     */ import com.mojang.datafixers.util.Either;
/*     */ import com.mojang.datafixers.util.Function8;
/*     */ import com.mojang.serialization.Codec;
/*     */ import com.mojang.serialization.MapCodec;
/*     */ import com.mojang.serialization.codecs.RecordCodecBuilder;
/*     */ import it.unimi.dsi.fastutil.objects.Object2BooleanMap;
/*     */ import it.unimi.dsi.fastutil.objects.Object2BooleanMaps;
/*     */ import it.unimi.dsi.fastutil.objects.Object2BooleanOpenHashMap;
/*     */ import it.unimi.dsi.fastutil.objects.ObjectIterator;
/*     */ import java.util.List;
/*     */ import java.util.Map;
/*     */ import java.util.Optional;
/*     */ import java.util.function.Function;
/*     */ import java.util.function.Predicate;
/*     */ import java.util.function.Supplier;
/*     */ import net.minecraft.advancements.AdvancementHolder;
/*     */ import net.minecraft.advancements.AdvancementProgress;
/*     */ import net.minecraft.advancements.CriterionProgress;
/*     */ import net.minecraft.advancements.predicates.FoodPredicate;
/*     */ import net.minecraft.advancements.predicates.GameTypePredicate;
/*     */ import net.minecraft.advancements.predicates.InputPredicate;
/*     */ import net.minecraft.advancements.predicates.MinMaxBounds;
/*     */ import net.minecraft.core.Holder;
/*     */ import net.minecraft.core.registries.BuiltInRegistries;
/*     */ import net.minecraft.resources.Identifier;
/*     */ import net.minecraft.resources.ResourceKey;
/*     */ import net.minecraft.server.PlayerAdvancements;
/*     */ import net.minecraft.server.ServerAdvancementManager;
/*     */ import net.minecraft.server.level.ServerLevel;
/*     */ import net.minecraft.server.level.ServerPlayer;
/*     */ import net.minecraft.stats.ServerRecipeBook;
/*     */ import net.minecraft.stats.ServerStatsCounter;
/*     */ import net.minecraft.stats.Stat;
/*     */ import net.minecraft.stats.StatType;
/*     */ import net.minecraft.stats.StatsCounter;
/*     */ import net.minecraft.util.ExtraCodecs;
/*     */ import net.minecraft.world.entity.Entity;
/*     */ import net.minecraft.world.entity.projectile.ProjectileUtil;
/*     */ import net.minecraft.world.item.crafting.Recipe;
/*     */ import net.minecraft.world.level.Level;
/*     */ import net.minecraft.world.phys.AABB;
/*     */ import net.minecraft.world.phys.EntityHitResult;
/*     */ import net.minecraft.world.phys.HitResult;
/*     */ import net.minecraft.world.phys.Vec3;
/*     */ 
/*     */ public final class PlayerPredicate extends Record implements EntitySubPredicate {
/*     */   private final MinMaxBounds.Ints level;
/*     */   
/*     */   private final FoodPredicate food;
/*     */   
/*     */   private final GameTypePredicate gameType;
/*     */   
/*     */   private final List<StatMatcher<?>> stats;
/*     */   
/*     */   private final Object2BooleanMap<ResourceKey<Recipe<?>>> recipes;
/*     */   
/*     */   private final Map<Identifier, AdvancementPredicate> advancements;
/*     */   
/*     */   private final Optional<EntityPredicate> lookingAt;
/*     */   
/*     */   private final Optional<InputPredicate> input;
/*     */   
/*     */   public static final int LOOKING_AT_RANGE = 100;
/*     */   
/*     */   public static final Codec<PlayerPredicate> CODEC;
/*     */   
/*     */   public PlayerPredicate(MinMaxBounds.Ints level, FoodPredicate food, GameTypePredicate gameType, List<StatMatcher<?>> stats, Object2BooleanMap<ResourceKey<Recipe<?>>> recipes, Map<Identifier, AdvancementPredicate> advancements, Optional<EntityPredicate> lookingAt, Optional<InputPredicate> input) {
/*  48 */     this.level = level;
/*  48 */     this.food = food;
/*  48 */     this.gameType = gameType;
/*  48 */     this.stats = stats;
/*  48 */     this.recipes = recipes;
/*  48 */     this.advancements = advancements;
/*  48 */     this.lookingAt = lookingAt;
/*  48 */     this.input = input;
/*     */   }
/*     */   
/*     */   public final String toString() {
/*     */     // Byte code:
/*     */     //   0: aload_0
/*     */     //   1: <illegal opcode> toString : (Lnet/minecraft/advancements/predicates/entity/PlayerPredicate;)Ljava/lang/String;
/*     */     //   6: areturn
/*     */     // Line number table:
/*     */     //   Java source line number -> byte code offset
/*     */     //   #48	-> 0
/*     */     // Local variable table:
/*     */     //   start	length	slot	name	descriptor
/*     */     //   0	7	0	this	Lnet/minecraft/advancements/predicates/entity/PlayerPredicate;
/*     */   }
/*     */   
/*     */   public final int hashCode() {
/*     */     // Byte code:
/*     */     //   0: aload_0
/*     */     //   1: <illegal opcode> hashCode : (Lnet/minecraft/advancements/predicates/entity/PlayerPredicate;)I
/*     */     //   6: ireturn
/*     */     // Line number table:
/*     */     //   Java source line number -> byte code offset
/*     */     //   #48	-> 0
/*     */     // Local variable table:
/*     */     //   start	length	slot	name	descriptor
/*     */     //   0	7	0	this	Lnet/minecraft/advancements/predicates/entity/PlayerPredicate;
/*     */   }
/*     */   
/*     */   public final boolean equals(Object o) {
/*     */     // Byte code:
/*     */     //   0: aload_0
/*     */     //   1: aload_1
/*     */     //   2: <illegal opcode> equals : (Lnet/minecraft/advancements/predicates/entity/PlayerPredicate;Ljava/lang/Object;)Z
/*     */     //   7: ireturn
/*     */     // Line number table:
/*     */     //   Java source line number -> byte code offset
/*     */     //   #48	-> 0
/*     */     // Local variable table:
/*     */     //   start	length	slot	name	descriptor
/*     */     //   0	8	0	this	Lnet/minecraft/advancements/predicates/entity/PlayerPredicate;
/*     */     //   0	8	1	o	Ljava/lang/Object;
/*     */   }
/*     */   
/*     */   public MinMaxBounds.Ints level() {
/*  48 */     return this.level;
/*     */   }
/*     */   
/*     */   public FoodPredicate food() {
/*  48 */     return this.food;
/*     */   }
/*     */   
/*     */   public GameTypePredicate gameType() {
/*  48 */     return this.gameType;
/*     */   }
/*     */   
/*     */   public List<StatMatcher<?>> stats() {
/*  48 */     return this.stats;
/*     */   }
/*     */   
/*     */   public Object2BooleanMap<ResourceKey<Recipe<?>>> recipes() {
/*  48 */     return this.recipes;
/*     */   }
/*     */   
/*     */   public Map<Identifier, AdvancementPredicate> advancements() {
/*  48 */     return this.advancements;
/*     */   }
/*     */   
/*     */   public Optional<EntityPredicate> lookingAt() {
/*  48 */     return this.lookingAt;
/*     */   }
/*     */   
/*     */   public Optional<InputPredicate> input() {
/*  48 */     return this.input;
/*     */   }
/*     */   
/*     */   static {
/*  60 */     CODEC = RecordCodecBuilder.create(i -> i.group((App)MinMaxBounds.Ints.CODEC.optionalFieldOf("level", MinMaxBounds.Ints.ANY).forGetter(PlayerPredicate::level), (App)FoodPredicate.CODEC.optionalFieldOf("food", FoodPredicate.ANY).forGetter(PlayerPredicate::food), (App)GameTypePredicate.CODEC.optionalFieldOf("gamemode", GameTypePredicate.ANY).forGetter(PlayerPredicate::gameType), (App)StatMatcher.CODEC.listOf().optionalFieldOf("stats", List.of()).forGetter(PlayerPredicate::stats), (App)ExtraCodecs.object2BooleanMap(Recipe.KEY_CODEC).optionalFieldOf("recipes", Object2BooleanMaps.emptyMap()).forGetter(PlayerPredicate::recipes), (App)Codec.unboundedMap(Identifier.CODEC, AdvancementPredicate.CODEC).optionalFieldOf("advancements", Map.of()).forGetter(PlayerPredicate::advancements), (App)EntityPredicate.CODEC.optionalFieldOf("looking_at").forGetter(PlayerPredicate::lookingAt), (App)InputPredicate.CODEC.optionalFieldOf("input").forGetter(PlayerPredicate::input)).apply((Applicative)i, PlayerPredicate::new));
/*     */   }
/*     */   
/*     */   private static interface AdvancementPredicate extends Predicate<AdvancementProgress> {
/*     */     public static final Codec<AdvancementPredicate> CODEC;
/*     */     
/*     */     static {
/*  72 */       CODEC = Codec.either(PlayerPredicate.AdvancementDonePredicate.CODEC, PlayerPredicate.AdvancementCriterionsPredicate.CODEC).xmap(Either::unwrap, predicate -> {
/*     */             if (predicate instanceof PlayerPredicate.AdvancementDonePredicate) {
/*     */               PlayerPredicate.AdvancementDonePredicate done = (PlayerPredicate.AdvancementDonePredicate)predicate;
/*     */               return Either.left(done);
/*     */             } 
/*     */             if (predicate instanceof PlayerPredicate.AdvancementCriterionsPredicate) {
/*     */               PlayerPredicate.AdvancementCriterionsPredicate criterions = (PlayerPredicate.AdvancementCriterionsPredicate)predicate;
/*     */               return Either.right(criterions);
/*     */             } 
/*     */             throw new UnsupportedOperationException();
/*     */           });
/*     */     }
/*     */   }
/*     */   
/*     */   private static final class AdvancementDonePredicate extends Record implements AdvancementPredicate {
/*     */     private final boolean state;
/*     */     
/*     */     private AdvancementDonePredicate(boolean state) {
/*  85 */       this.state = state;
/*     */     }
/*     */     
/*     */     public final String toString() {
/*     */       // Byte code:
/*     */       //   0: aload_0
/*     */       //   1: <illegal opcode> toString : (Lnet/minecraft/advancements/predicates/entity/PlayerPredicate$AdvancementDonePredicate;)Ljava/lang/String;
/*     */       //   6: areturn
/*     */       // Line number table:
/*     */       //   Java source line number -> byte code offset
/*     */       //   #85	-> 0
/*     */       // Local variable table:
/*     */       //   start	length	slot	name	descriptor
/*     */       //   0	7	0	this	Lnet/minecraft/advancements/predicates/entity/PlayerPredicate$AdvancementDonePredicate;
/*     */     }
/*     */     
/*     */     public final int hashCode() {
/*     */       // Byte code:
/*     */       //   0: aload_0
/*     */       //   1: <illegal opcode> hashCode : (Lnet/minecraft/advancements/predicates/entity/PlayerPredicate$AdvancementDonePredicate;)I
/*     */       //   6: ireturn
/*     */       // Line number table:
/*     */       //   Java source line number -> byte code offset
/*     */       //   #85	-> 0
/*     */       // Local variable table:
/*     */       //   start	length	slot	name	descriptor
/*     */       //   0	7	0	this	Lnet/minecraft/advancements/predicates/entity/PlayerPredicate$AdvancementDonePredicate;
/*     */     }
/*     */     
/*     */     public final boolean equals(Object o) {
/*     */       // Byte code:
/*     */       //   0: aload_0
/*     */       //   1: aload_1
/*     */       //   2: <illegal opcode> equals : (Lnet/minecraft/advancements/predicates/entity/PlayerPredicate$AdvancementDonePredicate;Ljava/lang/Object;)Z
/*     */       //   7: ireturn
/*     */       // Line number table:
/*     */       //   Java source line number -> byte code offset
/*     */       //   #85	-> 0
/*     */       // Local variable table:
/*     */       //   start	length	slot	name	descriptor
/*     */       //   0	8	0	this	Lnet/minecraft/advancements/predicates/entity/PlayerPredicate$AdvancementDonePredicate;
/*     */       //   0	8	1	o	Ljava/lang/Object;
/*     */     }
/*     */     
/*     */     public boolean state() {
/*  85 */       return this.state;
/*     */     }
/*     */     
/*  86 */     public static final Codec<AdvancementDonePredicate> CODEC = Codec.BOOL.xmap(AdvancementDonePredicate::new, AdvancementDonePredicate::state);
/*     */     
/*     */     public boolean test(AdvancementProgress progress) {
/*  90 */       return (progress.isDone() == this.state);
/*     */     }
/*     */   }
/*     */   
/*     */   private static final class AdvancementCriterionsPredicate extends Record implements AdvancementPredicate {
/*     */     private final Object2BooleanMap<String> criterions;
/*     */     
/*     */     private AdvancementCriterionsPredicate(Object2BooleanMap<String> criterions) {
/*  94 */       this.criterions = criterions;
/*     */     }
/*     */     
/*     */     public final String toString() {
/*     */       // Byte code:
/*     */       //   0: aload_0
/*     */       //   1: <illegal opcode> toString : (Lnet/minecraft/advancements/predicates/entity/PlayerPredicate$AdvancementCriterionsPredicate;)Ljava/lang/String;
/*     */       //   6: areturn
/*     */       // Line number table:
/*     */       //   Java source line number -> byte code offset
/*     */       //   #94	-> 0
/*     */       // Local variable table:
/*     */       //   start	length	slot	name	descriptor
/*     */       //   0	7	0	this	Lnet/minecraft/advancements/predicates/entity/PlayerPredicate$AdvancementCriterionsPredicate;
/*     */     }
/*     */     
/*     */     public final int hashCode() {
/*     */       // Byte code:
/*     */       //   0: aload_0
/*     */       //   1: <illegal opcode> hashCode : (Lnet/minecraft/advancements/predicates/entity/PlayerPredicate$AdvancementCriterionsPredicate;)I
/*     */       //   6: ireturn
/*     */       // Line number table:
/*     */       //   Java source line number -> byte code offset
/*     */       //   #94	-> 0
/*     */       // Local variable table:
/*     */       //   start	length	slot	name	descriptor
/*     */       //   0	7	0	this	Lnet/minecraft/advancements/predicates/entity/PlayerPredicate$AdvancementCriterionsPredicate;
/*     */     }
/*     */     
/*     */     public final boolean equals(Object o) {
/*     */       // Byte code:
/*     */       //   0: aload_0
/*     */       //   1: aload_1
/*     */       //   2: <illegal opcode> equals : (Lnet/minecraft/advancements/predicates/entity/PlayerPredicate$AdvancementCriterionsPredicate;Ljava/lang/Object;)Z
/*     */       //   7: ireturn
/*     */       // Line number table:
/*     */       //   Java source line number -> byte code offset
/*     */       //   #94	-> 0
/*     */       // Local variable table:
/*     */       //   start	length	slot	name	descriptor
/*     */       //   0	8	0	this	Lnet/minecraft/advancements/predicates/entity/PlayerPredicate$AdvancementCriterionsPredicate;
/*     */       //   0	8	1	o	Ljava/lang/Object;
/*     */     }
/*     */     
/*     */     public Object2BooleanMap<String> criterions() {
/*  94 */       return this.criterions;
/*     */     }
/*     */     
/*  95 */     public static final Codec<AdvancementCriterionsPredicate> CODEC = ExtraCodecs.object2BooleanMap((Codec)Codec.STRING).xmap(AdvancementCriterionsPredicate::new, AdvancementCriterionsPredicate::criterions);
/*     */     
/*     */     public boolean test(AdvancementProgress progress) {
/*  99 */       for (ObjectIterator<Object2BooleanMap.Entry<String>> objectIterator = this.criterions.object2BooleanEntrySet().iterator(); objectIterator.hasNext(); ) {
/*  99 */         Object2BooleanMap.Entry<String> e = objectIterator.next();
/* 100 */         CriterionProgress criterion = progress.getCriterion((String)e.getKey());
/* 101 */         if (criterion == null || criterion.isDone() != e.getBooleanValue())
/* 102 */           return false; 
/*     */       } 
/* 105 */       return true;
/*     */     }
/*     */   }
/*     */   
/*     */   public boolean matches(Entity entity, ServerLevel level, Vec3 position) {
/*     */     ServerPlayer player;
/* 111 */     if (entity instanceof ServerPlayer) {
/* 111 */       player = (ServerPlayer)entity;
/*     */     } else {
/* 112 */       return false;
/*     */     } 
/* 115 */     if (!this.level.matches(player.experienceLevel))
/* 116 */       return false; 
/* 119 */     if (!this.food.matches(player.getFoodData()))
/* 120 */       return false; 
/* 123 */     if (!this.gameType.matches(player.gameMode()))
/* 124 */       return false; 
/* 127 */     ServerStatsCounter serverStatsCounter = player.getStats();
/* 128 */     for (StatMatcher<?> stat : this.stats) {
/* 129 */       if (!stat.matches((StatsCounter)serverStatsCounter))
/* 130 */         return false; 
/*     */     } 
/* 134 */     ServerRecipeBook recipes = player.getRecipeBook();
/* 135 */     for (ObjectIterator<Object2BooleanMap.Entry<ResourceKey<Recipe<?>>>> objectIterator = this.recipes.object2BooleanEntrySet().iterator(); objectIterator.hasNext(); ) {
/* 135 */       Object2BooleanMap.Entry<ResourceKey<Recipe<?>>> e = objectIterator.next();
/* 136 */       if (recipes.contains((ResourceKey)e.getKey()) != e.getBooleanValue())
/* 137 */         return false; 
/*     */     } 
/* 141 */     if (!this.advancements.isEmpty()) {
/* 142 */       PlayerAdvancements advancements = player.getAdvancements();
/* 143 */       ServerAdvancementManager serverAdvancements = player.level().getServer().getAdvancements();
/* 145 */       for (Map.Entry<Identifier, AdvancementPredicate> entry : this.advancements.entrySet()) {
/* 146 */         AdvancementHolder advancement = serverAdvancements.get(entry.getKey());
/* 147 */         if (advancement == null || !((AdvancementPredicate)entry.getValue()).test(advancements.getOrStartProgress(advancement)))
/* 148 */           return false; 
/*     */       } 
/*     */     } 
/* 153 */     if (this.lookingAt.isPresent()) {
/* 154 */       Vec3 from = player.getEyePosition();
/* 155 */       Vec3 viewVec = player.getViewVector(1.0F);
/* 156 */       Vec3 to = from.add(viewVec.x * 100.0D, viewVec.y * 100.0D, viewVec.z * 100.0D);
/* 157 */       EntityHitResult lookingAtResult = ProjectileUtil.getEntityHitResult((Level)player.level(), (Entity)player, from, to, (new AABB(from, to)).inflate(1.0D), e -> !e.isSpectator(), 0.0F);
/* 158 */       if (lookingAtResult == null || lookingAtResult.getType() != HitResult.Type.ENTITY)
/* 159 */         return false; 
/* 161 */       Entity lookingAtEntity = lookingAtResult.getEntity();
/* 162 */       if (!((EntityPredicate)this.lookingAt.get()).matches(player, lookingAtEntity) || !player.hasLineOfSight(lookingAtEntity))
/* 163 */         return false; 
/*     */     } 
/* 168 */     if (this.input.isPresent() && !((InputPredicate)this.input.get()).matches(player.getLastClientInput()))
/* 169 */       return false; 
/* 172 */     return true;
/*     */   }
/*     */   
/*     */   private static final class StatMatcher<T> extends Record {
/*     */     private final StatType<T> type;
/*     */     
/*     */     private final Holder<T> value;
/*     */     
/*     */     private final MinMaxBounds.Ints range;
/*     */     
/*     */     private final Supplier<Stat<T>> stat;
/*     */     
/*     */     private StatMatcher(StatType<T> type, Holder<T> value, MinMaxBounds.Ints range, Supplier<Stat<T>> stat) {
/* 175 */       this.type = type;
/* 175 */       this.value = value;
/* 175 */       this.range = range;
/* 175 */       this.stat = stat;
/*     */     }
/*     */     
/*     */     public final String toString() {
/*     */       // Byte code:
/*     */       //   0: aload_0
/*     */       //   1: <illegal opcode> toString : (Lnet/minecraft/advancements/predicates/entity/PlayerPredicate$StatMatcher;)Ljava/lang/String;
/*     */       //   6: areturn
/*     */       // Line number table:
/*     */       //   Java source line number -> byte code offset
/*     */       //   #175	-> 0
/*     */       // Local variable table:
/*     */       //   start	length	slot	name	descriptor
/*     */       //   0	7	0	this	Lnet/minecraft/advancements/predicates/entity/PlayerPredicate$StatMatcher;
/*     */       // Local variable type table:
/*     */       //   start	length	slot	name	signature
/*     */       //   0	7	0	this	Lnet/minecraft/advancements/predicates/entity/PlayerPredicate$StatMatcher<TT;>;
/*     */     }
/*     */     
/*     */     public final int hashCode() {
/*     */       // Byte code:
/*     */       //   0: aload_0
/*     */       //   1: <illegal opcode> hashCode : (Lnet/minecraft/advancements/predicates/entity/PlayerPredicate$StatMatcher;)I
/*     */       //   6: ireturn
/*     */       // Line number table:
/*     */       //   Java source line number -> byte code offset
/*     */       //   #175	-> 0
/*     */       // Local variable table:
/*     */       //   start	length	slot	name	descriptor
/*     */       //   0	7	0	this	Lnet/minecraft/advancements/predicates/entity/PlayerPredicate$StatMatcher;
/*     */       // Local variable type table:
/*     */       //   start	length	slot	name	signature
/*     */       //   0	7	0	this	Lnet/minecraft/advancements/predicates/entity/PlayerPredicate$StatMatcher<TT;>;
/*     */     }
/*     */     
/*     */     public final boolean equals(Object o) {
/*     */       // Byte code:
/*     */       //   0: aload_0
/*     */       //   1: aload_1
/*     */       //   2: <illegal opcode> equals : (Lnet/minecraft/advancements/predicates/entity/PlayerPredicate$StatMatcher;Ljava/lang/Object;)Z
/*     */       //   7: ireturn
/*     */       // Line number table:
/*     */       //   Java source line number -> byte code offset
/*     */       //   #175	-> 0
/*     */       // Local variable table:
/*     */       //   start	length	slot	name	descriptor
/*     */       //   0	8	0	this	Lnet/minecraft/advancements/predicates/entity/PlayerPredicate$StatMatcher;
/*     */       //   0	8	1	o	Ljava/lang/Object;
/*     */       // Local variable type table:
/*     */       //   start	length	slot	name	signature
/*     */       //   0	8	0	this	Lnet/minecraft/advancements/predicates/entity/PlayerPredicate$StatMatcher<TT;>;
/*     */     }
/*     */     
/*     */     public StatType<T> type() {
/* 175 */       return this.type;
/*     */     }
/*     */     
/*     */     public Holder<T> value() {
/* 175 */       return this.value;
/*     */     }
/*     */     
/*     */     public MinMaxBounds.Ints range() {
/* 175 */       return this.range;
/*     */     }
/*     */     
/*     */     public Supplier<Stat<T>> stat() {
/* 175 */       return this.stat;
/*     */     }
/*     */     
/* 176 */     public static final Codec<StatMatcher<?>> CODEC = BuiltInRegistries.STAT_TYPE.byNameCodec().dispatch(StatMatcher::type, StatMatcher::createTypedCodec);
/*     */     
/*     */     private static <T> MapCodec<StatMatcher<T>> createTypedCodec(StatType<T> type) {
/* 179 */       return RecordCodecBuilder.mapCodec(i -> i.group((App)type.getRegistry().holderByNameCodec().fieldOf("stat").forGetter(StatMatcher::value), (App)MinMaxBounds.Ints.CODEC.optionalFieldOf("value", MinMaxBounds.Ints.ANY).forGetter(StatMatcher::range)).apply((Applicative)i, ()));
/*     */     }
/*     */     
/*     */     public StatMatcher(StatType<T> type, Holder<T> value, MinMaxBounds.Ints range) {
/* 186 */       this(type, value, range, (Supplier<Stat<T>>)Suppliers.memoize(() -> type.get(value.value())));
/*     */     }
/*     */     
/*     */     public boolean matches(StatsCounter counter) {
/* 190 */       return this.range.matches(counter.getValue(this.stat.get()));
/*     */     }
/*     */   }
/*     */   
/*     */   public static class Builder {
/*     */     private MinMaxBounds.Ints level;
/*     */     
/*     */     private FoodPredicate food;
/*     */     
/*     */     private GameTypePredicate gameType;
/*     */     
/*     */     private final ImmutableList.Builder<PlayerPredicate.StatMatcher<?>> stats;
/*     */     
/*     */     private final Object2BooleanMap<ResourceKey<Recipe<?>>> recipes;
/*     */     
/*     */     private final Map<Identifier, PlayerPredicate.AdvancementPredicate> advancements;
/*     */     
/*     */     private Optional<EntityPredicate> lookingAt;
/*     */     
/*     */     private Optional<InputPredicate> input;
/*     */     
/*     */     public Builder() {
/* 195 */       this.level = MinMaxBounds.Ints.ANY;
/* 196 */       this.food = FoodPredicate.ANY;
/* 197 */       this.gameType = GameTypePredicate.ANY;
/* 198 */       this.stats = ImmutableList.builder();
/* 199 */       this.recipes = (Object2BooleanMap<ResourceKey<Recipe<?>>>)new Object2BooleanOpenHashMap();
/* 200 */       this.advancements = Maps.newHashMap();
/* 201 */       this.lookingAt = Optional.empty();
/* 202 */       this.input = Optional.empty();
/*     */     }
/*     */     
/*     */     public static Builder player() {
/* 205 */       return new Builder();
/*     */     }
/*     */     
/*     */     public Builder setLevel(MinMaxBounds.Ints level) {
/* 209 */       this.level = level;
/* 210 */       return this;
/*     */     }
/*     */     
/*     */     public Builder setFood(FoodPredicate food) {
/* 214 */       this.food = food;
/* 215 */       return this;
/*     */     }
/*     */     
/*     */     public <T> Builder addStat(StatType<T> type, Holder.Reference<T> value, MinMaxBounds.Ints range) {
/* 219 */       this.stats.add(new PlayerPredicate.StatMatcher<>(type, (Holder<T>)value, range));
/* 220 */       return this;
/*     */     }
/*     */     
/*     */     public Builder addRecipe(ResourceKey<Recipe<?>> recipe, boolean present) {
/* 224 */       this.recipes.put(recipe, present);
/* 225 */       return this;
/*     */     }
/*     */     
/*     */     public Builder setGameType(GameTypePredicate gameType) {
/* 229 */       this.gameType = gameType;
/* 230 */       return this;
/*     */     }
/*     */     
/*     */     public Builder setLookingAt(EntityPredicate.Builder lookingAt) {
/* 234 */       this.lookingAt = Optional.of(lookingAt.build());
/* 235 */       return this;
/*     */     }
/*     */     
/*     */     public Builder checkAdvancementDone(Identifier advancement, boolean isDone) {
/* 239 */       this.advancements.put(advancement, new PlayerPredicate.AdvancementDonePredicate(isDone));
/* 240 */       return this;
/*     */     }
/*     */     
/*     */     public Builder checkAdvancementCriterions(Identifier advancement, Map<String, Boolean> criterions) {
/* 244 */       this.advancements.put(advancement, new PlayerPredicate.AdvancementCriterionsPredicate((Object2BooleanMap<String>)new Object2BooleanOpenHashMap(criterions)));
/* 245 */       return this;
/*     */     }
/*     */     
/*     */     public Builder hasInput(InputPredicate input) {
/* 249 */       this.input = Optional.of(input);
/* 250 */       return this;
/*     */     }
/*     */     
/*     */     public PlayerPredicate build() {
/* 254 */       return new PlayerPredicate(this.level, this.food, this.gameType, (List<PlayerPredicate.StatMatcher<?>>)this.stats.build(), this.recipes, this.advancements, this.lookingAt, this.input);
/*     */     }
/*     */   }
/*     */ }


/* Location:              C:\Users\karol\AppData\Roaming\ModrinthApp\meta\versions\26.2-0.19.5\26.2-0.19.5.jar!\net\minecraft\advancements\predicates\entity\PlayerPredicate.class
 * Java compiler version: 25 (69.0)
 * JD-Core Version:       1.1.3
 */