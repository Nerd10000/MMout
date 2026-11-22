/*     */ package net.minecraft.util.filefix;
/*     */ 
/*     */ import com.mojang.datafixers.DataFixerBuilder;
/*     */ import com.mojang.datafixers.schemas.Schema;
/*     */ import java.util.ArrayList;
/*     */ import java.util.List;
/*     */ import java.util.Locale;
/*     */ import java.util.function.BiFunction;
/*     */ 
/*     */ public class Builder {
/* 380 */   public final List<FileFix> fileFixes = new ArrayList<>();
/*     */   
/*     */   private final int currentVersion;
/*     */   
/*     */   private int latestFileFixerVersion;
/*     */   
/* 383 */   private final List<Schema> knownSchemas = new ArrayList<>();
/*     */   
/*     */   public Builder(int currentVersion) {
/* 386 */     this.currentVersion = currentVersion;
/*     */   }
/*     */   
/*     */   public void addFixer(FileFix fileFix) {
/* 390 */     if (!this.knownSchemas.contains(fileFix.getSchema()))
/* 391 */       throw new IllegalArgumentException("Tried to add file fixer with unknown schema. Add it through FileFixerUpper#addSchema instead"); 
/* 393 */     int fileFixVersion = fileFix.getVersion();
/* 394 */     if (fileFix.getVersion() > this.currentVersion)
/* 395 */       throw new IllegalArgumentException(String.format(Locale.ROOT, "Tried to add too recent file fix for version: %s. The data version of the game is: %s", new Object[] { Integer.valueOf(fileFixVersion), Integer.valueOf(this.currentVersion) })); 
/* 397 */     if (!this.fileFixes.isEmpty()) {
/* 398 */       FileFix last = this.fileFixes.getLast();
/* 399 */       if (last.getVersion() > fileFixVersion)
/* 400 */         throw new IllegalArgumentException(String.format(Locale.ROOT, "Tried to add too recent file fix for version: %s. The most recent file fix version is %s", new Object[] { Integer.valueOf(fileFixVersion), Integer.valueOf(last.getVersion()) })); 
/*     */     } 
/* 403 */     this.fileFixes.add(fileFix);
/*     */   }
/*     */   
/*     */   public Schema addSchema(DataFixerBuilder fixerUpper, int version, BiFunction<Integer, Schema, Schema> factory) {
/* 407 */     this.latestFileFixerVersion = Math.max(version, this.latestFileFixerVersion);
/* 408 */     Schema schema = fixerUpper.addSchema(version, factory);
/* 409 */     this.knownSchemas.add(schema);
/* 410 */     return schema;
/*     */   }
/*     */   
/*     */   public FileFixerUpper build(DataFixerBuilder.Result dataFixer) {
/* 414 */     return new FileFixerUpper(dataFixer, this.fileFixes, this.latestFileFixerVersion);
/*     */   }
/*     */ }


/* Location:              C:\Users\karol\AppData\Roaming\ModrinthApp\meta\versions\26.2-0.19.5\26.2-0.19.5.jar!\net\minecraf\\util\filefix\FileFixerUpper$Builder.class
 * Java compiler version: 25 (69.0)
 * JD-Core Version:       1.1.3
 */