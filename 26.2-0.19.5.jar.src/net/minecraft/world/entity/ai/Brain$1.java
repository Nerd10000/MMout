/*     */ package net.minecraft.world.entity.ai;
/*     */ 
/*     */ import net.minecraft.world.entity.ai.memory.ExpirableValue;
/*     */ import net.minecraft.world.entity.ai.memory.MemoryMap;
/*     */ import net.minecraft.world.entity.ai.memory.MemoryModuleType;
/*     */ 
/*     */ class null implements Brain.Visitor {
/*     */   null(Brain this$0) {}
/*     */   
/*     */   public <U> void acceptEmpty(MemoryModuleType<U> type) {}
/*     */   
/*     */   public <U> void accept(MemoryModuleType<U> type, U value, long timeToLive) {
/* 152 */     if (type.canSerialize())
/* 153 */       builder.add(type, ExpirableValue.of(value, timeToLive)); 
/*     */   }
/*     */   
/*     */   public <U> void accept(MemoryModuleType<U> type, U value) {
/* 159 */     if (type.canSerialize())
/* 160 */       builder.add(type, ExpirableValue.of(value)); 
/*     */   }
/*     */ }


/* Location:              C:\Users\karol\AppData\Roaming\ModrinthApp\meta\versions\26.2-0.19.5\26.2-0.19.5.jar!\net\minecraft\world\entity\ai\Brain$1.class
 * Java compiler version: 25 (69.0)
 * JD-Core Version:       1.1.3
 */