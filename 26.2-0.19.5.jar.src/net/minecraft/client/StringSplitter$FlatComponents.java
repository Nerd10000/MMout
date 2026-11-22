/*     */ package net.minecraft.client;
/*     */ 
/*     */ import java.util.List;
/*     */ import java.util.ListIterator;
/*     */ import java.util.Objects;
/*     */ import java.util.stream.Collectors;
/*     */ import net.minecraft.network.chat.FormattedText;
/*     */ import net.minecraft.network.chat.Style;
/*     */ 
/*     */ class FlatComponents {
/*     */   private final List<StringSplitter.LineComponent> parts;
/*     */   
/*     */   private String flatParts;
/*     */   
/*     */   public FlatComponents(List<StringSplitter.LineComponent> parts) {
/* 309 */     this.parts = parts;
/* 310 */     this.flatParts = parts.stream().map(p -> p.contents).collect(Collectors.joining());
/*     */   }
/*     */   
/*     */   public char charAt(int position) {
/* 314 */     return this.flatParts.charAt(position);
/*     */   }
/*     */   
/*     */   public FormattedText splitAt(int skipPosition, int skipSize, Style splitStyle) {
/* 318 */     ComponentCollector result = new ComponentCollector();
/* 319 */     ListIterator<StringSplitter.LineComponent> it = this.parts.listIterator();
/* 320 */     int position = skipPosition;
/* 321 */     boolean inSkip = false;
/* 322 */     while (it.hasNext()) {
/* 323 */       StringSplitter.LineComponent element = it.next();
/* 324 */       String contents = element.contents;
/* 325 */       int contentsSize = contents.length();
/* 327 */       if (!inSkip)
/* 328 */         if (position > contentsSize) {
/* 329 */           result.append(element);
/* 330 */           it.remove();
/* 331 */           position -= contentsSize;
/*     */         } else {
/* 333 */           String beforeSplit = contents.substring(0, position);
/* 334 */           if (!beforeSplit.isEmpty())
/* 335 */             result.append(FormattedText.of(beforeSplit, element.style)); 
/* 337 */           position += skipSize;
/* 338 */           inSkip = true;
/*     */         }  
/* 342 */       if (inSkip) {
/* 343 */         if (position > contentsSize) {
/* 344 */           it.remove();
/* 345 */           position -= contentsSize;
/*     */           continue;
/*     */         } 
/* 347 */         String afterSplit = contents.substring(position);
/* 348 */         if (afterSplit.isEmpty()) {
/* 349 */           it.remove();
/*     */           break;
/*     */         } 
/* 351 */         it.set(new StringSplitter.LineComponent(afterSplit, splitStyle));
/*     */         break;
/*     */       } 
/*     */     } 
/* 358 */     this.flatParts = this.flatParts.substring(skipPosition + skipSize);
/* 359 */     return result.getResultOrEmpty();
/*     */   }
/*     */   
/*     */   public FormattedText getRemainder() {
/* 363 */     ComponentCollector result = new ComponentCollector();
/* 364 */     Objects.requireNonNull(result);
/* 364 */     this.parts.forEach(result::append);
/* 365 */     this.parts.clear();
/* 366 */     return result.getResult();
/*     */   }
/*     */ }


/* Location:              C:\Users\karol\AppData\Roaming\ModrinthApp\meta\versions\26.2-0.19.5\26.2-0.19.5.jar!\net\minecraft\client\StringSplitter$FlatComponents.class
 * Java compiler version: 25 (69.0)
 * JD-Core Version:       1.1.3
 */