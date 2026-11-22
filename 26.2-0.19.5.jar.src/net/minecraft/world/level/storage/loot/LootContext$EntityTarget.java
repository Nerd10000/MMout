/*     */ package net.minecraft.world.level.storage.loot;
/*     */ 
/*     */ import net.minecraft.util.StringRepresentable;
/*     */ import net.minecraft.util.context.ContextKey;
/*     */ import net.minecraft.world.entity.Entity;
/*     */ import net.minecraft.world.level.storage.loot.parameters.LootContextParams;
/*     */ 
/*     */ public enum EntityTarget implements StringRepresentable, LootContextArg.SimpleGetter<Entity> {
/* 116 */   THIS("this", LootContextParams.THIS_ENTITY),
/* 117 */   ATTACKER("attacker", LootContextParams.ATTACKING_ENTITY),
/* 118 */   DIRECT_ATTACKER("direct_attacker", LootContextParams.DIRECT_ATTACKING_ENTITY),
/* 119 */   ATTACKING_PLAYER("attacking_player", LootContextParams.LAST_DAMAGE_PLAYER),
/* 120 */   TARGET_ENTITY("target_entity", LootContextParams.TARGET_ENTITY),
/* 121 */   INTERACTING_ENTITY("interacting_entity", LootContextParams.INTERACTING_ENTITY);
/*     */   
/*     */   public static final StringRepresentable.EnumCodec<EntityTarget> CODEC;
/*     */   
/*     */   private final String name;
/*     */   
/*     */   private final ContextKey<? extends Entity> param;
/*     */   
/*     */   static {
/* 124 */     CODEC = StringRepresentable.fromEnum(EntityTarget::values);
/*     */   }
/*     */   
/*     */   EntityTarget(String name, ContextKey<? extends Entity> param) {
/* 131 */     this.name = name;
/* 132 */     this.param = param;
/*     */   }
/*     */   
/*     */   public ContextKey<? extends Entity> contextParam() {
/* 137 */     return this.param;
/*     */   }
/*     */   
/*     */   public static EntityTarget getByName(String name) {
/* 141 */     EntityTarget target = (EntityTarget)CODEC.byName(name);
/* 142 */     if (target != null)
/* 143 */       return target; 
/* 145 */     throw new IllegalArgumentException("Invalid entity target " + name);
/*     */   }
/*     */   
/*     */   public String getSerializedName() {
/* 150 */     return this.name;
/*     */   }
/*     */ }


/* Location:              C:\Users\karol\AppData\Roaming\ModrinthApp\meta\versions\26.2-0.19.5\26.2-0.19.5.jar!\net\minecraft\world\level\storage\loot\LootContext$EntityTarget.class
 * Java compiler version: 25 (69.0)
 * JD-Core Version:       1.1.3
 */