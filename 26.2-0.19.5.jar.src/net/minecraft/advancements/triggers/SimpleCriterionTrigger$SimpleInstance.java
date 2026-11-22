/*    */ package net.minecraft.advancements.triggers;
/*    */ 
/*    */ import java.util.Optional;
/*    */ import net.minecraft.advancements.CriterionTriggerInstance;
/*    */ import net.minecraft.advancements.predicates.ContextAwarePredicate;
/*    */ import net.minecraft.world.level.storage.loot.Validatable;
/*    */ import net.minecraft.world.level.storage.loot.ValidationContextSource;
/*    */ 
/*    */ public interface SimpleInstance extends CriterionTriggerInstance {
/*    */   default void validate(ValidationContextSource validator) {
/* 60 */     Validatable.validate(validator.entityContext(), "player", player());
/*    */   }
/*    */   
/*    */   Optional<ContextAwarePredicate> player();
/*    */ }


/* Location:              C:\Users\karol\AppData\Roaming\ModrinthApp\meta\versions\26.2-0.19.5\26.2-0.19.5.jar!\net\minecraft\advancements\triggers\SimpleCriterionTrigger$SimpleInstance.class
 * Java compiler version: 25 (69.0)
 * JD-Core Version:       1.1.3
 */