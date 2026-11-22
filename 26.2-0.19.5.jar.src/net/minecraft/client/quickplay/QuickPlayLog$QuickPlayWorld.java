/*     */ package net.minecraft.client.quickplay;
/*     */ 
/*     */ import com.mojang.datafixers.kinds.App;
/*     */ import com.mojang.datafixers.kinds.Applicative;
/*     */ import com.mojang.datafixers.util.Function3;
/*     */ import com.mojang.serialization.Codec;
/*     */ import com.mojang.serialization.MapCodec;
/*     */ import com.mojang.serialization.codecs.RecordCodecBuilder;
/*     */ import net.minecraft.util.ExtraCodecs;
/*     */ 
/*     */ final class QuickPlayWorld extends Record {
/*     */   private final QuickPlayLog.Type type;
/*     */   
/*     */   private final String id;
/*     */   
/*     */   private final String name;
/*     */   
/*     */   public static final MapCodec<QuickPlayWorld> MAP_CODEC;
/*     */   
/*     */   private QuickPlayWorld(QuickPlayLog.Type type, String id, String name) {
/* 104 */     this.type = type;
/* 104 */     this.id = id;
/* 104 */     this.name = name;
/*     */   }
/*     */   
/*     */   public final String toString() {
/*     */     // Byte code:
/*     */     //   0: aload_0
/*     */     //   1: <illegal opcode> toString : (Lnet/minecraft/client/quickplay/QuickPlayLog$QuickPlayWorld;)Ljava/lang/String;
/*     */     //   6: areturn
/*     */     // Line number table:
/*     */     //   Java source line number -> byte code offset
/*     */     //   #104	-> 0
/*     */     // Local variable table:
/*     */     //   start	length	slot	name	descriptor
/*     */     //   0	7	0	this	Lnet/minecraft/client/quickplay/QuickPlayLog$QuickPlayWorld;
/*     */   }
/*     */   
/*     */   public final int hashCode() {
/*     */     // Byte code:
/*     */     //   0: aload_0
/*     */     //   1: <illegal opcode> hashCode : (Lnet/minecraft/client/quickplay/QuickPlayLog$QuickPlayWorld;)I
/*     */     //   6: ireturn
/*     */     // Line number table:
/*     */     //   Java source line number -> byte code offset
/*     */     //   #104	-> 0
/*     */     // Local variable table:
/*     */     //   start	length	slot	name	descriptor
/*     */     //   0	7	0	this	Lnet/minecraft/client/quickplay/QuickPlayLog$QuickPlayWorld;
/*     */   }
/*     */   
/*     */   public final boolean equals(Object o) {
/*     */     // Byte code:
/*     */     //   0: aload_0
/*     */     //   1: aload_1
/*     */     //   2: <illegal opcode> equals : (Lnet/minecraft/client/quickplay/QuickPlayLog$QuickPlayWorld;Ljava/lang/Object;)Z
/*     */     //   7: ireturn
/*     */     // Line number table:
/*     */     //   Java source line number -> byte code offset
/*     */     //   #104	-> 0
/*     */     // Local variable table:
/*     */     //   start	length	slot	name	descriptor
/*     */     //   0	8	0	this	Lnet/minecraft/client/quickplay/QuickPlayLog$QuickPlayWorld;
/*     */     //   0	8	1	o	Ljava/lang/Object;
/*     */   }
/*     */   
/*     */   public QuickPlayLog.Type type() {
/* 104 */     return this.type;
/*     */   }
/*     */   
/*     */   public String id() {
/* 104 */     return this.id;
/*     */   }
/*     */   
/*     */   public String name() {
/* 104 */     return this.name;
/*     */   }
/*     */   
/*     */   static {
/* 105 */     MAP_CODEC = RecordCodecBuilder.mapCodec(i -> i.group((App)QuickPlayLog.Type.CODEC.fieldOf("type").forGetter(QuickPlayWorld::type), (App)ExtraCodecs.ESCAPED_STRING.fieldOf("id").forGetter(QuickPlayWorld::id), (App)Codec.STRING.fieldOf("name").forGetter(QuickPlayWorld::name)).apply((Applicative)i, QuickPlayWorld::new));
/*     */   }
/*     */ }


/* Location:              C:\Users\karol\AppData\Roaming\ModrinthApp\meta\versions\26.2-0.19.5\26.2-0.19.5.jar!\net\minecraft\client\quickplay\QuickPlayLog$QuickPlayWorld.class
 * Java compiler version: 25 (69.0)
 * JD-Core Version:       1.1.3
 */