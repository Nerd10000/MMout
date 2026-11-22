/*    */ package net.minecraft.server.dedicated;
/*    */ 
/*    */ import java.nio.file.Path;
/*    */ import java.util.function.UnaryOperator;
/*    */ 
/*    */ public class DedicatedServerSettings {
/*    */   private final Path source;
/*    */   
/*    */   private DedicatedServerProperties properties;
/*    */   
/*    */   public DedicatedServerSettings(Path source) {
/* 11 */     this.source = source;
/* 12 */     this.properties = DedicatedServerProperties.fromFile(source);
/*    */   }
/*    */   
/*    */   public DedicatedServerProperties getProperties() {
/* 16 */     return this.properties;
/*    */   }
/*    */   
/*    */   public void forceSave() {
/* 20 */     this.properties.store(this.source);
/*    */   }
/*    */   
/*    */   public DedicatedServerSettings update(UnaryOperator<DedicatedServerProperties> mutator) {
/* 24 */     (this.properties = mutator.apply(this.properties)).store(this.source);
/* 25 */     return this;
/*    */   }
/*    */ }


/* Location:              C:\Users\karol\AppData\Roaming\ModrinthApp\meta\versions\26.2-0.19.5\26.2-0.19.5.jar!\net\minecraft\server\dedicated\DedicatedServerSettings.class
 * Java compiler version: 25 (69.0)
 * JD-Core Version:       1.1.3
 */