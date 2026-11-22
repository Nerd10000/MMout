/*     */ package net.minecraft.client.data.models.blockstates;
/*     */ 
/*     */ import com.mojang.datafixers.util.Function4;
/*     */ import java.util.List;
/*     */ import net.minecraft.world.level.block.state.properties.Property;
/*     */ 
/*     */ public class C4<V, T1 extends Comparable<T1>, T2 extends Comparable<T2>, T3 extends Comparable<T3>, T4 extends Comparable<T4>> extends PropertyDispatch<V> {
/*     */   private final Property<T1> property1;
/*     */   
/*     */   private final Property<T2> property2;
/*     */   
/*     */   private final Property<T3> property3;
/*     */   
/*     */   private final Property<T4> property4;
/*     */   
/*     */   private C4(Property<T1> property1, Property<T2> property2, Property<T3> property3, Property<T4> property4) {
/* 195 */     this.property1 = property1;
/* 196 */     this.property2 = property2;
/* 197 */     this.property3 = property3;
/* 198 */     this.property4 = property4;
/*     */   }
/*     */   
/*     */   public List<Property<?>> getDefinedProperties() {
/* 203 */     return List.of(this.property1, this.property2, this.property3, this.property4);
/*     */   }
/*     */   
/*     */   public C4<V, T1, T2, T3, T4> select(T1 value1, T2 value2, T3 value3, T4 value4, V variants) {
/* 207 */     PropertyValueList key = PropertyValueList.of((Property.Value<?>[])new Property.Value[] { this.property1
/* 208 */           .value((Comparable)value1), this.property2
/* 209 */           .value((Comparable)value2), this.property3
/* 210 */           .value((Comparable)value3), this.property4
/* 211 */           .value((Comparable)value4) });
/* 213 */     putValue(key, variants);
/* 214 */     return this;
/*     */   }
/*     */   
/*     */   public PropertyDispatch<V> generate(Function4<T1, T2, T3, T4, V> generator) {
/* 218 */     this.property1.getPossibleValues().forEach(value1 -> this.property2.getPossibleValues().forEach(()));
/* 227 */     return this;
/*     */   }
/*     */ }


/* Location:              C:\Users\karol\AppData\Roaming\ModrinthApp\meta\versions\26.2-0.19.5\26.2-0.19.5.jar!\net\minecraft\client\data\models\blockstates\PropertyDispatch$C4.class
 * Java compiler version: 25 (69.0)
 * JD-Core Version:       1.1.3
 */