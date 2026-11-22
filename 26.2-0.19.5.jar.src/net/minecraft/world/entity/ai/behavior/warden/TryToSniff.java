/*    */ package net.minecraft.world.entity.ai.behavior.warden;
/*    */ 
/*    */ import com.mojang.datafixers.kinds.App;
/*    */ import com.mojang.datafixers.kinds.Applicative;
/*    */ import net.minecraft.server.level.ServerLevel;
/*    */ import net.minecraft.util.Unit;
/*    */ import net.minecraft.util.valueproviders.IntProvider;
/*    */ import net.minecraft.util.valueproviders.UniformInt;
/*    */ import net.minecraft.world.entity.LivingEntity;
/*    */ import net.minecraft.world.entity.Pose;
/*    */ import net.minecraft.world.entity.ai.behavior.BehaviorControl;
/*    */ import net.minecraft.world.entity.ai.behavior.declarative.BehaviorBuilder;
/*    */ import net.minecraft.world.entity.ai.behavior.declarative.MemoryAccessor;
/*    */ import net.minecraft.world.entity.ai.behavior.declarative.Trigger;
/*    */ import net.minecraft.world.entity.ai.memory.MemoryModuleType;
/*    */ 
/*    */ public class TryToSniff {
/* 14 */   private static final IntProvider SNIFF_COOLDOWN = (IntProvider)UniformInt.of(100, 200);
/*    */   
/*    */   public static BehaviorControl<LivingEntity> create() {
/* 17 */     return (BehaviorControl<LivingEntity>)BehaviorBuilder.create(i -> i.group((App)i.registered(MemoryModuleType.IS_SNIFFING), (App)i.registered(MemoryModuleType.WALK_TARGET), (App)i.absent(MemoryModuleType.SNIFF_COOLDOWN), (App)i.present(MemoryModuleType.NEAREST_ATTACKABLE), (App)i.absent(MemoryModuleType.DISTURBANCE_LOCATION)).apply((Applicative)i, ()));
/*    */   }
/*    */ }


/* Location:              C:\Users\karol\AppData\Roaming\ModrinthApp\meta\versions\26.2-0.19.5\26.2-0.19.5.jar!\net\minecraft\world\entity\ai\behavior\warden\TryToSniff.class
 * Java compiler version: 25 (69.0)
 * JD-Core Version:       1.1.3
 */