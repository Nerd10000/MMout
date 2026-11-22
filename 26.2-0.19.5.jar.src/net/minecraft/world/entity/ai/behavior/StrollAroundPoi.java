/*    */ package net.minecraft.world.entity.ai.behavior;
/*    */ 
/*    */ import com.mojang.datafixers.kinds.App;
/*    */ import com.mojang.datafixers.kinds.Applicative;
/*    */ import java.util.Optional;
/*    */ import net.minecraft.core.GlobalPos;
/*    */ import net.minecraft.core.Position;
/*    */ import net.minecraft.server.level.ServerLevel;
/*    */ import net.minecraft.world.entity.PathfinderMob;
/*    */ import net.minecraft.world.entity.ai.behavior.declarative.BehaviorBuilder;
/*    */ import net.minecraft.world.entity.ai.behavior.declarative.MemoryAccessor;
/*    */ import net.minecraft.world.entity.ai.behavior.declarative.Trigger;
/*    */ import net.minecraft.world.entity.ai.memory.MemoryModuleType;
/*    */ import net.minecraft.world.entity.ai.memory.WalkTarget;
/*    */ import net.minecraft.world.entity.ai.util.LandRandomPos;
/*    */ import net.minecraft.world.phys.Vec3;
/*    */ import org.apache.commons.lang3.mutable.MutableLong;
/*    */ 
/*    */ public class StrollAroundPoi {
/*    */   private static final int MIN_TIME_BETWEEN_STROLLS = 180;
/*    */   
/*    */   private static final int STROLL_MAX_XZ_DIST = 8;
/*    */   
/*    */   private static final int STROLL_MAX_Y_DIST = 6;
/*    */   
/*    */   public static OneShot<PathfinderMob> create(MemoryModuleType<GlobalPos> memoryType, float speedModifier, int maxDistanceFromPoi) {
/* 25 */     MutableLong nextOkStartTime = new MutableLong(0L);
/* 27 */     return BehaviorBuilder.create(i -> i.group((App)i.registered(MemoryModuleType.WALK_TARGET), (App)i.present(memoryType)).apply((Applicative)i, ()));
/*    */   }
/*    */ }


/* Location:              C:\Users\karol\AppData\Roaming\ModrinthApp\meta\versions\26.2-0.19.5\26.2-0.19.5.jar!\net\minecraft\world\entity\ai\behavior\StrollAroundPoi.class
 * Java compiler version: 25 (69.0)
 * JD-Core Version:       1.1.3
 */