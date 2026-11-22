/*    */ package net.minecraft.world.entity.player;
/*    */ 
/*    */ import net.minecraft.network.FriendlyByteBuf;
/*    */ import net.minecraft.network.codec.StreamCodec;
/*    */ 
/*    */ public final class Input extends Record {
/*    */   private final boolean forward;
/*    */   
/*    */   private final boolean backward;
/*    */   
/*    */   private final boolean left;
/*    */   
/*    */   private final boolean right;
/*    */   
/*    */   private final boolean jump;
/*    */   
/*    */   private final boolean shift;
/*    */   
/*    */   private final boolean sprint;
/*    */   
/*    */   private static final byte FLAG_FORWARD = 1;
/*    */   
/*    */   private static final byte FLAG_BACKWARD = 2;
/*    */   
/*    */   private static final byte FLAG_LEFT = 4;
/*    */   
/*    */   private static final byte FLAG_RIGHT = 8;
/*    */   
/*    */   private static final byte FLAG_JUMP = 16;
/*    */   
/*    */   private static final byte FLAG_SHIFT = 32;
/*    */   
/*    */   private static final byte FLAG_SPRINT = 64;
/*    */   
/*    */   public Input(boolean forward, boolean backward, boolean left, boolean right, boolean jump, boolean shift, boolean sprint) {
/*  6 */     this.forward = forward;
/*  6 */     this.backward = backward;
/*  6 */     this.left = left;
/*  6 */     this.right = right;
/*  6 */     this.jump = jump;
/*  6 */     this.shift = shift;
/*  6 */     this.sprint = sprint;
/*    */   }
/*    */   
/*    */   public final String toString() {
/*    */     // Byte code:
/*    */     //   0: aload_0
/*    */     //   1: <illegal opcode> toString : (Lnet/minecraft/world/entity/player/Input;)Ljava/lang/String;
/*    */     //   6: areturn
/*    */     // Line number table:
/*    */     //   Java source line number -> byte code offset
/*    */     //   #6	-> 0
/*    */     // Local variable table:
/*    */     //   start	length	slot	name	descriptor
/*    */     //   0	7	0	this	Lnet/minecraft/world/entity/player/Input;
/*    */   }
/*    */   
/*    */   public final int hashCode() {
/*    */     // Byte code:
/*    */     //   0: aload_0
/*    */     //   1: <illegal opcode> hashCode : (Lnet/minecraft/world/entity/player/Input;)I
/*    */     //   6: ireturn
/*    */     // Line number table:
/*    */     //   Java source line number -> byte code offset
/*    */     //   #6	-> 0
/*    */     // Local variable table:
/*    */     //   start	length	slot	name	descriptor
/*    */     //   0	7	0	this	Lnet/minecraft/world/entity/player/Input;
/*    */   }
/*    */   
/*    */   public final boolean equals(Object o) {
/*    */     // Byte code:
/*    */     //   0: aload_0
/*    */     //   1: aload_1
/*    */     //   2: <illegal opcode> equals : (Lnet/minecraft/world/entity/player/Input;Ljava/lang/Object;)Z
/*    */     //   7: ireturn
/*    */     // Line number table:
/*    */     //   Java source line number -> byte code offset
/*    */     //   #6	-> 0
/*    */     // Local variable table:
/*    */     //   start	length	slot	name	descriptor
/*    */     //   0	8	0	this	Lnet/minecraft/world/entity/player/Input;
/*    */     //   0	8	1	o	Ljava/lang/Object;
/*    */   }
/*    */   
/*    */   public boolean forward() {
/*  6 */     return this.forward;
/*    */   }
/*    */   
/*    */   public boolean backward() {
/*  6 */     return this.backward;
/*    */   }
/*    */   
/*    */   public boolean left() {
/*  6 */     return this.left;
/*    */   }
/*    */   
/*    */   public boolean right() {
/*  6 */     return this.right;
/*    */   }
/*    */   
/*    */   public boolean jump() {
/*  6 */     return this.jump;
/*    */   }
/*    */   
/*    */   public boolean shift() {
/*  6 */     return this.shift;
/*    */   }
/*    */   
/*    */   public boolean sprint() {
/*  6 */     return this.sprint;
/*    */   }
/*    */   
/* 14 */   public static final StreamCodec<FriendlyByteBuf, Input> STREAM_CODEC = new StreamCodec<FriendlyByteBuf, Input>() {
/*    */       public void encode(FriendlyByteBuf output, Input value) {
/* 18 */         byte flags = 0;
/* 19 */         flags = (byte)(flags | (value.forward() ? 1 : 0));
/* 20 */         flags = (byte)(flags | (value.backward() ? 2 : 0));
/* 21 */         flags = (byte)(flags | (value.left() ? 4 : 0));
/* 22 */         flags = (byte)(flags | (value.right() ? 8 : 0));
/* 23 */         flags = (byte)(flags | (value.jump() ? 16 : 0));
/* 24 */         flags = (byte)(flags | (value.shift() ? 32 : 0));
/* 25 */         flags = (byte)(flags | (value.sprint() ? 64 : 0));
/* 26 */         output.writeByte(flags);
/*    */       }
/*    */       
/*    */       public Input decode(FriendlyByteBuf input) {
/* 31 */         byte flags = input.readByte();
/* 32 */         boolean forward = ((flags & 0x1) != 0);
/* 33 */         boolean backward = ((flags & 0x2) != 0);
/* 34 */         boolean left = ((flags & 0x4) != 0);
/* 35 */         boolean right = ((flags & 0x8) != 0);
/* 36 */         boolean jump = ((flags & 0x10) != 0);
/* 37 */         boolean shift = ((flags & 0x20) != 0);
/* 38 */         boolean sprint = ((flags & 0x40) != 0);
/* 39 */         return new Input(forward, backward, left, right, jump, shift, sprint);
/*    */       }
/*    */     };
/*    */   
/* 43 */   public static final Input EMPTY = new Input(false, false, false, false, false, false, false);
/*    */ }


/* Location:              C:\Users\karol\AppData\Roaming\ModrinthApp\meta\versions\26.2-0.19.5\26.2-0.19.5.jar!\net\minecraft\world\entity\player\Input.class
 * Java compiler version: 25 (69.0)
 * JD-Core Version:       1.1.3
 */