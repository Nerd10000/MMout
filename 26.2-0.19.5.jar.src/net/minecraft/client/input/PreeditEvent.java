/*    */ package net.minecraft.client.input;
/*    */ 
/*    */ import com.google.common.base.Preconditions;
/*    */ import com.google.common.collect.ImmutableList;
/*    */ import java.nio.IntBuffer;
/*    */ import java.util.List;
/*    */ import net.minecraft.network.chat.Component;
/*    */ import net.minecraft.network.chat.MutableComponent;
/*    */ import net.minecraft.network.chat.Style;
/*    */ import org.lwjgl.system.MemoryUtil;
/*    */ 
/*    */ public final class PreeditEvent extends Record {
/*    */   private final String fullText;
/*    */   
/*    */   private final int caretPosition;
/*    */   
/*    */   private final List<String> blocks;
/*    */   
/*    */   private final int focusedBlock;
/*    */   
/*    */   public final String toString() {
/*    */     // Byte code:
/*    */     //   0: aload_0
/*    */     //   1: <illegal opcode> toString : (Lnet/minecraft/client/input/PreeditEvent;)Ljava/lang/String;
/*    */     //   6: areturn
/*    */     // Line number table:
/*    */     //   Java source line number -> byte code offset
/*    */     //   #14	-> 0
/*    */     // Local variable table:
/*    */     //   start	length	slot	name	descriptor
/*    */     //   0	7	0	this	Lnet/minecraft/client/input/PreeditEvent;
/*    */   }
/*    */   
/*    */   public final int hashCode() {
/*    */     // Byte code:
/*    */     //   0: aload_0
/*    */     //   1: <illegal opcode> hashCode : (Lnet/minecraft/client/input/PreeditEvent;)I
/*    */     //   6: ireturn
/*    */     // Line number table:
/*    */     //   Java source line number -> byte code offset
/*    */     //   #14	-> 0
/*    */     // Local variable table:
/*    */     //   start	length	slot	name	descriptor
/*    */     //   0	7	0	this	Lnet/minecraft/client/input/PreeditEvent;
/*    */   }
/*    */   
/*    */   public final boolean equals(Object o) {
/*    */     // Byte code:
/*    */     //   0: aload_0
/*    */     //   1: aload_1
/*    */     //   2: <illegal opcode> equals : (Lnet/minecraft/client/input/PreeditEvent;Ljava/lang/Object;)Z
/*    */     //   7: ireturn
/*    */     // Line number table:
/*    */     //   Java source line number -> byte code offset
/*    */     //   #14	-> 0
/*    */     // Local variable table:
/*    */     //   start	length	slot	name	descriptor
/*    */     //   0	8	0	this	Lnet/minecraft/client/input/PreeditEvent;
/*    */     //   0	8	1	o	Ljava/lang/Object;
/*    */   }
/*    */   
/*    */   public String fullText() {
/* 14 */     return this.fullText;
/*    */   }
/*    */   
/*    */   public int caretPosition() {
/* 14 */     return this.caretPosition;
/*    */   }
/*    */   
/*    */   public List<String> blocks() {
/* 14 */     return this.blocks;
/*    */   }
/*    */   
/*    */   public int focusedBlock() {
/* 14 */     return this.focusedBlock;
/*    */   }
/*    */   
/*    */   public PreeditEvent(String fullText, int caretPosition, List<String> blocks, int focusedBlock) {
/* 21 */     Preconditions.checkElementIndex(focusedBlock, blocks.size());
/*    */     this.fullText = fullText;
/*    */     this.caretPosition = caretPosition;
/*    */     this.blocks = blocks;
/*    */     this.focusedBlock = focusedBlock;
/*    */   }
/*    */   
/*    */   public static PreeditEvent createFromCallback(int preeditSize, long preeditPtr, int blockCount, long blockSizesPtr, int focusedBlock, int caret) {
/* 25 */     if (preeditSize == 0)
/* 26 */       return null; 
/* 28 */     int[] codepoints = readIntBuffer(preeditSize, preeditPtr);
/* 29 */     int[] blockSizes = readIntBuffer(blockCount, blockSizesPtr);
/* 34 */     StringBuilder fullText = new StringBuilder();
/* 36 */     ImmutableList.Builder<String> blocks = ImmutableList.builder();
/* 37 */     int offset = 0;
/* 38 */     int convertedCaret = 0;
/* 40 */     for (int blockSize : blockSizes) {
/* 41 */       StringBuilder blockBuilder = new StringBuilder();
/* 42 */       for (int i = 0; i < blockSize; i++) {
/* 43 */         int codepoint = codepoints[offset];
/* 44 */         if (offset == caret)
/* 45 */           convertedCaret = fullText.length() + blockBuilder.length(); 
/* 47 */         blockBuilder.appendCodePoint(codepoint);
/* 48 */         offset++;
/*    */       } 
/* 50 */       String block = blockBuilder.toString();
/* 51 */       blocks.add(block);
/* 52 */       fullText.append(block);
/*    */     } 
/* 55 */     if (offset == caret)
/* 56 */       convertedCaret = fullText.length(); 
/* 59 */     return new PreeditEvent(fullText.toString(), convertedCaret, (List<String>)blocks.build(), focusedBlock);
/*    */   }
/*    */   
/*    */   private static int[] readIntBuffer(int size, long ptr) {
/* 63 */     IntBuffer buffer = MemoryUtil.memIntBuffer(ptr, size);
/* 64 */     int[] result = new int[size];
/* 65 */     buffer.get(result);
/* 66 */     return result;
/*    */   }
/*    */   
/*    */   public MutableComponent toFormattedText(Style focusedStyle) {
/* 70 */     int blockCount = this.blocks.size();
/* 71 */     if (blockCount == 1)
/* 72 */       return Component.literal(this.blocks.getFirst()).withStyle(focusedStyle); 
/* 74 */     MutableComponent result = Component.empty();
/* 75 */     for (int i = 0; i < blockCount; i++) {
/* 76 */       MutableComponent part = Component.literal(this.blocks.get(i));
/* 77 */       if (i == this.focusedBlock)
/* 78 */         part.withStyle(focusedStyle); 
/* 80 */       result.append((Component)part);
/*    */     } 
/* 82 */     return result;
/*    */   }
/*    */ }


/* Location:              C:\Users\karol\AppData\Roaming\ModrinthApp\meta\versions\26.2-0.19.5\26.2-0.19.5.jar!\net\minecraft\client\input\PreeditEvent.class
 * Java compiler version: 25 (69.0)
 * JD-Core Version:       1.1.3
 */