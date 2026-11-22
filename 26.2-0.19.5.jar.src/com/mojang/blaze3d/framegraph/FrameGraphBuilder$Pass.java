/*     */ package com.mojang.blaze3d.framegraph;
/*     */ 
/*     */ import com.mojang.blaze3d.resource.ResourceDescriptor;
/*     */ import com.mojang.blaze3d.resource.ResourceHandle;
/*     */ import java.util.ArrayList;
/*     */ import java.util.BitSet;
/*     */ import java.util.List;
/*     */ 
/*     */ class Pass implements FramePass {
/*     */   private final int id;
/*     */   
/*     */   private final String name;
/*     */   
/* 167 */   private final List<FrameGraphBuilder.Handle<?>> writesFrom = new ArrayList<>();
/*     */   
/* 168 */   private final BitSet requiredResourceIds = new BitSet();
/*     */   
/* 169 */   private final BitSet requiredPassIds = new BitSet();
/*     */   
/*     */   private Runnable task = () -> {
/*     */     
/*     */     };
/*     */   
/* 173 */   private final List<FrameGraphBuilder.InternalVirtualResource<?>> resourcesToAcquire = new ArrayList<>();
/*     */   
/* 174 */   private final BitSet resourcesToRelease = new BitSet();
/*     */   
/*     */   private boolean disableCulling;
/*     */   
/*     */   public Pass(int id, String name) {
/* 179 */     this.id = id;
/* 180 */     this.name = name;
/*     */   }
/*     */   
/*     */   private <T> void markResourceRequired(FrameGraphBuilder.Handle<T> handle) {
/* 184 */     FrameGraphBuilder.VirtualResource<T> virtualResource = handle.holder;
/* 184 */     if (virtualResource instanceof FrameGraphBuilder.InternalVirtualResource) {
/* 184 */       FrameGraphBuilder.InternalVirtualResource<?> resource = (FrameGraphBuilder.InternalVirtualResource)virtualResource;
/* 185 */       this.requiredResourceIds.set(resource.id);
/*     */     } 
/*     */   }
/*     */   
/*     */   private void markPassRequired(Pass pass) {
/* 190 */     this.requiredPassIds.set(pass.id);
/*     */   }
/*     */   
/*     */   public <T> ResourceHandle<T> createsInternal(String name, ResourceDescriptor<T> descriptor) {
/* 195 */     FrameGraphBuilder.InternalVirtualResource<T> resource = FrameGraphBuilder.this.createInternalResource(name, descriptor, this);
/* 196 */     this.requiredResourceIds.set(resource.id);
/* 197 */     return resource.handle;
/*     */   }
/*     */   
/*     */   public <T> void reads(ResourceHandle<T> handle) {
/* 202 */     _reads((FrameGraphBuilder.Handle)handle);
/*     */   }
/*     */   
/*     */   private <T> void _reads(FrameGraphBuilder.Handle<T> handle) {
/* 206 */     markResourceRequired(handle);
/* 208 */     if (handle.createdBy != null)
/* 209 */       markPassRequired(handle.createdBy); 
/* 211 */     handle.readBy.set(this.id);
/*     */   }
/*     */   
/*     */   public <T> ResourceHandle<T> readsAndWrites(ResourceHandle<T> handle) {
/* 216 */     return _readsAndWrites((FrameGraphBuilder.Handle<T>)handle);
/*     */   }
/*     */   
/*     */   public void requires(FramePass pass) {
/* 221 */     this.requiredPassIds.set(((Pass)pass).id);
/*     */   }
/*     */   
/*     */   public void disableCulling() {
/* 226 */     this.disableCulling = true;
/*     */   }
/*     */   
/*     */   private <T> FrameGraphBuilder.Handle<T> _readsAndWrites(FrameGraphBuilder.Handle<T> handle) {
/* 230 */     this.writesFrom.add(handle);
/* 231 */     _reads(handle);
/* 232 */     return handle.writeAndAlias(this);
/*     */   }
/*     */   
/*     */   public void executes(Runnable task) {
/* 237 */     this.task = task;
/*     */   }
/*     */   
/*     */   public String toString() {
/* 242 */     return this.name;
/*     */   }
/*     */ }


/* Location:              C:\Users\karol\AppData\Roaming\ModrinthApp\meta\versions\26.2-0.19.5\26.2-0.19.5.jar!\com\mojang\blaze3d\framegraph\FrameGraphBuilder$Pass.class
 * Java compiler version: 25 (69.0)
 * JD-Core Version:       1.1.3
 */