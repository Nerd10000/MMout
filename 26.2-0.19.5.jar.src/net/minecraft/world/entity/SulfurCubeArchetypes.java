/*     */ package net.minecraft.world.entity;
/*     */ 
/*     */ import java.util.List;
/*     */ import java.util.Optional;
/*     */ import java.util.function.Function;
/*     */ import net.minecraft.core.Holder;
/*     */ import net.minecraft.core.HolderSet;
/*     */ import net.minecraft.core.registries.Registries;
/*     */ import net.minecraft.data.worldgen.BootstrapContext;
/*     */ import net.minecraft.resources.Identifier;
/*     */ import net.minecraft.resources.ResourceKey;
/*     */ import net.minecraft.sounds.SoundEvent;
/*     */ import net.minecraft.sounds.SoundEvents;
/*     */ import net.minecraft.tags.ItemTags;
/*     */ import net.minecraft.tags.TagKey;
/*     */ import net.minecraft.util.valueproviders.ConstantFloat;
/*     */ import net.minecraft.util.valueproviders.FloatProvider;
/*     */ import net.minecraft.world.damagesource.DamageType;
/*     */ import net.minecraft.world.damagesource.DamageTypes;
/*     */ import net.minecraft.world.entity.ai.attributes.Attribute;
/*     */ import net.minecraft.world.entity.ai.attributes.Attributes;
/*     */ import net.minecraft.world.item.Item;
/*     */ 
/*     */ public class SulfurCubeArchetypes {
/*  26 */   public static final ResourceKey<SulfurCubeArchetype> REGULAR = createKey(Identifier.withDefaultNamespace("regular"));
/*     */   
/*  27 */   public static final ResourceKey<SulfurCubeArchetype> BOUNCY = createKey(Identifier.withDefaultNamespace("bouncy"));
/*     */   
/*  28 */   public static final ResourceKey<SulfurCubeArchetype> SLOW_BOUNCY = createKey(Identifier.withDefaultNamespace("slow_bouncy"));
/*     */   
/*  29 */   public static final ResourceKey<SulfurCubeArchetype> SLOW_FLAT = createKey(Identifier.withDefaultNamespace("slow_flat"));
/*     */   
/*  30 */   public static final ResourceKey<SulfurCubeArchetype> FAST_FLAT = createKey(Identifier.withDefaultNamespace("fast_flat"));
/*     */   
/*  31 */   public static final ResourceKey<SulfurCubeArchetype> LIGHT = createKey(Identifier.withDefaultNamespace("light"));
/*     */   
/*  32 */   public static final ResourceKey<SulfurCubeArchetype> FAST_SLIDING = createKey(Identifier.withDefaultNamespace("fast_sliding"));
/*     */   
/*  33 */   public static final ResourceKey<SulfurCubeArchetype> SLOW_SLIDING = createKey(Identifier.withDefaultNamespace("slow_sliding"));
/*     */   
/*  35 */   public static final ResourceKey<SulfurCubeArchetype> HIGH_RESISTANCE = createKey(Identifier.withDefaultNamespace("high_resistance"));
/*     */   
/*  36 */   public static final ResourceKey<SulfurCubeArchetype> STICKY = createKey(Identifier.withDefaultNamespace("sticky"));
/*     */   
/*  37 */   public static final ResourceKey<SulfurCubeArchetype> EXPLOSIVE = createKey(Identifier.withDefaultNamespace("explosive"));
/*     */   
/*  38 */   public static final ResourceKey<SulfurCubeArchetype> HOT = createKey(Identifier.withDefaultNamespace("hot"));
/*     */   
/*     */   public static void bootstrap(BootstrapContext<SulfurCubeArchetype> context) {
/*  41 */     register(context, REGULAR, ItemTags.SULFUR_CUBE_ARCHETYPE_REGULAR, archetype(1.0F, 0.5F, 0.3F, 0.1F), true, Optional.empty(), Optional.empty(), 
/*  42 */         knockBackHitScale(0.4125F, 0.09F), 
/*  43 */         soundSettings(SoundEvents.SULFUR_CUBE_REGULAR_HIT, SoundEvents.SULFUR_CUBE_REGULAR_PUSH, 0.2F, 0.5F));
/*  44 */     register(context, BOUNCY, ItemTags.SULFUR_CUBE_ARCHETYPE_BOUNCY, archetype(2.0F, 0.9F, 0.3F, 0.01F), true, Optional.empty(), Optional.empty(), 
/*  45 */         knockBackHitScale(0.4125F, 0.105F), 
/*  46 */         soundSettings(SoundEvents.SULFUR_CUBE_BOUNCY_HIT, SoundEvents.SULFUR_CUBE_BOUNCY_PUSH, 0.3F, 0.7F));
/*  47 */     register(context, SLOW_BOUNCY, ItemTags.SULFUR_CUBE_ARCHETYPE_SLOW_BOUNCY, archetype(-0.4F, 0.6F, 0.3F, 0.05F), false, Optional.empty(), Optional.empty(), 
/*  48 */         knockBackHitScale(0.4125F, 0.24F), 
/*  49 */         soundSettings(SoundEvents.SULFUR_CUBE_SLOW_BOUNCY_HIT, SoundEvents.SULFUR_CUBE_SLOW_BOUNCY_PUSH, 0.05F, 0.5F));
/*  50 */     register(context, SLOW_FLAT, ItemTags.SULFUR_CUBE_ARCHETYPE_SLOW_FLAT, archetype(-0.5F, 0.4F, 0.4F, 0.1F), false, Optional.empty(), Optional.empty(), 
/*  51 */         knockBackHitScale(0.4125F, 0.105F), 
/*  52 */         soundSettings(SoundEvents.SULFUR_CUBE_SLOW_FLAT_HIT, SoundEvents.SULFUR_CUBE_SLOW_FLAT_PUSH, 0.03F, 0.9F));
/*  53 */     register(context, FAST_FLAT, ItemTags.SULFUR_CUBE_ARCHETYPE_FAST_FLAT, archetype(1.0F, 0.5F, 0.2F, 0.01F), false, Optional.empty(), Optional.empty(), 
/*  54 */         knockBackHitScale(0.9125F, 0.09F), 
/*  55 */         soundSettings(SoundEvents.SULFUR_CUBE_FAST_FLAT_HIT, SoundEvents.SULFUR_CUBE_FAST_FLAT_PUSH, 0.03F, 0.9F));
/*  56 */     register(context, LIGHT, ItemTags.SULFUR_CUBE_ARCHETYPE_LIGHT, archetype(1.0F, 1.0F, 0.3F, 1.8F), true, Optional.empty(), Optional.empty(), 
/*  57 */         knockBackHitScale(0.4125F, 0.18F), 
/*  58 */         soundSettings(SoundEvents.SULFUR_CUBE_LIGHT_HIT, SoundEvents.SULFUR_CUBE_LIGHT_PUSH, 0.2F, 0.7F));
/*  59 */     register(context, FAST_SLIDING, ItemTags.SULFUR_CUBE_ARCHETYPE_FAST_SLIDING, archetype(-0.5F, 0.1F, 0.05F, 0.01F), false, Optional.empty(), Optional.empty(), 
/*  60 */         knockBackHitScale(0.6625F, 0.09F), 
/*  61 */         soundSettings(SoundEvents.SULFUR_CUBE_FAST_SLIDING_HIT, SoundEvents.SULFUR_CUBE_FAST_SLIDING_PUSH, 0.05F, 1.0F));
/*  62 */     register(context, SLOW_SLIDING, ItemTags.SULFUR_CUBE_ARCHETYPE_SLOW_SLIDING, archetype(-0.8F, 0.1F, 0.05F, 0.01F), false, Optional.empty(), Optional.empty(), 
/*  63 */         knockBackHitScale(0.4125F, 0.09F), 
/*  64 */         soundSettings(SoundEvents.SULFUR_CUBE_SLOW_SLIDING_HIT, SoundEvents.SULFUR_CUBE_SLOW_SLIDING_PUSH, 0.02F, 1.0F));
/*  65 */     register(context, STICKY, ItemTags.SULFUR_CUBE_ARCHETYPE_STICKY, archetype(2.0F, 0.0F, 2.0F, 0.01F), false, Optional.empty(), Optional.empty(), 
/*  66 */         knockBackHitScale(0.4125F, 0.09F), 
/*  67 */         soundSettings(SoundEvents.SULFUR_CUBE_STICKY_HIT, SoundEvents.SULFUR_CUBE_STICKY_PUSH, 0.05F, 0.5F));
/*  68 */     register(context, HIGH_RESISTANCE, ItemTags.SULFUR_CUBE_ARCHETYPE_HIGH_RESISTANCE, archetype(-0.7F, 0.2F, 1.0F, 0.01F), false, Optional.empty(), Optional.empty(), 
/*  69 */         knockBackHitScale(0.4125F, 0.09F), 
/*  70 */         soundSettings(SoundEvents.SULFUR_CUBE_HIGH_RESISTANCE_HIT, SoundEvents.SULFUR_CUBE_HIGH_RESISTANCE_PUSH, 0.03F, 0.7F));
/*  71 */     register(context, EXPLOSIVE, ItemTags.SULFUR_CUBE_ARCHETYPE_EXPLOSIVE, archetype(1.0F, 0.5F, 0.3F, 0.3F), true, Optional.of(new SulfurCubeArchetype.ExplosionData(3, false, 120)), Optional.empty(), 
/*  72 */         knockBackHitScale(0.4125F, 0.09F), 
/*  73 */         soundSettings(SoundEvents.SULFUR_CUBE_EXPLOSIVE_HIT, SoundEvents.SULFUR_CUBE_EXPLOSIVE_PUSH, 0.1F, 0.7F));
/*  74 */     register(context, HOT, ItemTags.SULFUR_CUBE_ARCHETYPE_HOT, archetype(1.0F, 0.5F, 0.3F, 0.1F), true, Optional.empty(), Optional.of(contactDamage(context, DamageTypes.SULFUR_CUBE_HOT, (FloatProvider)ConstantFloat.of(1.0F), false)), 
/*  75 */         knockBackHitScale(0.4125F, 0.09F), 
/*  76 */         soundSettings(SoundEvents.SULFUR_CUBE_HOT_HIT, SoundEvents.SULFUR_CUBE_HOT_PUSH, 0.2F, 0.7F));
/*     */   }
/*     */   
/*     */   private static ResourceKey<SulfurCubeArchetype> createKey(Identifier id) {
/*  80 */     return ResourceKey.create(Registries.SULFUR_CUBE_ARCHETYPE, id);
/*     */   }
/*     */   
/*     */   private static Function<ResourceKey<SulfurCubeArchetype>, SulfurCubeArchetype.AttributeEntry> add(Holder<Attribute> attribute, double amount) {
/*  84 */     return key -> SulfurCubeArchetype.AttributeEntry.add(attribute, amount, key);
/*     */   }
/*     */   
/*     */   private static Function<ResourceKey<SulfurCubeArchetype>, SulfurCubeArchetype.AttributeEntry> multiply(Holder<Attribute> attribute, double amount) {
/*  88 */     return key -> SulfurCubeArchetype.AttributeEntry.multiply(attribute, amount, key);
/*     */   }
/*     */   
/*     */   private static List<Function<ResourceKey<SulfurCubeArchetype>, SulfurCubeArchetype.AttributeEntry>> archetype(float speed, float bounce, float friction, float drag) {
/*  92 */     return List.of(
/*  93 */         add(Attributes.KNOCKBACK_RESISTANCE, -speed), 
/*  94 */         add(Attributes.EXPLOSION_KNOCKBACK_RESISTANCE, -speed), 
/*  95 */         add(Attributes.BOUNCINESS, bounce), 
/*  96 */         multiply(Attributes.FRICTION_MODIFIER, friction), 
/*  97 */         multiply(Attributes.AIR_DRAG_MODIFIER, drag));
/*     */   }
/*     */   
/*     */   private static SulfurCubeArchetype.ContactDamage contactDamage(BootstrapContext<SulfurCubeArchetype> context, ResourceKey<DamageType> damageType, FloatProvider amount, boolean attributeToSource) {
/* 102 */     return new SulfurCubeArchetype.ContactDamage((Holder<DamageType>)context.lookup(Registries.DAMAGE_TYPE).getOrThrow(damageType), amount, attributeToSource);
/*     */   }
/*     */   
/*     */   private static SulfurCubeArchetype.KnockbackModifiers knockBackHitScale(float horizontalPower, float verticalPower) {
/* 106 */     return new SulfurCubeArchetype.KnockbackModifiers(horizontalPower, verticalPower);
/*     */   }
/*     */   
/*     */   private static SulfurCubeArchetype.SoundSettings soundSettings(Holder<SoundEvent> hitSound, Holder<SoundEvent> pushSound, float threshold, float cooldown) {
/* 110 */     return new SulfurCubeArchetype.SoundSettings(hitSound, pushSound, threshold, cooldown);
/*     */   }
/*     */   
/*     */   private static void register(BootstrapContext<SulfurCubeArchetype> context, ResourceKey<SulfurCubeArchetype> name, TagKey<Item> blocks, List<Function<ResourceKey<SulfurCubeArchetype>, SulfurCubeArchetype.AttributeEntry>> modifiers, boolean floats, Optional<SulfurCubeArchetype.ExplosionData> maxFuse, Optional<SulfurCubeArchetype.ContactDamage> contactDamage, SulfurCubeArchetype.KnockbackModifiers knockbackModifiers, SulfurCubeArchetype.SoundSettings soundSettings) {
/* 114 */     context.register(name, new SulfurCubeArchetype((HolderSet<Item>)context
/* 115 */           .lookup(Registries.ITEM).getOrThrow(blocks), modifiers
/* 116 */           .stream().map(f -> (SulfurCubeArchetype.AttributeEntry)f.apply(name)).toList(), floats, maxFuse, contactDamage, knockbackModifiers, soundSettings));
/*     */   }
/*     */ }


/* Location:              C:\Users\karol\AppData\Roaming\ModrinthApp\meta\versions\26.2-0.19.5\26.2-0.19.5.jar!\net\minecraft\world\entity\SulfurCubeArchetypes.class
 * Java compiler version: 25 (69.0)
 * JD-Core Version:       1.1.3
 */