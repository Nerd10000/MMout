/*    */ package net.minecraft.world.entity.ai.memory;
/*    */ 
/*    */ import com.google.common.collect.ImmutableMap;
/*    */ import java.util.Map;
/*    */ 
/*    */ public class Builder {
/* 73 */   private final ImmutableMap.Builder<MemoryModuleType<?>, ExpirableValue<?>> builder = ImmutableMap.builder();
/*    */   
/*    */   public <U> Builder add(MemoryModuleType<U> type, ExpirableValue<U> value) {
/* 76 */     this.builder.put(type, value);
/* 77 */     return this;
/*    */   }
/*    */   
/*    */   public MemoryMap build() {
/* 81 */     return new MemoryMap((Map<MemoryModuleType<?>, ExpirableValue<?>>)this.builder.buildOrThrow());
/*    */   }
/*    */ }


/* Location:              C:\Users\karol\AppData\Roaming\ModrinthApp\meta\versions\26.2-0.19.5\26.2-0.19.5.jar!\net\minecraft\world\entity\ai\memory\MemoryMap$Builder.class
 * Java compiler version: 25 (69.0)
 * JD-Core Version:       1.1.3
 */