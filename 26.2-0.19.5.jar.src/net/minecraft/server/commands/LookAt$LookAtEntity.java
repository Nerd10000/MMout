/*    */ package net.minecraft.server.commands;
/*    */ 
/*    */ import net.minecraft.commands.CommandSourceStack;
/*    */ import net.minecraft.commands.arguments.EntityAnchorArgument;
/*    */ import net.minecraft.server.level.ServerPlayer;
/*    */ import net.minecraft.world.entity.Entity;
/*    */ 
/*    */ public final class LookAtEntity extends Record implements LookAt {
/*    */   private final Entity entity;
/*    */   
/*    */   private final EntityAnchorArgument.Anchor anchor;
/*    */   
/*    */   public LookAtEntity(Entity entity, EntityAnchorArgument.Anchor anchor) {
/* 13 */     this.entity = entity;
/* 13 */     this.anchor = anchor;
/*    */   }
/*    */   
/*    */   public final String toString() {
/*    */     // Byte code:
/*    */     //   0: aload_0
/*    */     //   1: <illegal opcode> toString : (Lnet/minecraft/server/commands/LookAt$LookAtEntity;)Ljava/lang/String;
/*    */     //   6: areturn
/*    */     // Line number table:
/*    */     //   Java source line number -> byte code offset
/*    */     //   #13	-> 0
/*    */     // Local variable table:
/*    */     //   start	length	slot	name	descriptor
/*    */     //   0	7	0	this	Lnet/minecraft/server/commands/LookAt$LookAtEntity;
/*    */   }
/*    */   
/*    */   public final int hashCode() {
/*    */     // Byte code:
/*    */     //   0: aload_0
/*    */     //   1: <illegal opcode> hashCode : (Lnet/minecraft/server/commands/LookAt$LookAtEntity;)I
/*    */     //   6: ireturn
/*    */     // Line number table:
/*    */     //   Java source line number -> byte code offset
/*    */     //   #13	-> 0
/*    */     // Local variable table:
/*    */     //   start	length	slot	name	descriptor
/*    */     //   0	7	0	this	Lnet/minecraft/server/commands/LookAt$LookAtEntity;
/*    */   }
/*    */   
/*    */   public final boolean equals(Object o) {
/*    */     // Byte code:
/*    */     //   0: aload_0
/*    */     //   1: aload_1
/*    */     //   2: <illegal opcode> equals : (Lnet/minecraft/server/commands/LookAt$LookAtEntity;Ljava/lang/Object;)Z
/*    */     //   7: ireturn
/*    */     // Line number table:
/*    */     //   Java source line number -> byte code offset
/*    */     //   #13	-> 0
/*    */     // Local variable table:
/*    */     //   start	length	slot	name	descriptor
/*    */     //   0	8	0	this	Lnet/minecraft/server/commands/LookAt$LookAtEntity;
/*    */     //   0	8	1	o	Ljava/lang/Object;
/*    */   }
/*    */   
/*    */   public Entity entity() {
/* 13 */     return this.entity;
/*    */   }
/*    */   
/*    */   public EntityAnchorArgument.Anchor anchor() {
/* 13 */     return this.anchor;
/*    */   }
/*    */   
/*    */   public void perform(CommandSourceStack source, Entity target) {
/* 16 */     if (target instanceof ServerPlayer) {
/* 16 */       ServerPlayer targetPlayer = (ServerPlayer)target;
/* 17 */       targetPlayer.lookAt(source.getAnchor(), this.entity, this.anchor);
/*    */     } else {
/* 19 */       target.lookAt(source.getAnchor(), this.anchor.apply(this.entity));
/*    */     } 
/*    */   }
/*    */ }


/* Location:              C:\Users\karol\AppData\Roaming\ModrinthApp\meta\versions\26.2-0.19.5\26.2-0.19.5.jar!\net\minecraft\server\commands\LookAt$LookAtEntity.class
 * Java compiler version: 25 (69.0)
 * JD-Core Version:       1.1.3
 */