/*     */ package net.minecraft.nbt;
/*     */ 
/*     */ import com.mojang.serialization.DynamicOps;
/*     */ import com.mojang.serialization.JavaOps;
/*     */ import it.unimi.dsi.fastutil.bytes.ByteArrayList;
/*     */ import java.nio.ByteBuffer;
/*     */ import java.util.List;
/*     */ import java.util.Set;
/*     */ import java.util.stream.IntStream;
/*     */ import java.util.stream.LongStream;
/*     */ import net.minecraft.util.parsing.packrat.ParseState;
/*     */ 
/*     */ enum ArrayPrefix {
/* 145 */   BYTE(SnbtGrammar.TypeSuffix.BYTE, new SnbtGrammar.TypeSuffix[0]) {
/* 146 */     private static final ByteBuffer EMPTY_BUFFER = ByteBuffer.wrap(new byte[0]);
/*     */     
/*     */     public <T> T create(DynamicOps<T> ops) {
/* 150 */       return (T)ops.createByteList(EMPTY_BUFFER);
/*     */     }
/*     */     
/*     */     public <T> T create(DynamicOps<T> ops, List<SnbtGrammar.IntegerLiteral> entries, ParseState<?> state) {
/* 155 */       ByteArrayList byteArrayList = new ByteArrayList();
/* 156 */       for (SnbtGrammar.IntegerLiteral entry : entries) {
/* 157 */         Number parsedNumber = buildNumber(entry, state);
/* 158 */         if (parsedNumber == null)
/* 159 */           return null; 
/* 161 */         byteArrayList.add(parsedNumber.byteValue());
/*     */       } 
/* 163 */       return (T)ops.createByteList(ByteBuffer.wrap(byteArrayList.toByteArray()));
/*     */     }
/*     */   },
/* 166 */   INT(SnbtGrammar.TypeSuffix.INT, new SnbtGrammar.TypeSuffix[] { SnbtGrammar.TypeSuffix.BYTE, SnbtGrammar.TypeSuffix.SHORT }) {
/*     */     public <T> T create(DynamicOps<T> ops) {
/* 169 */       return (T)ops.createIntList(IntStream.empty());
/*     */     }
/*     */     
/*     */     public <T> T create(DynamicOps<T> ops, List<SnbtGrammar.IntegerLiteral> entries, ParseState<?> state) {
/* 174 */       IntStream.Builder result = IntStream.builder();
/* 175 */       for (SnbtGrammar.IntegerLiteral entry : entries) {
/* 176 */         Number parsedNumber = buildNumber(entry, state);
/* 177 */         if (parsedNumber == null)
/* 178 */           return null; 
/* 180 */         result.add(parsedNumber.intValue());
/*     */       } 
/* 182 */       return (T)ops.createIntList(result.build());
/*     */     }
/*     */   },
/* 185 */   LONG(SnbtGrammar.TypeSuffix.LONG, new SnbtGrammar.TypeSuffix[] { SnbtGrammar.TypeSuffix.BYTE, SnbtGrammar.TypeSuffix.SHORT, SnbtGrammar.TypeSuffix.INT }) {
/*     */     public <T> T create(DynamicOps<T> ops) {
/* 188 */       return (T)ops.createLongList(LongStream.empty());
/*     */     }
/*     */     
/*     */     public <T> T create(DynamicOps<T> ops, List<SnbtGrammar.IntegerLiteral> entries, ParseState<?> state) {
/* 193 */       LongStream.Builder result = LongStream.builder();
/* 194 */       for (SnbtGrammar.IntegerLiteral entry : entries) {
/* 195 */         Number parsedNumber = buildNumber(entry, state);
/* 196 */         if (parsedNumber == null)
/* 197 */           return null; 
/* 199 */         result.add(parsedNumber.longValue());
/*     */       } 
/* 201 */       return (T)ops.createLongList(result.build());
/*     */     }
/*     */   };
/*     */   
/*     */   private final SnbtGrammar.TypeSuffix defaultType;
/*     */   
/*     */   private final Set<SnbtGrammar.TypeSuffix> additionalTypes;
/*     */   
/*     */   ArrayPrefix(SnbtGrammar.TypeSuffix defaultType, SnbtGrammar.TypeSuffix... additionalTypes) {
/* 210 */     this.additionalTypes = Set.of(additionalTypes);
/* 211 */     this.defaultType = defaultType;
/*     */   }
/*     */   
/*     */   public boolean isAllowed(SnbtGrammar.TypeSuffix type) {
/* 215 */     return (type == this.defaultType || this.additionalTypes.contains(type));
/*     */   }
/*     */   
/*     */   protected Number buildNumber(SnbtGrammar.IntegerLiteral entry, ParseState<?> state) {
/* 223 */     SnbtGrammar.TypeSuffix actualType = computeType(entry.suffix);
/* 224 */     if (actualType == null) {
/* 225 */       state.errorCollector().store(state.mark(), SnbtGrammar.ERROR_INVALID_ARRAY_ELEMENT_TYPE);
/* 226 */       return null;
/*     */     } 
/* 228 */     return entry.<Number>create((DynamicOps<Number>)JavaOps.INSTANCE, actualType, state);
/*     */   }
/*     */   
/*     */   private SnbtGrammar.TypeSuffix computeType(SnbtGrammar.IntegerSuffix value) {
/* 232 */     SnbtGrammar.TypeSuffix type = value.type();
/* 233 */     if (type == null)
/* 234 */       return this.defaultType; 
/* 236 */     if (!isAllowed(type))
/* 237 */       return null; 
/* 239 */     return type;
/*     */   }
/*     */   
/*     */   public abstract <T> T create(DynamicOps<T> paramDynamicOps);
/*     */   
/*     */   public abstract <T> T create(DynamicOps<T> paramDynamicOps, List<SnbtGrammar.IntegerLiteral> paramList, ParseState<?> paramParseState);
/*     */ }


/* Location:              C:\Users\karol\AppData\Roaming\ModrinthApp\meta\versions\26.2-0.19.5\26.2-0.19.5.jar!\net\minecraft\nbt\SnbtGrammar$ArrayPrefix.class
 * Java compiler version: 25 (69.0)
 * JD-Core Version:       1.1.3
 */