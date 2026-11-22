/*     */ package net.minecraft.world.level.storage.loot.functions;
/*     */ 
/*     */ import com.google.common.collect.Lists;
/*     */ import com.mojang.datafixers.kinds.App;
/*     */ import com.mojang.datafixers.kinds.Applicative;
/*     */ import com.mojang.datafixers.util.Function3;
/*     */ import com.mojang.datafixers.util.Function5;
/*     */ import com.mojang.serialization.Codec;
/*     */ import com.mojang.serialization.MapCodec;
/*     */ import com.mojang.serialization.codecs.RecordCodecBuilder;
/*     */ import java.util.EnumSet;
/*     */ import java.util.List;
/*     */ import java.util.Set;
/*     */ import net.minecraft.core.Holder;
/*     */ import net.minecraft.core.component.DataComponents;
/*     */ import net.minecraft.resources.Identifier;
/*     */ import net.minecraft.util.ExtraCodecs;
/*     */ import net.minecraft.util.RandomSource;
/*     */ import net.minecraft.util.Util;
/*     */ import net.minecraft.world.entity.EquipmentSlotGroup;
/*     */ import net.minecraft.world.entity.ai.attributes.Attribute;
/*     */ import net.minecraft.world.entity.ai.attributes.AttributeModifier;
/*     */ import net.minecraft.world.item.ItemStack;
/*     */ import net.minecraft.world.item.component.ItemAttributeModifiers;
/*     */ import net.minecraft.world.level.storage.loot.LootContext;
/*     */ import net.minecraft.world.level.storage.loot.LootContextUser;
/*     */ import net.minecraft.world.level.storage.loot.Validatable;
/*     */ import net.minecraft.world.level.storage.loot.ValidationContext;
/*     */ import net.minecraft.world.level.storage.loot.predicates.LootItemCondition;
/*     */ import net.minecraft.world.level.storage.loot.providers.number.NumberProvider;
/*     */ import net.minecraft.world.level.storage.loot.providers.number.NumberProviders;
/*     */ 
/*     */ public class SetAttributesFunction extends LootItemConditionalFunction {
/*     */   public static final MapCodec<SetAttributesFunction> MAP_CODEC;
/*     */   
/*     */   private final List<Modifier> modifiers;
/*     */   
/*     */   private final boolean replace;
/*     */   
/*     */   static {
/*  31 */     MAP_CODEC = RecordCodecBuilder.mapCodec(i -> commonFields(i).and(i.group((App)Modifier.CODEC.listOf().fieldOf("modifiers").forGetter(()), (App)Codec.BOOL.optionalFieldOf("replace", Boolean.valueOf(true)).forGetter(()))).apply((Applicative)i, SetAttributesFunction::new));
/*     */   }
/*     */   
/*     */   private SetAttributesFunction(List<LootItemCondition> predicates, List<Modifier> modifiers, boolean replace) {
/*  40 */     super(predicates);
/*  41 */     this.modifiers = List.copyOf(modifiers);
/*  42 */     this.replace = replace;
/*     */   }
/*     */   
/*     */   public MapCodec<SetAttributesFunction> codec() {
/*  47 */     return MAP_CODEC;
/*     */   }
/*     */   
/*     */   public void validate(ValidationContext context) {
/*  52 */     super.validate(context);
/*  54 */     Validatable.validate(context, "modifiers", this.modifiers);
/*     */   }
/*     */   
/*     */   public ItemStack run(ItemStack itemStack, LootContext context) {
/*  59 */     if (this.replace) {
/*  60 */       itemStack.set(DataComponents.ATTRIBUTE_MODIFIERS, updateModifiers(context, ItemAttributeModifiers.EMPTY));
/*     */     } else {
/*  62 */       itemStack.update(DataComponents.ATTRIBUTE_MODIFIERS, ItemAttributeModifiers.EMPTY, itemModifiers -> updateModifiers(context, itemModifiers));
/*     */     } 
/*  64 */     return itemStack;
/*     */   }
/*     */   
/*     */   private ItemAttributeModifiers updateModifiers(LootContext context, ItemAttributeModifiers itemModifiers) {
/*  68 */     RandomSource random = context.getRandom();
/*  69 */     for (Modifier modifier : this.modifiers) {
/*  70 */       EquipmentSlotGroup slot = (EquipmentSlotGroup)Util.getRandom(modifier.slots, random);
/*  71 */       itemModifiers = itemModifiers.withModifierAdded(modifier.attribute, new AttributeModifier(modifier.id, modifier.amount
/*     */             
/*  73 */             .getFloat(context), modifier.operation), slot);
/*     */     } 
/*  77 */     return itemModifiers;
/*     */   }
/*     */   
/*     */   public static class ModifierBuilder {
/*     */     private final Identifier id;
/*     */     
/*     */     private final Holder<Attribute> attribute;
/*     */     
/*     */     private final AttributeModifier.Operation operation;
/*     */     
/*     */     private final NumberProvider amount;
/*     */     
/*  86 */     private final Set<EquipmentSlotGroup> slots = EnumSet.noneOf(EquipmentSlotGroup.class);
/*     */     
/*     */     public ModifierBuilder(Identifier id, Holder<Attribute> attribute, AttributeModifier.Operation operation, NumberProvider amount) {
/*  89 */       this.id = id;
/*  90 */       this.attribute = attribute;
/*  91 */       this.operation = operation;
/*  92 */       this.amount = amount;
/*     */     }
/*     */     
/*     */     public ModifierBuilder forSlot(EquipmentSlotGroup slot) {
/*  96 */       this.slots.add(slot);
/*  97 */       return this;
/*     */     }
/*     */     
/*     */     public SetAttributesFunction.Modifier build() {
/* 101 */       return new SetAttributesFunction.Modifier(this.id, this.attribute, this.operation, this.amount, List.copyOf(this.slots));
/*     */     }
/*     */   }
/*     */   
/*     */   public static class Builder extends LootItemConditionalFunction.Builder<Builder> {
/*     */     private final boolean replace;
/*     */     
/* 107 */     private final List<SetAttributesFunction.Modifier> modifiers = Lists.newArrayList();
/*     */     
/*     */     public Builder(boolean replace) {
/* 110 */       this.replace = replace;
/*     */     }
/*     */     
/*     */     public Builder() {
/* 114 */       this(false);
/*     */     }
/*     */     
/*     */     protected Builder getThis() {
/* 119 */       return this;
/*     */     }
/*     */     
/*     */     public Builder withModifier(SetAttributesFunction.ModifierBuilder modifier) {
/* 123 */       this.modifiers.add(modifier.build());
/* 124 */       return this;
/*     */     }
/*     */     
/*     */     public LootItemFunction build() {
/* 129 */       return new SetAttributesFunction(getConditions(), this.modifiers, this.replace);
/*     */     }
/*     */   }
/*     */   
/*     */   public static ModifierBuilder modifier(Identifier id, Holder<Attribute> attribute, AttributeModifier.Operation operation, NumberProvider amount) {
/* 134 */     return new ModifierBuilder(id, attribute, operation, amount);
/*     */   }
/*     */   
/*     */   public static Builder setAttributes() {
/* 138 */     return new Builder();
/*     */   }
/*     */   
/*     */   private static final class Modifier extends Record implements LootContextUser {
/*     */     private final Identifier id;
/*     */     
/*     */     private final Holder<Attribute> attribute;
/*     */     
/*     */     private final AttributeModifier.Operation operation;
/*     */     
/*     */     private final NumberProvider amount;
/*     */     
/*     */     private final List<EquipmentSlotGroup> slots;
/*     */     
/*     */     private Modifier(Identifier id, Holder<Attribute> attribute, AttributeModifier.Operation operation, NumberProvider amount, List<EquipmentSlotGroup> slots) {
/* 141 */       this.id = id;
/* 141 */       this.attribute = attribute;
/* 141 */       this.operation = operation;
/* 141 */       this.amount = amount;
/* 141 */       this.slots = slots;
/*     */     }
/*     */     
/*     */     public final String toString() {
/*     */       // Byte code:
/*     */       //   0: aload_0
/*     */       //   1: <illegal opcode> toString : (Lnet/minecraft/world/level/storage/loot/functions/SetAttributesFunction$Modifier;)Ljava/lang/String;
/*     */       //   6: areturn
/*     */       // Line number table:
/*     */       //   Java source line number -> byte code offset
/*     */       //   #141	-> 0
/*     */       // Local variable table:
/*     */       //   start	length	slot	name	descriptor
/*     */       //   0	7	0	this	Lnet/minecraft/world/level/storage/loot/functions/SetAttributesFunction$Modifier;
/*     */     }
/*     */     
/*     */     public final int hashCode() {
/*     */       // Byte code:
/*     */       //   0: aload_0
/*     */       //   1: <illegal opcode> hashCode : (Lnet/minecraft/world/level/storage/loot/functions/SetAttributesFunction$Modifier;)I
/*     */       //   6: ireturn
/*     */       // Line number table:
/*     */       //   Java source line number -> byte code offset
/*     */       //   #141	-> 0
/*     */       // Local variable table:
/*     */       //   start	length	slot	name	descriptor
/*     */       //   0	7	0	this	Lnet/minecraft/world/level/storage/loot/functions/SetAttributesFunction$Modifier;
/*     */     }
/*     */     
/*     */     public final boolean equals(Object o) {
/*     */       // Byte code:
/*     */       //   0: aload_0
/*     */       //   1: aload_1
/*     */       //   2: <illegal opcode> equals : (Lnet/minecraft/world/level/storage/loot/functions/SetAttributesFunction$Modifier;Ljava/lang/Object;)Z
/*     */       //   7: ireturn
/*     */       // Line number table:
/*     */       //   Java source line number -> byte code offset
/*     */       //   #141	-> 0
/*     */       // Local variable table:
/*     */       //   start	length	slot	name	descriptor
/*     */       //   0	8	0	this	Lnet/minecraft/world/level/storage/loot/functions/SetAttributesFunction$Modifier;
/*     */       //   0	8	1	o	Ljava/lang/Object;
/*     */     }
/*     */     
/*     */     public Identifier id() {
/* 141 */       return this.id;
/*     */     }
/*     */     
/*     */     public Holder<Attribute> attribute() {
/* 141 */       return this.attribute;
/*     */     }
/*     */     
/*     */     public AttributeModifier.Operation operation() {
/* 141 */       return this.operation;
/*     */     }
/*     */     
/*     */     public NumberProvider amount() {
/* 141 */       return this.amount;
/*     */     }
/*     */     
/*     */     public List<EquipmentSlotGroup> slots() {
/* 141 */       return this.slots;
/*     */     }
/*     */     
/* 148 */     private static final Codec<List<EquipmentSlotGroup>> SLOTS_CODEC = ExtraCodecs.nonEmptyList(ExtraCodecs.compactListCodec(EquipmentSlotGroup.CODEC));
/*     */     
/*     */     public static final Codec<Modifier> CODEC;
/*     */     
/*     */     static {
/* 150 */       CODEC = RecordCodecBuilder.create(i -> i.group((App)Identifier.CODEC.fieldOf("id").forGetter(Modifier::id), (App)Attribute.CODEC.fieldOf("attribute").forGetter(Modifier::attribute), (App)AttributeModifier.Operation.CODEC.fieldOf("operation").forGetter(Modifier::operation), (App)NumberProviders.CODEC.fieldOf("amount").forGetter(Modifier::amount), (App)SLOTS_CODEC.fieldOf("slot").forGetter(Modifier::slots)).apply((Applicative)i, Modifier::new));
/*     */     }
/*     */     
/*     */     public void validate(ValidationContext context) {
/* 160 */       super.validate(context);
/* 162 */       Validatable.validate(context, "amount", (Validatable)this.amount);
/*     */     }
/*     */   }
/*     */ }


/* Location:              C:\Users\karol\AppData\Roaming\ModrinthApp\meta\versions\26.2-0.19.5\26.2-0.19.5.jar!\net\minecraft\world\level\storage\loot\functions\SetAttributesFunction.class
 * Java compiler version: 25 (69.0)
 * JD-Core Version:       1.1.3
 */