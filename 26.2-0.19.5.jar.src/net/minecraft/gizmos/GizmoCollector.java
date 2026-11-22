/*    */ package net.minecraft.gizmos;
/*    */ 
/*    */ public interface GizmoCollector {
/*  6 */   public static final GizmoProperties IGNORED = new GizmoProperties() {
/*    */       public GizmoProperties setAlwaysOnTop() {
/* 10 */         return this;
/*    */       }
/*    */       
/*    */       public GizmoProperties persistForMillis(int milliseconds) {
/* 15 */         return this;
/*    */       }
/*    */       
/*    */       public GizmoProperties fadeOut() {
/* 20 */         return this;
/*    */       }
/*    */     };
/*    */   
/*    */   public static final GizmoCollector NOOP = gizmo -> IGNORED;
/*    */   
/*    */   GizmoProperties add(Gizmo paramGizmo);
/*    */ }


/* Location:              C:\Users\karol\AppData\Roaming\ModrinthApp\meta\versions\26.2-0.19.5\26.2-0.19.5.jar!\net\minecraft\gizmos\GizmoCollector.class
 * Java compiler version: 25 (69.0)
 * JD-Core Version:       1.1.3
 */