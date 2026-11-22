/*     */ package net.minecraft.world.attribute;
/*     */ 
/*     */ import java.util.Objects;
/*     */ 
/*     */ public class Builder<Value> {
/*     */   private final AttributeType<Value> type;
/*     */   
/*     */   private Value defaultValue;
/*     */   
/*  74 */   private AttributeRange<Value> valueRange = AttributeRange.any();
/*     */   
/*     */   private boolean isSyncable = false;
/*     */   
/*     */   private boolean isPositional = true;
/*     */   
/*     */   private boolean isSpatiallyInterpolated = false;
/*     */   
/*     */   public Builder(AttributeType<Value> type) {
/*  80 */     this.type = type;
/*     */   }
/*     */   
/*     */   public Builder<Value> defaultValue(Value defaultValue) {
/*  84 */     this.defaultValue = defaultValue;
/*  85 */     return this;
/*     */   }
/*     */   
/*     */   public Builder<Value> valueRange(AttributeRange<Value> valueRange) {
/*  89 */     this.valueRange = valueRange;
/*  90 */     return this;
/*     */   }
/*     */   
/*     */   public Builder<Value> syncable() {
/*  94 */     this.isSyncable = true;
/*  95 */     return this;
/*     */   }
/*     */   
/*     */   public Builder<Value> notPositional() {
/*  99 */     this.isPositional = false;
/* 100 */     return this;
/*     */   }
/*     */   
/*     */   public Builder<Value> spatiallyInterpolated() {
/* 104 */     this.isSpatiallyInterpolated = true;
/* 105 */     return this;
/*     */   }
/*     */   
/*     */   public EnvironmentAttribute<Value> build() {
/* 109 */     return new EnvironmentAttribute<>(this.type, 
/*     */         
/* 111 */         Objects.requireNonNull(this.defaultValue, "Missing default value"), this.valueRange, this.isSyncable, this.isPositional, this.isSpatiallyInterpolated);
/*     */   }
/*     */ }


/* Location:              C:\Users\karol\AppData\Roaming\ModrinthApp\meta\versions\26.2-0.19.5\26.2-0.19.5.jar!\net\minecraft\world\attribute\EnvironmentAttribute$Builder.class
 * Java compiler version: 25 (69.0)
 * JD-Core Version:       1.1.3
 */