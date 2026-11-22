/*    */ package net.minecraft.client.data.models.blockstates;
/*    */ 
/*    */ import com.google.common.collect.ImmutableMap;
/*    */ import java.util.List;
/*    */ import java.util.Map;
/*    */ import java.util.Objects;
/*    */ import java.util.stream.Stream;
/*    */ import net.minecraft.client.renderer.block.dispatch.multipart.Condition;
/*    */ import net.minecraft.client.renderer.block.dispatch.multipart.KeyValueCondition;
/*    */ import net.minecraft.world.level.block.state.properties.Property;
/*    */ 
/*    */ public class ConditionBuilder {
/* 12 */   private final ImmutableMap.Builder<String, KeyValueCondition.Terms> terms = ImmutableMap.builder();
/*    */   
/*    */   private <T extends Comparable<T>> void putValue(Property<T> property, KeyValueCondition.Terms term) {
/* 15 */     this.terms.put(property.getName(), term);
/*    */   }
/*    */   
/*    */   public final <T extends Comparable<T>> ConditionBuilder term(Property<T> property, T value) {
/* 19 */     putValue(property, new KeyValueCondition.Terms(List.of(new KeyValueCondition.Term(property.getName((Comparable)value), false))));
/* 20 */     return this;
/*    */   }
/*    */   
/*    */   @SafeVarargs
/*    */   public final <T extends Comparable<T>> ConditionBuilder term(Property<T> property, T value, T... values) {
/* 26 */     Objects.requireNonNull(property);
/* 30 */     List<KeyValueCondition.Term> terms = Stream.concat(Stream.of(value), Stream.of((Object[])values)).map(property::getName).sorted().distinct().map(v -> new KeyValueCondition.Term(v, false)).toList();
/* 31 */     putValue(property, new KeyValueCondition.Terms(terms));
/* 32 */     return this;
/*    */   }
/*    */   
/*    */   public final <T extends Comparable<T>> ConditionBuilder negatedTerm(Property<T> property, T value) {
/* 36 */     putValue(property, new KeyValueCondition.Terms(List.of(new KeyValueCondition.Term(property.getName((Comparable)value), true))));
/* 37 */     return this;
/*    */   }
/*    */   
/*    */   public Condition build() {
/* 41 */     return (Condition)new KeyValueCondition((Map)this.terms.buildOrThrow());
/*    */   }
/*    */ }


/* Location:              C:\Users\karol\AppData\Roaming\ModrinthApp\meta\versions\26.2-0.19.5\26.2-0.19.5.jar!\net\minecraft\client\data\models\blockstates\ConditionBuilder.class
 * Java compiler version: 25 (69.0)
 * JD-Core Version:       1.1.3
 */