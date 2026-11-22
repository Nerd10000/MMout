/*     */ package net.minecraft.world.item;
/*     */ 
/*     */ import com.google.common.base.Suppliers;
/*     */ import com.google.common.collect.BiMap;
/*     */ import com.google.common.collect.ImmutableBiMap;
/*     */ import com.google.common.collect.ImmutableMap;
/*     */ import com.mojang.datafixers.util.Pair;
/*     */ import java.util.Iterator;
/*     */ import java.util.List;
/*     */ import java.util.Objects;
/*     */ import java.util.Optional;
/*     */ import java.util.function.Function;
/*     */ import java.util.function.Supplier;
/*     */ import java.util.stream.Stream;
/*     */ import net.minecraft.advancements.triggers.CriteriaTriggers;
/*     */ import net.minecraft.core.BlockPos;
/*     */ import net.minecraft.core.Holder;
/*     */ import net.minecraft.data.recipes.RecipeCategory;
/*     */ import net.minecraft.server.level.ServerPlayer;
/*     */ import net.minecraft.world.InteractionResult;
/*     */ import net.minecraft.world.entity.Entity;
/*     */ import net.minecraft.world.entity.player.Player;
/*     */ import net.minecraft.world.item.context.UseOnContext;
/*     */ import net.minecraft.world.level.Level;
/*     */ import net.minecraft.world.level.block.Block;
/*     */ import net.minecraft.world.level.block.Blocks;
/*     */ import net.minecraft.world.level.block.ChestBlock;
/*     */ import net.minecraft.world.level.block.WeatheringCopperCollection;
/*     */ import net.minecraft.world.level.block.entity.SignBlockEntity;
/*     */ import net.minecraft.world.level.block.entity.SignText;
/*     */ import net.minecraft.world.level.block.state.BlockState;
/*     */ import net.minecraft.world.level.block.state.properties.ChestType;
/*     */ import net.minecraft.world.level.block.state.properties.Property;
/*     */ import net.minecraft.world.level.gameevent.GameEvent;
/*     */ 
/*     */ public class HoneycombItem extends Item implements SignApplicator {
/*     */   public static final Supplier<BiMap<Block, Block>> WAXABLES;
/*     */   
/*     */   static {
/*  34 */     WAXABLES = (Supplier<BiMap<Block, Block>>)Suppliers.memoize(() -> {
/*     */           ImmutableBiMap.Builder<Block, Block> builder = ImmutableBiMap.builder();
/*     */           Stream.<WeatheringCopperCollection>of(new WeatheringCopperCollection[] { 
/*     */                 Blocks.COPPER_BLOCK, Blocks.CUT_COPPER, Blocks.CUT_COPPER_SLAB, Blocks.CUT_COPPER_STAIRS, Blocks.CHISELED_COPPER, Blocks.COPPER_DOOR, Blocks.COPPER_TRAPDOOR, Blocks.COPPER_BARS, Blocks.COPPER_GRATE, Blocks.COPPER_BULB, 
/*     */                 Blocks.COPPER_CHEST, Blocks.COPPER_GOLEM_STATUE, Blocks.LIGHTNING_ROD, Blocks.COPPER_LANTERN, Blocks.COPPER_CHAIN }).forEach(());
/*     */           return (BiMap)builder.build();
/*     */         });
/*     */   }
/*     */   
/*  44 */   public static final Supplier<BiMap<Block, Block>> WAX_OFF_BY_BLOCK = (Supplier<BiMap<Block, Block>>)Suppliers.memoize(() -> ((BiMap)WAXABLES.get()).inverse());
/*     */   
/*     */   public static final ImmutableMap<Block, Pair<RecipeCategory, String>> WAXED_RECIPES;
/*     */   
/*     */   private static final class WaxedRecipeGroup extends Record {
/*     */     private final WeatheringCopperCollection<Block> block;
/*     */     
/*     */     private final Function<Block, Pair<RecipeCategory, String>> recipeIdProvider;
/*     */     
/*     */     private WaxedRecipeGroup(WeatheringCopperCollection<Block> block, Function<Block, Pair<RecipeCategory, String>> recipeIdProvider) {
/*  48 */       this.block = block;
/*  48 */       this.recipeIdProvider = recipeIdProvider;
/*     */     }
/*     */     
/*     */     public final String toString() {
/*     */       // Byte code:
/*     */       //   0: aload_0
/*     */       //   1: <illegal opcode> toString : (Lnet/minecraft/world/item/HoneycombItem$WaxedRecipeGroup;)Ljava/lang/String;
/*     */       //   6: areturn
/*     */       // Line number table:
/*     */       //   Java source line number -> byte code offset
/*     */       //   #48	-> 0
/*     */       // Local variable table:
/*     */       //   start	length	slot	name	descriptor
/*     */       //   0	7	0	this	Lnet/minecraft/world/item/HoneycombItem$WaxedRecipeGroup;
/*     */     }
/*     */     
/*     */     public final int hashCode() {
/*     */       // Byte code:
/*     */       //   0: aload_0
/*     */       //   1: <illegal opcode> hashCode : (Lnet/minecraft/world/item/HoneycombItem$WaxedRecipeGroup;)I
/*     */       //   6: ireturn
/*     */       // Line number table:
/*     */       //   Java source line number -> byte code offset
/*     */       //   #48	-> 0
/*     */       // Local variable table:
/*     */       //   start	length	slot	name	descriptor
/*     */       //   0	7	0	this	Lnet/minecraft/world/item/HoneycombItem$WaxedRecipeGroup;
/*     */     }
/*     */     
/*     */     public final boolean equals(Object o) {
/*     */       // Byte code:
/*     */       //   0: aload_0
/*     */       //   1: aload_1
/*     */       //   2: <illegal opcode> equals : (Lnet/minecraft/world/item/HoneycombItem$WaxedRecipeGroup;Ljava/lang/Object;)Z
/*     */       //   7: ireturn
/*     */       // Line number table:
/*     */       //   Java source line number -> byte code offset
/*     */       //   #48	-> 0
/*     */       // Local variable table:
/*     */       //   start	length	slot	name	descriptor
/*     */       //   0	8	0	this	Lnet/minecraft/world/item/HoneycombItem$WaxedRecipeGroup;
/*     */       //   0	8	1	o	Ljava/lang/Object;
/*     */     }
/*     */     
/*     */     public WeatheringCopperCollection<Block> block() {
/*  48 */       return this.block;
/*     */     }
/*     */     
/*     */     public Function<Block, Pair<RecipeCategory, String>> recipeIdProvider() {
/*  48 */       return this.recipeIdProvider;
/*     */     }
/*     */   }
/*     */   
/*     */   static {
/*  51 */     ImmutableMap.Builder<Block, Pair<RecipeCategory, String>> builder = ImmutableMap.builder();
/*  52 */     for (Iterator<WaxedRecipeGroup> iterator = List.<WaxedRecipeGroup>of(new WaxedRecipeGroup(Blocks.COPPER_BULB, block -> Pair.of(RecipeCategory.REDSTONE, block.builtInRegistryHolder().key().identifier().getPath())), new WaxedRecipeGroup(Blocks.COPPER_DOOR, paramBlock -> Pair.of(RecipeCategory.REDSTONE, "waxed_copper_door")), new WaxedRecipeGroup(Blocks.COPPER_TRAPDOOR, paramBlock -> Pair.of(RecipeCategory.REDSTONE, "waxed_copper_trapdoor")), new WaxedRecipeGroup(Blocks.COPPER_GOLEM_STATUE, paramBlock -> Pair.of(RecipeCategory.BUILDING_BLOCKS, "waxed_copper_golem_statue")), new WaxedRecipeGroup(Blocks.COPPER_CHEST, paramBlock -> Pair.of(RecipeCategory.BUILDING_BLOCKS, "waxed_copper_chest")), new WaxedRecipeGroup(Blocks.LIGHTNING_ROD, paramBlock -> Pair.of(RecipeCategory.BUILDING_BLOCKS, "waxed_lightning_rod")), new WaxedRecipeGroup(Blocks.COPPER_BARS, paramBlock -> Pair.of(RecipeCategory.BUILDING_BLOCKS, "waxed_copper_bar")), new WaxedRecipeGroup(Blocks.COPPER_CHAIN, paramBlock -> Pair.of(RecipeCategory.BUILDING_BLOCKS, "waxed_copper_chain")), new WaxedRecipeGroup(Blocks.COPPER_LANTERN, paramBlock -> Pair.of(RecipeCategory.BUILDING_BLOCKS, "waxed_copper_lantern")), new WaxedRecipeGroup(Blocks.COPPER_BLOCK, paramBlock -> Pair.of(RecipeCategory.BUILDING_BLOCKS, "waxed_copper_block"))).iterator(); iterator.hasNext(); ) {
/*  52 */       WaxedRecipeGroup data = iterator.next();
/*  64 */       data.block.waxed().forEach(block -> builder.put(block, data.recipeIdProvider.apply(block)));
/*     */     } 
/*  66 */     WAXED_RECIPES = builder.build();
/*     */   }
/*     */   
/*     */   public HoneycombItem(Item.Properties properties) {
/*  70 */     super(properties);
/*     */   }
/*     */   
/*     */   public InteractionResult useOn(UseOnContext context) {
/*  75 */     Level level = context.getLevel();
/*  76 */     BlockPos pos = context.getClickedPos();
/*  77 */     BlockState oldState = level.getBlockState(pos);
/*  79 */     return (InteractionResult)getWaxed(oldState).map(waxedState -> {
/*     */           Player player = context.getPlayer();
/*     */           ItemStack itemInHand = context.getItemInHand();
/*     */           if (player instanceof ServerPlayer) {
/*     */             ServerPlayer serverPlayer = (ServerPlayer)player;
/*     */             CriteriaTriggers.ITEM_USED_ON_BLOCK.trigger(serverPlayer, pos, itemInHand);
/*     */           } 
/*     */           itemInHand.shrink(1);
/*     */           level.setBlock(pos, waxedState, 11);
/*     */           level.gameEvent((Holder)GameEvent.BLOCK_CHANGE, pos, GameEvent.Context.of((Entity)player, waxedState));
/*     */           level.levelEvent((Entity)player, 3003, pos, 0);
/*     */           if (oldState.getBlock() instanceof ChestBlock && oldState.getValue((Property)ChestBlock.TYPE) != ChestType.SINGLE) {
/*     */             BlockPos neighborPos = ChestBlock.getConnectedBlockPos(pos, oldState);
/*     */             level.gameEvent((Holder)GameEvent.BLOCK_CHANGE, neighborPos, GameEvent.Context.of((Entity)player, level.getBlockState(neighborPos)));
/*     */             level.levelEvent((Entity)player, 3003, neighborPos, 0);
/*     */           } 
/*     */           return (InteractionResult)InteractionResult.SUCCESS;
/*  98 */         }).orElse(InteractionResult.PASS);
/*     */   }
/*     */   
/*     */   public static Optional<BlockState> getWaxed(BlockState oldState) {
/* 102 */     return Optional.<Block>ofNullable((Block)((BiMap)WAXABLES.get()).get(oldState.getBlock())).map(b -> b.withPropertiesOf(oldState));
/*     */   }
/*     */   
/*     */   public boolean tryApplyToSign(Level level, SignBlockEntity sign, boolean isFrontText, ItemStack item, Player player) {
/* 107 */     if (sign.setWaxed(true)) {
/* 108 */       level.levelEvent(null, 3003, sign.getBlockPos(), 0);
/* 109 */       return true;
/*     */     } 
/* 111 */     return false;
/*     */   }
/*     */   
/*     */   public boolean canApplyToSign(SignText text, ItemStack item, Player player) {
/* 116 */     return true;
/*     */   }
/*     */ }


/* Location:              C:\Users\karol\AppData\Roaming\ModrinthApp\meta\versions\26.2-0.19.5\26.2-0.19.5.jar!\net\minecraft\world\item\HoneycombItem.class
 * Java compiler version: 25 (69.0)
 * JD-Core Version:       1.1.3
 */