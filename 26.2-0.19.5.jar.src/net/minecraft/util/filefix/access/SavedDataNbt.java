/*    */ package net.minecraft.util.filefix.access;
/*    */ 
/*    */ import com.mojang.datafixers.DSL;
/*    */ import com.mojang.serialization.Dynamic;
/*    */ import java.io.IOException;
/*    */ import java.nio.file.Path;
/*    */ import java.util.Optional;
/*    */ import net.minecraft.nbt.NbtUtils;
/*    */ import net.minecraft.nbt.Tag;
/*    */ import net.minecraft.util.datafix.DataFixers;
/*    */ 
/*    */ public class SavedDataNbt extends CompressedNbt {
/*    */   private final DSL.TypeReference type;
/*    */   
/*    */   private final int targetVersion;
/*    */   
/*    */   public SavedDataNbt(DSL.TypeReference type, Path path, int targetVersion, CompressedNbt.MissingSeverity missingSeverity) {
/* 18 */     super(path, missingSeverity);
/* 19 */     this.type = type;
/* 20 */     this.targetVersion = targetVersion;
/*    */   }
/*    */   
/*    */   public Optional<Dynamic<Tag>> read() throws IOException {
/* 25 */     return readFile().map(readData -> {
/*    */           int version = NbtUtils.getDataVersion(readData);
/*    */           return DataFixers.getDataFixer().update(this.type, readData, version, this.targetVersion).get("data").orElseEmptyMap();
/*    */         });
/*    */   }
/*    */   
/*    */   public <T> void write(Dynamic<T> data) {
/* 33 */     Dynamic<T> dataTag = data.emptyMap().set("data", data);
/* 34 */     Dynamic<T> wrappedAndWithDataVersion = NbtUtils.addDataVersion(dataTag, this.targetVersion);
/* 35 */     writeFile(wrappedAndWithDataVersion);
/*    */   }
/*    */ }


/* Location:              C:\Users\karol\AppData\Roaming\ModrinthApp\meta\versions\26.2-0.19.5\26.2-0.19.5.jar!\net\minecraf\\util\filefix\access\SavedDataNbt.class
 * Java compiler version: 25 (69.0)
 * JD-Core Version:       1.1.3
 */