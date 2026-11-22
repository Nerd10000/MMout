/*     */ package net.minecraft.server.packs.repository;
/*     */ 
/*     */ import java.util.List;
/*     */ import java.util.function.Function;
/*     */ import net.minecraft.server.packs.PackSelectionConfig;
/*     */ 
/*     */ public enum Position {
/* 152 */   TOP, BOTTOM;
/*     */   
/*     */   public <T> int insert(List<T> list, T value, Function<T, PackSelectionConfig> converter, boolean reverse) {
/* 157 */     Position self = reverse ? opposite() : this;
/* 158 */     if (self == BOTTOM) {
/* 159 */       int i = 0;
/* 160 */       while (i < list.size()) {
/* 161 */         PackSelectionConfig pack = converter.apply(list.get(i));
/* 162 */         if (pack.fixedPosition() && pack.defaultPosition() == this)
/* 163 */           i++; 
/*     */       } 
/* 168 */       list.add(i, value);
/* 169 */       return i;
/*     */     } 
/* 171 */     int index = list.size() - 1;
/* 172 */     while (index >= 0) {
/* 173 */       PackSelectionConfig pack = converter.apply(list.get(index));
/* 174 */       if (pack.fixedPosition() && pack.defaultPosition() == this)
/* 175 */         index--; 
/*     */     } 
/* 180 */     list.add(index + 1, value);
/* 181 */     return index + 1;
/*     */   }
/*     */   
/*     */   public Position opposite() {
/* 186 */     return (this == TOP) ? BOTTOM : TOP;
/*     */   }
/*     */ }


/* Location:              C:\Users\karol\AppData\Roaming\ModrinthApp\meta\versions\26.2-0.19.5\26.2-0.19.5.jar!\net\minecraft\server\packs\repository\Pack$Position.class
 * Java compiler version: 25 (69.0)
 * JD-Core Version:       1.1.3
 */