/*    */ package net.minecraft.world.entity.ai.behavior;
/*    */ 
/*    */ import com.google.common.collect.ImmutableMap;
/*    */ import java.util.Map;
/*    */ import net.minecraft.server.level.ServerLevel;
/*    */ import net.minecraft.tags.FluidTags;
/*    */ import net.minecraft.world.entity.LivingEntity;
/*    */ import net.minecraft.world.entity.Mob;
/*    */ import net.minecraft.world.entity.ai.memory.MemoryModuleType;
/*    */ import net.minecraft.world.entity.ai.memory.MemoryStatus;
/*    */ 
/*    */ public class Swim<T extends Mob> extends Behavior<T> {
/*    */   private final float chance;
/*    */   
/*    */   public Swim(float chance) {
/* 13 */     super((Map<MemoryModuleType<?>, MemoryStatus>)ImmutableMap.of());
/* 14 */     this.chance = chance;
/*    */   }
/*    */   
/*    */   public static <T extends Mob> boolean shouldSwim(T mob) {
/* 18 */     return ((mob.isInWater() && mob.getFluidHeight(FluidTags.WATER) > mob.getFluidJumpThreshold()) || mob.isInLava());
/*    */   }
/*    */   
/*    */   protected boolean checkExtraStartConditions(ServerLevel level, Mob body) {
/* 23 */     return shouldSwim(body);
/*    */   }
/*    */   
/*    */   protected boolean canStillUse(ServerLevel level, Mob body, long timestamp) {
/* 28 */     return checkExtraStartConditions(level, body);
/*    */   }
/*    */   
/*    */   protected void tick(ServerLevel level, Mob body, long timestamp) {
/* 33 */     if (body.getRandom().nextFloat() < this.chance)
/* 34 */       body.getJumpControl().jump(); 
/*    */   }
/*    */ }


/* Location:              C:\Users\karol\AppData\Roaming\ModrinthApp\meta\versions\26.2-0.19.5\26.2-0.19.5.jar!\net\minecraft\world\entity\ai\behavior\Swim.class
 * Java compiler version: 25 (69.0)
 * JD-Core Version:       1.1.3
 */