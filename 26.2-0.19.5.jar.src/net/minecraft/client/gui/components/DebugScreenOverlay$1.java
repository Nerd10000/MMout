/*     */ package net.minecraft.client.gui.components;
/*     */ 
/*     */ import java.util.ArrayList;
/*     */ import java.util.Collection;
/*     */ import java.util.List;
/*     */ import java.util.Map;
/*     */ import net.minecraft.client.gui.components.debug.DebugScreenDisplayer;
/*     */ import net.minecraft.resources.Identifier;
/*     */ 
/*     */ class null implements DebugScreenDisplayer {
/*     */   null(DebugScreenOverlay this$0) {}
/*     */   
/*     */   public void addPriorityLine(String line) {
/* 126 */     if (leftLines.size() > rightLines.size()) {
/* 127 */       rightLines.add(line);
/*     */     } else {
/* 129 */       leftLines.add(line);
/*     */     } 
/*     */   }
/*     */   
/*     */   public void addLine(String line) {
/* 135 */     regularLines.add(line);
/*     */   }
/*     */   
/*     */   public void addToGroup(Identifier group, Collection<String> lines) {
/* 140 */     ((Collection<String>)groups.computeIfAbsent(group, k -> new ArrayList())).addAll(lines);
/*     */   }
/*     */   
/*     */   public void addToGroup(Identifier group, String lines) {
/* 145 */     ((Collection<String>)groups.computeIfAbsent(group, k -> new ArrayList())).add(lines);
/*     */   }
/*     */ }


/* Location:              C:\Users\karol\AppData\Roaming\ModrinthApp\meta\versions\26.2-0.19.5\26.2-0.19.5.jar!\net\minecraft\client\gui\components\DebugScreenOverlay$1.class
 * Java compiler version: 25 (69.0)
 * JD-Core Version:       1.1.3
 */