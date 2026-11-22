/*     */ package net.minecraft.nbt;
/*     */ 
/*     */ import java.io.DataInput;
/*     */ import java.io.DataOutput;
/*     */ import java.io.IOException;
/*     */ import java.util.Optional;
/*     */ 
/*     */ public final class StringTag extends Record implements PrimitiveTag {
/*     */   private final String value;
/*     */   
/*     */   private static final int SELF_SIZE_IN_BYTES = 36;
/*     */   
/*     */   public final int hashCode() {
/*     */     // Byte code:
/*     */     //   0: aload_0
/*     */     //   1: <illegal opcode> hashCode : (Lnet/minecraft/nbt/StringTag;)I
/*     */     //   6: ireturn
/*     */     // Line number table:
/*     */     //   Java source line number -> byte code offset
/*     */     //   #8	-> 0
/*     */     // Local variable table:
/*     */     //   start	length	slot	name	descriptor
/*     */     //   0	7	0	this	Lnet/minecraft/nbt/StringTag;
/*     */   }
/*     */   
/*     */   public final boolean equals(Object o) {
/*     */     // Byte code:
/*     */     //   0: aload_0
/*     */     //   1: aload_1
/*     */     //   2: <illegal opcode> equals : (Lnet/minecraft/nbt/StringTag;Ljava/lang/Object;)Z
/*     */     //   7: ireturn
/*     */     // Line number table:
/*     */     //   Java source line number -> byte code offset
/*     */     //   #8	-> 0
/*     */     // Local variable table:
/*     */     //   start	length	slot	name	descriptor
/*     */     //   0	8	0	this	Lnet/minecraft/nbt/StringTag;
/*     */     //   0	8	1	o	Ljava/lang/Object;
/*     */   }
/*     */   
/*     */   public String value() {
/*   8 */     return this.value;
/*     */   }
/*     */   
/*  16 */   public static final TagType<StringTag> TYPE = new TagType.VariableSize<StringTag>() {
/*     */       public StringTag load(DataInput input, NbtAccounter accounter) throws IOException {
/*  19 */         return StringTag.valueOf(readAccounted(input, accounter));
/*     */       }
/*     */       
/*     */       public StreamTagVisitor.ValueResult parse(DataInput input, StreamTagVisitor output, NbtAccounter accounter) throws IOException {
/*  24 */         return output.visit(readAccounted(input, accounter));
/*     */       }
/*     */       
/*     */       private static String readAccounted(DataInput input, NbtAccounter accounter) throws IOException {
/*  28 */         accounter.accountBytes(36L);
/*  31 */         String data = input.readUTF();
/*  32 */         accounter.accountBytes(2L, data.length());
/*  33 */         return data;
/*     */       }
/*     */       
/*     */       public void skip(DataInput input, NbtAccounter accounter) throws IOException {
/*  38 */         StringTag.skipString(input);
/*     */       }
/*     */       
/*     */       public String getName() {
/*  43 */         return "STRING";
/*     */       }
/*     */       
/*     */       public String getPrettyName() {
/*  48 */         return "TAG_String";
/*     */       }
/*     */     };
/*     */   
/*     */   public static void skipString(DataInput input) throws IOException {
/*  53 */     input.skipBytes(input.readUnsignedShort());
/*     */   }
/*     */   
/*  56 */   private static final StringTag EMPTY = new StringTag("");
/*     */   
/*     */   private static final char DOUBLE_QUOTE = '"';
/*     */   
/*     */   private static final char SINGLE_QUOTE = '\'';
/*     */   
/*     */   private static final char ESCAPE = '\\';
/*     */   
/*     */   private static final char NOT_SET = '\000';
/*     */   
/*     */   @Deprecated(forRemoval = true)
/*     */   public StringTag(String value) {
/*  64 */     this.value = value;
/*     */   }
/*     */   
/*     */   public static StringTag valueOf(String data) {
/*  68 */     if (data.isEmpty())
/*  69 */       return EMPTY; 
/*  71 */     return new StringTag(data);
/*     */   }
/*     */   
/*     */   public void write(DataOutput output) throws IOException {
/*  76 */     output.writeUTF(this.value);
/*     */   }
/*     */   
/*     */   public int sizeInBytes() {
/*  81 */     return 36 + 2 * this.value.length();
/*     */   }
/*     */   
/*     */   public byte getId() {
/*  86 */     return 8;
/*     */   }
/*     */   
/*     */   public TagType<StringTag> getType() {
/*  91 */     return TYPE;
/*     */   }
/*     */   
/*     */   public String toString() {
/*  96 */     StringTagVisitor visitor = new StringTagVisitor();
/*  97 */     visitor.visitString(this);
/*  98 */     return visitor.build();
/*     */   }
/*     */   
/*     */   public StringTag copy() {
/* 103 */     return this;
/*     */   }
/*     */   
/*     */   public Optional<String> asString() {
/* 108 */     return Optional.of(this.value);
/*     */   }
/*     */   
/*     */   public void accept(TagVisitor visitor) {
/* 113 */     visitor.visitString(this);
/*     */   }
/*     */   
/*     */   public static String quoteAndEscape(String input) {
/* 117 */     StringBuilder result = new StringBuilder();
/* 118 */     quoteAndEscape(input, result);
/* 119 */     return result.toString();
/*     */   }
/*     */   
/*     */   public static void quoteAndEscape(String input, StringBuilder result) {
/* 125 */     int quoteMarkIndex = result.length();
/* 126 */     result.append(' ');
/* 127 */     char quote = Character.MIN_VALUE;
/* 128 */     for (int i = 0; i < input.length(); i++) {
/* 129 */       char c = input.charAt(i);
/* 130 */       if (c == '\\') {
/* 131 */         result.append("\\\\");
/* 132 */       } else if (c == '"' || c == '\'') {
/* 133 */         if (quote == '\000')
/* 134 */           quote = (c == '"') ? '\'' : '"'; 
/* 136 */         if (quote == c)
/* 137 */           result.append('\\'); 
/* 139 */         result.append(c);
/*     */       } else {
/* 141 */         String escaped = SnbtGrammar.escapeControlCharacters(c);
/* 142 */         if (escaped != null) {
/* 143 */           result.append('\\');
/* 144 */           result.append(escaped);
/*     */         } else {
/* 146 */           result.append(c);
/*     */         } 
/*     */       } 
/*     */     } 
/* 150 */     if (quote == '\000')
/* 151 */       quote = '"'; 
/* 154 */     result.setCharAt(quoteMarkIndex, quote);
/* 155 */     result.append(quote);
/*     */   }
/*     */   
/*     */   public static String escapeWithoutQuotes(String input) {
/* 159 */     StringBuilder result = new StringBuilder();
/* 160 */     escapeWithoutQuotes(input, result);
/* 161 */     return result.toString();
/*     */   }
/*     */   
/*     */   public static void escapeWithoutQuotes(String input, StringBuilder result) {
/* 166 */     for (int i = 0; i < input.length(); i++) {
/*     */       String escaped;
/* 167 */       char c = input.charAt(i);
/* 168 */       switch (c) {
/*     */         case '"':
/*     */         case '\'':
/*     */         case '\\':
/* 170 */           result.append('\\');
/* 171 */           result.append(c);
/*     */           break;
/*     */         default:
/* 174 */           escaped = SnbtGrammar.escapeControlCharacters(c);
/* 175 */           if (escaped != null) {
/* 176 */             result.append('\\');
/* 177 */             result.append(escaped);
/*     */             break;
/*     */           } 
/* 179 */           result.append(c);
/*     */           break;
/*     */       } 
/*     */     } 
/*     */   }
/*     */   
/*     */   public StreamTagVisitor.ValueResult accept(StreamTagVisitor visitor) {
/* 188 */     return visitor.visit(this.value);
/*     */   }
/*     */ }


/* Location:              C:\Users\karol\AppData\Roaming\ModrinthApp\meta\versions\26.2-0.19.5\26.2-0.19.5.jar!\net\minecraft\nbt\StringTag.class
 * Java compiler version: 25 (69.0)
 * JD-Core Version:       1.1.3
 */