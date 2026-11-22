/*    */ package net.minecraft.util.filefix.fixes;
/*    */ 
/*    */ import com.mojang.datafixers.schemas.Schema;
/*    */ import com.mojang.serialization.Dynamic;
/*    */ import java.io.IOException;
/*    */ import java.util.Optional;
/*    */ import net.minecraft.nbt.Tag;
/*    */ import net.minecraft.util.datafix.fixes.References;
/*    */ import net.minecraft.util.filefix.FileFix;
/*    */ import net.minecraft.util.filefix.access.FileAccess;
/*    */ import net.minecraft.util.filefix.access.FileAccessProvider;
/*    */ import net.minecraft.util.filefix.access.FileRelation;
/*    */ import net.minecraft.util.filefix.access.FileResourceTypes;
/*    */ import net.minecraft.util.filefix.access.LevelDat;
/*    */ import net.minecraft.util.filefix.access.SavedDataNbt;
/*    */ import net.minecraft.util.filefix.operations.ModifyContent;
/*    */ import net.minecraft.util.worldupdate.UpgradeProgress;
/*    */ 
/*    */ public class ReenableSpectatorsGenerateChunksInHardcoreWorldsFileFix extends FileFix {
/*    */   public ReenableSpectatorsGenerateChunksInHardcoreWorldsFileFix(Schema schema) {
/* 21 */     super(schema);
/*    */   }
/*    */   
/*    */   public void makeFixer() {
/* 26 */     addFileContentFix(files -> {
/*    */           FileAccess<LevelDat> levelDat = files.getFileAccess(FileResourceTypes.LEVEL_DAT, FileRelation.ORIGIN.forFile("level.dat"));
/*    */           FileAccess<SavedDataNbt> gameRules = files.getFileAccess(FileResourceTypes.savedData(References.SAVED_DATA_GAME_RULES), FileRelation.DATA.forFile("minecraft/game_rules.dat"));
/*    */           return ();
/*    */         });
/*    */   }
/*    */ }


/* Location:              C:\Users\karol\AppData\Roaming\ModrinthApp\meta\versions\26.2-0.19.5\26.2-0.19.5.jar!\net\minecraf\\util\filefix\fixes\ReenableSpectatorsGenerateChunksInHardcoreWorldsFileFix.class
 * Java compiler version: 25 (69.0)
 * JD-Core Version:       1.1.3
 */