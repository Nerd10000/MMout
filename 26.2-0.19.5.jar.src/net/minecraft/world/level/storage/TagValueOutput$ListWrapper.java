/*     */ package net.minecraft.world.level.storage;
/*     */ 
/*     */ import com.mojang.serialization.DynamicOps;
/*     */ import net.minecraft.nbt.CompoundTag;
/*     */ import net.minecraft.nbt.ListTag;
/*     */ import net.minecraft.nbt.Tag;
/*     */ import net.minecraft.util.ProblemReporter;
/*     */ 
/*     */ class ListWrapper implements ValueOutput.ValueOutputList {
/*     */   private final String fieldName;
/*     */   
/*     */   private final ProblemReporter problemReporter;
/*     */   
/*     */   private final DynamicOps<Tag> ops;
/*     */   
/*     */   private final ListTag output;
/*     */   
/*     */   private ListWrapper(String fieldName, ProblemReporter problemReporter, DynamicOps<Tag> ops, ListTag output) {
/* 154 */     this.fieldName = fieldName;
/* 155 */     this.problemReporter = problemReporter;
/* 156 */     this.ops = ops;
/* 157 */     this.output = output;
/*     */   }
/*     */   
/*     */   public ValueOutput addChild() {
/* 162 */     int newChildIndex = this.output.size();
/* 163 */     CompoundTag child = new CompoundTag();
/* 164 */     this.output.add(child);
/* 165 */     return new TagValueOutput(this.problemReporter.forChild((ProblemReporter.PathElement)new ProblemReporter.IndexedFieldPathElement(this.fieldName, newChildIndex)), this.ops, child);
/*     */   }
/*     */   
/*     */   public void discardLast() {
/* 170 */     this.output.removeLast();
/*     */   }
/*     */   
/*     */   public boolean isEmpty() {
/* 175 */     return this.output.isEmpty();
/*     */   }
/*     */ }


/* Location:              C:\Users\karol\AppData\Roaming\ModrinthApp\meta\versions\26.2-0.19.5\26.2-0.19.5.jar!\net\minecraft\world\level\storage\TagValueOutput$ListWrapper.class
 * Java compiler version: 25 (69.0)
 * JD-Core Version:       1.1.3
 */