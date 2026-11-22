/*    */ package net.minecraft.client.color.block;
/*    */ 
/*    */ import java.util.HashSet;
/*    */ import java.util.IdentityHashMap;
/*    */ import java.util.List;
/*    */ import java.util.Map;
/*    */ import java.util.Set;
/*    */ import net.minecraft.world.level.block.Block;
/*    */ import net.minecraft.world.level.block.Blocks;
/*    */ import net.minecraft.world.level.block.state.BlockState;
/*    */ import net.minecraft.world.level.block.state.properties.Property;
/*    */ 
/*    */ public class BlockColors {
/*    */   public static final int LILY_PAD_IN_WORLD = -14647248;
/*    */   
/*    */   public static final int LILY_PAD_DEFAULT = -9321636;
/*    */   
/* 21 */   private static final BlockTintSource BLANK_LAYER = BlockTintSources.constant(-1);
/*    */   
/* 23 */   private final Map<Block, List<BlockTintSource>> sources = new IdentityHashMap<>();
/*    */   
/*    */   public static BlockColors createDefault() {
/* 26 */     BlockColors colors = new BlockColors();
/* 28 */     colors.register(List.of(BlockTintSources.doubleTallGrass()), new Block[] { Blocks.LARGE_FERN, Blocks.TALL_GRASS });
/* 30 */     colors.register(List.of(BlockTintSources.grass()), new Block[] { Blocks.FERN, Blocks.SHORT_GRASS, Blocks.POTTED_FERN, Blocks.BUSH });
/* 31 */     colors.register(List.of(BlockTintSources.grassBlock()), new Block[] { Blocks.GRASS_BLOCK });
/* 33 */     colors.register(List.of(BLANK_LAYER, BlockTintSources.grass()), new Block[] { Blocks.PINK_PETALS, Blocks.WILDFLOWERS });
/* 35 */     colors.register(List.of(BlockTintSources.constant(-10380959)), new Block[] { Blocks.SPRUCE_LEAVES });
/* 36 */     colors.register(List.of(BlockTintSources.constant(-8345771)), new Block[] { Blocks.BIRCH_LEAVES });
/* 38 */     colors.register(List.of(BlockTintSources.foliage()), new Block[] { Blocks.OAK_LEAVES, Blocks.JUNGLE_LEAVES, Blocks.ACACIA_LEAVES, Blocks.DARK_OAK_LEAVES, Blocks.VINE, Blocks.MANGROVE_LEAVES });
/* 40 */     colors.register(List.of(BlockTintSources.dryFoliage()), new Block[] { Blocks.LEAF_LITTER });
/* 42 */     colors.register(List.of(BlockTintSources.water()), new Block[] { Blocks.WATER_CAULDRON });
/* 45 */     colors.register(List.of(BlockTintSources.waterParticles()), new Block[] { Blocks.WATER, Blocks.BUBBLE_COLUMN });
/* 47 */     colors.register(List.of(BlockTintSources.redstone()), new Block[] { Blocks.REDSTONE_WIRE });
/* 49 */     colors.register(List.of(BlockTintSources.sugarCane()), new Block[] { Blocks.SUGAR_CANE });
/* 51 */     colors.register(List.of(BlockTintSources.constant(-2046180)), new Block[] { Blocks.ATTACHED_MELON_STEM, Blocks.ATTACHED_PUMPKIN_STEM });
/* 53 */     colors.register(List.of(BlockTintSources.stem()), new Block[] { Blocks.MELON_STEM, Blocks.PUMPKIN_STEM });
/* 55 */     colors.register(List.of(BlockTintSources.constant(-9321636, -14647248)), new Block[] { Blocks.LILY_PAD });
/* 57 */     return colors;
/*    */   }
/*    */   
/*    */   public List<BlockTintSource> getTintSources(BlockState state) {
/* 61 */     return this.sources.getOrDefault(state.getBlock(), List.of());
/*    */   }
/*    */   
/*    */   public BlockTintSource getTintSource(BlockState state, int layer) {
/* 65 */     List<BlockTintSource> layers = getTintSources(state);
/* 66 */     return (layer >= layers.size()) ? null : layers.get(layer);
/*    */   }
/*    */   
/*    */   public void register(List<BlockTintSource> layers, Block... blocks) {
/* 70 */     for (Block block : blocks)
/* 71 */       this.sources.put(block, layers); 
/*    */   }
/*    */   
/*    */   public Set<Property<?>> getColoringProperties(Block block) {
/* 76 */     List<BlockTintSource> sources = this.sources.getOrDefault(block, List.of());
/* 77 */     if (sources.isEmpty())
/* 78 */       return Set.of(); 
/* 80 */     if (sources.size() == 1)
/* 81 */       return ((BlockTintSource)sources.getFirst()).relevantProperties(); 
/* 84 */     Set<Property<?>> result = new HashSet<>();
/* 85 */     for (BlockTintSource source : sources)
/* 86 */       result.addAll(source.relevantProperties()); 
/* 88 */     return result;
/*    */   }
/*    */ }


/* Location:              C:\Users\karol\AppData\Roaming\ModrinthApp\meta\versions\26.2-0.19.5\26.2-0.19.5.jar!\net\minecraft\client\color\block\BlockColors.class
 * Java compiler version: 25 (69.0)
 * JD-Core Version:       1.1.3
 */