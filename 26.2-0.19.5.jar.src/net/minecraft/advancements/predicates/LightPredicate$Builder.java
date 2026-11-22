/*    */ package net.minecraft.advancements.predicates;
/*    */ 
/*    */ public class Builder {
/* 24 */   private MinMaxBounds.Ints composite = MinMaxBounds.Ints.ANY;
/*    */   
/*    */   public static Builder light() {
/* 27 */     return new Builder();
/*    */   }
/*    */   
/*    */   public Builder setComposite(MinMaxBounds.Ints composite) {
/* 31 */     this.composite = composite;
/* 32 */     return this;
/*    */   }
/*    */   
/*    */   public LightPredicate build() {
/* 36 */     return new LightPredicate(this.composite);
/*    */   }
/*    */ }


/* Location:              C:\Users\karol\AppData\Roaming\ModrinthApp\meta\versions\26.2-0.19.5\26.2-0.19.5.jar!\net\minecraft\advancements\predicates\LightPredicate$Builder.class
 * Java compiler version: 25 (69.0)
 * JD-Core Version:       1.1.3
 */