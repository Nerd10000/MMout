/*    */ package net.minecraft.server.dialog;
/*    */ 
/*    */ import com.mojang.datafixers.kinds.App;
/*    */ import com.mojang.datafixers.kinds.Applicative;
/*    */ import com.mojang.serialization.Codec;
/*    */ import com.mojang.serialization.codecs.RecordCodecBuilder;
/*    */ import java.util.function.BiFunction;
/*    */ import net.minecraft.server.dialog.action.ParsedTemplate;
/*    */ import net.minecraft.server.dialog.input.InputControl;
/*    */ 
/*    */ public final class Input extends Record {
/*    */   private final String key;
/*    */   
/*    */   private final InputControl control;
/*    */   
/*    */   public static final Codec<Input> CODEC;
/*    */   
/*    */   public Input(String key, InputControl control) {
/*  8 */     this.key = key;
/*  8 */     this.control = control;
/*    */   }
/*    */   
/*    */   public final String toString() {
/*    */     // Byte code:
/*    */     //   0: aload_0
/*    */     //   1: <illegal opcode> toString : (Lnet/minecraft/server/dialog/Input;)Ljava/lang/String;
/*    */     //   6: areturn
/*    */     // Line number table:
/*    */     //   Java source line number -> byte code offset
/*    */     //   #8	-> 0
/*    */     // Local variable table:
/*    */     //   start	length	slot	name	descriptor
/*    */     //   0	7	0	this	Lnet/minecraft/server/dialog/Input;
/*    */   }
/*    */   
/*    */   public final int hashCode() {
/*    */     // Byte code:
/*    */     //   0: aload_0
/*    */     //   1: <illegal opcode> hashCode : (Lnet/minecraft/server/dialog/Input;)I
/*    */     //   6: ireturn
/*    */     // Line number table:
/*    */     //   Java source line number -> byte code offset
/*    */     //   #8	-> 0
/*    */     // Local variable table:
/*    */     //   start	length	slot	name	descriptor
/*    */     //   0	7	0	this	Lnet/minecraft/server/dialog/Input;
/*    */   }
/*    */   
/*    */   public final boolean equals(Object o) {
/*    */     // Byte code:
/*    */     //   0: aload_0
/*    */     //   1: aload_1
/*    */     //   2: <illegal opcode> equals : (Lnet/minecraft/server/dialog/Input;Ljava/lang/Object;)Z
/*    */     //   7: ireturn
/*    */     // Line number table:
/*    */     //   Java source line number -> byte code offset
/*    */     //   #8	-> 0
/*    */     // Local variable table:
/*    */     //   start	length	slot	name	descriptor
/*    */     //   0	8	0	this	Lnet/minecraft/server/dialog/Input;
/*    */     //   0	8	1	o	Ljava/lang/Object;
/*    */   }
/*    */   
/*    */   public String key() {
/*  8 */     return this.key;
/*    */   }
/*    */   
/*    */   public InputControl control() {
/*  8 */     return this.control;
/*    */   }
/*    */   
/*    */   static {
/* 12 */     CODEC = RecordCodecBuilder.create(i -> i.group((App)ParsedTemplate.VARIABLE_CODEC.fieldOf("key").forGetter(Input::key), (App)InputControl.MAP_CODEC.forGetter(Input::control)).apply((Applicative)i, Input::new));
/*    */   }
/*    */ }


/* Location:              C:\Users\karol\AppData\Roaming\ModrinthApp\meta\versions\26.2-0.19.5\26.2-0.19.5.jar!\net\minecraft\server\dialog\Input.class
 * Java compiler version: 25 (69.0)
 * JD-Core Version:       1.1.3
 */