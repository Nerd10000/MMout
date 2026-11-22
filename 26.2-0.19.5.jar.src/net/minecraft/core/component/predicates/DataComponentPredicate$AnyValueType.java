/*     */ package net.minecraft.core.component.predicates;
/*     */ 
/*     */ import com.mojang.serialization.MapCodec;
/*     */ import net.minecraft.core.component.DataComponentType;
/*     */ 
/*     */ public final class AnyValueType extends DataComponentPredicate.TypeBase<AnyValue> {
/*     */   private final AnyValue predicate;
/*     */   
/*     */   public AnyValueType(AnyValue predicate) {
/* 108 */     super(MapCodec.unitCodec(predicate));
/* 109 */     this.predicate = predicate;
/*     */   }
/*     */   
/*     */   public AnyValue predicate() {
/* 113 */     return this.predicate;
/*     */   }
/*     */   
/*     */   public DataComponentType<?> componentType() {
/* 117 */     return this.predicate.type();
/*     */   }
/*     */   
/*     */   public static AnyValueType create(DataComponentType<?> componentType) {
/* 121 */     return new AnyValueType(new AnyValue(componentType));
/*     */   }
/*     */ }


/* Location:              C:\Users\karol\AppData\Roaming\ModrinthApp\meta\versions\26.2-0.19.5\26.2-0.19.5.jar!\net\minecraft\core\component\predicates\DataComponentPredicate$AnyValueType.class
 * Java compiler version: 25 (69.0)
 * JD-Core Version:       1.1.3
 */