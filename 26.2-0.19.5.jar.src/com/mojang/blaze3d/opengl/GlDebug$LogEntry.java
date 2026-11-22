/*     */ package com.mojang.blaze3d.opengl;
/*     */ 
/*     */ class LogEntry {
/*     */   private final int id;
/*     */   
/*     */   private final int source;
/*     */   
/*     */   private final int type;
/*     */   
/*     */   private final int severity;
/*     */   
/*     */   private final String message;
/*     */   
/* 141 */   private int count = 1;
/*     */   
/*     */   private LogEntry(int source, int type, int id, int severity, String message) {
/* 144 */     this.id = id;
/* 145 */     this.source = source;
/* 146 */     this.type = type;
/* 147 */     this.severity = severity;
/* 148 */     this.message = message;
/*     */   }
/*     */   
/*     */   private boolean isSame(int source, int type, int id, int severity, String message) {
/* 152 */     return (type == this.type && source == this.source && id == this.id && severity == this.severity && message
/*     */       
/* 156 */       .equals(this.message));
/*     */   }
/*     */   
/*     */   public String toString() {
/* 161 */     return "id=" + this.id + ", source=" + 
/* 162 */       GlDebug.sourceToString(this.source) + ", type=" + 
/* 163 */       GlDebug.typeToString(this.type) + ", severity=" + 
/* 164 */       GlDebug.severityToString(this.severity) + ", message='" + this.message + "'";
/*     */   }
/*     */ }


/* Location:              C:\Users\karol\AppData\Roaming\ModrinthApp\meta\versions\26.2-0.19.5\26.2-0.19.5.jar!\com\mojang\blaze3d\opengl\GlDebug$LogEntry.class
 * Java compiler version: 25 (69.0)
 * JD-Core Version:       1.1.3
 */