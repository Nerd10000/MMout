/*    */ package net.minecraft.advancements.triggers;
/*    */ 
/*    */ import com.mojang.serialization.Codec;
/*    */ import com.mojang.serialization.MapCodec;
/*    */ import net.minecraft.advancements.CriterionTriggerInstance;
/*    */ import net.minecraft.util.ExtraCodecs;
/*    */ 
/*    */ public final class Criterion<T extends CriterionTriggerInstance> extends Record {
/*    */   private final CriterionTrigger<T> trigger;
/*    */   
/*    */   private final T triggerInstance;
/*    */   
/*    */   public Criterion(CriterionTrigger<T> trigger, T triggerInstance) {
/*  8 */     this.trigger = trigger;
/*  8 */     this.triggerInstance = triggerInstance;
/*    */   }
/*    */   
/*    */   public final String toString() {
/*    */     // Byte code:
/*    */     //   0: aload_0
/*    */     //   1: <illegal opcode> toString : (Lnet/minecraft/advancements/triggers/Criterion;)Ljava/lang/String;
/*    */     //   6: areturn
/*    */     // Line number table:
/*    */     //   Java source line number -> byte code offset
/*    */     //   #8	-> 0
/*    */     // Local variable table:
/*    */     //   start	length	slot	name	descriptor
/*    */     //   0	7	0	this	Lnet/minecraft/advancements/triggers/Criterion;
/*    */     // Local variable type table:
/*    */     //   start	length	slot	name	signature
/*    */     //   0	7	0	this	Lnet/minecraft/advancements/triggers/Criterion<TT;>;
/*    */   }
/*    */   
/*    */   public final int hashCode() {
/*    */     // Byte code:
/*    */     //   0: aload_0
/*    */     //   1: <illegal opcode> hashCode : (Lnet/minecraft/advancements/triggers/Criterion;)I
/*    */     //   6: ireturn
/*    */     // Line number table:
/*    */     //   Java source line number -> byte code offset
/*    */     //   #8	-> 0
/*    */     // Local variable table:
/*    */     //   start	length	slot	name	descriptor
/*    */     //   0	7	0	this	Lnet/minecraft/advancements/triggers/Criterion;
/*    */     // Local variable type table:
/*    */     //   start	length	slot	name	signature
/*    */     //   0	7	0	this	Lnet/minecraft/advancements/triggers/Criterion<TT;>;
/*    */   }
/*    */   
/*    */   public final boolean equals(Object o) {
/*    */     // Byte code:
/*    */     //   0: aload_0
/*    */     //   1: aload_1
/*    */     //   2: <illegal opcode> equals : (Lnet/minecraft/advancements/triggers/Criterion;Ljava/lang/Object;)Z
/*    */     //   7: ireturn
/*    */     // Line number table:
/*    */     //   Java source line number -> byte code offset
/*    */     //   #8	-> 0
/*    */     // Local variable table:
/*    */     //   start	length	slot	name	descriptor
/*    */     //   0	8	0	this	Lnet/minecraft/advancements/triggers/Criterion;
/*    */     //   0	8	1	o	Ljava/lang/Object;
/*    */     // Local variable type table:
/*    */     //   start	length	slot	name	signature
/*    */     //   0	8	0	this	Lnet/minecraft/advancements/triggers/Criterion<TT;>;
/*    */   }
/*    */   
/*    */   public CriterionTrigger<T> trigger() {
/*  8 */     return this.trigger;
/*    */   }
/*    */   
/*    */   public T triggerInstance() {
/*  8 */     return this.triggerInstance;
/*    */   }
/*    */   
/*  9 */   private static final MapCodec<Criterion<?>> MAP_CODEC = ExtraCodecs.dispatchOptionalValue("trigger", "conditions", CriteriaTriggers.CODEC, Criterion::trigger, Criterion::criterionCodec);
/*    */   
/* 10 */   public static final Codec<Criterion<?>> CODEC = MAP_CODEC.codec();
/*    */   
/*    */   private static <T extends CriterionTriggerInstance> Codec<Criterion<T>> criterionCodec(CriterionTrigger<T> trigger) {
/* 13 */     return trigger.codec().xmap(instance -> new Criterion<>(trigger, instance), Criterion::triggerInstance);
/*    */   }
/*    */ }


/* Location:              C:\Users\karol\AppData\Roaming\ModrinthApp\meta\versions\26.2-0.19.5\26.2-0.19.5.jar!\net\minecraft\advancements\triggers\Criterion.class
 * Java compiler version: 25 (69.0)
 * JD-Core Version:       1.1.3
 */