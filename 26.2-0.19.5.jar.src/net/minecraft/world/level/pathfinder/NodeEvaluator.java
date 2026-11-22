/*     */ package net.minecraft.world.level.pathfinder;
/*     */ 
/*     */ import it.unimi.dsi.fastutil.ints.Int2ObjectMap;
/*     */ import it.unimi.dsi.fastutil.ints.Int2ObjectOpenHashMap;
/*     */ import net.minecraft.core.BlockPos;
/*     */ import net.minecraft.tags.BlockTags;
/*     */ import net.minecraft.util.Mth;
/*     */ import net.minecraft.world.entity.Mob;
/*     */ import net.minecraft.world.level.CollisionGetter;
/*     */ import net.minecraft.world.level.PathNavigationRegion;
/*     */ import net.minecraft.world.level.block.Blocks;
/*     */ import net.minecraft.world.level.block.CampfireBlock;
/*     */ import net.minecraft.world.level.block.state.BlockState;
/*     */ 
/*     */ public abstract class NodeEvaluator {
/*     */   protected PathfindingContext currentContext;
/*     */   
/*     */   protected Mob mob;
/*     */   
/*  20 */   protected final Int2ObjectMap<Node> nodes = (Int2ObjectMap<Node>)new Int2ObjectOpenHashMap();
/*     */   
/*     */   protected int entityWidth;
/*     */   
/*     */   protected int entityHeight;
/*     */   
/*     */   protected int entityDepth;
/*     */   
/*     */   protected boolean canPassDoors = true;
/*     */   
/*     */   protected boolean canOpenDoors;
/*     */   
/*     */   protected boolean canFloat;
/*     */   
/*     */   protected boolean canWalkOverFences;
/*     */   
/*     */   public void prepare(PathNavigationRegion level, Mob entity) {
/*  34 */     this.currentContext = new PathfindingContext((CollisionGetter)level, entity);
/*  35 */     this.mob = entity;
/*  36 */     this.nodes.clear();
/*  38 */     this.entityWidth = Mth.floor(entity.getBbWidth() + 1.0F);
/*  39 */     this.entityHeight = Mth.floor(entity.getBbHeight() + 1.0F);
/*  40 */     this.entityDepth = Mth.floor(entity.getBbWidth() + 1.0F);
/*     */   }
/*     */   
/*     */   public void done() {
/*  44 */     this.currentContext = null;
/*  45 */     this.mob = null;
/*     */   }
/*     */   
/*     */   protected Node getNode(BlockPos pos) {
/*  49 */     return getNode(pos.getX(), pos.getY(), pos.getZ());
/*     */   }
/*     */   
/*     */   protected Node getNode(int x, int y, int z) {
/*  53 */     return (Node)this.nodes.computeIfAbsent(Node.createHash(x, y, z), k -> new Node(x, y, z));
/*     */   }
/*     */   
/*     */   protected Target getTargetNodeAt(double x, double y, double z) {
/*  61 */     return new Target(getNode(Mth.floor(x), Mth.floor(y), Mth.floor(z)));
/*     */   }
/*     */   
/*     */   public PathType getPathType(Mob mob, BlockPos pos) {
/*  71 */     return getPathType(new PathfindingContext((CollisionGetter)mob.level(), mob), pos.getX(), pos.getY(), pos.getZ());
/*     */   }
/*     */   
/*     */   public void setCanPassDoors(boolean canPassDoors) {
/*  75 */     this.canPassDoors = canPassDoors;
/*     */   }
/*     */   
/*     */   public void setCanOpenDoors(boolean canOpenDoors) {
/*  79 */     this.canOpenDoors = canOpenDoors;
/*     */   }
/*     */   
/*     */   public void setCanFloat(boolean canFloat) {
/*  83 */     this.canFloat = canFloat;
/*     */   }
/*     */   
/*     */   public void setCanWalkOverFences(boolean canWalkOverFences) {
/*  87 */     this.canWalkOverFences = canWalkOverFences;
/*     */   }
/*     */   
/*     */   public boolean canPassDoors() {
/*  91 */     return this.canPassDoors;
/*     */   }
/*     */   
/*     */   public boolean canOpenDoors() {
/*  95 */     return this.canOpenDoors;
/*     */   }
/*     */   
/*     */   public boolean canFloat() {
/*  99 */     return this.canFloat;
/*     */   }
/*     */   
/*     */   public boolean canWalkOverFences() {
/* 103 */     return this.canWalkOverFences;
/*     */   }
/*     */   
/*     */   public static boolean isBurningBlock(BlockState blockState) {
/* 107 */     return (blockState.is(BlockTags.FIRE) || blockState
/* 108 */       .is(Blocks.LAVA) || blockState
/* 109 */       .is(Blocks.MAGMA_BLOCK) || 
/* 110 */       CampfireBlock.isLitCampfire(blockState) || blockState
/* 111 */       .is(Blocks.LAVA_CAULDRON));
/*     */   }
/*     */   
/*     */   public abstract Node getStart();
/*     */   
/*     */   public abstract Target getTarget(double paramDouble1, double paramDouble2, double paramDouble3);
/*     */   
/*     */   public abstract int getNeighbors(Node[] paramArrayOfNode, Node paramNode);
/*     */   
/*     */   public abstract PathType getPathTypeOfMob(PathfindingContext paramPathfindingContext, int paramInt1, int paramInt2, int paramInt3, Mob paramMob);
/*     */   
/*     */   public abstract PathType getPathType(PathfindingContext paramPathfindingContext, int paramInt1, int paramInt2, int paramInt3);
/*     */ }


/* Location:              C:\Users\karol\AppData\Roaming\ModrinthApp\meta\versions\26.2-0.19.5\26.2-0.19.5.jar!\net\minecraft\world\level\pathfinder\NodeEvaluator.class
 * Java compiler version: 25 (69.0)
 * JD-Core Version:       1.1.3
 */