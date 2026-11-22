/*    */ package net.minecraft.advancements.predicates;
/*    */ 
/*    */ public class Builder {
/* 31 */   private MinMaxBounds.Ints level = MinMaxBounds.Ints.ANY;
/*    */   
/* 32 */   private MinMaxBounds.Doubles saturation = MinMaxBounds.Doubles.ANY;
/*    */   
/*    */   public Builder withLevel(MinMaxBounds.Ints level) {
/* 35 */     this.level = level;
/* 36 */     return this;
/*    */   }
/*    */   
/*    */   public Builder withSaturation(MinMaxBounds.Doubles saturation) {
/* 40 */     this.saturation = saturation;
/* 41 */     return this;
/*    */   }
/*    */   
/*    */   public static Builder food() {
/* 45 */     return new Builder();
/*    */   }
/*    */   
/*    */   public FoodPredicate build() {
/* 49 */     return new FoodPredicate(this.level, this.saturation);
/*    */   }
/*    */ }


/* Location:              C:\Users\karol\AppData\Roaming\ModrinthApp\meta\versions\26.2-0.19.5\26.2-0.19.5.jar!\net\minecraft\advancements\predicates\FoodPredicate$Builder.class
 * Java compiler version: 25 (69.0)
 * JD-Core Version:       1.1.3
 */