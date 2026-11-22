/*    */ package net.minecraft.network.chat.contents;
/*    */ 
/*    */ import com.mojang.brigadier.exceptions.CommandSyntaxException;
/*    */ import com.mojang.datafixers.kinds.App;
/*    */ import com.mojang.datafixers.kinds.Applicative;
/*    */ import com.mojang.serialization.MapCodec;
/*    */ import com.mojang.serialization.codecs.RecordCodecBuilder;
/*    */ import java.util.Optional;
/*    */ import java.util.function.BiFunction;
/*    */ import net.minecraft.network.chat.Component;
/*    */ import net.minecraft.network.chat.ComponentContents;
/*    */ import net.minecraft.network.chat.ComponentSerialization;
/*    */ import net.minecraft.network.chat.ComponentUtils;
/*    */ import net.minecraft.network.chat.FormattedText;
/*    */ import net.minecraft.network.chat.MutableComponent;
/*    */ import net.minecraft.network.chat.ResolutionContext;
/*    */ import net.minecraft.network.chat.Style;
/*    */ import net.minecraft.network.chat.contents.objects.ObjectInfo;
/*    */ import net.minecraft.network.chat.contents.objects.ObjectInfos;
/*    */ 
/*    */ public final class ObjectContents extends Record implements ComponentContents {
/*    */   private final ObjectInfo contents;
/*    */   
/*    */   private final Optional<Component> fallback;
/*    */   
/*    */   public ObjectContents(ObjectInfo contents, Optional<Component> fallback) {
/* 19 */     this.contents = contents;
/* 19 */     this.fallback = fallback;
/*    */   }
/*    */   
/*    */   public final String toString() {
/*    */     // Byte code:
/*    */     //   0: aload_0
/*    */     //   1: <illegal opcode> toString : (Lnet/minecraft/network/chat/contents/ObjectContents;)Ljava/lang/String;
/*    */     //   6: areturn
/*    */     // Line number table:
/*    */     //   Java source line number -> byte code offset
/*    */     //   #19	-> 0
/*    */     // Local variable table:
/*    */     //   start	length	slot	name	descriptor
/*    */     //   0	7	0	this	Lnet/minecraft/network/chat/contents/ObjectContents;
/*    */   }
/*    */   
/*    */   public final int hashCode() {
/*    */     // Byte code:
/*    */     //   0: aload_0
/*    */     //   1: <illegal opcode> hashCode : (Lnet/minecraft/network/chat/contents/ObjectContents;)I
/*    */     //   6: ireturn
/*    */     // Line number table:
/*    */     //   Java source line number -> byte code offset
/*    */     //   #19	-> 0
/*    */     // Local variable table:
/*    */     //   start	length	slot	name	descriptor
/*    */     //   0	7	0	this	Lnet/minecraft/network/chat/contents/ObjectContents;
/*    */   }
/*    */   
/*    */   public final boolean equals(Object o) {
/*    */     // Byte code:
/*    */     //   0: aload_0
/*    */     //   1: aload_1
/*    */     //   2: <illegal opcode> equals : (Lnet/minecraft/network/chat/contents/ObjectContents;Ljava/lang/Object;)Z
/*    */     //   7: ireturn
/*    */     // Line number table:
/*    */     //   Java source line number -> byte code offset
/*    */     //   #19	-> 0
/*    */     // Local variable table:
/*    */     //   start	length	slot	name	descriptor
/*    */     //   0	8	0	this	Lnet/minecraft/network/chat/contents/ObjectContents;
/*    */     //   0	8	1	o	Ljava/lang/Object;
/*    */   }
/*    */   
/*    */   public ObjectInfo contents() {
/* 19 */     return this.contents;
/*    */   }
/*    */   
/*    */   public Optional<Component> fallback() {
/* 19 */     return this.fallback;
/*    */   }
/*    */   
/* 23 */   private static final String PLACEHOLDER = Character.toString('￼');
/*    */   
/*    */   public static final MapCodec<ObjectContents> MAP_CODEC;
/*    */   
/*    */   static {
/* 25 */     MAP_CODEC = RecordCodecBuilder.mapCodec(i -> i.group((App)ObjectInfos.CODEC.forGetter(ObjectContents::contents), (App)ComponentSerialization.CODEC.optionalFieldOf("fallback").forGetter(ObjectContents::fallback)).apply((Applicative)i, ObjectContents::new));
/*    */   }
/*    */   
/*    */   public MapCodec<ObjectContents> codec() {
/* 32 */     return MAP_CODEC;
/*    */   }
/*    */   
/*    */   public MutableComponent resolve(ResolutionContext context, int recursionDepth) throws CommandSyntaxException {
/* 37 */     Optional<MutableComponent> fallback = ComponentUtils.resolve(context, this.fallback, recursionDepth);
/* 39 */     ObjectInfo validatedContents = context.validate(this.contents);
/* 40 */     if (validatedContents == null)
/* 41 */       return fallback.orElseGet(() -> Component.literal(this.contents.defaultFallback())); 
/* 44 */     return MutableComponent.create(new ObjectContents(validatedContents, fallback.map(o -> o)));
/*    */   }
/*    */   
/*    */   public <T> Optional<T> visit(FormattedText.ContentConsumer<T> output) {
/* 49 */     if (this.fallback.isPresent())
/* 50 */       return ((Component)this.fallback.get()).visit(output); 
/* 52 */     return output.accept(this.contents.defaultFallback());
/*    */   }
/*    */   
/*    */   public <T> Optional<T> visit(FormattedText.StyledContentConsumer<T> output, Style currentStyle) {
/* 57 */     return output.accept(currentStyle.withFont(this.contents.fontDescription()), PLACEHOLDER);
/*    */   }
/*    */ }


/* Location:              C:\Users\karol\AppData\Roaming\ModrinthApp\meta\versions\26.2-0.19.5\26.2-0.19.5.jar!\net\minecraft\network\chat\contents\ObjectContents.class
 * Java compiler version: 25 (69.0)
 * JD-Core Version:       1.1.3
 */