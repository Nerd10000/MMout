package net.minecraft.client.renderer;

import net.minecraft.client.renderer.chunk.SectionRenderDispatcher;
import net.minecraft.client.renderer.culling.Frustum;
import net.minecraft.world.phys.AABB;

public interface Node {
  void visitNodes(Octree.OctreeVisitor paramOctreeVisitor, boolean paramBoolean1, Frustum paramFrustum, int paramInt1, int paramInt2, boolean paramBoolean2);
  
  SectionRenderDispatcher.RenderSection getSection();
  
  AABB getAABB();
}


/* Location:              C:\Users\karol\AppData\Roaming\ModrinthApp\meta\versions\26.2-0.19.5\26.2-0.19.5.jar!\net\minecraft\client\renderer\Octree$Node.class
 * Java compiler version: 25 (69.0)
 * JD-Core Version:       1.1.3
 */