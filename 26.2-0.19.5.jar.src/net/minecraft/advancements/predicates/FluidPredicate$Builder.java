/*    */ package net.minecraft.advancements.predicates;
/*    */ 
/*    */ import java.util.Optional;
/*    */ import net.minecraft.core.Holder;
/*    */ import net.minecraft.core.HolderSet;
/*    */ import net.minecraft.world.level.material.Fluid;
/*    */ 
/*    */ public class Builder {
/* 40 */   private Optional<HolderSet<Fluid>> fluids = Optional.empty();
/*    */   
/* 41 */   private Optional<StatePropertiesPredicate> properties = Optional.empty();
/*    */   
/*    */   public static Builder fluid() {
/* 47 */     return new Builder();
/*    */   }
/*    */   
/*    */   public Builder of(Fluid fluid) {
/* 51 */     this.fluids = Optional.of(HolderSet.direct(new Holder[] { (Holder)fluid.builtInRegistryHolder() }));
/* 52 */     return this;
/*    */   }
/*    */   
/*    */   public Builder of(HolderSet<Fluid> fluids) {
/* 56 */     this.fluids = Optional.of(fluids);
/* 57 */     return this;
/*    */   }
/*    */   
/*    */   public Builder setProperties(StatePropertiesPredicate properties) {
/* 61 */     this.properties = Optional.of(properties);
/* 62 */     return this;
/*    */   }
/*    */   
/*    */   public FluidPredicate build() {
/* 66 */     return new FluidPredicate(this.fluids, this.properties);
/*    */   }
/*    */ }


/* Location:              C:\Users\karol\AppData\Roaming\ModrinthApp\meta\versions\26.2-0.19.5\26.2-0.19.5.jar!\net\minecraft\advancements\predicates\FluidPredicate$Builder.class
 * Java compiler version: 25 (69.0)
 * JD-Core Version:       1.1.3
 */