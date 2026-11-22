/*     */ package net.minecraft.world.level.storage.loot.functions;
/*     */ 
/*     */ import java.util.EnumSet;
/*     */ import java.util.List;
/*     */ import java.util.Set;
/*     */ import net.minecraft.core.Holder;
/*     */ import net.minecraft.resources.Identifier;
/*     */ import net.minecraft.world.entity.EquipmentSlotGroup;
/*     */ import net.minecraft.world.entity.ai.attributes.Attribute;
/*     */ import net.minecraft.world.entity.ai.attributes.AttributeModifier;
/*     */ import net.minecraft.world.level.storage.loot.providers.number.NumberProvider;
/*     */ 
/*     */ public class ModifierBuilder {
/*     */   private final Identifier id;
/*     */   
/*     */   private final Holder<Attribute> attribute;
/*     */   
/*     */   private final AttributeModifier.Operation operation;
/*     */   
/*     */   private final NumberProvider amount;
/*     */   
/*  86 */   private final Set<EquipmentSlotGroup> slots = EnumSet.noneOf(EquipmentSlotGroup.class);
/*     */   
/*     */   public ModifierBuilder(Identifier id, Holder<Attribute> attribute, AttributeModifier.Operation operation, NumberProvider amount) {
/*  89 */     this.id = id;
/*  90 */     this.attribute = attribute;
/*  91 */     this.operation = operation;
/*  92 */     this.amount = amount;
/*     */   }
/*     */   
/*     */   public ModifierBuilder forSlot(EquipmentSlotGroup slot) {
/*  96 */     this.slots.add(slot);
/*  97 */     return this;
/*     */   }
/*     */   
/*     */   public SetAttributesFunction.Modifier build() {
/* 101 */     return new SetAttributesFunction.Modifier(this.id, this.attribute, this.operation, this.amount, List.copyOf(this.slots));
/*     */   }
/*     */ }


/* Location:              C:\Users\karol\AppData\Roaming\ModrinthApp\meta\versions\26.2-0.19.5\26.2-0.19.5.jar!\net\minecraft\world\level\storage\loot\functions\SetAttributesFunction$ModifierBuilder.class
 * Java compiler version: 25 (69.0)
 * JD-Core Version:       1.1.3
 */