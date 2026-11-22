/*     */ package net.minecraft.advancements;
/*     */ 
/*     */ import com.google.common.collect.ImmutableMap;
/*     */ import java.util.Map;
/*     */ import java.util.Optional;
/*     */ import java.util.function.Consumer;
/*     */ import java.util.function.Function;
/*     */ import net.minecraft.advancements.triggers.Criterion;
/*     */ import net.minecraft.network.chat.Component;
/*     */ import net.minecraft.resources.Identifier;
/*     */ import net.minecraft.world.item.ItemStackTemplate;
/*     */ import net.minecraft.world.level.ItemLike;
/*     */ 
/*     */ public class Builder {
/* 108 */   private Optional<Identifier> parent = Optional.empty();
/*     */   
/* 109 */   private Optional<DisplayInfo> display = Optional.empty();
/*     */   
/* 110 */   private AdvancementRewards rewards = AdvancementRewards.EMPTY;
/*     */   
/* 111 */   private final ImmutableMap.Builder<String, Criterion<?>> criteria = ImmutableMap.builder();
/*     */   
/* 112 */   private Optional<AdvancementRequirements> requirements = Optional.empty();
/*     */   
/* 113 */   private AdvancementRequirements.Strategy requirementsStrategy = AdvancementRequirements.Strategy.AND;
/*     */   
/*     */   private boolean sendsTelemetryEvent;
/*     */   
/*     */   public static Builder advancement() {
/* 117 */     return (new Builder()).sendsTelemetryEvent();
/*     */   }
/*     */   
/*     */   public static Builder recipeAdvancement() {
/* 121 */     return new Builder();
/*     */   }
/*     */   
/*     */   public Builder parent(AdvancementHolder parent) {
/* 125 */     this.parent = Optional.of(parent.id());
/* 126 */     return this;
/*     */   }
/*     */   
/*     */   @Deprecated(forRemoval = true)
/*     */   public Builder parent(Identifier parent) {
/* 132 */     this.parent = Optional.of(parent);
/* 133 */     return this;
/*     */   }
/*     */   
/*     */   public Builder display(ItemStackTemplate icon, Component title, Component description, Identifier background, AdvancementType frame, boolean showToast, boolean announceChat, boolean hidden) {
/* 137 */     return display(new DisplayInfo(icon, title, description, Optional.<Identifier>ofNullable(background).map(net.minecraft.core.ClientAsset.ResourceTexture::new), frame, showToast, announceChat, hidden));
/*     */   }
/*     */   
/*     */   public Builder display(ItemLike icon, Component title, Component description, Identifier background, AdvancementType frame, boolean showToast, boolean announceChat, boolean hidden) {
/* 141 */     return display(new DisplayInfo(new ItemStackTemplate(icon.asItem()), title, description, Optional.<Identifier>ofNullable(background).map(net.minecraft.core.ClientAsset.ResourceTexture::new), frame, showToast, announceChat, hidden));
/*     */   }
/*     */   
/*     */   public Builder display(DisplayInfo display) {
/* 145 */     this.display = Optional.of(display);
/* 146 */     return this;
/*     */   }
/*     */   
/*     */   public Builder rewards(AdvancementRewards.Builder rewards) {
/* 150 */     return rewards(rewards.build());
/*     */   }
/*     */   
/*     */   public Builder rewards(AdvancementRewards rewards) {
/* 154 */     this.rewards = rewards;
/* 155 */     return this;
/*     */   }
/*     */   
/*     */   public Builder addCriterion(String name, Criterion<?> criterion) {
/* 159 */     this.criteria.put(name, criterion);
/* 160 */     return this;
/*     */   }
/*     */   
/*     */   public Builder requirements(AdvancementRequirements.Strategy strategy) {
/* 164 */     this.requirementsStrategy = strategy;
/* 165 */     return this;
/*     */   }
/*     */   
/*     */   public Builder requirements(AdvancementRequirements requirements) {
/* 169 */     this.requirements = Optional.of(requirements);
/* 170 */     return this;
/*     */   }
/*     */   
/*     */   public Builder sendsTelemetryEvent() {
/* 174 */     this.sendsTelemetryEvent = true;
/* 175 */     return this;
/*     */   }
/*     */   
/*     */   public AdvancementHolder build(Identifier id) {
/* 179 */     ImmutableMap immutableMap = this.criteria.buildOrThrow();
/* 180 */     AdvancementRequirements requirements = this.requirements.orElseGet(() -> this.requirementsStrategy.create(criteria.keySet()));
/* 181 */     return new AdvancementHolder(id, new Advancement(this.parent, this.display, this.rewards, (Map<String, Criterion<?>>)immutableMap, requirements, this.sendsTelemetryEvent));
/*     */   }
/*     */   
/*     */   public AdvancementHolder save(Consumer<AdvancementHolder> output, String name) {
/* 185 */     AdvancementHolder advancement = build(Identifier.parse(name));
/* 186 */     output.accept(advancement);
/* 187 */     return advancement;
/*     */   }
/*     */ }


/* Location:              C:\Users\karol\AppData\Roaming\ModrinthApp\meta\versions\26.2-0.19.5\26.2-0.19.5.jar!\net\minecraft\advancements\Advancement$Builder.class
 * Java compiler version: 25 (69.0)
 * JD-Core Version:       1.1.3
 */