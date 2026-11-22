/*     */ package net.minecraft.client.renderer.block;
/*     */ 
/*     */ import com.google.common.collect.UnmodifiableIterator;
/*     */ import com.mojang.math.Axis;
/*     */ import com.mojang.math.Transformation;
/*     */ import java.util.HashMap;
/*     */ import java.util.List;
/*     */ import java.util.Map;
/*     */ import java.util.Optional;
/*     */ import java.util.function.BiFunction;
/*     */ import java.util.function.Function;
/*     */ import net.minecraft.client.color.block.BlockColors;
/*     */ import net.minecraft.client.color.block.BlockTintSource;
/*     */ import net.minecraft.client.renderer.MultiblockChestResources;
/*     */ import net.minecraft.client.renderer.block.model.BlockModel;
/*     */ import net.minecraft.client.renderer.block.model.BlockStateModelWrapper;
/*     */ import net.minecraft.client.renderer.block.model.CompositeBlockModel;
/*     */ import net.minecraft.client.renderer.block.model.ConditionalBlockModel;
/*     */ import net.minecraft.client.renderer.block.model.EmptyBlockModel;
/*     */ import net.minecraft.client.renderer.block.model.SpecialBlockModelWrapper;
/*     */ import net.minecraft.client.renderer.block.model.properties.conditional.ConditionalBlockModelProperty;
/*     */ import net.minecraft.client.renderer.block.model.properties.conditional.IsXmas;
/*     */ import net.minecraft.client.renderer.block.model.properties.select.DisplayContext;
/*     */ import net.minecraft.client.renderer.blockentity.BannerRenderer;
/*     */ import net.minecraft.client.renderer.blockentity.ChestRenderer;
/*     */ import net.minecraft.client.renderer.blockentity.ConduitRenderer;
/*     */ import net.minecraft.client.renderer.blockentity.CopperGolemStatueBlockRenderer;
/*     */ import net.minecraft.client.renderer.blockentity.DecoratedPotRenderer;
/*     */ import net.minecraft.client.renderer.blockentity.ShulkerBoxRenderer;
/*     */ import net.minecraft.client.renderer.blockentity.SkullBlockRenderer;
/*     */ import net.minecraft.client.renderer.blockentity.TheEndPortalRenderer;
/*     */ import net.minecraft.client.renderer.entity.CopperGolemRenderer;
/*     */ import net.minecraft.client.renderer.special.BannerSpecialRenderer;
/*     */ import net.minecraft.client.renderer.special.BellSpecialRenderer;
/*     */ import net.minecraft.client.renderer.special.BookSpecialRenderer;
/*     */ import net.minecraft.client.renderer.special.ChestSpecialRenderer;
/*     */ import net.minecraft.client.renderer.special.ConduitSpecialRenderer;
/*     */ import net.minecraft.client.renderer.special.CopperGolemStatueSpecialRenderer;
/*     */ import net.minecraft.client.renderer.special.DecoratedPotSpecialRenderer;
/*     */ import net.minecraft.client.renderer.special.EndCubeSpecialRenderer;
/*     */ import net.minecraft.client.renderer.special.PlayerHeadSpecialRenderer;
/*     */ import net.minecraft.client.renderer.special.ShulkerBoxSpecialRenderer;
/*     */ import net.minecraft.client.renderer.special.SkullSpecialRenderer;
/*     */ import net.minecraft.client.renderer.special.SpecialModelRenderer;
/*     */ import net.minecraft.core.Direction;
/*     */ import net.minecraft.resources.Identifier;
/*     */ import net.minecraft.world.item.DyeColor;
/*     */ import net.minecraft.world.level.block.BannerBlock;
/*     */ import net.minecraft.world.level.block.Block;
/*     */ import net.minecraft.world.level.block.Blocks;
/*     */ import net.minecraft.world.level.block.ChestBlock;
/*     */ import net.minecraft.world.level.block.ColorCollection;
/*     */ import net.minecraft.world.level.block.CopperGolemStatueBlock;
/*     */ import net.minecraft.world.level.block.DecoratedPotBlock;
/*     */ import net.minecraft.world.level.block.PlayerHeadBlock;
/*     */ import net.minecraft.world.level.block.PlayerWallHeadBlock;
/*     */ import net.minecraft.world.level.block.ShulkerBoxBlock;
/*     */ import net.minecraft.world.level.block.SkullBlock;
/*     */ import net.minecraft.world.level.block.WallBannerBlock;
/*     */ import net.minecraft.world.level.block.WallSkullBlock;
/*     */ import net.minecraft.world.level.block.WeatheringCopper;
/*     */ import net.minecraft.world.level.block.state.BlockState;
/*     */ import net.minecraft.world.level.block.state.properties.ChestType;
/*     */ import net.minecraft.world.level.block.state.properties.Property;
/*     */ import org.joml.Quaternionfc;
/*     */ import org.joml.Vector3f;
/*     */ import org.joml.Vector3fc;
/*     */ 
/*     */ public class BuiltInBlockModels {
/*     */   private static void addDefaults(Builder builder) {
/*  68 */     createAir(builder, Blocks.AIR);
/*  69 */     createAir(builder, Blocks.CAVE_AIR);
/*  70 */     createAir(builder, Blocks.VOID_AIR);
/*  72 */     createMobHeads(builder, SkullBlock.Types.SKELETON, Blocks.SKELETON_SKULL, Blocks.SKELETON_WALL_SKULL);
/*  73 */     createMobHeads(builder, SkullBlock.Types.ZOMBIE, Blocks.ZOMBIE_HEAD, Blocks.ZOMBIE_WALL_HEAD);
/*  74 */     createMobHeads(builder, SkullBlock.Types.CREEPER, Blocks.CREEPER_HEAD, Blocks.CREEPER_WALL_HEAD);
/*  75 */     createMobHeads(builder, SkullBlock.Types.DRAGON, Blocks.DRAGON_HEAD, Blocks.DRAGON_WALL_HEAD);
/*  76 */     createMobHeads(builder, SkullBlock.Types.PIGLIN, Blocks.PIGLIN_HEAD, Blocks.PIGLIN_WALL_HEAD);
/*  77 */     createMobHeads(builder, SkullBlock.Types.WITHER_SKELETON, Blocks.WITHER_SKELETON_SKULL, Blocks.WITHER_SKELETON_WALL_SKULL);
/*  79 */     builder.put(createPlayerHead(), Blocks.PLAYER_HEAD);
/*  80 */     builder.put(createPlayerWallHead(), Blocks.PLAYER_WALL_HEAD);
/*  82 */     ColorCollection.zipApply(ColorCollection.VALUES, Blocks.BANNER, (color, banner) -> builder.put(createBanner(color), banner));
/*  83 */     ColorCollection.zipApply(ColorCollection.VALUES, Blocks.WALL_BANNER, (color, wallBanner) -> builder.put(createWallBanner(color), wallBanner));
/*  85 */     builder.put(createShulkerBox(), Blocks.SHULKER_BOX);
/*  86 */     ColorCollection.zipApply(ColorCollection.VALUES, Blocks.DYED_SHULKER_BOX, (color, box) -> builder.put(createDyedShulkerBox(color), box));
/*  88 */     builder.put(createSingletonChest(ChestSpecialRenderer.ENDER_CHEST), Blocks.ENDER_CHEST);
/*  90 */     builder.put(createXmasChest(ChestSpecialRenderer.REGULAR), Blocks.CHEST);
/*  91 */     builder.put(createXmasChest(ChestSpecialRenderer.TRAPPED), Blocks.TRAPPED_CHEST);
/*  93 */     WeatheringCopper.WeatherState.forEach(state -> {
/*     */           builder.put(createChest((MultiblockChestResources<Identifier>)ChestSpecialRenderer.COPPER.pick(state)), (Block)Blocks.COPPER_CHEST.weathering().pick(state), (Block)Blocks.COPPER_CHEST.waxed().pick(state));
/*     */           builder.put(createCopperGolem(state), (Block)Blocks.COPPER_GOLEM_STATUE.weathering().pick(state), (Block)Blocks.COPPER_GOLEM_STATUE.waxed().pick(state));
/*     */         });
/*  98 */     builder.put(special((SpecialModelRenderer.Unbaked<?>)new BellSpecialRenderer.Unbaked()), Blocks.BELL);
/*  99 */     builder.put(special((SpecialModelRenderer.Unbaked<?>)new ConduitSpecialRenderer.Unbaked(), ConduitRenderer.DEFAULT_TRANSFORMATION), Blocks.CONDUIT);
/* 100 */     builder.put(createDecoratedPot(), Blocks.DECORATED_POT);
/* 101 */     builder.put(createEnchantingTable(), Blocks.ENCHANTING_TABLE);
/* 102 */     builder.put(special((SpecialModelRenderer.Unbaked<?>)new EndCubeSpecialRenderer.Unbaked(EndCubeSpecialRenderer.Type.GATEWAY)), Blocks.END_GATEWAY);
/* 103 */     builder.put(special((SpecialModelRenderer.Unbaked<?>)new EndCubeSpecialRenderer.Unbaked(EndCubeSpecialRenderer.Type.PORTAL), TheEndPortalRenderer.TRANSFORMATION), Blocks.END_PORTAL);
/* 105 */     builder.put(BuiltInBlockModels::createFlowerBedModel, Blocks.WILDFLOWERS, Blocks.PINK_PETALS);
/*     */   }
/*     */   
/*     */   private static void createAir(Builder builder, Block block) {
/* 109 */     builder.put((BlockModel.Unbaked)new EmptyBlockModel.Unbaked(), block);
/*     */   }
/*     */   
/*     */   private static BlockModel.Unbaked special(SpecialModelRenderer.Unbaked<?> model) {
/* 113 */     return (BlockModel.Unbaked)new SpecialBlockModelWrapper.Unbaked(model, Optional.empty());
/*     */   }
/*     */   
/*     */   private static BlockModel.Unbaked special(SpecialModelRenderer.Unbaked<?> model, Transformation transformation) {
/* 117 */     return (BlockModel.Unbaked)new SpecialBlockModelWrapper.Unbaked(model, Optional.of(transformation));
/*     */   }
/*     */   
/*     */   private static SpecialModelFactory createMobHead(SkullBlock.Types type) {
/* 121 */     return specialModelWithPropertyDispatch((Property<Comparable>)SkullBlock.ROTATION, rotation -> special((SpecialModelRenderer.Unbaked<?>)new SkullSpecialRenderer.Unbaked((SkullBlock.Type)type), (Transformation)SkullBlockRenderer.TRANSFORMATIONS.freeTransformations(rotation.intValue())));
/*     */   }
/*     */   
/*     */   private static SpecialModelFactory createMobWallHead(SkullBlock.Types type) {
/* 130 */     return specialModelWithPropertyDispatch((Property<Comparable>)WallSkullBlock.FACING, facing -> special((SpecialModelRenderer.Unbaked<?>)new SkullSpecialRenderer.Unbaked((SkullBlock.Type)type), (Transformation)SkullBlockRenderer.TRANSFORMATIONS.wallTransformation(facing)));
/*     */   }
/*     */   
/*     */   private static void createMobHeads(Builder builder, SkullBlock.Types type, Block ground, Block wall) {
/* 139 */     builder.put(createMobHead(type), ground);
/* 140 */     builder.put(createMobWallHead(type), wall);
/*     */   }
/*     */   
/*     */   private static SpecialModelFactory createPlayerHead() {
/* 144 */     return specialModelWithPropertyDispatch((Property<Comparable>)PlayerHeadBlock.ROTATION, rotation -> special((SpecialModelRenderer.Unbaked<?>)new PlayerHeadSpecialRenderer.Unbaked(), (Transformation)SkullBlockRenderer.TRANSFORMATIONS.freeTransformations(rotation.intValue())));
/*     */   }
/*     */   
/*     */   private static SpecialModelFactory createPlayerWallHead() {
/* 153 */     return specialModelWithPropertyDispatch((Property<Comparable>)PlayerWallHeadBlock.FACING, facing -> special((SpecialModelRenderer.Unbaked<?>)new PlayerHeadSpecialRenderer.Unbaked(), (Transformation)SkullBlockRenderer.TRANSFORMATIONS.wallTransformation(facing)));
/*     */   }
/*     */   
/*     */   private static SpecialModelFactory createBanner(DyeColor color) {
/* 162 */     return specialModelWithPropertyDispatch((Property<Comparable>)BannerBlock.ROTATION, rotation -> special((SpecialModelRenderer.Unbaked<?>)new BannerSpecialRenderer.Unbaked(color, BannerBlock.AttachmentType.GROUND), (Transformation)BannerRenderer.TRANSFORMATIONS.freeTransformations(rotation.intValue())));
/*     */   }
/*     */   
/*     */   private static SpecialModelFactory createWallBanner(DyeColor color) {
/* 171 */     return specialModelWithPropertyDispatch((Property<Comparable>)WallBannerBlock.FACING, facing -> special((SpecialModelRenderer.Unbaked<?>)new BannerSpecialRenderer.Unbaked(color, BannerBlock.AttachmentType.WALL), (Transformation)BannerRenderer.TRANSFORMATIONS.wallTransformation(facing)));
/*     */   }
/*     */   
/*     */   private static SpecialModelFactory createShulkerBox() {
/* 180 */     return specialModelWithPropertyDispatch((Property<Comparable>)ShulkerBoxBlock.FACING, facing -> special((SpecialModelRenderer.Unbaked<?>)new ShulkerBoxSpecialRenderer.Unbaked(), ShulkerBoxRenderer.modelTransform(facing)));
/*     */   }
/*     */   
/*     */   private static SpecialModelFactory createDyedShulkerBox(DyeColor color) {
/* 189 */     return specialModelWithPropertyDispatch((Property<Comparable>)ShulkerBoxBlock.FACING, facing -> special((SpecialModelRenderer.Unbaked<?>)new ShulkerBoxSpecialRenderer.Unbaked(color), ShulkerBoxRenderer.modelTransform(facing)));
/*     */   }
/*     */   
/*     */   private static BlockModel.Unbaked createChest(Identifier texture, ChestType chestType, Direction facing) {
/* 198 */     return special((SpecialModelRenderer.Unbaked<?>)new ChestSpecialRenderer.Unbaked(texture, chestType), 
/*     */         
/* 200 */         ChestRenderer.modelTransformation(facing));
/*     */   }
/*     */   
/*     */   private static SpecialModelFactory createSingletonChest(Identifier texture) {
/* 205 */     return specialModelWithPropertyDispatch((Property<Comparable>)ChestBlock.FACING, facing -> createChest(texture, ChestType.SINGLE, facing));
/*     */   }
/*     */   
/*     */   private static SpecialModelFactory createChest(MultiblockChestResources<Identifier> textures) {
/* 211 */     return specialModelWithPropertyDispatch((Property<Comparable>)ChestBlock.FACING, (Property<Comparable>)ChestBlock.TYPE, (facing, type) -> createChest((Identifier)textures.select(type), type, facing));
/*     */   }
/*     */   
/*     */   private static SpecialModelFactory createXmasChest(MultiblockChestResources<Identifier> textures) {
/* 217 */     return specialModelWithPropertyDispatch((Property<Comparable>)ChestBlock.FACING, (Property<Comparable>)ChestBlock.TYPE, (facing, type) -> new ConditionalBlockModel.Unbaked(Optional.empty(), (ConditionalBlockModelProperty)new IsXmas(), createChest((Identifier)ChestSpecialRenderer.CHRISTMAS.select(type), type, facing), createChest((Identifier)textures.select(type), type, facing)));
/*     */   }
/*     */   
/*     */   private static SpecialModelFactory createCopperGolem(WeatheringCopper.WeatherState weatherState) {
/* 228 */     return specialModelWithPropertyDispatch((Property<Comparable>)CopperGolemStatueBlock.FACING, (Property<Comparable>)CopperGolemStatueBlock.POSE, (facing, pose) -> special((SpecialModelRenderer.Unbaked<?>)new CopperGolemStatueSpecialRenderer.Unbaked(weatherState, pose), CopperGolemStatueBlockRenderer.modelTransformation(facing)));
/*     */   }
/*     */   
/*     */   private static SpecialModelFactory createDecoratedPot() {
/* 237 */     return specialModelWithPropertyDispatch((Property<Comparable>)DecoratedPotBlock.HORIZONTAL_FACING, facing -> special((SpecialModelRenderer.Unbaked<?>)new DecoratedPotSpecialRenderer.Unbaked(), DecoratedPotRenderer.modelTransformation(facing)));
/*     */   }
/*     */   
/*     */   private static BlockStateModelWrapper.Unbaked createBlockStateModelWrapper(BlockColors blockColors, BlockState blockState) {
/* 246 */     return new BlockStateModelWrapper.Unbaked(blockState, blockColors
/*     */         
/* 248 */         .getTintSources(blockState), 
/* 249 */         Optional.empty());
/*     */   }
/*     */   
/*     */   private static CompositeBlockModel.Unbaked combineSpecialAndBlockModels(BlockModel.Unbaked specialModel, BlockColors blockColors, BlockState blockState) {
/* 254 */     return new CompositeBlockModel.Unbaked(
/* 255 */         (BlockModel.Unbaked)createBlockStateModelWrapper(blockColors, blockState), specialModel, 
/*     */         
/* 257 */         Optional.empty());
/*     */   }
/*     */   
/*     */   private static SelectBlockModel.Unbaked createFlowerBedModel(BlockColors blockColors, BlockState blockState) {
/* 263 */     List<BlockTintSource> tintSources = blockColors.getTintSources(blockState);
/* 264 */     Transformation customFlowerTransform = new Transformation((Vector3fc)new Vector3f(0.25F, 0.0F, 0.25F), null, null, null);
/* 268 */     BlockStateModelWrapper.Unbaked customTransformModel = new BlockStateModelWrapper.Unbaked(blockState, tintSources, Optional.of(customFlowerTransform));
/* 274 */     BlockStateModelWrapper.Unbaked normalTransformModel = new BlockStateModelWrapper.Unbaked(blockState, tintSources, Optional.empty());
/* 277 */     return new SelectBlockModel.Unbaked(
/* 278 */         Optional.empty(), new SelectBlockModel.UnbakedSwitch<>(new DisplayContext(), 
/*     */           
/* 281 */           List.of(new SelectBlockModel.SwitchCase(
/* 282 */               List.of(CopperGolemRenderer.BLOCK_DISPLAY_CONTEXT), (BlockModel.Unbaked)customTransformModel))), 
/*     */         
/* 285 */         (Optional)Optional.of(normalTransformModel));
/*     */   }
/*     */   
/*     */   private static BlockModel.Unbaked createEnchantingTable() {
/* 290 */     return special((SpecialModelRenderer.Unbaked<?>)new BookSpecialRenderer.Unbaked(0.0F, 0.0F, 0.0F), new Transformation((Vector3fc)new Vector3f(0.5F, 0.8125F, 0.5F), (Quaternionfc)Axis.ZP
/*     */           
/* 294 */           .rotationDegrees(180.0F), null, (Quaternionfc)Axis.XP
/*     */           
/* 296 */           .rotationDegrees(90.0F)));
/*     */   }
/*     */   
/*     */   private static class Builder {
/*     */     private final BlockColors blockColors;
/*     */     
/* 303 */     private final Map<BlockState, BlockModel.Unbaked> result = new HashMap<>();
/*     */     
/*     */     private Builder(BlockColors blockColors) {
/* 306 */       this.blockColors = blockColors;
/*     */     }
/*     */     
/*     */     private void put(BuiltInBlockModels.ModelFactory factory, Block a, Block b) {
/* 310 */       put(factory, a);
/* 311 */       put(factory, b);
/*     */     }
/*     */     
/*     */     private void put(BlockModel.Unbaked specialModel, Block block) {
/* 315 */       put(param1BlockState -> specialModel, block);
/*     */     }
/*     */     
/*     */     private void put(BuiltInBlockModels.ModelFactory factory, Block block) {
/* 319 */       for (UnmodifiableIterator<BlockState> unmodifiableIterator = block.getStateDefinition().getPossibleStates().iterator(); unmodifiableIterator.hasNext(); ) {
/* 319 */         BlockState blockState = unmodifiableIterator.next();
/* 320 */         this.result.put(blockState, factory.create(this.blockColors, blockState));
/*     */       } 
/*     */     }
/*     */     
/*     */     public Map<BlockState, BlockModel.Unbaked> build() {
/* 325 */       return Map.copyOf(this.result);
/*     */     }
/*     */   }
/*     */   
/*     */   @FunctionalInterface
/*     */   private static interface SpecialModelFactory extends ModelFactory {
/*     */     default BlockModel.Unbaked create(BlockColors colors, BlockState state) {
/* 339 */       return (BlockModel.Unbaked)BuiltInBlockModels.combineSpecialAndBlockModels(createSpecial(state), colors, state);
/*     */     }
/*     */     
/*     */     BlockModel.Unbaked createSpecial(BlockState param1BlockState);
/*     */   }
/*     */   
/*     */   private static <P extends Comparable<P>> SpecialModelFactory specialModelWithPropertyDispatch(Property<P> property, Function<P, BlockModel.Unbaked> blockModel) {
/* 346 */     return state -> {
/*     */         Comparable comparable = state.getValue(property);
/*     */         return blockModel.apply(comparable);
/*     */       };
/*     */   }
/*     */   
/*     */   private static <P1 extends Comparable<P1>, P2 extends Comparable<P2>> SpecialModelFactory specialModelWithPropertyDispatch(Property<P1> property1, Property<P2> property2, BiFunction<P1, P2, BlockModel.Unbaked> blockModel) {
/* 353 */     return state -> {
/*     */         Comparable comparable1 = state.getValue(property1);
/*     */         Comparable comparable2 = state.getValue(property2);
/*     */         return blockModel.apply(comparable1, comparable2);
/*     */       };
/*     */   }
/*     */   
/*     */   public static Map<BlockState, BlockModel.Unbaked> createBlockModels(BlockColors blockColors) {
/* 361 */     Builder builder = new Builder(blockColors);
/* 362 */     addDefaults(builder);
/* 363 */     return builder.build();
/*     */   }
/*     */   
/*     */   @FunctionalInterface
/*     */   private static interface ModelFactory {
/*     */     BlockModel.Unbaked create(BlockColors param1BlockColors, BlockState param1BlockState);
/*     */   }
/*     */ }


/* Location:              C:\Users\karol\AppData\Roaming\ModrinthApp\meta\versions\26.2-0.19.5\26.2-0.19.5.jar!\net\minecraft\client\renderer\block\BuiltInBlockModels.class
 * Java compiler version: 25 (69.0)
 * JD-Core Version:       1.1.3
 */