/*     */ package net.minecraft.world.level.storage;
/*     */ 
/*     */ import java.nio.file.Path;
/*     */ import java.time.ZonedDateTime;
/*     */ 
/*     */ public final class LevelDirectory extends Record {
/*     */   private final Path path;
/*     */   
/*     */   public LevelDirectory(Path path) {
/* 698 */     this.path = path;
/*     */   }
/*     */   
/*     */   public final String toString() {
/*     */     // Byte code:
/*     */     //   0: aload_0
/*     */     //   1: <illegal opcode> toString : (Lnet/minecraft/world/level/storage/LevelStorageSource$LevelDirectory;)Ljava/lang/String;
/*     */     //   6: areturn
/*     */     // Line number table:
/*     */     //   Java source line number -> byte code offset
/*     */     //   #698	-> 0
/*     */     // Local variable table:
/*     */     //   start	length	slot	name	descriptor
/*     */     //   0	7	0	this	Lnet/minecraft/world/level/storage/LevelStorageSource$LevelDirectory;
/*     */   }
/*     */   
/*     */   public final int hashCode() {
/*     */     // Byte code:
/*     */     //   0: aload_0
/*     */     //   1: <illegal opcode> hashCode : (Lnet/minecraft/world/level/storage/LevelStorageSource$LevelDirectory;)I
/*     */     //   6: ireturn
/*     */     // Line number table:
/*     */     //   Java source line number -> byte code offset
/*     */     //   #698	-> 0
/*     */     // Local variable table:
/*     */     //   start	length	slot	name	descriptor
/*     */     //   0	7	0	this	Lnet/minecraft/world/level/storage/LevelStorageSource$LevelDirectory;
/*     */   }
/*     */   
/*     */   public final boolean equals(Object o) {
/*     */     // Byte code:
/*     */     //   0: aload_0
/*     */     //   1: aload_1
/*     */     //   2: <illegal opcode> equals : (Lnet/minecraft/world/level/storage/LevelStorageSource$LevelDirectory;Ljava/lang/Object;)Z
/*     */     //   7: ireturn
/*     */     // Line number table:
/*     */     //   Java source line number -> byte code offset
/*     */     //   #698	-> 0
/*     */     // Local variable table:
/*     */     //   start	length	slot	name	descriptor
/*     */     //   0	8	0	this	Lnet/minecraft/world/level/storage/LevelStorageSource$LevelDirectory;
/*     */     //   0	8	1	o	Ljava/lang/Object;
/*     */   }
/*     */   
/*     */   public Path path() {
/* 698 */     return this.path;
/*     */   }
/*     */   
/*     */   public String directoryName() {
/* 700 */     return this.path.getFileName().toString();
/*     */   }
/*     */   
/*     */   public Path dataFile() {
/* 704 */     return resourcePath(LevelResource.LEVEL_DATA_FILE);
/*     */   }
/*     */   
/*     */   public Path oldDataFile() {
/* 708 */     return resourcePath(LevelResource.OLD_LEVEL_DATA_FILE);
/*     */   }
/*     */   
/*     */   public Path corruptedDataFile(ZonedDateTime time) {
/* 712 */     return this.path.resolve(LevelResource.LEVEL_DATA_FILE.id() + "_corrupted_" + LevelResource.LEVEL_DATA_FILE.id());
/*     */   }
/*     */   
/*     */   public Path rawDataFile(ZonedDateTime time) {
/* 716 */     return this.path.resolve(LevelResource.LEVEL_DATA_FILE.id() + "_raw_" + LevelResource.LEVEL_DATA_FILE.id());
/*     */   }
/*     */   
/*     */   public Path iconFile() {
/* 720 */     return resourcePath(LevelResource.ICON_FILE);
/*     */   }
/*     */   
/*     */   public Path lockFile() {
/* 724 */     return resourcePath(LevelResource.LOCK_FILE);
/*     */   }
/*     */   
/*     */   public Path resourcePath(LevelResource resource) {
/* 728 */     return this.path.resolve(resource.id());
/*     */   }
/*     */ }


/* Location:              C:\Users\karol\AppData\Roaming\ModrinthApp\meta\versions\26.2-0.19.5\26.2-0.19.5.jar!\net\minecraft\world\level\storage\LevelStorageSource$LevelDirectory.class
 * Java compiler version: 25 (69.0)
 * JD-Core Version:       1.1.3
 */