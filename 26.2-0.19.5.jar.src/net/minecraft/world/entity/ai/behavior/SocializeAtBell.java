/*    */ package net.minecraft.world.entity.ai.behavior;
/*    */ 
/*    */ import com.mojang.datafixers.kinds.App;
/*    */ import com.mojang.datafixers.kinds.Applicative;
/*    */ import net.minecraft.core.GlobalPos;
/*    */ import net.minecraft.core.Position;
/*    */ import net.minecraft.server.level.ServerLevel;
/*    */ import net.minecraft.world.entity.Entity;
/*    */ import net.minecraft.world.entity.EntityTypes;
/*    */ import net.minecraft.world.entity.LivingEntity;
/*    */ import net.minecraft.world.entity.ai.behavior.declarative.BehaviorBuilder;
/*    */ import net.minecraft.world.entity.ai.behavior.declarative.MemoryAccessor;
/*    */ import net.minecraft.world.entity.ai.behavior.declarative.Trigger;
/*    */ import net.minecraft.world.entity.ai.memory.MemoryModuleType;
/*    */ import net.minecraft.world.entity.ai.memory.NearestVisibleLivingEntities;
/*    */ import net.minecraft.world.entity.ai.memory.WalkTarget;
/*    */ 
/*    */ public class SocializeAtBell {
/*    */   private static final float SPEED_MODIFIER = 0.3F;
/*    */   
/*    */   public static OneShot<LivingEntity> create() {
/* 15 */     return BehaviorBuilder.create(i -> i.group((App)i.registered(MemoryModuleType.WALK_TARGET), (App)i.registered(MemoryModuleType.LOOK_TARGET), (App)i.present(MemoryModuleType.MEETING_POINT), (App)i.present(MemoryModuleType.NEAREST_VISIBLE_LIVING_ENTITIES), (App)i.absent(MemoryModuleType.INTERACTION_TARGET)).apply((Applicative)i, ()));
/*    */   }
/*    */ }


/* Location:              C:\Users\karol\AppData\Roaming\ModrinthApp\meta\versions\26.2-0.19.5\26.2-0.19.5.jar!\net\minecraft\world\entity\ai\behavior\SocializeAtBell.class
 * Java compiler version: 25 (69.0)
 * JD-Core Version:       1.1.3
 */