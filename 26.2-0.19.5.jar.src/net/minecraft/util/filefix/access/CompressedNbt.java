/*    */ package net.minecraft.util.filefix.access;
/*    */ 
/*    */ import com.mojang.logging.LogUtils;
/*    */ import com.mojang.serialization.Dynamic;
/*    */ import com.mojang.serialization.DynamicOps;
/*    */ import java.io.IOException;
/*    */ import java.nio.file.NoSuchFileException;
/*    */ import java.nio.file.Path;
/*    */ import java.util.Objects;
/*    */ import java.util.Optional;
/*    */ import java.util.function.BiConsumer;
/*    */ import net.minecraft.nbt.CompoundTag;
/*    */ import net.minecraft.nbt.NbtAccounter;
/*    */ import net.minecraft.nbt.NbtIo;
/*    */ import net.minecraft.nbt.NbtOps;
/*    */ import net.minecraft.nbt.Tag;
/*    */ import net.minecraft.util.FileUtil;
/*    */ import org.slf4j.Logger;
/*    */ 
/*    */ public abstract class CompressedNbt implements AutoCloseable {
/* 20 */   private static final Logger LOGGER = LogUtils.getLogger();
/*    */   
/*    */   private final Path path;
/*    */   
/*    */   private final MissingSeverity missingSeverity;
/*    */   
/*    */   public enum MissingSeverity {
/* 25 */     IMPORTANT((String)CompressedNbt.LOGGER::error),
/* 26 */     NEUTRAL((String)CompressedNbt.LOGGER::info),
/* 27 */     MINOR((String)CompressedNbt.LOGGER::debug);
/*    */     
/*    */     static {
/*    */       Objects.requireNonNull(CompressedNbt.LOGGER);
/*    */     }
/*    */     
/*    */     static {
/*    */       Objects.requireNonNull(CompressedNbt.LOGGER);
/*    */     }
/*    */     
/*    */     private final BiConsumer<String, Object> logFunction;
/*    */     
/*    */     static {
/* 27 */       Objects.requireNonNull(CompressedNbt.LOGGER);
/*    */     }
/*    */     
/*    */     MissingSeverity(BiConsumer<String, Object> logFunction) {
/* 32 */       this.logFunction = logFunction;
/*    */     }
/*    */     
/*    */     public void log(String message, Path path) {
/* 36 */       this.logFunction.accept(message, path);
/*    */     }
/*    */   }
/*    */   
/*    */   public CompressedNbt(Path path, MissingSeverity missingSeverity) {
/* 41 */     this.path = path;
/* 42 */     this.missingSeverity = missingSeverity;
/*    */   }
/*    */   
/*    */   public abstract Optional<Dynamic<Tag>> read() throws IOException;
/*    */   
/*    */   protected final Optional<Dynamic<Tag>> readFile() throws IOException {
/*    */     try {
/* 49 */       return Optional.of(new Dynamic((DynamicOps)NbtOps.INSTANCE, NbtIo.readCompressed(this.path, NbtAccounter.unlimitedHeap())));
/* 50 */     } catch (NoSuchFileException ignored) {
/* 51 */       this.missingSeverity.log("Missing file: {}", this.path);
/* 53 */       return Optional.empty();
/*    */     } 
/*    */   }
/*    */   
/*    */   public abstract <T> void write(Dynamic<T> paramDynamic);
/*    */   
/*    */   protected final <T> void writeFile(Dynamic<T> data) {
/* 59 */     CompoundTag cast = (CompoundTag)data.cast((DynamicOps)NbtOps.INSTANCE);
/*    */     try {
/* 61 */       FileUtil.createDirectoriesSafe(this.path.getParent());
/* 62 */       NbtIo.writeCompressed(cast, this.path);
/* 63 */     } catch (IOException e) {
/* 64 */       LOGGER.error("Failed to write to {}: {}", this.path, e);
/*    */     } 
/*    */   }
/*    */   
/*    */   public Path path() {
/* 69 */     return this.path;
/*    */   }
/*    */   
/*    */   public void close() {}
/*    */ }


/* Location:              C:\Users\karol\AppData\Roaming\ModrinthApp\meta\versions\26.2-0.19.5\26.2-0.19.5.jar!\net\minecraf\\util\filefix\access\CompressedNbt.class
 * Java compiler version: 25 (69.0)
 * JD-Core Version:       1.1.3
 */