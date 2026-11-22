/*     */ package net.minecraft.advancements;
/*     */ 
/*     */ import com.google.common.collect.ImmutableMap;
/*     */ import com.mojang.datafixers.kinds.App;
/*     */ import com.mojang.datafixers.kinds.Applicative;
/*     */ import com.mojang.serialization.Codec;
/*     */ import com.mojang.serialization.DataResult;
/*     */ import com.mojang.serialization.codecs.RecordCodecBuilder;
/*     */ import java.util.Map;
/*     */ import java.util.Optional;
/*     */ import java.util.function.Consumer;
/*     */ import java.util.function.Function;
/*     */ import net.minecraft.ChatFormatting;
/*     */ import net.minecraft.advancements.triggers.Criterion;
/*     */ import net.minecraft.core.HolderGetter;
/*     */ import net.minecraft.network.FriendlyByteBuf;
/*     */ import net.minecraft.network.RegistryFriendlyByteBuf;
/*     */ import net.minecraft.network.chat.Component;
/*     */ import net.minecraft.network.chat.ComponentUtils;
/*     */ import net.minecraft.network.chat.HoverEvent;
/*     */ import net.minecraft.network.chat.MutableComponent;
/*     */ import net.minecraft.network.chat.Style;
/*     */ import net.minecraft.network.codec.ByteBufCodecs;
/*     */ import net.minecraft.network.codec.StreamCodec;
/*     */ import net.minecraft.resources.Identifier;
/*     */ import net.minecraft.util.ProblemReporter;
/*     */ import net.minecraft.world.item.ItemStackTemplate;
/*     */ import net.minecraft.world.level.ItemLike;
/*     */ import net.minecraft.world.level.storage.loot.ValidationContextSource;
/*     */ 
/*     */ public final class Advancement extends Record {
/*     */   private final Optional<Identifier> parent;
/*     */   
/*     */   private final Optional<DisplayInfo> display;
/*     */   
/*     */   private final AdvancementRewards rewards;
/*     */   
/*     */   private final Map<String, Criterion<?>> criteria;
/*     */   
/*     */   private final AdvancementRequirements requirements;
/*     */   
/*     */   private final boolean sendsTelemetryEvent;
/*     */   
/*     */   private final Optional<Component> name;
/*     */   
/*     */   private static final Codec<Map<String, Criterion<?>>> CRITERIA_CODEC;
/*     */   
/*     */   public static final Codec<Advancement> CODEC;
/*     */   
/*     */   public Advancement(Optional<Identifier> parent, Optional<DisplayInfo> display, AdvancementRewards rewards, Map<String, Criterion<?>> criteria, AdvancementRequirements requirements, boolean sendsTelemetryEvent, Optional<Component> name) {
/*  30 */     this.parent = parent;
/*  30 */     this.display = display;
/*  30 */     this.rewards = rewards;
/*  30 */     this.criteria = criteria;
/*  30 */     this.requirements = requirements;
/*  30 */     this.sendsTelemetryEvent = sendsTelemetryEvent;
/*  30 */     this.name = name;
/*     */   }
/*     */   
/*     */   public final String toString() {
/*     */     // Byte code:
/*     */     //   0: aload_0
/*     */     //   1: <illegal opcode> toString : (Lnet/minecraft/advancements/Advancement;)Ljava/lang/String;
/*     */     //   6: areturn
/*     */     // Line number table:
/*     */     //   Java source line number -> byte code offset
/*     */     //   #30	-> 0
/*     */     // Local variable table:
/*     */     //   start	length	slot	name	descriptor
/*     */     //   0	7	0	this	Lnet/minecraft/advancements/Advancement;
/*     */   }
/*     */   
/*     */   public final int hashCode() {
/*     */     // Byte code:
/*     */     //   0: aload_0
/*     */     //   1: <illegal opcode> hashCode : (Lnet/minecraft/advancements/Advancement;)I
/*     */     //   6: ireturn
/*     */     // Line number table:
/*     */     //   Java source line number -> byte code offset
/*     */     //   #30	-> 0
/*     */     // Local variable table:
/*     */     //   start	length	slot	name	descriptor
/*     */     //   0	7	0	this	Lnet/minecraft/advancements/Advancement;
/*     */   }
/*     */   
/*     */   public final boolean equals(Object o) {
/*     */     // Byte code:
/*     */     //   0: aload_0
/*     */     //   1: aload_1
/*     */     //   2: <illegal opcode> equals : (Lnet/minecraft/advancements/Advancement;Ljava/lang/Object;)Z
/*     */     //   7: ireturn
/*     */     // Line number table:
/*     */     //   Java source line number -> byte code offset
/*     */     //   #30	-> 0
/*     */     // Local variable table:
/*     */     //   start	length	slot	name	descriptor
/*     */     //   0	8	0	this	Lnet/minecraft/advancements/Advancement;
/*     */     //   0	8	1	o	Ljava/lang/Object;
/*     */   }
/*     */   
/*     */   public Optional<Identifier> parent() {
/*  30 */     return this.parent;
/*     */   }
/*     */   
/*     */   public Optional<DisplayInfo> display() {
/*  30 */     return this.display;
/*     */   }
/*     */   
/*     */   public AdvancementRewards rewards() {
/*  30 */     return this.rewards;
/*     */   }
/*     */   
/*     */   public Map<String, Criterion<?>> criteria() {
/*  30 */     return this.criteria;
/*     */   }
/*     */   
/*     */   public AdvancementRequirements requirements() {
/*  30 */     return this.requirements;
/*     */   }
/*     */   
/*     */   public boolean sendsTelemetryEvent() {
/*  30 */     return this.sendsTelemetryEvent;
/*     */   }
/*     */   
/*     */   public Optional<Component> name() {
/*  30 */     return this.name;
/*     */   }
/*     */   
/*     */   static {
/*  40 */     CRITERIA_CODEC = Codec.unboundedMap((Codec)Codec.STRING, Criterion.CODEC).validate(criteria -> criteria.isEmpty() ? DataResult.error(()) : DataResult.success(criteria));
/*  52 */     CODEC = RecordCodecBuilder.create(i -> i.group((App)Identifier.CODEC.optionalFieldOf("parent").forGetter(Advancement::parent), (App)DisplayInfo.CODEC.optionalFieldOf("display").forGetter(Advancement::display), (App)AdvancementRewards.CODEC.optionalFieldOf("rewards", AdvancementRewards.EMPTY).forGetter(Advancement::rewards), (App)CRITERIA_CODEC.fieldOf("criteria").forGetter(Advancement::criteria), (App)AdvancementRequirements.CODEC.optionalFieldOf("requirements").forGetter(()), (App)Codec.BOOL.optionalFieldOf("sends_telemetry_event", Boolean.valueOf(false)).forGetter(Advancement::sendsTelemetryEvent)).apply((Applicative)i, ())).validate(Advancement::validate);
/*     */   }
/*     */   
/*  54 */   public static final StreamCodec<RegistryFriendlyByteBuf, Advancement> STREAM_CODEC = StreamCodec.ofMember(Advancement::write, Advancement::read);
/*     */   
/*     */   private static DataResult<Advancement> validate(Advancement advancement) {
/*  57 */     return advancement.requirements().validate(advancement.criteria().keySet()).map(r -> advancement);
/*     */   }
/*     */   
/*     */   public Advancement(Optional<Identifier> parent, Optional<DisplayInfo> display, AdvancementRewards rewards, Map<String, Criterion<?>> criteria, AdvancementRequirements requirements, boolean sendsTelemetryEvent) {
/*  61 */     this(parent, display, rewards, Map.copyOf(criteria), requirements, sendsTelemetryEvent, display.map(Advancement::decorateName));
/*     */   }
/*     */   
/*     */   private static Component decorateName(DisplayInfo display) {
/*  65 */     Component displayTitle = display.getTitle();
/*  66 */     ChatFormatting color = display.getType().getChatColor();
/*  68 */     MutableComponent mutableComponent1 = ComponentUtils.mergeStyles(displayTitle.copy(), Style.EMPTY.withColor(color)).append("\n").append(display.getDescription());
/*  69 */     MutableComponent mutableComponent2 = displayTitle.copy().withStyle(s -> s.withHoverEvent((HoverEvent)new HoverEvent.ShowText(tooltip)));
/*  71 */     return (Component)ComponentUtils.wrapInSquareBrackets((Component)mutableComponent2).withStyle(color);
/*     */   }
/*     */   
/*     */   public static Component name(AdvancementHolder holder) {
/*  75 */     return holder.value().name().orElseGet(() -> Component.literal(holder.id().toString()));
/*     */   }
/*     */   
/*     */   private void write(RegistryFriendlyByteBuf output) {
/*  79 */     output.writeOptional(this.parent, FriendlyByteBuf::writeIdentifier);
/*  80 */     DisplayInfo.STREAM_CODEC.apply(ByteBufCodecs::optional).encode(output, this.display);
/*  81 */     this.requirements.write((FriendlyByteBuf)output);
/*  82 */     output.writeBoolean(this.sendsTelemetryEvent);
/*     */   }
/*     */   
/*     */   private static Advancement read(RegistryFriendlyByteBuf input) {
/*  86 */     return new Advancement(input
/*  87 */         .readOptional(FriendlyByteBuf::readIdentifier), (Optional<DisplayInfo>)DisplayInfo.STREAM_CODEC
/*  88 */         .apply(ByteBufCodecs::optional).decode(input), AdvancementRewards.EMPTY, 
/*     */         
/*  90 */         Map.of(), new AdvancementRequirements((FriendlyByteBuf)input), input
/*     */         
/*  92 */         .readBoolean());
/*     */   }
/*     */   
/*     */   public boolean isRoot() {
/*  97 */     return this.parent.isEmpty();
/*     */   }
/*     */   
/*     */   public void validate(ProblemReporter reporter, HolderGetter.Provider lootData) {
/* 101 */     this.criteria.forEach((name, criterion) -> {
/*     */           ValidationContextSource validator = new ValidationContextSource(reporter.forChild((ProblemReporter.PathElement)new ProblemReporter.RootFieldPathElement(name)), lootData);
/*     */           criterion.triggerInstance().validate(validator);
/*     */         });
/*     */   }
/*     */   
/*     */   public static class Builder {
/* 108 */     private Optional<Identifier> parent = Optional.empty();
/*     */     
/* 109 */     private Optional<DisplayInfo> display = Optional.empty();
/*     */     
/* 110 */     private AdvancementRewards rewards = AdvancementRewards.EMPTY;
/*     */     
/* 111 */     private final ImmutableMap.Builder<String, Criterion<?>> criteria = ImmutableMap.builder();
/*     */     
/* 112 */     private Optional<AdvancementRequirements> requirements = Optional.empty();
/*     */     
/* 113 */     private AdvancementRequirements.Strategy requirementsStrategy = AdvancementRequirements.Strategy.AND;
/*     */     
/*     */     private boolean sendsTelemetryEvent;
/*     */     
/*     */     public static Builder advancement() {
/* 117 */       return (new Builder()).sendsTelemetryEvent();
/*     */     }
/*     */     
/*     */     public static Builder recipeAdvancement() {
/* 121 */       return new Builder();
/*     */     }
/*     */     
/*     */     public Builder parent(AdvancementHolder parent) {
/* 125 */       this.parent = Optional.of(parent.id());
/* 126 */       return this;
/*     */     }
/*     */     
/*     */     @Deprecated(forRemoval = true)
/*     */     public Builder parent(Identifier parent) {
/* 132 */       this.parent = Optional.of(parent);
/* 133 */       return this;
/*     */     }
/*     */     
/*     */     public Builder display(ItemStackTemplate icon, Component title, Component description, Identifier background, AdvancementType frame, boolean showToast, boolean announceChat, boolean hidden) {
/* 137 */       return display(new DisplayInfo(icon, title, description, Optional.<Identifier>ofNullable(background).map(net.minecraft.core.ClientAsset.ResourceTexture::new), frame, showToast, announceChat, hidden));
/*     */     }
/*     */     
/*     */     public Builder display(ItemLike icon, Component title, Component description, Identifier background, AdvancementType frame, boolean showToast, boolean announceChat, boolean hidden) {
/* 141 */       return display(new DisplayInfo(new ItemStackTemplate(icon.asItem()), title, description, Optional.<Identifier>ofNullable(background).map(net.minecraft.core.ClientAsset.ResourceTexture::new), frame, showToast, announceChat, hidden));
/*     */     }
/*     */     
/*     */     public Builder display(DisplayInfo display) {
/* 145 */       this.display = Optional.of(display);
/* 146 */       return this;
/*     */     }
/*     */     
/*     */     public Builder rewards(AdvancementRewards.Builder rewards) {
/* 150 */       return rewards(rewards.build());
/*     */     }
/*     */     
/*     */     public Builder rewards(AdvancementRewards rewards) {
/* 154 */       this.rewards = rewards;
/* 155 */       return this;
/*     */     }
/*     */     
/*     */     public Builder addCriterion(String name, Criterion<?> criterion) {
/* 159 */       this.criteria.put(name, criterion);
/* 160 */       return this;
/*     */     }
/*     */     
/*     */     public Builder requirements(AdvancementRequirements.Strategy strategy) {
/* 164 */       this.requirementsStrategy = strategy;
/* 165 */       return this;
/*     */     }
/*     */     
/*     */     public Builder requirements(AdvancementRequirements requirements) {
/* 169 */       this.requirements = Optional.of(requirements);
/* 170 */       return this;
/*     */     }
/*     */     
/*     */     public Builder sendsTelemetryEvent() {
/* 174 */       this.sendsTelemetryEvent = true;
/* 175 */       return this;
/*     */     }
/*     */     
/*     */     public AdvancementHolder build(Identifier id) {
/* 179 */       ImmutableMap immutableMap = this.criteria.buildOrThrow();
/* 180 */       AdvancementRequirements requirements = this.requirements.orElseGet(() -> this.requirementsStrategy.create(criteria.keySet()));
/* 181 */       return new AdvancementHolder(id, new Advancement(this.parent, this.display, this.rewards, (Map<String, Criterion<?>>)immutableMap, requirements, this.sendsTelemetryEvent));
/*     */     }
/*     */     
/*     */     public AdvancementHolder save(Consumer<AdvancementHolder> output, String name) {
/* 185 */       AdvancementHolder advancement = build(Identifier.parse(name));
/* 186 */       output.accept(advancement);
/* 187 */       return advancement;
/*     */     }
/*     */   }
/*     */ }


/* Location:              C:\Users\karol\AppData\Roaming\ModrinthApp\meta\versions\26.2-0.19.5\26.2-0.19.5.jar!\net\minecraft\advancements\Advancement.class
 * Java compiler version: 25 (69.0)
 * JD-Core Version:       1.1.3
 */