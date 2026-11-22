/*     */ package net.minecraft.client.renderer.item;
/*     */ 
/*     */ import com.mojang.datafixers.kinds.App;
/*     */ import com.mojang.datafixers.kinds.Applicative;
/*     */ import com.mojang.datafixers.util.Function3;
/*     */ import com.mojang.math.Transformation;
/*     */ import com.mojang.serialization.MapCodec;
/*     */ import com.mojang.serialization.codecs.RecordCodecBuilder;
/*     */ import java.util.Optional;
/*     */ import net.minecraft.client.renderer.special.SpecialModelRenderer;
/*     */ import net.minecraft.client.renderer.special.SpecialModelRenderers;
/*     */ import net.minecraft.client.resources.model.ModelBaker;
/*     */ import net.minecraft.client.resources.model.ResolvableModel;
/*     */ import net.minecraft.client.resources.model.ResolvedModel;
/*     */ import net.minecraft.client.resources.model.sprite.TextureSlots;
/*     */ import net.minecraft.resources.Identifier;
/*     */ import org.joml.Matrix4fc;
/*     */ 
/*     */ public final class Unbaked extends Record implements ItemModel.Unbaked {
/*     */   private final Identifier base;
/*     */   
/*     */   private final Optional<Transformation> transformation;
/*     */   
/*     */   private final SpecialModelRenderer.Unbaked<?> specialModel;
/*     */   
/*     */   public static final MapCodec<Unbaked> MAP_CODEC;
/*     */   
/*     */   public Unbaked(Identifier base, Optional<Transformation> transformation, SpecialModelRenderer.Unbaked<?> specialModel) {
/*  65 */     this.base = base;
/*  65 */     this.transformation = transformation;
/*  65 */     this.specialModel = specialModel;
/*     */   }
/*     */   
/*     */   public final String toString() {
/*     */     // Byte code:
/*     */     //   0: aload_0
/*     */     //   1: <illegal opcode> toString : (Lnet/minecraft/client/renderer/item/SpecialModelWrapper$Unbaked;)Ljava/lang/String;
/*     */     //   6: areturn
/*     */     // Line number table:
/*     */     //   Java source line number -> byte code offset
/*     */     //   #65	-> 0
/*     */     // Local variable table:
/*     */     //   start	length	slot	name	descriptor
/*     */     //   0	7	0	this	Lnet/minecraft/client/renderer/item/SpecialModelWrapper$Unbaked;
/*     */   }
/*     */   
/*     */   public final int hashCode() {
/*     */     // Byte code:
/*     */     //   0: aload_0
/*     */     //   1: <illegal opcode> hashCode : (Lnet/minecraft/client/renderer/item/SpecialModelWrapper$Unbaked;)I
/*     */     //   6: ireturn
/*     */     // Line number table:
/*     */     //   Java source line number -> byte code offset
/*     */     //   #65	-> 0
/*     */     // Local variable table:
/*     */     //   start	length	slot	name	descriptor
/*     */     //   0	7	0	this	Lnet/minecraft/client/renderer/item/SpecialModelWrapper$Unbaked;
/*     */   }
/*     */   
/*     */   public final boolean equals(Object o) {
/*     */     // Byte code:
/*     */     //   0: aload_0
/*     */     //   1: aload_1
/*     */     //   2: <illegal opcode> equals : (Lnet/minecraft/client/renderer/item/SpecialModelWrapper$Unbaked;Ljava/lang/Object;)Z
/*     */     //   7: ireturn
/*     */     // Line number table:
/*     */     //   Java source line number -> byte code offset
/*     */     //   #65	-> 0
/*     */     // Local variable table:
/*     */     //   start	length	slot	name	descriptor
/*     */     //   0	8	0	this	Lnet/minecraft/client/renderer/item/SpecialModelWrapper$Unbaked;
/*     */     //   0	8	1	o	Ljava/lang/Object;
/*     */   }
/*     */   
/*     */   public Identifier base() {
/*  65 */     return this.base;
/*     */   }
/*     */   
/*     */   public Optional<Transformation> transformation() {
/*  65 */     return this.transformation;
/*     */   }
/*     */   
/*     */   public SpecialModelRenderer.Unbaked<?> specialModel() {
/*  65 */     return this.specialModel;
/*     */   }
/*     */   
/*     */   static {
/*  70 */     MAP_CODEC = RecordCodecBuilder.mapCodec(i -> i.group((App)Identifier.CODEC.fieldOf("base").forGetter(Unbaked::base), (App)Transformation.EXTENDED_CODEC.optionalFieldOf("transformation").forGetter(Unbaked::transformation), (App)SpecialModelRenderers.CODEC.fieldOf("model").forGetter(Unbaked::specialModel)).apply((Applicative)i, Unbaked::new));
/*     */   }
/*     */   
/*     */   public void resolveDependencies(ResolvableModel.Resolver resolver) {
/*  78 */     resolver.markDependency(this.base);
/*     */   }
/*     */   
/*     */   public ItemModel bake(ItemModel.BakingContext context, Matrix4fc transformation) {
/*  83 */     Matrix4fc modelTransform = Transformation.compose(transformation, this.transformation);
/*  85 */     SpecialModelRenderer<?> bakedSpecialModel = this.specialModel.bake(context);
/*  86 */     if (bakedSpecialModel == null)
/*  87 */       return context.missingItemModel(modelTransform); 
/*  89 */     ModelRenderProperties properties = getProperties(context);
/*  90 */     return new SpecialModelWrapper(bakedSpecialModel, properties, modelTransform);
/*     */   }
/*     */   
/*     */   private ModelRenderProperties getProperties(ItemModel.BakingContext context) {
/*  94 */     ModelBaker baker = context.blockModelBaker();
/*  96 */     ResolvedModel model = baker.getModel(this.base);
/*  97 */     TextureSlots textureSlots = model.getTopTextureSlots();
/*  98 */     return ModelRenderProperties.fromResolvedModel(baker, model, textureSlots);
/*     */   }
/*     */   
/*     */   public MapCodec<Unbaked> type() {
/* 103 */     return MAP_CODEC;
/*     */   }
/*     */ }


/* Location:              C:\Users\karol\AppData\Roaming\ModrinthApp\meta\versions\26.2-0.19.5\26.2-0.19.5.jar!\net\minecraft\client\renderer\item\SpecialModelWrapper$Unbaked.class
 * Java compiler version: 25 (69.0)
 * JD-Core Version:       1.1.3
 */