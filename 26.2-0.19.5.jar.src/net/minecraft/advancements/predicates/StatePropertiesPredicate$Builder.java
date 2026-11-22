/*     */ package net.minecraft.advancements.predicates;
/*     */ 
/*     */ import com.google.common.collect.ImmutableList;
/*     */ import java.util.List;
/*     */ import java.util.Optional;
/*     */ import net.minecraft.util.StringRepresentable;
/*     */ import net.minecraft.world.level.block.state.properties.Property;
/*     */ 
/*     */ public class Builder {
/* 149 */   private final ImmutableList.Builder<StatePropertiesPredicate.PropertyMatcher> matchers = ImmutableList.builder();
/*     */   
/*     */   public static Builder properties() {
/* 155 */     return new Builder();
/*     */   }
/*     */   
/*     */   public Builder hasProperty(Property<?> property, String value) {
/* 159 */     this.matchers.add(new StatePropertiesPredicate.PropertyMatcher(property.getName(), new StatePropertiesPredicate.ExactMatcher(value)));
/* 160 */     return this;
/*     */   }
/*     */   
/*     */   public Builder hasProperty(Property<Integer> property, int value) {
/* 164 */     return hasProperty(property, Integer.toString(value));
/*     */   }
/*     */   
/*     */   public Builder hasProperty(Property<Boolean> property, boolean value) {
/* 168 */     return hasProperty(property, Boolean.toString(value));
/*     */   }
/*     */   
/*     */   public <T extends Comparable<T> & StringRepresentable> Builder hasProperty(Property<T> property, T value) {
/* 172 */     return hasProperty(property, ((StringRepresentable)value).getSerializedName());
/*     */   }
/*     */   
/*     */   public Optional<StatePropertiesPredicate> build() {
/* 176 */     return Optional.of(new StatePropertiesPredicate((List<StatePropertiesPredicate.PropertyMatcher>)this.matchers.build()));
/*     */   }
/*     */ }


/* Location:              C:\Users\karol\AppData\Roaming\ModrinthApp\meta\versions\26.2-0.19.5\26.2-0.19.5.jar!\net\minecraft\advancements\predicates\StatePropertiesPredicate$Builder.class
 * Java compiler version: 25 (69.0)
 * JD-Core Version:       1.1.3
 */