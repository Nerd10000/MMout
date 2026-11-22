/*    */ package net.minecraft.advancements.predicates;
/*    */ 
/*    */ import com.google.common.collect.ImmutableList;
/*    */ import java.util.List;
/*    */ import java.util.Optional;
/*    */ import net.minecraft.advancements.predicates.entity.EntityPredicate;
/*    */ import net.minecraft.world.damagesource.DamageType;
/*    */ 
/*    */ public class Builder {
/* 54 */   private final ImmutableList.Builder<TagPredicate<DamageType>> tags = ImmutableList.builder();
/*    */   
/* 55 */   private Optional<EntityPredicate> directEntity = Optional.empty();
/*    */   
/* 56 */   private Optional<EntityPredicate> sourceEntity = Optional.empty();
/*    */   
/* 57 */   private Optional<Boolean> isDirect = Optional.empty();
/*    */   
/*    */   public static Builder damageType() {
/* 60 */     return new Builder();
/*    */   }
/*    */   
/*    */   public Builder tag(TagPredicate<DamageType> tag) {
/* 64 */     this.tags.add(tag);
/* 65 */     return this;
/*    */   }
/*    */   
/*    */   public Builder direct(EntityPredicate.Builder directEntity) {
/* 69 */     this.directEntity = Optional.of(directEntity.build());
/* 70 */     return this;
/*    */   }
/*    */   
/*    */   public Builder source(EntityPredicate.Builder sourceEntity) {
/* 74 */     this.sourceEntity = Optional.of(sourceEntity.build());
/* 75 */     return this;
/*    */   }
/*    */   
/*    */   public Builder isDirect(boolean direct) {
/* 79 */     this.isDirect = Optional.of(Boolean.valueOf(direct));
/* 80 */     return this;
/*    */   }
/*    */   
/*    */   public DamageSourcePredicate build() {
/* 84 */     return new DamageSourcePredicate((List<TagPredicate<DamageType>>)this.tags.build(), this.directEntity, this.sourceEntity, this.isDirect);
/*    */   }
/*    */ }


/* Location:              C:\Users\karol\AppData\Roaming\ModrinthApp\meta\versions\26.2-0.19.5\26.2-0.19.5.jar!\net\minecraft\advancements\predicates\DamageSourcePredicate$Builder.class
 * Java compiler version: 25 (69.0)
 * JD-Core Version:       1.1.3
 */