package net.minecraft.client.data.models;

import java.util.function.BiConsumer;
import net.minecraft.client.data.models.blockstates.BlockModelDefinitionGenerator;
import net.minecraft.client.data.models.model.ModelInstance;
import net.minecraft.client.data.models.model.TextureMapping;
import net.minecraft.client.renderer.block.dispatch.Variant;
import net.minecraft.resources.Identifier;
import net.minecraft.world.level.block.Block;

@FunctionalInterface
interface BlockStateGeneratorSupplier {
  BlockModelDefinitionGenerator create(Block paramBlock, Variant paramVariant, TextureMapping paramTextureMapping, BiConsumer<Identifier, ModelInstance> paramBiConsumer);
}


/* Location:              C:\Users\karol\AppData\Roaming\ModrinthApp\meta\versions\26.2-0.19.5\26.2-0.19.5.jar!\net\minecraft\client\data\models\BlockModelGenerators$BlockStateGeneratorSupplier.class
 * Java compiler version: 25 (69.0)
 * JD-Core Version:       1.1.3
 */