/*    */ package net.minecraft.advancements.predicates;
/*    */ 
/*    */ import com.google.common.collect.ImmutableMap;
/*    */ import java.util.Map;
/*    */ import net.minecraft.core.component.DataComponentExactPredicate;
/*    */ import net.minecraft.core.component.DataComponentType;
/*    */ import net.minecraft.core.component.predicates.DataComponentPredicate;
/*    */ 
/*    */ public class Builder {
/* 53 */   private DataComponentExactPredicate exact = DataComponentExactPredicate.EMPTY;
/*    */   
/* 54 */   private final ImmutableMap.Builder<DataComponentPredicate.Type<?>, DataComponentPredicate> partial = ImmutableMap.builder();
/*    */   
/*    */   public static Builder components() {
/* 60 */     return new Builder();
/*    */   }
/*    */   
/*    */   public <T extends DataComponentType<?>> Builder any(DataComponentType<?> type) {
/* 64 */     DataComponentPredicate.AnyValueType predicateType = DataComponentPredicate.AnyValueType.create(type);
/* 65 */     this.partial.put(predicateType, predicateType.predicate());
/* 66 */     return this;
/*    */   }
/*    */   
/*    */   public <T extends DataComponentPredicate> Builder partial(DataComponentPredicate.Type<T> type, T predicate) {
/* 70 */     this.partial.put(type, predicate);
/* 71 */     return this;
/*    */   }
/*    */   
/*    */   public Builder exact(DataComponentExactPredicate exact) {
/* 75 */     this.exact = exact;
/* 76 */     return this;
/*    */   }
/*    */   
/*    */   public DataComponentMatchers build() {
/* 80 */     return new DataComponentMatchers(this.exact, (Map<DataComponentPredicate.Type<?>, DataComponentPredicate>)this.partial.buildOrThrow());
/*    */   }
/*    */ }


/* Location:              C:\Users\karol\AppData\Roaming\ModrinthApp\meta\versions\26.2-0.19.5\26.2-0.19.5.jar!\net\minecraft\advancements\predicates\DataComponentMatchers$Builder.class
 * Java compiler version: 25 (69.0)
 * JD-Core Version:       1.1.3
 */