/*     */ package net.minecraft.client.data.models.blockstates;
/*     */ 
/*     */ import com.mojang.datafixers.util.Function3;
/*     */ import java.util.List;
/*     */ import net.minecraft.world.level.block.state.properties.Property;
/*     */ 
/*     */ public class C3<V, T1 extends Comparable<T1>, T2 extends Comparable<T2>, T3 extends Comparable<T3>> extends PropertyDispatch<V> {
/*     */   private final Property<T1> property1;
/*     */   
/*     */   private final Property<T2> property2;
/*     */   
/*     */   private final Property<T3> property3;
/*     */   
/*     */   private C3(Property<T1> property1, Property<T2> property2, Property<T3> property3) {
/* 156 */     this.property1 = property1;
/* 157 */     this.property2 = property2;
/* 158 */     this.property3 = property3;
/*     */   }
/*     */   
/*     */   public List<Property<?>> getDefinedProperties() {
/* 163 */     return List.of(this.property1, this.property2, this.property3);
/*     */   }
/*     */   
/*     */   public C3<V, T1, T2, T3> select(T1 value1, T2 value2, T3 value3, V variants) {
/* 167 */     PropertyValueList key = PropertyValueList.of((Property.Value<?>[])new Property.Value[] { this.property1
/* 168 */           .value((Comparable)value1), this.property2
/* 169 */           .value((Comparable)value2), this.property3
/* 170 */           .value((Comparable)value3) });
/* 172 */     putValue(key, variants);
/* 173 */     return this;
/*     */   }
/*     */   
/*     */   public PropertyDispatch<V> generate(Function3<T1, T2, T3, V> generator) {
/* 177 */     this.property1.getPossibleValues().forEach(value1 -> this.property2.getPossibleValues().forEach(()));
/* 184 */     return this;
/*     */   }
/*     */ }


/* Location:              C:\Users\karol\AppData\Roaming\ModrinthApp\meta\versions\26.2-0.19.5\26.2-0.19.5.jar!\net\minecraft\client\data\models\blockstates\PropertyDispatch$C3.class
 * Java compiler version: 25 (69.0)
 * JD-Core Version:       1.1.3
 */