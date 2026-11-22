/*    */ package net.minecraft.world.item.crafting;
/*    */ 
/*    */ import java.lang.ref.WeakReference;
/*    */ import java.util.Arrays;
/*    */ import java.util.Optional;
/*    */ import net.minecraft.core.NonNullList;
/*    */ import net.minecraft.server.level.ServerLevel;
/*    */ import net.minecraft.world.item.ItemStack;
/*    */ import net.minecraft.world.level.Level;
/*    */ 
/*    */ public class RecipeCache {
/*    */   private final Entry[] entries;
/*    */   
/* 17 */   private WeakReference<RecipeManager> cachedRecipeManager = new WeakReference<>(null);
/*    */   
/*    */   public RecipeCache(int capacity) {
/* 20 */     this.entries = new Entry[capacity];
/*    */   }
/*    */   
/*    */   public Optional<RecipeHolder<CraftingRecipe>> get(ServerLevel level, CraftingInput input) {
/* 24 */     if (input.isEmpty())
/* 25 */       return Optional.empty(); 
/* 28 */     validateRecipeManager(level);
/* 30 */     for (int i = 0; i < this.entries.length; i++) {
/* 31 */       Entry entry = this.entries[i];
/* 32 */       if (entry != null && entry.matches(input)) {
/* 33 */         moveEntryToFront(i);
/* 34 */         return Optional.ofNullable(entry.value());
/*    */       } 
/*    */     } 
/* 37 */     return compute(input, level);
/*    */   }
/*    */   
/*    */   private void validateRecipeManager(ServerLevel level) {
/* 41 */     RecipeManager recipeManager = level.recipeAccess();
/* 42 */     if (recipeManager != this.cachedRecipeManager.get()) {
/* 44 */       this.cachedRecipeManager = new WeakReference<>(recipeManager);
/* 45 */       Arrays.fill((Object[])this.entries, (Object)null);
/*    */     } 
/*    */   }
/*    */   
/*    */   private Optional<RecipeHolder<CraftingRecipe>> compute(CraftingInput input, ServerLevel level) {
/* 50 */     Optional<RecipeHolder<CraftingRecipe>> recipe = level.recipeAccess().getRecipeFor(RecipeType.CRAFTING, input, (Level)level);
/* 51 */     insert(input, recipe.orElse(null));
/* 52 */     return recipe;
/*    */   }
/*    */   
/*    */   private void moveEntryToFront(int index) {
/* 56 */     if (index > 0) {
/* 57 */       Entry entry = this.entries[index];
/* 58 */       System.arraycopy(this.entries, 0, this.entries, 1, index);
/* 59 */       this.entries[0] = entry;
/*    */     } 
/*    */   }
/*    */   
/*    */   private void insert(CraftingInput input, RecipeHolder<CraftingRecipe> recipe) {
/* 64 */     NonNullList<ItemStack> key = NonNullList.withSize(input.size(), ItemStack.EMPTY);
/* 65 */     for (int i = 0; i < input.size(); i++)
/* 66 */       key.set(i, input.getItem(i).copyWithCount(1)); 
/* 68 */     System.arraycopy(this.entries, 0, this.entries, 1, this.entries.length - 1);
/* 69 */     this.entries[0] = new Entry(key, input.width(), input.height(), recipe);
/*    */   }
/*    */   
/*    */   private static final class Entry extends Record {
/*    */     private final NonNullList<ItemStack> key;
/*    */     
/*    */     private final int width;
/*    */     
/*    */     private final int height;
/*    */     
/*    */     private final RecipeHolder<CraftingRecipe> value;
/*    */     
/*    */     private Entry(NonNullList<ItemStack> key, int width, int height, RecipeHolder<CraftingRecipe> value) {
/* 72 */       this.key = key;
/* 72 */       this.width = width;
/* 72 */       this.height = height;
/* 72 */       this.value = value;
/*    */     }
/*    */     
/*    */     public final String toString() {
/*    */       // Byte code:
/*    */       //   0: aload_0
/*    */       //   1: <illegal opcode> toString : (Lnet/minecraft/world/item/crafting/RecipeCache$Entry;)Ljava/lang/String;
/*    */       //   6: areturn
/*    */       // Line number table:
/*    */       //   Java source line number -> byte code offset
/*    */       //   #72	-> 0
/*    */       // Local variable table:
/*    */       //   start	length	slot	name	descriptor
/*    */       //   0	7	0	this	Lnet/minecraft/world/item/crafting/RecipeCache$Entry;
/*    */     }
/*    */     
/*    */     public final int hashCode() {
/*    */       // Byte code:
/*    */       //   0: aload_0
/*    */       //   1: <illegal opcode> hashCode : (Lnet/minecraft/world/item/crafting/RecipeCache$Entry;)I
/*    */       //   6: ireturn
/*    */       // Line number table:
/*    */       //   Java source line number -> byte code offset
/*    */       //   #72	-> 0
/*    */       // Local variable table:
/*    */       //   start	length	slot	name	descriptor
/*    */       //   0	7	0	this	Lnet/minecraft/world/item/crafting/RecipeCache$Entry;
/*    */     }
/*    */     
/*    */     public final boolean equals(Object o) {
/*    */       // Byte code:
/*    */       //   0: aload_0
/*    */       //   1: aload_1
/*    */       //   2: <illegal opcode> equals : (Lnet/minecraft/world/item/crafting/RecipeCache$Entry;Ljava/lang/Object;)Z
/*    */       //   7: ireturn
/*    */       // Line number table:
/*    */       //   Java source line number -> byte code offset
/*    */       //   #72	-> 0
/*    */       // Local variable table:
/*    */       //   start	length	slot	name	descriptor
/*    */       //   0	8	0	this	Lnet/minecraft/world/item/crafting/RecipeCache$Entry;
/*    */       //   0	8	1	o	Ljava/lang/Object;
/*    */     }
/*    */     
/*    */     public NonNullList<ItemStack> key() {
/* 72 */       return this.key;
/*    */     }
/*    */     
/*    */     public int width() {
/* 72 */       return this.width;
/*    */     }
/*    */     
/*    */     public int height() {
/* 72 */       return this.height;
/*    */     }
/*    */     
/*    */     public RecipeHolder<CraftingRecipe> value() {
/* 72 */       return this.value;
/*    */     }
/*    */     
/*    */     public boolean matches(CraftingInput input) {
/* 74 */       if (this.width != input.width() || this.height != input.height())
/* 75 */         return false; 
/* 77 */       for (int i = 0; i < this.key.size(); i++) {
/* 78 */         if (!ItemStack.isSameItemSameComponents((ItemStack)this.key.get(i), input.getItem(i)))
/* 79 */           return false; 
/*    */       } 
/* 82 */       return true;
/*    */     }
/*    */   }
/*    */ }


/* Location:              C:\Users\karol\AppData\Roaming\ModrinthApp\meta\versions\26.2-0.19.5\26.2-0.19.5.jar!\net\minecraft\world\item\crafting\RecipeCache.class
 * Java compiler version: 25 (69.0)
 * JD-Core Version:       1.1.3
 */