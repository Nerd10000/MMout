/*    */ package net.minecraft.world.entity.ai.behavior;
/*    */ 
/*    */ import com.mojang.datafixers.kinds.App;
/*    */ import com.mojang.datafixers.kinds.Applicative;
/*    */ import java.util.List;
/*    */ import java.util.Optional;
/*    */ import net.minecraft.core.GlobalPos;
/*    */ import net.minecraft.core.Holder;
/*    */ import net.minecraft.server.level.ServerLevel;
/*    */ import net.minecraft.world.entity.LivingEntity;
/*    */ import net.minecraft.world.entity.ai.behavior.declarative.BehaviorBuilder;
/*    */ import net.minecraft.world.entity.ai.behavior.declarative.MemoryAccessor;
/*    */ import net.minecraft.world.entity.ai.behavior.declarative.Trigger;
/*    */ import net.minecraft.world.entity.ai.memory.MemoryModuleType;
/*    */ import net.minecraft.world.entity.ai.village.poi.PoiType;
/*    */ import net.minecraft.world.entity.npc.villager.Villager;
/*    */ import net.minecraft.world.entity.npc.villager.VillagerProfession;
/*    */ 
/*    */ public class PoiCompetitorScan {
/*    */   public static BehaviorControl<Villager> create() {
/* 20 */     return BehaviorBuilder.create(i -> i.group((App)i.present(MemoryModuleType.JOB_SITE), (App)i.present(MemoryModuleType.NEAREST_LIVING_ENTITIES)).apply((Applicative)i, ()));
/*    */   }
/*    */   
/*    */   private static Villager selectWinner(Villager first, Villager second) {
/*    */     Villager winner, loser;
/* 40 */     if (first.getVillagerXp() > second.getVillagerXp()) {
/* 41 */       winner = first;
/* 42 */       loser = second;
/*    */     } else {
/* 44 */       winner = second;
/* 45 */       loser = first;
/*    */     } 
/* 48 */     loser.getBrain().eraseMemory(MemoryModuleType.JOB_SITE);
/* 49 */     return winner;
/*    */   }
/*    */   
/*    */   private static boolean competesForSameJobsite(GlobalPos pos, Holder<PoiType> poiType, Villager nearbyVillager) {
/* 53 */     Optional<GlobalPos> jobSite = nearbyVillager.getBrain().getMemory(MemoryModuleType.JOB_SITE);
/* 54 */     return (jobSite.isPresent() && pos
/* 55 */       .equals(jobSite.get()) && 
/* 56 */       hasMatchingProfession(poiType, nearbyVillager.getVillagerData().profession()));
/*    */   }
/*    */   
/*    */   private static boolean hasMatchingProfession(Holder<PoiType> poiType, Holder<VillagerProfession> profession) {
/* 60 */     return ((VillagerProfession)profession.value()).heldJobSite().test(poiType);
/*    */   }
/*    */ }


/* Location:              C:\Users\karol\AppData\Roaming\ModrinthApp\meta\versions\26.2-0.19.5\26.2-0.19.5.jar!\net\minecraft\world\entity\ai\behavior\PoiCompetitorScan.class
 * Java compiler version: 25 (69.0)
 * JD-Core Version:       1.1.3
 */