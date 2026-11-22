/*     */ package net.minecraft.world.level.storage;
/*     */ 
/*     */ import com.google.common.collect.AbstractIterator;
/*     */ import com.google.common.collect.Streams;
/*     */ import java.util.Iterator;
/*     */ import java.util.List;
/*     */ import java.util.ListIterator;
/*     */ import java.util.stream.Stream;
/*     */ import net.minecraft.nbt.CompoundTag;
/*     */ import net.minecraft.util.ProblemReporter;
/*     */ 
/*     */ class CompoundListWrapper implements ValueInput.ValueInputList {
/*     */   private final ProblemReporter problemReporter;
/*     */   
/*     */   private final ValueInputContextHelper context;
/*     */   
/*     */   private final List<CompoundTag> list;
/*     */   
/*     */   public CompoundListWrapper(ProblemReporter problemReporter, ValueInputContextHelper context, List<CompoundTag> list) {
/* 368 */     this.problemReporter = problemReporter;
/* 369 */     this.context = context;
/* 370 */     this.list = list;
/*     */   }
/*     */   
/*     */   private ValueInput wrapChild(int index, CompoundTag compoundTag) {
/* 374 */     return TagValueInput.wrapChild(this.problemReporter.forChild((ProblemReporter.PathElement)new ProblemReporter.IndexedPathElement(index)), this.context, compoundTag);
/*     */   }
/*     */   
/*     */   public boolean isEmpty() {
/* 379 */     return this.list.isEmpty();
/*     */   }
/*     */   
/*     */   public Stream<ValueInput> stream() {
/* 384 */     return Streams.mapWithIndex(this.list.stream(), (value, index) -> wrapChild((int)index, value));
/*     */   }
/*     */   
/*     */   public Iterator<ValueInput> iterator() {
/* 389 */     final ListIterator<CompoundTag> iterator = this.list.listIterator();
/* 390 */     return (Iterator<ValueInput>)new AbstractIterator<ValueInput>() {
/*     */         protected ValueInput computeNext() {
/* 393 */           if (iterator.hasNext()) {
/* 394 */             int index = iterator.nextIndex();
/* 395 */             CompoundTag value = iterator.next();
/* 396 */             return TagValueInput.CompoundListWrapper.this.wrapChild(index, value);
/*     */           } 
/* 398 */           return (ValueInput)endOfData();
/*     */         }
/*     */       };
/*     */   }
/*     */ }


/* Location:              C:\Users\karol\AppData\Roaming\ModrinthApp\meta\versions\26.2-0.19.5\26.2-0.19.5.jar!\net\minecraft\world\level\storage\TagValueInput$CompoundListWrapper.class
 * Java compiler version: 25 (69.0)
 * JD-Core Version:       1.1.3
 */