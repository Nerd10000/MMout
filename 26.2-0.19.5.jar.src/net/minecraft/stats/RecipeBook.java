/*    */ package net.minecraft.stats;
/*    */ 
/*    */ import net.minecraft.world.inventory.RecipeBookType;
/*    */ 
/*    */ public class RecipeBook {
/*  6 */   protected final RecipeBookSettings bookSettings = new RecipeBookSettings();
/*    */   
/*    */   public boolean isOpen(RecipeBookType recipeBookType) {
/*  9 */     return this.bookSettings.isOpen(recipeBookType);
/*    */   }
/*    */   
/*    */   public void setOpen(RecipeBookType recipeBookType, boolean open) {
/* 13 */     this.bookSettings.setOpen(recipeBookType, open);
/*    */   }
/*    */   
/*    */   public boolean isFiltering(RecipeBookType type) {
/* 17 */     return this.bookSettings.isFiltering(type);
/*    */   }
/*    */   
/*    */   public void setFiltering(RecipeBookType type, boolean filtering) {
/* 21 */     this.bookSettings.setFiltering(type, filtering);
/*    */   }
/*    */   
/*    */   public void setBookSettings(RecipeBookSettings settings) {
/* 25 */     this.bookSettings.replaceFrom(settings);
/*    */   }
/*    */   
/*    */   public RecipeBookSettings getBookSettings() {
/* 29 */     return this.bookSettings;
/*    */   }
/*    */   
/*    */   public void setBookSetting(RecipeBookType bookType, boolean open, boolean filtering) {
/* 33 */     this.bookSettings.setOpen(bookType, open);
/* 34 */     this.bookSettings.setFiltering(bookType, filtering);
/*    */   }
/*    */ }


/* Location:              C:\Users\karol\AppData\Roaming\ModrinthApp\meta\versions\26.2-0.19.5\26.2-0.19.5.jar!\net\minecraft\stats\RecipeBook.class
 * Java compiler version: 25 (69.0)
 * JD-Core Version:       1.1.3
 */