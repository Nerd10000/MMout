/*     */ package net.minecraft.nbt;
/*     */ 
/*     */ import com.google.common.collect.Maps;
/*     */ import java.io.DataInput;
/*     */ import java.io.IOException;
/*     */ import java.util.Map;
/*     */ 
/*     */ class null implements TagType.VariableSize<CompoundTag> {
/*     */   public CompoundTag load(DataInput input, NbtAccounter accounter) throws IOException {
/*  68 */     accounter.pushDepth();
/*     */     try {
/*  70 */       return loadCompound(input, accounter);
/*     */     } finally {
/*  72 */       accounter.popDepth();
/*     */     } 
/*     */   }
/*     */   
/*     */   private static CompoundTag loadCompound(DataInput input, NbtAccounter accounter) throws IOException {
/*  77 */     accounter.accountBytes(48L);
/*  79 */     Map<String, Tag> values = Maps.newHashMap();
/*     */     byte tagType;
/*  81 */     while ((tagType = input.readByte()) != 0) {
/*  82 */       String key = readString(input, accounter);
/*  83 */       Tag tag = CompoundTag.readNamedTagData(TagTypes.getType(tagType), key, input, accounter);
/*  84 */       if (values.put(key, tag) == null)
/*  85 */         accounter.accountBytes(36L); 
/*     */     } 
/*  88 */     return new CompoundTag(values);
/*     */   }
/*     */   
/*     */   public StreamTagVisitor.ValueResult parse(DataInput input, StreamTagVisitor output, NbtAccounter accounter) throws IOException {
/*  93 */     accounter.pushDepth();
/*     */     try {
/*  95 */       return parseCompound(input, output, accounter);
/*     */     } finally {
/*  97 */       accounter.popDepth();
/*     */     } 
/*     */   }
/*     */   
/*     */   private static StreamTagVisitor.ValueResult parseCompound(DataInput input, StreamTagVisitor output, NbtAccounter accounter) throws IOException {
/* 102 */     accounter.accountBytes(48L);
/*     */     byte tagTypeId;
/* 107 */     while ((tagTypeId = input.readByte()) != 0) {
/* 108 */       TagType<?> tagType = TagTypes.getType(tagTypeId);
/* 110 */       switch (CompoundTag.null.$SwitchMap$net$minecraft$nbt$StreamTagVisitor$EntryResult[output.visitEntry(tagType).ordinal()]) {
/*     */         case 1:
/* 112 */           return StreamTagVisitor.ValueResult.HALT;
/*     */         case 2:
/* 115 */           StringTag.skipString(input);
/* 116 */           tagType.skip(input, accounter);
/*     */           break;
/*     */         case 3:
/* 120 */           StringTag.skipString(input);
/* 121 */           tagType.skip(input, accounter);
/*     */           continue;
/*     */       } 
/* 126 */       String key = readString(input, accounter);
/* 127 */       switch (CompoundTag.null.$SwitchMap$net$minecraft$nbt$StreamTagVisitor$EntryResult[output.visitEntry(tagType, key).ordinal()]) {
/*     */         case 1:
/* 129 */           return StreamTagVisitor.ValueResult.HALT;
/*     */         case 2:
/* 132 */           tagType.skip(input, accounter);
/*     */           break;
/*     */         case 3:
/* 136 */           tagType.skip(input, accounter);
/*     */           continue;
/*     */       } 
/* 141 */       accounter.accountBytes(36L);
/* 142 */       switch (CompoundTag.null.$SwitchMap$net$minecraft$nbt$StreamTagVisitor$ValueResult[tagType.parse(input, output, accounter).ordinal()]) {
/*     */         case 1:
/* 144 */           return StreamTagVisitor.ValueResult.HALT;
/*     */       } 
/*     */     } 
/* 151 */     if (tagTypeId != 0)
/* 152 */       while ((tagTypeId = input.readByte()) != 0) {
/* 153 */         StringTag.skipString(input);
/* 154 */         TagTypes.getType(tagTypeId).skip(input, accounter);
/*     */       }  
/* 158 */     return output.visitContainerEnd();
/*     */   }
/*     */   
/*     */   private static String readString(DataInput input, NbtAccounter accounter) throws IOException {
/* 162 */     String key = input.readUTF();
/* 163 */     accounter.accountBytes(28L);
/* 164 */     accounter.accountBytes(2L, key.length());
/* 165 */     return key;
/*     */   }
/*     */   
/*     */   public void skip(DataInput input, NbtAccounter accounter) throws IOException {
/* 170 */     accounter.pushDepth();
/*     */     try {
/*     */       byte tagTypeId;
/* 173 */       while ((tagTypeId = input.readByte()) != 0) {
/* 174 */         StringTag.skipString(input);
/* 175 */         TagTypes.getType(tagTypeId).skip(input, accounter);
/*     */       } 
/*     */     } finally {
/* 178 */       accounter.popDepth();
/*     */     } 
/*     */   }
/*     */   
/*     */   public String getName() {
/* 184 */     return "COMPOUND";
/*     */   }
/*     */   
/*     */   public String getPrettyName() {
/* 189 */     return "TAG_Compound";
/*     */   }
/*     */ }


/* Location:              C:\Users\karol\AppData\Roaming\ModrinthApp\meta\versions\26.2-0.19.5\26.2-0.19.5.jar!\net\minecraft\nbt\CompoundTag$1.class
 * Java compiler version: 25 (69.0)
 * JD-Core Version:       1.1.3
 */