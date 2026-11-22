/*    */ package net.minecraft.advancements.triggers;
/*    */ 
/*    */ import com.mojang.serialization.Codec;
/*    */ 
/*    */ public interface CriterionTrigger<T extends net.minecraft.advancements.CriterionTriggerInstance> {
/*    */   Codec<T> codec();
/*    */   
/*    */   default Criterion<T> createCriterion(T instance) {
/* 10 */     return new Criterion<>(this, instance);
/*    */   }
/*    */ }


/* Location:              C:\Users\karol\AppData\Roaming\ModrinthApp\meta\versions\26.2-0.19.5\26.2-0.19.5.jar!\net\minecraft\advancements\triggers\CriterionTrigger.class
 * Java compiler version: 25 (69.0)
 * JD-Core Version:       1.1.3
 */