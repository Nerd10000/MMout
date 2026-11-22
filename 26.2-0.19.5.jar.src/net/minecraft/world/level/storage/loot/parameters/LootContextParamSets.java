/*     */ package net.minecraft.world.level.storage.loot.parameters;
/*     */ 
/*     */ import com.google.common.collect.BiMap;
/*     */ import com.google.common.collect.HashBiMap;
/*     */ import com.mojang.serialization.Codec;
/*     */ import com.mojang.serialization.DataResult;
/*     */ import java.util.Objects;
/*     */ import java.util.Optional;
/*     */ import java.util.function.Consumer;
/*     */ import net.minecraft.resources.Identifier;
/*     */ import net.minecraft.util.context.ContextKeySet;
/*     */ 
/*     */ public class LootContextParamSets {
/*  14 */   private static final BiMap<Identifier, ContextKeySet> REGISTRY = (BiMap<Identifier, ContextKeySet>)HashBiMap.create();
/*     */   
/*     */   public static final Codec<ContextKeySet> CODEC;
/*     */   
/*     */   static {
/*  20 */     Objects.requireNonNull(REGISTRY.inverse());
/*  20 */     CODEC = Identifier.CODEC.comapFlatMap(location -> (DataResult)Optional.<ContextKeySet>ofNullable((ContextKeySet)REGISTRY.get(location)).map(DataResult::success).orElseGet(()), REGISTRY.inverse()::get);
/*     */   }
/*     */   
/*  23 */   public static final ContextKeySet EMPTY = register("empty", builder -> {
/*     */       
/*     */       });
/*     */   
/*     */   public static final ContextKeySet CHEST;
/*     */   
/*     */   public static final ContextKeySet COMMAND;
/*     */   
/*     */   public static final ContextKeySet SELECTOR;
/*     */   
/*     */   public static final ContextKeySet VILLAGER_TRADE;
/*     */   
/*     */   public static final ContextKeySet FISHING;
/*     */   
/*     */   public static final ContextKeySet ENTITY;
/*     */   
/*     */   public static final ContextKeySet EQUIPMENT;
/*     */   
/*     */   public static final ContextKeySet ARCHAEOLOGY;
/*     */   
/*     */   public static final ContextKeySet GIFT;
/*     */   
/*     */   public static final ContextKeySet PIGLIN_BARTER;
/*     */   
/*     */   public static final ContextKeySet VAULT;
/*     */   
/*     */   public static final ContextKeySet ADVANCEMENT_REWARD;
/*     */   
/*     */   public static final ContextKeySet ADVANCEMENT_ENTITY;
/*     */   
/*     */   public static final ContextKeySet ADVANCEMENT_LOCATION;
/*     */   
/*     */   public static final ContextKeySet BLOCK_USE;
/*     */   
/*     */   public static final ContextKeySet ALL_PARAMS;
/*     */   
/*     */   public static final ContextKeySet BLOCK;
/*     */   
/*     */   public static final ContextKeySet SHEARING;
/*     */   
/*     */   public static final ContextKeySet ENTITY_INTERACT;
/*     */   
/*     */   public static final ContextKeySet BLOCK_INTERACT;
/*     */   
/*     */   public static final ContextKeySet ENCHANTED_DAMAGE;
/*     */   
/*     */   public static final ContextKeySet ENCHANTED_ITEM;
/*     */   
/*     */   public static final ContextKeySet ENCHANTED_LOCATION;
/*     */   
/*     */   public static final ContextKeySet ENCHANTED_ENTITY;
/*     */   
/*     */   public static final ContextKeySet HIT_BLOCK;
/*     */   
/*     */   static {
/*  25 */     CHEST = register("chest", builder -> builder.required(LootContextParams.ORIGIN).optional(LootContextParams.THIS_ENTITY));
/*  30 */     COMMAND = register("command", builder -> builder.required(LootContextParams.ORIGIN).optional(LootContextParams.THIS_ENTITY));
/*  35 */     SELECTOR = register("selector", builder -> builder.required(LootContextParams.ORIGIN).required(LootContextParams.THIS_ENTITY));
/*  40 */     VILLAGER_TRADE = register("villager_trade", builder -> builder.required(LootContextParams.ORIGIN).required(LootContextParams.THIS_ENTITY).required(LootContextParams.ADDITIONAL_COST_COMPONENT_ALLOWED));
/*  46 */     FISHING = register("fishing", builder -> builder.required(LootContextParams.ORIGIN).required(LootContextParams.TOOL).optional(LootContextParams.THIS_ENTITY));
/*  52 */     ENTITY = register("entity", builder -> builder.required(LootContextParams.THIS_ENTITY).required(LootContextParams.ORIGIN).required(LootContextParams.DAMAGE_SOURCE).optional(LootContextParams.ATTACKING_ENTITY).optional(LootContextParams.DIRECT_ATTACKING_ENTITY).optional(LootContextParams.LAST_DAMAGE_PLAYER));
/*  61 */     EQUIPMENT = register("equipment", builder -> builder.required(LootContextParams.ORIGIN).required(LootContextParams.THIS_ENTITY));
/*  66 */     ARCHAEOLOGY = register("archaeology", builder -> builder.required(LootContextParams.ORIGIN).required(LootContextParams.THIS_ENTITY).required(LootContextParams.TOOL));
/*  72 */     GIFT = register("gift", builder -> builder.required(LootContextParams.ORIGIN).required(LootContextParams.THIS_ENTITY));
/*  77 */     PIGLIN_BARTER = register("barter", builder -> builder.required(LootContextParams.THIS_ENTITY));
/*  81 */     VAULT = register("vault", builder -> builder.required(LootContextParams.ORIGIN).optional(LootContextParams.THIS_ENTITY).optional(LootContextParams.TOOL));
/*  87 */     ADVANCEMENT_REWARD = register("advancement_reward", builder -> builder.required(LootContextParams.THIS_ENTITY).required(LootContextParams.ORIGIN));
/*  92 */     ADVANCEMENT_ENTITY = register("advancement_entity", builder -> builder.required(LootContextParams.THIS_ENTITY).required(LootContextParams.ORIGIN));
/*  97 */     ADVANCEMENT_LOCATION = register("advancement_location", builder -> builder.required(LootContextParams.THIS_ENTITY).required(LootContextParams.ORIGIN).required(LootContextParams.TOOL).required(LootContextParams.BLOCK_STATE));
/* 104 */     BLOCK_USE = register("block_use", builder -> builder.required(LootContextParams.THIS_ENTITY).required(LootContextParams.ORIGIN).required(LootContextParams.BLOCK_STATE));
/* 110 */     ALL_PARAMS = register("generic", builder -> builder.required(LootContextParams.THIS_ENTITY).required(LootContextParams.LAST_DAMAGE_PLAYER).required(LootContextParams.DAMAGE_SOURCE).required(LootContextParams.ATTACKING_ENTITY).required(LootContextParams.DIRECT_ATTACKING_ENTITY).required(LootContextParams.ORIGIN).required(LootContextParams.BLOCK_STATE).required(LootContextParams.BLOCK_ENTITY).required(LootContextParams.TOOL).required(LootContextParams.EXPLOSION_RADIUS).required(LootContextParams.ADDITIONAL_COST_COMPONENT_ALLOWED));
/* 124 */     BLOCK = register("block", builder -> builder.required(LootContextParams.BLOCK_STATE).required(LootContextParams.ORIGIN).required(LootContextParams.TOOL).optional(LootContextParams.THIS_ENTITY).optional(LootContextParams.BLOCK_ENTITY).optional(LootContextParams.EXPLOSION_RADIUS));
/* 133 */     SHEARING = register("shearing", builder -> builder.required(LootContextParams.ORIGIN).required(LootContextParams.THIS_ENTITY).required(LootContextParams.TOOL));
/* 139 */     ENTITY_INTERACT = register("entity_interact", builder -> builder.required(LootContextParams.TARGET_ENTITY).optional(LootContextParams.INTERACTING_ENTITY).required(LootContextParams.TOOL));
/* 145 */     BLOCK_INTERACT = register("block_interact", builder -> builder.required(LootContextParams.BLOCK_STATE).optional(LootContextParams.BLOCK_ENTITY).optional(LootContextParams.INTERACTING_ENTITY).optional(LootContextParams.TOOL));
/* 152 */     ENCHANTED_DAMAGE = register("enchanted_damage", builder -> builder.required(LootContextParams.THIS_ENTITY).required(LootContextParams.ENCHANTMENT_LEVEL).required(LootContextParams.ORIGIN).required(LootContextParams.DAMAGE_SOURCE).optional(LootContextParams.DIRECT_ATTACKING_ENTITY).optional(LootContextParams.ATTACKING_ENTITY));
/* 161 */     ENCHANTED_ITEM = register("enchanted_item", builder -> builder.required(LootContextParams.TOOL).required(LootContextParams.ENCHANTMENT_LEVEL));
/* 166 */     ENCHANTED_LOCATION = register("enchanted_location", builder -> builder.required(LootContextParams.THIS_ENTITY).required(LootContextParams.ENCHANTMENT_LEVEL).required(LootContextParams.ORIGIN).required(LootContextParams.ENCHANTMENT_ACTIVE));
/* 173 */     ENCHANTED_ENTITY = register("enchanted_entity", builder -> builder.required(LootContextParams.THIS_ENTITY).required(LootContextParams.ENCHANTMENT_LEVEL).required(LootContextParams.ORIGIN));
/* 179 */     HIT_BLOCK = register("hit_block", builder -> builder.required(LootContextParams.THIS_ENTITY).required(LootContextParams.ENCHANTMENT_LEVEL).required(LootContextParams.ORIGIN).required(LootContextParams.BLOCK_STATE));
/*     */   }
/*     */   
/*     */   private static ContextKeySet register(String name, Consumer<ContextKeySet.Builder> consumer) {
/* 187 */     ContextKeySet.Builder builder = new ContextKeySet.Builder();
/* 188 */     consumer.accept(builder);
/* 189 */     ContextKeySet result = builder.build();
/* 190 */     Identifier id = Identifier.withDefaultNamespace(name);
/* 191 */     ContextKeySet prev = (ContextKeySet)REGISTRY.put(id, result);
/* 192 */     if (prev != null)
/* 193 */       throw new IllegalStateException("Loot table parameter set " + String.valueOf(id) + " is already registered"); 
/* 195 */     return result;
/*     */   }
/*     */ }


/* Location:              C:\Users\karol\AppData\Roaming\ModrinthApp\meta\versions\26.2-0.19.5\26.2-0.19.5.jar!\net\minecraft\world\level\storage\loot\parameters\LootContextParamSets.class
 * Java compiler version: 25 (69.0)
 * JD-Core Version:       1.1.3
 */