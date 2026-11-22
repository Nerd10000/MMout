/*    */ package net.minecraft.world.entity.ai.behavior;
/*    */ 
/*    */ import com.mojang.datafixers.kinds.App;
/*    */ import com.mojang.datafixers.kinds.Applicative;
/*    */ import java.util.Optional;
/*    */ import java.util.function.Function;
/*    */ import net.minecraft.server.level.ServerLevel;
/*    */ import net.minecraft.world.entity.Entity;
/*    */ import net.minecraft.world.entity.LivingEntity;
/*    */ import net.minecraft.world.entity.Mob;
/*    */ import net.minecraft.world.entity.ai.behavior.declarative.BehaviorBuilder;
/*    */ import net.minecraft.world.entity.ai.behavior.declarative.MemoryAccessor;
/*    */ import net.minecraft.world.entity.ai.behavior.declarative.Trigger;
/*    */ import net.minecraft.world.entity.ai.memory.MemoryModuleType;
/*    */ import net.minecraft.world.entity.ai.memory.NearestVisibleLivingEntities;
/*    */ import net.minecraft.world.entity.ai.memory.WalkTarget;
/*    */ 
/*    */ public class SetWalkTargetFromAttackTargetIfTargetOutOfReach {
/*    */   private static final int PROJECTILE_ATTACK_RANGE_BUFFER = 1;
/*    */   
/*    */   public static BehaviorControl<Mob> create(float speedModifier) {
/* 19 */     return create(mob -> Float.valueOf(speedModifier));
/*    */   }
/*    */   
/*    */   public static BehaviorControl<Mob> create(Function<LivingEntity, Float> speedModifier) {
/* 23 */     return BehaviorBuilder.create(i -> i.group((App)i.registered(MemoryModuleType.WALK_TARGET), (App)i.registered(MemoryModuleType.LOOK_TARGET), (App)i.present(MemoryModuleType.ATTACK_TARGET), (App)i.registered(MemoryModuleType.NEAREST_VISIBLE_LIVING_ENTITIES)).apply((Applicative)i, ()));
/*    */   }
/*    */ }


/* Location:              C:\Users\karol\AppData\Roaming\ModrinthApp\meta\versions\26.2-0.19.5\26.2-0.19.5.jar!\net\minecraft\world\entity\ai\behavior\SetWalkTargetFromAttackTargetIfTargetOutOfReach.class
 * Java compiler version: 25 (69.0)
 * JD-Core Version:       1.1.3
 */