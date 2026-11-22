/*    */ package net.minecraft.world.entity.ai.behavior;
/*    */ 
/*    */ import com.mojang.datafixers.kinds.App;
/*    */ import com.mojang.datafixers.kinds.Applicative;
/*    */ import java.util.Optional;
/*    */ import java.util.function.Predicate;
/*    */ import net.minecraft.server.level.ServerLevel;
/*    */ import net.minecraft.util.RandomSource;
/*    */ import net.minecraft.util.valueproviders.UniformInt;
/*    */ import net.minecraft.world.entity.Entity;
/*    */ import net.minecraft.world.entity.EntityType;
/*    */ import net.minecraft.world.entity.LivingEntity;
/*    */ import net.minecraft.world.entity.ai.behavior.declarative.BehaviorBuilder;
/*    */ import net.minecraft.world.entity.ai.behavior.declarative.MemoryAccessor;
/*    */ import net.minecraft.world.entity.ai.behavior.declarative.Trigger;
/*    */ import net.minecraft.world.entity.ai.memory.MemoryModuleType;
/*    */ import net.minecraft.world.entity.ai.memory.NearestVisibleLivingEntities;
/*    */ 
/*    */ @Deprecated
/*    */ public class SetEntityLookTargetSometimes {
/*    */   public static BehaviorControl<LivingEntity> create(float maxDist, UniformInt interval) {
/* 18 */     return create(maxDist, interval, mob -> true);
/*    */   }
/*    */   
/*    */   public static BehaviorControl<LivingEntity> create(EntityType<?> type, float maxDist, UniformInt interval) {
/* 22 */     return create(maxDist, interval, mob -> mob.is(type));
/*    */   }
/*    */   
/*    */   private static BehaviorControl<LivingEntity> create(float maxDist, UniformInt interval, Predicate<LivingEntity> predicate) {
/* 26 */     float maxDistSqr = maxDist * maxDist;
/* 28 */     Ticker ticker = new Ticker(interval);
/* 30 */     return BehaviorBuilder.create(i -> i.group((App)i.absent(MemoryModuleType.LOOK_TARGET), (App)i.present(MemoryModuleType.NEAREST_VISIBLE_LIVING_ENTITIES)).apply((Applicative)i, ()));
/*    */   }
/*    */   
/*    */   public static final class Ticker {
/*    */     private final UniformInt interval;
/*    */     
/*    */     private int ticksUntilNextStart;
/*    */     
/*    */     public Ticker(UniformInt interval) {
/* 54 */       if (interval.minInclusive() <= 1)
/* 55 */         throw new IllegalArgumentException(); 
/* 57 */       this.interval = interval;
/*    */     }
/*    */     
/*    */     public boolean tickDownAndCheck(RandomSource random) {
/* 61 */       if (this.ticksUntilNextStart == 0) {
/* 62 */         this.ticksUntilNextStart = this.interval.sample(random) - 1;
/* 63 */         return false;
/*    */       } 
/* 66 */       return (--this.ticksUntilNextStart == 0);
/*    */     }
/*    */   }
/*    */ }


/* Location:              C:\Users\karol\AppData\Roaming\ModrinthApp\meta\versions\26.2-0.19.5\26.2-0.19.5.jar!\net\minecraft\world\entity\ai\behavior\SetEntityLookTargetSometimes.class
 * Java compiler version: 25 (69.0)
 * JD-Core Version:       1.1.3
 */