/*     */ package net.minecraft.world.ticks;
/*     */ 
/*     */ import it.unimi.dsi.fastutil.objects.ObjectOpenCustomHashSet;
/*     */ import java.util.ArrayList;
/*     */ import java.util.Comparator;
/*     */ import java.util.Iterator;
/*     */ import java.util.List;
/*     */ import java.util.PriorityQueue;
/*     */ import java.util.Queue;
/*     */ import java.util.Set;
/*     */ import java.util.function.BiConsumer;
/*     */ import java.util.function.Predicate;
/*     */ import java.util.stream.Stream;
/*     */ import net.minecraft.core.BlockPos;
/*     */ 
/*     */ public class LevelChunkTicks<T> implements TickContainerAccess<T>, SerializableTickContainer<T> {
/*  19 */   private static final Comparator<ScheduledTick<?>> SUB_TICK_ORDERING = Comparator.comparingLong(ScheduledTick::subTickOrder);
/*     */   
/*  20 */   private final Queue<ScheduledTick<T>> tickQueue = (Queue)new PriorityQueue<>(ScheduledTick.DRAIN_ORDER);
/*     */   
/*     */   private List<SavedTick<T>> pendingTicks;
/*     */   
/*  26 */   private final Set<ScheduledTick<?>> ticksPerPosition = (Set<ScheduledTick<?>>)new ObjectOpenCustomHashSet(ScheduledTick.UNIQUE_TICK_HASH);
/*     */   
/*     */   private BiConsumer<LevelChunkTicks<T>, ScheduledTick<T>> onTickAdded;
/*     */   
/*     */   public LevelChunkTicks() {}
/*     */   
/*     */   public LevelChunkTicks(List<SavedTick<T>> pendingTicks) {
/*  34 */     this.pendingTicks = pendingTicks;
/*  35 */     for (SavedTick<T> pendingTick : pendingTicks)
/*  36 */       this.ticksPerPosition.add(ScheduledTick.probe(pendingTick.type(), pendingTick.pos())); 
/*     */   }
/*     */   
/*     */   public void setOnTickAdded(BiConsumer<LevelChunkTicks<T>, ScheduledTick<T>> onTickAdded) {
/*  41 */     this.onTickAdded = onTickAdded;
/*     */   }
/*     */   
/*     */   public ScheduledTick<T> peek() {
/*  46 */     return this.tickQueue.peek();
/*     */   }
/*     */   
/*     */   public ScheduledTick<T> poll() {
/*  50 */     ScheduledTick<T> result = this.tickQueue.poll();
/*  51 */     if (result != null)
/*  52 */       this.ticksPerPosition.remove(result); 
/*  54 */     return result;
/*     */   }
/*     */   
/*     */   public void schedule(ScheduledTick<T> tick) {
/*  59 */     if (this.ticksPerPosition.add(tick))
/*  60 */       scheduleUnchecked(tick); 
/*     */   }
/*     */   
/*     */   private void scheduleUnchecked(ScheduledTick<T> tick) {
/*  65 */     this.tickQueue.add(tick);
/*  67 */     if (this.onTickAdded != null)
/*  68 */       this.onTickAdded.accept(this, tick); 
/*     */   }
/*     */   
/*     */   public boolean hasScheduledTick(BlockPos pos, T type) {
/*  75 */     return this.ticksPerPosition.contains(ScheduledTick.probe(type, pos));
/*     */   }
/*     */   
/*     */   public void removeIf(Predicate<ScheduledTick<T>> test) {
/*  79 */     for (Iterator<ScheduledTick<T>> iterator = this.tickQueue.iterator(); iterator.hasNext(); ) {
/*  80 */       ScheduledTick<T> tick = iterator.next();
/*  81 */       if (test.test(tick)) {
/*  82 */         iterator.remove();
/*  83 */         this.ticksPerPosition.remove(tick);
/*     */       } 
/*     */     } 
/*     */   }
/*     */   
/*     */   public Stream<ScheduledTick<T>> getAll() {
/*  89 */     return this.tickQueue.stream();
/*     */   }
/*     */   
/*     */   public int count() {
/*  94 */     return this.tickQueue.size() + ((this.pendingTicks != null) ? this.pendingTicks.size() : 0);
/*     */   }
/*     */   
/*     */   public List<SavedTick<T>> pack(long currentTick) {
/*  99 */     List<SavedTick<T>> ticks = new ArrayList<>(this.tickQueue.size());
/* 100 */     if (this.pendingTicks != null)
/* 101 */       ticks.addAll(this.pendingTicks); 
/* 105 */     List<ScheduledTick<T>> sortedTicks = new ArrayList<>(this.tickQueue);
/* 106 */     sortedTicks.sort(SUB_TICK_ORDERING);
/* 108 */     for (ScheduledTick<T> tick : sortedTicks)
/* 109 */       ticks.add(tick.toSavedTick(currentTick)); 
/* 111 */     return ticks;
/*     */   }
/*     */   
/*     */   public void unpack(long currentTick) {
/* 115 */     if (this.pendingTicks != null) {
/* 116 */       int subTickBase = -this.pendingTicks.size();
/* 117 */       for (SavedTick<T> pendingTick : this.pendingTicks)
/* 119 */         scheduleUnchecked(pendingTick.unpack(currentTick, subTickBase++)); 
/*     */     } 
/* 123 */     this.pendingTicks = null;
/*     */   }
/*     */ }


/* Location:              C:\Users\karol\AppData\Roaming\ModrinthApp\meta\versions\26.2-0.19.5\26.2-0.19.5.jar!\net\minecraft\world\ticks\LevelChunkTicks.class
 * Java compiler version: 25 (69.0)
 * JD-Core Version:       1.1.3
 */