/*     */ package net.minecraft.world.level.storage;
/*     */ 
/*     */ import com.google.common.collect.AbstractIterator;
/*     */ import com.google.common.collect.Streams;
/*     */ import java.util.Iterator;
/*     */ import java.util.Objects;
/*     */ import java.util.stream.Stream;
/*     */ import net.minecraft.nbt.CompoundTag;
/*     */ import net.minecraft.nbt.ListTag;
/*     */ import net.minecraft.nbt.Tag;
/*     */ import net.minecraft.util.ProblemReporter;
/*     */ 
/*     */ class ListWrapper implements ValueInput.ValueInputList {
/*     */   private final ProblemReporter problemReporter;
/*     */   
/*     */   private final String name;
/*     */   
/*     */   private final ValueInputContextHelper context;
/*     */   
/*     */   private final ListTag list;
/*     */   
/*     */   private ListWrapper(ProblemReporter problemReporter, String name, ValueInputContextHelper context, ListTag list) {
/* 243 */     this.problemReporter = problemReporter;
/* 244 */     this.name = name;
/* 245 */     this.context = context;
/* 246 */     this.list = list;
/*     */   }
/*     */   
/*     */   public boolean isEmpty() {
/* 251 */     return this.list.isEmpty();
/*     */   }
/*     */   
/*     */   private ProblemReporter reporterForChild(int index) {
/* 255 */     return this.problemReporter.forChild((ProblemReporter.PathElement)new ProblemReporter.IndexedFieldPathElement(this.name, index));
/*     */   }
/*     */   
/*     */   private void reportIndexUnwrapProblem(int index, Tag value) {
/* 259 */     this.problemReporter.report(new TagValueInput.UnexpectedListElementTypeProblem(this.name, index, CompoundTag.TYPE, value.getType()));
/*     */   }
/*     */   
/*     */   public Stream<ValueInput> stream() {
/* 264 */     return Streams.mapWithIndex(this.list.stream(), (value, index) -> {
/*     */           if (value instanceof CompoundTag) {
/*     */             CompoundTag compoundTag = (CompoundTag)value;
/*     */             return TagValueInput.wrapChild(reporterForChild((int)index), this.context, compoundTag);
/*     */           } 
/*     */           reportIndexUnwrapProblem((int)index, value);
/*     */           return null;
/* 272 */         }).filter(Objects::nonNull);
/*     */   }
/*     */   
/*     */   public Iterator<ValueInput> iterator() {
/* 277 */     final Iterator<Tag> iterator = this.list.iterator();
/* 278 */     return (Iterator<ValueInput>)new AbstractIterator<ValueInput>() {
/*     */         private int index;
/*     */         
/*     */         protected ValueInput computeNext() {
/* 283 */           while (iterator.hasNext()) {
/* 284 */             Tag value = iterator.next();
/* 285 */             int currentIndex = this.index++;
/* 286 */             if (value instanceof CompoundTag) {
/* 286 */               CompoundTag compoundTag = (CompoundTag)value;
/* 287 */               return TagValueInput.wrapChild(TagValueInput.ListWrapper.this.reporterForChild(currentIndex), TagValueInput.ListWrapper.this.context, compoundTag);
/*     */             } 
/* 289 */             TagValueInput.ListWrapper.this.reportIndexUnwrapProblem(currentIndex, value);
/*     */           } 
/* 292 */           return (ValueInput)endOfData();
/*     */         }
/*     */       };
/*     */   }
/*     */ }


/* Location:              C:\Users\karol\AppData\Roaming\ModrinthApp\meta\versions\26.2-0.19.5\26.2-0.19.5.jar!\net\minecraft\world\level\storage\TagValueInput$ListWrapper.class
 * Java compiler version: 25 (69.0)
 * JD-Core Version:       1.1.3
 */