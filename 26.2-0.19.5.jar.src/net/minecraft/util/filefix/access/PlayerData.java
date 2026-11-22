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
/*    */ public class PlayerData extends CompressedNbt {
/*    */   private final int targetVersion;
/*    */   
/*    */   public PlayerData(Path path, int targetVersion) {
/* 17 */     super(path, CompressedNbt.MissingSeverity.NEUTRAL);
/* 18 */     this.targetVersion = targetVersion;
/*    */   }
/*    */   
/*    */   public Optional<Dynamic<Tag>> read() throws IOException {
/* 23 */     return readFile().map(readData -> {
/*    */           int version = NbtUtils.getDataVersion(readData);
/*    */           return DataFixers.getDataFixer().update(References.PLAYER, readData, version, this.targetVersion);
/*    */         });
/*    */   }
/*    */   
/*    */   public <T> void write(Dynamic<T> data) {
/* 31 */     Dynamic<T> withDataVersion = NbtUtils.addDataVersion(data, this.targetVersion);
/* 32 */     writeFile(withDataVersion);
/*    */   }
/*    */ }


/* Location:              C:\Users\karol\AppData\Roaming\ModrinthApp\meta\versions\26.2-0.19.5\26.2-0.19.5.jar!\net\minecraf\\util\filefix\access\PlayerData.class
 * Java compiler version: 25 (69.0)
 * JD-Core Version:       1.1.3
 */