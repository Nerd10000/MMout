/*     */ package net.minecraft.util.debug;
/*     */ 
/*     */ import java.util.List;
/*     */ import java.util.Optional;
/*     */ import java.util.OptionalLong;
/*     */ import net.minecraft.server.level.ServerLevel;
/*     */ import net.minecraft.util.StringUtil;
/*     */ import net.minecraft.world.entity.ai.Brain;
/*     */ import net.minecraft.world.entity.ai.memory.MemoryModuleType;
/*     */ 
/*     */ class null implements Brain.Visitor {
/*     */   public <U> void acceptEmpty(MemoryModuleType<U> type) {
/* 178 */     collectResult(type, Optional.empty(), OptionalLong.empty());
/*     */   }
/*     */   
/*     */   public <U> void accept(MemoryModuleType<U> type, U value) {
/* 183 */     collectResult(type, Optional.of(value), OptionalLong.empty());
/*     */   }
/*     */   
/*     */   public <U> void accept(MemoryModuleType<U> type, U value, long timeToLive) {
/* 188 */     collectResult(type, Optional.of(value), OptionalLong.of(timestamp));
/*     */   }
/*     */   
/*     */   private void collectResult(MemoryModuleType<?> memoryType, Optional<?> value, OptionalLong ttl) {
/* 192 */     String description = DebugBrainDump.getMemoryDescription(level, timestamp, memoryType, value, ttl);
/* 193 */     result.add(StringUtil.truncateStringIfNecessary(description, 255, true));
/*     */   }
/*     */ }


/* Location:              C:\Users\karol\AppData\Roaming\ModrinthApp\meta\versions\26.2-0.19.5\26.2-0.19.5.jar!\net\minecraf\\util\debug\DebugBrainDump$1.class
 * Java compiler version: 25 (69.0)
 * JD-Core Version:       1.1.3
 */