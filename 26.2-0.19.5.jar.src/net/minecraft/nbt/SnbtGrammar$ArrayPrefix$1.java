/*     */ package net.minecraft.nbt;
/*     */ 
/*     */ import com.mojang.serialization.DynamicOps;
/*     */ import it.unimi.dsi.fastutil.bytes.ByteArrayList;
/*     */ import java.nio.ByteBuffer;
/*     */ import java.util.List;
/*     */ import net.minecraft.util.parsing.packrat.ParseState;
/*     */ 
/*     */ enum null {
/* 146 */   private static final ByteBuffer EMPTY_BUFFER = ByteBuffer.wrap(new byte[0]);
/*     */   
/*     */   public <T> T create(DynamicOps<T> ops) {
/* 150 */     return (T)ops.createByteList(EMPTY_BUFFER);
/*     */   }
/*     */   
/*     */   public <T> T create(DynamicOps<T> ops, List<SnbtGrammar.IntegerLiteral> entries, ParseState<?> state) {
/* 155 */     ByteArrayList byteArrayList = new ByteArrayList();
/* 156 */     for (SnbtGrammar.IntegerLiteral entry : entries) {
/* 157 */       Number parsedNumber = buildNumber(entry, state);
/* 158 */       if (parsedNumber == null)
/* 159 */         return null; 
/* 161 */       byteArrayList.add(parsedNumber.byteValue());
/*     */     } 
/* 163 */     return (T)ops.createByteList(ByteBuffer.wrap(byteArrayList.toByteArray()));
/*     */   }
/*     */ }


/* Location:              C:\Users\karol\AppData\Roaming\ModrinthApp\meta\versions\26.2-0.19.5\26.2-0.19.5.jar!\net\minecraft\nbt\SnbtGrammar$ArrayPrefix$1.class
 * Java compiler version: 25 (69.0)
 * JD-Core Version:       1.1.3
 */