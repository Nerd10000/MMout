/*    */ package net.minecraft.commands.synchronization;
/*    */ 
/*    */ import com.google.gson.JsonObject;
/*    */ import com.mojang.brigadier.arguments.ArgumentType;
/*    */ import java.util.function.Function;
/*    */ import java.util.function.Supplier;
/*    */ import net.minecraft.commands.CommandBuildContext;
/*    */ import net.minecraft.network.FriendlyByteBuf;
/*    */ 
/*    */ public class SingletonArgumentInfo<A extends ArgumentType<?>> implements ArgumentTypeInfo<A, SingletonArgumentInfo<A>.Template> {
/*    */   private final Template template;
/*    */   
/*    */   public final class Template implements ArgumentTypeInfo.Template<A> {
/*    */     private final Function<CommandBuildContext, A> constructor;
/*    */     
/*    */     public Template(Function<CommandBuildContext, A> constructor) {
/* 16 */       this.constructor = constructor;
/*    */     }
/*    */     
/*    */     public A instantiate(CommandBuildContext context) {
/* 21 */       return this.constructor.apply(context);
/*    */     }
/*    */     
/*    */     public ArgumentTypeInfo<A, ?> type() {
/* 26 */       return SingletonArgumentInfo.this;
/*    */     }
/*    */   }
/*    */   
/*    */   private SingletonArgumentInfo(Function<CommandBuildContext, A> constructor) {
/* 33 */     this.template = new Template(constructor);
/*    */   }
/*    */   
/*    */   public static <T extends ArgumentType<?>> SingletonArgumentInfo<T> contextFree(Supplier<T> constructor) {
/* 37 */     return new SingletonArgumentInfo<>(context -> (ArgumentType)constructor.get());
/*    */   }
/*    */   
/*    */   public static <T extends ArgumentType<?>> SingletonArgumentInfo<T> contextAware(Function<CommandBuildContext, T> constructor) {
/* 41 */     return new SingletonArgumentInfo<>(constructor);
/*    */   }
/*    */   
/*    */   public void serializeToNetwork(Template template, FriendlyByteBuf out) {}
/*    */   
/*    */   public void serializeToJson(Template template, JsonObject out) {}
/*    */   
/*    */   public Template deserializeFromNetwork(FriendlyByteBuf in) {
/* 54 */     return this.template;
/*    */   }
/*    */   
/*    */   public Template unpack(A argument) {
/* 59 */     return this.template;
/*    */   }
/*    */ }


/* Location:              C:\Users\karol\AppData\Roaming\ModrinthApp\meta\versions\26.2-0.19.5\26.2-0.19.5.jar!\net\minecraft\commands\synchronization\SingletonArgumentInfo.class
 * Java compiler version: 25 (69.0)
 * JD-Core Version:       1.1.3
 */