/*    */ package net.minecraft.server.dialog.input;
/*    */ 
/*    */ import com.mojang.datafixers.kinds.App;
/*    */ import com.mojang.datafixers.kinds.Applicative;
/*    */ import com.mojang.datafixers.util.Function4;
/*    */ import com.mojang.serialization.Codec;
/*    */ import com.mojang.serialization.MapCodec;
/*    */ import com.mojang.serialization.codecs.RecordCodecBuilder;
/*    */ import net.minecraft.network.chat.Component;
/*    */ import net.minecraft.network.chat.ComponentSerialization;
/*    */ 
/*    */ public final class BooleanInput extends Record implements InputControl {
/*    */   private final Component label;
/*    */   
/*    */   private final boolean initial;
/*    */   
/*    */   private final String onTrue;
/*    */   
/*    */   private final String onFalse;
/*    */   
/*    */   public static final MapCodec<BooleanInput> MAP_CODEC;
/*    */   
/*    */   public BooleanInput(Component label, boolean initial, String onTrue, String onFalse) {
/*  9 */     this.label = label;
/*  9 */     this.initial = initial;
/*  9 */     this.onTrue = onTrue;
/*  9 */     this.onFalse = onFalse;
/*    */   }
/*    */   
/*    */   public final String toString() {
/*    */     // Byte code:
/*    */     //   0: aload_0
/*    */     //   1: <illegal opcode> toString : (Lnet/minecraft/server/dialog/input/BooleanInput;)Ljava/lang/String;
/*    */     //   6: areturn
/*    */     // Line number table:
/*    */     //   Java source line number -> byte code offset
/*    */     //   #9	-> 0
/*    */     // Local variable table:
/*    */     //   start	length	slot	name	descriptor
/*    */     //   0	7	0	this	Lnet/minecraft/server/dialog/input/BooleanInput;
/*    */   }
/*    */   
/*    */   public final int hashCode() {
/*    */     // Byte code:
/*    */     //   0: aload_0
/*    */     //   1: <illegal opcode> hashCode : (Lnet/minecraft/server/dialog/input/BooleanInput;)I
/*    */     //   6: ireturn
/*    */     // Line number table:
/*    */     //   Java source line number -> byte code offset
/*    */     //   #9	-> 0
/*    */     // Local variable table:
/*    */     //   start	length	slot	name	descriptor
/*    */     //   0	7	0	this	Lnet/minecraft/server/dialog/input/BooleanInput;
/*    */   }
/*    */   
/*    */   public final boolean equals(Object o) {
/*    */     // Byte code:
/*    */     //   0: aload_0
/*    */     //   1: aload_1
/*    */     //   2: <illegal opcode> equals : (Lnet/minecraft/server/dialog/input/BooleanInput;Ljava/lang/Object;)Z
/*    */     //   7: ireturn
/*    */     // Line number table:
/*    */     //   Java source line number -> byte code offset
/*    */     //   #9	-> 0
/*    */     // Local variable table:
/*    */     //   start	length	slot	name	descriptor
/*    */     //   0	8	0	this	Lnet/minecraft/server/dialog/input/BooleanInput;
/*    */     //   0	8	1	o	Ljava/lang/Object;
/*    */   }
/*    */   
/*    */   public Component label() {
/*  9 */     return this.label;
/*    */   }
/*    */   
/*    */   public boolean initial() {
/*  9 */     return this.initial;
/*    */   }
/*    */   
/*    */   public String onTrue() {
/*  9 */     return this.onTrue;
/*    */   }
/*    */   
/*    */   public String onFalse() {
/*  9 */     return this.onFalse;
/*    */   }
/*    */   
/*    */   static {
/* 16 */     MAP_CODEC = RecordCodecBuilder.mapCodec(i -> i.group((App)ComponentSerialization.CODEC.fieldOf("label").forGetter(BooleanInput::label), (App)Codec.BOOL.optionalFieldOf("initial", Boolean.valueOf(false)).forGetter(BooleanInput::initial), (App)Codec.STRING.optionalFieldOf("on_true", "true").forGetter(BooleanInput::onTrue), (App)Codec.STRING.optionalFieldOf("on_false", "false").forGetter(BooleanInput::onFalse)).apply((Applicative)i, BooleanInput::new));
/*    */   }
/*    */   
/*    */   public MapCodec<BooleanInput> mapCodec() {
/* 25 */     return MAP_CODEC;
/*    */   }
/*    */ }


/* Location:              C:\Users\karol\AppData\Roaming\ModrinthApp\meta\versions\26.2-0.19.5\26.2-0.19.5.jar!\net\minecraft\server\dialog\input\BooleanInput.class
 * Java compiler version: 25 (69.0)
 * JD-Core Version:       1.1.3
 */