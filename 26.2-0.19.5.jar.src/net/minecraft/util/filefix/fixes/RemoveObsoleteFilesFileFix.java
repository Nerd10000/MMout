/*    */ package net.minecraft.util.filefix.fixes;
/*    */ 
/*    */ import com.google.common.collect.Streams;
/*    */ import com.mojang.datafixers.schemas.Schema;
/*    */ import java.util.stream.Stream;
/*    */ import net.minecraft.util.filefix.FileFix;
/*    */ import net.minecraft.util.filefix.operations.FileFixOperation;
/*    */ import net.minecraft.util.filefix.operations.FileFixOperations;
/*    */ 
/*    */ public class RemoveObsoleteFilesFileFix extends FileFix {
/*    */   public RemoveObsoleteFilesFileFix(Schema schema) {
/* 13 */     super(schema);
/*    */   }
/*    */   
/*    */   public void makeFixer() {
/* 19 */     addFileFixOperation((FileFixOperation)FileFixOperations.delete("data/villages.dat"));
/* 20 */     addFileFixOperation((FileFixOperation)FileFixOperations.delete("data/villages_end.dat"));
/* 21 */     addFileFixOperation((FileFixOperation)FileFixOperations.delete("data/villages_nether.dat"));
/* 22 */     addFileFixOperation((FileFixOperation)FileFixOperations.delete("data/advancements"));
/* 23 */     addFileFixOperation((FileFixOperation)FileFixOperations.delete("data/functions"));
/* 24 */     Streams.concat(new Stream[] { LegacyStructureFileFix.OVERWORLD_LEGACY_STRUCTURES
/* 25 */           .stream(), 
/* 26 */           Stream.of("Village"), LegacyStructureFileFix.NETHER_LEGACY_STRUCTURES
/* 27 */           .stream(), LegacyStructureFileFix.END_LEGACY_STRUCTURES
/* 28 */           .stream() }).forEach(leftoverStructure -> {
/*    */           addFileFixOperation((FileFixOperation)FileFixOperations.delete("data/" + leftoverStructure + ".dat"));
/*    */           addFileFixOperation((FileFixOperation)FileFixOperations.delete("data/" + leftoverStructure + "_index.dat"));
/*    */         });
/*    */   }
/*    */ }


/* Location:              C:\Users\karol\AppData\Roaming\ModrinthApp\meta\versions\26.2-0.19.5\26.2-0.19.5.jar!\net\minecraf\\util\filefix\fixes\RemoveObsoleteFilesFileFix.class
 * Java compiler version: 25 (69.0)
 * JD-Core Version:       1.1.3
 */