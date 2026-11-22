/*     */ package net.minecraft.data.structures;
/*     */ 
/*     */ import java.nio.file.Path;
/*     */ 
/*     */ class StructureConversionException extends RuntimeException {
/*     */   public StructureConversionException(Path path, Throwable t) {
/* 125 */     super(path.toAbsolutePath().toString(), t);
/*     */   }
/*     */ }


/* Location:              C:\Users\karol\AppData\Roaming\ModrinthApp\meta\versions\26.2-0.19.5\26.2-0.19.5.jar!\net\minecraft\data\structures\SnbtToNbt$StructureConversionException.class
 * Java compiler version: 25 (69.0)
 * JD-Core Version:       1.1.3
 */