/*     */ package net.minecraft.commands.arguments.selector;
/*     */ 
/*     */ import com.mojang.brigadier.StringReader;
/*     */ import com.mojang.brigadier.exceptions.CommandSyntaxException;
/*     */ import com.mojang.serialization.Codec;
/*     */ import it.unimi.dsi.fastutil.objects.ObjectArrayList;
/*     */ import java.util.List;
/*     */ import java.util.UUID;
/*     */ import java.util.function.BiConsumer;
/*     */ import java.util.function.Function;
/*     */ import java.util.function.Predicate;
/*     */ import net.minecraft.advancements.predicates.MinMaxBounds;
/*     */ import net.minecraft.commands.CommandSourceStack;
/*     */ import net.minecraft.commands.arguments.EntityArgument;
/*     */ import net.minecraft.network.chat.Component;
/*     */ import net.minecraft.network.chat.ComponentUtils;
/*     */ import net.minecraft.server.level.ServerLevel;
/*     */ import net.minecraft.server.level.ServerPlayer;
/*     */ import net.minecraft.server.permissions.Permissions;
/*     */ import net.minecraft.util.CompilableString;
/*     */ import net.minecraft.util.Util;
/*     */ import net.minecraft.world.entity.Entity;
/*     */ import net.minecraft.world.entity.EntityType;
/*     */ import net.minecraft.world.flag.FeatureFlagSet;
/*     */ import net.minecraft.world.level.entity.EntityTypeTest;
/*     */ import net.minecraft.world.phys.AABB;
/*     */ import net.minecraft.world.phys.Vec3;
/*     */ 
/*     */ public class EntitySelector {
/*     */   public static final int INFINITE = 2147483647;
/*     */   
/*     */   public static final BiConsumer<Vec3, List<? extends Entity>> ORDER_ARBITRARY = (p, c) -> {
/*     */     
/*     */     };
/*     */   
/*  35 */   private static final EntityTypeTest<Entity, ?> ANY_TYPE = new EntityTypeTest<Entity, Entity>() {
/*     */       public Entity tryCast(Entity entity) {
/*  38 */         return entity;
/*     */       }
/*     */       
/*     */       public Class<? extends Entity> getBaseClass() {
/*  43 */         return Entity.class;
/*     */       }
/*     */     };
/*     */   
/*  47 */   public static final Codec<CompilableString<EntitySelector>> COMPILABLE_CODEC = CompilableString.codec((Function)new CompilableString.CommandParserHelper<EntitySelector>() {
/*     */         protected EntitySelector parse(StringReader reader) throws CommandSyntaxException {
/*  51 */           return (new EntitySelectorParser(reader, true)).parse();
/*     */         }
/*     */         
/*     */         protected String errorMessage(String original, CommandSyntaxException exception) {
/*  56 */           return "Invalid selector component: " + original + ": " + exception.getMessage();
/*     */         }
/*     */       });
/*     */   
/*     */   private final int maxResults;
/*     */   
/*     */   private final boolean includesEntities;
/*     */   
/*     */   private final boolean worldLimited;
/*     */   
/*     */   private final List<Predicate<Entity>> contextFreePredicates;
/*     */   
/*     */   private final MinMaxBounds.Doubles range;
/*     */   
/*     */   private final Function<Vec3, Vec3> position;
/*     */   
/*     */   private final AABB aabb;
/*     */   
/*     */   private final BiConsumer<Vec3, List<? extends Entity>> order;
/*     */   
/*     */   private final boolean currentEntity;
/*     */   
/*     */   private final String playerName;
/*     */   
/*     */   private final UUID entityUUID;
/*     */   
/*     */   private final EntityTypeTest<Entity, ?> type;
/*     */   
/*     */   private final boolean usesSelector;
/*     */   
/*     */   public EntitySelector(int maxResults, boolean includesEntities, boolean worldLimited, List<Predicate<Entity>> contextFreePredicates, MinMaxBounds.Doubles range, Function<Vec3, Vec3> position, AABB aabb, BiConsumer<Vec3, List<? extends Entity>> order, boolean currentEntity, String playerName, UUID entityUUID, EntityType<?> type, boolean usesSelector) {
/*  77 */     this.maxResults = maxResults;
/*  78 */     this.includesEntities = includesEntities;
/*  79 */     this.worldLimited = worldLimited;
/*  80 */     this.contextFreePredicates = contextFreePredicates;
/*  81 */     this.range = range;
/*  82 */     this.position = position;
/*  83 */     this.aabb = aabb;
/*  84 */     this.order = order;
/*  85 */     this.currentEntity = currentEntity;
/*  86 */     this.playerName = playerName;
/*  87 */     this.entityUUID = entityUUID;
/*  88 */     this.type = (type == null) ? ANY_TYPE : (EntityTypeTest)type;
/*  89 */     this.usesSelector = usesSelector;
/*     */   }
/*     */   
/*     */   public int getMaxResults() {
/*  93 */     return this.maxResults;
/*     */   }
/*     */   
/*     */   public boolean includesEntities() {
/*  97 */     return this.includesEntities;
/*     */   }
/*     */   
/*     */   public boolean isSelfSelector() {
/* 101 */     return this.currentEntity;
/*     */   }
/*     */   
/*     */   public boolean isWorldLimited() {
/* 105 */     return this.worldLimited;
/*     */   }
/*     */   
/*     */   public boolean usesSelector() {
/* 109 */     return this.usesSelector;
/*     */   }
/*     */   
/*     */   private void checkPermissions(CommandSourceStack sender) throws CommandSyntaxException {
/* 113 */     if (this.usesSelector && !sender.permissions().hasPermission(Permissions.COMMANDS_ENTITY_SELECTORS))
/* 114 */       throw EntityArgument.ERROR_SELECTORS_NOT_ALLOWED.create(); 
/*     */   }
/*     */   
/*     */   public Entity findSingleEntity(CommandSourceStack sender) throws CommandSyntaxException {
/* 119 */     checkPermissions(sender);
/* 121 */     List<? extends Entity> entities = findEntities(sender);
/* 122 */     if (entities.isEmpty())
/* 123 */       throw EntityArgument.NO_ENTITIES_FOUND.create(); 
/* 125 */     if (entities.size() > 1)
/* 126 */       throw EntityArgument.ERROR_NOT_SINGLE_ENTITY.create(); 
/* 128 */     return entities.get(0);
/*     */   }
/*     */   
/*     */   public List<? extends Entity> findEntities(CommandSourceStack sender) throws CommandSyntaxException {
/* 132 */     checkPermissions(sender);
/* 134 */     if (!this.includesEntities)
/* 135 */       return (List)findPlayers(sender); 
/* 137 */     if (this.playerName != null) {
/* 138 */       ServerPlayer result = sender.getServer().getPlayerList().getPlayerByName(this.playerName);
/* 139 */       if (result == null)
/* 140 */         return List.of(); 
/* 142 */       return (List)List.of(result);
/*     */     } 
/* 144 */     if (this.entityUUID != null) {
/* 145 */       for (ServerLevel level : sender.getServer().getAllLevels()) {
/* 146 */         Entity entity = level.getEntity(this.entityUUID);
/* 147 */         if (entity != null) {
/* 148 */           if (!entity.getType().isEnabled(sender.enabledFeatures()))
/*     */             break; 
/* 151 */           return List.of(entity);
/*     */         } 
/*     */       } 
/* 154 */       return List.of();
/*     */     } 
/* 156 */     Vec3 pos = this.position.apply(sender.getPosition());
/* 157 */     AABB absoluteAabb = getAbsoluteAabb(pos);
/* 159 */     if (this.currentEntity) {
/* 161 */       Predicate<Entity> predicate1 = getPredicate(pos, absoluteAabb, null);
/* 162 */       if (sender.getEntity() != null && predicate1.test(sender.getEntity()))
/* 163 */         return List.of(sender.getEntity()); 
/* 165 */       return List.of();
/*     */     } 
/* 169 */     Predicate<Entity> predicate = getPredicate(pos, absoluteAabb, sender.enabledFeatures());
/* 170 */     ObjectArrayList objectArrayList = new ObjectArrayList();
/* 172 */     if (isWorldLimited()) {
/* 173 */       addEntities((List<Entity>)objectArrayList, sender.getLevel(), absoluteAabb, predicate);
/*     */     } else {
/* 175 */       for (ServerLevel level : sender.getServer().getAllLevels())
/* 176 */         addEntities((List<Entity>)objectArrayList, level, absoluteAabb, predicate); 
/*     */     } 
/* 180 */     return sortAndLimit(pos, (List<? extends Entity>)objectArrayList);
/*     */   }
/*     */   
/*     */   private void addEntities(List<Entity> result, ServerLevel level, AABB absoluteAABB, Predicate<Entity> predicate) {
/* 184 */     int limit = getResultLimit();
/* 185 */     if (result.size() >= limit)
/*     */       return; 
/* 188 */     if (absoluteAABB != null) {
/* 189 */       level.getEntities(this.type, absoluteAABB, predicate, result, limit);
/*     */     } else {
/* 191 */       level.getEntities(this.type, predicate, result, limit);
/*     */     } 
/*     */   }
/*     */   
/*     */   private int getResultLimit() {
/* 197 */     return (this.order == ORDER_ARBITRARY) ? this.maxResults : Integer.MAX_VALUE;
/*     */   }
/*     */   
/*     */   public ServerPlayer findSinglePlayer(CommandSourceStack sender) throws CommandSyntaxException {
/* 201 */     checkPermissions(sender);
/* 203 */     List<ServerPlayer> players = findPlayers(sender);
/* 204 */     if (players.size() != 1)
/* 205 */       throw EntityArgument.NO_PLAYERS_FOUND.create(); 
/* 207 */     return players.get(0);
/*     */   }
/*     */   
/*     */   public List<ServerPlayer> findPlayers(CommandSourceStack sender) throws CommandSyntaxException {
/*     */     ObjectArrayList<ServerPlayer> objectArrayList;
/* 211 */     checkPermissions(sender);
/* 214 */     if (this.playerName != null) {
/* 215 */       ServerPlayer result = sender.getServer().getPlayerList().getPlayerByName(this.playerName);
/* 216 */       if (result == null)
/* 217 */         return List.of(); 
/* 219 */       return List.of(result);
/*     */     } 
/* 221 */     if (this.entityUUID != null) {
/* 222 */       ServerPlayer result = sender.getServer().getPlayerList().getPlayer(this.entityUUID);
/* 223 */       if (result == null)
/* 224 */         return List.of(); 
/* 226 */       return List.of(result);
/*     */     } 
/* 230 */     Vec3 pos = this.position.apply(sender.getPosition());
/* 231 */     AABB absoluteAabb = getAbsoluteAabb(pos);
/* 232 */     Predicate<Entity> predicate = getPredicate(pos, absoluteAabb, null);
/* 234 */     if (this.currentEntity) {
/* 235 */       Entity entity = sender.getEntity();
/* 235 */       if (entity instanceof ServerPlayer) {
/* 235 */         ServerPlayer player = (ServerPlayer)entity;
/* 236 */         if (predicate.test(player))
/* 237 */           return List.of(player); 
/*     */       } 
/* 240 */       return List.of();
/*     */     } 
/* 245 */     int limit = getResultLimit();
/* 246 */     if (isWorldLimited()) {
/* 247 */       List<ServerPlayer> result = sender.getLevel().getPlayers(predicate, limit);
/*     */     } else {
/* 249 */       objectArrayList = new ObjectArrayList();
/* 250 */       for (ServerPlayer player : sender.getServer().getPlayerList().getPlayers()) {
/* 251 */         if (predicate.test(player)) {
/* 252 */           objectArrayList.add(player);
/* 253 */           if (objectArrayList.size() >= limit)
/* 254 */             return (List<ServerPlayer>)objectArrayList; 
/*     */         } 
/*     */       } 
/*     */     } 
/* 260 */     return sortAndLimit(pos, (List<ServerPlayer>)objectArrayList);
/*     */   }
/*     */   
/*     */   private AABB getAbsoluteAabb(Vec3 pos) {
/* 264 */     return (this.aabb != null) ? this.aabb.move(pos) : null;
/*     */   }
/*     */   
/*     */   private Predicate<Entity> getPredicate(Vec3 pos, AABB absoluteAabb, FeatureFlagSet enabledFeatures) {
/*     */     ObjectArrayList<Predicate<Entity>> objectArrayList;
/* 268 */     boolean filterFeatures = (enabledFeatures != null);
/* 269 */     boolean filterAabb = (absoluteAabb != null);
/* 270 */     boolean filterRange = (this.range != null);
/* 272 */     int extraCount = (filterFeatures ? 1 : 0) + (filterAabb ? 1 : 0) + (filterRange ? 1 : 0);
/* 275 */     if (extraCount == 0) {
/* 276 */       List<Predicate<Entity>> completePredicates = this.contextFreePredicates;
/*     */     } else {
/* 278 */       ObjectArrayList<Predicate<Entity>> objectArrayList1 = new ObjectArrayList(this.contextFreePredicates.size() + extraCount);
/* 279 */       objectArrayList1.addAll(this.contextFreePredicates);
/* 281 */       if (filterFeatures)
/* 282 */         objectArrayList1.add(e -> e.getType().isEnabled(enabledFeatures)); 
/* 285 */       if (filterAabb)
/* 286 */         objectArrayList1.add(e -> absoluteAabb.intersects(e.getBoundingBox())); 
/* 289 */       if (filterRange)
/* 290 */         objectArrayList1.add(e -> this.range.matchesSqr(e.distanceToSqr(pos))); 
/* 292 */       objectArrayList = objectArrayList1;
/*     */     } 
/* 294 */     return Util.allOf((List)objectArrayList);
/*     */   }
/*     */   
/*     */   private <T extends Entity> List<T> sortAndLimit(Vec3 pos, List<T> result) {
/* 298 */     if (result.size() > 1)
/* 299 */       this.order.accept(pos, result); 
/* 302 */     return result.subList(0, Math.min(this.maxResults, result.size()));
/*     */   }
/*     */   
/*     */   public static Component joinNames(List<? extends Entity> entities) {
/* 306 */     return ComponentUtils.formatList(entities, Entity::getDisplayName);
/*     */   }
/*     */ }


/* Location:              C:\Users\karol\AppData\Roaming\ModrinthApp\meta\versions\26.2-0.19.5\26.2-0.19.5.jar!\net\minecraft\commands\arguments\selector\EntitySelector.class
 * Java compiler version: 25 (69.0)
 * JD-Core Version:       1.1.3
 */