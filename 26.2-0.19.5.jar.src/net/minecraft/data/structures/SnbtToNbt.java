/*     */ package net.minecraft.data.structures;
/*     */ 
/*     */ import com.google.common.collect.Lists;
/*     */ import com.google.common.hash.HashCode;
/*     */ import com.google.common.hash.Hashing;
/*     */ import com.google.common.hash.HashingOutputStream;
/*     */ import com.mojang.logging.LogUtils;
/*     */ import java.io.BufferedReader;
/*     */ import java.io.ByteArrayOutputStream;
/*     */ import java.io.IOException;
/*     */ import java.io.OutputStream;
/*     */ import java.nio.file.Files;
/*     */ import java.nio.file.Path;
/*     */ import java.util.Iterator;
/*     */ import java.util.List;
/*     */ import java.util.concurrent.CompletableFuture;
/*     */ import java.util.concurrent.CompletionStage;
/*     */ import java.util.stream.Stream;
/*     */ import net.minecraft.data.CachedOutput;
/*     */ import net.minecraft.data.DataProvider;
/*     */ import net.minecraft.data.PackOutput;
/*     */ import net.minecraft.nbt.CompoundTag;
/*     */ import net.minecraft.nbt.NbtIo;
/*     */ import net.minecraft.nbt.NbtUtils;
/*     */ import net.minecraft.util.Util;
/*     */ import org.apache.commons.io.IOUtils;
/*     */ import org.slf4j.Logger;
/*     */ 
/*     */ public class SnbtToNbt implements DataProvider {
/*  28 */   private static final Logger LOGGER = LogUtils.getLogger();
/*     */   
/*     */   private final PackOutput output;
/*     */   
/*     */   private final Iterable<Path> inputFolders;
/*     */   
/*  33 */   private final List<Filter> filters = Lists.newArrayList();
/*     */   
/*     */   public SnbtToNbt(PackOutput output, Path inputFolder) {
/*  37 */     this(output, List.of(inputFolder));
/*     */   }
/*     */   
/*     */   public SnbtToNbt(PackOutput output, Iterable<Path> inputFolders) {
/*  41 */     this.output = output;
/*  42 */     this.inputFolders = inputFolders;
/*     */   }
/*     */   
/*     */   public SnbtToNbt addFilter(Filter filter) {
/*  46 */     this.filters.add(filter);
/*  47 */     return this;
/*     */   }
/*     */   
/*     */   private CompoundTag applyFilters(String name, CompoundTag input) {
/*  51 */     CompoundTag result = input;
/*  52 */     for (Filter filter : this.filters)
/*  53 */       result = filter.apply(name, result); 
/*  55 */     return result;
/*     */   }
/*     */   
/*     */   @FunctionalInterface
/*     */   public static interface Filter {
/*     */     CompoundTag apply(String param1String, CompoundTag param1CompoundTag);
/*     */   }
/*     */   
/*     */   private static final class TaskResult extends Record {
/*     */     private final String name;
/*     */     
/*     */     private final byte[] payload;
/*     */     
/*     */     private final HashCode hash;
/*     */     
/*     */     private TaskResult(String name, byte[] payload, HashCode hash) {
/*  58 */       this.name = name;
/*  58 */       this.payload = payload;
/*  58 */       this.hash = hash;
/*     */     }
/*     */     
/*     */     public final String toString() {
/*     */       // Byte code:
/*     */       //   0: aload_0
/*     */       //   1: <illegal opcode> toString : (Lnet/minecraft/data/structures/SnbtToNbt$TaskResult;)Ljava/lang/String;
/*     */       //   6: areturn
/*     */       // Line number table:
/*     */       //   Java source line number -> byte code offset
/*     */       //   #58	-> 0
/*     */       // Local variable table:
/*     */       //   start	length	slot	name	descriptor
/*     */       //   0	7	0	this	Lnet/minecraft/data/structures/SnbtToNbt$TaskResult;
/*     */     }
/*     */     
/*     */     public final int hashCode() {
/*     */       // Byte code:
/*     */       //   0: aload_0
/*     */       //   1: <illegal opcode> hashCode : (Lnet/minecraft/data/structures/SnbtToNbt$TaskResult;)I
/*     */       //   6: ireturn
/*     */       // Line number table:
/*     */       //   Java source line number -> byte code offset
/*     */       //   #58	-> 0
/*     */       // Local variable table:
/*     */       //   start	length	slot	name	descriptor
/*     */       //   0	7	0	this	Lnet/minecraft/data/structures/SnbtToNbt$TaskResult;
/*     */     }
/*     */     
/*     */     public final boolean equals(Object o) {
/*     */       // Byte code:
/*     */       //   0: aload_0
/*     */       //   1: aload_1
/*     */       //   2: <illegal opcode> equals : (Lnet/minecraft/data/structures/SnbtToNbt$TaskResult;Ljava/lang/Object;)Z
/*     */       //   7: ireturn
/*     */       // Line number table:
/*     */       //   Java source line number -> byte code offset
/*     */       //   #58	-> 0
/*     */       // Local variable table:
/*     */       //   start	length	slot	name	descriptor
/*     */       //   0	8	0	this	Lnet/minecraft/data/structures/SnbtToNbt$TaskResult;
/*     */       //   0	8	1	o	Ljava/lang/Object;
/*     */     }
/*     */     
/*     */     public String name() {
/*  58 */       return this.name;
/*     */     }
/*     */     
/*     */     public byte[] payload() {
/*  58 */       return this.payload;
/*     */     }
/*     */     
/*     */     public HashCode hash() {
/*  58 */       return this.hash;
/*     */     }
/*     */   }
/*     */   
/*     */   public CompletableFuture<?> run(CachedOutput cache) {
/*  62 */     Path output = this.output.getOutputFolder();
/*  64 */     List<CompletableFuture<?>> tasks = Lists.newArrayList();
/*  66 */     for (Iterator<Path> iterator = this.inputFolders.iterator(); iterator.hasNext(); ) {
/*  66 */       Path input = iterator.next();
/*  67 */       tasks.add(CompletableFuture.supplyAsync(() -> {
/*     */               try {
/*     */                 Stream<Path> files = Files.walk(input, new java.nio.file.FileVisitOption[0]);
/*     */                 try {
/*     */                   CompletableFuture<Void> completableFuture = CompletableFuture.allOf((CompletableFuture<?>[])files.filter(()).map(()).toArray(()));
/*     */                   if (files != null)
/*     */                     files.close(); 
/*     */                   return completableFuture;
/*  68 */                 } catch (Throwable t$) {
/*     */                   if (files != null)
/*     */                     try {
/*     */                       files.close();
/*  68 */                     } catch (Throwable x2) {
/*     */                       t$.addSuppressed(x2);
/*     */                     }  
/*     */                   throw t$;
/*     */                 } 
/*  75 */               } catch (Exception e) {
/*     */                 throw new RuntimeException("Failed to read structure input directory, aborting", e);
/*     */               } 
/*  78 */             }Util.backgroundExecutor().forName("SnbtToNbt")).thenCompose(v -> v));
/*     */     } 
/*  81 */     return Util.sequenceFailFast(tasks);
/*     */   }
/*     */   
/*     */   public final String getName() {
/*  86 */     return "SNBT -> NBT";
/*     */   }
/*     */   
/*     */   private String getName(Path root, Path path) {
/*  90 */     String name = root.relativize(path).toString().replaceAll("\\\\", "/");
/*  91 */     return name.substring(0, name.length() - ".snbt".length());
/*     */   }
/*     */   
/*     */   private TaskResult readStructure(Path path, String name) {
/*     */     try {
/*  95 */       BufferedReader reader = Files.newBufferedReader(path);
/*     */       try {
/*  96 */         String input = IOUtils.toString(reader);
/*  97 */         CompoundTag updated = applyFilters(name, NbtUtils.snbtToStructure(input));
/*  98 */         ByteArrayOutputStream bos = new ByteArrayOutputStream();
/*  99 */         HashingOutputStream hos = new HashingOutputStream(Hashing.sha1(), bos);
/* 100 */         NbtIo.writeCompressed(updated, (OutputStream)hos);
/* 101 */         byte[] bytes = bos.toByteArray();
/* 102 */         HashCode hash = hos.hash();
/* 103 */         TaskResult taskResult = new TaskResult(name, bytes, hash);
/* 104 */         if (reader != null)
/* 104 */           reader.close(); 
/*     */         return taskResult;
/*     */       } catch (Throwable throwable) {
/*     */         if (reader != null)
/*     */           try {
/*     */             reader.close();
/*     */           } catch (Throwable throwable1) {
/*     */             throwable.addSuppressed(throwable1);
/*     */           }  
/*     */         throw throwable;
/*     */       } 
/* 104 */     } catch (Throwable t) {
/* 105 */       throw new StructureConversionException(path, t);
/*     */     } 
/*     */   }
/*     */   
/*     */   private void storeStructureIfChanged(CachedOutput cache, TaskResult task, Path output) {
/* 110 */     Path destination = output.resolve(task.name + ".nbt");
/*     */     try {
/* 112 */       cache.writeIfNeeded(destination, task.payload, task.hash);
/* 113 */     } catch (IOException e) {
/* 114 */       LOGGER.error("Couldn't write structure {} at {}", new Object[] { task.name, destination, e });
/*     */     } 
/*     */   }
/*     */   
/*     */   private static class StructureConversionException extends RuntimeException {
/*     */     public StructureConversionException(Path path, Throwable t) {
/* 125 */       super(path.toAbsolutePath().toString(), t);
/*     */     }
/*     */   }
/*     */ }


/* Location:              C:\Users\karol\AppData\Roaming\ModrinthApp\meta\versions\26.2-0.19.5\26.2-0.19.5.jar!\net\minecraft\data\structures\SnbtToNbt.class
 * Java compiler version: 25 (69.0)
 * JD-Core Version:       1.1.3
 */