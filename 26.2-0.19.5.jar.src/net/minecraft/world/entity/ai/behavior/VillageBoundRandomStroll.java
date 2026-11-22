/*    */ package net.minecraft.world.entity.ai.behavior;
/*    */ 
/*    */ import com.mojang.datafixers.kinds.App;
/*    */ import com.mojang.datafixers.kinds.Applicative;
/*    */ import java.util.Optional;
/*    */ import net.minecraft.core.BlockPos;
/*    */ import net.minecraft.core.SectionPos;
/*    */ import net.minecraft.core.Vec3i;
/*    */ import net.minecraft.server.level.ServerLevel;
/*    */ import net.minecraft.world.entity.PathfinderMob;
/*    */ import net.minecraft.world.entity.ai.behavior.declarative.BehaviorBuilder;
/*    */ import net.minecraft.world.entity.ai.behavior.declarative.MemoryAccessor;
/*    */ import net.minecraft.world.entity.ai.behavior.declarative.Trigger;
/*    */ import net.minecraft.world.entity.ai.memory.MemoryModuleType;
/*    */ import net.minecraft.world.entity.ai.memory.WalkTarget;
/*    */ import net.minecraft.world.entity.ai.util.DefaultRandomPos;
/*    */ import net.minecraft.world.entity.ai.util.LandRandomPos;
/*    */ import net.minecraft.world.phys.Vec3;
/*    */ 
/*    */ public class VillageBoundRandomStroll {
/*    */   private static final int MAX_XZ_DIST = 10;
/*    */   
/*    */   private static final int MAX_Y_DIST = 7;
/*    */   
/*    */   public static OneShot<PathfinderMob> create(float speedModifier) {
/* 25 */     return create(speedModifier, 10, 7);
/*    */   }
/*    */   
/*    */   public static OneShot<PathfinderMob> create(float speedModifier, int maxXyDist, int maxYDist) {
/* 29 */     return BehaviorBuilder.create(i -> i.group((App)i.absent(MemoryModuleType.WALK_TARGET)).apply((Applicative)i, ()));
/*    */   }
/*    */ }


/* Location:              C:\Users\karol\AppData\Roaming\ModrinthApp\meta\versions\26.2-0.19.5\26.2-0.19.5.jar!\net\minecraft\world\entity\ai\behavior\VillageBoundRandomStroll.class
 * Java compiler version: 25 (69.0)
 * JD-Core Version:       1.1.3
 */