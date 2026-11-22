/*     */ package net.minecraft.world.entity.ai.attributes;
/*     */ 
/*     */ import com.google.common.annotations.VisibleForTesting;
/*     */ import com.google.common.collect.ImmutableSet;
/*     */ import com.google.common.collect.Maps;
/*     */ import com.mojang.datafixers.kinds.App;
/*     */ import com.mojang.datafixers.kinds.Applicative;
/*     */ import com.mojang.datafixers.util.Function3;
/*     */ import com.mojang.serialization.Codec;
/*     */ import com.mojang.serialization.codecs.RecordCodecBuilder;
/*     */ import it.unimi.dsi.fastutil.objects.Object2ObjectArrayMap;
/*     */ import it.unimi.dsi.fastutil.objects.Object2ObjectOpenHashMap;
/*     */ import java.util.Collection;
/*     */ import java.util.List;
/*     */ import java.util.Map;
/*     */ import java.util.Set;
/*     */ import java.util.function.Consumer;
/*     */ import net.minecraft.core.Holder;
/*     */ import net.minecraft.core.registries.BuiltInRegistries;
/*     */ import net.minecraft.resources.Identifier;
/*     */ import net.minecraft.util.ExtraCodecs;
/*     */ 
/*     */ public class AttributeInstance {
/*     */   private final Holder<Attribute> attribute;
/*     */   
/*  25 */   private final Map<AttributeModifier.Operation, Map<Identifier, AttributeModifier>> modifiersByOperation = Maps.newEnumMap(AttributeModifier.Operation.class);
/*     */   
/*  26 */   private final Map<Identifier, AttributeModifier> modifierById = (Map<Identifier, AttributeModifier>)new Object2ObjectArrayMap();
/*     */   
/*  27 */   private final Map<Identifier, AttributeModifier> permanentModifiers = (Map<Identifier, AttributeModifier>)new Object2ObjectArrayMap();
/*     */   
/*     */   private double baseValue;
/*     */   
/*     */   private boolean dirty = true;
/*     */   
/*     */   private double cachedValue;
/*     */   
/*     */   private final Consumer<AttributeInstance> onDirty;
/*     */   
/*     */   public AttributeInstance(Holder<Attribute> attribute, Consumer<AttributeInstance> onDirty) {
/*  34 */     this.attribute = attribute;
/*  35 */     this.onDirty = onDirty;
/*  36 */     this.baseValue = ((Attribute)attribute.value()).getDefaultValue();
/*     */   }
/*     */   
/*     */   public Holder<Attribute> getAttribute() {
/*  40 */     return this.attribute;
/*     */   }
/*     */   
/*     */   public double getBaseValue() {
/*  44 */     return this.baseValue;
/*     */   }
/*     */   
/*     */   public void setBaseValue(double baseValue) {
/*  48 */     if (baseValue == this.baseValue)
/*     */       return; 
/*  51 */     this.baseValue = baseValue;
/*  52 */     setDirty();
/*     */   }
/*     */   
/*     */   @VisibleForTesting
/*     */   Map<Identifier, AttributeModifier> getModifiers(AttributeModifier.Operation operation) {
/*  58 */     return this.modifiersByOperation.computeIfAbsent(operation, key -> new Object2ObjectOpenHashMap());
/*     */   }
/*     */   
/*     */   public Set<AttributeModifier> getModifiers() {
/*  62 */     return (Set<AttributeModifier>)ImmutableSet.copyOf(this.modifierById.values());
/*     */   }
/*     */   
/*     */   public Set<AttributeModifier> getPermanentModifiers() {
/*  66 */     return (Set<AttributeModifier>)ImmutableSet.copyOf(this.permanentModifiers.values());
/*     */   }
/*     */   
/*     */   public AttributeModifier getModifier(Identifier id) {
/*  70 */     return this.modifierById.get(id);
/*     */   }
/*     */   
/*     */   public boolean hasModifier(Identifier modifier) {
/*  74 */     return (this.modifierById.get(modifier) != null);
/*     */   }
/*     */   
/*     */   private void addModifier(AttributeModifier modifier) {
/*  78 */     AttributeModifier previous = this.modifierById.putIfAbsent(modifier.id(), modifier);
/*  79 */     if (previous != null)
/*  80 */       throw new IllegalArgumentException("Modifier is already applied on this attribute!"); 
/*  82 */     getModifiers(modifier.operation()).put(modifier.id(), modifier);
/*  83 */     setDirty();
/*     */   }
/*     */   
/*     */   public void addOrUpdateTransientModifier(AttributeModifier modifier) {
/*  87 */     AttributeModifier oldModifier = this.modifierById.put(modifier.id(), modifier);
/*  88 */     if (modifier == oldModifier)
/*     */       return; 
/*  91 */     getModifiers(modifier.operation()).put(modifier.id(), modifier);
/*  92 */     setDirty();
/*     */   }
/*     */   
/*     */   public void addTransientModifier(AttributeModifier modifier) {
/*  96 */     addModifier(modifier);
/*     */   }
/*     */   
/*     */   public void addOrReplacePermanentModifier(AttributeModifier modifier) {
/* 100 */     removeModifier(modifier.id());
/* 101 */     addModifier(modifier);
/* 102 */     this.permanentModifiers.put(modifier.id(), modifier);
/*     */   }
/*     */   
/*     */   public void addPermanentModifier(AttributeModifier modifier) {
/* 106 */     addModifier(modifier);
/* 107 */     this.permanentModifiers.put(modifier.id(), modifier);
/*     */   }
/*     */   
/*     */   public void addPermanentModifiers(Collection<AttributeModifier> modifiers) {
/* 111 */     for (AttributeModifier modifier : modifiers)
/* 112 */       addPermanentModifier(modifier); 
/*     */   }
/*     */   
/*     */   protected void setDirty() {
/* 117 */     this.dirty = true;
/* 118 */     this.onDirty.accept(this);
/*     */   }
/*     */   
/*     */   public void removeModifier(AttributeModifier modifier) {
/* 122 */     removeModifier(modifier.id());
/*     */   }
/*     */   
/*     */   public boolean removeModifier(Identifier id) {
/* 126 */     AttributeModifier modifier = this.modifierById.remove(id);
/* 127 */     if (modifier == null)
/* 128 */       return false; 
/* 130 */     getModifiers(modifier.operation()).remove(id);
/* 131 */     this.permanentModifiers.remove(id);
/* 132 */     setDirty();
/* 133 */     return true;
/*     */   }
/*     */   
/*     */   public void removeModifiers() {
/* 137 */     for (AttributeModifier modifier : getModifiers())
/* 138 */       removeModifier(modifier); 
/*     */   }
/*     */   
/*     */   public double getValue() {
/* 143 */     if (this.dirty) {
/* 144 */       this.cachedValue = calculateValue();
/* 145 */       this.dirty = false;
/*     */     } 
/* 148 */     return this.cachedValue;
/*     */   }
/*     */   
/*     */   private double calculateValue() {
/* 152 */     double base = getBaseValue();
/* 154 */     for (AttributeModifier modifier : getModifiersOrEmpty(AttributeModifier.Operation.ADD_VALUE))
/* 155 */       base += modifier.amount(); 
/* 158 */     double result = base;
/* 160 */     for (AttributeModifier modifier : getModifiersOrEmpty(AttributeModifier.Operation.ADD_MULTIPLIED_BASE))
/* 161 */       result += base * modifier.amount(); 
/* 164 */     for (AttributeModifier modifier : getModifiersOrEmpty(AttributeModifier.Operation.ADD_MULTIPLIED_TOTAL))
/* 165 */       result *= 1.0D + modifier.amount(); 
/* 168 */     return ((Attribute)this.attribute.value()).sanitizeValue(result);
/*     */   }
/*     */   
/*     */   private Collection<AttributeModifier> getModifiersOrEmpty(AttributeModifier.Operation operation) {
/* 172 */     return ((Map)this.modifiersByOperation.getOrDefault(operation, Map.of())).values();
/*     */   }
/*     */   
/*     */   public void replaceFrom(AttributeInstance other) {
/* 176 */     this.baseValue = other.baseValue;
/* 178 */     this.modifierById.clear();
/* 179 */     this.modifierById.putAll(other.modifierById);
/* 181 */     this.permanentModifiers.clear();
/* 182 */     this.permanentModifiers.putAll(other.permanentModifiers);
/* 184 */     this.modifiersByOperation.clear();
/* 185 */     other.modifiersByOperation.forEach((operation, attributeModifiers) -> getModifiers(operation).putAll(attributeModifiers));
/* 188 */     setDirty();
/*     */   }
/*     */   
/*     */   public Packed pack() {
/* 192 */     return new Packed(this.attribute, this.baseValue, 
/*     */         
/* 195 */         List.copyOf(this.permanentModifiers.values()));
/*     */   }
/*     */   
/*     */   public void apply(Packed packed) {
/* 200 */     this.baseValue = packed.baseValue;
/* 202 */     for (AttributeModifier modifier : packed.modifiers) {
/* 203 */       this.modifierById.put(modifier.id(), modifier);
/* 204 */       getModifiers(modifier.operation()).put(modifier.id(), modifier);
/* 205 */       this.permanentModifiers.put(modifier.id(), modifier);
/*     */     } 
/* 207 */     setDirty();
/*     */   }
/*     */   
/*     */   public static final class Packed extends Record {
/*     */     private final Holder<Attribute> attribute;
/*     */     
/*     */     private final double baseValue;
/*     */     
/*     */     private final List<AttributeModifier> modifiers;
/*     */     
/*     */     public static final Codec<Packed> CODEC;
/*     */     
/*     */     public Packed(Holder<Attribute> attribute, double baseValue, List<AttributeModifier> modifiers) {
/* 210 */       this.attribute = attribute;
/* 210 */       this.baseValue = baseValue;
/* 210 */       this.modifiers = modifiers;
/*     */     }
/*     */     
/*     */     public final String toString() {
/*     */       // Byte code:
/*     */       //   0: aload_0
/*     */       //   1: <illegal opcode> toString : (Lnet/minecraft/world/entity/ai/attributes/AttributeInstance$Packed;)Ljava/lang/String;
/*     */       //   6: areturn
/*     */       // Line number table:
/*     */       //   Java source line number -> byte code offset
/*     */       //   #210	-> 0
/*     */       // Local variable table:
/*     */       //   start	length	slot	name	descriptor
/*     */       //   0	7	0	this	Lnet/minecraft/world/entity/ai/attributes/AttributeInstance$Packed;
/*     */     }
/*     */     
/*     */     public final int hashCode() {
/*     */       // Byte code:
/*     */       //   0: aload_0
/*     */       //   1: <illegal opcode> hashCode : (Lnet/minecraft/world/entity/ai/attributes/AttributeInstance$Packed;)I
/*     */       //   6: ireturn
/*     */       // Line number table:
/*     */       //   Java source line number -> byte code offset
/*     */       //   #210	-> 0
/*     */       // Local variable table:
/*     */       //   start	length	slot	name	descriptor
/*     */       //   0	7	0	this	Lnet/minecraft/world/entity/ai/attributes/AttributeInstance$Packed;
/*     */     }
/*     */     
/*     */     public final boolean equals(Object o) {
/*     */       // Byte code:
/*     */       //   0: aload_0
/*     */       //   1: aload_1
/*     */       //   2: <illegal opcode> equals : (Lnet/minecraft/world/entity/ai/attributes/AttributeInstance$Packed;Ljava/lang/Object;)Z
/*     */       //   7: ireturn
/*     */       // Line number table:
/*     */       //   Java source line number -> byte code offset
/*     */       //   #210	-> 0
/*     */       // Local variable table:
/*     */       //   start	length	slot	name	descriptor
/*     */       //   0	8	0	this	Lnet/minecraft/world/entity/ai/attributes/AttributeInstance$Packed;
/*     */       //   0	8	1	o	Ljava/lang/Object;
/*     */     }
/*     */     
/*     */     public Holder<Attribute> attribute() {
/* 210 */       return this.attribute;
/*     */     }
/*     */     
/*     */     public double baseValue() {
/* 210 */       return this.baseValue;
/*     */     }
/*     */     
/*     */     public List<AttributeModifier> modifiers() {
/* 210 */       return this.modifiers;
/*     */     }
/*     */     
/*     */     static {
/* 215 */       CODEC = RecordCodecBuilder.create(i -> i.group((App)BuiltInRegistries.ATTRIBUTE.holderByNameCodec().fieldOf("id").forGetter(Packed::attribute), (App)ExtraCodecs.optionalAlwaysPresentFieldOf((Codec)Codec.DOUBLE, "base", Double.valueOf(0.0D)).forGetter(Packed::baseValue), (App)AttributeModifier.CODEC.listOf().optionalFieldOf("modifiers", List.of()).forGetter(Packed::modifiers)).apply((Applicative)i, Packed::new));
/*     */     }
/*     */     
/* 221 */     public static final Codec<List<Packed>> LIST_CODEC = CODEC.listOf();
/*     */   }
/*     */ }


/* Location:              C:\Users\karol\AppData\Roaming\ModrinthApp\meta\versions\26.2-0.19.5\26.2-0.19.5.jar!\net\minecraft\world\entity\ai\attributes\AttributeInstance.class
 * Java compiler version: 25 (69.0)
 * JD-Core Version:       1.1.3
 */