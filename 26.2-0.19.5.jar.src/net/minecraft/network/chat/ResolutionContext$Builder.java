/*    */ package net.minecraft.network.chat;
/*    */ 
/*    */ import java.util.function.Predicate;
/*    */ import net.minecraft.commands.CommandSourceStack;
/*    */ import net.minecraft.network.chat.contents.objects.ObjectInfo;
/*    */ import net.minecraft.world.entity.Entity;
/*    */ 
/*    */ public class Builder {
/*    */   private CommandSourceStack source;
/*    */   
/*    */   private Entity defaultScoreboardEntity;
/*    */   
/*    */   private Predicate<ObjectInfo> objectInfoValidator = paramObjectInfo -> true;
/*    */   
/* 38 */   private int depthLimit = 100;
/*    */   
/* 39 */   private ResolutionContext.LimitBehavior depthLimitBehavior = ResolutionContext.LimitBehavior.STOP_PROCESSING_AND_COPY_REMAINING;
/*    */   
/*    */   public Builder withSource(CommandSourceStack source) {
/* 42 */     this.source = source;
/* 43 */     this.defaultScoreboardEntity = source.getEntity();
/* 44 */     return this;
/*    */   }
/*    */   
/*    */   public Builder withEntityOverride(Entity defaultScoreboardEntity) {
/* 48 */     this.defaultScoreboardEntity = defaultScoreboardEntity;
/* 49 */     return this;
/*    */   }
/*    */   
/*    */   public Builder withObjectInfoValidator(Predicate<ObjectInfo> objectInfoValidator) {
/* 53 */     this.objectInfoValidator = objectInfoValidator;
/* 54 */     return this;
/*    */   }
/*    */   
/*    */   public Builder setDepthLimit(int depthLimit) {
/* 58 */     this.depthLimit = depthLimit;
/* 59 */     return this;
/*    */   }
/*    */   
/*    */   public Builder setDepthLimitBehavior(ResolutionContext.LimitBehavior behavior) {
/* 63 */     this.depthLimitBehavior = behavior;
/* 64 */     return this;
/*    */   }
/*    */   
/*    */   public ResolutionContext build() {
/* 68 */     return new ResolutionContext(this.source, this.defaultScoreboardEntity, this.objectInfoValidator, this.depthLimit, this.depthLimitBehavior);
/*    */   }
/*    */ }


/* Location:              C:\Users\karol\AppData\Roaming\ModrinthApp\meta\versions\26.2-0.19.5\26.2-0.19.5.jar!\net\minecraft\network\chat\ResolutionContext$Builder.class
 * Java compiler version: 25 (69.0)
 * JD-Core Version:       1.1.3
 */