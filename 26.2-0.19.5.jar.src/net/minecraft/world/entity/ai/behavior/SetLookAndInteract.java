/*    */ package net.minecraft.world.entity.ai.behavior;
/*    */ 
/*    */ import com.mojang.datafixers.kinds.App;
/*    */ import com.mojang.datafixers.kinds.Applicative;
/*    */ import java.util.Optional;
/*    */ import net.minecraft.server.level.ServerLevel;
/*    */ import net.minecraft.world.entity.Entity;
/*    */ import net.minecraft.world.entity.EntityType;
/*    */ import net.minecraft.world.entity.LivingEntity;
/*    */ import net.minecraft.world.entity.ai.behavior.declarative.BehaviorBuilder;
/*    */ import net.minecraft.world.entity.ai.behavior.declarative.MemoryAccessor;
/*    */ import net.minecraft.world.entity.ai.behavior.declarative.Trigger;
/*    */ import net.minecraft.world.entity.ai.memory.MemoryModuleType;
/*    */ import net.minecraft.world.entity.ai.memory.NearestVisibleLivingEntities;
/*    */ 
/*    */ public class SetLookAndInteract {
/*    */   public static BehaviorControl<LivingEntity> create(EntityType<?> type, int interactionRange) {
/* 12 */     int interactionRangeSqr = interactionRange * interactionRange;
/* 13 */     return BehaviorBuilder.create(i -> i.group((App)i.registered(MemoryModuleType.LOOK_TARGET), (App)i.absent(MemoryModuleType.INTERACTION_TARGET), (App)i.present(MemoryModuleType.NEAREST_VISIBLE_LIVING_ENTITIES)).apply((Applicative)i, ()));
/*    */   }
/*    */ }


/* Location:              C:\Users\karol\AppData\Roaming\ModrinthApp\meta\versions\26.2-0.19.5\26.2-0.19.5.jar!\net\minecraft\world\entity\ai\behavior\SetLookAndInteract.class
 * Java compiler version: 25 (69.0)
 * JD-Core Version:       1.1.3
 */