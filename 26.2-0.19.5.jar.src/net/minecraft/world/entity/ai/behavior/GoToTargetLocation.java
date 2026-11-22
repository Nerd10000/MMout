/*    */ package net.minecraft.world.entity.ai.behavior;
/*    */ 
/*    */ import com.mojang.datafixers.kinds.App;
/*    */ import com.mojang.datafixers.kinds.Applicative;
/*    */ import net.minecraft.core.BlockPos;
/*    */ import net.minecraft.core.Vec3i;
/*    */ import net.minecraft.server.level.ServerLevel;
/*    */ import net.minecraft.util.RandomSource;
/*    */ import net.minecraft.world.entity.LivingEntity;
/*    */ import net.minecraft.world.entity.Mob;
/*    */ import net.minecraft.world.entity.ai.behavior.declarative.BehaviorBuilder;
/*    */ import net.minecraft.world.entity.ai.behavior.declarative.MemoryAccessor;
/*    */ import net.minecraft.world.entity.ai.behavior.declarative.Trigger;
/*    */ import net.minecraft.world.entity.ai.memory.MemoryModuleType;
/*    */ 
/*    */ public class GoToTargetLocation {
/*    */   private static BlockPos getNearbyPos(Mob body, BlockPos pos) {
/* 11 */     RandomSource random = body.level().getRandom();
/* 12 */     return pos.offset(getRandomOffset(random), 0, getRandomOffset(random));
/*    */   }
/*    */   
/*    */   private static int getRandomOffset(RandomSource random) {
/* 16 */     return random.nextInt(3) - 1;
/*    */   }
/*    */   
/*    */   public static <E extends Mob> OneShot<E> create(MemoryModuleType<BlockPos> locationMemory, int closeEnoughDist, float speedModifier) {
/* 20 */     return BehaviorBuilder.create(i -> i.group((App)i.present(locationMemory), (App)i.absent(MemoryModuleType.ATTACK_TARGET), (App)i.absent(MemoryModuleType.WALK_TARGET), (App)i.registered(MemoryModuleType.LOOK_TARGET)).apply((Applicative)i, ()));
/*    */   }
/*    */ }


/* Location:              C:\Users\karol\AppData\Roaming\ModrinthApp\meta\versions\26.2-0.19.5\26.2-0.19.5.jar!\net\minecraft\world\entity\ai\behavior\GoToTargetLocation.class
 * Java compiler version: 25 (69.0)
 * JD-Core Version:       1.1.3
 */