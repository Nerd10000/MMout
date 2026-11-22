/*     */ package net.minecraft.world.item.alchemy;
/*     */ 
/*     */ import com.google.common.base.Function;
/*     */ import com.google.common.collect.Iterables;
/*     */ import com.google.common.collect.Lists;
/*     */ import com.mojang.datafixers.kinds.App;
/*     */ import com.mojang.datafixers.kinds.Applicative;
/*     */ import com.mojang.datafixers.util.Function4;
/*     */ import com.mojang.datafixers.util.Pair;
/*     */ import com.mojang.serialization.Codec;
/*     */ import com.mojang.serialization.codecs.RecordCodecBuilder;
/*     */ import java.util.List;
/*     */ import java.util.Optional;
/*     */ import java.util.OptionalInt;
/*     */ import java.util.function.Consumer;
/*     */ import java.util.function.Function;
/*     */ import net.minecraft.ChatFormatting;
/*     */ import net.minecraft.core.Holder;
/*     */ import net.minecraft.core.component.DataComponentGetter;
/*     */ import net.minecraft.core.component.DataComponents;
/*     */ import net.minecraft.network.RegistryFriendlyByteBuf;
/*     */ import net.minecraft.network.chat.CommonComponents;
/*     */ import net.minecraft.network.chat.Component;
/*     */ import net.minecraft.network.chat.MutableComponent;
/*     */ import net.minecraft.network.codec.ByteBufCodecs;
/*     */ import net.minecraft.network.codec.StreamCodec;
/*     */ import net.minecraft.server.level.ServerLevel;
/*     */ import net.minecraft.util.ARGB;
/*     */ import net.minecraft.util.Util;
/*     */ import net.minecraft.world.effect.MobEffect;
/*     */ import net.minecraft.world.effect.MobEffectInstance;
/*     */ import net.minecraft.world.effect.MobEffectUtil;
/*     */ import net.minecraft.world.entity.Entity;
/*     */ import net.minecraft.world.entity.LivingEntity;
/*     */ import net.minecraft.world.entity.ai.attributes.Attribute;
/*     */ import net.minecraft.world.entity.ai.attributes.AttributeModifier;
/*     */ import net.minecraft.world.entity.player.Player;
/*     */ import net.minecraft.world.item.Item;
/*     */ import net.minecraft.world.item.ItemStack;
/*     */ import net.minecraft.world.item.TooltipFlag;
/*     */ import net.minecraft.world.item.component.Consumable;
/*     */ import net.minecraft.world.item.component.ConsumableListener;
/*     */ import net.minecraft.world.item.component.ItemAttributeModifiers;
/*     */ import net.minecraft.world.item.component.TooltipProvider;
/*     */ import net.minecraft.world.level.ItemLike;
/*     */ import net.minecraft.world.level.Level;
/*     */ 
/*     */ public final class PotionContents extends Record implements ConsumableListener, TooltipProvider {
/*     */   private final Optional<Holder<Potion>> potion;
/*     */   
/*     */   private final Optional<Integer> customColor;
/*     */   
/*     */   private final List<MobEffectInstance> customEffects;
/*     */   
/*     */   private final Optional<String> customName;
/*     */   
/*     */   public PotionContents(Optional<Holder<Potion>> potion, Optional<Integer> customColor, List<MobEffectInstance> customEffects, Optional<String> customName) {
/*  44 */     this.potion = potion;
/*  44 */     this.customColor = customColor;
/*  44 */     this.customEffects = customEffects;
/*  44 */     this.customName = customName;
/*     */   }
/*     */   
/*     */   public final String toString() {
/*     */     // Byte code:
/*     */     //   0: aload_0
/*     */     //   1: <illegal opcode> toString : (Lnet/minecraft/world/item/alchemy/PotionContents;)Ljava/lang/String;
/*     */     //   6: areturn
/*     */     // Line number table:
/*     */     //   Java source line number -> byte code offset
/*     */     //   #44	-> 0
/*     */     // Local variable table:
/*     */     //   start	length	slot	name	descriptor
/*     */     //   0	7	0	this	Lnet/minecraft/world/item/alchemy/PotionContents;
/*     */   }
/*     */   
/*     */   public final int hashCode() {
/*     */     // Byte code:
/*     */     //   0: aload_0
/*     */     //   1: <illegal opcode> hashCode : (Lnet/minecraft/world/item/alchemy/PotionContents;)I
/*     */     //   6: ireturn
/*     */     // Line number table:
/*     */     //   Java source line number -> byte code offset
/*     */     //   #44	-> 0
/*     */     // Local variable table:
/*     */     //   start	length	slot	name	descriptor
/*     */     //   0	7	0	this	Lnet/minecraft/world/item/alchemy/PotionContents;
/*     */   }
/*     */   
/*     */   public final boolean equals(Object o) {
/*     */     // Byte code:
/*     */     //   0: aload_0
/*     */     //   1: aload_1
/*     */     //   2: <illegal opcode> equals : (Lnet/minecraft/world/item/alchemy/PotionContents;Ljava/lang/Object;)Z
/*     */     //   7: ireturn
/*     */     // Line number table:
/*     */     //   Java source line number -> byte code offset
/*     */     //   #44	-> 0
/*     */     // Local variable table:
/*     */     //   start	length	slot	name	descriptor
/*     */     //   0	8	0	this	Lnet/minecraft/world/item/alchemy/PotionContents;
/*     */     //   0	8	1	o	Ljava/lang/Object;
/*     */   }
/*     */   
/*     */   public Optional<Holder<Potion>> potion() {
/*  44 */     return this.potion;
/*     */   }
/*     */   
/*     */   public Optional<Integer> customColor() {
/*  44 */     return this.customColor;
/*     */   }
/*     */   
/*     */   public Optional<String> customName() {
/*  44 */     return this.customName;
/*     */   }
/*     */   
/*  50 */   public static final PotionContents EMPTY = new PotionContents(Optional.empty(), Optional.empty(), List.of(), Optional.empty());
/*     */   
/*  52 */   private static final Component NO_EFFECT = (Component)Component.translatable("effect.none").withStyle(ChatFormatting.GRAY);
/*     */   
/*     */   public static final int BASE_POTION_COLOR = -13083194;
/*     */   
/*     */   private static final Codec<PotionContents> FULL_CODEC;
/*     */   
/*     */   static {
/*  55 */     FULL_CODEC = RecordCodecBuilder.create(i -> i.group((App)Potion.CODEC.optionalFieldOf("potion").forGetter(PotionContents::potion), (App)Codec.INT.optionalFieldOf("custom_color").forGetter(PotionContents::customColor), (App)MobEffectInstance.CODEC.listOf().optionalFieldOf("custom_effects", List.of()).forGetter(PotionContents::customEffects), (App)Codec.STRING.optionalFieldOf("custom_name").forGetter(PotionContents::customName)).apply((Applicative)i, PotionContents::new));
/*     */   }
/*     */   
/*  62 */   public static final Codec<PotionContents> CODEC = Codec.withAlternative(FULL_CODEC, Potion.CODEC, PotionContents::new);
/*     */   
/*  68 */   public static final StreamCodec<RegistryFriendlyByteBuf, PotionContents> STREAM_CODEC = StreamCodec.composite(Potion.STREAM_CODEC
/*  69 */       .apply(ByteBufCodecs::optional), PotionContents::potion, ByteBufCodecs.INT
/*  70 */       .apply(ByteBufCodecs::optional), PotionContents::customColor, MobEffectInstance.STREAM_CODEC
/*  71 */       .apply(ByteBufCodecs.list()), PotionContents::customEffects, ByteBufCodecs.STRING_UTF8
/*  72 */       .apply(ByteBufCodecs::optional), PotionContents::customName, PotionContents::new);
/*     */   
/*     */   public PotionContents(Holder<Potion> potion) {
/*  77 */     this(Optional.of(potion), Optional.empty(), List.of(), Optional.empty());
/*     */   }
/*     */   
/*     */   public static ItemStack createItemStack(Item item, Holder<Potion> potion) {
/*  81 */     ItemStack itemStack = new ItemStack((ItemLike)item);
/*  82 */     itemStack.set(DataComponents.POTION_CONTENTS, new PotionContents(potion));
/*  83 */     return itemStack;
/*     */   }
/*     */   
/*     */   public boolean is(Holder<Potion> potion) {
/*  87 */     return (this.potion.isPresent() && ((Holder)this.potion.get()).is(potion) && this.customEffects.isEmpty());
/*     */   }
/*     */   
/*     */   public Iterable<MobEffectInstance> getAllEffects() {
/*  91 */     if (this.potion.isEmpty())
/*  92 */       return this.customEffects; 
/*  94 */     if (this.customEffects.isEmpty())
/*  95 */       return ((Potion)((Holder)this.potion.get()).value()).getEffects(); 
/*  97 */     return Iterables.concat(((Potion)((Holder)this.potion.get()).value()).getEffects(), this.customEffects);
/*     */   }
/*     */   
/*     */   public void forEachEffect(Consumer<MobEffectInstance> consumer, float durationScale) {
/* 101 */     if (this.potion.isPresent())
/* 102 */       for (MobEffectInstance effect : ((Potion)((Holder)this.potion.get()).value()).getEffects())
/* 103 */         consumer.accept(effect.withScaledDuration(durationScale));  
/* 106 */     for (MobEffectInstance effect : this.customEffects)
/* 107 */       consumer.accept(effect.withScaledDuration(durationScale)); 
/*     */   }
/*     */   
/*     */   public PotionContents withPotion(Holder<Potion> potion) {
/* 112 */     return new PotionContents(Optional.of(potion), this.customColor, this.customEffects, this.customName);
/*     */   }
/*     */   
/*     */   public PotionContents withEffectAdded(MobEffectInstance effect) {
/* 116 */     return new PotionContents(this.potion, this.customColor, Util.copyAndAdd(this.customEffects, effect), this.customName);
/*     */   }
/*     */   
/*     */   public int getColor() {
/* 120 */     return getColorOr(-13083194);
/*     */   }
/*     */   
/*     */   public int getColorOr(int defaultColor) {
/* 124 */     if (this.customColor.isPresent())
/* 125 */       return ((Integer)this.customColor.get()).intValue(); 
/* 127 */     return getColorOptional(getAllEffects()).orElse(defaultColor);
/*     */   }
/*     */   
/*     */   public Component getName(String prefix) {
/* 133 */     String suffix = this.customName.or(() -> this.potion.map(())).orElse("empty");
/* 135 */     return (Component)Component.translatable(prefix + prefix);
/*     */   }
/*     */   
/*     */   public static OptionalInt getColorOptional(Iterable<MobEffectInstance> effects) {
/* 139 */     int red = 0;
/* 140 */     int green = 0;
/* 141 */     int blue = 0;
/* 142 */     int totalWeight = 0;
/* 144 */     for (MobEffectInstance effect : effects) {
/* 145 */       if (!effect.isVisible())
/*     */         continue; 
/* 149 */       int color = ((MobEffect)effect.getEffect().value()).getColor();
/* 150 */       int amplifier = effect.getAmplifier() + 1;
/* 151 */       red += amplifier * ARGB.red(color);
/* 152 */       green += amplifier * ARGB.green(color);
/* 153 */       blue += amplifier * ARGB.blue(color);
/* 154 */       totalWeight += amplifier;
/*     */     } 
/* 157 */     if (totalWeight == 0)
/* 158 */       return OptionalInt.empty(); 
/* 161 */     return OptionalInt.of(ARGB.color(red / totalWeight, green / totalWeight, blue / totalWeight));
/*     */   }
/*     */   
/*     */   public boolean hasEffects() {
/* 169 */     if (!this.customEffects.isEmpty())
/* 170 */       return true; 
/* 172 */     return (this.potion.isPresent() && !((Potion)((Holder)this.potion.get()).value()).getEffects().isEmpty());
/*     */   }
/*     */   
/*     */   public List<MobEffectInstance> customEffects() {
/* 177 */     return Lists.transform(this.customEffects, MobEffectInstance::new);
/*     */   }
/*     */   
/*     */   public void applyToLivingEntity(LivingEntity entity, float durationScale) {
/*     */     ServerLevel serverLevel;
/* 181 */     Level level = entity.level();
/* 181 */     if (level instanceof ServerLevel) {
/* 181 */       serverLevel = (ServerLevel)level;
/*     */     } else {
/*     */       return;
/*     */     } 
/* 185 */     Player playerEntity = (Player)entity, player = (entity instanceof Player) ? playerEntity : null;
/* 186 */     forEachEffect(effect -> {
/*     */           if (((MobEffect)effect.getEffect().value()).isInstantaneous()) {
/*     */             ((MobEffect)effect.getEffect().value()).applyInstantaneousEffect(serverLevel, (Entity)player, (Entity)player, entity, effect.getAmplifier(), 1.0D);
/*     */           } else {
/*     */             entity.addEffect(effect);
/*     */           } 
/* 186 */         }durationScale);
/*     */   }
/*     */   
/*     */   public static void addPotionTooltip(Iterable<MobEffectInstance> effects, Consumer<Component> lines, float durationScale, float tickrate) {
/* 196 */     List<Pair<Holder<Attribute>, AttributeModifier>> modifiers = Lists.newArrayList();
/* 198 */     boolean noEffects = true;
/* 199 */     for (MobEffectInstance effect : effects) {
/* 200 */       noEffects = false;
/* 202 */       Holder<MobEffect> mobEffect = effect.getEffect();
/* 203 */       int amplifier = effect.getAmplifier();
/* 204 */       ((MobEffect)mobEffect.value()).createModifiers(amplifier, (attribute, modifier) -> modifiers.add(new Pair(attribute, modifier)));
/* 205 */       MutableComponent line = getPotionDescription(mobEffect, amplifier);
/* 206 */       if (!effect.endsWithin(20))
/* 207 */         line = Component.translatable("potion.withDuration", new Object[] { line, MobEffectUtil.formatDuration(effect, durationScale, tickrate) }); 
/* 210 */       lines.accept(line.withStyle(((MobEffect)mobEffect.value()).getCategory().getTooltipFormatting()));
/*     */     } 
/* 213 */     if (noEffects)
/* 214 */       lines.accept(NO_EFFECT); 
/* 217 */     if (!modifiers.isEmpty()) {
/* 218 */       lines.accept(CommonComponents.EMPTY);
/* 219 */       lines.accept(Component.translatable("potion.whenDrank").withStyle(ChatFormatting.DARK_PURPLE));
/* 221 */       for (Pair<Holder<Attribute>, AttributeModifier> entry : modifiers) {
/*     */         double displayAmount;
/* 222 */         AttributeModifier modifier = (AttributeModifier)entry.getSecond();
/* 223 */         double amount = modifier.amount();
/* 226 */         if (modifier.operation() == AttributeModifier.Operation.ADD_MULTIPLIED_BASE || modifier.operation() == AttributeModifier.Operation.ADD_MULTIPLIED_TOTAL) {
/* 227 */           displayAmount = modifier.amount() * 100.0D;
/*     */         } else {
/* 229 */           displayAmount = modifier.amount();
/*     */         } 
/* 232 */         if (amount > 0.0D) {
/* 233 */           lines.accept(
/* 234 */               Component.translatable("attribute.modifier.plus." + modifier.operation().id(), new Object[] { ItemAttributeModifiers.ATTRIBUTE_MODIFIER_FORMAT
/* 235 */                   .format(displayAmount), 
/* 236 */                   Component.translatable(((Attribute)((Holder)entry.getFirst()).value()).getDescriptionId()) }).withStyle(ChatFormatting.BLUE));
/*     */           continue;
/*     */         } 
/* 239 */         if (amount < 0.0D) {
/* 240 */           displayAmount *= -1.0D;
/* 241 */           lines.accept(
/* 242 */               Component.translatable("attribute.modifier.take." + modifier.operation().id(), new Object[] { ItemAttributeModifiers.ATTRIBUTE_MODIFIER_FORMAT
/* 243 */                   .format(displayAmount), 
/* 244 */                   Component.translatable(((Attribute)((Holder)entry.getFirst()).value()).getDescriptionId()) }).withStyle(ChatFormatting.RED));
/*     */         } 
/*     */       } 
/*     */     } 
/*     */   }
/*     */   
/*     */   public static MutableComponent getPotionDescription(Holder<MobEffect> mobEffect, int amplifier) {
/* 253 */     MutableComponent line = Component.translatable(((MobEffect)mobEffect.value()).getDescriptionId());
/* 254 */     if (amplifier > 0)
/* 255 */       return Component.translatable("potion.withAmplifier", new Object[] { line, Component.translatable("potion.potency." + amplifier) }); 
/* 257 */     return line;
/*     */   }
/*     */   
/*     */   public void onConsume(Level level, LivingEntity user, ItemStack stack, Consumable consumable) {
/* 262 */     applyToLivingEntity(user, ((Float)stack.getOrDefault(DataComponents.POTION_DURATION_SCALE, Float.valueOf(1.0F))).floatValue());
/*     */   }
/*     */   
/*     */   public void addToTooltip(Item.TooltipContext context, Consumer<Component> consumer, TooltipFlag flag, DataComponentGetter components) {
/* 267 */     addPotionTooltip(getAllEffects(), consumer, ((Float)components.getOrDefault(DataComponents.POTION_DURATION_SCALE, Float.valueOf(1.0F))).floatValue(), context.tickRate());
/*     */   }
/*     */ }


/* Location:              C:\Users\karol\AppData\Roaming\ModrinthApp\meta\versions\26.2-0.19.5\26.2-0.19.5.jar!\net\minecraft\world\item\alchemy\PotionContents.class
 * Java compiler version: 25 (69.0)
 * JD-Core Version:       1.1.3
 */