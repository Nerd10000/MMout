/*    */ package net.minecraft.world.entity.ai.behavior;
/*    */ 
/*    */ import com.mojang.datafixers.kinds.App;
/*    */ import com.mojang.datafixers.kinds.Applicative;
/*    */ import net.minecraft.core.HolderGetter;
/*    */ import net.minecraft.server.level.ServerLevel;
/*    */ import net.minecraft.world.entity.ai.behavior.declarative.BehaviorBuilder;
/*    */ import net.minecraft.world.entity.ai.behavior.declarative.MemoryAccessor;
/*    */ import net.minecraft.world.entity.ai.behavior.declarative.Trigger;
/*    */ import net.minecraft.world.entity.ai.memory.MemoryModuleType;
/*    */ import net.minecraft.world.entity.npc.villager.Villager;
/*    */ import net.minecraft.world.entity.npc.villager.VillagerData;
/*    */ import net.minecraft.world.entity.npc.villager.VillagerProfession;
/*    */ 
/*    */ public class ResetProfession {
/*    */   public static BehaviorControl<Villager> create() {
/* 13 */     return BehaviorBuilder.create(i -> i.group((App)i.absent(MemoryModuleType.JOB_SITE)).apply((Applicative)i, ()));
/*    */   }
/*    */ }


/* Location:              C:\Users\karol\AppData\Roaming\ModrinthApp\meta\versions\26.2-0.19.5\26.2-0.19.5.jar!\net\minecraft\world\entity\ai\behavior\ResetProfession.class
 * Java compiler version: 25 (69.0)
 * JD-Core Version:       1.1.3
 */