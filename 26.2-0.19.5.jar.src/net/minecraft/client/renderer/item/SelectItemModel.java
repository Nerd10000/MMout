/*     */ package net.minecraft.client.renderer.item;
/*     */ 
/*     */ import com.mojang.datafixers.kinds.App;
/*     */ import com.mojang.datafixers.kinds.Applicative;
/*     */ import com.mojang.datafixers.util.Function3;
/*     */ import com.mojang.math.Transformation;
/*     */ import com.mojang.serialization.Codec;
/*     */ import com.mojang.serialization.MapCodec;
/*     */ import com.mojang.serialization.codecs.RecordCodecBuilder;
/*     */ import it.unimi.dsi.fastutil.objects.Object2ObjectMap;
/*     */ import it.unimi.dsi.fastutil.objects.Object2ObjectOpenHashMap;
/*     */ import java.util.List;
/*     */ import java.util.Optional;
/*     */ import java.util.function.BiFunction;
/*     */ import net.minecraft.client.multiplayer.CacheSlot;
/*     */ import net.minecraft.client.multiplayer.ClientLevel;
/*     */ import net.minecraft.client.renderer.item.properties.select.SelectItemModelProperties;
/*     */ import net.minecraft.client.renderer.item.properties.select.SelectItemModelProperty;
/*     */ import net.minecraft.client.resources.model.ResolvableModel;
/*     */ import net.minecraft.core.HolderLookup;
/*     */ import net.minecraft.util.ExtraCodecs;
/*     */ import net.minecraft.util.RegistryContextSwapper;
/*     */ import net.minecraft.world.entity.ItemOwner;
/*     */ import net.minecraft.world.item.ItemDisplayContext;
/*     */ import net.minecraft.world.item.ItemStack;
/*     */ import org.joml.Matrix4fc;
/*     */ 
/*     */ public class SelectItemModel<T> implements ItemModel {
/*     */   private final SelectItemModelProperty<T> property;
/*     */   
/*     */   private final ModelSelector<T> models;
/*     */   
/*     */   public SelectItemModel(SelectItemModelProperty<T> property, ModelSelector<T> models) {
/*  30 */     this.property = property;
/*  31 */     this.models = models;
/*     */   }
/*     */   
/*     */   public void update(ItemStackRenderState output, ItemStack item, ItemModelResolver resolver, ItemDisplayContext displayContext, ClientLevel level, ItemOwner owner, int seed) {
/*  36 */     output.appendModelIdentityElement(this);
/*  37 */     T value = (T)this.property.get(item, level, (owner == null) ? null : owner.asLivingEntity(), seed, displayContext);
/*  38 */     this.models.get(value, level).update(output, item, resolver, displayContext, level, owner, seed);
/*     */   }
/*     */   
/*     */   public static final class Unbaked extends Record implements ItemModel.Unbaked {
/*     */     private final Optional<Transformation> transformation;
/*     */     
/*     */     private final SelectItemModel.UnbakedSwitch<?, ?> unbakedSwitch;
/*     */     
/*     */     private final Optional<ItemModel.Unbaked> fallback;
/*     */     
/*     */     public static final MapCodec<Unbaked> MAP_CODEC;
/*     */     
/*     */     public Unbaked(Optional<Transformation> transformation, SelectItemModel.UnbakedSwitch<?, ?> unbakedSwitch, Optional<ItemModel.Unbaked> fallback) {
/*  41 */       this.transformation = transformation;
/*  41 */       this.unbakedSwitch = unbakedSwitch;
/*  41 */       this.fallback = fallback;
/*     */     }
/*     */     
/*     */     public final String toString() {
/*     */       // Byte code:
/*     */       //   0: aload_0
/*     */       //   1: <illegal opcode> toString : (Lnet/minecraft/client/renderer/item/SelectItemModel$Unbaked;)Ljava/lang/String;
/*     */       //   6: areturn
/*     */       // Line number table:
/*     */       //   Java source line number -> byte code offset
/*     */       //   #41	-> 0
/*     */       // Local variable table:
/*     */       //   start	length	slot	name	descriptor
/*     */       //   0	7	0	this	Lnet/minecraft/client/renderer/item/SelectItemModel$Unbaked;
/*     */     }
/*     */     
/*     */     public final int hashCode() {
/*     */       // Byte code:
/*     */       //   0: aload_0
/*     */       //   1: <illegal opcode> hashCode : (Lnet/minecraft/client/renderer/item/SelectItemModel$Unbaked;)I
/*     */       //   6: ireturn
/*     */       // Line number table:
/*     */       //   Java source line number -> byte code offset
/*     */       //   #41	-> 0
/*     */       // Local variable table:
/*     */       //   start	length	slot	name	descriptor
/*     */       //   0	7	0	this	Lnet/minecraft/client/renderer/item/SelectItemModel$Unbaked;
/*     */     }
/*     */     
/*     */     public final boolean equals(Object o) {
/*     */       // Byte code:
/*     */       //   0: aload_0
/*     */       //   1: aload_1
/*     */       //   2: <illegal opcode> equals : (Lnet/minecraft/client/renderer/item/SelectItemModel$Unbaked;Ljava/lang/Object;)Z
/*     */       //   7: ireturn
/*     */       // Line number table:
/*     */       //   Java source line number -> byte code offset
/*     */       //   #41	-> 0
/*     */       // Local variable table:
/*     */       //   start	length	slot	name	descriptor
/*     */       //   0	8	0	this	Lnet/minecraft/client/renderer/item/SelectItemModel$Unbaked;
/*     */       //   0	8	1	o	Ljava/lang/Object;
/*     */     }
/*     */     
/*     */     public Optional<Transformation> transformation() {
/*  41 */       return this.transformation;
/*     */     }
/*     */     
/*     */     public SelectItemModel.UnbakedSwitch<?, ?> unbakedSwitch() {
/*  41 */       return this.unbakedSwitch;
/*     */     }
/*     */     
/*     */     public Optional<ItemModel.Unbaked> fallback() {
/*  41 */       return this.fallback;
/*     */     }
/*     */     
/*     */     static {
/*  46 */       MAP_CODEC = RecordCodecBuilder.mapCodec(i -> i.group((App)Transformation.EXTENDED_CODEC.optionalFieldOf("transformation").forGetter(Unbaked::transformation), (App)SelectItemModel.UnbakedSwitch.MAP_CODEC.forGetter(Unbaked::unbakedSwitch), (App)ItemModels.CODEC.optionalFieldOf("fallback").forGetter(Unbaked::fallback)).apply((Applicative)i, Unbaked::new));
/*     */     }
/*     */     
/*     */     public MapCodec<Unbaked> type() {
/*  54 */       return MAP_CODEC;
/*     */     }
/*     */     
/*     */     public ItemModel bake(ItemModel.BakingContext context, Matrix4fc transformation) {
/*  59 */       Matrix4fc childTransform = Transformation.compose(transformation, this.transformation);
/*  60 */       ItemModel bakedFallback = this.fallback.<ItemModel>map(m -> m.bake(context, childTransform)).orElseGet(() -> context.missingItemModel(childTransform));
/*  61 */       return this.unbakedSwitch.bake(context, childTransform, bakedFallback);
/*     */     }
/*     */     
/*     */     public void resolveDependencies(ResolvableModel.Resolver resolver) {
/*  66 */       this.unbakedSwitch.resolveDependencies(resolver);
/*  67 */       this.fallback.ifPresent(m -> m.resolveDependencies(resolver));
/*     */     }
/*     */   }
/*     */   
/*     */   public static final class UnbakedSwitch<P extends SelectItemModelProperty<T>, T> extends Record {
/*     */     private final P property;
/*     */     
/*     */     private final List<SelectItemModel.SwitchCase<T>> cases;
/*     */     
/*     */     public static final MapCodec<UnbakedSwitch<?, ?>> MAP_CODEC;
/*     */     
/*     */     public UnbakedSwitch(P property, List<SelectItemModel.SwitchCase<T>> cases) {
/*  71 */       this.property = property;
/*  71 */       this.cases = cases;
/*     */     }
/*     */     
/*     */     public final String toString() {
/*     */       // Byte code:
/*     */       //   0: aload_0
/*     */       //   1: <illegal opcode> toString : (Lnet/minecraft/client/renderer/item/SelectItemModel$UnbakedSwitch;)Ljava/lang/String;
/*     */       //   6: areturn
/*     */       // Line number table:
/*     */       //   Java source line number -> byte code offset
/*     */       //   #71	-> 0
/*     */       // Local variable table:
/*     */       //   start	length	slot	name	descriptor
/*     */       //   0	7	0	this	Lnet/minecraft/client/renderer/item/SelectItemModel$UnbakedSwitch;
/*     */       // Local variable type table:
/*     */       //   start	length	slot	name	signature
/*     */       //   0	7	0	this	Lnet/minecraft/client/renderer/item/SelectItemModel$UnbakedSwitch<TP;TT;>;
/*     */     }
/*     */     
/*     */     public final int hashCode() {
/*     */       // Byte code:
/*     */       //   0: aload_0
/*     */       //   1: <illegal opcode> hashCode : (Lnet/minecraft/client/renderer/item/SelectItemModel$UnbakedSwitch;)I
/*     */       //   6: ireturn
/*     */       // Line number table:
/*     */       //   Java source line number -> byte code offset
/*     */       //   #71	-> 0
/*     */       // Local variable table:
/*     */       //   start	length	slot	name	descriptor
/*     */       //   0	7	0	this	Lnet/minecraft/client/renderer/item/SelectItemModel$UnbakedSwitch;
/*     */       // Local variable type table:
/*     */       //   start	length	slot	name	signature
/*     */       //   0	7	0	this	Lnet/minecraft/client/renderer/item/SelectItemModel$UnbakedSwitch<TP;TT;>;
/*     */     }
/*     */     
/*     */     public final boolean equals(Object o) {
/*     */       // Byte code:
/*     */       //   0: aload_0
/*     */       //   1: aload_1
/*     */       //   2: <illegal opcode> equals : (Lnet/minecraft/client/renderer/item/SelectItemModel$UnbakedSwitch;Ljava/lang/Object;)Z
/*     */       //   7: ireturn
/*     */       // Line number table:
/*     */       //   Java source line number -> byte code offset
/*     */       //   #71	-> 0
/*     */       // Local variable table:
/*     */       //   start	length	slot	name	descriptor
/*     */       //   0	8	0	this	Lnet/minecraft/client/renderer/item/SelectItemModel$UnbakedSwitch;
/*     */       //   0	8	1	o	Ljava/lang/Object;
/*     */       // Local variable type table:
/*     */       //   start	length	slot	name	signature
/*     */       //   0	8	0	this	Lnet/minecraft/client/renderer/item/SelectItemModel$UnbakedSwitch<TP;TT;>;
/*     */     }
/*     */     
/*     */     public P property() {
/*  71 */       return this.property;
/*     */     }
/*     */     
/*     */     public List<SelectItemModel.SwitchCase<T>> cases() {
/*  71 */       return this.cases;
/*     */     }
/*     */     
/*     */     static {
/*  75 */       MAP_CODEC = SelectItemModelProperties.CODEC.dispatchMap("property", unbaked -> unbaked.property().type(), SelectItemModelProperty.Type::switchCodec);
/*     */     }
/*     */     
/*     */     public ItemModel bake(ItemModel.BakingContext context, Matrix4fc transformation, ItemModel fallback) {
/*  78 */       Object2ObjectOpenHashMap object2ObjectOpenHashMap = new Object2ObjectOpenHashMap();
/*  79 */       for (SelectItemModel.SwitchCase<T> c : this.cases) {
/*  80 */         ItemModel.Unbaked caseModel = c.model;
/*  82 */         ItemModel bakedCaseModel = caseModel.bake(context, transformation);
/*  83 */         for (T value : c.values)
/*  84 */           object2ObjectOpenHashMap.put(value, bakedCaseModel); 
/*     */       } 
/*  88 */       object2ObjectOpenHashMap.defaultReturnValue(fallback);
/*  89 */       return new SelectItemModel<>((SelectItemModelProperty<T>)this.property, createModelGetter((Object2ObjectMap<T, ItemModel>)object2ObjectOpenHashMap, context.contextSwapper()));
/*     */     }
/*     */     
/*     */     private SelectItemModel.ModelSelector<T> createModelGetter(Object2ObjectMap<T, ItemModel> originalModels, RegistryContextSwapper registrySwapper) {
/*  93 */       if (registrySwapper == null)
/*  94 */         return (value, param1ClientLevel) -> (ItemModel)originalModels.get(value); 
/*  96 */       ItemModel defaultModel = (ItemModel)originalModels.defaultReturnValue();
/*  97 */       CacheSlot<ClientLevel, Object2ObjectMap<T, ItemModel>> remappedModelCache = new CacheSlot(clientLevel -> {
/*     */             Object2ObjectOpenHashMap object2ObjectOpenHashMap = new Object2ObjectOpenHashMap(originalModels.size());
/*     */             object2ObjectOpenHashMap.defaultReturnValue(defaultModel);
/*     */             originalModels.forEach(());
/*     */             return (Object2ObjectMap)object2ObjectOpenHashMap;
/*     */           });
/* 107 */       return (value, context) -> (context == null) ? (ItemModel)originalModels.get(value) : ((value == null) ? defaultModel : (ItemModel)((Object2ObjectMap)remappedModelCache.compute((CacheSlot.Cleaner)context)).get(value));
/*     */     }
/*     */     
/*     */     public void resolveDependencies(ResolvableModel.Resolver resolver) {
/* 121 */       for (SelectItemModel.SwitchCase<?> c : this.cases)
/* 122 */         c.model.resolveDependencies(resolver); 
/*     */     }
/*     */   }
/*     */   
/*     */   public static final class SwitchCase<T> extends Record {
/*     */     private final List<T> values;
/*     */     
/*     */     private final ItemModel.Unbaked model;
/*     */     
/*     */     public SwitchCase(List<T> values, ItemModel.Unbaked model) {
/* 127 */       this.values = values;
/* 127 */       this.model = model;
/*     */     }
/*     */     
/*     */     public final String toString() {
/*     */       // Byte code:
/*     */       //   0: aload_0
/*     */       //   1: <illegal opcode> toString : (Lnet/minecraft/client/renderer/item/SelectItemModel$SwitchCase;)Ljava/lang/String;
/*     */       //   6: areturn
/*     */       // Line number table:
/*     */       //   Java source line number -> byte code offset
/*     */       //   #127	-> 0
/*     */       // Local variable table:
/*     */       //   start	length	slot	name	descriptor
/*     */       //   0	7	0	this	Lnet/minecraft/client/renderer/item/SelectItemModel$SwitchCase;
/*     */       // Local variable type table:
/*     */       //   start	length	slot	name	signature
/*     */       //   0	7	0	this	Lnet/minecraft/client/renderer/item/SelectItemModel$SwitchCase<TT;>;
/*     */     }
/*     */     
/*     */     public final int hashCode() {
/*     */       // Byte code:
/*     */       //   0: aload_0
/*     */       //   1: <illegal opcode> hashCode : (Lnet/minecraft/client/renderer/item/SelectItemModel$SwitchCase;)I
/*     */       //   6: ireturn
/*     */       // Line number table:
/*     */       //   Java source line number -> byte code offset
/*     */       //   #127	-> 0
/*     */       // Local variable table:
/*     */       //   start	length	slot	name	descriptor
/*     */       //   0	7	0	this	Lnet/minecraft/client/renderer/item/SelectItemModel$SwitchCase;
/*     */       // Local variable type table:
/*     */       //   start	length	slot	name	signature
/*     */       //   0	7	0	this	Lnet/minecraft/client/renderer/item/SelectItemModel$SwitchCase<TT;>;
/*     */     }
/*     */     
/*     */     public final boolean equals(Object o) {
/*     */       // Byte code:
/*     */       //   0: aload_0
/*     */       //   1: aload_1
/*     */       //   2: <illegal opcode> equals : (Lnet/minecraft/client/renderer/item/SelectItemModel$SwitchCase;Ljava/lang/Object;)Z
/*     */       //   7: ireturn
/*     */       // Line number table:
/*     */       //   Java source line number -> byte code offset
/*     */       //   #127	-> 0
/*     */       // Local variable table:
/*     */       //   start	length	slot	name	descriptor
/*     */       //   0	8	0	this	Lnet/minecraft/client/renderer/item/SelectItemModel$SwitchCase;
/*     */       //   0	8	1	o	Ljava/lang/Object;
/*     */       // Local variable type table:
/*     */       //   start	length	slot	name	signature
/*     */       //   0	8	0	this	Lnet/minecraft/client/renderer/item/SelectItemModel$SwitchCase<TT;>;
/*     */     }
/*     */     
/*     */     public List<T> values() {
/* 127 */       return this.values;
/*     */     }
/*     */     
/*     */     public ItemModel.Unbaked model() {
/* 127 */       return this.model;
/*     */     }
/*     */     
/*     */     public static <T> Codec<SwitchCase<T>> codec(Codec<T> valueCodec) {
/* 132 */       return RecordCodecBuilder.create(i -> i.group((App)ExtraCodecs.nonEmptyList(ExtraCodecs.compactListCodec(valueCodec)).fieldOf("when").forGetter(SwitchCase::values), (App)ItemModels.CODEC.fieldOf("model").forGetter(SwitchCase::model)).apply((Applicative)i, SwitchCase::new));
/*     */     }
/*     */   }
/*     */   
/*     */   @FunctionalInterface
/*     */   public static interface ModelSelector<T> {
/*     */     ItemModel get(T param1T, ClientLevel param1ClientLevel);
/*     */   }
/*     */ }


/* Location:              C:\Users\karol\AppData\Roaming\ModrinthApp\meta\versions\26.2-0.19.5\26.2-0.19.5.jar!\net\minecraft\client\renderer\item\SelectItemModel.class
 * Java compiler version: 25 (69.0)
 * JD-Core Version:       1.1.3
 */