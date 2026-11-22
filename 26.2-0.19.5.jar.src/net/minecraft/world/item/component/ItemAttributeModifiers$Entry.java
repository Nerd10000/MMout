/*     */ package net.minecraft.world.item.component;
/*     */ 
/*     */ import com.mojang.datafixers.kinds.App;
/*     */ import com.mojang.datafixers.kinds.Applicative;
/*     */ import com.mojang.datafixers.util.Function4;
/*     */ import com.mojang.serialization.Codec;
/*     */ import com.mojang.serialization.codecs.RecordCodecBuilder;
/*     */ import net.minecraft.core.Holder;
/*     */ import net.minecraft.network.RegistryFriendlyByteBuf;
/*     */ import net.minecraft.network.codec.StreamCodec;
/*     */ import net.minecraft.resources.Identifier;
/*     */ import net.minecraft.world.entity.EquipmentSlotGroup;
/*     */ import net.minecraft.world.entity.ai.attributes.Attribute;
/*     */ import net.minecraft.world.entity.ai.attributes.AttributeModifier;
/*     */ 
/*     */ public final class Entry extends Record {
/*     */   private final Holder<Attribute> attribute;
/*     */   
/*     */   private final AttributeModifier modifier;
/*     */   
/*     */   private final EquipmentSlotGroup slot;
/*     */   
/*     */   private final ItemAttributeModifiers.Display display;
/*     */   
/*     */   public static final Codec<Entry> CODEC;
/*     */   
/*     */   public Entry(Holder<Attribute> attribute, AttributeModifier modifier, EquipmentSlotGroup slot, ItemAttributeModifiers.Display display) {
/* 261 */     this.attribute = attribute;
/* 261 */     this.modifier = modifier;
/* 261 */     this.slot = slot;
/* 261 */     this.display = display;
/*     */   }
/*     */   
/*     */   public final String toString() {
/*     */     // Byte code:
/*     */     //   0: aload_0
/*     */     //   1: <illegal opcode> toString : (Lnet/minecraft/world/item/component/ItemAttributeModifiers$Entry;)Ljava/lang/String;
/*     */     //   6: areturn
/*     */     // Line number table:
/*     */     //   Java source line number -> byte code offset
/*     */     //   #261	-> 0
/*     */     // Local variable table:
/*     */     //   start	length	slot	name	descriptor
/*     */     //   0	7	0	this	Lnet/minecraft/world/item/component/ItemAttributeModifiers$Entry;
/*     */   }
/*     */   
/*     */   public final int hashCode() {
/*     */     // Byte code:
/*     */     //   0: aload_0
/*     */     //   1: <illegal opcode> hashCode : (Lnet/minecraft/world/item/component/ItemAttributeModifiers$Entry;)I
/*     */     //   6: ireturn
/*     */     // Line number table:
/*     */     //   Java source line number -> byte code offset
/*     */     //   #261	-> 0
/*     */     // Local variable table:
/*     */     //   start	length	slot	name	descriptor
/*     */     //   0	7	0	this	Lnet/minecraft/world/item/component/ItemAttributeModifiers$Entry;
/*     */   }
/*     */   
/*     */   public final boolean equals(Object o) {
/*     */     // Byte code:
/*     */     //   0: aload_0
/*     */     //   1: aload_1
/*     */     //   2: <illegal opcode> equals : (Lnet/minecraft/world/item/component/ItemAttributeModifiers$Entry;Ljava/lang/Object;)Z
/*     */     //   7: ireturn
/*     */     // Line number table:
/*     */     //   Java source line number -> byte code offset
/*     */     //   #261	-> 0
/*     */     // Local variable table:
/*     */     //   start	length	slot	name	descriptor
/*     */     //   0	8	0	this	Lnet/minecraft/world/item/component/ItemAttributeModifiers$Entry;
/*     */     //   0	8	1	o	Ljava/lang/Object;
/*     */   }
/*     */   
/*     */   public Holder<Attribute> attribute() {
/* 261 */     return this.attribute;
/*     */   }
/*     */   
/*     */   public AttributeModifier modifier() {
/* 261 */     return this.modifier;
/*     */   }
/*     */   
/*     */   public EquipmentSlotGroup slot() {
/* 261 */     return this.slot;
/*     */   }
/*     */   
/*     */   public ItemAttributeModifiers.Display display() {
/* 261 */     return this.display;
/*     */   }
/*     */   
/*     */   static {
/* 262 */     CODEC = RecordCodecBuilder.create(i -> i.group((App)Attribute.CODEC.fieldOf("type").forGetter(Entry::attribute), (App)AttributeModifier.MAP_CODEC.forGetter(Entry::modifier), (App)EquipmentSlotGroup.CODEC.optionalFieldOf("slot", EquipmentSlotGroup.ANY).forGetter(Entry::slot), (App)ItemAttributeModifiers.Display.CODEC.optionalFieldOf("display", ItemAttributeModifiers.Display.Default.INSTANCE).forGetter(Entry::display)).apply((Applicative)i, Entry::new));
/*     */   }
/*     */   
/* 269 */   public static final StreamCodec<RegistryFriendlyByteBuf, Entry> STREAM_CODEC = StreamCodec.composite(Attribute.STREAM_CODEC, Entry::attribute, AttributeModifier.STREAM_CODEC, Entry::modifier, EquipmentSlotGroup.STREAM_CODEC, Entry::slot, ItemAttributeModifiers.Display.STREAM_CODEC, Entry::display, Entry::new);
/*     */   
/*     */   public Entry(Holder<Attribute> attribute, AttributeModifier modifier, EquipmentSlotGroup slot) {
/* 278 */     this(attribute, modifier, slot, ItemAttributeModifiers.Display.attributeModifiers());
/*     */   }
/*     */   
/*     */   public boolean matches(Holder<Attribute> attribute, Identifier id) {
/* 282 */     return (attribute.equals(this.attribute) && this.modifier.is(id));
/*     */   }
/*     */ }


/* Location:              C:\Users\karol\AppData\Roaming\ModrinthApp\meta\versions\26.2-0.19.5\26.2-0.19.5.jar!\net\minecraft\world\item\component\ItemAttributeModifiers$Entry.class
 * Java compiler version: 25 (69.0)
 * JD-Core Version:       1.1.3
 */