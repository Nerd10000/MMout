/*     */ package net.minecraft.world.level.storage.loot.functions;
/*     */ 
/*     */ import com.mojang.datafixers.kinds.App;
/*     */ import com.mojang.datafixers.kinds.Applicative;
/*     */ import com.mojang.datafixers.util.Function5;
/*     */ import com.mojang.serialization.Codec;
/*     */ import com.mojang.serialization.codecs.RecordCodecBuilder;
/*     */ import java.util.List;
/*     */ import net.minecraft.core.Holder;
/*     */ import net.minecraft.resources.Identifier;
/*     */ import net.minecraft.util.ExtraCodecs;
/*     */ import net.minecraft.world.entity.EquipmentSlotGroup;
/*     */ import net.minecraft.world.entity.ai.attributes.Attribute;
/*     */ import net.minecraft.world.entity.ai.attributes.AttributeModifier;
/*     */ import net.minecraft.world.level.storage.loot.LootContextUser;
/*     */ import net.minecraft.world.level.storage.loot.Validatable;
/*     */ import net.minecraft.world.level.storage.loot.ValidationContext;
/*     */ import net.minecraft.world.level.storage.loot.providers.number.NumberProvider;
/*     */ import net.minecraft.world.level.storage.loot.providers.number.NumberProviders;
/*     */ 
/*     */ final class Modifier extends Record implements LootContextUser {
/*     */   private final Identifier id;
/*     */   
/*     */   private final Holder<Attribute> attribute;
/*     */   
/*     */   private final AttributeModifier.Operation operation;
/*     */   
/*     */   private final NumberProvider amount;
/*     */   
/*     */   private final List<EquipmentSlotGroup> slots;
/*     */   
/*     */   private Modifier(Identifier id, Holder<Attribute> attribute, AttributeModifier.Operation operation, NumberProvider amount, List<EquipmentSlotGroup> slots) {
/* 141 */     this.id = id;
/* 141 */     this.attribute = attribute;
/* 141 */     this.operation = operation;
/* 141 */     this.amount = amount;
/* 141 */     this.slots = slots;
/*     */   }
/*     */   
/*     */   public final String toString() {
/*     */     // Byte code:
/*     */     //   0: aload_0
/*     */     //   1: <illegal opcode> toString : (Lnet/minecraft/world/level/storage/loot/functions/SetAttributesFunction$Modifier;)Ljava/lang/String;
/*     */     //   6: areturn
/*     */     // Line number table:
/*     */     //   Java source line number -> byte code offset
/*     */     //   #141	-> 0
/*     */     // Local variable table:
/*     */     //   start	length	slot	name	descriptor
/*     */     //   0	7	0	this	Lnet/minecraft/world/level/storage/loot/functions/SetAttributesFunction$Modifier;
/*     */   }
/*     */   
/*     */   public final int hashCode() {
/*     */     // Byte code:
/*     */     //   0: aload_0
/*     */     //   1: <illegal opcode> hashCode : (Lnet/minecraft/world/level/storage/loot/functions/SetAttributesFunction$Modifier;)I
/*     */     //   6: ireturn
/*     */     // Line number table:
/*     */     //   Java source line number -> byte code offset
/*     */     //   #141	-> 0
/*     */     // Local variable table:
/*     */     //   start	length	slot	name	descriptor
/*     */     //   0	7	0	this	Lnet/minecraft/world/level/storage/loot/functions/SetAttributesFunction$Modifier;
/*     */   }
/*     */   
/*     */   public final boolean equals(Object o) {
/*     */     // Byte code:
/*     */     //   0: aload_0
/*     */     //   1: aload_1
/*     */     //   2: <illegal opcode> equals : (Lnet/minecraft/world/level/storage/loot/functions/SetAttributesFunction$Modifier;Ljava/lang/Object;)Z
/*     */     //   7: ireturn
/*     */     // Line number table:
/*     */     //   Java source line number -> byte code offset
/*     */     //   #141	-> 0
/*     */     // Local variable table:
/*     */     //   start	length	slot	name	descriptor
/*     */     //   0	8	0	this	Lnet/minecraft/world/level/storage/loot/functions/SetAttributesFunction$Modifier;
/*     */     //   0	8	1	o	Ljava/lang/Object;
/*     */   }
/*     */   
/*     */   public Identifier id() {
/* 141 */     return this.id;
/*     */   }
/*     */   
/*     */   public Holder<Attribute> attribute() {
/* 141 */     return this.attribute;
/*     */   }
/*     */   
/*     */   public AttributeModifier.Operation operation() {
/* 141 */     return this.operation;
/*     */   }
/*     */   
/*     */   public NumberProvider amount() {
/* 141 */     return this.amount;
/*     */   }
/*     */   
/*     */   public List<EquipmentSlotGroup> slots() {
/* 141 */     return this.slots;
/*     */   }
/*     */   
/* 148 */   private static final Codec<List<EquipmentSlotGroup>> SLOTS_CODEC = ExtraCodecs.nonEmptyList(ExtraCodecs.compactListCodec(EquipmentSlotGroup.CODEC));
/*     */   
/*     */   public static final Codec<Modifier> CODEC;
/*     */   
/*     */   static {
/* 150 */     CODEC = RecordCodecBuilder.create(i -> i.group((App)Identifier.CODEC.fieldOf("id").forGetter(Modifier::id), (App)Attribute.CODEC.fieldOf("attribute").forGetter(Modifier::attribute), (App)AttributeModifier.Operation.CODEC.fieldOf("operation").forGetter(Modifier::operation), (App)NumberProviders.CODEC.fieldOf("amount").forGetter(Modifier::amount), (App)SLOTS_CODEC.fieldOf("slot").forGetter(Modifier::slots)).apply((Applicative)i, Modifier::new));
/*     */   }
/*     */   
/*     */   public void validate(ValidationContext context) {
/* 160 */     super.validate(context);
/* 162 */     Validatable.validate(context, "amount", (Validatable)this.amount);
/*     */   }
/*     */ }


/* Location:              C:\Users\karol\AppData\Roaming\ModrinthApp\meta\versions\26.2-0.19.5\26.2-0.19.5.jar!\net\minecraft\world\level\storage\loot\functions\SetAttributesFunction$Modifier.class
 * Java compiler version: 25 (69.0)
 * JD-Core Version:       1.1.3
 */