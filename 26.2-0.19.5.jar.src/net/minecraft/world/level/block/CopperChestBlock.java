/*     */ package net.minecraft.world.level.block;
/*     */ 
/*     */ import com.google.common.base.Suppliers;
/*     */ import com.google.common.collect.BiMap;
/*     */ import com.google.common.collect.ImmutableMap;
/*     */ import com.mojang.datafixers.kinds.App;
/*     */ import com.mojang.datafixers.kinds.Applicative;
/*     */ import com.mojang.datafixers.util.Function4;
/*     */ import com.mojang.serialization.MapCodec;
/*     */ import com.mojang.serialization.codecs.RecordCodecBuilder;
/*     */ import java.util.Map;
/*     */ import java.util.Objects;
/*     */ import java.util.Optional;
/*     */ import java.util.function.Supplier;
/*     */ import net.minecraft.core.BlockPos;
/*     */ import net.minecraft.core.Direction;
/*     */ import net.minecraft.core.registries.BuiltInRegistries;
/*     */ import net.minecraft.sounds.SoundEvent;
/*     */ import net.minecraft.sounds.SoundEvents;
/*     */ import net.minecraft.tags.BlockTags;
/*     */ import net.minecraft.util.RandomSource;
/*     */ import net.minecraft.world.item.HoneycombItem;
/*     */ import net.minecraft.world.item.context.BlockPlaceContext;
/*     */ import net.minecraft.world.level.Level;
/*     */ import net.minecraft.world.level.LevelReader;
/*     */ import net.minecraft.world.level.ScheduledTickAccess;
/*     */ import net.minecraft.world.level.block.entity.BlockEntityType;
/*     */ import net.minecraft.world.level.block.entity.BlockEntityTypes;
/*     */ import net.minecraft.world.level.block.state.BlockBehaviour;
/*     */ import net.minecraft.world.level.block.state.BlockState;
/*     */ import net.minecraft.world.level.block.state.properties.ChestType;
/*     */ import net.minecraft.world.level.block.state.properties.Property;
/*     */ 
/*     */ public class CopperChestBlock extends ChestBlock {
/*     */   public static final MapCodec<CopperChestBlock> CODEC;
/*     */   
/*     */   private static final Supplier<Map<Block, Block>> COPPER_TO_COPPER_CHEST_MAPPING;
/*     */   
/*     */   private final WeatheringCopper.WeatherState weatherState;
/*     */   
/*     */   static {
/*  28 */     CODEC = RecordCodecBuilder.mapCodec(i -> i.group((App)WeatheringCopper.WeatherState.CODEC.fieldOf("weathering_state").forGetter(CopperChestBlock::getState), (App)BuiltInRegistries.SOUND_EVENT.byNameCodec().fieldOf("open_sound").forGetter(ChestBlock::getOpenChestSound), (App)BuiltInRegistries.SOUND_EVENT.byNameCodec().fieldOf("close_sound").forGetter(ChestBlock::getCloseChestSound), (App)propertiesCodec()).apply((Applicative)i, CopperChestBlock::new));
/*  35 */     COPPER_TO_COPPER_CHEST_MAPPING = (Supplier<Map<Block, Block>>)Suppliers.memoize(() -> {
/*     */           ImmutableMap.Builder<Block, Block> result = ImmutableMap.builder();
/*     */           Objects.requireNonNull(result);
/*     */           WeatheringCopperCollection.zipApply(Blocks.COPPER_BLOCK, Blocks.COPPER_CHEST, result::put);
/*     */           return (Map)result.buildOrThrow();
/*     */         });
/*     */   }
/*     */   
/*     */   public MapCodec<? extends CopperChestBlock> codec() {
/*  43 */     return CODEC;
/*     */   }
/*     */   
/*     */   public CopperChestBlock(WeatheringCopper.WeatherState weatherState, SoundEvent openSound, SoundEvent closeSound, BlockBehaviour.Properties properties) {
/*  49 */     super(() -> BlockEntityTypes.CHEST, openSound, closeSound, properties);
/*  50 */     this.weatherState = weatherState;
/*     */   }
/*     */   
/*     */   public static SoundEvent getHingeSound(WeatheringCopper.WeatherState state, boolean open) {
/*  54 */     switch (state) {
/*     */       case WEATHERED:
/*  54 */         return 
/*  55 */           open ? SoundEvents.COPPER_CHEST_WEATHERED_OPEN : SoundEvents.COPPER_CHEST_WEATHERED_CLOSE;
/*     */       case OXIDIZED:
/*  56 */         return open ? SoundEvents.COPPER_CHEST_OXIDIZED_OPEN : SoundEvents.COPPER_CHEST_OXIDIZED_CLOSE;
/*     */     } 
/*  57 */     return open ? SoundEvents.COPPER_CHEST_OPEN : SoundEvents.COPPER_CHEST_CLOSE;
/*     */   }
/*     */   
/*     */   public boolean chestCanConnectTo(BlockState blockState) {
/*  63 */     return (blockState.is(BlockTags.COPPER_CHESTS) && blockState.hasProperty((Property)ChestBlock.TYPE));
/*     */   }
/*     */   
/*     */   public BlockState getStateForPlacement(BlockPlaceContext context) {
/*  68 */     BlockState state = super.getStateForPlacement(context);
/*  69 */     return getLeastOxidizedChestOfConnectedBlocks(state, context.getLevel(), context.getClickedPos());
/*     */   }
/*     */   
/*     */   private static BlockState getLeastOxidizedChestOfConnectedBlocks(BlockState state, Level level, BlockPos pos) {
/*  73 */     BlockState connectedState = level.getBlockState(pos.relative(getConnectedDirection(state)));
/*  74 */     if (!((ChestType)state.getValue((Property)ChestBlock.TYPE)).equals(ChestType.SINGLE)) {
/*  74 */       Block block = state.getBlock();
/*  74 */       if (block instanceof CopperChestBlock) {
/*  74 */         CopperChestBlock copperChestBlock = (CopperChestBlock)block;
/*  74 */         block = connectedState.getBlock();
/*  74 */         if (block instanceof CopperChestBlock) {
/*  74 */           CopperChestBlock connectedCopperChestBlock = (CopperChestBlock)block;
/*  75 */           BlockState updatedBlockState = state;
/*  76 */           BlockState connectedPredictedBlockState = connectedState;
/*  78 */           if (copperChestBlock.isWaxed() != connectedCopperChestBlock.isWaxed()) {
/*  79 */             updatedBlockState = unwaxBlock(copperChestBlock, state).orElse(updatedBlockState);
/*  80 */             connectedPredictedBlockState = unwaxBlock(connectedCopperChestBlock, connectedState).orElse(connectedPredictedBlockState);
/*     */           } 
/*  83 */           Block leastOxidizedBlock = (copperChestBlock.weatherState.ordinal() <= connectedCopperChestBlock.weatherState.ordinal()) ? updatedBlockState.getBlock() : connectedPredictedBlockState.getBlock();
/*  84 */           return leastOxidizedBlock.withPropertiesOf(updatedBlockState);
/*     */         } 
/*     */       } 
/*     */     } 
/*  86 */     return state;
/*     */   }
/*     */   
/*     */   protected BlockState updateShape(BlockState state, LevelReader level, ScheduledTickAccess ticks, BlockPos pos, Direction directionToNeighbour, BlockPos neighbourPos, BlockState neighbourState, RandomSource random) {
/*  91 */     BlockState blockState = super.updateShape(state, level, ticks, pos, directionToNeighbour, neighbourPos, neighbourState, random);
/*  92 */     if (chestCanConnectTo(neighbourState)) {
/*  93 */       ChestType chestType = (ChestType)blockState.getValue((Property)ChestBlock.TYPE);
/*  94 */       if (!chestType.equals(ChestType.SINGLE) && getConnectedDirection(blockState) == directionToNeighbour)
/*  95 */         return neighbourState.getBlock().withPropertiesOf(blockState); 
/*     */     } 
/*  98 */     return blockState;
/*     */   }
/*     */   
/*     */   private static Optional<BlockState> unwaxBlock(CopperChestBlock copperChestBlock, BlockState state) {
/* 102 */     if (!copperChestBlock.isWaxed())
/* 103 */       return Optional.of(state); 
/* 105 */     return Optional.<Block>ofNullable((Block)((BiMap)HoneycombItem.WAX_OFF_BY_BLOCK.get()).get(state.getBlock())).map(b -> b.withPropertiesOf(state));
/*     */   }
/*     */   
/*     */   public WeatheringCopper.WeatherState getState() {
/* 109 */     return this.weatherState;
/*     */   }
/*     */   
/*     */   public static BlockState getFromCopperBlock(Block copperBlock, Direction facing, Level level, BlockPos pos) {
/* 113 */     CopperChestBlock block = (CopperChestBlock)((Map)COPPER_TO_COPPER_CHEST_MAPPING.get()).getOrDefault(copperBlock, Blocks.COPPER_CHEST.weathering().unaffected());
/* 114 */     ChestType chestType = block.getChestType(level, pos, facing);
/* 115 */     BlockState state = (BlockState)((BlockState)block.defaultBlockState().setValue((Property)FACING, (Comparable)facing)).setValue((Property)TYPE, (Comparable)chestType);
/* 116 */     return getLeastOxidizedChestOfConnectedBlocks(state, level, pos);
/*     */   }
/*     */   
/*     */   public boolean isWaxed() {
/* 120 */     return true;
/*     */   }
/*     */   
/*     */   public boolean shouldChangedStateKeepBlockEntity(BlockState oldState) {
/* 125 */     return oldState.is(BlockTags.COPPER_CHESTS);
/*     */   }
/*     */ }


/* Location:              C:\Users\karol\AppData\Roaming\ModrinthApp\meta\versions\26.2-0.19.5\26.2-0.19.5.jar!\net\minecraft\world\level\block\CopperChestBlock.class
 * Java compiler version: 25 (69.0)
 * JD-Core Version:       1.1.3
 */