/*    */ package net.minecraft.advancements.triggers;
/*    */ 
/*    */ import java.util.ArrayList;
/*    */ import java.util.List;
/*    */ import java.util.Map;
/*    */ import java.util.Optional;
/*    */ import java.util.function.Predicate;
/*    */ import net.minecraft.advancements.CriterionTriggerInstance;
/*    */ import net.minecraft.advancements.predicates.ContextAwarePredicate;
/*    */ import net.minecraft.advancements.predicates.entity.EntityPredicate;
/*    */ import net.minecraft.server.PlayerAdvancements;
/*    */ import net.minecraft.server.level.ServerPlayer;
/*    */ import net.minecraft.world.entity.Entity;
/*    */ import net.minecraft.world.level.storage.loot.LootContext;
/*    */ import net.minecraft.world.level.storage.loot.Validatable;
/*    */ import net.minecraft.world.level.storage.loot.ValidationContextSource;
/*    */ 
/*    */ public abstract class SimpleCriterionTrigger<T extends SimpleCriterionTrigger.SimpleInstance> implements CriterionTrigger<T> {
/*    */   protected void trigger(ServerPlayer player, Predicate<T> matcher) {
/* 20 */     PlayerAdvancements advancements = player.getAdvancements();
/* 21 */     Map<PlayerAdvancements.TriggerInstanceKey, T> listenersForType = advancements.getTriggerMapForType(this);
/* 23 */     if (listenersForType == null || listenersForType.isEmpty())
/*    */       return; 
/* 27 */     LootContext playerContext = EntityPredicate.createContext(player, (Entity)player);
/* 30 */     List<PlayerAdvancements.TriggerInstanceKey> matchedConditions = null;
/* 32 */     for (Map.Entry<PlayerAdvancements.TriggerInstanceKey, T> entry : listenersForType.entrySet()) {
/* 33 */       SimpleInstance simpleInstance = (SimpleInstance)entry.getValue();
/* 36 */       if (!matcher.test((T)simpleInstance))
/*    */         continue; 
/* 39 */       Optional<ContextAwarePredicate> predicate = simpleInstance.player();
/* 40 */       if (predicate.isPresent() && !((ContextAwarePredicate)predicate.get()).matches(playerContext))
/*    */         continue; 
/* 44 */       if (matchedConditions == null)
/* 45 */         matchedConditions = new ArrayList<>(); 
/* 47 */       matchedConditions.add(entry.getKey());
/*    */     } 
/* 50 */     if (matchedConditions != null)
/* 51 */       for (PlayerAdvancements.TriggerInstanceKey criterion : matchedConditions)
/* 52 */         advancements.award(criterion.advancement(), criterion.criterion());  
/*    */   }
/*    */   
/*    */   public static interface SimpleInstance extends CriterionTriggerInstance {
/*    */     default void validate(ValidationContextSource validator) {
/* 60 */       Validatable.validate(validator.entityContext(), "player", player());
/*    */     }
/*    */     
/*    */     Optional<ContextAwarePredicate> player();
/*    */   }
/*    */ }


/* Location:              C:\Users\karol\AppData\Roaming\ModrinthApp\meta\versions\26.2-0.19.5\26.2-0.19.5.jar!\net\minecraft\advancements\triggers\SimpleCriterionTrigger.class
 * Java compiler version: 25 (69.0)
 * JD-Core Version:       1.1.3
 */