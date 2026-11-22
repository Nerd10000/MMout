/*    */ package net.minecraft.commands;
/*    */ 
/*    */ import com.mojang.serialization.Codec;
/*    */ import java.util.Objects;
/*    */ import java.util.Optional;
/*    */ import java.util.function.Function;
/*    */ import net.minecraft.commands.functions.CommandFunction;
/*    */ import net.minecraft.resources.Identifier;
/*    */ import net.minecraft.server.ServerFunctionManager;
/*    */ 
/*    */ public class CacheableFunction {
/* 12 */   public static final Codec<CacheableFunction> CODEC = Identifier.CODEC.xmap(CacheableFunction::new, CacheableFunction::getId);
/*    */   
/*    */   private final Identifier id;
/*    */   
/*    */   private boolean resolved;
/*    */   
/* 16 */   private Optional<CommandFunction<CommandSourceStack>> function = Optional.empty();
/*    */   
/*    */   public CacheableFunction(Identifier id) {
/* 19 */     this.id = id;
/*    */   }
/*    */   
/*    */   public Optional<CommandFunction<CommandSourceStack>> get(ServerFunctionManager manager) {
/* 23 */     if (!this.resolved) {
/* 24 */       this.function = manager.get(this.id);
/* 25 */       this.resolved = true;
/*    */     } 
/* 27 */     return this.function;
/*    */   }
/*    */   
/*    */   public Identifier getId() {
/* 31 */     return this.id;
/*    */   }
/*    */   
/*    */   public boolean equals(Object obj) {
/* 36 */     if (obj == this)
/* 37 */       return true; 
/* 39 */     if (obj instanceof CacheableFunction) {
/* 39 */       CacheableFunction cacheableFunction = (CacheableFunction)obj;
/* 39 */       if (getId().equals(cacheableFunction.getId()));
/*    */     } 
/* 39 */     return false;
/*    */   }
/*    */   
/*    */   public int hashCode() {
/* 44 */     return Objects.hash(new Object[] { this.id });
/*    */   }
/*    */ }


/* Location:              C:\Users\karol\AppData\Roaming\ModrinthApp\meta\versions\26.2-0.19.5\26.2-0.19.5.jar!\net\minecraft\commands\CacheableFunction.class
 * Java compiler version: 25 (69.0)
 * JD-Core Version:       1.1.3
 */