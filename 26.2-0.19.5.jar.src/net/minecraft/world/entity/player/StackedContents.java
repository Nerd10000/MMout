/*     */ package net.minecraft.world.entity.player;
/*     */ 
/*     */ import com.google.common.annotations.VisibleForTesting;
/*     */ import it.unimi.dsi.fastutil.ints.IntArrayList;
/*     */ import it.unimi.dsi.fastutil.ints.IntList;
/*     */ import it.unimi.dsi.fastutil.objects.ObjectIterable;
/*     */ import it.unimi.dsi.fastutil.objects.ObjectIterator;
/*     */ import it.unimi.dsi.fastutil.objects.Reference2IntMap;
/*     */ import it.unimi.dsi.fastutil.objects.Reference2IntMaps;
/*     */ import it.unimi.dsi.fastutil.objects.Reference2IntOpenHashMap;
/*     */ import java.util.ArrayList;
/*     */ import java.util.BitSet;
/*     */ import java.util.List;
/*     */ 
/*     */ public class StackedContents<T> {
/*  17 */   public final Reference2IntOpenHashMap<T> amounts = new Reference2IntOpenHashMap();
/*     */   
/*     */   private boolean hasAtLeast(T item, int count) {
/*  20 */     return (this.amounts.getInt(item) >= count);
/*     */   }
/*     */   
/*     */   private void take(T item, int amount) {
/*  24 */     int previous = this.amounts.addTo(item, -amount);
/*  25 */     if (previous < amount)
/*  26 */       throw new IllegalStateException("Took " + amount + " items, but only had " + previous); 
/*     */   }
/*     */   
/*     */   private void put(T item, int count) {
/*  31 */     this.amounts.addTo(item, count);
/*     */   }
/*     */   
/*     */   public boolean tryPick(List<? extends IngredientInfo<T>> ingredients, int amount, Output<T> output) {
/*  35 */     return (new RecipePicker(ingredients)).tryPick(amount, output);
/*     */   }
/*     */   
/*     */   public int tryPickAll(List<? extends IngredientInfo<T>> ingredients, int maxSize, Output<T> output) {
/*  39 */     return (new RecipePicker(ingredients)).tryPickAll(maxSize, output);
/*     */   }
/*     */   
/*     */   public void clear() {
/*  43 */     this.amounts.clear();
/*     */   }
/*     */   
/*     */   public void account(T item, int count) {
/*  47 */     put(item, count);
/*     */   }
/*     */   
/*     */   private List<T> getUniqueAvailableIngredientItems(Iterable<? extends IngredientInfo<T>> ingredients) {
/*  52 */     List<T> result = new ArrayList<>();
/*  53 */     for (ObjectIterator<Reference2IntMap.Entry<T>> objectIterator = Reference2IntMaps.fastIterable((Reference2IntMap)this.amounts).iterator(); objectIterator.hasNext(); ) {
/*  53 */       Reference2IntMap.Entry<T> availableItem = objectIterator.next();
/*  54 */       if (availableItem.getIntValue() > 0 && anyIngredientMatches(ingredients, (T)availableItem.getKey()))
/*  55 */         result.add((T)availableItem.getKey()); 
/*     */     } 
/*  58 */     return result;
/*     */   }
/*     */   
/*     */   private static <T> boolean anyIngredientMatches(Iterable<? extends IngredientInfo<T>> ingredients, T item) {
/*  62 */     for (IngredientInfo<T> ingredient : ingredients) {
/*  63 */       if (ingredient.acceptsItem(item))
/*  64 */         return true; 
/*     */     } 
/*  67 */     return false;
/*     */   }
/*     */   
/*     */   @VisibleForTesting
/*     */   public int getResultUpperBound(List<? extends IngredientInfo<T>> ingredients) {
/*  73 */     int min = Integer.MAX_VALUE;
/*  75 */     ObjectIterable<Reference2IntMap.Entry<T>> availableItems = Reference2IntMaps.fastIterable((Reference2IntMap)this.amounts);
/*  76 */     label20: for (IngredientInfo<T> ingredient : ingredients) {
/*  77 */       int max = 0;
/*  79 */       for (ObjectIterator<Reference2IntMap.Entry<T>> objectIterator = availableItems.iterator(); objectIterator.hasNext(); ) {
/*  79 */         Reference2IntMap.Entry<T> entry = objectIterator.next();
/*  80 */         int itemCount = entry.getIntValue();
/*  81 */         if (itemCount <= max)
/*     */           continue; 
/*  86 */         if (ingredient.acceptsItem((T)entry.getKey()))
/*  87 */           max = itemCount; 
/*  90 */         if (max >= min)
/*     */           continue label20; 
/*     */       } 
/*  96 */       min = max;
/*  97 */       if (min == 0)
/*     */         break; 
/*     */     } 
/* 101 */     return min;
/*     */   }
/*     */   
/*     */   private class RecipePicker {
/*     */     private final List<? extends StackedContents.IngredientInfo<T>> ingredients;
/*     */     
/*     */     private final int ingredientCount;
/*     */     
/*     */     private final List<T> items;
/*     */     
/*     */     private final int itemCount;
/*     */     
/*     */     private final BitSet data;
/*     */     
/* 123 */     private final IntList path = (IntList)new IntArrayList();
/*     */     
/*     */     public RecipePicker(List<? extends StackedContents.IngredientInfo<T>> ingredients) {
/* 126 */       this.ingredients = ingredients;
/* 128 */       this.ingredientCount = ingredients.size();
/* 129 */       this.items = StackedContents.this.getUniqueAvailableIngredientItems(ingredients);
/* 130 */       this.itemCount = this.items.size();
/* 132 */       this.data = new BitSet(visitedIngredientCount() + visitedItemCount() + satisfiedCount() + connectionCount() + residualCount());
/* 133 */       setInitialConnections();
/*     */     }
/*     */     
/*     */     private void setInitialConnections() {
/* 137 */       for (int ingredient = 0; ingredient < this.ingredientCount; ingredient++) {
/* 138 */         StackedContents.IngredientInfo<T> ingredientInfo = this.ingredients.get(ingredient);
/* 139 */         for (int item = 0; item < this.itemCount; item++) {
/* 140 */           if (ingredientInfo.acceptsItem(this.items.get(item)))
/* 141 */             setConnection(item, ingredient); 
/*     */         } 
/*     */       } 
/*     */     }
/*     */     
/*     */     public boolean tryPick(int capacity, StackedContents.Output<T> output) {
/* 153 */       if (capacity <= 0)
/* 154 */         return true; 
/* 157 */       int satisfiedIngredientCount = 0;
/*     */       while (true) {
/* 159 */         IntList path = tryAssigningNewItem(capacity);
/* 160 */         if (path == null)
/*     */           break; 
/* 167 */         int assignedItem = path.getInt(0);
/* 168 */         StackedContents.this.take(this.items.get(assignedItem), capacity);
/* 171 */         int satisfiedIngredient = path.size() - 1;
/* 172 */         setSatisfied(path.getInt(satisfiedIngredient));
/* 173 */         satisfiedIngredientCount++;
/* 177 */         for (int i = 0; i < path.size() - 1; i++) {
/* 178 */           if (isPathIndexItem(i)) {
/* 179 */             int item = path.getInt(i);
/* 180 */             int j = path.getInt(i + 1);
/* 181 */             assign(item, j);
/*     */           } else {
/* 183 */             int item = path.getInt(i + 1);
/* 184 */             int j = path.getInt(i);
/* 185 */             unassign(item, j);
/*     */           } 
/*     */         } 
/*     */       } 
/* 190 */       boolean isValidAssignment = (satisfiedIngredientCount == this.ingredientCount);
/* 192 */       boolean hasOutput = (isValidAssignment && output != null);
/* 195 */       clearAllVisited();
/* 196 */       clearSatisfied();
/* 199 */       for (int ingredient = 0; ingredient < this.ingredientCount; ingredient++) {
/* 200 */         for (int item = 0; item < this.itemCount; item++) {
/* 201 */           if (isAssigned(item, ingredient)) {
/* 202 */             unassign(item, ingredient);
/* 203 */             StackedContents.this.put(this.items.get(item), capacity);
/* 205 */             if (hasOutput)
/* 206 */               output.accept(this.items.get(item)); 
/*     */             break;
/*     */           } 
/*     */         } 
/*     */       } 
/* 215 */       assert this.data.get(residualOffset(), residualOffset() + residualCount()).isEmpty();
/* 217 */       return isValidAssignment;
/*     */     }
/*     */     
/*     */     private static boolean isPathIndexItem(int index) {
/* 221 */       return ((index & 0x1) == 0);
/*     */     }
/*     */     
/*     */     private IntList tryAssigningNewItem(int capacity) {
/* 225 */       clearAllVisited();
/* 227 */       for (int item = 0; item < this.itemCount; item++) {
/* 228 */         if (StackedContents.this.hasAtLeast(this.items.get(item), capacity)) {
/* 229 */           IntList path = findNewItemAssignmentPath(item);
/* 230 */           if (path != null)
/* 231 */             return path; 
/*     */         } 
/*     */       } 
/* 236 */       return null;
/*     */     }
/*     */     
/*     */     private IntList findNewItemAssignmentPath(int startingItem) {
/* 240 */       this.path.clear();
/* 241 */       visitItem(startingItem);
/* 242 */       this.path.add(startingItem);
/* 245 */       while (!this.path.isEmpty()) {
/* 246 */         int pathLength = this.path.size();
/* 247 */         if (isPathIndexItem(pathLength - 1)) {
/* 248 */           int itemToAssign = this.path.getInt(pathLength - 1);
/* 251 */           for (int ingredient = 0; ingredient < this.ingredientCount; ingredient++) {
/* 252 */             if (!hasVisitedIngredient(ingredient) && hasConnection(itemToAssign, ingredient) && !isAssigned(itemToAssign, ingredient)) {
/* 253 */               visitIngredient(ingredient);
/* 254 */               this.path.add(ingredient);
/*     */               break;
/*     */             } 
/*     */           } 
/*     */         } else {
/* 259 */           int lastAssignedIngredient = this.path.getInt(pathLength - 1);
/* 261 */           if (!isSatisfied(lastAssignedIngredient))
/* 262 */             return this.path; 
/* 266 */           for (int item = 0; item < this.itemCount; item++) {
/* 267 */             if (!hasVisitedItem(item) && isAssigned(item, lastAssignedIngredient)) {
/* 269 */               assert hasConnection(item, lastAssignedIngredient);
/* 270 */               visitItem(item);
/* 271 */               this.path.add(item);
/*     */               break;
/*     */             } 
/*     */           } 
/*     */         } 
/* 278 */         int newLength = this.path.size();
/* 279 */         if (newLength == pathLength)
/* 280 */           this.path.removeInt(newLength - 1); 
/*     */       } 
/* 284 */       return null;
/*     */     }
/*     */     
/*     */     private int visitedIngredientOffset() {
/* 288 */       return 0;
/*     */     }
/*     */     
/*     */     private int visitedIngredientCount() {
/* 292 */       return this.ingredientCount;
/*     */     }
/*     */     
/*     */     private int visitedItemOffset() {
/* 296 */       return visitedIngredientOffset() + visitedIngredientCount();
/*     */     }
/*     */     
/*     */     private int visitedItemCount() {
/* 300 */       return this.itemCount;
/*     */     }
/*     */     
/*     */     private int satisfiedOffset() {
/* 304 */       return visitedItemOffset() + visitedItemCount();
/*     */     }
/*     */     
/*     */     private int satisfiedCount() {
/* 308 */       return this.ingredientCount;
/*     */     }
/*     */     
/*     */     private int connectionOffset() {
/* 312 */       return satisfiedOffset() + satisfiedCount();
/*     */     }
/*     */     
/*     */     private int connectionCount() {
/* 316 */       return this.ingredientCount * this.itemCount;
/*     */     }
/*     */     
/*     */     private int residualOffset() {
/* 320 */       return connectionOffset() + connectionCount();
/*     */     }
/*     */     
/*     */     private int residualCount() {
/* 324 */       return this.ingredientCount * this.itemCount;
/*     */     }
/*     */     
/*     */     private boolean isSatisfied(int ingredient) {
/* 328 */       return this.data.get(getSatisfiedIndex(ingredient));
/*     */     }
/*     */     
/*     */     private void setSatisfied(int ingredient) {
/* 332 */       this.data.set(getSatisfiedIndex(ingredient));
/*     */     }
/*     */     
/*     */     private int getSatisfiedIndex(int ingredient) {
/* 336 */       assert ingredient >= 0 && ingredient < this.ingredientCount;
/* 337 */       return satisfiedOffset() + ingredient;
/*     */     }
/*     */     
/*     */     private void clearSatisfied() {
/* 341 */       clearRange(satisfiedOffset(), satisfiedCount());
/*     */     }
/*     */     
/*     */     private void setConnection(int item, int ingredient) {
/* 345 */       this.data.set(getConnectionIndex(item, ingredient));
/*     */     }
/*     */     
/*     */     private boolean hasConnection(int item, int ingredient) {
/* 349 */       return this.data.get(getConnectionIndex(item, ingredient));
/*     */     }
/*     */     
/*     */     private int getConnectionIndex(int item, int ingredient) {
/* 353 */       assert item >= 0 && item < this.itemCount;
/* 354 */       assert ingredient >= 0 && ingredient < this.ingredientCount;
/* 355 */       return connectionOffset() + item * this.ingredientCount + ingredient;
/*     */     }
/*     */     
/*     */     private boolean isAssigned(int item, int ingredient) {
/* 359 */       return this.data.get(getResidualIndex(item, ingredient));
/*     */     }
/*     */     
/*     */     private void assign(int item, int ingredient) {
/* 363 */       int residualIndex = getResidualIndex(item, ingredient);
/* 364 */       assert !this.data.get(residualIndex);
/* 365 */       this.data.set(residualIndex);
/*     */     }
/*     */     
/*     */     private void unassign(int item, int ingredient) {
/* 369 */       int residualIndex = getResidualIndex(item, ingredient);
/* 370 */       assert this.data.get(residualIndex);
/* 371 */       this.data.clear(residualIndex);
/*     */     }
/*     */     
/*     */     private int getResidualIndex(int item, int ingredient) {
/* 375 */       assert item >= 0 && item < this.itemCount;
/* 376 */       assert ingredient >= 0 && ingredient < this.ingredientCount;
/* 377 */       return residualOffset() + item * this.ingredientCount + ingredient;
/*     */     }
/*     */     
/*     */     private void visitIngredient(int item) {
/* 381 */       this.data.set(getVisitedIngredientIndex(item));
/*     */     }
/*     */     
/*     */     private boolean hasVisitedIngredient(int ingredient) {
/* 385 */       return this.data.get(getVisitedIngredientIndex(ingredient));
/*     */     }
/*     */     
/*     */     private int getVisitedIngredientIndex(int ingredient) {
/* 389 */       assert ingredient >= 0 && ingredient < this.ingredientCount;
/* 390 */       return visitedIngredientOffset() + ingredient;
/*     */     }
/*     */     
/*     */     private void visitItem(int item) {
/* 394 */       this.data.set(getVisitiedItemIndex(item));
/*     */     }
/*     */     
/*     */     private boolean hasVisitedItem(int item) {
/* 398 */       return this.data.get(getVisitiedItemIndex(item));
/*     */     }
/*     */     
/*     */     private int getVisitiedItemIndex(int item) {
/* 402 */       assert item >= 0 && item < this.itemCount;
/* 403 */       return visitedItemOffset() + item;
/*     */     }
/*     */     
/*     */     private void clearAllVisited() {
/* 407 */       clearRange(visitedIngredientOffset(), visitedIngredientCount());
/* 408 */       clearRange(visitedItemOffset(), visitedItemCount());
/*     */     }
/*     */     
/*     */     private void clearRange(int offset, int count) {
/* 412 */       this.data.clear(offset, offset + count);
/*     */     }
/*     */     
/*     */     public int tryPickAll(int maxSize, StackedContents.Output<T> output) {
/* 416 */       int mid, min = 0;
/* 417 */       int max = Math.min(maxSize, StackedContents.this.getResultUpperBound(this.ingredients)) + 1;
/*     */       while (true) {
/* 422 */         mid = (min + max) / 2;
/* 424 */         if (tryPick(mid, null)) {
/* 425 */           if (max - min <= 1)
/*     */             break; 
/* 428 */           min = mid;
/*     */           continue;
/*     */         } 
/* 430 */         max = mid;
/*     */       } 
/* 434 */       if (mid > 0)
/* 435 */         tryPick(mid, output); 
/* 438 */       return mid;
/*     */     }
/*     */   }
/*     */   
/*     */   @FunctionalInterface
/*     */   public static interface Output<T> {
/*     */     void accept(T param1T);
/*     */   }
/*     */   
/*     */   @FunctionalInterface
/*     */   public static interface IngredientInfo<T> {
/*     */     boolean acceptsItem(T param1T);
/*     */   }
/*     */ }


/* Location:              C:\Users\karol\AppData\Roaming\ModrinthApp\meta\versions\26.2-0.19.5\26.2-0.19.5.jar!\net\minecraft\world\entity\player\StackedContents.class
 * Java compiler version: 25 (69.0)
 * JD-Core Version:       1.1.3
 */