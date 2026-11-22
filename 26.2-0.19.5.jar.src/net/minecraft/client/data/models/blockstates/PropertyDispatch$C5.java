/*     */ package net.minecraft.client.data.models.blockstates;
/*     */ 
/*     */ import com.mojang.datafixers.util.Function5;
/*     */ import java.util.List;
/*     */ import net.minecraft.world.level.block.state.properties.Property;
/*     */ 
/*     */ public class C5<V, T1 extends Comparable<T1>, T2 extends Comparable<T2>, T3 extends Comparable<T3>, T4 extends Comparable<T4>, T5 extends Comparable<T5>> extends PropertyDispatch<V> {
/*     */   private final Property<T1> property1;
/*     */   
/*     */   private final Property<T2> property2;
/*     */   
/*     */   private final Property<T3> property3;
/*     */   
/*     */   private final Property<T4> property4;
/*     */   
/*     */   private final Property<T5> property5;
/*     */   
/*     */   private C5(Property<T1> property1, Property<T2> property2, Property<T3> property3, Property<T4> property4, Property<T5> property5) {
/* 239 */     this.property1 = property1;
/* 240 */     this.property2 = property2;
/* 241 */     this.property3 = property3;
/* 242 */     this.property4 = property4;
/* 243 */     this.property5 = property5;
/*     */   }
/*     */   
/*     */   public List<Property<?>> getDefinedProperties() {
/* 248 */     return List.of(this.property1, this.property2, this.property3, this.property4, this.property5);
/*     */   }
/*     */   
/*     */   public C5<V, T1, T2, T3, T4, T5> select(T1 value1, T2 value2, T3 value3, T4 value4, T5 value5, V variants) {
/* 252 */     PropertyValueList key = PropertyValueList.of((Property.Value<?>[])new Property.Value[] { this.property1
/* 253 */           .value((Comparable)value1), this.property2
/* 254 */           .value((Comparable)value2), this.property3
/* 255 */           .value((Comparable)value3), this.property4
/* 256 */           .value((Comparable)value4), this.property5
/* 257 */           .value((Comparable)value5) });
/* 259 */     putValue(key, variants);
/* 260 */     return this;
/*     */   }
/*     */   
/*     */   public PropertyDispatch<V> generate(Function5<T1, T2, T3, T4, T5, V> generator) {
/* 264 */     this.property1.getPossibleValues().forEach(value1 -> this.property2.getPossibleValues().forEach(()));
/* 275 */     return this;
/*     */   }
/*     */ }


/* Location:              C:\Users\karol\AppData\Roaming\ModrinthApp\meta\versions\26.2-0.19.5\26.2-0.19.5.jar!\net\minecraft\client\data\models\blockstates\PropertyDispatch$C5.class
 * Java compiler version: 25 (69.0)
 * JD-Core Version:       1.1.3
 */