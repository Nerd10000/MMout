/*    */ package net.minecraft.data;
/*    */ 
/*    */ public enum Variant {
/* 23 */   BUTTON("button"),
/* 24 */   CHISELED("chiseled") {
/*    */     public Variant getBaseVariantForCrafting() {
/* 27 */       return SLAB;
/*    */     }
/*    */   },
/* 30 */   CRACKED("cracked"),
/* 31 */   CUT("cut") {
/*    */     public String getPrefixedRecipeGroup(String prefix) {
/* 34 */       return prefix;
/*    */     }
/*    */   },
/* 37 */   DOOR("door"),
/* 38 */   CUSTOM_FENCE("fence"),
/* 39 */   FENCE("fence"),
/* 40 */   CUSTOM_FENCE_GATE("fence_gate"),
/* 41 */   FENCE_GATE("fence_gate"),
/* 42 */   CUSTOM_HANGING_SIGN("hanging_sign") {
/*    */     public Variant getBaseVariantForCrafting() {
/* 45 */       return STRIPPED_LOG;
/*    */     }
/*    */   },
/* 48 */   HANGING_SIGN("hanging_sign") {
/*    */     public Variant getBaseVariantForCrafting() {
/* 51 */       return STRIPPED_LOG;
/*    */     }
/*    */   },
/* 54 */   LOG("log"),
/* 55 */   STRIPPED_LOG("stripped_log"),
/* 56 */   MOSAIC("mosaic"),
/* 57 */   SIGN("sign"),
/* 58 */   SLAB("slab"),
/* 59 */   STAIRS("stairs"),
/* 60 */   PRESSURE_PLATE("pressure_plate"),
/* 61 */   POLISHED("polished"),
/* 62 */   TRAPDOOR("trapdoor"),
/* 63 */   WALL("wall"),
/* 64 */   WALL_SIGN("wall_sign"),
/* 65 */   CUSTOM_WALL_HANGING_SIGN("wall_hanging_sign"),
/* 66 */   WALL_HANGING_SIGN("wall_hanging_sign"),
/* 67 */   BRICKS("bricks"),
/* 68 */   COBBLED("cobbled"),
/* 69 */   TILES("tiles"),
/* 70 */   PILLAR("pillar");
/*    */   
/*    */   private final String recipeGroup;
/*    */   
/*    */   Variant(String recipeGroup) {
/* 75 */     this.recipeGroup = recipeGroup;
/*    */   }
/*    */   
/*    */   public String getRecipeGroup() {
/* 79 */     return this.recipeGroup;
/*    */   }
/*    */   
/*    */   public String getPrefixedRecipeGroup(String prefix) {
/* 83 */     return prefix + "_" + prefix;
/*    */   }
/*    */   
/*    */   public Variant getBaseVariantForCrafting() {
/* 87 */     return null;
/*    */   }
/*    */ }


/* Location:              C:\Users\karol\AppData\Roaming\ModrinthApp\meta\versions\26.2-0.19.5\26.2-0.19.5.jar!\net\minecraft\data\BlockFamily$Variant.class
 * Java compiler version: 25 (69.0)
 * JD-Core Version:       1.1.3
 */