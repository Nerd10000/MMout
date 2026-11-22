/*    */ package net.minecraft.network.chat.contents;
/*    */ 
/*    */ import com.mojang.brigadier.exceptions.CommandSyntaxException;
/*    */ import com.mojang.datafixers.kinds.App;
/*    */ import com.mojang.datafixers.kinds.Applicative;
/*    */ import com.mojang.serialization.MapCodec;
/*    */ import com.mojang.serialization.codecs.RecordCodecBuilder;
/*    */ import java.util.Optional;
/*    */ import java.util.function.BiFunction;
/*    */ import net.minecraft.commands.CommandSourceStack;
/*    */ import net.minecraft.commands.arguments.selector.EntitySelector;
/*    */ import net.minecraft.network.chat.Component;
/*    */ import net.minecraft.network.chat.ComponentContents;
/*    */ import net.minecraft.network.chat.ComponentSerialization;
/*    */ import net.minecraft.network.chat.ComponentUtils;
/*    */ import net.minecraft.network.chat.FormattedText;
/*    */ import net.minecraft.network.chat.MutableComponent;
/*    */ import net.minecraft.network.chat.ResolutionContext;
/*    */ import net.minecraft.network.chat.Style;
/*    */ import net.minecraft.util.CompilableString;
/*    */ import net.minecraft.world.entity.Entity;
/*    */ 
/*    */ public final class SelectorContents extends Record implements ComponentContents {
/*    */   private final CompilableString<EntitySelector> selector;
/*    */   
/*    */   private final Optional<Component> separator;
/*    */   
/*    */   public static final MapCodec<SelectorContents> MAP_CODEC;
/*    */   
/*    */   public SelectorContents(CompilableString<EntitySelector> selector, Optional<Component> separator) {
/* 21 */     this.selector = selector;
/* 21 */     this.separator = separator;
/*    */   }
/*    */   
/*    */   public final int hashCode() {
/*    */     // Byte code:
/*    */     //   0: aload_0
/*    */     //   1: <illegal opcode> hashCode : (Lnet/minecraft/network/chat/contents/SelectorContents;)I
/*    */     //   6: ireturn
/*    */     // Line number table:
/*    */     //   Java source line number -> byte code offset
/*    */     //   #21	-> 0
/*    */     // Local variable table:
/*    */     //   start	length	slot	name	descriptor
/*    */     //   0	7	0	this	Lnet/minecraft/network/chat/contents/SelectorContents;
/*    */   }
/*    */   
/*    */   public final boolean equals(Object o) {
/*    */     // Byte code:
/*    */     //   0: aload_0
/*    */     //   1: aload_1
/*    */     //   2: <illegal opcode> equals : (Lnet/minecraft/network/chat/contents/SelectorContents;Ljava/lang/Object;)Z
/*    */     //   7: ireturn
/*    */     // Line number table:
/*    */     //   Java source line number -> byte code offset
/*    */     //   #21	-> 0
/*    */     // Local variable table:
/*    */     //   start	length	slot	name	descriptor
/*    */     //   0	8	0	this	Lnet/minecraft/network/chat/contents/SelectorContents;
/*    */     //   0	8	1	o	Ljava/lang/Object;
/*    */   }
/*    */   
/*    */   public CompilableString<EntitySelector> selector() {
/* 21 */     return this.selector;
/*    */   }
/*    */   
/*    */   public Optional<Component> separator() {
/* 21 */     return this.separator;
/*    */   }
/*    */   
/*    */   static {
/* 22 */     MAP_CODEC = RecordCodecBuilder.mapCodec(i -> i.group((App)EntitySelector.COMPILABLE_CODEC.fieldOf("selector").forGetter(SelectorContents::selector), (App)ComponentSerialization.CODEC.optionalFieldOf("separator").forGetter(SelectorContents::separator)).apply((Applicative)i, SelectorContents::new));
/*    */   }
/*    */   
/*    */   public MapCodec<SelectorContents> codec() {
/* 29 */     return MAP_CODEC;
/*    */   }
/*    */   
/*    */   public MutableComponent resolve(ResolutionContext context, int recursionDepth) throws CommandSyntaxException {
/* 34 */     CommandSourceStack source = context.source();
/* 35 */     if (source == null)
/* 36 */       return Component.empty(); 
/* 38 */     Optional<? extends Component> resolvedSeparator = ComponentUtils.resolve(context, this.separator, recursionDepth);
/* 39 */     return ComponentUtils.formatList(((EntitySelector)this.selector.compiled()).findEntities(source), resolvedSeparator, Entity::getDisplayName);
/*    */   }
/*    */   
/*    */   public <T> Optional<T> visit(FormattedText.StyledContentConsumer<T> output, Style currentStyle) {
/* 44 */     return output.accept(currentStyle, this.selector.source());
/*    */   }
/*    */   
/*    */   public <T> Optional<T> visit(FormattedText.ContentConsumer<T> output) {
/* 49 */     return output.accept(this.selector.source());
/*    */   }
/*    */   
/*    */   public String toString() {
/* 54 */     return "pattern{" + String.valueOf(this.selector) + "}";
/*    */   }
/*    */ }


/* Location:              C:\Users\karol\AppData\Roaming\ModrinthApp\meta\versions\26.2-0.19.5\26.2-0.19.5.jar!\net\minecraft\network\chat\contents\SelectorContents.class
 * Java compiler version: 25 (69.0)
 * JD-Core Version:       1.1.3
 */