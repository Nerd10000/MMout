/*    */ package net.minecraft.network.chat;
/*    */ 
/*    */ import java.util.function.Predicate;
/*    */ import net.minecraft.commands.CommandSourceStack;
/*    */ import net.minecraft.network.chat.contents.objects.ObjectInfo;
/*    */ import net.minecraft.world.entity.Entity;
/*    */ 
/*    */ public final class ResolutionContext extends Record {
/*    */   private final CommandSourceStack source;
/*    */   
/*    */   private final Entity defaultScoreboardEntity;
/*    */   
/*    */   private final Predicate<ObjectInfo> objectInfoValidator;
/*    */   
/*    */   private final int depthLimit;
/*    */   
/*    */   private final LimitBehavior depthLimitBehavior;
/*    */   
/*    */   public ResolutionContext(CommandSourceStack source, Entity defaultScoreboardEntity, Predicate<ObjectInfo> objectInfoValidator, int depthLimit, LimitBehavior depthLimitBehavior) {
/* 10 */     this.source = source;
/* 10 */     this.defaultScoreboardEntity = defaultScoreboardEntity;
/* 10 */     this.objectInfoValidator = objectInfoValidator;
/* 10 */     this.depthLimit = depthLimit;
/* 10 */     this.depthLimitBehavior = depthLimitBehavior;
/*    */   }
/*    */   
/*    */   public final String toString() {
/*    */     // Byte code:
/*    */     //   0: aload_0
/*    */     //   1: <illegal opcode> toString : (Lnet/minecraft/network/chat/ResolutionContext;)Ljava/lang/String;
/*    */     //   6: areturn
/*    */     // Line number table:
/*    */     //   Java source line number -> byte code offset
/*    */     //   #10	-> 0
/*    */     // Local variable table:
/*    */     //   start	length	slot	name	descriptor
/*    */     //   0	7	0	this	Lnet/minecraft/network/chat/ResolutionContext;
/*    */   }
/*    */   
/*    */   public final int hashCode() {
/*    */     // Byte code:
/*    */     //   0: aload_0
/*    */     //   1: <illegal opcode> hashCode : (Lnet/minecraft/network/chat/ResolutionContext;)I
/*    */     //   6: ireturn
/*    */     // Line number table:
/*    */     //   Java source line number -> byte code offset
/*    */     //   #10	-> 0
/*    */     // Local variable table:
/*    */     //   start	length	slot	name	descriptor
/*    */     //   0	7	0	this	Lnet/minecraft/network/chat/ResolutionContext;
/*    */   }
/*    */   
/*    */   public final boolean equals(Object o) {
/*    */     // Byte code:
/*    */     //   0: aload_0
/*    */     //   1: aload_1
/*    */     //   2: <illegal opcode> equals : (Lnet/minecraft/network/chat/ResolutionContext;Ljava/lang/Object;)Z
/*    */     //   7: ireturn
/*    */     // Line number table:
/*    */     //   Java source line number -> byte code offset
/*    */     //   #10	-> 0
/*    */     // Local variable table:
/*    */     //   start	length	slot	name	descriptor
/*    */     //   0	8	0	this	Lnet/minecraft/network/chat/ResolutionContext;
/*    */     //   0	8	1	o	Ljava/lang/Object;
/*    */   }
/*    */   
/*    */   public CommandSourceStack source() {
/* 10 */     return this.source;
/*    */   }
/*    */   
/*    */   public Entity defaultScoreboardEntity() {
/* 10 */     return this.defaultScoreboardEntity;
/*    */   }
/*    */   
/*    */   public Predicate<ObjectInfo> objectInfoValidator() {
/* 10 */     return this.objectInfoValidator;
/*    */   }
/*    */   
/*    */   public int depthLimit() {
/* 10 */     return this.depthLimit;
/*    */   }
/*    */   
/*    */   public LimitBehavior depthLimitBehavior() {
/* 10 */     return this.depthLimitBehavior;
/*    */   }
/*    */   
/*    */   public ObjectInfo validate(ObjectInfo description) {
/* 18 */     return this.objectInfoValidator.test(description) ? description : null;
/*    */   }
/*    */   
/*    */   public static ResolutionContext create(CommandSourceStack source) {
/* 22 */     return builder().withSource(source).build();
/*    */   }
/*    */   
/*    */   public static Builder builder() {
/* 26 */     return new Builder();
/*    */   }
/*    */   
/*    */   public enum LimitBehavior {
/* 30 */     DISCARD_REMAINING, STOP_PROCESSING_AND_COPY_REMAINING;
/*    */   }
/*    */   
/*    */   public static class Builder {
/*    */     private CommandSourceStack source;
/*    */     
/*    */     private Entity defaultScoreboardEntity;
/*    */     
/*    */     private Predicate<ObjectInfo> objectInfoValidator;
/*    */     
/*    */     private int depthLimit;
/*    */     
/*    */     private ResolutionContext.LimitBehavior depthLimitBehavior;
/*    */     
/*    */     public Builder() {
/* 37 */       this.objectInfoValidator = (param1ObjectInfo -> true);
/* 38 */       this.depthLimit = 100;
/* 39 */       this.depthLimitBehavior = ResolutionContext.LimitBehavior.STOP_PROCESSING_AND_COPY_REMAINING;
/*    */     }
/*    */     
/*    */     public Builder withSource(CommandSourceStack source) {
/* 42 */       this.source = source;
/* 43 */       this.defaultScoreboardEntity = source.getEntity();
/* 44 */       return this;
/*    */     }
/*    */     
/*    */     public Builder withEntityOverride(Entity defaultScoreboardEntity) {
/* 48 */       this.defaultScoreboardEntity = defaultScoreboardEntity;
/* 49 */       return this;
/*    */     }
/*    */     
/*    */     public Builder withObjectInfoValidator(Predicate<ObjectInfo> objectInfoValidator) {
/* 53 */       this.objectInfoValidator = objectInfoValidator;
/* 54 */       return this;
/*    */     }
/*    */     
/*    */     public Builder setDepthLimit(int depthLimit) {
/* 58 */       this.depthLimit = depthLimit;
/* 59 */       return this;
/*    */     }
/*    */     
/*    */     public Builder setDepthLimitBehavior(ResolutionContext.LimitBehavior behavior) {
/* 63 */       this.depthLimitBehavior = behavior;
/* 64 */       return this;
/*    */     }
/*    */     
/*    */     public ResolutionContext build() {
/* 68 */       return new ResolutionContext(this.source, this.defaultScoreboardEntity, this.objectInfoValidator, this.depthLimit, this.depthLimitBehavior);
/*    */     }
/*    */   }
/*    */ }


/* Location:              C:\Users\karol\AppData\Roaming\ModrinthApp\meta\versions\26.2-0.19.5\26.2-0.19.5.jar!\net\minecraft\network\chat\ResolutionContext.class
 * Java compiler version: 25 (69.0)
 * JD-Core Version:       1.1.3
 */