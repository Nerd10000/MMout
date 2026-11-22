/*    */ package net.minecraft.world.clock;
/*    */ 
/*    */ import net.minecraft.core.registries.Registries;
/*    */ import net.minecraft.data.worldgen.BootstrapContext;
/*    */ import net.minecraft.resources.Identifier;
/*    */ import net.minecraft.resources.ResourceKey;
/*    */ 
/*    */ public interface WorldClocks {
/*  9 */   public static final ResourceKey<WorldClock> OVERWORLD = key("overworld");
/*    */   
/* 10 */   public static final ResourceKey<WorldClock> THE_END = key("the_end");
/*    */   
/*    */   static void bootstrap(BootstrapContext<WorldClock> context) {
/* 13 */     context.register(OVERWORLD, new WorldClock());
/* 14 */     context.register(THE_END, new WorldClock());
/*    */   }
/*    */   
/*    */   private static ResourceKey<WorldClock> key(String id) {
/* 18 */     return ResourceKey.create(Registries.WORLD_CLOCK, Identifier.withDefaultNamespace(id));
/*    */   }
/*    */ }


/* Location:              C:\Users\karol\AppData\Roaming\ModrinthApp\meta\versions\26.2-0.19.5\26.2-0.19.5.jar!\net\minecraft\world\clock\WorldClocks.class
 * Java compiler version: 25 (69.0)
 * JD-Core Version:       1.1.3
 */