/*    */ package com.mojang.blaze3d.audio;
/*    */ 
/*    */ import java.nio.ByteBuffer;
/*    */ import java.util.OptionalInt;
/*    */ import javax.sound.sampled.AudioFormat;
/*    */ import org.lwjgl.openal.AL10;
/*    */ 
/*    */ public class SoundBuffer {
/*    */   private ByteBuffer data;
/*    */   
/*    */   private final AudioFormat format;
/*    */   
/*    */   private boolean hasAlBuffer;
/*    */   
/*    */   private int alBuffer;
/*    */   
/*    */   private final int size;
/*    */   
/*    */   public SoundBuffer(ByteBuffer data, AudioFormat format) {
/* 23 */     this.data = data;
/* 24 */     this.format = format;
/* 25 */     this.size = data.limit();
/*    */   }
/*    */   
/*    */   OptionalInt getAlBuffer() {
/* 30 */     if (!this.hasAlBuffer) {
/* 31 */       if (this.data == null)
/* 32 */         return OptionalInt.empty(); 
/* 34 */       int audioFormat = OpenAlUtil.audioFormatToOpenAl(this.format);
/* 35 */       int[] intBuffer = new int[1];
/* 36 */       AL10.alGenBuffers(intBuffer);
/* 37 */       if (OpenAlUtil.checkALError("Creating buffer"))
/* 38 */         return OptionalInt.empty(); 
/* 40 */       AL10.alBufferData(intBuffer[0], audioFormat, this.data, (int)this.format.getSampleRate());
/* 41 */       if (OpenAlUtil.checkALError("Assigning buffer data"))
/* 42 */         return OptionalInt.empty(); 
/* 44 */       this.alBuffer = intBuffer[0];
/* 45 */       this.hasAlBuffer = true;
/* 46 */       this.data = null;
/*    */     } 
/* 49 */     return OptionalInt.of(this.alBuffer);
/*    */   }
/*    */   
/*    */   public void discardAlBuffer() {
/* 53 */     if (this.hasAlBuffer) {
/* 54 */       AL10.alDeleteBuffers(new int[] { this.alBuffer });
/* 55 */       if (OpenAlUtil.checkALError("Deleting stream buffers"))
/*    */         return; 
/*    */     } 
/* 59 */     this.hasAlBuffer = false;
/*    */   }
/*    */   
/*    */   public OptionalInt releaseAlBuffer() {
/* 63 */     OptionalInt result = getAlBuffer();
/* 64 */     this.hasAlBuffer = false;
/* 65 */     return result;
/*    */   }
/*    */   
/*    */   public AudioFormat format() {
/* 69 */     return this.format;
/*    */   }
/*    */   
/*    */   public int size() {
/* 73 */     return this.size;
/*    */   }
/*    */   
/*    */   public boolean isValid() {
/* 77 */     return (this.data != null || this.hasAlBuffer);
/*    */   }
/*    */ }


/* Location:              C:\Users\karol\AppData\Roaming\ModrinthApp\meta\versions\26.2-0.19.5\26.2-0.19.5.jar!\com\mojang\blaze3d\audio\SoundBuffer.class
 * Java compiler version: 25 (69.0)
 * JD-Core Version:       1.1.3
 */