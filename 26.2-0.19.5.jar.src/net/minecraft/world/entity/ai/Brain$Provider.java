/*    */ package net.minecraft.world.entity.ai;
/*    */ 
/*    */ import java.util.Collection;
/*    */ import java.util.List;
/*    */ import net.minecraft.world.entity.LivingEntity;
/*    */ import net.minecraft.world.entity.ai.memory.MemoryModuleType;
/*    */ import net.minecraft.world.entity.ai.sensing.Sensor;
/*    */ import net.minecraft.world.entity.ai.sensing.SensorType;
/*    */ 
/*    */ public final class Provider<E extends LivingEntity> {
/*    */   private final Collection<? extends MemoryModuleType<?>> memoryTypes;
/*    */   
/*    */   private final Collection<? extends SensorType<? extends Sensor<? super E>>> sensorTypes;
/*    */   
/*    */   private final Brain.ActivitySupplier<E> activities;
/*    */   
/*    */   private Provider(Collection<? extends MemoryModuleType<?>> memoryTypes, Collection<? extends SensorType<? extends Sensor<? super E>>> sensorTypes, Brain.ActivitySupplier<E> activities) {
/* 54 */     this.memoryTypes = memoryTypes;
/* 55 */     this.sensorTypes = sensorTypes;
/* 56 */     this.activities = activities;
/*    */   }
/*    */   
/*    */   public Brain<E> makeBrain(E body, Brain.Packed packed) {
/* 60 */     List<ActivityData<E>> activities = this.activities.createActivities(body);
/* 61 */     return new Brain<>(this.memoryTypes, this.sensorTypes, activities, packed.memories, body.getRandom());
/*    */   }
/*    */ }


/* Location:              C:\Users\karol\AppData\Roaming\ModrinthApp\meta\versions\26.2-0.19.5\26.2-0.19.5.jar!\net\minecraft\world\entity\ai\Brain$Provider.class
 * Java compiler version: 25 (69.0)
 * JD-Core Version:       1.1.3
 */