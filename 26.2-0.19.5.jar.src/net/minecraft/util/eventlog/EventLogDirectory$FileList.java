/*     */ package net.minecraft.util.eventlog;
/*     */ 
/*     */ import java.io.IOException;
/*     */ import java.nio.file.Files;
/*     */ import java.time.LocalDate;
/*     */ import java.util.ArrayList;
/*     */ import java.util.Iterator;
/*     */ import java.util.List;
/*     */ import java.util.ListIterator;
/*     */ import java.util.Set;
/*     */ import java.util.stream.Collectors;
/*     */ import java.util.stream.Stream;
/*     */ 
/*     */ public class FileList implements Iterable<EventLogDirectory.File> {
/*     */   private final List<EventLogDirectory.File> files;
/*     */   
/*     */   private FileList(List<EventLogDirectory.File> files) {
/* 130 */     this.files = new ArrayList<>(files);
/*     */   }
/*     */   
/*     */   public FileList prune(LocalDate date, int expiryDays) {
/* 134 */     this.files.removeIf(file -> {
/*     */           EventLogDirectory.FileId id = file.id();
/*     */           LocalDate expiresAt = id.date().plusDays(expiryDays);
/*     */           if (!date.isBefore(expiresAt))
/*     */             try {
/*     */               Files.delete(file.path());
/*     */               return true;
/* 141 */             } catch (IOException e) {
/*     */               EventLogDirectory.LOGGER.warn("Failed to delete expired event log file: {}", file.path(), e);
/*     */             }  
/*     */           return false;
/*     */         });
/* 147 */     return this;
/*     */   }
/*     */   
/*     */   public FileList compressAll() {
/* 151 */     ListIterator<EventLogDirectory.File> iterator = this.files.listIterator();
/* 152 */     while (iterator.hasNext()) {
/* 153 */       EventLogDirectory.File file = iterator.next();
/*     */       try {
/* 155 */         iterator.set(file.compress());
/* 156 */       } catch (IOException e) {
/* 157 */         EventLogDirectory.LOGGER.warn("Failed to compress event log file: {}", file.path(), e);
/*     */       } 
/*     */     } 
/* 160 */     return this;
/*     */   }
/*     */   
/*     */   public Iterator<EventLogDirectory.File> iterator() {
/* 165 */     return this.files.iterator();
/*     */   }
/*     */   
/*     */   public Stream<EventLogDirectory.File> stream() {
/* 169 */     return this.files.stream();
/*     */   }
/*     */   
/*     */   public Set<EventLogDirectory.FileId> ids() {
/* 173 */     return (Set<EventLogDirectory.FileId>)this.files.stream().map(EventLogDirectory.File::id).collect(Collectors.toSet());
/*     */   }
/*     */ }


/* Location:              C:\Users\karol\AppData\Roaming\ModrinthApp\meta\versions\26.2-0.19.5\26.2-0.19.5.jar!\net\minecraf\\util\eventlog\EventLogDirectory$FileList.class
 * Java compiler version: 25 (69.0)
 * JD-Core Version:       1.1.3
 */