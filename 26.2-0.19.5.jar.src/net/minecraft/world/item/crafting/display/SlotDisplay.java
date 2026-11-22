/*     */ package net.minecraft.world.item.crafting.display;
/*     */ 
/*     */ import com.mojang.datafixers.kinds.App;
/*     */ import com.mojang.datafixers.kinds.Applicative;
/*     */ import com.mojang.datafixers.util.Function3;
/*     */ import com.mojang.serialization.Codec;
/*     */ import com.mojang.serialization.MapCodec;
/*     */ import com.mojang.serialization.codecs.RecordCodecBuilder;
/*     */ import java.util.List;
/*     */ import java.util.Objects;
/*     */ import java.util.Optional;
/*     */ import java.util.function.BiFunction;
/*     */ import java.util.function.BinaryOperator;
/*     */ import java.util.function.Function;
/*     */ import java.util.stream.IntStream;
/*     */ import java.util.stream.Stream;
/*     */ import net.minecraft.core.Holder;
/*     */ import net.minecraft.core.HolderLookup;
/*     */ import net.minecraft.core.HolderSet;
/*     */ import net.minecraft.core.component.DataComponentType;
/*     */ import net.minecraft.core.component.DataComponents;
/*     */ import net.minecraft.core.registries.BuiltInRegistries;
/*     */ import net.minecraft.core.registries.Registries;
/*     */ import net.minecraft.network.RegistryFriendlyByteBuf;
/*     */ import net.minecraft.network.codec.ByteBufCodecs;
/*     */ import net.minecraft.network.codec.StreamCodec;
/*     */ import net.minecraft.tags.TagKey;
/*     */ import net.minecraft.util.RandomSource;
/*     */ import net.minecraft.util.Util;
/*     */ import net.minecraft.util.context.ContextMap;
/*     */ import net.minecraft.world.flag.FeatureFlagSet;
/*     */ import net.minecraft.world.item.DyeColor;
/*     */ import net.minecraft.world.item.Item;
/*     */ import net.minecraft.world.item.ItemStack;
/*     */ import net.minecraft.world.item.ItemStackTemplate;
/*     */ import net.minecraft.world.item.alchemy.Potion;
/*     */ import net.minecraft.world.item.alchemy.PotionContents;
/*     */ import net.minecraft.world.item.component.DyedItemColor;
/*     */ import net.minecraft.world.item.crafting.SmithingTrimRecipe;
/*     */ import net.minecraft.world.item.equipment.trim.TrimPattern;
/*     */ import net.minecraft.world.level.block.entity.FuelValues;
/*     */ 
/*     */ public interface SlotDisplay {
/*  41 */   public static final Codec<SlotDisplay> CODEC = BuiltInRegistries.SLOT_DISPLAY.byNameCodec().dispatch(SlotDisplay::type, Type::codec);
/*     */   
/*  42 */   public static final StreamCodec<RegistryFriendlyByteBuf, SlotDisplay> STREAM_CODEC = ByteBufCodecs.registry(Registries.SLOT_DISPLAY).dispatch(SlotDisplay::type, Type::streamCodec);
/*     */   
/*     */   default boolean isEnabled(FeatureFlagSet enabledFeatures) {
/*  50 */     return true;
/*     */   }
/*     */   
/*     */   public static final class Type<T extends SlotDisplay> extends Record {
/*     */     private final MapCodec<T> codec;
/*     */     
/*     */     private final StreamCodec<RegistryFriendlyByteBuf, T> streamCodec;
/*     */     
/*     */     public Type(MapCodec<T> codec, StreamCodec<RegistryFriendlyByteBuf, T> streamCodec) {
/*  53 */       this.codec = codec;
/*  53 */       this.streamCodec = streamCodec;
/*     */     }
/*     */     
/*     */     public final String toString() {
/*     */       // Byte code:
/*     */       //   0: aload_0
/*     */       //   1: <illegal opcode> toString : (Lnet/minecraft/world/item/crafting/display/SlotDisplay$Type;)Ljava/lang/String;
/*     */       //   6: areturn
/*     */       // Line number table:
/*     */       //   Java source line number -> byte code offset
/*     */       //   #53	-> 0
/*     */       // Local variable table:
/*     */       //   start	length	slot	name	descriptor
/*     */       //   0	7	0	this	Lnet/minecraft/world/item/crafting/display/SlotDisplay$Type;
/*     */       // Local variable type table:
/*     */       //   start	length	slot	name	signature
/*     */       //   0	7	0	this	Lnet/minecraft/world/item/crafting/display/SlotDisplay$Type<TT;>;
/*     */     }
/*     */     
/*     */     public final int hashCode() {
/*     */       // Byte code:
/*     */       //   0: aload_0
/*     */       //   1: <illegal opcode> hashCode : (Lnet/minecraft/world/item/crafting/display/SlotDisplay$Type;)I
/*     */       //   6: ireturn
/*     */       // Line number table:
/*     */       //   Java source line number -> byte code offset
/*     */       //   #53	-> 0
/*     */       // Local variable table:
/*     */       //   start	length	slot	name	descriptor
/*     */       //   0	7	0	this	Lnet/minecraft/world/item/crafting/display/SlotDisplay$Type;
/*     */       // Local variable type table:
/*     */       //   start	length	slot	name	signature
/*     */       //   0	7	0	this	Lnet/minecraft/world/item/crafting/display/SlotDisplay$Type<TT;>;
/*     */     }
/*     */     
/*     */     public final boolean equals(Object o) {
/*     */       // Byte code:
/*     */       //   0: aload_0
/*     */       //   1: aload_1
/*     */       //   2: <illegal opcode> equals : (Lnet/minecraft/world/item/crafting/display/SlotDisplay$Type;Ljava/lang/Object;)Z
/*     */       //   7: ireturn
/*     */       // Line number table:
/*     */       //   Java source line number -> byte code offset
/*     */       //   #53	-> 0
/*     */       // Local variable table:
/*     */       //   start	length	slot	name	descriptor
/*     */       //   0	8	0	this	Lnet/minecraft/world/item/crafting/display/SlotDisplay$Type;
/*     */       //   0	8	1	o	Ljava/lang/Object;
/*     */       // Local variable type table:
/*     */       //   start	length	slot	name	signature
/*     */       //   0	8	0	this	Lnet/minecraft/world/item/crafting/display/SlotDisplay$Type<TT;>;
/*     */     }
/*     */     
/*     */     public MapCodec<T> codec() {
/*  53 */       return this.codec;
/*     */     }
/*     */     
/*     */     public StreamCodec<RegistryFriendlyByteBuf, T> streamCodec() {
/*  53 */       return this.streamCodec;
/*     */     }
/*     */   }
/*     */   
/*     */   public static class ItemStackContentsFactory implements DisplayContentsFactory.ForStacks<ItemStack> {
/*  60 */     public static final ItemStackContentsFactory INSTANCE = new ItemStackContentsFactory();
/*     */     
/*     */     public ItemStack forStack(ItemStack stack) {
/*  64 */       return stack;
/*     */     }
/*     */   }
/*     */   
/*     */   default List<ItemStack> resolveForStacks(ContextMap context) {
/*  69 */     return resolve(context, ItemStackContentsFactory.INSTANCE).toList();
/*     */   }
/*     */   
/*     */   default ItemStack resolveForFirstStack(ContextMap context) {
/*  73 */     return resolve(context, ItemStackContentsFactory.INSTANCE).findFirst().orElse(ItemStack.EMPTY);
/*     */   }
/*     */   
/*     */   public static class Empty implements SlotDisplay {
/*  77 */     public static final Empty INSTANCE = new Empty();
/*     */     
/*  79 */     public static final MapCodec<Empty> MAP_CODEC = MapCodec.unit(INSTANCE);
/*     */     
/*  81 */     public static final StreamCodec<RegistryFriendlyByteBuf, Empty> STREAM_CODEC = StreamCodec.unit(INSTANCE);
/*     */     
/*  83 */     public static final SlotDisplay.Type<Empty> TYPE = new SlotDisplay.Type<>(MAP_CODEC, STREAM_CODEC);
/*     */     
/*     */     public SlotDisplay.Type<Empty> type() {
/*  90 */       return TYPE;
/*     */     }
/*     */     
/*     */     public String toString() {
/*  95 */       return "<empty>";
/*     */     }
/*     */     
/*     */     public <T> Stream<T> resolve(ContextMap context, DisplayContentsFactory<T> factory) {
/* 100 */       return Stream.empty();
/*     */     }
/*     */   }
/*     */   
/*     */   public static class AnyFuel implements SlotDisplay {
/* 105 */     public static final AnyFuel INSTANCE = new AnyFuel();
/*     */     
/* 107 */     public static final MapCodec<AnyFuel> MAP_CODEC = MapCodec.unit(INSTANCE);
/*     */     
/* 109 */     public static final StreamCodec<RegistryFriendlyByteBuf, AnyFuel> STREAM_CODEC = StreamCodec.unit(INSTANCE);
/*     */     
/* 111 */     public static final SlotDisplay.Type<AnyFuel> TYPE = new SlotDisplay.Type<>(MAP_CODEC, STREAM_CODEC);
/*     */     
/*     */     public SlotDisplay.Type<AnyFuel> type() {
/* 118 */       return TYPE;
/*     */     }
/*     */     
/*     */     public String toString() {
/* 123 */       return "<any fuel>";
/*     */     }
/*     */     
/*     */     public <T> Stream<T> resolve(ContextMap context, DisplayContentsFactory<T> factory) {
/* 128 */       if (factory instanceof DisplayContentsFactory.ForStacks) {
/* 128 */         DisplayContentsFactory.ForStacks<T> stacks = (DisplayContentsFactory.ForStacks<T>)factory;
/* 129 */         FuelValues fuelValues = (FuelValues)context.getOptional(SlotDisplayContext.FUEL_VALUES);
/* 130 */         if (fuelValues != null) {
/* 131 */           Objects.requireNonNull(stacks);
/* 131 */           return fuelValues.fuelItems().stream().map(stacks::forStack);
/*     */         } 
/*     */       } 
/* 134 */       return Stream.empty();
/*     */     }
/*     */   }
/*     */   
/*     */   public static final class WithAnyPotion extends Record implements SlotDisplay {
/*     */     private final SlotDisplay display;
/*     */     
/*     */     public static final MapCodec<WithAnyPotion> MAP_CODEC;
/*     */     
/*     */     public WithAnyPotion(SlotDisplay display) {
/* 138 */       this.display = display;
/*     */     }
/*     */     
/*     */     public final String toString() {
/*     */       // Byte code:
/*     */       //   0: aload_0
/*     */       //   1: <illegal opcode> toString : (Lnet/minecraft/world/item/crafting/display/SlotDisplay$WithAnyPotion;)Ljava/lang/String;
/*     */       //   6: areturn
/*     */       // Line number table:
/*     */       //   Java source line number -> byte code offset
/*     */       //   #138	-> 0
/*     */       // Local variable table:
/*     */       //   start	length	slot	name	descriptor
/*     */       //   0	7	0	this	Lnet/minecraft/world/item/crafting/display/SlotDisplay$WithAnyPotion;
/*     */     }
/*     */     
/*     */     public final int hashCode() {
/*     */       // Byte code:
/*     */       //   0: aload_0
/*     */       //   1: <illegal opcode> hashCode : (Lnet/minecraft/world/item/crafting/display/SlotDisplay$WithAnyPotion;)I
/*     */       //   6: ireturn
/*     */       // Line number table:
/*     */       //   Java source line number -> byte code offset
/*     */       //   #138	-> 0
/*     */       // Local variable table:
/*     */       //   start	length	slot	name	descriptor
/*     */       //   0	7	0	this	Lnet/minecraft/world/item/crafting/display/SlotDisplay$WithAnyPotion;
/*     */     }
/*     */     
/*     */     public final boolean equals(Object o) {
/*     */       // Byte code:
/*     */       //   0: aload_0
/*     */       //   1: aload_1
/*     */       //   2: <illegal opcode> equals : (Lnet/minecraft/world/item/crafting/display/SlotDisplay$WithAnyPotion;Ljava/lang/Object;)Z
/*     */       //   7: ireturn
/*     */       // Line number table:
/*     */       //   Java source line number -> byte code offset
/*     */       //   #138	-> 0
/*     */       // Local variable table:
/*     */       //   start	length	slot	name	descriptor
/*     */       //   0	8	0	this	Lnet/minecraft/world/item/crafting/display/SlotDisplay$WithAnyPotion;
/*     */       //   0	8	1	o	Ljava/lang/Object;
/*     */     }
/*     */     
/*     */     public SlotDisplay display() {
/* 138 */       return this.display;
/*     */     }
/*     */     
/*     */     static {
/* 139 */       MAP_CODEC = RecordCodecBuilder.mapCodec(i -> i.group((App)SlotDisplay.CODEC.fieldOf("contents").forGetter(WithAnyPotion::display)).apply((Applicative)i, WithAnyPotion::new));
/*     */     }
/*     */     
/* 143 */     public static final StreamCodec<RegistryFriendlyByteBuf, WithAnyPotion> STREAM_CODEC = StreamCodec.composite(SlotDisplay.STREAM_CODEC, WithAnyPotion::display, WithAnyPotion::new);
/*     */     
/* 148 */     public static final SlotDisplay.Type<WithAnyPotion> TYPE = new SlotDisplay.Type<>(MAP_CODEC, STREAM_CODEC);
/*     */     
/*     */     public SlotDisplay.Type<WithAnyPotion> type() {
/* 152 */       return TYPE;
/*     */     }
/*     */     
/*     */     public <T> Stream<T> resolve(ContextMap context, DisplayContentsFactory<T> factory) {
/* 157 */       if (factory instanceof DisplayContentsFactory.ForStacks) {
/* 157 */         DisplayContentsFactory.ForStacks<T> stacks = (DisplayContentsFactory.ForStacks<T>)factory;
/* 158 */         List<ItemStack> displayItems = this.display.resolveForStacks(context);
/* 161 */         Optional<? extends HolderLookup.RegistryLookup<Potion>> potions = Optional.<HolderLookup.Provider>ofNullable((HolderLookup.Provider)context.getOptional(SlotDisplayContext.REGISTRIES)).flatMap(r -> r.lookup(Registries.POTION));
/* 162 */         return potions
/* 163 */           .stream()
/* 164 */           .flatMap(HolderLookup::listElements)
/* 165 */           .flatMap(potion -> {
/*     */               PotionContents potionContents = new PotionContents((Holder)potion);
/*     */               return displayItems.stream().map(());
/*     */             });
/*     */       } 
/* 174 */       return Stream.empty();
/*     */     }
/*     */   }
/*     */   
/*     */   public static final class OnlyWithComponent extends Record implements SlotDisplay {
/*     */     private final SlotDisplay source;
/*     */     
/*     */     private final DataComponentType<?> component;
/*     */     
/*     */     public static final MapCodec<OnlyWithComponent> MAP_CODEC;
/*     */     
/*     */     public OnlyWithComponent(SlotDisplay source, DataComponentType<?> component) {
/* 178 */       this.source = source;
/* 178 */       this.component = component;
/*     */     }
/*     */     
/*     */     public final String toString() {
/*     */       // Byte code:
/*     */       //   0: aload_0
/*     */       //   1: <illegal opcode> toString : (Lnet/minecraft/world/item/crafting/display/SlotDisplay$OnlyWithComponent;)Ljava/lang/String;
/*     */       //   6: areturn
/*     */       // Line number table:
/*     */       //   Java source line number -> byte code offset
/*     */       //   #178	-> 0
/*     */       // Local variable table:
/*     */       //   start	length	slot	name	descriptor
/*     */       //   0	7	0	this	Lnet/minecraft/world/item/crafting/display/SlotDisplay$OnlyWithComponent;
/*     */     }
/*     */     
/*     */     public final int hashCode() {
/*     */       // Byte code:
/*     */       //   0: aload_0
/*     */       //   1: <illegal opcode> hashCode : (Lnet/minecraft/world/item/crafting/display/SlotDisplay$OnlyWithComponent;)I
/*     */       //   6: ireturn
/*     */       // Line number table:
/*     */       //   Java source line number -> byte code offset
/*     */       //   #178	-> 0
/*     */       // Local variable table:
/*     */       //   start	length	slot	name	descriptor
/*     */       //   0	7	0	this	Lnet/minecraft/world/item/crafting/display/SlotDisplay$OnlyWithComponent;
/*     */     }
/*     */     
/*     */     public final boolean equals(Object o) {
/*     */       // Byte code:
/*     */       //   0: aload_0
/*     */       //   1: aload_1
/*     */       //   2: <illegal opcode> equals : (Lnet/minecraft/world/item/crafting/display/SlotDisplay$OnlyWithComponent;Ljava/lang/Object;)Z
/*     */       //   7: ireturn
/*     */       // Line number table:
/*     */       //   Java source line number -> byte code offset
/*     */       //   #178	-> 0
/*     */       // Local variable table:
/*     */       //   start	length	slot	name	descriptor
/*     */       //   0	8	0	this	Lnet/minecraft/world/item/crafting/display/SlotDisplay$OnlyWithComponent;
/*     */       //   0	8	1	o	Ljava/lang/Object;
/*     */     }
/*     */     
/*     */     public SlotDisplay source() {
/* 178 */       return this.source;
/*     */     }
/*     */     
/*     */     public DataComponentType<?> component() {
/* 178 */       return this.component;
/*     */     }
/*     */     
/*     */     static {
/* 179 */       MAP_CODEC = RecordCodecBuilder.mapCodec(i -> i.group((App)SlotDisplay.CODEC.fieldOf("contents").forGetter(OnlyWithComponent::source), (App)DataComponentType.CODEC.fieldOf("component").forGetter(OnlyWithComponent::component)).apply((Applicative)i, OnlyWithComponent::new));
/*     */     }
/*     */     
/* 184 */     public static final StreamCodec<RegistryFriendlyByteBuf, OnlyWithComponent> STREAM_CODEC = StreamCodec.composite(SlotDisplay.STREAM_CODEC, OnlyWithComponent::source, DataComponentType.STREAM_CODEC, OnlyWithComponent::component, OnlyWithComponent::new);
/*     */     
/* 190 */     public static final SlotDisplay.Type<OnlyWithComponent> TYPE = new SlotDisplay.Type<>(MAP_CODEC, STREAM_CODEC);
/*     */     
/*     */     public <T> Stream<T> resolve(ContextMap context, DisplayContentsFactory<T> builder) {
/* 194 */       if (builder instanceof DisplayContentsFactory.ForStacks) {
/* 194 */         DisplayContentsFactory.ForStacks<T> stacks = (DisplayContentsFactory.ForStacks<T>)builder;
/* 195 */         Objects.requireNonNull(stacks);
/* 195 */         return this.source.resolve(context, SlotDisplay.ItemStackContentsFactory.INSTANCE).filter(s -> s.has(this.component)).map(stacks::forStack);
/*     */       } 
/* 198 */       return Stream.empty();
/*     */     }
/*     */     
/*     */     public SlotDisplay.Type<OnlyWithComponent> type() {
/* 203 */       return TYPE;
/*     */     }
/*     */   }
/*     */   
/*     */   public static final class DyedSlotDemo extends Record implements SlotDisplay {
/*     */     private final SlotDisplay dye;
/*     */     
/*     */     private final SlotDisplay target;
/*     */     
/*     */     public static final MapCodec<DyedSlotDemo> MAP_CODEC;
/*     */     
/*     */     public DyedSlotDemo(SlotDisplay dye, SlotDisplay target) {
/* 207 */       this.dye = dye;
/* 207 */       this.target = target;
/*     */     }
/*     */     
/*     */     public final String toString() {
/*     */       // Byte code:
/*     */       //   0: aload_0
/*     */       //   1: <illegal opcode> toString : (Lnet/minecraft/world/item/crafting/display/SlotDisplay$DyedSlotDemo;)Ljava/lang/String;
/*     */       //   6: areturn
/*     */       // Line number table:
/*     */       //   Java source line number -> byte code offset
/*     */       //   #207	-> 0
/*     */       // Local variable table:
/*     */       //   start	length	slot	name	descriptor
/*     */       //   0	7	0	this	Lnet/minecraft/world/item/crafting/display/SlotDisplay$DyedSlotDemo;
/*     */     }
/*     */     
/*     */     public final int hashCode() {
/*     */       // Byte code:
/*     */       //   0: aload_0
/*     */       //   1: <illegal opcode> hashCode : (Lnet/minecraft/world/item/crafting/display/SlotDisplay$DyedSlotDemo;)I
/*     */       //   6: ireturn
/*     */       // Line number table:
/*     */       //   Java source line number -> byte code offset
/*     */       //   #207	-> 0
/*     */       // Local variable table:
/*     */       //   start	length	slot	name	descriptor
/*     */       //   0	7	0	this	Lnet/minecraft/world/item/crafting/display/SlotDisplay$DyedSlotDemo;
/*     */     }
/*     */     
/*     */     public final boolean equals(Object o) {
/*     */       // Byte code:
/*     */       //   0: aload_0
/*     */       //   1: aload_1
/*     */       //   2: <illegal opcode> equals : (Lnet/minecraft/world/item/crafting/display/SlotDisplay$DyedSlotDemo;Ljava/lang/Object;)Z
/*     */       //   7: ireturn
/*     */       // Line number table:
/*     */       //   Java source line number -> byte code offset
/*     */       //   #207	-> 0
/*     */       // Local variable table:
/*     */       //   start	length	slot	name	descriptor
/*     */       //   0	8	0	this	Lnet/minecraft/world/item/crafting/display/SlotDisplay$DyedSlotDemo;
/*     */       //   0	8	1	o	Ljava/lang/Object;
/*     */     }
/*     */     
/*     */     public SlotDisplay dye() {
/* 207 */       return this.dye;
/*     */     }
/*     */     
/*     */     public SlotDisplay target() {
/* 207 */       return this.target;
/*     */     }
/*     */     
/*     */     static {
/* 208 */       MAP_CODEC = RecordCodecBuilder.mapCodec(i -> i.group((App)SlotDisplay.CODEC.fieldOf("dye").forGetter(DyedSlotDemo::dye), (App)SlotDisplay.CODEC.fieldOf("target").forGetter(DyedSlotDemo::target)).apply((Applicative)i, DyedSlotDemo::new));
/*     */     }
/*     */     
/* 213 */     public static final StreamCodec<RegistryFriendlyByteBuf, DyedSlotDemo> STREAM_CODEC = StreamCodec.composite(SlotDisplay.STREAM_CODEC, DyedSlotDemo::dye, SlotDisplay.STREAM_CODEC, DyedSlotDemo::target, DyedSlotDemo::new);
/*     */     
/* 219 */     public static final SlotDisplay.Type<DyedSlotDemo> TYPE = new SlotDisplay.Type<>(MAP_CODEC, STREAM_CODEC);
/*     */     
/*     */     public SlotDisplay.Type<DyedSlotDemo> type() {
/* 223 */       return TYPE;
/*     */     }
/*     */     
/*     */     public <T> Stream<T> resolve(ContextMap context, DisplayContentsFactory<T> factory) {
/* 229 */       BinaryOperator<ItemStack> transformation = (target, dye) -> {
/*     */           DyeColor dyeValue = (DyeColor)dye.getOrDefault(DataComponents.DYE, DyeColor.WHITE);
/*     */           return DyedItemColor.applyDyes(target.copy(), List.of(dyeValue));
/*     */         };
/* 233 */       return SlotDisplay.applyDemoTransformation(context, factory, this.target, this.dye, transformation);
/*     */     }
/*     */   }
/*     */   
/*     */   public static final class SmithingTrimDemoSlotDisplay extends Record implements SlotDisplay {
/*     */     private final SlotDisplay base;
/*     */     
/*     */     private final SlotDisplay material;
/*     */     
/*     */     private final Holder<TrimPattern> pattern;
/*     */     
/*     */     public static final MapCodec<SmithingTrimDemoSlotDisplay> MAP_CODEC;
/*     */     
/*     */     public SmithingTrimDemoSlotDisplay(SlotDisplay base, SlotDisplay material, Holder<TrimPattern> pattern) {
/* 237 */       this.base = base;
/* 237 */       this.material = material;
/* 237 */       this.pattern = pattern;
/*     */     }
/*     */     
/*     */     public final String toString() {
/*     */       // Byte code:
/*     */       //   0: aload_0
/*     */       //   1: <illegal opcode> toString : (Lnet/minecraft/world/item/crafting/display/SlotDisplay$SmithingTrimDemoSlotDisplay;)Ljava/lang/String;
/*     */       //   6: areturn
/*     */       // Line number table:
/*     */       //   Java source line number -> byte code offset
/*     */       //   #237	-> 0
/*     */       // Local variable table:
/*     */       //   start	length	slot	name	descriptor
/*     */       //   0	7	0	this	Lnet/minecraft/world/item/crafting/display/SlotDisplay$SmithingTrimDemoSlotDisplay;
/*     */     }
/*     */     
/*     */     public final int hashCode() {
/*     */       // Byte code:
/*     */       //   0: aload_0
/*     */       //   1: <illegal opcode> hashCode : (Lnet/minecraft/world/item/crafting/display/SlotDisplay$SmithingTrimDemoSlotDisplay;)I
/*     */       //   6: ireturn
/*     */       // Line number table:
/*     */       //   Java source line number -> byte code offset
/*     */       //   #237	-> 0
/*     */       // Local variable table:
/*     */       //   start	length	slot	name	descriptor
/*     */       //   0	7	0	this	Lnet/minecraft/world/item/crafting/display/SlotDisplay$SmithingTrimDemoSlotDisplay;
/*     */     }
/*     */     
/*     */     public final boolean equals(Object o) {
/*     */       // Byte code:
/*     */       //   0: aload_0
/*     */       //   1: aload_1
/*     */       //   2: <illegal opcode> equals : (Lnet/minecraft/world/item/crafting/display/SlotDisplay$SmithingTrimDemoSlotDisplay;Ljava/lang/Object;)Z
/*     */       //   7: ireturn
/*     */       // Line number table:
/*     */       //   Java source line number -> byte code offset
/*     */       //   #237	-> 0
/*     */       // Local variable table:
/*     */       //   start	length	slot	name	descriptor
/*     */       //   0	8	0	this	Lnet/minecraft/world/item/crafting/display/SlotDisplay$SmithingTrimDemoSlotDisplay;
/*     */       //   0	8	1	o	Ljava/lang/Object;
/*     */     }
/*     */     
/*     */     public SlotDisplay base() {
/* 237 */       return this.base;
/*     */     }
/*     */     
/*     */     public SlotDisplay material() {
/* 237 */       return this.material;
/*     */     }
/*     */     
/*     */     public Holder<TrimPattern> pattern() {
/* 237 */       return this.pattern;
/*     */     }
/*     */     
/*     */     static {
/* 243 */       MAP_CODEC = RecordCodecBuilder.mapCodec(i -> i.group((App)SlotDisplay.CODEC.fieldOf("base").forGetter(SmithingTrimDemoSlotDisplay::base), (App)SlotDisplay.CODEC.fieldOf("material").forGetter(SmithingTrimDemoSlotDisplay::material), (App)TrimPattern.CODEC.fieldOf("pattern").forGetter(SmithingTrimDemoSlotDisplay::pattern)).apply((Applicative)i, SmithingTrimDemoSlotDisplay::new));
/*     */     }
/*     */     
/* 249 */     public static final StreamCodec<RegistryFriendlyByteBuf, SmithingTrimDemoSlotDisplay> STREAM_CODEC = StreamCodec.composite(SlotDisplay.STREAM_CODEC, SmithingTrimDemoSlotDisplay::base, SlotDisplay.STREAM_CODEC, SmithingTrimDemoSlotDisplay::material, TrimPattern.STREAM_CODEC, SmithingTrimDemoSlotDisplay::pattern, SmithingTrimDemoSlotDisplay::new);
/*     */     
/* 256 */     public static final SlotDisplay.Type<SmithingTrimDemoSlotDisplay> TYPE = new SlotDisplay.Type<>(MAP_CODEC, STREAM_CODEC);
/*     */     
/*     */     public SlotDisplay.Type<SmithingTrimDemoSlotDisplay> type() {
/* 260 */       return TYPE;
/*     */     }
/*     */     
/*     */     public <T> Stream<T> resolve(ContextMap context, DisplayContentsFactory<T> factory) {
/* 266 */       RandomSource randomSource = RandomSource.createThreadLocalInstance(System.identityHashCode(this));
/* 267 */       BinaryOperator<ItemStack> transformation = (base, material) -> SmithingTrimRecipe.applyTrim(base, material, this.pattern);
/* 268 */       return SlotDisplay.applyDemoTransformation(context, factory, this.base, this.material, randomSource, transformation);
/*     */     }
/*     */   }
/*     */   
/*     */   private static <T> Stream<T> applyDemoTransformation(ContextMap context, DisplayContentsFactory<T> factory, SlotDisplay firstDisplay, SlotDisplay secondDisplay, RandomSource randomSource, BinaryOperator<ItemStack> operation) {
/* 273 */     if (factory instanceof DisplayContentsFactory.ForStacks) {
/* 273 */       DisplayContentsFactory.ForStacks<T> stacks = (DisplayContentsFactory.ForStacks<T>)factory;
/* 274 */       List<ItemStack> firstItems = firstDisplay.resolveForStacks(context);
/* 275 */       if (firstItems.isEmpty())
/* 276 */         return Stream.empty(); 
/* 278 */       List<ItemStack> secondItems = secondDisplay.resolveForStacks(context);
/* 279 */       if (secondItems.isEmpty())
/* 280 */         return Stream.empty(); 
/* 289 */       Objects.requireNonNull(stacks);
/*     */       return Stream.generate(() -> {
/*     */             ItemStack first = (ItemStack)Util.getRandom(firstItems, randomSource);
/*     */             ItemStack second = (ItemStack)Util.getRandom(secondItems, randomSource);
/*     */             return operation.apply(first, second);
/* 289 */           }).limit(256L).filter(s -> !s.isEmpty()).limit(16L).map(stacks::forStack);
/*     */     } 
/* 291 */     return Stream.empty();
/*     */   }
/*     */   
/*     */   private static <T> Stream<T> applyDemoTransformation(ContextMap context, DisplayContentsFactory<T> factory, SlotDisplay firstDisplay, SlotDisplay secondDisplay, BinaryOperator<ItemStack> operation) {
/* 295 */     if (factory instanceof DisplayContentsFactory.ForStacks) {
/* 295 */       DisplayContentsFactory.ForStacks<T> stacks = (DisplayContentsFactory.ForStacks<T>)factory;
/* 296 */       List<ItemStack> firstItems = firstDisplay.resolveForStacks(context);
/* 297 */       if (firstItems.isEmpty())
/* 298 */         return Stream.empty(); 
/* 300 */       List<ItemStack> secondItems = secondDisplay.resolveForStacks(context);
/* 301 */       if (secondItems.isEmpty())
/* 302 */         return Stream.empty(); 
/* 305 */       int cycle = firstItems.size() * secondItems.size();
/* 314 */       Objects.requireNonNull(stacks);
/*     */       return IntStream.range(0, cycle).mapToObj(index -> {
/*     */             int firstItemCount = firstItems.size();
/*     */             int firstItemIndex = index % firstItemCount;
/*     */             int secondItemIndex = index / firstItemCount;
/*     */             ItemStack first = firstItems.get(firstItemIndex);
/*     */             ItemStack second = secondItems.get(secondItemIndex);
/*     */             return operation.apply(first, second);
/* 314 */           }).filter(s -> !s.isEmpty()).limit(16L).map(stacks::forStack);
/*     */     } 
/* 316 */     return Stream.empty();
/*     */   }
/*     */   
/*     */   <T> Stream<T> resolve(ContextMap paramContextMap, DisplayContentsFactory<T> paramDisplayContentsFactory);
/*     */   
/*     */   Type<? extends SlotDisplay> type();
/*     */   
/*     */   public static final class ItemSlotDisplay extends Record implements SlotDisplay {
/*     */     private final Holder<Item> item;
/*     */     
/*     */     public static final MapCodec<ItemSlotDisplay> MAP_CODEC;
/*     */     
/*     */     public ItemSlotDisplay(Holder<Item> item) {
/* 319 */       this.item = item;
/*     */     }
/*     */     
/*     */     public final String toString() {
/*     */       // Byte code:
/*     */       //   0: aload_0
/*     */       //   1: <illegal opcode> toString : (Lnet/minecraft/world/item/crafting/display/SlotDisplay$ItemSlotDisplay;)Ljava/lang/String;
/*     */       //   6: areturn
/*     */       // Line number table:
/*     */       //   Java source line number -> byte code offset
/*     */       //   #319	-> 0
/*     */       // Local variable table:
/*     */       //   start	length	slot	name	descriptor
/*     */       //   0	7	0	this	Lnet/minecraft/world/item/crafting/display/SlotDisplay$ItemSlotDisplay;
/*     */     }
/*     */     
/*     */     public final int hashCode() {
/*     */       // Byte code:
/*     */       //   0: aload_0
/*     */       //   1: <illegal opcode> hashCode : (Lnet/minecraft/world/item/crafting/display/SlotDisplay$ItemSlotDisplay;)I
/*     */       //   6: ireturn
/*     */       // Line number table:
/*     */       //   Java source line number -> byte code offset
/*     */       //   #319	-> 0
/*     */       // Local variable table:
/*     */       //   start	length	slot	name	descriptor
/*     */       //   0	7	0	this	Lnet/minecraft/world/item/crafting/display/SlotDisplay$ItemSlotDisplay;
/*     */     }
/*     */     
/*     */     public final boolean equals(Object o) {
/*     */       // Byte code:
/*     */       //   0: aload_0
/*     */       //   1: aload_1
/*     */       //   2: <illegal opcode> equals : (Lnet/minecraft/world/item/crafting/display/SlotDisplay$ItemSlotDisplay;Ljava/lang/Object;)Z
/*     */       //   7: ireturn
/*     */       // Line number table:
/*     */       //   Java source line number -> byte code offset
/*     */       //   #319	-> 0
/*     */       // Local variable table:
/*     */       //   start	length	slot	name	descriptor
/*     */       //   0	8	0	this	Lnet/minecraft/world/item/crafting/display/SlotDisplay$ItemSlotDisplay;
/*     */       //   0	8	1	o	Ljava/lang/Object;
/*     */     }
/*     */     
/*     */     public Holder<Item> item() {
/* 319 */       return this.item;
/*     */     }
/*     */     
/*     */     static {
/* 320 */       MAP_CODEC = RecordCodecBuilder.mapCodec(i -> i.group((App)Item.CODEC.fieldOf("item").forGetter(ItemSlotDisplay::item)).apply((Applicative)i, ItemSlotDisplay::new));
/*     */     }
/*     */     
/* 324 */     public static final StreamCodec<RegistryFriendlyByteBuf, ItemSlotDisplay> STREAM_CODEC = StreamCodec.composite(Item.STREAM_CODEC, ItemSlotDisplay::item, ItemSlotDisplay::new);
/*     */     
/* 329 */     public static final SlotDisplay.Type<ItemSlotDisplay> TYPE = new SlotDisplay.Type<>(MAP_CODEC, STREAM_CODEC);
/*     */     
/*     */     public SlotDisplay.Type<ItemSlotDisplay> type() {
/* 333 */       return TYPE;
/*     */     }
/*     */     
/*     */     public ItemSlotDisplay(Item item) {
/* 337 */       this((Holder<Item>)item.builtInRegistryHolder());
/*     */     }
/*     */     
/*     */     public <T> Stream<T> resolve(ContextMap context, DisplayContentsFactory<T> factory) {
/* 342 */       if (factory instanceof DisplayContentsFactory.ForStacks) {
/* 342 */         DisplayContentsFactory.ForStacks<T> stacks = (DisplayContentsFactory.ForStacks<T>)factory;
/* 343 */         return Stream.of(stacks.forStack(this.item));
/*     */       } 
/* 345 */       return Stream.empty();
/*     */     }
/*     */     
/*     */     public boolean isEnabled(FeatureFlagSet enabledFeatures) {
/* 350 */       return ((Item)this.item.value()).isEnabled(enabledFeatures);
/*     */     }
/*     */   }
/*     */   
/*     */   public static final class ItemStackSlotDisplay extends Record implements SlotDisplay {
/*     */     private final ItemStackTemplate stack;
/*     */     
/*     */     public static final MapCodec<ItemStackSlotDisplay> MAP_CODEC;
/*     */     
/*     */     public ItemStackSlotDisplay(ItemStackTemplate stack) {
/* 354 */       this.stack = stack;
/*     */     }
/*     */     
/*     */     public final String toString() {
/*     */       // Byte code:
/*     */       //   0: aload_0
/*     */       //   1: <illegal opcode> toString : (Lnet/minecraft/world/item/crafting/display/SlotDisplay$ItemStackSlotDisplay;)Ljava/lang/String;
/*     */       //   6: areturn
/*     */       // Line number table:
/*     */       //   Java source line number -> byte code offset
/*     */       //   #354	-> 0
/*     */       // Local variable table:
/*     */       //   start	length	slot	name	descriptor
/*     */       //   0	7	0	this	Lnet/minecraft/world/item/crafting/display/SlotDisplay$ItemStackSlotDisplay;
/*     */     }
/*     */     
/*     */     public final int hashCode() {
/*     */       // Byte code:
/*     */       //   0: aload_0
/*     */       //   1: <illegal opcode> hashCode : (Lnet/minecraft/world/item/crafting/display/SlotDisplay$ItemStackSlotDisplay;)I
/*     */       //   6: ireturn
/*     */       // Line number table:
/*     */       //   Java source line number -> byte code offset
/*     */       //   #354	-> 0
/*     */       // Local variable table:
/*     */       //   start	length	slot	name	descriptor
/*     */       //   0	7	0	this	Lnet/minecraft/world/item/crafting/display/SlotDisplay$ItemStackSlotDisplay;
/*     */     }
/*     */     
/*     */     public final boolean equals(Object o) {
/*     */       // Byte code:
/*     */       //   0: aload_0
/*     */       //   1: aload_1
/*     */       //   2: <illegal opcode> equals : (Lnet/minecraft/world/item/crafting/display/SlotDisplay$ItemStackSlotDisplay;Ljava/lang/Object;)Z
/*     */       //   7: ireturn
/*     */       // Line number table:
/*     */       //   Java source line number -> byte code offset
/*     */       //   #354	-> 0
/*     */       // Local variable table:
/*     */       //   start	length	slot	name	descriptor
/*     */       //   0	8	0	this	Lnet/minecraft/world/item/crafting/display/SlotDisplay$ItemStackSlotDisplay;
/*     */       //   0	8	1	o	Ljava/lang/Object;
/*     */     }
/*     */     
/*     */     public ItemStackTemplate stack() {
/* 354 */       return this.stack;
/*     */     }
/*     */     
/*     */     static {
/* 355 */       MAP_CODEC = RecordCodecBuilder.mapCodec(i -> i.group((App)ItemStackTemplate.CODEC.fieldOf("item").forGetter(ItemStackSlotDisplay::stack)).apply((Applicative)i, ItemStackSlotDisplay::new));
/*     */     }
/*     */     
/* 359 */     public static final StreamCodec<RegistryFriendlyByteBuf, ItemStackSlotDisplay> STREAM_CODEC = StreamCodec.composite(ItemStackTemplate.STREAM_CODEC, ItemStackSlotDisplay::stack, ItemStackSlotDisplay::new);
/*     */     
/* 364 */     public static final SlotDisplay.Type<ItemStackSlotDisplay> TYPE = new SlotDisplay.Type<>(MAP_CODEC, STREAM_CODEC);
/*     */     
/*     */     public SlotDisplay.Type<ItemStackSlotDisplay> type() {
/* 368 */       return TYPE;
/*     */     }
/*     */     
/*     */     public <T> Stream<T> resolve(ContextMap context, DisplayContentsFactory<T> factory) {
/* 373 */       if (factory instanceof DisplayContentsFactory.ForStacks) {
/* 373 */         DisplayContentsFactory.ForStacks<T> stacks = (DisplayContentsFactory.ForStacks<T>)factory;
/* 374 */         return Stream.of(stacks.forStack(this.stack.create()));
/*     */       } 
/* 376 */       return Stream.empty();
/*     */     }
/*     */     
/*     */     public boolean isEnabled(FeatureFlagSet enabledFeatures) {
/* 381 */       return ((Item)this.stack.item().value()).isEnabled(enabledFeatures);
/*     */     }
/*     */   }
/*     */   
/*     */   public static final class TagSlotDisplay extends Record implements SlotDisplay {
/*     */     private final TagKey<Item> tag;
/*     */     
/*     */     public static final MapCodec<TagSlotDisplay> MAP_CODEC;
/*     */     
/*     */     public TagSlotDisplay(TagKey<Item> tag) {
/* 385 */       this.tag = tag;
/*     */     }
/*     */     
/*     */     public final String toString() {
/*     */       // Byte code:
/*     */       //   0: aload_0
/*     */       //   1: <illegal opcode> toString : (Lnet/minecraft/world/item/crafting/display/SlotDisplay$TagSlotDisplay;)Ljava/lang/String;
/*     */       //   6: areturn
/*     */       // Line number table:
/*     */       //   Java source line number -> byte code offset
/*     */       //   #385	-> 0
/*     */       // Local variable table:
/*     */       //   start	length	slot	name	descriptor
/*     */       //   0	7	0	this	Lnet/minecraft/world/item/crafting/display/SlotDisplay$TagSlotDisplay;
/*     */     }
/*     */     
/*     */     public final int hashCode() {
/*     */       // Byte code:
/*     */       //   0: aload_0
/*     */       //   1: <illegal opcode> hashCode : (Lnet/minecraft/world/item/crafting/display/SlotDisplay$TagSlotDisplay;)I
/*     */       //   6: ireturn
/*     */       // Line number table:
/*     */       //   Java source line number -> byte code offset
/*     */       //   #385	-> 0
/*     */       // Local variable table:
/*     */       //   start	length	slot	name	descriptor
/*     */       //   0	7	0	this	Lnet/minecraft/world/item/crafting/display/SlotDisplay$TagSlotDisplay;
/*     */     }
/*     */     
/*     */     public final boolean equals(Object o) {
/*     */       // Byte code:
/*     */       //   0: aload_0
/*     */       //   1: aload_1
/*     */       //   2: <illegal opcode> equals : (Lnet/minecraft/world/item/crafting/display/SlotDisplay$TagSlotDisplay;Ljava/lang/Object;)Z
/*     */       //   7: ireturn
/*     */       // Line number table:
/*     */       //   Java source line number -> byte code offset
/*     */       //   #385	-> 0
/*     */       // Local variable table:
/*     */       //   start	length	slot	name	descriptor
/*     */       //   0	8	0	this	Lnet/minecraft/world/item/crafting/display/SlotDisplay$TagSlotDisplay;
/*     */       //   0	8	1	o	Ljava/lang/Object;
/*     */     }
/*     */     
/*     */     public TagKey<Item> tag() {
/* 385 */       return this.tag;
/*     */     }
/*     */     
/*     */     static {
/* 386 */       MAP_CODEC = RecordCodecBuilder.mapCodec(i -> i.group((App)TagKey.codec(Registries.ITEM).fieldOf("tag").forGetter(TagSlotDisplay::tag)).apply((Applicative)i, TagSlotDisplay::new));
/*     */     }
/*     */     
/* 390 */     public static final StreamCodec<RegistryFriendlyByteBuf, TagSlotDisplay> STREAM_CODEC = StreamCodec.composite(
/* 391 */         TagKey.streamCodec(Registries.ITEM), TagSlotDisplay::tag, TagSlotDisplay::new);
/*     */     
/* 395 */     public static final SlotDisplay.Type<TagSlotDisplay> TYPE = new SlotDisplay.Type<>(MAP_CODEC, STREAM_CODEC);
/*     */     
/*     */     public SlotDisplay.Type<TagSlotDisplay> type() {
/* 399 */       return TYPE;
/*     */     }
/*     */     
/*     */     public <T> Stream<T> resolve(ContextMap context, DisplayContentsFactory<T> factory) {
/* 404 */       if (factory instanceof DisplayContentsFactory.ForStacks) {
/* 404 */         DisplayContentsFactory.ForStacks<T> stacks = (DisplayContentsFactory.ForStacks<T>)factory;
/* 405 */         HolderLookup.Provider registries = (HolderLookup.Provider)context.getOptional(SlotDisplayContext.REGISTRIES);
/* 406 */         if (registries != null)
/* 407 */           return registries.lookupOrThrow(Registries.ITEM)
/* 408 */             .get(this.tag)
/* 409 */             .map(t -> {
/*     */                 Objects.requireNonNull(stacks);
/*     */                 return t.stream().map(stacks::forStack);
/* 410 */               }).stream().flatMap(s -> s); 
/*     */       } 
/* 413 */       return Stream.empty();
/*     */     }
/*     */   }
/*     */   
/*     */   public static final class Composite extends Record implements SlotDisplay {
/*     */     private final List<SlotDisplay> contents;
/*     */     
/*     */     public static final MapCodec<Composite> MAP_CODEC;
/*     */     
/*     */     public Composite(List<SlotDisplay> contents) {
/* 417 */       this.contents = contents;
/*     */     }
/*     */     
/*     */     public final String toString() {
/*     */       // Byte code:
/*     */       //   0: aload_0
/*     */       //   1: <illegal opcode> toString : (Lnet/minecraft/world/item/crafting/display/SlotDisplay$Composite;)Ljava/lang/String;
/*     */       //   6: areturn
/*     */       // Line number table:
/*     */       //   Java source line number -> byte code offset
/*     */       //   #417	-> 0
/*     */       // Local variable table:
/*     */       //   start	length	slot	name	descriptor
/*     */       //   0	7	0	this	Lnet/minecraft/world/item/crafting/display/SlotDisplay$Composite;
/*     */     }
/*     */     
/*     */     public final int hashCode() {
/*     */       // Byte code:
/*     */       //   0: aload_0
/*     */       //   1: <illegal opcode> hashCode : (Lnet/minecraft/world/item/crafting/display/SlotDisplay$Composite;)I
/*     */       //   6: ireturn
/*     */       // Line number table:
/*     */       //   Java source line number -> byte code offset
/*     */       //   #417	-> 0
/*     */       // Local variable table:
/*     */       //   start	length	slot	name	descriptor
/*     */       //   0	7	0	this	Lnet/minecraft/world/item/crafting/display/SlotDisplay$Composite;
/*     */     }
/*     */     
/*     */     public final boolean equals(Object o) {
/*     */       // Byte code:
/*     */       //   0: aload_0
/*     */       //   1: aload_1
/*     */       //   2: <illegal opcode> equals : (Lnet/minecraft/world/item/crafting/display/SlotDisplay$Composite;Ljava/lang/Object;)Z
/*     */       //   7: ireturn
/*     */       // Line number table:
/*     */       //   Java source line number -> byte code offset
/*     */       //   #417	-> 0
/*     */       // Local variable table:
/*     */       //   start	length	slot	name	descriptor
/*     */       //   0	8	0	this	Lnet/minecraft/world/item/crafting/display/SlotDisplay$Composite;
/*     */       //   0	8	1	o	Ljava/lang/Object;
/*     */     }
/*     */     
/*     */     public List<SlotDisplay> contents() {
/* 417 */       return this.contents;
/*     */     }
/*     */     
/*     */     static {
/* 418 */       MAP_CODEC = RecordCodecBuilder.mapCodec(i -> i.group((App)SlotDisplay.CODEC.listOf().fieldOf("contents").forGetter(Composite::contents)).apply((Applicative)i, Composite::new));
/*     */     }
/*     */     
/* 422 */     public static final StreamCodec<RegistryFriendlyByteBuf, Composite> STREAM_CODEC = StreamCodec.composite(SlotDisplay.STREAM_CODEC
/* 423 */         .apply(ByteBufCodecs.list()), Composite::contents, Composite::new);
/*     */     
/* 427 */     public static final SlotDisplay.Type<Composite> TYPE = new SlotDisplay.Type<>(MAP_CODEC, STREAM_CODEC);
/*     */     
/*     */     public SlotDisplay.Type<Composite> type() {
/* 431 */       return TYPE;
/*     */     }
/*     */     
/*     */     public <T> Stream<T> resolve(ContextMap context, DisplayContentsFactory<T> factory) {
/* 436 */       return this.contents.stream().flatMap(d -> d.resolve(context, factory));
/*     */     }
/*     */     
/*     */     public boolean isEnabled(FeatureFlagSet enabledFeatures) {
/* 441 */       return this.contents.stream().allMatch(c -> c.isEnabled(enabledFeatures));
/*     */     }
/*     */   }
/*     */   
/*     */   public static final class WithRemainder extends Record implements SlotDisplay {
/*     */     private final SlotDisplay input;
/*     */     
/*     */     private final SlotDisplay remainder;
/*     */     
/*     */     public static final MapCodec<WithRemainder> MAP_CODEC;
/*     */     
/*     */     public WithRemainder(SlotDisplay input, SlotDisplay remainder) {
/* 445 */       this.input = input;
/* 445 */       this.remainder = remainder;
/*     */     }
/*     */     
/*     */     public final String toString() {
/*     */       // Byte code:
/*     */       //   0: aload_0
/*     */       //   1: <illegal opcode> toString : (Lnet/minecraft/world/item/crafting/display/SlotDisplay$WithRemainder;)Ljava/lang/String;
/*     */       //   6: areturn
/*     */       // Line number table:
/*     */       //   Java source line number -> byte code offset
/*     */       //   #445	-> 0
/*     */       // Local variable table:
/*     */       //   start	length	slot	name	descriptor
/*     */       //   0	7	0	this	Lnet/minecraft/world/item/crafting/display/SlotDisplay$WithRemainder;
/*     */     }
/*     */     
/*     */     public final int hashCode() {
/*     */       // Byte code:
/*     */       //   0: aload_0
/*     */       //   1: <illegal opcode> hashCode : (Lnet/minecraft/world/item/crafting/display/SlotDisplay$WithRemainder;)I
/*     */       //   6: ireturn
/*     */       // Line number table:
/*     */       //   Java source line number -> byte code offset
/*     */       //   #445	-> 0
/*     */       // Local variable table:
/*     */       //   start	length	slot	name	descriptor
/*     */       //   0	7	0	this	Lnet/minecraft/world/item/crafting/display/SlotDisplay$WithRemainder;
/*     */     }
/*     */     
/*     */     public final boolean equals(Object o) {
/*     */       // Byte code:
/*     */       //   0: aload_0
/*     */       //   1: aload_1
/*     */       //   2: <illegal opcode> equals : (Lnet/minecraft/world/item/crafting/display/SlotDisplay$WithRemainder;Ljava/lang/Object;)Z
/*     */       //   7: ireturn
/*     */       // Line number table:
/*     */       //   Java source line number -> byte code offset
/*     */       //   #445	-> 0
/*     */       // Local variable table:
/*     */       //   start	length	slot	name	descriptor
/*     */       //   0	8	0	this	Lnet/minecraft/world/item/crafting/display/SlotDisplay$WithRemainder;
/*     */       //   0	8	1	o	Ljava/lang/Object;
/*     */     }
/*     */     
/*     */     public SlotDisplay input() {
/* 445 */       return this.input;
/*     */     }
/*     */     
/*     */     public SlotDisplay remainder() {
/* 445 */       return this.remainder;
/*     */     }
/*     */     
/*     */     static {
/* 449 */       MAP_CODEC = RecordCodecBuilder.mapCodec(i -> i.group((App)SlotDisplay.CODEC.fieldOf("input").forGetter(WithRemainder::input), (App)SlotDisplay.CODEC.fieldOf("remainder").forGetter(WithRemainder::remainder)).apply((Applicative)i, WithRemainder::new));
/*     */     }
/*     */     
/* 454 */     public static final StreamCodec<RegistryFriendlyByteBuf, WithRemainder> STREAM_CODEC = StreamCodec.composite(SlotDisplay.STREAM_CODEC, WithRemainder::input, SlotDisplay.STREAM_CODEC, WithRemainder::remainder, WithRemainder::new);
/*     */     
/* 460 */     public static final SlotDisplay.Type<WithRemainder> TYPE = new SlotDisplay.Type<>(MAP_CODEC, STREAM_CODEC);
/*     */     
/*     */     public SlotDisplay.Type<WithRemainder> type() {
/* 464 */       return TYPE;
/*     */     }
/*     */     
/*     */     public <T> Stream<T> resolve(ContextMap context, DisplayContentsFactory<T> factory) {
/* 469 */       if (factory instanceof DisplayContentsFactory.ForRemainders) {
/* 469 */         DisplayContentsFactory.ForRemainders<T> remainders = (DisplayContentsFactory.ForRemainders<T>)factory;
/* 470 */         List<T> resolvedRemainders = this.remainder.<T>resolve(context, factory).toList();
/* 471 */         return this.input.<T>resolve(context, factory).map(input -> remainders.addRemainder(input, resolvedRemainders));
/*     */       } 
/* 473 */       return this.input.resolve(context, factory);
/*     */     }
/*     */     
/*     */     public boolean isEnabled(FeatureFlagSet enabledFeatures) {
/* 478 */       return (this.input.isEnabled(enabledFeatures) && this.remainder.isEnabled(enabledFeatures));
/*     */     }
/*     */   }
/*     */ }


/* Location:              C:\Users\karol\AppData\Roaming\ModrinthApp\meta\versions\26.2-0.19.5\26.2-0.19.5.jar!\net\minecraft\world\item\crafting\display\SlotDisplay.class
 * Java compiler version: 25 (69.0)
 * JD-Core Version:       1.1.3
 */