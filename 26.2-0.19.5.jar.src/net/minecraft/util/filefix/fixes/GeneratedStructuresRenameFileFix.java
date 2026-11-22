/*    */ package net.minecraft.util.filefix.fixes;
/*    */ 
/*    */ import com.mojang.datafixers.schemas.Schema;
/*    */ import java.util.List;
/*    */ import net.minecraft.util.filefix.FileFix;
/*    */ import net.minecraft.util.filefix.access.FileRelation;
/*    */ import net.minecraft.util.filefix.operations.FileFixOperation;
/*    */ import net.minecraft.util.filefix.operations.FileFixOperations;
/*    */ 
/*    */ public class GeneratedStructuresRenameFileFix extends FileFix {
/*    */   public GeneratedStructuresRenameFileFix(Schema schema) {
/* 14 */     super(schema);
/*    */   }
/*    */   
/*    */   public void makeFixer() {
/* 19 */     addFileFixOperation((FileFixOperation)FileFixOperations.applyInFolders(FileRelation.GENERATED_NAMESPACES, List.of(FileFixOperations.move("structures", "structure"))));
/*    */   }
/*    */ }


/* Location:              C:\Users\karol\AppData\Roaming\ModrinthApp\meta\versions\26.2-0.19.5\26.2-0.19.5.jar!\net\minecraf\\util\filefix\fixes\GeneratedStructuresRenameFileFix.class
 * Java compiler version: 25 (69.0)
 * JD-Core Version:       1.1.3
 */