/*    */ package net.minecraft.data.recipes;
/*    */ 
/*    */ public enum RecipeCategory {
/*  4 */   BUILDING_BLOCKS("building_blocks"),
/*  5 */   DECORATIONS("decorations"),
/*  6 */   REDSTONE("redstone"),
/*  7 */   TRANSPORTATION("transportation"),
/*  8 */   TOOLS("tools"),
/*  9 */   COMBAT("combat"),
/* 10 */   FOOD("food"),
/* 11 */   BREWING("brewing"),
/* 12 */   MISC("misc");
/*    */   
/*    */   private final String recipeFolderName;
/*    */   
/*    */   RecipeCategory(String recipeFolderName) {
/* 17 */     this.recipeFolderName = recipeFolderName;
/*    */   }
/*    */   
/*    */   public String getFolderName() {
/* 21 */     return this.recipeFolderName;
/*    */   }
/*    */ }


/* Location:              C:\Users\karol\AppData\Roaming\ModrinthApp\meta\versions\26.2-0.19.5\26.2-0.19.5.jar!\net\minecraft\data\recipes\RecipeCategory.class
 * Java compiler version: 25 (69.0)
 * JD-Core Version:       1.1.3
 */