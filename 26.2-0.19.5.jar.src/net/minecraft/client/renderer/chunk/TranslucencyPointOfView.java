/*    */ package net.minecraft.client.renderer.chunk;
/*    */ 
/*    */ import java.util.Objects;
/*    */ import net.minecraft.core.SectionPos;
/*    */ import net.minecraft.util.Mth;
/*    */ import net.minecraft.world.phys.Vec3;
/*    */ 
/*    */ public final class TranslucencyPointOfView {
/*    */   private int x;
/*    */   
/*    */   private int y;
/*    */   
/*    */   private int z;
/*    */   
/*    */   public static TranslucencyPointOfView of(Vec3 cameraPos, long sectionNode) {
/* 15 */     return (new TranslucencyPointOfView()).set(cameraPos, sectionNode);
/*    */   }
/*    */   
/*    */   public TranslucencyPointOfView set(Vec3 cameraPos, long sectionPos) {
/* 19 */     this.x = getCoordinate(cameraPos.x(), SectionPos.x(sectionPos));
/* 20 */     this.y = getCoordinate(cameraPos.y(), SectionPos.y(sectionPos));
/* 21 */     this.z = getCoordinate(cameraPos.z(), SectionPos.z(sectionPos));
/* 22 */     return this;
/*    */   }
/*    */   
/*    */   private static int getCoordinate(double cameraCoordinate, int section) {
/* 26 */     int relativeSection = SectionPos.blockToSectionCoord(cameraCoordinate) - section;
/* 27 */     return Mth.clamp(relativeSection, -1, 1);
/*    */   }
/*    */   
/*    */   public boolean isAxisAligned() {
/* 31 */     return (this.x == 0 || this.y == 0 || this.z == 0);
/*    */   }
/*    */   
/*    */   public boolean equals(Object other) {
/* 36 */     if (other == this)
/* 37 */       return true; 
/* 39 */     if (other instanceof TranslucencyPointOfView) {
/* 39 */       TranslucencyPointOfView otherPerspective = (TranslucencyPointOfView)other;
/* 40 */       return (this.x == otherPerspective.x && this.y == otherPerspective.y && this.z == otherPerspective.z);
/*    */     } 
/* 42 */     return false;
/*    */   }
/*    */   
/*    */   public int hashCode() {
/* 47 */     return Objects.hash(new Object[] { Integer.valueOf(this.x), Integer.valueOf(this.y), Integer.valueOf(this.z) });
/*    */   }
/*    */ }


/* Location:              C:\Users\karol\AppData\Roaming\ModrinthApp\meta\versions\26.2-0.19.5\26.2-0.19.5.jar!\net\minecraft\client\renderer\chunk\TranslucencyPointOfView.class
 * Java compiler version: 25 (69.0)
 * JD-Core Version:       1.1.3
 */