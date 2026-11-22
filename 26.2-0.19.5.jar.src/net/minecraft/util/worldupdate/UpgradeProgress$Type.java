/*    */ package net.minecraft.util.worldupdate;
/*    */ 
/*    */ import net.minecraft.network.chat.Component;
/*    */ import net.minecraft.util.StringRepresentable;
/*    */ 
/*    */ public enum Type implements StringRepresentable {
/* 42 */   FILES("files"),
/* 43 */   LEGACY_STRUCTURES("legacy_structures"),
/* 44 */   REGIONS("regions");
/*    */   
/*    */   private final String id;
/*    */   
/*    */   private final Component label;
/*    */   
/*    */   Type(String id) {
/* 51 */     this.id = id;
/* 52 */     this.label = (Component)Component.translatable("upgradeWorld.progress.type." + getSerializedName());
/*    */   }
/*    */   
/*    */   public String getSerializedName() {
/* 57 */     return this.id;
/*    */   }
/*    */   
/*    */   public Component label() {
/* 61 */     return this.label;
/*    */   }
/*    */ }


/* Location:              C:\Users\karol\AppData\Roaming\ModrinthApp\meta\versions\26.2-0.19.5\26.2-0.19.5.jar!\net\minecraf\\util\worldupdate\UpgradeProgress$Type.class
 * Java compiler version: 25 (69.0)
 * JD-Core Version:       1.1.3
 */