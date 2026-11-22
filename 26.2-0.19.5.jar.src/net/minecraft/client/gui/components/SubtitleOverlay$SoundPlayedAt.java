/*     */ package net.minecraft.client.gui.components;
/*     */ 
/*     */ import net.minecraft.world.phys.Vec3;
/*     */ 
/*     */ final class SoundPlayedAt extends Record {
/*     */   private final Vec3 location;
/*     */   
/*     */   private final long time;
/*     */   
/*     */   private SoundPlayedAt(Vec3 location, long time) {
/* 147 */     this.location = location;
/* 147 */     this.time = time;
/*     */   }
/*     */   
/*     */   public final String toString() {
/*     */     // Byte code:
/*     */     //   0: aload_0
/*     */     //   1: <illegal opcode> toString : (Lnet/minecraft/client/gui/components/SubtitleOverlay$SoundPlayedAt;)Ljava/lang/String;
/*     */     //   6: areturn
/*     */     // Line number table:
/*     */     //   Java source line number -> byte code offset
/*     */     //   #147	-> 0
/*     */     // Local variable table:
/*     */     //   start	length	slot	name	descriptor
/*     */     //   0	7	0	this	Lnet/minecraft/client/gui/components/SubtitleOverlay$SoundPlayedAt;
/*     */   }
/*     */   
/*     */   public final int hashCode() {
/*     */     // Byte code:
/*     */     //   0: aload_0
/*     */     //   1: <illegal opcode> hashCode : (Lnet/minecraft/client/gui/components/SubtitleOverlay$SoundPlayedAt;)I
/*     */     //   6: ireturn
/*     */     // Line number table:
/*     */     //   Java source line number -> byte code offset
/*     */     //   #147	-> 0
/*     */     // Local variable table:
/*     */     //   start	length	slot	name	descriptor
/*     */     //   0	7	0	this	Lnet/minecraft/client/gui/components/SubtitleOverlay$SoundPlayedAt;
/*     */   }
/*     */   
/*     */   public final boolean equals(Object o) {
/*     */     // Byte code:
/*     */     //   0: aload_0
/*     */     //   1: aload_1
/*     */     //   2: <illegal opcode> equals : (Lnet/minecraft/client/gui/components/SubtitleOverlay$SoundPlayedAt;Ljava/lang/Object;)Z
/*     */     //   7: ireturn
/*     */     // Line number table:
/*     */     //   Java source line number -> byte code offset
/*     */     //   #147	-> 0
/*     */     // Local variable table:
/*     */     //   start	length	slot	name	descriptor
/*     */     //   0	8	0	this	Lnet/minecraft/client/gui/components/SubtitleOverlay$SoundPlayedAt;
/*     */     //   0	8	1	o	Ljava/lang/Object;
/*     */   }
/*     */   
/*     */   public Vec3 location() {
/* 147 */     return this.location;
/*     */   }
/*     */   
/*     */   public long time() {
/* 147 */     return this.time;
/*     */   }
/*     */ }


/* Location:              C:\Users\karol\AppData\Roaming\ModrinthApp\meta\versions\26.2-0.19.5\26.2-0.19.5.jar!\net\minecraft\client\gui\components\SubtitleOverlay$SoundPlayedAt.class
 * Java compiler version: 25 (69.0)
 * JD-Core Version:       1.1.3
 */