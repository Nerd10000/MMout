/*     */ package net.minecraft.nbt;
/*     */ 
/*     */ import java.io.DataOutput;
/*     */ import java.io.IOException;
/*     */ import java.io.UTFDataFormatException;
/*     */ import net.minecraft.util.DelegateDataOutput;
/*     */ import net.minecraft.util.Util;
/*     */ 
/*     */ public class StringFallbackDataOutput extends DelegateDataOutput {
/*     */   public StringFallbackDataOutput(DataOutput parent) {
/* 201 */     super(parent);
/*     */   }
/*     */   
/*     */   public void writeUTF(String s) throws IOException {
/*     */     try {
/* 207 */       super.writeUTF(s);
/* 208 */     } catch (UTFDataFormatException exception) {
/* 209 */       Util.logAndPauseIfInIde("Failed to write NBT String", exception);
/* 211 */       super.writeUTF("");
/*     */     } 
/*     */   }
/*     */ }


/* Location:              C:\Users\karol\AppData\Roaming\ModrinthApp\meta\versions\26.2-0.19.5\26.2-0.19.5.jar!\net\minecraft\nbt\NbtIo$StringFallbackDataOutput.class
 * Java compiler version: 25 (69.0)
 * JD-Core Version:       1.1.3
 */