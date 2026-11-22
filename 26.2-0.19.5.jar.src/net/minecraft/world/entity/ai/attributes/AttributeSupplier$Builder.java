/*    */ package net.minecraft.world.entity.ai.attributes;
/*    */ 
/*    */ import com.google.common.collect.ImmutableMap;
/*    */ import java.util.Map;
/*    */ import net.minecraft.core.Holder;
/*    */ 
/*    */ public class Builder {
/* 66 */   private final ImmutableMap.Builder<Holder<Attribute>, AttributeInstance> builder = ImmutableMap.builder();
/*    */   
/*    */   private boolean instanceFrozen;
/*    */   
/*    */   private AttributeInstance create(Holder<Attribute> attribute) {
/* 70 */     AttributeInstance result = new AttributeInstance(attribute, attributeInstance -> {
/*    */           if (this.instanceFrozen)
/*    */             throw new UnsupportedOperationException("Tried to change value for default attribute instance: " + attribute.getRegisteredName()); 
/*    */         });
/* 75 */     this.builder.put(attribute, result);
/* 76 */     return result;
/*    */   }
/*    */   
/*    */   public Builder add(Holder<Attribute> attribute) {
/* 80 */     create(attribute);
/* 81 */     return this;
/*    */   }
/*    */   
/*    */   public Builder add(Holder<Attribute> attribute, double baseValue) {
/* 85 */     AttributeInstance result = create(attribute);
/* 86 */     result.setBaseValue(baseValue);
/* 87 */     return this;
/*    */   }
/*    */   
/*    */   public AttributeSupplier build() {
/* 91 */     this.instanceFrozen = true;
/* 92 */     return new AttributeSupplier((Map<Holder<Attribute>, AttributeInstance>)this.builder.buildKeepingLast());
/*    */   }
/*    */ }


/* Location:              C:\Users\karol\AppData\Roaming\ModrinthApp\meta\versions\26.2-0.19.5\26.2-0.19.5.jar!\net\minecraft\world\entity\ai\attributes\AttributeSupplier$Builder.class
 * Java compiler version: 25 (69.0)
 * JD-Core Version:       1.1.3
 */