/*    */ package net.minecraft.commands.synchronization;
/*    */ 
/*    */ import java.util.function.Function;
/*    */ import net.minecraft.commands.CommandBuildContext;
/*    */ 
/*    */ public final class Template implements ArgumentTypeInfo.Template<A> {
/*    */   private final Function<CommandBuildContext, A> constructor;
/*    */   
/*    */   public Template(Function<CommandBuildContext, A> constructor) {
/* 16 */     this.constructor = constructor;
/*    */   }
/*    */   
/*    */   public A instantiate(CommandBuildContext context) {
/* 21 */     return this.constructor.apply(context);
/*    */   }
/*    */   
/*    */   public ArgumentTypeInfo<A, ?> type() {
/* 26 */     return SingletonArgumentInfo.this;
/*    */   }
/*    */ }


/* Location:              C:\Users\karol\AppData\Roaming\ModrinthApp\meta\versions\26.2-0.19.5\26.2-0.19.5.jar!\net\minecraft\commands\synchronization\SingletonArgumentInfo$Template.class
 * Java compiler version: 25 (69.0)
 * JD-Core Version:       1.1.3
 */