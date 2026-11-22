/*     */ package net.minecraft.client.resources.model.cuboid;
/*     */ 
/*     */ import com.mojang.math.Quadrant;
/*     */ import java.util.Collection;
/*     */ import java.util.HashSet;
/*     */ import java.util.List;
/*     */ import java.util.Set;
/*     */ import net.minecraft.client.renderer.block.dispatch.ModelState;
/*     */ import net.minecraft.client.renderer.texture.SpriteContents;
/*     */ import net.minecraft.client.resources.model.ModelBaker;
/*     */ import net.minecraft.client.resources.model.ModelDebugName;
/*     */ import net.minecraft.client.resources.model.UnbakedModel;
/*     */ import net.minecraft.client.resources.model.geometry.BakedQuad;
/*     */ import net.minecraft.client.resources.model.geometry.QuadCollection;
/*     */ import net.minecraft.client.resources.model.geometry.UnbakedGeometry;
/*     */ import net.minecraft.client.resources.model.sprite.Material;
/*     */ import net.minecraft.client.resources.model.sprite.TextureSlots;
/*     */ import net.minecraft.core.Direction;
/*     */ import net.minecraft.resources.Identifier;
/*     */ import org.joml.Vector3f;
/*     */ import org.joml.Vector3fc;
/*     */ 
/*     */ public class ItemModelGenerator implements UnbakedModel {
/*  26 */   public static final Identifier GENERATED_ITEM_MODEL_ID = Identifier.withDefaultNamespace("builtin/generated");
/*     */   
/*  28 */   public static final List<String> LAYERS = List.of("layer0", "layer1", "layer2", "layer3", "layer4");
/*     */   
/*     */   private static final float MIN_Z = 7.5F;
/*     */   
/*     */   private static final float MAX_Z = 8.5F;
/*     */   
/*  32 */   private static final TextureSlots.Data TEXTURE_SLOTS = (new TextureSlots.Data.Builder()).addReference("particle", "layer0").build();
/*     */   
/*  34 */   private static final CuboidFace.UVs SOUTH_FACE_UVS = new CuboidFace.UVs(0.0F, 0.0F, 16.0F, 16.0F);
/*     */   
/*  35 */   private static final CuboidFace.UVs NORTH_FACE_UVS = new CuboidFace.UVs(16.0F, 0.0F, 0.0F, 16.0F);
/*     */   
/*     */   private static final float UV_SHRINK = 0.1F;
/*     */   
/*     */   public TextureSlots.Data textureSlots() {
/*  40 */     return TEXTURE_SLOTS;
/*     */   }
/*     */   
/*     */   public UnbakedGeometry geometry() {
/*  45 */     return ItemModelGenerator::bake;
/*     */   }
/*     */   
/*     */   public UnbakedModel.GuiLight guiLight() {
/*  50 */     return UnbakedModel.GuiLight.FRONT;
/*     */   }
/*     */   
/*     */   private static QuadCollection bake(TextureSlots textureSlots, ModelBaker modelBaker, ModelState modelState, ModelDebugName name) {
/*  54 */     QuadCollection singleResult = null;
/*  55 */     QuadCollection.Builder builder = null;
/*  57 */     for (int layerIndex = 0; layerIndex < LAYERS.size(); layerIndex++) {
/*  58 */       String textureReference = LAYERS.get(layerIndex);
/*  59 */       Material material = textureSlots.getMaterial(textureReference);
/*  60 */       if (material == null)
/*     */         break; 
/*  63 */       Material.Baked bakedMaterial = modelBaker.materials().get(material, name);
/*  64 */       QuadCollection bakedLayer = (QuadCollection)modelBaker.compute(new ItemLayerKey(bakedMaterial, modelState, layerIndex));
/*  66 */       if (builder != null) {
/*  67 */         builder.addAll(bakedLayer);
/*  68 */       } else if (singleResult != null) {
/*  69 */         builder = new QuadCollection.Builder();
/*  70 */         builder.addAll(singleResult);
/*  71 */         builder.addAll(bakedLayer);
/*  72 */         singleResult = null;
/*     */       } else {
/*  74 */         singleResult = bakedLayer;
/*     */       } 
/*     */     } 
/*  77 */     if (builder != null)
/*  78 */       return builder.build(); 
/*  80 */     return (singleResult != null) ? singleResult : QuadCollection.EMPTY;
/*     */   }
/*     */   
/*     */   private static void bakeExtrudedSprite(QuadCollection.Builder builder, ModelBaker.Interner interner, ModelState modelState, BakedQuad.MaterialInfo materialInfo) {
/*  84 */     Vector3f from = new Vector3f(0.0F, 0.0F, 7.5F);
/*  85 */     Vector3f to = new Vector3f(16.0F, 16.0F, 8.5F);
/*  87 */     builder.addUnculledFace(FaceBakery.bakeQuad(interner, (Vector3fc)from, (Vector3fc)to, SOUTH_FACE_UVS, Quadrant.R0, materialInfo, Direction.SOUTH, modelState, null));
/*  88 */     builder.addUnculledFace(FaceBakery.bakeQuad(interner, (Vector3fc)from, (Vector3fc)to, NORTH_FACE_UVS, Quadrant.R0, materialInfo, Direction.NORTH, modelState, null));
/*  90 */     bakeSideFaces(builder, interner, modelState, materialInfo);
/*     */   }
/*     */   
/*     */   private static void bakeSideFaces(QuadCollection.Builder builder, ModelBaker.Interner interner, ModelState modelState, BakedQuad.MaterialInfo materialInfo) {
/*  94 */     SpriteContents sprite = materialInfo.sprite().contents();
/*  95 */     float xScale = 16.0F / sprite.width();
/*  96 */     float yScale = 16.0F / sprite.height();
/*  97 */     Vector3f from = new Vector3f();
/*  98 */     Vector3f to = new Vector3f();
/*  99 */     for (SideFace sideFace : getSideFaces(sprite)) {
/* 100 */       float v0, v1, x = sideFace.x();
/* 101 */       float y = sideFace.y();
/* 103 */       SideDirection sideDirection = sideFace.facing();
/* 104 */       float u0 = x + 0.1F;
/* 105 */       float u1 = x + 1.0F - 0.1F;
/* 108 */       if (sideDirection.isHorizontal()) {
/* 109 */         v0 = y + 0.1F;
/* 110 */         v1 = y + 1.0F - 0.1F;
/*     */       } else {
/* 112 */         v0 = y + 1.0F - 0.1F;
/* 113 */         v1 = y + 0.1F;
/*     */       } 
/* 116 */       float startX = x;
/* 117 */       float startY = y;
/* 118 */       float endX = x;
/* 119 */       float endY = y;
/* 120 */       switch (sideDirection.ordinal()) {
/*     */         case 0:
/* 121 */           endX++;
/*     */           break;
/*     */         case 1:
/* 123 */           endX++;
/* 124 */           startY++;
/* 125 */           endY++;
/*     */           break;
/*     */         case 2:
/* 127 */           endY++;
/*     */           break;
/*     */         case 3:
/* 129 */           startX++;
/* 130 */           endX++;
/* 131 */           endY++;
/*     */           break;
/*     */       } 
/* 135 */       startX *= xScale;
/* 136 */       endX *= xScale;
/* 137 */       startY *= yScale;
/* 138 */       endY *= yScale;
/* 140 */       startY = 16.0F - startY;
/* 141 */       endY = 16.0F - endY;
/* 143 */       switch (sideDirection.ordinal()) {
/*     */         case 0:
/* 145 */           from.set(startX, startY, 7.5F);
/* 146 */           to.set(endX, startY, 8.5F);
/*     */           break;
/*     */         case 1:
/* 149 */           from.set(startX, endY, 7.5F);
/* 150 */           to.set(endX, endY, 8.5F);
/*     */           break;
/*     */         case 2:
/* 153 */           from.set(startX, startY, 7.5F);
/* 154 */           to.set(startX, endY, 8.5F);
/*     */           break;
/*     */         case 3:
/* 157 */           from.set(endX, startY, 7.5F);
/* 158 */           to.set(endX, endY, 8.5F);
/*     */           break;
/*     */         default:
/* 160 */           throw new UnsupportedOperationException();
/*     */       } 
/* 163 */       CuboidFace.UVs uvs = new CuboidFace.UVs(u0 * xScale, v0 * yScale, u1 * xScale, v1 * yScale);
/* 164 */       builder.addUnculledFace(FaceBakery.bakeQuad(interner, (Vector3fc)from, (Vector3fc)to, uvs, Quadrant.R0, materialInfo, sideDirection.getDirection(), modelState, null));
/*     */     } 
/*     */   }
/*     */   
/*     */   private static Collection<SideFace> getSideFaces(SpriteContents sprite) {
/* 169 */     int width = sprite.width();
/* 170 */     int height = sprite.height();
/* 172 */     Set<SideFace> sideFaces = new HashSet<>();
/* 173 */     sprite.getUniqueFrames().forEach(frame -> {
/*     */           for (int y = 0; y < height; y++) {
/*     */             for (int x = 0; x < width; x++) {
/*     */               boolean thisOpaque = !isTransparent(sprite, frame, x, y, width, height);
/*     */               if (thisOpaque) {
/*     */                 checkTransition(SideDirection.UP, sideFaces, sprite, frame, x, y, width, height);
/*     */                 checkTransition(SideDirection.DOWN, sideFaces, sprite, frame, x, y, width, height);
/*     */                 checkTransition(SideDirection.LEFT, sideFaces, sprite, frame, x, y, width, height);
/*     */                 checkTransition(SideDirection.RIGHT, sideFaces, sprite, frame, x, y, width, height);
/*     */               } 
/*     */             } 
/*     */           } 
/*     */         });
/* 187 */     return sideFaces;
/*     */   }
/*     */   
/*     */   private static void checkTransition(SideDirection facing, Set<SideFace> sideFaces, SpriteContents sprite, int frame, int x, int y, int width, int height) {
/* 191 */     if (isTransparent(sprite, frame, x - facing.direction.getStepX(), y - facing.direction.getStepY(), width, height))
/* 192 */       sideFaces.add(new SideFace(facing, x, y)); 
/*     */   }
/*     */   
/*     */   private static boolean isTransparent(SpriteContents sprite, int frame, int x, int y, int width, int height) {
/* 197 */     if (x < 0 || y < 0 || x >= width || y >= height)
/* 198 */       return true; 
/* 200 */     return sprite.isTransparent(frame, x, y);
/*     */   }
/*     */   
/*     */   private enum SideDirection {
/* 204 */     UP((String)Direction.UP),
/* 205 */     DOWN((String)Direction.DOWN),
/* 206 */     LEFT((String)Direction.EAST),
/* 207 */     RIGHT((String)Direction.WEST);
/*     */     
/*     */     private final Direction direction;
/*     */     
/*     */     SideDirection(Direction direction) {
/* 212 */       this.direction = direction;
/*     */     }
/*     */     
/*     */     public Direction getDirection() {
/* 216 */       return this.direction;
/*     */     }
/*     */     
/*     */     private boolean isHorizontal() {
/* 220 */       return (this == DOWN || this == UP);
/*     */     }
/*     */   }
/*     */   
/*     */   private static final class SideFace extends Record {
/*     */     private final ItemModelGenerator.SideDirection facing;
/*     */     
/*     */     private final int x;
/*     */     
/*     */     private final int y;
/*     */     
/*     */     private SideFace(ItemModelGenerator.SideDirection facing, int x, int y) {
/* 224 */       this.facing = facing;
/* 224 */       this.x = x;
/* 224 */       this.y = y;
/*     */     }
/*     */     
/*     */     public final String toString() {
/*     */       // Byte code:
/*     */       //   0: aload_0
/*     */       //   1: <illegal opcode> toString : (Lnet/minecraft/client/resources/model/cuboid/ItemModelGenerator$SideFace;)Ljava/lang/String;
/*     */       //   6: areturn
/*     */       // Line number table:
/*     */       //   Java source line number -> byte code offset
/*     */       //   #224	-> 0
/*     */       // Local variable table:
/*     */       //   start	length	slot	name	descriptor
/*     */       //   0	7	0	this	Lnet/minecraft/client/resources/model/cuboid/ItemModelGenerator$SideFace;
/*     */     }
/*     */     
/*     */     public final int hashCode() {
/*     */       // Byte code:
/*     */       //   0: aload_0
/*     */       //   1: <illegal opcode> hashCode : (Lnet/minecraft/client/resources/model/cuboid/ItemModelGenerator$SideFace;)I
/*     */       //   6: ireturn
/*     */       // Line number table:
/*     */       //   Java source line number -> byte code offset
/*     */       //   #224	-> 0
/*     */       // Local variable table:
/*     */       //   start	length	slot	name	descriptor
/*     */       //   0	7	0	this	Lnet/minecraft/client/resources/model/cuboid/ItemModelGenerator$SideFace;
/*     */     }
/*     */     
/*     */     public final boolean equals(Object o) {
/*     */       // Byte code:
/*     */       //   0: aload_0
/*     */       //   1: aload_1
/*     */       //   2: <illegal opcode> equals : (Lnet/minecraft/client/resources/model/cuboid/ItemModelGenerator$SideFace;Ljava/lang/Object;)Z
/*     */       //   7: ireturn
/*     */       // Line number table:
/*     */       //   Java source line number -> byte code offset
/*     */       //   #224	-> 0
/*     */       // Local variable table:
/*     */       //   start	length	slot	name	descriptor
/*     */       //   0	8	0	this	Lnet/minecraft/client/resources/model/cuboid/ItemModelGenerator$SideFace;
/*     */       //   0	8	1	o	Ljava/lang/Object;
/*     */     }
/*     */     
/*     */     public ItemModelGenerator.SideDirection facing() {
/* 224 */       return this.facing;
/*     */     }
/*     */     
/*     */     public int x() {
/* 224 */       return this.x;
/*     */     }
/*     */     
/*     */     public int y() {
/* 224 */       return this.y;
/*     */     }
/*     */   }
/*     */   
/*     */   private static final class ItemLayerKey extends Record implements ModelBaker.SharedOperationKey<QuadCollection> {
/*     */     private final Material.Baked material;
/*     */     
/*     */     private final ModelState modelState;
/*     */     
/*     */     private final int layerIndex;
/*     */     
/*     */     private ItemLayerKey(Material.Baked material, ModelState modelState, int layerIndex) {
/* 226 */       this.material = material;
/* 226 */       this.modelState = modelState;
/* 226 */       this.layerIndex = layerIndex;
/*     */     }
/*     */     
/*     */     public final String toString() {
/*     */       // Byte code:
/*     */       //   0: aload_0
/*     */       //   1: <illegal opcode> toString : (Lnet/minecraft/client/resources/model/cuboid/ItemModelGenerator$ItemLayerKey;)Ljava/lang/String;
/*     */       //   6: areturn
/*     */       // Line number table:
/*     */       //   Java source line number -> byte code offset
/*     */       //   #226	-> 0
/*     */       // Local variable table:
/*     */       //   start	length	slot	name	descriptor
/*     */       //   0	7	0	this	Lnet/minecraft/client/resources/model/cuboid/ItemModelGenerator$ItemLayerKey;
/*     */     }
/*     */     
/*     */     public final int hashCode() {
/*     */       // Byte code:
/*     */       //   0: aload_0
/*     */       //   1: <illegal opcode> hashCode : (Lnet/minecraft/client/resources/model/cuboid/ItemModelGenerator$ItemLayerKey;)I
/*     */       //   6: ireturn
/*     */       // Line number table:
/*     */       //   Java source line number -> byte code offset
/*     */       //   #226	-> 0
/*     */       // Local variable table:
/*     */       //   start	length	slot	name	descriptor
/*     */       //   0	7	0	this	Lnet/minecraft/client/resources/model/cuboid/ItemModelGenerator$ItemLayerKey;
/*     */     }
/*     */     
/*     */     public final boolean equals(Object o) {
/*     */       // Byte code:
/*     */       //   0: aload_0
/*     */       //   1: aload_1
/*     */       //   2: <illegal opcode> equals : (Lnet/minecraft/client/resources/model/cuboid/ItemModelGenerator$ItemLayerKey;Ljava/lang/Object;)Z
/*     */       //   7: ireturn
/*     */       // Line number table:
/*     */       //   Java source line number -> byte code offset
/*     */       //   #226	-> 0
/*     */       // Local variable table:
/*     */       //   start	length	slot	name	descriptor
/*     */       //   0	8	0	this	Lnet/minecraft/client/resources/model/cuboid/ItemModelGenerator$ItemLayerKey;
/*     */       //   0	8	1	o	Ljava/lang/Object;
/*     */     }
/*     */     
/*     */     public Material.Baked material() {
/* 226 */       return this.material;
/*     */     }
/*     */     
/*     */     public ModelState modelState() {
/* 226 */       return this.modelState;
/*     */     }
/*     */     
/*     */     public int layerIndex() {
/* 226 */       return this.layerIndex;
/*     */     }
/*     */     
/*     */     public QuadCollection compute(ModelBaker modelBakery) {
/* 229 */       QuadCollection.Builder builder = new QuadCollection.Builder();
/* 231 */       BakedQuad.MaterialInfo materialInfo = modelBakery.interner().materialInfo(BakedQuad.MaterialInfo.of(this.material, this.material.sprite().transparency(), this.layerIndex, true, 0));
/* 232 */       ItemModelGenerator.bakeExtrudedSprite(builder, modelBakery.interner(), this.modelState, materialInfo);
/* 233 */       return builder.build();
/*     */     }
/*     */   }
/*     */ }


/* Location:              C:\Users\karol\AppData\Roaming\ModrinthApp\meta\versions\26.2-0.19.5\26.2-0.19.5.jar!\net\minecraft\client\resources\model\cuboid\ItemModelGenerator.class
 * Java compiler version: 25 (69.0)
 * JD-Core Version:       1.1.3
 */