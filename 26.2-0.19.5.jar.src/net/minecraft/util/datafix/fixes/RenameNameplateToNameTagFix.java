/*    */ package net.minecraft.util.datafix.fixes;
/*    */ 
/*    */ import com.mojang.datafixers.schemas.Schema;
/*    */ 
/*    */ public class RenameNameplateToNameTagFix extends AttributesRenameFix {
/*    */   public RenameNameplateToNameTagFix(Schema outputSchema) {
/*  7 */     super(outputSchema, "RenameNameplateToNameTag", RenameNameplateToNameTagFix::rename);
/*    */   }
/*    */   
/*    */   private static String rename(String oldName) {
/* 11 */     if (oldName.equals("minecraft:nameplate_distance"))
/* 12 */       return "minecraft:name_tag_distance"; 
/* 14 */     return oldName;
/*    */   }
/*    */ }


/* Location:              C:\Users\karol\AppData\Roaming\ModrinthApp\meta\versions\26.2-0.19.5\26.2-0.19.5.jar!\net\minecraf\\util\datafix\fixes\RenameNameplateToNameTagFix.class
 * Java compiler version: 25 (69.0)
 * JD-Core Version:       1.1.3
 */