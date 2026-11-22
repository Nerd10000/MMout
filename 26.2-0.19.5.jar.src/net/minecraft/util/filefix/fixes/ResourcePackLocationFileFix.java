/*    */ package net.minecraft.util.filefix.fixes;
/*    */ 
/*    */ import com.mojang.datafixers.schemas.Schema;
/*    */ import net.minecraft.util.filefix.FileFix;
/*    */ import net.minecraft.util.filefix.operations.FileFixOperation;
/*    */ import net.minecraft.util.filefix.operations.FileFixOperations;
/*    */ 
/*    */ public class ResourcePackLocationFileFix extends FileFix {
/*    */   public ResourcePackLocationFileFix(Schema schema) {
/* 10 */     super(schema);
/*    */   }
/*    */   
/*    */   public void makeFixer() {
/* 15 */     addFileFixOperation((FileFixOperation)FileFixOperations.move("resources.zip", "resourcepacks/resources.zip"));
/*    */   }
/*    */ }


/* Location:              C:\Users\karol\AppData\Roaming\ModrinthApp\meta\versions\26.2-0.19.5\26.2-0.19.5.jar!\net\minecraf\\util\filefix\fixes\ResourcePackLocationFileFix.class
 * Java compiler version: 25 (69.0)
 * JD-Core Version:       1.1.3
 */