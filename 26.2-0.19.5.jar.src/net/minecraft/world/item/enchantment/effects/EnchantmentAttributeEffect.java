/*    */ package net.minecraft.world.item.enchantment.effects;
/*    */ 
/*    */ import com.google.common.collect.HashMultimap;
/*    */ import com.google.common.collect.Multimap;
/*    */ import com.mojang.datafixers.kinds.App;
/*    */ import com.mojang.datafixers.kinds.Applicative;
/*    */ import com.mojang.datafixers.util.Function4;
/*    */ import com.mojang.serialization.MapCodec;
/*    */ import com.mojang.serialization.codecs.RecordCodecBuilder;
/*    */ import net.minecraft.core.Holder;
/*    */ import net.minecraft.resources.Identifier;
/*    */ import net.minecraft.server.level.ServerLevel;
/*    */ import net.minecraft.util.StringRepresentable;
/*    */ import net.minecraft.world.entity.Entity;
/*    */ import net.minecraft.world.entity.EquipmentSlot;
/*    */ import net.minecraft.world.entity.LivingEntity;
/*    */ import net.minecraft.world.entity.ai.attributes.Attribute;
/*    */ import net.minecraft.world.entity.ai.attributes.AttributeModifier;
/*    */ import net.minecraft.world.item.enchantment.EnchantedItemInUse;
/*    */ import net.minecraft.world.item.enchantment.LevelBasedValue;
/*    */ import net.minecraft.world.phys.Vec3;
/*    */ 
/*    */ public final class EnchantmentAttributeEffect extends Record implements EnchantmentLocationBasedEffect {
/*    */   private final Identifier id;
/*    */   
/*    */   private final Holder<Attribute> attribute;
/*    */   
/*    */   private final LevelBasedValue amount;
/*    */   
/*    */   private final AttributeModifier.Operation operation;
/*    */   
/*    */   public static final MapCodec<EnchantmentAttributeEffect> MAP_CODEC;
/*    */   
/*    */   public EnchantmentAttributeEffect(Identifier id, Holder<Attribute> attribute, LevelBasedValue amount, AttributeModifier.Operation operation) {
/* 19 */     this.id = id;
/* 19 */     this.attribute = attribute;
/* 19 */     this.amount = amount;
/* 19 */     this.operation = operation;
/*    */   }
/*    */   
/*    */   public final String toString() {
/*    */     // Byte code:
/*    */     //   0: aload_0
/*    */     //   1: <illegal opcode> toString : (Lnet/minecraft/world/item/enchantment/effects/EnchantmentAttributeEffect;)Ljava/lang/String;
/*    */     //   6: areturn
/*    */     // Line number table:
/*    */     //   Java source line number -> byte code offset
/*    */     //   #19	-> 0
/*    */     // Local variable table:
/*    */     //   start	length	slot	name	descriptor
/*    */     //   0	7	0	this	Lnet/minecraft/world/item/enchantment/effects/EnchantmentAttributeEffect;
/*    */   }
/*    */   
/*    */   public final int hashCode() {
/*    */     // Byte code:
/*    */     //   0: aload_0
/*    */     //   1: <illegal opcode> hashCode : (Lnet/minecraft/world/item/enchantment/effects/EnchantmentAttributeEffect;)I
/*    */     //   6: ireturn
/*    */     // Line number table:
/*    */     //   Java source line number -> byte code offset
/*    */     //   #19	-> 0
/*    */     // Local variable table:
/*    */     //   start	length	slot	name	descriptor
/*    */     //   0	7	0	this	Lnet/minecraft/world/item/enchantment/effects/EnchantmentAttributeEffect;
/*    */   }
/*    */   
/*    */   public final boolean equals(Object o) {
/*    */     // Byte code:
/*    */     //   0: aload_0
/*    */     //   1: aload_1
/*    */     //   2: <illegal opcode> equals : (Lnet/minecraft/world/item/enchantment/effects/EnchantmentAttributeEffect;Ljava/lang/Object;)Z
/*    */     //   7: ireturn
/*    */     // Line number table:
/*    */     //   Java source line number -> byte code offset
/*    */     //   #19	-> 0
/*    */     // Local variable table:
/*    */     //   start	length	slot	name	descriptor
/*    */     //   0	8	0	this	Lnet/minecraft/world/item/enchantment/effects/EnchantmentAttributeEffect;
/*    */     //   0	8	1	o	Ljava/lang/Object;
/*    */   }
/*    */   
/*    */   public Identifier id() {
/* 19 */     return this.id;
/*    */   }
/*    */   
/*    */   public Holder<Attribute> attribute() {
/* 19 */     return this.attribute;
/*    */   }
/*    */   
/*    */   public LevelBasedValue amount() {
/* 19 */     return this.amount;
/*    */   }
/*    */   
/*    */   public AttributeModifier.Operation operation() {
/* 19 */     return this.operation;
/*    */   }
/*    */   
/*    */   static {
/* 25 */     MAP_CODEC = RecordCodecBuilder.mapCodec(i -> i.group((App)Identifier.CODEC.fieldOf("id").forGetter(EnchantmentAttributeEffect::id), (App)Attribute.CODEC.fieldOf("attribute").forGetter(EnchantmentAttributeEffect::attribute), (App)LevelBasedValue.CODEC.fieldOf("amount").forGetter(EnchantmentAttributeEffect::amount), (App)AttributeModifier.Operation.CODEC.fieldOf("operation").forGetter(EnchantmentAttributeEffect::operation)).apply((Applicative)i, EnchantmentAttributeEffect::new));
/*    */   }
/*    */   
/*    */   private Identifier idForSlot(StringRepresentable slot) {
/* 33 */     return this.id.withSuffix("/" + slot.getSerializedName());
/*    */   }
/*    */   
/*    */   public AttributeModifier getModifier(int level, StringRepresentable slot) {
/* 37 */     return new AttributeModifier(idForSlot(slot), amount().calculate(level), operation());
/*    */   }
/*    */   
/*    */   public void onChangedBlock(ServerLevel serverLevel, int enchantmentLevel, EnchantedItemInUse item, Entity entity, Vec3 position, boolean becameActive) {
/* 42 */     if (becameActive && entity instanceof LivingEntity) {
/* 42 */       LivingEntity living = (LivingEntity)entity;
/* 43 */       living.getAttributes().addTransientAttributeModifiers((Multimap)makeAttributeMap(enchantmentLevel, item.inSlot()));
/*    */     } 
/*    */   }
/*    */   
/*    */   public void onDeactivated(EnchantedItemInUse item, Entity entity, Vec3 position, int level) {
/* 49 */     if (entity instanceof LivingEntity) {
/* 49 */       LivingEntity living = (LivingEntity)entity;
/* 50 */       living.getAttributes().removeAttributeModifiers((Multimap)makeAttributeMap(level, item.inSlot()));
/*    */     } 
/*    */   }
/*    */   
/*    */   private HashMultimap<Holder<Attribute>, AttributeModifier> makeAttributeMap(int enchantmentLevel, EquipmentSlot slot) {
/* 55 */     HashMultimap<Holder<Attribute>, AttributeModifier> map = HashMultimap.create();
/* 56 */     map.put(this.attribute, getModifier(enchantmentLevel, (StringRepresentable)slot));
/* 57 */     return map;
/*    */   }
/*    */   
/*    */   public MapCodec<EnchantmentAttributeEffect> codec() {
/* 62 */     return MAP_CODEC;
/*    */   }
/*    */ }


/* Location:              C:\Users\karol\AppData\Roaming\ModrinthApp\meta\versions\26.2-0.19.5\26.2-0.19.5.jar!\net\minecraft\world\item\enchantment\effects\EnchantmentAttributeEffect.class
 * Java compiler version: 25 (69.0)
 * JD-Core Version:       1.1.3
 */