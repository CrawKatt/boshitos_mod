package crawkatt.boshitosmod.entity.client

import crawkatt.boshitosmod.BoshitosMod
import net.minecraft.client.model.geom.ModelLayerLocation
import net.minecraft.resources.ResourceLocation

object ModModelLayers {
    val BOSHITO_LAYER: ModelLayerLocation = ModelLayerLocation(
        ResourceLocation(BoshitosMod.MOD_ID, "boshito_layer"), "main"
    )
}