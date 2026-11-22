/*    */ package net.minecraft.util.filefix.access;
/*    */ 
/*    */ import com.mojang.serialization.Dynamic;
/*    */ import java.io.IOException;
/*    */ import java.nio.file.Path;
/*    */ import java.util.Optional;
/*    */ import net.minecraft.nbt.NbtUtils;
/*    */ import net.minecraft.nbt.Tag;
/*    */ import net.minecraft.util.datafix.DataFixers;
/*    */ import net.minecraft.util.datafix.fixes.References;
/*    */ 
/*    */ public class LevelDat extends CompressedNbt {
/*    */   private final int targetVersion;
/*    */   
/*    */   public LevelDat(Path path, int targetVersion) {
/* 18 */     super(path, CompressedNbt.MissingSeverity.IMPORTANT);
/* 19 */     this.targetVersion = targetVersion;
/*    */   }
/*    */   
/*    */   public Optional<Dynamic<Tag>> read() throws IOException {
/* 24 */     return readFile().map(readData -> {
/*    */           Dynamic<Tag> content = readData.get("Data").orElseEmptyMap();
/*    */           int dataVersion = NbtUtils.getDataVersion(content);
/*    */           return DataFixers.getDataFixer().update(References.LEVEL, content, dataVersion, this.targetVersion);
/*    */         });
/*    */   }
/*    */   
/*    */   public <T> void write(Dynamic<T> data) {
/* 33 */     writeFile(data.emptyMap().set("Data", NbtUtils.addDataVersion(data, this.targetVersion)));
/*    */   }
/*    */ }


/* Location:              C:\Users\karol\AppData\Roaming\ModrinthApp\meta\versions\26.2-0.19.5\26.2-0.19.5.jar!\net\minecraf\\util\filefix\access\LevelDat.class
 * Java compiler version: 25 (69.0)
 * JD-Core Version:       1.1.3
 */