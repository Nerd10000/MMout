/*    */ package net.minecraft.world.entity.ai.behavior;
/*    */ 
/*    */ import com.google.common.collect.ImmutableMap;
/*    */ import java.util.Map;
/*    */ import net.minecraft.server.level.ServerLevel;
/*    */ import net.minecraft.util.Mth;
/*    */ import net.minecraft.util.RandomSource;
/*    */ import net.minecraft.util.valueproviders.IntProvider;
/*    */ import net.minecraft.world.entity.LivingEntity;
/*    */ import net.minecraft.world.entity.Mob;
/*    */ import net.minecraft.world.entity.ai.memory.MemoryModuleType;
/*    */ import net.minecraft.world.entity.ai.memory.MemoryStatus;
/*    */ import net.minecraft.world.phys.Vec3;
/*    */ 
/*    */ public class RandomLookAround extends Behavior<Mob> {
/*    */   private final IntProvider interval;
/*    */   
/*    */   private final float maxYaw;
/*    */   
/*    */   private final float minPitch;
/*    */   
/*    */   private final float pitchRange;
/*    */   
/*    */   public RandomLookAround(IntProvider interval, float maxYaw, float minPitch, float maxPitch) {
/* 22 */     super((Map<MemoryModuleType<?>, MemoryStatus>)ImmutableMap.of(MemoryModuleType.LOOK_TARGET, MemoryStatus.VALUE_ABSENT, MemoryModuleType.GAZE_COOLDOWN_TICKS, MemoryStatus.VALUE_ABSENT));
/* 23 */     if (minPitch > maxPitch)
/* 24 */       throw new IllegalArgumentException("Minimum pitch is larger than maximum pitch! " + minPitch + " > " + maxPitch); 
/* 26 */     this.interval = interval;
/* 27 */     this.maxYaw = maxYaw;
/* 28 */     this.minPitch = minPitch;
/* 29 */     this.pitchRange = maxPitch - minPitch;
/*    */   }
/*    */   
/*    */   protected void start(ServerLevel level, Mob body, long timestamp) {
/* 34 */     RandomSource random = body.getRandom();
/* 36 */     float pitch = Mth.clamp(random.nextFloat() * this.pitchRange + this.minPitch, -90.0F, 90.0F);
/* 37 */     float rotation = Mth.wrapDegrees(body.getYRot() + 2.0F * random.nextFloat() * this.maxYaw - this.maxYaw);
/* 38 */     Vec3 newLookVec = Vec3.directionFromRotation(pitch, rotation);
/* 40 */     body.getBrain().setMemory(MemoryModuleType.LOOK_TARGET, new BlockPosTracker(body.getEyePosition().add(newLookVec)));
/* 41 */     body.getBrain().setMemory(MemoryModuleType.GAZE_COOLDOWN_TICKS, Integer.valueOf(this.interval.sample(random)));
/*    */   }
/*    */ }


/* Location:              C:\Users\karol\AppData\Roaming\ModrinthApp\meta\versions\26.2-0.19.5\26.2-0.19.5.jar!\net\minecraft\world\entity\ai\behavior\RandomLookAround.class
 * Java compiler version: 25 (69.0)
 * JD-Core Version:       1.1.3
 */