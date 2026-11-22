/*    */ package net.minecraft.world.entity.ai.sensing;
/*    */ 
/*    */ import com.google.common.collect.ImmutableSet;
/*    */ import com.google.common.collect.Iterables;
/*    */ import java.util.Collection;
/*    */ import java.util.List;
/*    */ import java.util.Set;
/*    */ import net.minecraft.server.level.ServerLevel;
/*    */ import net.minecraft.world.entity.EntitySelector;
/*    */ import net.minecraft.world.entity.LivingEntity;
/*    */ import net.minecraft.world.entity.ai.memory.MemoryModuleType;
/*    */ import net.minecraft.world.entity.monster.breeze.Breeze;
/*    */ 
/*    */ public class BreezeAttackEntitySensor extends NearestLivingEntitySensor<Breeze> {
/*    */   public Set<MemoryModuleType<?>> requires() {
/* 18 */     return (Set<MemoryModuleType<?>>)ImmutableSet.copyOf(Iterables.concat(super
/* 19 */           .requires(), 
/* 20 */           List.of(MemoryModuleType.NEAREST_ATTACKABLE)));
/*    */   }
/*    */   
/*    */   protected void doTick(ServerLevel level, Breeze breeze) {
/* 26 */     super.doTick(level, breeze);
/* 28 */     breeze.getBrain().getMemory(MemoryModuleType.NEAREST_LIVING_ENTITIES).stream()
/* 29 */       .flatMap(Collection::stream)
/* 30 */       .filter(EntitySelector.NO_CREATIVE_OR_SPECTATOR)
/* 31 */       .filter(entity -> Sensor.isEntityAttackable(level, (LivingEntity)breeze, entity)).findFirst()
/* 32 */       .ifPresentOrElse(entity -> breeze.getBrain().setMemory(MemoryModuleType.NEAREST_ATTACKABLE, entity), () -> breeze.getBrain().eraseMemory(MemoryModuleType.NEAREST_ATTACKABLE));
/*    */   }
/*    */ }


/* Location:              C:\Users\karol\AppData\Roaming\ModrinthApp\meta\versions\26.2-0.19.5\26.2-0.19.5.jar!\net\minecraft\world\entity\ai\sensing\BreezeAttackEntitySensor.class
 * Java compiler version: 25 (69.0)
 * JD-Core Version:       1.1.3
 */