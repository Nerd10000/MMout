/*    */ package net.minecraft.client.renderer.item;
/*    */ 
/*    */ import com.mojang.datafixers.kinds.App;
/*    */ import com.mojang.datafixers.kinds.Applicative;
/*    */ import com.mojang.datafixers.util.Function4;
/*    */ import com.mojang.math.Transformation;
/*    */ import com.mojang.serialization.MapCodec;
/*    */ import com.mojang.serialization.codecs.RecordCodecBuilder;
/*    */ import java.util.Optional;
/*    */ import net.minecraft.client.multiplayer.CacheSlot;
/*    */ import net.minecraft.client.multiplayer.ClientLevel;
/*    */ import net.minecraft.client.renderer.item.properties.conditional.ConditionalItemModelProperties;
/*    */ import net.minecraft.client.renderer.item.properties.conditional.ConditionalItemModelProperty;
/*    */ import net.minecraft.client.renderer.item.properties.conditional.ItemModelPropertyTest;
/*    */ import net.minecraft.client.resources.model.ResolvableModel;
/*    */ import net.minecraft.core.HolderLookup;
/*    */ import net.minecraft.util.RegistryContextSwapper;
/*    */ import net.minecraft.world.entity.LivingEntity;
/*    */ import net.minecraft.world.item.ItemDisplayContext;
/*    */ import net.minecraft.world.item.ItemStack;
/*    */ import org.joml.Matrix4fc;
/*    */ 
/*    */ public final class Unbaked extends Record implements ItemModel.Unbaked {
/*    */   private final Optional<Transformation> transformation;
/*    */   
/*    */   private final ConditionalItemModelProperty property;
/*    */   
/*    */   private final ItemModel.Unbaked onTrue;
/*    */   
/*    */   private final ItemModel.Unbaked onFalse;
/*    */   
/*    */   public static final MapCodec<Unbaked> MAP_CODEC;
/*    */   
/*    */   public Unbaked(Optional<Transformation> transformation, ConditionalItemModelProperty property, ItemModel.Unbaked onTrue, ItemModel.Unbaked onFalse) {
/* 38 */     this.transformation = transformation;
/* 38 */     this.property = property;
/* 38 */     this.onTrue = onTrue;
/* 38 */     this.onFalse = onFalse;
/*    */   }
/*    */   
/*    */   public final String toString() {
/*    */     // Byte code:
/*    */     //   0: aload_0
/*    */     //   1: <illegal opcode> toString : (Lnet/minecraft/client/renderer/item/ConditionalItemModel$Unbaked;)Ljava/lang/String;
/*    */     //   6: areturn
/*    */     // Line number table:
/*    */     //   Java source line number -> byte code offset
/*    */     //   #38	-> 0
/*    */     // Local variable table:
/*    */     //   start	length	slot	name	descriptor
/*    */     //   0	7	0	this	Lnet/minecraft/client/renderer/item/ConditionalItemModel$Unbaked;
/*    */   }
/*    */   
/*    */   public final int hashCode() {
/*    */     // Byte code:
/*    */     //   0: aload_0
/*    */     //   1: <illegal opcode> hashCode : (Lnet/minecraft/client/renderer/item/ConditionalItemModel$Unbaked;)I
/*    */     //   6: ireturn
/*    */     // Line number table:
/*    */     //   Java source line number -> byte code offset
/*    */     //   #38	-> 0
/*    */     // Local variable table:
/*    */     //   start	length	slot	name	descriptor
/*    */     //   0	7	0	this	Lnet/minecraft/client/renderer/item/ConditionalItemModel$Unbaked;
/*    */   }
/*    */   
/*    */   public final boolean equals(Object o) {
/*    */     // Byte code:
/*    */     //   0: aload_0
/*    */     //   1: aload_1
/*    */     //   2: <illegal opcode> equals : (Lnet/minecraft/client/renderer/item/ConditionalItemModel$Unbaked;Ljava/lang/Object;)Z
/*    */     //   7: ireturn
/*    */     // Line number table:
/*    */     //   Java source line number -> byte code offset
/*    */     //   #38	-> 0
/*    */     // Local variable table:
/*    */     //   start	length	slot	name	descriptor
/*    */     //   0	8	0	this	Lnet/minecraft/client/renderer/item/ConditionalItemModel$Unbaked;
/*    */     //   0	8	1	o	Ljava/lang/Object;
/*    */   }
/*    */   
/*    */   public Optional<Transformation> transformation() {
/* 38 */     return this.transformation;
/*    */   }
/*    */   
/*    */   public ConditionalItemModelProperty property() {
/* 38 */     return this.property;
/*    */   }
/*    */   
/*    */   public ItemModel.Unbaked onTrue() {
/* 38 */     return this.onTrue;
/*    */   }
/*    */   
/*    */   public ItemModel.Unbaked onFalse() {
/* 38 */     return this.onFalse;
/*    */   }
/*    */   
/*    */   static {
/* 44 */     MAP_CODEC = RecordCodecBuilder.mapCodec(i -> i.group((App)Transformation.EXTENDED_CODEC.optionalFieldOf("transformation").forGetter(Unbaked::transformation), (App)ConditionalItemModelProperties.MAP_CODEC.forGetter(Unbaked::property), (App)ItemModels.CODEC.fieldOf("on_true").forGetter(Unbaked::onTrue), (App)ItemModels.CODEC.fieldOf("on_false").forGetter(Unbaked::onFalse)).apply((Applicative)i, Unbaked::new));
/*    */   }
/*    */   
/*    */   public MapCodec<Unbaked> type() {
/* 53 */     return MAP_CODEC;
/*    */   }
/*    */   
/*    */   public ItemModel bake(ItemModel.BakingContext context, Matrix4fc transformation) {
/* 58 */     Matrix4fc childTransform = Transformation.compose(transformation, this.transformation);
/* 59 */     return new ConditionalItemModel(
/* 60 */         adaptProperty(this.property, context.contextSwapper()), this.onTrue
/* 61 */         .bake(context, childTransform), this.onFalse
/* 62 */         .bake(context, childTransform));
/*    */   }
/*    */   
/*    */   private ItemModelPropertyTest adaptProperty(ConditionalItemModelProperty originalProperty, RegistryContextSwapper contextSwapper) {
/* 67 */     if (contextSwapper == null)
/* 68 */       return (ItemModelPropertyTest)originalProperty; 
/* 70 */     CacheSlot<ClientLevel, ItemModelPropertyTest> remappedModelCache = new CacheSlot(context -> swapContext(originalProperty, contextSwapper, context));
/* 71 */     return (itemStack, level, owner, seed, displayContext) -> {
/*    */         ItemModelPropertyTest property = (level == null) ? (ItemModelPropertyTest)originalProperty : (ItemModelPropertyTest)remappedModelCache.compute((CacheSlot.Cleaner)level);
/*    */         return property.get(itemStack, level, owner, seed, displayContext);
/*    */       };
/*    */   }
/*    */   
/*    */   private static <T extends ConditionalItemModelProperty> T swapContext(T originalProperty, RegistryContextSwapper contextSwapper, ClientLevel context) {
/* 79 */     return (T)contextSwapper.swapTo(originalProperty.type().codec(), originalProperty, (HolderLookup.Provider)context.registryAccess()).result()
/*    */       
/* 81 */       .orElse((ConditionalItemModelProperty)originalProperty);
/*    */   }
/*    */   
/*    */   public void resolveDependencies(ResolvableModel.Resolver resolver) {
/* 86 */     this.onTrue.resolveDependencies(resolver);
/* 87 */     this.onFalse.resolveDependencies(resolver);
/*    */   }
/*    */ }


/* Location:              C:\Users\karol\AppData\Roaming\ModrinthApp\meta\versions\26.2-0.19.5\26.2-0.19.5.jar!\net\minecraft\client\renderer\item\ConditionalItemModel$Unbaked.class
 * Java compiler version: 25 (69.0)
 * JD-Core Version:       1.1.3
 */