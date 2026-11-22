/*     */ package net.minecraft.nbt;
/*     */ 
/*     */ import com.mojang.serialization.DynamicOps;
/*     */ import java.util.List;
/*     */ import java.util.stream.LongStream;
/*     */ import net.minecraft.util.parsing.packrat.ParseState;
/*     */ 
/*     */ enum null {
/*     */   public <T> T create(DynamicOps<T> ops) {
/* 188 */     return (T)ops.createLongList(LongStream.empty());
/*     */   }
/*     */   
/*     */   public <T> T create(DynamicOps<T> ops, List<SnbtGrammar.IntegerLiteral> entries, ParseState<?> state) {
/* 193 */     LongStream.Builder result = LongStream.builder();
/* 194 */     for (SnbtGrammar.IntegerLiteral entry : entries) {
/* 195 */       Number parsedNumber = buildNumber(entry, state);
/* 196 */       if (parsedNumber == null)
/* 197 */         return null; 
/* 199 */       result.add(parsedNumber.longValue());
/*     */     } 
/* 201 */     return (T)ops.createLongList(result.build());
/*     */   }
/*     */ }


/* Location:              C:\Users\karol\AppData\Roaming\ModrinthApp\meta\versions\26.2-0.19.5\26.2-0.19.5.jar!\net\minecraft\nbt\SnbtGrammar$ArrayPrefix$3.class
 * Java compiler version: 25 (69.0)
 * JD-Core Version:       1.1.3
 */