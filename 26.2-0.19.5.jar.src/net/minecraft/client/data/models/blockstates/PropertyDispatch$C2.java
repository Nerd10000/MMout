/*     */ package net.minecraft.client.data.models.blockstates;
/*     */ 
/*     */ import java.util.List;
/*     */ import java.util.function.BiFunction;
/*     */ import net.minecraft.world.level.block.state.properties.Property;
/*     */ 
/*     */ public class C2<V, T1 extends Comparable<T1>, T2 extends Comparable<T2>> extends PropertyDispatch<V> {
/*     */   private final Property<T1> property1;
/*     */   
/*     */   private final Property<T2> property2;
/*     */   
/*     */   private C2(Property<T1> property1, Property<T2> property2) {
/* 122 */     this.property1 = property1;
/* 123 */     this.property2 = property2;
/*     */   }
/*     */   
/*     */   public List<Property<?>> getDefinedProperties() {
/* 128 */     return List.of(this.property1, this.property2);
/*     */   }
/*     */   
/*     */   public C2<V, T1, T2> select(T1 value1, T2 value2, V variants) {
/* 132 */     PropertyValueList key = PropertyValueList.of((Property.Value<?>[])new Property.Value[] { this.property1
/* 133 */           .value((Comparable)value1), this.property2
/* 134 */           .value((Comparable)value2) });
/* 136 */     putValue(key, variants);
/* 137 */     return this;
/*     */   }
/*     */   
/*     */   public PropertyDispatch<V> generate(BiFunction<T1, T2, V> generator) {
/* 141 */     this.property1.getPossibleValues().forEach(value1 -> this.property2.getPossibleValues().forEach(()));
/* 146 */     return this;
/*     */   }
/*     */ }


/* Location:              C:\Users\karol\AppData\Roaming\ModrinthApp\meta\versions\26.2-0.19.5\26.2-0.19.5.jar!\net\minecraft\client\data\models\blockstates\PropertyDispatch$C2.class
 * Java compiler version: 25 (69.0)
 * JD-Core Version:       1.1.3
 */