/*    */ package net.minecraft.world.entity.ai.behavior;
/*    */ 
/*    */ import com.mojang.datafixers.kinds.App;
/*    */ import com.mojang.datafixers.kinds.Applicative;
/*    */ import java.util.List;
/*    */ import java.util.function.Predicate;
/*    */ import net.minecraft.core.BlockPos;
/*    */ import net.minecraft.core.GlobalPos;
/*    */ import net.minecraft.core.Holder;
/*    */ import net.minecraft.core.Position;
/*    */ import net.minecraft.server.level.ServerLevel;
/*    */ import net.minecraft.tags.BlockTags;
/*    */ import net.minecraft.world.entity.LivingEntity;
/*    */ import net.minecraft.world.entity.ai.behavior.declarative.BehaviorBuilder;
/*    */ import net.minecraft.world.entity.ai.behavior.declarative.MemoryAccessor;
/*    */ import net.minecraft.world.entity.ai.behavior.declarative.Trigger;
/*    */ import net.minecraft.world.entity.ai.memory.MemoryModuleType;
/*    */ import net.minecraft.world.entity.ai.village.poi.PoiType;
/*    */ import net.minecraft.world.entity.npc.villager.Villager;
/*    */ import net.minecraft.world.level.block.BedBlock;
/*    */ import net.minecraft.world.level.block.state.BlockState;
/*    */ import net.minecraft.world.level.block.state.properties.Property;
/*    */ import net.minecraft.world.phys.AABB;
/*    */ 
/*    */ public class ValidateNearbyPoi {
/*    */   private static final int MAX_DISTANCE = 16;
/*    */   
/*    */   public static BehaviorControl<LivingEntity> create(Predicate<Holder<PoiType>> poiType, MemoryModuleType<GlobalPos> memoryType) {
/* 26 */     return BehaviorBuilder.create(i -> i.group((App)i.present(memoryType)).apply((Applicative)i, ()));
/*    */   }
/*    */   
/*    */   private static boolean bedIsOccupied(ServerLevel poiLevel, BlockPos poiPos, LivingEntity body) {
/* 52 */     BlockState blockState = poiLevel.getBlockState(poiPos);
/* 53 */     return (blockState.is(BlockTags.BEDS) && ((Boolean)blockState.getValue((Property)BedBlock.OCCUPIED)).booleanValue() && !body.isSleeping());
/*    */   }
/*    */   
/*    */   private static boolean bedIsOccupiedByVillager(ServerLevel poiLevel, BlockPos poiPos) {
/* 57 */     List<Villager> villagers = poiLevel.getEntitiesOfClass(Villager.class, new AABB(poiPos), LivingEntity::isSleeping);
/* 58 */     return !villagers.isEmpty();
/*    */   }
/*    */ }


/* Location:              C:\Users\karol\AppData\Roaming\ModrinthApp\meta\versions\26.2-0.19.5\26.2-0.19.5.jar!\net\minecraft\world\entity\ai\behavior\ValidateNearbyPoi.class
 * Java compiler version: 25 (69.0)
 * JD-Core Version:       1.1.3
 */