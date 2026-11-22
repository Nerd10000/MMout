/*    */ package net.minecraft.data.structures;
/*    */ 
/*    */ import com.google.common.hash.Hashing;
/*    */ import com.google.common.hash.HashingOutputStream;
/*    */ import com.mojang.logging.LogUtils;
/*    */ import java.io.ByteArrayOutputStream;
/*    */ import java.io.IOException;
/*    */ import java.io.InputStream;
/*    */ import java.nio.charset.StandardCharsets;
/*    */ import java.nio.file.Files;
/*    */ import java.nio.file.Path;
/*    */ import java.util.ArrayList;
/*    */ import java.util.Collection;
/*    */ import java.util.Iterator;
/*    */ import java.util.List;
/*    */ import java.util.concurrent.CompletableFuture;
/*    */ import java.util.concurrent.CompletionStage;
/*    */ import java.util.concurrent.Executor;
/*    */ import java.util.stream.Stream;
/*    */ import net.minecraft.data.CachedOutput;
/*    */ import net.minecraft.data.DataProvider;
/*    */ import net.minecraft.data.PackOutput;
/*    */ import net.minecraft.nbt.NbtAccounter;
/*    */ import net.minecraft.nbt.NbtIo;
/*    */ import net.minecraft.nbt.NbtUtils;
/*    */ import net.minecraft.util.FastBufferedInputStream;
/*    */ import net.minecraft.util.Util;
/*    */ import org.slf4j.Logger;
/*    */ 
/*    */ public class NbtToSnbt implements DataProvider {
/* 30 */   private static final Logger LOGGER = LogUtils.getLogger();
/*    */   
/*    */   private final Iterable<Path> inputFolders;
/*    */   
/*    */   private final PackOutput output;
/*    */   
/*    */   public NbtToSnbt(PackOutput output, Collection<Path> inputFolders) {
/* 36 */     this.inputFolders = inputFolders;
/* 37 */     this.output = output;
/*    */   }
/*    */   
/*    */   public CompletableFuture<?> run(CachedOutput cache) {
/* 42 */     Path output = this.output.getOutputFolder();
/* 44 */     List<CompletableFuture<?>> tasks = new ArrayList<>();
/* 46 */     for (Iterator<Path> iterator = this.inputFolders.iterator(); iterator.hasNext(); ) {
/* 46 */       Path input = iterator.next();
/* 47 */       tasks.add(CompletableFuture.supplyAsync(() -> {
/*    */               try {
/*    */                 Stream<Path> walk = Files.walk(input, new java.nio.file.FileVisitOption[0]);
/*    */                 try {
/*    */                   CompletableFuture<Void> completableFuture = CompletableFuture.allOf((CompletableFuture<?>[])walk.filter(()).map(()).toArray(()));
/*    */                   if (walk != null)
/*    */                     walk.close(); 
/*    */                   return completableFuture;
/* 48 */                 } catch (Throwable t$) {
/*    */                   if (walk != null)
/*    */                     try {
/*    */                       walk.close();
/* 48 */                     } catch (Throwable x2) {
/*    */                       t$.addSuppressed(x2);
/*    */                     }  
/*    */                   throw t$;
/*    */                 } 
/* 53 */               } catch (IOException e) {
/*    */                 LOGGER.error("Failed to read structure input directory", e);
/*    */                 return CompletableFuture.completedFuture(null);
/*    */               } 
/* 57 */             }Util.backgroundExecutor().forName("NbtToSnbt")).thenCompose(v -> v));
/*    */     } 
/* 60 */     return CompletableFuture.allOf((CompletableFuture<?>[])tasks.toArray(x$0 -> new CompletableFuture[x$0]));
/*    */   }
/*    */   
/*    */   public final String getName() {
/* 65 */     return "NBT -> SNBT";
/*    */   }
/*    */   
/*    */   private static String getName(Path root, Path path) {
/* 69 */     String name = root.relativize(path).toString().replaceAll("\\\\", "/");
/* 70 */     return name.substring(0, name.length() - ".nbt".length());
/*    */   }
/*    */   
/*    */   public static Path convertStructure(CachedOutput cache, Path path, String name, Path output) {
/*    */     try {
/* 77 */       InputStream rawInput = Files.newInputStream(path, new java.nio.file.OpenOption[0]);
/*    */       try {
/* 78 */         FastBufferedInputStream fastBufferedInputStream = new FastBufferedInputStream(rawInput);
/*    */         try {
/* 80 */           Path resultPath = output.resolve(name + ".snbt");
/* 81 */           writeSnbt(cache, resultPath, NbtUtils.structureToSnbt(NbtIo.readCompressed((InputStream)fastBufferedInputStream, NbtAccounter.unlimitedHeap())));
/* 82 */           LOGGER.info("Converted {} from NBT to SNBT", name);
/* 83 */           Path path1 = resultPath;
/* 84 */           fastBufferedInputStream.close();
/* 84 */           if (rawInput != null)
/* 84 */             rawInput.close(); 
/*    */           return path1;
/*    */         } catch (Throwable throwable) {
/*    */           try {
/*    */             fastBufferedInputStream.close();
/*    */           } catch (Throwable throwable1) {
/*    */             throwable.addSuppressed(throwable1);
/*    */           } 
/*    */           throw throwable;
/*    */         } 
/*    */       } catch (Throwable throwable) {
/*    */         if (rawInput != null)
/*    */           try {
/*    */             rawInput.close();
/*    */           } catch (Throwable throwable1) {
/*    */             throwable.addSuppressed(throwable1);
/*    */           }  
/*    */         throw throwable;
/*    */       } 
/* 84 */     } catch (IOException e) {
/* 85 */       LOGGER.error("Couldn't convert {} from NBT to SNBT at {}", new Object[] { name, path, e });
/* 86 */       return null;
/*    */     } 
/*    */   }
/*    */   
/*    */   public static void writeSnbt(CachedOutput cache, Path destination, String text) throws IOException {
/* 91 */     ByteArrayOutputStream bytes = new ByteArrayOutputStream();
/* 92 */     HashingOutputStream hashedBytes = new HashingOutputStream(Hashing.sha1(), bytes);
/* 93 */     hashedBytes.write(text.getBytes(StandardCharsets.UTF_8));
/* 94 */     hashedBytes.write(10);
/* 95 */     cache.writeIfNeeded(destination, bytes.toByteArray(), hashedBytes.hash());
/*    */   }
/*    */ }


/* Location:              C:\Users\karol\AppData\Roaming\ModrinthApp\meta\versions\26.2-0.19.5\26.2-0.19.5.jar!\net\minecraft\data\structures\NbtToSnbt.class
 * Java compiler version: 25 (69.0)
 * JD-Core Version:       1.1.3
 */