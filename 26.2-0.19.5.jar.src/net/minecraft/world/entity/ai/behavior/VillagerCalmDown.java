/*    */ package net.minecraft.world.entity.ai.behavior;
/*    */ 
/*    */ import com.mojang.datafixers.kinds.App;
/*    */ import com.mojang.datafixers.kinds.Applicative;
/*    */ import net.minecraft.server.level.ServerLevel;
/*    */ import net.minecraft.world.entity.Entity;
/*    */ import net.minecraft.world.entity.LivingEntity;
/*    */ import net.minecraft.world.entity.ai.behavior.declarative.BehaviorBuilder;
/*    */ import net.minecraft.world.entity.ai.behavior.declarative.MemoryAccessor;
/*    */ import net.minecraft.world.entity.ai.behavior.declarative.Trigger;
/*    */ import net.minecraft.world.entity.ai.memory.MemoryModuleType;
/*    */ 
/*    */ public class VillagerCalmDown {
/*    */   private static final int SAFE_DISTANCE_FROM_DANGER = 36;
/*    */   
/*    */   public static BehaviorControl<LivingEntity> create() {
/* 13 */     return BehaviorBuilder.create(i -> i.group((App)i.registered(MemoryModuleType.HURT_BY), (App)i.registered(MemoryModuleType.HURT_BY_ENTITY), (App)i.registered(MemoryModuleType.NEAREST_HOSTILE)).apply((Applicative)i, ()));
/*    */   }
/*    */ }


/* Location:              C:\Users\karol\AppData\Roaming\ModrinthApp\meta\versions\26.2-0.19.5\26.2-0.19.5.jar!\net\minecraft\world\entity\ai\behavior\VillagerCalmDown.class
 * Java compiler version: 25 (69.0)
 * JD-Core Version:       1.1.3
 */