/*     */ package net.minecraft.core.component.predicates;
/*     */ 
/*     */ import com.mojang.serialization.Codec;
/*     */ 
/*     */ public final class ConcreteType<T extends DataComponentPredicate> extends DataComponentPredicate.TypeBase<T> {
/*     */   public ConcreteType(Codec<T> codec) {
/* 100 */     super(codec);
/*     */   }
/*     */ }


/* Location:              C:\Users\karol\AppData\Roaming\ModrinthApp\meta\versions\26.2-0.19.5\26.2-0.19.5.jar!\net\minecraft\core\component\predicates\DataComponentPredicate$ConcreteType.class
 * Java compiler version: 25 (69.0)
 * JD-Core Version:       1.1.3
 */