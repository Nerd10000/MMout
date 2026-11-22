/*     */ package net.minecraft;
/*     */ 
/*     */ import com.google.common.collect.Lists;
/*     */ import com.mojang.logging.LogUtils;
/*     */ import java.io.PrintWriter;
/*     */ import java.io.StringWriter;
/*     */ import java.io.Writer;
/*     */ import java.nio.charset.StandardCharsets;
/*     */ import java.nio.file.Files;
/*     */ import java.nio.file.Path;
/*     */ import java.time.ZonedDateTime;
/*     */ import java.time.format.DateTimeFormatter;
/*     */ import java.util.List;
/*     */ import java.util.Locale;
/*     */ import net.minecraft.util.FileUtil;
/*     */ import net.minecraft.util.MemoryReserve;
/*     */ import org.apache.commons.io.IOUtils;
/*     */ import org.apache.commons.lang3.ArrayUtils;
/*     */ import org.slf4j.Logger;
/*     */ 
/*     */ public class CrashReport {
/*  25 */   private static final Logger LOGGER = LogUtils.getLogger();
/*     */   
/*  26 */   private static final DateTimeFormatter DATE_TIME_FORMATTER = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss", Locale.ROOT);
/*     */   
/*     */   private final String title;
/*     */   
/*     */   private final Throwable exception;
/*     */   
/*  30 */   private final List<CrashReportCategory> details = Lists.newArrayList();
/*     */   
/*     */   private Path saveFile;
/*     */   
/*     */   private boolean trackingStackTrace = true;
/*     */   
/*  33 */   private StackTraceElement[] uncategorizedStackTrace = new StackTraceElement[0];
/*     */   
/*  35 */   private final SystemReport systemReport = new SystemReport();
/*     */   
/*     */   public CrashReport(String title, Throwable t) {
/*  38 */     this.title = title;
/*  39 */     this.exception = t;
/*     */   }
/*     */   
/*     */   public String getTitle() {
/*  43 */     return this.title;
/*     */   }
/*     */   
/*     */   public Throwable getException() {
/*  47 */     return this.exception;
/*     */   }
/*     */   
/*     */   public String getDetails() {
/*  51 */     StringBuilder builder = new StringBuilder();
/*  53 */     getDetails(builder);
/*  55 */     return builder.toString();
/*     */   }
/*     */   
/*     */   public void getDetails(StringBuilder builder) {
/*  59 */     if (this.uncategorizedStackTrace.length <= 0 && !this.details.isEmpty())
/*  60 */       this.uncategorizedStackTrace = (StackTraceElement[])ArrayUtils.subarray((Object[])((CrashReportCategory)this.details.get(0)).getStacktrace(), 0, 1); 
/*  63 */     if (this.uncategorizedStackTrace.length > 0) {
/*  64 */       builder.append("-- Head --\n");
/*  65 */       builder.append("Thread: ").append(Thread.currentThread().getName()).append("\n");
/*  66 */       builder.append("Stacktrace:\n");
/*  68 */       for (StackTraceElement element : this.uncategorizedStackTrace) {
/*  69 */         builder.append("\t").append("at ").append(element);
/*  70 */         builder.append("\n");
/*     */       } 
/*  72 */       builder.append("\n");
/*     */     } 
/*  75 */     for (CrashReportCategory entry : this.details) {
/*  76 */       entry.getDetails(builder);
/*  77 */       builder.append("\n\n");
/*     */     } 
/*  80 */     this.systemReport.appendToCrashReportString(builder);
/*     */   }
/*     */   
/*     */   public String getExceptionMessage() {
/*  84 */     StringWriter writer = null;
/*  85 */     PrintWriter printWriter = null;
/*  86 */     Throwable exception = this.exception;
/*  88 */     if (exception.getMessage() == null)
/*  89 */       exception = replaceMessage(exception, this.title); 
/*     */     try {
/*  93 */       writer = new StringWriter();
/*  94 */       printWriter = new PrintWriter(writer);
/*  95 */       exception.printStackTrace(printWriter);
/*  96 */       return writer.toString();
/*     */     } finally {
/*  98 */       IOUtils.closeQuietly(writer);
/*  99 */       IOUtils.closeQuietly(printWriter);
/*     */     } 
/*     */   }
/*     */   
/*     */   private static Throwable copyProperties(Throwable original, Throwable copy) {
/*     */     try {
/* 105 */       Throwable cause = original.getCause();
/* 106 */       if (cause != null)
/* 107 */         copy.initCause(cause); 
/* 109 */       Throwable[] suppressed = original.getSuppressed();
/* 110 */       for (Throwable throwable : suppressed)
/* 111 */         copy.addSuppressed(throwable); 
/* 113 */       copy.setStackTrace(original.getStackTrace());
/* 114 */       return copy;
/* 115 */     } catch (Throwable e) {
/* 117 */       return original;
/*     */     } 
/*     */   }
/*     */   
/*     */   private static Throwable replaceMessage(Throwable original, String title) {
/*     */     // Byte code:
/*     */     //   0: aload_0
/*     */     //   1: dup
/*     */     //   2: invokestatic requireNonNull : (Ljava/lang/Object;)Ljava/lang/Object;
/*     */     //   5: pop
/*     */     //   6: astore_2
/*     */     //   7: iconst_0
/*     */     //   8: istore_3
/*     */     //   9: aload_2
/*     */     //   10: iload_3
/*     */     //   11: <illegal opcode> typeSwitch : (Ljava/lang/Throwable;I)I
/*     */     //   16: tableswitch default -> 89, 0 -> 44, 1 -> 59, 2 -> 74
/*     */     //   44: aload_0
/*     */     //   45: new java/lang/NullPointerException
/*     */     //   48: dup
/*     */     //   49: aload_1
/*     */     //   50: invokespecial <init> : (Ljava/lang/String;)V
/*     */     //   53: invokestatic copyProperties : (Ljava/lang/Throwable;Ljava/lang/Throwable;)Ljava/lang/Throwable;
/*     */     //   56: goto -> 90
/*     */     //   59: aload_0
/*     */     //   60: new java/lang/StackOverflowError
/*     */     //   63: dup
/*     */     //   64: aload_1
/*     */     //   65: invokespecial <init> : (Ljava/lang/String;)V
/*     */     //   68: invokestatic copyProperties : (Ljava/lang/Throwable;Ljava/lang/Throwable;)Ljava/lang/Throwable;
/*     */     //   71: goto -> 90
/*     */     //   74: aload_0
/*     */     //   75: new java/lang/OutOfMemoryError
/*     */     //   78: dup
/*     */     //   79: aload_1
/*     */     //   80: invokespecial <init> : (Ljava/lang/String;)V
/*     */     //   83: invokestatic copyProperties : (Ljava/lang/Throwable;Ljava/lang/Throwable;)Ljava/lang/Throwable;
/*     */     //   86: goto -> 90
/*     */     //   89: aload_0
/*     */     //   90: areturn
/*     */     // Line number table:
/*     */     //   Java source line number -> byte code offset
/*     */     //   #123	-> 0
/*     */     //   #124	-> 44
/*     */     //   #125	-> 59
/*     */     //   #126	-> 74
/*     */     //   #127	-> 89
/*     */     //   #123	-> 90
/*     */     // Local variable table:
/*     */     //   start	length	slot	name	descriptor
/*     */     //   0	91	0	original	Ljava/lang/Throwable;
/*     */     //   0	91	1	title	Ljava/lang/String;
/*     */   }
/*     */   
/*     */   public String getFriendlyReport(ReportType reportType, List<String> extraComments) {
/* 132 */     StringBuilder builder = new StringBuilder();
/* 134 */     reportType.appendHeader(builder, extraComments);
/* 136 */     builder.append("Time: ");
/* 137 */     builder.append(DATE_TIME_FORMATTER.format(ZonedDateTime.now()));
/* 138 */     builder.append("\n");
/* 140 */     builder.append("Description: ");
/* 141 */     builder.append(this.title);
/* 142 */     builder.append("\n\n");
/* 144 */     builder.append(getExceptionMessage());
/* 145 */     builder.append("\n\nA detailed walkthrough of the error, its code path and all known details is as follows:\n");
/* 147 */     for (int i = 0; i < 87; i++)
/* 148 */       builder.append("-"); 
/* 150 */     builder.append("\n\n");
/* 151 */     getDetails(builder);
/* 153 */     return builder.toString();
/*     */   }
/*     */   
/*     */   public String getFriendlyReport(ReportType reportType) {
/* 157 */     return getFriendlyReport(reportType, List.of());
/*     */   }
/*     */   
/*     */   public Path getSaveFile() {
/* 161 */     return this.saveFile;
/*     */   }
/*     */   
/*     */   public boolean saveToFile(Path saveFile, ReportType reportType, List<String> extraComments) {
/* 165 */     if (this.saveFile != null)
/* 166 */       return false; 
/*     */     try {
/* 170 */       if (saveFile.getParent() != null)
/* 171 */         FileUtil.createDirectoriesSafe(saveFile.getParent()); 
/* 174 */       Writer writer = Files.newBufferedWriter(saveFile, StandardCharsets.UTF_8, new java.nio.file.OpenOption[0]);
/*     */       try {
/* 175 */         writer.write(getFriendlyReport(reportType, extraComments));
/* 176 */         if (writer != null)
/* 176 */           writer.close(); 
/*     */       } catch (Throwable throwable) {
/*     */         if (writer != null)
/*     */           try {
/*     */             writer.close();
/*     */           } catch (Throwable throwable1) {
/*     */             throwable.addSuppressed(throwable1);
/*     */           }  
/*     */         throw throwable;
/*     */       } 
/* 178 */       this.saveFile = saveFile;
/* 179 */       return true;
/* 180 */     } catch (Throwable t) {
/* 181 */       LOGGER.error("Could not save crash report to {}", saveFile, t);
/* 182 */       return false;
/*     */     } 
/*     */   }
/*     */   
/*     */   public boolean saveToFile(Path file, ReportType reportType) {
/* 187 */     return saveToFile(file, reportType, List.of());
/*     */   }
/*     */   
/*     */   public SystemReport getSystemReport() {
/* 191 */     return this.systemReport;
/*     */   }
/*     */   
/*     */   public CrashReportCategory addCategory(String name) {
/* 195 */     return addCategory(name, 1);
/*     */   }
/*     */   
/*     */   public CrashReportCategory addCategory(String name, int nestedOffset) {
/* 199 */     CrashReportCategory category = new CrashReportCategory(name);
/* 201 */     if (this.trackingStackTrace) {
/* 202 */       int size = category.fillInStackTrace(nestedOffset);
/* 203 */       StackTraceElement[] fullTrace = this.exception.getStackTrace();
/* 204 */       StackTraceElement source = null;
/* 205 */       StackTraceElement next = null;
/* 207 */       int traceIndex = fullTrace.length - size;
/* 208 */       if (traceIndex < 0)
/* 209 */         LOGGER.error("Negative index in crash report handler ({}/{})", Integer.valueOf(fullTrace.length), Integer.valueOf(size)); 
/* 212 */       if (0 <= traceIndex && traceIndex < fullTrace.length) {
/* 213 */         source = fullTrace[traceIndex];
/* 215 */         if (fullTrace.length + 1 - size < fullTrace.length)
/* 216 */           next = fullTrace[fullTrace.length + 1 - size]; 
/*     */       } 
/* 220 */       this.trackingStackTrace = category.validateStackTrace(source, next);
/* 222 */       if (fullTrace.length >= size && 0 <= traceIndex && traceIndex < fullTrace.length) {
/* 223 */         this.uncategorizedStackTrace = new StackTraceElement[traceIndex];
/* 224 */         System.arraycopy(fullTrace, 0, this.uncategorizedStackTrace, 0, this.uncategorizedStackTrace.length);
/*     */       } else {
/* 226 */         this.trackingStackTrace = false;
/*     */       } 
/*     */     } 
/* 230 */     this.details.add(category);
/* 231 */     return category;
/*     */   }
/*     */   
/*     */   public static CrashReport forThrowable(Throwable t, String title) {
/*     */     CrashReport report;
/* 237 */     while (t instanceof java.util.concurrent.CompletionException && t.getCause() != null)
/* 238 */       t = t.getCause(); 
/* 241 */     if (t instanceof ReportedException) {
/* 241 */       ReportedException reportedException = (ReportedException)t;
/* 242 */       report = reportedException.getReport();
/*     */     } else {
/* 244 */       report = new CrashReport(title, t);
/*     */     } 
/* 247 */     return report;
/*     */   }
/*     */   
/*     */   public static void preload() {
/* 251 */     MemoryReserve.allocate();
/* 252 */     (new CrashReport("Don't panic!", new Throwable())).getFriendlyReport(ReportType.CRASH);
/*     */   }
/*     */ }


/* Location:              C:\Users\karol\AppData\Roaming\ModrinthApp\meta\versions\26.2-0.19.5\26.2-0.19.5.jar!\net\minecraft\CrashReport.class
 * Java compiler version: 25 (69.0)
 * JD-Core Version:       1.1.3
 */