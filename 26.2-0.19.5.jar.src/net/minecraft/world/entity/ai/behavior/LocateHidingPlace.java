/*    */ package net.minecraft.world.entity.ai.behavior;
/*    */ 
/*    */ import com.mojang.datafixers.kinds.App;
/*    */ import com.mojang.datafixers.kinds.Applicative;
/*    */ import java.util.Optional;
/*    */ import net.minecraft.core.BlockPos;
/*    */ import net.minecraft.core.GlobalPos;
/*    */ import net.minecraft.core.Holder;
/*    */ import net.minecraft.core.Position;
/*    */ import net.minecraft.server.level.ServerLevel;
/*    */ import net.minecraft.world.entity.LivingEntity;
/*    */ import net.minecraft.world.entity.ai.behavior.declarative.BehaviorBuilder;
/*    */ import net.minecraft.world.entity.ai.behavior.declarative.MemoryAccessor;
/*    */ import net.minecraft.world.entity.ai.behavior.declarative.Trigger;
/*    */ import net.minecraft.world.entity.ai.memory.MemoryModuleType;
/*    */ import net.minecraft.world.entity.ai.memory.WalkTarget;
/*    */ import net.minecraft.world.entity.ai.village.poi.PoiManager;
/*    */ import net.minecraft.world.entity.ai.village.poi.PoiTypes;
/*    */ 
/*    */ public class LocateHidingPlace {
/*    */   public static OneShot<LivingEntity> create(int radius, float speedModifier, int closeEnoughDist) {
/* 13 */     return BehaviorBuilder.create(i -> i.group((App)i.absent(MemoryModuleType.WALK_TARGET), (App)i.registered(MemoryModuleType.HOME), (App)i.registered(MemoryModuleType.HIDING_PLACE), (App)i.registered(MemoryModuleType.PATH), (App)i.registered(MemoryModuleType.LOOK_TARGET), (App)i.registered(MemoryModuleType.BREED_TARGET), (App)i.registered(MemoryModuleType.INTERACTION_TARGET)).apply((Applicative)i, ()));
/*    */   }
/*    */ }


/* Location:              C:\Users\karol\AppData\Roaming\ModrinthApp\meta\versions\26.2-0.19.5\26.2-0.19.5.jar!\net\minecraft\world\entity\ai\behavior\LocateHidingPlace.class
 * Java compiler version: 25 (69.0)
 * JD-Core Version:       1.1.3
 */