/*     */ package net.minecraft.world.entity.ai.navigation;
/*     */ 
/*     */ import net.minecraft.core.BlockPos;
/*     */ import net.minecraft.core.Direction;
/*     */ import net.minecraft.core.SectionPos;
/*     */ import net.minecraft.util.Mth;
/*     */ import net.minecraft.world.entity.Entity;
/*     */ import net.minecraft.world.entity.Mob;
/*     */ import net.minecraft.world.level.Level;
/*     */ import net.minecraft.world.level.block.Blocks;
/*     */ import net.minecraft.world.level.block.state.BlockState;
/*     */ import net.minecraft.world.level.chunk.LevelChunk;
/*     */ import net.minecraft.world.level.pathfinder.Node;
/*     */ import net.minecraft.world.level.pathfinder.NodeEvaluator;
/*     */ import net.minecraft.world.level.pathfinder.Path;
/*     */ import net.minecraft.world.level.pathfinder.PathFinder;
/*     */ import net.minecraft.world.level.pathfinder.WalkNodeEvaluator;
/*     */ import net.minecraft.world.phys.Vec3;
/*     */ 
/*     */ public class GroundPathNavigation extends PathNavigation {
/*     */   private boolean avoidSun;
/*     */   
/*     */   private boolean canPathToTargetsBelowSurface;
/*     */   
/*     */   public GroundPathNavigation(Mob mob, Level level) {
/*  24 */     super(mob, level);
/*     */   }
/*     */   
/*     */   protected PathFinder createPathFinder(int maxVisitedNodes) {
/*  29 */     this.nodeEvaluator = (NodeEvaluator)new WalkNodeEvaluator();
/*  30 */     return new PathFinder(this.nodeEvaluator, maxVisitedNodes);
/*     */   }
/*     */   
/*     */   protected boolean canUpdatePath() {
/*  35 */     return (this.mob.onGround() || this.mob.isInLiquid() || this.mob.isPassenger());
/*     */   }
/*     */   
/*     */   protected Vec3 getTempMobPos() {
/*  40 */     return new Vec3(this.mob.getX(), getSurfaceY(), this.mob.getZ());
/*     */   }
/*     */   
/*     */   public Path createPath(BlockPos pos, int reachRange) {
/*  45 */     LevelChunk chunk = this.level.getChunkSource().getChunkNow(SectionPos.blockToSectionCoord(pos.getX()), SectionPos.blockToSectionCoord(pos.getZ()));
/*  46 */     if (chunk == null)
/*  47 */       return null; 
/*  50 */     if (!this.canPathToTargetsBelowSurface)
/*  51 */       pos = findSurfacePosition(chunk, pos, reachRange); 
/*  54 */     return super.createPath(pos, reachRange);
/*     */   }
/*     */   
/*     */   private BlockPos findSurfacePosition(LevelChunk chunk, BlockPos pos, int reachRange) {
/*     */     BlockPos.MutableBlockPos mutableBlockPos;
/*  58 */     if (chunk.getBlockState(pos).isAir()) {
/*  59 */       BlockPos.MutableBlockPos columnPos = pos.mutable().move(Direction.DOWN);
/*  60 */       while (columnPos.getY() >= this.level.getMinY() && chunk.getBlockState((BlockPos)columnPos).isAir())
/*  61 */         columnPos.move(Direction.DOWN); 
/*  64 */       if (columnPos.getY() >= this.level.getMinY())
/*  65 */         return columnPos.above(); 
/*  68 */       columnPos.setY(pos.getY() + 1);
/*  69 */       while (columnPos.getY() <= this.level.getMaxY() && chunk.getBlockState((BlockPos)columnPos).isAir())
/*  70 */         columnPos.move(Direction.UP); 
/*  72 */       mutableBlockPos = columnPos;
/*     */     } 
/*  75 */     if (chunk.getBlockState((BlockPos)mutableBlockPos).isSolid()) {
/*  76 */       BlockPos.MutableBlockPos columnPos = mutableBlockPos.mutable().move(Direction.UP);
/*  77 */       while (columnPos.getY() <= this.level.getMaxY() && chunk.getBlockState((BlockPos)columnPos).isSolid())
/*  78 */         columnPos.move(Direction.UP); 
/*  80 */       return columnPos.immutable();
/*     */     } 
/*  82 */     return (BlockPos)mutableBlockPos;
/*     */   }
/*     */   
/*     */   public Path createPath(Entity target, int reachRange) {
/*  87 */     return createPath(target.blockPosition(), reachRange);
/*     */   }
/*     */   
/*     */   private int getSurfaceY() {
/*  91 */     if (!this.mob.isInWater() || !canFloat())
/*  92 */       return Mth.floor(this.mob.getY() + 0.5D); 
/*  96 */     int surface = this.mob.getBlockY();
/*  97 */     BlockState state = this.level.getBlockState(BlockPos.containing(this.mob.getX(), surface, this.mob.getZ()));
/*  98 */     int steps = 0;
/*  99 */     while (state.is(Blocks.WATER)) {
/* 100 */       surface++;
/* 101 */       state = this.level.getBlockState(BlockPos.containing(this.mob.getX(), surface, this.mob.getZ()));
/* 102 */       if (++steps > 16)
/* 103 */         return this.mob.getBlockY(); 
/*     */     } 
/* 106 */     return surface;
/*     */   }
/*     */   
/*     */   protected void trimPath() {
/* 111 */     super.trimPath();
/* 113 */     if (this.avoidSun) {
/* 114 */       if (this.level.canSeeSky(BlockPos.containing(this.mob.getX(), this.mob.getY() + 0.5D, this.mob.getZ())))
/*     */         return; 
/* 118 */       for (int i = 0; i < this.path.getNodeCount(); i++) {
/* 119 */         Node node = this.path.getNode(i);
/* 120 */         if (this.level.canSeeSky(new BlockPos(node.x, node.y, node.z))) {
/* 121 */           this.path.truncateNodes(i);
/*     */           return;
/*     */         } 
/*     */       } 
/*     */     } 
/*     */   }
/*     */   
/*     */   public boolean canNavigateGround() {
/* 130 */     return true;
/*     */   }
/*     */   
/*     */   public void setAvoidSun(boolean avoidSun) {
/* 134 */     this.avoidSun = avoidSun;
/*     */   }
/*     */   
/*     */   public void setCanWalkOverFences(boolean canWalkOverFences) {
/* 138 */     this.nodeEvaluator.setCanWalkOverFences(canWalkOverFences);
/*     */   }
/*     */   
/*     */   public void setCanPathToTargetsBelowSurface(boolean canPathToTargetsBelowSurface) {
/* 142 */     this.canPathToTargetsBelowSurface = canPathToTargetsBelowSurface;
/*     */   }
/*     */ }


/* Location:              C:\Users\karol\AppData\Roaming\ModrinthApp\meta\versions\26.2-0.19.5\26.2-0.19.5.jar!\net\minecraft\world\entity\ai\navigation\GroundPathNavigation.class
 * Java compiler version: 25 (69.0)
 * JD-Core Version:       1.1.3
 */