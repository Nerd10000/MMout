/*     */ package net.minecraft.client;
/*     */ 
/*     */ import net.minecraft.network.chat.Style;
/*     */ import net.minecraft.util.FormattedCharSink;
/*     */ 
/*     */ class LineBreakFinder implements FormattedCharSink {
/*     */   private final float maxWidth;
/*     */   
/* 142 */   private int lineBreak = -1;
/*     */   
/* 143 */   private Style lineBreakStyle = Style.EMPTY;
/*     */   
/*     */   private boolean hadNonZeroWidthChar;
/*     */   
/*     */   private float width;
/*     */   
/* 146 */   private int lastSpace = -1;
/*     */   
/* 147 */   private Style lastSpaceStyle = Style.EMPTY;
/*     */   
/*     */   private int nextChar;
/*     */   
/*     */   private int offset;
/*     */   
/*     */   public LineBreakFinder(float maxWidth) {
/* 152 */     this.maxWidth = Math.max(maxWidth, 1.0F);
/*     */   }
/*     */   
/*     */   public boolean accept(int position, Style style, int codepoint) {
/* 157 */     int adjustedPosition = position + this.offset;
/* 158 */     switch (codepoint) {
/*     */       case 10:
/* 160 */         return finishIteration(adjustedPosition, style);
/*     */       case 32:
/* 162 */         this.lastSpace = adjustedPosition;
/* 163 */         this.lastSpaceStyle = style;
/*     */         break;
/*     */     } 
/* 166 */     float charWidth = StringSplitter.this.widthProvider.getWidth(codepoint, style);
/* 167 */     this.width += charWidth;
/* 168 */     if (this.hadNonZeroWidthChar && this.width > this.maxWidth) {
/* 169 */       if (this.lastSpace != -1)
/* 170 */         return finishIteration(this.lastSpace, this.lastSpaceStyle); 
/* 172 */       return finishIteration(adjustedPosition, style);
/*     */     } 
/* 175 */     this.hadNonZeroWidthChar |= (charWidth != 0.0F) ? 1 : 0;
/* 176 */     this.nextChar = adjustedPosition + Character.charCount(codepoint);
/* 177 */     return true;
/*     */   }
/*     */   
/*     */   private boolean finishIteration(int lineBreak, Style style) {
/* 183 */     this.lineBreak = lineBreak;
/* 184 */     this.lineBreakStyle = style;
/* 185 */     return false;
/*     */   }
/*     */   
/*     */   private boolean lineBreakFound() {
/* 189 */     return (this.lineBreak != -1);
/*     */   }
/*     */   
/*     */   public int getSplitPosition() {
/* 193 */     return lineBreakFound() ? this.lineBreak : this.nextChar;
/*     */   }
/*     */   
/*     */   public Style getSplitStyle() {
/* 197 */     return this.lineBreakStyle;
/*     */   }
/*     */   
/*     */   public void addToOffset(int delta) {
/* 201 */     this.offset += delta;
/*     */   }
/*     */ }


/* Location:              C:\Users\karol\AppData\Roaming\ModrinthApp\meta\versions\26.2-0.19.5\26.2-0.19.5.jar!\net\minecraft\client\StringSplitter$LineBreakFinder.class
 * Java compiler version: 25 (69.0)
 * JD-Core Version:       1.1.3
 */