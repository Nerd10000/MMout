/*    */ package net.minecraft.client.gui.font;
/*    */ 
/*    */ import net.minecraft.resources.Identifier;
/*    */ 
/*    */ final class BuilderId extends Record {
/*    */   private final Identifier fontId;
/*    */   
/*    */   private final String pack;
/*    */   
/*    */   private final int index;
/*    */   
/*    */   private BuilderId(Identifier fontId, String pack, int index) {
/* 91 */     this.fontId = fontId;
/* 91 */     this.pack = pack;
/* 91 */     this.index = index;
/*    */   }
/*    */   
/*    */   public final int hashCode() {
/*    */     // Byte code:
/*    */     //   0: aload_0
/*    */     //   1: <illegal opcode> hashCode : (Lnet/minecraft/client/gui/font/FontManager$BuilderId;)I
/*    */     //   6: ireturn
/*    */     // Line number table:
/*    */     //   Java source line number -> byte code offset
/*    */     //   #91	-> 0
/*    */     // Local variable table:
/*    */     //   start	length	slot	name	descriptor
/*    */     //   0	7	0	this	Lnet/minecraft/client/gui/font/FontManager$BuilderId;
/*    */   }
/*    */   
/*    */   public final boolean equals(Object o) {
/*    */     // Byte code:
/*    */     //   0: aload_0
/*    */     //   1: aload_1
/*    */     //   2: <illegal opcode> equals : (Lnet/minecraft/client/gui/font/FontManager$BuilderId;Ljava/lang/Object;)Z
/*    */     //   7: ireturn
/*    */     // Line number table:
/*    */     //   Java source line number -> byte code offset
/*    */     //   #91	-> 0
/*    */     // Local variable table:
/*    */     //   start	length	slot	name	descriptor
/*    */     //   0	8	0	this	Lnet/minecraft/client/gui/font/FontManager$BuilderId;
/*    */     //   0	8	1	o	Ljava/lang/Object;
/*    */   }
/*    */   
/*    */   public Identifier fontId() {
/* 91 */     return this.fontId;
/*    */   }
/*    */   
/*    */   public String pack() {
/* 91 */     return this.pack;
/*    */   }
/*    */   
/*    */   public int index() {
/* 91 */     return this.index;
/*    */   }
/*    */   
/*    */   public String toString() {
/* 94 */     return "(" + String.valueOf(this.fontId) + ": builder #" + this.index + " from pack " + this.pack + ")";
/*    */   }
/*    */ }


/* Location:              C:\Users\karol\AppData\Roaming\ModrinthApp\meta\versions\26.2-0.19.5\26.2-0.19.5.jar!\net\minecraft\client\gui\font\FontManager$BuilderId.class
 * Java compiler version: 25 (69.0)
 * JD-Core Version:       1.1.3
 */