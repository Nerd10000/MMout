/*     */ package net.minecraft.world.attribute;
/*     */ 
/*     */ import com.google.common.annotations.VisibleForTesting;
/*     */ import it.unimi.dsi.fastutil.objects.Reference2ObjectOpenHashMap;
/*     */ import java.util.ArrayList;
/*     */ import java.util.HashMap;
/*     */ import java.util.List;
/*     */ import java.util.Map;
/*     */ import java.util.Objects;
/*     */ import java.util.stream.Stream;
/*     */ import net.minecraft.SharedConstants;
/*     */ import net.minecraft.core.Holder;
/*     */ import net.minecraft.core.HolderLookup;
/*     */ import net.minecraft.core.RegistryAccess;
/*     */ import net.minecraft.core.registries.Registries;
/*     */ import net.minecraft.world.clock.ClockManager;
/*     */ import net.minecraft.world.level.Level;
/*     */ import net.minecraft.world.level.biome.Biome;
/*     */ import net.minecraft.world.level.biome.BiomeManager;
/*     */ import net.minecraft.world.level.dimension.DimensionType;
/*     */ import net.minecraft.world.phys.Vec3;
/*     */ import net.minecraft.world.timeline.Timeline;
/*     */ 
/*     */ public class EnvironmentAttributeSystem implements EnvironmentAttributeReader {
/*  28 */   private final Map<EnvironmentAttribute<?>, ValueSampler<?>> attributeSamplers = (Map<EnvironmentAttribute<?>, ValueSampler<?>>)new Reference2ObjectOpenHashMap();
/*     */   
/*     */   private EnvironmentAttributeSystem(Map<EnvironmentAttribute<?>, List<EnvironmentAttributeLayer<?>>> layersByAttribute) {
/*  31 */     layersByAttribute.forEach((attribute, layers) -> this.attributeSamplers.put(attribute, bakeLayerSampler(attribute, layers)));
/*     */   }
/*     */   
/*     */   private <Value> ValueSampler<Value> bakeLayerSampler(EnvironmentAttribute<Value> attribute, List<? extends EnvironmentAttributeLayer<?>> untypedLayers) {
/*  38 */     List<EnvironmentAttributeLayer<Value>> layers = (List)new ArrayList<>(untypedLayers);
/*  40 */     Value constantBaseValue = attribute.defaultValue();
/*  41 */     while (!layers.isEmpty()) {
/*  41 */       EnvironmentAttributeLayer.Constant constant = (EnvironmentAttributeLayer.Constant)layers.getFirst();
/*  41 */       if (constant instanceof EnvironmentAttributeLayer.Constant) {
/*  41 */         EnvironmentAttributeLayer.Constant<Value> constantLayer = constant;
/*  42 */         constantBaseValue = constantLayer.applyConstant(constantBaseValue);
/*  43 */         layers.removeFirst();
/*     */       } 
/*     */     } 
/*  46 */     boolean isAffectedByPosition = layers.stream().anyMatch(layer -> layer instanceof EnvironmentAttributeLayer.Positional);
/*  48 */     return new ValueSampler<>(attribute, constantBaseValue, List.copyOf(layers), isAffectedByPosition);
/*     */   }
/*     */   
/*     */   public static Builder builder() {
/*  52 */     return new Builder();
/*     */   }
/*     */   
/*     */   private static void addDefaultLayers(Builder builder, Level level) {
/*  56 */     RegistryAccess registries = level.registryAccess();
/*  57 */     BiomeManager biomeManager = level.getBiomeManager();
/*  58 */     ClockManager clockManager = level.clockManager();
/*  59 */     addDimensionLayer(builder, level.dimensionType());
/*  60 */     addBiomeLayer(builder, (HolderLookup<Biome>)registries.lookupOrThrow(Registries.BIOME), biomeManager);
/*  61 */     level.dimensionType().timelines().forEach(timeline -> builder.addTimelineLayer(timeline, clockManager));
/*  62 */     if (level.canHaveWeather())
/*  63 */       WeatherAttributes.addBuiltinLayers(builder, WeatherAttributes.WeatherAccess.from(level)); 
/*     */   }
/*     */   
/*     */   private static void addDimensionLayer(Builder builder, DimensionType dimensionType) {
/*  68 */     builder.addConstantLayer(dimensionType.attributes());
/*     */   }
/*     */   
/*     */   private static void addBiomeLayer(Builder builder, HolderLookup<Biome> biomes, BiomeManager biomeManager) {
/*  76 */     Stream<EnvironmentAttribute<?>> attributesProvidedByBiomes = biomes.listElements().flatMap(biome -> ((Biome)biome.value()).getAttributes().keySet().stream()).distinct();
/*  77 */     attributesProvidedByBiomes.forEach(attribute -> addBiomeLayerForAttribute(builder, attribute, biomeManager));
/*     */   }
/*     */   
/*     */   private static <Value> void addBiomeLayerForAttribute(Builder builder, EnvironmentAttribute<Value> attribute, BiomeManager biomeManager) {
/*  83 */     builder.addPositionalLayer(attribute, (baseValue, pos, biomeWeights) -> {
/*     */           if (biomeWeights != null && attribute.isSpatiallyInterpolated())
/*     */             return biomeWeights.applyAttributeLayer(attribute, baseValue); 
/*     */           Holder<Biome> biome = biomeManager.getNoiseBiomeAtPosition(pos.x, pos.y, pos.z);
/*     */           return ((Biome)biome.value()).getAttributes().applyModifier(attribute, baseValue);
/*     */         });
/*     */   }
/*     */   
/*     */   public void invalidateTickCache() {
/*  94 */     this.attributeSamplers.values().forEach(ValueSampler::invalidateTickCache);
/*     */   }
/*     */   
/*     */   private <Value> ValueSampler<Value> getValueSampler(EnvironmentAttribute<Value> attribute) {
/*  99 */     return (ValueSampler<Value>)this.attributeSamplers.get(attribute);
/*     */   }
/*     */   
/*     */   public <Value> Value getDimensionValue(EnvironmentAttribute<Value> attribute) {
/* 104 */     if (SharedConstants.IS_RUNNING_IN_IDE && attribute.isPositional())
/* 105 */       throw new IllegalStateException("Position must always be provided for positional attribute " + String.valueOf(attribute)); 
/* 107 */     ValueSampler<Value> sampler = getValueSampler(attribute);
/* 108 */     if (sampler == null)
/* 109 */       return attribute.defaultValue(); 
/* 111 */     return sampler.getDimensionValue();
/*     */   }
/*     */   
/*     */   public <Value> Value getValue(EnvironmentAttribute<Value> attribute, Vec3 pos, SpatialAttributeInterpolator biomeInterpolator) {
/* 116 */     ValueSampler<Value> sampler = getValueSampler(attribute);
/* 117 */     if (sampler == null)
/* 118 */       return attribute.defaultValue(); 
/* 120 */     return sampler.getValue(pos, biomeInterpolator);
/*     */   }
/*     */   
/*     */   @VisibleForTesting
/*     */   <Value> Value getConstantBaseValue(EnvironmentAttribute<Value> attribute) {
/* 126 */     ValueSampler<Value> sampler = getValueSampler(attribute);
/* 127 */     return (sampler != null) ? sampler.baseValue : attribute.defaultValue();
/*     */   }
/*     */   
/*     */   @VisibleForTesting
/*     */   boolean isAffectedByPosition(EnvironmentAttribute<?> attribute) {
/* 133 */     ValueSampler<?> sampler = getValueSampler(attribute);
/* 134 */     return (sampler != null && sampler.isAffectedByPosition);
/*     */   }
/*     */   
/*     */   public static class Builder {
/* 138 */     private final Map<EnvironmentAttribute<?>, List<EnvironmentAttributeLayer<?>>> layersByAttribute = new HashMap<>();
/*     */     
/*     */     public Builder addDefaultLayers(Level level) {
/* 144 */       EnvironmentAttributeSystem.addDefaultLayers(this, level);
/* 145 */       return this;
/*     */     }
/*     */     
/*     */     public Builder addConstantLayer(EnvironmentAttributeMap attributeMap) {
/* 149 */       for (EnvironmentAttribute<?> attribute : attributeMap.keySet())
/* 150 */         addConstantEntry(attribute, attributeMap); 
/* 152 */       return this;
/*     */     }
/*     */     
/*     */     private <Value> Builder addConstantEntry(EnvironmentAttribute<Value> attribute, EnvironmentAttributeMap attributeMap) {
/* 156 */       EnvironmentAttributeMap.Entry<Value, ?> entry = attributeMap.get(attribute);
/* 157 */       if (entry == null)
/* 158 */         throw new IllegalArgumentException("Missing attribute " + String.valueOf(attribute)); 
/* 160 */       Objects.requireNonNull(entry);
/* 160 */       return addConstantLayer(attribute, entry::applyModifier);
/*     */     }
/*     */     
/*     */     public <Value> Builder addConstantLayer(EnvironmentAttribute<Value> attribute, EnvironmentAttributeLayer.Constant<Value> layer) {
/* 164 */       return addLayer(attribute, layer);
/*     */     }
/*     */     
/*     */     public <Value> Builder addTimeBasedLayer(EnvironmentAttribute<Value> attribute, EnvironmentAttributeLayer.TimeBased<Value> layer) {
/* 168 */       return addLayer(attribute, layer);
/*     */     }
/*     */     
/*     */     public <Value> Builder addPositionalLayer(EnvironmentAttribute<Value> attribute, EnvironmentAttributeLayer.Positional<Value> layer) {
/* 172 */       return addLayer(attribute, layer);
/*     */     }
/*     */     
/*     */     private <Value> Builder addLayer(EnvironmentAttribute<Value> attribute, EnvironmentAttributeLayer<Value> layer) {
/* 176 */       ((List<EnvironmentAttributeLayer<Value>>)this.layersByAttribute.computeIfAbsent(attribute, t -> new ArrayList())).add(layer);
/* 177 */       return this;
/*     */     }
/*     */     
/*     */     public Builder addTimelineLayer(Holder<Timeline> timeline, ClockManager clockManager) {
/* 181 */       for (EnvironmentAttribute<?> attribute : (Iterable<EnvironmentAttribute<?>>)((Timeline)timeline.value()).attributes())
/* 182 */         addTimelineLayerForAttribute(timeline, attribute, clockManager); 
/* 184 */       return this;
/*     */     }
/*     */     
/*     */     private <Value> void addTimelineLayerForAttribute(Holder<Timeline> timeline, EnvironmentAttribute<Value> attribute, ClockManager clockManager) {
/* 188 */       addTimeBasedLayer(attribute, (EnvironmentAttributeLayer.TimeBased<Value>)((Timeline)timeline.value()).createTrackSampler(attribute, clockManager));
/*     */     }
/*     */     
/*     */     public EnvironmentAttributeSystem build() {
/* 192 */       return new EnvironmentAttributeSystem(this.layersByAttribute);
/*     */     }
/*     */   }
/*     */   
/*     */   private static class ValueSampler<Value> {
/*     */     private final EnvironmentAttribute<Value> attribute;
/*     */     
/*     */     private final Value baseValue;
/*     */     
/*     */     private final List<EnvironmentAttributeLayer<Value>> layers;
/*     */     
/*     */     private final boolean isAffectedByPosition;
/*     */     
/*     */     private Value cachedTickValue;
/*     */     
/*     */     private int cacheTickId;
/*     */     
/*     */     private ValueSampler(EnvironmentAttribute<Value> attribute, Value baseValue, List<EnvironmentAttributeLayer<Value>> layers, boolean isAffectedByPosition) {
/* 206 */       this.attribute = attribute;
/* 207 */       this.baseValue = baseValue;
/* 208 */       this.layers = layers;
/* 209 */       this.isAffectedByPosition = isAffectedByPosition;
/*     */     }
/*     */     
/*     */     public void invalidateTickCache() {
/* 213 */       this.cachedTickValue = null;
/* 214 */       this.cacheTickId++;
/*     */     }
/*     */     
/*     */     public Value getDimensionValue() {
/* 218 */       if (this.cachedTickValue != null)
/* 219 */         return this.cachedTickValue; 
/* 221 */       Value result = computeValueNotPositional();
/* 222 */       this.cachedTickValue = result;
/* 223 */       return result;
/*     */     }
/*     */     
/*     */     public Value getValue(Vec3 pos, SpatialAttributeInterpolator biomeInterpolator) {
/* 228 */       if (!this.isAffectedByPosition)
/* 229 */         return getDimensionValue(); 
/* 231 */       return computeValuePositional(pos, biomeInterpolator);
/*     */     }
/*     */     
/*     */     private Value computeValuePositional(Vec3 pos, SpatialAttributeInterpolator biomeInterpolator) {
/*     */       // Byte code:
/*     */       //   0: aload_0
/*     */       //   1: getfield baseValue : Ljava/lang/Object;
/*     */       //   4: astore_3
/*     */       //   5: aload_0
/*     */       //   6: getfield layers : Ljava/util/List;
/*     */       //   9: invokeinterface iterator : ()Ljava/util/Iterator;
/*     */       //   14: astore #4
/*     */       //   16: aload #4
/*     */       //   18: invokeinterface hasNext : ()Z
/*     */       //   23: ifeq -> 161
/*     */       //   26: aload #4
/*     */       //   28: invokeinterface next : ()Ljava/lang/Object;
/*     */       //   33: checkcast net/minecraft/world/attribute/EnvironmentAttributeLayer
/*     */       //   36: astore #5
/*     */       //   38: aload #5
/*     */       //   40: dup
/*     */       //   41: invokestatic requireNonNull : (Ljava/lang/Object;)Ljava/lang/Object;
/*     */       //   44: pop
/*     */       //   45: astore #6
/*     */       //   47: iconst_0
/*     */       //   48: istore #7
/*     */       //   50: aload #6
/*     */       //   52: iload #7
/*     */       //   54: <illegal opcode> typeSwitch : (Lnet/minecraft/world/attribute/EnvironmentAttributeLayer;I)I
/*     */       //   59: tableswitch default -> 84, 0 -> 94, 1 -> 112, 2 -> 134
/*     */       //   84: new java/lang/MatchException
/*     */       //   87: dup
/*     */       //   88: aconst_null
/*     */       //   89: aconst_null
/*     */       //   90: invokespecial <init> : (Ljava/lang/String;Ljava/lang/Throwable;)V
/*     */       //   93: athrow
/*     */       //   94: aload #6
/*     */       //   96: checkcast net/minecraft/world/attribute/EnvironmentAttributeLayer$Constant
/*     */       //   99: astore #8
/*     */       //   101: aload #8
/*     */       //   103: aload_3
/*     */       //   104: invokeinterface applyConstant : (Ljava/lang/Object;)Ljava/lang/Object;
/*     */       //   109: goto -> 157
/*     */       //   112: aload #6
/*     */       //   114: checkcast net/minecraft/world/attribute/EnvironmentAttributeLayer$TimeBased
/*     */       //   117: astore #9
/*     */       //   119: aload #9
/*     */       //   121: aload_3
/*     */       //   122: aload_0
/*     */       //   123: getfield cacheTickId : I
/*     */       //   126: invokeinterface applyTimeBased : (Ljava/lang/Object;I)Ljava/lang/Object;
/*     */       //   131: goto -> 157
/*     */       //   134: aload #6
/*     */       //   136: checkcast net/minecraft/world/attribute/EnvironmentAttributeLayer$Positional
/*     */       //   139: astore #10
/*     */       //   141: aload #10
/*     */       //   143: aload_3
/*     */       //   144: aload_1
/*     */       //   145: invokestatic requireNonNull : (Ljava/lang/Object;)Ljava/lang/Object;
/*     */       //   148: checkcast net/minecraft/world/phys/Vec3
/*     */       //   151: aload_2
/*     */       //   152: invokeinterface applyPositional : (Ljava/lang/Object;Lnet/minecraft/world/phys/Vec3;Lnet/minecraft/world/attribute/SpatialAttributeInterpolator;)Ljava/lang/Object;
/*     */       //   157: astore_3
/*     */       //   158: goto -> 16
/*     */       //   161: aload_0
/*     */       //   162: getfield attribute : Lnet/minecraft/world/attribute/EnvironmentAttribute;
/*     */       //   165: aload_3
/*     */       //   166: invokevirtual sanitizeValue : (Ljava/lang/Object;)Ljava/lang/Object;
/*     */       //   169: areturn
/*     */       // Line number table:
/*     */       //   Java source line number -> byte code offset
/*     */       //   #235	-> 0
/*     */       //   #236	-> 5
/*     */       //   #237	-> 38
/*     */       //   #238	-> 94
/*     */       //   #239	-> 101
/*     */       //   #240	-> 112
/*     */       //   #241	-> 119
/*     */       //   #242	-> 134
/*     */       //   #243	-> 141
/*     */       //   #244	-> 157
/*     */       //   #245	-> 158
/*     */       //   #246	-> 161
/*     */       // Local variable table:
/*     */       //   start	length	slot	name	descriptor
/*     */       //   101	11	8	constantLayer	Lnet/minecraft/world/attribute/EnvironmentAttributeLayer$Constant;
/*     */       //   119	15	9	timeBasedLayer	Lnet/minecraft/world/attribute/EnvironmentAttributeLayer$TimeBased;
/*     */       //   141	16	10	positionalLayer	Lnet/minecraft/world/attribute/EnvironmentAttributeLayer$Positional;
/*     */       //   38	120	5	layer	Lnet/minecraft/world/attribute/EnvironmentAttributeLayer;
/*     */       //   0	170	0	this	Lnet/minecraft/world/attribute/EnvironmentAttributeSystem$ValueSampler;
/*     */       //   0	170	1	pos	Lnet/minecraft/world/phys/Vec3;
/*     */       //   0	170	2	biomeInterpolator	Lnet/minecraft/world/attribute/SpatialAttributeInterpolator;
/*     */       //   5	165	3	result	Ljava/lang/Object;
/*     */       // Local variable type table:
/*     */       //   start	length	slot	name	signature
/*     */       //   101	11	8	constantLayer	Lnet/minecraft/world/attribute/EnvironmentAttributeLayer$Constant<TValue;>;
/*     */       //   119	15	9	timeBasedLayer	Lnet/minecraft/world/attribute/EnvironmentAttributeLayer$TimeBased<TValue;>;
/*     */       //   141	16	10	positionalLayer	Lnet/minecraft/world/attribute/EnvironmentAttributeLayer$Positional<TValue;>;
/*     */       //   38	120	5	layer	Lnet/minecraft/world/attribute/EnvironmentAttributeLayer<TValue;>;
/*     */       //   0	170	0	this	Lnet/minecraft/world/attribute/EnvironmentAttributeSystem$ValueSampler<TValue;>;
/*     */       //   5	165	3	result	TValue;
/*     */     }
/*     */     
/*     */     private Value computeValueNotPositional() {
/*     */       // Byte code:
/*     */       //   0: aload_0
/*     */       //   1: getfield baseValue : Ljava/lang/Object;
/*     */       //   4: astore_1
/*     */       //   5: aload_0
/*     */       //   6: getfield layers : Ljava/util/List;
/*     */       //   9: invokeinterface iterator : ()Ljava/util/Iterator;
/*     */       //   14: astore_2
/*     */       //   15: aload_2
/*     */       //   16: invokeinterface hasNext : ()Z
/*     */       //   21: ifeq -> 142
/*     */       //   24: aload_2
/*     */       //   25: invokeinterface next : ()Ljava/lang/Object;
/*     */       //   30: checkcast net/minecraft/world/attribute/EnvironmentAttributeLayer
/*     */       //   33: astore_3
/*     */       //   34: aload_3
/*     */       //   35: dup
/*     */       //   36: invokestatic requireNonNull : (Ljava/lang/Object;)Ljava/lang/Object;
/*     */       //   39: pop
/*     */       //   40: astore #4
/*     */       //   42: iconst_0
/*     */       //   43: istore #5
/*     */       //   45: aload #4
/*     */       //   47: iload #5
/*     */       //   49: <illegal opcode> typeSwitch : (Lnet/minecraft/world/attribute/EnvironmentAttributeLayer;I)I
/*     */       //   54: tableswitch default -> 80, 0 -> 90, 1 -> 108, 2 -> 130
/*     */       //   80: new java/lang/MatchException
/*     */       //   83: dup
/*     */       //   84: aconst_null
/*     */       //   85: aconst_null
/*     */       //   86: invokespecial <init> : (Ljava/lang/String;Ljava/lang/Throwable;)V
/*     */       //   89: athrow
/*     */       //   90: aload #4
/*     */       //   92: checkcast net/minecraft/world/attribute/EnvironmentAttributeLayer$Constant
/*     */       //   95: astore #6
/*     */       //   97: aload #6
/*     */       //   99: aload_1
/*     */       //   100: invokeinterface applyConstant : (Ljava/lang/Object;)Ljava/lang/Object;
/*     */       //   105: goto -> 138
/*     */       //   108: aload #4
/*     */       //   110: checkcast net/minecraft/world/attribute/EnvironmentAttributeLayer$TimeBased
/*     */       //   113: astore #7
/*     */       //   115: aload #7
/*     */       //   117: aload_1
/*     */       //   118: aload_0
/*     */       //   119: getfield cacheTickId : I
/*     */       //   122: invokeinterface applyTimeBased : (Ljava/lang/Object;I)Ljava/lang/Object;
/*     */       //   127: goto -> 138
/*     */       //   130: aload #4
/*     */       //   132: checkcast net/minecraft/world/attribute/EnvironmentAttributeLayer$Positional
/*     */       //   135: astore #8
/*     */       //   137: aload_1
/*     */       //   138: astore_1
/*     */       //   139: goto -> 15
/*     */       //   142: aload_0
/*     */       //   143: getfield attribute : Lnet/minecraft/world/attribute/EnvironmentAttribute;
/*     */       //   146: aload_1
/*     */       //   147: invokevirtual sanitizeValue : (Ljava/lang/Object;)Ljava/lang/Object;
/*     */       //   150: areturn
/*     */       // Line number table:
/*     */       //   Java source line number -> byte code offset
/*     */       //   #250	-> 0
/*     */       //   #251	-> 5
/*     */       //   #252	-> 34
/*     */       //   #253	-> 90
/*     */       //   #254	-> 97
/*     */       //   #255	-> 108
/*     */       //   #256	-> 115
/*     */       //   #258	-> 130
/*     */       //   #259	-> 138
/*     */       //   #260	-> 139
/*     */       //   #261	-> 142
/*     */       // Local variable table:
/*     */       //   start	length	slot	name	descriptor
/*     */       //   97	11	6	constantLayer	Lnet/minecraft/world/attribute/EnvironmentAttributeLayer$Constant;
/*     */       //   115	15	7	timeBasedLayer	Lnet/minecraft/world/attribute/EnvironmentAttributeLayer$TimeBased;
/*     */       //   137	1	8	ignored	Lnet/minecraft/world/attribute/EnvironmentAttributeLayer$Positional;
/*     */       //   34	105	3	layer	Lnet/minecraft/world/attribute/EnvironmentAttributeLayer;
/*     */       //   0	151	0	this	Lnet/minecraft/world/attribute/EnvironmentAttributeSystem$ValueSampler;
/*     */       //   5	146	1	result	Ljava/lang/Object;
/*     */       // Local variable type table:
/*     */       //   start	length	slot	name	signature
/*     */       //   97	11	6	constantLayer	Lnet/minecraft/world/attribute/EnvironmentAttributeLayer$Constant<TValue;>;
/*     */       //   115	15	7	timeBasedLayer	Lnet/minecraft/world/attribute/EnvironmentAttributeLayer$TimeBased<TValue;>;
/*     */       //   137	1	8	ignored	Lnet/minecraft/world/attribute/EnvironmentAttributeLayer$Positional<TValue;>;
/*     */       //   34	105	3	layer	Lnet/minecraft/world/attribute/EnvironmentAttributeLayer<TValue;>;
/*     */       //   0	151	0	this	Lnet/minecraft/world/attribute/EnvironmentAttributeSystem$ValueSampler<TValue;>;
/*     */       //   5	146	1	result	TValue;
/*     */     }
/*     */   }
/*     */ }


/* Location:              C:\Users\karol\AppData\Roaming\ModrinthApp\meta\versions\26.2-0.19.5\26.2-0.19.5.jar!\net\minecraft\world\attribute\EnvironmentAttributeSystem.class
 * Java compiler version: 25 (69.0)
 * JD-Core Version:       1.1.3
 */