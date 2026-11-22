/*     */ package net.minecraft.network.syncher;
/*     */ 
/*     */ public class DataItem<T> {
/*     */   private final EntityDataAccessor<T> accessor;
/*     */   
/*     */   private T value;
/*     */   
/*     */   private final T initialValue;
/*     */   
/*     */   private boolean dirty;
/*     */   
/*     */   public DataItem(EntityDataAccessor<T> accessor, T initialValue) {
/* 167 */     this.accessor = accessor;
/* 168 */     this.initialValue = initialValue;
/* 169 */     this.value = initialValue;
/*     */   }
/*     */   
/*     */   public EntityDataAccessor<T> getAccessor() {
/* 173 */     return this.accessor;
/*     */   }
/*     */   
/*     */   public void setValue(T value) {
/* 177 */     this.value = value;
/*     */   }
/*     */   
/*     */   public T getValue() {
/* 181 */     return this.value;
/*     */   }
/*     */   
/*     */   public boolean isDirty() {
/* 185 */     return this.dirty;
/*     */   }
/*     */   
/*     */   public void setDirty(boolean dirty) {
/* 189 */     this.dirty = dirty;
/*     */   }
/*     */   
/*     */   public boolean isSetToDefault() {
/* 193 */     return this.initialValue.equals(this.value);
/*     */   }
/*     */   
/*     */   public SynchedEntityData.DataValue<T> value() {
/* 197 */     return SynchedEntityData.DataValue.create(this.accessor, this.value);
/*     */   }
/*     */ }


/* Location:              C:\Users\karol\AppData\Roaming\ModrinthApp\meta\versions\26.2-0.19.5\26.2-0.19.5.jar!\net\minecraft\network\syncher\SynchedEntityData$DataItem.class
 * Java compiler version: 25 (69.0)
 * JD-Core Version:       1.1.3
 */