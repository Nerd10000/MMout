/*     */ package net.minecraft.nbt;
/*     */ 
/*     */ import com.mojang.serialization.DynamicOps;
/*     */ import java.util.Objects;
/*     */ import net.minecraft.util.parsing.packrat.ParseState;
/*     */ 
/*     */ final class IntegerLiteral extends Record {
/*     */   private final SnbtGrammar.Sign sign;
/*     */   
/*     */   private final SnbtGrammar.Base base;
/*     */   
/*     */   private final String digits;
/*     */   
/*     */   private final SnbtGrammar.IntegerSuffix suffix;
/*     */   
/*     */   private IntegerLiteral(SnbtGrammar.Sign sign, SnbtGrammar.Base base, String digits, SnbtGrammar.IntegerSuffix suffix) {
/* 342 */     this.sign = sign;
/* 342 */     this.base = base;
/* 342 */     this.digits = digits;
/* 342 */     this.suffix = suffix;
/*     */   }
/*     */   
/*     */   public final String toString() {
/*     */     // Byte code:
/*     */     //   0: aload_0
/*     */     //   1: <illegal opcode> toString : (Lnet/minecraft/nbt/SnbtGrammar$IntegerLiteral;)Ljava/lang/String;
/*     */     //   6: areturn
/*     */     // Line number table:
/*     */     //   Java source line number -> byte code offset
/*     */     //   #342	-> 0
/*     */     // Local variable table:
/*     */     //   start	length	slot	name	descriptor
/*     */     //   0	7	0	this	Lnet/minecraft/nbt/SnbtGrammar$IntegerLiteral;
/*     */   }
/*     */   
/*     */   public final int hashCode() {
/*     */     // Byte code:
/*     */     //   0: aload_0
/*     */     //   1: <illegal opcode> hashCode : (Lnet/minecraft/nbt/SnbtGrammar$IntegerLiteral;)I
/*     */     //   6: ireturn
/*     */     // Line number table:
/*     */     //   Java source line number -> byte code offset
/*     */     //   #342	-> 0
/*     */     // Local variable table:
/*     */     //   start	length	slot	name	descriptor
/*     */     //   0	7	0	this	Lnet/minecraft/nbt/SnbtGrammar$IntegerLiteral;
/*     */   }
/*     */   
/*     */   public final boolean equals(Object o) {
/*     */     // Byte code:
/*     */     //   0: aload_0
/*     */     //   1: aload_1
/*     */     //   2: <illegal opcode> equals : (Lnet/minecraft/nbt/SnbtGrammar$IntegerLiteral;Ljava/lang/Object;)Z
/*     */     //   7: ireturn
/*     */     // Line number table:
/*     */     //   Java source line number -> byte code offset
/*     */     //   #342	-> 0
/*     */     // Local variable table:
/*     */     //   start	length	slot	name	descriptor
/*     */     //   0	8	0	this	Lnet/minecraft/nbt/SnbtGrammar$IntegerLiteral;
/*     */     //   0	8	1	o	Ljava/lang/Object;
/*     */   }
/*     */   
/*     */   public SnbtGrammar.Sign sign() {
/* 342 */     return this.sign;
/*     */   }
/*     */   
/*     */   public SnbtGrammar.Base base() {
/* 342 */     return this.base;
/*     */   }
/*     */   
/*     */   public String digits() {
/* 342 */     return this.digits;
/*     */   }
/*     */   
/*     */   public SnbtGrammar.IntegerSuffix suffix() {
/* 342 */     return this.suffix;
/*     */   }
/*     */   
/*     */   private SnbtGrammar.SignedPrefix signedOrDefault() {
/* 349 */     if (this.suffix.signed != null)
/* 350 */       return this.suffix.signed; 
/* 355 */     switch (this.base.ordinal()) {
/*     */       default:
/* 355 */         throw new MatchException(null, null);
/*     */       case 0:
/*     */       case 2:
/*     */       
/*     */       case 1:
/*     */         break;
/*     */     } 
/* 355 */     return 
/*     */       
/* 357 */       SnbtGrammar.SignedPrefix.SIGNED;
/*     */   }
/*     */   
/*     */   private String cleanupDigits(SnbtGrammar.Sign sign) {
/* 362 */     boolean needsUnderscoreRemoval = SnbtGrammar.needsUnderscoreRemoval(this.digits);
/* 364 */     if (sign == SnbtGrammar.Sign.MINUS || needsUnderscoreRemoval) {
/* 365 */       StringBuilder result = new StringBuilder();
/* 366 */       sign.append(result);
/* 367 */       SnbtGrammar.cleanAndAppend(result, this.digits, needsUnderscoreRemoval);
/* 368 */       return result.toString();
/*     */     } 
/* 370 */     return this.digits;
/*     */   }
/*     */   
/*     */   public <T> T create(DynamicOps<T> ops, ParseState<?> state) {
/* 374 */     return create(ops, Objects.<SnbtGrammar.TypeSuffix>requireNonNullElse(this.suffix.type, SnbtGrammar.TypeSuffix.INT), state);
/*     */   }
/*     */   
/*     */   public <T> T create(DynamicOps<T> ops, SnbtGrammar.TypeSuffix type, ParseState<?> state) {
/* 378 */     boolean isSigned = (signedOrDefault() == SnbtGrammar.SignedPrefix.SIGNED);
/* 379 */     if (!isSigned && this.sign == SnbtGrammar.Sign.MINUS) {
/* 381 */       state.errorCollector().store(state.mark(), SnbtGrammar.ERROR_EXPECTED_NON_NEGATIVE_NUMBER);
/* 382 */       return null;
/*     */     } 
/* 385 */     String fixedDigits = cleanupDigits(this.sign);
/* 386 */     switch (this.base.ordinal()) {
/*     */       default:
/* 386 */         throw new MatchException(null, null);
/*     */       case 0:
/*     */       
/*     */       case 1:
/*     */       
/*     */       case 2:
/*     */         break;
/*     */     } 
/* 386 */     int radix = 
/*     */       
/* 389 */       16;
/*     */     try {
/* 393 */       if (isSigned) {
/* 394 */         switch (type.ordinal()) {
/*     */           case 2:
/*     */           
/*     */           case 3:
/*     */           
/*     */           case 4:
/*     */           
/*     */           case 5:
/*     */           
/*     */         } 
/* 400 */         state.errorCollector().store(state.mark(), SnbtGrammar.ERROR_EXPECTED_INTEGER_TYPE);
/* 401 */         return null;
/*     */       } 
/* 405 */       switch (type.ordinal()) {
/*     */         case 2:
/*     */         
/*     */         case 3:
/*     */         
/*     */         case 4:
/*     */         
/*     */         case 5:
/*     */         
/*     */       } 
/* 411 */       state.errorCollector().store(state.mark(), SnbtGrammar.ERROR_EXPECTED_INTEGER_TYPE);
/* 412 */       return null;
/* 416 */     } catch (NumberFormatException e) {
/* 419 */       state.errorCollector().store(state.mark(), SnbtGrammar.createNumberParseError(e));
/* 420 */       return null;
/*     */     } 
/*     */   }
/*     */ }


/* Location:              C:\Users\karol\AppData\Roaming\ModrinthApp\meta\versions\26.2-0.19.5\26.2-0.19.5.jar!\net\minecraft\nbt\SnbtGrammar$IntegerLiteral.class
 * Java compiler version: 25 (69.0)
 * JD-Core Version:       1.1.3
 */