/*     */ package net.minecraft.world.item.component;
/*     */ 
/*     */ import com.mojang.datafixers.kinds.App;
/*     */ import com.mojang.datafixers.kinds.Applicative;
/*     */ import com.mojang.serialization.Codec;
/*     */ import com.mojang.serialization.MapCodec;
/*     */ import com.mojang.serialization.codecs.RecordCodecBuilder;
/*     */ import io.netty.buffer.ByteBuf;
/*     */ import java.util.function.Consumer;
/*     */ import java.util.function.Function;
/*     */ import java.util.function.IntFunction;
/*     */ import net.minecraft.ChatFormatting;
/*     */ import net.minecraft.core.Holder;
/*     */ import net.minecraft.network.RegistryFriendlyByteBuf;
/*     */ import net.minecraft.network.chat.CommonComponents;
/*     */ import net.minecraft.network.chat.Component;
/*     */ import net.minecraft.network.chat.ComponentSerialization;
/*     */ import net.minecraft.network.codec.ByteBufCodecs;
/*     */ import net.minecraft.network.codec.StreamCodec;
/*     */ import net.minecraft.util.ByIdMap;
/*     */ import net.minecraft.util.StringRepresentable;
/*     */ import net.minecraft.world.entity.ai.attributes.Attribute;
/*     */ import net.minecraft.world.entity.ai.attributes.AttributeModifier;
/*     */ import net.minecraft.world.entity.ai.attributes.Attributes;
/*     */ import net.minecraft.world.entity.player.Player;
/*     */ import net.minecraft.world.item.Item;
/*     */ 
/*     */ public interface Display {
/*     */   public static final Codec<Display> CODEC;
/*     */   
/*     */   static {
/* 108 */     CODEC = Type.CODEC.dispatch("type", Display::type, type -> type.codec);
/*     */   }
/*     */   
/* 110 */   public static final StreamCodec<RegistryFriendlyByteBuf, Display> STREAM_CODEC = Type.STREAM_CODEC.cast().dispatch(Display::type, Type::streamCodec);
/*     */   
/*     */   static Display attributeModifiers() {
/* 113 */     return Default.INSTANCE;
/*     */   }
/*     */   
/*     */   static Display hidden() {
/* 117 */     return Hidden.INSTANCE;
/*     */   }
/*     */   
/*     */   static Display override(Component component) {
/* 121 */     return new OverrideText(component);
/*     */   }
/*     */   
/*     */   Type type();
/*     */   
/*     */   void apply(Consumer<Component> paramConsumer, Player paramPlayer, Holder<Attribute> paramHolder, AttributeModifier paramAttributeModifier);
/*     */   
/*     */   public enum Type implements StringRepresentable {
/* 129 */     DEFAULT("default", 0, (String)ItemAttributeModifiers.Display.Default.CODEC, ItemAttributeModifiers.Display.Default.STREAM_CODEC),
/* 130 */     HIDDEN("hidden", 1, (String)ItemAttributeModifiers.Display.Hidden.CODEC, ItemAttributeModifiers.Display.Hidden.STREAM_CODEC),
/* 131 */     OVERRIDE("override", 2, (String)ItemAttributeModifiers.Display.OverrideText.CODEC, ItemAttributeModifiers.Display.OverrideText.STREAM_CODEC);
/*     */     
/* 134 */     private static final Codec<Type> CODEC = (Codec<Type>)StringRepresentable.fromEnum(Type::values);
/*     */     
/* 136 */     private static final IntFunction<Type> BY_ID = ByIdMap.continuous(Type::id, (Object[])values(), ByIdMap.OutOfBoundsStrategy.ZERO);
/*     */     
/* 137 */     private static final StreamCodec<ByteBuf, Type> STREAM_CODEC = ByteBufCodecs.idMapper(BY_ID, Type::id);
/*     */     
/*     */     private final String name;
/*     */     
/*     */     private final int id;
/*     */     
/*     */     private final MapCodec<? extends ItemAttributeModifiers.Display> codec;
/*     */     
/*     */     private final StreamCodec<RegistryFriendlyByteBuf, ? extends ItemAttributeModifiers.Display> streamCodec;
/*     */     
/*     */     static {
/*     */     
/*     */     }
/*     */     
/*     */     Type(String name, int id, MapCodec<? extends ItemAttributeModifiers.Display> codec, StreamCodec<RegistryFriendlyByteBuf, ? extends ItemAttributeModifiers.Display> streamCodec) {
/* 145 */       this.name = name;
/* 146 */       this.id = id;
/* 147 */       this.codec = codec;
/* 148 */       this.streamCodec = streamCodec;
/*     */     }
/*     */     
/*     */     public String getSerializedName() {
/* 153 */       return this.name;
/*     */     }
/*     */     
/*     */     private int id() {
/* 157 */       return this.id;
/*     */     }
/*     */     
/*     */     private StreamCodec<RegistryFriendlyByteBuf, ? extends ItemAttributeModifiers.Display> streamCodec() {
/* 161 */       return this.streamCodec;
/*     */     }
/*     */   }
/*     */   
/*     */   public static final class Default extends Record implements Display {
/*     */     public final String toString() {
/*     */       // Byte code:
/*     */       //   0: aload_0
/*     */       //   1: <illegal opcode> toString : (Lnet/minecraft/world/item/component/ItemAttributeModifiers$Display$Default;)Ljava/lang/String;
/*     */       //   6: areturn
/*     */       // Line number table:
/*     */       //   Java source line number -> byte code offset
/*     */       //   #165	-> 0
/*     */       // Local variable table:
/*     */       //   start	length	slot	name	descriptor
/*     */       //   0	7	0	this	Lnet/minecraft/world/item/component/ItemAttributeModifiers$Display$Default;
/*     */     }
/*     */     
/*     */     public final int hashCode() {
/*     */       // Byte code:
/*     */       //   0: aload_0
/*     */       //   1: <illegal opcode> hashCode : (Lnet/minecraft/world/item/component/ItemAttributeModifiers$Display$Default;)I
/*     */       //   6: ireturn
/*     */       // Line number table:
/*     */       //   Java source line number -> byte code offset
/*     */       //   #165	-> 0
/*     */       // Local variable table:
/*     */       //   start	length	slot	name	descriptor
/*     */       //   0	7	0	this	Lnet/minecraft/world/item/component/ItemAttributeModifiers$Display$Default;
/*     */     }
/*     */     
/*     */     public final boolean equals(Object o) {
/*     */       // Byte code:
/*     */       //   0: aload_0
/*     */       //   1: aload_1
/*     */       //   2: <illegal opcode> equals : (Lnet/minecraft/world/item/component/ItemAttributeModifiers$Display$Default;Ljava/lang/Object;)Z
/*     */       //   7: ireturn
/*     */       // Line number table:
/*     */       //   Java source line number -> byte code offset
/*     */       //   #165	-> 0
/*     */       // Local variable table:
/*     */       //   start	length	slot	name	descriptor
/*     */       //   0	8	0	this	Lnet/minecraft/world/item/component/ItemAttributeModifiers$Display$Default;
/*     */       //   0	8	1	o	Ljava/lang/Object;
/*     */     }
/*     */     
/* 166 */     private static final Default INSTANCE = new Default();
/*     */     
/* 168 */     private static final MapCodec<Default> CODEC = MapCodec.unit(INSTANCE);
/*     */     
/* 169 */     private static final StreamCodec<RegistryFriendlyByteBuf, Default> STREAM_CODEC = StreamCodec.unit(INSTANCE);
/*     */     
/*     */     public ItemAttributeModifiers.Display.Type type() {
/* 173 */       return ItemAttributeModifiers.Display.Type.DEFAULT;
/*     */     }
/*     */     
/*     */     public void apply(Consumer<Component> consumer, Player player, Holder<Attribute> attribute, AttributeModifier modifier) {
/* 179 */       double displayAmount, amount = modifier.amount();
/* 180 */       boolean displayWithBase = false;
/* 182 */       if (player != null)
/* 183 */         if (modifier.is(Item.BASE_ATTACK_DAMAGE_ID)) {
/* 184 */           amount += player.getAttributeBaseValue(Attributes.ATTACK_DAMAGE);
/* 185 */           displayWithBase = true;
/* 186 */         } else if (modifier.is(Item.BASE_ATTACK_SPEED_ID)) {
/* 187 */           amount += player.getAttributeBaseValue(Attributes.ATTACK_SPEED);
/* 188 */           displayWithBase = true;
/*     */         }  
/* 193 */       if (modifier.operation() == AttributeModifier.Operation.ADD_MULTIPLIED_BASE || modifier.operation() == AttributeModifier.Operation.ADD_MULTIPLIED_TOTAL) {
/* 194 */         displayAmount = amount * 100.0D;
/* 195 */       } else if (attribute.is(Attributes.KNOCKBACK_RESISTANCE)) {
/* 196 */         displayAmount = amount * 10.0D;
/*     */       } else {
/* 198 */         displayAmount = amount;
/*     */       } 
/* 201 */       if (displayWithBase) {
/* 202 */         consumer.accept(
/* 203 */             CommonComponents.space().append(
/* 204 */               (Component)Component.translatable("attribute.modifier.equals." + modifier.operation().id(), new Object[] { ItemAttributeModifiers.ATTRIBUTE_MODIFIER_FORMAT
/* 205 */                   .format(displayAmount), 
/* 206 */                   Component.translatable(((Attribute)attribute.value()).getDescriptionId()) })).withStyle(ChatFormatting.DARK_GREEN));
/* 210 */       } else if (amount > 0.0D) {
/* 211 */         consumer.accept(Component.translatable("attribute.modifier.plus." + modifier.operation().id(), new Object[] { ItemAttributeModifiers.ATTRIBUTE_MODIFIER_FORMAT
/* 212 */                 .format(displayAmount), 
/* 213 */                 Component.translatable(((Attribute)attribute.value()).getDescriptionId()) }).withStyle(((Attribute)attribute.value()).getStyle(true)));
/* 215 */       } else if (amount < 0.0D) {
/* 216 */         consumer.accept(Component.translatable("attribute.modifier.take." + modifier.operation().id(), new Object[] { ItemAttributeModifiers.ATTRIBUTE_MODIFIER_FORMAT
/* 217 */                 .format(-displayAmount), 
/* 218 */                 Component.translatable(((Attribute)attribute.value()).getDescriptionId()) }).withStyle(((Attribute)attribute.value()).getStyle(false)));
/*     */       } 
/*     */     }
/*     */   }
/*     */   
/*     */   public static final class Hidden extends Record implements Display {
/*     */     public final String toString() {
/*     */       // Byte code:
/*     */       //   0: aload_0
/*     */       //   1: <illegal opcode> toString : (Lnet/minecraft/world/item/component/ItemAttributeModifiers$Display$Hidden;)Ljava/lang/String;
/*     */       //   6: areturn
/*     */       // Line number table:
/*     */       //   Java source line number -> byte code offset
/*     */       //   #224	-> 0
/*     */       // Local variable table:
/*     */       //   start	length	slot	name	descriptor
/*     */       //   0	7	0	this	Lnet/minecraft/world/item/component/ItemAttributeModifiers$Display$Hidden;
/*     */     }
/*     */     
/*     */     public final int hashCode() {
/*     */       // Byte code:
/*     */       //   0: aload_0
/*     */       //   1: <illegal opcode> hashCode : (Lnet/minecraft/world/item/component/ItemAttributeModifiers$Display$Hidden;)I
/*     */       //   6: ireturn
/*     */       // Line number table:
/*     */       //   Java source line number -> byte code offset
/*     */       //   #224	-> 0
/*     */       // Local variable table:
/*     */       //   start	length	slot	name	descriptor
/*     */       //   0	7	0	this	Lnet/minecraft/world/item/component/ItemAttributeModifiers$Display$Hidden;
/*     */     }
/*     */     
/*     */     public final boolean equals(Object o) {
/*     */       // Byte code:
/*     */       //   0: aload_0
/*     */       //   1: aload_1
/*     */       //   2: <illegal opcode> equals : (Lnet/minecraft/world/item/component/ItemAttributeModifiers$Display$Hidden;Ljava/lang/Object;)Z
/*     */       //   7: ireturn
/*     */       // Line number table:
/*     */       //   Java source line number -> byte code offset
/*     */       //   #224	-> 0
/*     */       // Local variable table:
/*     */       //   start	length	slot	name	descriptor
/*     */       //   0	8	0	this	Lnet/minecraft/world/item/component/ItemAttributeModifiers$Display$Hidden;
/*     */       //   0	8	1	o	Ljava/lang/Object;
/*     */     }
/*     */     
/* 225 */     private static final Hidden INSTANCE = new Hidden();
/*     */     
/* 227 */     private static final MapCodec<Hidden> CODEC = MapCodec.unit(INSTANCE);
/*     */     
/* 228 */     private static final StreamCodec<RegistryFriendlyByteBuf, Hidden> STREAM_CODEC = StreamCodec.unit(INSTANCE);
/*     */     
/*     */     public ItemAttributeModifiers.Display.Type type() {
/* 232 */       return ItemAttributeModifiers.Display.Type.HIDDEN;
/*     */     }
/*     */     
/*     */     public void apply(Consumer<Component> consumer, Player player, Holder<Attribute> attribute, AttributeModifier modifier) {}
/*     */   }
/*     */   
/*     */   public static final class OverrideText extends Record implements Display {
/*     */     private final Component component;
/*     */     
/*     */     private static final MapCodec<OverrideText> CODEC;
/*     */     
/*     */     public OverrideText(Component component) {
/* 239 */       this.component = component;
/*     */     }
/*     */     
/*     */     public final String toString() {
/*     */       // Byte code:
/*     */       //   0: aload_0
/*     */       //   1: <illegal opcode> toString : (Lnet/minecraft/world/item/component/ItemAttributeModifiers$Display$OverrideText;)Ljava/lang/String;
/*     */       //   6: areturn
/*     */       // Line number table:
/*     */       //   Java source line number -> byte code offset
/*     */       //   #239	-> 0
/*     */       // Local variable table:
/*     */       //   start	length	slot	name	descriptor
/*     */       //   0	7	0	this	Lnet/minecraft/world/item/component/ItemAttributeModifiers$Display$OverrideText;
/*     */     }
/*     */     
/*     */     public final int hashCode() {
/*     */       // Byte code:
/*     */       //   0: aload_0
/*     */       //   1: <illegal opcode> hashCode : (Lnet/minecraft/world/item/component/ItemAttributeModifiers$Display$OverrideText;)I
/*     */       //   6: ireturn
/*     */       // Line number table:
/*     */       //   Java source line number -> byte code offset
/*     */       //   #239	-> 0
/*     */       // Local variable table:
/*     */       //   start	length	slot	name	descriptor
/*     */       //   0	7	0	this	Lnet/minecraft/world/item/component/ItemAttributeModifiers$Display$OverrideText;
/*     */     }
/*     */     
/*     */     public final boolean equals(Object o) {
/*     */       // Byte code:
/*     */       //   0: aload_0
/*     */       //   1: aload_1
/*     */       //   2: <illegal opcode> equals : (Lnet/minecraft/world/item/component/ItemAttributeModifiers$Display$OverrideText;Ljava/lang/Object;)Z
/*     */       //   7: ireturn
/*     */       // Line number table:
/*     */       //   Java source line number -> byte code offset
/*     */       //   #239	-> 0
/*     */       // Local variable table:
/*     */       //   start	length	slot	name	descriptor
/*     */       //   0	8	0	this	Lnet/minecraft/world/item/component/ItemAttributeModifiers$Display$OverrideText;
/*     */       //   0	8	1	o	Ljava/lang/Object;
/*     */     }
/*     */     
/*     */     public Component component() {
/* 239 */       return this.component;
/*     */     }
/*     */     
/*     */     static {
/* 240 */       CODEC = RecordCodecBuilder.mapCodec(i -> i.group((App)ComponentSerialization.CODEC.fieldOf("value").forGetter(OverrideText::component)).apply((Applicative)i, OverrideText::new));
/*     */     }
/*     */     
/* 244 */     private static final StreamCodec<RegistryFriendlyByteBuf, OverrideText> STREAM_CODEC = StreamCodec.composite(ComponentSerialization.STREAM_CODEC, OverrideText::component, OverrideText::new);
/*     */     
/*     */     public ItemAttributeModifiers.Display.Type type() {
/* 251 */       return ItemAttributeModifiers.Display.Type.OVERRIDE;
/*     */     }
/*     */     
/*     */     public void apply(Consumer<Component> consumer, Player player, Holder<Attribute> attribute, AttributeModifier modifier) {
/* 256 */       consumer.accept(this.component);
/*     */     }
/*     */   }
/*     */ }


/* Location:              C:\Users\karol\AppData\Roaming\ModrinthApp\meta\versions\26.2-0.19.5\26.2-0.19.5.jar!\net\minecraft\world\item\component\ItemAttributeModifiers$Display.class
 * Java compiler version: 25 (69.0)
 * JD-Core Version:       1.1.3
 */