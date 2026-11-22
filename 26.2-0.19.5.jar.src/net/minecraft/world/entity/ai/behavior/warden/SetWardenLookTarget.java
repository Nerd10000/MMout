/*    */ package net.minecraft.world.entity.ai.behavior.warden;
/*    */ 
/*    */ import com.mojang.datafixers.kinds.App;
/*    */ import com.mojang.datafixers.kinds.Applicative;
/*    */ import java.util.Optional;
/*    */ import net.minecraft.core.BlockPos;
/*    */ import net.minecraft.server.level.ServerLevel;
/*    */ import net.minecraft.world.entity.Entity;
/*    */ import net.minecraft.world.entity.LivingEntity;
/*    */ import net.minecraft.world.entity.ai.behavior.BehaviorControl;
/*    */ import net.minecraft.world.entity.ai.behavior.BlockPosTracker;
/*    */ import net.minecraft.world.entity.ai.behavior.declarative.BehaviorBuilder;
/*    */ import net.minecraft.world.entity.ai.behavior.declarative.MemoryAccessor;
/*    */ import net.minecraft.world.entity.ai.behavior.declarative.Trigger;
/*    */ import net.minecraft.world.entity.ai.memory.MemoryModuleType;
/*    */ 
/*    */ public class SetWardenLookTarget {
/*    */   public static BehaviorControl<LivingEntity> create() {
/* 15 */     return (BehaviorControl<LivingEntity>)BehaviorBuilder.create(i -> i.group((App)i.registered(MemoryModuleType.LOOK_TARGET), (App)i.registered(MemoryModuleType.DISTURBANCE_LOCATION), (App)i.registered(MemoryModuleType.ROAR_TARGET), (App)i.absent(MemoryModuleType.ATTACK_TARGET)).apply((Applicative)i, ()));
/*    */   }
/*    */ }


/* Location:              C:\Users\karol\AppData\Roaming\ModrinthApp\meta\versions\26.2-0.19.5\26.2-0.19.5.jar!\net\minecraft\world\entity\ai\behavior\warden\SetWardenLookTarget.class
 * Java compiler version: 25 (69.0)
 * JD-Core Version:       1.1.3
 */