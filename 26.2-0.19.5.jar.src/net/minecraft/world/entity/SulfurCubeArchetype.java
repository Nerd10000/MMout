/*    */ package net.minecraft.world.entity;
/*    */ 
/*    */ import com.mojang.datafixers.kinds.App;
/*    */ import com.mojang.datafixers.kinds.Applicative;
/*    */ import com.mojang.datafixers.util.Function3;
/*    */ import com.mojang.datafixers.util.Function4;
/*    */ import com.mojang.datafixers.util.Function7;
/*    */ import com.mojang.serialization.Codec;
/*    */ import com.mojang.serialization.codecs.RecordCodecBuilder;
/*    */ import java.util.List;
/*    */ import java.util.Optional;
/*    */ import java.util.function.BiFunction;
/*    */ import net.minecraft.core.Holder;
/*    */ import net.minecraft.core.HolderSet;
/*    */ import net.minecraft.core.RegistryCodecs;
/*    */ import net.minecraft.core.registries.Registries;
/*    */ import net.minecraft.resources.Identifier;
/*    */ import net.minecraft.resources.ResourceKey;
/*    */ import net.minecraft.sounds.SoundEvent;
/*    */ import net.minecraft.sounds.SoundEvents;
/*    */ import net.minecraft.util.ExtraCodecs;
/*    */ import net.minecraft.util.valueproviders.FloatProvider;
/*    */ import net.minecraft.util.valueproviders.FloatProviders;
/*    */ import net.minecraft.world.damagesource.DamageType;
/*    */ import net.minecraft.world.entity.ai.attributes.Attribute;
/*    */ import net.minecraft.world.entity.ai.attributes.AttributeModifier;
/*    */ import net.minecraft.world.item.Item;
/*    */ 
/*    */ public final class SulfurCubeArchetype extends Record {
/*    */   private final HolderSet<Item> items;
/*    */   
/*    */   private final List<AttributeEntry> attributeModifiers;
/*    */   
/*    */   private final boolean buoyant;
/*    */   
/*    */   private final Optional<ExplosionData> explosion;
/*    */   
/*    */   private final Optional<ContactDamage> contactDamage;
/*    */   
/*    */   private final KnockbackModifiers knockbackModifiers;
/*    */   
/*    */   private final SoundSettings soundSettings;
/*    */   
/*    */   public static final Codec<SulfurCubeArchetype> DIRECT_CODEC;
/*    */   
/*    */   public SulfurCubeArchetype(HolderSet<Item> items, List<AttributeEntry> attributeModifiers, boolean buoyant, Optional<ExplosionData> explosion, Optional<ContactDamage> contactDamage, KnockbackModifiers knockbackModifiers, SoundSettings soundSettings) {
/* 24 */     this.items = items;
/* 24 */     this.attributeModifiers = attributeModifiers;
/* 24 */     this.buoyant = buoyant;
/* 24 */     this.explosion = explosion;
/* 24 */     this.contactDamage = contactDamage;
/* 24 */     this.knockbackModifiers = knockbackModifiers;
/* 24 */     this.soundSettings = soundSettings;
/*    */   }
/*    */   
/*    */   public final String toString() {
/*    */     // Byte code:
/*    */     //   0: aload_0
/*    */     //   1: <illegal opcode> toString : (Lnet/minecraft/world/entity/SulfurCubeArchetype;)Ljava/lang/String;
/*    */     //   6: areturn
/*    */     // Line number table:
/*    */     //   Java source line number -> byte code offset
/*    */     //   #24	-> 0
/*    */     // Local variable table:
/*    */     //   start	length	slot	name	descriptor
/*    */     //   0	7	0	this	Lnet/minecraft/world/entity/SulfurCubeArchetype;
/*    */   }
/*    */   
/*    */   public final int hashCode() {
/*    */     // Byte code:
/*    */     //   0: aload_0
/*    */     //   1: <illegal opcode> hashCode : (Lnet/minecraft/world/entity/SulfurCubeArchetype;)I
/*    */     //   6: ireturn
/*    */     // Line number table:
/*    */     //   Java source line number -> byte code offset
/*    */     //   #24	-> 0
/*    */     // Local variable table:
/*    */     //   start	length	slot	name	descriptor
/*    */     //   0	7	0	this	Lnet/minecraft/world/entity/SulfurCubeArchetype;
/*    */   }
/*    */   
/*    */   public final boolean equals(Object o) {
/*    */     // Byte code:
/*    */     //   0: aload_0
/*    */     //   1: aload_1
/*    */     //   2: <illegal opcode> equals : (Lnet/minecraft/world/entity/SulfurCubeArchetype;Ljava/lang/Object;)Z
/*    */     //   7: ireturn
/*    */     // Line number table:
/*    */     //   Java source line number -> byte code offset
/*    */     //   #24	-> 0
/*    */     // Local variable table:
/*    */     //   start	length	slot	name	descriptor
/*    */     //   0	8	0	this	Lnet/minecraft/world/entity/SulfurCubeArchetype;
/*    */     //   0	8	1	o	Ljava/lang/Object;
/*    */   }
/*    */   
/*    */   public HolderSet<Item> items() {
/* 24 */     return this.items;
/*    */   }
/*    */   
/*    */   public List<AttributeEntry> attributeModifiers() {
/* 24 */     return this.attributeModifiers;
/*    */   }
/*    */   
/*    */   public boolean buoyant() {
/* 24 */     return this.buoyant;
/*    */   }
/*    */   
/*    */   public Optional<ExplosionData> explosion() {
/* 24 */     return this.explosion;
/*    */   }
/*    */   
/*    */   public Optional<ContactDamage> contactDamage() {
/* 24 */     return this.contactDamage;
/*    */   }
/*    */   
/*    */   public KnockbackModifiers knockbackModifiers() {
/* 24 */     return this.knockbackModifiers;
/*    */   }
/*    */   
/*    */   public SoundSettings soundSettings() {
/* 24 */     return this.soundSettings;
/*    */   }
/*    */   
/*    */   static {
/* 33 */     DIRECT_CODEC = RecordCodecBuilder.create(i -> i.group((App)RegistryCodecs.homogeneousList(Registries.ITEM).fieldOf("items").forGetter(SulfurCubeArchetype::items), (App)AttributeEntry.CODEC.listOf().fieldOf("attribute_modifiers").forGetter(SulfurCubeArchetype::attributeModifiers), (App)Codec.BOOL.optionalFieldOf("buoyant", Boolean.valueOf(false)).forGetter(SulfurCubeArchetype::buoyant), (App)ExplosionData.CODEC.optionalFieldOf("explosion").forGetter(SulfurCubeArchetype::explosion), (App)ContactDamage.CODEC.optionalFieldOf("contact_damage").forGetter(SulfurCubeArchetype::contactDamage), (App)KnockbackModifiers.CODEC.fieldOf("knockback_modifiers").forGetter(SulfurCubeArchetype::knockbackModifiers), (App)SoundSettings.CODEC.fieldOf("sound_settings").forGetter(SulfurCubeArchetype::soundSettings)).apply((Applicative)i, SulfurCubeArchetype::new));
/*    */   }
/*    */   
/*    */   public static final class AttributeEntry extends Record {
/*    */     private final Holder<Attribute> attribute;
/*    */     
/*    */     private final AttributeModifier modifier;
/*    */     
/*    */     public static final Codec<AttributeEntry> CODEC;
/*    */     
/*    */     public AttributeEntry(Holder<Attribute> attribute, AttributeModifier modifier) {
/* 43 */       this.attribute = attribute;
/* 43 */       this.modifier = modifier;
/*    */     }
/*    */     
/*    */     public final String toString() {
/*    */       // Byte code:
/*    */       //   0: aload_0
/*    */       //   1: <illegal opcode> toString : (Lnet/minecraft/world/entity/SulfurCubeArchetype$AttributeEntry;)Ljava/lang/String;
/*    */       //   6: areturn
/*    */       // Line number table:
/*    */       //   Java source line number -> byte code offset
/*    */       //   #43	-> 0
/*    */       // Local variable table:
/*    */       //   start	length	slot	name	descriptor
/*    */       //   0	7	0	this	Lnet/minecraft/world/entity/SulfurCubeArchetype$AttributeEntry;
/*    */     }
/*    */     
/*    */     public final int hashCode() {
/*    */       // Byte code:
/*    */       //   0: aload_0
/*    */       //   1: <illegal opcode> hashCode : (Lnet/minecraft/world/entity/SulfurCubeArchetype$AttributeEntry;)I
/*    */       //   6: ireturn
/*    */       // Line number table:
/*    */       //   Java source line number -> byte code offset
/*    */       //   #43	-> 0
/*    */       // Local variable table:
/*    */       //   start	length	slot	name	descriptor
/*    */       //   0	7	0	this	Lnet/minecraft/world/entity/SulfurCubeArchetype$AttributeEntry;
/*    */     }
/*    */     
/*    */     public final boolean equals(Object o) {
/*    */       // Byte code:
/*    */       //   0: aload_0
/*    */       //   1: aload_1
/*    */       //   2: <illegal opcode> equals : (Lnet/minecraft/world/entity/SulfurCubeArchetype$AttributeEntry;Ljava/lang/Object;)Z
/*    */       //   7: ireturn
/*    */       // Line number table:
/*    */       //   Java source line number -> byte code offset
/*    */       //   #43	-> 0
/*    */       // Local variable table:
/*    */       //   start	length	slot	name	descriptor
/*    */       //   0	8	0	this	Lnet/minecraft/world/entity/SulfurCubeArchetype$AttributeEntry;
/*    */       //   0	8	1	o	Ljava/lang/Object;
/*    */     }
/*    */     
/*    */     public Holder<Attribute> attribute() {
/* 43 */       return this.attribute;
/*    */     }
/*    */     
/*    */     public AttributeModifier modifier() {
/* 43 */       return this.modifier;
/*    */     }
/*    */     
/*    */     static {
/* 44 */       CODEC = RecordCodecBuilder.create(i -> i.group((App)Attribute.CODEC.fieldOf("attribute").forGetter(AttributeEntry::attribute), (App)AttributeModifier.MAP_CODEC.forGetter(AttributeEntry::modifier)).apply((Applicative)i, AttributeEntry::new));
/*    */     }
/*    */     
/*    */     public static AttributeEntry add(Holder<Attribute> attribute, double amount, ResourceKey<SulfurCubeArchetype> archetype) {
/* 50 */       return new AttributeEntry(attribute, new AttributeModifier(Identifier.withDefaultNamespace(archetype.identifier().getPath() + "_add_" + archetype.identifier().getPath()), amount, AttributeModifier.Operation.ADD_VALUE));
/*    */     }
/*    */     
/*    */     public static AttributeEntry multiply(Holder<Attribute> attribute, double amount, ResourceKey<SulfurCubeArchetype> archetype) {
/* 54 */       return new AttributeEntry(attribute, new AttributeModifier(Identifier.withDefaultNamespace(archetype.identifier().getPath() + "_mul_" + archetype.identifier().getPath()), amount - 1.0D, AttributeModifier.Operation.ADD_MULTIPLIED_TOTAL));
/*    */     }
/*    */   }
/*    */   
/*    */   public static final class ExplosionData extends Record {
/*    */     private final int power;
/*    */     
/*    */     private final boolean causesFire;
/*    */     
/*    */     private final int fuse;
/*    */     
/*    */     public static final Codec<ExplosionData> CODEC;
/*    */     
/*    */     public ExplosionData(int power, boolean causesFire, int fuse) {
/* 58 */       this.power = power;
/* 58 */       this.causesFire = causesFire;
/* 58 */       this.fuse = fuse;
/*    */     }
/*    */     
/*    */     public final String toString() {
/*    */       // Byte code:
/*    */       //   0: aload_0
/*    */       //   1: <illegal opcode> toString : (Lnet/minecraft/world/entity/SulfurCubeArchetype$ExplosionData;)Ljava/lang/String;
/*    */       //   6: areturn
/*    */       // Line number table:
/*    */       //   Java source line number -> byte code offset
/*    */       //   #58	-> 0
/*    */       // Local variable table:
/*    */       //   start	length	slot	name	descriptor
/*    */       //   0	7	0	this	Lnet/minecraft/world/entity/SulfurCubeArchetype$ExplosionData;
/*    */     }
/*    */     
/*    */     public final int hashCode() {
/*    */       // Byte code:
/*    */       //   0: aload_0
/*    */       //   1: <illegal opcode> hashCode : (Lnet/minecraft/world/entity/SulfurCubeArchetype$ExplosionData;)I
/*    */       //   6: ireturn
/*    */       // Line number table:
/*    */       //   Java source line number -> byte code offset
/*    */       //   #58	-> 0
/*    */       // Local variable table:
/*    */       //   start	length	slot	name	descriptor
/*    */       //   0	7	0	this	Lnet/minecraft/world/entity/SulfurCubeArchetype$ExplosionData;
/*    */     }
/*    */     
/*    */     public final boolean equals(Object o) {
/*    */       // Byte code:
/*    */       //   0: aload_0
/*    */       //   1: aload_1
/*    */       //   2: <illegal opcode> equals : (Lnet/minecraft/world/entity/SulfurCubeArchetype$ExplosionData;Ljava/lang/Object;)Z
/*    */       //   7: ireturn
/*    */       // Line number table:
/*    */       //   Java source line number -> byte code offset
/*    */       //   #58	-> 0
/*    */       // Local variable table:
/*    */       //   start	length	slot	name	descriptor
/*    */       //   0	8	0	this	Lnet/minecraft/world/entity/SulfurCubeArchetype$ExplosionData;
/*    */       //   0	8	1	o	Ljava/lang/Object;
/*    */     }
/*    */     
/*    */     public int power() {
/* 58 */       return this.power;
/*    */     }
/*    */     
/*    */     public boolean causesFire() {
/* 58 */       return this.causesFire;
/*    */     }
/*    */     
/*    */     public int fuse() {
/* 58 */       return this.fuse;
/*    */     }
/*    */     
/*    */     static {
/* 59 */       CODEC = RecordCodecBuilder.create(i -> i.group((App)ExtraCodecs.NON_NEGATIVE_INT.fieldOf("power").forGetter(ExplosionData::power), (App)Codec.BOOL.fieldOf("causes_fire").forGetter(ExplosionData::causesFire), (App)ExtraCodecs.POSITIVE_INT.fieldOf("fuse").forGetter(ExplosionData::fuse)).apply((Applicative)i, ExplosionData::new));
/*    */     }
/*    */   }
/*    */   
/*    */   public static final class ContactDamage extends Record {
/*    */     private final Holder<DamageType> damageType;
/*    */     
/*    */     private final FloatProvider amount;
/*    */     
/*    */     private final boolean attributeToSource;
/*    */     
/*    */     public static final Codec<ContactDamage> CODEC;
/*    */     
/*    */     public ContactDamage(Holder<DamageType> damageType, FloatProvider amount, boolean attributeToSource) {
/* 66 */       this.damageType = damageType;
/* 66 */       this.amount = amount;
/* 66 */       this.attributeToSource = attributeToSource;
/*    */     }
/*    */     
/*    */     public final String toString() {
/*    */       // Byte code:
/*    */       //   0: aload_0
/*    */       //   1: <illegal opcode> toString : (Lnet/minecraft/world/entity/SulfurCubeArchetype$ContactDamage;)Ljava/lang/String;
/*    */       //   6: areturn
/*    */       // Line number table:
/*    */       //   Java source line number -> byte code offset
/*    */       //   #66	-> 0
/*    */       // Local variable table:
/*    */       //   start	length	slot	name	descriptor
/*    */       //   0	7	0	this	Lnet/minecraft/world/entity/SulfurCubeArchetype$ContactDamage;
/*    */     }
/*    */     
/*    */     public final int hashCode() {
/*    */       // Byte code:
/*    */       //   0: aload_0
/*    */       //   1: <illegal opcode> hashCode : (Lnet/minecraft/world/entity/SulfurCubeArchetype$ContactDamage;)I
/*    */       //   6: ireturn
/*    */       // Line number table:
/*    */       //   Java source line number -> byte code offset
/*    */       //   #66	-> 0
/*    */       // Local variable table:
/*    */       //   start	length	slot	name	descriptor
/*    */       //   0	7	0	this	Lnet/minecraft/world/entity/SulfurCubeArchetype$ContactDamage;
/*    */     }
/*    */     
/*    */     public final boolean equals(Object o) {
/*    */       // Byte code:
/*    */       //   0: aload_0
/*    */       //   1: aload_1
/*    */       //   2: <illegal opcode> equals : (Lnet/minecraft/world/entity/SulfurCubeArchetype$ContactDamage;Ljava/lang/Object;)Z
/*    */       //   7: ireturn
/*    */       // Line number table:
/*    */       //   Java source line number -> byte code offset
/*    */       //   #66	-> 0
/*    */       // Local variable table:
/*    */       //   start	length	slot	name	descriptor
/*    */       //   0	8	0	this	Lnet/minecraft/world/entity/SulfurCubeArchetype$ContactDamage;
/*    */       //   0	8	1	o	Ljava/lang/Object;
/*    */     }
/*    */     
/*    */     public Holder<DamageType> damageType() {
/* 66 */       return this.damageType;
/*    */     }
/*    */     
/*    */     public FloatProvider amount() {
/* 66 */       return this.amount;
/*    */     }
/*    */     
/*    */     public boolean attributeToSource() {
/* 66 */       return this.attributeToSource;
/*    */     }
/*    */     
/*    */     static {
/* 67 */       CODEC = RecordCodecBuilder.create(i -> i.group((App)DamageType.CODEC.fieldOf("damage_type").forGetter(ContactDamage::damageType), (App)FloatProviders.codec(0.0F).fieldOf("amount").forGetter(ContactDamage::amount), (App)Codec.BOOL.fieldOf("attribute_to_source").forGetter(ContactDamage::attributeToSource)).apply((Applicative)i, ContactDamage::new));
/*    */     }
/*    */   }
/*    */   
/*    */   public static final class KnockbackModifiers extends Record {
/*    */     private final float horizontalPower;
/*    */     
/*    */     private final float verticalPower;
/*    */     
/*    */     public static final Codec<KnockbackModifiers> CODEC;
/*    */     
/*    */     public KnockbackModifiers(float horizontalPower, float verticalPower) {
/* 74 */       this.horizontalPower = horizontalPower;
/* 74 */       this.verticalPower = verticalPower;
/*    */     }
/*    */     
/*    */     public final String toString() {
/*    */       // Byte code:
/*    */       //   0: aload_0
/*    */       //   1: <illegal opcode> toString : (Lnet/minecraft/world/entity/SulfurCubeArchetype$KnockbackModifiers;)Ljava/lang/String;
/*    */       //   6: areturn
/*    */       // Line number table:
/*    */       //   Java source line number -> byte code offset
/*    */       //   #74	-> 0
/*    */       // Local variable table:
/*    */       //   start	length	slot	name	descriptor
/*    */       //   0	7	0	this	Lnet/minecraft/world/entity/SulfurCubeArchetype$KnockbackModifiers;
/*    */     }
/*    */     
/*    */     public final int hashCode() {
/*    */       // Byte code:
/*    */       //   0: aload_0
/*    */       //   1: <illegal opcode> hashCode : (Lnet/minecraft/world/entity/SulfurCubeArchetype$KnockbackModifiers;)I
/*    */       //   6: ireturn
/*    */       // Line number table:
/*    */       //   Java source line number -> byte code offset
/*    */       //   #74	-> 0
/*    */       // Local variable table:
/*    */       //   start	length	slot	name	descriptor
/*    */       //   0	7	0	this	Lnet/minecraft/world/entity/SulfurCubeArchetype$KnockbackModifiers;
/*    */     }
/*    */     
/*    */     public final boolean equals(Object o) {
/*    */       // Byte code:
/*    */       //   0: aload_0
/*    */       //   1: aload_1
/*    */       //   2: <illegal opcode> equals : (Lnet/minecraft/world/entity/SulfurCubeArchetype$KnockbackModifiers;Ljava/lang/Object;)Z
/*    */       //   7: ireturn
/*    */       // Line number table:
/*    */       //   Java source line number -> byte code offset
/*    */       //   #74	-> 0
/*    */       // Local variable table:
/*    */       //   start	length	slot	name	descriptor
/*    */       //   0	8	0	this	Lnet/minecraft/world/entity/SulfurCubeArchetype$KnockbackModifiers;
/*    */       //   0	8	1	o	Ljava/lang/Object;
/*    */     }
/*    */     
/*    */     public float horizontalPower() {
/* 74 */       return this.horizontalPower;
/*    */     }
/*    */     
/*    */     public float verticalPower() {
/* 74 */       return this.verticalPower;
/*    */     }
/*    */     
/*    */     static {
/* 75 */       CODEC = RecordCodecBuilder.create(i -> i.group((App)Codec.FLOAT.fieldOf("horizontal_power").forGetter(KnockbackModifiers::horizontalPower), (App)Codec.FLOAT.fieldOf("vertical_power").forGetter(KnockbackModifiers::verticalPower)).apply((Applicative)i, KnockbackModifiers::new));
/*    */     }
/*    */   }
/*    */   
/*    */   public static final class SoundSettings extends Record {
/*    */     private final Holder<SoundEvent> hitSound;
/*    */     
/*    */     private final Holder<SoundEvent> pushSound;
/*    */     
/*    */     private final float pushSoundImpulseThreshold;
/*    */     
/*    */     private final float pushSoundCooldown;
/*    */     
/*    */     public static final Codec<SoundSettings> CODEC;
/*    */     
/*    */     public SoundSettings(Holder<SoundEvent> hitSound, Holder<SoundEvent> pushSound, float pushSoundImpulseThreshold, float pushSoundCooldown) {
/* 81 */       this.hitSound = hitSound;
/* 81 */       this.pushSound = pushSound;
/* 81 */       this.pushSoundImpulseThreshold = pushSoundImpulseThreshold;
/* 81 */       this.pushSoundCooldown = pushSoundCooldown;
/*    */     }
/*    */     
/*    */     public final String toString() {
/*    */       // Byte code:
/*    */       //   0: aload_0
/*    */       //   1: <illegal opcode> toString : (Lnet/minecraft/world/entity/SulfurCubeArchetype$SoundSettings;)Ljava/lang/String;
/*    */       //   6: areturn
/*    */       // Line number table:
/*    */       //   Java source line number -> byte code offset
/*    */       //   #81	-> 0
/*    */       // Local variable table:
/*    */       //   start	length	slot	name	descriptor
/*    */       //   0	7	0	this	Lnet/minecraft/world/entity/SulfurCubeArchetype$SoundSettings;
/*    */     }
/*    */     
/*    */     public final int hashCode() {
/*    */       // Byte code:
/*    */       //   0: aload_0
/*    */       //   1: <illegal opcode> hashCode : (Lnet/minecraft/world/entity/SulfurCubeArchetype$SoundSettings;)I
/*    */       //   6: ireturn
/*    */       // Line number table:
/*    */       //   Java source line number -> byte code offset
/*    */       //   #81	-> 0
/*    */       // Local variable table:
/*    */       //   start	length	slot	name	descriptor
/*    */       //   0	7	0	this	Lnet/minecraft/world/entity/SulfurCubeArchetype$SoundSettings;
/*    */     }
/*    */     
/*    */     public final boolean equals(Object o) {
/*    */       // Byte code:
/*    */       //   0: aload_0
/*    */       //   1: aload_1
/*    */       //   2: <illegal opcode> equals : (Lnet/minecraft/world/entity/SulfurCubeArchetype$SoundSettings;Ljava/lang/Object;)Z
/*    */       //   7: ireturn
/*    */       // Line number table:
/*    */       //   Java source line number -> byte code offset
/*    */       //   #81	-> 0
/*    */       // Local variable table:
/*    */       //   start	length	slot	name	descriptor
/*    */       //   0	8	0	this	Lnet/minecraft/world/entity/SulfurCubeArchetype$SoundSettings;
/*    */       //   0	8	1	o	Ljava/lang/Object;
/*    */     }
/*    */     
/*    */     public Holder<SoundEvent> hitSound() {
/* 81 */       return this.hitSound;
/*    */     }
/*    */     
/*    */     public Holder<SoundEvent> pushSound() {
/* 81 */       return this.pushSound;
/*    */     }
/*    */     
/*    */     public float pushSoundImpulseThreshold() {
/* 81 */       return this.pushSoundImpulseThreshold;
/*    */     }
/*    */     
/*    */     public float pushSoundCooldown() {
/* 81 */       return this.pushSoundCooldown;
/*    */     }
/*    */     
/*    */     static {
/* 82 */       CODEC = RecordCodecBuilder.create(i -> i.group((App)SoundEvent.CODEC.fieldOf("hit_sound").forGetter(SoundSettings::hitSound), (App)SoundEvent.CODEC.fieldOf("push_sound").forGetter(SoundSettings::pushSound), (App)Codec.FLOAT.fieldOf("push_sound_impulse_threshold").forGetter(SoundSettings::pushSoundImpulseThreshold), (App)Codec.FLOAT.fieldOf("push_sound_cooldown").forGetter(SoundSettings::pushSoundCooldown)).apply((Applicative)i, SoundSettings::new));
/*    */     }
/*    */   }
/*    */   
/* 90 */   public static KnockbackModifiers DEFAULT_KNOCKBACK_MODIFIERS = new KnockbackModifiers(0.33F, 0.06F);
/*    */   
/* 91 */   public static SoundSettings DEFAULT_SOUND_SETTINGS = new SoundSettings(SoundEvents.SULFUR_CUBE_REGULAR_HIT, SoundEvents.SULFUR_CUBE_REGULAR_PUSH, 0.2F, 0.5F);
/*    */ }


/* Location:              C:\Users\karol\AppData\Roaming\ModrinthApp\meta\versions\26.2-0.19.5\26.2-0.19.5.jar!\net\minecraft\world\entity\SulfurCubeArchetype.class
 * Java compiler version: 25 (69.0)
 * JD-Core Version:       1.1.3
 */