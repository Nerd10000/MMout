/*    */ package net.minecraft.util.filefix.access;
/*    */ 
/*    */ import com.mojang.datafixers.DSL;
/*    */ import java.nio.file.Path;
/*    */ import net.minecraft.util.datafix.DataFixTypes;
/*    */ import net.minecraft.world.level.chunk.storage.RegionStorageInfo;
/*    */ 
/*    */ public class FileResourceTypes {
/*  8 */   public static final FileResourceType<LevelDat> LEVEL_DAT = new FileResourceType<>(LevelDat::new);
/*    */   
/*  9 */   public static final FileResourceType<PlayerData> PLAYER_DATA = new FileResourceType<>(PlayerData::new);
/*    */   
/*    */   public static FileResourceType<SavedDataNbt> savedData(DSL.TypeReference type) {
/* 12 */     return savedData(type, CompressedNbt.MissingSeverity.NEUTRAL);
/*    */   }
/*    */   
/*    */   public static FileResourceType<SavedDataNbt> savedData(DSL.TypeReference type, CompressedNbt.MissingSeverity missingSeverity) {
/* 16 */     return new FileResourceType<>((path, dataVersion) -> new SavedDataNbt(type, path, dataVersion, missingSeverity));
/*    */   }
/*    */   
/*    */   public static FileResourceType<ChunkNbt> chunk(DataFixTypes type, RegionStorageInfo info) {
/* 20 */     return new FileResourceType<>((path, dataVersion) -> new ChunkNbt(info, path, type, dataVersion));
/*    */   }
/*    */ }


/* Location:              C:\Users\karol\AppData\Roaming\ModrinthApp\meta\versions\26.2-0.19.5\26.2-0.19.5.jar!\net\minecraf\\util\filefix\access\FileResourceTypes.class
 * Java compiler version: 25 (69.0)
 * JD-Core Version:       1.1.3
 */