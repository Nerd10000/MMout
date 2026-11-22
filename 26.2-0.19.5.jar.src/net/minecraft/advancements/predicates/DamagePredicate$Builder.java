/*    */ package net.minecraft.advancements.predicates;
/*    */ 
/*    */ import java.util.Optional;
/*    */ import net.minecraft.advancements.predicates.entity.EntityPredicate;
/*    */ 
/*    */ public class Builder {
/* 46 */   private MinMaxBounds.Doubles dealtDamage = MinMaxBounds.Doubles.ANY;
/*    */   
/* 47 */   private MinMaxBounds.Doubles takenDamage = MinMaxBounds.Doubles.ANY;
/*    */   
/* 48 */   private Optional<EntityPredicate> sourceEntity = Optional.empty();
/*    */   
/* 49 */   private Optional<Boolean> blocked = Optional.empty();
/*    */   
/* 50 */   private Optional<DamageSourcePredicate> type = Optional.empty();
/*    */   
/*    */   public static Builder damageInstance() {
/* 53 */     return new Builder();
/*    */   }
/*    */   
/*    */   public Builder dealtDamage(MinMaxBounds.Doubles dealtDamage) {
/* 57 */     this.dealtDamage = dealtDamage;
/* 58 */     return this;
/*    */   }
/*    */   
/*    */   public Builder takenDamage(MinMaxBounds.Doubles takenDamage) {
/* 62 */     this.takenDamage = takenDamage;
/* 63 */     return this;
/*    */   }
/*    */   
/*    */   public Builder sourceEntity(EntityPredicate sourceEntity) {
/* 67 */     this.sourceEntity = Optional.of(sourceEntity);
/* 68 */     return this;
/*    */   }
/*    */   
/*    */   public Builder blocked(Boolean blocked) {
/* 72 */     this.blocked = Optional.of(blocked);
/* 73 */     return this;
/*    */   }
/*    */   
/*    */   public Builder type(DamageSourcePredicate type) {
/* 77 */     this.type = Optional.of(type);
/* 78 */     return this;
/*    */   }
/*    */   
/*    */   public Builder type(DamageSourcePredicate.Builder type) {
/* 82 */     this.type = Optional.of(type.build());
/* 83 */     return this;
/*    */   }
/*    */   
/*    */   public DamagePredicate build() {
/* 87 */     return new DamagePredicate(this.dealtDamage, this.takenDamage, this.sourceEntity, this.blocked, this.type);
/*    */   }
/*    */ }


/* Location:              C:\Users\karol\AppData\Roaming\ModrinthApp\meta\versions\26.2-0.19.5\26.2-0.19.5.jar!\net\minecraft\advancements\predicates\DamagePredicate$Builder.class
 * Java compiler version: 25 (69.0)
 * JD-Core Version:       1.1.3
 */