package net.minecraft.world.level.entity;

import java.util.UUID;

public interface UUIDLookup<IdentifiedType extends UniquelyIdentifyable> {
  IdentifiedType lookup(UUID paramUUID);
}


/* Location:              C:\Users\karol\AppData\Roaming\ModrinthApp\meta\versions\26.2-0.19.5\26.2-0.19.5.jar!\net\minecraft\world\level\entity\UUIDLookup.class
 * Java compiler version: 25 (69.0)
 * JD-Core Version:       1.1.3
 */