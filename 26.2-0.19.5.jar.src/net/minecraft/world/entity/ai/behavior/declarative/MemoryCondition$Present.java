/*    */ package net.minecraft.world.entity.ai.behavior.declarative;
/*    */ 
/*    */ import com.mojang.datafixers.kinds.App;
/*    */ import com.mojang.datafixers.kinds.IdF;
/*    */ import java.util.Optional;
/*    */ import net.minecraft.world.entity.ai.Brain;
/*    */ import net.minecraft.world.entity.ai.memory.MemoryModuleType;
/*    */ import net.minecraft.world.entity.ai.memory.MemoryStatus;
/*    */ 
/*    */ public final class Present<Value> extends Record implements MemoryCondition<IdF.Mu, Value> {
/*    */   private final MemoryModuleType<Value> memory;
/*    */   
/*    */   public Present(MemoryModuleType<Value> memory) {
/* 41 */     this.memory = memory;
/*    */   }
/*    */   
/*    */   public final String toString() {
/*    */     // Byte code:
/*    */     //   0: aload_0
/*    */     //   1: <illegal opcode> toString : (Lnet/minecraft/world/entity/ai/behavior/declarative/MemoryCondition$Present;)Ljava/lang/String;
/*    */     //   6: areturn
/*    */     // Line number table:
/*    */     //   Java source line number -> byte code offset
/*    */     //   #41	-> 0
/*    */     // Local variable table:
/*    */     //   start	length	slot	name	descriptor
/*    */     //   0	7	0	this	Lnet/minecraft/world/entity/ai/behavior/declarative/MemoryCondition$Present;
/*    */     // Local variable type table:
/*    */     //   start	length	slot	name	signature
/*    */     //   0	7	0	this	Lnet/minecraft/world/entity/ai/behavior/declarative/MemoryCondition$Present<TValue;>;
/*    */   }
/*    */   
/*    */   public final int hashCode() {
/*    */     // Byte code:
/*    */     //   0: aload_0
/*    */     //   1: <illegal opcode> hashCode : (Lnet/minecraft/world/entity/ai/behavior/declarative/MemoryCondition$Present;)I
/*    */     //   6: ireturn
/*    */     // Line number table:
/*    */     //   Java source line number -> byte code offset
/*    */     //   #41	-> 0
/*    */     // Local variable table:
/*    */     //   start	length	slot	name	descriptor
/*    */     //   0	7	0	this	Lnet/minecraft/world/entity/ai/behavior/declarative/MemoryCondition$Present;
/*    */     // Local variable type table:
/*    */     //   start	length	slot	name	signature
/*    */     //   0	7	0	this	Lnet/minecraft/world/entity/ai/behavior/declarative/MemoryCondition$Present<TValue;>;
/*    */   }
/*    */   
/*    */   public final boolean equals(Object o) {
/*    */     // Byte code:
/*    */     //   0: aload_0
/*    */     //   1: aload_1
/*    */     //   2: <illegal opcode> equals : (Lnet/minecraft/world/entity/ai/behavior/declarative/MemoryCondition$Present;Ljava/lang/Object;)Z
/*    */     //   7: ireturn
/*    */     // Line number table:
/*    */     //   Java source line number -> byte code offset
/*    */     //   #41	-> 0
/*    */     // Local variable table:
/*    */     //   start	length	slot	name	descriptor
/*    */     //   0	8	0	this	Lnet/minecraft/world/entity/ai/behavior/declarative/MemoryCondition$Present;
/*    */     //   0	8	1	o	Ljava/lang/Object;
/*    */     // Local variable type table:
/*    */     //   start	length	slot	name	signature
/*    */     //   0	8	0	this	Lnet/minecraft/world/entity/ai/behavior/declarative/MemoryCondition$Present<TValue;>;
/*    */   }
/*    */   
/*    */   public MemoryModuleType<Value> memory() {
/* 41 */     return this.memory;
/*    */   }
/*    */   
/*    */   public MemoryStatus condition() {
/* 44 */     return MemoryStatus.VALUE_PRESENT;
/*    */   }
/*    */   
/*    */   public MemoryAccessor<IdF.Mu, Value> createAccessor(Brain<?> brain, Optional<Value> value) {
/* 49 */     if (value.isEmpty())
/* 50 */       return null; 
/* 54 */     return new MemoryAccessor<>(brain, this.memory, (App<IdF.Mu, Value>)IdF.create(value.get()));
/*    */   }
/*    */ }


/* Location:              C:\Users\karol\AppData\Roaming\ModrinthApp\meta\versions\26.2-0.19.5\26.2-0.19.5.jar!\net\minecraft\world\entity\ai\behavior\declarative\MemoryCondition$Present.class
 * Java compiler version: 25 (69.0)
 * JD-Core Version:       1.1.3
 */