/*    */ package net.minecraft.world.entity.ai.behavior;
/*    */ 
/*    */ import com.mojang.datafixers.kinds.App;
/*    */ import com.mojang.datafixers.kinds.Applicative;
/*    */ import java.util.Optional;
/*    */ import java.util.function.Function;
/*    */ import java.util.function.Predicate;
/*    */ import net.minecraft.core.Position;
/*    */ import net.minecraft.server.level.ServerLevel;
/*    */ import net.minecraft.world.entity.LivingEntity;
/*    */ import net.minecraft.world.entity.ai.behavior.declarative.BehaviorBuilder;
/*    */ import net.minecraft.world.entity.ai.behavior.declarative.MemoryAccessor;
/*    */ import net.minecraft.world.entity.ai.behavior.declarative.Trigger;
/*    */ import net.minecraft.world.entity.ai.memory.MemoryModuleType;
/*    */ import net.minecraft.world.entity.ai.memory.WalkTarget;
/*    */ 
/*    */ public class StayCloseToTarget {
/*    */   public static BehaviorControl<LivingEntity> create(Function<LivingEntity, Optional<PositionTracker>> targetPositionGetter, Predicate<LivingEntity> shouldRunPredicate, int closeEnough, int tooFar, float speedModifier) {
/* 14 */     return BehaviorBuilder.create(i -> i.group((App)i.registered(MemoryModuleType.LOOK_TARGET), (App)i.registered(MemoryModuleType.WALK_TARGET)).apply((Applicative)i, ()));
/*    */   }
/*    */ }


/* Location:              C:\Users\karol\AppData\Roaming\ModrinthApp\meta\versions\26.2-0.19.5\26.2-0.19.5.jar!\net\minecraft\world\entity\ai\behavior\StayCloseToTarget.class
 * Java compiler version: 25 (69.0)
 * JD-Core Version:       1.1.3
 */