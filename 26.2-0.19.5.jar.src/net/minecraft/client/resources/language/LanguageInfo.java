/*    */ package net.minecraft.client.resources.language;
/*    */ 
/*    */ import com.mojang.datafixers.kinds.App;
/*    */ import com.mojang.datafixers.kinds.Applicative;
/*    */ import com.mojang.datafixers.util.Function3;
/*    */ import com.mojang.serialization.Codec;
/*    */ import com.mojang.serialization.codecs.RecordCodecBuilder;
/*    */ import net.minecraft.network.chat.Component;
/*    */ import net.minecraft.util.ExtraCodecs;
/*    */ 
/*    */ public final class LanguageInfo extends Record {
/*    */   private final String region;
/*    */   
/*    */   private final String name;
/*    */   
/*    */   private final boolean bidirectional;
/*    */   
/*    */   public static final Codec<LanguageInfo> CODEC;
/*    */   
/*    */   public LanguageInfo(String region, String name, boolean bidirectional) {
/*  9 */     this.region = region;
/*  9 */     this.name = name;
/*  9 */     this.bidirectional = bidirectional;
/*    */   }
/*    */   
/*    */   public final String toString() {
/*    */     // Byte code:
/*    */     //   0: aload_0
/*    */     //   1: <illegal opcode> toString : (Lnet/minecraft/client/resources/language/LanguageInfo;)Ljava/lang/String;
/*    */     //   6: areturn
/*    */     // Line number table:
/*    */     //   Java source line number -> byte code offset
/*    */     //   #9	-> 0
/*    */     // Local variable table:
/*    */     //   start	length	slot	name	descriptor
/*    */     //   0	7	0	this	Lnet/minecraft/client/resources/language/LanguageInfo;
/*    */   }
/*    */   
/*    */   public final int hashCode() {
/*    */     // Byte code:
/*    */     //   0: aload_0
/*    */     //   1: <illegal opcode> hashCode : (Lnet/minecraft/client/resources/language/LanguageInfo;)I
/*    */     //   6: ireturn
/*    */     // Line number table:
/*    */     //   Java source line number -> byte code offset
/*    */     //   #9	-> 0
/*    */     // Local variable table:
/*    */     //   start	length	slot	name	descriptor
/*    */     //   0	7	0	this	Lnet/minecraft/client/resources/language/LanguageInfo;
/*    */   }
/*    */   
/*    */   public final boolean equals(Object o) {
/*    */     // Byte code:
/*    */     //   0: aload_0
/*    */     //   1: aload_1
/*    */     //   2: <illegal opcode> equals : (Lnet/minecraft/client/resources/language/LanguageInfo;Ljava/lang/Object;)Z
/*    */     //   7: ireturn
/*    */     // Line number table:
/*    */     //   Java source line number -> byte code offset
/*    */     //   #9	-> 0
/*    */     // Local variable table:
/*    */     //   start	length	slot	name	descriptor
/*    */     //   0	8	0	this	Lnet/minecraft/client/resources/language/LanguageInfo;
/*    */     //   0	8	1	o	Ljava/lang/Object;
/*    */   }
/*    */   
/*    */   public String region() {
/*  9 */     return this.region;
/*    */   }
/*    */   
/*    */   public String name() {
/*  9 */     return this.name;
/*    */   }
/*    */   
/*    */   public boolean bidirectional() {
/*  9 */     return this.bidirectional;
/*    */   }
/*    */   
/*    */   static {
/* 14 */     CODEC = RecordCodecBuilder.create(i -> i.group((App)ExtraCodecs.NON_EMPTY_STRING.fieldOf("region").forGetter(LanguageInfo::region), (App)ExtraCodecs.NON_EMPTY_STRING.fieldOf("name").forGetter(LanguageInfo::name), (App)Codec.BOOL.optionalFieldOf("bidirectional", Boolean.valueOf(false)).forGetter(LanguageInfo::bidirectional)).apply((Applicative)i, LanguageInfo::new));
/*    */   }
/*    */   
/*    */   public Component toComponent() {
/* 21 */     return (Component)Component.literal(this.name + " (" + this.name + ")");
/*    */   }
/*    */ }


/* Location:              C:\Users\karol\AppData\Roaming\ModrinthApp\meta\versions\26.2-0.19.5\26.2-0.19.5.jar!\net\minecraft\client\resources\language\LanguageInfo.class
 * Java compiler version: 25 (69.0)
 * JD-Core Version:       1.1.3
 */