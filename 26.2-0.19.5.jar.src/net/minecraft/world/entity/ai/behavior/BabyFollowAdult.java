/*    */ package net.minecraft.world.entity.ai.behavior;
/*    */ 
/*    */ import com.mojang.datafixers.kinds.App;
/*    */ import com.mojang.datafixers.kinds.Applicative;
/*    */ import java.util.function.Function;
/*    */ import net.minecraft.server.level.ServerLevel;
/*    */ import net.minecraft.util.valueproviders.UniformInt;
/*    */ import net.minecraft.world.entity.Entity;
/*    */ import net.minecraft.world.entity.LivingEntity;
/*    */ import net.minecraft.world.entity.ai.behavior.declarative.BehaviorBuilder;
/*    */ import net.minecraft.world.entity.ai.behavior.declarative.MemoryAccessor;
/*    */ import net.minecraft.world.entity.ai.behavior.declarative.Trigger;
/*    */ import net.minecraft.world.entity.ai.memory.MemoryModuleType;
/*    */ import net.minecraft.world.entity.ai.memory.WalkTarget;
/*    */ 
/*    */ public class BabyFollowAdult {
/*    */   public static OneShot<LivingEntity> create(UniformInt followRange, float speedModifier) {
/* 13 */     return create(followRange, mob -> Float.valueOf(speedModifier), MemoryModuleType.NEAREST_VISIBLE_ADULT, false);
/*    */   }
/*    */   
/*    */   public static OneShot<LivingEntity> create(UniformInt followRange, Function<LivingEntity, Float> speedModifier, MemoryModuleType<? extends LivingEntity> nearestVisibleType, boolean targetEye) {
/* 17 */     return BehaviorBuilder.create(i -> i.group((App)i.present(nearestVisibleType), (App)i.registered(MemoryModuleType.LOOK_TARGET), (App)i.absent(MemoryModuleType.WALK_TARGET)).apply((Applicative)i, ()));
/*    */   }
/*    */ }


/* Location:              C:\Users\karol\AppData\Roaming\ModrinthApp\meta\versions\26.2-0.19.5\26.2-0.19.5.jar!\net\minecraft\world\entity\ai\behavior\BabyFollowAdult.class
 * Java compiler version: 25 (69.0)
 * JD-Core Version:       1.1.3
 */