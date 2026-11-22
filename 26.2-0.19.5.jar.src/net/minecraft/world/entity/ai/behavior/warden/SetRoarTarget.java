/*    */ package net.minecraft.world.entity.ai.behavior.warden;
/*    */ 
/*    */ import com.mojang.datafixers.kinds.App;
/*    */ import com.mojang.datafixers.kinds.Applicative;
/*    */ import java.util.Objects;
/*    */ import java.util.Optional;
/*    */ import java.util.function.Function;
/*    */ import net.minecraft.server.level.ServerLevel;
/*    */ import net.minecraft.world.entity.LivingEntity;
/*    */ import net.minecraft.world.entity.ai.behavior.BehaviorControl;
/*    */ import net.minecraft.world.entity.ai.behavior.declarative.BehaviorBuilder;
/*    */ import net.minecraft.world.entity.ai.behavior.declarative.MemoryAccessor;
/*    */ import net.minecraft.world.entity.ai.behavior.declarative.Trigger;
/*    */ import net.minecraft.world.entity.ai.memory.MemoryModuleType;
/*    */ import net.minecraft.world.entity.monster.warden.Warden;
/*    */ 
/*    */ public class SetRoarTarget {
/*    */   public static <E extends Warden> BehaviorControl<E> create(Function<E, Optional<? extends LivingEntity>> targetFinderFunction) {
/* 15 */     return (BehaviorControl<E>)BehaviorBuilder.create(i -> i.group((App)i.absent(MemoryModuleType.ROAR_TARGET), (App)i.absent(MemoryModuleType.ATTACK_TARGET), (App)i.registered(MemoryModuleType.CANT_REACH_WALK_TARGET_SINCE)).apply((Applicative)i, ()));
/*    */   }
/*    */ }


/* Location:              C:\Users\karol\AppData\Roaming\ModrinthApp\meta\versions\26.2-0.19.5\26.2-0.19.5.jar!\net\minecraft\world\entity\ai\behavior\warden\SetRoarTarget.class
 * Java compiler version: 25 (69.0)
 * JD-Core Version:       1.1.3
 */