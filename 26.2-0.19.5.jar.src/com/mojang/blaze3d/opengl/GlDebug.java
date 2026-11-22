/*     */ package com.mojang.blaze3d.opengl;
/*     */ 
/*     */ import com.google.common.collect.EvictingQueue;
/*     */ import com.google.common.collect.ImmutableList;
/*     */ import com.google.common.collect.Lists;
/*     */ import com.mojang.logging.LogUtils;
/*     */ import java.util.HexFormat;
/*     */ import java.util.List;
/*     */ import java.util.Objects;
/*     */ import java.util.Queue;
/*     */ import java.util.Set;
/*     */ import org.lwjgl.opengl.ARBDebugOutput;
/*     */ import org.lwjgl.opengl.GL;
/*     */ import org.lwjgl.opengl.GL33C;
/*     */ import org.lwjgl.opengl.GLCapabilities;
/*     */ import org.lwjgl.opengl.GLDebugMessageARBCallback;
/*     */ import org.lwjgl.opengl.GLDebugMessageARBCallbackI;
/*     */ import org.lwjgl.opengl.GLDebugMessageCallback;
/*     */ import org.lwjgl.opengl.GLDebugMessageCallbackI;
/*     */ import org.lwjgl.opengl.KHRDebug;
/*     */ import org.slf4j.Logger;
/*     */ 
/*     */ public class GlDebug {
/*  23 */   private static final Logger LOGGER = LogUtils.getLogger();
/*     */   
/*     */   private static final int CIRCULAR_LOG_SIZE = 10;
/*     */   
/*     */   private static String printUnknownToken(int token) {
/*  26 */     return "Unknown (0x" + HexFormat.of().withUpperCase().toHexDigits(token) + ")";
/*     */   }
/*     */   
/*     */   public static String sourceToString(int source) {
/*  30 */     switch (source) {
/*     */       case 33350:
/*     */       
/*     */       case 33351:
/*     */       
/*     */       case 33352:
/*     */       
/*     */       case 33353:
/*     */       
/*     */       case 33354:
/*     */       
/*     */       case 33355:
/*     */       
/*     */     } 
/*  30 */     return 
/*     */       
/*  37 */       printUnknownToken(source);
/*     */   }
/*     */   
/*     */   public static String typeToString(int type) {
/*  42 */     switch (type) {
/*     */       case 33356:
/*     */       
/*     */       case 33357:
/*     */       
/*     */       case 33358:
/*     */       
/*     */       case 33359:
/*     */       
/*     */       case 33360:
/*     */       
/*     */       case 33361:
/*     */       
/*     */       case 33384:
/*     */       
/*     */     } 
/*  42 */     return 
/*     */       
/*  50 */       printUnknownToken(type);
/*     */   }
/*     */   
/*     */   public static String severityToString(int severity) {
/*  55 */     switch (severity) {
/*     */       case 37190:
/*     */       
/*     */       case 37191:
/*     */       
/*     */       case 37192:
/*     */       
/*     */       case 33387:
/*     */       
/*     */     } 
/*  55 */     return 
/*     */       
/*  60 */       printUnknownToken(severity);
/*     */   }
/*     */   
/*  65 */   private final Queue<LogEntry> MESSAGE_BUFFER = (Queue<LogEntry>)EvictingQueue.create(10);
/*     */   
/*     */   private volatile LogEntry lastEntry;
/*     */   
/*     */   private void printDebugLog(int source, int type, int id, int severity, int length, long message, long userParam) {
/*     */     LogEntry entry;
/*  69 */     String msg = GLDebugMessageCallback.getMessage(length, message);
/*  72 */     synchronized (this.MESSAGE_BUFFER) {
/*  73 */       entry = this.lastEntry;
/*  74 */       if (entry == null || !entry.isSame(source, type, id, severity, msg)) {
/*  75 */         entry = new LogEntry(source, type, id, severity, msg);
/*  76 */         this.MESSAGE_BUFFER.add(entry);
/*  77 */         this.lastEntry = entry;
/*     */       } else {
/*  79 */         entry.count++;
/*     */       } 
/*     */     } 
/*  83 */     LOGGER.info("OpenGL debug message: {}", entry);
/*     */   }
/*     */   
/*     */   public List<String> getLastOpenGlDebugMessages() {
/*  87 */     synchronized (this.MESSAGE_BUFFER) {
/*  88 */       List<String> result = Lists.newArrayListWithCapacity(this.MESSAGE_BUFFER.size());
/*  89 */       for (LogEntry e : this.MESSAGE_BUFFER)
/*  90 */         result.add(String.valueOf(e) + " x " + String.valueOf(e)); 
/*  92 */       return result;
/*     */     } 
/*     */   }
/*     */   
/*  96 */   private static final List<Integer> DEBUG_LEVELS = (List<Integer>)ImmutableList.of(Integer.valueOf(37190), Integer.valueOf(37191), Integer.valueOf(37192), Integer.valueOf(33387));
/*     */   
/*  97 */   private static final List<Integer> DEBUG_LEVELS_ARB = (List<Integer>)ImmutableList.of(Integer.valueOf(37190), Integer.valueOf(37191), Integer.valueOf(37192));
/*     */   
/*     */   public static GlDebug enableDebugCallback(int verbosity, boolean debugSynchronousGlLogs, Set<String> enabledExtensions) {
/* 100 */     if (verbosity <= 0)
/* 101 */       return null; 
/* 104 */     GLCapabilities caps = GL.getCapabilities();
/* 105 */     if (caps.GL_KHR_debug && GlDevice.USE_GL_KHR_debug) {
/* 106 */       GlDebug debug = new GlDebug();
/* 107 */       enabledExtensions.add("GL_KHR_debug");
/* 108 */       GL33C.glEnable(37600);
/* 109 */       if (debugSynchronousGlLogs)
/* 110 */         GL33C.glEnable(33346); 
/* 112 */       for (int i = 0; i < DEBUG_LEVELS.size(); i++) {
/* 113 */         boolean isEnabled = (i < verbosity);
/* 114 */         KHRDebug.glDebugMessageControl(4352, 4352, ((Integer)DEBUG_LEVELS.get(i)).intValue(), (int[])null, isEnabled);
/*     */       } 
/* 116 */       Objects.requireNonNull(debug);
/* 116 */       KHRDebug.glDebugMessageCallback((GLDebugMessageCallbackI)GLDebugMessageCallback.create(debug::printDebugLog), 0L);
/* 117 */       return debug;
/*     */     } 
/* 118 */     if (caps.GL_ARB_debug_output && GlDevice.USE_GL_ARB_debug_output) {
/* 119 */       GlDebug debug = new GlDebug();
/* 120 */       enabledExtensions.add("GL_ARB_debug_output");
/* 121 */       if (debugSynchronousGlLogs)
/* 122 */         GL33C.glEnable(33346); 
/* 124 */       for (int i = 0; i < DEBUG_LEVELS_ARB.size(); i++) {
/* 125 */         boolean isEnabled = (i < verbosity);
/* 126 */         ARBDebugOutput.glDebugMessageControlARB(4352, 4352, ((Integer)DEBUG_LEVELS_ARB.get(i)).intValue(), (int[])null, isEnabled);
/*     */       } 
/* 128 */       Objects.requireNonNull(debug);
/* 128 */       ARBDebugOutput.glDebugMessageCallbackARB((GLDebugMessageARBCallbackI)GLDebugMessageARBCallback.create(debug::printDebugLog), 0L);
/* 129 */       return debug;
/*     */     } 
/* 131 */     return null;
/*     */   }
/*     */   
/*     */   private static class LogEntry {
/*     */     private final int id;
/*     */     
/*     */     private final int source;
/*     */     
/*     */     private final int type;
/*     */     
/*     */     private final int severity;
/*     */     
/*     */     private final String message;
/*     */     
/* 141 */     private int count = 1;
/*     */     
/*     */     private LogEntry(int source, int type, int id, int severity, String message) {
/* 144 */       this.id = id;
/* 145 */       this.source = source;
/* 146 */       this.type = type;
/* 147 */       this.severity = severity;
/* 148 */       this.message = message;
/*     */     }
/*     */     
/*     */     private boolean isSame(int source, int type, int id, int severity, String message) {
/* 152 */       return (type == this.type && source == this.source && id == this.id && severity == this.severity && message
/*     */         
/* 156 */         .equals(this.message));
/*     */     }
/*     */     
/*     */     public String toString() {
/* 161 */       return "id=" + this.id + ", source=" + 
/* 162 */         GlDebug.sourceToString(this.source) + ", type=" + 
/* 163 */         GlDebug.typeToString(this.type) + ", severity=" + 
/* 164 */         GlDebug.severityToString(this.severity) + ", message='" + this.message + "'";
/*     */     }
/*     */   }
/*     */ }


/* Location:              C:\Users\karol\AppData\Roaming\ModrinthApp\meta\versions\26.2-0.19.5\26.2-0.19.5.jar!\com\mojang\blaze3d\opengl\GlDebug.class
 * Java compiler version: 25 (69.0)
 * JD-Core Version:       1.1.3
 */