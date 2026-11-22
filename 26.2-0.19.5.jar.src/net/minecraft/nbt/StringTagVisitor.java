/*     */ package net.minecraft.nbt;
/*     */ 
/*     */ import java.util.ArrayList;
/*     */ import java.util.Comparator;
/*     */ import java.util.List;
/*     */ import java.util.Map;
/*     */ import java.util.regex.Pattern;
/*     */ 
/*     */ public class StringTagVisitor implements TagVisitor {
/*  11 */   private static final Pattern UNQUOTED_KEY_MATCH = Pattern.compile("[A-Za-z._]+[A-Za-z0-9._+-]*");
/*     */   
/*  13 */   private final StringBuilder builder = new StringBuilder();
/*     */   
/*     */   public String build() {
/*  16 */     return this.builder.toString();
/*     */   }
/*     */   
/*     */   public void visitString(StringTag tag) {
/*  21 */     this.builder.append(StringTag.quoteAndEscape(tag.value()));
/*     */   }
/*     */   
/*     */   public void visitByte(ByteTag tag) {
/*  26 */     this.builder.append(tag.value()).append('b');
/*     */   }
/*     */   
/*     */   public void visitShort(ShortTag tag) {
/*  31 */     this.builder.append(tag.value()).append('s');
/*     */   }
/*     */   
/*     */   public void visitInt(IntTag tag) {
/*  36 */     this.builder.append(tag.value());
/*     */   }
/*     */   
/*     */   public void visitLong(LongTag tag) {
/*  41 */     this.builder.append(tag.value()).append('L');
/*     */   }
/*     */   
/*     */   public void visitFloat(FloatTag tag) {
/*  46 */     this.builder.append(tag.value()).append('f');
/*     */   }
/*     */   
/*     */   public void visitDouble(DoubleTag tag) {
/*  51 */     this.builder.append(tag.value()).append('d');
/*     */   }
/*     */   
/*     */   public void visitByteArray(ByteArrayTag tag) {
/*  56 */     this.builder.append("[B;");
/*  57 */     byte[] data = tag.getAsByteArray();
/*  58 */     for (int i = 0; i < data.length; i++) {
/*  59 */       if (i != 0)
/*  60 */         this.builder.append(','); 
/*  62 */       this.builder.append(data[i]).append('B');
/*     */     } 
/*  64 */     this.builder.append(']');
/*     */   }
/*     */   
/*     */   public void visitIntArray(IntArrayTag tag) {
/*  69 */     this.builder.append("[I;");
/*  70 */     int[] data = tag.getAsIntArray();
/*  71 */     for (int i = 0; i < data.length; i++) {
/*  72 */       if (i != 0)
/*  73 */         this.builder.append(','); 
/*  75 */       this.builder.append(data[i]);
/*     */     } 
/*  77 */     this.builder.append(']');
/*     */   }
/*     */   
/*     */   public void visitLongArray(LongArrayTag tag) {
/*  82 */     this.builder.append("[L;");
/*  83 */     long[] data = tag.getAsLongArray();
/*  84 */     for (int i = 0; i < data.length; i++) {
/*  85 */       if (i != 0)
/*  86 */         this.builder.append(','); 
/*  88 */       this.builder.append(data[i]).append('L');
/*     */     } 
/*  90 */     this.builder.append(']');
/*     */   }
/*     */   
/*     */   public void visitList(ListTag tag) {
/*  95 */     this.builder.append('[');
/*  96 */     for (int i = 0; i < tag.size(); i++) {
/*  97 */       if (i != 0)
/*  98 */         this.builder.append(','); 
/* 100 */       tag.get(i).accept(this);
/*     */     } 
/* 102 */     this.builder.append(']');
/*     */   }
/*     */   
/*     */   public void visitCompound(CompoundTag tag) {
/* 107 */     this.builder.append('{');
/* 109 */     List<Map.Entry<String, Tag>> entries = new ArrayList<>(tag.entrySet());
/* 110 */     entries.sort((Comparator)Map.Entry.comparingByKey());
/* 111 */     for (int i = 0; i < entries.size(); i++) {
/* 112 */       Map.Entry<String, Tag> entry = entries.get(i);
/* 113 */       if (i != 0)
/* 114 */         this.builder.append(','); 
/* 116 */       handleKeyEscape(entry.getKey());
/* 117 */       this.builder.append(':');
/* 118 */       ((Tag)entry.getValue()).accept(this);
/*     */     } 
/* 121 */     this.builder.append('}');
/*     */   }
/*     */   
/*     */   private void handleKeyEscape(String input) {
/* 125 */     if (!input.equalsIgnoreCase("true") && !input.equalsIgnoreCase("false") && UNQUOTED_KEY_MATCH.matcher(input).matches()) {
/* 126 */       this.builder.append(input);
/*     */     } else {
/* 128 */       StringTag.quoteAndEscape(input, this.builder);
/*     */     } 
/*     */   }
/*     */   
/*     */   public void visitEnd(EndTag tag) {
/* 134 */     this.builder.append("END");
/*     */   }
/*     */ }


/* Location:              C:\Users\karol\AppData\Roaming\ModrinthApp\meta\versions\26.2-0.19.5\26.2-0.19.5.jar!\net\minecraft\nbt\StringTagVisitor.class
 * Java compiler version: 25 (69.0)
 * JD-Core Version:       1.1.3
 */