/*    */ package net.minecraft.world.level.storage.loot.parameters;
/*    */ 
/*    */ import net.minecraft.util.Unit;
/*    */ import net.minecraft.util.context.ContextKey;
/*    */ import net.minecraft.world.damagesource.DamageSource;
/*    */ import net.minecraft.world.entity.Entity;
/*    */ import net.minecraft.world.entity.player.Player;
/*    */ import net.minecraft.world.item.ItemInstance;
/*    */ import net.minecraft.world.level.block.entity.BlockEntity;
/*    */ import net.minecraft.world.level.block.state.BlockState;
/*    */ import net.minecraft.world.phys.Vec3;
/*    */ 
/*    */ public class LootContextParams {
/* 15 */   public static final ContextKey<Entity> THIS_ENTITY = ContextKey.vanilla("this_entity");
/*    */   
/* 17 */   public static final ContextKey<Entity> INTERACTING_ENTITY = ContextKey.vanilla("interacting_entity");
/*    */   
/* 19 */   public static final ContextKey<Entity> TARGET_ENTITY = ContextKey.vanilla("target_entity");
/*    */   
/* 21 */   public static final ContextKey<Player> LAST_DAMAGE_PLAYER = ContextKey.vanilla("last_damage_player");
/*    */   
/* 23 */   public static final ContextKey<DamageSource> DAMAGE_SOURCE = ContextKey.vanilla("damage_source");
/*    */   
/* 25 */   public static final ContextKey<Entity> ATTACKING_ENTITY = ContextKey.vanilla("attacking_entity");
/*    */   
/* 27 */   public static final ContextKey<Entity> DIRECT_ATTACKING_ENTITY = ContextKey.vanilla("direct_attacking_entity");
/*    */   
/* 29 */   public static final ContextKey<Vec3> ORIGIN = ContextKey.vanilla("origin");
/*    */   
/* 31 */   public static final ContextKey<BlockState> BLOCK_STATE = ContextKey.vanilla("block_state");
/*    */   
/* 33 */   public static final ContextKey<BlockEntity> BLOCK_ENTITY = ContextKey.vanilla("block_entity");
/*    */   
/* 35 */   public static final ContextKey<ItemInstance> TOOL = ContextKey.vanilla("tool");
/*    */   
/* 37 */   public static final ContextKey<Float> EXPLOSION_RADIUS = ContextKey.vanilla("explosion_radius");
/*    */   
/* 39 */   public static final ContextKey<Integer> ENCHANTMENT_LEVEL = ContextKey.vanilla("enchantment_level");
/*    */   
/* 41 */   public static final ContextKey<Boolean> ENCHANTMENT_ACTIVE = ContextKey.vanilla("enchantment_active");
/*    */   
/* 43 */   public static final ContextKey<Unit> ADDITIONAL_COST_COMPONENT_ALLOWED = ContextKey.vanilla("additional_cost_component_allowed");
/*    */ }


/* Location:              C:\Users\karol\AppData\Roaming\ModrinthApp\meta\versions\26.2-0.19.5\26.2-0.19.5.jar!\net\minecraft\world\level\storage\loot\parameters\LootContextParams.class
 * Java compiler version: 25 (69.0)
 * JD-Core Version:       1.1.3
 */