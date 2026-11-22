/*     */ package net.minecraft.client.renderer.item;
/*     */ 
/*     */ import com.mojang.datafixers.kinds.App;
/*     */ import com.mojang.datafixers.kinds.Applicative;
/*     */ import com.mojang.datafixers.util.Function5;
/*     */ import com.mojang.math.Transformation;
/*     */ import com.mojang.serialization.Codec;
/*     */ import com.mojang.serialization.MapCodec;
/*     */ import com.mojang.serialization.codecs.RecordCodecBuilder;
/*     */ import java.util.ArrayList;
/*     */ import java.util.Arrays;
/*     */ import java.util.Comparator;
/*     */ import java.util.List;
/*     */ import java.util.Optional;
/*     */ import java.util.function.BiFunction;
/*     */ import net.minecraft.client.multiplayer.ClientLevel;
/*     */ import net.minecraft.client.renderer.item.properties.numeric.RangeSelectItemModelProperties;
/*     */ import net.minecraft.client.renderer.item.properties.numeric.RangeSelectItemModelProperty;
/*     */ import net.minecraft.client.resources.model.ResolvableModel;
/*     */ import net.minecraft.world.entity.ItemOwner;
/*     */ import net.minecraft.world.item.ItemDisplayContext;
/*     */ import net.minecraft.world.item.ItemStack;
/*     */ import org.joml.Matrix4fc;
/*     */ 
/*     */ public class RangeSelectItemModel implements ItemModel {
/*     */   private static final int LINEAR_SEARCH_THRESHOLD = 16;
/*     */   
/*     */   private final RangeSelectItemModelProperty property;
/*     */   
/*     */   private final float scale;
/*     */   
/*     */   private final float[] thresholds;
/*     */   
/*     */   private final ItemModel[] models;
/*     */   
/*     */   private final ItemModel fallback;
/*     */   
/*     */   private RangeSelectItemModel(RangeSelectItemModelProperty property, float scale, float[] thresholds, ItemModel[] models, ItemModel fallback) {
/*  33 */     this.property = property;
/*  34 */     this.thresholds = thresholds;
/*  35 */     this.models = models;
/*  36 */     this.fallback = fallback;
/*  37 */     this.scale = scale;
/*     */   }
/*     */   
/*     */   private static int lastIndexLessOrEqual(float[] haystack, float needle) {
/*  43 */     if (haystack.length < 16) {
/*  44 */       for (int i = 0; i < haystack.length; i++) {
/*  45 */         if (haystack[i] > needle)
/*  46 */           return i - 1; 
/*     */       } 
/*  49 */       return haystack.length - 1;
/*     */     } 
/*  51 */     int index = Arrays.binarySearch(haystack, needle);
/*  52 */     if (index < 0) {
/*  53 */       int insertionPoint = index ^ 0xFFFFFFFF;
/*  54 */       return insertionPoint - 1;
/*     */     } 
/*  57 */     return index;
/*     */   }
/*     */   
/*     */   public void update(ItemStackRenderState output, ItemStack item, ItemModelResolver resolver, ItemDisplayContext displayContext, ClientLevel level, ItemOwner owner, int seed) {
/*     */     ItemModel selectedModel;
/*  63 */     output.appendModelIdentityElement(this);
/*  64 */     float value = this.property.get(item, level, owner, seed) * this.scale;
/*  66 */     if (Float.isNaN(value)) {
/*  68 */       selectedModel = this.fallback;
/*     */     } else {
/*  70 */       int index = lastIndexLessOrEqual(this.thresholds, value);
/*  71 */       selectedModel = (index == -1) ? this.fallback : this.models[index];
/*     */     } 
/*  73 */     selectedModel.update(output, item, resolver, displayContext, level, owner, seed);
/*     */   }
/*     */   
/*     */   public static final class Unbaked extends Record implements ItemModel.Unbaked {
/*     */     private final Optional<Transformation> transformation;
/*     */     
/*     */     private final RangeSelectItemModelProperty property;
/*     */     
/*     */     private final float scale;
/*     */     
/*     */     private final List<RangeSelectItemModel.Entry> entries;
/*     */     
/*     */     private final Optional<ItemModel.Unbaked> fallback;
/*     */     
/*     */     public static final MapCodec<Unbaked> MAP_CODEC;
/*     */     
/*     */     public Unbaked(Optional<Transformation> transformation, RangeSelectItemModelProperty property, float scale, List<RangeSelectItemModel.Entry> entries, Optional<ItemModel.Unbaked> fallback) {
/*  76 */       this.transformation = transformation;
/*  76 */       this.property = property;
/*  76 */       this.scale = scale;
/*  76 */       this.entries = entries;
/*  76 */       this.fallback = fallback;
/*     */     }
/*     */     
/*     */     public final String toString() {
/*     */       // Byte code:
/*     */       //   0: aload_0
/*     */       //   1: <illegal opcode> toString : (Lnet/minecraft/client/renderer/item/RangeSelectItemModel$Unbaked;)Ljava/lang/String;
/*     */       //   6: areturn
/*     */       // Line number table:
/*     */       //   Java source line number -> byte code offset
/*     */       //   #76	-> 0
/*     */       // Local variable table:
/*     */       //   start	length	slot	name	descriptor
/*     */       //   0	7	0	this	Lnet/minecraft/client/renderer/item/RangeSelectItemModel$Unbaked;
/*     */     }
/*     */     
/*     */     public final int hashCode() {
/*     */       // Byte code:
/*     */       //   0: aload_0
/*     */       //   1: <illegal opcode> hashCode : (Lnet/minecraft/client/renderer/item/RangeSelectItemModel$Unbaked;)I
/*     */       //   6: ireturn
/*     */       // Line number table:
/*     */       //   Java source line number -> byte code offset
/*     */       //   #76	-> 0
/*     */       // Local variable table:
/*     */       //   start	length	slot	name	descriptor
/*     */       //   0	7	0	this	Lnet/minecraft/client/renderer/item/RangeSelectItemModel$Unbaked;
/*     */     }
/*     */     
/*     */     public final boolean equals(Object o) {
/*     */       // Byte code:
/*     */       //   0: aload_0
/*     */       //   1: aload_1
/*     */       //   2: <illegal opcode> equals : (Lnet/minecraft/client/renderer/item/RangeSelectItemModel$Unbaked;Ljava/lang/Object;)Z
/*     */       //   7: ireturn
/*     */       // Line number table:
/*     */       //   Java source line number -> byte code offset
/*     */       //   #76	-> 0
/*     */       // Local variable table:
/*     */       //   start	length	slot	name	descriptor
/*     */       //   0	8	0	this	Lnet/minecraft/client/renderer/item/RangeSelectItemModel$Unbaked;
/*     */       //   0	8	1	o	Ljava/lang/Object;
/*     */     }
/*     */     
/*     */     public Optional<Transformation> transformation() {
/*  76 */       return this.transformation;
/*     */     }
/*     */     
/*     */     public RangeSelectItemModelProperty property() {
/*  76 */       return this.property;
/*     */     }
/*     */     
/*     */     public float scale() {
/*  76 */       return this.scale;
/*     */     }
/*     */     
/*     */     public List<RangeSelectItemModel.Entry> entries() {
/*  76 */       return this.entries;
/*     */     }
/*     */     
/*     */     public Optional<ItemModel.Unbaked> fallback() {
/*  76 */       return this.fallback;
/*     */     }
/*     */     
/*     */     static {
/*  83 */       MAP_CODEC = RecordCodecBuilder.mapCodec(i -> i.group((App)Transformation.EXTENDED_CODEC.optionalFieldOf("transformation").forGetter(Unbaked::transformation), (App)RangeSelectItemModelProperties.MAP_CODEC.forGetter(Unbaked::property), (App)Codec.FLOAT.optionalFieldOf("scale", Float.valueOf(1.0F)).forGetter(Unbaked::scale), (App)RangeSelectItemModel.Entry.CODEC.listOf().fieldOf("entries").forGetter(Unbaked::entries), (App)ItemModels.CODEC.optionalFieldOf("fallback").forGetter(Unbaked::fallback)).apply((Applicative)i, Unbaked::new));
/*     */     }
/*     */     
/*     */     public MapCodec<Unbaked> type() {
/*  93 */       return MAP_CODEC;
/*     */     }
/*     */     
/*     */     public ItemModel bake(ItemModel.BakingContext context, Matrix4fc transformation) {
/*  98 */       Matrix4fc childTransform = Transformation.compose(transformation, this.transformation);
/*  99 */       float[] thresholds = new float[this.entries.size()];
/* 100 */       ItemModel[] models = new ItemModel[this.entries.size()];
/* 101 */       List<RangeSelectItemModel.Entry> mutableEntries = new ArrayList<>(this.entries);
/* 102 */       mutableEntries.sort(RangeSelectItemModel.Entry.BY_THRESHOLD);
/* 103 */       for (int i = 0; i < mutableEntries.size(); i++) {
/* 104 */         RangeSelectItemModel.Entry entry = mutableEntries.get(i);
/* 105 */         thresholds[i] = entry.threshold;
/* 106 */         models[i] = entry.model.bake(context, childTransform);
/*     */       } 
/* 108 */       ItemModel bakedFallback = this.fallback.<ItemModel>map(m -> m.bake(context, childTransform)).orElseGet(() -> context.missingItemModel(childTransform));
/* 109 */       return new RangeSelectItemModel(this.property, this.scale, thresholds, models, bakedFallback);
/*     */     }
/*     */     
/*     */     public void resolveDependencies(ResolvableModel.Resolver resolver) {
/* 114 */       this.fallback.ifPresent(m -> m.resolveDependencies(resolver));
/* 115 */       this.entries.forEach(entry -> entry.model.resolveDependencies(resolver));
/*     */     }
/*     */   }
/*     */   
/*     */   public static final class Entry extends Record {
/*     */     private final float threshold;
/*     */     
/*     */     private final ItemModel.Unbaked model;
/*     */     
/*     */     public static final Codec<Entry> CODEC;
/*     */     
/*     */     public Entry(float threshold, ItemModel.Unbaked model) {
/* 119 */       this.threshold = threshold;
/* 119 */       this.model = model;
/*     */     }
/*     */     
/*     */     public final String toString() {
/*     */       // Byte code:
/*     */       //   0: aload_0
/*     */       //   1: <illegal opcode> toString : (Lnet/minecraft/client/renderer/item/RangeSelectItemModel$Entry;)Ljava/lang/String;
/*     */       //   6: areturn
/*     */       // Line number table:
/*     */       //   Java source line number -> byte code offset
/*     */       //   #119	-> 0
/*     */       // Local variable table:
/*     */       //   start	length	slot	name	descriptor
/*     */       //   0	7	0	this	Lnet/minecraft/client/renderer/item/RangeSelectItemModel$Entry;
/*     */     }
/*     */     
/*     */     public final int hashCode() {
/*     */       // Byte code:
/*     */       //   0: aload_0
/*     */       //   1: <illegal opcode> hashCode : (Lnet/minecraft/client/renderer/item/RangeSelectItemModel$Entry;)I
/*     */       //   6: ireturn
/*     */       // Line number table:
/*     */       //   Java source line number -> byte code offset
/*     */       //   #119	-> 0
/*     */       // Local variable table:
/*     */       //   start	length	slot	name	descriptor
/*     */       //   0	7	0	this	Lnet/minecraft/client/renderer/item/RangeSelectItemModel$Entry;
/*     */     }
/*     */     
/*     */     public final boolean equals(Object o) {
/*     */       // Byte code:
/*     */       //   0: aload_0
/*     */       //   1: aload_1
/*     */       //   2: <illegal opcode> equals : (Lnet/minecraft/client/renderer/item/RangeSelectItemModel$Entry;Ljava/lang/Object;)Z
/*     */       //   7: ireturn
/*     */       // Line number table:
/*     */       //   Java source line number -> byte code offset
/*     */       //   #119	-> 0
/*     */       // Local variable table:
/*     */       //   start	length	slot	name	descriptor
/*     */       //   0	8	0	this	Lnet/minecraft/client/renderer/item/RangeSelectItemModel$Entry;
/*     */       //   0	8	1	o	Ljava/lang/Object;
/*     */     }
/*     */     
/*     */     public float threshold() {
/* 119 */       return this.threshold;
/*     */     }
/*     */     
/*     */     public ItemModel.Unbaked model() {
/* 119 */       return this.model;
/*     */     }
/*     */     
/*     */     static {
/* 120 */       CODEC = RecordCodecBuilder.create(i -> i.group((App)Codec.FLOAT.fieldOf("threshold").forGetter(Entry::threshold), (App)ItemModels.CODEC.fieldOf("model").forGetter(Entry::model)).apply((Applicative)i, Entry::new));
/*     */     }
/*     */     
/* 125 */     public static final Comparator<Entry> BY_THRESHOLD = Comparator.comparingDouble(Entry::threshold);
/*     */   }
/*     */ }


/* Location:              C:\Users\karol\AppData\Roaming\ModrinthApp\meta\versions\26.2-0.19.5\26.2-0.19.5.jar!\net\minecraft\client\renderer\item\RangeSelectItemModel.class
 * Java compiler version: 25 (69.0)
 * JD-Core Version:       1.1.3
 */