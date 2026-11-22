/*    */ package net.minecraft.server.dialog;
/*    */ 
/*    */ import com.mojang.datafixers.kinds.App;
/*    */ import com.mojang.datafixers.kinds.Applicative;
/*    */ import com.mojang.datafixers.util.Function3;
/*    */ import com.mojang.serialization.MapCodec;
/*    */ import com.mojang.serialization.codecs.RecordCodecBuilder;
/*    */ import java.util.Optional;
/*    */ import net.minecraft.network.chat.Component;
/*    */ import net.minecraft.network.chat.ComponentSerialization;
/*    */ 
/*    */ public final class CommonButtonData extends Record {
/*    */   private final Component label;
/*    */   
/*    */   private final Optional<Component> tooltip;
/*    */   
/*    */   private final int width;
/*    */   
/*    */   public static final int DEFAULT_WIDTH = 150;
/*    */   
/*    */   public static final MapCodec<CommonButtonData> MAP_CODEC;
/*    */   
/*    */   public CommonButtonData(Component label, Optional<Component> tooltip, int width) {
/* 10 */     this.label = label;
/* 10 */     this.tooltip = tooltip;
/* 10 */     this.width = width;
/*    */   }
/*    */   
/*    */   public final String toString() {
/*    */     // Byte code:
/*    */     //   0: aload_0
/*    */     //   1: <illegal opcode> toString : (Lnet/minecraft/server/dialog/CommonButtonData;)Ljava/lang/String;
/*    */     //   6: areturn
/*    */     // Line number table:
/*    */     //   Java source line number -> byte code offset
/*    */     //   #10	-> 0
/*    */     // Local variable table:
/*    */     //   start	length	slot	name	descriptor
/*    */     //   0	7	0	this	Lnet/minecraft/server/dialog/CommonButtonData;
/*    */   }
/*    */   
/*    */   public final int hashCode() {
/*    */     // Byte code:
/*    */     //   0: aload_0
/*    */     //   1: <illegal opcode> hashCode : (Lnet/minecraft/server/dialog/CommonButtonData;)I
/*    */     //   6: ireturn
/*    */     // Line number table:
/*    */     //   Java source line number -> byte code offset
/*    */     //   #10	-> 0
/*    */     // Local variable table:
/*    */     //   start	length	slot	name	descriptor
/*    */     //   0	7	0	this	Lnet/minecraft/server/dialog/CommonButtonData;
/*    */   }
/*    */   
/*    */   public final boolean equals(Object o) {
/*    */     // Byte code:
/*    */     //   0: aload_0
/*    */     //   1: aload_1
/*    */     //   2: <illegal opcode> equals : (Lnet/minecraft/server/dialog/CommonButtonData;Ljava/lang/Object;)Z
/*    */     //   7: ireturn
/*    */     // Line number table:
/*    */     //   Java source line number -> byte code offset
/*    */     //   #10	-> 0
/*    */     // Local variable table:
/*    */     //   start	length	slot	name	descriptor
/*    */     //   0	8	0	this	Lnet/minecraft/server/dialog/CommonButtonData;
/*    */     //   0	8	1	o	Ljava/lang/Object;
/*    */   }
/*    */   
/*    */   public Component label() {
/* 10 */     return this.label;
/*    */   }
/*    */   
/*    */   public Optional<Component> tooltip() {
/* 10 */     return this.tooltip;
/*    */   }
/*    */   
/*    */   public int width() {
/* 10 */     return this.width;
/*    */   }
/*    */   
/*    */   static {
/* 18 */     MAP_CODEC = RecordCodecBuilder.mapCodec(i -> i.group((App)ComponentSerialization.CODEC.fieldOf("label").forGetter(CommonButtonData::label), (App)ComponentSerialization.CODEC.optionalFieldOf("tooltip").forGetter(CommonButtonData::tooltip), (App)Dialog.WIDTH_CODEC.optionalFieldOf("width", Integer.valueOf(150)).forGetter(CommonButtonData::width)).apply((Applicative)i, CommonButtonData::new));
/*    */   }
/*    */   
/*    */   public CommonButtonData(Component label, int width) {
/* 25 */     this(label, Optional.empty(), width);
/*    */   }
/*    */ }


/* Location:              C:\Users\karol\AppData\Roaming\ModrinthApp\meta\versions\26.2-0.19.5\26.2-0.19.5.jar!\net\minecraft\server\dialog\CommonButtonData.class
 * Java compiler version: 25 (69.0)
 * JD-Core Version:       1.1.3
 */