/*     */ package net.minecraft.util.eventlog;
/*     */ 
/*     */ import java.time.LocalDate;
/*     */ import java.time.format.DateTimeFormatter;
/*     */ 
/*     */ public final class FileId extends Record {
/*     */   private final LocalDate date;
/*     */   
/*     */   private final int index;
/*     */   
/*     */   public FileId(LocalDate date, int index) {
/* 220 */     this.date = date;
/* 220 */     this.index = index;
/*     */   }
/*     */   
/*     */   public final int hashCode() {
/*     */     // Byte code:
/*     */     //   0: aload_0
/*     */     //   1: <illegal opcode> hashCode : (Lnet/minecraft/util/eventlog/EventLogDirectory$FileId;)I
/*     */     //   6: ireturn
/*     */     // Line number table:
/*     */     //   Java source line number -> byte code offset
/*     */     //   #220	-> 0
/*     */     // Local variable table:
/*     */     //   start	length	slot	name	descriptor
/*     */     //   0	7	0	this	Lnet/minecraft/util/eventlog/EventLogDirectory$FileId;
/*     */   }
/*     */   
/*     */   public final boolean equals(Object o) {
/*     */     // Byte code:
/*     */     //   0: aload_0
/*     */     //   1: aload_1
/*     */     //   2: <illegal opcode> equals : (Lnet/minecraft/util/eventlog/EventLogDirectory$FileId;Ljava/lang/Object;)Z
/*     */     //   7: ireturn
/*     */     // Line number table:
/*     */     //   Java source line number -> byte code offset
/*     */     //   #220	-> 0
/*     */     // Local variable table:
/*     */     //   start	length	slot	name	descriptor
/*     */     //   0	8	0	this	Lnet/minecraft/util/eventlog/EventLogDirectory$FileId;
/*     */     //   0	8	1	o	Ljava/lang/Object;
/*     */   }
/*     */   
/*     */   public LocalDate date() {
/* 220 */     return this.date;
/*     */   }
/*     */   
/*     */   public int index() {
/* 220 */     return this.index;
/*     */   }
/*     */   
/* 221 */   private static final DateTimeFormatter DATE_FORMATTER = DateTimeFormatter.BASIC_ISO_DATE;
/*     */   
/*     */   public static FileId parse(String name) {
/* 224 */     int separator = name.indexOf("-");
/* 225 */     if (separator == -1)
/* 226 */       return null; 
/* 229 */     String date = name.substring(0, separator);
/* 230 */     String index = name.substring(separator + 1);
/*     */     try {
/* 233 */       return new FileId(
/* 234 */           LocalDate.parse(date, DATE_FORMATTER), 
/* 235 */           Integer.parseInt(index));
/* 237 */     } catch (NumberFormatException|java.time.format.DateTimeParseException e) {
/* 238 */       return null;
/*     */     } 
/*     */   }
/*     */   
/*     */   public String toString() {
/* 244 */     return DATE_FORMATTER.format(this.date) + "-" + DATE_FORMATTER.format(this.date);
/*     */   }
/*     */   
/*     */   public String toFileName(String extension) {
/* 248 */     return String.valueOf(this) + String.valueOf(this);
/*     */   }
/*     */ }


/* Location:              C:\Users\karol\AppData\Roaming\ModrinthApp\meta\versions\26.2-0.19.5\26.2-0.19.5.jar!\net\minecraf\\util\eventlog\EventLogDirectory$FileId.class
 * Java compiler version: 25 (69.0)
 * JD-Core Version:       1.1.3
 */