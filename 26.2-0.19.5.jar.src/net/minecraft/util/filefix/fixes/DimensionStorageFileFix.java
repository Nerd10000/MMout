/*    */ package net.minecraft.util.filefix.fixes;
/*    */ 
/*    */ import com.mojang.datafixers.schemas.Schema;
/*    */ import java.util.List;
/*    */ import java.util.Map;
/*    */ import net.minecraft.util.filefix.FileFix;
/*    */ import net.minecraft.util.filefix.access.FileRelation;
/*    */ import net.minecraft.util.filefix.operations.FileFixOperation;
/*    */ import net.minecraft.util.filefix.operations.FileFixOperations;
/*    */ 
/*    */ public class DimensionStorageFileFix extends FileFix {
/*    */   public DimensionStorageFileFix(Schema schema) {
/* 20 */     super(schema);
/*    */   }
/*    */   
/*    */   public void makeFixer() {
/* 25 */     addFileFixOperation((FileFixOperation)FileFixOperations.applyInFolders(FileRelation.DIMENSIONS_DATA, 
/*    */           
/* 27 */           List.of(
/* 28 */             FileFixOperations.move("chunks.dat", "minecraft/chunk_tickets.dat"), 
/* 29 */             FileFixOperations.move("raids.dat", "minecraft/raids.dat"), 
/* 30 */             FileFixOperations.move("world_border.dat", "minecraft/world_border.dat"))));
/* 33 */     addFileFixOperation((FileFixOperation)FileFixOperations.groupMove(
/* 34 */           Map.of("data", "dimensions/minecraft/overworld/data/minecraft", "DIM-1/data", "dimensions/minecraft/the_nether/data/minecraft", "DIM1/data", "dimensions/minecraft/the_end/data/minecraft"), 
/*    */           
/* 39 */           List.of(
/* 40 */             FileFixOperations.move("chunks.dat", "chunk_tickets.dat"), 
/* 41 */             FileFixOperations.moveSimple("raids.dat"), 
/* 42 */             FileFixOperations.move("raids_end.dat", "raids.dat"), 
/* 43 */             FileFixOperations.moveSimple("world_border.dat"))));
/* 46 */     addFileFixOperation((FileFixOperation)FileFixOperations.delete("DIM1/data"));
/* 47 */     addFileFixOperation((FileFixOperation)FileFixOperations.delete("DIM-1/data"));
/* 49 */     addFileFixOperation((FileFixOperation)FileFixOperations.applyInFolders(FileRelation.DATA, 
/*    */           
/* 51 */           List.of(
/* 52 */             FileFixOperations.move("scoreboard.dat", "minecraft/scoreboard.dat"), 
/* 53 */             FileFixOperations.move("stopwatches.dat", "minecraft/stopwatches.dat"), 
/* 54 */             FileFixOperations.moveRegex("command_storage_([a-z0-9_.-]+)\\.dat", "$1/command_storage\\.dat"), 
/* 55 */             FileFixOperations.move("idcounts.dat", "minecraft/maps/last_id.dat"), 
/* 56 */             FileFixOperations.moveRegex("map_(\\d+)\\.dat", "minecraft/maps/$1\\.dat"), 
/* 57 */             FileFixOperations.move("random_sequences.dat", "minecraft/random_sequences.dat"))));
/* 61 */     addFileFixOperation((FileFixOperation)FileFixOperations.groupMove(
/* 62 */           Map.of("", "dimensions/minecraft/overworld", "DIM-1", "dimensions/minecraft/the_nether", "DIM1", "dimensions/minecraft/the_end"), 
/*    */           
/* 67 */           List.of(
/* 68 */             FileFixOperations.moveSimple("region"), 
/* 69 */             FileFixOperations.moveSimple("entities"), 
/* 70 */             FileFixOperations.moveSimple("poi"))));
/* 73 */     addFileFixOperation((FileFixOperation)FileFixOperations.delete("DIM-1"));
/* 74 */     addFileFixOperation((FileFixOperation)FileFixOperations.delete("DIM1"));
/*    */   }
/*    */ }


/* Location:              C:\Users\karol\AppData\Roaming\ModrinthApp\meta\versions\26.2-0.19.5\26.2-0.19.5.jar!\net\minecraf\\util\filefix\fixes\DimensionStorageFileFix.class
 * Java compiler version: 25 (69.0)
 * JD-Core Version:       1.1.3
 */