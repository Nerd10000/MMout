/*     */ package net.minecraft.util;
/*     */ 
/*     */ import com.google.common.collect.Lists;
/*     */ import it.unimi.dsi.fastutil.floats.Float2FloatFunction;
/*     */ import it.unimi.dsi.fastutil.floats.FloatArrayList;
/*     */ import it.unimi.dsi.fastutil.floats.FloatList;
/*     */ import java.util.List;
/*     */ 
/*     */ public final class Builder<I extends BoundedFloatFunction<?>> {
/*     */   private final I coordinate;
/*     */   
/*     */   private final Float2FloatFunction valueTransformer;
/*     */   
/* 341 */   private final FloatList locations = (FloatList)new FloatArrayList();
/*     */   
/* 342 */   private final List<CubicSpline<I>> values = Lists.newArrayList();
/*     */   
/* 343 */   private final FloatList derivatives = (FloatList)new FloatArrayList();
/*     */   
/*     */   private Builder(I coordinate) {
/* 346 */     this(coordinate, Float2FloatFunction.identity());
/*     */   }
/*     */   
/*     */   private Builder(I coordinate, Float2FloatFunction valueTransformer) {
/* 350 */     this.coordinate = coordinate;
/* 351 */     this.valueTransformer = valueTransformer;
/*     */   }
/*     */   
/*     */   public Builder<I> addPoint(float location, float value) {
/* 355 */     return addPoint(location, new CubicSpline.Constant<>(((Float)this.valueTransformer.apply(Float.valueOf(value))).floatValue()), 0.0F);
/*     */   }
/*     */   
/*     */   public Builder<I> addPoint(float location, float value, float derivative) {
/* 359 */     return addPoint(location, new CubicSpline.Constant<>(((Float)this.valueTransformer.apply(Float.valueOf(value))).floatValue()), derivative);
/*     */   }
/*     */   
/*     */   public Builder<I> addPoint(float location, CubicSpline<I> sampler) {
/* 363 */     return addPoint(location, sampler, 0.0F);
/*     */   }
/*     */   
/*     */   private Builder<I> addPoint(float location, CubicSpline<I> sampler, float derivative) {
/* 367 */     if (!this.locations.isEmpty() && location <= this.locations.getFloat(this.locations.size() - 1))
/* 368 */       throw new IllegalArgumentException("Please register points in ascending order"); 
/* 370 */     this.locations.add(location);
/* 371 */     this.values.add(sampler);
/* 372 */     this.derivatives.add(derivative);
/* 373 */     return this;
/*     */   }
/*     */   
/*     */   public CubicSpline<I> build() {
/* 377 */     if (this.locations.isEmpty())
/* 378 */       throw new IllegalStateException("No elements added"); 
/* 380 */     return new CubicSpline.Multipoint<>(this.coordinate, this.locations.toFloatArray(), List.copyOf(this.values), this.derivatives.toFloatArray());
/*     */   }
/*     */ }


/* Location:              C:\Users\karol\AppData\Roaming\ModrinthApp\meta\versions\26.2-0.19.5\26.2-0.19.5.jar!\net\minecraf\\util\CubicSpline$Builder.class
 * Java compiler version: 25 (69.0)
 * JD-Core Version:       1.1.3
 */