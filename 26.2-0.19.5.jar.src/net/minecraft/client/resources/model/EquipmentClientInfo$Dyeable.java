/*    */ package net.minecraft.client.resources.model;
/*    */ 
/*    */ import com.mojang.datafixers.kinds.App;
/*    */ import com.mojang.datafixers.kinds.Applicative;
/*    */ import com.mojang.serialization.Codec;
/*    */ import com.mojang.serialization.codecs.RecordCodecBuilder;
/*    */ import java.util.Optional;
/*    */ import java.util.function.Function;
/*    */ import net.minecraft.util.ExtraCodecs;
/*    */ 
/*    */ public final class Dyeable extends Record {
/*    */   private final Optional<Integer> colorWhenUndyed;
/*    */   
/*    */   public static final Codec<Dyeable> CODEC;
/*    */   
/*    */   public Dyeable(Optional<Integer> colorWhenUndyed) {
/* 62 */     this.colorWhenUndyed = colorWhenUndyed;
/*    */   }
/*    */   
/*    */   public final String toString() {
/*    */     // Byte code:
/*    */     //   0: aload_0
/*    */     //   1: <illegal opcode> toString : (Lnet/minecraft/client/resources/model/EquipmentClientInfo$Dyeable;)Ljava/lang/String;
/*    */     //   6: areturn
/*    */     // Line number table:
/*    */     //   Java source line number -> byte code offset
/*    */     //   #62	-> 0
/*    */     // Local variable table:
/*    */     //   start	length	slot	name	descriptor
/*    */     //   0	7	0	this	Lnet/minecraft/client/resources/model/EquipmentClientInfo$Dyeable;
/*    */   }
/*    */   
/*    */   public final int hashCode() {
/*    */     // Byte code:
/*    */     //   0: aload_0
/*    */     //   1: <illegal opcode> hashCode : (Lnet/minecraft/client/resources/model/EquipmentClientInfo$Dyeable;)I
/*    */     //   6: ireturn
/*    */     // Line number table:
/*    */     //   Java source line number -> byte code offset
/*    */     //   #62	-> 0
/*    */     // Local variable table:
/*    */     //   start	length	slot	name	descriptor
/*    */     //   0	7	0	this	Lnet/minecraft/client/resources/model/EquipmentClientInfo$Dyeable;
/*    */   }
/*    */   
/*    */   public final boolean equals(Object o) {
/*    */     // Byte code:
/*    */     //   0: aload_0
/*    */     //   1: aload_1
/*    */     //   2: <illegal opcode> equals : (Lnet/minecraft/client/resources/model/EquipmentClientInfo$Dyeable;Ljava/lang/Object;)Z
/*    */     //   7: ireturn
/*    */     // Line number table:
/*    */     //   Java source line number -> byte code offset
/*    */     //   #62	-> 0
/*    */     // Local variable table:
/*    */     //   start	length	slot	name	descriptor
/*    */     //   0	8	0	this	Lnet/minecraft/client/resources/model/EquipmentClientInfo$Dyeable;
/*    */     //   0	8	1	o	Ljava/lang/Object;
/*    */   }
/*    */   
/*    */   public Optional<Integer> colorWhenUndyed() {
/* 62 */     return this.colorWhenUndyed;
/*    */   }
/*    */   
/*    */   static {
/* 65 */     CODEC = RecordCodecBuilder.create(i -> i.group((App)ExtraCodecs.RGB_COLOR_CODEC.optionalFieldOf("color_when_undyed").forGetter(Dyeable::colorWhenUndyed)).apply((Applicative)i, Dyeable::new));
/*    */   }
/*    */ }


/* Location:              C:\Users\karol\AppData\Roaming\ModrinthApp\meta\versions\26.2-0.19.5\26.2-0.19.5.jar!\net\minecraft\client\resources\model\EquipmentClientInfo$Dyeable.class
 * Java compiler version: 25 (69.0)
 * JD-Core Version:       1.1.3
 */