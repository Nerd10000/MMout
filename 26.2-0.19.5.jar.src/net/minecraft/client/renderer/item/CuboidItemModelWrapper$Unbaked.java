/*     */ package net.minecraft.client.renderer.item;
/*     */ 
/*     */ import com.mojang.datafixers.kinds.App;
/*     */ import com.mojang.datafixers.kinds.Applicative;
/*     */ import com.mojang.datafixers.util.Function3;
/*     */ import com.mojang.math.Transformation;
/*     */ import com.mojang.serialization.MapCodec;
/*     */ import com.mojang.serialization.codecs.RecordCodecBuilder;
/*     */ import java.util.List;
/*     */ import java.util.Optional;
/*     */ import net.minecraft.client.color.item.ItemTintSource;
/*     */ import net.minecraft.client.color.item.ItemTintSources;
/*     */ import net.minecraft.client.renderer.block.dispatch.BlockModelRotation;
/*     */ import net.minecraft.client.renderer.block.dispatch.ModelState;
/*     */ import net.minecraft.client.resources.model.ModelBaker;
/*     */ import net.minecraft.client.resources.model.ResolvableModel;
/*     */ import net.minecraft.client.resources.model.ResolvedModel;
/*     */ import net.minecraft.client.resources.model.geometry.QuadCollection;
/*     */ import net.minecraft.client.resources.model.sprite.TextureSlots;
/*     */ import net.minecraft.resources.Identifier;
/*     */ import org.joml.Matrix4fc;
/*     */ 
/*     */ public final class Unbaked extends Record implements ItemModel.Unbaked {
/*     */   private final Identifier model;
/*     */   
/*     */   private final Optional<Transformation> transformation;
/*     */   
/*     */   private final List<ItemTintSource> tints;
/*     */   
/*     */   public static final MapCodec<Unbaked> MAP_CODEC;
/*     */   
/*     */   public Unbaked(Identifier model, Optional<Transformation> transformation, List<ItemTintSource> tints) {
/* 116 */     this.model = model;
/* 116 */     this.transformation = transformation;
/* 116 */     this.tints = tints;
/*     */   }
/*     */   
/*     */   public final String toString() {
/*     */     // Byte code:
/*     */     //   0: aload_0
/*     */     //   1: <illegal opcode> toString : (Lnet/minecraft/client/renderer/item/CuboidItemModelWrapper$Unbaked;)Ljava/lang/String;
/*     */     //   6: areturn
/*     */     // Line number table:
/*     */     //   Java source line number -> byte code offset
/*     */     //   #116	-> 0
/*     */     // Local variable table:
/*     */     //   start	length	slot	name	descriptor
/*     */     //   0	7	0	this	Lnet/minecraft/client/renderer/item/CuboidItemModelWrapper$Unbaked;
/*     */   }
/*     */   
/*     */   public final int hashCode() {
/*     */     // Byte code:
/*     */     //   0: aload_0
/*     */     //   1: <illegal opcode> hashCode : (Lnet/minecraft/client/renderer/item/CuboidItemModelWrapper$Unbaked;)I
/*     */     //   6: ireturn
/*     */     // Line number table:
/*     */     //   Java source line number -> byte code offset
/*     */     //   #116	-> 0
/*     */     // Local variable table:
/*     */     //   start	length	slot	name	descriptor
/*     */     //   0	7	0	this	Lnet/minecraft/client/renderer/item/CuboidItemModelWrapper$Unbaked;
/*     */   }
/*     */   
/*     */   public final boolean equals(Object o) {
/*     */     // Byte code:
/*     */     //   0: aload_0
/*     */     //   1: aload_1
/*     */     //   2: <illegal opcode> equals : (Lnet/minecraft/client/renderer/item/CuboidItemModelWrapper$Unbaked;Ljava/lang/Object;)Z
/*     */     //   7: ireturn
/*     */     // Line number table:
/*     */     //   Java source line number -> byte code offset
/*     */     //   #116	-> 0
/*     */     // Local variable table:
/*     */     //   start	length	slot	name	descriptor
/*     */     //   0	8	0	this	Lnet/minecraft/client/renderer/item/CuboidItemModelWrapper$Unbaked;
/*     */     //   0	8	1	o	Ljava/lang/Object;
/*     */   }
/*     */   
/*     */   public Identifier model() {
/* 116 */     return this.model;
/*     */   }
/*     */   
/*     */   public Optional<Transformation> transformation() {
/* 116 */     return this.transformation;
/*     */   }
/*     */   
/*     */   public List<ItemTintSource> tints() {
/* 116 */     return this.tints;
/*     */   }
/*     */   
/*     */   static {
/* 121 */     MAP_CODEC = RecordCodecBuilder.mapCodec(i -> i.group((App)Identifier.CODEC.fieldOf("model").forGetter(Unbaked::model), (App)Transformation.EXTENDED_CODEC.optionalFieldOf("transformation").forGetter(Unbaked::transformation), (App)ItemTintSources.CODEC.listOf().optionalFieldOf("tints", List.of()).forGetter(Unbaked::tints)).apply((Applicative)i, Unbaked::new));
/*     */   }
/*     */   
/*     */   public void resolveDependencies(ResolvableModel.Resolver resolver) {
/* 129 */     resolver.markDependency(this.model);
/*     */   }
/*     */   
/*     */   public ItemModel bake(ItemModel.BakingContext context, Matrix4fc transformation) {
/* 134 */     ModelBaker baker = context.blockModelBaker();
/* 136 */     ResolvedModel resolvedModel = baker.getModel(this.model);
/* 137 */     TextureSlots textureSlots = resolvedModel.getTopTextureSlots();
/* 138 */     QuadCollection quads = resolvedModel.bakeTopGeometry(textureSlots, baker, (ModelState)BlockModelRotation.IDENTITY);
/* 139 */     ModelRenderProperties properties = ModelRenderProperties.fromResolvedModel(baker, resolvedModel, textureSlots);
/* 141 */     CuboidItemModelWrapper.validateAtlasUsage(quads.getAll());
/* 143 */     Matrix4fc modelTransform = Transformation.compose(transformation, this.transformation);
/* 144 */     return new CuboidItemModelWrapper(this.tints, quads, properties, modelTransform);
/*     */   }
/*     */   
/*     */   public MapCodec<Unbaked> type() {
/* 149 */     return MAP_CODEC;
/*     */   }
/*     */ }


/* Location:              C:\Users\karol\AppData\Roaming\ModrinthApp\meta\versions\26.2-0.19.5\26.2-0.19.5.jar!\net\minecraft\client\renderer\item\CuboidItemModelWrapper$Unbaked.class
 * Java compiler version: 25 (69.0)
 * JD-Core Version:       1.1.3
 */