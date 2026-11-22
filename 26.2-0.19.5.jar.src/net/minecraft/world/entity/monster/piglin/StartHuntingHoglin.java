/*    */ package net.minecraft.world.entity.monster.piglin;
/*    */ 
/*    */ import com.mojang.datafixers.kinds.App;
/*    */ import com.mojang.datafixers.kinds.Applicative;
/*    */ import java.util.List;
/*    */ import net.minecraft.server.level.ServerLevel;
/*    */ import net.minecraft.world.entity.LivingEntity;
/*    */ import net.minecraft.world.entity.ai.behavior.OneShot;
/*    */ import net.minecraft.world.entity.ai.behavior.declarative.BehaviorBuilder;
/*    */ import net.minecraft.world.entity.ai.behavior.declarative.MemoryAccessor;
/*    */ import net.minecraft.world.entity.ai.behavior.declarative.Trigger;
/*    */ import net.minecraft.world.entity.ai.memory.MemoryModuleType;
/*    */ import net.minecraft.world.entity.monster.hoglin.Hoglin;
/*    */ 
/*    */ public class StartHuntingHoglin {
/*    */   public static OneShot<Piglin> create() {
/* 10 */     return BehaviorBuilder.create(i -> i.group((App)i.present(MemoryModuleType.NEAREST_VISIBLE_HUNTABLE_HOGLIN), (App)i.absent(MemoryModuleType.ANGRY_AT), (App)i.absent(MemoryModuleType.HUNTED_RECENTLY), (App)i.registered(MemoryModuleType.NEAREST_VISIBLE_ADULT_PIGLINS)).apply((Applicative)i, ()));
/*    */   }
/*    */   
/*    */   private static boolean hasHuntedRecently(AbstractPiglin otherPiglin) {
/* 33 */     return otherPiglin.getBrain().hasMemoryValue(MemoryModuleType.HUNTED_RECENTLY);
/*    */   }
/*    */ }


/* Location:              C:\Users\karol\AppData\Roaming\ModrinthApp\meta\versions\26.2-0.19.5\26.2-0.19.5.jar!\net\minecraft\world\entity\monster\piglin\StartHuntingHoglin.class
 * Java compiler version: 25 (69.0)
 * JD-Core Version:       1.1.3
 */