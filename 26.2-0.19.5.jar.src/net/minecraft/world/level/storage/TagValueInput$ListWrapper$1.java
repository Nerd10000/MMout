/*     */ package net.minecraft.world.level.storage;
/*     */ 
/*     */ import com.google.common.collect.AbstractIterator;
/*     */ import java.util.Iterator;
/*     */ import net.minecraft.nbt.CompoundTag;
/*     */ import net.minecraft.nbt.Tag;
/*     */ 
/*     */ class null extends AbstractIterator<ValueInput> {
/*     */   private int index;
/*     */   
/*     */   protected ValueInput computeNext() {
/* 283 */     while (iterator.hasNext()) {
/* 284 */       Tag value = iterator.next();
/* 285 */       int currentIndex = this.index++;
/* 286 */       if (value instanceof CompoundTag) {
/* 286 */         CompoundTag compoundTag = (CompoundTag)value;
/* 287 */         return TagValueInput.wrapChild(TagValueInput.ListWrapper.this.reporterForChild(currentIndex), TagValueInput.ListWrapper.this.context, compoundTag);
/*     */       } 
/* 289 */       TagValueInput.ListWrapper.this.reportIndexUnwrapProblem(currentIndex, value);
/*     */     } 
/* 292 */     return (ValueInput)endOfData();
/*     */   }
/*     */ }


/* Location:              C:\Users\karol\AppData\Roaming\ModrinthApp\meta\versions\26.2-0.19.5\26.2-0.19.5.jar!\net\minecraft\world\level\storage\TagValueInput$ListWrapper$1.class
 * Java compiler version: 25 (69.0)
 * JD-Core Version:       1.1.3
 */