/*     */ package net.minecraft.nbt;
/*     */ 
/*     */ import com.mojang.serialization.DynamicOps;
/*     */ import java.util.List;
/*     */ import java.util.stream.IntStream;
/*     */ import net.minecraft.util.parsing.packrat.ParseState;
/*     */ 
/*     */ enum null {
/*     */   public <T> T create(DynamicOps<T> ops) {
/* 169 */     return (T)ops.createIntList(IntStream.empty());
/*     */   }
/*     */   
/*     */   public <T> T create(DynamicOps<T> ops, List<SnbtGrammar.IntegerLiteral> entries, ParseState<?> state) {
/* 174 */     IntStream.Builder result = IntStream.builder();
/* 175 */     for (SnbtGrammar.IntegerLiteral entry : entries) {
/* 176 */       Number parsedNumber = buildNumber(entry, state);
/* 177 */       if (parsedNumber == null)
/* 178 */         return null; 
/* 180 */       result.add(parsedNumber.intValue());
/*     */     } 
/* 182 */     return (T)ops.createIntList(result.build());
/*     */   }
/*     */ }


/* Location:              C:\Users\karol\AppData\Roaming\ModrinthApp\meta\versions\26.2-0.19.5\26.2-0.19.5.jar!\net\minecraft\nbt\SnbtGrammar$ArrayPrefix$2.class
 * Java compiler version: 25 (69.0)
 * JD-Core Version:       1.1.3
 */