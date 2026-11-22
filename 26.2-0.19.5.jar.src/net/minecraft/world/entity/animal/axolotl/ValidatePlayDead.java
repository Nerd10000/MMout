/*    */ package net.minecraft.world.entity.animal.axolotl;
/*    */ 
/*    */ import com.mojang.datafixers.kinds.App;
/*    */ import com.mojang.datafixers.kinds.Applicative;
/*    */ import net.minecraft.server.level.ServerLevel;
/*    */ import net.minecraft.world.entity.LivingEntity;
/*    */ import net.minecraft.world.entity.ai.behavior.BehaviorControl;
/*    */ import net.minecraft.world.entity.ai.behavior.declarative.BehaviorBuilder;
/*    */ import net.minecraft.world.entity.ai.behavior.declarative.MemoryAccessor;
/*    */ import net.minecraft.world.entity.ai.behavior.declarative.Trigger;
/*    */ import net.minecraft.world.entity.ai.memory.MemoryModuleType;
/*    */ 
/*    */ public class ValidatePlayDead {
/*    */   public static BehaviorControl<LivingEntity> create() {
/* 10 */     return (BehaviorControl<LivingEntity>)BehaviorBuilder.create(i -> i.group((App)i.present(MemoryModuleType.PLAY_DEAD_TICKS), (App)i.registered(MemoryModuleType.HURT_BY_ENTITY)).apply((Applicative)i, ()));
/*    */   }
/*    */ }


/* Location:              C:\Users\karol\AppData\Roaming\ModrinthApp\meta\versions\26.2-0.19.5\26.2-0.19.5.jar!\net\minecraft\world\entity\animal\axolotl\ValidatePlayDead.class
 * Java compiler version: 25 (69.0)
 * JD-Core Version:       1.1.3
 */