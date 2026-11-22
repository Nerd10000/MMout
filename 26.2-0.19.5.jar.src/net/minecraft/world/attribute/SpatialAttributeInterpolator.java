/*    */ package net.minecraft.world.attribute;
/*    */ 
/*    */ import it.unimi.dsi.fastutil.objects.ObjectIterator;
/*    */ import it.unimi.dsi.fastutil.objects.Reference2DoubleArrayMap;
/*    */ import it.unimi.dsi.fastutil.objects.Reference2DoubleMap;
/*    */ import it.unimi.dsi.fastutil.objects.Reference2DoubleMaps;
/*    */ import java.util.Objects;
/*    */ 
/*    */ public class SpatialAttributeInterpolator {
/* 14 */   private final Reference2DoubleArrayMap<EnvironmentAttributeMap> weightsBySource = new Reference2DoubleArrayMap();
/*    */   
/*    */   public void clear() {
/* 17 */     this.weightsBySource.clear();
/*    */   }
/*    */   
/*    */   public SpatialAttributeInterpolator accumulate(double weight, EnvironmentAttributeMap attributes) {
/* 21 */     this.weightsBySource.mergeDouble(attributes, weight, Double::sum);
/* 22 */     return this;
/*    */   }
/*    */   
/*    */   public <Value> Value applyAttributeLayer(EnvironmentAttribute<Value> attribute, Value baseValue) {
/* 26 */     if (this.weightsBySource.isEmpty())
/* 27 */       return baseValue; 
/* 28 */     if (this.weightsBySource.size() == 1) {
/* 29 */       EnvironmentAttributeMap sourceAttributes = (EnvironmentAttributeMap)this.weightsBySource.keySet().iterator().next();
/* 30 */       return sourceAttributes.applyModifier(attribute, baseValue);
/*    */     } 
/* 33 */     LerpFunction<Value> lerp = attribute.type().spatialLerp();
/* 34 */     Value resultValue = null;
/* 37 */     double accumulatedWeight = 0.0D;
/* 38 */     for (ObjectIterator<Reference2DoubleMap.Entry<EnvironmentAttributeMap>> objectIterator = Reference2DoubleMaps.fastIterable((Reference2DoubleMap)this.weightsBySource).iterator(); objectIterator.hasNext(); ) {
/* 38 */       Reference2DoubleMap.Entry<EnvironmentAttributeMap> entry = objectIterator.next();
/* 39 */       EnvironmentAttributeMap sourceAttributes = (EnvironmentAttributeMap)entry.getKey();
/* 40 */       double sourceWeight = entry.getDoubleValue();
/* 41 */       Value sourceValue = sourceAttributes.applyModifier(attribute, baseValue);
/* 42 */       accumulatedWeight += sourceWeight;
/* 43 */       if (resultValue == null) {
/* 44 */         resultValue = sourceValue;
/*    */         continue;
/*    */       } 
/* 46 */       float relativeFraction = (float)(sourceWeight / accumulatedWeight);
/* 47 */       resultValue = lerp.apply(relativeFraction, resultValue, sourceValue);
/*    */     } 
/* 51 */     return Objects.requireNonNull(resultValue);
/*    */   }
/*    */ }


/* Location:              C:\Users\karol\AppData\Roaming\ModrinthApp\meta\versions\26.2-0.19.5\26.2-0.19.5.jar!\net\minecraft\world\attribute\SpatialAttributeInterpolator.class
 * Java compiler version: 25 (69.0)
 * JD-Core Version:       1.1.3
 */