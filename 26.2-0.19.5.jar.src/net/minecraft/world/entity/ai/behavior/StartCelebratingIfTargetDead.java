/*    */ package net.minecraft.world.entity.ai.behavior;
/*    */ 
/*    */ import com.mojang.datafixers.kinds.App;
/*    */ import com.mojang.datafixers.kinds.Applicative;
/*    */ import java.util.function.BiPredicate;
/*    */ import net.minecraft.server.level.ServerLevel;
/*    */ import net.minecraft.world.entity.EntityTypes;
/*    */ import net.minecraft.world.entity.LivingEntity;
/*    */ import net.minecraft.world.entity.ai.behavior.declarative.BehaviorBuilder;
/*    */ import net.minecraft.world.entity.ai.behavior.declarative.MemoryAccessor;
/*    */ import net.minecraft.world.entity.ai.behavior.declarative.Trigger;
/*    */ import net.minecraft.world.entity.ai.memory.MemoryModuleType;
/*    */ import net.minecraft.world.level.gamerules.GameRules;
/*    */ 
/*    */ public class StartCelebratingIfTargetDead {
/*    */   public static BehaviorControl<LivingEntity> create(int celebrateDuration, BiPredicate<LivingEntity, LivingEntity> dancePredicate) {
/* 15 */     return BehaviorBuilder.create(i -> i.group((App)i.present(MemoryModuleType.ATTACK_TARGET), (App)i.registered(MemoryModuleType.ANGRY_AT), (App)i.absent(MemoryModuleType.CELEBRATE_LOCATION), (App)i.registered(MemoryModuleType.DANCING)).apply((Applicative)i, ()));
/*    */   }
/*    */ }


/* Location:              C:\Users\karol\AppData\Roaming\ModrinthApp\meta\versions\26.2-0.19.5\26.2-0.19.5.jar!\net\minecraft\world\entity\ai\behavior\StartCelebratingIfTargetDead.class
 * Java compiler version: 25 (69.0)
 * JD-Core Version:       1.1.3
 */