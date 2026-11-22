/*     */ package net.minecraft.world.inventory;
/*     */ 
/*     */ import net.minecraft.core.NonNullList;
/*     */ import net.minecraft.server.level.ServerLevel;
/*     */ import net.minecraft.world.Container;
/*     */ import net.minecraft.world.entity.player.Player;
/*     */ import net.minecraft.world.item.ItemStack;
/*     */ import net.minecraft.world.item.crafting.CraftingInput;
/*     */ import net.minecraft.world.item.crafting.CraftingRecipe;
/*     */ import net.minecraft.world.item.crafting.RecipeHolder;
/*     */ import net.minecraft.world.item.crafting.RecipeInput;
/*     */ import net.minecraft.world.item.crafting.RecipeType;
/*     */ import net.minecraft.world.level.Level;
/*     */ 
/*     */ public class ResultSlot extends Slot {
/*     */   private final CraftingContainer craftSlots;
/*     */   
/*     */   private final Player player;
/*     */   
/*     */   private int removeCount;
/*     */   
/*     */   public ResultSlot(Player player, CraftingContainer craftSlots, Container container, int id, int x, int y) {
/*  19 */     super(container, id, x, y);
/*  20 */     this.player = player;
/*  21 */     this.craftSlots = craftSlots;
/*     */   }
/*     */   
/*     */   public boolean mayPlace(ItemStack itemStack) {
/*  26 */     return false;
/*     */   }
/*     */   
/*     */   public ItemStack remove(int amount) {
/*  31 */     if (hasItem())
/*  32 */       this.removeCount += Math.min(amount, getItem().getCount()); 
/*  34 */     return super.remove(amount);
/*     */   }
/*     */   
/*     */   protected void onQuickCraft(ItemStack picked, int count) {
/*  39 */     this.removeCount += count;
/*  40 */     checkTakeAchievements(picked);
/*     */   }
/*     */   
/*     */   protected void onSwapCraft(int count) {
/*  45 */     this.removeCount += count;
/*     */   }
/*     */   
/*     */   public ItemStack safeClone(Player player) {
/*  50 */     ItemStack result = super.safeClone(player);
/*  51 */     result.getItem().onCraftedBy(result, player);
/*  52 */     return result;
/*     */   }
/*     */   
/*     */   protected void checkTakeAchievements(ItemStack carried) {
/*  57 */     if (this.removeCount > 0)
/*  58 */       carried.onCraftedBy(this.player, this.removeCount); 
/*  60 */     Container container = this.container;
/*  60 */     if (container instanceof RecipeCraftingHolder) {
/*  60 */       RecipeCraftingHolder recipeCraftingHolder = (RecipeCraftingHolder)container;
/*  61 */       recipeCraftingHolder.awardUsedRecipes(this.player, this.craftSlots.getItems());
/*     */     } 
/*  63 */     this.removeCount = 0;
/*     */   }
/*     */   
/*     */   private static NonNullList<ItemStack> copyAllInputItems(CraftingInput input) {
/*  67 */     NonNullList<ItemStack> result = NonNullList.withSize(input.size(), ItemStack.EMPTY);
/*  68 */     for (int slot = 0; slot < result.size(); slot++)
/*  69 */       result.set(slot, input.getItem(slot)); 
/*  71 */     return result;
/*     */   }
/*     */   
/*     */   private NonNullList<ItemStack> getRemainingItems(CraftingInput input, Level level) {
/*  75 */     if (level instanceof ServerLevel) {
/*  75 */       ServerLevel serverLevel = (ServerLevel)level;
/*  77 */       return serverLevel.recipeAccess().getRecipeFor(RecipeType.CRAFTING, (RecipeInput)input, (Level)serverLevel)
/*  78 */         .map(recipe -> ((CraftingRecipe)recipe.value()).getRemainingItems(input))
/*  79 */         .orElseGet(() -> copyAllInputItems(input));
/*     */     } 
/*  83 */     return CraftingRecipe.defaultCraftingReminder(input);
/*     */   }
/*     */   
/*     */   public void onTake(Player player, ItemStack carried) {
/*  89 */     checkTakeAchievements(carried);
/*  91 */     CraftingInput.Positioned positionedRecipe = this.craftSlots.asPositionedCraftInput();
/*  92 */     CraftingInput input = positionedRecipe.input();
/*  93 */     int recipeLeft = positionedRecipe.left();
/*  94 */     int recipeTop = positionedRecipe.top();
/*  96 */     NonNullList<ItemStack> remaining = getRemainingItems(input, player.level());
/*  98 */     for (int y = 0; y < input.height(); y++) {
/*  99 */       for (int x = 0; x < input.width(); x++) {
/* 100 */         int slot = x + recipeLeft + (y + recipeTop) * this.craftSlots.getWidth();
/* 101 */         ItemStack itemStack = this.craftSlots.getItem(slot);
/* 103 */         ItemStack replacement = (ItemStack)remaining.get(x + y * input.width());
/* 105 */         if (!itemStack.isEmpty()) {
/* 106 */           this.craftSlots.removeItem(slot, 1);
/* 107 */           itemStack = this.craftSlots.getItem(slot);
/*     */         } 
/* 110 */         if (!replacement.isEmpty())
/* 111 */           if (itemStack.isEmpty()) {
/* 113 */             this.craftSlots.setItem(slot, replacement);
/* 114 */           } else if (ItemStack.isSameItemSameComponents(itemStack, replacement)) {
/* 115 */             replacement.grow(itemStack.getCount());
/* 116 */             this.craftSlots.setItem(slot, replacement);
/* 117 */           } else if (!this.player.getInventory().add(replacement)) {
/* 119 */             this.player.drop(replacement, false);
/*     */           }  
/*     */       } 
/*     */     } 
/*     */   }
/*     */   
/*     */   public boolean isFake() {
/* 128 */     return true;
/*     */   }
/*     */ }


/* Location:              C:\Users\karol\AppData\Roaming\ModrinthApp\meta\versions\26.2-0.19.5\26.2-0.19.5.jar!\net\minecraft\world\inventory\ResultSlot.class
 * Java compiler version: 25 (69.0)
 * JD-Core Version:       1.1.3
 */