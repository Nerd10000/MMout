/*     */ package net.minecraft.world.level.block;
/*     */ 
/*     */ import java.util.HashMap;
/*     */ import java.util.LinkedList;
/*     */ import java.util.List;
/*     */ import java.util.Map;
/*     */ import java.util.Objects;
/*     */ import java.util.function.Consumer;
/*     */ import java.util.function.IntFunction;
/*     */ import net.minecraft.core.BlockPos;
/*     */ import net.minecraft.core.Direction;
/*     */ import net.minecraft.world.level.LevelAccessor;
/*     */ import net.minecraft.world.level.block.state.BlockState;
/*     */ import net.minecraft.world.level.block.state.properties.SideChainPart;
/*     */ 
/*     */ public interface SideChainPartBlock {
/*     */   SideChainPart getSideChainPart(BlockState paramBlockState);
/*     */   
/*     */   BlockState setSideChainPart(BlockState paramBlockState, SideChainPart paramSideChainPart);
/*     */   
/*     */   Direction getFacing(BlockState paramBlockState);
/*     */   
/*     */   boolean isConnectable(BlockState paramBlockState);
/*     */   
/*     */   int getMaxChainLength();
/*     */   
/*     */   default List<BlockPos> getAllBlocksConnectedTo(LevelAccessor level, BlockPos pos) {
/*  28 */     BlockState state = level.getBlockState(pos);
/*  29 */     if (!isConnectable(state))
/*  30 */       return List.of(); 
/*  33 */     Neighbors neighbors = getNeighbors(level, pos, getFacing(state));
/*  34 */     List<BlockPos> results = new LinkedList<>();
/*  35 */     results.add(pos);
/*  37 */     Objects.requireNonNull(neighbors);
/*  37 */     Objects.requireNonNull(results);
/*  37 */     addBlocksConnectingTowards(neighbors::left, SideChainPart.LEFT, results::addFirst);
/*  38 */     Objects.requireNonNull(neighbors);
/*  38 */     Objects.requireNonNull(results);
/*  38 */     addBlocksConnectingTowards(neighbors::right, SideChainPart.RIGHT, results::addLast);
/*  40 */     return results;
/*     */   }
/*     */   
/*     */   private void addBlocksConnectingTowards(IntFunction<Neighbor> getNeighbor, SideChainPart endPart, Consumer<BlockPos> accumulator) {
/*  48 */     for (int steps = 1; steps < getMaxChainLength(); steps++) {
/*  49 */       Neighbor neighbor = getNeighbor.apply(steps);
/*  50 */       if (neighbor.connectsTowards(endPart))
/*  51 */         accumulator.accept(neighbor.pos()); 
/*  53 */       if (neighbor.isUnconnectableOrChainEnd())
/*     */         break; 
/*     */     } 
/*     */   }
/*     */   
/*     */   default void updateNeighborsAfterPoweringDown(LevelAccessor level, BlockPos pos, BlockState state) {
/*  60 */     Neighbors neighbors = getNeighbors(level, pos, getFacing(state));
/*  61 */     neighbors.left().disconnectFromRight();
/*  62 */     neighbors.right().disconnectFromLeft();
/*     */   }
/*     */   
/*     */   default void updateSelfAndNeighborsOnPoweringUp(LevelAccessor level, BlockPos pos, BlockState state, BlockState oldState) {
/*  66 */     if (!isConnectable(state))
/*     */       return; 
/*  70 */     if (isBeingUpdatedByNeighbor(state, oldState))
/*     */       return; 
/*  75 */     Neighbors neighbors = getNeighbors(level, pos, getFacing(state));
/*  76 */     SideChainPart newPartForSelf = SideChainPart.UNCONNECTED;
/*  78 */     int existingChainOnTheLeft = neighbors.left().isConnectable() ? getAllBlocksConnectedTo(level, neighbors.left().pos()).size() : 0;
/*  79 */     int existingChainOnTheRight = neighbors.right().isConnectable() ? getAllBlocksConnectedTo(level, neighbors.right().pos()).size() : 0;
/*  80 */     int currentChainLength = 1;
/*  82 */     if (canConnect(existingChainOnTheLeft, currentChainLength)) {
/*  83 */       newPartForSelf = newPartForSelf.whenConnectedToTheLeft();
/*  84 */       neighbors.left().connectToTheRight();
/*  85 */       currentChainLength += existingChainOnTheLeft;
/*     */     } 
/*  88 */     if (canConnect(existingChainOnTheRight, currentChainLength)) {
/*  89 */       newPartForSelf = newPartForSelf.whenConnectedToTheRight();
/*  90 */       neighbors.right().connectToTheLeft();
/*     */     } 
/*  93 */     setPart(level, pos, newPartForSelf);
/*     */   }
/*     */   
/*     */   private boolean canConnect(int newBlocksToConnectTo, int currentChainLength) {
/*  97 */     return (newBlocksToConnectTo > 0 && currentChainLength + newBlocksToConnectTo <= getMaxChainLength());
/*     */   }
/*     */   
/*     */   private boolean isBeingUpdatedByNeighbor(BlockState state, BlockState oldState) {
/* 101 */     boolean isGettingConnected = getSideChainPart(state).isConnected();
/* 102 */     boolean hasBeenConnectedBefore = (isConnectable(oldState) && getSideChainPart(oldState).isConnected());
/* 103 */     return (isGettingConnected || hasBeenConnectedBefore);
/*     */   }
/*     */   
/*     */   private Neighbors getNeighbors(LevelAccessor level, BlockPos center, Direction facing) {
/* 107 */     return new Neighbors(this, level, facing, center, new HashMap<>());
/*     */   }
/*     */   
/*     */   private void setPart(LevelAccessor level, BlockPos pos, SideChainPart newPart) {
/* 111 */     BlockState state = level.getBlockState(pos);
/* 112 */     if (getSideChainPart(state) != newPart)
/* 113 */       level.setBlock(pos, setSideChainPart(state, newPart), 3); 
/*     */   }
/*     */   
/*     */   public static final class Neighbors extends Record {
/*     */     private final SideChainPartBlock block;
/*     */     
/*     */     private final LevelAccessor level;
/*     */     
/*     */     private final Direction facing;
/*     */     
/*     */     private final BlockPos center;
/*     */     
/*     */     private final Map<BlockPos, SideChainPartBlock.Neighbor> cache;
/*     */     
/*     */     public Neighbors(SideChainPartBlock block, LevelAccessor level, Direction facing, BlockPos center, Map<BlockPos, SideChainPartBlock.Neighbor> cache) {
/* 117 */       this.block = block;
/* 117 */       this.level = level;
/* 117 */       this.facing = facing;
/* 117 */       this.center = center;
/* 117 */       this.cache = cache;
/*     */     }
/*     */     
/*     */     public final String toString() {
/*     */       // Byte code:
/*     */       //   0: aload_0
/*     */       //   1: <illegal opcode> toString : (Lnet/minecraft/world/level/block/SideChainPartBlock$Neighbors;)Ljava/lang/String;
/*     */       //   6: areturn
/*     */       // Line number table:
/*     */       //   Java source line number -> byte code offset
/*     */       //   #117	-> 0
/*     */       // Local variable table:
/*     */       //   start	length	slot	name	descriptor
/*     */       //   0	7	0	this	Lnet/minecraft/world/level/block/SideChainPartBlock$Neighbors;
/*     */     }
/*     */     
/*     */     public final int hashCode() {
/*     */       // Byte code:
/*     */       //   0: aload_0
/*     */       //   1: <illegal opcode> hashCode : (Lnet/minecraft/world/level/block/SideChainPartBlock$Neighbors;)I
/*     */       //   6: ireturn
/*     */       // Line number table:
/*     */       //   Java source line number -> byte code offset
/*     */       //   #117	-> 0
/*     */       // Local variable table:
/*     */       //   start	length	slot	name	descriptor
/*     */       //   0	7	0	this	Lnet/minecraft/world/level/block/SideChainPartBlock$Neighbors;
/*     */     }
/*     */     
/*     */     public final boolean equals(Object o) {
/*     */       // Byte code:
/*     */       //   0: aload_0
/*     */       //   1: aload_1
/*     */       //   2: <illegal opcode> equals : (Lnet/minecraft/world/level/block/SideChainPartBlock$Neighbors;Ljava/lang/Object;)Z
/*     */       //   7: ireturn
/*     */       // Line number table:
/*     */       //   Java source line number -> byte code offset
/*     */       //   #117	-> 0
/*     */       // Local variable table:
/*     */       //   start	length	slot	name	descriptor
/*     */       //   0	8	0	this	Lnet/minecraft/world/level/block/SideChainPartBlock$Neighbors;
/*     */       //   0	8	1	o	Ljava/lang/Object;
/*     */     }
/*     */     
/*     */     public SideChainPartBlock block() {
/* 117 */       return this.block;
/*     */     }
/*     */     
/*     */     public LevelAccessor level() {
/* 117 */       return this.level;
/*     */     }
/*     */     
/*     */     public Direction facing() {
/* 117 */       return this.facing;
/*     */     }
/*     */     
/*     */     public BlockPos center() {
/* 117 */       return this.center;
/*     */     }
/*     */     
/*     */     public Map<BlockPos, SideChainPartBlock.Neighbor> cache() {
/* 117 */       return this.cache;
/*     */     }
/*     */     
/*     */     private boolean isConnectableToThisBlock(BlockState neighbor) {
/* 120 */       return (this.block.isConnectable(neighbor) && this.block.getFacing(neighbor) == this.facing);
/*     */     }
/*     */     
/*     */     private SideChainPartBlock.Neighbor createNewNeighbor(BlockPos pos) {
/* 124 */       BlockState neighbor = this.level.getBlockState(pos);
/* 125 */       SideChainPart part = isConnectableToThisBlock(neighbor) ? this.block.getSideChainPart(neighbor) : null;
/* 126 */       return (part == null) ? new SideChainPartBlock.EmptyNeighbor(pos) : new SideChainPartBlock.SideChainNeighbor(this.level, this.block, pos, part);
/*     */     }
/*     */     
/*     */     private SideChainPartBlock.Neighbor getOrCreateNeighbor(Direction dir, Integer steps) {
/* 130 */       return this.cache.computeIfAbsent(this.center.relative(dir, steps.intValue()), this::createNewNeighbor);
/*     */     }
/*     */     
/*     */     public SideChainPartBlock.Neighbor left(int steps) {
/* 134 */       return getOrCreateNeighbor(this.facing.getClockWise(), Integer.valueOf(steps));
/*     */     }
/*     */     
/*     */     public SideChainPartBlock.Neighbor right(int steps) {
/* 138 */       return getOrCreateNeighbor(this.facing.getCounterClockWise(), Integer.valueOf(steps));
/*     */     }
/*     */     
/*     */     public SideChainPartBlock.Neighbor left() {
/* 142 */       return left(1);
/*     */     }
/*     */     
/*     */     public SideChainPartBlock.Neighbor right() {
/* 146 */       return right(1);
/*     */     }
/*     */   }
/*     */   
/*     */   public static interface Neighbor {
/*     */     BlockPos pos();
/*     */     
/*     */     boolean isConnectable();
/*     */     
/*     */     boolean isUnconnectableOrChainEnd();
/*     */     
/*     */     boolean connectsTowards(SideChainPart param1SideChainPart);
/*     */     
/*     */     default void connectToTheRight() {}
/*     */     
/*     */     default void connectToTheLeft() {}
/*     */     
/*     */     default void disconnectFromRight() {}
/*     */     
/*     */     default void disconnectFromLeft() {}
/*     */   }
/*     */   
/*     */   public static final class EmptyNeighbor extends Record implements Neighbor {
/*     */     private final BlockPos pos;
/*     */     
/*     */     public EmptyNeighbor(BlockPos pos) {
/* 165 */       this.pos = pos;
/*     */     }
/*     */     
/*     */     public final String toString() {
/*     */       // Byte code:
/*     */       //   0: aload_0
/*     */       //   1: <illegal opcode> toString : (Lnet/minecraft/world/level/block/SideChainPartBlock$EmptyNeighbor;)Ljava/lang/String;
/*     */       //   6: areturn
/*     */       // Line number table:
/*     */       //   Java source line number -> byte code offset
/*     */       //   #165	-> 0
/*     */       // Local variable table:
/*     */       //   start	length	slot	name	descriptor
/*     */       //   0	7	0	this	Lnet/minecraft/world/level/block/SideChainPartBlock$EmptyNeighbor;
/*     */     }
/*     */     
/*     */     public final int hashCode() {
/*     */       // Byte code:
/*     */       //   0: aload_0
/*     */       //   1: <illegal opcode> hashCode : (Lnet/minecraft/world/level/block/SideChainPartBlock$EmptyNeighbor;)I
/*     */       //   6: ireturn
/*     */       // Line number table:
/*     */       //   Java source line number -> byte code offset
/*     */       //   #165	-> 0
/*     */       // Local variable table:
/*     */       //   start	length	slot	name	descriptor
/*     */       //   0	7	0	this	Lnet/minecraft/world/level/block/SideChainPartBlock$EmptyNeighbor;
/*     */     }
/*     */     
/*     */     public final boolean equals(Object o) {
/*     */       // Byte code:
/*     */       //   0: aload_0
/*     */       //   1: aload_1
/*     */       //   2: <illegal opcode> equals : (Lnet/minecraft/world/level/block/SideChainPartBlock$EmptyNeighbor;Ljava/lang/Object;)Z
/*     */       //   7: ireturn
/*     */       // Line number table:
/*     */       //   Java source line number -> byte code offset
/*     */       //   #165	-> 0
/*     */       // Local variable table:
/*     */       //   start	length	slot	name	descriptor
/*     */       //   0	8	0	this	Lnet/minecraft/world/level/block/SideChainPartBlock$EmptyNeighbor;
/*     */       //   0	8	1	o	Ljava/lang/Object;
/*     */     }
/*     */     
/*     */     public BlockPos pos() {
/* 165 */       return this.pos;
/*     */     }
/*     */     
/*     */     public boolean isConnectable() {
/* 168 */       return false;
/*     */     }
/*     */     
/*     */     public boolean isUnconnectableOrChainEnd() {
/* 173 */       return true;
/*     */     }
/*     */     
/*     */     public boolean connectsTowards(SideChainPart endPart) {
/* 178 */       return false;
/*     */     }
/*     */   }
/*     */   
/*     */   public static final class SideChainNeighbor extends Record implements Neighbor {
/*     */     private final LevelAccessor level;
/*     */     
/*     */     private final SideChainPartBlock block;
/*     */     
/*     */     private final BlockPos pos;
/*     */     
/*     */     private final SideChainPart part;
/*     */     
/*     */     public SideChainNeighbor(LevelAccessor level, SideChainPartBlock block, BlockPos pos, SideChainPart part) {
/* 182 */       this.level = level;
/* 182 */       this.block = block;
/* 182 */       this.pos = pos;
/* 182 */       this.part = part;
/*     */     }
/*     */     
/*     */     public final String toString() {
/*     */       // Byte code:
/*     */       //   0: aload_0
/*     */       //   1: <illegal opcode> toString : (Lnet/minecraft/world/level/block/SideChainPartBlock$SideChainNeighbor;)Ljava/lang/String;
/*     */       //   6: areturn
/*     */       // Line number table:
/*     */       //   Java source line number -> byte code offset
/*     */       //   #182	-> 0
/*     */       // Local variable table:
/*     */       //   start	length	slot	name	descriptor
/*     */       //   0	7	0	this	Lnet/minecraft/world/level/block/SideChainPartBlock$SideChainNeighbor;
/*     */     }
/*     */     
/*     */     public final int hashCode() {
/*     */       // Byte code:
/*     */       //   0: aload_0
/*     */       //   1: <illegal opcode> hashCode : (Lnet/minecraft/world/level/block/SideChainPartBlock$SideChainNeighbor;)I
/*     */       //   6: ireturn
/*     */       // Line number table:
/*     */       //   Java source line number -> byte code offset
/*     */       //   #182	-> 0
/*     */       // Local variable table:
/*     */       //   start	length	slot	name	descriptor
/*     */       //   0	7	0	this	Lnet/minecraft/world/level/block/SideChainPartBlock$SideChainNeighbor;
/*     */     }
/*     */     
/*     */     public final boolean equals(Object o) {
/*     */       // Byte code:
/*     */       //   0: aload_0
/*     */       //   1: aload_1
/*     */       //   2: <illegal opcode> equals : (Lnet/minecraft/world/level/block/SideChainPartBlock$SideChainNeighbor;Ljava/lang/Object;)Z
/*     */       //   7: ireturn
/*     */       // Line number table:
/*     */       //   Java source line number -> byte code offset
/*     */       //   #182	-> 0
/*     */       // Local variable table:
/*     */       //   start	length	slot	name	descriptor
/*     */       //   0	8	0	this	Lnet/minecraft/world/level/block/SideChainPartBlock$SideChainNeighbor;
/*     */       //   0	8	1	o	Ljava/lang/Object;
/*     */     }
/*     */     
/*     */     public LevelAccessor level() {
/* 182 */       return this.level;
/*     */     }
/*     */     
/*     */     public SideChainPartBlock block() {
/* 182 */       return this.block;
/*     */     }
/*     */     
/*     */     public BlockPos pos() {
/* 182 */       return this.pos;
/*     */     }
/*     */     
/*     */     public SideChainPart part() {
/* 182 */       return this.part;
/*     */     }
/*     */     
/*     */     public boolean isConnectable() {
/* 185 */       return true;
/*     */     }
/*     */     
/*     */     public boolean isUnconnectableOrChainEnd() {
/* 190 */       return this.part.isChainEnd();
/*     */     }
/*     */     
/*     */     public boolean connectsTowards(SideChainPart endPart) {
/* 195 */       return this.part.isConnectionTowards(endPart);
/*     */     }
/*     */     
/*     */     public void connectToTheRight() {
/* 200 */       this.block.setPart(this.level, this.pos, this.part.whenConnectedToTheRight());
/*     */     }
/*     */     
/*     */     public void connectToTheLeft() {
/* 205 */       this.block.setPart(this.level, this.pos, this.part.whenConnectedToTheLeft());
/*     */     }
/*     */     
/*     */     public void disconnectFromRight() {
/* 210 */       this.block.setPart(this.level, this.pos, this.part.whenDisconnectedFromTheRight());
/*     */     }
/*     */     
/*     */     public void disconnectFromLeft() {
/* 215 */       this.block.setPart(this.level, this.pos, this.part.whenDisconnectedFromTheLeft());
/*     */     }
/*     */   }
/*     */ }


/* Location:              C:\Users\karol\AppData\Roaming\ModrinthApp\meta\versions\26.2-0.19.5\26.2-0.19.5.jar!\net\minecraft\world\level\block\SideChainPartBlock.class
 * Java compiler version: 25 (69.0)
 * JD-Core Version:       1.1.3
 */