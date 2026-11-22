/*    */ package net.minecraft.server.packs.repository;
/*    */ 
/*    */ import java.util.function.UnaryOperator;
/*    */ import net.minecraft.ChatFormatting;
/*    */ import net.minecraft.network.chat.Component;
/*    */ import net.minecraft.network.chat.MutableComponent;
/*    */ 
/*    */ public interface PackSource {
/*  9 */   public static final UnaryOperator<Component> NO_DECORATION = UnaryOperator.identity();
/*    */   
/* 12 */   public static final PackSource DEFAULT = create(NO_DECORATION, true);
/*    */   
/* 13 */   public static final PackSource BUILT_IN = create(decorateWithSource("pack.source.builtin"), true);
/*    */   
/* 14 */   public static final PackSource FEATURE = create(decorateWithSource("pack.source.feature"), false);
/*    */   
/* 15 */   public static final PackSource WORLD = create(decorateWithSource("pack.source.world"), true);
/*    */   
/* 18 */   public static final PackSource SERVER = create(decorateWithSource("pack.source.server"), true);
/*    */   
/*    */   Component decorate(Component paramComponent);
/*    */   
/*    */   boolean shouldAddAutomatically();
/*    */   
/*    */   static PackSource create(final UnaryOperator<Component> decorator, final boolean addAutomatically) {
/* 25 */     return new PackSource() {
/*    */         public Component decorate(Component packDescription) {
/* 28 */           return decorator.apply(packDescription);
/*    */         }
/*    */         
/*    */         public boolean shouldAddAutomatically() {
/* 33 */           return addAutomatically;
/*    */         }
/*    */       };
/*    */   }
/*    */   
/*    */   private static UnaryOperator<Component> decorateWithSource(String descriptionId) {
/* 39 */     MutableComponent mutableComponent = Component.translatable(descriptionId);
/* 40 */     return packDescription -> Component.translatable("pack.nameAndSource", new Object[] { packDescription, description }).withStyle(ChatFormatting.GRAY);
/*    */   }
/*    */ }


/* Location:              C:\Users\karol\AppData\Roaming\ModrinthApp\meta\versions\26.2-0.19.5\26.2-0.19.5.jar!\net\minecraft\server\packs\repository\PackSource.class
 * Java compiler version: 25 (69.0)
 * JD-Core Version:       1.1.3
 */