/*    */ package net.minecraft.client.gui.font.providers;
/*    */ 
/*    */ import com.mojang.datafixers.kinds.App;
/*    */ import com.mojang.datafixers.kinds.Applicative;
/*    */ import com.mojang.datafixers.util.Either;
/*    */ import com.mojang.serialization.MapCodec;
/*    */ import com.mojang.serialization.codecs.RecordCodecBuilder;
/*    */ import java.util.function.Function;
/*    */ import net.minecraft.resources.Identifier;
/*    */ 
/*    */ public final class ProviderReferenceDefinition extends Record implements GlyphProviderDefinition {
/*    */   private final Identifier id;
/*    */   
/*    */   public static final MapCodec<ProviderReferenceDefinition> CODEC;
/*    */   
/*    */   public ProviderReferenceDefinition(Identifier id) {
/*  8 */     this.id = id;
/*    */   }
/*    */   
/*    */   public final String toString() {
/*    */     // Byte code:
/*    */     //   0: aload_0
/*    */     //   1: <illegal opcode> toString : (Lnet/minecraft/client/gui/font/providers/ProviderReferenceDefinition;)Ljava/lang/String;
/*    */     //   6: areturn
/*    */     // Line number table:
/*    */     //   Java source line number -> byte code offset
/*    */     //   #8	-> 0
/*    */     // Local variable table:
/*    */     //   start	length	slot	name	descriptor
/*    */     //   0	7	0	this	Lnet/minecraft/client/gui/font/providers/ProviderReferenceDefinition;
/*    */   }
/*    */   
/*    */   public final int hashCode() {
/*    */     // Byte code:
/*    */     //   0: aload_0
/*    */     //   1: <illegal opcode> hashCode : (Lnet/minecraft/client/gui/font/providers/ProviderReferenceDefinition;)I
/*    */     //   6: ireturn
/*    */     // Line number table:
/*    */     //   Java source line number -> byte code offset
/*    */     //   #8	-> 0
/*    */     // Local variable table:
/*    */     //   start	length	slot	name	descriptor
/*    */     //   0	7	0	this	Lnet/minecraft/client/gui/font/providers/ProviderReferenceDefinition;
/*    */   }
/*    */   
/*    */   public final boolean equals(Object o) {
/*    */     // Byte code:
/*    */     //   0: aload_0
/*    */     //   1: aload_1
/*    */     //   2: <illegal opcode> equals : (Lnet/minecraft/client/gui/font/providers/ProviderReferenceDefinition;Ljava/lang/Object;)Z
/*    */     //   7: ireturn
/*    */     // Line number table:
/*    */     //   Java source line number -> byte code offset
/*    */     //   #8	-> 0
/*    */     // Local variable table:
/*    */     //   start	length	slot	name	descriptor
/*    */     //   0	8	0	this	Lnet/minecraft/client/gui/font/providers/ProviderReferenceDefinition;
/*    */     //   0	8	1	o	Ljava/lang/Object;
/*    */   }
/*    */   
/*    */   public Identifier id() {
/*  8 */     return this.id;
/*    */   }
/*    */   
/*    */   static {
/*  9 */     CODEC = RecordCodecBuilder.mapCodec(i -> i.group((App)Identifier.CODEC.fieldOf("id").forGetter(ProviderReferenceDefinition::id)).apply((Applicative)i, ProviderReferenceDefinition::new));
/*    */   }
/*    */   
/*    */   public GlyphProviderType type() {
/* 15 */     return GlyphProviderType.REFERENCE;
/*    */   }
/*    */   
/*    */   public Either<GlyphProviderDefinition.Loader, GlyphProviderDefinition.Reference> unpack() {
/* 20 */     return Either.right(new GlyphProviderDefinition.Reference(this.id));
/*    */   }
/*    */ }


/* Location:              C:\Users\karol\AppData\Roaming\ModrinthApp\meta\versions\26.2-0.19.5\26.2-0.19.5.jar!\net\minecraft\client\gui\font\providers\ProviderReferenceDefinition.class
 * Java compiler version: 25 (69.0)
 * JD-Core Version:       1.1.3
 */