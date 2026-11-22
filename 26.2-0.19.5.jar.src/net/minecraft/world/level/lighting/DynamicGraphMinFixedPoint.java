/*     */ package net.minecraft.world.level.lighting;
/*     */ 
/*     */ import it.unimi.dsi.fastutil.longs.Long2ByteMap;
/*     */ import it.unimi.dsi.fastutil.longs.Long2ByteOpenHashMap;
/*     */ import it.unimi.dsi.fastutil.longs.LongArrayList;
/*     */ import it.unimi.dsi.fastutil.longs.LongList;
/*     */ import java.util.function.LongPredicate;
/*     */ import net.minecraft.util.Mth;
/*     */ 
/*     */ public abstract class DynamicGraphMinFixedPoint {
/*     */   public static final long SOURCE = 9223372036854775807L;
/*     */   
/*     */   private static final int NO_COMPUTED_LEVEL = 255;
/*     */   
/*     */   protected final int levelCount;
/*     */   
/*     */   private final LeveledPriorityQueue priorityQueue;
/*     */   
/*     */   private final Long2ByteMap computedLevels;
/*     */   
/*     */   private volatile boolean hasWork;
/*     */   
/*     */   protected DynamicGraphMinFixedPoint(int levelCount, int minQueueSize, final int minMapSize) {
/*  36 */     if (levelCount >= 254)
/*  37 */       throw new IllegalArgumentException("Level count must be < 254."); 
/*  39 */     this.levelCount = levelCount;
/*  41 */     this.priorityQueue = new LeveledPriorityQueue(levelCount, minQueueSize);
/*  43 */     this.computedLevels = (Long2ByteMap)new Long2ByteOpenHashMap(minMapSize, 0.5F) {
/*     */         protected void rehash(int newN) {
/*  46 */           if (newN > minMapSize)
/*  47 */             super.rehash(newN); 
/*     */         }
/*     */       };
/*  51 */     this.computedLevels.defaultReturnValue((byte)-1);
/*     */   }
/*     */   
/*     */   protected void removeFromQueue(long node) {
/*  55 */     int computedLevel = this.computedLevels.remove(node) & 0xFF;
/*  56 */     if (computedLevel == 255)
/*     */       return; 
/*  59 */     int level = getLevel(node);
/*  60 */     int priority = calculatePriority(level, computedLevel);
/*  61 */     this.priorityQueue.dequeue(node, priority, this.levelCount);
/*  62 */     this.hasWork = !this.priorityQueue.isEmpty();
/*     */   }
/*     */   
/*     */   public void removeIf(LongPredicate pred) {
/*  66 */     LongArrayList longArrayList = new LongArrayList();
/*  68 */     this.computedLevels.keySet().forEach(node -> {
/*     */           if (pred.test(node))
/*     */             nodesToRemove.add(node); 
/*     */         });
/*  74 */     longArrayList.forEach(this::removeFromQueue);
/*     */   }
/*     */   
/*     */   private int calculatePriority(int level, int computedLevel) {
/*  78 */     return Math.min(Math.min(level, computedLevel), this.levelCount - 1);
/*     */   }
/*     */   
/*     */   protected void checkNode(long node) {
/*  82 */     checkEdge(node, node, this.levelCount - 1, false);
/*     */   }
/*     */   
/*     */   protected void checkEdge(long from, long to, int newLevelFrom, boolean onlyDecreased) {
/*  86 */     checkEdge(from, to, newLevelFrom, getLevel(to), this.computedLevels.get(to) & 0xFF, onlyDecreased);
/*  87 */     this.hasWork = !this.priorityQueue.isEmpty();
/*     */   }
/*     */   
/*     */   private void checkEdge(long from, long to, int newLevelFrom, int levelTo, int oldComputedLevel, boolean onlyDecreased) {
/*     */     int newComputedLevel;
/*  91 */     if (isSource(to))
/*     */       return; 
/*  94 */     newLevelFrom = Mth.clamp(newLevelFrom, 0, this.levelCount - 1);
/*  95 */     levelTo = Mth.clamp(levelTo, 0, this.levelCount - 1);
/*  96 */     boolean wasConsistent = (oldComputedLevel == 255);
/*  97 */     if (wasConsistent)
/*  98 */       oldComputedLevel = levelTo; 
/* 101 */     if (onlyDecreased) {
/* 103 */       newComputedLevel = Math.min(oldComputedLevel, newLevelFrom);
/*     */     } else {
/* 105 */       newComputedLevel = Mth.clamp(getComputedLevel(to, from, newLevelFrom), 0, this.levelCount - 1);
/*     */     } 
/* 107 */     int oldPriority = calculatePriority(levelTo, oldComputedLevel);
/* 108 */     if (levelTo != newComputedLevel) {
/* 109 */       int newPriority = calculatePriority(levelTo, newComputedLevel);
/* 110 */       if (oldPriority != newPriority && !wasConsistent)
/* 111 */         this.priorityQueue.dequeue(to, oldPriority, newPriority); 
/* 113 */       this.priorityQueue.enqueue(to, newPriority);
/* 114 */       this.computedLevels.put(to, (byte)newComputedLevel);
/* 115 */     } else if (!wasConsistent) {
/* 116 */       this.priorityQueue.dequeue(to, oldPriority, this.levelCount);
/* 117 */       this.computedLevels.remove(to);
/*     */     } 
/*     */   }
/*     */   
/*     */   protected final void checkNeighbor(long from, long to, int level, boolean onlyDecreased) {
/* 122 */     int storedOldComputedLevel = this.computedLevels.get(to) & 0xFF;
/* 123 */     int levelFrom = Mth.clamp(computeLevelFromNeighbor(from, to, level), 0, this.levelCount - 1);
/* 124 */     if (onlyDecreased) {
/* 125 */       checkEdge(from, to, levelFrom, getLevel(to), storedOldComputedLevel, onlyDecreased);
/*     */     } else {
/*     */       int oldComputedLevel;
/* 128 */       boolean wasConsistent = (storedOldComputedLevel == 255);
/* 129 */       if (wasConsistent) {
/* 130 */         oldComputedLevel = Mth.clamp(getLevel(to), 0, this.levelCount - 1);
/*     */       } else {
/* 132 */         oldComputedLevel = storedOldComputedLevel;
/*     */       } 
/* 135 */       if (levelFrom == oldComputedLevel)
/* 137 */         checkEdge(from, to, this.levelCount - 1, wasConsistent ? oldComputedLevel : getLevel(to), storedOldComputedLevel, onlyDecreased); 
/*     */     } 
/*     */   }
/*     */   
/*     */   protected final boolean hasWork() {
/* 143 */     return this.hasWork;
/*     */   }
/*     */   
/*     */   protected final int runUpdates(int count) {
/* 147 */     if (this.priorityQueue.isEmpty())
/* 148 */       return count; 
/* 150 */     while (!this.priorityQueue.isEmpty() && count > 0) {
/* 151 */       count--;
/* 152 */       long node = this.priorityQueue.removeFirstLong();
/* 153 */       int level = Mth.clamp(getLevel(node), 0, this.levelCount - 1);
/* 154 */       int computedLevel = this.computedLevels.remove(node) & 0xFF;
/* 155 */       if (computedLevel < level) {
/* 157 */         setLevel(node, computedLevel);
/* 158 */         checkNeighborsAfterUpdate(node, computedLevel, true);
/*     */         continue;
/*     */       } 
/* 159 */       if (computedLevel > level) {
/* 161 */         setLevel(node, this.levelCount - 1);
/* 162 */         if (computedLevel != this.levelCount - 1) {
/* 164 */           this.priorityQueue.enqueue(node, calculatePriority(this.levelCount - 1, computedLevel));
/* 165 */           this.computedLevels.put(node, (byte)computedLevel);
/*     */         } 
/* 167 */         checkNeighborsAfterUpdate(node, level, false);
/*     */       } 
/*     */     } 
/* 170 */     this.hasWork = !this.priorityQueue.isEmpty();
/* 171 */     return count;
/*     */   }
/*     */   
/*     */   public int getQueueSize() {
/* 175 */     return this.computedLevels.size();
/*     */   }
/*     */   
/*     */   protected boolean isSource(long node) {
/* 179 */     return (node == Long.MAX_VALUE);
/*     */   }
/*     */   
/*     */   protected abstract int getComputedLevel(long paramLong1, long paramLong2, int paramInt);
/*     */   
/*     */   protected abstract void checkNeighborsAfterUpdate(long paramLong, int paramInt, boolean paramBoolean);
/*     */   
/*     */   protected abstract int getLevel(long paramLong);
/*     */   
/*     */   protected abstract void setLevel(long paramLong, int paramInt);
/*     */   
/*     */   protected abstract int computeLevelFromNeighbor(long paramLong1, long paramLong2, int paramInt);
/*     */ }


/* Location:              C:\Users\karol\AppData\Roaming\ModrinthApp\meta\versions\26.2-0.19.5\26.2-0.19.5.jar!\net\minecraft\world\level\lighting\DynamicGraphMinFixedPoint.class
 * Java compiler version: 25 (69.0)
 * JD-Core Version:       1.1.3
 */