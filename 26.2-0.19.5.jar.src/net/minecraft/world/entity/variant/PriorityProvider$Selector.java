/*    */ package net.minecraft.world.entity.variant;
/*    */ 
/*    */ import com.mojang.datafixers.kinds.App;
/*    */ import com.mojang.datafixers.kinds.Applicative;
/*    */ import com.mojang.serialization.Codec;
/*    */ import com.mojang.serialization.codecs.RecordCodecBuilder;
/*    */ import java.util.Optional;
/*    */ import java.util.function.BiFunction;
/*    */ 
/*    */ public final class Selector<Context, Condition extends PriorityProvider.SelectorCondition<Context>> extends Record {
/*    */   private final Optional<Condition> condition;
/*    */   
/*    */   private final int priority;
/*    */   
/*    */   public Selector(Optional<Condition> condition, int priority) {
/* 21 */     this.condition = condition;
/* 21 */     this.priority = priority;
/*    */   }
/*    */   
/*    */   public final String toString() {
/*    */     // Byte code:
/*    */     //   0: aload_0
/*    */     //   1: <illegal opcode> toString : (Lnet/minecraft/world/entity/variant/PriorityProvider$Selector;)Ljava/lang/String;
/*    */     //   6: areturn
/*    */     // Line number table:
/*    */     //   Java source line number -> byte code offset
/*    */     //   #21	-> 0
/*    */     // Local variable table:
/*    */     //   start	length	slot	name	descriptor
/*    */     //   0	7	0	this	Lnet/minecraft/world/entity/variant/PriorityProvider$Selector;
/*    */     // Local variable type table:
/*    */     //   start	length	slot	name	signature
/*    */     //   0	7	0	this	Lnet/minecraft/world/entity/variant/PriorityProvider$Selector<TContext;TCondition;>;
/*    */   }
/*    */   
/*    */   public final int hashCode() {
/*    */     // Byte code:
/*    */     //   0: aload_0
/*    */     //   1: <illegal opcode> hashCode : (Lnet/minecraft/world/entity/variant/PriorityProvider$Selector;)I
/*    */     //   6: ireturn
/*    */     // Line number table:
/*    */     //   Java source line number -> byte code offset
/*    */     //   #21	-> 0
/*    */     // Local variable table:
/*    */     //   start	length	slot	name	descriptor
/*    */     //   0	7	0	this	Lnet/minecraft/world/entity/variant/PriorityProvider$Selector;
/*    */     // Local variable type table:
/*    */     //   start	length	slot	name	signature
/*    */     //   0	7	0	this	Lnet/minecraft/world/entity/variant/PriorityProvider$Selector<TContext;TCondition;>;
/*    */   }
/*    */   
/*    */   public final boolean equals(Object o) {
/*    */     // Byte code:
/*    */     //   0: aload_0
/*    */     //   1: aload_1
/*    */     //   2: <illegal opcode> equals : (Lnet/minecraft/world/entity/variant/PriorityProvider$Selector;Ljava/lang/Object;)Z
/*    */     //   7: ireturn
/*    */     // Line number table:
/*    */     //   Java source line number -> byte code offset
/*    */     //   #21	-> 0
/*    */     // Local variable table:
/*    */     //   start	length	slot	name	descriptor
/*    */     //   0	8	0	this	Lnet/minecraft/world/entity/variant/PriorityProvider$Selector;
/*    */     //   0	8	1	o	Ljava/lang/Object;
/*    */     // Local variable type table:
/*    */     //   start	length	slot	name	signature
/*    */     //   0	8	0	this	Lnet/minecraft/world/entity/variant/PriorityProvider$Selector<TContext;TCondition;>;
/*    */   }
/*    */   
/*    */   public Optional<Condition> condition() {
/* 21 */     return this.condition;
/*    */   }
/*    */   
/*    */   public int priority() {
/* 21 */     return this.priority;
/*    */   }
/*    */   
/*    */   public Selector(Condition condition, int priority) {
/* 26 */     this(Optional.of(condition), priority);
/*    */   }
/*    */   
/*    */   public Selector(int priority) {
/* 30 */     this(Optional.empty(), priority);
/*    */   }
/*    */   
/*    */   public static <Context, Condition extends PriorityProvider.SelectorCondition<Context>> Codec<Selector<Context, Condition>> codec(Codec<Condition> conditionCodec) {
/* 34 */     return RecordCodecBuilder.create(i -> i.group((App)conditionCodec.optionalFieldOf("condition").forGetter(Selector::condition), (App)Codec.INT.fieldOf("priority").forGetter(Selector::priority)).apply((Applicative)i, Selector::new));
/*    */   }
/*    */ }


/* Location:              C:\Users\karol\AppData\Roaming\ModrinthApp\meta\versions\26.2-0.19.5\26.2-0.19.5.jar!\net\minecraft\world\entity\variant\PriorityProvider$Selector.class
 * Java compiler version: 25 (69.0)
 * JD-Core Version:       1.1.3
 */