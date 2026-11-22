/*     */ package net.minecraft.client.renderer.item;
/*     */ 
/*     */ import com.mojang.serialization.MapCodec;
/*     */ import it.unimi.dsi.fastutil.objects.Object2ObjectMap;
/*     */ import it.unimi.dsi.fastutil.objects.Object2ObjectOpenHashMap;
/*     */ import java.util.List;
/*     */ import net.minecraft.client.multiplayer.CacheSlot;
/*     */ import net.minecraft.client.multiplayer.ClientLevel;
/*     */ import net.minecraft.client.renderer.item.properties.select.SelectItemModelProperties;
/*     */ import net.minecraft.client.renderer.item.properties.select.SelectItemModelProperty;
/*     */ import net.minecraft.client.resources.model.ResolvableModel;
/*     */ import net.minecraft.core.HolderLookup;
/*     */ import net.minecraft.util.RegistryContextSwapper;
/*     */ import org.joml.Matrix4fc;
/*     */ 
/*     */ public final class UnbakedSwitch<P extends SelectItemModelProperty<T>, T> extends Record {
/*     */   private final P property;
/*     */   
/*     */   private final List<SelectItemModel.SwitchCase<T>> cases;
/*     */   
/*     */   public static final MapCodec<UnbakedSwitch<?, ?>> MAP_CODEC;
/*     */   
/*     */   public UnbakedSwitch(P property, List<SelectItemModel.SwitchCase<T>> cases) {
/*  71 */     this.property = property;
/*  71 */     this.cases = cases;
/*     */   }
/*     */   
/*     */   public final String toString() {
/*     */     // Byte code:
/*     */     //   0: aload_0
/*     */     //   1: <illegal opcode> toString : (Lnet/minecraft/client/renderer/item/SelectItemModel$UnbakedSwitch;)Ljava/lang/String;
/*     */     //   6: areturn
/*     */     // Line number table:
/*     */     //   Java source line number -> byte code offset
/*     */     //   #71	-> 0
/*     */     // Local variable table:
/*     */     //   start	length	slot	name	descriptor
/*     */     //   0	7	0	this	Lnet/minecraft/client/renderer/item/SelectItemModel$UnbakedSwitch;
/*     */     // Local variable type table:
/*     */     //   start	length	slot	name	signature
/*     */     //   0	7	0	this	Lnet/minecraft/client/renderer/item/SelectItemModel$UnbakedSwitch<TP;TT;>;
/*     */   }
/*     */   
/*     */   public final int hashCode() {
/*     */     // Byte code:
/*     */     //   0: aload_0
/*     */     //   1: <illegal opcode> hashCode : (Lnet/minecraft/client/renderer/item/SelectItemModel$UnbakedSwitch;)I
/*     */     //   6: ireturn
/*     */     // Line number table:
/*     */     //   Java source line number -> byte code offset
/*     */     //   #71	-> 0
/*     */     // Local variable table:
/*     */     //   start	length	slot	name	descriptor
/*     */     //   0	7	0	this	Lnet/minecraft/client/renderer/item/SelectItemModel$UnbakedSwitch;
/*     */     // Local variable type table:
/*     */     //   start	length	slot	name	signature
/*     */     //   0	7	0	this	Lnet/minecraft/client/renderer/item/SelectItemModel$UnbakedSwitch<TP;TT;>;
/*     */   }
/*     */   
/*     */   public final boolean equals(Object o) {
/*     */     // Byte code:
/*     */     //   0: aload_0
/*     */     //   1: aload_1
/*     */     //   2: <illegal opcode> equals : (Lnet/minecraft/client/renderer/item/SelectItemModel$UnbakedSwitch;Ljava/lang/Object;)Z
/*     */     //   7: ireturn
/*     */     // Line number table:
/*     */     //   Java source line number -> byte code offset
/*     */     //   #71	-> 0
/*     */     // Local variable table:
/*     */     //   start	length	slot	name	descriptor
/*     */     //   0	8	0	this	Lnet/minecraft/client/renderer/item/SelectItemModel$UnbakedSwitch;
/*     */     //   0	8	1	o	Ljava/lang/Object;
/*     */     // Local variable type table:
/*     */     //   start	length	slot	name	signature
/*     */     //   0	8	0	this	Lnet/minecraft/client/renderer/item/SelectItemModel$UnbakedSwitch<TP;TT;>;
/*     */   }
/*     */   
/*     */   public P property() {
/*  71 */     return this.property;
/*     */   }
/*     */   
/*     */   public List<SelectItemModel.SwitchCase<T>> cases() {
/*  71 */     return this.cases;
/*     */   }
/*     */   
/*     */   static {
/*  75 */     MAP_CODEC = SelectItemModelProperties.CODEC.dispatchMap("property", unbaked -> unbaked.property().type(), SelectItemModelProperty.Type::switchCodec);
/*     */   }
/*     */   
/*     */   public ItemModel bake(ItemModel.BakingContext context, Matrix4fc transformation, ItemModel fallback) {
/*  78 */     Object2ObjectOpenHashMap object2ObjectOpenHashMap = new Object2ObjectOpenHashMap();
/*  79 */     for (SelectItemModel.SwitchCase<T> c : this.cases) {
/*  80 */       ItemModel.Unbaked caseModel = c.model;
/*  82 */       ItemModel bakedCaseModel = caseModel.bake(context, transformation);
/*  83 */       for (T value : c.values)
/*  84 */         object2ObjectOpenHashMap.put(value, bakedCaseModel); 
/*     */     } 
/*  88 */     object2ObjectOpenHashMap.defaultReturnValue(fallback);
/*  89 */     return new SelectItemModel<>((SelectItemModelProperty<T>)this.property, createModelGetter((Object2ObjectMap<T, ItemModel>)object2ObjectOpenHashMap, context.contextSwapper()));
/*     */   }
/*     */   
/*     */   private SelectItemModel.ModelSelector<T> createModelGetter(Object2ObjectMap<T, ItemModel> originalModels, RegistryContextSwapper registrySwapper) {
/*  93 */     if (registrySwapper == null)
/*  94 */       return (value, paramClientLevel) -> (ItemModel)originalModels.get(value); 
/*  96 */     ItemModel defaultModel = (ItemModel)originalModels.defaultReturnValue();
/*  97 */     CacheSlot<ClientLevel, Object2ObjectMap<T, ItemModel>> remappedModelCache = new CacheSlot(clientLevel -> {
/*     */           Object2ObjectOpenHashMap object2ObjectOpenHashMap = new Object2ObjectOpenHashMap(originalModels.size());
/*     */           object2ObjectOpenHashMap.defaultReturnValue(defaultModel);
/*     */           originalModels.forEach(());
/*     */           return (Object2ObjectMap)object2ObjectOpenHashMap;
/*     */         });
/* 107 */     return (value, context) -> (context == null) ? (ItemModel)originalModels.get(value) : ((value == null) ? defaultModel : (ItemModel)((Object2ObjectMap)remappedModelCache.compute((CacheSlot.Cleaner)context)).get(value));
/*     */   }
/*     */   
/*     */   public void resolveDependencies(ResolvableModel.Resolver resolver) {
/* 121 */     for (SelectItemModel.SwitchCase<?> c : this.cases)
/* 122 */       c.model.resolveDependencies(resolver); 
/*     */   }
/*     */ }


/* Location:              C:\Users\karol\AppData\Roaming\ModrinthApp\meta\versions\26.2-0.19.5\26.2-0.19.5.jar!\net\minecraft\client\renderer\item\SelectItemModel$UnbakedSwitch.class
 * Java compiler version: 25 (69.0)
 * JD-Core Version:       1.1.3
 */