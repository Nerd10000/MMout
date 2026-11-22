/*    */ package net.minecraft.world.entity.ai.behavior.declarative;
/*    */ 
/*    */ import com.mojang.datafixers.kinds.App;
/*    */ import com.mojang.datafixers.kinds.Const;
/*    */ import com.mojang.datafixers.util.Unit;
/*    */ import java.util.Optional;
/*    */ import net.minecraft.world.entity.ai.Brain;
/*    */ import net.minecraft.world.entity.ai.memory.MemoryModuleType;
/*    */ import net.minecraft.world.entity.ai.memory.MemoryStatus;
/*    */ 
/*    */ public final class Absent<Value> extends Record implements MemoryCondition<Const.Mu<Unit>, Value> {
/*    */   private final MemoryModuleType<Value> memory;
/*    */   
/*    */   public Absent(MemoryModuleType<Value> memory) {
/* 58 */     this.memory = memory;
/*    */   }
/*    */   
/*    */   public final String toString() {
/*    */     // Byte code:
/*    */     //   0: aload_0
/*    */     //   1: <illegal opcode> toString : (Lnet/minecraft/world/entity/ai/behavior/declarative/MemoryCondition$Absent;)Ljava/lang/String;
/*    */     //   6: areturn
/*    */     // Line number table:
/*    */     //   Java source line number -> byte code offset
/*    */     //   #58	-> 0
/*    */     // Local variable table:
/*    */     //   start	length	slot	name	descriptor
/*    */     //   0	7	0	this	Lnet/minecraft/world/entity/ai/behavior/declarative/MemoryCondition$Absent;
/*    */     // Local variable type table:
/*    */     //   start	length	slot	name	signature
/*    */     //   0	7	0	this	Lnet/minecraft/world/entity/ai/behavior/declarative/MemoryCondition$Absent<TValue;>;
/*    */   }
/*    */   
/*    */   public final int hashCode() {
/*    */     // Byte code:
/*    */     //   0: aload_0
/*    */     //   1: <illegal opcode> hashCode : (Lnet/minecraft/world/entity/ai/behavior/declarative/MemoryCondition$Absent;)I
/*    */     //   6: ireturn
/*    */     // Line number table:
/*    */     //   Java source line number -> byte code offset
/*    */     //   #58	-> 0
/*    */     // Local variable table:
/*    */     //   start	length	slot	name	descriptor
/*    */     //   0	7	0	this	Lnet/minecraft/world/entity/ai/behavior/declarative/MemoryCondition$Absent;
/*    */     // Local variable type table:
/*    */     //   start	length	slot	name	signature
/*    */     //   0	7	0	this	Lnet/minecraft/world/entity/ai/behavior/declarative/MemoryCondition$Absent<TValue;>;
/*    */   }
/*    */   
/*    */   public final boolean equals(Object o) {
/*    */     // Byte code:
/*    */     //   0: aload_0
/*    */     //   1: aload_1
/*    */     //   2: <illegal opcode> equals : (Lnet/minecraft/world/entity/ai/behavior/declarative/MemoryCondition$Absent;Ljava/lang/Object;)Z
/*    */     //   7: ireturn
/*    */     // Line number table:
/*    */     //   Java source line number -> byte code offset
/*    */     //   #58	-> 0
/*    */     // Local variable table:
/*    */     //   start	length	slot	name	descriptor
/*    */     //   0	8	0	this	Lnet/minecraft/world/entity/ai/behavior/declarative/MemoryCondition$Absent;
/*    */     //   0	8	1	o	Ljava/lang/Object;
/*    */     // Local variable type table:
/*    */     //   start	length	slot	name	signature
/*    */     //   0	8	0	this	Lnet/minecraft/world/entity/ai/behavior/declarative/MemoryCondition$Absent<TValue;>;
/*    */   }
/*    */   
/*    */   public MemoryModuleType<Value> memory() {
/* 58 */     return this.memory;
/*    */   }
/*    */   
/*    */   public MemoryStatus condition() {
/* 61 */     return MemoryStatus.VALUE_ABSENT;
/*    */   }
/*    */   
/*    */   public MemoryAccessor<Const.Mu<Unit>, Value> createAccessor(Brain<?> brain, Optional<Value> value) {
/* 66 */     if (value.isPresent())
/* 67 */       return null; 
/* 71 */     return new MemoryAccessor<>(brain, this.memory, (App<Const.Mu<Unit>, Value>)Const.create(Unit.INSTANCE));
/*    */   }
/*    */ }


/* Location:              C:\Users\karol\AppData\Roaming\ModrinthApp\meta\versions\26.2-0.19.5\26.2-0.19.5.jar!\net\minecraft\world\entity\ai\behavior\declarative\MemoryCondition$Absent.class
 * Java compiler version: 25 (69.0)
 * JD-Core Version:       1.1.3
 */