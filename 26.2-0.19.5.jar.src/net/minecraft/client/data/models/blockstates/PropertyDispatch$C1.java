/*     */ package net.minecraft.client.data.models.blockstates;
/*     */ 
/*     */ import java.util.List;
/*     */ import java.util.function.Function;
/*     */ import net.minecraft.world.level.block.state.properties.Property;
/*     */ 
/*     */ public class C1<V, T1 extends Comparable<T1>> extends PropertyDispatch<V> {
/*     */   private final Property<T1> property1;
/*     */   
/*     */   private C1(Property<T1> property1) {
/*  93 */     this.property1 = property1;
/*     */   }
/*     */   
/*     */   public List<Property<?>> getDefinedProperties() {
/*  98 */     return List.of(this.property1);
/*     */   }
/*     */   
/*     */   public C1<V, T1> select(T1 value1, V variants) {
/* 102 */     PropertyValueList key = PropertyValueList.of((Property.Value<?>[])new Property.Value[] { this.property1
/* 103 */           .value((Comparable)value1) });
/* 105 */     putValue(key, variants);
/* 106 */     return this;
/*     */   }
/*     */   
/*     */   public PropertyDispatch<V> generate(Function<T1, V> generator) {
/* 110 */     this.property1.getPossibleValues().forEach(value1 -> select((T1)value1, generator.apply(value1)));
/* 113 */     return this;
/*     */   }
/*     */ }


/* Location:              C:\Users\karol\AppData\Roaming\ModrinthApp\meta\versions\26.2-0.19.5\26.2-0.19.5.jar!\net\minecraft\client\data\models\blockstates\PropertyDispatch$C1.class
 * Java compiler version: 25 (69.0)
 * JD-Core Version:       1.1.3
 */