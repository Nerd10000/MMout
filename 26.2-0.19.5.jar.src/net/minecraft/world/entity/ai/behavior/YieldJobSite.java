/*    */ package net.minecraft.world.entity.ai.behavior;
/*    */ 
/*    */ import com.mojang.datafixers.kinds.App;
/*    */ import com.mojang.datafixers.kinds.Applicative;
/*    */ import java.util.List;
/*    */ import java.util.Optional;
/*    */ import net.minecraft.core.BlockPos;
/*    */ import net.minecraft.core.GlobalPos;
/*    */ import net.minecraft.core.Holder;
/*    */ import net.minecraft.server.level.ServerLevel;
/*    */ import net.minecraft.world.entity.LivingEntity;
/*    */ import net.minecraft.world.entity.PathfinderMob;
/*    */ import net.minecraft.world.entity.ai.behavior.declarative.BehaviorBuilder;
/*    */ import net.minecraft.world.entity.ai.behavior.declarative.MemoryAccessor;
/*    */ import net.minecraft.world.entity.ai.behavior.declarative.Trigger;
/*    */ import net.minecraft.world.entity.ai.memory.MemoryModuleType;
/*    */ import net.minecraft.world.entity.ai.village.poi.PoiType;
/*    */ import net.minecraft.world.entity.npc.villager.Villager;
/*    */ import net.minecraft.world.entity.npc.villager.VillagerProfession;
/*    */ import net.minecraft.world.level.pathfinder.Path;
/*    */ 
/*    */ public class YieldJobSite {
/*    */   public static BehaviorControl<Villager> create(float speedModifier) {
/* 21 */     return BehaviorBuilder.create(i -> i.group((App)i.present(MemoryModuleType.POTENTIAL_JOB_SITE), (App)i.absent(MemoryModuleType.JOB_SITE), (App)i.present(MemoryModuleType.NEAREST_LIVING_ENTITIES), (App)i.registered(MemoryModuleType.WALK_TARGET), (App)i.registered(MemoryModuleType.LOOK_TARGET)).apply((Applicative)i, ()));
/*    */   }
/*    */   
/*    */   private static boolean nearbyWantsJobsite(Holder<PoiType> type, Villager nearbyVillager, BlockPos poiPos) {
/* 67 */     boolean nearbyHasPotentialJobSite = nearbyVillager.getBrain().getMemory(MemoryModuleType.POTENTIAL_JOB_SITE).isPresent();
/* 68 */     if (nearbyHasPotentialJobSite)
/* 69 */       return false; 
/* 72 */     Optional<GlobalPos> nearbyVillagerJobSiteMemory = nearbyVillager.getBrain().getMemory(MemoryModuleType.JOB_SITE);
/* 73 */     Holder<VillagerProfession> nearbyProfession = nearbyVillager.getVillagerData().profession();
/* 76 */     if (((VillagerProfession)nearbyProfession.value()).heldJobSite().test(type)) {
/* 77 */       if (nearbyVillagerJobSiteMemory.isEmpty())
/* 78 */         return canReachPos((PathfinderMob)nearbyVillager, poiPos, (PoiType)type.value()); 
/* 80 */       return ((GlobalPos)nearbyVillagerJobSiteMemory.get()).pos().equals(poiPos);
/*    */     } 
/* 82 */     return false;
/*    */   }
/*    */   
/*    */   private static boolean canReachPos(PathfinderMob nearbyVillager, BlockPos poiPos, PoiType type) {
/* 86 */     Path path = nearbyVillager.getNavigation().createPath(poiPos, type.validRange());
/* 87 */     return (path != null && path.canReach());
/*    */   }
/*    */ }


/* Location:              C:\Users\karol\AppData\Roaming\ModrinthApp\meta\versions\26.2-0.19.5\26.2-0.19.5.jar!\net\minecraft\world\entity\ai\behavior\YieldJobSite.class
 * Java compiler version: 25 (69.0)
 * JD-Core Version:       1.1.3
 */