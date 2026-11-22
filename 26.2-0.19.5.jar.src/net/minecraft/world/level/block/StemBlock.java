/*     */ package net.minecraft.world.level.block;
/*     */ 
/*     */ import com.mojang.datafixers.DataFixUtils;
/*     */ import com.mojang.datafixers.kinds.App;
/*     */ import com.mojang.datafixers.kinds.Applicative;
/*     */ import com.mojang.datafixers.util.Function6;
/*     */ import com.mojang.serialization.MapCodec;
/*     */ import com.mojang.serialization.codecs.RecordCodecBuilder;
/*     */ import java.util.Optional;
/*     */ import net.minecraft.core.BlockPos;
/*     */ import net.minecraft.core.Direction;
/*     */ import net.minecraft.core.Registry;
/*     */ import net.minecraft.core.registries.Registries;
/*     */ import net.minecraft.resources.ResourceKey;
/*     */ import net.minecraft.server.level.ServerLevel;
/*     */ import net.minecraft.tags.TagKey;
/*     */ import net.minecraft.util.Mth;
/*     */ import net.minecraft.util.RandomSource;
/*     */ import net.minecraft.world.item.Item;
/*     */ import net.minecraft.world.item.ItemStack;
/*     */ import net.minecraft.world.level.BlockGetter;
/*     */ import net.minecraft.world.level.ItemLike;
/*     */ import net.minecraft.world.level.Level;
/*     */ import net.minecraft.world.level.LevelReader;
/*     */ import net.minecraft.world.level.block.state.BlockBehaviour;
/*     */ import net.minecraft.world.level.block.state.BlockState;
/*     */ import net.minecraft.world.level.block.state.StateDefinition;
/*     */ import net.minecraft.world.level.block.state.properties.BlockStateProperties;
/*     */ import net.minecraft.world.level.block.state.properties.IntegerProperty;
/*     */ import net.minecraft.world.level.block.state.properties.Property;
/*     */ import net.minecraft.world.phys.shapes.CollisionContext;
/*     */ import net.minecraft.world.phys.shapes.VoxelShape;
/*     */ 
/*     */ public class StemBlock extends VegetationBlock implements BonemealableBlock {
/*     */   public static final MapCodec<StemBlock> CODEC;
/*     */   
/*     */   public static final int MAX_AGE = 7;
/*     */   
/*     */   static {
/*  31 */     CODEC = RecordCodecBuilder.mapCodec(i -> i.group((App)ResourceKey.codec(Registries.BLOCK).fieldOf("fruit").forGetter(()), (App)ResourceKey.codec(Registries.BLOCK).fieldOf("attached_stem").forGetter(()), (App)ResourceKey.codec(Registries.ITEM).fieldOf("seed").forGetter(()), (App)TagKey.codec(Registries.BLOCK).fieldOf("stem_support_blocks").forGetter(()), (App)TagKey.codec(Registries.BLOCK).fieldOf("fruit_support_blocks").forGetter(()), (App)propertiesCodec()).apply((Applicative)i, StemBlock::new));
/*     */   }
/*     */   
/*     */   public MapCodec<StemBlock> codec() {
/*  42 */     return CODEC;
/*     */   }
/*     */   
/*  46 */   public static final IntegerProperty AGE = BlockStateProperties.AGE_7;
/*     */   
/*     */   private static final VoxelShape[] SHAPES;
/*     */   
/*     */   private final ResourceKey<Block> fruit;
/*     */   
/*     */   private final ResourceKey<Block> attachedStem;
/*     */   
/*     */   private final ResourceKey<Item> seed;
/*     */   
/*     */   private final TagKey<Block> stemSupportBlocks;
/*     */   
/*     */   private final TagKey<Block> fruitSupportBlocks;
/*     */   
/*     */   static {
/*  48 */     SHAPES = Block.boxes(7, age -> Block.column(2.0D, 0.0D, (2 + age * 2)));
/*     */   }
/*     */   
/*     */   protected StemBlock(ResourceKey<Block> fruit, ResourceKey<Block> attachedStem, ResourceKey<Item> seed, TagKey<Block> stemSupportBlocks, TagKey<Block> fruitSupportBlocks, BlockBehaviour.Properties properties) {
/*  58 */     super(properties);
/*  59 */     this.fruit = fruit;
/*  60 */     this.attachedStem = attachedStem;
/*  61 */     this.seed = seed;
/*  62 */     this.stemSupportBlocks = stemSupportBlocks;
/*  63 */     this.fruitSupportBlocks = fruitSupportBlocks;
/*  64 */     registerDefaultState((BlockState)((BlockState)this.stateDefinition.any()).setValue((Property)AGE, Integer.valueOf(0)));
/*     */   }
/*     */   
/*     */   protected VoxelShape getShape(BlockState state, BlockGetter level, BlockPos pos, CollisionContext context) {
/*  69 */     return SHAPES[((Integer)state.getValue((Property)AGE)).intValue()];
/*     */   }
/*     */   
/*     */   protected boolean mayPlaceOn(BlockState state, BlockGetter level, BlockPos pos) {
/*  74 */     return state.is(this.stemSupportBlocks);
/*     */   }
/*     */   
/*     */   protected void randomTick(BlockState state, ServerLevel level, BlockPos pos, RandomSource random) {
/*  79 */     if (level.getRawBrightness(pos, 0) < 9)
/*     */       return; 
/*  83 */     float growthSpeed = CropBlock.getGrowthSpeed(this, (BlockGetter)level, pos);
/*  84 */     if (random.nextInt((int)(25.0F / growthSpeed) + 1) == 0) {
/*  85 */       int age = ((Integer)state.getValue((Property)AGE)).intValue();
/*  86 */       if (age < 7) {
/*  87 */         state = (BlockState)state.setValue((Property)AGE, Integer.valueOf(age + 1));
/*  88 */         level.setBlock(pos, state, 2);
/*     */       } else {
/*  90 */         Direction direction = Direction.Plane.HORIZONTAL.getRandomDirection(random);
/*  91 */         BlockPos relative = pos.relative(direction);
/*  93 */         BlockState stateBelow = level.getBlockState(relative.below());
/*  94 */         if (level.getBlockState(relative).isAir() && stateBelow.is(this.fruitSupportBlocks)) {
/*  95 */           Registry<Block> blocks = level.registryAccess().lookupOrThrow(Registries.BLOCK);
/*  96 */           Optional<Block> fruit = blocks.getOptional(this.fruit);
/*  97 */           Optional<Block> stem = blocks.getOptional(this.attachedStem);
/*  98 */           if (fruit.isPresent() && stem.isPresent()) {
/*  99 */             level.setBlockAndUpdate(relative, ((Block)fruit.get()).defaultBlockState());
/* 100 */             level.setBlockAndUpdate(pos, (BlockState)((Block)stem.get()).defaultBlockState().setValue((Property)HorizontalDirectionalBlock.FACING, (Comparable)direction));
/*     */           } 
/*     */         } 
/*     */       } 
/*     */     } 
/*     */   }
/*     */   
/*     */   protected ItemStack getCloneItemStack(LevelReader level, BlockPos pos, BlockState state, boolean includeData) {
/* 109 */     return new ItemStack((ItemLike)DataFixUtils.orElse(level.registryAccess().lookupOrThrow(Registries.ITEM).getOptional(this.seed), this));
/*     */   }
/*     */   
/*     */   public boolean isValidBonemealTarget(LevelReader level, BlockPos pos, BlockState state) {
/* 114 */     return (((Integer)state.getValue((Property)AGE)).intValue() != 7);
/*     */   }
/*     */   
/*     */   public boolean isBonemealSuccess(Level level, RandomSource random, BlockPos pos, BlockState state) {
/* 119 */     return true;
/*     */   }
/*     */   
/*     */   public void performBonemeal(ServerLevel level, RandomSource random, BlockPos pos, BlockState state) {
/* 124 */     int age = Math.min(7, ((Integer)state.getValue((Property)AGE)).intValue() + Mth.nextInt(random, 2, 5));
/* 125 */     BlockState newState = (BlockState)state.setValue((Property)AGE, Integer.valueOf(age));
/* 126 */     level.setBlock(pos, newState, 2);
/* 127 */     if (age == 7)
/* 128 */       newState.randomTick(level, pos, random); 
/*     */   }
/*     */   
/*     */   protected void createBlockStateDefinition(StateDefinition.Builder<Block, BlockState> builder) {
/* 134 */     builder.add(new Property[] { (Property)AGE });
/*     */   }
/*     */ }


/* Location:              C:\Users\karol\AppData\Roaming\ModrinthApp\meta\versions\26.2-0.19.5\26.2-0.19.5.jar!\net\minecraft\world\level\block\StemBlock.class
 * Java compiler version: 25 (69.0)
 * JD-Core Version:       1.1.3
 */