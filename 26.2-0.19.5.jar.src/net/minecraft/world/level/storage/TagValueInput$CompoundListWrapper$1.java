/*     */ package net.minecraft.world.level.storage;
/*     */ 
/*     */ import com.google.common.collect.AbstractIterator;
/*     */ import java.util.ListIterator;
/*     */ import net.minecraft.nbt.CompoundTag;
/*     */ 
/*     */ class null extends AbstractIterator<ValueInput> {
/*     */   protected ValueInput computeNext() {
/* 393 */     if (iterator.hasNext()) {
/* 394 */       int index = iterator.nextIndex();
/* 395 */       CompoundTag value = iterator.next();
/* 396 */       return TagValueInput.CompoundListWrapper.this.wrapChild(index, value);
/*     */     } 
/* 398 */     return (ValueInput)endOfData();
/*     */   }
/*     */ }


/* Location:              C:\Users\karol\AppData\Roaming\ModrinthApp\meta\versions\26.2-0.19.5\26.2-0.19.5.jar!\net\minecraft\world\level\storage\TagValueInput$CompoundListWrapper$1.class
 * Java compiler version: 25 (69.0)
 * JD-Core Version:       1.1.3
 */