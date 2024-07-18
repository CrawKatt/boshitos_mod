package crawkatt.boshitosmod.entity.client

import crawkatt.boshitosmod.BoshitosMod
import crawkatt.boshitosmod.entity.custom.MaiNoboshiEntity
import net.minecraft.client.renderer.entity.EntityRendererProvider
import net.minecraft.client.renderer.entity.HumanoidMobRenderer
import net.minecraft.resources.ResourceLocation

class MaiNoboshiRenderer(context: EntityRendererProvider.Context) : HumanoidMobRenderer<MaiNoboshiEntity, MaiNoboshiModel>(
    context, MaiNoboshiModel(context.bakeLayer(MaiNoboshiModel.LAYER_LOCATION)), 0.5f) {

    companion object {
        private val TEXTURE = ResourceLocation(BoshitosMod.MOD_ID, "textures/entity/mainoboshi.png")
    }

    override fun getTextureLocation(entity: MaiNoboshiEntity): ResourceLocation {
        return TEXTURE
    }
}