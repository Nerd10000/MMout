/*    */ package net.minecraft.server.dialog.input;
/*    */ 
/*    */ import com.mojang.datafixers.kinds.App;
/*    */ import com.mojang.datafixers.kinds.Applicative;
/*    */ import com.mojang.datafixers.util.Function6;
/*    */ import com.mojang.serialization.Codec;
/*    */ import com.mojang.serialization.DataResult;
/*    */ import com.mojang.serialization.MapCodec;
/*    */ import com.mojang.serialization.codecs.RecordCodecBuilder;
/*    */ import java.util.Optional;
/*    */ import java.util.function.BiFunction;
/*    */ import net.minecraft.network.chat.Component;
/*    */ import net.minecraft.network.chat.ComponentSerialization;
/*    */ import net.minecraft.server.dialog.Dialog;
/*    */ import net.minecraft.util.ExtraCodecs;
/*    */ 
/*    */ public final class TextInput extends Record implements InputControl {
/*    */   private final int width;
/*    */   
/*    */   private final Component label;
/*    */   
/*    */   private final boolean labelVisible;
/*    */   
/*    */   private final String initial;
/*    */   
/*    */   private final int maxLength;
/*    */   
/*    */   private final Optional<MultilineOptions> multiline;
/*    */   
/*    */   public static final MapCodec<TextInput> MAP_CODEC;
/*    */   
/*    */   public TextInput(int width, Component label, boolean labelVisible, String initial, int maxLength, Optional<MultilineOptions> multiline) {
/* 15 */     this.width = width;
/* 15 */     this.label = label;
/* 15 */     this.labelVisible = labelVisible;
/* 15 */     this.initial = initial;
/* 15 */     this.maxLength = maxLength;
/* 15 */     this.multiline = multiline;
/*    */   }
/*    */   
/*    */   public final String toString() {
/*    */     // Byte code:
/*    */     //   0: aload_0
/*    */     //   1: <illegal opcode> toString : (Lnet/minecraft/server/dialog/input/TextInput;)Ljava/lang/String;
/*    */     //   6: areturn
/*    */     // Line number table:
/*    */     //   Java source line number -> byte code offset
/*    */     //   #15	-> 0
/*    */     // Local variable table:
/*    */     //   start	length	slot	name	descriptor
/*    */     //   0	7	0	this	Lnet/minecraft/server/dialog/input/TextInput;
/*    */   }
/*    */   
/*    */   public final int hashCode() {
/*    */     // Byte code:
/*    */     //   0: aload_0
/*    */     //   1: <illegal opcode> hashCode : (Lnet/minecraft/server/dialog/input/TextInput;)I
/*    */     //   6: ireturn
/*    */     // Line number table:
/*    */     //   Java source line number -> byte code offset
/*    */     //   #15	-> 0
/*    */     // Local variable table:
/*    */     //   start	length	slot	name	descriptor
/*    */     //   0	7	0	this	Lnet/minecraft/server/dialog/input/TextInput;
/*    */   }
/*    */   
/*    */   public final boolean equals(Object o) {
/*    */     // Byte code:
/*    */     //   0: aload_0
/*    */     //   1: aload_1
/*    */     //   2: <illegal opcode> equals : (Lnet/minecraft/server/dialog/input/TextInput;Ljava/lang/Object;)Z
/*    */     //   7: ireturn
/*    */     // Line number table:
/*    */     //   Java source line number -> byte code offset
/*    */     //   #15	-> 0
/*    */     // Local variable table:
/*    */     //   start	length	slot	name	descriptor
/*    */     //   0	8	0	this	Lnet/minecraft/server/dialog/input/TextInput;
/*    */     //   0	8	1	o	Ljava/lang/Object;
/*    */   }
/*    */   
/*    */   public int width() {
/* 15 */     return this.width;
/*    */   }
/*    */   
/*    */   public Component label() {
/* 15 */     return this.label;
/*    */   }
/*    */   
/*    */   public boolean labelVisible() {
/* 15 */     return this.labelVisible;
/*    */   }
/*    */   
/*    */   public String initial() {
/* 15 */     return this.initial;
/*    */   }
/*    */   
/*    */   public int maxLength() {
/* 15 */     return this.maxLength;
/*    */   }
/*    */   
/*    */   public Optional<MultilineOptions> multiline() {
/* 15 */     return this.multiline;
/*    */   }
/*    */   
/*    */   static {
/* 32 */     MAP_CODEC = RecordCodecBuilder.mapCodec(i -> i.group((App)Dialog.WIDTH_CODEC.optionalFieldOf("width", Integer.valueOf(200)).forGetter(TextInput::width), (App)ComponentSerialization.CODEC.fieldOf("label").forGetter(TextInput::label), (App)Codec.BOOL.optionalFieldOf("label_visible", Boolean.valueOf(true)).forGetter(TextInput::labelVisible), (App)Codec.STRING.optionalFieldOf("initial", "").forGetter(TextInput::initial), (App)ExtraCodecs.POSITIVE_INT.optionalFieldOf("max_length", Integer.valueOf(32)).forGetter(TextInput::maxLength), (App)MultilineOptions.CODEC.optionalFieldOf("multiline").forGetter(TextInput::multiline)).apply((Applicative)i, TextInput::new)).validate(o -> (o.initial.length() > o.maxLength()) ? DataResult.error(()) : DataResult.success(o));
/*    */   }
/*    */   
/*    */   public static final class MultilineOptions extends Record {
/*    */     private final Optional<Integer> maxLines;
/*    */     
/*    */     private final Optional<Integer> height;
/*    */     
/*    */     public static final int MAX_HEIGHT = 512;
/*    */     
/*    */     public static final Codec<MultilineOptions> CODEC;
/*    */     
/*    */     public MultilineOptions(Optional<Integer> maxLines, Optional<Integer> height) {
/* 40 */       this.maxLines = maxLines;
/* 40 */       this.height = height;
/*    */     }
/*    */     
/*    */     public final String toString() {
/*    */       // Byte code:
/*    */       //   0: aload_0
/*    */       //   1: <illegal opcode> toString : (Lnet/minecraft/server/dialog/input/TextInput$MultilineOptions;)Ljava/lang/String;
/*    */       //   6: areturn
/*    */       // Line number table:
/*    */       //   Java source line number -> byte code offset
/*    */       //   #40	-> 0
/*    */       // Local variable table:
/*    */       //   start	length	slot	name	descriptor
/*    */       //   0	7	0	this	Lnet/minecraft/server/dialog/input/TextInput$MultilineOptions;
/*    */     }
/*    */     
/*    */     public final int hashCode() {
/*    */       // Byte code:
/*    */       //   0: aload_0
/*    */       //   1: <illegal opcode> hashCode : (Lnet/minecraft/server/dialog/input/TextInput$MultilineOptions;)I
/*    */       //   6: ireturn
/*    */       // Line number table:
/*    */       //   Java source line number -> byte code offset
/*    */       //   #40	-> 0
/*    */       // Local variable table:
/*    */       //   start	length	slot	name	descriptor
/*    */       //   0	7	0	this	Lnet/minecraft/server/dialog/input/TextInput$MultilineOptions;
/*    */     }
/*    */     
/*    */     public final boolean equals(Object o) {
/*    */       // Byte code:
/*    */       //   0: aload_0
/*    */       //   1: aload_1
/*    */       //   2: <illegal opcode> equals : (Lnet/minecraft/server/dialog/input/TextInput$MultilineOptions;Ljava/lang/Object;)Z
/*    */       //   7: ireturn
/*    */       // Line number table:
/*    */       //   Java source line number -> byte code offset
/*    */       //   #40	-> 0
/*    */       // Local variable table:
/*    */       //   start	length	slot	name	descriptor
/*    */       //   0	8	0	this	Lnet/minecraft/server/dialog/input/TextInput$MultilineOptions;
/*    */       //   0	8	1	o	Ljava/lang/Object;
/*    */     }
/*    */     
/*    */     public Optional<Integer> maxLines() {
/* 40 */       return this.maxLines;
/*    */     }
/*    */     
/*    */     public Optional<Integer> height() {
/* 40 */       return this.height;
/*    */     }
/*    */     
/*    */     static {
/* 45 */       CODEC = RecordCodecBuilder.create(i -> i.group((App)ExtraCodecs.POSITIVE_INT.optionalFieldOf("max_lines").forGetter(MultilineOptions::maxLines), (App)ExtraCodecs.intRange(1, 512).optionalFieldOf("height").forGetter(MultilineOptions::height)).apply((Applicative)i, MultilineOptions::new));
/*    */     }
/*    */   }
/*    */   
/*    */   public MapCodec<TextInput> mapCodec() {
/* 53 */     return MAP_CODEC;
/*    */   }
/*    */ }


/* Location:              C:\Users\karol\AppData\Roaming\ModrinthApp\meta\versions\26.2-0.19.5\26.2-0.19.5.jar!\net\minecraft\server\dialog\input\TextInput.class
 * Java compiler version: 25 (69.0)
 * JD-Core Version:       1.1.3
 */