/*   */ package net.minecraft.world.entity.ai.behavior;
/*   */ 
/*   */ import com.mojang.datafixers.kinds.App;
/*   */ import net.minecraft.server.level.ServerLevel;
/*   */ import net.minecraft.world.entity.LivingEntity;
/*   */ import net.minecraft.world.entity.ai.behavior.declarative.BehaviorBuilder;
/*   */ 
/*   */ public class UpdateActivityFromSchedule {
/*   */   public static BehaviorControl<LivingEntity> create() {
/* 8 */     return BehaviorBuilder.create(i -> i.point(()));
/*   */   }
/*   */ }


/* Location:              C:\Users\karol\AppData\Roaming\ModrinthApp\meta\versions\26.2-0.19.5\26.2-0.19.5.jar!\net\minecraft\world\entity\ai\behavior\UpdateActivityFromSchedule.class
 * Java compiler version: 25 (69.0)
 * JD-Core Version:       1.1.3
 */