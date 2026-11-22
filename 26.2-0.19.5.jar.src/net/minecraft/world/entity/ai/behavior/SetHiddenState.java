/*    */ package net.minecraft.world.entity.ai.behavior;
/*    */ 
/*    */ import com.mojang.datafixers.kinds.App;
/*    */ import com.mojang.datafixers.kinds.Applicative;
/*    */ import net.minecraft.core.BlockPos;
/*    */ import net.minecraft.core.GlobalPos;
/*    */ import net.minecraft.core.Vec3i;
/*    */ import net.minecraft.server.level.ServerLevel;
/*    */ import net.minecraft.world.entity.LivingEntity;
/*    */ import net.minecraft.world.entity.ai.behavior.declarative.BehaviorBuilder;
/*    */ import net.minecraft.world.entity.ai.behavior.declarative.MemoryAccessor;
/*    */ import net.minecraft.world.entity.ai.behavior.declarative.Trigger;
/*    */ import net.minecraft.world.entity.ai.memory.MemoryModuleType;
/*    */ import org.apache.commons.lang3.mutable.MutableInt;
/*    */ 
/*    */ public class SetHiddenState {
/*    */   private static final int HIDE_TIMEOUT = 300;
/*    */   
/*    */   public static BehaviorControl<LivingEntity> create(int seconds, int closeEnoughDist) {
/* 17 */     int stayHiddenTicks = seconds * 20;
/* 20 */     MutableInt ticksHidden = new MutableInt(0);
/* 22 */     return BehaviorBuilder.create(i -> i.group((App)i.present(MemoryModuleType.HIDING_PLACE), (App)i.present(MemoryModuleType.HEARD_BELL_TIME)).apply((Applicative)i, ()));
/*    */   }
/*    */ }


/* Location:              C:\Users\karol\AppData\Roaming\ModrinthApp\meta\versions\26.2-0.19.5\26.2-0.19.5.jar!\net\minecraft\world\entity\ai\behavior\SetHiddenState.class
 * Java compiler version: 25 (69.0)
 * JD-Core Version:       1.1.3
 */