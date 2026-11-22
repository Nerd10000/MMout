/*    */ package net.minecraft.world.entity;
/*    */ 
/*    */ import net.minecraft.world.phys.AABB;
/*    */ import net.minecraft.world.phys.Vec3;
/*    */ 
/*    */ public final class EntityDimensions extends Record {
/*    */   private final float width;
/*    */   
/*    */   private final float height;
/*    */   
/*    */   private final float eyeHeight;
/*    */   
/*    */   private final EntityAttachments attachments;
/*    */   
/*    */   private final boolean fixed;
/*    */   
/*    */   public EntityDimensions(float width, float height, float eyeHeight, EntityAttachments attachments, boolean fixed) {
/*  6 */     this.width = width;
/*  6 */     this.height = height;
/*  6 */     this.eyeHeight = eyeHeight;
/*  6 */     this.attachments = attachments;
/*  6 */     this.fixed = fixed;
/*    */   }
/*    */   
/*    */   public final String toString() {
/*    */     // Byte code:
/*    */     //   0: aload_0
/*    */     //   1: <illegal opcode> toString : (Lnet/minecraft/world/entity/EntityDimensions;)Ljava/lang/String;
/*    */     //   6: areturn
/*    */     // Line number table:
/*    */     //   Java source line number -> byte code offset
/*    */     //   #6	-> 0
/*    */     // Local variable table:
/*    */     //   start	length	slot	name	descriptor
/*    */     //   0	7	0	this	Lnet/minecraft/world/entity/EntityDimensions;
/*    */   }
/*    */   
/*    */   public final int hashCode() {
/*    */     // Byte code:
/*    */     //   0: aload_0
/*    */     //   1: <illegal opcode> hashCode : (Lnet/minecraft/world/entity/EntityDimensions;)I
/*    */     //   6: ireturn
/*    */     // Line number table:
/*    */     //   Java source line number -> byte code offset
/*    */     //   #6	-> 0
/*    */     // Local variable table:
/*    */     //   start	length	slot	name	descriptor
/*    */     //   0	7	0	this	Lnet/minecraft/world/entity/EntityDimensions;
/*    */   }
/*    */   
/*    */   public final boolean equals(Object o) {
/*    */     // Byte code:
/*    */     //   0: aload_0
/*    */     //   1: aload_1
/*    */     //   2: <illegal opcode> equals : (Lnet/minecraft/world/entity/EntityDimensions;Ljava/lang/Object;)Z
/*    */     //   7: ireturn
/*    */     // Line number table:
/*    */     //   Java source line number -> byte code offset
/*    */     //   #6	-> 0
/*    */     // Local variable table:
/*    */     //   start	length	slot	name	descriptor
/*    */     //   0	8	0	this	Lnet/minecraft/world/entity/EntityDimensions;
/*    */     //   0	8	1	o	Ljava/lang/Object;
/*    */   }
/*    */   
/*    */   public float width() {
/*  6 */     return this.width;
/*    */   }
/*    */   
/*    */   public float height() {
/*  6 */     return this.height;
/*    */   }
/*    */   
/*    */   public float eyeHeight() {
/*  6 */     return this.eyeHeight;
/*    */   }
/*    */   
/*    */   public EntityAttachments attachments() {
/*  6 */     return this.attachments;
/*    */   }
/*    */   
/*    */   public boolean fixed() {
/*  6 */     return this.fixed;
/*    */   }
/*    */   
/*    */   private EntityDimensions(float width, float height, boolean fixed) {
/*  8 */     this(width, height, defaultEyeHeight(height), EntityAttachments.createDefault(width, height), fixed);
/*    */   }
/*    */   
/*    */   private static float defaultEyeHeight(float height) {
/* 12 */     return height * 0.85F;
/*    */   }
/*    */   
/*    */   public AABB makeBoundingBox(Vec3 pos) {
/* 16 */     return makeBoundingBox(pos.x, pos.y, pos.z);
/*    */   }
/*    */   
/*    */   public AABB makeBoundingBox(double x, double y, double z) {
/* 20 */     float w = this.width / 2.0F;
/* 21 */     float h = this.height;
/* 22 */     return new AABB(x - w, y, z - w, x + w, y + h, z + w);
/*    */   }
/*    */   
/*    */   public EntityDimensions scale(float scaleFactor) {
/* 26 */     return scale(scaleFactor, scaleFactor);
/*    */   }
/*    */   
/*    */   public EntityDimensions scale(float widthScaleFactor, float heightScaleFactor) {
/* 30 */     if (this.fixed || (widthScaleFactor == 1.0F && heightScaleFactor == 1.0F))
/* 31 */       return this; 
/* 33 */     return new EntityDimensions(this.width * widthScaleFactor, this.height * heightScaleFactor, this.eyeHeight * heightScaleFactor, this.attachments
/*    */         
/* 37 */         .scale(widthScaleFactor, heightScaleFactor, widthScaleFactor), false);
/*    */   }
/*    */   
/*    */   public static EntityDimensions scalable(float width, float height) {
/* 44 */     return new EntityDimensions(width, height, false);
/*    */   }
/*    */   
/*    */   public static EntityDimensions fixed(float width, float height) {
/* 48 */     return new EntityDimensions(width, height, true);
/*    */   }
/*    */   
/*    */   public EntityDimensions withEyeHeight(float eyeHeight) {
/* 52 */     return new EntityDimensions(this.width, this.height, eyeHeight, this.attachments, this.fixed);
/*    */   }
/*    */   
/*    */   public EntityDimensions withAttachments(EntityAttachments.Builder attachments) {
/* 56 */     return new EntityDimensions(this.width, this.height, this.eyeHeight, attachments.build(this.width, this.height), this.fixed);
/*    */   }
/*    */ }


/* Location:              C:\Users\karol\AppData\Roaming\ModrinthApp\meta\versions\26.2-0.19.5\26.2-0.19.5.jar!\net\minecraft\world\entity\EntityDimensions.class
 * Java compiler version: 25 (69.0)
 * JD-Core Version:       1.1.3
 */