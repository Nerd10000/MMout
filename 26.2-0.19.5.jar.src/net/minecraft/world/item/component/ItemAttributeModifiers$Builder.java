/*     */ package net.minecraft.world.item.component;
/*     */ 
/*     */ import com.google.common.collect.ImmutableList;
/*     */ import java.util.List;
/*     */ import net.minecraft.core.Holder;
/*     */ import net.minecraft.world.entity.EquipmentSlotGroup;
/*     */ import net.minecraft.world.entity.ai.attributes.Attribute;
/*     */ import net.minecraft.world.entity.ai.attributes.AttributeModifier;
/*     */ 
/*     */ public class Builder {
/* 287 */   private final ImmutableList.Builder<ItemAttributeModifiers.Entry> entries = ImmutableList.builder();
/*     */   
/*     */   public Builder add(Holder<Attribute> attribute, AttributeModifier modifier, EquipmentSlotGroup slot) {
/* 293 */     this.entries.add(new ItemAttributeModifiers.Entry(attribute, modifier, slot));
/* 294 */     return this;
/*     */   }
/*     */   
/*     */   public Builder add(Holder<Attribute> attribute, AttributeModifier modifier, EquipmentSlotGroup slot, ItemAttributeModifiers.Display display) {
/* 299 */     this.entries.add(new ItemAttributeModifiers.Entry(attribute, modifier, slot, display));
/* 300 */     return this;
/*     */   }
/*     */   
/*     */   public ItemAttributeModifiers build() {
/* 304 */     return new ItemAttributeModifiers((List<ItemAttributeModifiers.Entry>)this.entries.build());
/*     */   }
/*     */ }


/* Location:              C:\Users\karol\AppData\Roaming\ModrinthApp\meta\versions\26.2-0.19.5\26.2-0.19.5.jar!\net\minecraft\world\item\component\ItemAttributeModifiers$Builder.class
 * Java compiler version: 25 (69.0)
 * JD-Core Version:       1.1.3
 */