/*     */ package net.minecraft.client.renderer.item;
/*     */ 
/*     */ import com.google.common.base.Suppliers;
/*     */ import com.mojang.datafixers.kinds.App;
/*     */ import com.mojang.datafixers.kinds.Applicative;
/*     */ import com.mojang.datafixers.util.Function3;
/*     */ import com.mojang.math.Transformation;
/*     */ import com.mojang.serialization.MapCodec;
/*     */ import com.mojang.serialization.codecs.RecordCodecBuilder;
/*     */ import it.unimi.dsi.fastutil.ints.IntList;
/*     */ import java.util.HashSet;
/*     */ import java.util.Iterator;
/*     */ import java.util.List;
/*     */ import java.util.Optional;
/*     */ import java.util.Set;
/*     */ import java.util.function.Supplier;
/*     */ import net.minecraft.client.color.item.ItemTintSource;
/*     */ import net.minecraft.client.color.item.ItemTintSources;
/*     */ import net.minecraft.client.multiplayer.ClientLevel;
/*     */ import net.minecraft.client.renderer.block.dispatch.BlockModelRotation;
/*     */ import net.minecraft.client.renderer.block.dispatch.ModelState;
/*     */ import net.minecraft.client.renderer.texture.TextureAtlas;
/*     */ import net.minecraft.client.resources.model.ModelBaker;
/*     */ import net.minecraft.client.resources.model.ResolvableModel;
/*     */ import net.minecraft.client.resources.model.ResolvedModel;
/*     */ import net.minecraft.client.resources.model.geometry.BakedQuad;
/*     */ import net.minecraft.client.resources.model.geometry.QuadCollection;
/*     */ import net.minecraft.client.resources.model.sprite.TextureSlots;
/*     */ import net.minecraft.resources.Identifier;
/*     */ import net.minecraft.tags.ItemTags;
/*     */ import net.minecraft.world.entity.ItemOwner;
/*     */ import net.minecraft.world.item.ItemDisplayContext;
/*     */ import net.minecraft.world.item.ItemStack;
/*     */ import net.minecraft.world.item.Items;
/*     */ import org.joml.Matrix4fc;
/*     */ import org.joml.Vector3fc;
/*     */ 
/*     */ public class CuboidItemModelWrapper implements ItemModel {
/*     */   private final List<ItemTintSource> tints;
/*     */   
/*     */   private final QuadCollection quads;
/*     */   
/*     */   private final Supplier<Vector3fc[]> extents;
/*     */   
/*     */   private final ModelRenderProperties properties;
/*     */   
/*     */   private final Matrix4fc transformation;
/*     */   
/*     */   private CuboidItemModelWrapper(List<ItemTintSource> tints, QuadCollection quads, ModelRenderProperties properties, Matrix4fc transformation) {
/*  43 */     this.tints = tints;
/*  44 */     this.quads = quads;
/*  45 */     this.properties = properties;
/*  46 */     this.transformation = transformation;
/*  47 */     this.extents = (Supplier<Vector3fc[]>)Suppliers.memoize(() -> computeExtents(quads.getAll()));
/*     */   }
/*     */   
/*     */   public static Vector3fc[] computeExtents(List<BakedQuad> quads) {
/*  51 */     Set<Vector3fc> result = new HashSet<>();
/*  52 */     for (BakedQuad quad : quads) {
/*  53 */       for (int vertex = 0; vertex < 4; vertex++)
/*  54 */         result.add(quad.position(vertex)); 
/*     */     } 
/*  57 */     return (Vector3fc[])result.toArray(x$0 -> new Vector3fc[x$0]);
/*     */   }
/*     */   
/*     */   public void update(ItemStackRenderState output, ItemStack item, ItemModelResolver resolver, ItemDisplayContext displayContext, ClientLevel level, ItemOwner owner, int seed) {
/*  62 */     output.appendModelIdentityElement(this);
/*  63 */     ItemStackRenderState.LayerRenderState layer = output.newLayer();
/*  64 */     if (item.hasFoil()) {
/*  65 */       ItemStackRenderState.FoilType foilType = hasSpecialAnimatedTexture(item) ? ItemStackRenderState.FoilType.SPECIAL : ItemStackRenderState.FoilType.STANDARD;
/*  66 */       layer.setFoilType(foilType);
/*  67 */       output.setAnimated();
/*  68 */       output.appendModelIdentityElement(foilType);
/*     */     } 
/*  71 */     if (!this.tints.isEmpty()) {
/*  72 */       IntList tintLayers = layer.tintLayers();
/*  73 */       for (ItemTintSource tintSource : this.tints) {
/*  74 */         int tint = tintSource.calculate(item, level, (owner == null) ? null : owner.asLivingEntity());
/*  75 */         tintLayers.add(tint);
/*  76 */         output.appendModelIdentityElement(Integer.valueOf(tint));
/*     */       } 
/*     */     } 
/*  80 */     layer.setExtents(this.extents);
/*  81 */     layer.setLocalTransform(this.transformation);
/*  83 */     this.properties.applyToLayer(layer, displayContext);
/*  84 */     layer.prepareQuadList().addAll(this.quads.getAll());
/*  85 */     if (this.quads.hasMaterialFlag(2))
/*  86 */       output.setAnimated(); 
/*     */   }
/*     */   
/*     */   private static void validateAtlasUsage(List<BakedQuad> quads) {
/*  91 */     Iterator<BakedQuad> quadIterator = quads.iterator();
/*  92 */     if (!quadIterator.hasNext())
/*     */       return; 
/*  98 */     Identifier expectedAtlas = ((BakedQuad)quadIterator.next()).materialInfo().sprite().atlasLocation();
/*  99 */     while (quadIterator.hasNext()) {
/* 100 */       BakedQuad quad = quadIterator.next();
/* 101 */       Identifier quadAtlas = quad.materialInfo().sprite().atlasLocation();
/* 102 */       if (!quadAtlas.equals(expectedAtlas))
/* 103 */         throw new IllegalStateException("Multiple atlases used in model, expected " + String.valueOf(expectedAtlas) + ", but also got " + String.valueOf(quadAtlas)); 
/*     */     } 
/* 107 */     if (!expectedAtlas.equals(TextureAtlas.LOCATION_ITEMS) && !expectedAtlas.equals(TextureAtlas.LOCATION_BLOCKS))
/* 108 */       throw new IllegalArgumentException("Atlas " + String.valueOf(expectedAtlas) + " can't be usef for item models"); 
/*     */   }
/*     */   
/*     */   private static boolean hasSpecialAnimatedTexture(ItemStack itemStack) {
/* 113 */     return (itemStack.is(ItemTags.COMPASSES) || itemStack.is(Items.CLOCK));
/*     */   }
/*     */   
/*     */   public static final class Unbaked extends Record implements ItemModel.Unbaked {
/*     */     private final Identifier model;
/*     */     
/*     */     private final Optional<Transformation> transformation;
/*     */     
/*     */     private final List<ItemTintSource> tints;
/*     */     
/*     */     public static final MapCodec<Unbaked> MAP_CODEC;
/*     */     
/*     */     public Unbaked(Identifier model, Optional<Transformation> transformation, List<ItemTintSource> tints) {
/* 116 */       this.model = model;
/* 116 */       this.transformation = transformation;
/* 116 */       this.tints = tints;
/*     */     }
/*     */     
/*     */     public final String toString() {
/*     */       // Byte code:
/*     */       //   0: aload_0
/*     */       //   1: <illegal opcode> toString : (Lnet/minecraft/client/renderer/item/CuboidItemModelWrapper$Unbaked;)Ljava/lang/String;
/*     */       //   6: areturn
/*     */       // Line number table:
/*     */       //   Java source line number -> byte code offset
/*     */       //   #116	-> 0
/*     */       // Local variable table:
/*     */       //   start	length	slot	name	descriptor
/*     */       //   0	7	0	this	Lnet/minecraft/client/renderer/item/CuboidItemModelWrapper$Unbaked;
/*     */     }
/*     */     
/*     */     public final int hashCode() {
/*     */       // Byte code:
/*     */       //   0: aload_0
/*     */       //   1: <illegal opcode> hashCode : (Lnet/minecraft/client/renderer/item/CuboidItemModelWrapper$Unbaked;)I
/*     */       //   6: ireturn
/*     */       // Line number table:
/*     */       //   Java source line number -> byte code offset
/*     */       //   #116	-> 0
/*     */       // Local variable table:
/*     */       //   start	length	slot	name	descriptor
/*     */       //   0	7	0	this	Lnet/minecraft/client/renderer/item/CuboidItemModelWrapper$Unbaked;
/*     */     }
/*     */     
/*     */     public final boolean equals(Object o) {
/*     */       // Byte code:
/*     */       //   0: aload_0
/*     */       //   1: aload_1
/*     */       //   2: <illegal opcode> equals : (Lnet/minecraft/client/renderer/item/CuboidItemModelWrapper$Unbaked;Ljava/lang/Object;)Z
/*     */       //   7: ireturn
/*     */       // Line number table:
/*     */       //   Java source line number -> byte code offset
/*     */       //   #116	-> 0
/*     */       // Local variable table:
/*     */       //   start	length	slot	name	descriptor
/*     */       //   0	8	0	this	Lnet/minecraft/client/renderer/item/CuboidItemModelWrapper$Unbaked;
/*     */       //   0	8	1	o	Ljava/lang/Object;
/*     */     }
/*     */     
/*     */     public Identifier model() {
/* 116 */       return this.model;
/*     */     }
/*     */     
/*     */     public Optional<Transformation> transformation() {
/* 116 */       return this.transformation;
/*     */     }
/*     */     
/*     */     public List<ItemTintSource> tints() {
/* 116 */       return this.tints;
/*     */     }
/*     */     
/*     */     static {
/* 121 */       MAP_CODEC = RecordCodecBuilder.mapCodec(i -> i.group((App)Identifier.CODEC.fieldOf("model").forGetter(Unbaked::model), (App)Transformation.EXTENDED_CODEC.optionalFieldOf("transformation").forGetter(Unbaked::transformation), (App)ItemTintSources.CODEC.listOf().optionalFieldOf("tints", List.of()).forGetter(Unbaked::tints)).apply((Applicative)i, Unbaked::new));
/*     */     }
/*     */     
/*     */     public void resolveDependencies(ResolvableModel.Resolver resolver) {
/* 129 */       resolver.markDependency(this.model);
/*     */     }
/*     */     
/*     */     public ItemModel bake(ItemModel.BakingContext context, Matrix4fc transformation) {
/* 134 */       ModelBaker baker = context.blockModelBaker();
/* 136 */       ResolvedModel resolvedModel = baker.getModel(this.model);
/* 137 */       TextureSlots textureSlots = resolvedModel.getTopTextureSlots();
/* 138 */       QuadCollection quads = resolvedModel.bakeTopGeometry(textureSlots, baker, (ModelState)BlockModelRotation.IDENTITY);
/* 139 */       ModelRenderProperties properties = ModelRenderProperties.fromResolvedModel(baker, resolvedModel, textureSlots);
/* 141 */       CuboidItemModelWrapper.validateAtlasUsage(quads.getAll());
/* 143 */       Matrix4fc modelTransform = Transformation.compose(transformation, this.transformation);
/* 144 */       return new CuboidItemModelWrapper(this.tints, quads, properties, modelTransform);
/*     */     }
/*     */     
/*     */     public MapCodec<Unbaked> type() {
/* 149 */       return MAP_CODEC;
/*     */     }
/*     */   }
/*     */ }


/* Location:              C:\Users\karol\AppData\Roaming\ModrinthApp\meta\versions\26.2-0.19.5\26.2-0.19.5.jar!\net\minecraft\client\renderer\item\CuboidItemModelWrapper.class
 * Java compiler version: 25 (69.0)
 * JD-Core Version:       1.1.3
 */