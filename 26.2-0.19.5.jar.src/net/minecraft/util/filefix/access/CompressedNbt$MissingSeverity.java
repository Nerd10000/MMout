/*    */ package net.minecraft.util.filefix.access;
/*    */ 
/*    */ import java.nio.file.Path;
/*    */ import java.util.Objects;
/*    */ import java.util.function.BiConsumer;
/*    */ 
/*    */ public enum MissingSeverity {
/* 25 */   IMPORTANT(CompressedNbt.LOGGER::error),
/* 26 */   NEUTRAL(CompressedNbt.LOGGER::info),
/* 27 */   MINOR(CompressedNbt.LOGGER::debug);
/*    */   
/*    */   static {
/*    */     Objects.requireNonNull(CompressedNbt.LOGGER);
/*    */   }
/*    */   
/*    */   static {
/*    */     Objects.requireNonNull(CompressedNbt.LOGGER);
/*    */   }
/*    */   
/*    */   private final BiConsumer<String, Object> logFunction;
/*    */   
/*    */   static {
/* 27 */     Objects.requireNonNull(CompressedNbt.LOGGER);
/*    */   }
/*    */   
/*    */   MissingSeverity(BiConsumer<String, Object> logFunction) {
/* 32 */     this.logFunction = logFunction;
/*    */   }
/*    */   
/*    */   public void log(String message, Path path) {
/* 36 */     this.logFunction.accept(message, path);
/*    */   }
/*    */ }


/* Location:              C:\Users\karol\AppData\Roaming\ModrinthApp\meta\versions\26.2-0.19.5\26.2-0.19.5.jar!\net\minecraf\\util\filefix\access\CompressedNbt$MissingSeverity.class
 * Java compiler version: 25 (69.0)
 * JD-Core Version:       1.1.3
 */