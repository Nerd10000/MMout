/*    */ package net.minecraft.world.attribute;
/*    */ 
/*    */ class ValueProbe<Value> {
/*    */   private Value lastValue;
/*    */   
/*    */   private Value newValue;
/*    */   
/*    */   public ValueProbe(EnvironmentAttribute<Value> attribute) {
/* 54 */     Value value = getValueFromLevel(attribute);
/* 55 */     this.lastValue = value;
/* 56 */     this.newValue = value;
/*    */   }
/*    */   
/*    */   private Value getValueFromLevel(EnvironmentAttribute<Value> attribute) {
/* 60 */     if (EnvironmentAttributeProbe.this.level == null || EnvironmentAttributeProbe.this.position == null)
/* 61 */       return attribute.defaultValue(); 
/* 63 */     return EnvironmentAttributeProbe.this.level.environmentAttributes().getValue(attribute, EnvironmentAttributeProbe.this.position, EnvironmentAttributeProbe.this.biomeInterpolator);
/*    */   }
/*    */   
/*    */   public boolean tick() {
/* 68 */     if (this.newValue == null)
/* 69 */       return true; 
/* 71 */     this.lastValue = this.newValue;
/* 72 */     this.newValue = null;
/* 73 */     return false;
/*    */   }
/*    */   
/*    */   public Value get(EnvironmentAttribute<Value> attribute, float partialTicks) {
/* 77 */     if (this.newValue == null)
/* 78 */       this.newValue = getValueFromLevel(attribute); 
/* 80 */     return attribute.type().partialTickLerp().apply(partialTicks, this.lastValue, this.newValue);
/*    */   }
/*    */ }


/* Location:              C:\Users\karol\AppData\Roaming\ModrinthApp\meta\versions\26.2-0.19.5\26.2-0.19.5.jar!\net\minecraft\world\attribute\EnvironmentAttributeProbe$ValueProbe.class
 * Java compiler version: 25 (69.0)
 * JD-Core Version:       1.1.3
 */