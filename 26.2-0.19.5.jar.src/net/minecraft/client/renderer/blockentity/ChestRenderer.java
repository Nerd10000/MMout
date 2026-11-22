/*     */ package net.minecraft.client.renderer.blockentity;
/*     */ 
/*     */ import com.mojang.blaze3d.vertex.PoseStack;
/*     */ import com.mojang.math.Axis;
/*     */ import com.mojang.math.Transformation;
/*     */ import java.util.Map;
/*     */ import net.minecraft.client.model.Model;
/*     */ import net.minecraft.client.model.geom.ModelLayerLocation;
/*     */ import net.minecraft.client.model.geom.ModelLayers;
/*     */ import net.minecraft.client.model.object.chest.ChestModel;
/*     */ import net.minecraft.client.renderer.MultiblockChestResources;
/*     */ import net.minecraft.client.renderer.Sheets;
/*     */ import net.minecraft.client.renderer.SubmitNodeCollector;
/*     */ import net.minecraft.client.renderer.blockentity.state.BlockEntityRenderState;
/*     */ import net.minecraft.client.renderer.blockentity.state.ChestRenderState;
/*     */ import net.minecraft.client.renderer.feature.ModelFeatureRenderer;
/*     */ import net.minecraft.client.renderer.state.level.CameraRenderState;
/*     */ import net.minecraft.client.renderer.texture.OverlayTexture;
/*     */ import net.minecraft.client.resources.model.sprite.SpriteGetter;
/*     */ import net.minecraft.client.resources.model.sprite.SpriteId;
/*     */ import net.minecraft.core.Direction;
/*     */ import net.minecraft.util.SpecialDates;
/*     */ import net.minecraft.util.Util;
/*     */ import net.minecraft.world.level.block.Block;
/*     */ import net.minecraft.world.level.block.CopperChestBlock;
/*     */ import net.minecraft.world.level.block.WeatheringCopper;
/*     */ import net.minecraft.world.level.block.entity.BlockEntity;
/*     */ import net.minecraft.world.level.block.entity.LidBlockEntity;
/*     */ import net.minecraft.world.phys.Vec3;
/*     */ import org.joml.Matrix4f;
/*     */ import org.joml.Matrix4fc;
/*     */ import org.joml.Quaternionfc;
/*     */ 
/*     */ public class ChestRenderer<T extends BlockEntity & LidBlockEntity> implements BlockEntityRenderer<T, ChestRenderState> {
/*  40 */   public static final MultiblockChestResources<ModelLayerLocation> LAYERS = new MultiblockChestResources(ModelLayers.CHEST, ModelLayers.DOUBLE_CHEST_LEFT, ModelLayers.DOUBLE_CHEST_RIGHT);
/*     */   
/*  46 */   private static final Map<Direction, Transformation> TRANSFORMATIONS = Util.makeEnumMap(Direction.class, ChestRenderer::createModelTransformation);
/*     */   
/*     */   private final SpriteGetter sprites;
/*     */   
/*     */   private final MultiblockChestResources<ChestModel> models;
/*     */   
/*     */   private final boolean xmasTextures;
/*     */   
/*     */   public ChestRenderer(BlockEntityRendererProvider.Context context) {
/*  55 */     this.sprites = context.sprites();
/*  57 */     this.xmasTextures = xmasTextures();
/*  59 */     this.models = LAYERS.map(layer -> new ChestModel(context.bakeLayer(layer)));
/*     */   }
/*     */   
/*     */   public static boolean xmasTextures() {
/*  63 */     return SpecialDates.isExtendedChristmas();
/*     */   }
/*     */   
/*     */   public ChestRenderState createRenderState() {
/*  68 */     return new ChestRenderState();
/*     */   }
/*     */   
/*     */   public void extractRenderState(T blockEntity, ChestRenderState state, float partialTicks, Vec3 cameraPosition, ModelFeatureRenderer.CrumblingOverlay breakProgress) {
/*     */     // Byte code:
/*     */     //   0: aload_0
/*     */     //   1: aload_1
/*     */     //   2: aload_2
/*     */     //   3: fload_3
/*     */     //   4: aload #4
/*     */     //   6: aload #5
/*     */     //   8: invokespecial extractRenderState : (Lnet/minecraft/world/level/block/entity/BlockEntity;Lnet/minecraft/client/renderer/blockentity/state/BlockEntityRenderState;FLnet/minecraft/world/phys/Vec3;Lnet/minecraft/client/renderer/feature/ModelFeatureRenderer$CrumblingOverlay;)V
/*     */     //   11: aload_1
/*     */     //   12: invokevirtual getLevel : ()Lnet/minecraft/world/level/Level;
/*     */     //   15: ifnull -> 22
/*     */     //   18: iconst_1
/*     */     //   19: goto -> 23
/*     */     //   22: iconst_0
/*     */     //   23: istore #6
/*     */     //   25: iload #6
/*     */     //   27: ifeq -> 37
/*     */     //   30: aload_1
/*     */     //   31: invokevirtual getBlockState : ()Lnet/minecraft/world/level/block/state/BlockState;
/*     */     //   34: goto -> 55
/*     */     //   37: getstatic net/minecraft/world/level/block/Blocks.CHEST : Lnet/minecraft/world/level/block/Block;
/*     */     //   40: invokevirtual defaultBlockState : ()Lnet/minecraft/world/level/block/state/BlockState;
/*     */     //   43: getstatic net/minecraft/world/level/block/ChestBlock.FACING : Lnet/minecraft/world/level/block/state/properties/EnumProperty;
/*     */     //   46: getstatic net/minecraft/core/Direction.SOUTH : Lnet/minecraft/core/Direction;
/*     */     //   49: invokevirtual setValue : (Lnet/minecraft/world/level/block/state/properties/Property;Ljava/lang/Comparable;)Ljava/lang/Object;
/*     */     //   52: checkcast net/minecraft/world/level/block/state/BlockState
/*     */     //   55: astore #7
/*     */     //   57: aload_2
/*     */     //   58: aload #7
/*     */     //   60: getstatic net/minecraft/world/level/block/ChestBlock.TYPE : Lnet/minecraft/world/level/block/state/properties/EnumProperty;
/*     */     //   63: invokevirtual hasProperty : (Lnet/minecraft/world/level/block/state/properties/Property;)Z
/*     */     //   66: ifeq -> 83
/*     */     //   69: aload #7
/*     */     //   71: getstatic net/minecraft/world/level/block/ChestBlock.TYPE : Lnet/minecraft/world/level/block/state/properties/EnumProperty;
/*     */     //   74: invokevirtual getValue : (Lnet/minecraft/world/level/block/state/properties/Property;)Ljava/lang/Comparable;
/*     */     //   77: checkcast net/minecraft/world/level/block/state/properties/ChestType
/*     */     //   80: goto -> 86
/*     */     //   83: getstatic net/minecraft/world/level/block/state/properties/ChestType.SINGLE : Lnet/minecraft/world/level/block/state/properties/ChestType;
/*     */     //   86: putfield type : Lnet/minecraft/world/level/block/state/properties/ChestType;
/*     */     //   89: aload_2
/*     */     //   90: aload #7
/*     */     //   92: getstatic net/minecraft/world/level/block/ChestBlock.FACING : Lnet/minecraft/world/level/block/state/properties/EnumProperty;
/*     */     //   95: invokevirtual getValue : (Lnet/minecraft/world/level/block/state/properties/Property;)Ljava/lang/Comparable;
/*     */     //   98: checkcast net/minecraft/core/Direction
/*     */     //   101: putfield facing : Lnet/minecraft/core/Direction;
/*     */     //   104: aload_2
/*     */     //   105: aload_1
/*     */     //   106: aload_0
/*     */     //   107: getfield xmasTextures : Z
/*     */     //   110: invokestatic getChestMaterial : (Lnet/minecraft/world/level/block/entity/BlockEntity;Z)Lnet/minecraft/client/renderer/blockentity/state/ChestRenderState$ChestMaterialType;
/*     */     //   113: putfield material : Lnet/minecraft/client/renderer/blockentity/state/ChestRenderState$ChestMaterialType;
/*     */     //   116: iload #6
/*     */     //   118: ifeq -> 164
/*     */     //   121: aload #7
/*     */     //   123: invokevirtual getBlock : ()Lnet/minecraft/world/level/block/Block;
/*     */     //   126: astore #10
/*     */     //   128: aload #10
/*     */     //   130: instanceof net/minecraft/world/level/block/ChestBlock
/*     */     //   133: ifeq -> 164
/*     */     //   136: aload #10
/*     */     //   138: checkcast net/minecraft/world/level/block/ChestBlock
/*     */     //   141: astore #9
/*     */     //   143: aload #9
/*     */     //   145: aload #7
/*     */     //   147: aload_1
/*     */     //   148: invokevirtual getLevel : ()Lnet/minecraft/world/level/Level;
/*     */     //   151: aload_1
/*     */     //   152: invokevirtual getBlockPos : ()Lnet/minecraft/core/BlockPos;
/*     */     //   155: iconst_1
/*     */     //   156: invokevirtual combine : (Lnet/minecraft/world/level/block/state/BlockState;Lnet/minecraft/world/level/Level;Lnet/minecraft/core/BlockPos;Z)Lnet/minecraft/world/level/block/DoubleBlockCombiner$NeighborCombineResult;
/*     */     //   159: astore #8
/*     */     //   161: goto -> 171
/*     */     //   164: <illegal opcode> apply : ()Lnet/minecraft/world/level/block/DoubleBlockCombiner$NeighborCombineResult;
/*     */     //   169: astore #8
/*     */     //   171: aload_2
/*     */     //   172: aload #8
/*     */     //   174: aload_1
/*     */     //   175: checkcast net/minecraft/world/level/block/entity/LidBlockEntity
/*     */     //   178: invokestatic opennessCombiner : (Lnet/minecraft/world/level/block/entity/LidBlockEntity;)Lnet/minecraft/world/level/block/DoubleBlockCombiner$Combiner;
/*     */     //   181: invokeinterface apply : (Lnet/minecraft/world/level/block/DoubleBlockCombiner$Combiner;)Ljava/lang/Object;
/*     */     //   186: checkcast it/unimi/dsi/fastutil/floats/Float2FloatFunction
/*     */     //   189: fload_3
/*     */     //   190: invokeinterface get : (F)F
/*     */     //   195: putfield open : F
/*     */     //   198: aload_2
/*     */     //   199: getfield type : Lnet/minecraft/world/level/block/state/properties/ChestType;
/*     */     //   202: getstatic net/minecraft/world/level/block/state/properties/ChestType.SINGLE : Lnet/minecraft/world/level/block/state/properties/ChestType;
/*     */     //   205: if_acmpeq -> 238
/*     */     //   208: aload_2
/*     */     //   209: aload #8
/*     */     //   211: new net/minecraft/client/renderer/blockentity/BrightnessCombiner
/*     */     //   214: dup
/*     */     //   215: invokespecial <init> : ()V
/*     */     //   218: invokeinterface apply : (Lnet/minecraft/world/level/block/DoubleBlockCombiner$Combiner;)Ljava/lang/Object;
/*     */     //   223: checkcast it/unimi/dsi/fastutil/ints/Int2IntFunction
/*     */     //   226: aload_2
/*     */     //   227: getfield lightCoords : I
/*     */     //   230: invokeinterface applyAsInt : (I)I
/*     */     //   235: putfield lightCoords : I
/*     */     //   238: return
/*     */     // Line number table:
/*     */     //   Java source line number -> byte code offset
/*     */     //   #73	-> 0
/*     */     //   #75	-> 11
/*     */     //   #76	-> 25
/*     */     //   #77	-> 57
/*     */     //   #78	-> 89
/*     */     //   #79	-> 104
/*     */     //   #82	-> 116
/*     */     //   #83	-> 143
/*     */     //   #85	-> 164
/*     */     //   #88	-> 171
/*     */     //   #90	-> 198
/*     */     //   #91	-> 208
/*     */     //   #93	-> 238
/*     */     // Local variable table:
/*     */     //   start	length	slot	name	descriptor
/*     */     //   161	3	8	combineResult	Lnet/minecraft/world/level/block/DoubleBlockCombiner$NeighborCombineResult;
/*     */     //   143	21	9	chestBlock	Lnet/minecraft/world/level/block/ChestBlock;
/*     */     //   0	239	0	this	Lnet/minecraft/client/renderer/blockentity/ChestRenderer;
/*     */     //   0	239	1	blockEntity	Lnet/minecraft/world/level/block/entity/BlockEntity;
/*     */     //   0	239	2	state	Lnet/minecraft/client/renderer/blockentity/state/ChestRenderState;
/*     */     //   0	239	3	partialTicks	F
/*     */     //   0	239	4	cameraPosition	Lnet/minecraft/world/phys/Vec3;
/*     */     //   0	239	5	breakProgress	Lnet/minecraft/client/renderer/feature/ModelFeatureRenderer$CrumblingOverlay;
/*     */     //   25	214	6	hasLevel	Z
/*     */     //   57	182	7	blockState	Lnet/minecraft/world/level/block/state/BlockState;
/*     */     //   171	68	8	combineResult	Lnet/minecraft/world/level/block/DoubleBlockCombiner$NeighborCombineResult;
/*     */     // Local variable type table:
/*     */     //   start	length	slot	name	signature
/*     */     //   161	3	8	combineResult	Lnet/minecraft/world/level/block/DoubleBlockCombiner$NeighborCombineResult<+Lnet/minecraft/world/level/block/entity/ChestBlockEntity;>;
/*     */     //   0	239	0	this	Lnet/minecraft/client/renderer/blockentity/ChestRenderer<TT;>;
/*     */     //   0	239	1	blockEntity	TT;
/*     */     //   171	68	8	combineResult	Lnet/minecraft/world/level/block/DoubleBlockCombiner$NeighborCombineResult<+Lnet/minecraft/world/level/block/entity/ChestBlockEntity;>;
/*     */   }
/*     */   
/*     */   public void submit(ChestRenderState state, PoseStack poseStack, SubmitNodeCollector submitNodeCollector, CameraRenderState camera) {
/*  97 */     poseStack.pushPose();
/*  99 */     poseStack.mulPose(modelTransformation(state.facing));
/* 101 */     float open = state.open;
/* 103 */     open = 1.0F - open;
/* 104 */     open = 1.0F - open * open * open;
/* 106 */     SpriteId spriteId = Sheets.chooseSprite(state.material, state.type);
/* 108 */     ChestModel model = (ChestModel)this.models.select(state.type);
/* 109 */     submitNodeCollector.submitModel((Model)model, Float.valueOf(open), poseStack, state.lightCoords, OverlayTexture.NO_OVERLAY, -1, spriteId, this.sprites, 0, state.breakProgress);
/* 111 */     poseStack.popPose();
/*     */   }
/*     */   
/*     */   private static ChestRenderState.ChestMaterialType getChestMaterial(BlockEntity entity, boolean xmasTextures) {
/* 115 */     Block block = entity.getBlockState().getBlock();
/* 115 */     if (block instanceof CopperChestBlock) {
/* 115 */       CopperChestBlock copperChestBlock = (CopperChestBlock)block;
/* 116 */       switch (copperChestBlock.getState()) {
/*     */         default:
/* 116 */           throw new MatchException(null, null);
/*     */         case UNAFFECTED:
/*     */         
/*     */         case EXPOSED:
/*     */         
/*     */         case WEATHERED:
/*     */         
/*     */         case OXIDIZED:
/*     */           break;
/*     */       } 
/* 116 */       return 
/*     */         
/* 120 */         ChestRenderState.ChestMaterialType.COPPER_OXIDIZED;
/*     */     } 
/* 122 */     if (entity instanceof net.minecraft.world.level.block.entity.EnderChestBlockEntity)
/* 123 */       return ChestRenderState.ChestMaterialType.ENDER_CHEST; 
/* 124 */     if (xmasTextures)
/* 125 */       return ChestRenderState.ChestMaterialType.CHRISTMAS; 
/* 126 */     if (entity instanceof net.minecraft.world.level.block.entity.TrappedChestBlockEntity)
/* 127 */       return ChestRenderState.ChestMaterialType.TRAPPED; 
/* 129 */     return ChestRenderState.ChestMaterialType.REGULAR;
/*     */   }
/*     */   
/*     */   public static Transformation modelTransformation(Direction facing) {
/* 133 */     return TRANSFORMATIONS.get(facing);
/*     */   }
/*     */   
/*     */   private static Transformation createModelTransformation(Direction facing) {
/* 137 */     return new Transformation((Matrix4fc)(new Matrix4f()).rotationAround((Quaternionfc)Axis.YP.rotationDegrees(-facing.toYRot()), 0.5F, 0.0F, 0.5F));
/*     */   }
/*     */ }


/* Location:              C:\Users\karol\AppData\Roaming\ModrinthApp\meta\versions\26.2-0.19.5\26.2-0.19.5.jar!\net\minecraft\client\renderer\blockentity\ChestRenderer.class
 * Java compiler version: 25 (69.0)
 * JD-Core Version:       1.1.3
 */