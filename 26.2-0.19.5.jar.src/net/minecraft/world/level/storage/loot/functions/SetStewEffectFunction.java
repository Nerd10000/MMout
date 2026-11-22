/*     */ package net.minecraft.world.level.storage.loot.functions;
/*     */ 
/*     */ import com.google.common.collect.ImmutableList;
/*     */ import com.mojang.datafixers.kinds.App;
/*     */ import com.mojang.datafixers.kinds.Applicative;
/*     */ import com.mojang.serialization.Codec;
/*     */ import com.mojang.serialization.DataResult;
/*     */ import com.mojang.serialization.MapCodec;
/*     */ import com.mojang.serialization.codecs.RecordCodecBuilder;
/*     */ import it.unimi.dsi.fastutil.objects.ObjectOpenHashSet;
/*     */ import java.util.List;
/*     */ import java.util.function.BiFunction;
/*     */ import net.minecraft.core.Holder;
/*     */ import net.minecraft.core.component.DataComponents;
/*     */ import net.minecraft.util.Util;
/*     */ import net.minecraft.world.effect.MobEffect;
/*     */ import net.minecraft.world.item.ItemStack;
/*     */ import net.minecraft.world.item.Items;
/*     */ import net.minecraft.world.item.component.SuspiciousStewEffects;
/*     */ import net.minecraft.world.level.storage.loot.LootContext;
/*     */ import net.minecraft.world.level.storage.loot.LootContextUser;
/*     */ import net.minecraft.world.level.storage.loot.Validatable;
/*     */ import net.minecraft.world.level.storage.loot.ValidationContext;
/*     */ import net.minecraft.world.level.storage.loot.predicates.LootItemCondition;
/*     */ import net.minecraft.world.level.storage.loot.providers.number.NumberProvider;
/*     */ import net.minecraft.world.level.storage.loot.providers.number.NumberProviders;
/*     */ 
/*     */ public class SetStewEffectFunction extends LootItemConditionalFunction {
/*     */   private static final Codec<List<EffectEntry>> EFFECTS_LIST;
/*     */   
/*     */   public static final MapCodec<SetStewEffectFunction> MAP_CODEC;
/*     */   
/*     */   private final List<EffectEntry> effects;
/*     */   
/*     */   static {
/*  29 */     EFFECTS_LIST = EffectEntry.CODEC.listOf().validate(entries -> {
/*     */           ObjectOpenHashSet<Holder<MobEffect>> objectOpenHashSet = new ObjectOpenHashSet();
/*     */           for (EffectEntry entry : entries) {
/*     */             if (!objectOpenHashSet.add(entry.effect()))
/*     */               return DataResult.error(()); 
/*     */           } 
/*     */           return DataResult.success(entries);
/*     */         });
/*  39 */     MAP_CODEC = RecordCodecBuilder.mapCodec(i -> commonFields(i).and((App)EFFECTS_LIST.optionalFieldOf("effects", List.of()).forGetter(())).apply((Applicative)i, SetStewEffectFunction::new));
/*     */   }
/*     */   
/*     */   private SetStewEffectFunction(List<LootItemCondition> predicates, List<EffectEntry> effects) {
/*  46 */     super(predicates);
/*  47 */     this.effects = effects;
/*     */   }
/*     */   
/*     */   public MapCodec<SetStewEffectFunction> codec() {
/*  52 */     return MAP_CODEC;
/*     */   }
/*     */   
/*     */   public void validate(ValidationContext context) {
/*  57 */     super.validate(context);
/*  59 */     Validatable.validate(context, "effects", this.effects);
/*     */   }
/*     */   
/*     */   public ItemStack run(ItemStack itemStack, LootContext context) {
/*  64 */     if (!itemStack.is(Items.SUSPICIOUS_STEW) || this.effects.isEmpty())
/*  65 */       return itemStack; 
/*  68 */     EffectEntry entry = (EffectEntry)Util.getRandom(this.effects, context.getRandom());
/*  70 */     Holder<MobEffect> effect = entry.effect();
/*  71 */     int duration = entry.duration().getInt(context);
/*  72 */     if (!((MobEffect)effect.value()).isInstantaneous())
/*  73 */       duration *= 20; 
/*  76 */     SuspiciousStewEffects.Entry newEntry = new SuspiciousStewEffects.Entry(effect, duration);
/*  77 */     itemStack.update(DataComponents.SUSPICIOUS_STEW_EFFECTS, SuspiciousStewEffects.EMPTY, newEntry, SuspiciousStewEffects::withEffectAdded);
/*  79 */     return itemStack;
/*     */   }
/*     */   
/*     */   public static class Builder extends LootItemConditionalFunction.Builder<Builder> {
/*  83 */     private final ImmutableList.Builder<SetStewEffectFunction.EffectEntry> effects = ImmutableList.builder();
/*     */     
/*     */     protected Builder getThis() {
/*  87 */       return this;
/*     */     }
/*     */     
/*     */     public Builder withEffect(Holder<MobEffect> effect, NumberProvider duration) {
/*  91 */       this.effects.add(new SetStewEffectFunction.EffectEntry(effect, duration));
/*  92 */       return this;
/*     */     }
/*     */     
/*     */     public LootItemFunction build() {
/*  97 */       return new SetStewEffectFunction(getConditions(), (List<SetStewEffectFunction.EffectEntry>)this.effects.build());
/*     */     }
/*     */   }
/*     */   
/*     */   public static Builder stewEffect() {
/* 102 */     return new Builder();
/*     */   }
/*     */   
/*     */   private static final class EffectEntry extends Record implements LootContextUser {
/*     */     private final Holder<MobEffect> effect;
/*     */     
/*     */     private final NumberProvider duration;
/*     */     
/*     */     public static final Codec<EffectEntry> CODEC;
/*     */     
/*     */     private EffectEntry(Holder<MobEffect> effect, NumberProvider duration) {
/* 105 */       this.effect = effect;
/* 105 */       this.duration = duration;
/*     */     }
/*     */     
/*     */     public final String toString() {
/*     */       // Byte code:
/*     */       //   0: aload_0
/*     */       //   1: <illegal opcode> toString : (Lnet/minecraft/world/level/storage/loot/functions/SetStewEffectFunction$EffectEntry;)Ljava/lang/String;
/*     */       //   6: areturn
/*     */       // Line number table:
/*     */       //   Java source line number -> byte code offset
/*     */       //   #105	-> 0
/*     */       // Local variable table:
/*     */       //   start	length	slot	name	descriptor
/*     */       //   0	7	0	this	Lnet/minecraft/world/level/storage/loot/functions/SetStewEffectFunction$EffectEntry;
/*     */     }
/*     */     
/*     */     public final int hashCode() {
/*     */       // Byte code:
/*     */       //   0: aload_0
/*     */       //   1: <illegal opcode> hashCode : (Lnet/minecraft/world/level/storage/loot/functions/SetStewEffectFunction$EffectEntry;)I
/*     */       //   6: ireturn
/*     */       // Line number table:
/*     */       //   Java source line number -> byte code offset
/*     */       //   #105	-> 0
/*     */       // Local variable table:
/*     */       //   start	length	slot	name	descriptor
/*     */       //   0	7	0	this	Lnet/minecraft/world/level/storage/loot/functions/SetStewEffectFunction$EffectEntry;
/*     */     }
/*     */     
/*     */     public final boolean equals(Object o) {
/*     */       // Byte code:
/*     */       //   0: aload_0
/*     */       //   1: aload_1
/*     */       //   2: <illegal opcode> equals : (Lnet/minecraft/world/level/storage/loot/functions/SetStewEffectFunction$EffectEntry;Ljava/lang/Object;)Z
/*     */       //   7: ireturn
/*     */       // Line number table:
/*     */       //   Java source line number -> byte code offset
/*     */       //   #105	-> 0
/*     */       // Local variable table:
/*     */       //   start	length	slot	name	descriptor
/*     */       //   0	8	0	this	Lnet/minecraft/world/level/storage/loot/functions/SetStewEffectFunction$EffectEntry;
/*     */       //   0	8	1	o	Ljava/lang/Object;
/*     */     }
/*     */     
/*     */     public Holder<MobEffect> effect() {
/* 105 */       return this.effect;
/*     */     }
/*     */     
/*     */     public NumberProvider duration() {
/* 105 */       return this.duration;
/*     */     }
/*     */     
/*     */     static {
/* 106 */       CODEC = RecordCodecBuilder.create(i -> i.group((App)MobEffect.CODEC.fieldOf("type").forGetter(EffectEntry::effect), (App)NumberProviders.CODEC.fieldOf("duration").forGetter(EffectEntry::duration)).apply((Applicative)i, EffectEntry::new));
/*     */     }
/*     */     
/*     */     public void validate(ValidationContext context) {
/* 113 */       super.validate(context);
/* 115 */       Validatable.validate(context, "duration", (Validatable)this.duration);
/*     */     }
/*     */   }
/*     */ }


/* Location:              C:\Users\karol\AppData\Roaming\ModrinthApp\meta\versions\26.2-0.19.5\26.2-0.19.5.jar!\net\minecraft\world\level\storage\loot\functions\SetStewEffectFunction.class
 * Java compiler version: 25 (69.0)
 * JD-Core Version:       1.1.3
 */