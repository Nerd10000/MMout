/*    */ package net.minecraft.world.entity.monster.piglin;
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
/*    */ import net.minecraft.world.entity.item.ItemEntity;
/*    */ 
/*    */ public class StartAdmiringItemIfSeen {
/*    */   public static BehaviorControl<LivingEntity> create(int admireDuration) {
/* 11 */     return (BehaviorControl<LivingEntity>)BehaviorBuilder.create(i -> i.group((App)i.present(MemoryModuleType.NEAREST_VISIBLE_WANTED_ITEM), (App)i.absent(MemoryModuleType.ADMIRING_ITEM), (App)i.absent(MemoryModuleType.ADMIRING_DISABLED), (App)i.absent(MemoryModuleType.DISABLE_WALK_TO_ADMIRE_ITEM)).apply((Applicative)i, ()));
/*    */   }
/*    */ }


/* Location:              C:\Users\karol\AppData\Roaming\ModrinthApp\meta\versions\26.2-0.19.5\26.2-0.19.5.jar!\net\minecraft\world\entity\monster\piglin\StartAdmiringItemIfSeen.class
 * Java compiler version: 25 (69.0)
 * JD-Core Version:       1.1.3
 */