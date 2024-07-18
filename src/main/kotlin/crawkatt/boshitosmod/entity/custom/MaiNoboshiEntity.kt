package crawkatt.boshitosmod.entity.custom

import net.minecraft.sounds.SoundEvents
import net.minecraft.world.entity.EntityType
import net.minecraft.world.entity.EquipmentSlot
import net.minecraft.world.entity.LivingEntity
import net.minecraft.world.entity.ai.attributes.AttributeSupplier
import net.minecraft.world.entity.ai.attributes.Attributes
import net.minecraft.world.entity.ai.goal.LookAtPlayerGoal
import net.minecraft.world.entity.ai.goal.RandomLookAroundGoal
import net.minecraft.world.entity.ai.goal.RangedBowAttackGoal
import net.minecraft.world.entity.ai.goal.WaterAvoidingRandomStrollGoal
import net.minecraft.world.entity.ai.goal.target.HurtByTargetGoal
import net.minecraft.world.entity.ai.goal.target.NearestAttackableTargetGoal
import net.minecraft.world.entity.animal.IronGolem
import net.minecraft.world.entity.monster.Monster
import net.minecraft.world.entity.monster.RangedAttackMob
import net.minecraft.world.entity.player.Player
import net.minecraft.world.entity.projectile.AbstractArrow
import net.minecraft.world.entity.projectile.Arrow
import net.minecraft.world.item.ItemStack
import net.minecraft.world.item.Items
import net.minecraft.world.level.Level
import kotlin.math.sqrt


class MaiNoboshiEntity(type: EntityType<out Monster>, world: Level) : Monster(type, world), RangedAttackMob {

    init {
        this.setItemSlot(EquipmentSlot.MAINHAND, ItemStack(Items.BOW))
    }

    companion object {
        fun createAttributes(): AttributeSupplier {
            return createMobAttributes()
                .add(Attributes.MAX_HEALTH, 20.0)
                .add(Attributes.MOVEMENT_SPEED, 0.25)
                .build()
        }
    }

    override fun registerGoals() {
        super.registerGoals()
        this.goalSelector.addGoal(2, RangedBowAttackGoal<MaiNoboshiEntity>(this, 1.0, 20, 15.0f))
        this.goalSelector.addGoal(5, WaterAvoidingRandomStrollGoal(this, 1.0))
        this.goalSelector.addGoal(6, LookAtPlayerGoal(this, Player::class.java, 8.0f))
        this.goalSelector.addGoal(6, RandomLookAroundGoal(this))
        this.targetSelector.addGoal(1, HurtByTargetGoal(this))
        this.targetSelector.addGoal(2, NearestAttackableTargetGoal(this, Player::class.java, true))
        this.targetSelector.addGoal(3, NearestAttackableTargetGoal(this, IronGolem::class.java, true))
    }

    override fun performRangedAttack(p0: LivingEntity, distanceFactor: Float) {
        val arrow = shootArrow(distanceFactor)
        val d0 = target!!.x - this.x
        val d1 = target!!.eyeY - arrow.y
        val d2 = target!!.z - this.z
        val f = sqrt(d0 * d0 + d2 * d2).toFloat()
        arrow.shoot(d0, d1 + f * 0.2, d2, 1.6f, (14 - this.level().difficulty.id * 4).toFloat())
        this.playSound(SoundEvents.SKELETON_SHOOT, 1.0f, 1.0f / (this.random.nextFloat() * 0.4f + 0.8f))
        this.level().addFreshEntity(arrow)
    }

    private fun shootArrow(distanceFactor: Float): AbstractArrow {
        val arrow = Arrow(this.level(), this)
        arrow.baseDamage = distanceFactor * 2.0
        return arrow
    }
}