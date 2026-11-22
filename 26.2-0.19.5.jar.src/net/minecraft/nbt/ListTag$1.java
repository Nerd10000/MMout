/*     */ package net.minecraft.nbt;
/*     */ 
/*     */ import java.io.DataInput;
/*     */ import java.io.IOException;
/*     */ import java.util.ArrayList;
/*     */ 
/*     */ class null implements TagType.VariableSize<ListTag> {
/*     */   public ListTag load(DataInput input, NbtAccounter accounter) throws IOException {
/*  33 */     accounter.pushDepth();
/*     */     try {
/*  35 */       return loadList(input, accounter);
/*     */     } finally {
/*  37 */       accounter.popDepth();
/*     */     } 
/*     */   }
/*     */   
/*     */   private static ListTag loadList(DataInput input, NbtAccounter accounter) throws IOException {
/*  42 */     accounter.accountBytes(36L);
/*  43 */     byte typeId = input.readByte();
/*  44 */     int count = readListCount(input);
/*  45 */     if (typeId == 0 && count > 0)
/*  46 */       throw new NbtFormatException("Missing type on ListTag"); 
/*  48 */     accounter.accountBytes(4L, count);
/*  49 */     TagType<?> type = TagTypes.getType(typeId);
/*  50 */     ListTag list = new ListTag(new ArrayList<>(count));
/*  51 */     for (int i = 0; i < count; i++)
/*  52 */       list.addAndUnwrap((Tag)type.load(input, accounter)); 
/*  54 */     return list;
/*     */   }
/*     */   
/*     */   public StreamTagVisitor.ValueResult parse(DataInput input, StreamTagVisitor output, NbtAccounter accounter) throws IOException {
/*  59 */     accounter.pushDepth();
/*     */     try {
/*  61 */       return parseList(input, output, accounter);
/*     */     } finally {
/*  63 */       accounter.popDepth();
/*     */     } 
/*     */   }
/*     */   
/*     */   private static StreamTagVisitor.ValueResult parseList(DataInput input, StreamTagVisitor output, NbtAccounter accounter) throws IOException {
/*  68 */     accounter.accountBytes(36L);
/*  69 */     TagType<?> elementType = TagTypes.getType(input.readByte());
/*  70 */     int count = readListCount(input);
/*  71 */     switch (ListTag.null.$SwitchMap$net$minecraft$nbt$StreamTagVisitor$ValueResult[output.visitList(elementType, count).ordinal()]) {
/*     */       case 1:
/*  73 */         return StreamTagVisitor.ValueResult.HALT;
/*     */       case 2:
/*  76 */         elementType.skip(input, count, accounter);
/*  77 */         return output.visitContainerEnd();
/*     */     } 
/*  81 */     accounter.accountBytes(4L, count);
/*     */     int i;
/*  84 */     for (i = 0; i < count; i++) {
/*  85 */       switch (ListTag.null.$SwitchMap$net$minecraft$nbt$StreamTagVisitor$EntryResult[output.visitElement(elementType, i).ordinal()]) {
/*     */         case 1:
/*  87 */           return StreamTagVisitor.ValueResult.HALT;
/*     */         case 2:
/*  90 */           elementType.skip(input, accounter);
/*     */           break;
/*     */         case 3:
/*  94 */           elementType.skip(input, accounter);
/*     */           break;
/*     */         default:
/*  99 */           switch (ListTag.null.$SwitchMap$net$minecraft$nbt$StreamTagVisitor$ValueResult[elementType.parse(input, output, accounter).ordinal()]) {
/*     */             case 1:
/* 101 */               return StreamTagVisitor.ValueResult.HALT;
/*     */             case 2:
/*     */               break;
/*     */           } 
/*     */           break;
/*     */       } 
/*     */     } 
/* 108 */     int amountToSkip = count - 1 - i;
/* 109 */     if (amountToSkip > 0)
/* 110 */       elementType.skip(input, amountToSkip, accounter); 
/* 112 */     return output.visitContainerEnd();
/*     */   }
/*     */   
/*     */   private static int readListCount(DataInput input) throws IOException {
/* 116 */     int count = input.readInt();
/* 117 */     if (count < 0)
/* 118 */       throw new NbtFormatException("ListTag length cannot be negative: " + count); 
/* 120 */     return count;
/*     */   }
/*     */   
/*     */   public void skip(DataInput input, NbtAccounter accounter) throws IOException {
/* 125 */     accounter.pushDepth();
/*     */     try {
/* 127 */       TagType<?> type = TagTypes.getType(input.readByte());
/* 128 */       int count = input.readInt();
/* 129 */       type.skip(input, count, accounter);
/*     */     } finally {
/* 131 */       accounter.popDepth();
/*     */     } 
/*     */   }
/*     */   
/*     */   public String getName() {
/* 137 */     return "LIST";
/*     */   }
/*     */   
/*     */   public String getPrettyName() {
/* 142 */     return "TAG_List";
/*     */   }
/*     */ }


/* Location:              C:\Users\karol\AppData\Roaming\ModrinthApp\meta\versions\26.2-0.19.5\26.2-0.19.5.jar!\net\minecraft\nbt\ListTag$1.class
 * Java compiler version: 25 (69.0)
 * JD-Core Version:       1.1.3
 */