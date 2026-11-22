/*    */ package net.minecraft.util.datafix.fixes;
/*    */ 
/*    */ import com.mojang.datafixers.schemas.Schema;
/*    */ import java.util.function.Function;
/*    */ 
/*    */ class null extends BlockRenameFix {
/*    */   null(Schema outputSchema, String name) {
/* 72 */     super(outputSchema, name);
/*    */   }
/*    */   
/*    */   protected String renameBlock(String block) {
/* 75 */     return renamer.apply(block);
/*    */   }
/*    */ }


/* Location:              C:\Users\karol\AppData\Roaming\ModrinthApp\meta\versions\26.2-0.19.5\26.2-0.19.5.jar!\net\minecraf\\util\datafix\fixes\BlockRenameFix$1.class
 * Java compiler version: 25 (69.0)
 * JD-Core Version:       1.1.3
 */