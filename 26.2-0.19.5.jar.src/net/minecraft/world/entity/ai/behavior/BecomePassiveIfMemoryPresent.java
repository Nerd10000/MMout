/*   */ package net.minecraft.world.entity.ai.behavior;
/*   */ 
/*   */ import com.mojang.datafixers.kinds.App;
/*   */ import com.mojang.datafixers.kinds.Applicative;
/*   */ import net.minecraft.server.level.ServerLevel;
/*   */ import net.minecraft.world.entity.LivingEntity;
/*   */ import net.minecraft.world.entity.ai.behavior.declarative.BehaviorBuilder;
/*   */ import net.minecraft.world.entity.ai.behavior.declarative.MemoryAccessor;
/*   */ import net.minecraft.world.entity.ai.behavior.declarative.Trigger;
/*   */ import net.minecraft.world.entity.ai.memory.MemoryModuleType;
/*   */ 
/*   */ public class BecomePassiveIfMemoryPresent {
/*   */   public static BehaviorControl<LivingEntity> create(MemoryModuleType<?> pacifyingMemory, int pacifyDuration) {
/* 9 */     return BehaviorBuilder.create(i -> i.group((App)i.registered(MemoryModuleType.ATTACK_TARGET), (App)i.absent(MemoryModuleType.PACIFIED), (App)i.present(pacifyingMemory)).apply((Applicative)i, (App)i.point((), ())));
/*   */   }
/*   */ }


/* Location:              C:\Users\karol\AppData\Roaming\ModrinthApp\meta\versions\26.2-0.19.5\26.2-0.19.5.jar!\net\minecraft\world\entity\ai\behavior\BecomePassiveIfMemoryPresent.class
 * Java compiler version: 25 (69.0)
 * JD-Core Version:       1.1.3
 */