/*     */ package net.minecraft.nbt;
/*     */ 
/*     */ import com.google.common.annotations.VisibleForTesting;
/*     */ import java.io.BufferedOutputStream;
/*     */ import java.io.DataInput;
/*     */ import java.io.DataInputStream;
/*     */ import java.io.DataOutput;
/*     */ import java.io.DataOutputStream;
/*     */ import java.io.IOException;
/*     */ import java.io.InputStream;
/*     */ import java.io.OutputStream;
/*     */ import java.io.UTFDataFormatException;
/*     */ import java.nio.file.Files;
/*     */ import java.nio.file.OpenOption;
/*     */ import java.nio.file.Path;
/*     */ import java.nio.file.StandardOpenOption;
/*     */ import java.util.zip.GZIPInputStream;
/*     */ import java.util.zip.GZIPOutputStream;
/*     */ import net.minecraft.CrashReport;
/*     */ import net.minecraft.CrashReportCategory;
/*     */ import net.minecraft.util.DelegateDataOutput;
/*     */ import net.minecraft.util.FastBufferedInputStream;
/*     */ import net.minecraft.util.Util;
/*     */ 
/*     */ public class NbtIo {
/*  29 */   private static final OpenOption[] SYNC_OUTPUT_OPTIONS = new OpenOption[] { StandardOpenOption.SYNC, StandardOpenOption.WRITE, StandardOpenOption.CREATE, StandardOpenOption.TRUNCATE_EXISTING };
/*     */   
/*     */   public static CompoundTag readCompressed(Path file, NbtAccounter accounter) throws IOException {
/*  33 */     InputStream rawInput = Files.newInputStream(file, new OpenOption[0]);
/*     */     try {
/*  34 */       FastBufferedInputStream fastBufferedInputStream = new FastBufferedInputStream(rawInput);
/*     */       try {
/*  36 */         CompoundTag compoundTag = readCompressed((InputStream)fastBufferedInputStream, accounter);
/*  37 */         fastBufferedInputStream.close();
/*  37 */         if (rawInput != null)
/*  37 */           rawInput.close(); 
/*     */         return compoundTag;
/*     */       } catch (Throwable throwable) {
/*     */         try {
/*     */           fastBufferedInputStream.close();
/*     */         } catch (Throwable throwable1) {
/*     */           throwable.addSuppressed(throwable1);
/*     */         } 
/*     */         throw throwable;
/*     */       } 
/*     */     } catch (Throwable throwable) {
/*     */       if (rawInput != null)
/*     */         try {
/*     */           rawInput.close();
/*     */         } catch (Throwable throwable1) {
/*     */           throwable.addSuppressed(throwable1);
/*     */         }  
/*     */       throw throwable;
/*     */     } 
/*     */   }
/*     */   
/*     */   private static DataInputStream createDecompressorStream(InputStream in) throws IOException {
/*  41 */     return new DataInputStream((InputStream)new FastBufferedInputStream(new GZIPInputStream(in)));
/*     */   }
/*     */   
/*     */   private static DataOutputStream createCompressorStream(OutputStream out) throws IOException {
/*  45 */     return new DataOutputStream(new BufferedOutputStream(new GZIPOutputStream(out)));
/*     */   }
/*     */   
/*     */   public static CompoundTag readCompressed(InputStream in, NbtAccounter accounter) throws IOException {
/*  49 */     DataInputStream dis = createDecompressorStream(in);
/*     */     try {
/*  50 */       CompoundTag compoundTag = read(dis, accounter);
/*  51 */       if (dis != null)
/*  51 */         dis.close(); 
/*     */       return compoundTag;
/*     */     } catch (Throwable throwable) {
/*     */       if (dis != null)
/*     */         try {
/*     */           dis.close();
/*     */         } catch (Throwable throwable1) {
/*     */           throwable.addSuppressed(throwable1);
/*     */         }  
/*     */       throw throwable;
/*     */     } 
/*     */   }
/*     */   
/*     */   public static void parseCompressed(Path file, StreamTagVisitor output, NbtAccounter accounter) throws IOException {
/*  56 */     InputStream rawInput = Files.newInputStream(file, new OpenOption[0]);
/*     */     try {
/*  57 */       FastBufferedInputStream fastBufferedInputStream = new FastBufferedInputStream(rawInput);
/*     */       try {
/*  59 */         parseCompressed((InputStream)fastBufferedInputStream, output, accounter);
/*  60 */         fastBufferedInputStream.close();
/*     */       } catch (Throwable throwable) {
/*     */         try {
/*     */           fastBufferedInputStream.close();
/*     */         } catch (Throwable throwable1) {
/*     */           throwable.addSuppressed(throwable1);
/*     */         } 
/*     */         throw throwable;
/*     */       } 
/*  60 */       if (rawInput != null)
/*  60 */         rawInput.close(); 
/*     */     } catch (Throwable throwable) {
/*     */       if (rawInput != null)
/*     */         try {
/*     */           rawInput.close();
/*     */         } catch (Throwable throwable1) {
/*     */           throwable.addSuppressed(throwable1);
/*     */         }  
/*     */       throw throwable;
/*     */     } 
/*     */   }
/*     */   
/*     */   public static void parseCompressed(InputStream in, StreamTagVisitor output, NbtAccounter accounter) throws IOException {
/*  64 */     DataInputStream dis = createDecompressorStream(in);
/*     */     try {
/*  65 */       parse(dis, output, accounter);
/*  66 */       if (dis != null)
/*  66 */         dis.close(); 
/*     */     } catch (Throwable throwable) {
/*     */       if (dis != null)
/*     */         try {
/*     */           dis.close();
/*     */         } catch (Throwable throwable1) {
/*     */           throwable.addSuppressed(throwable1);
/*     */         }  
/*     */       throw throwable;
/*     */     } 
/*     */   }
/*     */   
/*     */   public static void writeCompressed(CompoundTag tag, Path file) throws IOException {
/*  70 */     OutputStream out = Files.newOutputStream(file, SYNC_OUTPUT_OPTIONS);
/*     */     try {
/*  71 */       OutputStream bufferedOut = new BufferedOutputStream(out);
/*     */       try {
/*  72 */         writeCompressed(tag, bufferedOut);
/*  73 */         bufferedOut.close();
/*     */       } catch (Throwable throwable) {
/*     */         try {
/*     */           bufferedOut.close();
/*     */         } catch (Throwable throwable1) {
/*     */           throwable.addSuppressed(throwable1);
/*     */         } 
/*     */         throw throwable;
/*     */       } 
/*  73 */       if (out != null)
/*  73 */         out.close(); 
/*     */     } catch (Throwable throwable) {
/*     */       if (out != null)
/*     */         try {
/*     */           out.close();
/*     */         } catch (Throwable throwable1) {
/*     */           throwable.addSuppressed(throwable1);
/*     */         }  
/*     */       throw throwable;
/*     */     } 
/*     */   }
/*     */   
/*     */   public static void writeCompressed(CompoundTag tag, OutputStream out) throws IOException {
/*  77 */     DataOutputStream dos = createCompressorStream(out);
/*     */     try {
/*  78 */       write(tag, dos);
/*  79 */       if (dos != null)
/*  79 */         dos.close(); 
/*     */     } catch (Throwable throwable) {
/*     */       if (dos != null)
/*     */         try {
/*     */           dos.close();
/*     */         } catch (Throwable throwable1) {
/*     */           throwable.addSuppressed(throwable1);
/*     */         }  
/*     */       throw throwable;
/*     */     } 
/*     */   }
/*     */   
/*     */   public static void write(CompoundTag tag, Path file) throws IOException {
/*  84 */     OutputStream out = Files.newOutputStream(file, SYNC_OUTPUT_OPTIONS);
/*     */     try {
/*  85 */       OutputStream bufferedOut = new BufferedOutputStream(out);
/*     */       try {
/*  86 */         DataOutputStream dos = new DataOutputStream(bufferedOut);
/*     */         try {
/*  88 */           write(tag, dos);
/*  89 */           dos.close();
/*     */         } catch (Throwable throwable) {
/*     */           try {
/*     */             dos.close();
/*     */           } catch (Throwable throwable1) {
/*     */             throwable.addSuppressed(throwable1);
/*     */           } 
/*     */           throw throwable;
/*     */         } 
/*  89 */         bufferedOut.close();
/*     */       } catch (Throwable throwable) {
/*     */         try {
/*     */           bufferedOut.close();
/*     */         } catch (Throwable throwable1) {
/*     */           throwable.addSuppressed(throwable1);
/*     */         } 
/*     */         throw throwable;
/*     */       } 
/*  89 */       if (out != null)
/*  89 */         out.close(); 
/*     */     } catch (Throwable throwable) {
/*     */       if (out != null)
/*     */         try {
/*     */           out.close();
/*     */         } catch (Throwable throwable1) {
/*     */           throwable.addSuppressed(throwable1);
/*     */         }  
/*     */       throw throwable;
/*     */     } 
/*     */   }
/*     */   
/*     */   public static CompoundTag read(Path file) throws IOException {
/*  93 */     if (!Files.exists(file, new java.nio.file.LinkOption[0]))
/*  94 */       return null; 
/*  97 */     InputStream in = Files.newInputStream(file, new OpenOption[0]);
/*     */     try {
/*  98 */       DataInputStream dis = new DataInputStream(in);
/*     */       try {
/* 100 */         CompoundTag compoundTag = read(dis, NbtAccounter.unlimitedHeap());
/* 101 */         dis.close();
/* 101 */         if (in != null)
/* 101 */           in.close(); 
/*     */         return compoundTag;
/*     */       } catch (Throwable throwable) {
/*     */         try {
/*     */           dis.close();
/*     */         } catch (Throwable throwable1) {
/*     */           throwable.addSuppressed(throwable1);
/*     */         } 
/*     */         throw throwable;
/*     */       } 
/*     */     } catch (Throwable throwable) {
/*     */       if (in != null)
/*     */         try {
/*     */           in.close();
/*     */         } catch (Throwable throwable1) {
/*     */           throwable.addSuppressed(throwable1);
/*     */         }  
/*     */       throw throwable;
/*     */     } 
/*     */   }
/*     */   
/*     */   public static CompoundTag read(DataInput input) throws IOException {
/* 105 */     return read(input, NbtAccounter.unlimitedHeap());
/*     */   }
/*     */   
/*     */   public static CompoundTag read(DataInput input, NbtAccounter accounter) throws IOException {
/* 109 */     Tag tag = readUnnamedTag(input, accounter);
/* 110 */     if (tag instanceof CompoundTag) {
/* 110 */       CompoundTag compoundTag = (CompoundTag)tag;
/* 111 */       return compoundTag;
/*     */     } 
/* 113 */     throw new IOException("Root tag must be a named compound tag");
/*     */   }
/*     */   
/*     */   public static void write(CompoundTag tag, DataOutput output) throws IOException {
/* 117 */     writeUnnamedTagWithFallback(tag, output);
/*     */   }
/*     */   
/*     */   public static void parse(DataInput input, StreamTagVisitor output, NbtAccounter accounter) throws IOException {
/* 121 */     TagType<?> type = TagTypes.getType(input.readByte());
/* 122 */     if (type == EndTag.TYPE) {
/* 123 */       if (output.visitRootEntry(EndTag.TYPE) == StreamTagVisitor.ValueResult.CONTINUE)
/* 124 */         output.visitEnd(); 
/*     */       return;
/*     */     } 
/* 129 */     switch (output.visitRootEntry(type)) {
/*     */       case BREAK:
/* 133 */         StringTag.skipString(input);
/* 134 */         type.skip(input, accounter);
/*     */         break;
/*     */       case CONTINUE:
/* 137 */         StringTag.skipString(input);
/* 138 */         type.parse(input, output, accounter);
/*     */         break;
/*     */     } 
/*     */   }
/*     */   
/*     */   public static Tag readAnyTag(DataInput input, NbtAccounter accounter) throws IOException {
/* 144 */     byte type = input.readByte();
/* 145 */     if (type == 0)
/* 146 */       return EndTag.INSTANCE; 
/* 148 */     return readTagSafe(input, accounter, type);
/*     */   }
/*     */   
/*     */   public static void writeAnyTag(Tag tag, DataOutput output) throws IOException {
/* 152 */     output.writeByte(tag.getId());
/* 153 */     if (tag.getId() == 0)
/*     */       return; 
/* 156 */     tag.write(output);
/*     */   }
/*     */   
/*     */   public static void writeUnnamedTag(Tag tag, DataOutput output) throws IOException {
/* 160 */     output.writeByte(tag.getId());
/* 161 */     if (tag.getId() == 0)
/*     */       return; 
/* 166 */     output.writeUTF("");
/* 168 */     tag.write(output);
/*     */   }
/*     */   
/*     */   public static void writeUnnamedTagWithFallback(Tag tag, DataOutput output) throws IOException {
/* 172 */     writeUnnamedTag(tag, (DataOutput)new StringFallbackDataOutput(output));
/*     */   }
/*     */   
/*     */   @VisibleForTesting
/*     */   public static Tag readUnnamedTag(DataInput input, NbtAccounter accounter) throws IOException {
/* 177 */     byte type = input.readByte();
/* 178 */     if (type == 0)
/* 179 */       return EndTag.INSTANCE; 
/* 183 */     StringTag.skipString(input);
/* 185 */     return readTagSafe(input, accounter, type);
/*     */   }
/*     */   
/*     */   private static Tag readTagSafe(DataInput input, NbtAccounter accounter, byte type) {
/*     */     try {
/* 190 */       return (Tag)TagTypes.getType(type).load(input, accounter);
/* 191 */     } catch (IOException e) {
/* 192 */       CrashReport report = CrashReport.forThrowable(e, "Loading NBT data");
/* 193 */       CrashReportCategory category = report.addCategory("NBT Tag");
/* 194 */       category.setDetail("Tag type", Byte.valueOf(type));
/* 195 */       throw new ReportedNbtException(report);
/*     */     } 
/*     */   }
/*     */   
/*     */   public static class StringFallbackDataOutput extends DelegateDataOutput {
/*     */     public StringFallbackDataOutput(DataOutput parent) {
/* 201 */       super(parent);
/*     */     }
/*     */     
/*     */     public void writeUTF(String s) throws IOException {
/*     */       try {
/* 207 */         super.writeUTF(s);
/* 208 */       } catch (UTFDataFormatException exception) {
/* 209 */         Util.logAndPauseIfInIde("Failed to write NBT String", exception);
/* 211 */         super.writeUTF("");
/*     */       } 
/*     */     }
/*     */   }
/*     */ }


/* Location:              C:\Users\karol\AppData\Roaming\ModrinthApp\meta\versions\26.2-0.19.5\26.2-0.19.5.jar!\net\minecraft\nbt\NbtIo.class
 * Java compiler version: 25 (69.0)
 * JD-Core Version:       1.1.3
 */