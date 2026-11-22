/*    */ package net.minecraft.world.scores;
/*    */ 
/*    */ import java.util.Objects;
/*    */ import net.minecraft.network.chat.Component;
/*    */ import net.minecraft.network.chat.MutableComponent;
/*    */ import net.minecraft.network.chat.numbers.NumberFormat;
/*    */ 
/*    */ public final class PlayerScoreEntry extends Record {
/*    */   private final String owner;
/*    */   
/*    */   private final int value;
/*    */   
/*    */   private final Component display;
/*    */   
/*    */   private final NumberFormat numberFormatOverride;
/*    */   
/*    */   public PlayerScoreEntry(String owner, int value, Component display, NumberFormat numberFormatOverride) {
/* 10 */     this.owner = owner;
/* 10 */     this.value = value;
/* 10 */     this.display = display;
/* 10 */     this.numberFormatOverride = numberFormatOverride;
/*    */   }
/*    */   
/*    */   public final String toString() {
/*    */     // Byte code:
/*    */     //   0: aload_0
/*    */     //   1: <illegal opcode> toString : (Lnet/minecraft/world/scores/PlayerScoreEntry;)Ljava/lang/String;
/*    */     //   6: areturn
/*    */     // Line number table:
/*    */     //   Java source line number -> byte code offset
/*    */     //   #10	-> 0
/*    */     // Local variable table:
/*    */     //   start	length	slot	name	descriptor
/*    */     //   0	7	0	this	Lnet/minecraft/world/scores/PlayerScoreEntry;
/*    */   }
/*    */   
/*    */   public final int hashCode() {
/*    */     // Byte code:
/*    */     //   0: aload_0
/*    */     //   1: <illegal opcode> hashCode : (Lnet/minecraft/world/scores/PlayerScoreEntry;)I
/*    */     //   6: ireturn
/*    */     // Line number table:
/*    */     //   Java source line number -> byte code offset
/*    */     //   #10	-> 0
/*    */     // Local variable table:
/*    */     //   start	length	slot	name	descriptor
/*    */     //   0	7	0	this	Lnet/minecraft/world/scores/PlayerScoreEntry;
/*    */   }
/*    */   
/*    */   public final boolean equals(Object o) {
/*    */     // Byte code:
/*    */     //   0: aload_0
/*    */     //   1: aload_1
/*    */     //   2: <illegal opcode> equals : (Lnet/minecraft/world/scores/PlayerScoreEntry;Ljava/lang/Object;)Z
/*    */     //   7: ireturn
/*    */     // Line number table:
/*    */     //   Java source line number -> byte code offset
/*    */     //   #10	-> 0
/*    */     // Local variable table:
/*    */     //   start	length	slot	name	descriptor
/*    */     //   0	8	0	this	Lnet/minecraft/world/scores/PlayerScoreEntry;
/*    */     //   0	8	1	o	Ljava/lang/Object;
/*    */   }
/*    */   
/*    */   public String owner() {
/* 10 */     return this.owner;
/*    */   }
/*    */   
/*    */   public int value() {
/* 10 */     return this.value;
/*    */   }
/*    */   
/*    */   public Component display() {
/* 10 */     return this.display;
/*    */   }
/*    */   
/*    */   public NumberFormat numberFormatOverride() {
/* 10 */     return this.numberFormatOverride;
/*    */   }
/*    */   
/*    */   public boolean isHidden() {
/* 12 */     return this.owner.startsWith("#");
/*    */   }
/*    */   
/*    */   public Component ownerName() {
/* 16 */     if (this.display != null)
/* 17 */       return this.display; 
/* 19 */     return (Component)Component.literal(owner());
/*    */   }
/*    */   
/*    */   public MutableComponent formatValue(NumberFormat _default) {
/* 23 */     return ((NumberFormat)Objects.<NumberFormat>requireNonNullElse(this.numberFormatOverride, _default)).format(this.value);
/*    */   }
/*    */ }


/* Location:              C:\Users\karol\AppData\Roaming\ModrinthApp\meta\versions\26.2-0.19.5\26.2-0.19.5.jar!\net\minecraft\world\scores\PlayerScoreEntry.class
 * Java compiler version: 25 (69.0)
 * JD-Core Version:       1.1.3
 */