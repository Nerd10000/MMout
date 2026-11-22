/*    */ package net.minecraft.world.entity.ai.behavior.declarative;
/*    */ 
/*    */ import com.mojang.datafixers.kinds.App;
/*    */ import com.mojang.datafixers.kinds.K1;
/*    */ import java.util.Optional;
/*    */ import net.minecraft.world.entity.ai.Brain;
/*    */ import net.minecraft.world.entity.ai.memory.MemoryModuleType;
/*    */ 
/*    */ public final class MemoryAccessor<F extends K1, Value> {
/*    */   private final Brain<?> brain;
/*    */   
/*    */   private final MemoryModuleType<Value> memoryType;
/*    */   
/*    */   private final App<F, Value> value;
/*    */   
/*    */   public MemoryAccessor(Brain<?> brain, MemoryModuleType<Value> memoryType, App<F, Value> value) {
/* 18 */     this.brain = brain;
/* 19 */     this.memoryType = memoryType;
/* 20 */     this.value = value;
/*    */   }
/*    */   
/*    */   public App<F, Value> value() {
/* 24 */     return this.value;
/*    */   }
/*    */   
/*    */   public void set(Value value) {
/* 28 */     this.brain.setMemory(this.memoryType, Optional.of(value));
/*    */   }
/*    */   
/*    */   public void setOrErase(Optional<Value> value) {
/* 32 */     this.brain.setMemory(this.memoryType, value);
/*    */   }
/*    */   
/*    */   public void setWithExpiry(Value value, long timeToLive) {
/* 36 */     this.brain.setMemoryWithExpiry(this.memoryType, value, timeToLive);
/*    */   }
/*    */   
/*    */   public void erase() {
/* 40 */     this.brain.eraseMemory(this.memoryType);
/*    */   }
/*    */ }


/* Location:              C:\Users\karol\AppData\Roaming\ModrinthApp\meta\versions\26.2-0.19.5\26.2-0.19.5.jar!\net\minecraft\world\entity\ai\behavior\declarative\MemoryAccessor.class
 * Java compiler version: 25 (69.0)
 * JD-Core Version:       1.1.3
 */