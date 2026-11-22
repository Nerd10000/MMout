/*    */ package net.minecraft.world.entity.ai.behavior;
/*    */ 
/*    */ import com.mojang.datafixers.kinds.App;
/*    */ import com.mojang.datafixers.kinds.Applicative;
/*    */ import java.util.function.BiPredicate;
/*    */ import net.minecraft.server.level.ServerLevel;
/*    */ import net.minecraft.world.entity.Entity;
/*    */ import net.minecraft.world.entity.LivingEntity;
/*    */ import net.minecraft.world.entity.ai.behavior.declarative.BehaviorBuilder;
/*    */ import net.minecraft.world.entity.ai.behavior.declarative.MemoryAccessor;
/*    */ import net.minecraft.world.entity.ai.behavior.declarative.Trigger;
/*    */ import net.minecraft.world.entity.ai.memory.MemoryModuleType;
/*    */ 
/*    */ public class DismountOrSkipMounting {
/*    */   public static <E extends LivingEntity> BehaviorControl<E> create(int maxWalkDistToRideTarget, BiPredicate<E, Entity> dontRideIf) {
/* 14 */     return BehaviorBuilder.create(i -> i.group((App)i.registered(MemoryModuleType.RIDE_TARGET)).apply((Applicative)i, ()));
/*    */   }
/*    */   
/*    */   private static boolean isVehicleValid(LivingEntity body, Entity vehicle, int maxWalkDistToRideTarget) {
/* 34 */     return (vehicle.isAlive() && vehicle
/* 35 */       .closerThan((Entity)body, maxWalkDistToRideTarget) && vehicle
/* 36 */       .level() == body.level());
/*    */   }
/*    */ }


/* Location:              C:\Users\karol\AppData\Roaming\ModrinthApp\meta\versions\26.2-0.19.5\26.2-0.19.5.jar!\net\minecraft\world\entity\ai\behavior\DismountOrSkipMounting.class
 * Java compiler version: 25 (69.0)
 * JD-Core Version:       1.1.3
 */