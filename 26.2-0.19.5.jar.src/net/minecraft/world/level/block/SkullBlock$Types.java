/*    */ package net.minecraft.world.level.block;
/*    */ 
/*    */ public enum Types implements SkullBlock.Type {
/* 39 */   SKELETON("skeleton"),
/* 40 */   WITHER_SKELETON("wither_skeleton"),
/* 41 */   PLAYER("player"),
/* 42 */   ZOMBIE("zombie"),
/* 43 */   CREEPER("creeper"),
/* 44 */   PIGLIN("piglin"),
/* 45 */   DRAGON("dragon");
/*    */   
/*    */   private final String name;
/*    */   
/*    */   Types(String name) {
/* 51 */     this.name = name;
/* 52 */     TYPES.put(name, this);
/*    */   }
/*    */   
/*    */   public String getSerializedName() {
/* 57 */     return this.name;
/*    */   }
/*    */ }


/* Location:              C:\Users\karol\AppData\Roaming\ModrinthApp\meta\versions\26.2-0.19.5\26.2-0.19.5.jar!\net\minecraft\world\level\block\SkullBlock$Types.class
 * Java compiler version: 25 (69.0)
 * JD-Core Version:       1.1.3
 */