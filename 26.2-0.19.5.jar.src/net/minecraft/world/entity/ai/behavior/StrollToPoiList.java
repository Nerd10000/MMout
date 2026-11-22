/*    */ package net.minecraft.world.entity.ai.behavior;
/*    */ 
/*    */ import com.mojang.datafixers.kinds.App;
/*    */ import com.mojang.datafixers.kinds.Applicative;
/*    */ import java.util.List;
/*    */ import net.minecraft.core.GlobalPos;
/*    */ import net.minecraft.core.Position;
/*    */ import net.minecraft.server.level.ServerLevel;
/*    */ import net.minecraft.world.entity.ai.behavior.declarative.BehaviorBuilder;
/*    */ import net.minecraft.world.entity.ai.behavior.declarative.MemoryAccessor;
/*    */ import net.minecraft.world.entity.ai.behavior.declarative.Trigger;
/*    */ import net.minecraft.world.entity.ai.memory.MemoryModuleType;
/*    */ import net.minecraft.world.entity.ai.memory.WalkTarget;
/*    */ import net.minecraft.world.entity.npc.villager.Villager;
/*    */ import org.apache.commons.lang3.mutable.MutableLong;
/*    */ 
/*    */ public class StrollToPoiList {
/*    */   public static BehaviorControl<Villager> create(MemoryModuleType<List<GlobalPos>> strollToMemoryType, float speedModifier, int closeEnoughDist, int maxDistanceFromPoi, MemoryModuleType<GlobalPos> mustBeCloseToMemoryType) {
/* 17 */     MutableLong nextOkStartTime = new MutableLong(0L);
/* 19 */     return BehaviorBuilder.create(i -> i.group((App)i.registered(MemoryModuleType.WALK_TARGET), (App)i.present(strollToMemoryType), (App)i.present(mustBeCloseToMemoryType)).apply((Applicative)i, ()));
/*    */   }
/*    */ }


/* Location:              C:\Users\karol\AppData\Roaming\ModrinthApp\meta\versions\26.2-0.19.5\26.2-0.19.5.jar!\net\minecraft\world\entity\ai\behavior\StrollToPoiList.class
 * Java compiler version: 25 (69.0)
 * JD-Core Version:       1.1.3
 */