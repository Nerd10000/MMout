/*    */ package net.minecraft.world.entity;
/*    */ 
/*    */ import java.util.ArrayList;
/*    */ import java.util.EnumMap;
/*    */ import java.util.List;
/*    */ import java.util.Map;
/*    */ import net.minecraft.util.Util;
/*    */ import net.minecraft.world.phys.Vec3;
/*    */ 
/*    */ public class Builder {
/* 80 */   private final Map<EntityAttachment, List<Vec3>> attachments = new EnumMap<>(EntityAttachment.class);
/*    */   
/*    */   public Builder attach(EntityAttachment attachment, float x, float y, float z) {
/* 86 */     return attach(attachment, new Vec3(x, y, z));
/*    */   }
/*    */   
/*    */   public Builder attach(EntityAttachment attachment, Vec3 point) {
/* 90 */     ((List<Vec3>)this.attachments.computeIfAbsent(attachment, a -> new ArrayList(1))).add(point);
/* 91 */     return this;
/*    */   }
/*    */   
/*    */   public EntityAttachments build(float width, float height) {
/* 95 */     Map<EntityAttachment, List<Vec3>> attachments = Util.makeEnumMap(EntityAttachment.class, attachment -> {
/*    */           List<Vec3> points = this.attachments.get(attachment);
/*    */           return (points == null) ? attachment.createFallbackPoints(width, height) : List.<Vec3>copyOf(points);
/*    */         });
/* 99 */     return new EntityAttachments(attachments);
/*    */   }
/*    */ }


/* Location:              C:\Users\karol\AppData\Roaming\ModrinthApp\meta\versions\26.2-0.19.5\26.2-0.19.5.jar!\net\minecraft\world\entity\EntityAttachments$Builder.class
 * Java compiler version: 25 (69.0)
 * JD-Core Version:       1.1.3
 */