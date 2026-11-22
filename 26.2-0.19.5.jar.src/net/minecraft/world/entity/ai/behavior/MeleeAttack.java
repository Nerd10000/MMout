/*    */ package net.minecraft.world.entity.ai.behavior;
/*    */ 
/*    */ import com.mojang.datafixers.kinds.App;
/*    */ import com.mojang.datafixers.kinds.Applicative;
/*    */ import java.util.Objects;
/*    */ import java.util.function.Predicate;
/*    */ import net.minecraft.server.level.ServerLevel;
/*    */ import net.minecraft.world.InteractionHand;
/*    */ import net.minecraft.world.entity.Entity;
/*    */ import net.minecraft.world.entity.LivingEntity;
/*    */ import net.minecraft.world.entity.Mob;
/*    */ import net.minecraft.world.entity.ai.behavior.declarative.BehaviorBuilder;
/*    */ import net.minecraft.world.entity.ai.behavior.declarative.MemoryAccessor;
/*    */ import net.minecraft.world.entity.ai.behavior.declarative.Trigger;
/*    */ import net.minecraft.world.entity.ai.memory.MemoryModuleType;
/*    */ import net.minecraft.world.entity.ai.memory.NearestVisibleLivingEntities;
/*    */ 
/*    */ public class MeleeAttack {
/*    */   public static <T extends Mob> OneShot<T> create(int cooldownBetweenAttacks) {
/* 15 */     return create(body -> true, cooldownBetweenAttacks);
/*    */   }
/*    */   
/*    */   public static <T extends Mob> OneShot<T> create(Predicate<T> canAttackPredicate, int cooldownBetweenAttacks) {
/* 19 */     return BehaviorBuilder.create(i -> i.group((App)i.registered(MemoryModuleType.LOOK_TARGET), (App)i.present(MemoryModuleType.ATTACK_TARGET), (App)i.absent(MemoryModuleType.ATTACK_COOLING_DOWN), (App)i.present(MemoryModuleType.NEAREST_VISIBLE_LIVING_ENTITIES)).apply((Applicative)i, ()));
/*    */   }
/*    */   
/*    */   private static boolean isHoldingUsableNonMeleeWeapon(Mob body) {
/* 40 */     Objects.requireNonNull(body);
/* 40 */     return body.isHolding(body::canUseNonMeleeWeapon);
/*    */   }
/*    */ }


/* Location:              C:\Users\karol\AppData\Roaming\ModrinthApp\meta\versions\26.2-0.19.5\26.2-0.19.5.jar!\net\minecraft\world\entity\ai\behavior\MeleeAttack.class
 * Java compiler version: 25 (69.0)
 * JD-Core Version:       1.1.3
 */