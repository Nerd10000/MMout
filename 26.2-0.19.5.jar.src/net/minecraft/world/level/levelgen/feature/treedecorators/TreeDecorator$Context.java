/*    */ package net.minecraft.world.level.levelgen.feature.treedecorators;
/*    */ 
/*    */ import it.unimi.dsi.fastutil.objects.ObjectArrayList;
/*    */ import java.util.Comparator;
/*    */ import java.util.Set;
/*    */ import java.util.function.BiConsumer;
/*    */ import java.util.function.Predicate;
/*    */ import net.minecraft.core.BlockPos;
/*    */ import net.minecraft.core.Vec3i;
/*    */ import net.minecraft.util.RandomSource;
/*    */ import net.minecraft.world.level.WorldGenLevel;
/*    */ import net.minecraft.world.level.block.Blocks;
/*    */ import net.minecraft.world.level.block.state.BlockBehaviour;
/*    */ import net.minecraft.world.level.block.state.BlockState;
/*    */ import net.minecraft.world.level.block.state.properties.BooleanProperty;
/*    */ import net.minecraft.world.level.block.state.properties.Property;
/*    */ 
/*    */ public final class Context {
/*    */   private final WorldGenLevel level;
/*    */   
/*    */   private final BiConsumer<BlockPos, BlockState> decorationSetter;
/*    */   
/*    */   private final RandomSource random;
/*    */   
/*    */   private final ObjectArrayList<BlockPos> logs;
/*    */   
/*    */   private final ObjectArrayList<BlockPos> leaves;
/*    */   
/*    */   private final ObjectArrayList<BlockPos> roots;
/*    */   
/*    */   public Context(WorldGenLevel level, BiConsumer<BlockPos, BlockState> decorationSetter, RandomSource random, Set<BlockPos> trunkSet, Set<BlockPos> foliageSet, Set<BlockPos> rootSet) {
/* 34 */     this.level = level;
/* 35 */     this.decorationSetter = decorationSetter;
/* 36 */     this.random = random;
/* 38 */     this.roots = new ObjectArrayList(rootSet);
/* 39 */     this.logs = new ObjectArrayList(trunkSet);
/* 40 */     this.leaves = new ObjectArrayList(foliageSet);
/* 42 */     this.logs.sort(Comparator.comparingInt(Vec3i::getY));
/* 43 */     this.leaves.sort(Comparator.comparingInt(Vec3i::getY));
/* 44 */     this.roots.sort(Comparator.comparingInt(Vec3i::getY));
/*    */   }
/*    */   
/*    */   public void placeVine(BlockPos pos, BooleanProperty direction) {
/* 48 */     setBlock(pos, (BlockState)Blocks.VINE.defaultBlockState().setValue((Property)direction, Boolean.valueOf(true)));
/*    */   }
/*    */   
/*    */   public void setBlock(BlockPos pos, BlockState state) {
/* 52 */     this.decorationSetter.accept(pos, state);
/*    */   }
/*    */   
/*    */   public boolean isAir(BlockPos pos) {
/* 56 */     return this.level.isStateAtPosition(pos, BlockBehaviour.BlockStateBase::isAir);
/*    */   }
/*    */   
/*    */   public boolean checkBlock(BlockPos pos, Predicate<BlockState> predicate) {
/* 60 */     return this.level.isStateAtPosition(pos, predicate);
/*    */   }
/*    */   
/*    */   public WorldGenLevel level() {
/* 64 */     return this.level;
/*    */   }
/*    */   
/*    */   public RandomSource random() {
/* 68 */     return this.random;
/*    */   }
/*    */   
/*    */   public ObjectArrayList<BlockPos> logs() {
/* 72 */     return this.logs;
/*    */   }
/*    */   
/*    */   public ObjectArrayList<BlockPos> leaves() {
/* 76 */     return this.leaves;
/*    */   }
/*    */   
/*    */   public ObjectArrayList<BlockPos> roots() {
/* 80 */     return this.roots;
/*    */   }
/*    */ }


/* Location:              C:\Users\karol\AppData\Roaming\ModrinthApp\meta\versions\26.2-0.19.5\26.2-0.19.5.jar!\net\minecraft\world\level\levelgen\feature\treedecorators\TreeDecorator$Context.class
 * Java compiler version: 25 (69.0)
 * JD-Core Version:       1.1.3
 */