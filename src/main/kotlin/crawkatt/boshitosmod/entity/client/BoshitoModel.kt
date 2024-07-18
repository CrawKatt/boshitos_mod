package crawkatt.boshitosmod.entity.client

import crawkatt.boshitosmod.BoshitosMod
import crawkatt.boshitosmod.entity.custom.BoshitoEntity
import net.minecraft.resources.ResourceLocation
import software.bernie.geckolib.model.GeoModel

class BoshitoModel : GeoModel<BoshitoEntity?>() {
    override fun getModelResource(boshitoEntity: BoshitoEntity?): ResourceLocation {
        return ResourceLocation(BoshitosMod.MOD_ID, "geo/boshito.geo.json")
    }

    override fun getTextureResource(boshitoEntity: BoshitoEntity?): ResourceLocation {
        return ResourceLocation(BoshitosMod.MOD_ID, "textures/entity/boshito.png")
    }

    override fun getAnimationResource(boshitoEntity: BoshitoEntity?): ResourceLocation? {
        return null
    }
}