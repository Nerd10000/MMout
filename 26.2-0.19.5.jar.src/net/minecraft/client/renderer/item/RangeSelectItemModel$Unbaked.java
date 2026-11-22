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
/*     */ import java.util.List;
/*     */ import java.util.Optional;
/*     */ import net.minecraft.client.renderer.item.properties.numeric.RangeSelectItemModelProperties;
/*     */ import net.minecraft.client.renderer.item.properties.numeric.RangeSelectItemModelProperty;
/*     */ import net.minecraft.client.resources.model.ResolvableModel;
/*     */ import org.joml.Matrix4fc;
/*     */ 
/*     */ public final class Unbaked extends Record implements ItemModel.Unbaked {
/*     */   private final Optional<Transformation> transformation;
/*     */   
/*     */   private final RangeSelectItemModelProperty property;
/*     */   
/*     */   private final float scale;
/*     */   
/*     */   private final List<RangeSelectItemModel.Entry> entries;
/*     */   
/*     */   private final Optional<ItemModel.Unbaked> fallback;
/*     */   
/*     */   public static final MapCodec<Unbaked> MAP_CODEC;
/*     */   
/*     */   public Unbaked(Optional<Transformation> transformation, RangeSelectItemModelProperty property, float scale, List<RangeSelectItemModel.Entry> entries, Optional<ItemModel.Unbaked> fallback) {
/*  76 */     this.transformation = transformation;
/*  76 */     this.property = property;
/*  76 */     this.scale = scale;
/*  76 */     this.entries = entries;
/*  76 */     this.fallback = fallback;
/*     */   }
/*     */   
/*     */   public final String toString() {
/*     */     // Byte code:
/*     */     //   0: aload_0
/*     */     //   1: <illegal opcode> toString : (Lnet/minecraft/client/renderer/item/RangeSelectItemModel$Unbaked;)Ljava/lang/String;
/*     */     //   6: areturn
/*     */     // Line number table:
/*     */     //   Java source line number -> byte code offset
/*     */     //   #76	-> 0
/*     */     // Local variable table:
/*     */     //   start	length	slot	name	descriptor
/*     */     //   0	7	0	this	Lnet/minecraft/client/renderer/item/RangeSelectItemModel$Unbaked;
/*     */   }
/*     */   
/*     */   public final int hashCode() {
/*     */     // Byte code:
/*     */     //   0: aload_0
/*     */     //   1: <illegal opcode> hashCode : (Lnet/minecraft/client/renderer/item/RangeSelectItemModel$Unbaked;)I
/*     */     //   6: ireturn
/*     */     // Line number table:
/*     */     //   Java source line number -> byte code offset
/*     */     //   #76	-> 0
/*     */     // Local variable table:
/*     */     //   start	length	slot	name	descriptor
/*     */     //   0	7	0	this	Lnet/minecraft/client/renderer/item/RangeSelectItemModel$Unbaked;
/*     */   }
/*     */   
/*     */   public final boolean equals(Object o) {
/*     */     // Byte code:
/*     */     //   0: aload_0
/*     */     //   1: aload_1
/*     */     //   2: <illegal opcode> equals : (Lnet/minecraft/client/renderer/item/RangeSelectItemModel$Unbaked;Ljava/lang/Object;)Z
/*     */     //   7: ireturn
/*     */     // Line number table:
/*     */     //   Java source line number -> byte code offset
/*     */     //   #76	-> 0
/*     */     // Local variable table:
/*     */     //   start	length	slot	name	descriptor
/*     */     //   0	8	0	this	Lnet/minecraft/client/renderer/item/RangeSelectItemModel$Unbaked;
/*     */     //   0	8	1	o	Ljava/lang/Object;
/*     */   }
/*     */   
/*     */   public Optional<Transformation> transformation() {
/*  76 */     return this.transformation;
/*     */   }
/*     */   
/*     */   public RangeSelectItemModelProperty property() {
/*  76 */     return this.property;
/*     */   }
/*     */   
/*     */   public float scale() {
/*  76 */     return this.scale;
/*     */   }
/*     */   
/*     */   public List<RangeSelectItemModel.Entry> entries() {
/*  76 */     return this.entries;
/*     */   }
/*     */   
/*     */   public Optional<ItemModel.Unbaked> fallback() {
/*  76 */     return this.fallback;
/*     */   }
/*     */   
/*     */   static {
/*  83 */     MAP_CODEC = RecordCodecBuilder.mapCodec(i -> i.group((App)Transformation.EXTENDED_CODEC.optionalFieldOf("transformation").forGetter(Unbaked::transformation), (App)RangeSelectItemModelProperties.MAP_CODEC.forGetter(Unbaked::property), (App)Codec.FLOAT.optionalFieldOf("scale", Float.valueOf(1.0F)).forGetter(Unbaked::scale), (App)RangeSelectItemModel.Entry.CODEC.listOf().fieldOf("entries").forGetter(Unbaked::entries), (App)ItemModels.CODEC.optionalFieldOf("fallback").forGetter(Unbaked::fallback)).apply((Applicative)i, Unbaked::new));
/*     */   }
/*     */   
/*     */   public MapCodec<Unbaked> type() {
/*  93 */     return MAP_CODEC;
/*     */   }
/*     */   
/*     */   public ItemModel bake(ItemModel.BakingContext context, Matrix4fc transformation) {
/*  98 */     Matrix4fc childTransform = Transformation.compose(transformation, this.transformation);
/*  99 */     float[] thresholds = new float[this.entries.size()];
/* 100 */     ItemModel[] models = new ItemModel[this.entries.size()];
/* 101 */     List<RangeSelectItemModel.Entry> mutableEntries = new ArrayList<>(this.entries);
/* 102 */     mutableEntries.sort(RangeSelectItemModel.Entry.BY_THRESHOLD);
/* 103 */     for (int i = 0; i < mutableEntries.size(); i++) {
/* 104 */       RangeSelectItemModel.Entry entry = mutableEntries.get(i);
/* 105 */       thresholds[i] = entry.threshold;
/* 106 */       models[i] = entry.model.bake(context, childTransform);
/*     */     } 
/* 108 */     ItemModel bakedFallback = this.fallback.<ItemModel>map(m -> m.bake(context, childTransform)).orElseGet(() -> context.missingItemModel(childTransform));
/* 109 */     return new RangeSelectItemModel(this.property, this.scale, thresholds, models, bakedFallback);
/*     */   }
/*     */   
/*     */   public void resolveDependencies(ResolvableModel.Resolver resolver) {
/* 114 */     this.fallback.ifPresent(m -> m.resolveDependencies(resolver));
/* 115 */     this.entries.forEach(entry -> entry.model.resolveDependencies(resolver));
/*     */   }
/*     */ }


/* Location:              C:\Users\karol\AppData\Roaming\ModrinthApp\meta\versions\26.2-0.19.5\26.2-0.19.5.jar!\net\minecraft\client\renderer\item\RangeSelectItemModel$Unbaked.class
 * Java compiler version: 25 (69.0)
 * JD-Core Version:       1.1.3
 */