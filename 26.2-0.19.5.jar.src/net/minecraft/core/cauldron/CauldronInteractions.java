/*     */ package net.minecraft.core.cauldron;
/*     */ 
/*     */ import com.mojang.serialization.Codec;
/*     */ import java.util.function.Predicate;
/*     */ import net.minecraft.core.BlockPos;
/*     */ import net.minecraft.core.Holder;
/*     */ import net.minecraft.core.component.DataComponents;
/*     */ import net.minecraft.sounds.SoundEvent;
/*     */ import net.minecraft.sounds.SoundEvents;
/*     */ import net.minecraft.sounds.SoundSource;
/*     */ import net.minecraft.stats.Stats;
/*     */ import net.minecraft.tags.FluidTags;
/*     */ import net.minecraft.tags.ItemTags;
/*     */ import net.minecraft.util.ExtraCodecs;
/*     */ import net.minecraft.world.InteractionHand;
/*     */ import net.minecraft.world.InteractionResult;
/*     */ import net.minecraft.world.entity.player.Player;
/*     */ import net.minecraft.world.item.Item;
/*     */ import net.minecraft.world.item.ItemStack;
/*     */ import net.minecraft.world.item.ItemUtils;
/*     */ import net.minecraft.world.item.Items;
/*     */ import net.minecraft.world.item.alchemy.PotionContents;
/*     */ import net.minecraft.world.item.alchemy.Potions;
/*     */ import net.minecraft.world.level.ItemLike;
/*     */ import net.minecraft.world.level.Level;
/*     */ import net.minecraft.world.level.block.Block;
/*     */ import net.minecraft.world.level.block.Blocks;
/*     */ import net.minecraft.world.level.block.LayeredCauldronBlock;
/*     */ import net.minecraft.world.level.block.entity.BannerPatternLayers;
/*     */ import net.minecraft.world.level.block.state.BlockState;
/*     */ import net.minecraft.world.level.block.state.properties.Property;
/*     */ import net.minecraft.world.level.gameevent.GameEvent;
/*     */ import net.minecraft.world.level.material.FluidState;
/*     */ 
/*     */ public class CauldronInteractions {
/*  35 */   private static final ExtraCodecs.LateBoundIdMapper<String, CauldronInteraction.Dispatcher> ID_MAPPER = new ExtraCodecs.LateBoundIdMapper();
/*     */   
/*  37 */   public static final Codec<CauldronInteraction.Dispatcher> CODEC = ID_MAPPER.codec((Codec)Codec.STRING);
/*     */   
/*  39 */   public static final CauldronInteraction.Dispatcher EMPTY = newDispatcher("empty");
/*     */   
/*  40 */   public static final CauldronInteraction.Dispatcher WATER = newDispatcher("water");
/*     */   
/*  41 */   public static final CauldronInteraction.Dispatcher LAVA = newDispatcher("lava");
/*     */   
/*  42 */   public static final CauldronInteraction.Dispatcher POWDER_SNOW = newDispatcher("powder_snow");
/*     */   
/*     */   private static CauldronInteraction.Dispatcher newDispatcher(String name) {
/*  45 */     CauldronInteraction.Dispatcher result = new CauldronInteraction.Dispatcher();
/*  46 */     ID_MAPPER.put(name, result);
/*  47 */     return result;
/*     */   }
/*     */   
/*     */   public static void bootStrap() {
/*  52 */     addDefaultInteractions(EMPTY);
/*  54 */     EMPTY.put(Items.POTION, (paramBlockState, level, pos, player, hand, itemInHand) -> {
/*     */           PotionContents potion = (PotionContents)itemInHand.get(DataComponents.POTION_CONTENTS);
/*     */           if (potion == null || !potion.is(Potions.WATER))
/*     */             return (InteractionResult)InteractionResult.TRY_WITH_EMPTY_HAND; 
/*     */           if (!level.isClientSide()) {
/*     */             Item usedItem = itemInHand.getItem();
/*     */             player.setItemInHand(hand, ItemUtils.createFilledResult(itemInHand, player, new ItemStack((ItemLike)Items.GLASS_BOTTLE)));
/*     */             player.awardStat(Stats.USE_CAULDRON);
/*     */             player.awardStat(Stats.ITEM_USED.get(usedItem));
/*     */             level.setBlockAndUpdate(pos, Blocks.WATER_CAULDRON.defaultBlockState());
/*     */             level.playSound(null, pos, SoundEvents.BOTTLE_EMPTY, SoundSource.BLOCKS, 1.0F, 1.0F);
/*     */             level.gameEvent(null, (Holder)GameEvent.FLUID_PLACE, pos);
/*     */           } 
/*     */           return (InteractionResult)InteractionResult.SUCCESS;
/*     */         });
/*  75 */     addDefaultInteractions(WATER);
/*  77 */     WATER.put(Items.BUCKET, (state, level, pos, player, hand, itemInHand) -> fillBucket(state, level, pos, player, hand, itemInHand, new ItemStack((ItemLike)Items.WATER_BUCKET), (), SoundEvents.BUCKET_FILL));
/*  81 */     WATER.put(Items.GLASS_BOTTLE, (state, level, pos, player, hand, itemInHand) -> {
/*     */           if (!level.isClientSide()) {
/*     */             Item usedItem = itemInHand.getItem();
/*     */             player.setItemInHand(hand, ItemUtils.createFilledResult(itemInHand, player, PotionContents.createItemStack(Items.POTION, Potions.WATER)));
/*     */             player.awardStat(Stats.USE_CAULDRON);
/*     */             player.awardStat(Stats.ITEM_USED.get(usedItem));
/*     */             LayeredCauldronBlock.lowerFillLevel(state, level, pos);
/*     */             level.playSound(null, pos, SoundEvents.BOTTLE_FILL, SoundSource.BLOCKS, 1.0F, 1.0F);
/*     */             level.gameEvent(null, (Holder)GameEvent.FLUID_PICKUP, pos);
/*     */           } 
/*     */           return (InteractionResult)InteractionResult.SUCCESS;
/*     */         });
/*  94 */     WATER.put(Items.POTION, (state, level, pos, player, hand, itemInHand) -> {
/*     */           if (((Integer)state.getValue((Property)LayeredCauldronBlock.LEVEL)).intValue() == 3)
/*     */             return (InteractionResult)InteractionResult.TRY_WITH_EMPTY_HAND; 
/*     */           PotionContents potion = (PotionContents)itemInHand.get(DataComponents.POTION_CONTENTS);
/*     */           if (potion == null || !potion.is(Potions.WATER))
/*     */             return (InteractionResult)InteractionResult.TRY_WITH_EMPTY_HAND; 
/*     */           if (!level.isClientSide()) {
/*     */             player.setItemInHand(hand, ItemUtils.createFilledResult(itemInHand, player, new ItemStack((ItemLike)Items.GLASS_BOTTLE)));
/*     */             player.awardStat(Stats.USE_CAULDRON);
/*     */             player.awardStat(Stats.ITEM_USED.get(itemInHand.getItem()));
/*     */             level.setBlockAndUpdate(pos, (BlockState)state.cycle((Property)LayeredCauldronBlock.LEVEL));
/*     */             level.playSound(null, pos, SoundEvents.BOTTLE_EMPTY, SoundSource.BLOCKS, 1.0F, 1.0F);
/*     */             level.gameEvent(null, (Holder)GameEvent.FLUID_PLACE, pos);
/*     */           } 
/*     */           return (InteractionResult)InteractionResult.SUCCESS;
/*     */         });
/* 116 */     WATER.put(ItemTags.CAULDRON_CAN_REMOVE_DYE, CauldronInteractions::dyedItemIteration);
/* 118 */     Items.BANNER.forEach(banner -> WATER.put(banner, CauldronInteractions::bannerInteraction));
/* 120 */     Items.DYED_SHULKER_BOX.forEach(dyedShulkerBox -> WATER.put(dyedShulkerBox, CauldronInteractions::shulkerBoxInteraction));
/* 123 */     LAVA.put(Items.BUCKET, (state, level, pos, player, hand, itemInHand) -> fillBucket(state, level, pos, player, hand, itemInHand, new ItemStack((ItemLike)Items.LAVA_BUCKET), (), SoundEvents.BUCKET_FILL_LAVA));
/* 127 */     addDefaultInteractions(LAVA);
/* 129 */     POWDER_SNOW.put(Items.BUCKET, (state, level, pos, player, hand, itemInHand) -> fillBucket(state, level, pos, player, hand, itemInHand, new ItemStack((ItemLike)Items.POWDER_SNOW_BUCKET), (), SoundEvents.BUCKET_FILL_POWDER_SNOW));
/* 133 */     addDefaultInteractions(POWDER_SNOW);
/*     */   }
/*     */   
/*     */   public static void addDefaultInteractions(CauldronInteraction.Dispatcher interactionMap) {
/* 137 */     interactionMap.put(Items.LAVA_BUCKET, CauldronInteractions::fillLavaInteraction);
/* 138 */     interactionMap.put(Items.WATER_BUCKET, CauldronInteractions::fillWaterInteraction);
/* 139 */     interactionMap.put(Items.POWDER_SNOW_BUCKET, CauldronInteractions::fillPowderSnowInteraction);
/*     */   }
/*     */   
/*     */   public static InteractionResult fillBucket(BlockState state, Level level, BlockPos pos, Player player, InteractionHand hand, ItemStack itemInHand, ItemStack newItem, Predicate<BlockState> canFill, SoundEvent soundEvent) {
/* 143 */     if (!canFill.test(state))
/* 144 */       return (InteractionResult)InteractionResult.TRY_WITH_EMPTY_HAND; 
/* 147 */     if (!level.isClientSide()) {
/* 148 */       Item itemUsed = itemInHand.getItem();
/* 149 */       player.setItemInHand(hand, ItemUtils.createFilledResult(itemInHand, player, newItem));
/* 150 */       player.awardStat(Stats.USE_CAULDRON);
/* 151 */       player.awardStat(Stats.ITEM_USED.get(itemUsed));
/* 153 */       level.setBlockAndUpdate(pos, Blocks.CAULDRON.defaultBlockState());
/* 154 */       level.playSound(null, pos, soundEvent, SoundSource.BLOCKS, 1.0F, 1.0F);
/* 155 */       level.gameEvent(null, (Holder)GameEvent.FLUID_PICKUP, pos);
/*     */     } 
/* 157 */     return (InteractionResult)InteractionResult.SUCCESS;
/*     */   }
/*     */   
/*     */   public static InteractionResult emptyBucket(Level level, BlockPos pos, Player player, InteractionHand hand, ItemStack itemInHand, BlockState newState, SoundEvent soundEvent) {
/* 161 */     if (!level.isClientSide()) {
/* 162 */       Item itemUsed = itemInHand.getItem();
/* 163 */       player.setItemInHand(hand, ItemUtils.createFilledResult(itemInHand, player, new ItemStack((ItemLike)Items.BUCKET)));
/* 164 */       player.awardStat(Stats.FILL_CAULDRON);
/* 165 */       player.awardStat(Stats.ITEM_USED.get(itemUsed));
/* 167 */       level.setBlockAndUpdate(pos, newState);
/* 168 */       level.playSound(null, pos, soundEvent, SoundSource.BLOCKS, 1.0F, 1.0F);
/* 169 */       level.gameEvent(null, (Holder)GameEvent.FLUID_PLACE, pos);
/*     */     } 
/* 171 */     return (InteractionResult)InteractionResult.SUCCESS;
/*     */   }
/*     */   
/*     */   private static InteractionResult fillWaterInteraction(BlockState state, Level level, BlockPos pos, Player player, InteractionHand hand, ItemStack itemInHand) {
/* 175 */     return emptyBucket(level, pos, player, hand, itemInHand, (BlockState)Blocks.WATER_CAULDRON.defaultBlockState().setValue((Property)LayeredCauldronBlock.LEVEL, Integer.valueOf(3)), SoundEvents.BUCKET_EMPTY);
/*     */   }
/*     */   
/*     */   private static InteractionResult fillLavaInteraction(BlockState state, Level level, BlockPos pos, Player player, InteractionHand hand, ItemStack itemInHand) {
/* 179 */     return isUnderWater(level, pos) ? (InteractionResult)InteractionResult.CONSUME : emptyBucket(level, pos, player, hand, itemInHand, Blocks.LAVA_CAULDRON.defaultBlockState(), SoundEvents.BUCKET_EMPTY_LAVA);
/*     */   }
/*     */   
/*     */   private static InteractionResult fillPowderSnowInteraction(BlockState state, Level level, BlockPos pos, Player player, InteractionHand hand, ItemStack itemInHand) {
/* 183 */     return isUnderWater(level, pos) ? (InteractionResult)InteractionResult.CONSUME : emptyBucket(level, pos, player, hand, itemInHand, (BlockState)Blocks.POWDER_SNOW_CAULDRON.defaultBlockState().setValue((Property)LayeredCauldronBlock.LEVEL, Integer.valueOf(3)), SoundEvents.BUCKET_EMPTY_POWDER_SNOW);
/*     */   }
/*     */   
/*     */   private static InteractionResult shulkerBoxInteraction(BlockState state, Level level, BlockPos pos, Player player, InteractionHand hand, ItemStack itemInHand) {
/* 187 */     Block block = Block.byItem(itemInHand.getItem());
/* 188 */     if (!(block instanceof net.minecraft.world.level.block.ShulkerBoxBlock))
/* 189 */       return (InteractionResult)InteractionResult.TRY_WITH_EMPTY_HAND; 
/* 192 */     if (!level.isClientSide()) {
/* 193 */       ItemStack cleanedShulkerBox = itemInHand.transmuteCopy((ItemLike)Blocks.SHULKER_BOX, 1);
/* 194 */       player.setItemInHand(hand, ItemUtils.createFilledResult(itemInHand, player, cleanedShulkerBox, false));
/* 195 */       player.awardStat(Stats.CLEAN_SHULKER_BOX);
/* 196 */       LayeredCauldronBlock.lowerFillLevel(state, level, pos);
/*     */     } 
/* 199 */     return (InteractionResult)InteractionResult.SUCCESS;
/*     */   }
/*     */   
/*     */   private static InteractionResult bannerInteraction(BlockState state, Level level, BlockPos pos, Player player, InteractionHand hand, ItemStack itemInHand) {
/* 203 */     BannerPatternLayers patterns = (BannerPatternLayers)itemInHand.getOrDefault(DataComponents.BANNER_PATTERNS, BannerPatternLayers.EMPTY);
/* 204 */     if (patterns.layers().isEmpty())
/* 205 */       return (InteractionResult)InteractionResult.TRY_WITH_EMPTY_HAND; 
/* 208 */     if (!level.isClientSide()) {
/* 209 */       ItemStack cleanedBanner = itemInHand.copyWithCount(1);
/* 210 */       cleanedBanner.set(DataComponents.BANNER_PATTERNS, patterns.removeLast());
/* 211 */       player.setItemInHand(hand, ItemUtils.createFilledResult(itemInHand, player, cleanedBanner, false));
/* 212 */       player.awardStat(Stats.CLEAN_BANNER);
/* 213 */       LayeredCauldronBlock.lowerFillLevel(state, level, pos);
/*     */     } 
/* 216 */     return (InteractionResult)InteractionResult.SUCCESS;
/*     */   }
/*     */   
/*     */   private static InteractionResult dyedItemIteration(BlockState state, Level level, BlockPos pos, Player player, InteractionHand hand, ItemStack itemInHand) {
/* 220 */     if (!itemInHand.has(DataComponents.DYED_COLOR))
/* 221 */       return (InteractionResult)InteractionResult.TRY_WITH_EMPTY_HAND; 
/* 224 */     if (!level.isClientSide()) {
/* 225 */       itemInHand.remove(DataComponents.DYED_COLOR);
/* 226 */       player.awardStat(Stats.CLEAN_ARMOR);
/* 227 */       LayeredCauldronBlock.lowerFillLevel(state, level, pos);
/*     */     } 
/* 230 */     return (InteractionResult)InteractionResult.SUCCESS;
/*     */   }
/*     */   
/*     */   private static boolean isUnderWater(Level level, BlockPos pos) {
/* 234 */     FluidState fluidState = level.getFluidState(pos.above());
/* 235 */     return fluidState.is(FluidTags.WATER);
/*     */   }
/*     */ }


/* Location:              C:\Users\karol\AppData\Roaming\ModrinthApp\meta\versions\26.2-0.19.5\26.2-0.19.5.jar!\net\minecraft\core\cauldron\CauldronInteractions.class
 * Java compiler version: 25 (69.0)
 * JD-Core Version:       1.1.3
 */