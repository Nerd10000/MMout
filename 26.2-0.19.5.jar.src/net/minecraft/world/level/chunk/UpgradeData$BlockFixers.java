/*     */ package net.minecraft.world.level.chunk;
/*     */ 
/*     */ import com.google.common.collect.Lists;
/*     */ import it.unimi.dsi.fastutil.objects.ObjectIterator;
/*     */ import it.unimi.dsi.fastutil.objects.ObjectOpenHashSet;
/*     */ import it.unimi.dsi.fastutil.objects.ObjectSet;
/*     */ import java.util.List;
/*     */ import net.minecraft.core.BlockPos;
/*     */ import net.minecraft.core.Direction;
/*     */ import net.minecraft.core.Vec3i;
/*     */ import net.minecraft.world.level.LevelAccessor;
/*     */ import net.minecraft.world.level.LevelReader;
/*     */ import net.minecraft.world.level.ScheduledTickAccess;
/*     */ import net.minecraft.world.level.block.Block;
/*     */ import net.minecraft.world.level.block.Blocks;
/*     */ import net.minecraft.world.level.block.ChestBlock;
/*     */ import net.minecraft.world.level.block.HorizontalDirectionalBlock;
/*     */ import net.minecraft.world.level.block.StemBlock;
/*     */ import net.minecraft.world.level.block.entity.BlockEntity;
/*     */ import net.minecraft.world.level.block.entity.ChestBlockEntity;
/*     */ import net.minecraft.world.level.block.state.BlockState;
/*     */ import net.minecraft.world.level.block.state.properties.BlockStateProperties;
/*     */ import net.minecraft.world.level.block.state.properties.ChestType;
/*     */ import net.minecraft.world.level.block.state.properties.Property;
/*     */ 
/*     */ enum BlockFixers implements UpgradeData.BlockFixer {
/* 228 */   BLACKLIST(new Block[] { 
/* 228 */       Blocks.OBSERVER, Blocks.NETHER_PORTAL, (Block)Blocks.CONCRETE_POWDER
/*     */       
/* 232 */       .white(), (Block)Blocks.CONCRETE_POWDER
/* 233 */       .orange(), (Block)Blocks.CONCRETE_POWDER
/* 234 */       .magenta(), (Block)Blocks.CONCRETE_POWDER
/* 235 */       .lightBlue(), (Block)Blocks.CONCRETE_POWDER
/* 236 */       .yellow(), (Block)Blocks.CONCRETE_POWDER
/* 237 */       .lime(), (Block)Blocks.CONCRETE_POWDER
/* 238 */       .pink(), (Block)Blocks.CONCRETE_POWDER
/* 239 */       .gray(), 
/* 239 */       (Block)Blocks.CONCRETE_POWDER
/* 240 */       .lightGray(), (Block)Blocks.CONCRETE_POWDER
/* 241 */       .cyan(), (Block)Blocks.CONCRETE_POWDER
/* 242 */       .purple(), (Block)Blocks.CONCRETE_POWDER
/* 243 */       .blue(), (Block)Blocks.CONCRETE_POWDER
/* 244 */       .brown(), (Block)Blocks.CONCRETE_POWDER
/* 245 */       .green(), (Block)Blocks.CONCRETE_POWDER
/* 246 */       .red(), (Block)Blocks.CONCRETE_POWDER
/* 247 */       .black(), Blocks.ANVIL, Blocks.CHIPPED_ANVIL, 
/* 247 */       Blocks.DAMAGED_ANVIL, Blocks.DRAGON_EGG, Blocks.GRAVEL, Blocks.SAND, Blocks.RED_SAND, Blocks.OAK_SIGN, Blocks.SPRUCE_SIGN, Blocks.BIRCH_SIGN, Blocks.ACACIA_SIGN, Blocks.CHERRY_SIGN, 
/* 247 */       Blocks.JUNGLE_SIGN, Blocks.DARK_OAK_SIGN, Blocks.PALE_OAK_SIGN, Blocks.OAK_WALL_SIGN, Blocks.SPRUCE_WALL_SIGN, Blocks.BIRCH_WALL_SIGN, Blocks.ACACIA_WALL_SIGN, Blocks.JUNGLE_WALL_SIGN, Blocks.DARK_OAK_WALL_SIGN, Blocks.PALE_OAK_WALL_SIGN, 
/* 247 */       Blocks.OAK_HANGING_SIGN, Blocks.SPRUCE_HANGING_SIGN, Blocks.BIRCH_HANGING_SIGN, Blocks.ACACIA_HANGING_SIGN, Blocks.JUNGLE_HANGING_SIGN, Blocks.DARK_OAK_HANGING_SIGN, Blocks.PALE_OAK_HANGING_SIGN, Blocks.OAK_WALL_HANGING_SIGN, Blocks.SPRUCE_WALL_HANGING_SIGN, Blocks.BIRCH_WALL_HANGING_SIGN, 
/* 247 */       Blocks.ACACIA_WALL_HANGING_SIGN, Blocks.JUNGLE_WALL_HANGING_SIGN, Blocks.DARK_OAK_WALL_HANGING_SIGN, Blocks.PALE_OAK_WALL_HANGING_SIGN }) {
/*     */     public BlockState updateShape(BlockState state, Direction direction, BlockState neighbour, LevelAccessor level, BlockPos pos, BlockPos neighbourPos) {
/* 288 */       return state;
/*     */     }
/*     */   },
/* 291 */   DEFAULT(new Block[0]) {
/*     */     public BlockState updateShape(BlockState state, Direction direction, BlockState neighbour, LevelAccessor level, BlockPos pos, BlockPos neighbourPos) {
/* 294 */       return state.updateShape((LevelReader)level, (ScheduledTickAccess)level, pos, direction, neighbourPos, level.getBlockState(neighbourPos), level.getRandom());
/*     */     }
/*     */   },
/* 297 */   CHEST(new Block[] { Blocks.CHEST, Blocks.TRAPPED_CHEST }) {
/*     */     public BlockState updateShape(BlockState state, Direction direction, BlockState neighbour, LevelAccessor level, BlockPos pos, BlockPos neighbourPos) {
/* 300 */       if (neighbour.is(state.getBlock()) && direction.getAxis().isHorizontal() && 
/* 301 */         state.getValue((Property)ChestBlock.TYPE) == ChestType.SINGLE && neighbour.getValue((Property)ChestBlock.TYPE) == ChestType.SINGLE) {
/* 302 */         Direction facing = (Direction)state.getValue((Property)ChestBlock.FACING);
/* 303 */         if (direction.getAxis() != facing.getAxis() && facing == neighbour.getValue((Property)ChestBlock.FACING)) {
/* 304 */           ChestType newType = (direction == facing.getClockWise()) ? ChestType.LEFT : ChestType.RIGHT;
/* 305 */           level.setBlock(neighbourPos, (BlockState)neighbour.setValue((Property)ChestBlock.TYPE, (Comparable)newType.getOpposite()), 18);
/* 308 */           if (facing == Direction.NORTH || facing == Direction.EAST) {
/* 309 */             BlockEntity one = level.getBlockEntity(pos);
/* 310 */             BlockEntity two = level.getBlockEntity(neighbourPos);
/* 311 */             if (one instanceof ChestBlockEntity) {
/* 311 */               ChestBlockEntity firstChestBlockEntity = (ChestBlockEntity)one;
/* 311 */               if (two instanceof ChestBlockEntity) {
/* 311 */                 ChestBlockEntity secondChestBlockEntity = (ChestBlockEntity)two;
/* 312 */                 ChestBlockEntity.swapContents(firstChestBlockEntity, secondChestBlockEntity);
/*     */               } 
/*     */             } 
/*     */           } 
/* 316 */           return (BlockState)state.setValue((Property)ChestBlock.TYPE, (Comparable)newType);
/*     */         } 
/*     */       } 
/* 320 */       return state;
/*     */     }
/*     */   },
/* 324 */   LEAVES(true, new Block[] { Blocks.ACACIA_LEAVES, Blocks.CHERRY_LEAVES, Blocks.BIRCH_LEAVES, Blocks.PALE_OAK_LEAVES, Blocks.DARK_OAK_LEAVES, Blocks.JUNGLE_LEAVES, Blocks.OAK_LEAVES, Blocks.SPRUCE_LEAVES }) {
/* 325 */     private final ThreadLocal<List<ObjectSet<BlockPos>>> queue = ThreadLocal.withInitial(() -> Lists.newArrayListWithCapacity(7));
/*     */     
/*     */     public BlockState updateShape(BlockState state, Direction direction, BlockState neighbour, LevelAccessor level, BlockPos pos, BlockPos neighbourPos) {
/* 329 */       BlockState newState = state.updateShape((LevelReader)level, (ScheduledTickAccess)level, pos, direction, neighbourPos, level.getBlockState(neighbourPos), level.getRandom());
/* 330 */       if (state != newState) {
/* 331 */         int distance = ((Integer)newState.getValue((Property)BlockStateProperties.DISTANCE)).intValue();
/* 332 */         List<ObjectSet<BlockPos>> queue = this.queue.get();
/* 333 */         if (queue.isEmpty())
/* 334 */           for (int i = 0; i < 7; i++)
/* 335 */             queue.add(new ObjectOpenHashSet());  
/* 338 */         ((ObjectSet)queue.get(distance)).add(pos.immutable());
/*     */       } 
/* 340 */       return state;
/*     */     }
/*     */     
/*     */     public void processChunk(LevelAccessor level) {
/* 345 */       BlockPos.MutableBlockPos neighborPos = new BlockPos.MutableBlockPos();
/* 347 */       List<ObjectSet<BlockPos>> queue = this.queue.get();
/* 348 */       for (int neighborDistance = 2; neighborDistance < queue.size(); neighborDistance++) {
/* 349 */         int currentDistance = neighborDistance - 1;
/* 350 */         ObjectSet<BlockPos> set = queue.get(currentDistance);
/* 351 */         ObjectSet<BlockPos> newSet = queue.get(neighborDistance);
/* 353 */         for (ObjectIterator<BlockPos> objectIterator = set.iterator(); objectIterator.hasNext(); ) {
/* 353 */           BlockPos pos = objectIterator.next();
/* 354 */           BlockState state = level.getBlockState(pos);
/* 355 */           if (((Integer)state.getValue((Property)BlockStateProperties.DISTANCE)).intValue() < currentDistance)
/*     */             continue; 
/* 359 */           level.setBlock(pos, (BlockState)state.setValue((Property)BlockStateProperties.DISTANCE, Integer.valueOf(currentDistance)), 18);
/* 361 */           if (neighborDistance != 7)
/* 362 */             for (Direction direction : DIRECTIONS) {
/* 363 */               neighborPos.setWithOffset((Vec3i)pos, direction);
/* 364 */               BlockState neighbor = level.getBlockState((BlockPos)neighborPos);
/* 366 */               if (neighbor.hasProperty((Property)BlockStateProperties.DISTANCE) && ((Integer)state.getValue((Property)BlockStateProperties.DISTANCE)).intValue() > neighborDistance)
/* 367 */                 newSet.add(neighborPos.immutable()); 
/*     */             }  
/*     */         } 
/*     */       } 
/* 374 */       queue.clear();
/*     */     }
/*     */   },
/* 377 */   STEM_BLOCK(new Block[] { Blocks.MELON_STEM, Blocks.PUMPKIN_STEM }) {
/*     */     public BlockState updateShape(BlockState state, Direction direction, BlockState neighbour, LevelAccessor level, BlockPos pos, BlockPos neighbourPos) {
/* 380 */       if (((Integer)state.getValue((Property)StemBlock.AGE)).intValue() == 7) {
/* 381 */         Block fruit = state.is(Blocks.PUMPKIN_STEM) ? Blocks.PUMPKIN : Blocks.MELON;
/* 382 */         if (neighbour.is(fruit))
/* 383 */           return (BlockState)(state.is(Blocks.PUMPKIN_STEM) ? Blocks.ATTACHED_PUMPKIN_STEM : Blocks.ATTACHED_MELON_STEM).defaultBlockState().setValue((Property)HorizontalDirectionalBlock.FACING, (Comparable)direction); 
/*     */       } 
/* 386 */       return state;
/*     */     }
/*     */   };
/*     */   
/*     */   public static final Direction[] DIRECTIONS;
/*     */   
/*     */   static {
/* 391 */     DIRECTIONS = Direction.values();
/*     */   }
/*     */   
/*     */   BlockFixers(boolean chunky, Block... blocks) {
/* 398 */     for (Block block : blocks)
/* 399 */       UpgradeData.MAP.put(block, this); 
/* 401 */     if (chunky)
/* 402 */       UpgradeData.CHUNKY_FIXERS.add(this); 
/*     */   }
/*     */ }


/* Location:              C:\Users\karol\AppData\Roaming\ModrinthApp\meta\versions\26.2-0.19.5\26.2-0.19.5.jar!\net\minecraft\world\level\chunk\UpgradeData$BlockFixers.class
 * Java compiler version: 25 (69.0)
 * JD-Core Version:       1.1.3
 */