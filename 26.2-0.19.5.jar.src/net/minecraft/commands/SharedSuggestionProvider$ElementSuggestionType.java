/*    */ package net.minecraft.commands;
/*    */ 
/*    */ public enum ElementSuggestionType {
/* 83 */   TAGS, ELEMENTS, ALL;
/*    */   
/*    */   public boolean shouldSuggestTags() {
/* 89 */     return (this == TAGS || this == ALL);
/*    */   }
/*    */   
/*    */   public boolean shouldSuggestElements() {
/* 93 */     return (this == ELEMENTS || this == ALL);
/*    */   }
/*    */ }


/* Location:              C:\Users\karol\AppData\Roaming\ModrinthApp\meta\versions\26.2-0.19.5\26.2-0.19.5.jar!\net\minecraft\commands\SharedSuggestionProvider$ElementSuggestionType.class
 * Java compiler version: 25 (69.0)
 * JD-Core Version:       1.1.3
 */